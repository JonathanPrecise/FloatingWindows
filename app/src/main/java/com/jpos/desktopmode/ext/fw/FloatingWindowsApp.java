package com.jpos.desktopmode.ext.fw;

import android.app.Application;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AlertDialog;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import io.fabric.sdk.android.Fabric;

/**
 * Project: FloatingWindows.
 * Created by jonos on 8/6/2016.
 */
public class FloatingWindowsApp extends Application {

    private static final long UID_MAX_VALUE = 99999999999999999L;

    @Override
    public void onCreate() {
        super.onCreate();

        /* Initialize CustomActivityOnCrash */
        CustomActivityOnCrash.install(this);

        /* Initialize Fabric with Crashlytics and Answers */
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics(), new Answers())
                .debuggable(true)
                .build();
        Fabric.with(fabric);
        logUser();
    }

    private void logUser() {
        long randomNumber;
        String userID;
        boolean randomRetrError = false;

        //noinspection deprecation
        SharedPreferences prefs = getSharedPreferences(
                Common.PREFERENCE_CRASHLYTICS,
                MODE_MULTI_PROCESS
        );

        if (!prefs.getBoolean("IS_USERID_GEN", false)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                randomNumber = ThreadLocalRandom.current().nextLong(
                        Integer.MAX_VALUE,
                        (UID_MAX_VALUE + 1)
                );
            } else {
                randomNumber = RandomHelper.getRandomInt(
                        1,
                        (Integer.MAX_VALUE - 1));
            }

            userID = String.valueOf(Build.VERSION.SDK_INT) + String.valueOf(randomNumber);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong("USERID_BASE", randomNumber);
            editor.putBoolean("IS_USERID_GEN", true);
            editor.apply();
        } else {
            randomNumber = prefs.getLong("USERID_BASE", 0L);
            if (randomNumber == 0L) {
                randomRetrError = true;
            }
            userID = String.valueOf(Build.VERSION.SDK_INT) + String.valueOf(randomNumber);
        }
        /* USER ID */
        Crashlytics.setUserIdentifier(userID);

        /* APP INFO */
        Crashlytics.setString("APP_VERSION", BuildConfig.VERSION_NAME);
        Crashlytics.setInt("APP_BUILD", BuildConfig.VERSION_CODE);

        /* OS INFO */
        Crashlytics.setString("OS_NAME", "ANDROID");

        /* ANDROID-SPECIFIC INFO */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Crashlytics.setString("OS_ANDROID_BASE", Build.VERSION.BASE_OS);
        } else {
            Crashlytics.setString("OS_ANDROID_BASE", "N/A");
        }
        Crashlytics.setString("OS_ANDROID_CODENAME", Build.VERSION.CODENAME);
        Crashlytics.setString("OS_ANDROID_INCREMENTAL", Build.VERSION.INCREMENTAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Crashlytics.setInt("OS_ANDROID_PREVIEW_SDK_INT", Build.VERSION.PREVIEW_SDK_INT);
        } else {
            Crashlytics.setInt("OS_ANDROID_PREVIEW_SDK_INT", 0);
        }
        Crashlytics.setString("OS_ANDROID_RELEASE", Build.VERSION.RELEASE);
        Crashlytics.setString("OS_ANDROID_ROM_NAME", Build.DISPLAY); // (Try to) get the ROM, stock
                                                                     // or otherwise
        Crashlytics.setInt("OS_ANDROID_SDK_INT", Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Crashlytics.setString("OS_ANDROID_SECURITY_PATCH", Build.VERSION.BASE_OS);
        } else {
            Crashlytics.setString("OS_ANDROID_SECURITY_PATCH", "N/A");
        }

        Crashlytics.setString("DEVICE_BOOTLOADER_VER", Build.BOOTLOADER);
        Crashlytics.setString("DEVICE_BRAND", Build.BRAND);
        Crashlytics.setString("DEVICE_GETDEVICE", Build.DEVICE);
        Crashlytics.setString("DEVICE_HARDWARE", Build.HARDWARE);
        Crashlytics.setString("DEVICE_MANUFACTURER", Build.MANUFACTURER);
        Crashlytics.setString("DEVICE_MODEL", Build.MODEL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            for (int i = 0; i < Build.SUPPORTED_ABIS.length; ++i) {
                if (i == 0) {
                    Crashlytics.setString("DEVICE_ABIS_PRIMARY", Build.SUPPORTED_ABIS[0]);
                } else {
                    Crashlytics.setString("DEVICE_ABIS_SECONDARY_" + i, Build.SUPPORTED_ABIS[i]);
                }
            }
        } else {
            //noinspection deprecation
            Crashlytics.setString("DEVICE_ABIS_PRIMARY", Build.CPU_ABI);
            //noinspection deprecation
            Crashlytics.setString("DEVICE_ABIS_SECONDARY_1", Build.CPU_ABI2);
        }

        //noinspection deprecation
        Map<String,?> keys = getSharedPreferences(
                Common.PREFERENCE_MAIN_FILE,
                MODE_MULTI_PROCESS
        ).getAll();

        CustomEvent customEvent = new CustomEvent("APP_SETTINGS");

        for (Map.Entry<String,?> entry : keys.entrySet()) {
            try {
                Crashlytics.setString(
                        "APP_SETTING_" + entry.getKey(),
                        Objects.toString(entry.getValue(), "null")
                );

                customEvent.putCustomAttribute(
                        entry.getKey(),
                        Objects.toString(entry.getValue(), "null")
                );
            } catch (Exception e) {
                e.printStackTrace();
                Crashlytics.setString(
                        "APP_SETTING_" + entry.getKey(),
                        "Not Available"
                );
            }
        }

        Answers.getInstance().logCustom(customEvent);

        if (randomRetrError) {
            new AlertDialog.Builder(FloatingWindowsApp.this)
                    .setTitle(R.string.crash_title)
                    .setMessage(R.string.crash_message)
                    .setPositiveButton("Crash", new CrashlyticsErrorListener())
                    .setCancelable(false)
                    .create()
                    .show();
        }
    }

    private static class CrashlyticsErrorListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            throw new UserIdInvalidException("UserID Must not be 0L, or greater " +
                            "than Long.MAX_VALUE");
        }
    }
}

