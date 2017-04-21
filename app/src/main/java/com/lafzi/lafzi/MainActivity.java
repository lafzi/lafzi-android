package com.lafzi.lafzi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.lafzi.lafzi.adapters.AyatAdapter;
import com.lafzi.lafzi.queryListeners.AyatQuranQueryListeners;

import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private SearchView searchView;

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
        switch (item.getItemId()){
            case R.id.pengaturan:
                Toast.makeText(this, "Pengaturan", Toast.LENGTH_LONG).show();
                return true;
        }
        return true;
    }

    private void modifyActionBar(){
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo_s);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    private void setView(){
        searchView = (SearchView) findViewById(R.id.search);
        searchView.setQueryHint(getString(R.string.search_placeholder));
        searchView.setIconified(false);

        final AyatAdapter ayatAdapter = new AyatAdapter(this, Collections.EMPTY_LIST);

        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(ayatAdapter);
        listView.setEmptyView(findViewById(R.id.empty_result));
        searchView.setOnQueryTextListener(new AyatQuranQueryListeners(ayatAdapter));
    }
}
