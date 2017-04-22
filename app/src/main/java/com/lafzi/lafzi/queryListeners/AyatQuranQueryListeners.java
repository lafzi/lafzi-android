package com.lafzi.lafzi.queryListeners;

import android.content.Context;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import com.lafzi.lafzi.R;
import com.lafzi.lafzi.adapters.AyatAdapter;

/**
 * Created by alfat on 21/04/17.
 */

public class AyatQuranQueryListeners implements SearchView.OnQueryTextListener {

    private final AyatAdapter adapter;
    private final Context context;

    public AyatQuranQueryListeners(final AyatAdapter ayatAdapter, final Context context){
        this.adapter = ayatAdapter;
        this.context = context;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query.length() < 3)
            Toast.makeText(context, R.string.query_less_than_3, Toast.LENGTH_SHORT).show();
        else
            adapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
