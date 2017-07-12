package com.juggernaut.hotspot;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import static com.juggernaut.hotspot.OnOfHotspot.spin;

public class MainActivity extends Activity implements View.OnClickListener {

    public EditText hotspotNameText;
    public Button enableHotspot;
    public EditText passwordText;
    public TextView status;
    public Boolean aBoolean;
    public Spinner spinner;
    public TextInputLayout textInputLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enableHotspot = (Button) findViewById(R.id.enableHotspot);
        enableHotspot.setOnClickListener(this);

        hotspotNameText = (EditText) findViewById(R.id.hotspotName);
        passwordText = (EditText) findViewById(R.id.passwordId);

        status = (TextView) findViewById(R.id.status);

        aBoolean = OnOfHotspot.isApOn(this);

        if (aBoolean) {
            enableHotspot.setText("DISABLE");
            status.setText("WiFi Hotspot is ON!");
        } else {
            enableHotspot.setText("ENABLE");
            status.setText("WiFi Hotspot is OFF!");
        }

        textInputLayout = (TextInputLayout) findViewById(R.id.jugger);

        spinner = (Spinner) findViewById(R.id.spinner);
        String[] encryption = {"Open", "WPA-PSK"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, encryption);
        spinner.setAdapter(adapter);

        passwordText.setText("12345678");
        hotspotNameText.setText("Wi-Fi Hotspot");
        writePermission();

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
    protected void onStart() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ("WPA-PSK".equals(spinner.getSelectedItem().toString().toUpperCase())) {
                    spin = 1;
                    textInputLayout.setVisibility(View.VISIBLE);
                    passwordText.setVisibility(View.VISIBLE);

                } else if ("OPEN".equals(spinner.getSelectedItem().toString().toUpperCase())) {
                    spin = 0;
                    textInputLayout.setVisibility(View.INVISIBLE);
                    passwordText.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        super.onStart();
    }

    @Override
    public void onClick(View v) {


        Button toggle = (Button) v;
        String buttonText = toggle.getText().toString();
        if (buttonText.equals("DISABLE")) {

            try {
                OnOfHotspot.getApConfiguration(this);
                OnOfHotspot.configApState(this, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            toggle.setText("ENABLE");
            status.setText("WiFi Hotspot is OFF!");

        } else if (buttonText.equals("ENABLE")) {
            try {
                if (!((hotspotNameText.getText().toString()).equals(""))) {
                    String password = passwordText.getText().toString();
                    if (!(password.isEmpty() || password.length() < 8 || password.length() > 15)) {

                        OnOfHotspot.hotspotName = hotspotNameText.getText().toString();
                        OnOfHotspot.password = passwordText.getText().toString();
                        OnOfHotspot.getApConfiguration(this);
                        OnOfHotspot.configApState(this, true);

                        toggle.setText("DISABLE");
                        status.setText("WiFi Hotspot is ON!");
                    } else {
                        passwordText.setError("between 8 and 15 characters");
                    }
                } else {
                    hotspotNameText.setError("Please Specify Hotspot name!");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}