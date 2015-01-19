package com.vpineda.duinocontrol.app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.vpineda.duinocontrol.app.R;
import com.vpineda.duinocontrol.app.databases.DbHelper;
import com.vpineda.duinocontrol.app.databases.Room;

import java.util.List;

/**
 * Created by vpineda1996 on 2015-01-13.
 */
public class RoomFragment extends Fragment {

    private Room mRoom;
    private int mRoomSelectedFromList;
    DbHelper helper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.room_fragment,container,false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        openDbHelper();
        if(getArguments() != null){
            mRoomSelectedFromList = getArguments().getInt("roomID", 0);
            mRoom = getRoomWithIndex();
        }



    }
    private Room getRoomWithIndex() {
        List<Room> rooms = helper.getAllRooms();
        return rooms.get(mRoomSelectedFromList);
    }

    private void openDbHelper() {
        helper = new DbHelper(getActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(mRoom != null ) {
            String[] roomTogglesNames = {"Hello", "Test"};
            //helper.getAllTogglesProperty(DbContract.ToggleEntry.COLUMN_TOGGLE_TITLE, mRoom.getUuid());
            ListView listView = (ListView) getActivity().findViewById(R.id.room_ListView);
            listView.setAdapter(new ArrayAdapter<String>(
                    getActivity(),
                    android.R.layout.simple_list_item_1,
                    roomTogglesNames
            ));

        }
    }

}
