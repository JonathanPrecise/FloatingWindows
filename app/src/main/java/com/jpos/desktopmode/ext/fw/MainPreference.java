package com.jpos.desktopmode.ext.fw;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.jpos.desktopmode.ext.fw.prefs.BehaviorFragment;
import com.jpos.desktopmode.ext.fw.prefs.MainFragment;
import com.jpos.desktopmode.ext.fw.prefs.MovingFragment;
import com.jpos.desktopmode.ext.fw.prefs.OverlayFragment;

/**
 * This is an activity which shows each Settings fragment in the ViewPager.
 *
 * It extends {@link AppCompatActivity} for a Material Design implementation
 *     backwards-compatible with Android 4.4 Kitkat
 */
public class MainPreference extends AppCompatActivity {

    /**
     * Create the activity, set the ViewPager Adapter, setup the TabLayout with the ViewPager,
     *     and set the Toolbar as the SupportActionBar.
     * @param savedInstanceState The view's saved instance.
     */
    @SuppressLint("WorldReadableFiles")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Call parent activity
        setContentView(R.layout.activity_viewpager); // Set layout

        if (!DesktopModeHelper.isDesktopModeInstalled(this)) {
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("PocketPC is not installed")
                    .setMessage("This extension depends on PocketPC to function correctly. " +
                            "Please install PocketPC, and try again.")
                    .setPositiveButton(
                            android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MainPreference.this.finish();
                                }
                    })
                    .create()
                    .show();
            return;
        }

        /**
         * Make an adapter for ViewPager
         */
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getFragmentManager()) {

            /**
             * Get the fragment for each viewpager page.
             * @param position The page number.
             * @return the corresponding fragment.
             */
            @Override
            public Fragment getItem(int position) {
                /**
                 * Check which (valid) position is needed.
                 */
                switch (position) {
                    case 0:
                        return MainFragment.getInstance();
                    case 1:
                        return MovingFragment.getInstance();
                    case 2:
                        return BehaviorFragment.getInstance();
                    case 3:
                        return OverlayFragment.getInstance();

                    /* DEPRECATED
                    case 2:
                        return TestingActivity.getInstance();
                    */
                }

                /**
                 * Log an exception in logcat if not any of the above values.
                 */
                new Fragment.InstantiationException(
                        "Error making fragment for position " + position,
                        new IllegalArgumentException(
                                "Position " + position + " is invalid. Must be an integer from " +
                                        "0 to 3."
                        )
                ).printStackTrace();

                /**
                 * But in any case, don't crash. Just return a new (empty) fragment instance.
                 */
                return new Fragment();
            }

            /**
             * Get the page title
             * @param pos The page number
             * @return the page title
             */
            @Override
            public String getPageTitle(int pos) {
                /**
                 * Check what the page number is, and return the corresponding values
                 */
                switch (pos) {
                    case 0:
                        return getResources().getString(R.string.pref_main_top_title);
                    case 1:
                        return getResources().getString(R.string.pref_movable_top_title);
                    case 2:
                        return getResources().getString(R.string.pref_behavior_title);
                    case 3:
                        return getResources().getString(R.string.pref_overlay_title);

                    /* DEPRECATED
                    case 2:
                        return getResources().getString(R.string.pref_testing_top_title);
                    */
                }

                /**
                 * Log an exception in logcat if not any of the above values.
                 */
                new IllegalArgumentException(
                        "Position " + pos + " is invalid. Must be an integer from " +
                                "0 to 3."
                ).printStackTrace();

                /**
                 * But in any case, don't crash. Just return "ERROR" as the title.
                 */
                return "ERROR";
            }

            /**
             * Get how many pages the ViewPager will have.
             *
             * Since 0.1-alpha0, it uses advanced brain recognition technology to predict
             *     how many tabs the user wants
             *
             * @return the number of pages.
             */
            @Override
            public int getCount() {
                /**
                 * Now uses advanced brain wave sensing to get the requested count.
                 */
                BrainRecognition brainRecognition;
                brainRecognition = BrainRecognition.createInstance(
                        MainPreference.this,
                        R.string.br_api_key
                );
                brainRecognition.validateApiKey();
                try {
                    return brainRecognition.predictInt();
                } catch (ApiNotVerifiedException e) {
                    Log.e(Common.LOG_TAG, "getCount: Error using brainRecognition, returning 4", e);
                    return 4;
                }
            }
        };

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout pts = (TabLayout) findViewById(R.id.pager_title_strip);
        pts.setupWithViewPager(viewPager);

        /**
         * Test if the Xposed Module is active.
         */
        testSettings(true);


        //noinspection deprecation
        SharedPreferences mPrefs = getSharedPreferences(
                Common.PREFERENCE_MAIN_FILE,
                PreferenceActivity.MODE_WORLD_READABLE
        );

        if (mPrefs.getBoolean(Common.KEY_USER_FIRSTRUN, true)) {
            Intent starter = new Intent(this, FWIntroActivity.class);
            this.startActivityForResult(starter, RESULT_FIRST_USER);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode != RESULT_FIRST_USER) || (resultCode != RESULT_CANCELED)) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_about:
                AboutAppActivity.start(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Test if the Xposed module is active. If true is passed, the module is inactive. If
     *     false, the module is active.
     *
     * NOTE: This should always be called with the value true, as {@link TestingSettingHook}
     *     should be in charge of hooking into this method, and making it return false when
     *     necesary.
     *
     * @param mVis Module state
     */
    private void testSettings(boolean mVis){
        TextView notActiveTitle = (TextView) findViewById(R.id.notActiveTitle);
        notActiveTitle.setText(
                mVis ? getResources().getString(R.string.pref_testing_inactive_title)
                        : getResources().getString(R.string.pref_testing_active_title)
        );
        //notActiveTitle.setVisibility(mVis?View.VISIBLE:View.GONE);
    }
}
