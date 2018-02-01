/*
 * Copyright (C) 2018 Citrus-CAF Project
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

package com.citrus.settings.fragments;

import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.UserHandle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;

import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.settings.Utils;

public class NotificationSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {
    
    private ListPreference mNoisyNotification;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.notifications_settings);
        
        PreferenceScreen prefSet = getPreferenceScreen();
        ContentResolver resolver = getActivity().getContentResolver();
    
        mNoisyNotification = (ListPreference) findPreference("notification_sound_vib_screen_on");
        mNoisyNotification.setOnPreferenceChangeListener(this);
        int mode = Settings.System.getIntForUser(resolver,
                Settings.System.NOTIFICATION_SOUND_VIB_SCREEN_ON,
                1, UserHandle.USER_CURRENT);
        mNoisyNotification.setValue(String.valueOf(mode));
        mNoisyNotification.setSummary(mNoisyNotification.getEntry());


        boolean mChargingLedsEnabled = (getResources().getBoolean(
                        com.android.internal.R.bool.config_intrusiveBatteryLed));
 
        boolean mNotificationLedsEnabled = (getResources().getBoolean(
                        com.android.internal.R.bool.config_intrusiveNotificationLed));
 
        PreferenceCategory mLedsCategory = (PreferenceCategory) findPreference("custom_leds");
        Preference mChargingLeds = (Preference) findPreference("charging_light");
        Preference mNotificationLeds = (Preference) findPreference("notification_light");
 
        if (mChargingLeds != null && mNotificationLeds != null) {
            if (!mChargingLedsEnabled) {
                mLedsCategory.removePreference(mChargingLeds);
            } else if (!mNotificationLedsEnabled) {
                mLedsCategory.removePreference(mNotificationLeds);
            } else if (!mChargingLedsEnabled && !mNotificationLedsEnabled) {
                prefSet.removePreference(mLedsCategory);
            }
        }
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
        if (preference.equals(mNoisyNotification)) {
            int mode = Integer.parseInt(((String) objValue).toString());
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.NOTIFICATION_SOUND_VIB_SCREEN_ON, mode, UserHandle.USER_CURRENT);
            int index = mNoisyNotification.findIndexOfValue((String) objValue);
            mNoisyNotification.setSummary(
                    mNoisyNotification.getEntries()[index]);
            return true;
        }
        return false;
    }
}
