package com.lafzi.lafzi.model;

/**
 * Created by alfat on 19/04/17.
 */

public class Ayat {

    private final String arabic;
    private final String indonesia;

    public Ayat(final String indonesia, final String arabic){
        this.indonesia = indonesia;
        this.arabic = arabic;
    }

    public String getArabic() {
        return arabic;
    }

    public String getIndonesia() {
        return indonesia;
    }
}
