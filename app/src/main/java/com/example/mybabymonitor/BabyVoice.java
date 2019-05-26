package com.example.mybabymonitor;

import android.media.MediaRecorder;

import java.io.IOException;

public class BabyVoice {

    static final private double EMA_FILTER = 0.6;

    private MediaRecorder mRecorder = null;
    private double mEMA = 0.0;

    public void start() {

        if (mRecorder == null) {

            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");

            try {
                mRecorder.prepare(); } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            mRecorder.start();
            mEMA = 0.0;
        }
    }

    public void stop() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public double getAmplitude() {

        if (mRecorder != null) {
            // Cellphones can catch up to 90 db + -

            // we need to divide maxAmplitude with (32767/0.6325)
            double f1 = mRecorder.getMaxAmplitude()/51805.5336;//51805.5336 or if 100db so 46676.6381
            if (f1>0) {
                //Assuming that the minimum reference pressure is 0.000085 Pascal (on most phones) is equal to 0 db
                return (Math.abs(20 * Math.log10(f1 / 0.000085)));
            }
            return 0;
        }
        else
            return 0;

    }
}
