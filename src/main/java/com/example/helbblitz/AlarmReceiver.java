package com.example.helbblitz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        new NotificationClass().sendReminderNotification(context); // Ma classe notif
    }
}