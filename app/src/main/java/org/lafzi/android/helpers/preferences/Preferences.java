package org.lafzi.android.helpers.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.lafzi.android.R;

/**
 * Created by alfat on 23/04/17.
 */

public class Preferences {

    private final SharedPreferences sp;
    private final Context context;

    public Preferences(final Context context){
        this.context = context;
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean isDatabaseUpdated(){
        return sp.getBoolean(getString(R.string.database_updated), false);
    }

    public void setDatabaseUpdated(final boolean isUpdated){
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(getString(R.string.database_updated), isUpdated);
        editor.commit();
    }

    public boolean isVocal(){
        return sp.getBoolean(getString(R.string.isvocal), true);
    }

    public void setVocal(final boolean isVocal){
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(getString(R.string.isvocal), isVocal);
        editor.commit();
    }

    public boolean showTranslation() {
        return sp.getBoolean(getString(R.string.showtrans), true);
    }

    private String getString(final int resId){
        return context.getString(resId);
    }

}
