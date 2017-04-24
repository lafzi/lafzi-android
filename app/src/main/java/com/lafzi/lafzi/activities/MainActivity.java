package com.lafzi.lafzi.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.lafzi.lafzi.R;
import com.lafzi.lafzi.adapters.AyatAdapter;
import com.lafzi.lafzi.listeners.AyatLongClickListener;
import com.lafzi.lafzi.listeners.AyatQuranQueryListeners;
import com.lafzi.lafzi.models.AyatQuran;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        modifyActionBar();
        setContentView(R.layout.activity_main);

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

    private void modifyActionBar(){
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo_s);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    private void setView(){
        final SearchView searchView = (SearchView) findViewById(R.id.search);
        searchView.setQueryHint(getString(R.string.search_placeholder));
        searchView.setIconified(false);

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

}
