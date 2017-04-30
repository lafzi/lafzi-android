package org.lafzi.android.filters;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.lafzi.android.R;
import org.lafzi.android.adapters.AyatAdapter;
import org.lafzi.android.helpers.database.DbHelper;
import org.lafzi.android.helpers.database.dao.AyatQuranDao;
import org.lafzi.android.helpers.database.dao.AyatQuranDaoFactory;
import org.lafzi.android.helpers.database.dao.IndexDao;
import org.lafzi.android.helpers.database.dao.IndexDaoFactory;
import org.lafzi.android.helpers.preferences.Preferences;
import org.lafzi.android.models.AyatQuran;
import org.lafzi.android.models.FoundDocument;
import org.lafzi.android.utils.HighlightUtil;
import org.lafzi.android.utils.QueryUtil;
import org.lafzi.android.utils.SearchUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by alfat on 21/04/17.
 */

public class AyatAdapterFilter extends Filter {

    private final AyatQuranDao ayatQuranDao;
    private final IndexDao indexDao;

    private final Activity activity;
    private final AyatAdapter adapter;

    private int maxScore;

    public AyatAdapterFilter(final Activity activity, final AyatAdapter adapter){
        final DbHelper dbHelper = DbHelper.getInstance();
        final SQLiteDatabase db = dbHelper.getReadableDatabase();

        ayatQuranDao = AyatQuranDaoFactory.createAyatDao(db);
        indexDao = IndexDaoFactory.createIndexDao(db);

        this.adapter = adapter;
        this.activity = activity;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        Map<Integer, FoundDocument> matchedDocs = null;
        double threshold = 0.9;
        final boolean isVocal = Preferences.getInstance().isVocal();

        final String queryFinal = QueryUtil.normalizeQuery(constraint.toString(), isVocal);
        maxScore = queryFinal.length() - 2;

        do {
            try {
                matchedDocs = SearchUtil.search(
                        queryFinal,
                        isVocal,
                        true,
                        true,
                        threshold,
                        indexDao);
            } catch (JSONException e) {
                Log.e("error", "Index JSON cannot be parsed", e);
            }
            threshold -= 0.1;
        } while ((matchedDocs.size() < 1) && (threshold >= 0.7));


        List<FoundDocument> matchedDocsValue;
        List<AyatQuran> ayatQurans = new ArrayList<>();

        if (matchedDocs.size() > 0){

            HighlightUtil.highlightPositions(matchedDocs, isVocal, ayatQuranDao);
            matchedDocsValue = getMatchedDocsValues(matchedDocs);

            Collections.sort(matchedDocsValue, new Comparator<FoundDocument>() {
                @Override
                public int compare(FoundDocument o1, FoundDocument o2) {
                    if (o1.getScore() == o2.getScore()){
                        return o1.getAyatQuranId() - o2.getAyatQuranId();
                    }

                    return o1.getScore() < o2.getScore() ? 1 : -1;
                }
            });

            ayatQurans = getMatchedAyats(matchedDocsValue);
        }

        final FilterResults results = new FilterResults();
        results.values = ayatQurans;
        results.count = ayatQurans.size();

        return results;
    }

    private List<FoundDocument> getMatchedDocsValues(final Map<Integer, FoundDocument> matchedDocs){
        final List<FoundDocument> values = new LinkedList<>();
        for (Map.Entry<Integer, FoundDocument> entry : matchedDocs.entrySet()){
            values.add(entry.getValue());
        }

        return values;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        ProgressBar pb = (ProgressBar) activity.findViewById(R.id.searching_progress_bar);
        pb.setVisibility(View.GONE);

        SearchView searchView = (SearchView) activity.findViewById(R.id.search);
        searchView.clearFocus();

        final TextView resultCounter = (TextView) activity.findViewById(R.id.result_counter);
        if (results.count > 0) {
            adapter.addAll((List<AyatQuran>)results.values);

            resultCounter.setText(activity.getString(R.string.search_result_count, results.count));
            resultCounter.setVisibility(View.VISIBLE);
        } else {
            resultCounter.setVisibility(View.GONE);
            TextView tvEmptyResult = (TextView) activity.findViewById(R.id.empty_result);
            tvEmptyResult.setVisibility(View.VISIBLE);
        }

        LinearLayout tvSearchHelp = (LinearLayout) activity.findViewById(R.id.search_help);
        tvSearchHelp.setVisibility(View.GONE);
    }

    private List<AyatQuran> getMatchedAyats(final List<FoundDocument> foundDocuments){

        final List<AyatQuran> ayatQurans = new LinkedList<>();
        for (FoundDocument document : foundDocuments){
            final double relevance = Math.min(Math.floor(document.getScore() / maxScore * 100), 100);

            final AyatQuran ayatQuran = document.getAyatQuran();
            ayatQuran.relevance = relevance;
            ayatQuran.highlightPositions = document.getHighlightPosition();

            ayatQurans.add(ayatQuran);
        }

        return ayatQurans;
    }
}
