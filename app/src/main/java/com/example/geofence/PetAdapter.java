package com.example.geofence;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {

    List<Pet> petList;
    Context context;
    String IP;

    UserApplication userApplication;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser = mAuth.getCurrentUser();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    public PetAdapter(List<Pet> petList, Context context) {
        this.petList = petList;
        this.context = context;
    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_layout_view, parent, false);
        PetViewHolder holder = new PetViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        /* Add image from database */
        holder.petPic.setImageResource(R.drawable.ic_baseline_pets_24);
        holder.petName.setText(petList.get(position).getPetName());
        IP = petList.get(position).getPetCameraIP();
        holder.petIP.setText(IP);
//        databaseReference.child("Trackers").child(petList.get(position).getPetTrackerID().toString()).child("isActive").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.getValue(Boolean.class)){
//                    holder.petStatus.setText("Active");
//                }
//                else{
//                    holder.petStatus.setText("Inactive");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public class PetViewHolder extends RecyclerView.ViewHolder{
        ImageView petPic;
        TextView petName;
        TextView petIP;
        // Button bRequestFeed;

        public PetViewHolder(@NonNull View itemView) {
            super(itemView);

            petPic = itemView.findViewById(R.id.imageView);
            petName = itemView.findViewById(R.id.pName_Account);
            petIP = itemView.findViewById(R.id.pIPAddress);
            itemView.findViewById(R.id.pCamera).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(context.getApplicationContext(), "Go to activity to make request", Toast.LENGTH_SHORT).show();
                    Log.i("Yo", "Pet IP: " + petIP.getText());
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("http://"+ petIP.getText()));
                    context.startActivity(intent);
                }
            });
            itemView.findViewById(R.id.adapter_CloseButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Toast.makeText(context.getApplicationContext(), "Delete from database", Toast.LENGTH_SHORT).show();
                    //Log.i("Yo", "PetRef: " + databaseReference.child("Users").child(mUser.getUid()).child("Pets").orderByChild("petName").equalTo(petName.getText().toString()).getRef().getRef());
                    //Log.i("Yo", "PetRef: " + petName.getText().toString());

                    // Alert Dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(PetAdapter.this.context, R.style.AlertDialogTheme);
                    View dialogView = LayoutInflater.from(PetAdapter.this.context).inflate(
                            R.layout.dialog_layout,
                            (ConstraintLayout)view.findViewById(R.id.dialog_container)
                    );
                    builder.setView(dialogView);

                    ((TextView) dialogView.findViewById(R.id.dialog_title)).setText("Delete " + petName.getText() + "?");
                    ((TextView) dialogView.findViewById(R.id.dialog_message)).setText("Are you sure you want to delete your pet, " + petName.getText() + "? You will not be able to undo this action.");
                    ((ImageView) dialogView.findViewById(R.id.dialog_icon)).setImageResource(R.drawable.ic_baseline_info_24);

                    builder.setCancelable(false);

                    AlertDialog alertDialog = builder.create();

                    dialogView.findViewById(R.id.dialog_positive).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            databaseReference.child("Users").child(mUser.getUid()).child("Pets").orderByChild("petName").equalTo(petName.getText().toString()).getRef()
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                                Log.i("Yo", dataSnapshot.child("petName").getValue().toString());
                                                if(dataSnapshot.child("petName").getValue().toString().equals(petName.getText().toString())) {
                                                    dataSnapshot.getRef().removeValue();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                            alertDialog.dismiss();
                        }
                    });

                    dialogView.findViewById(R.id.dialog_negative).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });

                    alertDialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
                    alertDialog.show();

                }
            });
        }
    }
}
