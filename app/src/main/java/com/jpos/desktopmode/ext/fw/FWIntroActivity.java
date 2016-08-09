package com.jpos.desktopmode.ext.fw;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.app.OnNavigationBlockedListener;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;
import com.heinrichreimersoftware.materialintro.slide.Slide;

public class FWIntroActivity extends IntroActivity {

    private boolean reShowDialog;
    private FragmentSlide disclaimerSlide;
    protected int pageNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setFullscreen(true);

        super.onCreate(savedInstanceState);
        boolean overlayPermissions;

        /*setButtonNextVisible(false);
        setButtonBackVisible(false);*/

        reShowDialog = false;

        //noinspection SimplifiableIfStatement
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // Check if we can overlay in API 23+
            overlayPermissions = Settings.canDrawOverlays(this);
        } else { // Automatically granted in API 22 and below
            overlayPermissions = true;
        }

        if (!overlayPermissions) {
            overlayConsent();
        }

        addSlide(new SimpleSlide.Builder()
                .title(R.string.slide1_title)
                .description(R.string.slide1_message)
                .image(R.mipmap.ic_launcher)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.slide2_title)
                .description(R.string.slide2_message)
                .image(R.mipmap.ic_launcher)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .build());

        disclaimerSlide = new FragmentSlide.Builder()
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .fragment(DisclaimerFragment.newInstance())
                .build();
        addSlide(disclaimerSlide);

        addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                pageNum = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        addOnNavigationBlockedListener(new OnNavigationBlockedListener() {
            @Override
            public void onNavigationBlocked(int position, int direction) {
                View contentView = findViewById(android.R.id.content);
                if (contentView != null) {
                    Slide slide = getSlide(position);

                    if (slide == disclaimerSlide) {
                        Snackbar.make(
                                contentView,
                                R.string.label_disclaim,
                                Snackbar.LENGTH_LONG
                        ).show();
                    }
                }
            }
        });
    }

    private void overlayConsent() {
        TextView showText = new TextView(this);
        showText.setText(R.string.overlay_needed_message);
        showText.setTextIsSelectable(true);
        showText.setPadding(
                Util.dp(16, this),
                Util.dp(16, this),
                Util.dp(16, this),
                Util.dp(16, this)
        );

        AlertDialog.Builder builder = new AlertDialog.Builder(FWIntroActivity.this)
                .setTitle(R.string.overlay_needed_title)
                .setView(showText)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        reShowDialog = true;
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                if (reShowDialog) {
                    overlayConsent();
                    reShowDialog = false;
                }
            } else {
                reShowDialog = false;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (!(pageNum == 0)) {
            super.onBackPressed();
            return;
        }
        setResult(RESULT_CANCELED);
        finish();
    }
}
