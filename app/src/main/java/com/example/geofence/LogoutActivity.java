package com.example.geofence;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.geofence.databinding.ActivityLogoutBinding;

public class LogoutActivity extends DrawerBaseActivity {

    ActivityLogoutBinding activityLogoutBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLogoutBinding = ActivityLogoutBinding.inflate(getLayoutInflater());
        setContentView(activityLogoutBinding.getRoot());
        setNavActivityTitle("Logout");
    }

    // Disable back button navigation
    @Override
    public void onBackPressed() {

    }
}