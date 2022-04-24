package com.example.geofence;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.geofence.databinding.ActivitySettingsBinding;

public class SettingsActivity extends DrawerBaseActivity {

    ActivitySettingsBinding activitySettingsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySettingsBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(activitySettingsBinding.getRoot());
        setNavActivityTitle("Settings");
    }

    // Disable back button navigation
    @Override
    public void onBackPressed() {

    }
}