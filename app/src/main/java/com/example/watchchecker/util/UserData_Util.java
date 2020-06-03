package com.example.watchchecker.util;

import com.example.watchchecker.data.UserData;
import com.example.watchchecker.dataModel.TimekeepingEntry;
import com.example.watchchecker.dataModel.WatchDataEntry;
import com.example.watchchecker.dataModel.WatchTimekeepingMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Class for static utility methods when managing the elements of the fields of {@link UserData}.
 */
public class UserData_Util {

    public static void addWatchDataEntry(WatchDataEntry watchDataEntry) {
        UserData.getWatchTimekeepingMap().getDataMap().putIfAbsent(watchDataEntry, new ArrayList<>());
    }

    public static void removeWatchDataEntry(WatchDataEntry watchDataEntry) {
        UserData.getWatchTimekeepingMap().getDataMap().remove(watchDataEntry);
    }

    public static void addTimekeepingEntry(WatchDataEntry watchDataEntry, TimekeepingEntry timekeepingEntry) {
        WatchTimekeepingMap watchTimekeepingMap = UserData.getWatchTimekeepingMap();
        List<TimekeepingEntry> timekeepingEntries = watchTimekeepingMap.getDataMap().getOrDefault(watchDataEntry, Collections.emptyList());
        Objects.requireNonNull(timekeepingEntries).add(timekeepingEntry);
        watchTimekeepingMap.getDataMap().put(watchDataEntry, timekeepingEntries);
    }

    public static void removeTimekeepingEntry(WatchDataEntry watchDataEntry, TimekeepingEntry timekeepingEntry) {
        WatchTimekeepingMap watchTimekeepingMap = UserData.getWatchTimekeepingMap();
        List<TimekeepingEntry> timekeepingEntries = watchTimekeepingMap.getDataMap().getOrDefault(watchDataEntry, Collections.emptyList());
        Objects.requireNonNull(timekeepingEntries).remove(timekeepingEntry);
        UserData.getWatchTimekeepingMap().getDataMap().put(watchDataEntry, timekeepingEntries);
    }
}
