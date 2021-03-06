package com.jpos.desktopmode.ext.fw.prefs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.SeekBar;

public class WidgetWindowAlphaPicker extends WidgetFloatPercentage {
	
	public WidgetWindowAlphaPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		super.onProgressChanged(seekBar, progress, fromUser);
		int realValue = progress + Math.round((mMin * 100));
		if (getDialog() != null) {
			WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
			lp.alpha = 0.01f * realValue;
			getDialog().getWindow().setAttributes(lp);
		}
	}
	
}
