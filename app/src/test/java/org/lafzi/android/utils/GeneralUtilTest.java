package org.lafzi.android.utils;

import org.json.JSONArray;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by alfat on 01/05/17.
 */
public class GeneralUtilTest {

    @Test
    public void readIndexPositions() throws Exception {
        final String jsonArrayStr = "[1, 3, 4, 2]";
        final JSONArray jsonArray = new JSONArray(jsonArrayStr);
        final List<Integer> results = GeneralUtil.readIndexPositions(jsonArray);

        assertThat(results.size(), equalTo(4));

        assertThat(results.get(0), equalTo(1));
        assertThat(results.get(1), equalTo(3));
        assertThat(results.get(2), equalTo(4));
        assertThat(results.get(3), equalTo(2));
    }

    @Test
    public void isNullOrEmpty() throws Exception {
        assertThat(GeneralUtil.isNullOrEmpty(""), is(true));
        assertThat(GeneralUtil.isNullOrEmpty(null), is(true));
        assertThat(GeneralUtil.isNullOrEmpty("with value"), is(false));
    }

}