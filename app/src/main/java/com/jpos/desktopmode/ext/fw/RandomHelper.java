package com.jpos.desktopmode.ext.fw;

import java.util.Random;

/**
 * Project: FloatingWindows.
 * Created by jonos on 8/5/2016.
 */

@SuppressWarnings("WeakerAccess")
public class RandomHelper {
    private static Random randomInstance;

    @SuppressWarnings("WeakerAccess")
    public static Random getRandomInstance() {
        if (randomInstance == null) {
            randomInstance = new Random();
        }
        return randomInstance;
    }

    @SuppressWarnings("WeakerAccess")
    public static int getRandomInt(int min, int max) {
        Random random = getRandomInstance();
        return (random.nextInt((max - min) + 1) + min);
    }
}
