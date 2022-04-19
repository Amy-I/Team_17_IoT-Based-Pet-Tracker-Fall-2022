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

    List<Pet> petList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        addPet();

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

    private void addPet(){
        Pet p1 = new Pet("Fido", "123456", "128.12.456");
        Pet p2 = new Pet("Molly", "234567", "128.12.457");
        Pet p3 = new Pet("Spot", "345678", "128.12.458");
        Pet p4 = new Pet("Princess", "456789", "128.12.459");
        Pet p5 = new Pet("Nugget", "567891", "128.12.460");

        petList.addAll(Arrays.asList(new Pet[] {p1, p2, p3, p4, p5}));
    }

    private void goToAddPetPage() {
        Intent goToAdd = new Intent(this, MapsActivity.class);
        startActivity(goToAdd);
    }
}