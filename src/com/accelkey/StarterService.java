package com.accelkey;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class StarterService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
    }

    /**
     * The started service opens the Activity.
     */
    @Override
    public void onStart(Intent intent, int startid) {
        Intent intents = new Intent(getBaseContext(), Unlock.class);
        intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intents);
    }
}