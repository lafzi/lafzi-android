package com.lafzi.lafzi.models;

import java.util.List;

/**
 * Created by alfat on 21/04/17.
 */

public class FreqAndPosition {

    private final int freq;
    private final List<Integer> positions;

    public FreqAndPosition(int freq, List<Integer> positions) {
        this.freq = freq;
        this.positions = positions;
    }

    public int getFreq() {
        return freq;
    }

    public List<Integer> getPositions() {
        return positions;
    }
}
