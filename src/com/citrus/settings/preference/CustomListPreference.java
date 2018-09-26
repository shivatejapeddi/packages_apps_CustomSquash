/*
 * Copyright (C) 2016 The CyanogenMod Project
 * Copyright (C) 2018 The LineageOS Project
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
package com.citrus.settings.preference;

import android.content.Context;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.PreferenceDataStore;
import android.util.AttributeSet;

public abstract class CustomListPreference extends ListPreference {

    public CustomListPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setPreferenceDataStore(new DataStore());
    }

    public CustomListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPreferenceDataStore(new DataStore());
    }

    public CustomListPreference(Context context) {
        super(context);
        setPreferenceDataStore(new DataStore());
    }

    protected abstract boolean isPersisted();
    protected abstract void putString(String key, String value);
    protected abstract String getString(String key, String defaultValue);

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        final String value;
        if (!restorePersistedValue || !isPersisted()) {
            if (defaultValue == null) {
                return;
            }
            value = (String) defaultValue;
            if (shouldPersist()) {
                persistString(value);
            }
        } else {
            // Note: the default is not used because to have got here
            // isPersisted() must be true.
            value = getString(getKey(), null /* not used */);
        }
        setValue(value);
    }

    private class DataStore extends PreferenceDataStore {
        @Override
        public void putString(String key, String value) {
            CustomListPreference.this.putString(key, value);
        }

        @Override
        public String getString(String key, String defaultValue) {
            return CustomListPreference.this.getString(key, defaultValue);
        }
    }
}