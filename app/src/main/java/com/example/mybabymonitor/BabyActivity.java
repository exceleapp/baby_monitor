package com.example.mybabymonitor;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class BabyActivity extends AppCompatActivity {

    private static final int POLL_INTERVAL = 300;
    private boolean mRunning = false;
    private int mThreshold;
    int RECORD_AUDIO = 0;
    private PowerManager.WakeLock mWakeLock;
    private Handler mHandler = new Handler();
    private TextView mStatusView,tv_noice;
    private BabyVoice mSensor;
    ProgressBar bar;
    ImageView babyImage;
    private Runnable mSleepTask = new Runnable() {
        public void run() {
            //Log.i("Noise", "runnable mSleepTask");
            start();
        }
    };

    // Create runnable thread to Monitor Voice
    private Runnable mPollTask = new Runnable() {
        public void run() {
            double amp = mSensor.getAmplitude();
            //Log.i("Noise", "runnable mPollTask");
            updateDisplay("Monitoring Voice...", amp);

            if ((amp > mThreshold)) {
                callForHelp(amp);
                //Log.i("Noise", "==== onCreate ===");

            }
            // Runnable(mPollTask) will again execute after POLL_INTERVAL
            mHandler.postDelayed(mPollTask, POLL_INTERVAL);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby);

        mStatusView = (TextView) findViewById(R.id.status);
        tv_noice = (TextView)findViewById(R.id.tv_noise);
        bar = (ProgressBar)findViewById(R.id.progressBar1);
        // Used to record voice
        mSensor = new BabyVoice();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakelockTag");
        mWakeLock.acquire();

    }
    @Override
    public void onResume() {
        super.onResume();
        //Log.i("Noise", "==== onResume ===");

        initializeApplicationConstants();
        if (!mRunning) {
            mRunning = true;
            start();
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        // Log.i("Noise", "==== onStop ===");
        //Stop noise monitoring
        stop();
    }
    private void start() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    RECORD_AUDIO);
        }

        //Log.i("Noise", "==== start ===");
        mSensor.start();
        if (!mWakeLock.isHeld()) {
            mWakeLock.acquire();
        }
        //Noise monitoring start
        // Runnable(mPollTask) will execute after POLL_INTERVAL
        mHandler.postDelayed(mPollTask, POLL_INTERVAL);
    }
    private void stop() {
        Log.d("Noise", "==== Stop Noise Monitoring===");
        if (mWakeLock.isHeld()) {
            mWakeLock.release();
        }
        mHandler.removeCallbacks(mSleepTask);
        mHandler.removeCallbacks(mPollTask);
        mSensor.stop();
        bar.setProgress(0);
        updateDisplay("stopped...", 0.0);
        mRunning = false;

    }


    private void initializeApplicationConstants() {
        // Set Noise Threshold
        mThreshold = 75;

    }

    private void updateDisplay(String status, double signalEMA) {
        mStatusView.setText(status);
        //
        bar.setProgress((int)signalEMA);
        Log.d("SOUND", String.valueOf(signalEMA));
        tv_noice.setText(String.format("%.2f",signalEMA)+"dB");
    }


    private void callForHelp(double signalEMA) {

        //stop();

        // Show alert when noise thersold crossed
        babyImage = findViewById(R.id.turu);
        babyImage.setBackground(ResourcesCompat.getDrawable(getResources(), R.mipmap.crying,null));

        Toast.makeText(getApplicationContext(), "Baby is crying, contact parents as soon as possible",
                Toast.LENGTH_LONG).show();
        Log.d("SOUND", String.valueOf(signalEMA));
        tv_noice.setText(String.format("%.2f",signalEMA)+"dB");
    }

};
