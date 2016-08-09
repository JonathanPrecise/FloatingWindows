package com.jpos.desktopmode.ext.fw;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

@SuppressWarnings("WeakerAccess")
public class SystemUIHelper {

    public static boolean IS_FROM_DESKTOP_MODE = false;

    public static void onBackPressed(Activity mActivity) {
        XposedBridgeHelper.logd(Common.LOG_TAG, "Simulating back press");

        simulateBack(mActivity);
    }

    public static void simulateBack(Activity mActivity) {
        try {
            /* Work-around for bug:
             * When closing a floating window using the titlebar
             * while the keyboard is open, the floating window
             * closes but the keyboard remains open on top of
             * another fullscreen app.
             */
            InputMethodManager imm = (InputMethodManager)
                    mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            //noinspection ConstantConditions
            imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            //ignore
        }
        if (MovableWindow.mWindowHolder != null
                && MovableWindow.mWindowHolder.mActivity != null) {
            MovableWindow.mWindowHolder.mActivity.onBackPressed();
        } else {
            mActivity.onBackPressed();
        }
    }
}
