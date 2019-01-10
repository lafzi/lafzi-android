package org.lafzi.lafzi.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.github.clans.fab.FloatingActionButton;

import org.lafzi.android.R;
import org.lafzi.lafzi.adapters.AyatAdapter;
import org.lafzi.lafzi.listeners.AyatQuranQueryListeners;
import org.lafzi.lafzi.models.AyatQuran;
import org.lafzi.lafzi.utils.GeneralUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final int MY_PERMISSIONS_REQUEST_INTERNET = 100;
    private final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 200;
    private final int MY_SPEECH_REQUEST = 300;

    private boolean hasInternetPermission;
    private boolean hasRecordAudioPermission;
    private final String AYAT_ID = "ayat_id";
    private final String ALL_AYAT = "all_ayat";

    private SearchView mSearchView;

    private boolean preferencesChanged;

    private List<AyatQuran> allAyats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        preferencesChanged = false;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(this);

        setView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.bantuan:
                intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
                return true;
            case R.id.pengaturan:
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                return true;
            case R.id.about:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    private void setView(){
        mSearchView = findViewById(R.id.search);
        mSearchView.setSubmitButtonEnabled(false);

        final AyatAdapter ayatAdapter = new AyatAdapter(this, new LinkedList<AyatQuran>());
        mSearchView.setOnQueryTextListener(new AyatQuranQueryListeners(ayatAdapter, this));

        final ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(ayatAdapter);
        listView.setEmptyView(findViewById(R.id.search_help));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Intent intent = new Intent(getApplicationContext(), SingleAyatActivity.class);
                intent.putExtra(AYAT_ID, view.getId());
                intent.putExtra(ALL_AYAT, (Serializable) allAyats);
                startActivity(intent);
            }
        });

        setSearchExamples();

        final FloatingActionButton fab = findViewById(R.id.action_main);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doPermissionCheck();
                prepareSpeech();
            }
        });
        fab.bringToFront();
    }

    private void prepareSpeech() {
        isOnline();
        if (hasInternetPermission && hasRecordAudioPermission) {
            if (!doGoogleAppCheck()) {
                Toast.makeText(MainActivity.this, R.string.no_google_app, Toast.LENGTH_LONG).show();
                return;
            }
            setupSpeechInput();
        }
    }

    private void setSearchExamples() {

        final SearchView searchView = findViewById(R.id.search);

        String examples[] = getString(R.string.search_examples).split(";");
        Collections.shuffle(Arrays.asList(examples));
        final ViewFlipper viewFlipper = findViewById(R.id.search_examples);

        for (int i = 0; i < examples.length; i++) {
            final String example = examples[i].trim();
            if (!GeneralUtil.isNullOrEmpty(example)) {
                TextView tv = new TextView(this);
                tv.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                tv.setGravity(Gravity.CENTER);
                tv.setTypeface(null, Typeface.BOLD);
                tv.setTextSize(20);
                tv.setText(example + " \u25B6");
                if (example.contains("suara")) {
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setupSpeechInput();
                        }
                    });
                } else {
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            searchView.setQuery(example, true);
                        }
                    });
                }
                viewFlipper.addView(tv);
            }
        }

    }

    private void setupSpeechInput() {
        Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        if (!mSpeechRecognizerIntent.hasExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE)) {
            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        }
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-AE");
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        startActivityForResult(mSpeechRecognizerIntent, MY_SPEECH_REQUEST);
    }

    private boolean doGoogleAppCheck() {
        boolean result = true;
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo("com.google.android.googlequicksearchbox", 0);
            if (!ai.enabled) {
                result = false;
            }
        } catch (PackageManager.NameNotFoundException e) {
            result = false;
        }
        return result;
    }

    private void isOnline() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final boolean[] online = {false};
                Runtime runtime = Runtime.getRuntime();
                try {
                    Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
                    int exitValue = ipProcess.waitFor();
                    online[0] = (exitValue == 0);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }

                if (!online[0]) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void doPermissionCheck() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.INTERNET},
                    MY_PERMISSIONS_REQUEST_INTERNET);
        } else {
            hasInternetPermission = true;
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
        } else {
            hasRecordAudioPermission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_INTERNET:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hasInternetPermission = true;
                    prepareSpeech();
                }
                break;
            case MY_PERMISSIONS_REQUEST_RECORD_AUDIO:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hasRecordAudioPermission = true;
                    prepareSpeech();
                }
                break;
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        preferencesChanged = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (preferencesChanged) {
            // reload search
            final SearchView searchView = findViewById(R.id.search);
            searchView.setQuery(searchView.getQuery(), true);
            preferencesChanged = false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_SPEECH_REQUEST) {
            if (resultCode == RESULT_OK) {
                List<String> results = data.getStringArrayListExtra
                        (RecognizerIntent.EXTRA_RESULTS);
                this.mSearchView.setQuery(results.get(0), true);
            }
        }
    }
}
