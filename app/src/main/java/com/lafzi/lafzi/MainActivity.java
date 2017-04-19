package com.lafzi.lafzi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lafzi.lafzi.adapters.AyatAdapter;
import com.lafzi.lafzi.model.Ayat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setView();

        final AyatAdapter adapter = new AyatAdapter(this, generateAyat());
        listView.setAdapter(adapter);
    }

    private void setView(){
        searchView = (SearchView) findViewById(R.id.search);
        listView = (ListView) findViewById(R.id.list_view);
    }

    private List<Ayat> generateAyat(){

        final List<Ayat> ayats = new ArrayList<>();

        final Ayat ayat1 = new Ayat(
                "Segala puji bagi Allah",
                "ٱلْحَمْدُ لِلَّهِ رَبِّ ٱلْعَـٰلَمِين"
        );

        final Ayat ayat2 = new Ayat(
                "Maka orang-orang yang zalim itu dimusnahkan sampai ke akar-akarnya. Segala puji bagi Allah, Tuhan semesta alam",
                "فَقُطِعَ دَابِرُ ٱلْقَوْمِ ٱلَّذِينَ ظَلَمُوا۟ ۚ وَٱلْحَمْدُ لِلَّهِ رَبِّ ٱلْعَـٰلَمِينَ"
        );

        ayats.add(ayat1);
        ayats.add(ayat2);

        return ayats;
    }
}
