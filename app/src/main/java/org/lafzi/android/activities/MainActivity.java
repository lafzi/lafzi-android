package org.lafzi.android.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import org.lafzi.android.R;
import org.lafzi.android.adapters.AyatAdapter;
import org.lafzi.android.listeners.AyatLongClickListener;
import org.lafzi.android.listeners.AyatQuranQueryListeners;
import org.lafzi.android.models.AyatQuran;
import org.lafzi.android.utils.GeneralUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private boolean preferencesChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        ((SearchView) findViewById(R.id.search)).setSubmitButtonEnabled(true);

        preferencesChanged = false;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(this);

        setView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.bantuan:
                intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
                return true;
            case R.id.pengaturan:
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                return true;
            case R.id.about:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    private void setView(){
        final SearchView searchView = (SearchView) findViewById(R.id.search);

        final AyatAdapter ayatAdapter = new AyatAdapter(this, new LinkedList<AyatQuran>());
        searchView.setOnQueryTextListener(new AyatQuranQueryListeners(ayatAdapter, this));

        final ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(ayatAdapter);
        listView.setEmptyView(findViewById(R.id.search_help));
        listView.setOnItemLongClickListener(new AyatLongClickListener(this));

        setSearchExamples();
    }

    private void setSearchExamples() {

        final SearchView searchView = (SearchView) findViewById(R.id.search);

        String examples[] = getString(R.string.search_examples).split(";");
        Collections.shuffle(Arrays.asList(examples));
        final ViewFlipper viewFlipper = (ViewFlipper) findViewById(R.id.search_examples);

        for (int i = 0; i < examples.length; i++) {
            final String example = examples[i].trim();
            if (!GeneralUtil.isNullOrEmpty(example)) {
                TextView tv = new TextView(this);
                tv.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                tv.setGravity(Gravity.CENTER);
                tv.setTypeface(null, Typeface.BOLD);
                tv.setTextSize(20);
                tv.setText(example + " \u25B6");
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchView.setQuery(example, true);
                    }
                });
                viewFlipper.addView(tv);
            }
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        preferencesChanged = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (preferencesChanged) {
            // reload search
            final SearchView searchView = (SearchView) findViewById(R.id.search);
            searchView.setQuery(searchView.getQuery(), true);
            preferencesChanged = false;
        }
    }
}
