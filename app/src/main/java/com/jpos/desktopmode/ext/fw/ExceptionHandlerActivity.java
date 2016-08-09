package com.jpos.desktopmode.ext.fw;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

/**
 * Project: FloatingWindows.
 * Created by jonos on 8/8/2016.
 */

public class ExceptionHandlerActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_acra);

        Button btn = (Button) findViewById(R.id.restartApp);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final Class<? extends Activity> restartActivityClass =
                CustomActivityOnCrash.getRestartActivityClassFromIntent(getIntent());
        final CustomActivityOnCrash.EventListener eventListener =
                CustomActivityOnCrash.getEventListenerFromIntent(getIntent());

        Intent intent = new Intent(
                ExceptionHandlerActivity.this,
                restartActivityClass
        );
        CustomActivityOnCrash.restartApplicationWithIntent(
                ExceptionHandlerActivity.this,
                intent,
                eventListener
        );
    }

    @Override
    public boolean onLongClick(View v) {
        AlertDialog dialog = new AlertDialog.Builder(ExceptionHandlerActivity.this)
                .setTitle("Error details")
                .setMessage(
                        CustomActivityOnCrash.getAllErrorDetailsFromIntent(
                                ExceptionHandlerActivity.this,
                                getIntent()
                        )
                ).setPositiveButton("Close", null)
                .setNeutralButton(
                        "Copy",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                copyErrorToClipboard();
                                Toast.makeText(
                                        ExceptionHandlerActivity.this,
                                        "Copied to clipboard",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }
                ).show();

        try {
            TextView textView = (TextView) dialog.findViewById(android.R.id.message);
            if (textView != null) {
                textView.setTextIsSelectable(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    private void copyErrorToClipboard() {
        String errorInformation =
                CustomActivityOnCrash.getAllErrorDetailsFromIntent(
                        ExceptionHandlerActivity.this,
                        getIntent()
                );

        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Error information", errorInformation);
        clipboard.setPrimaryClip(clip);
    }
}
