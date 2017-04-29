package org.lafzi.android.models;

import android.provider.BaseColumns;

import java.util.List;

/**
 * Created by alfat on 21/04/17.
 */

public class Index implements BaseColumns {

    public static final String VOCAL_INDEX_TABLE_NAME = "vocal_index";
    public static final String NONVOCAL_INDEX_TABLE_NAME = "nonvocal_index";
    public static final String TERM = "term";
    public static final String AYAT_QURAN_ID = "ayat_quran_id";
    public static final String FREQUENCY = "frequency";
    public static final String POSITION = "position";

    public static final String TERM_INDEX_NAME = "_term_index";

    private final int id;
    private final String term;
    private final int ayatQuranId;
    private final int frequency;
    private final List<Integer> position;

    public Index(int id,
                 String term,
                 int ayatQuranId,
                 int frequency,
                 List<Integer> position) {
        this.id = id;
        this.term = term;
        this.ayatQuranId = ayatQuranId;
        this.frequency = frequency;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public String getTerm() {
        return term;
    }

    public int getAyatQuranId() {
        return ayatQuranId;
    }

    public int getFrequency() {
        return frequency;
    }

    public List<Integer> getPosition() {
        return position;
    }
}
