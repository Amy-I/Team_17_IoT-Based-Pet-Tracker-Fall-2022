package com.example.geofence;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;

// Global variable for UserID so I don't have to reference the database everytime...

public class UserApplication extends Application {
    private static String mUserID;

    private static FirebaseAuth mAuth;

    public static String getmUserID() {
        return mUserID;
    }

    public static FirebaseAuth getmAuth() {
        return mAuth;
    }

    public static void setmUserID(String mUserID) {
        UserApplication.mUserID = mUserID;
    }

    public static void setmAuth(FirebaseAuth mAuth) {
        UserApplication.mAuth = mAuth;
    }
}
