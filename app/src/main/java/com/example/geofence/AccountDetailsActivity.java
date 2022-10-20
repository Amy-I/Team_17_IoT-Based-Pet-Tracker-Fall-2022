package com.example.geofence;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.geofence.databinding.ActivityAccountDetailsBinding;
import com.example.geofence.databinding.ActivityLauncherBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccountDetailsActivity extends DrawerBaseActivity {

    ActivityAccountDetailsBinding activityAccountDetailsBinding;

    Button bAddPet;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    UserApplication userApplication = (UserApplication) this.getApplication();
    PetApplication petApplication = (PetApplication) this.getApplication();

    private String mUID;

    List<Pet> petList;
    // Observe size of petlist

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAccountDetailsBinding = ActivityAccountDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityAccountDetailsBinding.getRoot());
        setNavActivityTitle("Pets");

        mUID = userApplication.getmUserID();
        petList = petApplication.getPetList();

        bAddPet = (Button) findViewById(R.id.addPet);

        recyclerView = (RecyclerView) findViewById(R.id.petViewHolder);
        recyclerView.setHasFixedSize(true);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users/"+ mUID + "/Pets");

        bAddPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddPetPage();
            }
        });

        // Using linear Layout layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Adapter
        mAdapter = new PetAdapter(petList, this);
        recyclerView.setAdapter(mAdapter);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                petList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    String petName = dataSnapshot.child("petName").getValue(String.class);
                    String petTID = dataSnapshot.child("petTrackerID").getValue(String.class);
                    String petCIP = dataSnapshot.child("petCameraIP").getValue(String.class);

                    Pet pet = new Pet(petName, petTID, petCIP);

                    petList.add(pet);
                    Log.i("Yo", petList.toString());

                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // If there are no pets, show this alert
        if (petList.size() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AccountDetailsActivity.this, R.style.AlertDialogTheme);
            View dialogView = LayoutInflater.from(AccountDetailsActivity.this).inflate(
                    R.layout.dialog_information_layout,
                    (ConstraintLayout) findViewById(R.id.dialog_information_container)
            );
            builder.setView(dialogView);

            ((TextView) dialogView.findViewById(R.id.dialog_information_title)).setText("Getting Started");
            ((TextView) dialogView.findViewById(R.id.dialog_information_message)).setText("Welcome to the IoT Based Pet Tracker! To get started, click the 'Add Pet' button near the bottom of the page.");
            ((ImageView) dialogView.findViewById(R.id.dialog_information_icon)).setImageResource(R.drawable.ic_baseline_info_24);
            ((Button) dialogView.findViewById(R.id.dialog_information_positive)).setText("Ok, got it");

            AlertDialog alertDialog = builder.create();

            dialogView.findViewById(R.id.dialog_information_positive).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });

            alertDialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
            alertDialog.show();
        }
        // Tell user how to navigate
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(AccountDetailsActivity.this, R.style.AlertDialogTheme);
            View dialogView = LayoutInflater.from(AccountDetailsActivity.this).inflate(
                    R.layout.dialog_information_layout,
                    (ConstraintLayout) findViewById(R.id.dialog_information_container)
            );
            builder.setView(dialogView);

            ((TextView) dialogView.findViewById(R.id.dialog_information_title)).setText("Tracking Your Pet");
            ((TextView) dialogView.findViewById(R.id.dialog_information_message)).setText("1) You can request video feed from the tracker by selecting the 'Request Feed' on this page.\n\n" +
                    "2) For Safe Area setup and monitoring, click the action bar on the top left to open the navigation menu and select 'Map'.");
            ((ImageView) dialogView.findViewById(R.id.dialog_information_icon)).setImageResource(R.drawable.ic_baseline_info_24);
            ((Button) dialogView.findViewById(R.id.dialog_information_positive)).setText("Ok, got it");

            AlertDialog alertDialog = builder.create();

            dialogView.findViewById(R.id.dialog_information_positive).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });

            alertDialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
            alertDialog.show();
        }
//
//        databaseReference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//
//                    String petName = dataSnapshot.getValue(String.class);
//                    String petTID = dataSnapshot.child("petTrackerID").getValue(String.class);
//                    String petCIP = dataSnapshot.child("petCameraIP").getValue(String.class);
//
//                    Pet pet = new Pet(petName, "10", "1010101");
//
//                    petList.add(pet);
//                    Log.i("Yo", petList.toString());
//
//                }
//                mAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//
//                    Pet pet = dataSnapshot.getValue(Pet.class);
//                    petList.add(pet);
//                    Log.i("Yo", petList.toString());
//
//                }
//                mAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        //EventChangeListener();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    // Disable back button navigation
    // If left enabled, it will go back to the Launcher Activity (don't want that)
    @Override
    public void onBackPressed() {

    }

    private void goToAddPetPage() {
        Intent goToAdd = new Intent(this, AddPetActivity.class);
        startActivity(goToAdd);
    }

//    private void EventChangeListener() {
//        databaseReference.addValueEventListener()
//    }
}