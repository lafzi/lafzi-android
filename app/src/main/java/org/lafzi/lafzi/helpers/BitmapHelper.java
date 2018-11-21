package org.lafzi.lafzi.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.lafzi.android.R;
import org.lafzi.lafzi.models.AyatQuran;
import org.lafzi.lafzi.utils.GeneralUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.ContentValues.TAG;

public class BitmapHelper {

    private static final int MY_REQUEST_PERMISSION = 999;

    public static void requestPermissionWritePublicStorage(Context context, Runnable runnable) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    (Activity)context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_REQUEST_PERMISSION
            );
        } else new Handler().post(runnable);
    }

    public static Bitmap loadBitmapFromView(AyatQuran ayatQuran, Context context) {
        View v = generateView(ayatQuran, context);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        final int width = dm.widthPixels >= 720 ? dm.widthPixels : 720;

        v.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        v.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        v.layout(0,0,v.getMeasuredWidth(), v.getMeasuredHeight());

        final Bitmap b = Bitmap.createBitmap(
                v.getWidth(),
                v.getHeight(),
                Bitmap.Config.ARGB_8888
        );
        final Canvas c = new Canvas(b);
        v.layout(0, 0, v.getWidth(), v.getHeight());
        v.draw(c);
        return b;
    }

    private static View generateView(AyatQuran ayatQuran, Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout view = new LinearLayout(context);
        inflater.inflate(R.layout.dummy_layout, view, true);

        TextView suratAyat = (TextView) view.findViewById(R.id.dummy_surat_ayat);
        TextView indo = (TextView) view.findViewById(R.id.dummy_ayat_indo);
        TextView arab = (TextView) view.findViewById(R.id.dummy_ayat_arab);
        ImageView iv = (ImageView) view.findViewById(R.id.footer_logo);

        final String surah = context.getString(R.string.surah);
        final String ayah = context.getString(R.string.ayah);
        final String suratAndAyatText = surah + " " + ayatQuran.getSurahName() + " (" + ayatQuran.getSurahNo() + ") " + ayah + " " + ayatQuran.getAyatNo();

        suratAyat.setText(suratAndAyatText);
        Typeface meQuran = Typeface.createFromAsset(context.getAssets(), "fonts/me_quran.ttf");
        arab.setTypeface(meQuran);
        final String ayatArabic = GeneralUtil.isNullOrEmpty(ayatQuran.getAyatMuqathaat()) ?
                ayatQuran.getAyatArabic()  : ayatQuran.getAyatMuqathaat();
        arab.setText(ayatArabic);
        indo.setText(ayatQuran.getAyatIndonesian());

        iv.setImageResource(R.drawable.logo_lafzi_big);

        return view;
    }

    public static File saveBitmap(Bitmap b, Context context) throws IOException {
        final File lafziDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Lafzi");
        if (!lafziDir.exists()) {
            final boolean created = lafziDir.mkdirs();
            if (!created) {
                final String msg = "Can't create directory Lafzi!";
                Log.e(TAG, msg);
                throw new RuntimeException(msg);
            }
        }
        final String filename = lafziDir.getPath() + File.separator + "IMG_" + System.currentTimeMillis() + ".jpg";
        final File imageFile = new File(filename);
        MediaScannerConnection.scanFile(
                context,
                new String[]{imageFile.getAbsolutePath()},
                null,null
                );
        FileOutputStream fos = null;
        try {
            final boolean created = imageFile.createNewFile();
            if (!created) {
                final String msg = "Can't create file";
                Log.e(TAG, msg);
                throw new RuntimeException(msg);
            }

            fos = new FileOutputStream(imageFile);
            b.compress(Bitmap.CompressFormat.JPEG, 70, fos);
            return imageFile;
        } catch (IOException e) {
            Log.e(TAG, "Can't create file", e);
            throw e;
        } finally {
            if (fos != null) {
                fos.flush();
                fos.close();
            }
        }

    }
}
