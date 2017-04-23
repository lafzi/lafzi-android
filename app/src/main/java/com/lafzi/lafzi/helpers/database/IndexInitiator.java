package com.lafzi.lafzi.helpers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lafzi.lafzi.R;
import com.lafzi.lafzi.models.Index;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by alfat on 22/04/17.
 */

public class IndexInitiator {

    private IndexInitiator(){}

    public static void initiateTableVocalIndex(final SQLiteDatabase db){
        initiateTableIndex(db, true);
    }


    public static void initiateTableNonVocalIndex(final SQLiteDatabase db){
        initiateTableIndex(db, false);
    }

    private static void initiateTableIndex(final SQLiteDatabase db, final boolean isVocal){

        final String tableName = isVocal ? Index.VOCAL_INDEX_TABLE_NAME : Index.NONVOCAL_INDEX_TABLE_NAME;

        final String sql = "CREATE TABLE " + tableName + " (" +
                Index._ID + " INTEGER PRIMARY KEY," +
                Index.TERM + " TEXT," +
                Index.AYAT_QURAN_ID + " INTEGER," +
                Index.FREQUENCY + " INTEGER," +
                Index.POSITION + " TEXT)";
        db.execSQL(sql);

        final String createIndexSql = "CREATE INDEX "
                + tableName + Index.TERM_INDEX_NAME +
                " ON " + tableName +
                " (" + Index.TERM + ")";
        db.execSQL(createIndexSql);
        Log.i("database", "Successfully initiated " + tableName + " table");
    }

    public static void insertionTableNonVocalIndex(final Context context, final SQLiteDatabase db) throws IOException {
        insertionTableIndex(context, db, false);
    }

    public static void insertionTableVocalIndex(final Context context, final SQLiteDatabase db) throws IOException {
        insertionTableIndex(context, db, true);
    }

    private static void insertionTableIndex(final Context context, final SQLiteDatabase db, final boolean isVocal) throws IOException {

        final int rawIndexResource = isVocal ? R.raw.index_postlist_vokal : R.raw.index_postlist_nonvokal;
        final int rawTermResource = isVocal ? R.raw.index_termlist_vokal : R.raw.index_termlist_nonvokal;
        final String tableName = isVocal ? Index.VOCAL_INDEX_TABLE_NAME : Index.NONVOCAL_INDEX_TABLE_NAME;

        final InputStream inputStreamIndex = context.getResources().openRawResource(rawIndexResource);
        final InputStream inputStreamTerm = context.getResources().openRawResource(rawTermResource);

        final Scanner scannerIndex = new Scanner(inputStreamIndex);
        final Scanner scannerTerm = new Scanner(inputStreamTerm);

        while (scannerTerm.hasNextLine()){
            final String[] term = scannerTerm.nextLine().split("\\|");
            final String[] indexes = scannerIndex.nextLine().split(";");

            for (int i = 0; i < indexes.length; i++){

                final String[] index = indexes[i].split(":");

                final ContentValues values = new ContentValues();
                values.put(Index.TERM, term[0]);
                values.put(Index.AYAT_QURAN_ID, index[0]);
                values.put(Index.FREQUENCY, index[1]);
                values.put(Index.POSITION, index[2]);

                db.insert(
                        tableName,
                        null,
                        values
                );
            }
        }

        inputStreamIndex.close();
        inputStreamTerm.close();
        scannerIndex.close();
        scannerTerm.close();
    }

}
