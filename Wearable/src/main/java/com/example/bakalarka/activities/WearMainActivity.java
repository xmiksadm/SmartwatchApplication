package com.example.bakalarka.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;
import androidx.wear.widget.WearableLinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;

import com.example.bakalarka.data.FirebaseData;
import com.example.bakalarka.service.Adapter;
import com.example.bakalarka.R;
import com.example.bakalarka.data.Conditions;
import com.example.bakalarka.data.Person;
import com.example.bakalarka.data.Room;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WearMainActivity extends FragmentActivity
        implements DataClient.OnDataChangedListener {

    private static final String TAG = "MainActivity";
    public static final String CONDITIONS = "/conditions";
    public static final String ROOM_DATA = "/data";
    public static final String PERSON_INFO = "/person";
    private static final String ROOM_NAME = "/room_name";
    private static final String IDS = "/ids";
    public static final String BASIC_DATA = "/basic_";
    private static final String FIREBASE = "/firebase_";

    /**
     * Maps For second activity
     */
    private Map<Integer, Person> personInfo;
    private Map<Integer, Room> roomData;
    private Map<Integer, Conditions> conditions;
    private Map<Integer, FirebaseData> firebaseData;
    private ArrayList<Integer> ids = new ArrayList<>();

    private WearableRecyclerView wearableRecyclerView;
    private Adapter adapter;
    private Button buttonSensors;

    /**
     * Saving preferences, for saving app state after reopening
     */
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        loadSharedPreferences();
    }

    /**
     * Check the shared preferences, or so saved data about person, room and conditions
     * and set accordingly to maps
     */
    private void loadSharedPreferences() {

        //removeSharedPreference();
        Gson gson = new Gson();

        // get to string present from our shared prefs if not present setting it as null.
        String jsonPerson = mPreferences.getString("personInfo", null);
        String jsonRoom = mPreferences.getString("roomData", null);
        String jsonConditions = mPreferences.getString("conditions", null);
        String jsonFirebaseData = mPreferences.getString("firebase", null);

        // get the type of our map
        Type typePerson = new TypeToken<HashMap<Integer, Person>>() {}.getType();
        Type typeRoom = new TypeToken<HashMap<Integer, Room>>() {}.getType();
        Type typeConditions = new TypeToken<HashMap<Integer, Conditions>>() {}.getType();
        Type typeFirebaseData = new TypeToken<HashMap<Integer, FirebaseData>>() {}.getType();

        // getting data from gson and saving it to our map
        personInfo = gson.fromJson(jsonPerson, typePerson);
        roomData = gson.fromJson(jsonRoom, typeRoom);
        conditions = gson.fromJson(jsonConditions, typeConditions);
        firebaseData = gson.fromJson(jsonFirebaseData, typeFirebaseData);

        // if the map is empty create new empty hash map
        if (personInfo == null) {
            personInfo = new HashMap<>();
        }
        if (roomData == null) {
            roomData = new HashMap<>();
        }
        if (conditions == null) {
            conditions = new HashMap<>();
        }
        if (firebaseData == null) {
            firebaseData = new HashMap<>();
        }

        createPatientsRecyclerView();
        Log.d(TAG, "onCreate: " + personInfo.toString());
    }

    /**
     * Save data about person, room, and conditions in a Shared preferences
     * so the data is not lost after exiting application
     */
    private void saveSharedPreferences() {
        mEditor = mPreferences.edit();
        // creating a new variable for gson.
        Gson gson = new Gson();

        // getting data from gson and storing it in a string.
        String jsonPerson = gson.toJson(personInfo);
        String jsonRoom = gson.toJson(roomData);
        String jsonConditions = gson.toJson(conditions);
        String jsonFirebaseData = gson.toJson(firebaseData);

        // save data in shared prefs in the form of string.
        mEditor.putString("personInfo", jsonPerson);
        mEditor.putString("roomData", jsonRoom);
        mEditor.putString("conditions", jsonConditions);
        mEditor.putString("firebase", jsonFirebaseData);
        mEditor.apply();
        Log.d(TAG, "saveSharedPreferences: " + jsonPerson + jsonRoom + jsonConditions + jsonFirebaseData);
    }

    /**
     * Drops all saved preferences
     */
    public void removeSharedPreference() {
        checkForRemoved();
        mEditor = mPreferences.edit();
        mEditor.clear();
        mEditor.apply();
        //saveSharedPreferences();
    }

    /**
     * Method that checks if room is deleted in phone, and when user hit sync, it adds new ids to
     * array, and then looks if there are more ids in roomData map
     * If true, remove all Maps containing this id as key
     */
    public void checkForRemoved() {
        ArrayList<Integer> toRemove = new ArrayList<>();
        if (roomData != null) {
            for (Integer id : roomData.keySet()) {
                if (!ids.isEmpty() && !ids.contains(id)) {
                    toRemove.add(id);
                }
            }
        }
        for (Integer id : toRemove) {
            System.out.println("Removing: " + id);
            removeRoom(id);
        }
    }

    private void createPatientsRecyclerView() {
        setContentView(R.layout.activity_main);
        buttonSensors = findViewById(R.id.buttonSensors);
        wearableRecyclerView = findViewById(R.id.rvPatients);
        updateRecyclerView();
    }

    public void updateRecyclerView() {

        if (personInfo.size() > 0) {
            buttonSensors.setVisibility(View.INVISIBLE);
        }
        else
            buttonSensors.setOnClickListener(v -> openSensorActivity());

        // Aligns the first and last items on the list vertically centered on the screen.
        wearableRecyclerView.setEdgeItemsCenteringEnabled(true);
        adapter = new Adapter(this, personInfo, roomData, conditions, firebaseData);
        wearableRecyclerView.setAdapter(adapter);
        wearableRecyclerView.setLayoutManager(new WearableLinearLayoutManager(this));

        wearableRecyclerView.setHasFixedSize(true);
    }

    public void openSensorActivity() {
        Intent intent = new Intent(this, SensorActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Wearable.getDataClient(this).addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.getDataClient(this).removeListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveSharedPreferences();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //saveSharedPreferences();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                String path = event.getDataItem().getUri().getPath();
                if (IDS.equals(path)) {
                    DataItem item = event.getDataItem();
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    ids = dataMap.get("ids");
                    Log.d(TAG, "Ids received: " + dataMap);
                    removeSharedPreference();

                } else if (path.startsWith(BASIC_DATA)) {
                    DataItem item = event.getDataItem();
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    Log.d(TAG, "Basic data received: " + dataMap);

                    ids.add(dataMap.get("id"));
                    updateData(dataMap.get("id"), dataMap.get("room_name"), dataMap.get("patient_name"), dataMap.get("patient_age"));
                    updateConditions(dataMap.get("id"),
                            dataMap.get("temperature_min"), dataMap.get("temperature_low"), dataMap.get("temperature_high"), dataMap.get("temperature_max"),
                            dataMap.get("humidity_min"), dataMap.get("humidity_low"), dataMap.get("humidity_high"), dataMap.get("humidity_max"),
                            dataMap.get("pressure_min"), dataMap.get("pressure_low"), dataMap.get("pressure_high"), dataMap.get("pressure_max"),
                            dataMap.get("voc_min"), dataMap.get("voc_low"), dataMap.get("voc_high"), dataMap.get("voc_max"));

                    updateRoomData(dataMap.get("id"), dataMap.get("room_name"), dataMap.get("temperature"), dataMap.get("humidity"), dataMap.get("pressure"), dataMap.get("voc"));
                    updateFirebaseData(dataMap.get("id"), dataMap.get("watchOn"), dataMap.get("heartRate"), dataMap.get("fallDetected"));

                } else if (CONDITIONS.equals(path)) {
                    DataItem item = event.getDataItem();
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    Log.d(TAG, "Conditions received: " + dataMap.toString());

                    updateConditions(dataMap.get("id"),
                            dataMap.get("temperature_min"), dataMap.get("temperature_low"), dataMap.get("temperature_high"), dataMap.get("temperature_max"),
                            dataMap.get("humidity_min"), dataMap.get("humidity_low"), dataMap.get("humidity_high"), dataMap.get("humidity_max"),
                            dataMap.get("pressure_min"), dataMap.get("pressure_low"), dataMap.get("pressure_high"), dataMap.get("pressure_max"),
                            dataMap.get("voc_min"), dataMap.get("voc_low"), dataMap.get("voc_high"), dataMap.get("voc_max"));

                } else if (ROOM_DATA.equals(path)) {
                    DataItem item = event.getDataItem();
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    Log.d(TAG, "Room data received: " + dataMap.toString());

                    updateRoomData(dataMap.get("id"), dataMap.get("room_name"), dataMap.get("temperature"), dataMap.get("humidity"), dataMap.get("pressure"), dataMap.get("voc"));

                } else if (PERSON_INFO.equals(path)) {
                    DataItem item = event.getDataItem();
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    Log.d(TAG, "Person info received: " + "\n" + dataMap.toString());

                    updatePersonInfo(dataMap.get("id"), dataMap.get("patient_name"), dataMap.get("patient_age"));

                } else if (ROOM_NAME.equals(path)) {
                    DataItem item = event.getDataItem();
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    Log.d(TAG, "New room name received: " + "\n" + dataMap.toString());

                    updateRoomName(dataMap.get("id"), dataMap.get("room_name"));

                } else if (path.startsWith(FIREBASE)) {
                    DataItem item = event.getDataItem();
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    Log.d(TAG, "Firebase data received: " + "\n" + dataMap.toString());

                    updateFirebaseData(dataMap.get("id"), dataMap.get("watchOn"), dataMap.get("heartRate"), dataMap.get("fallDetected"));

                } else {
                    Log.d(TAG, "Unrecognized path: " + path);
                }

                updateRecyclerView();

            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                DataItem item = event.getDataItem();
                DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                Log.d(TAG, "DataItem Deleted: " + event.getDataItem().toString());

                removeRoom(dataMap.get("id"));
                updateRecyclerView();

            } else {
                Log.d(TAG, "Unknown data event type. Type = " + event.getType());
            }
        }
        checkForRemoved();
    }

    public void updateData(int id, String roomName, String patientName, int age) {
        Person person = new Person(id, patientName, age, roomName);
        personInfo.put(id, person);
    }

    public void updateRoomData(int id, String roomName, float temperature, float humidity, float pressure, float voc) {
        if (personInfo.size() > 0) {
            Room room = new Room(id, roomName, temperature, humidity, pressure, voc);
            roomData.put(id, room);
        }
    }

    public void updatePersonInfo(int id, String patientName, int age) {
        Person oldPerson = personInfo.get(id);
        Person newPerson = new Person(id, patientName, age, oldPerson.getRoomName());
        personInfo.put(id, newPerson);
    }

    public void updateRoomName(int id, String roomName) {
        Person oldPerson = personInfo.get(id);
        Person newPerson = new Person(id, oldPerson.getName(), oldPerson.getAge(), roomName);
        personInfo.put(id, newPerson);
    }

    public void updateConditions(int id,
                                 float tempMin, float tempLow, float tempHigh, float tempMax,
                                 float humidityMin, float humidityLow, float humidityHigh, float humidityMax,
                                 float pressureMin, float pressureLow, float pressureHigh, float pressureMax,
                                 float vocMin, float vocLow, float vocHigh, float vocMax) {

        Conditions newConditions = new Conditions(id, tempMin, tempLow, tempHigh, tempMax,
                humidityMin, humidityLow, humidityHigh, humidityMax,
                pressureMin, pressureLow, pressureHigh, pressureMax,
                vocMin, vocLow, vocHigh, vocMax);
        conditions.put(id, newConditions);
    }

    public void updateFirebaseData(int id, boolean watchOn, int heartRate, String fallDetected) {
        FirebaseData newFirebaseData = new FirebaseData(id, watchOn, heartRate, fallDetected);
        firebaseData.put(id, newFirebaseData);
    }

    public void removeRoom(int id) {
        personInfo.remove(id);
        roomData.remove(id);
        conditions.remove(id);
        firebaseData.remove(id);
        removeSharedPreference();
        updateRecyclerView();
    }
}