package com.jpos.desktopmode.ext.fw;

import android.app.Application;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;

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
        CustomActivityOnCrash.setLaunchErrorActivityWhenInBackground(false);
        CustomActivityOnCrash.install(this);

        /* Initialize Fabric with Crashlytics and Answers */
        final Fabric fabric =
                new Fabric.Builder(this).kits(new Crashlytics(), new Answers()).build();
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

        if (!prefs.getBoolean(Common.KEY_CRASHLYTICS_IS_UID_GEN, false)) {
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
            editor.putLong(Common.KEY_CRASHLYTICS_USERID, randomNumber);
            editor.putBoolean(Common.KEY_CRASHLYTICS_IS_UID_GEN, true);
            editor.apply();
        } else {
            randomNumber = prefs.getLong(Common.KEY_CRASHLYTICS_USERID, 0L);
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
        Crashlytics.setString("OS_ANDROID_INCREMENTAL", Build.VERSION.INCREMENTAL);
        Crashlytics.setString("OS_ANDROID_RELEASE", Build.VERSION.RELEASE);
        Crashlytics.setString("OS_ANDROID_ROM_NAME", Build.DISPLAY); // (Try to) get the ROM, stock
                                                                     // or otherwise

        /* DEVICE INFO */
        Crashlytics.setString("DEVICE_BOOTLOADER_VER", Build.BOOTLOADER);
        Crashlytics.setString("DEVICE_GETDEVICE", Build.DEVICE);
        Crashlytics.setString("DEVICE_MANUFACTURER", Build.MANUFACTURER);
        //noinspection deprecation
        Crashlytics.setString("DEVICE_ABIS_PRIMARY", Build.CPU_ABI);
        //noinspection deprecation
        Crashlytics.setString("DEVICE_ABIS_SECONDARY", Build.CPU_ABI2);

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
            throw new UserIdInvalidException("UserID Must not be 0L, or greater " +
                    "than Long.MAX_VALUE");
        }
    }

    private static class CrashlyticsErrorListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

        }
    }
}

