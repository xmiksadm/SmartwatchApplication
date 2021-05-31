package com.example.bakalarka.data.room;

public class RoomData {

    private float temperature;
    private float humidity;
    private float pressure;
    private float voc;


    public RoomData() {
        this.temperature = 0;
        this.humidity = 0;
        this.pressure = 0;
        this.voc = 0;
    }

    // Getters
    public float getTemperature() {
        return temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getPressure() {
        return pressure;
    }

    public float getVoc() {
        return voc;
    }


    // Setters
    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public void setVoc(float voc) {
        this.voc = voc;
    }
}
