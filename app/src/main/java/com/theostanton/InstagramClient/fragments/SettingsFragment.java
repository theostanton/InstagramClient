package com.theostanton.InstagramClient.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.widget.Toast;

import com.theostanton.InstagramClient.bitmap.BitmapHandler;
import com.theostanton.InstagramClient.fragments.header.HeaderFragment;
import com.theostanton.InstagramClient.instagram.Instagram;
import com.theostanton.InstagramClient.instagram.br.com.dina.oauth.instagram.ApplicationData;
import com.theostanton.InstagramClient.instagram.br.com.dina.oauth.instagram.InstagramApp;
import com.theostanton.InstragramClient.R;

/**
 * Created by theo on 03/01/15.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener {

    private static final String TAG = "SettingsFragment";
    // Oauth
    InstagramApp.OAuthAuthenticationListener authListener = new InstagramApp.OAuthAuthenticationListener() {

        @Override
        public void onSuccess() {
            Log.d(TAG, "Connected as " + mApp.getUserName());
            Instagram.getInstance(mApp.getmAccessToken());

            findPreference(getResources().getString(R.string.account_key)).setTitle(mApp.getUserName());
        }

        @Override
        public void onFail(String error) {
            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
        }
    };
    private BitmapHandler bitmapHandler;
    private InstagramApp mApp;

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

        mApp = new InstagramApp(getActivity(), ApplicationData.CLIENT_ID,
                ApplicationData.CLIENT_SECRET, ApplicationData.CALLBACK_URL);
        mApp.setListener(authListener);


        Intent intent = new Intent(HeaderFragment.TITLE_ACTION);
        intent.putExtra(HeaderFragment.TITLE_EXTRA, "Settings");
        getActivity().sendBroadcast(intent);

        findPreference(getResources().getString(R.string.clear_cache_key)).setOnPreferenceClickListener(this);
        findPreference(getResources().getString(R.string.account_key)).setOnPreferenceClickListener(this);


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
        if (mApp.hasAccessToken()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    getActivity());
            builder.setMessage("Disconnect from Instagram?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    mApp.resetAccessToken();
                                    mApp.authorize();
                                    Log.d(TAG, "reset accestoken");
                                }
                            })
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            final AlertDialog alert = builder.create();
            alert.show();
        } else {
            mApp.authorize();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG, "onSharedPreferenceChanged " + key);
        if (key.equals(getResources().getString(R.string.crop_borders_key))) {
            boolean cropBorders = sharedPreferences.getBoolean(key, true);
            bitmapHandler.setCropBorderless(cropBorders);
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        Log.d(TAG, "click " + key);

        if (key.equals(getResources().getString(R.string.clear_cache_key))) {
            bitmapHandler.clearAll();
            updateCacheSizeSummary();
            return true;
        }

        if (key.equals(getResources().getString(R.string.account_key))) {
            changeAccount();
            return true;
        }

        return false;
    }
}
