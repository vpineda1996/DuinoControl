package com.vpineda.duinocontrol.app.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import com.vpineda.duinocontrol.app.R;
import com.vpineda.duinocontrol.app.databases.DbContract;
import com.vpineda.duinocontrol.app.databases.DbHelper;
import com.vpineda.duinocontrol.app.databases.Room;
import com.vpineda.duinocontrol.app.fragments.NavigationalDrawerFragment;

import java.util.List;


/**
 * Created by vpineda1996 on 2015-01-27.
 */
public class EditRoomFragment extends DialogFragment {

    private EditText mEditText;
    private Room mOldRoom;
    private String mNewRoomName;
    private DbHelper helper;
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

        View view = inflater.inflate(R.layout.edit_room_main, null);
        mEditText = (EditText) view.findViewById(R.id.nameOfRoomEditText);
        mEditText.setHint("Name");

        builder.setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // read the string in EditText if it is empty if it is dont do anything
                        // otherwise check of we have initialized mOldRoom
                        // if we have update that room otherwise create a new one
                        mNewRoomName = mEditText.getText().toString();
                        if (mOldRoom == null && !(mNewRoomName.equals(""))) {
                            helper.addRoom(new Room(mNewRoomName));
                        } else if (!(mNewRoomName.equals(""))) {
                            helper.updateRoom(mOldRoom, DbContract.RoomEntry.COLUMN_ROOM_TITLE, mNewRoomName);
                        }
                        mDrawer.getData();
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
        // activate the database helper
        helper = new DbHelper(getActivity());

        // if opened from a room option menu add add that room to mOldRoom
        if(getArguments() != null){
            int pos = getArguments().getInt("add_room");
            List<Room> rooms = helper.getAllRooms();
            mOldRoom = rooms.get(pos);
        }
    }
}