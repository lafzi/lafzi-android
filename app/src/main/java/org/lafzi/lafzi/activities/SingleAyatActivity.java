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
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;

import org.lafzi.android.R;
import org.lafzi.lafzi.adapters.AyatPagerAdapter;
import org.lafzi.lafzi.helpers.BitmapHelper;
import org.lafzi.lafzi.helpers.database.AllAyatHandler;
import org.lafzi.lafzi.models.AyatQuran;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SingleAyatActivity extends AppCompatActivity {

    private final String AYAT_ID = "ayat_id";
    private int ayatId;
    private List<AyatQuran> ayatqurans;

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
        ayatqurans  = AllAyatHandler.getInstance().getAllAyats();
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
                    new Handler().post(new ShareRunnable());
                }
        }
    }

    private void setView() {
        final ViewPager pager = (ViewPager) findViewById(R.id.pager);
        final FloatingActionButton shareButton = (FloatingActionButton) findViewById(R.id.share_button);
        final FloatingActionButton backButton = (FloatingActionButton) findViewById(R.id.back_button);
        final FloatingActionButton nextButton = (FloatingActionButton) findViewById(R.id.next_button);

        final FragmentStatePagerAdapter adapter = new AyatPagerAdapter(getSupportFragmentManager(), this.ayatqurans);
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

        shareButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_share_black_24dp));
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
                        switch (which) {
                            case 0:
                                BitmapHelper.requestPermissionWritePublicStorage(SingleAyatActivity.this, new ShareRunnable());
                                break;
                            case 1:
                                shareIntent.setType("text/plain");

                                final String arabic = ((TextView) pager.findViewById(R.id.single_ayat_arab)).getText().toString();
                                final String indonesia = ((TextView) pager.findViewById(R.id.single_ayat_indo)).getText().toString();
                                final String suratAyat = ((TextView) pager.findViewById(R.id.single_surat_ayat)).getText().toString();

                                shareIntent.putExtra(Intent.EXTRA_TEXT, suratAyat + "\n" + arabic + "\n" + indonesia);
                                startActivity(Intent.createChooser(shareIntent, getString(R.string.bagikan_menggunakan)));
                        }
                    }
                });
                builder.show();
            }
        });

        backButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_24dp));
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pager.getCurrentItem() > 0) {
                    ayatId--;
                    pager.setCurrentItem(ayatId - 1);
                }
            }
        });

        nextButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_arrow_forward_black_24dp));
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

        @Override
        public void run() {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/jpeg");
            File imageFile = null;
            try {
                imageFile = BitmapHelper.saveBitmap(BitmapHelper.loadBitmapFromView(ayatqurans.get(ayatId-1), getApplicationContext()), getApplicationContext());
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
