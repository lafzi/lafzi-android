package org.lafzi.lafzi.models;

import android.util.Log;

import org.lafzi.lafzi.utils.SearchUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class QuranText implements Serializable {

    public static final String DOCID = "docid";
    public static final String TEXT = "text";
    public static final String POS = "pos";
    public static final String FULL_LENGTH = "full_length";
    public static final String SHORT_LENGTH = "short_length";
    public static final String SUBSEQUENCE = "subsequence";

    private final int docId;
    private final String text;
    private final List<Integer> pos;
    private final int fullLength;
    private final int shortLength;
    private final int score;

    public QuranText(int docid, String text, String pos, int fullLength, int shortLength, byte[] subsequence) {
        this.docId = docid;
        this.text = text;
        this.fullLength = fullLength;
        this.shortLength = shortLength;
        this.pos = splitOffsets(pos);
        this.score = subsequence[0];
    }

    private List<Integer> splitOffsets(String offset) {
        List<Integer> results = new ArrayList<>();
        String[] splitted = offset.split(" ");
        int biggestMinus = 0;
        for (int i = 0; i < splitted.length; i += 4) {
            int index = i + 2;
            int pos = Integer.valueOf(splitted[index]);
            int size = Integer.valueOf(splitted[index + 1]);

            double temp = shortLength - pos - size * ((double)fullLength / shortLength);
            pos = (int) temp;

            if (pos < biggestMinus) biggestMinus = pos;

            for (int j = 0; j < size; j++) {
                results.add(pos + j);
            }
        }

        if (biggestMinus < 0) {
            int index = 0;
            for (Integer res : results) {
                int newRes = res - biggestMinus;
                results.set(index, newRes);
                index++;
            }
        }

        results = SearchUtil.longestContiguousSubsequence(results, 7);
        return results;
    }

    public int getDocId() {
        return docId;
    }

    public String getText() {
        return text;
    }

    public List<Integer> getPos() {
        return pos;
    }

    public int getFullLength() {
        return fullLength;
    }

    public int getShortLength() {
        return shortLength;
    }

    public int getScore() {
        return score;
    }
}
