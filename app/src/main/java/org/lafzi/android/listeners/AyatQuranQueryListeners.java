package org.lafzi.android.listeners;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import org.lafzi.android.R;
import org.lafzi.android.adapters.AyatAdapter;

/**
 * Created by alfat on 21/04/17.
 */

public class AyatQuranQueryListeners implements SearchView.OnQueryTextListener {

    private final AyatAdapter adapter;
    private final Activity activity;
    private ProgressDialog progressDialog;

    public AyatQuranQueryListeners(final AyatAdapter ayatAdapter,
                                   final Activity activity,
                                   final ProgressDialog progressDialog){
        this.adapter = ayatAdapter;
        this.activity = activity;
        this.progressDialog = progressDialog;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query.length() < 3)
            Toast.makeText(activity, R.string.query_less_than_3, Toast.LENGTH_SHORT).show();
        else {
            progressDialog.show();
            adapter.getFilter().filter(query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
