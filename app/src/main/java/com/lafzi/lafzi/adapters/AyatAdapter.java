package com.lafzi.lafzi.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lafzi.lafzi.R;
import com.lafzi.lafzi.model.Ayat;

import java.util.List;

/**
 * Created by alfat on 19/04/17.
 */

public class AyatAdapter extends ArrayAdapter<Ayat> {

    public AyatAdapter(Context context, List<Ayat> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Ayat ayat = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.ayat_layout, parent, false);

        final TextView arabicTextView = (TextView) convertView.findViewById(R.id.ayat_arab);
        final TextView indoTextView = (TextView) convertView.findViewById(R.id.ayat_indo);

        arabicTextView.setText(ayat.getArabic());
        indoTextView.setText(ayat.getIndonesia());

        return convertView;
    }
}
