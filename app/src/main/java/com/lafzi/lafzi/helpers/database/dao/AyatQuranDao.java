package com.lafzi.lafzi.helpers.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.lafzi.lafzi.models.AyatQuran;
import com.lafzi.lafzi.models.builder.AyatQuranBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public List<AyatQuran> getAyatQurans(final List<Integer> ids){

        final String[] projection = {
                AyatQuran._ID,
                AyatQuran.SURAH_NO,
                AyatQuran.SURAH_NAME,
                AyatQuran.AYAT_NO,
                AyatQuran.AYAT_ARABIC,
                AyatQuran.AYAT_INDONESIAN
        };

        final String selection = AyatQuran._ID + " IN (" +
                TextUtils.join(",", Collections.nCopies(ids.size(), "?")) + ")";
        final String[] selectionArgs = new String[ids.size()];
        int i = 0;
        for (Integer id : ids){
            selectionArgs[i] = Integer.toString(id);
            i++;
        }

        String order = "";
        i = 0;
        for (Integer id : ids){
            order += AyatQuran._ID + " = " + id + " DESC";
            if (i < ids.size() - 1)
                order += ", ";
            i++;
        }

        final Cursor cursor = db.query(AyatQuran.TABLE_NAME, projection, selection, selectionArgs, null, null, order);
        final List<AyatQuran> results = new ArrayList<>();
        while (cursor.moveToNext())
            results.add(readAyatQuranFromCursor(cursor));

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

}
