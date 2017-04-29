package org.lafzi.android.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import org.lafzi.android.R;
import org.lafzi.android.adapters.AyatAdapter;
import org.lafzi.android.listeners.AyatLongClickListener;
import org.lafzi.android.listeners.AyatQuranQueryListeners;
import org.lafzi.android.models.AyatQuran;

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
        final ProgressDialog pd = createProgressDialog();

        //listView.setOnScrollListener();
        searchView.setOnQueryTextListener(new AyatQuranQueryListeners(ayatAdapter, this, pd));

        final ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(ayatAdapter);
        listView.setEmptyView(findViewById(R.id.empty_result));
        listView.setOnItemLongClickListener(new AyatLongClickListener(this));
        listView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                pd.dismiss();
            }
        });

    }

    private ProgressDialog createProgressDialog(){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setProgressNumberFormat(null);
        pd.setProgressPercentFormat(null);
        pd.setMessage(getString(R.string.mencari));

        return pd;
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
