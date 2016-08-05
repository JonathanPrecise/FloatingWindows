package com.jpos.desktopmode.ext.fw;

import android.util.Log;

import de.robv.android.xposed.XposedBridge;

/**
 * Project: FloatingWindows.
 * Created by jonos on 8/5/2016.
 */

public class XposedBridgeHelper {

    /**
     * Prevent constructor usage.
     */
    private XposedBridgeHelper() {}

    /**
     * Log to the debug log
     * @param tag        TAG
     * @param message    MESSAGE
     */
    @SuppressWarnings("WeakerAccess")
    public static void logd(String tag, String message) {
        Log.d(tag, message);
        XposedBridge.log(tag + message);
    }

    /**
     * Log to the error log
     * @param tag        TAG
     * @param message    MESSAGE
     * @param t          THROWABLE
     */
    @SuppressWarnings("WeakerAccess")
    public static void loge(String tag, String message, Throwable t) {
        Log.e(tag, message);
        XposedBridge.log(t);
    }
}
