package com.lafzi.lafzi.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.lafzi.lafzi.R;

/**
 * Created by alfat on 23/04/17.
 */

public class SettingFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
