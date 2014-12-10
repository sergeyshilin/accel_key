package com.accelkey.algorythm;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.accelkey.Unlock;
import com.accelkey.UpdateKey;
import com.accelkey.R;

import java.util.LinkedList;

public class StateListener implements SensorEventListener {
    private SensorManager sensorManager;

    private KeyInstance originKey;
    private KeyInstance testKey;
    private LinkedList<Integer> originDelta;
    private LinkedList<Integer> testDelta;
    private int state = 0;

    private Activity activity;

    public StateListener(UpdateKey accelKey) {
        activity = accelKey;
        sensorManager = (SensorManager) accelKey.getSystemService(Context.SENSOR_SERVICE);
        originKey = new KeyInstance(sensorManager);
        testKey = new KeyInstance(sensorManager);
    }

    public StateListener(Unlock unlock) {
        activity = unlock;
        originKey = null;
        testKey = new KeyInstance(sensorManager);
    }

    public void listenStartButtonClicks(int click) {
        Button start = (Button) activity.findViewById(R.id.start);
        switch (click) {
            case 1:
                start.setText("Остановить");
                originKey.clear();
                setState(1);
                writeKey();
                break;
            case 2:
                start.setText("Повторить ввод");
                stopWriting();
                break;
            case 3:
                start.setText("Остановить");
                testKey.clear();
                setState(2);
                writeKey();
                break;
            case 4:
                stopWriting();
                processKeys();
                break;
        }
    }

    public void listenUnlockButtonClicks(int click) {
        Button unlock = (Button) activity.findViewById(R.id.unlock);
        switch (click) {
            case 1:
                unlock.setText("Остановить");
                originKey.clear();
                setState(2);
                writeKey();
                break;
            case 2:
                unlock.setVisibility(View.INVISIBLE);
                stopWriting();
                processKeys();
                break;
        }
    }

    private void processKeys() {
        simplifyKeys();
        buildDelta();
        compareDeltas();
    }

    private void compareDeltas() {
        if(originKey.isEmpty()) {
            String originKey, testKey;
            SharedPreferences sharedPref = this.activity.getPreferences(Context.MODE_PRIVATE);
            originKey = sharedPref.getString("key", "SOMEWRONGDATA");

            StringBuffer test = new StringBuffer();
            for(Integer s : testDelta){
                test.append(s.toString());
                test.append(",");
            }

            if(test.toString().equals(originKey)) {
                activity.finish();
            } else {
                ((TextView) activity.findViewById(R.id.info)).setText("Неверно. Попробуйте еще");
                Button unlock = (Button) activity.findViewById(R.id.unlock);
                unlock.setVisibility(View.VISIBLE);
                unlock.setText("Ввести ключ");
            }
        } else {
            if (originDelta.equals(testDelta)) {
                System.out.println("EQUALS");
                writeKeyToStorage();
                Button close = (Button) activity.findViewById(R.id.start);
                close.setText("Закрыть");
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.finish();
                    }
                });

                ((TextView) activity.findViewById(R.id.info)).setText("Ключ обновлен");
            } else {
                System.out.println("DIFFERENT");
            }
        }
    }

    private void writeKeyToStorage() {
        StringBuilder csvList = new StringBuilder();
        for(Integer s : originDelta){
            csvList.append(s.toString());
            csvList.append(",");
        }

        SharedPreferences sharedPref = this.activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("key", csvList.toString());
        editor.commit();
    }

    private void buildDelta() {
        if(!originKey.isEmpty()) originDelta = originKey.getDelta();
        if(!testKey.isEmpty()) testDelta = testKey.getDelta();
    }

    private void simplifyKeys() {
        for(KeyInstance key : new KeyInstance[] {originKey, testKey}) {
            if(!key.isEmpty()) key.simplify();
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
