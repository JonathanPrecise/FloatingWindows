package com.jpos.desktopmode.ext.fw;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class FWIntroActivity extends IntroActivity {

    private boolean reShowDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean overlayPermissions;

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
}
