package org.lafzi.lafzi.helpers.database;

import org.lafzi.lafzi.models.AyatQuran;

import java.util.List;

public class AllAyatHandler {

    private static AllAyatHandler singleton;

    private List<AyatQuran> allAyats;

    private AllAyatHandler(){}

    public static AllAyatHandler getInstance() {
        if (singleton == null) {
            singleton = new AllAyatHandler();
        }
        return singleton;
    }

    public void setAllAyat(List<AyatQuran> allAyat) {
        this.allAyats = allAyat;
    }

    public List<AyatQuran> getAllAyats() {
        return allAyats;
    }

}
