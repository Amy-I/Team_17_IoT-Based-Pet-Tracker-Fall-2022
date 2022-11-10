package com.example.geofence;

import android.app.Application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PetApplication extends Application {

    private static List<Pet> petList = new ArrayList<Pet>();
    private static int nextInt = 0;
    private static String petIP;

    public PetApplication() {
    }

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

    public static String getPetIP(){
        return petIP;
    }

    public static void setPetIP(String petIP){
        PetApplication.petIP = petIP;
    }
}
