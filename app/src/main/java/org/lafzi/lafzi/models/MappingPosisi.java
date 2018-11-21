package org.lafzi.lafzi.models;

import android.provider.BaseColumns;

import java.util.List;

/**
 * Created by alfat on 22/04/17.
 */

public class MappingPosisi implements BaseColumns {

    public static final String VOCAL_MAPPING_TABLE_NAME = "vocal_mapping_position";
    public static final String NONVOCAL_MAPPING_TABLE_NAME = "nonvocal_mapping_position";
    public static final String POSITION = "position";

    private final int id;
    private final List<Integer> positions;

    public MappingPosisi(int id, List<Integer> positions) {
        this.id = id;
        this.positions = positions;
    }

    public int getId() {
        return id;
    }

    public List<Integer> getPositions() {
        return positions;
    }
}
