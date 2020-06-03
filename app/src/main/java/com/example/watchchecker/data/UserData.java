package com.example.watchchecker.data;

import com.example.watchchecker.dataModel.TimekeepingEntry;
import com.example.watchchecker.dataModel.WatchDataEntry;
import com.example.watchchecker.dataModel.WatchTimekeepingMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Static class to give easy access to objects storing user data.
 */
public class UserData {
    private static WatchTimekeepingMap WATCH_TIMEKEEPING_MAP = null;

    public static WatchTimekeepingMap getWatchTimekeepingMap() {
        if (WATCH_TIMEKEEPING_MAP == null) {
            throw new UnsupportedOperationException();
        }
        return WATCH_TIMEKEEPING_MAP;
    }

    public static List<WatchDataEntry> getWatchDataEntries() {
        return new ArrayList<>(WATCH_TIMEKEEPING_MAP.getDataMap().keySet());
    }

    public static void addWatchDataEntry(WatchDataEntry watchDataEntry) {
        getWatchTimekeepingMap().getDataMap().putIfAbsent(watchDataEntry, new ArrayList<>());
    }

    public static void removeWatchDataEntry(WatchDataEntry watchDataEntry) {
        getWatchTimekeepingMap().getDataMap().remove(watchDataEntry);
    }

    public static void addTimekeepingEntry(WatchDataEntry watchDataEntry, TimekeepingEntry timekeepingEntry) {
        List<TimekeepingEntry> timekeepingEntries = getWatchTimekeepingMap().getDataMap().getOrDefault(watchDataEntry, Collections.emptyList());
        Objects.requireNonNull(timekeepingEntries).add(timekeepingEntry);
        getWatchTimekeepingMap().getDataMap().put(watchDataEntry, timekeepingEntries);
    }

    public static void removeTimekeepingEntry(WatchDataEntry watchDataEntry, TimekeepingEntry timekeepingEntry) {
        List<TimekeepingEntry> timekeepingEntries = getWatchTimekeepingMap().getDataMap().getOrDefault(watchDataEntry, Collections.emptyList());
        Objects.requireNonNull(timekeepingEntries).remove(timekeepingEntry);
        getWatchTimekeepingMap().getDataMap().put(watchDataEntry, timekeepingEntries);
    }

    public static void setWatchTimekeepingMap(WatchTimekeepingMap watchTimekeepingMap) {
        UserData.WATCH_TIMEKEEPING_MAP = watchTimekeepingMap;
    }
}