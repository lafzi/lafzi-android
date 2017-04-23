package com.lafzi.lafzi.utils;

import com.lafzi.lafzi.models.FreqAndPosition;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by alfat on 21/04/17.
 */

public class TrigramUtil {

    public static Map<String, FreqAndPosition> extractTrigramFrequencyAndPosition(final String text){

        final Map<String, FreqAndPosition> results = new HashMap<>();

        if (text.length() < 3) return results;
        if (text.length() == 3){
            final FreqAndPosition fp = new FreqAndPosition(1, Collections.singletonList(1));
            results.put(text, fp);
            return results;
        }

        for (int i = 0; i < text.length() - 2; i++){
            final String trigram = text.substring(i, i + 3);

            FreqAndPosition newFp;
            if (results.containsKey(trigram)){
                FreqAndPosition fp = results.get(trigram);
                int freq = fp.getFreq();
                List<Integer> positions = fp.getPositions();

                positions.add(i);
                newFp = new FreqAndPosition(freq + 1, positions);
            } else {
                newFp = new FreqAndPosition(1, new LinkedList<>(Collections.singletonList(i)));
            }
            results.put(trigram, newFp);
        }

        return results;
    }

}
