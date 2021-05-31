package com.example.bakalarka.data.sensor;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.util.Arrays;

// Senzor
public class GasensSensor {

    public static final String[] TOPICS = {"voc", "hum", "temp", "pres"};
    protected static final String URL = "tcp://hairdresser.cloudmqtt.com:15522";
    protected static final String USERNAME = "dnwvdmyg";
    protected static final String PASSWORD = "aIswpmgLoR9d";

    private final int id;

    public GasensSensor(int id) {
        this.id = id;
    }

    @NonNull
    public String[] getTopics(){
        String[] topics = Arrays.copyOf(TOPICS, TOPICS.length);
        @SuppressLint("DefaultLocale") String stringId = String.format("%02d",this.id);
        for (int i = 0; i<TOPICS.length; i++){
            topics[i] = "gasens_id"+stringId+"_"+topics[i];
        }
        return topics;
    }

    public int getId() {
        return id;
    }
}
