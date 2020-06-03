package com.example.watchchecker.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.watchchecker.dataModel.WatchDataEntry;

/**
 *
 */
public class WatchTimekeepingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Unparcel the WatchDataEntry that caused all this
        WatchDataEntry watchDataEntry = getIntent().getExtras().getParcelable(WatchDataEntry.PARCEL_KEY);
    }
}
