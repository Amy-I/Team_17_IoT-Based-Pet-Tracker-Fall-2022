package com.example.geofence;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {

    List<Pet> petList;
    Context context;

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
        holder.petPic.setImageResource(R.drawable.ic_launcher_foreground);
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
            bRequestFeed = itemView.findViewById(R.id.pCamera);
        }
    }
}
