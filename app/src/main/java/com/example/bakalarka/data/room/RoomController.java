package com.example.bakalarka.data.room;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bakalarka.data.wear.WearController;
import com.example.bakalarka.database.RoomDatabaseController;
import com.example.bakalarka.data.risks.Conditions;
import com.example.bakalarka.data.risks.ConditionsController;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class RoomController {
    // Rooms list
    public static final ArrayList<Room> rooms = new ArrayList<>();

    // Controller for database
    @NonNull
    private final RoomDatabaseController roomDatabaseController;

    // Controller for Android Wear
    @NonNull
    private final WearController wearController;

    // Constructor
    public RoomController() {
        this.roomDatabaseController = new RoomDatabaseController();
        this.wearController = new WearController();
    }

    // Adding rooms
    public void addRooms(@NonNull List<Room> rooms){
        for (Room room: rooms){
            addRoom(room);
        }
    }

    public void addRoom(@NonNull Room room){
        if (room.getConditionsMap() == null){
            createConditions(room);
        }
        startDataSave(room);
        rooms.add(room);
        this.roomDatabaseController.listRooms();
    }

    // Methods for getting rooms
    @NonNull
    public Room getRoomById(@Nullable Integer id){
        if (id == null){
            throw new NullPointerException("GetRoomById: id is null: "+id);
        }
        for (Room room: rooms){
            if (room.getId() == id){
                return room;
            }
        }
        throw new NullPointerException("GetRoomById: No room with id: "+id);
    }

    public int getPositionInList(int id){
        Room room = getRoomById(id);
        return rooms.indexOf(room);
    }

    @NonNull
    public List<Room> getRiskyRooms(){
        List<Room> riskyRooms = new ArrayList<>();
        for(Room room: rooms){
            if(room.getRiskLevel() > ConditionsController.LOW){
                riskyRooms.add(room);
            }
        }
        return riskyRooms;
    }

    @NonNull
    public ArrayList<Integer> getAllIds(){
        ArrayList<Integer> ids = new ArrayList<>();
        for (Room room: rooms){
            ids.add(room.getId());
        }
        return ids;
    }

    // Removing rooms
    public void removeRooms(@NonNull List<Room> rooms) {
        for (Room room: rooms){
            new Thread(() -> this.roomDatabaseController.deleteRoomInDB(room.getId())).start();
            removeRoom(room.getId());
        }
    }

    public void removeRoom(int roomId) {
        Room room = getRoomById(roomId);
        try {
            room.getMqttClient().getClient().disconnect();
        } catch (MqttException e) {
            System.out.println(e.getMessage());
        }
        new Thread(() -> this.roomDatabaseController.deleteWholeRoom(roomId)).start();
        rooms.remove(room);
    }


    public boolean containsRoom(int id){
        for (Room room: rooms){
            if (room.getId() == id){
                return true;
            }
        }
        return false;
    }

    public void createConditions(@NonNull Room room){
        Map<String, Conditions> conditionsMap = new HashMap<>();

        conditionsMap.put(Conditions.TEMPERATURE_CONDITIONS, new Conditions(Conditions.TEMPERATURE_CONDITIONS));
        conditionsMap.put(Conditions.HUMIDITY_CONDITIONS, new Conditions(Conditions.HUMIDITY_CONDITIONS));
        conditionsMap.put(Conditions.PRESSURE_CONDITIONS, new Conditions(Conditions.PRESSURE_CONDITIONS));
        conditionsMap.put(Conditions.VOC_CONDITIONS, new Conditions(Conditions.VOC_CONDITIONS));

        room.setConditionsMap(conditionsMap);
    }

    // Begin saving data for room
    public void startDataSave(@NonNull Room room){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                RoomData roomData = room.getMqttClient().getRoomData();

                room.setRoomData(roomData);
                room.getRoomDataRecords().addRecord(roomData);

                ConditionsController conditionsController = new ConditionsController();
                Map<String, Integer> risks = conditionsController.getRisks(room);
                room.setRiskLevel(conditionsController.getRiskLevel(room));
                conditionsController.createNotification(room.getNotification(), risks);
                wearController.sendRoomData(room.getId(), roomData);
            }
        }, 2*60*1000, 2*60*1000);
    }

    // Getters
    @NonNull
    public RoomDatabaseController getRoomDatabaseController() {
        return roomDatabaseController;
    }
}
