package com.vpineda.duinocontrol.app.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.vpineda.duinocontrol.app.R;
import com.vpineda.duinocontrol.app.classes.model.Server;
import com.vpineda.duinocontrol.app.databases.DbContract;
import com.vpineda.duinocontrol.app.databases.DbHelper;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by vpineda1996 on 2015-01-11.
 */
public class EditServerFragment extends DialogFragment {

    private int clickPos;
    public static String CLICK_BUNDLE_POSITION_KEY = "click_pos";
    private boolean updating = false; // tells which method it will use to update
                                      // or create entries
    private EditText name, hostname, port, path;
    Button saveButton;
    private List<String> str;
    private Spinner protocol;
    private Server selectedServer;


    public static EditServerFragment newInstance(@Nullable Integer allServersListClickedPosition){
        EditServerFragment esf = new EditServerFragment();

        // Setup bundle
        if(allServersListClickedPosition != null){
            Bundle b = new Bundle();
            b.putInt(CLICK_BUNDLE_POSITION_KEY,allServersListClickedPosition);
            esf.setArguments(b);
        }
        return esf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get array that contains the available protocols
        str = Arrays.asList(getResources().getStringArray(R.array.available_protocols));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_server_main_old, container, false);
        name = (EditText) view.findViewById(R.id.server_tittle_editText);
        hostname = (EditText) view.findViewById(R.id.server_hostname_editText);
        port = (EditText) view.findViewById(R.id.server_port_editText);
        path = (EditText) view.findViewById(R.id.server_path_editText);
        protocol = (Spinner) view.findViewById(R.id.server_protocol_spinner1);
        saveButton = (Button) view.findViewById(R.id.button);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // get all servers
        DbHelper dbHelper = new DbHelper(getActivity());
        List<Server> listServers= dbHelper.getAllServers();

        //start all the listeners in prefs and add the listener to the button
        SpinnerAdapter spinnerAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                str);
        protocol.setAdapter(spinnerAdapter);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the preference and popBack the stack of windows
                try {
                    savePreferencesToDatabase();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                getFragmentManager().popBackStack();
            }
        });

        // if this fragment was opened for a specific server set the selected
        // options
        if(getArguments()!= null) {
            updating = true;
            clickPos = getArguments().getInt(CLICK_BUNDLE_POSITION_KEY);
            selectedServer = listServers.get(clickPos);
            showSelectedServerOptions(selectedServer);
        }
        // Close the database
        dbHelper.close();

    }

    private void savePreferencesToDatabase() throws URISyntaxException {
        // get all servers
        DbHelper dbHelper = new DbHelper(getActivity());

        // Get all of the names
        String currentName = name.getText().toString();
        String currentHostname = hostname.getText().toString();
        String currentProtocol = (String) protocol.getSelectedItem();
        String currentPort = port.getText().toString();
        String currentPath = path.getText().toString();

        // If you are updating then check every individual reading and then transform
        // it into a URI else transform it into a URI and then create a new server
        // entry
        URI newURI = new URI(currentProtocol,
                null,
                currentHostname,
                Integer.valueOf(currentPort),
                currentPath,
                null, null) ;
        // If the newly created URI is not valid it will throw an exception
        if (updating) {
            dbHelper.updateServer(selectedServer,
                    DbContract.ServerEntry.COLUMN_SERVER_TITLE,
                    currentName);
            dbHelper.updateServer(selectedServer,
                    DbContract.ServerEntry.COLUMN_SERVER_URI,newURI.toASCIIString());
        }else{
            dbHelper.addServer(new Server(currentName,
                    newURI));
        }

        // Close the database
        dbHelper.close();
    }

    private void showSelectedServerOptions(Server selectedServer) {
        name.setText(selectedServer.getName());
        hostname.setText(selectedServer.getUri().getHost());
        protocol.setSelection(returnProtocolIndex(selectedServer.getUri().getScheme()));
        port.setText(String.valueOf(selectedServer.getUri().getPort()));
        path.setText(selectedServer.getUri().getPath());
    }

    private int returnProtocolIndex(String scheme) {
        int i = 0;
        for(String arrayProtocol : str){
            if(arrayProtocol.equals(scheme))
                return i;
            else i++;
        }
        return i;
    }

    public void showAndSaveStackDialog(FragmentManager fragmentManager){
        fragmentManager.beginTransaction()
                .addToBackStack("EditServerFragment")
                .add(this,"editServerFragment")
                .commit();
    }
}