package com.jpos.desktopmode.ext.fw;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

@SuppressWarnings("WeakerAccess")
public class TestingSettingHook {

    /**
     * Hook called by Xposed, to notify the app that the module is enabled.
     * @param lpp LoadPackageParam for classLoader and packageName
     */
    public static void handleLoadPackage(LoadPackageParam lpp) {
        //noinspection StatementWithEmptyBody
        if (lpp.packageName.equals(Common.THIS_MOD_PACKAGE_NAME)) { // It's our app. Yay!

            /**
             * Hook into the class
             */
            Class<?> hookClass = XposedHelpers.findClass(
                    "com.jpos.desktopmode.ext.fw.MainPreference",
                    lpp.classLoader
            );
            XposedBridge.hookAllMethods(
                    hookClass,
                    "testSettings",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.args[0] = false; // Set to false, to make the app know that
                                                   // the module is actually enabled.
                        }
                    }
            );
        } else {
            // It's not our app. Boo!
        }
    }

}