package com.example.geofence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerificationActivity extends AppCompatActivity {

    UserApplication userApplication = (UserApplication) this.getApplication();
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        mAuth = userApplication.getmAuth();

        checkForVerification(mAuth.getCurrentUser());

    }

    // No back button navigation
    @Override
    public void onBackPressed() {

    }

    private void goToLogin(){
        Intent goToLogin = new Intent(this, LoginActivity.class);
        startActivity(goToLogin);
    }

    private void checkForVerification(FirebaseUser user){
        if(!user.isEmailVerified()){
            // Send email verification
            AlertDialog.Builder builder = new AlertDialog.Builder(VerificationActivity.this, R.style.AlertDialogTheme);
            View dialogView = LayoutInflater.from(VerificationActivity.this).inflate(
                    R.layout.dialog_information_layout_no_checkbox,
                    (ConstraintLayout) findViewById(R.id.dialog_information_container_no_checkbox)
            );
            builder.setView(dialogView);

            ((TextView) dialogView.findViewById(R.id.dialog_information_title_no_checkbox)).setText("Verification Needed");
            ((TextView) dialogView.findViewById(R.id.dialog_information_message_no_checkbox)).setText("A verification email will be sent to " + user.getEmail() + ". Please verify your account before attempting to log in.");
            ((ImageView) dialogView.findViewById(R.id.dialog_information_icon_no_checkbox)).setImageResource(R.drawable.ic_baseline_info_24);
            ((Button) dialogView.findViewById(R.id.dialog_information_positive_no_checkbox)).setText("Verify Email");

            builder.setCancelable(false);

            AlertDialog alertDialog = builder.create();

            dialogView.findViewById(R.id.dialog_information_positive_no_checkbox).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();

                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(VerificationActivity.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();
                            goToLogin();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(VerificationActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                            goToLogin();
                        }
                    });
                }
            });

            alertDialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
            alertDialog.show();

        }
    }
}