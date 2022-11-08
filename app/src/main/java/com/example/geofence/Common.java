package com.example.geofence;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

public class Common {
    @SuppressLint("MissingPermission")
    public static boolean isConnectedToNetworkAndInternet(Context context){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
//        boolean hasAccessToInternet = false;
//        try {
//            String command = "ping -c 1 google.com";
//            hasAccessToInternet = (Runtime.getRuntime().exec(command).waitFor() == 300);
//        } catch (Exception e) {
//            hasAccessToInternet = false;
//        }
        //return activeNetworkInfo != null && activeNetworkInfo.isConnected(); //&& hasAccessToInternet;

        Network[] networks = connectivityManager.getAllNetworks();
        NetworkInfo networkInfo;
        Network network;
        for (int i = 0; i < networks.length; i++){
            network = networks[i];
            networkInfo = connectivityManager.getNetworkInfo(network);
            if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                return true;
            }
        }
        return false;
    }
}
