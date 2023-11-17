package com.example.helbblitz;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;


public class NotificationClass {

    private static final String CHANNEL_ID = "channel_id";
    private static final int NOTIFICATION_ID = 1;

    public void sendReminderNotification(Context context) {
        try {
            createReminderNotification(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createReminderNotification(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Nom du canal", NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription("Description du canal");
                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }

            String reminderText = "N'oubliez pas de découvrir un nouveau jeu aujourd'hui !";

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notifications_signup)
                    .setContentTitle("Rappel")
                    .setContentText(reminderText)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
