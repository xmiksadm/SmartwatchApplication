package com.example.bakalarka.data.notifications;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.bakalarka.activities.MainActivity;

/*
    Notifikácie
        Parametre:
            String title - Nadpis upozornenia
            String message - Správa upozornenia
            Context context - kontext aktivity
 */
public class Notification extends Activity {

    private final int roomId;
    private String notificationTitle;
    private String notificationMessage;
    private boolean playSound;

    @NonNull
    private final NotificationManager notificationManager;
    private NotificationCompat.Builder builder;

    // Konštruktory
    public Notification(int roomId) {
        this.roomId = roomId;
        this.notificationTitle = "Room (id="+ roomId +")";
        this.notificationMessage = "";
        this.playSound = false;

        // Vytvorenie notifikácie
        this.notificationManager = (NotificationManager) MainActivity.context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    // Setters
    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public void setBuilder(NotificationCompat.Builder builder) {
        this.builder = builder;
    }

    public void setPlaySound(boolean playSound) {
        this.playSound = playSound;
    }

    // Getters
    public String getNotificationTitle() {
        return notificationTitle;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public int getRoomId() {
        return roomId;
    }

    public boolean isPlaySound() {
        return playSound;
    }

    @NonNull
    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public NotificationCompat.Builder getBuilder() {
        return builder;
    }
}
