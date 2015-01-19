package com.vpineda.duinocontrol.app;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.vpineda.duinocontrol.app.fragments.NavigationalDrawerFragment;
import com.vpineda.duinocontrol.app.settings.EditServerFragment;
import com.vpineda.duinocontrol.app.settings.MainPreferences;


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
        mDrawer.setUp(((DrawerLayout) findViewById(R.id.drawer_layout)), mToolbar);
        if(savedInstanceState == null){
            mDrawer.selectItem(0);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, MainPreferences.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawer.getView());
        menu.findItem(R.id.refresh_button).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }


    private void setToolbar() {
        setContentView(R.layout.activity_navigation_drawer);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.holo_blue_dark)));
        }
    }

    public void onButtonDrawerAdd(View view) {
        getSupportFragmentManager().beginTransaction()
                .addToBackStack("add_room")
                .replace(android.R.id.content, new EditServerFragment())
                .commit();
    }
}
