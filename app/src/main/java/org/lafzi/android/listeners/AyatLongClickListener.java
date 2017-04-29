package org.lafzi.android.listeners;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import org.lafzi.android.R;

/**
 * Created by alfat on 23/04/17.
 */

public class AyatLongClickListener implements AdapterView.OnItemLongClickListener {

    private final Context context;

    public AyatLongClickListener(Context context) {
        this.context = context;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final String arabic = ((TextView) view.findViewById(R.id.ayat_arab)).getText().toString();
        final String indonesia = ((TextView) view.findViewById(R.id.ayat_indo)).getText().toString();

        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", arabic + "\n" + indonesia);
        cm.setPrimaryClip(clipData);

        Toast.makeText(context, R.string.copied, Toast.LENGTH_SHORT).show();
        return true;
    }
}
