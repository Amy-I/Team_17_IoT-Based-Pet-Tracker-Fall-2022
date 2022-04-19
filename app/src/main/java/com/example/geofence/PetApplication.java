package com.example.geofence;

import android.app.Application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PetApplication extends Application {

    private static List<Pet> petList = new ArrayList<Pet>();
    private static int nextInt = 0;

    public PetApplication() {
        //addPet();
    }

    /* Testing purposes
    private void addPet(){
        Pet p1 = new Pet("Fido", "123456", "128.12.456");
        Pet p2 = new Pet("Molly", "234567", "128.12.457");
        Pet p3 = new Pet("Spot", "345678", "128.12.458");
        Pet p4 = new Pet("Princess", "456789", "128.12.459");
        Pet p5 = new Pet("Nugget", "567891", "128.12.460");

        petList.addAll(Arrays.asList(new Pet[] {p1, p2, p3, p4, p5}));
    }

    */

    public static List<Pet> getPetList() {
        return petList;
    }

    public static void setPetList(List<Pet> petList) {
        PetApplication.petList = petList;
    }

    public static int getNextInt() {
        return petList.size()+1;
    }

    public static void setNextInt(int nextInt) {
        PetApplication.nextInt = nextInt;
    }
}
