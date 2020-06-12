package com.example.watchchecker.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;

/**
 * Data structure to store {@link WatchDataEntry} objects with their corresponding {@link TimingEntry}
 * objects.
 */
public class WatchTimekeepingMap extends Observable {

    private final Map<WatchDataEntry, List<TimekeepingEntry>> dataMap;

    /**
     * Field to ensure that the map isn't written to storage all willy-nilly
     */
    private boolean allChangesCommitted = false;

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

    public void changed() {
        this.setChanged();
        allChangesCommitted = false;
    }

    public boolean areAllChangesCommitted() {
        return allChangesCommitted;
    }

    public void setAllChangesCommitted() {
        this.allChangesCommitted = true;
    }

    @Override
    public void notifyObservers() {
        super.notifyObservers();
    }
}
