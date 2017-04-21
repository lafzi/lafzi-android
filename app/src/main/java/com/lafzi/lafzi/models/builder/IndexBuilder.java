package com.lafzi.lafzi.models.builder;

import com.lafzi.lafzi.models.Index;

import java.util.List;

/**
 * Created by alfat on 21/04/17.
 */

public class IndexBuilder {

    private int id;
    private String term;
    private int ayatQuranId;
    private int frequency;
    private List<Integer> position;

    private static final IndexBuilder instance = new IndexBuilder();

    private IndexBuilder(){}

    public static IndexBuilder getInstance(){
        return instance;
    }

    public IndexBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public IndexBuilder setTerm(String term) {
        this.term = term;
        return this;
    }

    public IndexBuilder setAyatQuranId(int ayatQuranId) {
        this.ayatQuranId = ayatQuranId;
        return this;
    }

    public IndexBuilder setFrequency(int frequency) {
        this.frequency = frequency;
        return this;
    }

    public IndexBuilder setPosition(List<Integer> position) {
        this.position = position;
        return this;
    }

    public Index build(){
        return new Index(id, term, ayatQuranId, frequency, position);
    }
}
