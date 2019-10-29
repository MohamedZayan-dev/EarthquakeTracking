package com.example.android.quakereport;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.content.SharedPreferences;

public class SettingsActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);



    }
    public static class EarthquakePreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);
            Preference minMagn=findPreference(getString(R.string.MinimumKey));
            bindPreferenceValueToSummary(minMagn);

            Preference orderBy=findPreference(getString(R.string.ListPreferenceOrderByKey));
            bindPreferenceValueToSummary(orderBy);
        }

        private void bindPreferenceValueToSummary (Preference preference){
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString=preferences.getString(preference.getKey(),"");
            onPreferenceChange(preference,preferenceString);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String value=newValue.toString();
            if(preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int indexOfItemsInListPreference = listPreference.findIndexOfValue(value);
                if (indexOfItemsInListPreference >= 0) {
                    CharSequence[] titles = listPreference.getEntries();
                    preference.setSummary(titles[indexOfItemsInListPreference]);

                }
            }
                else {
                preference.setSummary(value);
            }
            return true;
        }
    }
}
