package com.accelkey;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import com.accelkey.algorythm.StateListener;

public class Unlock extends Activity {

    StateListener sl;
    private static int click = 0;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        super.onCreate(savedInstanceState);
        sl = new StateListener(this);
        setContentView(R.layout.unlock);
        Button button = (Button) findViewById(R.id.unlock);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click == 2)
                    click = 0;
                sl.listenUnlockButtonClicks(++click);
            }
        });

    }

}