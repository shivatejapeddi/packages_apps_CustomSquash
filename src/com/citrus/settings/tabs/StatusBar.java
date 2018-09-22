/*
 * Copyright (C) 2016 Cardinal-AOSP Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.citrus.settings.tabs;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.os.UserHandle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.util.Log;
import android.provider.SearchIndexableResource;
import android.provider.Settings;

import com.android.settings.R;

import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;

import com.citrus.settings.preference.CustomSeekBarPreference;
import com.citrus.settings.preference.SystemSettingSwitchPreference;

import java.util.ArrayList;
import java.util.List;

public class StatusBar extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener, Indexable {

    private ListPreference mCustomLogoStyle;
    private ListPreference mCustomLogoPos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.statusbar_tab);

        ContentResolver resolver = getActivity().getContentResolver();
/*
        mCustomLogoStyle = (ListPreference) findPreference("status_bar_custom_logo_style");
        int customLogoStyle = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.STATUS_BAR_CUSTOM_LOGO_STYLE, 0,
                UserHandle.USER_CURRENT);
        mCustomLogoStyle.setValue(String.valueOf(customLogoStyle));
        mCustomLogoStyle.setSummary(mCustomLogoStyle.getEntry());
        mCustomLogoStyle.setOnPreferenceChangeListener(this);

        mCustomLogoPos = (ListPreference) findPreference("status_bar_custom_logo_position");
        int customLogoPos = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.STATUS_BAR_CUSTOM_LOGO_POSITION, 0,
                UserHandle.USER_CURRENT);
        mCustomLogoPos.setValue(String.valueOf(customLogoPos));
        mCustomLogoPos.setSummary(mCustomLogoPos.getEntry());
        mCustomLogoPos.setOnPreferenceChangeListener(this);
*/
    }

    @Override
    public int getMetricsCategory() {
        return MetricsEvent.CUSTOM_SQUASH;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        ContentResolver resolver = getActivity().getContentResolver();
/*        if (preference == mCustomLogoStyle) {
            int val = Integer.valueOf((String) objValue);
            Settings.System.putIntForUser(getContentResolver(),
		            Settings.System.STATUS_BAR_CUSTOM_LOGO_STYLE, val,
                    UserHandle.USER_CURRENT);
            int index = mCustomLogoStyle.findIndexOfValue((String) objValue);
            mCustomLogoStyle.setSummary(mCustomLogoStyle.getEntries()[index]);
            return true;
        }  else if (preference == mCustomLogoPos) {
            int val = Integer.valueOf((String) objValue);
            Settings.System.putIntForUser(getContentResolver(),
		            Settings.System.STATUS_BAR_CUSTOM_LOGO_POSITION, val,
                    UserHandle.USER_CURRENT);
            int index = mCustomLogoPos.findIndexOfValue((String) objValue);
            mCustomLogoPos.setSummary(mCustomLogoPos.getEntries()[index]);
            return true;
        }*/
        return false;
    }

    public static final Indexable.SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                        boolean enabled) {
                    ArrayList<SearchIndexableResource> result =
                            new ArrayList<SearchIndexableResource>();

                    SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.statusbar_tab;
                    result.add(sir);
                    return result;
                }

                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    ArrayList<String> result = new ArrayList<String>();
                    return result;
                }
            };
}
