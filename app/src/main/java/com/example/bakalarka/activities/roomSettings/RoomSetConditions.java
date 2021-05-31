package com.example.bakalarka.activities.roomSettings;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bakalarka.R;
import com.example.bakalarka.data.risks.Conditions;
import com.example.bakalarka.data.room.RoomController;
import com.example.bakalarka.data.room.Room;
import com.example.bakalarka.activities.BaseActivity;
import com.example.bakalarka.data.wear.WearController;

public class RoomSetConditions extends BaseActivity {

    Button setConditions;
    RoomController roomController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_conditions);

        this.roomController = new RoomController();

        System.out.println(getIntent().getIntExtra("roomId", 0));
        int roomId = getIntent().getIntExtra("roomId", 0);
        Room room = roomController.getRoomById(roomId);

        EditText tmp_min, tmp_max, hum_min, hum_max, pres_min, pres_max, voc_min, voc_max;
        tmp_min = findViewById(R.id.temperature_condition_min);
        tmp_max = findViewById(R.id.temperature_condition_max);
        hum_min = findViewById(R.id.humidity_condition_min);
        hum_max = findViewById(R.id.humidity_condition_max);
        pres_min = findViewById(R.id.pressure_condition_min);
        pres_max = findViewById(R.id.pressure_condition_max);
        voc_min = findViewById(R.id.voc_condition_min);
        voc_max = findViewById(R.id.voc_condition_max);

        tmp_min.setText(room.getConditionsMap().get(Conditions.TEMPERATURE_CONDITIONS).getMin() + "");
        tmp_max.setText(room.getConditionsMap().get(Conditions.TEMPERATURE_CONDITIONS).getMax() + "");
        hum_min.setText(room.getConditionsMap().get(Conditions.HUMIDITY_CONDITIONS).getMin() + "");
        hum_max.setText(room.getConditionsMap().get(Conditions.HUMIDITY_CONDITIONS).getMax() + "");
        pres_min.setText(room.getConditionsMap().get(Conditions.PRESSURE_CONDITIONS).getMin() + "");
        pres_max.setText(room.getConditionsMap().get(Conditions.PRESSURE_CONDITIONS).getMax() + "");
        voc_min.setText(room.getConditionsMap().get(Conditions.VOC_CONDITIONS).getMin() + "");
        voc_max.setText(room.getConditionsMap().get(Conditions.VOC_CONDITIONS).getMax() + "");

        this.setConditions = findViewById(R.id.set_conditions);

        this.setConditions.setOnClickListener(v -> {
            boolean change = false;

            WearController wearController = new WearController();

            String value_min = tmp_min.getText().toString();
            String value_max = tmp_max.getText().toString();
            if(setCondition(value_min, value_max, room, Conditions.TEMPERATURE_CONDITIONS)){
                setOtherConditions(Conditions.TEMPERATURE_CONDITIONS, room);
                change = true;
            }
            value_min = hum_min.getText().toString();
            value_max = hum_max.getText().toString();
            if(setCondition(value_min, value_max, room, Conditions.HUMIDITY_CONDITIONS)){
                setOtherConditions(Conditions.HUMIDITY_CONDITIONS, room);
                change = true;
            }
            value_min = pres_min.getText().toString();
            value_max = pres_max.getText().toString();
            if(setCondition(value_min, value_max, room, Conditions.PRESSURE_CONDITIONS)){
                setOtherConditions(Conditions.PRESSURE_CONDITIONS, room);
                change = true;
            }
            value_min = voc_min.getText().toString();
            value_max = voc_max.getText().toString();
            if(setCondition(value_min, value_max, room, Conditions.VOC_CONDITIONS)){
                setOtherConditions(Conditions.VOC_CONDITIONS, room);
                change = true;
            }
            if (change){

                new Thread(() -> roomController.getRoomDatabaseController().updateConditionsInDB(roomId, room.getConditionsMap())).start();
                wearController.sendConditions(roomId, room.getConditionsMap());
            }
            finish();
        });
    }

    private boolean setCondition(@NonNull String value_min, @NonNull String value_max, @NonNull Room room, String condition){
        if (value_min.matches("") && value_max.matches("")){
            return false;
        }
        if(!value_min.matches("")){
            room.getConditionsMap().get(condition).setMin(Float.parseFloat(value_min));
        }
        if(!value_max.matches("")){
            room.getConditionsMap().get(condition).setMax(Float.parseFloat(value_max));
        }
        return true;
    }

    public void setOtherConditions(String condition, @NonNull Room room){
        float min = room.getConditionsMap().get(condition).getMin();
        float max = room.getConditionsMap().get(condition).getMax();
        float low = min + (max-min)/8;
        float ideal = min + (max-min)/2;
        float high = max - (max-min)/8;
        room.getConditionsMap().get(condition).setLow(low);
        room.getConditionsMap().get(condition).setIdeal(ideal);
        room.getConditionsMap().get(condition).setHigh(high);
    }
}
