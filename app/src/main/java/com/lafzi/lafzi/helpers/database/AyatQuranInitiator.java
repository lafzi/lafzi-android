package com.lafzi.lafzi.helpers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.SparseArray;

import com.lafzi.lafzi.R;
import com.lafzi.lafzi.models.AyatQuran;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by alfat on 22/04/17.
 */

public class AyatQuranInitiator {

    private AyatQuranInitiator(){}

    public static void initiateTableAyatQuran(final SQLiteDatabase db){
        final String sql = "CREATE TABLE " + AyatQuran.TABLE_NAME + " (" +
                AyatQuran._ID + " INTEGER PRIMARY KEY," +
                AyatQuran.SURAH_NO + " INTEGER," +
                AyatQuran.SURAH_NAME + " TEXT," +
                AyatQuran.AYAT_NO + " INTEGER," +
                AyatQuran.AYAT_ARABIC + " TEXT," +
                AyatQuran.AYAT_INDONESIAN + " TEXT," +
                AyatQuran.AYAT_MUQATHAAT + " TEXT)";

        db.execSQL(sql);
        Log.i("database", "Successfully initiated ayat_quran table");
    }

    public static void insertionTableAyatQuran(final Context context, final SQLiteDatabase db) throws IOException {

        final SparseArray<SparseArray<String>> muqathaatMap = createMuqathaatMap(context);

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

            final int surahNo = Integer.parseInt(quranText[0]);
            final int ayahNo = Integer.parseInt(quranText[2]);

            if (muqathaatMap
                    .get(surahNo, new SparseArray<String>())
                    .get(ayahNo, null) != null){
                contentValues.put(
                        AyatQuran.AYAT_MUQATHAAT,
                        muqathaatMap.get(surahNo).get(ayahNo)
                );
            }

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
        muqathaatMap.clear();
    }

    private static SparseArray<SparseArray<String>> createMuqathaatMap(final Context context) throws IOException {

        final InputStream inputStream = context
                .getResources()
                .openRawResource(R.raw.quran_muqathaat);

        final Scanner scanner = new Scanner(inputStream);
        final SparseArray<SparseArray<String>> surahMap = new SparseArray<>();

        while (scanner.hasNextLine()){
            final String[] line = scanner.nextLine().split("\\|");
            final int surahNo = Integer.parseInt(line[0]);
            final int ayahNo = Integer.parseInt(line[2]);
            final String ayah = line[3];

            final SparseArray<String> ayahMap = surahMap.get(surahNo, new SparseArray<String>());
            ayahMap.put(ayahNo, ayah);
            surahMap.put(surahNo, ayahMap);
        }

        inputStream.close();
        scanner.close();
        return surahMap;
    }

}
