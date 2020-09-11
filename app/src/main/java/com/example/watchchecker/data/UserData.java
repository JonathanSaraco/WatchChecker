package com.example.watchchecker.data;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.util.Log;

import com.example.watchchecker.activity.WriteUserDataService;
import com.example.watchchecker.io.TimekeepingMapWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Static class to give easy access to objects storing user data.
 */
public class UserData {
    private static WatchTimekeepingMap WATCH_TIMEKEEPING_MAP = null;

    public static void saveData(Context context) {
        Intent finalizeIntent = new Intent(context, WriteUserDataService.class);
        finalizeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(finalizeIntent);
    }

    public static boolean containsWatchDataEntry(WatchDataEntry watchDataEntry) {
        try {
            getEquivalentWatchDataEntry(watchDataEntry);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static WatchTimekeepingMap getWatchTimekeepingMap() {
        if (WATCH_TIMEKEEPING_MAP == null) {
            throw new UnsupportedOperationException();
        }
        return WATCH_TIMEKEEPING_MAP;
    }

    public static List<WatchDataEntry> getWatchDataEntries() {
        List<WatchDataEntry> watchDataEntries = new ArrayList<>(WATCH_TIMEKEEPING_MAP.getDataMap().keySet());
        watchDataEntries.sort(Comparator.comparing(watchDataEntry -> {
            try {
                return watchDataEntry.getCreationDate().getComplexDate();
            } catch (Exception e) {
                return new Date();
            }
        }));
        return watchDataEntries;
    }

    public static void addWatchDataEntry(WatchDataEntry watchDataEntry) {
        getWatchTimekeepingMap().getDataMap().putIfAbsent(watchDataEntry, new ArrayList<>());
        notifyObservers();
    }

    /**
     * Edits a {@link WatchDataEntry} with new information given to it by {@param entryWithNewInfo}.
     */
    public static void editWatchDataEntry(WatchDataEntry entryToEdit, WatchDataEntry entryWithNewInfo) {
        WatchDataEntry watchDataEntryAsKey = getEquivalentWatchDataEntry(entryToEdit);
        entryToEdit.setModel(entryWithNewInfo.getModel());
        entryToEdit.setBrand(entryWithNewInfo.getBrand());
        entryToEdit.setMovement(entryWithNewInfo.getMovement());
        entryToEdit.setPurchaseDate(entryWithNewInfo.getPurchaseDate());
        entryToEdit.setLastServiceDate(entryWithNewInfo.getLastServiceDate());
        notifyObservers();
    }

    /**
     * Adds a new {@link WatchDataEntry} with a pre-defined list of {@link TimekeepingEntry} objects.
     * ONLY USE IF YOU ARE OKAY WITH FULLY REPLACING DATA.
     */
    public static void addWatchDataEntry(WatchDataEntry watchDataEntry, List<TimekeepingEntry> timekeepingEntries) {
        getWatchTimekeepingMap().getDataMap().put(watchDataEntry, timekeepingEntries);
        notifyObservers();
    }

    public static void removeWatchDataEntry(WatchDataEntry watchDataEntry) {
        WatchDataEntry watchDataEntryAsKey = getEquivalentWatchDataEntry(watchDataEntry);
        getWatchTimekeepingMap().getDataMap().remove(watchDataEntryAsKey);
        notifyObservers();
    }

    private static void notifyObservers() {
        getWatchTimekeepingMap().changed();
        getWatchTimekeepingMap().notifyObservers();
    }

    /**
     * @return A {@link WatchDataEntry} that is equivalent to {@param watchDataEntry}. Needed because
     * we need to be able to access a real {@link WatchDataEntry} using a {@link Parcel} instance
     */
    private static WatchDataEntry getEquivalentWatchDataEntry(WatchDataEntry watchDataEntry) {
        WatchDataEntry watchDataEntryAsKey = watchDataEntry;
        if (!getWatchTimekeepingMap().getDataMap().containsKey(watchDataEntry)) {
            Optional<WatchDataEntry> actualWatchDataEntry = getWatchTimekeepingMap().getDataMap().keySet().stream()
                    .filter(watchDataEntry::equals)
                    .findAny();
            if (actualWatchDataEntry.isPresent()) {
                watchDataEntryAsKey = actualWatchDataEntry.get();
            } else {
                throw new IllegalStateException();
            }
        }
        return watchDataEntryAsKey;
    }

    /**
     * @return
     */
    public static List<TimekeepingEntry> getTimekeepingEntries(WatchDataEntry watchDataEntry) {
        Optional<List<TimekeepingEntry>> timekeepingEntries = getWatchTimekeepingMap().getDataMap().entrySet().stream()
                .filter(dataMapEntry -> dataMapEntry.getKey().equals(watchDataEntry))
                .map(Map.Entry::getValue)
                .findAny();
        if (timekeepingEntries.isPresent()){
            return timekeepingEntries.get().stream()
                    .sorted(Comparator.comparing((TimekeepingEntry timingEntry) -> timingEntry.getLastTimekeepingEvent().getTime()).reversed())
                    .collect(Collectors.toList());
        }
        Log.e("UserData", "Failed to get timekeeping entries.");
        throw new IllegalStateException();
    }

    public static Map.Entry<WatchDataEntry, List<TimekeepingEntry>> getDataMapEntry(WatchDataEntry watchDataEntry) {
        Optional<Map.Entry<WatchDataEntry, List<TimekeepingEntry>>> dataMapEntry = getWatchTimekeepingMap().getDataMap().entrySet().stream()
                .filter(entry -> entry.getKey().equals(watchDataEntry))
                .findAny();
        if (dataMapEntry.isPresent()) return dataMapEntry.get();
        Log.e("UserData", "Failed to get entry of user data map.");
        throw new IllegalStateException();
    }

    public static TimekeepingEntry getLastTimekeepingEntry(WatchDataEntry watchDataEntry) {
        List<TimekeepingEntry> timekeepingEntries = getTimekeepingEntries(watchDataEntry);
        if (timekeepingEntries.isEmpty()) {
            TimekeepingEntry timekeepingEntry = new TimekeepingEntry();
            timekeepingEntries.add(timekeepingEntry);
        }
        return timekeepingEntries.get(0);
    }

    public static void addTimekeepingEntry(WatchDataEntry watchDataEntry, TimekeepingEntry timekeepingEntry) {
        Map.Entry<WatchDataEntry, List<TimekeepingEntry>> dataMapEntry = getDataMapEntry(watchDataEntry);
        List<TimekeepingEntry> timekeepingEntries = new ArrayList<>(dataMapEntry.getValue());
        timekeepingEntries.add(timekeepingEntry);
        getWatchTimekeepingMap().getDataMap().put(dataMapEntry.getKey(), timekeepingEntries);
        notifyObservers();
    }

    public static void removeTimekeepingEntry(WatchDataEntry watchDataEntry, TimekeepingEntry timekeepingEntry) {
        Map.Entry<WatchDataEntry, List<TimekeepingEntry>> dataMapEntry = getDataMapEntry(watchDataEntry);
        List<TimekeepingEntry> timekeepingEntries = new ArrayList<>(dataMapEntry.getValue());
        timekeepingEntries.remove(timekeepingEntry);
        getWatchTimekeepingMap().getDataMap().put(watchDataEntry, timekeepingEntries);
        notifyObservers();
    }

    public static void addTimingEntry(WatchDataEntry watchDataEntry, TimingEntry timingEntry, boolean newRun) {
        WatchDataEntry watchDataEntryAsKey = getEquivalentWatchDataEntry(watchDataEntry);
        if (!newRun) {
            TimekeepingEntry lastTimekeepingEntry = getLastTimekeepingEntry(watchDataEntryAsKey);
            lastTimekeepingEntry.addTimingEntry(timingEntry);
        } else {
            TimekeepingEntry newTimekeepingEntry = new TimekeepingEntry();
            newTimekeepingEntry.addTimingEntry(timingEntry);
            addTimekeepingEntry(watchDataEntry, newTimekeepingEntry);
        }
        notifyObservers();
    }

    public static void removeTimingEntry(TimekeepingEntry timekeepingEntry, TimingEntry timingEntry) {
        timekeepingEntry.removeTimingEntry(timingEntry);
        notifyObservers();
    }

    public static void setWatchTimekeepingMap(WatchTimekeepingMap watchTimekeepingMap) {
        UserData.WATCH_TIMEKEEPING_MAP = watchTimekeepingMap;
        notifyObservers();
    }

    public static void setWatchDataEntryImage(WatchDataEntry watchDataEntry, String imagePath) {
        WatchDataEntry watchDataEntryAsKey = getEquivalentWatchDataEntry(watchDataEntry);
        watchDataEntryAsKey.setImagePath(imagePath);
        notifyObservers();
    }

    public static void writeUserData(Context context) throws IOException {
        if (!getWatchTimekeepingMap().areAllChangesCommitted()) {
            new TimekeepingMapWriter(context).write(WATCH_TIMEKEEPING_MAP);
        }
    }
}
