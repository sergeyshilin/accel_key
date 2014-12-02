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
    private int check = 0;

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
        } else if (state == 2) {
            testKey.loadNewSensorData(event);
        } else {

        }


//        Position last;
//        if (state == 1){
//            if (originKey.isEmpty()) {
//                originKey.add(nowposition);
//                return;
//            }
//            last = originKey.getLast();
//        }else{
//            if (testKey.isEmpty()) {
//                last = new Position (1000, 1000, 1000);
//            }else{
//                last = testKey.getLast();
//            }
//        }
//        if (Math.abs(Math.toDegrees(orientationData[0]) - last.getXy()) < 90
//                && Math.abs(Math.toDegrees(orientationData[1]) - last.getXz()) < 90
//                && Math.abs(Math.toDegrees(orientationData[2]) - last.getYz()) < 90)
//            return;
//
//        if (state == 1)
//            originKey.add(nowposition);
//        else {
//            if (check == 0) {
//                if (originKey.getFirst().equals(nowposition)){
//                    testKey.add(nowposition);
//                    check = 1;
//                }
//            }else {
//                if (originKey.get(check).equals(nowposition)){
//                    check++;
//                    testKey.add(nowposition);
//                    if (originKey.size() == check) {
//                        state = 3;
//                        stopWritin();
//                    }
//                }
//                else{
//                    check = 0;
//                    testKey.clear();
//                }
//            }
//        }

        TextView t1 =(TextView) activity.findViewById(R.id.xyValue);
        t1.setText(String.valueOf(state));

        TextView t2 = (TextView) activity.findViewById(R.id.xzValue);
        t2.setText(String.valueOf(check));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void setState(int state) {
        this.state = state;
    }
}
