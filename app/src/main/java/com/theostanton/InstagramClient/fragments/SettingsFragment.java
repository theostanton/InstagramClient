package com.theostanton.InstagramClient.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.theostanton.InstagramClient.bitmap.BitmapHandler;
import com.theostanton.InstagramClient.fragments.header.HeaderFragment;
import com.theostanton.InstragramClient.R;

/**
 * Created by theo on 03/01/15.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "SettingsFragment";

    private BitmapHandler bitmapHandler;


    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        bitmapHandler = BitmapHandler.getInstance();


        Intent intent = new Intent(HeaderFragment.TITLE_INTENT);
        intent.putExtra(HeaderFragment.TITLE_EXTRA, "Settings");
        getActivity().sendBroadcast(intent);

        final Preference button = findPreference(getResources().getString(R.string.clear_cache_key));
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference arg0) {
                bitmapHandler.clearAll();
                updateCacheSizeSummary();
                return true;
            }
        });

        updateCacheSizeSummary();
    }

    private void updateCacheSizeSummary() {
        final Preference button = findPreference(getResources().getString(R.string.clear_cache_key));

        new Thread(new Runnable() {
            @Override
            public void run() {
                String diskCacheSize = bitmapHandler.getDiskCacheSize();
                int fileCount = bitmapHandler.getFileCount();
                final StringBuilder sb = new StringBuilder();
                sb.append(diskCacheSize);
                sb.append(" ");
                sb.append(fileCount);
                sb.append(" files");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        button.setSummary(sb.toString());

                    }
                });
            }
        }).start();
    }

    private void changeAccount() {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG, "onSharedPreferenceChanged " + key);
        if (key.equals(getResources().getString(R.string.crop_borders_key))) {
            boolean cropBorders = sharedPreferences.getBoolean(key, true);
            bitmapHandler.setCropBorderless(cropBorders);
        } else if (key.equals(getResources().getString(R.string.account_key))) {
            changeAccount();
        }
    }
}
