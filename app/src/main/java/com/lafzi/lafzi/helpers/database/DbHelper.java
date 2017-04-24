package com.lafzi.lafzi.helpers.database;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lafzi.lafzi.R;

import java.io.IOException;

/**
 * Created by alfat on 21/04/17.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static DbHelper instance;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "lafzi.sqlite";

    private final Context context;
    private final ProgressBar migrationProgressBar;
    private final TextView migrationTextView;

    public static synchronized DbHelper getInstance(Context context){
        if (instance == null){
            instance = new DbHelper(context);
        }
        return instance;
    }

    private DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

        migrationProgressBar = (ProgressBar) ((Activity)context)
                .findViewById(R.id.migration_progress_bar);
        migrationTextView = (TextView) ((Activity) context).findViewById(R.id.migration_text);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            migrationProgressBar.setVisibility(View.VISIBLE);
            migrationTextView.setVisibility(View.VISIBLE);

            AyatQuranInitiator.initiateTableAyatQuran(db);
            migrationProgressBar.setProgress(5);
            IndexInitiator.initiateTableNonVocalIndex(db);
            migrationProgressBar.setProgress(10);
            IndexInitiator.initiateTableVocalIndex(db);
            migrationProgressBar.setProgress(15);
            MappingPosisiInitiator.initiateTableNonVocalMapping(db);
            migrationProgressBar.setProgress(20);
            MappingPosisiInitiator.initiateTableVocalMapping(db);
            migrationProgressBar.setProgress(25);

            AyatQuranInitiator.insertionTableAyatQuran(context, db);
            migrationProgressBar.setProgress(40);
            IndexInitiator.insertionTableNonVocalIndex(context, db);
            migrationProgressBar.setProgress(60);
            IndexInitiator.insertionTableVocalIndex(context, db);
            migrationProgressBar.setProgress(80);
            MappingPosisiInitiator.insertionTableNonVocalMapping(context, db);
            migrationProgressBar.setProgress(90);
            MappingPosisiInitiator.insertionTableVocalMapping(context, db);
            migrationProgressBar.setProgress(100);
        } catch (IOException e) {
            Log.e("database", "Failed to initiate database!", e);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
