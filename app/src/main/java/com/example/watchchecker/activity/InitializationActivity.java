package com.example.watchchecker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.watchchecker.init.UserDataInitialization;
import com.example.watchchecker.init.WatchCheckStarter;
import com.example.watchchecker.init.WatchPhotoCleaner;
import com.example.watchchecker.util.ThemeUtil;

public class InitializationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(ThemeUtil.getThemeResourceID(ThemeUtil.getThemeFromPreferences(getApplicationContext())));
        // Do app initialization
        new WatchCheckStarter().run();
        new Thread(new UserDataInitialization(getApplicationContext())).run();
        new WatchPhotoCleaner(getApplicationContext()).run();
        // Done initialization, transition to the main activity
        Intent intent = new Intent(this, WatchCollectionActivity.class);
        startActivity(intent);
        finish();
    }
}
