package com.accelkey;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.accelkey.algorythm.StateListener;

public class AccelKey extends Activity {

    StateListener sl;
    private static int click = 0;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        sl = new StateListener(this);
        setContentView(R.layout.main);
        Button button = (Button) findViewById(R.id.start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click == 4)
                    click = 0;
                sl.listenStartButtonClicks(++click);
            }
        });

    }

}