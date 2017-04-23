package com.lafzi.lafzi.helpers.database.dao;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by alfat on 22/04/17.
 */

public class MappingPosisiDaoFactory {

    private MappingPosisiDaoFactory(){}

    public static MappingPosisiDao createMappingPosisiDao(final SQLiteDatabase db){
        return new MappingPosisiDao(db);
    }

}
