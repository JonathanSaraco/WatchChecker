package com.example.watchchecker.data;

import android.content.Context;

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
        getWatchTimekeepingMap().changed();
        getWatchTimekeepingMap().notifyObservers();
    }

    public static void removeWatchDataEntry(WatchDataEntry watchDataEntry) {
        getWatchTimekeepingMap().getDataMap().remove(watchDataEntry);
        getWatchTimekeepingMap().hasChanged();
        getWatchTimekeepingMap().changed();
        getWatchTimekeepingMap().notifyObservers();
    }

    public static List<TimekeepingEntry> getTimekeepingEntries(WatchDataEntry watchDataEntry) {
        if (!getWatchTimekeepingMap().getDataMap().containsKey(watchDataEntry)) {
            Optional<WatchDataEntry> actualWatchDataEntry = getWatchTimekeepingMap().getDataMap().keySet().stream()
                    .filter(watchDataEntry::equals)
                    .findAny();
            if (actualWatchDataEntry.isPresent()) {
                return getWatchTimekeepingMap().getTimekeepingEntries(actualWatchDataEntry.get());
            } else {
                return new ArrayList<>();
            }
        }
        return getWatchTimekeepingMap().getTimekeepingEntries(watchDataEntry);
    }

    public static void addTimekeepingEntry(WatchDataEntry watchDataEntry, TimekeepingEntry timekeepingEntry) {
        List<TimekeepingEntry> timekeepingEntries = getWatchTimekeepingMap().getDataMap().getOrDefault(watchDataEntry, Collections.emptyList());
        Objects.requireNonNull(timekeepingEntries).add(timekeepingEntry);
        getWatchTimekeepingMap().getDataMap().put(watchDataEntry, timekeepingEntries);
        getWatchTimekeepingMap().changed();
        getWatchTimekeepingMap().notifyObservers();
    }

    public static void removeTimekeepingEntry(WatchDataEntry watchDataEntry, TimekeepingEntry timekeepingEntry) {
        List<TimekeepingEntry> timekeepingEntries = getWatchTimekeepingMap().getDataMap().getOrDefault(watchDataEntry, Collections.emptyList());
        Objects.requireNonNull(timekeepingEntries).remove(timekeepingEntry);
        getWatchTimekeepingMap().getDataMap().put(watchDataEntry, timekeepingEntries);
        getWatchTimekeepingMap().changed();
        getWatchTimekeepingMap().notifyObservers();
    }

    public static void setWatchTimekeepingMap(WatchTimekeepingMap watchTimekeepingMap) {
        UserData.WATCH_TIMEKEEPING_MAP = watchTimekeepingMap;
        getWatchTimekeepingMap().changed();
        getWatchTimekeepingMap().notifyObservers();
    }

    public static void writeUserData(Context context) throws IOException {
        new TimekeepingMapWriter(context).write(WATCH_TIMEKEEPING_MAP);
    }
}
