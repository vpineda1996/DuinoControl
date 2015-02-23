package com.vpineda.duinocontrol.app.classes.ui.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.vpineda.duinocontrol.app.R;
import com.vpineda.duinocontrol.app.classes.model.Server;
import com.vpineda.duinocontrol.app.classes.model.toggles.Toggle;
import com.vpineda.duinocontrol.app.classes.model.toggles.ToggleTypes;
import com.vpineda.duinocontrol.app.databases.DbContract;
import com.vpineda.duinocontrol.app.databases.DbHelper;
import com.vpineda.duinocontrol.app.settings.EditServerFragment;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by vpineda1996 on 2015-02-16.
 */
public class EditToogleFragment extends DialogFragment {
    private String toggleName, serverUUID, toggleUUID;
    private ToggleTypes toggleType;
    private List<UUID> roomsUUID;
    private int togglePin;

    private Spinner mServerSpinner, mTypeSpinner;
    private EditText mEditTextName;
    private EditText mEditTextPin;

    private List<String> serversName;
    private boolean newToggle = true;

    public static String ROOMUUIDBUNDLEKEY = "roomUUID";
    public static String NAMEBUNDLEKEY = "name";
    public static String PINBUNDLEKEY = "pin";
    public static String SERVERBUNDLEKEY = "server";
    public static String TOGGLEUUIDBUNDLEKEY = "uuid";
    public static String TOGGLETYPEBUNDLEKEY = "toggletype";

    /**
     * Default constructor for this class
     * @param roomID the room where the new Toggle will be allocated
     * @return the dialog view
     */
    public static EditToogleFragment newInstance(@Nullable UUID roomID, @Nullable Toggle toggle){
        EditToogleFragment edf = new EditToogleFragment();
        // Add params to the bundle
        Bundle b = new Bundle();
        if(toggle != null){
            b.putString(NAMEBUNDLEKEY, toggle.getName());
            b.putInt(PINBUNDLEKEY, toggle.getPin());
            b.putString(ROOMUUIDBUNDLEKEY, toggle.getRoomsUUIDAsJSON().toString());
            b.putString(SERVERBUNDLEKEY, toggle.getServer().getUuid().toString());
            b.putString(TOGGLEUUIDBUNDLEKEY,toggle.getUuid().toString());
            b.putString(TOGGLETYPEBUNDLEKEY, toggle.getType().name());
        }else if (roomID != null) {
            b.putString(ROOMUUIDBUNDLEKEY, roomID.toString());
        }
        if(toggle != null || roomID != null){
            edf.setArguments(b);
        }
        return edf;
    }

    public EditToogleFragment() {
        super();
        roomsUUID = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // If we are in a room add that room to the room id list
        // and if we are currently editing a room get the values from the bundle
        if (getArguments() != null) {
            if(getArguments().getString(ROOMUUIDBUNDLEKEY) != null)
                roomsUUID.add(UUID.fromString(getArguments().getString(ROOMUUIDBUNDLEKEY)));
            if(getArguments().getString(NAMEBUNDLEKEY) != null){
                toggleName = getArguments().getString(NAMEBUNDLEKEY);
                toggleUUID = getArguments().getString(TOGGLEUUIDBUNDLEKEY);
                serverUUID = getArguments().getString(SERVERBUNDLEKEY);
                togglePin = getArguments().getInt(PINBUNDLEKEY);
                toggleType = ToggleTypes.valueOf(getArguments().getString(TOGGLETYPEBUNDLEKEY));
                newToggle = false;
            }
        }
    }

    /**
     * Creates the view, assigns all of the views so we can modify them and creates the listeners
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create the view
        View v = getActivity().getLayoutInflater().inflate(R.layout.classes_ui_settings_edit_toggle,
                (ViewGroup) getActivity().getWindow().getDecorView().getRootView(),false);
        // Assign the view ids for the spinners and editText's
        mServerSpinner = (Spinner) v.findViewById(R.id.ui_settings_edit_toggle_fragment_server_spinner);
        mTypeSpinner = (Spinner) v.findViewById(R.id.ui_settings_edit_toggle_fragment_type_spinner);
        mEditTextName = (EditText) v.findViewById(R.id.ui_settings_edit_toggle_fragment_name_editTextView);
        mEditTextPin = (EditText) v.findViewById(R.id.ui_settings_edit_toggle_fragment_pin_editTextView);

        // Set listeners to spinners
        mServerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkIfAddServerIsSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Add new view to the alert message and build it
        // Also setup the OK button and cancel button
        return new AlertDialog.Builder(getActivity()).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateDatabase();
            }
        }).setNegativeButton("Cancel", null).setView(v)
                .create();
    }

    /**
     * Called when the user changes the selection in the server spinner (if he wants to a add a new server,
     * the EditServerFragment will show up
     * @param position
     */
    private void checkIfAddServerIsSelected(int position) {
        System.out.println(String.valueOf(position) + "\n and Servers position: " + String.valueOf(serversName.size()));
        if(position > serversName.size()){
            EditServerFragment.newInstance(null).showAndSaveStackDialog(getFragmentManager());
        }
    }

