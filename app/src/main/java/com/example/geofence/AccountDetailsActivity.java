package com.example.geofence;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geofence.databinding.ActivityAccountDetailsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    // Shared Preference
    SharedPreferences sharedPreferences;

    // Boolean
    public boolean noPetTipShown = false;
    public boolean noTrackTipShown = false;

    // Check for network changes
    AlertDialog networkDialog;

    @SuppressLint("MissingPermission")
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

        // Shared Preference
        sharedPreferences = getSharedPreferences("dont_show", Context.MODE_PRIVATE);
        int shouldShowNoPets = sharedPreferences.getInt("no_pets", 0);
        int shouldShowOtherInstruct = sharedPreferences.getInt("no_instruct", 0);

        SharedPreferences.Editor edit = sharedPreferences.edit();

        // Network Alert
        AlertDialog.Builder builder = new AlertDialog.Builder(AccountDetailsActivity.this, R.style.AlertDialogTheme);
        View dialogView = LayoutInflater.from(AccountDetailsActivity.this).inflate(
                R.layout.dialog_information_layout_no_checkbox,
                null
        );
        builder.setView(dialogView);

        ((TextView) dialogView.findViewById(R.id.dialog_information_title_no_checkbox)).setText("No Network Found");
        ((TextView) dialogView.findViewById(R.id.dialog_information_message_no_checkbox)).setText("There was no network detected. Check your connection settings and try again.");
        ((ImageView) dialogView.findViewById(R.id.dialog_information_icon_no_checkbox)).setImageResource(R.drawable.ic_baseline_info_24);
        ((Button) dialogView.findViewById(R.id.dialog_information_positive_no_checkbox)).setText("Check Connection");

        builder.setCancelable(false);

        networkDialog = builder.create();

        dialogView.findViewById(R.id.dialog_information_positive_no_checkbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });

        networkDialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        //alertDialog.show();

        // Wait for it to succeed, then show the prompts
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

                // If there are no pets, show this alert
                if (petList.size() == 0 && shouldShowNoPets != 1 && !noPetTipShown) {
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

                    builder.setCancelable(false);

                    AlertDialog alertDialog = builder.create();

                    dialogView.findViewById(R.id.dialog_information_positive).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                            if(((CheckBox) dialogView.findViewById(R.id.dialog_information_checkbox)).isChecked()){
                                edit.putInt("no_pets", 1);
                                edit.apply();
                            }
                        }
                    });

                    alertDialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
                    alertDialog.show();
                }
                // Tell user how to navigate
                else if (petList.size() > 0){
                    noPetTipShown = true;
                    if(shouldShowOtherInstruct != 1 && !noTrackTipShown){
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

                        builder.setCancelable(false);

                        AlertDialog alertDialog = builder.create();

                        dialogView.findViewById(R.id.dialog_information_positive).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                                noTrackTipShown = true;
                                if(((CheckBox) dialogView.findViewById(R.id.dialog_information_checkbox)).isChecked()){
                                    edit.putInt("no_instruct", 1);
                                    edit.apply();
                                }
                            }
                        });

                        alertDialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
                        alertDialog.show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkReceiver);
        super.onStop();
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

    private AlertDialog getNetworkAlert(AlertDialog alertDialog){
        return alertDialog;
    }

    private final BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(!Common.isConnectedToNetworkAndInternet(context)){
                networkDialog.show();
            }
            else{
                networkDialog.dismiss();
            }
        }
    };
}