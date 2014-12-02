package com.accelkey.algorythm;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import java.util.LinkedList;


public class KeyInstance extends LinkedList<Position> {

    private float[] rotationMatrix = new float[16];
    private float[] accelData = new float[3];
    private float[] magnetData = new float[3];
    private float[] orientationData = new float[3];

    private SensorManager sm;

    public KeyInstance(SensorManager _sm) {
        this.sm = _sm;
    }

    void loadNewSensorData(SensorEvent event) {

        final int type = event.sensor.getType();

        if (type == Sensor.TYPE_ACCELEROMETER)
            accelData = event.values.clone();

        if (type == Sensor.TYPE_MAGNETIC_FIELD)
            magnetData = event.values.clone();

        sm.getRotationMatrix(rotationMatrix, null, accelData,
                magnetData);
        sm.getOrientation(rotationMatrix, orientationData);


    }
}
