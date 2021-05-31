package com.example.bakalarka.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bakalarka.R;
import com.example.bakalarka.data.room.RoomController;
import com.example.bakalarka.data.sensor.GasensSensor;
import com.example.bakalarka.data.room.Room;
import com.example.bakalarka.data.qrCodeScan.QRCodeScanner;
import com.example.bakalarka.activities.overview.OverviewAllRoomsActivity;

public class AddSensor  extends AppCompatActivity {

    Button submitButton, scanButton;
    EditText id/*url, port, username, password*/;
    RoomController roomController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sensor);

        this.roomController = new RoomController();

        submitButton = findViewById(R.id.submit);
        id = findViewById(R.id.gasens_id);
        /*
        url = findViewById(R.id.url);
        port = findViewById(R.id.port);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
         */

        scanButton = findViewById(R.id.scan_btn);

        submitButton.setOnClickListener(v -> {
            int roomId = Integer.parseInt(id.getText().toString());
            GasensSensor gasensSensor = new GasensSensor(roomId /*url.getText().toString(), port.getText().toString(), username.getText().toString(), password.getText().toString()*/);
            Room room = new Room(gasensSensor.getId());
            roomController.addRoom(room);
            roomController.getRoomDatabaseController().saveWholeRoomInDB(roomController.getRoomById(roomId));

            Intent intent = new Intent(getApplicationContext(), OverviewAllRoomsActivity.class);
            startActivity(intent);
            finish();
        });

        scanButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), QRCodeScanner.class);
            startActivity(intent);

        });
    }
}
