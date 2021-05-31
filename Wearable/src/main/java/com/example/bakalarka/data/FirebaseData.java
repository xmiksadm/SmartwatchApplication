package com.example.bakalarka.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirebaseData {

    public static List<FirebaseData> firebaseDataList;

    private int id;
    //private String roomName;
    private boolean watchOn;
    private int heartRate;
    private String fallDetected;

    public FirebaseData(Map<Integer, FirebaseData> firebaseDataMap) {
        firebaseDataList = new ArrayList<>(firebaseDataMap.values());
    }

    public FirebaseData(int id, boolean watchOn, int heartRate, String fallDetected) {
        this.id = id;
        //this.roomName = roomName;
        this.watchOn = watchOn;
        this.heartRate = heartRate;
        this.fallDetected = fallDetected;
    }

    public static List<FirebaseData> getFirebaseDataList() {
        return firebaseDataList;
    }

    public static void setFirebaseDataList(List<FirebaseData> firebaseDataList) {
        FirebaseData.firebaseDataList = firebaseDataList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isWatchOn() {
        return watchOn;
    }

    public void setWatchOn(boolean watchOn) {
        this.watchOn = watchOn;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public String getFallDetected() {
        return fallDetected;
    }

    public void setFallDetected(String fallDetected) {
        this.fallDetected = fallDetected;
    }
}
