package com.example.watchchecker.data;

import com.example.watchchecker.dataModel.WatchTimekeepingMap;

/**
 * Static class to give easy access to objects storing user data.
 */
public class UserData {
    private static WatchTimekeepingMap WATCH_TIMEKEEPING_MAP = new WatchTimekeepingMap();

    public static WatchTimekeepingMap getWatchTimekeepingMap() {
        return WATCH_TIMEKEEPING_MAP;
    }

    public static void setWatchTimekeepingMap(WatchTimekeepingMap watchTimekeepingMap) {
        UserData.WATCH_TIMEKEEPING_MAP = watchTimekeepingMap;
    }
}
