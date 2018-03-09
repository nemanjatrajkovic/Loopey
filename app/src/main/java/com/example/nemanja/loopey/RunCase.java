package com.example.nemanja.loopey;

/**
 * Created by nemanja on 3/8/18.
 */

public class RunCase extends Thread {

    private long startTime = 0;
    private long stopTime= 0;
    private long totalTime = 0;

    public void startTiming() {
        startTime = System.currentTimeMillis();
    }

    public void stopTiming() {
        stopTime = System.currentTimeMillis();
        totalTime = stopTime - startTime;
    }


    @Override
    public void run() {
//        postStart();
//        runTest();
//        postStop();
    }
}
