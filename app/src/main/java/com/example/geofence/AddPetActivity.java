package com.example.geofence;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AddPetActivity extends AppCompatActivity {

    //FirebaseDatabase firebaseDatabase;

    PetApplication petApplication = (PetApplication) this.getApplication();
    List<Pet> petList;

    TextInputEditText PetName;
    EditText TrackerID;
    EditText CameraIP;

    Button bAdd;
    Button bCancel;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);

        petList = petApplication.getPetList();

        PetName = (TextInputEditText) findViewById(R.id.add_PetName);
        TrackerID = (EditText) findViewById(R.id.add_TrackerID);
        CameraIP = (EditText) findViewById(R.id.add_CameraIP);

        bAdd = (Button) findViewById(R.id.add_AddButton);
        bCancel = (Button) findViewById(R.id.add_CancelButton);

        progressDialog = new ProgressDialog(this);

        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPet();
            }
        });

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAccountDetails();
            }
        });
    }

    private void addPet() {
        String petName = PetName.getText().toString();
        String trackerID = TrackerID.getText().toString();
        String cameraIP = CameraIP.getText().toString();

        Pet pet = new Pet(petName, trackerID, cameraIP);

        petList.add(pet);

        goToAccountDetails();
    }

    private void goToAccountDetails(){
        Intent goToAccount = new Intent(this, AccountDetailsActivity.class);
        startActivity(goToAccount);
    }

}