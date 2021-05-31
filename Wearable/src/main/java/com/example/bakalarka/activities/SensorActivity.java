package com.example.bakalarka.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import com.example.bakalarka.R;

import com.example.bakalarka.service.SensorBackgroundService;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SensorActivity extends Activity {

    private static final String TAG = "SensorActivity";
    private Button mStart;
    private Button mStop;

    private ArrayList<String> roomsList;
    private Spinner dropdown;
    private String selected;
    private DatabaseReference rooms;
    private DatabaseReference root;
    ArrayList<String> data;

    /**
     * Saving preferences, for saving app state after reopening
     */
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        mStart = findViewById(R.id.start);
        mStop = findViewById(R.id.stop);

        mStart.setOnClickListener(clickButtonStart);
        mStop.setOnClickListener(clickButtonStop);

        dropdown = findViewById(R.id.spinner1);
        roomsList = new ArrayList<>();
        root = FirebaseDatabase.getInstance().getReference();
        rooms = root.child("rooms");

        data = new ArrayList<>();
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String spinnerValue = mPreferences.getString("selected","-1");
        boolean dropdownClicakble = mPreferences.getBoolean("dropdown", true);
        boolean startClickable = mPreferences.getBoolean("start",true);
        boolean stopClicakble = mPreferences.getBoolean("stop", false);
        if (!spinnerValue.equals("-1")) {
            //check for saved preference, if exists add to this arraylist, set spinner
            data.add(spinnerValue);
            dropdown.setSelection(Integer.parseInt(spinnerValue));
            dropdown.setEnabled(dropdownClicakble);
            mStart.setEnabled(startClickable);
            mStop.setEnabled(stopClicakble);
        }

        spinnerData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // save preferences for this activity
        SharedPreferences.Editor mEditor = mPreferences.edit();
        mEditor.putString("selected", selected);
        if (mStart.isEnabled()) {
            mEditor.putBoolean("dropdown", true);
            mEditor.putBoolean("start", true);
            mEditor.putBoolean("stop", false);
        }
        else {
            mEditor.putBoolean("dropdown", false);
            mEditor.putBoolean("start", false);
            mEditor.putBoolean("stop", true);
        }
        Log.d(TAG, "onStop: Saved: " + selected);
        mEditor.apply();
    }

    /**
     * Receives data from firebase, and passes it to showDataInSpinner method
     */
    private void spinnerData() {

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.e("Spinner Data", "Spinner data is changed!");

                for (DataSnapshot lists : snapshot.getChildren()){

                    // check if specific id is already connected and watch is monitoring, if not add to list
                    if (String.valueOf(lists.child("watchOn").getValue()).equals("false")
                            || !String.valueOf(lists.child("watchOn").getValue()).equals("true")) {

                        if (!data.contains(lists.getKey()))
                            data.add(lists.getKey()); //("Room ID " + lists.getKey() + " " + lists.child("patientName").getValue());
                    }
                }
                showDataInSpinner(data);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //Log.d("Changed", "Child changed: " + snapshot + " Previous name: " + previousChildName);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Log.d("Removed", "Child removed: " + snapshot);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("Moved", "Previous child: " + previousChildName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", "Failed to read value", error.toException());
            }
        });
    }

    /**
     * Populates dropdown list with all Room ids from firebase
     * Also Listens for selected item
     * @param data Room ids from firebase
     */
    public void showDataInSpinner(ArrayList<String> data) {

        roomsList = data;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, R.layout.spinner_item, roomsList); //Create the Adapter to describe how the items are displayed

        adapter.setDropDownViewResource(R.layout.spinner_textview_align); //Set the layout resource to create the drop down views.
        dropdown.setAdapter(adapter); //Set the data to your spinner

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                // On selecting a spinner item
                selected = adapterView.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast toast = Toast.makeText(adapterView.getContext(), "Please select room", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }

    /**
     * If this is true, do not start the service
     * @return true if device is on charger
     */
    public boolean isPlugged() {
        // Intent to check the actions on battery
        boolean isPlugged;
        Intent batteryStatus = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        // isPlugged if true indicates charging is ongoing and vice-versa
        int plugged = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        isPlugged = plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB;
        isPlugged = isPlugged || plugged == BatteryManager.BATTERY_PLUGGED_WIRELESS;

        return isPlugged;
    }

    /**
     * If wifi is turned on and connected to access point, start background service of monitoring
     */
    private final View.OnClickListener clickButtonStart = v -> {

        Toast toast;

        /*if (isPlugged()) {
            toast = Toast.makeText(this, "Device is charging", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            System.out.println("Device is charging");
        }*/

        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiMgr.isWifiEnabled()) { //&& !isPlugged()) { // Wi-Fi adapter is ON and device is not charging

            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

            // If Not connected to an access point
            if( wifiInfo.getNetworkId() == -1 ) {
                toast = Toast.makeText(this, "Not connected to an access point", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            // Else Connected to an access point, start background service of monitoring
            // First disable dropdown
            dropdown.setEnabled(false);
            mStart.setEnabled(false);
            mStop.setEnabled(true);

            Intent serviceIntent = new Intent(new Intent(SensorActivity.this, SensorBackgroundService.class));
            serviceIntent.putExtra("selected", selected);
            startService(serviceIntent);
        }
        // Wi-Fi adapter is OFF
        else {
            toast = Toast.makeText(this, "Wi-Fi is OFF", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    };

    /**
     * If Stop button is clicked, stop background service of monitoring
     */
    private final View.OnClickListener clickButtonStop = v -> {
        dropdown.setEnabled(true);
        mStart.setEnabled(true);
        mStop.setEnabled(false);
        stopService(new Intent(SensorActivity.this, SensorBackgroundService.class));
    };
}