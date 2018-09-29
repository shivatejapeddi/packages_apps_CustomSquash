package com.citrus.settings;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.provider.Settings;

import android.text.TextUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.settings.dashboard.SummaryLoader;
import com.android.settings.search.actionbar.SearchMenuController;
import com.android.settings.R;
import com.android.settings.core.InstrumentedFragment;

import com.citrus.settings.tabs.Buttons;
import com.citrus.settings.tabs.StatusBar;
import com.citrus.settings.tabs.Ui;
import com.citrus.settings.tabs.Misc;
import com.citrus.settings.tabs.LockScreen;

import com.citrus.settings.PagerSlidingTabStrip;

import com.android.internal.logging.nano.MetricsProto.MetricsEvent;

import java.util.Random;

public class CustomSquash extends InstrumentedFragment {

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
        getActivity().getActionBar().setTitle(R.string.custom_squash_title);
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
/*
    private static class SummaryProvider implements SummaryLoader.SummaryProvider {

        private final Context mContext;
        private final SummaryLoader mSummaryLoader;

        public SummaryProvider(Context context, SummaryLoader summaryLoader) {
            mContext = context;
            mSummaryLoader = summaryLoader;
        }

        @Override
        public void setListening(boolean listening) {
            String mCustomSummary = Settings.System.getString(
                    mContext.getContentResolver(), Settings.System.CUSTOM_SETTINGS_SUMMARY);
            boolean mRandSum = Settings.System.getInt(
                    mContext.getContentResolver(), Settings.System.CUSTOM_SETTINGS_RANDOM_SUMMARY, 0) == 1;
            final String[] summariesArray = mContext.getResources().getStringArray(R.array.custom_summaries);
            String chosenSum = randomSummary(summariesArray);

            if (listening) {
                if (TextUtils.isEmpty(mCustomSummary) && !mRandSum) {
                    mSummaryLoader.setSummary(this, mContext.getString(R.string.custom_title_summary));
                } else if (!TextUtils.isEmpty(mCustomSummary) && !mRandSum) { //Random is off, Use User's input
                    mSummaryLoader.setSummary(this, mCustomSummary);
                } else if (TextUtils.isEmpty(mCustomSummary) && mRandSum) { //Random is on, User Input is blank
                    mSummaryLoader.setSummary(this, chosenSum);
                } else if (!TextUtils.isEmpty(mCustomSummary) && mRandSum) { //Random is on, but User has input
                    mSummaryLoader.setSummary(this, chosenSum); //Override Text from user input
                }
            }
       }

        public static String randomSummary(String[] array) {
            int rand = new Random().nextInt(array.length);
            return array[rand];
        }
   }

    public static final SummaryLoader.SummaryProviderFactory SUMMARY_PROVIDER_FACTORY
            = new SummaryLoader.SummaryProviderFactory() {
        @Override
        public SummaryLoader.SummaryProvider createSummaryProvider(Activity activity,
                                                                   SummaryLoader summaryLoader) {
            return new SummaryProvider(activity, summaryLoader);
        }
    };
*/
    @Override
    public int getMetricsCategory() {
        return MetricsEvent.CUSTOM_SQUASH;
    }
}
