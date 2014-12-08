package com.accelkey.algorythm;

import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import javax.xml.transform.Source;
import java.security.Key;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;


public class KeyInstance extends LinkedList<Position> {

    private float[] rotationMatrix = new float[16];
    private float[] accelData = new float[3];
    private float[] magnetData = new float[3];
    private float[] orientationData = new float[3];
    private Timer timer;
    private boolean isTimerStarted = false;
    private KeyInstance me;

    private SensorManager sm;
    private boolean timerStarted;

    public KeyInstance(SensorManager _sm) {

        this.sm = _sm;
        this.timer = new Timer();
        this.me = this;
    }

    public KeyInstance() {
        super();
    }

    public void timerStop(){
        this.timer.cancel();
        isTimerStarted = !isTimerStarted;
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

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                isTimerStarted = !isTimerStarted;
                me.add(new Position(orientationData));
                try {
                    Thread.sleep(Utils.INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 20, Utils.INTERVAL);

    }

    public Timer getTimer() {
        return timer;
    }

    public boolean isTimerStarted() {
        return timerStarted;
    }

    public void simplify() {
        double similarityPercent = 0.97;
        for(int i = this.size() - 1; i > 0; i--) {
            if(this.get(i).equals(this.get(i-1), similarityPercent))
                this.remove(i);
        }
    }

    public LinkedList<Integer> getDelta() {
        LinkedList<Integer> delta = new LinkedList<>();

        for(int i = 1; i < this.size(); i++) {
            Integer area = Utils.getDeltaArea(this.get(i).minus(this.get(i - 1)));
            if(!delta.isEmpty() && delta.getLast() != area || delta.isEmpty())
                delta.add(area);
        }

        for(Position position : this) {
            Integer area = Utils.getDeltaArea(position);
            if(!delta.isEmpty() && delta.getLast() != area || delta.isEmpty())
                delta.add(area);
        }

        return delta;
    }
}

