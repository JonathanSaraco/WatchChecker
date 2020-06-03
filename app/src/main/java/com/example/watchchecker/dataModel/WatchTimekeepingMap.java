package com.example.watchchecker.dataModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Data structure to store {@link WatchDataEntry} objects with their corresponding {@link TimingEntry}
 * objects.
 */
public class WatchTimekeepingMap {

    private final Map<WatchDataEntry, List<TimekeepingEntry>> dataMap;

    public WatchTimekeepingMap() {
        dataMap = new HashMap<>();
    }

    public WatchTimekeepingMap(Map<WatchDataEntry, List<TimekeepingEntry>> watchToTimekeepingMap) {
        this.dataMap = new HashMap<>(watchToTimekeepingMap);
    }

    public Map<WatchDataEntry, List<TimekeepingEntry>> getDataMap() {
        return dataMap;
    }

    public void put(WatchDataEntry keyEntry, TimekeepingEntry timekeepingEntry) {
        List<TimekeepingEntry> entryList = dataMap.getOrDefault(keyEntry, new ArrayList<>());
        Objects.requireNonNull(entryList).add(timekeepingEntry);
        dataMap.put(keyEntry, entryList);
    }

    public List<TimekeepingEntry> getTimekeepingEntries(WatchDataEntry keyEntry) {
        return dataMap.get(keyEntry);
    }

    public WatchTimekeepingMap union(WatchTimekeepingMap mapToAdd) {
        Map<WatchDataEntry, List<TimekeepingEntry>> newMap = new HashMap<>(dataMap);
        for (WatchDataEntry watchDataEntry : mapToAdd.getDataMap().keySet()) {
            List<TimekeepingEntry> entryList = dataMap.getOrDefault(watchDataEntry, new ArrayList<>());
            Objects.requireNonNull(entryList).addAll(mapToAdd.getTimekeepingEntries(watchDataEntry));
            newMap.put(watchDataEntry, entryList);
        }
        return new WatchTimekeepingMap(newMap);
    }
}
