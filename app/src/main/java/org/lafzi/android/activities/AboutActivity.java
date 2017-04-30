package org.lafzi.android.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import org.lafzi.android.R;
import org.lafzi.android.BuildConfig;

/**
 * Created by alfat on 23/04/17.
 */

public class AboutActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setToolbar();

        TextView tvAbout = (TextView) findViewById(R.id.about_text_view);
        tvAbout.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.secondary_toolbar);
        setSupportActionBar(toolbar);

        String versionName = getString(R.string.app_name) + " versi " + BuildConfig.VERSION_NAME;

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(versionName);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
