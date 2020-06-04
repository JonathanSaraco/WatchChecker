package com.example.watchchecker.init;

import android.content.Context;

import com.example.watchchecker.data.UserData;
import com.example.watchchecker.dataModel.WatchDataEntry;
import com.example.watchchecker.dataModel.WatchTimekeepingMap;
import com.example.watchchecker.dataModel.WatchType;
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

        // DEBUG TODO remove
        WatchTimekeepingMap dummyMap = new WatchTimekeepingMap();
        WatchDataEntry sarbEntry = new WatchDataEntry("Seiko",
                "SARB033",
                "6R15C",
                WatchType.DRESS,
                new Date(),
                new Date());
        WatchDataEntry sinn104Entry = new WatchDataEntry("Sinn",
                "104 St Sa I---------------",
                "SW200-1",
                WatchType.AVIATION,
                new Date(),
                new Date());
        WatchDataEntry ct701Entry = new WatchDataEntry("Modded",
                "CT701 Sumo",
                "NH35",
                WatchType.DIVER,
                new Date(),
                new Date());
        WatchDataEntry ct701Entry2 = new WatchDataEntry("Modded",
                "CT701 Sumo",
                "NH35",
                WatchType.DIVER,
                new Date(),
                new Date());
        WatchDataEntry ct701Entry3 = new WatchDataEntry("Modded",
                "CT701 Sumo",
                "NH35",
                WatchType.DIVER,
                new Date(),
                new Date());
        WatchDataEntry ct701Entry4 = new WatchDataEntry("Modded",
                "CT701 Sumo",
                "NH35",
                WatchType.DIVER,
                new Date(),
                new Date());
        UserData.addWatchDataEntry(sarbEntry);
        UserData.addWatchDataEntry(sinn104Entry);
        UserData.addWatchDataEntry(ct701Entry);
        UserData.addWatchDataEntry(ct701Entry2);
        UserData.addWatchDataEntry(ct701Entry3);
        UserData.addWatchDataEntry(ct701Entry4);
    }
}
