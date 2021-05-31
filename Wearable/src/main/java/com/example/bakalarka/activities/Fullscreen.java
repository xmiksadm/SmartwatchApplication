package com.example.bakalarka.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.bakalarka.R;

/**
 * When user clicks on item in selected room, show it on fullscreen
 * Data is passed as Intent from previous activity
 */
public class Fullscreen extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreen);
        String data = getIntent().getStringExtra("data");
        TextView text = findViewById(R.id.data);
        text.setText(data);
    }
}
