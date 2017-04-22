package com.lafzi.lafzi.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lafzi.lafzi.R;
import com.lafzi.lafzi.filters.AyatAdapterFilter;
import com.lafzi.lafzi.models.AyatQuran;

import java.util.LinkedList;

/**
 * Created by alfat on 19/04/17.
 */

public class AyatAdapter extends ArrayAdapter<AyatQuran> {

    private final AyatAdapterFilter mFilter;

    public AyatAdapter(Context context, LinkedList<AyatQuran> objects) {
        super(context, 0, objects);
        this.mFilter = new AyatAdapterFilter(context, this);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final AyatQuran ayat = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.ayat_layout, parent, false);

        final TextView noUrut = (TextView) convertView.findViewById(R.id.no_urut);
        final TextView suratAyat = (TextView) convertView.findViewById(R.id.surat_ayat);
        final TextView arabicTextView = (TextView) convertView.findViewById(R.id.ayat_arab);
        final TextView indoTextView = (TextView) convertView.findViewById(R.id.ayat_indo);

        noUrut.setText("[" + Integer.toString(position + 1) + "]");

        final String surah = getContext().getString(R.string.surah);
        final String ayah = getContext().getString(R.string.ayah);
        final String suratAndAyatText = surah + " " + ayat.getSurahName() + " (" + ayat.getSurahNo() + ") " + ayah + " " + ayat.getAyatNo();

        suratAyat.setText(suratAndAyatText);
        arabicTextView.setText(ayat.getAyatArabic());
        indoTextView.setText(ayat.getAyatIndonesian());

        notifyDataSetChanged();

        return convertView;
    }

    @NonNull
    @Override
    public AyatAdapterFilter getFilter() {
        return mFilter;
    }
}
