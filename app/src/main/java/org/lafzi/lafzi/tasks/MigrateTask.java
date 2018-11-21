package org.lafzi.lafzi.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.lafzi.android.R;
import org.lafzi.lafzi.helpers.database.DbHelper;
import org.lafzi.lafzi.helpers.preferences.Preferences;

/**
 * Created by alfat on 22/04/17.
 */

public class MigrateTask extends AsyncTask<Void, Void, Void> {

    private final Activity activity;

    public MigrateTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        ProgressBar pb = (ProgressBar) activity.findViewById(R.id.migration_progress_bar);
        pb.setVisibility(View.VISIBLE);

        TextView tv = (TextView) activity.findViewById(R.id.migration_text);
        tv.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... params) {
        DbHelper.getInstance().getWritableDatabase();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Preferences.getInstance().setDatabaseUpdated(true);
    }
}
