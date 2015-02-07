package com.vpineda.duinocontrol.app.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.*;
import com.vpineda.duinocontrol.app.R;

/**
 * Created by vpineda1996 on 2015-01-07.
 */
public class ServerFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener{

    ListPreference protocol;
    EditTextPreference host, port, path;


    int serverID = 123456;
    final String SERVER_ID_KEY="serverID";

    String key_protocol, key_hostname, key_port;




    // =====================================
    // Overwritten Methods
    // =====================================


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //addPreferencesFromResource(R.xml.preferences_server_fragment);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if(getArguments() != null){
            serverID = getArguments().getInt("serverID");
        }else {
            serverID = 1;
        }

        // Replace default values with saved ones
        setupFragment(sharedPreferences, serverID);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(key_hostname)){
            Preference hostname = findPreference(key_hostname);
            hostname.setSummary(sharedPreferences.getString(key,"10.0.0.2"));
        }
        if(key.equals(key_port)){
            Preference port = findPreference(key_port);
            port.setSummary(sharedPreferences.getString(key,"80"));
        }

        if(key.equals(key_protocol)){
            Preference protocol = findPreference(key_protocol);
            protocol.setSummary(sharedPreferences.getString(key,"http://"));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Start the listener
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop the listener
        getPreferenceScreen().getSharedPreferences().
                unregisterOnSharedPreferenceChangeListener(this);
    }



    // ======================================
    // HELPER METHODS
    // ======================================


    private void setupFragment(SharedPreferences sharedPreferences, int serverNumber) {

        // CONSTANTS

        key_protocol = "pref_server_protocol_" + String.valueOf(serverNumber);
        key_hostname = "pref_server_hostname_" + String.valueOf(serverNumber);
        key_port = "pref_server_port_" + String.valueOf(serverNumber);

        // Set the available options on listPreference
        // and replace if info already given

        ListPreference listPreference = (ListPreference) findPreference("pref_server_protocol");
        listPreference.setTitle(R.string.protocol);
        listPreference.setKey(key_protocol);
        listPreference.setEntries(R.array.available_protocols);
        //listPreference.setEntryValues(R.array.available_protocols_values);
        if(sharedPreferences.contains(key_protocol)){
            listPreference.setSummary(sharedPreferences.getString(key_protocol,"NULL"));
        }

        // For hostname
        Preference hostname = findPreference("pref_server_hostname");
        hostname.setKey(key_hostname);
        if(sharedPreferences.contains(key_hostname) &&
                !(sharedPreferences.getString(key_hostname,"example.me").equals("example.me"))) {
            hostname.setSummary(sharedPreferences.getString(key_hostname,"example.me"));
        }

        // For port
        Preference port = findPreference("pref_server_port");
        port.setKey(key_port);
        if(sharedPreferences.contains(key_port) &&
                !(sharedPreferences.getString(key_port,"0").equals("0"))){
            port.setSummary(sharedPreferences.getString(key_port,"0"));
        }


    }


}
