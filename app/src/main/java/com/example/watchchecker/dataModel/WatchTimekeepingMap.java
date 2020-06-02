package com.example.watchchecker.dataModel;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.List;

/**
 * Data structure to store {@link WatchDataEntry} objects with their corresponding {@link TimingEntry}
 * objects.
 */
public class WatchTimekeepingMap {

    private final ListMultimap<WatchDataEntry, TimekeepingEntry> watchToTimekeepingMap;

    public WatchTimekeepingMap() {
        watchToTimekeepingMap = ArrayListMultimap.create();
    }

    public void putTimekeepingEntry(WatchDataEntry keyEntry, TimekeepingEntry timekeepingEntry) {
        watchToTimekeepingMap.put(keyEntry, timekeepingEntry);
    }

    public List<TimekeepingEntry> getTimekeepingEntries(WatchDataEntry keyEntry) {
        return watchToTimekeepingMap.get(keyEntry);
    }
}
