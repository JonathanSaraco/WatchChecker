package com.example.watchchecker.data;

import android.content.Context;
import android.content.Intent;

import com.example.watchchecker.activity.WriteUserDataService;
import com.example.watchchecker.io.TimekeepingMapWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
            getWatchDataEntryKey(watchDataEntry);
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
     * Adds a new {@link WatchDataEntry} with a pre-defined list of {@link TimekeepingEntry} objects.
     * ONLY USE IF YOU ARE OKAY WITH FULLY REPLACING DATA.
     */
    public static void addWatchDataEntry(WatchDataEntry watchDataEntry, List<TimekeepingEntry> timekeepingEntries) {
        getWatchTimekeepingMap().getDataMap().put(watchDataEntry, timekeepingEntries);
        notifyObservers();
    }

    public static void removeWatchDataEntry(WatchDataEntry watchDataEntry) {
        WatchDataEntry watchDataEntryAsKey = getWatchDataEntryKey(watchDataEntry);
        getWatchTimekeepingMap().getDataMap().remove(watchDataEntryAsKey);
        notifyObservers();
    }

    private static void notifyObservers() {
        getWatchTimekeepingMap().changed();
        getWatchTimekeepingMap().notifyObservers();
    }

    public static List<TimekeepingEntry> getTimekeepingEntries(WatchDataEntry watchDataEntry) {
        WatchDataEntry watchDataEntryAsKey = getWatchDataEntryKey(watchDataEntry);
        List<TimekeepingEntry> timekeepingEntries = getWatchTimekeepingMap().getTimekeepingEntries(watchDataEntryAsKey);
        timekeepingEntries.sort(Comparator.comparing(TimekeepingEntry::getLastTimekeepingEvent).reversed());
        return getWatchTimekeepingMap().getTimekeepingEntries(watchDataEntryAsKey);
    }

    public static TimekeepingEntry getLastTimekeepingEntry(WatchDataEntry watchDataEntry) {
        WatchDataEntry watchDataEntryAsKey = getWatchDataEntryKey(watchDataEntry);
        List<TimekeepingEntry> timekeepingEntries = getTimekeepingEntries(watchDataEntryAsKey);
        if (timekeepingEntries.isEmpty()) {
            TimekeepingEntry timekeepingEntry = new TimekeepingEntry();
            timekeepingEntries.add(timekeepingEntry);
        }
        return timekeepingEntries.get(0);
    }

    public static void addTimekeepingEntry(WatchDataEntry watchDataEntry, TimekeepingEntry timekeepingEntry) {
        WatchDataEntry watchDataEntryAsKey = getWatchDataEntryKey(watchDataEntry);
        List<TimekeepingEntry> timekeepingEntries = getWatchTimekeepingMap().getDataMap().getOrDefault(watchDataEntryAsKey, Collections.emptyList());
        Objects.requireNonNull(timekeepingEntries).add(timekeepingEntry);
        getWatchTimekeepingMap().getDataMap().put(watchDataEntryAsKey, timekeepingEntries);
        notifyObservers();
    }

    public static void addTimekeepingEntries(WatchDataEntry watchDataEntry, List<TimekeepingEntry> newTimekeepingEntries) {
        WatchDataEntry watchDataEntryAsKey = getWatchDataEntryKey(watchDataEntry);
        List<TimekeepingEntry> timekeepingEntries = getWatchTimekeepingMap().getDataMap().getOrDefault(watchDataEntryAsKey, Collections.emptyList());
        Objects.requireNonNull(timekeepingEntries).addAll(newTimekeepingEntries);
        getWatchTimekeepingMap().getDataMap().put(watchDataEntryAsKey, timekeepingEntries);
        notifyObservers();
    }

    public static void removeTimekeepingEntry(WatchDataEntry watchDataEntry, TimekeepingEntry timekeepingEntry) {
        WatchDataEntry watchDataEntryAsKey = getWatchDataEntryKey(watchDataEntry);
        List<TimekeepingEntry> timekeepingEntries = getWatchTimekeepingMap().getDataMap().getOrDefault(watchDataEntryAsKey, Collections.emptyList());
        Objects.requireNonNull(timekeepingEntries).remove(timekeepingEntry);
        getWatchTimekeepingMap().getDataMap().put(watchDataEntryAsKey, timekeepingEntries);
        notifyObservers();
    }

    public static void addTimingEntry(WatchDataEntry watchDataEntry, TimingEntry timingEntry, boolean newRun) {
        WatchDataEntry watchDataEntryAsKey = getWatchDataEntryKey(watchDataEntry);
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

    private static WatchDataEntry getWatchDataEntryKey(WatchDataEntry watchDataEntry) {
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

    public static void setWatchTimekeepingMap(WatchTimekeepingMap watchTimekeepingMap) {
        UserData.WATCH_TIMEKEEPING_MAP = watchTimekeepingMap;
        notifyObservers();
    }

    public static void setWatchDataEntryImage(WatchDataEntry watchDataEntry, String imagePath) {
        WatchDataEntry watchDataEntryAsKey = getWatchDataEntryKey(watchDataEntry);
        watchDataEntryAsKey.setImagePath(imagePath);
        notifyObservers();
    }

    public static void writeUserData(Context context) throws IOException {
        if (!getWatchTimekeepingMap().areAllChangesCommitted()) {
            new TimekeepingMapWriter(context).write(WATCH_TIMEKEEPING_MAP);
        }
    }
}
