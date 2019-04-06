package com.horcrux.shareandgo.screens;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.horcrux.shareandgo.R;

import java.util.HashMap;

import io.cobrowse.core.CobrowseIO;

public class ShareScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_screen);

        CobrowseIO.instance().license("CWfG1dT5lYZw0A");

        Log.i("App", "Cobrowse device id: " + CobrowseIO.instance().deviceId(getApplication()));

        HashMap<String, Object> customData = new HashMap<>();
        customData.put("user_id", "982621");
        customData.put("user_name", "Himanshu Dubey");
        customData.put("user_email", "hmnshdb10@gmail.com");
        customData.put("device_id", "88715");
        customData.put("device_name", "ShareAndGo");
        CobrowseIO.instance().customData(customData);

        CobrowseIO.instance().start(this);

    }
}
