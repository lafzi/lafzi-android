package com.lafzi.lafzi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.lafzi.lafzi.R;
import com.lafzi.lafzi.adapters.AyatAdapter;
import com.lafzi.lafzi.clickListeners.AyatLongClickListener;
import com.lafzi.lafzi.models.AyatQuran;
import com.lafzi.lafzi.queryListeners.AyatQuranQueryListeners;

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

        final ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(ayatAdapter);
        listView.setEmptyView(findViewById(R.id.empty_result));
        listView.setOnItemLongClickListener(new AyatLongClickListener(this));
        searchView.setOnQueryTextListener(new AyatQuranQueryListeners(ayatAdapter, getApplicationContext()));
    }

}
