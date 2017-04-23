package com.lafzi.lafzi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lafzi.lafzi.R;
import com.lafzi.lafzi.helpers.preferences.Preferences;
import com.lafzi.lafzi.tasks.MigrateTask;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow()
                .getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        if (new Preferences(this).isDatabaseUpdated()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    final Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_TIME_OUT);
        } else {
            new MigrateTask(this).execute();
        }

    }

}
