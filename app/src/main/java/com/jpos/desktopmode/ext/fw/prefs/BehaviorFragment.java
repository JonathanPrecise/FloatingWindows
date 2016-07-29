package com.jpos.desktopmode.ext.fw.prefs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jpos.desktopmode.ext.fw.Common;
import com.jpos.desktopmode.ext.fw.R;

public class BehaviorFragment extends PreferenceFragment {

    @SuppressWarnings("WeakerAccess")
    static BehaviorFragment mInstance;
    SharedPreferences mPref;

    public static BehaviorFragment getInstance() {
        if (mInstance == null) {
            mInstance = new BehaviorFragment();
        }
        return mInstance;
    }

    @Override
    @SuppressWarnings({"deprecation", "SpellCheckingInspection", "LongLine"})
    @SuppressLint("WorldReadableFiles")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(Common.PREFERENCE_MAIN_FILE);
        getPreferenceManager().setSharedPreferencesMode(PreferenceActivity.MODE_WORLD_READABLE);
        addPreferencesFromResource(R.xml.pref_behavior);

        findPreference(Common.KEY_KEYBOARD_MODE).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showKeyboardDialog();
                return true;
            }
        });
        mPref = getActivity().getSharedPreferences(Common.PREFERENCE_MAIN_FILE,
                PreferenceActivity.MODE_WORLD_READABLE);

        // FIXME: 7/28/2016 REMOVE COMMENTED CODE
        
        /*
        
        findPreference("window_whitelist").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    //getActivity().startActivity(new Intent(getActivity(), XhitelistActivity.class));
                    return false;
                }
        });
        findPreference("window_blacklist").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    //getActivity().startActivity(new Intent(getActivity(), ClacklistActivity.class));
                    return false;
                }
        });
        findPreference("window_maximized").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    //getActivity().startActivity(new Intent(getActivity(), NaximizedActivity.class));
                    return false;
                }
        });
        
        */
    }

    private void showKeyboardDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final ListView modeList = new ListView(getActivity());

        builder.setView(modeList);
        builder.setTitle(R.string.pref_keyboard_title);

        final AlertDialog dialog = builder.create();
        //noinspection Convert2Diamond
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1);

        adapter.add(getResources().getString(R.string.keyboard_default));
        adapter.add(getResources().getString(R.string.keyboard_pan));
        adapter.add(getResources().getString(R.string.keyboard_scale));

        modeList.setAdapter(adapter);
        modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @SuppressLint("CommitPrefEdits")
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                    String title = ((TextView) view.findViewById(android.R.id.text1))
                        .getText().toString();
                    if (title.equals(getResources().getString(R.string.keyboard_default))) {
                        mPref.edit().putInt(Common.KEY_KEYBOARD_MODE, 1).commit();
                    } else if (title.equals(getResources().getString(R.string.keyboard_pan))) {
                        mPref.edit().putInt(Common.KEY_KEYBOARD_MODE, 2).commit();
                    } else if (title.equals(getResources().getString(R.string.keyboard_scale))) {
                        mPref.edit().putInt(Common.KEY_KEYBOARD_MODE, 3).commit();
                    }
                    Toast.makeText(getActivity(), title, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
        });
        dialog.show();
    }
}
