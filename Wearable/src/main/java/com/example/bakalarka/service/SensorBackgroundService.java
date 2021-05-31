package com.example.bakalarka.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.bakalarka.R;
import com.example.bakalarka.activities.SensorActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import static androidx.core.app.NotificationCompat.PRIORITY_MIN;

public class SensorBackgroundService extends Service implements SensorEventListener {

    private static final String TAG = SensorBackgroundService.class.getSimpleName();
    private static final int NOTIFICATION_ID = 30;
    private static final int RECORDS = 30; // 30 * 0.2 = 6 seconds
    private static final double FALL_THRESHOLD = 1.3, SHOCK_THRESHOLD = 25;

    private boolean toastShown = false;
    private String selected;
    private DatabaseReference rooms;
    private DatabaseReference root;
    private DatabaseReference oneRoom;

    private SensorManager mSensorManager = null;
    private Sensor mHeartRateSensor;
    private Sensor mAccelerometer;
    double threshold;
    double[] lastRecords;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        selected = intent.getStringExtra("selected");
        Toast.makeText(this, "Service started. Selected = " + selected, Toast.LENGTH_LONG).show();
        Log.d(TAG, "onStartCommand: Selected = " + selected);
        root = FirebaseDatabase.getInstance().getReference();
        rooms = root.child("rooms");
        oneRoom = rooms.child(selected);
        lastRecords = new double[RECORDS];
        getCount();

        setNotification();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service destroyed by user.", Toast.LENGTH_LONG).show();
        stopCount();
    }

    private String currentTimeStr() {
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        return df.format(c.getTime());
    }

    private void getCount() {

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (mHeartRateSensor != null) {
            mSensorManager.registerListener(this, mHeartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mAccelerometer != null) {
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL); //1000000, 1000000);
        }

        if (!toastShown) {
            Toast toast = Toast.makeText(this, "Started monitoring: " + selected, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            oneRoom.child("watchOn").setValue("true");
            toastShown = true;
        }
    }

    private void stopCount() {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorManager.unregisterListener(this);

        if (toastShown) {
            Toast toast = Toast.makeText(this, "Stopped monitoring: " + selected, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            oneRoom.child("watchOn").setValue("false");
            toastShown = false;
        }
        stopSelf();
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(NotificationManager notificationManager){
        String channelId = "my_service_channelid";
        String channelName = "My Foreground Service";
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);

        channel.setImportance(NotificationManager.IMPORTANCE_NONE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        notificationManager.createNotificationChannel(channel);
        return channelId;
    }

    public void setNotification() {

        // Create the Foreground Service
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? createNotificationChannel(notificationManager) : "";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);

        Intent notificationIntent = new Intent(this, SensorActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent intent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = notificationBuilder
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher)
                .setColor(this.getResources().getColor(R.color.red_a200))
                .setContentTitle("niCE-life")
                .setContentText("Monitoring")
                .setPriority(PRIORITY_MIN)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setContentIntent(intent)
                .setWhen(System.currentTimeMillis())
                .build();

        // Builds the notification and issues it.
        notificationManager.notify(0, notification);

        startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, "Accuracy Changed : " + accuracy);
        if (sensor == mAccelerometer) {
            switch (accuracy) {
                case 0:
                    System.out.println(" Accelerometer Unreliable");
                    break;
                case 1:
                    System.out.println("Accelerometer Low Accuracy");
                    break;
                case 2:
                    System.out.println("Accelerometer Medium Accuracy");
                    break;
                case 3:
                    System.out.println("Accelerometer High Accuracy");
                    break;
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {

            if (event.accuracy == SensorManager.SENSOR_STATUS_NO_CONTACT ||
                    event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
                Log.d(TAG, "Heart Rate Monitor not in contact or unreliable");

            } else {
                // send heart rate data to firebase server every one minute
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    public void run() {
                        //Firebase
                        oneRoom.child("heartRate").setValue(event.values[0]);

                        Log.d(TAG, "Heart Rate: " + event.values[0]);

                        getCount();
                    }
                }, 60 * 1000, 60 * 1000); // once per minute
            }

        } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            double x = event.values[0];
            double y = event.values[1];
            double z = event.values[2];

            // If you want the magnitude of the total acceleration, you have to do vector addition.
            threshold = Math.sqrt(x*x + y*y + z*z);
            shiftRightAdd(threshold);

            if (fallDetected()) {
                Toast toast = Toast.makeText(this, "Fall detected", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            getCount();

        } else {
            Log.d(TAG, "Unknown sensor type");
        }
    }

    /**
     * @param newElement sensor value to be added to global array of
     *                   last 30 records (6 seconds) from accelerometer
     */
    public void shiftRightAdd(double newElement) {
        int i;

        /* shifting array elements */
        for(i = RECORDS-1; i > 0; i--) {
            lastRecords[i] = lastRecords[i-1];
        }
        lastRecords[0] = newElement;

        /*System.out.println("\nNew array after rotating by one position in the right direction");
        for(i = 0; i < RECORDS; i++) {
            System.out.print(lastRecords[i] + " ");
        }*/
    }

    /**
     * @return true if fall is detected
     */
    public boolean fallDetected() {
        boolean fall = false;
        boolean shock = false;
        boolean motionless = false;
        int indexFall = 0, indexShock = 0, indexMotionless = 0;
        double freeFall = 0, shockk = 0, motionlesss = 0;

        for (int i = 0; i < RECORDS-1; i++) {
            if (lastRecords[i] > 0 && lastRecords[i] < FALL_THRESHOLD) {
                fall = true;
                freeFall = lastRecords[i];
                indexFall = i;
            }
            else if (lastRecords[i] > SHOCK_THRESHOLD) {
                shock = true;
                shockk = lastRecords[i];
                indexShock = i;
            }
            else if (lastRecords[i] > 5 && lastRecords[i] < 10) {
                motionless = true;
                motionlesss = lastRecords[i];
                indexMotionless = i;
            }
        }

        if (fall && shock && motionless && indexFall > indexShock && indexShock > indexMotionless) {
            //Send data to Firebase
            oneRoom.child("fallDetected").setValue(currentTimeStr());

            // for testing purposes only, sending data to firebase in case of fall to see if it works correctly
            /*oneRoom.child("accelerometer").child("free fall").setValue(freeFall);
            oneRoom.child("accelerometer").child("shock").setValue(shockk);
            oneRoom.child("accelerometer").child("motionless").setValue(motionlesss);*/
            return true;
        }

        return false;
    }


    /**
     * Only used for unit test
     * @param lastRecords double[]
     */
    public void setLastRecords(double[] lastRecords) {
        this.lastRecords = lastRecords;
    }
}