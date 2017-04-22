package com.lafzi.lafzi.helpers.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lafzi.lafzi.models.Index;
import com.lafzi.lafzi.models.builder.IndexBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alfat on 21/04/17.
 */

public class IndexDao {

    private final SQLiteDatabase db;

    public IndexDao(SQLiteDatabase db) {
        this.db = db;
    }

    public List<Index> getIndexByTrigramTerm(final String term, final boolean isVocal){

        final List<Index> results = new ArrayList<>();
        final String tableName = isVocal ? Index.VOCAL_INDEX_TABLE_NAME : Index.NONVOCAL_INDEX_TABLE_NAME;

        final String[] projection = {
                Index._ID,
                Index.AYAT_QURAN_ID,
                Index.TERM,
                Index.FREQUENCY,
                Index.POSITION
        };

        final String selection = Index.TERM + " = ?";
        final String[] selectionArgs = { term };

        final Cursor cursor = db.query(tableName, projection, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()){
            results.add(readIndexFromCursor(cursor));
        }

        return results;
    }

    private Index readIndexFromCursor(final Cursor cursor){
        final IndexBuilder builder = IndexBuilder.getInstance();

        builder.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Index._ID)));
        builder.setAyatQuranId(cursor.getInt(cursor.getColumnIndexOrThrow(Index.AYAT_QURAN_ID)));
        builder.setTerm(cursor.getString(cursor.getColumnIndexOrThrow(Index.TERM)));
        builder.setFrequency(cursor.getInt(cursor.getColumnIndexOrThrow(Index.FREQUENCY)));

        final String[] positionsStr = cursor
                .getString(cursor.getColumnIndexOrThrow(Index.POSITION))
                .split(",");

        final List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < positionsStr.length; i++){
            positions.add(Integer.parseInt(positionsStr[i]));
        }
        builder.setPosition(positions);

        return builder.build();
    }
}
