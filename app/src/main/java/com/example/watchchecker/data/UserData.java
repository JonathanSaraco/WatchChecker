package com.example.watchchecker.data;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;

import com.example.watchchecker.activity.WriteWatchDataEntryService;
import com.example.watchchecker.io.WatchTimekeepingEntryWriter;
import com.example.watchchecker.util.IO_Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Static class to give easy access to objects storing user data.
 */
public class UserData {
    private static WatchTimekeepingMap WATCH_TIMEKEEPING_MAP = null;

    public static void saveWatchTimekeepingEntry(Context context, WatchDataEntry watchDataEntry) {
        Intent finalizeIntent = new Intent(context, WriteWatchDataEntryService.class);
        finalizeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putParcelable(WatchDataEntry.PARCEL_KEY, watchDataEntry);
        finalizeIntent.putExtras(bundle);
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
    public static void editWatchDataEntry(Context context, WatchDataEntry entryToEdit, WatchDataEntry entryWithNewInfo) {
        IO_Util.removeWatchTimekeepingEntryFile(context, entryToEdit);
        WatchDataEntry watchDataEntryAsKey = getEquivalentWatchDataEntry(entryToEdit);
        watchDataEntryAsKey.setModel(entryWithNewInfo.getModel());
        watchDataEntryAsKey.setBrand(entryWithNewInfo.getBrand());
        watchDataEntryAsKey.setMovement(entryWithNewInfo.getMovement());
        watchDataEntryAsKey.setPurchaseDate(entryWithNewInfo.getPurchaseDate());
        watchDataEntryAsKey.setLastServiceDate(entryWithNewInfo.getLastServiceDate());
        UserData.saveWatchTimekeepingEntry(context, watchDataEntryAsKey);
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
        for (Map.Entry<WatchDataEntry, List<TimekeepingEntry>> watchTimekeepingEntry : getWatchTimekeepingMap().getDataMap().entrySet()) {
            if (watchTimekeepingEntry.getKey().equals(watchDataEntry)) {
                return watchTimekeepingEntry.getKey();
            }
        }
        Log.e(UserData.class.getSimpleName(), "Could not find equivalent WatchDataEntry");
        throw new IllegalStateException();
    }

    /**
     * @return
     */
    public static List<TimekeepingEntry> getTimekeepingEntries(WatchDataEntry watchDataEntry) {
        for (Map.Entry<WatchDataEntry, List<TimekeepingEntry>> timekeepingMapEntry : getWatchTimekeepingMap().getDataMap().entrySet()) {
            if (timekeepingMapEntry.getKey().equals(watchDataEntry)) {
                return timekeepingMapEntry.getValue();
            }
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
        timekeepingEntries.add(0, timekeepingEntry);
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

    public static void writeWatchDataEntry(Context context, WatchDataEntry watchDataEntry) throws IOException {
        new WatchTimekeepingEntryWriter(context).write(watchDataEntry);
    }
}
