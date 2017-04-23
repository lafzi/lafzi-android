package com.lafzi.lafzi.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lafzi.lafzi.R;
import com.lafzi.lafzi.filters.AyatAdapterFilter;
import com.lafzi.lafzi.models.AyatQuran;
import com.lafzi.lafzi.models.HighlightPosition;

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
        final ProgressBar bar = (ProgressBar) convertView.findViewById(R.id.percentage_bar);
        final TextView percentageText = (TextView) convertView.findViewById(R.id.percentage_text);

        noUrut.setText("[" + Integer.toString(position + 1) + "]");

        final String surah = getContext().getString(R.string.surah);
        final String ayah = getContext().getString(R.string.ayah);
        final String suratAndAyatText = surah + " " + ayat.getSurahName() + " (" + ayat.getSurahNo() + ") " + ayah + " " + ayat.getAyatNo();

        suratAyat.setText(suratAndAyatText);
        indoTextView.setText(ayat.getAyatIndonesian());

        final String ayatArabic = ayat.getAyatMuqathaat() == null ?
                ayat.getAyatArabic()  : ayat.getAyatMuqathaat();

        final Spannable wordToSpan = new SpannableString(ayatArabic);
        for (HighlightPosition hp : ayat.highlightPositions){

            int end = hp.getEndHighlight();
            if (hp.getEndHighlight() > ayatArabic.length() - 1)
                end = ayatArabic.length() - 1;
            wordToSpan.setSpan(new BackgroundColorSpan(Color.YELLOW),
                    hp.getStartHighlight(),
                    end + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        arabicTextView.setText(wordToSpan);

        int relevance = (int) Math.round(ayat.relevance);
        bar.setProgress(relevance);
        percentageText.setText(String.format("%s%%", Integer.toString(relevance)));

        notifyDataSetChanged();

        return convertView;
    }


    @NonNull
    @Override
    public AyatAdapterFilter getFilter() {
        return mFilter;
    }
}
