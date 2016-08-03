package com.jpos.desktopmode.ext.fw;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Call parent activity
        setContentView(R.layout.activity_viewpager); // Set layout

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
        pts.setupWithViewPager(viewPager, true);

        ActionBar bar = getSupportActionBar();

        try {
            //noinspection ConstantConditions
            bar.setElevation(4f);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * Test if the Xposed Module is active.
         */
        testSettings(true);
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
