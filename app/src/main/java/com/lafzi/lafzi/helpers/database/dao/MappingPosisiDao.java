package com.lafzi.lafzi.helpers.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lafzi.lafzi.models.MappingPosisi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alfat on 22/04/17.
 */

public class MappingPosisiDao {

    private final SQLiteDatabase db;

    public MappingPosisiDao(SQLiteDatabase db) {
        this.db = db;
    }

    public MappingPosisi getMappingPosisi(int id, boolean isVocal){

        final String tableName = isVocal ? MappingPosisi.VOCAL_MAPPING_TABLE_NAME : MappingPosisi.NONVOCAL_MAPPING_TABLE_NAME;

        final String[] projection = {
                MappingPosisi._ID,
                MappingPosisi.POSITION
        };

        final String selection = MappingPosisi._ID + " = ?";
        final String[] selectionArgs = { Integer.toString(id) };

        final Cursor cursor = db.query(tableName, projection, selection, selectionArgs, null, null, null);

        if (cursor.moveToNext()){
            return readMappingPosisiFromCursor(cursor);
        }

        return null;
    }

    private MappingPosisi readMappingPosisiFromCursor(final Cursor cursor){

        final String[] positionsStr = cursor
                .getString(cursor.getColumnIndexOrThrow(MappingPosisi.POSITION))
                .split(",");

        final List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < positionsStr.length; i++){
            positions.add(Integer.parseInt(positionsStr[i]));
        }

        return new MappingPosisi(
                cursor.getInt(cursor.getColumnIndexOrThrow(MappingPosisi._ID)),
                positions
        );
    }
}
