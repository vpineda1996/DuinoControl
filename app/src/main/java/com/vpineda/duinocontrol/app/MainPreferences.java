package com.vpineda.duinocontrol.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.*;

/**
 * Created by vpineda1996 on 2015-01-07.
 */
public class MainPreferences extends PreferenceActivity
                                implements SharedPreferences.OnSharedPreferenceChangeListener{

    // Variables



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Set the available options on listPreference
        ListPreference listPreference = (ListPreference) findPreference("pref_server_protocol");
        listPreference.setEntries(R.array.available_protocols);
        listPreference.setEntryValues(R.array.available_protocols_values);

        // Set the users values on the settings menu
        showSelectedValesOrDefault(sharedPreferences);
    }

    private void showSelectedValesOrDefault(SharedPreferences sharedPreferences) {

        // All of the vales of the server
        // For protocol
        Preference protocol = findPreference("pref_server_protocol");
        if(sharedPreferences.contains("pref_server_protocol")){
            protocol.setSummary(sharedPreferences.getString("pref_server_protocol","NULL"));
        }

        // For hostname
        Preference hostname = findPreference("pref_server_hostname");
        if(sharedPreferences.contains("pref_server_hostname") &&
                !(sharedPreferences.getString("pref_server_hostname","example.me").equals("example.me"))){
            hostname.setSummary(sharedPreferences.getString("pref_server_hostname","example.me"));
        }

        // For port
        Preference port = (EditTextPreference) findPreference("pref_server_port");
        if(sharedPreferences.contains("pref_server_port") &&
                !(sharedPreferences.getString("pref_server_port","0").equals("0"))){
            port.setSummary(sharedPreferences.getString("pref_server_port","0"));
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals("pref_server_hostname")){
            Preference hostname = findPreference("pref_server_hostname");
            hostname.setSummary(sharedPreferences.getString(key,"10.0.0.2"));
        }
        if(key.equals("pref_server_port")){
            Preference port = findPreference("pref_server_port");
            port.setSummary(sharedPreferences.getString(key,"80"));
        }

        if(key.equals("pref_server_protocol")){
            Preference protocol = findPreference("pref_server_protocol");
            protocol.setSummary(sharedPreferences.getString(key,"http://"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();

        getPreferenceScreen().getSharedPreferences().
                unregisterOnSharedPreferenceChangeListener(this);
    }
}
