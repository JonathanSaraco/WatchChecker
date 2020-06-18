package com.example.watchchecker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.watchchecker.R;
import com.example.watchchecker.data.UserData;
import com.example.watchchecker.data.WatchDataEntry;
import com.example.watchchecker.fragment.PreferencesFragment;
import com.example.watchchecker.fragment.WatchCollectionFragment;
import com.example.watchchecker.util.BitmapUtil;
import com.example.watchchecker.util.IO_Util;
import com.example.watchchecker.util.IntentUtil;
import com.example.watchchecker.util.ThemeUtil;

public class WatchCollectionActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView = null;
    Toolbar toolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTheme(ThemeUtil.getThemeResourceID(ThemeUtil.getThemeFromPreferences(this)));
        // Needed to create file URI
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        //Set the check watch fragment at startup
        Fragment fragment = new WatchCollectionFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == IntentUtil.CAMERA_INTENT_REQUEST_CODE &&
                resultCode == RESULT_OK) {
            try {
                // Check to see if the image file needs to be rotated and re-written
                BitmapUtil.rotateAndRewriteBitmap(IO_Util.getPathForNewImage());
                // Get data from IO_Util since we can't parcel shit
                WatchDataEntry watchDataEntry = IO_Util.getWatchDataEntryForNewImage();
                UserData.setWatchDataEntryImage(watchDataEntry, IO_Util.getPathForNewImage());
                // Save timekeeping map
                UserData.saveData(getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_watch_collection) {
            //Set the check watch fragment
            Fragment fragment = new WatchCollectionFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment, "WatchCollectionFragment");
            // Clears back stack because this is the main fragment
            getSupportFragmentManager().popBackStackImmediate(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_settings) {
            Fragment fragment = new PreferencesFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment, "PreferencesFragment");
            // Don't add inordinate SettingsFragments to the back stack
            if (getSupportFragmentManager().getBackStackEntryCount() < 1) {
                fragmentTransaction.addToBackStack("PreferencesFragment");
            }
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Saves user data, we override this method because onPause() is called whenever we switch activity
     * (which is too often), and because onDestroy() is only called when we exit the app via back
     * button (not often enough).
     */
    @Override
    protected void onStop() {
        UserData.saveData(getApplicationContext());
        super.onStop();
    }
}
