package com.example.geofence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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
    private FirebaseUser mUser;

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
        mUser = mAuth.getCurrentUser();

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
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("key", 1);
                        editor.apply();
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        // Set the logged in user id
                        userApplication.setmUserID(mAuth.getUid());
                        Log.i("Yo", "UID: " + mAuth.getUid());
                        goToAccountDetails();
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void goToAccountDetails(){
        Intent goToAccount = new Intent(this, AccountDetailsActivity.class);
        startActivity(goToAccount);
    }
}