package com.example.geofence;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.geofence.databinding.ActivityLogoutBinding;
import com.google.firebase.auth.FirebaseAuth;

public class LogoutActivity extends DrawerBaseActivity {

    private FirebaseAuth mAuth;

    ActivityLogoutBinding activityLogoutBinding;
    SharedPreferences sharedPreferences;
    private UserApplication userApplication = (UserApplication) this.getApplication();

    Button bLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLogoutBinding = ActivityLogoutBinding.inflate(getLayoutInflater());
        setContentView(activityLogoutBinding.getRoot());
        setNavActivityTitle("Logout");

        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences("savedlogin", Context.MODE_PRIVATE);

        bLogOut = (Button) findViewById(R.id.logout_Logout);

        bLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });
    }

    private void logOut() {
        // Reset value so saved login is disabled
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("key", 0);
        editor.apply();

        // Clear the user ID
        userApplication.setmUserID("");

        // Logout
        mAuth.signOut();

        // Stop service
        Intent intent = new Intent(this, ForegroundService.class);
        stopService(intent);

        // Remove all listeners from Trackers in Maps Activity

        goToLauncherPage();
    }

    // Disable back button navigation
    @Override
    public void onBackPressed() {

    }

    private void goToLauncherPage(){
        Intent goToLauncher = new Intent(this, LoginAndRegisterActivity.class);
        startActivity(goToLauncher);
    }
}