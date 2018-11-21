package org.lafzi.lafzi.models.builder;

import org.lafzi.lafzi.models.AyatQuran;

import java.util.List;

/**
 * Created by alfat on 21/04/17.
 */

public class AyatQuranBuilder {

    private int id;
    private int surahNo;
    private String surahName;
    private int ayatNo;
    private String ayatArabic;
    private String ayatIndonesian;
    private String ayatMuqathaat;
    private List<Integer> mappingPositions;

    private AyatQuranBuilder(){}

    private static final AyatQuranBuilder ayatQuranBuilder = new AyatQuranBuilder();

    public static AyatQuranBuilder getInstance(){
        return ayatQuranBuilder;
    }

    public AyatQuranBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public AyatQuranBuilder setSurahNo(int surahNo) {
        this.surahNo = surahNo;
        return this;
    }

    public AyatQuranBuilder setSurahName(String surahName) {
        this.surahName = surahName;
        return this;
    }

    public AyatQuranBuilder setAyatNo(int ayatNo) {
        this.ayatNo = ayatNo;
        return this;
    }

    public AyatQuranBuilder setAyatArabic(String ayatArabic) {
        this.ayatArabic = ayatArabic;
        return this;
    }

    public AyatQuranBuilder setAyatIndonesian(String ayatIndonesian) {
        this.ayatIndonesian = ayatIndonesian;
        return this;
    }

    public AyatQuranBuilder setAyatMuqathaat(String ayatMuqathaat) {
        this.ayatMuqathaat = ayatMuqathaat;
        return this;
    }

    public AyatQuranBuilder setMappingPositions(List<Integer> mappingPositions){
        this.mappingPositions = mappingPositions;
        return this;
    }

    public AyatQuran build(){
        return new AyatQuran(
                id,
                surahNo,
                surahName,
                ayatNo,
                ayatArabic,
                ayatIndonesian,
                ayatMuqathaat,
                mappingPositions
        );
    }
}