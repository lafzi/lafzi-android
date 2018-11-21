package org.lafzi.lafzi.utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alfat on 30/04/17.
 */

public class GeneralUtil {

    public static List<Integer> readIndexPositions(final JSONArray positions) throws JSONException {

        final List<Integer> result = new ArrayList<>(200);
        for (int i = 0; i < positions.length(); i++){
            int pos = (Integer) positions.get(i);
            result.add(pos);
        }
        return result;
    }

    public static boolean isNullOrEmpty(final String input){

        if (input != null && input.length() > 0){
            return false;
        }
        return true;
    }

}
