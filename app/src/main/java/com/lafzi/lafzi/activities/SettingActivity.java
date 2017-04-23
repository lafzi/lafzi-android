package com.lafzi.lafzi.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.lafzi.lafzi.fragments.SettingFragment;

/**
 * Created by alfat on 23/04/17.
 */

public class SettingActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new SettingFragment())
                .commit();
    }
}
