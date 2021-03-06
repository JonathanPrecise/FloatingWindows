package com.jpos.desktopmode.ext.fw.prefs;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jpos.desktopmode.ext.fw.Common;
import com.jpos.desktopmode.ext.fw.R;

import java.io.DataOutputStream;

/**
 * "General" tab fragment.
 */
public class MainFragment extends PreferenceFragment { /* implements OnPreferenceClickListener */

    @SuppressWarnings("WeakerAccess")
    static MainFragment mInstance;
    SharedPreferences mPref;

    /**
     * Create the fragment
     * @param savedInstanceState savedInstanceState
     */
    @Override
    @SuppressWarnings({"deprecation", "SpellCheckingInspection"})
    @SuppressLint("WorldReadableFiles")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(Common.PREFERENCE_MAIN_FILE);
        getPreferenceManager().setSharedPreferencesMode(PreferenceActivity.MODE_WORLD_READABLE);
        addPreferencesFromResource(R.xml.pref_general);
        mPref = getActivity().getSharedPreferences(Common.PREFERENCE_MAIN_FILE,
                PreferenceActivity.MODE_WORLD_READABLE);

        /* DEPRECATED
        findPreference(Common.KEY_KEYBOARD_MODE).setOnPreferenceClickListener(this);
        findPreference(Common.KEY_RESTART_SYSTEMUI).setOnPreferenceClickListener(this);
        findPreference(
                Common.KEY_STATUSBAR_TASKBAR_RESTART_SYSTEMUI).setOnPreferenceClickListener(this);
        findPreference(Common.KEY_BLACKLIST_APPS).setOnPreferenceClickListener(this);
        findPreference(Common.KEY_WHITELIST_APPS).setOnPreferenceClickListener(this);
        findPreference(Common.KEY_STATUSBAR_TASKBAR_PINNED_APPS).setOnPreferenceClickListener(this);
        if (Build.VERSION.SDK_INT >= 20) { // Lollipop
            Preference p = findPreference("system_notif_top");
            p.setEnabled(false);
            p.setSummary(R.string.pref_systemui_top_summary_not_supported_lollipop);
        }
        */
    }

    /**
     * Seems unused, will remove later
     */
    @SuppressWarnings("unused")
    private void showKeyboardDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final ListView modeList = new ListView(getActivity());

        builder.setView(modeList);
        builder.setTitle(R.string.pref_keyboard_title);

        final AlertDialog dialog = builder.create();
        //noinspection Convert2Diamond
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1);

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

    @SuppressWarnings("unused")
    private void showKillPackageDialog(final String pkgToKill) {
        AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
        build.setMessage(R.string.pref_systemui_restart_title);
        build.setNegativeButton(android.R.string.cancel, null);
        build.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                killPackage(pkgToKill);
            }
        });
        build.show();
    }

    private void killPackage(final String pkgToKill) {
        //noinspection AnonymousInnerClassMayBeStatic
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Process su = Runtime.getRuntime().exec("su");
                    if (su == null) return;
                    DataOutputStream os = new DataOutputStream(su.getOutputStream());
                    os.writeBytes("pkill " + pkgToKill + "\n");
                    os.writeBytes("exit\n");
                    su.waitFor();
                    os.close();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static MainFragment getInstance() {
        if (mInstance == null) {
            mInstance = new MainFragment();
        }
        return mInstance;
    }

    /* DEPRECATED

    @Override
    public boolean onPreferenceClick(Preference p) {
        String k = p.getKey();
        if (k.equals(Common.KEY_KEYBOARD_MODE)) {
            showKeyboardDialog();
            return true;
        } else if (k.equals(Common.KEY_RESTART_SYSTEMUI)
                || k.equals(Common.KEY_STATUSBAR_TASKBAR_RESTART_SYSTEMUI)) {
            showKillPackageDialog("com.android.systemui");
            return true;
        } else if (k.equals(Common.KEY_BLACKLIST_APPS)) {
            showBlacklistActivity();
            return true;
        } else if (k.equals(Common.KEY_WHITELIST_APPS)) {
            showWhitelistActivity();
            return true;
        } else if (k.equals(Common.KEY_STATUSBAR_TASKBAR_PINNED_APPS)) {
            showStatusbarTaskbarPinAppActivity();
            return true;
        }
        return false;
    }

    private void showStatusbarTaskbarPinAppActivity() {
        startActivity(new Intent(getActivity(), StatusbarTaskbarPinAppActivity.class));
    }

    private void showWhitelistActivity() {
        startActivity(new Intent(getActivity(), WhitelistActivity.class));
    }

    private void showBlacklistActivity() {
        startActivity(new Intent(getActivity(), BlacklistActivity.class));
    }

    */
}
