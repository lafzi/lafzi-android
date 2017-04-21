package com.lafzi.lafzi.helpers.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;

/**
 * Created by alfat on 21/04/17.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "lafzi.sqlite";
    private Context context = null;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            DbInitiator.initiateTableAyatQuran(db);
            DbInitiator.initiateTableNonVocalIndex(db);
            DbInitiator.initiateTableVocalIndex(db);

            DbInitiator.insertionTableAyatQuran(context, db);
            DbInitiator.insertionTableNonVocalIndex(context, db);
            DbInitiator.insertionTableVocalIndex(context, db);
        } catch (IOException e) {
            Log.e("database", "Failed to initiate database!", e);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
