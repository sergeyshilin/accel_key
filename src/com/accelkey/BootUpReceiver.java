package com.accelkey;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootUpReceiver extends BroadcastReceiver {

    /**
     * Listens for Android's BOOT_COMPLETED broadcast and then executes
     * the onReceive() method.
     */
    @Override
    public void onReceive(Context context, Intent arg1) {
        Intent intent = new Intent(context, StarterService.class);
        context.startService(intent);
    }
}