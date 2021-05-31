package com.example.bakalarka.activities.overview.basic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bakalarka.R;
import com.example.bakalarka.activities.roomSettings.DeleteRoomDialog;
import com.example.bakalarka.data.chart.ChartController;
import com.example.bakalarka.data.risks.ConditionsController;
import com.example.bakalarka.data.room.Room;
import com.example.bakalarka.data.room.RoomController;
import com.example.bakalarka.activities.roomSettings.RoomSettingsActivity;
import com.example.bakalarka.data.room.person.Illness;
import com.example.bakalarka.activities.charts.linechart.HumidityLineChartActivity;
import com.example.bakalarka.activities.charts.linechart.PressureLineChartActivity;
import com.example.bakalarka.activities.charts.linechart.VocLineChartActivity;
import com.example.bakalarka.activities.charts.linechart.TemperatureLineChartActivity;
import com.github.mikephil.charting.charts.PieChart;

import java.util.Map;
import java.util.Objects;

// Fragment v prehľade izby
public class OverviewFragment extends Fragment {

    PieChart temperatureChart, humidityChart, pressureChart, vocChart;
    TextView temperatureText, humidityText, pressureText, vocText;

    View view;
    Room room;

    @NonNull
    public static OverviewFragment newInstance(@Nullable Bundle args){
        OverviewFragment fragment = new OverviewFragment();
        if(args != null){
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_overview, container, false);

        ImageView deleteRoom = this.view.findViewById(R.id.delete_room);
        ImageView roomSettings = this.view.findViewById(R.id.room_settings);

        deleteRoom.setOnClickListener(v -> {
            DeleteRoomDialog deleteRoomDialog = DeleteRoomDialog.newInstance(room.getId());
            deleteRoomDialog.show(getFragmentManager(), "delete");
        });

        roomSettings.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RoomSettingsActivity.class);
            intent.putExtra("roomId", this.room.getId());
            startActivity(intent);
            //Objects.requireNonNull(getActivity()).finish();
        });

        setRoomSettings();
        //createCharts(view);
        findElements();
        setCharts();
        setListeners();

        return view;
    }

    @Nullable
    private Context getApplicationContext() {
        return this.getContext();
    }

    private void setRoomSettings(){
        TextView textView = this.view.findViewById(R.id.person_name);
        textView.setText(Html.fromHtml("<font color='black'>Meno: </font>" + this.room.getPerson().getName()));
        textView = this.view.findViewById(R.id.person_age);
        int age = this.room.getPerson().getAge();
        if (age > 0){
            textView.setText(Html.fromHtml("<font color='black'>Vek: </font>"+ age));
        }else{
            textView.setText(Html.fromHtml("<font color='black'>Vek: </font>"));
        }
        textView = this.view.findViewById(R.id.person_illnesses);
        StringBuilder illnesses = new StringBuilder();
        if(this.room.getPerson().getIllnesses() != null){
            for (Illness illness: this.room.getPerson().getIllnesses()){
                illnesses.append(illness.getName()).append(", ");
            }
        }

        textView.setText(Html.fromHtml("<font color='black'>Choroby: </font>" + illnesses));
    }

    private void findElements(){
        this.temperatureChart = view.findViewById(R.id.temperatureChart);
        this.humidityChart = view.findViewById(R.id.humidityChart);
        this.vocChart = view.findViewById(R.id.resistanceChart);
        this.pressureChart = view.findViewById(R.id.pressureChart);

        this.temperatureText = view.findViewById(R.id.temperature_text);
        this.humidityText = view.findViewById(R.id.humidity_text);
        this.vocText = view.findViewById(R.id.resistance_text);
        this.pressureText = view.findViewById(R.id.pressure_text);
    }

    public void setCharts(){
        Map<String, Integer> risks = new ConditionsController().getRisks(room);
        ChartController chartController = new ChartController();

        chartController.setPieChart(this.temperatureChart, risks.get(ConditionsController.RISKS_KEY_TMP), this.room.getRoomData().getTemperature(), this.temperatureText, "°C", 35);
        chartController.setPieChart(this.humidityChart, risks.get(ConditionsController.RISKS_KEY_HUM), this.room.getRoomData().getHumidity(), this.humidityText, "%", 100);
        chartController.setPieChart(this.pressureChart, risks.get(ConditionsController.RISKS_KEY_PRES), this.room.getRoomData().getPressure(), this.pressureText, "P", 2000);
        chartController.setPieChart(this.vocChart, risks.get(ConditionsController.RISKS_KEY_VOC), this.room.getRoomData().getVoc(), this.vocText, "", 500);
    }


    private void setListeners(){
        setOpenLineChartListener(this.view.findViewById(R.id.open_line_chart_tmp), TemperatureLineChartActivity.class);
        setOpenLineChartListener(this.view.findViewById(R.id.open_line_chart_hum), HumidityLineChartActivity.class);
        setOpenLineChartListener(this.view.findViewById(R.id.open_line_chart_pres), PressureLineChartActivity.class);
        setOpenLineChartListener(this.view.findViewById(R.id.open_line_chart_voc), VocLineChartActivity.class);
    }

    private void setOpenLineChartListener(@NonNull ImageView view, Class c){
        view.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), c);
            intent.putExtra("room_id", this.room.getId());
            startActivity(intent);
        });
    }

    public void updateFragment(){
        //this.room = RoomController.findRoomById(id);
        setCharts();
        this.temperatureChart.invalidate();
        this.humidityChart.invalidate();
        this.vocChart.invalidate();
        this.pressureChart.invalidate();
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        RoomController roomController = new RoomController();
        this.room = roomController.getRoomById(Objects.requireNonNull(args).getInt("room"));
    }

}
