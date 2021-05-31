package com.example.bakalarka.activities.charts.basic;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bakalarka.R;
import com.example.bakalarka.data.room.RoomController;
import com.example.bakalarka.data.room.RoomDataRecords;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

// Základná aktivita pre zobrazenie stĺpcového grafu
public abstract class LineChartActivity extends ChartActivity {
    RoomController roomController;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);

        this.roomController = new RoomController();

        int roomId = getIntent().getIntExtra("room_id",0);
        RoomDataRecords dataRecords = roomController.getRoomById(roomId).getRoomDataRecords();
        List<Float> data = setData(dataRecords);

        // Graf
        LineChart chart = findViewById(R.id.line_chart);
        // Nastavenie dát grafu
        List<Entry> lineEntries = new ArrayList<>();
        for (int i=0; i<data.size(); i++){
            lineEntries.add(new Entry(2*i, data.get(i)));
        }


        LineDataSet lineDataSet = new LineDataSet(lineEntries, "Label");
        LineData lineData = new LineData(lineDataSet);
        chart.setData(lineData);
    }

    @NonNull
    public abstract List<Float> setData(RoomDataRecords data);

/*
    public static LineData setLineChartData(LineChart lineChart, ArrayList<Entry> data, String name){

        LineDataSet lineDataSet = new LineDataSet(data, name);
       /* lineDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        lineDataSet.setValueTextColor(Color.BLACK);
        lineDataSet.setValueTextSize(16f);

        LineData lineData = new LineData(lineDataSet);

        lineChart.setData(lineData);
        lineChart.animateY(1000);

        return lineData;
    }*/
}
