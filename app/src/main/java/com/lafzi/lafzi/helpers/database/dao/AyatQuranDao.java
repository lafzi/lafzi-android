package com.lafzi.lafzi.helpers.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lafzi.lafzi.models.AyatQuran;
import com.lafzi.lafzi.models.builder.AyatQuranBuilder;

/**
 * Created by alfat on 21/04/17.
 */

public class AyatQuranDao {

    private final SQLiteDatabase db;

    public AyatQuranDao(final SQLiteDatabase db) {
        this.db = db;
    }

    public AyatQuran getAyatQuran(final int id){

        final String[] projection = {
                AyatQuran._ID,
                AyatQuran.SURAH_NO,
                AyatQuran.SURAH_NAME,
                AyatQuran.AYAT_NO,
                AyatQuran.AYAT_ARABIC,
                AyatQuran.AYAT_INDONESIAN,
                AyatQuran.AYAT_MUQATHAAT,
        };

        final String selection = AyatQuran._ID + " = ?";
        final String[] selectionArgs = { Integer.toString(id) };

        final Cursor cursor = db.query(AyatQuran.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if (cursor.moveToNext()){
            try {
                return readAyatQuranFromCursor(cursor);
            } finally {
                cursor.close();
            }

        }

        return null;
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

        aqBuilder.setAyatMuqathaat(cursor
                .getString(cursor
                        .getColumnIndexOrThrow(AyatQuran.AYAT_MUQATHAAT)));

        return aqBuilder.build();
    }

}
