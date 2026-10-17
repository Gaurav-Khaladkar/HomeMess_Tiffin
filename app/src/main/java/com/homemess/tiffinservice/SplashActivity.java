package com.homemess.tiffinservice;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Splash screen for Home Mess Tiffin Service Android Application
 * Sir Parashurambhau College, Pune
 * Project by: Gaurav Kaluram Khaladkar & Aditay Balasaheb More
 */
public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 1800; // 1.8 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_DURATION);
    }
}
