package com.jpos.desktopmode.ext.fw.prefs;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import com.jpos.desktopmode.ext.fw.Common;
import com.jpos.desktopmode.ext.fw.R;

public class MovingFragment extends PreferenceFragment {

    static MovingFragment mInstance;
    SharedPreferences mPref;

    public static MovingFragment getInstance() {
        if (mInstance == null) {
            mInstance = new MovingFragment();
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
        addPreferencesFromResource(R.xml.pref_moving);
//		findPreference(Common.KEY_MOVABLE_WINDOW + "_titlebar_screen").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//				@Override
//				public boolean onPreferenceClick(Preference preference) {
//					getActivity().startActivity(new Intent(getActivity(), TitleBarSettingsActivity.class));
//					return false;
//				}
//			});
        mPref = getActivity().getSharedPreferences(Common.PREFERENCE_MAIN_FILE,
                                                   PreferenceActivity.MODE_WORLD_READABLE);
    }
}
