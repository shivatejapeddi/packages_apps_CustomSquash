/*
 * Copyright (C) 2017 The Dirty Unicorns Project
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

import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.utils.du.ActionConstants;
import com.android.internal.utils.du.DUActionUtils;

import com.citrus.settings.fragments.ActionFragment;
import com.citrus.settings.preference.CustomSeekBarPreference;
import com.citrus.settings.preference.SystemSettingSwitchPreference;

public class HardwareKeys extends ActionFragment implements Preference.OnPreferenceChangeListener {

    // Hardwarekey
    private static final String HWKEY_DISABLE = "hardware_keys_disable";
    
    // ANBI
    private static final String KEY_ANBI = "anbi";

    // Buttons brightness
    private static final String KEY_CATEGORY_BRIGHTNESS = "button_backlight";
    private static final String KEY_BUTTON_BRIGHTNESS = "button_brightness";
    private static final String KEY_BUTTON_MANUAL_BRIGHTNESS_NEW = "button_manual_brightness_new";
    private static final String KEY_BUTTON_TIMEOUT = "button_timeout";
    private static final String KEY_BUTTON_BACKLIGHT_TOUCH = "button_backlight_on_touch_only";

     // category keys
    private static final String CATEGORY_HWKEY = "hw_keys";
    private static final String CATEGORY_BACK = "back_key";
    private static final String CATEGORY_HOME = "home_key";
    private static final String CATEGORY_MENU = "menu_key";
    private static final String CATEGORY_ASSIST = "assist_key";
    private static final String CATEGORY_APPSWITCH = "app_switch_key";
    private static final String CATEGORY_VOLUME = "volume_keys";
    private static final String CATEGORY_POWER = "power_key";

    // Masks for checking presence of hardware keys.
    // Must match values in frameworks/base/core/res/res/values/config.xml
    public static final int KEY_MASK_HOME = 0x01;
    public static final int KEY_MASK_BACK = 0x02;
    public static final int KEY_MASK_MENU = 0x04;
    public static final int KEY_MASK_ASSIST = 0x08;
    public static final int KEY_MASK_APP_SWITCH = 0x10;
    public static final int KEY_MASK_CAMERA = 0x20;
    public static final int KEY_MASK_VOLUME = 0x40;

    private CustomSeekBarPreference mManualButtonBrightness;
    private ListPreference mBacklightTimeout;
    private SwitchPreference mHwKeyDisable;
    private SwitchPreference mAnbiPreference;
    private SwitchPreference mButtonBrightness;
    private SystemSettingSwitchPreference mButtonBacklightTouch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.hardware_keys);

        final Resources res = getActivity().getResources();
        PreferenceScreen prefScreen = getPreferenceScreen();
        ContentResolver resolver = getActivity().getContentResolver();

        final boolean mDeviceHasVariableButtonBrightness = res.getBoolean(
                com.android.internal.R.bool.config_deviceHasVariableButtonBrightness);
        
        final boolean needsNavbar = DUActionUtils.hasNavbarByDefault(getActivity());

        final int mDeviceDefaultButtonBrightness = res.getInteger(
                com.android.internal.R.integer.config_buttonBrightnessSettingDefault);
        
        final PreferenceCategory hwkeyCat = (PreferenceCategory) prefScreen.findPreference(CATEGORY_HWKEY);
        final PreferenceCategory brightnessCategory = (PreferenceCategory) prefScreen.findPreference(KEY_CATEGORY_BRIGHTNESS);

        /* Accidental navigation button interaction */
        mAnbiPreference = (SwitchPreference) findPreference(KEY_ANBI);
        if (mAnbiPreference != null) {
            mAnbiPreference.setChecked((Settings.System.getInt(getContentResolver(),
            Settings.System.ANBI_ENABLED, 0) == 1));
            mAnbiPreference.setOnPreferenceChangeListener(this);
        }

        mButtonBrightness = (SwitchPreference) findPreference(KEY_BUTTON_BRIGHTNESS);
        if (mButtonBrightness != null) {
            mButtonBrightness.setChecked((Settings.System.getInt(resolver,
                    Settings.System.BUTTON_BRIGHTNESS, 1) == 1));
            mButtonBrightness.setOnPreferenceChangeListener(this);
        }

        mManualButtonBrightness = (CustomSeekBarPreference) findPreference(KEY_BUTTON_MANUAL_BRIGHTNESS_NEW);
        if (mManualButtonBrightness != null) {
            int ManualButtonBrightness = Settings.System.getInt(resolver,
                    Settings.System.BUTTON_BRIGHTNESS, mDeviceDefaultButtonBrightness);
            mManualButtonBrightness.setValue(ManualButtonBrightness / 1);
            mManualButtonBrightness.setOnPreferenceChangeListener(this);
        }

        mBacklightTimeout = (ListPreference) findPreference(KEY_BUTTON_TIMEOUT);
        if (mBacklightTimeout != null) {
            int BacklightTimeout = Settings.System.getInt(resolver,
                    Settings.System.BUTTON_BACKLIGHT_TIMEOUT, 5000);
            mBacklightTimeout.setValue(Integer.toString(BacklightTimeout));
            mBacklightTimeout.setSummary(mBacklightTimeout.getEntry());
            mBacklightTimeout.setOnPreferenceChangeListener(this);
        }

        mButtonBacklightTouch = (SystemSettingSwitchPreference) findPreference(KEY_BUTTON_BACKLIGHT_TOUCH);

        mHwKeyDisable = (SwitchPreference) findPreference(HWKEY_DISABLE);
        int keysDisabled = 0;                    
        if (mHwKeyDisable != null) {
            keysDisabled = Settings.Secure.getIntForUser(getContentResolver(),
                    Settings.Secure.HARDWARE_KEYS_DISABLE, 0,
                    UserHandle.USER_CURRENT);
            mHwKeyDisable.setChecked(keysDisabled != 0);
            mHwKeyDisable.setOnPreferenceChangeListener(this);
        }

        if (mDeviceHasVariableButtonBrightness) {
            brightnessCategory.removePreference(mButtonBrightness);
        } else {
            brightnessCategory.removePreference(mManualButtonBrightness);
        }

        if (needsNavbar) {
            hwkeyCat.removePreference(mAnbiPreference);
            hwkeyCat.removePreference(mHwKeyDisable); 
            brightnessCategory.removePreference(mBacklightTimeout);
            brightnessCategory.removePreference(mButtonBacklightTouch);
            prefScreen.removePreference(hwkeyCat);
            prefScreen.removePreference(brightnessCategory);            
        }

        // bits for hardware keys present on device
        final int deviceKeys = getResources().getInteger(
                com.android.internal.R.integer.config_deviceHardwareKeys);

        // read bits for present hardware keys
        final boolean hasHomeKey = (deviceKeys & KEY_MASK_HOME) != 0;
        final boolean hasBackKey = (deviceKeys & KEY_MASK_BACK) != 0;
        final boolean hasMenuKey = (deviceKeys & KEY_MASK_MENU) != 0;
        final boolean hasAssistKey = (deviceKeys & KEY_MASK_ASSIST) != 0;
        final boolean hasAppSwitchKey = (deviceKeys & KEY_MASK_APP_SWITCH) != 0;

        // load categories and init/remove preferences based on device
        // configuration
        final PreferenceCategory backCategory = (PreferenceCategory) prefScreen
                .findPreference(CATEGORY_BACK);
        final PreferenceCategory homeCategory = (PreferenceCategory) prefScreen
                .findPreference(CATEGORY_HOME);
        final PreferenceCategory menuCategory = (PreferenceCategory) prefScreen
                .findPreference(CATEGORY_MENU);
        final PreferenceCategory assistCategory = (PreferenceCategory) prefScreen
                .findPreference(CATEGORY_ASSIST);
        final PreferenceCategory appSwitchCategory = (PreferenceCategory) prefScreen
                .findPreference(CATEGORY_APPSWITCH);

        // back key
        if (!hasBackKey) {
            prefScreen.removePreference(backCategory);
        }

        // home key
        if (!hasHomeKey) {
            prefScreen.removePreference(homeCategory);
        }

        // App switch key (recents)
        if (!hasAppSwitchKey) {
            prefScreen.removePreference(appSwitchCategory);
        }

        // menu key
        if (!hasMenuKey) {
            prefScreen.removePreference(menuCategory);
        }

        // search/assist key
        if (!hasAssistKey) {
            prefScreen.removePreference(assistCategory);
        }

        // load preferences first
        setActionPreferencesEnabled(keysDisabled == 0);

        // let super know we can load ActionPreferences
        onPreferenceScreenLoaded(ActionConstants.getDefaults(ActionConstants.HWKEYS));
    }
 
    @Override
    protected boolean usesExtendedActionsList() {
        return true;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mHwKeyDisable) {
            boolean value = (Boolean) newValue;
            Settings.Secure.putInt(getContentResolver(), Settings.Secure.HARDWARE_KEYS_DISABLE,
                    value ? 1 : 0);
            setActionPreferencesEnabled(!value);
            return true;
        } else if (preference == mAnbiPreference) {
             boolean value = (Boolean) newValue;
             Settings.System.putInt(getActivity().getContentResolver(),
                      Settings.System.ANBI_ENABLED, value ? 1 : 0);
            return true;

        } else if (preference == mBacklightTimeout) {
            String BacklightTimeout = (String) newValue;
            int BacklightTimeoutValue = Integer.parseInt(BacklightTimeout);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.BUTTON_BACKLIGHT_TIMEOUT, BacklightTimeoutValue);
            int BacklightTimeoutIndex = mBacklightTimeout.findIndexOfValue(BacklightTimeout);
            mBacklightTimeout.setSummary(mBacklightTimeout.getEntries()[BacklightTimeoutIndex]);
            return true;
        } else if (preference == mManualButtonBrightness) {
            int value = (Integer) newValue;
            Settings.System.putInt(getContentResolver(),
                    Settings.System.BUTTON_BRIGHTNESS, value * 1);
            return true;
        } else if (preference == mButtonBrightness) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getContentResolver(),
                    Settings.System.BUTTON_BRIGHTNESS, value ? 1 : 0);
        return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.CUSTOM_SQUASH;
    }
}
