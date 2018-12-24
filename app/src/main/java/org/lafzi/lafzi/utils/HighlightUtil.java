package org.lafzi.lafzi.utils;

import android.util.Log;

import org.lafzi.lafzi.helpers.database.dao.AyatQuranDao;
import org.lafzi.lafzi.models.AyatQuran;
import org.lafzi.lafzi.models.FoundDocument;
import org.lafzi.lafzi.models.HighlightPosition;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by alfat on 22/04/17.
 */

public class HighlightUtil {

    private HighlightUtil(){}

    public static void highlightPositions(final Map<Integer, FoundDocument> matchedDocs,
                                          final boolean isVocal,
                                          final AyatQuranDao quranDao){

        for (Map.Entry<Integer,  FoundDocument> entry : matchedDocs.entrySet()){
            final FoundDocument doc = entry.getValue();
            final AyatQuran ayatQuran = quranDao.getAyatQuran(doc.getAyatQuranId(), isVocal);

            char[] docText = {};

            try {
                docText = new String(ayatQuran
                        .getAyatArabic()
                        .getBytes("UTF-8")).toCharArray();
            } catch (UnsupportedEncodingException e) {
                Log.e("encoding", "Wrong encoding!", e);
            }

            doc.setAyatQuran(ayatQuran);

            final List<Integer> posisiReal = new ArrayList<>();

            final Set<Integer> seq = new LinkedHashSet<>();

            for (Integer pos : doc.getLis()){
                seq.add(pos);
                seq.add(pos + 1);
                seq.add(pos + 2);
            }

            final Iterator<Integer> iterator = seq.iterator();
            while (iterator.hasNext())
                posisiReal.add(ayatQuran.getMappingPositions().get(iterator.next() - 1));

            doc.setHighlightPosition(
                    longestHighlightLookforward(posisiReal, 6)
            );

            final List<HighlightPosition> hps = doc.getHighlightPosition();
            int endPosition = hps.get(hps.size() - 1).getEndHighlight();

            if (endPosition > docText.length - 3) endPosition = docText.length - 3;

            if (docText[endPosition + 1] == ' '
                    || docText.length - 1 <= endPosition + 1)
                doc.setScore(doc.getScore() + 0.001);
            else if (docText.length - 1 <= endPosition + 2
                    || docText[endPosition + 2] == ' ')
                doc.setScore(doc.getScore() + 0.001);
            else if (docText.length - 1 <= endPosition + 3
                    || docText[endPosition + 3] == ' ')
                doc.setScore(doc.getScore() + 0.001);

            entry.setValue(doc);
        }
    }

    private static List<HighlightPosition> longestHighlightLookforward(final List<Integer> highlightSequences,
                                                                       final int minLength){
        final int len = highlightSequences.size();

        if (len == 1){
            final HighlightPosition hp = new HighlightPosition();
            hp.setStartHighlight(highlightSequences.get(0));
            hp.setEndHighlight(highlightSequences.get(0) + minLength);

            return Collections.singletonList(hp);
        }

        Collections.sort(highlightSequences);

        final List<HighlightPosition> results = new ArrayList<>();
        int j = 1;

        for (int i = 0; i < len; i++){
            while (highlightSequences.size() - 1 >= j
                    && highlightSequences.get(j) - highlightSequences.get(j-1) <= minLength + 1
                    && j < len){
                j++;
            }

            final HighlightPosition hp = new HighlightPosition();
            hp.setStartHighlight(highlightSequences.get(i));
            hp.setEndHighlight(highlightSequences.get(j-1));

            results.add(hp);
            i = j - 1;
            j++;
        }

        return results;
    }

}
