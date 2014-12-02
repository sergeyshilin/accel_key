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

        SensorManager.getRotationMatrix(rotationMatrix, null, accelData,
                magnetData);
        SensorManager.getOrientation(rotationMatrix, orientationData);

        Position current = new Position(orientationData);
        boolean piRotation = (
                this.size() != 0 && this.getLast().moreThan(current, 90)
                || this.size() == 0)
                ? true : false;

        if(piRotation) {
//            Position p = (this.size() == 0) ? current : new Position(getDelta(orientationData));
            this.add(current);
        }

    }

    private Position getDelta(float[] orientationData) {
        Position last = getLast();
        Position current = new Position(orientationData);
        long xy = current.getXy() - last.getXy();
        long xz = current.getXz() - last.getXz();
        long yz = current.getYz() - last.getYz();
        return new Position(xy, xz, yz);
    }
}
