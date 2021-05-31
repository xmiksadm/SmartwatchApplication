package com.example.bakalarka.data.chart;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.bakalarka.data.risks.ConditionsController;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ChartController {
    public void setPieChart(@NonNull PieChart pieChart, int riskLevel, float data, @NonNull TextView textView, String unit, float max){
        setText(textView, data+""+unit);
        pieChart.setData(setPieChartData(riskLevel, data, max));
        setBasicPieChart(pieChart);
    }

    private void setText(@NonNull TextView view, String text){
        view.setText(text);
    }

    @NonNull
    public PieData setPieChartData(int riskLevel, float data, float max){
        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(data));
        pieEntries.add(new PieEntry(max-data));
        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        String color;
        if (riskLevel == ConditionsController.NONE){
            color = "32CD32";
        }else if(riskLevel == ConditionsController.LOW){
            color = "FF6600";
        }else{
            color = "C00000";
        }
        dataSet.setColors(ColorTemplate.rgb(color), ColorTemplate.rgb("cccccc"));
        dataSet.setDrawValues(false);
        return new PieData(dataSet);
    }


    private void setBasicPieChart(@NonNull PieChart chart){
        chart.setMaxAngle(180);
        chart.setHoleRadius(65);
        chart.setDescription(null);
        chart.getLegend().setEnabled(false);
        chart.setRotationEnabled(false);
        chart.setHighlightPerTapEnabled(false);
    }

}
