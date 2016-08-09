package com.jpos.desktopmode.ext.fw;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Project: FloatingWindows.
 * Created by jonos on 8/8/2016.
 */

public class DesktopModeHelper {
    public static boolean isDesktopModeInstalled(Context context) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo("com.jonathan.desktopmode", PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }
}
