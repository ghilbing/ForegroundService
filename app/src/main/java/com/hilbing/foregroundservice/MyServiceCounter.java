package com.hilbing.foregroundservice;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.amirarcane.lockscreen.activity.EnterPinActivity;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static com.hilbing.foregroundservice.App.CHANNEL_ID;
import static com.hilbing.foregroundservice.App.CHANNEL_ID_COUNT_DOWN;

public class MyServiceCounter extends Service {

    public static final String TAG = MyServiceCounter.class.getSimpleName();
    public static final String TOAST_MESSAGE = "toast_message";

    private NotificationManagerCompat notificationManagerCompat;


    private CountDownTimer mCountDownTimer;
    private static final long START_TIME_IN_MILLIS = 10000;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    public MyServiceCounter() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
    }

    void createNotification(){

        Intent activityIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0, activityIntent, 0);

        Intent broadcastIntent = new Intent(getApplicationContext(), NotificationReceiver.class);
        broadcastIntent.putExtra(TOAST_MESSAGE, "HELLO DEAR FRIENDS, JUST TESTING");

        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID_COUNT_DOWN)
                .setSmallIcon(R.drawable.ic_timer)
                .setContentTitle(getResources().getString(R.string.arkanhelp_about_to_send_messages))
                .setContentText(getResources().getString(R.string.deactivate_messages))
                .setLights(1, 2000, 1000)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setColor(Color.RED)
                .addAction(R.drawable.ic_timer, getResources().getString(R.string.deactivate_counter), makePendingIntent(getResources().getString(R.string.deactivate)))
                .build();
        notificationManagerCompat.notify(2, notification);

    }

    public PendingIntent makePendingIntent(String name){
        Intent intent = new Intent(getApplicationContext(), EnterPinActivity.class);
        intent.setAction(name);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        return pendingIntent;
    }

    void startTimer(){
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long l) {
                mTimeLeftInMillis = l;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                Toast.makeText(getApplicationContext(), "Finished!!!! Let's send help messages!!!", Toast.LENGTH_SHORT).show();
                
            }
        }.start();
        mTimerRunning = true;



    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
        Log.i(TAG, timeLeftFormatted);
    }

    void cancelTimer(){
        mCountDownTimer.cancel();
        mTimerRunning = false;
        Log.i(TAG, "cancelTimer: Cancelled");
        Toast.makeText(getApplicationContext(), "CountDown Cancelled !!!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(mTimerRunning){
            cancelTimer();

        } else {
            startTimer();
            createNotification();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelTimer();

    }
}