package com.example.geofence;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccountDetailsActivity extends AppCompatActivity {

    Button bAddPet;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    PetApplication petApplication = (PetApplication) this.getApplication();

    List<Pet> petList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        petList = petApplication.getPetList();

        bAddPet = (Button) findViewById(R.id.addPet);

        recyclerView = (RecyclerView) findViewById(R.id.petViewHolder);
        recyclerView.setHasFixedSize(true);

        bAddPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddPetPage();
            }
        });

        // Using linear Layout layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // adapter
        mAdapter = new PetAdapter(petList, this);
        recyclerView.setAdapter(mAdapter);
    }

    // Disable back button navigation
    // If left enabled, it will go back to the Launcher Activity
    @Override
    public void onBackPressed() {

    }

    private void goToAddPetPage() {
        Intent goToAdd = new Intent(this, AddPetActivity.class);
        startActivity(goToAdd);
    }
}