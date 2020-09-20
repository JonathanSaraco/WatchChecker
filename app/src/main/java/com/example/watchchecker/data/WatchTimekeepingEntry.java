package com.example.watchchecker.data;

import java.util.ArrayList;
import java.util.List;

public class WatchTimekeepingEntry {

    private WatchDataEntry watchDataEntry;

    private List<TimekeepingEntry> timekeepingEntries;

    public WatchTimekeepingEntry(WatchDataEntry watchDataEntry) {
        this(watchDataEntry, new ArrayList<>());
    }

    public WatchTimekeepingEntry(WatchDataEntry watchDataEntry, List<TimekeepingEntry> timekeepingEntries) {
        this.watchDataEntry = watchDataEntry;
        this.timekeepingEntries = timekeepingEntries;
    }

    public WatchDataEntry getWatchDataEntry() {
        return watchDataEntry;
    }

    public List<TimekeepingEntry> getTimekeepingEntries() {
        return timekeepingEntries;
    }
}
