package com.example.bakalarka.data.wear;

import androidx.annotation.NonNull;

import com.example.bakalarka.data.notifications.Notification;

import java.util.HashMap;
import java.util.Map;

public class WearRoomFirebase {

    // All rooms
    public static final Map<Integer, WearRoomFirebase> rooms = new HashMap<>();

    private int id;
    private boolean watch;
    private int heartRate;
    private String fallDetected;
    @NonNull
    private Notification notification;

    public WearRoomFirebase() {
    }

    public WearRoomFirebase(int id) {
        this.id = id;
        this.notification = new Notification(id);
    }

    public WearRoomFirebase(int id, boolean watch, int heartRate, String fallDetected) {
        this.id = id;
        this.watch = watch;
        this.heartRate = heartRate;
        this.fallDetected = fallDetected;
        this.notification = new Notification(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean hasWatch() {
        return watch;
    }

    public void setWatch(boolean watch) {
        this.watch = watch;
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

    @NonNull
    public Notification getNotification() {
        return notification;
    }

    public static Map<Integer, WearRoomFirebase> getRooms() {
        return rooms;
    }
}
