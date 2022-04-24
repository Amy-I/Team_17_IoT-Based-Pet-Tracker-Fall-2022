package com.example.geofence;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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