package com.lafzi.lafzi.helpers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lafzi.lafzi.R;
import com.lafzi.lafzi.models.AyatQuran;
import com.lafzi.lafzi.models.Index;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by alfat on 21/04/17.
 */

public class DbInitiator {

    public static void initiateTableAyatQuran(final SQLiteDatabase db){
        final String sql = "CREATE TABLE " + AyatQuran.TABLE_NAME + " (" +
                AyatQuran._ID + " INTEGER PRIMARY KEY," +
                AyatQuran.SURAH_NO + " INTEGER," +
                AyatQuran.SURAH_NAME + " TEXT," +
                AyatQuran.AYAT_NO + " INTEGER," +
                AyatQuran.AYAT_ARABIC + " TEXT," +
                AyatQuran.AYAT_INDONESIAN + " TEXT)";

        db.execSQL(sql);
        Log.i("database", "Successfully initiated ayat_quran table");
    }

    public static void initiateTableVocalIndex(final SQLiteDatabase db){
        final String sql = "CREATE TABLE " + Index.VOCAL_INDEX_TABLE_NAME + " (" +
                Index._ID + " INTEGER PRIMARY KEY," +
                Index.TERM + " TEXT," +
                Index.AYAT_QURAN_ID + " INTEGER," +
                Index.FREQUENCY + " INTEGER," +
                Index.POSITION + " TEXT)";
        db.execSQL(sql);

        final String createIndexSql = "CREATE INDEX "
                + Index.VOCAL_INDEX_TABLE_NAME + Index.TERM_INDEX_NAME +
                " ON " + Index.VOCAL_INDEX_TABLE_NAME +
                " (" + Index.TERM + ")";
        db.execSQL(createIndexSql);
        Log.i("database", "Successfully initiated vocal_index table");
    }


    public static void initiateTableNonVocalIndex(final SQLiteDatabase db){
        final String sql = "CREATE TABLE " + Index.NONVOCAL_INDEX_TABLE_NAME+ " (" +
                Index._ID + " INTEGER PRIMARY KEY," +
                Index.TERM + " TEXT," +
                Index.AYAT_QURAN_ID + " INTEGER," +
                Index.FREQUENCY + " INTEGER," +
                Index.POSITION + " TEXT)";
        db.execSQL(sql);

        final String createIndexSql = "CREATE INDEX "
                + Index.NONVOCAL_INDEX_TABLE_NAME + Index.TERM_INDEX_NAME +
                " ON " + Index.NONVOCAL_INDEX_TABLE_NAME +
                " (" + Index.TERM + ")";
        db.execSQL(createIndexSql);
        Log.i("database", "Successfully initiated nonvocal_index table");
    }

    public static void insertionTableAyatQuran(final Context context, final SQLiteDatabase db) throws IOException {

        final InputStream inputStreamQuranText = context
                .getResources()
                .openRawResource(R.raw.quran_teks);
        final InputStream inputStreamQuranTranslate = context
                .getResources()
                .openRawResource(R.raw.trans_indonesian);

        final Scanner scannerInputStreamQuranText = new Scanner(inputStreamQuranText);
        final Scanner scannerInputStreamQuranTranslate = new Scanner(inputStreamQuranTranslate);

        int i = 1; //start 1 index
        while (scannerInputStreamQuranText.hasNextLine()){
            final String[] quranText = scannerInputStreamQuranText
                    .nextLine()
                    .split("\\|");
            final String[] quranTranslate = scannerInputStreamQuranTranslate
                    .nextLine()
                    .split("\\|");

            final ContentValues contentValues = new ContentValues();
            contentValues.put(AyatQuran._ID, i);
            contentValues.put(AyatQuran.SURAH_NO, quranText[0]);
            contentValues.put(AyatQuran.SURAH_NAME, quranText[1]);
            contentValues.put(AyatQuran.AYAT_NO, quranText[2]);
            contentValues.put(AyatQuran.AYAT_ARABIC, quranText[3]);
            contentValues.put(AyatQuran.AYAT_INDONESIAN, quranTranslate[2]);

            db.insert(
                    AyatQuran.TABLE_NAME,
                    null,
                    contentValues);
            i++;
        }

        inputStreamQuranText.close();
        inputStreamQuranTranslate.close();
        scannerInputStreamQuranText.close();
        scannerInputStreamQuranTranslate.close();
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
