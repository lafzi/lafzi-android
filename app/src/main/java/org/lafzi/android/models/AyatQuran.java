package org.lafzi.android.models;

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
    public static final String AYAT_MUQATHAAT = "ayat_muqathaat";
    public static final String VOCAL_MAPPING_POSITION = "vocal_mapping_position";
    public static final String NONVOCAL_MAPPING_POSITIONG = "nonvocal_mapping_position";

    private final int id;
    private final int surahNo;
    private final String surahName;
    private final int ayatNo;
    private final String ayatArabic;
    private final String ayatIndonesian;
    private final String ayatMuqathaat;
    private final List<Integer> mappingPositions;

    public List<HighlightPosition> highlightPositions = new ArrayList<>();
    public double relevance;

    public AyatQuran(int id,
                     int surahNo,
                     String surahName,
                     int ayatNo,
                     String ayatArabic,
                     String ayatIndonesian,
                     String ayatMuqathaat,
                     List<Integer> mappingPosition) {
        this.id = id;
        this.surahNo = surahNo;
        this.surahName = surahName;
        this.ayatNo = ayatNo;
        this.ayatArabic = ayatArabic;
        this.ayatIndonesian = ayatIndonesian;
        this.ayatMuqathaat = ayatMuqathaat;
        this.mappingPositions = mappingPosition;
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

    public String getAyatMuqathaat() {
        return ayatMuqathaat;
    }

    public List<Integer> getMappingPositions() {
        return mappingPositions;
    }
}
