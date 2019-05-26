package com.example.mybabymonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

public class SplashScreen extends Activity {

    ImageView Splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);

        Splash = (ImageView) findViewById(R.id.splash);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
                finish();
            }
        }, 3000L);
    }
}