package com.jpos.desktopmode.ext.fw;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.jpos.desktopmode.ext.fw.prefs.BehaviorFragment;
import com.jpos.desktopmode.ext.fw.prefs.MainFragment;
import com.jpos.desktopmode.ext.fw.prefs.MovingFragment;
import com.jpos.desktopmode.ext.fw.prefs.OverlayFragment;

public class MainPreference extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_viewpager);

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                case 0:
                    return MainFragment.getInstance();
                case 1:
                    return MovingFragment.getInstance();
                case 2:
                    return BehaviorFragment.getInstance();
                case 3:
                    return OverlayFragment.getInstance();
//				case 2:
//					return TestingActivity.getInstance();
                }
                return new Fragment();
            }

            @Override
            public String getPageTitle(int pos) {
                switch (pos) {
                case 0:
                    return getResources().getString(R.string.pref_main_top_title);
                case 1:
                    return getResources().getString(R.string.pref_movable_top_title);
                case 2:
                    return getResources().getString(R.string.pref_behavior_title);
                case 3:
                    return getResources().getString(R.string.pref_overlay_title);
//				case 2:
//					return getResources().getString(R.string.pref_testing_top_title);
                }
                return "";
            }

            @Override
            public int getCount() {
                return 4;
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

        testSettings(true);

    }

    private void testSettings(boolean mVis){
        TextView notActiveTitle = (TextView) findViewById(R.id.notActiveTitle);
        notActiveTitle.setText(
                mVis ? getResources().getString(R.string.pref_testing_inactive_title)
                        : getResources().getString(R.string.pref_testing_active_title)
        );
        //notActiveTitle.setVisibility(mVis?View.VISIBLE:View.GONE);
    }
}
