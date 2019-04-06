package com.horcrux.shareandgo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.horcrux.shareandgo.screens.LoginActivity;
import com.horcrux.shareandgo.screens.ManageContent;
import com.horcrux.shareandgo.screens.Sharepad;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView;

    private static final String permissions[] = {"android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.ACCESS_NETWORK_STATE",
            "android.permission.INTERNET",
            "android.permission.CAMERA",
            "android.permission.READ_PHONE_STATE",
            "android.permission.READ_SMS",
            "android.permission.RECEIVE_SMS",
            "android.permission.ACCESS_WIFI_STATE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.RECORD_AUDIO",
            "android.permission.READ_PHONE_NUMBERS"};

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_CODE = 1;
    private boolean permission;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        imageView = findViewById(R.id.image);
        textView = findViewById(R.id.text);

    }

    @Override
    protected void onStart() {
        super.onStart();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(permissions, MY_PERMISSIONS_REQUEST_ACCESS_CODE);
                } else {

                    FirebaseUser currentUser = mAuth.getCurrentUser();

                    if (currentUser != null) {
                        startActivity(new Intent(MainActivity.this, HomeScreen.class));
                        finish();
                    } else {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                }

            }
        }, 2000);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case 2:
                permission = grantResults[0] == PackageManager.PERMISSION_GRANTED;

        }

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            startActivity(new Intent(MainActivity.this, HomeScreen.class));
            finish();
        } else {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }
}
