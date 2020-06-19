package com.example.watchchecker.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
