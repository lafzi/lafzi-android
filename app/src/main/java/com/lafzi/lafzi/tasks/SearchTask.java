package com.lafzi.lafzi.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import com.lafzi.lafzi.models.AyatQuran;

/**
 * Created by alfat on 24/04/17.
 */

public class SearchTask extends AsyncTask {

    private final ArrayAdapter<AyatQuran> adapter;
    private final Activity activity;
    private final String query;

    public SearchTask(final ArrayAdapter<AyatQuran> adapter,
                      final Activity activity,
                      final String query){
        this.adapter = adapter;
        this.activity = activity;
        this.query = query;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        adapter.getFilter().filter(query);
        return null;
    }

}
