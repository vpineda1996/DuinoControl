package com.vpineda.duinocontrol.app.fragments;

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
import android.widget.Toast;
import com.vpineda.duinocontrol.app.R;
import com.vpineda.duinocontrol.app.adapters.DrawerAdapter;
import com.vpineda.duinocontrol.app.classes.model.Room;
import com.vpineda.duinocontrol.app.classes.model.Server;
import com.vpineda.duinocontrol.app.classes.model.toggles.Lights;
import com.vpineda.duinocontrol.app.classes.model.toggles.Toggle;
import com.vpineda.duinocontrol.app.classes.ui.fragments.RoomFragment;
import com.vpineda.duinocontrol.app.databases.DbHelper;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by vpineda1996 on 2015-01-16.
 */
public class NavigationalDrawerFragment extends Fragment implements DrawerAdapter.OnItemClickListener{

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;

    private RecyclerView mRecycleView;
    private DrawerAdapter mDrawerAdapter;
    private List<Room> rooms;

    private CharSequence mTitle;


    public void setUp(DrawerLayout drawerLayout, Toolbar toolbar) {
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
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
        getData();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecycleView = (RecyclerView) getActivity().findViewById(R.id.drawer_recycler_view);
        mDrawerAdapter = new DrawerAdapter(getActivity(), this, rooms);
        mRecycleView.setAdapter(mDrawerAdapter);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmnet_navigation_drawer_old,container);
    }

    public void getData(){
//        DbHelper dbHelper = new DbHelper(getActivity());
//        rooms = dbHelper.getAllRooms();
//        if(mDrawerAdapter != null) {
//            mDrawerAdapter.updateData(rooms);
//            mDrawerAdapter.notifyDataSetChanged();
//        }
    }

    /* The click listener for RecyclerView in the navigation drawer */
    @Override
    public void onClick(View view, int position) {
        selectItem(position);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.room_recycler_view_context_menu, menu);
    }

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

    public void selectItem(int position) {
        // update the main content by replacing fragments
        RoomFragment fragment = new RoomFragment();
        Room r = new Room("name",getActivity());
        DbHelper helper = new DbHelper(getActivity());

        List<UUID> rooms = new ArrayList<>();
        rooms.add(r.getUuid());
        Server server = new Server("123", URI.create("http://google.com"));
        helper.addServer(server);
        helper.addToggle(new Lights("ToggleTest",13,server,rooms));
        helper.addToggle(new Lights("ToggleTest",12,server,rooms));
        helper.addToggle(new Lights("ToggleTest",11,server,rooms));
        helper.addToggle(new Lights("ToggleTest",13,server,rooms));


        helper.close();
        fragment.newInstance(r);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.content_frame, fragment)
                .commit();

        // update selected item title, then close the drawer
        //setTitle(rooms.get(position).getName());
        Log.i("Fragment Position",String.valueOf(position));
        mDrawerLayout.closeDrawer(getView());
    }


    private void setTitle(CharSequence title) {
        mTitle = title;
        Log.i("Title", (String) mTitle);
        if(mToolbar != null) {
            mToolbar.setTitle(mTitle);
        }
    }

}
