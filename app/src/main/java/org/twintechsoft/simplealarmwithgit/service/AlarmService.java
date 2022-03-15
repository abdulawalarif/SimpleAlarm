package org.twintechsoft.simplealarmwithgit.service;


import static android.app.Notification.VISIBILITY_PUBLIC;
import static org.twintechsoft.simplealarmwithgit.application.App.CHANNEL_ID;
import static org.twintechsoft.simplealarmwithgit.broadcastreceiver.AlarmBroadcastReceiver.CATEGORY;
import static org.twintechsoft.simplealarmwithgit.broadcastreceiver.AlarmBroadcastReceiver.DATE;
import static org.twintechsoft.simplealarmwithgit.broadcastreceiver.AlarmBroadcastReceiver.TITLE;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import org.twintechsoft.simplealarmwithgit.R;
import org.twintechsoft.simplealarmwithgit.activities.RingActivity;


public class AlarmService extends Service {
    private MediaPlayer mediaPlayer;

    private Vibrator vibrator;


    @Override
    public void onCreate() {
        super.onCreate();
       SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String id = sharedPreferences.getString("key","");
       // Toast.makeText(this, id, Toast.LENGTH_LONG).show();
        if(id.equals("0")){
            mediaPlayer = MediaPlayer.create(this, R.raw.sound_one);
        }
        else if(id.equals("1")){
            mediaPlayer = MediaPlayer.create(this, R.raw.sound_two);
        }
        else if(id.equals("2")){
            mediaPlayer = MediaPlayer.create(this, R.raw.sound_three);
        }
        else if(id.equals("3")){
            mediaPlayer = MediaPlayer.create(this, R.raw.sound_four);
        }
        else if(id.equals("4")){
            mediaPlayer = MediaPlayer.create(this, R.raw.sound_five);
        }
        else if(id.equals("5")){
            mediaPlayer = MediaPlayer.create(this, R.raw.sound_six);
        }
        else {
            mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
        }

        mediaPlayer.setLooping(true);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent notificationIntent = new Intent(this, RingActivity.class);

        //Implementing data sender for Ring activity
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.edit().putString(CATEGORY, intent.getStringExtra(CATEGORY)).apply();
        sharedPref.edit().putString(TITLE, intent.getStringExtra(TITLE)).apply();
        sharedPref.edit().putString(DATE, intent.getStringExtra(DATE)).apply();

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        String alarmTitle = String.format("%s Alarm", intent.getStringExtra(TITLE));

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(alarmTitle)
                .setContentText(intent.getStringExtra(CATEGORY))
                .setSmallIcon(R.drawable.ic_alarm_black_24dp)
                .setContentIntent(pendingIntent)
                .build();

        mediaPlayer.start();

        long[] pattern = { 0, 100, 1000 };
        vibrator.vibrate(pattern, 0);
        startForeground(1, notification);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mediaPlayer.stop();
        vibrator.cancel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
