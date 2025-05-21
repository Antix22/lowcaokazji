package com.example.lowcaokazji.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.lowcaokazji.R;

import java.util.Random;

public class NotificationHelper {

    private static final String CHANNEL_ID = "deals";
    private static final String CHANNEL_NAME = "Deals";

    public static void showDealNotification(Context ctx, String title, String msg) {
        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification) // Dodaj swoją ikonę do drawable
                .setContentTitle(title)
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        int notificationId = new Random().nextInt(1_000_000); // zawsze >= 0
        notificationManager.notify(notificationId, builder.build());
    }
}