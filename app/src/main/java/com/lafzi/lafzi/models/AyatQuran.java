package com.lafzi.lafzi.models;

import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alfat on 19/04/17.
 */

public class AyatQuran implements BaseColumns {

    public static final String TABLE_NAME = "ayat_quran";
    public static final String SURAH_NO = "surah_no";
    public static final String SURAH_NAME = "surah_name";
    public static final String AYAT_NO = "ayat_no";
    public static final String AYAT_ARABIC = "ayat_arabic";
    public static final String AYAT_INDONESIAN = "ayat_indonesian";

    private final int id;
    private final int surahNo;
    private final String surahName;
    private final int ayatNo;
    private final String ayatArabic;
    private final String ayatIndonesian;

    public List<HighlightPosition> highlightPositions = new ArrayList<>();
    public double relevance;

    public AyatQuran(int id,
                     int surahNo,
                     String surahName,
                     int ayatNo,
                     String ayatArabic,
                     String ayatIndonesian) {
        this.id = id;
        this.surahNo = surahNo;
        this.surahName = surahName;
        this.ayatNo = ayatNo;
        this.ayatArabic = ayatArabic;
        this.ayatIndonesian = ayatIndonesian;
    }

    public int getId() {
        return id;
    }

    public int getSurahNo() {
        return surahNo;
    }

    public String getSurahName() {
        return surahName;
    }

    public int getAyatNo() {
        return ayatNo;
    }

    public String getAyatArabic() {
        return ayatArabic;
    }

    public String getAyatIndonesian() {
        return ayatIndonesian;
    }
}
