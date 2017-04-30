package org.lafzi.android;

import android.app.Application;
import android.content.Context;

/**
 * Created by alfat on 30/04/17.
 */

public class LafziApplication extends Application {

    /*
    Beware of renaming the package,
    This class package and android:name in app manifest need to be matched
     */
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        LafziApplication.context = getApplicationContext();
    }

    /*
    NOTE:
    This is application context, not activity context, so DO NOT cast it into Activity,
    it'll throw an exception!!!

    Can be use for resources stuff though (ex: get string or asset)
     */
    public static Context getLafziContext(){
        return context;
    }
}
