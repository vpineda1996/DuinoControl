package com.vpineda.duinocontrol.app;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.vpineda.duinocontrol.app.classes.ui.fragments.NavigationalDrawerFragment;
import com.vpineda.duinocontrol.app.classes.ui.settings.EditToogleFragment;
import com.vpineda.duinocontrol.app.settings.MainPreferences;

import java.util.UUID;


public class MainActivity extends ActionBarActivity {

    private DrawerLayout mDrawerLayout;
    private NavigationalDrawerFragment mDrawer;
    private Toolbar mToolbar;


    // =====================================
    // OVERWRITES
    // =====================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar();
        mDrawerLayout = (DrawerLayout) findViewById (R.id.drawer_layout);
        mDrawer = (NavigationalDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.left_drawer);
        mDrawer.newInstance(((DrawerLayout) findViewById(R.id.drawer_layout)), mToolbar);
        if(savedInstanceState == null){
            mDrawer.selectItem(-1);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, MainPreferences.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.ui_activites_main_activity_add_toggleButton){
            UUID roomSelected = mDrawer.getRoomSelected().getUuid()
            if(roomSelected != null) {
                EditToogleFragment.newInstance(roomSelected).show(getSupportFragmentManager(), "addToggle");
            }else {
                EditToogleFragment.newInstance(null).show(getSupportFragmentManager(), "addToggle");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawer.getView());
        menu.findItem(R.id.refresh_button).setVisible(!drawerOpen);
        menu.findItem(R.id.ui_activites_main_activity_add_toggleButton).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    // set background color and text color
    private void setToolbar() {
        setContentView(R.layout.activity_navigation_drawer_old);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(10);
            mToolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.app_action_bar)));
            mToolbar.setTitleTextColor(getResources().getColor(android.R.color.primary_text_dark));
        }
    }
}
