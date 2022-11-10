package com.example.geofence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    EditText Email, Password;
    Button bLogin;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    // To save to the right database for the user
    UserApplication userApplication = (UserApplication) this.getApplication();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences("savedlogin", Context.MODE_PRIVATE);

        Email = (EditText) findViewById(R.id.enterEmailAddress_LoginPage);
        Password = (EditText) findViewById(R.id.enterPassword_LoginPage);

        bLogin = (Button) findViewById(R.id.Login_LoginPage);

        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });
    }

    private void Login(){
        String email = Email.getText().toString();
        String password = Password.getText().toString();

        // Make sure it is a valid email
        if(!email.matches(emailPattern)){
            Email.requestFocus();
            Email.setError("Invalid Email");
        }
        // Make sure password is entered and has proper length
        else if(password.isEmpty() || password.length() < 6){
            Password.requestFocus();
            Password.setError("Enter Password at least 6 characters long");
        }
        else{
            progressDialog.setTitle("Logging in");
            progressDialog.setMessage("User is being logged in...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    // When the task is complete
                    if(task.isSuccessful()){
                        progressDialog.dismiss();

                        if(mAuth.getCurrentUser().isEmailVerified()){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("key", 1);
                            editor.apply();
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            // Set the logged in user id
                            userApplication.setmUserID(mAuth.getUid());
                            Log.i("Yo", "UID: " + mAuth.getUid());
                            goToAccountDetails();
                        }
                        else if(!mAuth.getCurrentUser().isEmailVerified()){
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.AlertDialogTheme);
                            View dialogView = LayoutInflater.from(LoginActivity.this).inflate(
                                    R.layout.dialog_information_layout_no_checkbox,
                                    (ConstraintLayout) findViewById(R.id.dialog_information_container_no_checkbox)
                            );
                            builder.setView(dialogView);

                            ((TextView) dialogView.findViewById(R.id.dialog_information_title_no_checkbox)).setText("Email Not Verified");
                            ((TextView) dialogView.findViewById(R.id.dialog_information_message_no_checkbox)).setText("Your email has not been verified. Please verify your account before attempting to log in.");
                            ((ImageView) dialogView.findViewById(R.id.dialog_information_icon_no_checkbox)).setImageResource(R.drawable.ic_baseline_info_24);
                            ((Button) dialogView.findViewById(R.id.dialog_information_positive_no_checkbox)).setText("Verify Email");

                            builder.setCancelable(false);

                            AlertDialog alertDialog = builder.create();

                            dialogView.findViewById(R.id.dialog_information_positive_no_checkbox).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alertDialog.dismiss();
                                    mAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(LoginActivity.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(LoginActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });

                            alertDialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
                            alertDialog.show();
                        }

                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        goToLauncherPage();
    }

    private void goToAccountDetails(){
        Intent goToAccount = new Intent(this, AccountDetailsActivity.class);
        startActivity(goToAccount);
        overridePendingTransition(0, 0);
    }

    private void goToLauncherPage(){
        Intent goToLauncher = new Intent(this, LoginAndRegisterActivity.class);
        startActivity(goToLauncher);
        overridePendingTransition(0, 0);
    }
}