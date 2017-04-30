package org.lafzi.android.helpers.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.lafzi.android.models.AyatQuran;
import org.lafzi.android.models.builder.AyatQuranBuilder;

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

    public AyatQuran getAyatQuran(final int id, final boolean isVocal){

        final String mappingPosColumnName = isVocal ?
                AyatQuran.VOCAL_MAPPING_POSITION :
                AyatQuran.NONVOCAL_MAPPING_POSITIONG;

        final String[] projection = {
                AyatQuran._ID,
                AyatQuran.SURAH_NO,
                AyatQuran.SURAH_NAME,
                AyatQuran.AYAT_NO,
                AyatQuran.AYAT_ARABIC,
                AyatQuran.AYAT_INDONESIAN,
                AyatQuran.AYAT_MUQATHAAT,
                mappingPosColumnName
        };

        final String selection = AyatQuran._ID + " = ?";
        final String[] selectionArgs = { Integer.toString(id) };

        final Cursor cursor = db.query(AyatQuran.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if (cursor.moveToNext()){
            try {
                return readAyatQuranFromCursor(cursor, mappingPosColumnName);
            } finally {
                cursor.close();
            }

        }

        return null;
    }

    private AyatQuran readAyatQuranFromCursor(final Cursor cursor, final String mappingPosColumnName){
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

        aqBuilder.setMappingPositions(getMappingPositions(cursor, mappingPosColumnName));

        return aqBuilder.build();
    }

    private List<Integer> getMappingPositions(final Cursor cursor, final String mappingPosColumnName){
        final String[] mappingPositionsStr = cursor
                .getString(cursor.getColumnIndex(mappingPosColumnName))
                .split(",");

        final List<Integer> result = new ArrayList<>(mappingPositionsStr.length);
        for (int i = 0; i < mappingPositionsStr.length; i++){
            result.add(Integer.parseInt(mappingPositionsStr[i]));
        }
        return result;
    }

}
