package com.example.geofence;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LauncherActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String mUID;
    private UserApplication userApplication = (UserApplication) this.getApplication();

    ImageView launcherIcon;

    int NETWORK_STATE_ACCESS_REQUEST_CODE = 10003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        launcherIcon = (ImageView) findViewById(R.id.launcher_icon);

        // Animation
        final Animation bounce = AnimationUtils.loadAnimation(this,R.anim.bounce);
        launcherIcon.setAnimation(bounce);

        bounce.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();

                sharedPreferences = getSharedPreferences("savedlogin", Context.MODE_PRIVATE);
                int isLoginSaved = sharedPreferences.getInt("key", 0);

                mAuth = FirebaseAuth.getInstance();

//                AlertDialog.Builder builder = new AlertDialog.Builder(LauncherActivity.this, R.style.AlertDialogTheme);
//                View dialogView = LayoutInflater.from(LauncherActivity.this).inflate(
//                        R.layout.dialog_information_layout_no_checkbox,
//                        (ConstraintLayout) findViewById(R.id.dialog_information_container_no_checkbox)
//                );
//                builder.setView(dialogView);
//
//                ((TextView) dialogView.findViewById(R.id.dialog_information_title_no_checkbox)).setText("Permissions Needed");
//                ((TextView) dialogView.findViewById(R.id.dialog_information_message_no_checkbox)).setText("The following permissions are needed in order for this app to function properly.");
//                ((ImageView) dialogView.findViewById(R.id.dialog_information_icon_no_checkbox)).setImageResource(R.drawable.ic_baseline_info_24);
//                ((Button) dialogView.findViewById(R.id.dialog_information_positive_no_checkbox)).setText("Ok, got it");
//
//                builder.setCancelable(false);
//
//                AlertDialog alertDialog = builder.create();
//
//                dialogView.findViewById(R.id.dialog_information_positive_no_checkbox).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        alertDialog.dismiss();
//                    }
//                });
//
//                alertDialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
//                alertDialog.show();

                while(ContextCompat.checkSelfPermission(LauncherActivity.this, Manifest.permission.ACCESS_NETWORK_STATE)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(LauncherActivity.this,
                                                            new String[] {Manifest.permission.ACCESS_NETWORK_STATE},
                                                            NETWORK_STATE_ACCESS_REQUEST_CODE);
                }

                if(ContextCompat.checkSelfPermission(LauncherActivity.this, Manifest.permission.ACCESS_NETWORK_STATE)
                        == PackageManager.PERMISSION_GRANTED){
                    if(isLoginSaved > 0){
                        mUID = mAuth.getCurrentUser().getUid();
                        userApplication.setmUserID(mUID);
                        goToAccountDetails();
                    }
                    else{
                        goToLoginAndRegister();
                    }
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    public void onBackPressed() {

    }

    private void goToAccountDetails(){
        Intent goToAccount = new Intent(this, AccountDetailsActivity.class);
        startActivity(goToAccount);
        overridePendingTransition(0, 0);
    }

    private void goToLoginAndRegister(){
        Intent goToLoginAndRegister = new Intent(this, LoginAndRegisterActivity.class);
        startActivity(goToLoginAndRegister);
        overridePendingTransition(0, 0);
    }
}