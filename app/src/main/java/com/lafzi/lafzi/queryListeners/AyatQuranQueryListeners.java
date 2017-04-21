package com.lafzi.lafzi.queryListeners;

import android.support.v7.widget.SearchView;

import com.lafzi.lafzi.adapters.AyatAdapter;

/**
 * Created by alfat on 21/04/17.
 */

public class AyatQuranQueryListeners implements SearchView.OnQueryTextListener {

    final private AyatAdapter adapter;

    public AyatQuranQueryListeners(final AyatAdapter ayatAdapter){
        this.adapter = ayatAdapter;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query.length() < 3)
            adapter.clear();
        else
            adapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
