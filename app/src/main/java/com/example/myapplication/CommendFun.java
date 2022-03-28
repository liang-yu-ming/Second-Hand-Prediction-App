package com.example.myapplication;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

public class CommendFun {
    @SuppressLint("DefaultLocale")
    @TargetApi(Build.VERSION_CODES.M)// if not this .getSystemService requires api 23 but current 21
    public static String getLocalIP(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(context.WIFI_SERVICE);
        assert wifiManager != null;
        WifiInfo info = wifiManager.getConnectionInfo();
        int ipAddress = info.getIpAddress();
        return String.format("%d.%d.%d.%d"
                , ipAddress & 0xff
                , ipAddress >> 8 & 0xff
                , ipAddress >> 16 & 0xff
                , ipAddress >> 24 & 0xff);
    }
}
