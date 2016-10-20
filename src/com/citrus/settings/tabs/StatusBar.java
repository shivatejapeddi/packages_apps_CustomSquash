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

import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.UserHandle;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.logging.MetricsProto.MetricsEvent;
import com.android.settings.Utils;

import net.margaritov.preference.colorpicker.ColorPickerPreference;

public class StatusBar extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String KEY_CITRUS_LOGO_COLOR = "status_bar_citrus_logo_color";
    private static final String KEY_CITRUS_LOGO_STYLE = "status_bar_citrus_logo_style";

    private ColorPickerPreference mCitrusLogoColor;
    private ListPreference mCitrusLogoStyle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.statusbar_tab);

        PreferenceScreen prefSet = getPreferenceScreen();
        ContentResolver resolver = getActivity().getContentResolver();

        mCitrusLogoStyle = (ListPreference) findPreference(KEY_CITRUS_LOGO_STYLE);
        int citrusLogoStyle = Settings.System.getIntForUser(resolver,
                Settings.System.STATUS_BAR_CITRUS_LOGO_STYLE, 0,
                UserHandle.USER_CURRENT);
        mCitrusLogoStyle.setValue(String.valueOf(citrusLogoStyle));
        mCitrusLogoStyle.setSummary(mCitrusLogoStyle.getEntry());
        mCitrusLogoStyle.setOnPreferenceChangeListener(this);

        // Citrus-CAF logo color
        mCitrusLogoColor =
            (ColorPickerPreference) prefSet.findPreference(KEY_CITRUS_LOGO_COLOR);
        mCitrusLogoColor.setOnPreferenceChangeListener(this);
        int intColor = Settings.System.getInt(resolver,
                Settings.System.STATUS_BAR_CITRUS_LOGO_COLOR, 0xffffffff);
        String hexColor = String.format("#%08x", (0xffffffff & intColor));
        mCitrusLogoColor.setSummary(hexColor);
        mCitrusLogoColor.setNewPreviewColor(intColor);
    }

    @Override
    protected int getMetricsCategory() {
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
        final String key = preference.getKey();
         if (preference == mCitrusLogoColor) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(objValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(resolver,
                    Settings.System.STATUS_BAR_CITRUS_LOGO_COLOR, intHex);
            return true;
        } else if (preference == mCitrusLogoStyle) {
            int citrusLogoStyle = Integer.valueOf((String) objValue);
            int index = mCitrusLogoStyle.findIndexOfValue((String) objValue);
            Settings.System.putIntForUser(
                    resolver, Settings.System.STATUS_BAR_CITRUS_LOGO_STYLE, citrusLogoStyle,
                    UserHandle.USER_CURRENT);
            mCitrusLogoStyle.setSummary(mCitrusLogoStyle.getEntries()[index]);
            return true;
        }
        return true;
    }
}
