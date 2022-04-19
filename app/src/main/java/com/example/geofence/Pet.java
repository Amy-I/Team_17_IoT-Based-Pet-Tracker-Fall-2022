package com.example.geofence;

public class Pet {
    private String pName;
    private String pTrackerID;
    private String pCameraIP;

    public Pet(String name, String trackerID, String cameraIP){
        pName = name;
        pTrackerID = trackerID;
        pCameraIP = cameraIP;
    }

    public void setPetName(String str){
        pName = str;
    }

    public String getPetName(){
        return pName;
    }

    public void setPetTrackerID(String str){
        pTrackerID = str;
    }

    public String getPetTrackerID(){
        return pTrackerID;
    }

    public void setPetCameraIP(String str){
        pCameraIP = str;
    }

    public String getPetCameraIP(){
        return pCameraIP;
    }
}
