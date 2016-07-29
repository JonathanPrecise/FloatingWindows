package com.jpos.desktopmode.ext.fw.prefs;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.SeekBar;

public class WidgetBackgroundDimPicker extends WidgetFloatPercentage {

    public WidgetBackgroundDimPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);
        if (getDialog() != null) {
            WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
            lp.dimAmount = 0.01f * 0f;
            getDialog().getWindow().setAttributes(lp);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        super.onProgressChanged(seekBar, progress, fromUser);
        int realValue = progress + Math.round((mMin * 100));
        if (getDialog() != null) {
            WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
            lp.dimAmount = 0.01f * realValue;
            getDialog().getWindow().setAttributes(lp);
        }
    }

}
