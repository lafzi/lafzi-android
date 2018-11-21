package org.lafzi.lafzi.helpers.database.dao;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by alfat on 21/04/17.
 */

public class IndexDaoFactory {

    private IndexDaoFactory(){}

    public static IndexDao createIndexDao(final SQLiteDatabase db){
        return new IndexDao(db);
    }

}
