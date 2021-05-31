package com.example.bakalarka.activities.charts.linechart;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.bakalarka.data.room.RoomDataRecords;
import com.example.bakalarka.activities.charts.basic.LineChartActivity;

import java.util.List;

public class VocLineChartActivity extends LineChartActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public List<Float> setData(@NonNull RoomDataRecords data) {
        return data.getVocList();
    }
}