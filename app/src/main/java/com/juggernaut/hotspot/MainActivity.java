package com.juggernaut.hotspot;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;


public class MainActivity extends Activity implements View.OnClickListener {

    public static String hotspotName = null;
    Button enableHotspot;
    Button disableHotspot;
    TextView textView;
    Boolean aBoolean;
    Button state;
    EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enableHotspot = (Button) findViewById(R.id.enableWifiHotSpotId);
        enableHotspot.setOnClickListener(this);

        editText = (EditText) findViewById(R.id.edittext);

        textView = (TextView) findViewById(R.id.hotspotstate);

        state = (Button) findViewById(R.id.checkstate);
        state.setOnClickListener(this);

        disableHotspot = (Button) findViewById(R.id.disableWifiHotspotId);
        disableHotspot.setOnClickListener(this);

    }
    public void writePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 200);

            }
        }
    }

    @Override
    public void onClick(View v) {
        writePermission();
        switch (v.getId()) {
            case R.id.enableWifiHotSpotId:
                try {
                    if (!((editText.getText().toString()).equals(""))) {
                        hotspotName = editText.getText().toString();
                        OnOfHotspot.getApConfiguration(this);
                        OnOfHotspot.configApState(this, true);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Specify Hotspot name!", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.disableWifiHotspotId:
                try {
                    OnOfHotspot.getApConfiguration(this);
                    OnOfHotspot.configApState(this, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.checkstate:
                aBoolean = OnOfHotspot.isApOn(this);
                if (aBoolean) {
                    textView.setText("WiFi Hotspot already ON!");
                } else {
                    textView.setText("WiFi Hotspot is OFF!");
                }
                break;
        }
    }
}
