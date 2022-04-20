package com.example.geofence;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LauncherActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    // Buttons
    Button bRegister;
    Button bLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        sharedPreferences = getSharedPreferences("savedlogin", Context.MODE_PRIVATE);
        int isLoginSaved = sharedPreferences.getInt("key", 0);

        if(isLoginSaved > 0){
            goToAccountDetails();
        }

        bRegister = (Button) findViewById(R.id.Register);
        bLogin = (Button) findViewById(R.id.Login);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegisterPage();
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLoginPage();
            }
        });
    }

    private void goToLoginPage(){
        Intent goToLogin = new Intent(this, LoginActivity.class);
        startActivity(goToLogin);
    }

    private void goToRegisterPage(){
        Intent goToRegister = new Intent(this, RegisterActivity.class);
        startActivity(goToRegister);
    }

    private void goToAccountDetails(){
        Intent goToAccount = new Intent(this, AccountDetailsActivity.class);
        startActivity(goToAccount);
    }

}