package com.accelkey.algorythm;

import android.app.Activity;
import android.app.Application;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
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
import org.w3c.dom.Text;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class StateListener implements SensorEventListener {
    private SensorManager sensorManager;

    private KeyInstance originKey;
    private KeyInstance testKey;
    private LinkedList<Integer> originDelta;
    private LinkedList<Integer> testDelta;
    private int state = 0;

    private String origin = "";

    private Activity activity;

    public StateListener(UpdateKey accelKey) {
        activity = accelKey;
        sensorManager = (SensorManager) accelKey.getSystemService(Context.SENSOR_SERVICE);
        originKey = new KeyInstance(sensorManager);
        testKey = new KeyInstance(sensorManager);
    }

    public StateListener(Unlock unlock) {
        activity = unlock;
        origin = readKeyFromStorage();

        System.out.println(origin);
        if(origin.isEmpty())
            activity.startActivity(new Intent(activity, UpdateKey.class));

        sensorManager = (SensorManager) unlock.getSystemService(Context.SENSOR_SERVICE);
        originKey = null;
        testKey = new KeyInstance(sensorManager);
    }

    private String readKeyFromStorage() {
        String key = "";
        FileInputStream fis = null;
        try {
            fis = activity.getApplicationContext().openFileInput(Utils.KEYFILE);
            StringBuffer fileContent = new StringBuffer("");
            byte[] buffer = new byte[1024];
            int n;
            while ((n = fis.read(buffer)) != -1)
            {
                fileContent.append(new String(buffer, 0, n));
            }
            fis.close();
            key = fileContent.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return key;
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
                testKey.clear();
                setState(2);
                writeKey();
                break;
            case 2:
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
        if(originKey == null) {
            String test;

            StringBuffer testsb = new StringBuffer();
            for(Integer s : testDelta){
                testsb.append(s.toString());
                testsb.append(",");
            }

            System.out.println(testsb.toString());
            System.out.println(origin);
            if(testsb.toString().equals(origin)) {
                Button unlock = (Button) activity.findViewById(R.id.unlock);
                TextView change = (TextView) activity.findViewById(R.id.changekey);
                ((TextView) activity.findViewById(R.id.info)).setText("Ключ введен успешно");
                change.setVisibility(View.VISIBLE);
                unlock.setText("Закрыть");
                unlock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.finish();
//                        openMainWindowApp();
                    }
                });

                change.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.startActivity(new Intent(activity, UpdateKey.class));
                    }
                });

            } else {
                ((TextView) activity.findViewById(R.id.info)).setText("Неверно. Попробуйте еще");
                Button unlock = (Button) activity.findViewById(R.id.unlock);
                unlock.setVisibility(View.VISIBLE);
                unlock.setText("Ввести ключ");
            }
        } else {
            if (originDelta.equals(testDelta)) {
                try {
                    writeKeyToStorage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                Button close = (Button) activity.findViewById(R.id.start);
                close.setText("Ввести ключ");
            }
        }
    }

    private void openMainWindowApp() {
        Intent i;
        PackageManager manager = activity.getPackageManager();
        try {
            i = manager.getLaunchIntentForPackage("com.sec.android.app.myfiles");
            if (i == null)
                throw new PackageManager.NameNotFoundException();
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            activity.startActivity(i);
            System.exit(0);
        } catch (PackageManager.NameNotFoundException e) {

        }
    }

    private void writeKeyToStorage() throws IOException {
        StringBuilder csvList = new StringBuilder();
        for(Integer s : originDelta){
            csvList.append(s.toString());
            csvList.append(",");
        }

        System.out.println(csvList.toString());
        FileOutputStream fos = activity.getApplicationContext().openFileOutput(Utils.KEYFILE, Context.MODE_PRIVATE);
        fos.write(csvList.toString().getBytes());
        fos.close();
    }

    private void buildDelta() {
        if(originKey != null) originDelta = originKey.getDelta();
        if(!testKey.isEmpty()) testDelta = testKey.getDelta();
    }

    private void simplifyKeys() {
        for(KeyInstance key : new KeyInstance[] {originKey, testKey}) {
            if(key != null) key.simplify();
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
        if(originKey != null && originKey.getTimer() != null && originKey.isTimerStarted())
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
