package org.lafzi.lafzi.helpers.database.dao;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by alfat on 21/04/17.
 */

public class AyatQuranDaoFactory {

    private AyatQuranDaoFactory(){}

    public static AyatQuranDao createAyatDao(final SQLiteDatabase db){

        return new AyatQuranDao(db);
    }

}
