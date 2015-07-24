package com.example.root.myapplication;

/**
 * Created by root on 7/4/15.
 */

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;


/**
 * Created by Antonio on 1/31/2015.
 */
public class WifiService extends IntentService {

    private final static long WAIT_TIME = 10*1000;

    public WifiService() {
        super("WifiService");
        Log.d("WIFISERVICE: ", "WifiService Constructed");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> configs = wifi.getConfiguredNetworks();
        List<ScanResult> scans;
        WifiConfiguration connect;
        WifiInfo current = wifi.getConnectionInfo();
        Log.d("WIFISERVICE: ", "Started WiFiService");
        Log.d("WIFISERVICE: ", "current: " + current.getSSID());
        boolean connected;

        while(true) {
            synchronized (this) {
                try {
                    if(wifi.startScan())
                        Log.d("WIFISERVICE: ", "Scanned");
                    else
                        Log.d("WIFISERVICE: ", "Not Scanned");
                    scans = wifi.getScanResults();
                    for (int i = 0; i < scans.size(); i++) {
                        for (int j = 0; j < configs.size(); j++) {
                            //Log.d("WIFISERVICE: ", "config: " + configs.get(j).SSID);
                            //Log.d("WIFISERVICE: ", "scan: " + scans.get(i).SSID);

                            //if ssid equals scan results and level is greater than current level
                            //TODO:change SSID to BSSID
                            if (configs.get(j).SSID.equals("\"" + scans.get(i).SSID + "\"") && current.getRssi() + 3 < scans.get(i).level && !current.getSSID().equals(scans.get(i).SSID)) {
                                Log.d("WIFISERVICE: ", "Current: " + current.getSSID());
                                Log.d("WIFISERVICE: ", "Scan: " + scans.get(i).SSID);
                                Log.d("WIFISERVICE: ", "Got match. Connecting to " + scans.get(i).SSID);

                                if(wifi.disconnect())
                                    Log.d("WIFISERVICE: ", "Disconnection Succesful");
                                else
                                    Log.d("WIFI:SERIVICE: ", "Disconnection Failed");
                                if(wifi.enableNetwork(configs.get(j).networkId, true))
                                    Log.d("WIFISERVICE: ", "Network enabled");
                                else
                                    Log.d("WIFISERVICE: ", "Network could not be enabled");
                                if(wifi.reconnect())
                                    Log.d("WIFISERVICE: ", "Connection Successful");
                                else
                                    Log.d("WIFISERVICE: ", "Connection failed");
                                connected = true;
                                while(connected) {
                                    if(wifi.getWifiState() == WifiManager.WIFI_STATE_DISABLED || wifi.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
                                        Log.d("WIFISERVICE: ", "Waiting for Wifi to be enabled");
                                        wait(2000);
                                    }
                                    else if(wifi.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
                                        current = wifi.getConnectionInfo();
                                        Log.d("WIFISERVICE: ", "Wifi enabled. changing current wifi info");
                                        connected = false;
                                    }
                                    else
                                        Log.d("WIFISERVICE: ", "getWifiState did not work");
                                }
                            }
                            /*else {
                                for (int x = 0; x < scans.size(); x++) {
                                    Log.d("WIFISERVICE_SCANS: ", scans.get(x).SSID + " Strength: " + scans.get(x).level + "\n");
                                }
                            }*/
                        }
                    }
                    Log.d("WIFISERVICE: ", "Waiting");
                    wait(WAIT_TIME);
                } catch (InterruptedException e) {
                    Log.d("WIFISERVICE:", "InterruptedException " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
}