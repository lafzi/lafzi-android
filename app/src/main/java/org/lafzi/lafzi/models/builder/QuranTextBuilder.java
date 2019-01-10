package org.lafzi.lafzi.models.builder;

import org.lafzi.lafzi.models.QuranText;

import java.util.List;

public class QuranTextBuilder {

    private int docId;
    private String text;
    private String pos;
    private int fullLength;
    private int shortLength;
    private byte[] subsequence;

    private QuranTextBuilder(){};

    private static final QuranTextBuilder quranTextBuilder = new QuranTextBuilder();

    public static QuranTextBuilder getInstance() {return quranTextBuilder;}

    public QuranTextBuilder setDocId(int docId) {
        this.docId = docId;
        return this;
    }

    public QuranTextBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public QuranTextBuilder setPos(String pos) {
        this.pos = pos;
        return this;
    }

    public QuranTextBuilder setFullLength(int fullLength) {
        this.fullLength = fullLength;
        return this;
    }

    public QuranTextBuilder setShortLength(int shortLength) {
        this.shortLength = shortLength;
        return this;
    }

    public QuranTextBuilder setSubsequence(byte[] val) {
        this.subsequence = val;
        return this;
    }

    public QuranText build() {
        return new QuranText(
                this.docId,
                this.text,
                this.pos,
                this.fullLength,
                this.shortLength,
                this.subsequence
        );
    }
}
