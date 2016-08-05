package com.jpos.desktopmode.ext.fw.prefs;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import com.jpos.desktopmode.ext.fw.Common;
import com.jpos.desktopmode.ext.fw.R;

public class FloatDotFragment extends PreferenceFragment {

    static FloatDotFragment mInstance;
    SharedPreferences mPref;

    public static FloatDotFragment getInstance() {
        if (mInstance == null) {
            mInstance = new FloatDotFragment();
        }
        return mInstance;
    }
    @Override
    @SuppressWarnings("deprecation")
    @SuppressLint("WorldReadableFiles")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(Common.PREFERENCE_MAIN_FILE);
        getPreferenceManager().setSharedPreferencesMode(PreferenceActivity.MODE_WORLD_READABLE);
        addPreferencesFromResource(R.xml.pref_floatdot);

        mPref = getActivity().getSharedPreferences(Common.PREFERENCE_MAIN_FILE,
                                                   PreferenceActivity.MODE_WORLD_READABLE);
    }
}
