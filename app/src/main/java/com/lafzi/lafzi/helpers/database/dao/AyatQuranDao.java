package com.lafzi.lafzi.helpers.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lafzi.lafzi.models.AyatQuran;
import com.lafzi.lafzi.models.builder.AyatQuranBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alfat on 21/04/17.
 */

public class AyatQuranDao {

    private final SQLiteDatabase db;

    public AyatQuranDao(final SQLiteDatabase db) {
        this.db = db;
    }

    public AyatQuran getAyatQuran(int id){

        final String[] projection = {
                AyatQuran._ID,
                AyatQuran.SURAH_NO,
                AyatQuran.SURAH_NAME,
                AyatQuran.AYAT_NO,
                AyatQuran.AYAT_ARABIC,
                AyatQuran.AYAT_INDONESIAN
        };

        final String selection = AyatQuran._ID + " = ?";
        final String[] selectionArgs = { Integer.toString(id) };

        final Cursor cursor = db.query(AyatQuran.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if (cursor.moveToNext()){
            return readAyatQuranFromCursor(cursor);
        }

        return null;
    }

    public List<AyatQuran> getAyatQuransByTrigramTerm(final String term){

        final List<AyatQuran> results = new ArrayList<>();
        final String[] selectionArgs = { term };
        final Cursor cursor = db.rawQuery(sqlAyatByTerm, selectionArgs);

        while (cursor.moveToNext()){
            results.add(readAyatQuranFromCursor(cursor));
        }

        return results;
    }

    private AyatQuran readAyatQuranFromCursor(final Cursor cursor){
        final AyatQuranBuilder aqBuilder = AyatQuranBuilder.getInstance();

        aqBuilder.setId(cursor
                .getInt(cursor
                        .getColumnIndexOrThrow(AyatQuran._ID)));

        aqBuilder.setSurahNo(cursor
                .getInt(cursor
                        .getColumnIndexOrThrow(AyatQuran.SURAH_NO)));

        aqBuilder.setSurahName(cursor
                .getString(cursor
                        .getColumnIndexOrThrow(AyatQuran.SURAH_NAME)));

        aqBuilder.setAyatNo(cursor
                .getInt(cursor
                        .getColumnIndexOrThrow(AyatQuran.AYAT_NO)));

        aqBuilder.setAyatArabic(cursor
                .getString(cursor
                        .getColumnIndexOrThrow(AyatQuran.AYAT_ARABIC)));

        aqBuilder.setAyatIndonesian(cursor
                .getString(cursor
                        .getColumnIndexOrThrow(AyatQuran.AYAT_INDONESIAN)));

        return aqBuilder.build();
    }

    private String sqlAyatByTerm = "SELECT \n" +
            "aq.*\n" +
            "FROM\n" +
            "ayat_quran aq\n" +
            "INNER JOIN nonvocal_index nvi ON aq.\"_id\" = nvi.ayat_quran_id\n" +
            "WHERE\n" +
            "nvi.term = ?";

    public void close(){
        db.close();
    }

}
