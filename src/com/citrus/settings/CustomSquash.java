package com.citrus.settings;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.preference.Preference.OnPreferenceChangeListener;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.citrus.settings.tabs.Buttons;
import com.citrus.settings.tabs.StatusBar;
import com.citrus.settings.tabs.Ui;
import com.citrus.settings.tabs.Misc;
import com.citrus.settings.tabs.LockScreen;

import com.citrus.settings.PagerSlidingTabStrip;

import com.android.internal.logging.nano.MetricsProto.MetricsEvent;

public class CustomSquash extends SettingsPreferenceFragment {

    ViewPager mViewPager;
    String titleString[];
    ViewGroup mContainer;
    PagerSlidingTabStrip mTabs;

    static Bundle mSavedState;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContainer = container;
        View view = inflater.inflate(R.layout.custom_squash, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
	      mTabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);

        StatusBarAdapter StatusBarAdapter = new StatusBarAdapter(getFragmentManager());
        mViewPager.setAdapter(StatusBarAdapter);

	mTabs.setViewPager(mViewPager);
        setHasOptionsMenu(true);
        return view;
    }

   @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // After confirming PreferenceScreen is available, we call super.
        super.onActivityCreated(savedInstanceState);

        // Set actionbar elevation 0 to make tab and actionbar look uniform.
        getActivity().getActionBar().setElevation(0);
    }

    @Override
    public void onSaveInstanceState(Bundle saveState) {
        super.onSaveInstanceState(saveState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    class StatusBarAdapter extends FragmentPagerAdapter {
        String titles[] = getTitles();
        private Fragment frags[] = new Fragment[titles.length];

        public StatusBarAdapter(FragmentManager fm) {
            super(fm);
            frags[0] = new Ui();
            frags[1] = new StatusBar();
            frags[2] = new LockScreen();
            frags[3] = new Buttons();
            frags[4] = new Misc();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return frags[position];
        }

        @Override
        public int getCount() {
            return frags.length;
        }
    }

    private String[] getTitles() {
        String titleString[];
        titleString = new String[]{
            getString(R.string.ui_title),
            getString(R.string.statusbar_title),
            getString(R.string.lockscreen_title),
            getString(R.string.buttons_title),
            getString(R.string.misc_title)};
        return titleString;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsEvent.CUSTOM_SQUASH;
    }
}
