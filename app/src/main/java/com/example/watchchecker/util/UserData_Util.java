package com.example.watchchecker.util;

import com.example.watchchecker.data.UserData;
import com.example.watchchecker.dataModel.TimekeepingEntry;
import com.example.watchchecker.dataModel.WatchDataEntry;
import com.example.watchchecker.dataModel.WatchTimekeepingMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class UserData_Util {

    public static final void addWatchDataEntry(WatchDataEntry watchDataEntry) {
        UserData.getWatchTimekeepingMap().getDataMap().putIfAbsent(watchDataEntry, new ArrayList<>());
    }

    public static final void addTimekeepingEntry(WatchDataEntry watchDataEntry, TimekeepingEntry timekeepingEntry) {
        WatchTimekeepingMap watchTimekeepingMap = UserData.getWatchTimekeepingMap();
        List<TimekeepingEntry> timekeepingEntries = watchTimekeepingMap.getDataMap().getOrDefault(watchDataEntry, Collections.emptyList());
        Objects.requireNonNull(timekeepingEntries).add(timekeepingEntry);
        watchTimekeepingMap.getDataMap().put(watchDataEntry, timekeepingEntries);
    }
}
