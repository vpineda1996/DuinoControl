package com.vpineda.duinocontrol.app.classes.ui.fragments;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.Toast;
import com.vpineda.duinocontrol.app.R;
import com.vpineda.duinocontrol.app.adapters.DrawerAdapter;
import com.vpineda.duinocontrol.app.classes.model.Room;
import com.vpineda.duinocontrol.app.classes.model.toggles.Toggle;
import com.vpineda.duinocontrol.app.classes.ui.settings.EditRoomFragment;
import com.vpineda.duinocontrol.app.databases.DbContract;
import com.vpineda.duinocontrol.app.databases.DbHelper;

import java.util.List;
import java.util.UUID;

/**
 * Created by vpineda1996 on 2015-01-16.
 */
public class NavigationalDrawerFragment extends Fragment{

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;

    private RecyclerView mRecycleView;
    private DrawerAdapter mDrawerAdapter;
    private List<String> roomsName;

    private CharSequence mTitle;
    private Button mButton;
    private Room roomSelected;

    /**
     * You must call newInstance before setting up the fragment this will add setup
     * the toolbar variable abd drawer layout
     * @param drawerLayout the drawer layout of the app
     * @param toolbar action bar of the app
     */
    public void newInstance(DrawerLayout drawerLayout, Toolbar toolbar) {
        mDrawerLayout = drawerLayout;
        mToolbar = toolbar;
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),
                drawerLayout,
                mToolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // delete all of the menu settings, refresh et etc
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }
        };
        // Set the drawer listener to the new mDrawerToggle that we just created
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
        // get all roomsName from the server and save it to roomsName
        getDataFromDatabase();
    }

    /**
     * sets mRecyclerView to the view in the drawer, the drawer adapter gets the room list and
     * sends it to the DrawerAdapter
     * @param savedInstanceState the bundle
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Setup the navigational drawer
        mDrawerAdapter = new DrawerAdapter(getActivity(), new DrawerAdapter.OnItemClickListener() {
            // Set up the listeners from the DrawerAdapter
            @Override
            public void onClick(View view, int position) {
                selectItem(position);
            }

            @Override
            public void onCreateContextMenu(ContextMenu menu) {
                MenuInflater inflater = getActivity().getMenuInflater();
                inflater.inflate(R.menu.classes_model_ui_fragments_navigational_drawer_room_recycler_view_context_menu, menu);
            }
        }, roomsName);
        mRecycleView.setAdapter(mDrawerAdapter);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Setup the button
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDrawerAddButtonClick(v);
            }
        });
    }

    /**
     * View inflater and gets the id's of the elements in the view
     * @return the inflated view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.classes_ui_fragments_navigational_drawer,container);
        mButton = (Button) view.findViewById(R.id.ui_fragments_navigationaldrawer_add_room_icon);
        mRecycleView = (RecyclerView) view.findViewById(R.id.ui_fragments_navigationaldrawer_drawer_recycler_view);
        return view;
    }

    /**
     * Called when an a room has been long clicked and an option in the menu has been selected
     * @param item the item has been selected
     * @return true when it ends gracefully
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_room:
                Toast.makeText(getActivity(), "Edit", Toast.LENGTH_SHORT).show();
                //TODO
                break;
            case R.id.remove_room:
                //TODO: remove room from id
                break;
        }
        return true;
    }

    /**
     * Gets the list of roomsName from database and assigns it to roomsName
     */
    public void getDataFromDatabase(){
        DbHelper dbHelper = new DbHelper(getActivity());
        roomsName = dbHelper.getAllRoomsProperty(DbContract.RoomEntry.COLUMN_ROOM_TITLE);
        // if getDataFromDatabase is called and the mDrawerAdapter is not null (that is it is currently running)
        // then update the data in mDrawerAdapter and update the view
        if(mDrawerAdapter != null) {
            mDrawerAdapter.updateData(roomsName);
            mDrawerAdapter.notifyDataSetChanged();
        }
    }

    /**
     * called when an item is selected in the recycler view
     * @param position the position that has been selected if you want default run it with -1
     */
    public void selectItem(int position) {
        RoomFragment fragment = new RoomFragment();
        DbHelper helper = new DbHelper(getActivity());
        List<String> roomsUUID = helper.getAllRoomsProperty(DbContract.RoomEntry.COLUMN_ROOM_UUID);
        // if room is null, means that we don't know where we are thus, lets go to
        // the default view otherwise search for the specific toggles for the room
        if (position >= 0) {
            roomSelected = helper.getRoom(UUID.fromString(roomsUUID.get(position)));
            fragment.newInstance(roomSelected);
            setTitle(roomSelected.getName());
            Toast.makeText(getActivity(),roomSelected.getName(),Toast.LENGTH_SHORT).show();
        }else {
            // Setup default room
            List<Toggle> toggles;
            // TODO
        }
        helper.close();

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.content_frame, fragment)
                .commit();

        // Close the drawer once the fragmentManager has finished
        Log.i("Fragment Position",String.valueOf(position));
        mDrawerLayout.closeDrawer(getView());
    }


    private void onDrawerAddButtonClick(View view) {
        EditRoomFragment editRoomFragment = new EditRoomFragment();
        editRoomFragment.newInstance(this);
        editRoomFragment.show(getFragmentManager(),"roomEditFragment");
    }


    private void setTitle(CharSequence title) {
        mTitle = title;
        Log.i("Title", (String) mTitle);
        if(mToolbar != null) {
            mToolbar.setTitle(mTitle);
        }
    }

    public Room getRoomSelected() {
        return roomSelected;
    }
}
