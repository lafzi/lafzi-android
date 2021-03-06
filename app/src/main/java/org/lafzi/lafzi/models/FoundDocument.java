package org.lafzi.lafzi.models;

import java.util.List;
import java.util.Map;

/**
 * Created by alfat on 21/04/17.
 */

public class FoundDocument {

    private int ayatQuranId;
    private int matchedTrigramsCount;
    private int matchedTermsOrderScore;
    private int matchedTermsCountScore;
    private double matchedTermsContiguityScore;
    private double score;
    private Map<String, List<Integer>> matchedTerms;
    private List<Integer> lis;
    private List<HighlightPosition> highlightPosition;
    private AyatQuran ayatQuran;

    public int getAyatQuranId() {
        return ayatQuranId;
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

    public double getMatchedTermsContiguityScore() {
        return matchedTermsContiguityScore;
    }

    public double getScore() {
        return score;
    }

    public Map<String, List<Integer>> getMatchedTerms() {
        return matchedTerms;
    }

    public List<Integer> getLis() {
        return lis;
    }

    public List<HighlightPosition> getHighlightPosition() {
        return highlightPosition;
    }

    public AyatQuran getAyatQuran() {
        return ayatQuran;
    }

    public void setAyatQuranId(int ayatQuranId) {
        this.ayatQuranId = ayatQuranId;
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

    public void setMatchedTermsContiguityScore(double matchedTermsContiguityScore) {
        this.matchedTermsContiguityScore = matchedTermsContiguityScore;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setMatchedTerms(Map<String, List<Integer>> matchedTerms) {
        this.matchedTerms = matchedTerms;
    }

    public void setLis(List<Integer> lis) {
        this.lis = lis;
    }

    public void setHighlightPosition(List<HighlightPosition> highlightPosition) {
        this.highlightPosition = highlightPosition;
    }

    public void setAyatQuran(AyatQuran ayatQuran) {
        this.ayatQuran = ayatQuran;
    }
}
