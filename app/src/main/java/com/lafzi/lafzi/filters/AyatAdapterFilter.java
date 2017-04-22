package com.lafzi.lafzi.filters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Filter;

import com.lafzi.lafzi.adapters.AyatAdapter;
import com.lafzi.lafzi.helpers.database.DbHelper;
import com.lafzi.lafzi.helpers.database.dao.AyatQuranDao;
import com.lafzi.lafzi.helpers.database.dao.AyatQuranDaoFactory;
import com.lafzi.lafzi.helpers.database.dao.IndexDao;
import com.lafzi.lafzi.helpers.database.dao.IndexDaoFactory;
import com.lafzi.lafzi.models.AyatQuran;
import com.lafzi.lafzi.models.FoundDocument;
import com.lafzi.lafzi.utils.SearchUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alfat on 21/04/17.
 */

public class AyatAdapterFilter extends Filter {

    private final AyatQuranDao ayatQuranDao;
    private final IndexDao indexDao;
    private AyatAdapter adapter;

    public AyatAdapterFilter(final Context context, final AyatAdapter adapter){
        final DbHelper dbHelper = new DbHelper(context);
        final SQLiteDatabase db = dbHelper.getReadableDatabase();

        ayatQuranDao = AyatQuranDaoFactory.createAyatDao(db);
        indexDao = IndexDaoFactory.createIndexDao(db);

        this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        Map<Integer, FoundDocument> matchedDocs = new HashMap<>();
        double threshold = 0.9;

        do {
            matchedDocs = SearchUtil.search(
                    constraint.toString(),
                    false,
                    true,
                    true,
                    threshold,
                    indexDao);
            threshold -= 0.1;
        } while ((matchedDocs.size() < 1) && (threshold >= 0.7));


        List<FoundDocument> matchedDocsValue = new ArrayList<>();
        List<AyatQuran> ayatQurans = new ArrayList<>();

        if (matchedDocs.size() > 0){
            matchedDocsValue = new ArrayList<>(matchedDocs.values());
            Collections.sort(matchedDocsValue, new Comparator<FoundDocument>() {
                @Override
                public int compare(FoundDocument o1, FoundDocument o2) {
                    if (o1.getScore() == o2.getScore()){
                        return o1.getAyatQuranId() - o2.getAyatQuranId();
                    }

                    return o1.getScore() < o2.getScore() ? 1 : -1;
                }
            });

            ayatQurans = displayMatchedAyats(matchedDocsValue);
        }

        final FilterResults results = new FilterResults();
        results.values = ayatQurans;
        results.count = ayatQurans.size();

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        if (((List)results.values).size() > 0) {
            adapter.clear();
            final List<AyatQuran> supportedTypes = new ArrayList<>((List<AyatQuran>)results.values);
            adapter.addAll((supportedTypes));
            adapter.notifyDataSetChanged();
        }
    }

    private List<AyatQuran> displayMatchedAyats(final List<FoundDocument> foundDocuments){

        final List<Integer> ids = new ArrayList();
        for (FoundDocument document : foundDocuments){
            ids.add(document.getAyatQuranId());
        }
        return ayatQuranDao.getAyatQurans(ids);
    }
}
