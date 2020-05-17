package com.example.notification;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class Music extends Service {

    MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mediaPlayer = MediaPlayer.create(this,R.raw.sound);
        mediaPlayer.start();

        if (intent.getAction()=="ShutDown"){
            Log.d("turn off","turn off");
        }

        Log.d("Music","toi chay service");
        return super.onStartCommand(intent, flags, startId);
    }

}
