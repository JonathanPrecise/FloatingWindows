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

        /* DEPRECATED

        SystemUIHelper.IS_FROM_DESKTOP_MODE = true;

        KeyEvent keyEvent = new KeyEvent(
                SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(),
                KeyEvent.ACTION_DOWN,
                KeyEvent.KEYCODE_BACK,
                0,
                0,
                KeyCharacterMap.VIRTUAL_KEYBOARD,
                0,
                KeyEvent.FLAG_FROM_SYSTEM | KeyEvent.FLAG_VIRTUAL_HARD_KEY,
                InputDevice.SOURCE_KEYBOARD
        );

        XposedBridgeHelper.logd(Common.LOG_TAG, "KeyEvent built");

        InputManager imanager = (InputManager) XposedHelpers
                .callStaticMethod(InputManager.class, "getInstance");

        XposedBridgeHelper.logd(Common.LOG_TAG, "Got instance");

        XposedHelpers.callMethod(
                imanager,
                "injectInputEvent",
                new Class[]{
                        InputEvent.class,
                        int.class
                },
                keyEvent,
                AndroidHooks.IM_INJECT_INPUT_EVENT_MODE_ASYNC);
        */
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
