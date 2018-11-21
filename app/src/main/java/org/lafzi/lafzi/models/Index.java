package org.lafzi.lafzi.models;

import android.provider.BaseColumns;

import org.json.JSONObject;

/**
 * Created by alfat on 21/04/17.
 */

public class Index implements BaseColumns {

    public static final String VOCAL_INDEX_TABLE_NAME = "vocal_index";
    public static final String NONVOCAL_INDEX_TABLE_NAME = "nonvocal_index";
    public static final String TERM = "term";
    public static final String POST = "post";

    private final String term;
    private final JSONObject post;

    public Index(String term,
                 JSONObject post) {
        this.term = term;
        this.post = post;
    }

    public String getTerm() {
        return term;
    }

    public JSONObject getPost() {
        return post;
    }
}
