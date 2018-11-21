package org.lafzi.lafzi.adapters;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.lafzi.android.R;
import org.lafzi.lafzi.models.AyatQuran;
import org.lafzi.lafzi.utils.GeneralUtil;

import java.util.List;

public class AyatPagerAdapter extends FragmentStatePagerAdapter {

    private final List<AyatQuran> ayatQurans;

    public AyatPagerAdapter(FragmentManager fm, List<AyatQuran> ayatQurans) {
        super(fm);
        this.ayatQurans = ayatQurans;
    }

    @Override
    public Fragment getItem(int position) {
        final AyatQuran ayatQuran = ayatQurans.get(position);
        return SingleAyatFragment.newInstance(ayatQuran);
    }

    @Override
    public int getCount() {
        return ayatQurans.size();
    }

    public static class SingleAyatFragment extends Fragment {
        private AyatQuran ayatQuran;


        public static SingleAyatFragment newInstance(AyatQuran ayatQuran) {
            SingleAyatFragment ayatFragment = new SingleAyatFragment();
            ayatFragment.ayatQuran = ayatQuran;
            return ayatFragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.fragment_single_ayat, container, false);

            TextView tvNamaSuratAyat = (TextView) view.findViewById(R.id.single_surat_ayat);
            TextView tvIndo = (TextView) view.findViewById(R.id.single_ayat_indo);
            TextView tvArab = (TextView) view.findViewById(R.id.single_ayat_arab);

            final String surah = getContext().getString(R.string.surah);
            final String ayah = getContext().getString(R.string.ayah);
            final String suratAndAyatText = surah + " " + ayatQuran.getSurahName() + " (" + ayatQuran.getSurahNo() + ") " + ayah + " " + ayatQuran.getAyatNo();
            tvNamaSuratAyat.setText(suratAndAyatText);

            Typeface meQuran = Typeface.createFromAsset(getContext().getAssets(), "fonts/me_quran.ttf");
            tvArab.setTypeface(meQuran);
            final String ayatArabic = GeneralUtil.isNullOrEmpty(ayatQuran.getAyatMuqathaat()) ?
                    ayatQuran.getAyatArabic()  : ayatQuran.getAyatMuqathaat();
            tvArab.setText(ayatArabic);
            tvIndo.setText(ayatQuran.getAyatIndonesian());

            return view;
        }
    }
}
