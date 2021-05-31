package com.example.bakalarka.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Room {

    public static List<Room> roomList;

    private int id;
    private String roomName;
    private float temperature;
    private float humidity;
    private float pressure;
    private  float voc;

    public Room(int id, String roomName, float temperature, float humidity, float pressure, float voc) {
        this.id = id;
        this.roomName = roomName;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.voc = voc;
    }

    public Room(Map<Integer, Room> room) {
        roomList = new ArrayList<>(room.values());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getVoc() {
        return voc;
    }

    public void setVoc(float voc) {
        this.voc = voc;
    }

    public static List<Room> getRoomList() {
        return roomList;
    }

    public static void setRoomList(List<Room> roomList) {
        Room.roomList = roomList;
    }
}
