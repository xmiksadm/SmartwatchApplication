package com.example.bakalarka.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bakalarka.service.Adapter;
import com.example.bakalarka.R;

public class SecondActivity extends AppCompatActivity {

    TextView room, patientName;
    ImageButton temperature, pressure, voc, humidity, heartRate, fall;

    String roomName, patientNameString;
    int patientAgeInt, positionInList;
    float vocFloat, temperatureFloat, humidityFloat, pressureFloat;

    boolean watchOn;
    String fallDetected;
    int heartRateInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        findViews();
        getData();
        setData();
        buttonListeners();
    }

    public void findViews() {
        room = findViewById(R.id.room);
        patientName = findViewById(R.id.personName);
        temperature = findViewById(R.id.temperature);
        pressure = findViewById(R.id.pressure);
        humidity = findViewById(R.id.humidity);
        voc = findViewById(R.id.voc);
        //watch = findViewById(R.id.watch);
        heartRate = findViewById(R.id.heartRate);
        fall = findViewById(R.id.fall);
    }

    public void buttonListeners() {
        String temp = temperatureFloat + " °C";
        temperature.setOnClickListener(v -> openData(temp));
        String press = pressureFloat + " P";
        pressure.setOnClickListener(v -> openData(press));
        String hum = humidityFloat + " %";
        humidity.setOnClickListener(v -> openData(hum));
        String vocSend = vocFloat + "";
        voc.setOnClickListener(v -> openData(vocSend));
        String heart = heartRateInt + "";
        heartRate.setOnClickListener(v -> openData(heart));
        String fallDet = "Možný pád: " + fallDetected;
        fall.setOnClickListener(v -> openData(fallDet));
    }

    public void openData(String data) {
        Intent intent = new Intent(this, Fullscreen.class);
        intent.putExtra("data", data);
        startActivity(intent);
    }

    private void getData() {
        if (getIntent().hasExtra("roomName") ) {

            roomName = getIntent().getStringExtra("roomName");
            patientNameString = getIntent().getStringExtra("patientName");
            patientAgeInt = getIntent().getIntExtra("patientAge", 0);

            temperatureFloat = getIntent().getFloatExtra("temp", 0.0F);
            humidityFloat = getIntent().getFloatExtra("humidity", 0.0F);
            pressureFloat = getIntent().getFloatExtra("pressure", 0.0F);
            vocFloat = getIntent().getFloatExtra("voc", 0.0F);
            positionInList = getIntent().getIntExtra("position", 0);

            watchOn = getIntent().getBooleanExtra("watchOn", false);
            heartRateInt = getIntent().getIntExtra("heartRate", 0);
            fallDetected = getIntent().getStringExtra("fall");

        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    private void setData() {
        room.setText(roomName);
        patientName.setText(patientNameString + ", " + patientAgeInt); // set name and age

        if (!watchOn) {
            // nema hodinky
            fall.setBackgroundColor(Color.parseColor("#444444"));
            heartRate.setBackgroundColor(Color.parseColor("#444444"));
        }
        else {
            // ma hodinky
            fall.setBackgroundColor(Color.parseColor("#4CAF50"));
            heartRate.setBackgroundColor(Color.parseColor("#4CAF50"));
        }

        setItemColor();
    }

    public void setItemColor() {
        Adapter adapterCheck = new Adapter();
        int colorChecker;

        colorChecker = adapterCheck.checkTemperatureColor(temperatureFloat, positionInList);
        if (colorChecker == 2)
            temperature.setBackgroundColor(Color.parseColor("#cc0000"));
            //temperature.setTextColor(Color.parseColor("#FF0000")); // red
        else if (colorChecker == 1)
            temperature.setBackgroundColor(Color.parseColor("#FF9800")); // orange
            //temperature.setTextColor(Color.parseColor("#FFA500")); // orange
        else
            temperature.setBackgroundColor(Color.parseColor("#4CAF50")); // green
            //temperature.setTextColor(Color.parseColor("#00C853")); // green

        colorChecker = adapterCheck.checkPressureColor(pressureFloat, positionInList);
        if (colorChecker == 2)
           pressure.setBackgroundColor(Color.parseColor("#cc0000"));
        else if (colorChecker == 1)
            pressure.setBackgroundColor(Color.parseColor("#FF9800"));
        else
            pressure.setBackgroundColor(Color.parseColor("#4CAF50"));

        colorChecker = adapterCheck.checkHumidityColor(humidityFloat, positionInList);
        if (colorChecker == 2)
            humidity.setBackgroundColor(Color.parseColor("#cc0000"));
        else if (colorChecker == 1)
            humidity.setBackgroundColor(Color.parseColor("#FF9800"));
        else
            humidity.setBackgroundColor(Color.parseColor("#4CAF50"));

        colorChecker = adapterCheck.checkVocColor(vocFloat, positionInList);
        if (colorChecker == 2)
            voc.setBackgroundColor(Color.parseColor("#cc0000"));
        else if (colorChecker == 1)
            voc.setBackgroundColor(Color.parseColor("#FF9800"));
        else
            voc.setBackgroundColor(Color.parseColor("#4CAF50"));
    }
}