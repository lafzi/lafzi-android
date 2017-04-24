package com.lafzi.lafzi.listeners;

import android.widget.AbsListView;

import com.lafzi.lafzi.adapters.AyatAdapter;

/**
 * Created by alfat on 24/04/17.
 */

public class AyatListScrollListener implements AbsListView.OnScrollListener {

    private final AyatAdapter adapter;

    public AyatListScrollListener(final AyatAdapter adapter){
        this.adapter = adapter;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {}

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        final int lastInScreen = firstVisibleItem + visibleItemCount;

    }
}
