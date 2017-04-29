package org.lafzi.android.tasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import org.lafzi.android.activities.MainActivity;
import org.lafzi.android.helpers.database.DbHelper;
import org.lafzi.android.helpers.preferences.Preferences;

/**
 * Created by alfat on 22/04/17.
 */

public class MigrateTask extends AsyncTask<Void, Void, Void> {

    private final Context context;

    public MigrateTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        DbHelper.getInstance(context).getWritableDatabase();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        new Preferences(context).setDatabaseUpdated(true);
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        ((Activity) context).finish();
    }
}
