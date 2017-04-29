package org.lafzi.android.helpers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.lafzi.android.R;
import org.lafzi.android.models.MappingPosisi;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by alfat on 22/04/17.
 */

public class MappingPosisiInitiator {

    private MappingPosisiInitiator(){}

    public static void initiateTableVocalMapping(final SQLiteDatabase db){
        final String sql = "CREATE TABLE " + MappingPosisi.VOCAL_MAPPING_TABLE_NAME + " (" +
                MappingPosisi._ID + " INTEGER PRIMARY KEY," +
                MappingPosisi.POSITION + " TEXT)";

        db.execSQL(sql);
        Log.i("database", "Successfully initiated vocal_mapping_position table");
    }

    public static void initiateTableNonVocalMapping(final SQLiteDatabase db){
        final String sql = "CREATE TABLE " + MappingPosisi.NONVOCAL_MAPPING_TABLE_NAME + " (" +
                MappingPosisi._ID + " INTEGER PRIMARY KEY," +
                MappingPosisi.POSITION + " TEXT)";

        db.execSQL(sql);
        Log.i("database", "Successfully initiated nonvocal_mapping_position table");
    }

    public static void insertionTableNonVocalMapping(final Context context, final SQLiteDatabase db) throws IOException {
        insertionTableMappingPosisi(context, db, false);
    }

    public static void insertionTableVocalMapping(final Context context, final SQLiteDatabase db) throws IOException {
        insertionTableMappingPosisi(context, db, true);
    }

    private static void insertionTableMappingPosisi(final Context context, final SQLiteDatabase db, final boolean isVocal) throws IOException {

        final int rawMappingResource = isVocal ? R.raw.mapping_posisi_vokal : R.raw.mapping_posisi;
        final String tableName = isVocal ? MappingPosisi.VOCAL_MAPPING_TABLE_NAME : MappingPosisi.NONVOCAL_MAPPING_TABLE_NAME;

        final InputStream is = context.getResources().openRawResource(rawMappingResource);
        final Scanner scanner = new Scanner(is);

        int i = 1; // start 1 index
        while (scanner.hasNextLine()){
            final String positions = scanner.nextLine();

            final ContentValues values = new ContentValues();
            values.put(MappingPosisi._ID, i);
            values.put(MappingPosisi.POSITION, positions);

            db.insert(tableName, null, values);
            i++;
        }

        is.close();
        scanner.close();
    }

}
