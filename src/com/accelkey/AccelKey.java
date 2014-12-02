package com.accelkey;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.accelkey.algorythm.StateListener;

public class AccelKey extends Activity {

    StateListener sl;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        sl = new StateListener(this);
        setContentView(R.layout.main);

    }

    public void onclick(View v) {
        Button start = (Button) findViewById(R.id.start);
        Button stop = (Button) findViewById(R.id.stop);
        switch (v.getId()) {
            case R.id.start:
                sl.setState(1);
                sl.writeKey();
                start.setVisibility(View.INVISIBLE);
                stop.setVisibility(View.VISIBLE);
                break;
            case R.id.stop:
                sl.setState(2);
                sl.writeKey();
                stop.setVisibility(View.INVISIBLE);
                break;
        }
    }

}