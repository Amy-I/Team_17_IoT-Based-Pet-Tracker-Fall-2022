package com.example.geofence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

public class RegisterActivity extends AppCompatActivity {

    EditText Email, Password, ConfirmPassword;
    Button bRegister;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String validPassword = "[A-Za-z0-9!@#$%&+=-]+";

    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Email = (EditText) findViewById(R.id.enterEmailAddress_RegisterPage);
        Password = (EditText) findViewById(R.id.enterPassword_RegisterPage);
        ConfirmPassword = (EditText) findViewById(R.id.ConfirmPassword);

        bRegister = (Button) findViewById(R.id.Register_RegisterPage);

        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });
    }

    private void Register(){
        String email = Email.getText().toString();
        String password = Password.getText().toString();
        String confirmPassword = ConfirmPassword.getText().toString();

        // Make sure it is a valid email
        if(!email.matches(emailPattern)){
            Email.requestFocus();
            Email.setError("Invalid Email");
        }
        // Make sure password is entered and has proper length
        else if(password.isEmpty() || password.length() < 8){
            Password.requestFocus();
            Password.setError("Enter password at least 8 characters long");
        }
        // Check for valid characters
        else if(!password.matches(validPassword)){
            Password.requestFocus();
            Password.setError("Invalid special character(s)\nAccepted characters: !@#$%&+=");
        }
        // And make sure password is secure
        else if(!checkForSecurePass(password)){
            Password.requestFocus();
            Password.setError("Password must contain at least one uppercase character, one number, and one special character (!@#$%^&+=)");
        }
        // If the passwords don't match...
        else if(!password.equals(confirmPassword)){
            ConfirmPassword.requestFocus();
            ConfirmPassword.setError("Passwords don't match");
        }
        else{
            progressDialog.setTitle("Registration in Progress");
            progressDialog.setMessage("Please wait while we register your account...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    // When the task is complete
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        goToLogin();
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //checkForVerification(mAuth.getCurrentUser());
        }
    }

    @Override
    public void onBackPressed() {
        goToLauncherPage();
    }

    private void checkForVerification(FirebaseUser user){
        if(!user.isEmailVerified()){
            // Send email verification
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this, R.style.AlertDialogTheme);
            View dialogView = LayoutInflater.from(RegisterActivity.this).inflate(
                    R.layout.dialog_information_layout_no_checkbox,
                    (ConstraintLayout) findViewById(R.id.dialog_information_container_no_checkbox)
            );
            builder.setView(dialogView);

            ((TextView) dialogView.findViewById(R.id.dialog_information_title_no_checkbox)).setText("Verification Needed");
            ((TextView) dialogView.findViewById(R.id.dialog_information_message_no_checkbox)).setText("A verification email will sent to " + user.getEmail() + ". Please verify your account before attempting to log in.");
            ((ImageView) dialogView.findViewById(R.id.dialog_information_icon_no_checkbox)).setImageResource(R.drawable.ic_baseline_info_24);
            ((Button) dialogView.findViewById(R.id.dialog_information_positive_no_checkbox)).setText("Send Email");

            builder.setCancelable(false);

            AlertDialog alertDialog = builder.create();

            dialogView.findViewById(R.id.dialog_information_positive_no_checkbox).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(RegisterActivity.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();
                            goToLogin();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                            goToLauncherPage();
                        }
                    });
                }
            });

            alertDialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
            alertDialog.show();

        }
    }

    private void goToLogin(){
        Intent goToLogin = new Intent(this, LoginActivity.class);
        startActivity(goToLogin);
    }

    private void goToLauncherPage(){
        Intent goToLauncher = new Intent(this, LoginAndRegisterActivity.class);
        startActivity(goToLauncher);
    }

    private boolean checkForSecurePass(String str){
        int digits = 0, specialChars = 0;

        for (int i = 0; i < str.length(); i++) {
            char ch = str.toCharArray()[i];
            if (Character.isDigit(ch)) {
                digits++;
            }
            else if (isSpecialChar(ch)) {
                specialChars++;
            }
            else if (Character.isWhitespace(ch)) {
                return false;
            }
        }
        return !(digits < 1 || specialChars < 1);
    }

    private static boolean isSpecialChar(char c) {
        switch (c) {
            case '!':
            case '@':
            case '#':
            case '$':
            case '%':
            case '^':
            case '&':
            case '+':
            case '=':
                return true;
            default:
                return false;
        }
    }

}