package com.example.bakalarka.service;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakalarka.R;
import com.example.bakalarka.activities.SecondActivity;
import com.example.bakalarka.data.Conditions;
import com.example.bakalarka.data.FirebaseData;
import com.example.bakalarka.data.Person;
import com.example.bakalarka.data.Room;

import java.util.Map;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    Context context;

    public Adapter() {
    }

    public Adapter(Context context, Map<Integer, Person> personInfo, Map<Integer, Room> roomData, Map<Integer, Conditions> conditions, Map<Integer, FirebaseData> firebaseData) {
        this.context = context;

        new Person(personInfo);
        new Room(roomData);
        new Conditions(conditions);
        new FirebaseData(firebaseData);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder = setColor(holder, position);

        holder.mainLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, SecondActivity.class);

            float temp, humidity, pressure, voc;
            temp = Room.getRoomList().get(position).getTemperature();
            humidity = Room.getRoomList().get(position).getHumidity();
            pressure = Room.getRoomList().get(position).getPressure();
            voc = Room.getRoomList().get(position).getVoc();

            intent.putExtra("roomName", Person.getPersonList().get(position).getRoomName());
            intent.putExtra("patientName", Person.getPersonList().get(position).getName());
            intent.putExtra("patientAge", Person.getPersonList().get(position).getAge());
            intent.putExtra("temp", temp);
            intent.putExtra("humidity", humidity);
            intent.putExtra("pressure", pressure);
            intent.putExtra("voc", voc);
            intent.putExtra("position", position);
            intent.putExtra("watchOn", FirebaseData.getFirebaseDataList().get(position).isWatchOn());
            intent.putExtra("heartRate", FirebaseData.getFirebaseDataList().get(position).getHeartRate());
            intent.putExtra("fall", FirebaseData.getFirebaseDataList().get(position).getFallDetected());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (Room.getRoomList().isEmpty())
            return 0;
        return Room.getRoomList().size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView patientName, voc, temp, humidity, pressure;
        ConstraintLayout mainLayout;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            patientName = itemView.findViewById(R.id.patient_name);
            voc = itemView.findViewById(R.id.voc);
            temp = itemView.findViewById(R.id.temperature);
            humidity = itemView.findViewById(R.id.humidity);
            pressure = itemView.findViewById(R.id.pressure);
            imageView = itemView.findViewById(R.id.menu_icon);

            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }

    public MyViewHolder setColor(MyViewHolder holder, int position) {

        if (!Room.getRoomList().isEmpty()) {
            // set room name to red if one of room data is out of bounds
            if (checkVocColor(Room.getRoomList().get(position).getVoc(), position) == 2 ||
                    checkTemperatureColor(Room.getRoomList().get(position).getTemperature(), position) == 2 ||
                    checkHumidityColor(Room.getRoomList().get(position).getHumidity(), position) == 2 ||
                    checkPressureColor(Room.getRoomList().get(position).getPressure(), position) == 2)
            {

                holder.imageView.setColorFilter(Color.parseColor("#cc0000"));
                //holder.patientName.setTextColor(Color.parseColor("#cc0000"));
            }
            // set room name to green if all room data are satisfying
            else if (checkVocColor(Room.getRoomList().get(position).getVoc(), position) == 0 &&
                    checkTemperatureColor(Room.getRoomList().get(position).getTemperature(), position) == 0 &&
                    checkHumidityColor(Room.getRoomList().get(position).getHumidity(), position) == 0 &&
                    checkPressureColor(Room.getRoomList().get(position).getPressure(), position) == 0)
            {
                holder.imageView.setColorFilter(Color.parseColor("#4CAF50"));
                //holder.patientName.setTextColor(Color.parseColor("#4CAF50"));
            }
            // set room name to orange
            else {
                holder.imageView.setColorFilter(Color.parseColor("#FF9800"));
                //holder.patientName.setTextColor(Color.parseColor("#FF9800"));
            }
        }

        if (position < Person.getPersonList().size()) {
            holder.patientName.setText(String.valueOf(Person.getPersonList().get(position).getRoomName()));
        }

        return holder;
    }

    public int checkVocColor(float checkVoc, int position) throws IndexOutOfBoundsException {

        // check for IndexOutOfBoundsException
        if (position < Conditions.conditionsList.size()) {

            // returns 2 if conditions are critical
            if (checkVoc < Conditions.conditionsList.get(position).getVocMin() || checkVoc > Conditions.conditionsList.get(position).getVocMax()) {
                return 2;
            }
            // returns 1 if conditions are near critical
            else if (checkVoc < Conditions.conditionsList.get(position).getVocLow() || checkVoc > Conditions.conditionsList.get(position).getVocHigh()) {
                return 1;
            }
            // return 0 if conditions are fine
            else {
                return 0;
            }
        }
        return 2;
    }

    public int checkTemperatureColor(float checkTemp, int position) throws IndexOutOfBoundsException {

        if (position < Conditions.conditionsList.size()) {
            if (checkTemp < Conditions.conditionsList.get(position).getTempMin() || checkTemp > Conditions.conditionsList.get(position).getTempMax()) {
                return 2;
            }
            else if (checkTemp < Conditions.conditionsList.get(position).getTempLow() || checkTemp > Conditions.conditionsList.get(position).getTempHigh()) {
                return 1;
            }
            else {
                return 0;
            }
        }
        return 2;
    }

    public int checkHumidityColor(float checkHumidity, int position) throws IndexOutOfBoundsException {

        if (position < Conditions.conditionsList.size()) {
            if (checkHumidity < Conditions.conditionsList.get(position).getHumidityMin() || checkHumidity > Conditions.conditionsList.get(position).getHumidityMax()) {
                return 2;
            }
            else if (checkHumidity < Conditions.conditionsList.get(position).getHumidityLow() || checkHumidity > Conditions.conditionsList.get(position).getHumidityHigh()) {
                return 1;
            }
            else {
                return 0;
            }
        }
        return 2;
    }

    public int checkPressureColor(float checkPressure, int position) throws IndexOutOfBoundsException {

        if (position < Conditions.conditionsList.size()) {
            if (checkPressure < Conditions.conditionsList.get(position).getPressureMin() || checkPressure > Conditions.conditionsList.get(position).getPressureMax()) {
                return 2;
            }
            else if (checkPressure < Conditions.conditionsList.get(position).getPressureLow() || checkPressure > Conditions.conditionsList.get(position).getPressureHigh()) {
                return 1;
            }
            else {
                return 0;
            }
        }
        return 2;
    }

}
