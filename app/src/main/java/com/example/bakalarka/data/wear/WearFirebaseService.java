package com.example.bakalarka.data.wear;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bakalarka.data.notifications.Notification;
import com.example.bakalarka.data.notifications.NotificationController;
import com.example.bakalarka.data.room.Room;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class WearFirebaseService {

    private ArrayList<Integer> ids = new ArrayList<>();

    public ArrayList<Integer> getIds() {
        return ids;
    }

    public void setIds(ArrayList<Integer> ids) {
        this.ids = ids;
    }

    private static final String TAG = "WearFirebase";
    DatabaseReference rooms = FirebaseDatabase.getInstance().getReference().child("rooms");
    WearController wearController = new WearController();

    public void writeRoomToFirebase(@NotNull Room room) {

        ArrayList<Integer> idsToDelete = new ArrayList<>();
        DatabaseReference oneRoom = rooms.child(String.valueOf(room.getId()));
        oneRoom.child("roomName").setValue(room.getRoomName());

        // to prevent overwriting watchOn: "false" for watches that are already "true"
        if (WearRoomFirebase.rooms.get(room.getId()) != null) {
            if (!WearRoomFirebase.rooms.get(room.getId()).hasWatch()) {
                oneRoom.child("watchOn").setValue("false");
            }
        } else {
                oneRoom.child("watchOn").setValue("false");
        }

        rooms.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                WearRoomFirebase wearRoomFirebase;

                Log.e("Firebase listener", "Data changed!");

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    // check if there are rooms in Firebase that needs to be deleted
                    if (!ids.contains(Integer.parseInt(snapshot.getKey()))) {
                        idsToDelete.add(Integer.parseInt(snapshot.getKey()));
                    }
                    if (!idsToDelete.isEmpty()) {
                        removeFromFirebase(idsToDelete);
                    }

                    Log.d(TAG, "ID: " + snapshot.getKey() + " " + snapshot.getValue().toString());
                    boolean watch = false;
                    String fallDetected = "";
                    int heartRate = 0;

                    if (snapshot.child("watchOn").exists()) {
                        watch = Boolean.parseBoolean(snapshot.child("watchOn").getValue().toString());
                    }
                    if (snapshot.child("fallDetected").exists()) {
                        fallDetected = snapshot.child("fallDetected").getValue().toString();
                    }
                    if (snapshot.child("heartRate").exists()) {
                        heartRate = Integer.parseInt(snapshot.child("heartRate").getValue().toString());
                    }
                    wearRoomFirebase = new WearRoomFirebase(Integer.parseInt(snapshot.getKey()),
                            watch,
                            heartRate,
                            fallDetected);

                    WearRoomFirebase.rooms.put(Integer.parseInt(snapshot.getKey()), wearRoomFirebase);
                    //wearController.sendFirebaseData(wearRoomFirebase);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", "Failed to read value", error.toException());
            }
        });
    }

    public void removeFromFirebase(ArrayList<Integer> idsToDelete) {
        for (int id : idsToDelete) {
            rooms.child(String.valueOf(id)).removeValue();
        }
    }

    public void firebaseDataListener() {
        rooms.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                WearRoomFirebase wearRoomFirebase;

                Log.e("Firebase listener", "Data changed!");

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Log.d(TAG, "ID: " + snapshot.getKey() + " " + snapshot.getValue().toString());
                    boolean watch = false;
                    String fallDetected = "";
                    int heartRate = 0;

                    if (snapshot.child("watchOn").exists()) {
                        watch = Boolean.parseBoolean(snapshot.child("watchOn").getValue().toString());
                    }
                    if (snapshot.child("fallDetected").exists()) {
                        fallDetected = snapshot.child("fallDetected").getValue().toString();
                        fallDetectionChanged(Integer.parseInt(snapshot.getKey()));
                    }
                    if (snapshot.child("heartRate").exists()) {
                        heartRate = Integer.parseInt(snapshot.child("heartRate").getValue().toString());
                    }
                    wearRoomFirebase = new WearRoomFirebase(Integer.parseInt(snapshot.getKey()),
                            watch,
                            heartRate,
                            fallDetected);

                    WearRoomFirebase.rooms.put(Integer.parseInt(snapshot.getKey()), wearRoomFirebase);
                    wearController.sendFirebaseData(wearRoomFirebase);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", "Failed to read value", error.toException());
            }
        });
    }

    public void fallDetectionChanged(int id) {

        rooms.child(String.valueOf(id)).child("fallDetected").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (WearRoomFirebase.rooms.get(id) == null || !WearRoomFirebase.rooms.get(id).getFallDetected().equals(snapshot.getValue())) {
                            
                            Log.d(TAG, "onDataChange: fall " + snapshot.getValue());
                            Notification notification = new Notification(id);
                            notification.setNotificationMessage("Možný pád");
                            final NotificationController notificationController = new NotificationController();
                            notificationController.showNotification(notification, 2);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Error", "Failed to read value", error.toException());
                    }
                }
        );
    }
}
