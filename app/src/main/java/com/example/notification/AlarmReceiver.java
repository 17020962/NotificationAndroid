package com.example.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class AlarmReceiver extends BroadcastReceiver  {

    private static final String CHANNEL_ID = "simlified_coding";
    private static final String CHANNEL_NAME = "Simlified_Coding";
    private static final String CHANNEL_DESC = "Simlified_Coding";
    public static MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == "NOTIFICATION") {
            mediaPlayer = MediaPlayer.create(context,R.raw.sound);
            mediaPlayer.start();

            Intent intentShutDown = new Intent(context, AlarmReceiver.class);
            intentShutDown.setAction("SHUTDOWN_NOTIFICATION");
            intentShutDown.putExtra("Turn Off", 0);
            PendingIntent pendingIntentShutDown =
                    PendingIntent.getBroadcast(context, 0, intentShutDown, 0);

            NotificationCompat.Builder mbuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Thu Cuối")
                    .setContentText("Thông báo đang phát một bài hát")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)
                    .addAction(R.drawable.ic_launcher_foreground, "Tắt",
                            pendingIntentShutDown)
                    .setWhen(System.currentTimeMillis() + 1000);

            NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(context);
            mbuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
            mNotificationMgr.notify(1, mbuilder.build());

        }

        if (intent.getAction() == "SHUTDOWN_NOTIFICATION") {
            try {
                mediaPlayer.stop();
            }
            catch (Exception e){

            }
        }
    }
}
