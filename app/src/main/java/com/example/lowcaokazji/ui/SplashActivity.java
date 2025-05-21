package com.example.lowcaokazji.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lowcaokazji.R;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DELAY = 1500; // ms

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Splash screen, then go to LoginActivity (or MainActivity if logged in)
        new Handler().postDelayed(() -> {
            boolean loggedIn = getSharedPreferences("user_prefs", MODE_PRIVATE)
                    .getBoolean("logged_in", false);
            Intent intent = new Intent(this, loggedIn ? MainActivity.class : LoginActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_DELAY);
    }
}