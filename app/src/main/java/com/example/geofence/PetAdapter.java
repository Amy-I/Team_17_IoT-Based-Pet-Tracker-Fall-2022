package com.example.geofence;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {

    List<Pet> petList;
    Context context;

    //private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();;
    //private DatabaseReference databaseReference = firebaseDatabase.getReference();

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
        holder.petStatus.setText(petList.get(position).getPetCameraIP());
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public class PetViewHolder extends RecyclerView.ViewHolder{
        ImageView petPic;
        TextView petName;
        TextView petStatus;
        Button bRequestFeed;

        public PetViewHolder(@NonNull View itemView) {
            super(itemView);

            petPic = itemView.findViewById(R.id.imageView);
            petName = itemView.findViewById(R.id.pName_Account);
            petStatus = itemView.findViewById(R.id.pTrackerStatus);
            itemView.findViewById(R.id.pCamera).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context.getApplicationContext(), "Go to website: " + petStatus.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
