package com.example.geofence;

import android.app.Application;

// Global variable for UserID so I don't have to reference the database everytime...

public class UserApplication extends Application {
    private static String mUserID;

    public static String getmUserID() {
        return mUserID;
    }

    public static void setmUserID(String mUserID) {
        UserApplication.mUserID = mUserID;
    }
}
