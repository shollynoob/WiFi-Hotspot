package com.juggernaut.hotspot;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements View.OnClickListener {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.enableWifiHotSpotId:
                try {
                    if (!((editText.getText().toString()).equals(""))) {
                        OnOfHotspot.hotspotName = editText.getText().toString();
                        OnOfHotspot.getApConfiguration(this);
                        OnOfHotspot.configApState(this, true);
                    }else {
                        Toast.makeText(getApplicationContext(),"Please Specify Hotspot name!",Toast.LENGTH_LONG).show();
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