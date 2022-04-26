package com.example.geofence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

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

            mAuth.createUserWithEmailAndPassword(email, password.toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    // When the task is complete
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        goToLogin();
                        Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        goToLauncherPage();
    }

    private void goToLogin(){
        Intent goToLogin = new Intent(this, LoginActivity.class);
        startActivity(goToLogin);
    }

    private void goToLauncherPage(){
        Intent goToLauncher = new Intent(this, LauncherActivity.class);
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