package com.example.nemanja.loopey;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicLong;

public class MainActivity extends AppCompatActivity {

    private static final int TEST_SIZE = 1_000_000;
    private TextView startTimeForEach;
    private TextView stopTimeForEach;
    private TextView totalTimeForEach;

    private TextView startTimeForIndex;
    private TextView stopTimeForIndex;
    private TextView totalTimeForIndex;
    private TextView start;

    private TextView listSizeText;
    private View.OnClickListener startClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (R.id.start == view.getId()) startTest();
            else if (R.id.createList == view.getId()) createTestList();
        }
    };
    private TextView testStatus;

    private void createTestList() {
        if (testList.size() != TEST_SIZE) {
//        if (testList.length != TEST_SIZE) {
            new Thread(createTestList).start();
        } else createListButton.setEnabled(false);

    }

    private Button createListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startTimeForEach = findViewById(R.id.startTimeForEach);
        stopTimeForEach = findViewById(R.id.stopTimeTimeForEach);
        totalTimeForEach = findViewById(R.id.totalTimeTimeForEach);

        startTimeForIndex = findViewById(R.id.startTimeForIndex);
        stopTimeForIndex = findViewById(R.id.stopTimeTimeForIndex);
        totalTimeForIndex = findViewById(R.id.totalTimeTimeForIndex);

        testStatus = findViewById(R.id.testStatus);

        start = findViewById(R.id.start);
        start.setOnClickListener(startClickListener);

        createListButton = findViewById(R.id.createList);
        createListButton.setOnClickListener(startClickListener);

        listSizeText = findViewById(R.id.listSize);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private boolean inTest = false;
    private AtomicLong startForEach = new AtomicLong();
    private AtomicLong stopForEach = new AtomicLong();
    private AtomicLong totalForEach = new AtomicLong();

    private AtomicLong startForIndex = new AtomicLong();
    private AtomicLong stopForIndex = new AtomicLong();
    private AtomicLong totalForIndex = new AtomicLong();

//    private long startForEach = 0;
//    private long stopForEach = 0;
//    private long totalForEach = 0;
//
//    private long startForIndex = 0;
//    private long stopForIndex = 0;
//    private long totalForIndex = 0;

    Thread testThread;
    private Runnable forEachTest = new Runnable() {
        @Override
        public void run() {
            //for each
            startForEach.set(System.currentTimeMillis());
//            startForEach = System.currentTimeMillis();
            for (Integer item : testList) {
                if (item > -1) listItem = item;
            }
            stopForEach.set(System.currentTimeMillis());
            totalForEach.set(stopForEach.get() - startForEach.get());
//            stopForEach = System.currentTimeMillis();
//            totalForEach = stopForEach - startForEach;
        }
    };
    private Runnable forIndexTest = new Runnable() {
        @Override
        public void run() {
            //for index
            startForIndex.set(System.currentTimeMillis());
//            startForIndex = System.currentTimeMillis();
            for (Integer item : testList) {
                if (item > -1) listItem = item;
            }
            stopForIndex.set(System.currentTimeMillis());
            totalForIndex.set(stopForIndex.get() - startForIndex.get());
//            stopForIndex = System.currentTimeMillis();
//            totalForIndex = stopForIndex - startForIndex;
        }
    };
    private Integer listItem;
    Runnable testRun = new Runnable() {
        @Override
        public void run() {
            postTestRunning();
            listItem = -1;

            final Thread forEach = new Thread(forEachTest);
            final Thread forIndex = new Thread(forIndexTest);
            try {
                forEach.start();
                forIndex.start();
                forEach.join();
                forIndex.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Log.i("FOREACH: ", listItem + "");
            Log.i("FORINDEX: ", listItem + "");

            postTestCompleted();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    printForEachTimes();
                    printForIndexTimes();
                }
            });
        }
    };

//    final Set<Integer> testList = new HashSet<>();
    final List<Integer> testList = new ArrayList<>(TEST_SIZE);
//    Integer[] testList = new Integer[1];

    Runnable createTestList = new Runnable() {
        @Override
        public void run() {
//            testList = new Integer[TEST_SIZE];
            int i = 0;
            Log.e("LAMEMORIA ", "Start adding elements to testList.");
            try {
                for (; i < TEST_SIZE; i++) {
                    MainActivity.this.testList.add(1000 + i);
//                testList[i] = 1000 + i;
                }
            } catch (OutOfMemoryError oom) {
                Log.e("LAMEMORIA "+ i, oom.getMessage());
            }
            Log.e("LAMEMORIA ", "Done adding elements to testList.");
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listSizeText.setText(String.format(Locale.getDefault(), "List size: %s", MainActivity.this.testList.size()));
//                    listSizeText.setText(String.format(Locale.getDefault(), "List size: %s", testList.length));
                }
            });
        }
    };

    private void startTest() {
        if (testThread != null && testThread.isAlive()) {
            Toast.makeText(getBaseContext(), "Test already in progress.", Toast.LENGTH_SHORT).show();
        } else if (!inTest && testList.size() == TEST_SIZE) {
//        } else if (!inTest && testList.length == TEST_SIZE) {
            testThread = new Thread(testRun);
            testThread.start();
        } else  {
            String text = inTest ? "Test is already in progress." : "List size is incorrect.";
            Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT).show();
        }

    }

    private void printForEachTimes() {
        startTimeForEach.setText(String.format(Locale.getDefault(), "start time: %s", startForEach));
        stopTimeForEach.setText(String.format(Locale.getDefault(), "stop time: %s", stopForEach));
        totalTimeForEach.setText(String.format(Locale.getDefault(), "total time: %s", totalForEach));

    }

    private void printForIndexTimes() {
        startTimeForIndex.setText(String.format(Locale.getDefault(), "start time: %s", startForIndex));
        stopTimeForIndex.setText(String.format(Locale.getDefault(), "stop time: %s", stopForIndex));
        totalTimeForIndex.setText(String.format(Locale.getDefault(), "total time: %s", totalForIndex));

    }

    private void postTestRunning() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                testStatus.setBackgroundColor(getResources().getColor(R.color.red));
                testStatus.setText("Test Status: running...");
            }
        });
    }

    private void postTestCompleted() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                testStatus.setBackgroundColor(getResources().getColor(R.color.green));
                testStatus.setText("Test Status: completed");
            }
        });
    }
}
