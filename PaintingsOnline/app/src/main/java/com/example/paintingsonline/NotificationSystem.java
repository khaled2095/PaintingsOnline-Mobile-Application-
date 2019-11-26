package com.example.paintingsonline;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class NotificationSystem extends Application
{
    public static final String CHANNEL_1_ID = "channel1";

    @Override
    public void onCreate()
    {
        super.onCreate();

        CreateNotificationChannel();
    }

    public void CreateNotificationChannel()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setDescription("Order Status Updated");
            notificationChannel.enableVibration(true);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }
}
