package com.vpineda.duinocontrol.app.classes.ui.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.vpineda.duinocontrol.app.R;
import com.vpineda.duinocontrol.app.classes.model.Room;
import com.vpineda.duinocontrol.app.databases.DbContract;
import com.vpineda.duinocontrol.app.databases.DbHelper;
import com.vpineda.duinocontrol.app.classes.ui.fragments.NavigationalDrawerFragment;

import java.util.List;


/**
 * Created by vpineda1996 on 2015-01-27.
 */
public class EditRoomFragment extends DialogFragment {

    private EditText mEditText;
    private String mOldRoom;
    private String mNewRoomName;
    private NavigationalDrawerFragment mDrawer;

    public void newInstance(NavigationalDrawerFragment mDrawer){
        this.mDrawer = mDrawer;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set custom layout in the alert dialog
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        View view = inflater.inflate(R.layout.classes_ui_settings_edit_room,
                (ViewGroup) getActivity().getWindow().getDecorView().getRootView(),false);
        mEditText = (EditText) view.findViewById(R.id.ui_settings_edit_room_EditText);
        // TODO replace by a resources String
        mEditText.setHint("Name");

        builder.setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // read the string in EditText if it is empty if it is dont do anything
                        // otherwise check of we have initialized mOldRoom
                        // if we have update that room otherwise create a new one
                        DbHelper helper = new DbHelper(getActivity());
                        mNewRoomName = mEditText.getText().toString();
                        if (mOldRoom == null && !(mNewRoomName.equals(""))) {
                            helper.addRoom(new Room(mNewRoomName,getActivity()));
                        } else if (!(mNewRoomName.equals(""))) {
                            //helper.updateRoom();
                        }
                        mDrawer.getDataFromDatabase();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditRoomFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // if opened from a room option menu add add that room to mOldRoom
        if(getArguments() != null){
            int pos = getArguments().getInt("add_room");
            DbHelper helper = new DbHelper(getActivity());
            List<String> rooms = helper.getAllRoomsProperty(DbContract.RoomEntry.COLUMN_ROOM_TITLE);
            mOldRoom = rooms.get(pos);
        }
    }

}