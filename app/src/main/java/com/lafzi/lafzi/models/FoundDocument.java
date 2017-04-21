package com.lafzi.lafzi.models;

import java.util.List;

/**
 * Created by alfat on 21/04/17.
 */

public class FoundDocument {

    private String id;
    private int matchedTrigramsCount;
    private int matchedTermsOrderScore;
    private int matchedTermsCountScore;
    private int matchedTermsContiguityScore;
    private int score;
    private List<String> terms;
    private List<Integer> lis;
    private List<Integer> highlightPosition;

    public String getId() {
        return id;
    }

    public int getMatchedTrigramsCount() {
        return matchedTrigramsCount;
    }

    public int getMatchedTermsOrderScore() {
        return matchedTermsOrderScore;
    }

    public int getMatchedTermsCountScore() {
        return matchedTermsCountScore;
    }

    public int getMatchedTermsContiguityScore() {
        return matchedTermsContiguityScore;
    }

    public int getScore() {
        return score;
    }

    public List<String> getTerms() {
        return terms;
    }

    public List<Integer> getLis() {
        return lis;
    }

    public List<Integer> getHighlightPosition() {
        return highlightPosition;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMatchedTrigramsCount(int matchedTrigramsCount) {
        this.matchedTrigramsCount = matchedTrigramsCount;
    }

    public void setMatchedTermsOrderScore(int matchedTermsOrderScore) {
        this.matchedTermsOrderScore = matchedTermsOrderScore;
    }

    public void setMatchedTermsCountScore(int matchedTermsCountScore) {
        this.matchedTermsCountScore = matchedTermsCountScore;
    }

    public void setMatchedTermsContiguityScore(int matchedTermsContiguityScore) {
        this.matchedTermsContiguityScore = matchedTermsContiguityScore;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setTerms(List<String> terms) {
        this.terms = terms;
    }

    public void setLis(List<Integer> lis) {
        this.lis = lis;
    }

    public void setHighlightPosition(List<Integer> highlightPosition) {
        this.highlightPosition = highlightPosition;
    }
}
