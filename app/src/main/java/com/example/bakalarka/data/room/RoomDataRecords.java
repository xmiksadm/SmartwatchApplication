package com.example.bakalarka.data.room;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class RoomDataRecords {

    private final List<Float> vocList = new ArrayList<>();
    private final List<Float> temperatureList = new ArrayList<>();
    private final List<Float> humidityList = new ArrayList<>();
    private final List<Float> pressureList = new ArrayList<>();

    public RoomDataRecords() {
    }

    public void addRecord(@NonNull RoomData roomData){
        this.vocList.add(roomData.getVoc());
        this.temperatureList.add(roomData.getTemperature());
        this.humidityList.add(roomData.getTemperature());
        this.pressureList.add(roomData.getPressure());
        checkSize();
    }

    private void checkSize(){
        checkListSize(humidityList);
        checkListSize(vocList);
        checkListSize(temperatureList);
        checkListSize(pressureList);
    }

    private void checkListSize(@NonNull List<Float> list){
        if(list.size()>25){
            list.remove(0);
            checkListSize(list);
        }
    }

    @NonNull
    public List<Float> getVocList() {
        return vocList;
    }

    @NonNull
    public List<Float> getTemperatureList() {
        return temperatureList;
    }

    @NonNull
    public List<Float> getHumidityList() {
        return humidityList;
    }

    @NonNull
    public List<Float> getPressureList() {
        return pressureList;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder();
        for(Float value: temperatureList){
            tmp.append(value).append(", ");
        }
        StringBuilder hum = new StringBuilder();
        for(Float value: humidityList){
            hum.append(value).append(", ");
        }
        StringBuilder pres = new StringBuilder();
        for(Float value: pressureList){
            pres.append(value).append(", ");
        }
        StringBuilder voc = new StringBuilder();
        for(Float value: vocList){
            voc.append(value).append(", ");
        }
        return "RoomDataRecords{" +
                "resistanceList=" + tmp +
                ", temperatureList=" + hum +
                ", humidityList=" + pres +
                ", pressureList=" + voc +
                '}';
    }
}
