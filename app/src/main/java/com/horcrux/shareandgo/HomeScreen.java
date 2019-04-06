package com.horcrux.shareandgo;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.horcrux.shareandgo.screens.ManageContent;
import com.horcrux.shareandgo.screens.ShareScreen;
import com.horcrux.shareandgo.screens.Sharepad;

public class HomeScreen extends AppCompatActivity {

    ImageView iv1, iv2;
    Button sharePad, shareScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        iv1 = findViewById(R.id.homeimage1);
        iv2 = findViewById(R.id.homeimage2);
        sharePad = findViewById(R.id.homebutton1);
        shareScreen = findViewById(R.id.homebutton2);

        shareScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeScreen.this, ShareScreen.class));
            }
        });

        sharePad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeScreen.this, ManageContent.class));
            }
        });

//        manageWorkspace.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                acceptCall(HomeScreen.this);
//
//            }
//        });

    }

//    @TargetApi(Build.VERSION_CODES.O)
//    public void acceptCall(Context context) {
//        TelecomManager tm = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
//
//        if (tm == null) {
//            // whether you want to handle this is up to you really
//            throw new NullPointerException("tm == null");
//        }
//
//        if (ActivityCompat.checkSelfPermission(HomeScreen.this, Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        tm.acceptRingingCall();
//    }




}
