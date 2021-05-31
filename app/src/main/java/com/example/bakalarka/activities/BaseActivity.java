package com.example.bakalarka.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bakalarka.R;
import com.example.bakalarka.activities.AddSensor;
import com.example.bakalarka.data.wear.WearController;

// Základná aktivita
public class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Vytvorenie menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Ovládanie menu
        int id = item.getItemId();

        if (id == R.id.add_sensor) {
            Intent intent = new Intent(getApplicationContext(), AddSensor.class);
            startActivity(intent);
        }else if (id == R.id.synchronize) {
            WearController wearController = new WearController();
            wearController.synchronize();
        }

        return super.onOptionsItemSelected(item);
    }
}
