package com.example.bakalarka.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Button;

import com.example.bakalarka.R;
import com.example.bakalarka.data.room.RoomController;
import com.example.bakalarka.activities.overview.OverviewAllRoomsActivity;
import com.example.bakalarka.activities.overview.OverviewRiskyRoomsActivity;
import com.example.bakalarka.data.wear.WearFirebaseService;
import com.example.bakalarka.database.RoomDatabaseController;
import com.example.bakalarka.database.illness.IllnessDB;

public class MainActivity extends BaseActivity {

    //@SuppressLint("StaticFieldLeak")
    public static Context context;
    private static boolean start = true;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WearFirebaseService wearFirebaseService = new WearFirebaseService();
        wearFirebaseService.firebaseDataListener();

        Button allRoomsBtn = findViewById(R.id.all_rooms_btn);
        Button riskyRoomsBtn = findViewById(R.id.risky_rooms_btn);

        MainActivity.context = getApplicationContext();
        if (start){
            new Thread(() -> {
                Looper.prepare();
                (new RoomController()).addRooms((new RoomDatabaseController()).getRoomsFromDB());
            }).start();
            start = false;
        }

        allRoomsBtn.setOnClickListener(v -> {
            // Spustenie Prehľadu izieb
            Intent intent = new Intent(getApplicationContext(), OverviewAllRoomsActivity.class);
            startActivity(intent);
        });

        riskyRoomsBtn.setOnClickListener(v -> {
            // Spustenie Prehľadu izieb
            Intent intent = new Intent(getApplicationContext(), OverviewRiskyRoomsActivity.class);
            startActivity(intent);
        });

    }
}