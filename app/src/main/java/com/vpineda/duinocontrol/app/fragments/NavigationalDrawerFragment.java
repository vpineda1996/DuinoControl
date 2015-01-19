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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.vpineda.duinocontrol.app.R;
import com.vpineda.duinocontrol.app.adapters.DrawerAdapter;
import com.vpineda.duinocontrol.app.databases.DbHelper;
import com.vpineda.duinocontrol.app.databases.Room;

import java.util.List;

/**
 * Created by vpineda1996 on 2015-01-16.
 */
public class NavigationalDrawerFragment extends Fragment implements DrawerAdapter.OnItemClickListener {

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
        return inflater.inflate(R.layout.fragmnet_navigation_drawer,container);
    }

    public void getData(){
        DbHelper dbHelper = new DbHelper(getActivity());
        rooms = dbHelper.getAllRooms();
    }

    /* The click listener for RecyclerView in the navigation drawer */
    @Override
    public void onClick(View view, int position) {
        selectItem(position);
    }

    public void selectItem(int position) {
        // update the main content by replacing fragments
        Bundle bundle = new Bundle();
        bundle.putInt("roomID",position);
        RoomFragment fragment = new RoomFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.content_frame, fragment)
                .commit();

        // update selected item title, then close the drawer
        setTitle(rooms.get(position).getName());
        Log.i("test",String.valueOf(position));
        mDrawerLayout.closeDrawer(getView());
    }


    public void setTitle(CharSequence title) {
        mTitle = title;
        Log.i("Title", (String) mTitle);
        if(mToolbar != null) {
            mToolbar.setTitle(mTitle);
        }
    }




}
