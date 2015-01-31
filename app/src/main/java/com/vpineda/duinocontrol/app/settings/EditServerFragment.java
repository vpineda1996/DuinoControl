package com.vpineda.duinocontrol.app.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.vpineda.duinocontrol.app.R;
import com.vpineda.duinocontrol.app.databases.DbContract;
import com.vpineda.duinocontrol.app.databases.DbHelper;
import com.vpineda.duinocontrol.app.databases.DuServer;

import java.util.Arrays;
import java.util.List;

/**
 * Created by vpineda1996 on 2015-01-11.
 */
public class EditServerFragment extends Fragment {

    private int clickPos;
    private String CLICK_BUNDLE_POSITION_KEY = "click_pos";
    private boolean updating = false; // tells which method it will use to update
                                      // or create entries
    private EditText name, hostname, port, path;
    private List<String> str;
    private Spinner protocol;
    private DuServer selectedServer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        str = Arrays.asList(getResources().getStringArray(R.array.available_protocols));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_server_main, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // get all servers
        final DbHelper dbHelper = new DbHelper(getActivity());
        List<DuServer> listServers= dbHelper.getAllDuServers();

        //start all the listeners in prefs and add the listener to the button
        name = (EditText) getActivity().findViewById(R.id.server_tittle_editText);
        hostname = (EditText) getActivity().findViewById(R.id.server_hostname_editText);
        port = (EditText) getActivity().findViewById(R.id.server_port_editText);
        path = (EditText) getActivity().findViewById(R.id.server_path_editText);

        protocol = (Spinner) getActivity().findViewById(R.id.server_protocol_spinner);
        SpinnerAdapter spinnerAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                str);
        protocol.setAdapter(spinnerAdapter);

        Button button = (Button) getActivity().findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!updating) {
                    // save new entry
                    try {
                        DuServer newServer = new DuServer(
                                name.getText().toString(),
                                protocol.getSelectedItemPosition(),
                                hostname.getText().toString(),
                                Integer.valueOf(port.getText().toString()),
                                path.getText().toString());
                        dbHelper.addDuServer(newServer);
                    }catch (Exception e){

                    }
                }else{
                    // update entry
                    try {
                        if (!(name.getText().toString().equals(selectedServer.getServerName()))) {
                            dbHelper.updateServer(
                                    selectedServer,
                                    DbContract.ServerEntry.COLUMN_SERVER_TITLE,
                                    name.getText().toString());
                        }
                        if (!(protocol.getSelectedItemPosition() == selectedServer.getProtocol())) {
                            dbHelper.updateServer(
                                    selectedServer,
                                    DbContract.ServerEntry.COLUMN_SERVER_PROTOCOL,
                                    String.valueOf(protocol.getSelectedItemId()));
                        }
                        if (!(hostname.getText().toString().equals(selectedServer.getHost()))) {
                            dbHelper.updateServer(
                                    selectedServer,
                                    DbContract.ServerEntry.COLUMN_SERVER_HOST,
                                    hostname.getText().toString());
                        }
                        if (!(port.getText().toString().equals(selectedServer.getHost()))) {
                            dbHelper.updateServer(
                                    selectedServer,
                                    DbContract.ServerEntry.COLUMN_SERVER_PORT,
                                    port.getText().toString());
                        }
                        if (!(path.getText().toString().equals(selectedServer.getHost()))) {
                            dbHelper.updateServer(
                                    selectedServer,
                                    DbContract.ServerEntry.COLUMN_SERVER_PATH,
                                    path.getText().toString());
                        }
                    }catch (Exception e){
                    }
                }
                getFragmentManager().popBackStack();
            }
        });

        if(getArguments()!= null) {
            updating = true;
            clickPos = getArguments().getInt(CLICK_BUNDLE_POSITION_KEY);
            selectedServer = listServers.get(clickPos);
            showSelectedServerOptions(selectedServer);
        }

    }

    private void showSelectedServerOptions(DuServer selectedServer) {
        name.setText(selectedServer.getServerName());
        hostname.setText(selectedServer.getHost());
        protocol.setSelection(selectedServer.getProtocol());
        port.setText(String.valueOf(selectedServer.getPort()));
        path.setText(selectedServer.getPath());
    }
}