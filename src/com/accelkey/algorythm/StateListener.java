package com.accelkey.algorythm;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.accelkey.AccelKey;
import com.accelkey.R;

import javax.xml.transform.Source;
import java.security.Key;
import java.util.LinkedList;

public class StateListener implements SensorEventListener {
    private SensorManager sensorManager;

    private KeyInstance originKey;
    private KeyInstance testKey;
    private LinkedList<Integer> originDelta;
    private LinkedList<Integer> testDelta;
    private int state = 0;

    private Activity activity;

    public StateListener(AccelKey accelKey) {
        activity = accelKey;
        sensorManager = (SensorManager) accelKey.getSystemService(Context.SENSOR_SERVICE);
        originKey = new KeyInstance(sensorManager);
        testKey = new KeyInstance(sensorManager);
    }

    public void listenStartButtonClicks(int click) {
        Button start = (Button) activity.findViewById(R.id.start);
        switch (click) {
            case 1:
                System.out.println("was click");
                start.setText("Остановить");
                originKey.clear();
                setState(1);
                writeKey();
                break;
            case 2:
                System.out.println("was click");
                start.setText("Повторить ввод");
                stopWriting();
                break;
            case 3:
                System.out.println("was click");
                start.setText("Остановить");
                testKey.clear();
                setState(2);
                writeKey();
                break;
            case 4:
                System.out.println("was click");
                stopWriting();
                simplifyKeys();
                buildDelta();
                printDelta();
                break;
        }
    }

    private void buildDelta() {
        originDelta = originKey.getDelta();
        testDelta = testKey.getDelta();
    }

    private void simplifyKeys() {
        for(KeyInstance key : new KeyInstance[] {originKey, testKey}) {
            key.simplify();
        }
    }

    public void writeKey() {
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_UI);
    }

    public void stopWriting() {
        if(originKey.getTimer() != null && originKey.isTimerStarted())
            originKey.timerStop();
        if(testKey.getTimer() != null && testKey.isTimerStarted())
            testKey.timerStop();

        sensorManager.unregisterListener(this);
    }

    public void onSensorChanged(SensorEvent event) {

        if(state == 1) {
            originKey.loadNewSensorData(event);
        } else if (state == 2) {
            testKey.loadNewSensorData(event);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void setState(int state) {
        this.state = state;
    }

    public KeyInstance getOriginKey() {
        return originKey;
    }

    public KeyInstance getTestKey() {
        return testKey;
    }

    public void printKeys() {
        for(KeyInstance key : new KeyInstance[]{originKey, testKey}) {
            System.out.println("/* ----------------- Start key ----------------- */");
            System.out.println("Size of key is: " + key.size());
            for(Position pos : key) {
                pos.print();
            }
            System.out.println("/* ----------------- End key ----------------- */");
        }
    }

    public void printDelta() {
        for(LinkedList<Integer> key : new LinkedList[]{originDelta, testDelta}) {
            System.out.println("/* ----------------- START DELTA ----------------- */");
            System.out.println("Size of delta is: " + key.size());
            for(Integer area : key) {
                System.out.print(area + "  ");
            }
            System.out.println("/* ----------------- END DELTA ----------------- */");
        }
    }

}
