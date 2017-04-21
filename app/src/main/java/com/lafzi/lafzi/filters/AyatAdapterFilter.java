package com.lafzi.lafzi.filters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Filter;

import com.lafzi.lafzi.helpers.database.DbHelper;
import com.lafzi.lafzi.helpers.database.dao.AyatQuranDao;
import com.lafzi.lafzi.helpers.database.dao.AyatQuranDaoFactory;
import com.lafzi.lafzi.helpers.database.dao.IndexDao;
import com.lafzi.lafzi.helpers.database.dao.IndexDaoFactory;
import com.lafzi.lafzi.models.AyatQuran;
import com.lafzi.lafzi.models.FoundDocument;
import com.lafzi.lafzi.models.FreqAndPosition;
import com.lafzi.lafzi.models.Index;
import com.lafzi.lafzi.utils.QueryUtil;
import com.lafzi.lafzi.utils.TrigramUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alfat on 21/04/17.
 */

public class AyatAdapterFilter extends Filter {

    private final AyatQuranDao ayatQuranDao;
    private final IndexDao indexDao;

    public AyatAdapterFilter(final Context context){
        final DbHelper dbHelper = new DbHelper(context);
        final SQLiteDatabase db = dbHelper.getReadableDatabase();

        ayatQuranDao = AyatQuranDaoFactory.createAyatDao(db);
        indexDao = IndexDaoFactory.createIndexDao(db);
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        final FilterResults results = new FilterResults();
        final Map<String, FoundDocument> matchedDocs = new HashMap<>();

        // transform query
        final String transformedQuery = QueryUtil.normalizeQuery(constraint.toString(), true);
        // get trigram with frequency and positions
        final Map<String, FreqAndPosition> trigrams = TrigramUtil.extractTrigramFrequencyAndPosition(transformedQuery);

        if (trigrams.size() < 1){
            results.count = 0;
            results.values = Collections.EMPTY_LIST;
            return results;
        }

        for (final Map.Entry<String, FreqAndPosition> trigram : trigrams.entrySet()){
            final String term = trigram.getKey();
            final List<Index> indices = indexDao.getIndexByTrigramTerm(term, true);

            for (Index index : indices){
                final int ayatQuranId = Integer.parseInt(index.getAyatQuranId());
                final int termFreq = Integer.parseInt(index.getFrequency());
                final FoundDocument document = new FoundDocument();

            }

        }

    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

    }
}
