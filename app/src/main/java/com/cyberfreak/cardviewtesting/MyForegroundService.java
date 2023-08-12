package com.cyberfreak.cardviewtesting;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.IOException;

public class MyForegroundService extends Service {
    private MyHTTPD server;
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "ForegroundServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        // Start the NanoHTTPD server here   ANy Logic or Code to run in Background
        server = new MyHTTPD(8080,this);
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Perform your foreground service logic here

        // Create the notification channel (required for Android 8.0 and above)
        createNotificationChannel();

        // Create the notification for the foreground service
        Notification notification = buildNotification();

        // Start the service in the foreground with the provided notification
        startForeground(NOTIFICATION_ID, notification);

        // Return START_STICKY to ensure the service is restarted if it's killed by the system
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (server != null) {
            server.stop();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private Notification buildNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText("Running in the background")
                .setSmallIcon(R.drawable.ic_launcher_background);

        // Customize the notification as per your requirements

        return builder.build();
    }
}
