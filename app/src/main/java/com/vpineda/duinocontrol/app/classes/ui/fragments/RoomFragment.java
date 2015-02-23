package com.vpineda.duinocontrol.app.classes.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.vpineda.duinocontrol.app.R;
import com.vpineda.duinocontrol.app.classes.model.Room;
import com.vpineda.duinocontrol.app.classes.model.toggles.Toggle;
import com.vpineda.duinocontrol.app.classes.ui.adapters.ToggleAdapter;
import com.vpineda.duinocontrol.app.databases.DbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vpineda1996 on 2015-02-07.
 */
public class RoomFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ToggleAdapter mToggleAdapter;
    private List<Toggle> toggles;
    private Room room;

    /**
     * run before starting the fragment transaction
     * @param room
     */
    public void newInstance(Room room){
        this.room = room;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.classes_ui_fragments_room_fragment,container,false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.classes_ui_fragments_room_fragment_recycle_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getTogglesFromDatabase(view);
    }

    /**
     * get all the toggles from the database and then display them
     * in the recycler view by calling setUpRecyclerView.
     * room must be a valid Room in order to display the room's toggles
     * otherwise it will display all of the Toggles
     * @param view the current view
     */
    private void getTogglesFromDatabase(View view) {
        toggles = new ArrayList<>();
        // if the newInstance method is called then it will assign the desired room
        // but if it isn't fallback and display the default
        new Thread(new Runnable() {
            @Override
            public void run() {
                DbHelper helper = new DbHelper(getActivity());
                if(room != null) {
                    toggles = helper.getAllRoomToggles(room);
                    setUpRecyclerView();
                }else {
                    // TODO save the current instace state so if the user rotates the screen we will be able to restore data
                    toggles = helper.getAllToggles();
                    setUpRecyclerView();
                }
                helper.close();

            }
        }).run();
    }

    /**
     * Sets the current
     */
    private void setUpRecyclerView() {
        // Set the adapter and linear view TODO: can we use a better thing than GridLayoutManager?
        mToggleAdapter = new ToggleAdapter(getActivity(),toggles);
        mRecyclerView.setAdapter(mToggleAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
    }
}
