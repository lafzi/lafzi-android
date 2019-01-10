package org.lafzi.lafzi.helpers.database.dao;

import android.database.sqlite.SQLiteDatabase;

public class QuranTextDaoFactory {

    private QuranTextDaoFactory(){}

    public static QuranTextDao createQuranTextDao(final SQLiteDatabase db) {
        return new QuranTextDao(db);
    }

}
