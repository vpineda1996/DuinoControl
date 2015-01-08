package com.vpineda.duinocontrol.app.settings;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by vpineda1996 on 2015-01-07.
 */
public class MainPreferences extends Activity {

    // ======================================
    // Variables
    // ======================================



    // =====================================
    // Overwritten Methods
    // =====================================



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int serverNumber = 4;
        openServer(serverNumber);

    }

    private void openServer(int serverNumber) {

        final String SERVER_ID_KEY="serverID";

        ServerFragment serverFragment = new ServerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(SERVER_ID_KEY,serverNumber);

        serverFragment.setArguments(bundle);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, serverFragment)
                .commit();
    }


}
