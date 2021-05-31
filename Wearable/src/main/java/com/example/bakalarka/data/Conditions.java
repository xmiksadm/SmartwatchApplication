package com.example.bakalarka.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Conditions {

    private int id;

    private float tempMin = 16, tempLow = 28, tempHigh = 20, tempMax = 25;
    private float humidityMin = 20, humidityLow = 60, humidityHigh = 30, humidityMax = 45;
    private float pressureMin = 960, pressureLow = 1060, pressureHigh = 980, pressureMax = 1040;
    private float vocMin = 200, vocLow = 250, vocHigh = 400, vocMax = 500;

    private Map<Integer, Conditions> conditions = new HashMap<>();
    public static List<Conditions> conditionsList;

    public Conditions() {
    }

    public Conditions(Map<Integer, Conditions> conditions) {
        this.conditions = conditions;
        conditionsList = new ArrayList<>(conditions.values());
    }

    public Conditions(int id,
                      float tempMin, float tempLow, float tempHigh, float tempMax,
                      float humidityMin, float humidityLow, float humidityHigh, float humidityMax,
                      float pressureMin, float pressureLow, float pressureHigh, float pressureMax,
                      float vocMin, float vocLow, float vocHigh, float vocMax) {

        this.id = id;
        this.tempMin = tempMin;
        this.tempLow = tempLow;
        this.tempHigh = tempHigh;
        this.tempMax = tempMax;

        this.humidityMin = humidityMin;
        this.humidityLow = humidityLow;
        this.humidityHigh = humidityHigh;
        this.humidityMax = humidityMax;

        this.pressureMin = pressureMin;
        this.pressureLow = pressureLow;
        this.pressureHigh = pressureHigh;
        this.pressureMax = pressureMax;

        this.vocMin = vocMin;
        this.vocLow = vocLow;
        this.vocHigh = vocHigh;
        this.vocMax = vocMax;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getVocMin() {
        return vocMin;
    }

    public void setVocMin(float vocMin) {
        this.vocMin = vocMin;
    }

    public float getVocLow() {
        return vocLow;
    }

    public void setVocLow(float vocLow) {
        this.vocLow = vocLow;
    }

    public float getVocHigh() {
        return vocHigh;
    }

    public void setVocHigh(float vocHigh) {
        this.vocHigh = vocHigh;
    }

    public float getVocMax() {
        return vocMax;
    }

    public void setVocMax(float vocMax) {
        this.vocMax = vocMax;
    }

    public float getTempMin() {
        return tempMin;
    }

    public void setTempMin(float tempMin) {
        this.tempMin = tempMin;
    }

    public float getTempLow() {
        return tempLow;
    }

    public void setTempLow(float tempLow) {
        this.tempLow = tempLow;
    }

    public float getTempHigh() {
        return tempHigh;
    }

    public void setTempHigh(float tempHigh) {
        this.tempHigh = tempHigh;
    }

    public float getTempMax() {
        return tempMax;
    }

    public void setTempMax(float tempMax) {
        this.tempMax = tempMax;
    }

    public float getHumidityMin() {
        return humidityMin;
    }

    public void setHumidityMin(float humidityMin) {
        this.humidityMin = humidityMin;
    }

    public float getHumidityLow() {
        return humidityLow;
    }

    public void setHumidityLow(float humidityLow) {
        this.humidityLow = humidityLow;
    }

    public float getHumidityHigh() {
        return humidityHigh;
    }

    public void setHumidityHigh(float humidityHigh) {
        this.humidityHigh = humidityHigh;
    }

    public float getHumidityMax() {
        return humidityMax;
    }

    public void setHumidityMax(float humidityMax) {
        this.humidityMax = humidityMax;
    }

    public float getPressureMin() {
        return pressureMin;
    }

    public void setPressureMin(float pressureMin) {
        this.pressureMin = pressureMin;
    }

    public float getPressureLow() {
        return pressureLow;
    }

    public void setPressureLow(float pressureLow) {
        this.pressureLow = pressureLow;
    }

    public float getPressureHigh() {
        return pressureHigh;
    }

    public void setPressureHigh(float pressureHigh) {
        this.pressureHigh = pressureHigh;
    }

    public float getPressureMax() {
        return pressureMax;
    }

    public void setPressureMax(float pressureMax) {
        this.pressureMax = pressureMax;
    }

    public static List<Conditions> getConditionsList() {
        return conditionsList;
    }

    public static void setConditionsList(List<Conditions> conditionsList) {
        Conditions.conditionsList = conditionsList;
    }
}