    private void updateDatabase() {
        DbHelper helper = new DbHelper(getActivity());
        if (newToggle){
            // when the toggle is new we need to create it
            if ((mServerSpinner.getSelectedItemPosition() - 1) >= 0 &&
                    mServerSpinner.getSelectedItemPosition() <= helper.getAllServers().size() &&
                    mEditTextName.getText().length() > 0 &&
                    mEditTextPin.getText().length() > 0) { // if the user has modified all of the required spaces save it \
                helper.addToggle(mEditTextName.getText().toString(),                       // get the string from the Name text entry
                        UUID.randomUUID().toString(),                                      // generate a new random UUID for this toggle
                        ToggleTypes.values()[mTypeSpinner.getSelectedItemPosition()],      // transform the position of the spinner into a valid toggle type
                        Integer.valueOf(mEditTextPin.getText().toString()),                // get the value from the EditText and transform it to a integer
                        roomsUUID,                                                         // Rooms list
                        helper.getAllServers()                                             // Get all the servers from the database and get the index -1
                                .get(mServerSpinner.getSelectedItemPosition() - 1)         //    since we have Select One in the list of options in Spinner
                                .getUuid().toString());
            }else{ // else notify him that he has made a mistake
                Toast.makeText(getActivity(),"Error, couldn't save entry",Toast.LENGTH_SHORT).show();
            }
        }else { // This is when we need to update the toggle so we need to get the data from the Spinners and edit texts
            // First check if the server has been modified and if it is, update the database
            if(! helper.getAllServersProperty(DbContract.ServerEntry.COLUMN_SERVER_UUID)
                [mServerSpinner.getSelectedItemPosition() - 1].equals(serverUUID)){
                helper.updateToggle(toggleUUID,DbContract.ToggleEntry.COLUMN_TOGGLE_SERVER_UUID,
                        helper.getAllServersProperty(DbContract.ServerEntry.COLUMN_SERVER_UUID)
                [mServerSpinner.getSelectedItemPosition() - 1]);
            }
            // Then check the toggle type spinner
            if(! ToggleTypes.values()[mTypeSpinner.getSelectedItemPosition()].equals(toggleType)){
                helper.updateToggle(toggleUUID, DbContract.ToggleEntry.COLUMN_TOGGLE_TYPE,
                        ToggleTypes.values()[mTypeSpinner.getSelectedItemPosition()].name());
            }
            // Then check if the name has changed
            if(! mEditTextName.getText().toString().equals(toggleName)){
                helper.updateToggle(toggleUUID, DbContract.ToggleEntry.COLUMN_TOGGLE_TITLE,
                        mEditTextName.getText().toString());
            }
            // Lastly check if the pin number has changed
            if(Integer.valueOf(mEditTextPin.getText().toString()) != togglePin){
                helper.updateToggle(toggleUUID,DbContract.ToggleEntry.COLUMN_TOGGLE_PIN,
                        mEditTextPin.getText().toString());
            }
        }
        helper.close();
        // TODO tell the previous fragment to update the view
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Get all of the servers and rooms from the database
        DbHelper helper = new DbHelper(getActivity());
        serversName = new ArrayList<>(Arrays.asList(helper.getAllServersProperty(DbContract.ServerEntry.COLUMN_SERVER_TITLE)));
        List<String> serversNameForSpinner = new ArrayList<>(serversName);
        // Add the select one option and the Add option to the dropdown menu
        serversNameForSpinner.add(0,"Select one");
        serversNameForSpinner.add("Add..");
        // Set the spinner to all of the servers you have found and add at the end the add button
        ArrayAdapter<String> mServerSpinnerAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.classes_ui_settings_edit_toogle_server_spinner_adapter,
                serversNameForSpinner);
        mServerSpinner.setAdapter(mServerSpinnerAdapter);

        // Then proceed to setup the adater for the Type spinner
        ArrayAdapter<String> mTypeSpinnerAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.classes_ui_settings_edit_toogle_server_spinner_adapter,
                ToggleTypes.getAllToggleNames());
        mTypeSpinner.setAdapter(mTypeSpinnerAdapter);

        // Select all of the values if we opened the view from a toggle
        if(!newToggle){
            setTheViewsToTheToggle(helper);
        }

        helper.close();
    }

    private void setTheViewsToTheToggle(DbHelper helper) {
        //TODO
    }
}
