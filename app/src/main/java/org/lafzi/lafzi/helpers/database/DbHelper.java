package org.lafzi.lafzi.helpers.database;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import org.lafzi.lafzi.LafziApplication;

/**
 * Created by alfat on 21/04/17.
 */

public class DbHelper extends SQLiteAssetHelper {

    private static DbHelper instance;

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "lafzi.sqlite";

    private final Context context;

    public static DbHelper getInstance(){
        if (instance == null){
            instance = new DbHelper(LafziApplication.getLafziContext());
        }
        return instance;
    }

    private DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        setForcedUpgrade();
    }

}
