package com.example.watchchecker.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.watchchecker.R;
import com.example.watchchecker.data.UserData;
import com.example.watchchecker.data.WatchDataEntry;
import com.example.watchchecker.fragment.PreferencesFragment;
import com.example.watchchecker.fragment.WatchCollectionFragment;
import com.example.watchchecker.util.IO_Util;
import com.example.watchchecker.util.ImageUtil;
import com.example.watchchecker.util.IntentUtil;
import com.example.watchchecker.util.ThemeUtil;
import com.google.android.material.navigation.NavigationView;

public class WatchCollectionActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView = null;
    private Toolbar toolbar = null;

    private Fragment watchCollectionFragment = null;
    private Fragment preferencesFragment = null;

    private boolean isWatchCollectionVisible = false;

    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(ThemeUtil.getNoActionBarThemeResourceID(ThemeUtil.getThemeFromPreferences(this)));
        setContentView(R.layout.activity_main);
        // Needed to create file URI
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        // Set fragments, or reuse them if savedInstanceState is not null
        if (savedInstanceState == null) {
            initializeFragments();
        } else {
            redisplayFragments(savedInstanceState);
        }

        // Setup preference change listener to see when the theme changes
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        listener = (sharedPreferences1, key) -> {
            if (key.equals(getApplicationContext().getString(R.string.ref_theme_key))) {
                ThemeUtil.resetThemeConfiguration();
                // Set theme and refresh the view
                setTheme(ThemeUtil.getNoActionBarThemeResourceID(ThemeUtil.getThemeFromPreferences(this)));
                recreate();
            }
        };
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);

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

    private void redisplayFragments(Bundle savedInstanceState) {
        watchCollectionFragment = getSupportFragmentManager().findFragmentByTag("watchFrag");
        preferencesFragment = getSupportFragmentManager().findFragmentByTag("prefFrag");
        boolean isWatchCollectionVisible = savedInstanceState.getBoolean("isWatchCollectionVisible");
        if (isWatchCollectionVisible) {
            getSupportFragmentManager().beginTransaction()
                    .hide(preferencesFragment)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .hide(watchCollectionFragment)
                    .commit();
        }
    }

    private void initializeFragments() {
        watchCollectionFragment = new WatchCollectionFragment();
        preferencesFragment = new PreferencesFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, watchCollectionFragment, "watchFrag")
                .add(R.id.fragment_container, preferencesFragment, "prefFrag")
                .hide(preferencesFragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        isWatchCollectionVisible = watchCollectionFragment.isVisible();
        outState.putBoolean("isWatchCollectionVisible", isWatchCollectionVisible);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentUtil.CAMERA_INTENT_REQUEST_CODE &&
                resultCode == RESULT_OK) {
            try {
                // Check to see if the image file needs to be rotated and re-written
                ImageUtil.rotateAndRewriteBitmap(IO_Util.getPathForNewImage());
                // Get data from IO_Util since we can't parcel shit
                WatchDataEntry watchDataEntry = IO_Util.getWatchDataEntryForNewImage();
                UserData.setWatchDataEntryImage(watchDataEntry, IO_Util.getPathForNewImage());
                // Save timekeeping map
                UserData.saveData(getApplicationContext());
            } catch (Exception e) {
                Log.e("WatchCollectionActivity", "Failed to set WatchDataEntry photo");
            }
        } else if (requestCode == IntentUtil.GALLERY_INTENT_REQUEST_CODE &&
                data != null) {
            try {
                // Get path of new image file
                String newImagePath = IO_Util.getImageFile(this).getPath();
                // Read image from URI and save it to app storage
                Uri imageUri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                ImageUtil.rotateAndRewriteBitmap(bitmap, newImagePath);
                // Set photo of WatchDataEntry and save data
                WatchDataEntry watchDataEntry = IO_Util.getWatchDataEntryForNewImage();
                UserData.setWatchDataEntryImage(watchDataEntry, newImagePath);
                // Save timekeeping map
                UserData.saveData(getApplicationContext());
            } catch (Exception e) {
                Log.e("WatchCollectionActivity", "Failed to set WatchDataEntry photo");
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (preferencesFragment.isVisible()) {
                super.onBackPressed();
            } else {
                new AlertDialog.Builder(this, ThemeUtil.getDialogThemeResourceID(ThemeUtil.getThemeFromPreferences(this)))
                        .setTitle("Exit")
                        .setMessage("Are you sure you want to exit?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).setNegativeButton("No", null).show();
            }
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
            //Show the watch collection fragment
            if (!watchCollectionFragment.isVisible()) {
                // Clears back stack because this is the main fragment
                getSupportFragmentManager().popBackStackImmediate(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction()
                        .show(watchCollectionFragment)
                        .hide(preferencesFragment)
                        .commit();
            }
        } else if (id == R.id.nav_settings) {
            if (!preferencesFragment.isVisible()) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction
                        .show(preferencesFragment)
                        .hide(watchCollectionFragment);
                if (getSupportFragmentManager().getBackStackEntryCount() < 1) {
                    fragmentTransaction.addToBackStack("watchFrag");
                }
                fragmentTransaction.commit();
            }
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
