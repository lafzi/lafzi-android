package org.lafzi.android.utils;

import org.junit.Test;
import org.lafzi.lafzi.utils.QueryUtil;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by alfat on 01/05/17.
 */
public class QueryUtilTest {

    @Test
    public void normalizeVocal(){
        String result = QueryUtil.normalizeQuery("bismillah", true);
        assertThat(result, equalTo("BISMILAH"));

        result = QueryUtil.normalizeQuery("zzzzzzz", true);
        assertThat(result, equalTo("Z"));

        result = QueryUtil.normalizeQuery("Adzaabis saiir", true);
        assertThat(result, equalTo("XAZABISAYR"));
    }

    @Test
    public void normalizeNonvocal(){
        String result = QueryUtil.normalizeQuery("bismillah", false);
        assertThat(result, equalTo("BSMLH"));

        result = QueryUtil.normalizeQuery("MuNafiquun", false);
        assertThat(result, equalTo("MNFKN"));

        result = QueryUtil.normalizeQuery("wallahi", false);
        assertThat(result, equalTo("WLH"));
    }

}