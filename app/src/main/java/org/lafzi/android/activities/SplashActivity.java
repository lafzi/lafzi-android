package org.lafzi.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import org.lafzi.android.R;
import org.lafzi.android.helpers.preferences.Preferences;
import org.lafzi.android.tasks.MigrateTask;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
