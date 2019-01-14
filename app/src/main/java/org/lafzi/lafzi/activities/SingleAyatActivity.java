package org.lafzi.lafzi.activities;

/**
 * Created by alfat on 18/11/18.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.lafzi.android.R;
import org.lafzi.lafzi.adapters.AyatPagerAdapter;
import org.lafzi.lafzi.helpers.BitmapHelper;
import org.lafzi.lafzi.models.AyatQuran;
import org.lafzi.lafzi.models.builder.AyatQuranBuilder;

import java.io.File;
import java.io.IOException;

public class SingleAyatActivity extends AppCompatActivity {

    private final String AYAT_ID = "ayat_id";
    private int ayatId;
    private AyatQuran mAyatShare;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_ayat_layout);

        Toolbar mainToolbar = (Toolbar) findViewById(R.id.single_main_toolbar);
        mainToolbar.setNavigationIcon(null);
        setSupportActionBar(mainToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ayatId = getIntent().getIntExtra(AYAT_ID, 0);
        setView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 999:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new Handler().post(new ShareRunnable(mAyatShare));
                }
        }
    }

    private void setView() {
        final ViewPager pager = findViewById(R.id.pager);
        final FloatingActionMenu menu = findViewById(R.id.action_menu);
        final FloatingActionButton shareButton = findViewById(R.id.share_button);
        final FloatingActionButton backButton = findViewById(R.id.back_button);
        final FloatingActionButton nextButton = findViewById(R.id.next_button);

        final FragmentStatePagerAdapter adapter = new AyatPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setCurrentItem(this.ayatId - 1);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                ayatId = position + 1;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        menu.getMenuIconView().setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_more_vert_black_24dp));

        shareButton.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_share_black_24dp));
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] options = getString(R.string.opsi_bagikan).split(";");
                AlertDialog.Builder builder = new AlertDialog.Builder(SingleAyatActivity.this);
                builder.setTitle(R.string.titel_opsi_bagikan);
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        final String arabic = ((TextView) pager.findViewById(R.id.single_ayat_arab)).getText().toString();
                        final String indonesia = ((TextView) pager.findViewById(R.id.single_ayat_indo)).getText().toString();
                        final String suratAyat = ((TextView) pager.findViewById(R.id.single_surat_ayat)).getText().toString();
                        switch (which) {
                            case 0:
                                AyatQuranBuilder aqBuilder = AyatQuranBuilder.getInstance();
                                aqBuilder.setAyatArabic(arabic);
                                aqBuilder.setAyatIndonesian(indonesia);
                                aqBuilder.setSurahName(suratAyat);
                                mAyatShare = aqBuilder.build();
                                Log.i("SHARE", mAyatShare.getAyatArabic());
                                BitmapHelper.requestPermissionWritePublicStorage(SingleAyatActivity.this, new ShareRunnable(mAyatShare));
                                break;
                            case 1:
                                shareIntent.setType("text/plain");
                                shareIntent.putExtra(Intent.EXTRA_TEXT, suratAyat + "\n\n" + arabic + "\n" + indonesia + "\n\n" + getString(R.string.text_footer));
                                startActivity(Intent.createChooser(shareIntent, getString(R.string.bagikan_menggunakan)));
                        }
                    }
                });
                builder.show();
            }
        });

        backButton.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_arrow_back_black_24dp));
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pager.getCurrentItem() > 0) {
                    ayatId--;
                    pager.setCurrentItem(ayatId - 1);
                }
            }
        });

        nextButton.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_arrow_forward_black_24dp));
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pager.getCurrentItem() < pager.getAdapter().getCount() - 1) {
                    ayatId++;
                    pager.setCurrentItem(ayatId - 1);
                }
            }
        });

    }

    private class ShareRunnable implements Runnable {

        private final AyatQuran aq;

        public ShareRunnable(AyatQuran aq) {
            this.aq = aq;
        }

        @Override
        public void run() {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/jpeg");
            File imageFile = null;
            try {
                imageFile = BitmapHelper.saveBitmap(BitmapHelper
                                .loadBitmapFromView(aq, getApplicationContext()), getApplicationContext());
            } catch (IOException e) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getApplicationContext());
                alertBuilder.setTitle(R.string.gagal_membagi);
                alertBuilder.setMessage(R.string.tidak_bisa_muat_gambar);
                alertBuilder.show();
            }

            shareIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", imageFile));
            startActivity(Intent.createChooser(shareIntent, getString(R.string.bagikan_menggunakan)));
        }
    }
}
