package com.accelkey.algorythm;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;
import com.accelkey.AccelKey;
import com.accelkey.R;

public class StateListener implements SensorEventListener {
    private SensorManager sensorManager;

    private KeyInstance originKey;
    private KeyInstance testKey;
    private int state = 0;

    private Activity activity;

    public StateListener(AccelKey accelKey) {
        activity = accelKey;
        sensorManager = (SensorManager) accelKey.getSystemService(Context.SENSOR_SERVICE);
        originKey = new KeyInstance(sensorManager);
        testKey = new KeyInstance(sensorManager);
    }

    public void writeKey() {
        stopWriting();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_UI);
    }

    public void stopWriting() {
        sensorManager.unregisterListener(this);
    }

    public void onSensorChanged(SensorEvent event) {

        if(state == 1) {
            originKey.loadNewSensorData(event);
        }
//        else if (state == 2) {
//            testKey.loadNewSensorData(event);
//        }

        TextView t1 = (TextView) activity.findViewById(R.id.xyValue);
        t1.setText(String.valueOf(state));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void setState(int state) {
        this.state = state;
    }

    public void printKey() {
        System.out.println("Size of key is: " + originKey.size());
        for(Position pos : originKey) {
            pos.print();
        }
    }

    public KeyInstance getOriginKey() {
        return originKey;
    }
}
