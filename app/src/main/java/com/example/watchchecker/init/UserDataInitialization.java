package com.example.watchchecker.init;

import android.content.Context;

import com.example.watchchecker.data.DateString;
import com.example.watchchecker.data.TimekeepingEntry;
import com.example.watchchecker.data.UserData;
import com.example.watchchecker.data.WatchDataEntry;
import com.example.watchchecker.data.WatchTimekeepingMap;
import com.example.watchchecker.io.TimekeepingMapReader;

import java.io.IOException;
import java.util.Date;

/**
 * Class to read in data from {@link Context#getFilesDir()} and initialize static fields to store
 * this data.
 */
public class UserDataInitialization implements Runnable {

    private final Context context;

    public UserDataInitialization(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        try {
            initializeWatchTimekeepingMap();
        } catch (IOException ignore) {}
    }

    private void initializeWatchTimekeepingMap() throws IOException {
        UserData.setWatchTimekeepingMap(new TimekeepingMapReader(context.getFilesDir()).read());
    }
}
