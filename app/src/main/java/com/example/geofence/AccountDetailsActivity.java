package com.example.geofence;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.geofence.databinding.ActivityAccountDetailsBinding;
import com.example.geofence.databinding.ActivityLauncherBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccountDetailsActivity extends DrawerBaseActivity {

    ActivityAccountDetailsBinding activityAccountDetailsBinding;

    Button bAddPet;
    Button bMaps;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    PetApplication petApplication = (PetApplication) this.getApplication();

    List<Pet> petList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAccountDetailsBinding = ActivityAccountDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityAccountDetailsBinding.getRoot());
        setNavActivityTitle("Pets");

        petList = petApplication.getPetList();

        bAddPet = (Button) findViewById(R.id.addPet);
        bMaps = (Button) findViewById(R.id.goToMap);

        recyclerView = (RecyclerView) findViewById(R.id.petViewHolder);
        recyclerView.setHasFixedSize(true);

        bAddPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddPetPage();
            }
        });

        bMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMaps();
            }
        });

        // Using linear Layout layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Adapter
        mAdapter = new PetAdapter(petList, this);
        recyclerView.setAdapter(mAdapter);
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

    private void goToMaps() {
        Intent goToMap = new Intent(this, MapsActivity.class);
        startActivity(goToMap);
    }
}