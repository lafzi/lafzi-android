package org.lafzi.android.listeners;

import android.app.Activity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.lafzi.android.R;
import org.lafzi.android.adapters.AyatAdapter;

/**
 * Created by alfat on 21/04/17.
 */

public class AyatQuranQueryListeners implements SearchView.OnQueryTextListener {

    private final AyatAdapter adapter;
    private final Activity activity;

    public AyatQuranQueryListeners(final AyatAdapter ayatAdapter,
                                   final Activity activity){
        this.adapter = ayatAdapter;
        this.activity = activity;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query.length() < 3)
            Toast.makeText(activity, R.string.query_less_than_3, Toast.LENGTH_SHORT).show();
        else {
            adapter.clear();

            ProgressBar pb = (ProgressBar) activity.findViewById(R.id.searching_progress_bar);
            TextView tv = (TextView) activity.findViewById(R.id.empty_result);

            pb.setVisibility(View.VISIBLE);
            tv.setVisibility(View.GONE);
            adapter.getFilter().filter(query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
