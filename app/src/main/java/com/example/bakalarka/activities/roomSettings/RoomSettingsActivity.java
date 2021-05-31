package com.example.bakalarka.activities.roomSettings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.example.bakalarka.R;
import com.example.bakalarka.activities.overview.OverviewAllRoomsActivity;
import com.example.bakalarka.data.room.RoomController;
import com.example.bakalarka.data.room.Room;
import com.example.bakalarka.data.room.person.Illness;
import com.example.bakalarka.activities.BaseActivity;
import com.example.bakalarka.data.wear.WearController;
import com.example.bakalarka.database.RoomDatabaseController;

public class RoomSettingsActivity extends BaseActivity {
    Button roomName, personInfo, newIllness, riskLimits, back;
    RoomController roomController;
    WearController wearController;
    boolean personChange;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_room);

        this.roomController = new RoomController();

        int roomId = getIntent().getIntExtra("roomId", 0);
        Room room = roomController.getRoomById(roomId);

        this.roomName = findViewById(R.id.set_room_name);
        this.personInfo= findViewById(R.id.set_person_info);
        this.newIllness = findViewById(R.id.add_illness);
        this.riskLimits = findViewById(R.id.set_risk_limits);
        this.back = findViewById(R.id.back_on_overview);

        this.personChange = false;

        this.wearController = new WearController();

        this.roomName.setOnClickListener(v -> {
            EditText roomNameEditText = findViewById(R.id.room_name_edit_text);
            String roomName = roomNameEditText.getText().toString();
            if(!roomName.matches("")){
                room.setRoomName(roomName);
                room.getNotification().setNotificationTitle(roomName);
                new Thread(() -> this.roomController.getRoomDatabaseController().updateRoomInDB(room)).start();
                wearController.sendRoomName(roomId, roomName);
                Intent intent = new Intent(getApplicationContext(), OverviewAllRoomsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        this.personInfo.setOnClickListener(v -> {
            EditText personNameEditText = findViewById(R.id.person_name_edit_text);
            EditText personAgeEditText = findViewById(R.id.person_age_edit_text);
            String personName = personNameEditText.getText().toString();
            String personAge = personAgeEditText.getText().toString();
            if(!personName.matches("")){
                room.getPerson().setName(personName);
                this.personChange = true;
            }
            if(!personAge.matches("")){
                room.getPerson().setAge(Integer.parseInt(personAge));
                this.personChange = true;
            }
            if (personChange){
                new Thread(() -> roomController.getRoomDatabaseController().updatePersonInDB(roomId, room.getPerson())).start();
                wearController.sendPatientData(roomId, room.getPerson());

                Intent intent = new Intent(getApplicationContext(), OverviewAllRoomsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        this.newIllness.setOnClickListener(v -> {
            EditText addIllnessEditText = findViewById(R.id.new_illness_edit_text);
            String illnessName = addIllnessEditText.getText().toString();
            if(!illnessName.matches("")){
                Illness illness = new Illness(illnessName);
                room.getPerson().getIllnesses().add(illness);
                RoomDatabaseController roomDatabaseController = new RoomDatabaseController();
                new Thread(() -> roomDatabaseController.saveIllnessInDB(roomDatabaseController.getPersonEntity(roomId).getId(), illness)).start();

                Intent intent = new Intent(getApplicationContext(), OverviewAllRoomsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        this.riskLimits.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RoomSetConditions.class);
            intent.putExtra("roomId", roomId);
            startActivity(intent);
        });
        this.back.setOnClickListener(v -> finish());
    }
}
