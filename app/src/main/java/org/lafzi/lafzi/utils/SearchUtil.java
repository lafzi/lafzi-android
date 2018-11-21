package org.lafzi.lafzi.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.lafzi.lafzi.helpers.database.dao.IndexDao;
import org.lafzi.lafzi.models.FoundDocument;
import org.lafzi.lafzi.models.FreqAndPosition;
import org.lafzi.lafzi.models.Index;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by alfat on 22/04/17.
 */

public class SearchUtil {

    private SearchUtil(){}

    public static Map<Integer, FoundDocument> search(final String queryFinal,
                                                     final boolean isVocal,
                                                     final boolean orderedByScore,
                                                     final boolean filtered,
                                                     final double filterThreshold,
                                                     final IndexDao indexDao) throws JSONException {

        final Map<Integer, FoundDocument> matchedDocs = new HashMap<>();

        // get trigram with frequency and positions
        final Map<String, FreqAndPosition> trigrams = TrigramUtil.extractTrigramFrequencyAndPosition(queryFinal);

        for (final Map.Entry<String, FreqAndPosition> trigram : trigrams.entrySet()){
            final String term = trigram.getKey();

            // Index from SQLite
            final Index index = indexDao.getIndexByTrigramTerm(term, isVocal);
            final Iterator<String> indexIterator = index.getPost().keys();

            while (indexIterator.hasNext()){

                FoundDocument document = new FoundDocument();

                final String ayatQuranIdStr = indexIterator.next();
                final Integer ayatQuranId = Integer.parseInt(ayatQuranIdStr);
                final JSONArray positions = index.getPost().getJSONArray(ayatQuranIdStr);

                if (matchedDocs.containsKey(ayatQuranId)){

                    document = matchedDocs.get(ayatQuranId);
                    int matchedTrigrams = document.getMatchedTrigramsCount();
                    final int queryTrigramFreq = trigram.getValue().getFreq();
                    final int termFreq = positions.length();

                    matchedTrigrams += (queryTrigramFreq < termFreq) ? queryTrigramFreq : termFreq;
                    document.setMatchedTrigramsCount(matchedTrigrams);
                } else {
                    document.setMatchedTrigramsCount(1);
                    document.setAyatQuranId(ayatQuranId);
                }

                final Map<String, List<Integer>> matchedTerms = document.getMatchedTerms() == null ?
                        new HashMap<String, List<Integer>>() : document.getMatchedTerms();

                matchedTerms.put(trigram.getKey(), GeneralUtil.readIndexPositions(positions));
                document.setMatchedTerms(matchedTerms);

                matchedDocs.put(ayatQuranId, document);
            }

        }

        // if filtered, only take docs match above threshold
        final Map<Integer, FoundDocument> filteredDocs = new HashMap<>();
        final double minScore = filterThreshold * (queryFinal.length() - 2);

        // scoring based on match trigrams and contiguous
        if (orderedByScore){
            for (Map.Entry<Integer, FoundDocument> entry : matchedDocs.entrySet()){

                final FoundDocument foundDocument = entry.getValue();
                foundDocument.setMatchedTermsCountScore(
                        foundDocument.getMatchedTrigramsCount()
                );

                final List<Integer> flattenMatchedTerms = arrayValuesFlatten(foundDocument.getMatchedTerms());
                final List<Integer> lis = longestContiguousSubsequence(flattenMatchedTerms, 7);

                foundDocument.setMatchedTermsOrderScore(lis.size());
                foundDocument.setLis(lis);

                final double contiguityScore = reciprocalDiffAverage(lis);
                foundDocument.setMatchedTermsContiguityScore(contiguityScore);

                foundDocument.setScore(
                        foundDocument.getMatchedTermsOrderScore() * foundDocument.getMatchedTermsContiguityScore()
                );

                if (filtered && (foundDocument.getMatchedTrigramsCount() >= minScore)){
                    filteredDocs.put(entry.getKey(), foundDocument);
                }
            }
        } else {
            for (Map.Entry<Integer, FoundDocument> entry : matchedDocs.entrySet()){
                final FoundDocument foundDocument = entry.getValue();
                foundDocument.setMatchedTermsCountScore(
                        foundDocument.getMatchedTrigramsCount()
                );
                foundDocument.setScore(
                        foundDocument.getMatchedTermsCountScore()
                );

                matchedDocs.put(entry.getKey(), foundDocument);

                if (filtered && (foundDocument.getMatchedTrigramsCount() >= minScore)){
                    filteredDocs.put(entry.getKey(), foundDocument);
                }
            }
        }

        if (filtered) return filteredDocs;
        return matchedDocs;
    }

    private static List<Integer> arrayValuesFlatten(final Map<String, List<Integer>> matchedTerms){
        final List<Integer> results = new ArrayList<>();
        for (List<Integer> sequence : matchedTerms.values()){
            results.addAll(sequence);
        }
        return results;
    }

    private static List<Integer> longestContiguousSubsequence(final List<Integer> sequence,
                                                              final int maxGap){
        Collections.sort(sequence);
        final int size = sequence.size();
        int start = 0;
        int length = 0;
        int maxStart = 0;
        int maxLength = 0;

        for (int i = 0; i < size - 1; i++){
            if ((sequence.get(i+1) - sequence.get(i)) > maxGap){
                length = 0;
                start = i + 1;
            } else {
                length++;
                if (length > maxLength){
                    maxLength = length;
                    maxStart = start;
                }
            }
        }

        maxLength++;
        return sequence.subList(maxStart, maxStart + maxLength);
    }

    private static double reciprocalDiffAverage(final List<Integer> lis){
        final List<Double> diffs = new ArrayList<>();
        final int len = lis.size();

        if (len == 1) return 1;

        for (int i = 0; i < len-1; i++)
            diffs.add(
                    1.0 / (lis.get(i+1) - lis.get(i))
            );

        double totalDiff = 0;
        for (Double diff : diffs){
            totalDiff += diff;
        }

        return totalDiff / (len - 1);
    }

}
