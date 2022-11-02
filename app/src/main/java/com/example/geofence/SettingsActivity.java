package com.example.geofence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geofence.databinding.ActivitySettingsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends DrawerBaseActivity {

    ActivitySettingsBinding activitySettingsBinding;

    // Reset Option
    LinearLayout reset;

    // Shared Preference
    SharedPreferences sharedPreferences;

    // Sending SMS
    private static final int SEND_SMS_ACCESS_REQUEST_CODE = 10002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySettingsBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(activitySettingsBinding.getRoot());
        setNavActivityTitle("Settings");

        sharedPreferences = getSharedPreferences("dont_show", Context.MODE_PRIVATE);

        reset = findViewById(R.id.reset_option);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert();
            }
        });

    }

    public void showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this, R.style.AlertDialogTheme);
        View dialogView = LayoutInflater.from(SettingsActivity.this).inflate(
                R.layout.dialog_layout,
                (ConstraintLayout) findViewById(R.id.dialog_container)
        );
        builder.setView(dialogView);

        ((TextView) dialogView.findViewById(R.id.dialog_title)).setText("Reset All Dialogs?");
        ((TextView) dialogView.findViewById(R.id.dialog_message)).setText("This action will reset the view option for all optional tip and information dialogs. Proceed?");
        ((ImageView) dialogView.findViewById(R.id.dialog_icon)).setImageResource(R.drawable.ic_baseline_info_24);

        builder.setCancelable(false);

        AlertDialog alertDialog = builder.create();

        dialogView.findViewById(R.id.dialog_positive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putInt("no_map", 0);
                edit.putInt("no_pets", 0);
                edit.putInt("no_instruct", 0);
                edit.apply();
                Toast.makeText(SettingsActivity.this, "Dialogs reset", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

        dialogView.findViewById(R.id.dialog_negative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        alertDialog.show();
    }

    public void setSMSPermissions(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.SEND_SMS},
                        SEND_SMS_ACCESS_REQUEST_CODE);

            }
        }
    }

    // Disable back button navigation
    @Override
    public void onBackPressed() {

    }
}