package com.jpos.desktopmode.ext.fw.prefs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jpos.desktopmode.ext.fw.Common;
import com.jpos.desktopmode.ext.fw.R;
import com.jpos.desktopmode.ext.fw.Util;

@SuppressWarnings({"WeakerAccess"})
@SuppressLint("WorldReadableFiles")
public class TitleBarSettingsActivity extends Activity implements OnSharedPreferenceChangeListener {


    SharedPreferences mPref;
    Resources mResource;
    int mIconType;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mResource = getResources();
        setContentView(R.layout.dialog_titlebar_icon_theme_chooser);
        init();
        ListView lv = (ListView) findViewById(R.id.titlebar_themes);
        final ThemeItemAdapter adapter = new ThemeItemAdapter(getContext());

        adapter.add(new ThemeItem(mResource,
                                  R.string.tbic_theme_none_t,
                                  R.string.tbic_theme_none_s, Common.TITLEBAR_ICON_NONE));
        adapter.add(new ThemeItem(mResource,
                                  R.string.tbic_theme_original_t,
                                  R.string.tbic_theme_original_s, Common.TITLEBAR_ICON_ORIGINAL));
        adapter.add(new ThemeItem(mResource,
                                  R.string.tbic_theme_win_t,
                                  R.string.tbic_theme_win_s, Common.TITLEBAR_ICON_WIN));
        adapter.add(new ThemeItem(mResource,
                                  R.string.tbic_theme_clearer_t,
                                  R.string.tbic_theme_clearer_s, Common.TITLEBAR_ICON_ANDROIDY));
        adapter.add(new ThemeItem(mResource,
                                  R.string.tbic_theme_ssnjr_t,
                                  R.string.tbic_theme_ssnjr_s, Common.TITLEBAR_ICON_NOUGATY));
        adapter.mSelectedId = mIconType;

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @SuppressLint({"LogConditional", "CommitPrefEdits"})
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                    adapter.mSelectedId = pos;
                    adapter.notifyDataSetChanged();
                    Log.d("Xposed", "Titlebar selected " + pos);
                    mPref.edit().putInt(Common.KEY_WINDOW_TITLEBAR_ICON_TYPE, pos).commit();
                }
            });
    }

    private Context getContext() {
        return this;
    }

    // Foreground Titlebar Views
    TextView tbAppTitle;
    ImageButton tbCloseButton;
    ImageButton tbMaxButton;
    ImageButton tbMinButton;
    ImageButton tbMoreButton;
    View tbDivider;
    // Foreground Actionbar Views
    ImageView abAppIcon;
    TextView abAppTitle;
    ImageView abOverflowButton;
    // Background
    View abBackground;
    View tbBackground;

    private void init() {
        //noinspection deprecation
        mPref = getSharedPreferences(Common.PREFERENCE_MAIN_FILE, MODE_WORLD_READABLE);

        tbAppTitle = (TextView) findViewById(R.id.movable_titlebar_appname);
        tbCloseButton = (ImageButton) findViewById(R.id.movable_titlebar_close);
        tbMaxButton = (ImageButton) findViewById(R.id.movable_titlebar_max);
        tbMinButton = (ImageButton) findViewById(R.id.movable_titlebar_min);
        tbMoreButton = (ImageButton) findViewById(R.id.movable_titlebar_more);
        tbDivider = findViewById(R.id.movable_titlebar_line);

        abAppIcon = (ImageView) findViewById(android.R.id.button1);
        abAppTitle = (TextView) findViewById(android.R.id.candidatesArea);
        abOverflowButton = (ImageView) findViewById(android.R.id.button2);

        abBackground = findViewById(android.R.id.background);
        tbBackground = findViewById(R.id.movable_titlebar);

        updatePref();
    }

    private void updatePref() {
        //noinspection UnnecessaryLocalVariable
        int tbHeight = Util.realDp(mPref.getInt(Common.KEY_WINDOW_TITLEBAR_SIZE,
                                                Common.DEFAULT_WINDOW_TITLEBAR_SIZE), getContext());
        ((LinearLayout.LayoutParams) tbBackground.getLayoutParams()).height = tbHeight;

        int tbDividerHei = Util.realDp(mPref.getInt(Common.KEY_WINDOW_TITLEBAR_SEPARATOR_SIZE,
                                                    Common.DEFAULT_WINDOW_TITLEBAR_SEPARATOR_SIZE), getContext());
        if (mPref.getBoolean(Common.KEY_WINDOW_TITLEBAR_SEPARATOR_ENABLED,
                             Common.DEFAULT_WINDOW_TITLEBAR_SEPARATOR_ENABLED)) {
            ((RelativeLayout.LayoutParams) tbDivider.getLayoutParams()).height = tbDividerHei;
            int tbDividerCol = Color.parseColor("#" + mPref.getString(Common.KEY_WINDOW_TITLEBAR_SEPARATOR_COLOR,
                                                                      Common.DEFAULT_WINDOW_TITLEBAR_SEPARATOR_COLOR));
            tbDivider.setBackgroundColor(tbDividerCol);
        } else {
            ((RelativeLayout.LayoutParams) tbDivider.getLayoutParams()).height = 0;
        }

        mIconType = mPref.getInt(Common.KEY_WINDOW_TITLEBAR_ICON_TYPE,
                                 Common.DEFAULT_WINDOW_TITLEBAR_ICONS_TYPE);

        switch (mIconType) {
            case Common.TITLEBAR_ICON_NONE:
                ((LinearLayout.LayoutParams) tbBackground.getLayoutParams()).height = 0;
                break;
            case Common.TITLEBAR_ICON_ORIGINAL:
                tbCloseButton.setImageDrawable(mResource.getDrawable(R.drawable.movable_title_close_original));
                tbMaxButton.setImageDrawable(mResource.getDrawable(R.drawable.movable_title_max_original));
                tbMinButton.setImageDrawable(mResource.getDrawable(R.drawable.movable_title_min_original));
                tbMoreButton.setImageDrawable(mResource.getDrawable(R.drawable.movable_title_more_original));
                break;
            case Common.TITLEBAR_ICON_WIN:
                tbCloseButton.setImageDrawable(mResource.getDrawable(R.drawable.movable_title_close_win));
                tbMaxButton.setImageDrawable(mResource.getDrawable(R.drawable.movable_title_max_win));
                tbMinButton.setImageDrawable(mResource.getDrawable(R.drawable.movable_title_min_win));
                tbMoreButton.setImageDrawable(mResource.getDrawable(R.drawable.movable_title_more_win));
                break;
            case Common.TITLEBAR_ICON_ANDROIDY:
                tbCloseButton.setImageDrawable(mResource.getDrawable(R.drawable.movable_title_close_material));
                tbMaxButton.setImageDrawable(mResource.getDrawable(R.drawable.movable_title_max_material));
                tbMinButton.setImageDrawable(mResource.getDrawable(R.drawable.movable_title_min_material));
                tbMoreButton.setImageDrawable(mResource.getDrawable(R.drawable.movable_title_more_material));
                break;
            case Common.TITLEBAR_ICON_NOUGATY:
                tbCloseButton.setImageDrawable(mResource.getDrawable(R.drawable.movable_title_close_nougat));
                tbMaxButton.setImageDrawable(mResource.getDrawable(R.drawable.movable_title_max_nougat));
                tbMinButton.setImageDrawable(mResource.getDrawable(R.drawable.movable_title_min_nougat));
                tbMoreButton.setImageDrawable(mResource.getDrawable(R.drawable.movable_title_more_nougat_alt));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPref.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPref.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updatePref();
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    class ThemeItem {
        public final String title;
        public final String msg;
        public final int id;
        public ThemeItem(Resources r, int _title, int _msg, int _id) {
            title = r.getString(_title);
            msg = r.getString(_msg);
            id = _id;
        }
    }

    @SuppressWarnings({"WeakerAccess", "InnerClassMayBeStatic"})
    class ThemeItemAdapter extends ArrayAdapter<ThemeItem> {
        class ItemView extends LinearLayout {
            public TextView title;
            public TextView msg;
            public ItemView(Context context, LayoutInflater inflator) {
                super(context);
                inflator.inflate(R.layout.view_app_list, this);
                title = (TextView) findViewById(android.R.id.title);
                msg = (TextView) findViewById(android.R.id.message);
                findViewById(android.R.id.icon).setVisibility(View.GONE);

                int padding = Util.dp(8, context);
                setPadding(padding, padding, padding, padding);
            }
        }

        LayoutInflater mInflator;
        int mSelectedId;

        public ThemeItemAdapter(Context context) {
            super(context, 0);
            mInflator = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        }

        public View getView(int position, View v, ViewGroup parent) {
            if (v == null) {
                v = new ItemView(getContext(), mInflator);
            }
            ItemView convertView = (ItemView) v;

            final ThemeItem item = getItem(position);
            if (item != null) {
                final String title = item.title;
                final String msg = item.msg;
                final boolean isSelected = (item.id == mSelectedId);
                //noinspection deprecation
                convertView.title.setText(title);
                convertView.msg.setText(msg);

                if (isSelected) {
                    convertView.setBackgroundColor(
                            ContextCompat.getColor(
                                    TitleBarSettingsActivity.this,
                                    R.color.listViewSelectedLight
                            )
                    );
                } else {
                    convertView.setBackgroundColor(
                            ContextCompat.getColor(
                                    TitleBarSettingsActivity.this,
                                    android.R.color.background_light
                            )
                    );
                }
            }
            //noinspection ReturnOfInnerClass
            return convertView;
        }
    }
}
