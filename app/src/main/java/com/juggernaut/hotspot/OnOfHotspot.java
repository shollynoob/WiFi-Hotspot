package com.juggernaut.hotspot;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.juggernaut.hotspot.MainActivity.hotspotName;

/**
 * Created by Kailash on 7/8/2017.
 */

public class OnOfHotspot {

    public static WifiConfiguration config;


    //check whether wifi hotspot on or off

    public static boolean isApOn(Context context) {
        WifiManager wifimanager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        try {
            Method method = wifimanager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifimanager);
        } catch (Throwable ignored) {
        }
        return false;
    }

    // toggle wifi hotspot on or off
    public static boolean configApState(Context context, boolean apState) {

        WifiManager wifimanager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifimanager.setWifiEnabled(false);
        try {
            if (apState) {
                WifiConfiguration netConfig = new WifiConfiguration();

                netConfig.SSID = MainActivity.hotspotName;
                netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);

                Method method = WifiManager.class.getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
                method.invoke(wifimanager, netConfig, apState);
                Toast.makeText(context, "WiFi Hotspot is Created!", Toast.LENGTH_SHORT).show();
                return true;
            } else {

                Method method = WifiManager.class.getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
                method.invoke(wifimanager, config, false);
                Toast.makeText(context, "WiFi Hotspot is Disabled!", Toast.LENGTH_SHORT).show();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static WifiConfiguration getApConfiguration(Context context) {

        WifiConfiguration config = null;
        WifiManager wifimanager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Method[] methods = WifiManager.class.getDeclaredMethods();
        for (Method m : methods) {
            if (m.getName().equals("getWifiApConfiguration")) {
                try {
                    config = (WifiConfiguration) m.invoke(wifimanager);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    Log.d("Testing", e.getCause().toString());
                }
            }
        }
        OnOfHotspot.config = config;
        return config;
    }

}