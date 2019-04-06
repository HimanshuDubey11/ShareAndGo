package com.horcrux.shareandgo.screens;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.horcrux.shareandgo.R;
import com.horcrux.shareandgo.models.Doc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.Objects;

public class Sharepad extends AppCompatActivity {

    private static final int RC_PICK = 2;
    DatabaseReference padReference;

    EditText sharePadEditText;
    Button saveButton, scanText;
    EditText title;

    ProgressDialog dialog;

    public static String titleSave = "";
    private String resultText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharepad);

        title = findViewById(R.id.title);

        title.setText(titleSave);

        dialog = new ProgressDialog(Sharepad.this);
        dialog.setMessage("Loading..");
        padReference = FirebaseDatabase.getInstance().getReference("" + FirebaseAuth.getInstance().getUid()).child("Docs").child(titleSave);

        dialog.show();

        sharePadEditText = findViewById(R.id.sharepadedittext);

        scanText = findViewById(R.id.addfromimage);

        saveButton = findViewById(R.id.savebutton);

        padReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Doc doc = dataSnapshot.getValue(Doc.class);
                String docu = "";
                if (doc != null) {
                    docu = doc.getData();
                }
                if(!docu.isEmpty()) {
                    sharePadEditText.setText(docu);
                }

                dialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
            }
        });


        scanText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                photoIntent.setType("image/*");
                startActivityForResult(photoIntent,RC_PICK);

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                padReference.removeValue();

                titleSave = title.getText().toString();

                padReference = FirebaseDatabase.getInstance().getReference("" + FirebaseAuth.getInstance().getUid()).child("Docs").child(titleSave);


                dialog.show();
                Doc doc = new Doc(titleSave,sharePadEditText.getText().toString());
                padReference.setValue(doc);

                dialog.dismiss();
//            writeToFile(sharePadEditText.getText().toString(), Sharepad.this);


            }
        });

        sharePadEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                Doc doc = new Doc(titleSave,sharePadEditText.getText().toString());
                padReference.setValue(doc);

//                writeToFile(sharePadEditText.getText().toString(), Sharepad.this);

            }
        });

    }

//    private void writeToFile(String data, Context context) {
//        try {
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("HackathonData.txt", Context.MODE_PRIVATE));
//            outputStreamWriter.write(data);
//            outputStreamWriter.close();
//        }
//        catch (IOException e) {
//        }
//    }

    @Override
    protected void onStart() {
        super.onStart();

    }

//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        sharePadEditText.setText(readFromFile(Sharepad.this));
//
//
//    }
//
//    private String readFromFile(Context context) {
//
//        String ret = "";
//
//        try {
//            InputStream inputStream = context.openFileInput("HackathonData.txt");
//
//            if ( inputStream != null ) {
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                String receiveString = "";
//                StringBuilder stringBuilder = new StringBuilder();
//
//                while ( (receiveString = bufferedReader.readLine()) != null ) {
//                    stringBuilder.append(receiveString);
//                }
//
//                inputStream.close();
//                ret = stringBuilder.toString();
//            }
//        }
//        catch (FileNotFoundException e) {
//
//            return "";
//
//        } catch (IOException e) {
//
//            return "";
//
//        }
//
//        return ret;
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_PICK && resultCode == Activity.RESULT_OK && null!= data) {


            InputStream imageStream = null;
            try {
                Uri imageUri = data.getData();
                imageStream = getContentResolver().openInputStream(imageUri);

                File file = new File(getRealPathFromURI(imageUri));

                if (file.exists()) {

                    Drawable d = Drawable.createFromPath(file.getAbsolutePath());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                        relativeLayout.setBackground(d);
                    }
                }

                recognizeText(imageUri);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    private void recognizeText(Uri uri) {

        //through URi
        FirebaseVisionImage image = null;
        try {
            image = FirebaseVisionImage.fromFilePath(Sharepad.this, uri);
        } catch (IOException e) {
            e.printStackTrace();
        }


        FirebaseApp.initializeApp(this);
        FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();

        textRecognizer.processImage(image)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText result) {
                        // Task completed successfully
                        // ...
                        resultText= result.getText();

                        sharePadEditText.setText(sharePadEditText.getText().toString() + resultText);
//
//                        for (FirebaseVisionText.TextBlock block: result.getTextBlocks()) {
//                            String blockText = block.getText();
//                            Float blockConfidence = block.getConfidence();
//                            List<RecognizedLanguage> blockLanguages = block.getRecognizedLanguages();
//                            Point[] blockCornerPoints = block.getCornerPoints();
//                            Rect blockFrame = block.getBoundingBox();
//                            for (FirebaseVisionText.Line line: block.getLines()) {
//                                String lineText = line.getText();
//                                Float lineConfidence = line.getConfidence();
//                                List<RecognizedLanguage> lineLanguages = line.getRecognizedLanguages();
//                                Point[] lineCornerPoints = line.getCornerPoints();
//                                Rect lineFrame = line.getBoundingBox();
//                                for (FirebaseVisionText.Element element: line.getElements()) {
//                                    String elementText = element.getText();
//                                    Float elementConfidence = element.getConfidence();
//                                    List<RecognizedLanguage> elementLanguages = element.getRecognizedLanguages();
//                                    Point[] elementCornerPoints = element.getCornerPoints();
//                                    Rect elementFrame = element.getBoundingBox();
//                                }
//                            }
//                        }

                    }
                })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
                                // ...

                            }
                        });
    }

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private int getRotationCompensation(String cameraId, Activity activity, Context context)
            throws CameraAccessException {
        // Get the device's current rotation relative to its "native" orientation.
        // Then, from the ORIENTATIONS table, look up the angle the image must be
        // rotated to compensate for the device's rotation.
        int deviceRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int rotationCompensation = ORIENTATIONS.get(deviceRotation);

        // On most devices, the sensor orientation is 90 degrees, but for some
        // devices it is 270 degrees. For devices with a sensor orientation of
        // 270, rotate the image an additional 180 ((270 + 270) % 360) degrees.
        CameraManager cameraManager = (CameraManager) context.getSystemService(CAMERA_SERVICE);
        int sensorOrientation = cameraManager
                .getCameraCharacteristics(cameraId)
                .get(CameraCharacteristics.SENSOR_ORIENTATION);
        rotationCompensation = (rotationCompensation + sensorOrientation + 270) % 360;

        // Return the corresponding FirebaseVisionImageMetadata rotation value.
        int result;
        switch (rotationCompensation) {
            case 0:
                result = FirebaseVisionImageMetadata.ROTATION_0;
                break;
            case 90:
                result = FirebaseVisionImageMetadata.ROTATION_90;
                break;
            case 180:
                result = FirebaseVisionImageMetadata.ROTATION_180;
                break;
            case 270:
                result = FirebaseVisionImageMetadata.ROTATION_270;
                break;
            default:
                result = FirebaseVisionImageMetadata.ROTATION_0;
                Log.e("ErrorRotation", "Bad rotation value: " + rotationCompensation);
        }
        return result;
    }

}
