package com.hilbing.foregroundservice;

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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import static com.hilbing.foregroundservice.App.CHANNEL_ID;

public class MyService extends Service implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor mAccelerometer;

    public static final String TAG = MyService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //String input = intent.getStringExtra("inputExtra");

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getResources().getString(R.string.arkanhelp))
                .setContentText(getResources().getString(R.string.service_running))
                .setSmallIcon(R.drawable.ic_notification_important)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(mAccelerometer != null){
            sensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            //  Log.d(TAG, "onSensorChanged: Registered accelerometer");
        } else {
           //    Log.d(TAG, "onSensorChanged: Accelerometer not supported");
        }

        //Sensor sensor = sensorEvent.sensor;
        // Log.d(TAG, "onSensorChanged: X " + sensorEvent.values[0] + " Y " + sensorEvent.values[1] + " Z " + sensorEvent.values[2]);
        double rootSquare = Math.sqrt(Math.pow(sensorEvent.values[0], 2) + Math.pow(sensorEvent.values[1], 2)
                + Math.pow(sensorEvent.values[2], 2));
        if(rootSquare < 2.0){
           // Toast.makeText(getApplicationContext(), "FALL DETECTED!!!!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), MyServiceCounter.class);
            startService(intent);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}