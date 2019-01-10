package org.lafzi.lafzi.helpers.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.lafzi.lafzi.models.QuranText;
import org.lafzi.lafzi.models.builder.QuranTextBuilder;
import org.lafzi.lafzi.utils.GeneralUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class QuranTextDao {

    private final SQLiteDatabase db;

    public QuranTextDao(final SQLiteDatabase db) { this.db = db; }

    private String normalizeInput(String in) {
        String[] splitted = in.split(" ");
        List<String> result = new ArrayList<>();
        for (int i = 0; i < splitted.length; i++) {
            if (splitted[i].length() > 2) {
                String val = "*" + splitted[i];
                if (i != splitted.length - 1) {
                    val = val + " ";
                }
                result.add(val);
            }
        }

        StringBuilder sb = new StringBuilder();
        for (String text : result) {
            sb.append(text);
        }
        return sb.toString();
    }

    public List<QuranText> searchArabic(final String in) {
        String query = GeneralUtil.reverse(normalizeInput(in));
        final String[] args = {query};

        Cursor cursor = db.rawQuery("SELECT\n" +
                "\tdocid,\n" +
                "\toffsets(quran_text) pos,\n" +
                "\ttext_reverse text ,\n" +
                "\tayat_quran.arabic_text_length full_length,\n" +
                "\tayat_quran.short_arabic_text_length short_length,\n" +
                "\tmatchinfo(quran_text, 's') subsequence\n" +
                "from \n" +
                "\tquran_text \n" +
                "JOIN ayat_quran ON ayat_quran._id = quran_text.docid\n" +
                "WHERE text_reverse MATCH ?", args);

        final List<QuranText> results = new ArrayList<>();
        while (cursor.moveToNext()) {
            final QuranText qt = readQuranTextFromCursor(cursor);
            results.add(qt);
        }
        Collections.sort(results, new Comparator<QuranText>() {
            @Override
            public int compare(QuranText t0, QuranText t1) {
                return t0.getScore() - t1.getScore();
            }
        });

        return results;
    }

    private QuranText readQuranTextFromCursor(final Cursor cursor) {
        final QuranTextBuilder builder = QuranTextBuilder.getInstance();

        builder.setDocId(cursor.getInt(
                cursor.getColumnIndexOrThrow(QuranText.DOCID)
        ));

        String text = cursor.getString(
                cursor.getColumnIndexOrThrow(QuranText.TEXT)
        );

        builder.setText(GeneralUtil.reverse(text));

        builder.setPos(cursor.getString(
                cursor.getColumnIndexOrThrow(QuranText.POS)
        ));

        builder.setFullLength(cursor.getInt(
                cursor.getColumnIndexOrThrow(QuranText.FULL_LENGTH)
        ));

        builder.setShortLength(cursor.getInt(
                cursor.getColumnIndexOrThrow(QuranText.SHORT_LENGTH)
        ));

        builder.setSubsequence(cursor.getBlob(
                cursor.getColumnIndexOrThrow(QuranText.SUBSEQUENCE)
        ));

        return builder.build();
    }

}
