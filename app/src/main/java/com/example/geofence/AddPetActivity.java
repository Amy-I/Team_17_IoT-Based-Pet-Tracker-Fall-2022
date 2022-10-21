package com.example.geofence;

import static android.net.InetAddresses.isNumericAddress;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    UserApplication userApplication = (UserApplication) this.getApplication();
    String mUID;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, trackerReference;
    private boolean isTrackerIDValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);

        mUID = userApplication.getmUserID();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users/"+ mUID + "/Pets");
        trackerReference = firebaseDatabase.getReference("Trackers");

        petList = petApplication.getPetList();

        PetName = (TextInputEditText) findViewById(R.id.add_PetName);
        TrackerID = (EditText) findViewById(R.id.add_TrackerID);
        CameraIP = (EditText) findViewById(R.id.add_CameraIP);

        bAdd = (Button) findViewById(R.id.add_AddButton);
        bCancel = (Button) findViewById(R.id.add_CancelButton);

        progressDialog = new ProgressDialog(this);

        bAdd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
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

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void addPet() {
        String petName = PetName.getText().toString();
        String trackerID = TrackerID.getText().toString();
        String cameraIP = CameraIP.getText().toString();

        // Add check for tracker id in tracker
        readData(trackerReference, new OnGetDataListener() {
            @Override
            public void onSuccess(String dataSnapshotValue) {
                Log.i("Yo", ""+dataSnapshotValue.contains(trackerID+"={"));
                isTrackerIDValid = dataSnapshotValue.contains(trackerID+"={");

                // Validation Checks
                if(petName.isEmpty()){
                    PetName.requestFocus();
                    PetName.setError("Please enter the name of your pet");
                }
                else if(!isTrackerIDValid){
                    TrackerID.requestFocus();
                    TrackerID.setError("Tracker ID not found in database");
                }
                // Add more checks
                else if(!isNumericAddress(cameraIP)){
                    CameraIP.requestFocus();
                    CameraIP.setError("Enter valid IP address\nExample: 255.255.255.255");
                }
                else{
                    Pet pet = new Pet(petName, trackerID, cameraIP);

                    progressDialog.setTitle("Adding Pet");
                    progressDialog.setMessage("Pet is being added...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    databaseReference.push().setValue(pet).addOnSuccessListener(AddPetActivity.this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(AddPetActivity.this, "Pet added", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            goToAccountDetails();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddPetActivity.this, "Error adding pet", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    // Added to wait for async task to finish
    public void readData(DatabaseReference ref, final OnGetDataListener listener) {

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void goToAccountDetails(){
        Intent goToAccount = new Intent(this, AccountDetailsActivity.class);
        startActivity(goToAccount);
    }

}