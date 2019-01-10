package org.lafzi.lafzi.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.lafzi.android.R;
import org.lafzi.lafzi.filters.AyatAdapterFilter;
import org.lafzi.lafzi.helpers.preferences.Preferences;
import org.lafzi.lafzi.models.AyatQuran;
import org.lafzi.lafzi.models.HighlightPosition;
import org.lafzi.lafzi.utils.GeneralUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by alfat on 19/04/17.
 */

public class AyatAdapter extends ArrayAdapter<AyatQuran> {

    private final AyatAdapterFilter mFilter;

    private List<AyatQuran> datas;

    public AyatAdapter(Activity activity, LinkedList<AyatQuran> objects) {
        super(activity, 0, objects);
        this.mFilter = new AyatAdapterFilter(activity, this);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        final boolean showTrans = Preferences.getInstance().showTranslation();

        final AyatQuran ayat = getItem(position);
        if (convertView == null) {

            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.ayat_layout, parent, false);

            viewHolder.noUrut = (TextView) convertView.findViewById(R.id.no_urut);
            viewHolder.suratAyat = (TextView) convertView.findViewById(R.id.surat_ayat);
            viewHolder.arabicTextView = (TextView) convertView.findViewById(R.id.ayat_arab);
            viewHolder.indoTextView = (TextView) convertView.findViewById(R.id.ayat_indo);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position % 2 == 1) {
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.searchResultStripe));
        } else {
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.appBackground));
        }

        viewHolder.noUrut.setText(Integer.toString(position + 1));

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

        final String ayatArabic = GeneralUtil.isNullOrEmpty(ayat.getAyatMuqathaat()) ?
                ayat.getAyatArabic()  : ayat.getAyatMuqathaat();

        final Spannable wordToSpan = new SpannableString(ayatArabic);
        for (HighlightPosition hp : ayat.highlightPositions){

            int start = hp.getStartHighlight();
            int end = hp.getEndHighlight();

            if (start > end) {
                int temp = start;
                start = end;
                end = temp;
            }

            if (start < 0) start = 0;
            if (end < 0) end = 0;

            if (start > ayatArabic.length() - 1) start = ayatArabic.length() - 1;
            if (end > ayatArabic.length() - 1) end = ayatArabic.length() - 1;

            wordToSpan.setSpan(new BackgroundColorSpan(Color.argb(128, 255, 238, 64)),
                    start,
                    end + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        Typeface meQuran = Typeface.createFromAsset(getContext().getAssets(), "fonts/me_quran.ttf");
        viewHolder.arabicTextView.setTypeface(meQuran);
        viewHolder.arabicTextView.setText(wordToSpan);
        convertView.setId(ayat.getId());

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
    }

}
