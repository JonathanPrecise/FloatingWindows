package com.jpos.desktopmode.ext.fw;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Project: FloatingWindows.
 * Created by jonos on 8/6/2016.
 */

public class AboutAppActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //////////////////////////////////////////////////////////
        LinearLayout layout = new LinearLayout(this);
        layout.setPadding(
                Util.realDp(16, this),
                Util.realDp(16, this),
                Util.realDp(16, this),
                Util.realDp(16, this)
        );
        setContentView(layout);

        //////////////////////////////////////////////////////////
        TextView textView = new TextView(this);
        textView.setText(
                "Version " + BuildConfig.VERSION_NAME
                + " (Build " + BuildConfig.VERSION_CODE + ")"
        );
        textView.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );

        //////////////////////////////////////////////////////////
        layout.addView(textView);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, AboutAppActivity.class);
        context.startActivity(starter);
    }
}
