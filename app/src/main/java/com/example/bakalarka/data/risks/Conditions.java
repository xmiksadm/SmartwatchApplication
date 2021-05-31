package com.example.bakalarka.data.risks;

import androidx.annotation.NonNull;

public class Conditions {
    public static final String TEMPERATURE_CONDITIONS = "temperature";
    public static final String HUMIDITY_CONDITIONS = "humidity";
    public static final String PRESSURE_CONDITIONS = "pressure";
    public static final String VOC_CONDITIONS = "voc";
    public static final float MAX_TMP = 27;
    public static final float HIGH_TMP = 24;
    public static final float IDEAL_TMP = 22;
    public static final float LOW_TMP = 18;
    public static final float MIN_TMP = 15;
    public static final float MAX_PRES = 1100;
    public static final float HIGH_PRES = 1050;
    public static final float IDEAL_PRES = 1000;
    public static final float LOW_PRES = 950;
    public static final float MIN_PRES = 900;
    public static final float MAX_HUM = 60;
    public static final float HIGH_HUM = 40;
    public static final float IDEAL_HUM = 30;
    public static final float LOW_HUM = 20;
    public static final float MIN_HUM = 5;
    public static final float MAX_VOC = 500;
    public static final float HIGH_VOC = 400;
    public static final float IDEAL_VOC = 300;
    public static final float LOW_VOC = 250;
    public static final float MIN_VOC = 200;

    private float max, high, ideal, low, min;

    public Conditions(float max, float high, float ideal, float low, float min) {
        this.max = max;
        this.high = high;
        this.ideal = ideal;
        this.low = low;
        this.min = min;
    }

    public Conditions(@NonNull String s){
        switch (s) {
            case TEMPERATURE_CONDITIONS:
                setConditions(MAX_TMP, HIGH_TMP, IDEAL_TMP, LOW_TMP, MIN_TMP);
                break;
            case HUMIDITY_CONDITIONS:
                setConditions(MAX_HUM, HIGH_HUM, IDEAL_HUM, LOW_HUM, MIN_HUM);
                break;
            case PRESSURE_CONDITIONS:
                setConditions(MAX_PRES, HIGH_PRES, IDEAL_PRES, LOW_PRES, MIN_PRES);
                break;
            case VOC_CONDITIONS:
                setConditions(MAX_VOC, HIGH_VOC, IDEAL_VOC, LOW_VOC, MIN_VOC);
                break;
            default:
                setConditions(0, 0, 0, 0, 0);
                break;
        }
    }

    private void setConditions(float max, float high, float ideal, float low, float min){
        setMax(max);
        setHigh(high);
        setIdeal(ideal);
        setLow(low);
        setMin(min);
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getIdeal() {
        return ideal;
    }

    public void setIdeal(float ideal) {
        this.ideal = ideal;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }
}
