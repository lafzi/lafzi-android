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
import com.lafzi.lafzi.helpers.preferences.Preferences;
import com.lafzi.lafzi.models.AyatQuran;
import com.lafzi.lafzi.models.HighlightPosition;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by alfat on 19/04/17.
 */

public class AyatAdapter extends ArrayAdapter<AyatQuran> {

    private final AyatAdapterFilter mFilter;

    private List<AyatQuran> datas;

    public AyatAdapter(Context context, LinkedList<AyatQuran> objects) {
        super(context, 0, objects);
        this.mFilter = new AyatAdapterFilter(context, this);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        final boolean showTrans = new Preferences(getContext()).showTranslation();

        final AyatQuran ayat = getItem(position);
        if (convertView == null) {

            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.ayat_layout, parent, false);

            viewHolder.noUrut = (TextView) convertView.findViewById(R.id.no_urut);
            viewHolder.suratAyat = (TextView) convertView.findViewById(R.id.surat_ayat);
            viewHolder.arabicTextView = (TextView) convertView.findViewById(R.id.ayat_arab);
            viewHolder.indoTextView = (TextView) convertView.findViewById(R.id.ayat_indo);
            viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.percentage_bar);
            viewHolder.percentageText = (TextView) convertView.findViewById(R.id.percentage_text);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.noUrut.setText("[" + Integer.toString(position + 1) + "]");

        final String surah = getContext().getString(R.string.surah);
        final String ayah = getContext().getString(R.string.ayah);
        final String suratAndAyatText = surah + " " + ayat.getSurahName() + " (" + ayat.getSurahNo() + ") " + ayah + " " + ayat.getAyatNo();

        viewHolder.suratAyat.setText(suratAndAyatText);

        if (showTrans) {
            viewHolder.indoTextView.setVisibility(View.VISIBLE);
            viewHolder.indoTextView.setText(ayat.getAyatIndonesian());
        } else {
            viewHolder.indoTextView.setVisibility(View.GONE);
        }

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
        viewHolder.arabicTextView.setText(wordToSpan);

        int relevance = (int) Math.round(ayat.relevance);
        viewHolder.progressBar.setProgress(relevance);
        viewHolder.percentageText.setText(String.format("%s%%", Integer.toString(relevance)));

        return convertView;
    }


    @NonNull
    @Override
    public AyatAdapterFilter getFilter() {
        return mFilter;
    }

    private static class ViewHolder{
        private TextView noUrut;
        private TextView suratAyat;
        private TextView arabicTextView;
        private TextView indoTextView;
        private ProgressBar progressBar;
        private TextView percentageText;
    }

    public void assignDatas(List<AyatQuran> results){
        datas = results;
    }
}
