package com.example.watchchecker.io;

import androidx.annotation.VisibleForTesting;

import com.example.watchchecker.data.WatchTimekeepingEntry;
import com.example.watchchecker.data.WatchTimekeepingMap;
import com.example.watchchecker.util.IO_Util;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Parses all .wtkm files in {@code getFileDir()} and returns a {@link WatchTimekeepingMap} storing
 * the data.
 */
public class TimekeepingMapReader {

    private final File readDirectory;

    public TimekeepingMapReader(File readDirectory) {
        this.readDirectory = readDirectory;
    }

    public WatchTimekeepingMap read() throws IOException {
        WatchTimekeepingMap watchTimekeepingMap = new WatchTimekeepingMap();
        Gson gson = new Gson();
        for (File watchTimekeepingEntryFile : IO_Util.getWatchTimekeepingEntryFiles(readDirectory)) {
            WatchTimekeepingEntry watchTimekeepingEntry;
            try {
                watchTimekeepingEntry = gson.fromJson(new FileReader(watchTimekeepingEntryFile), WatchTimekeepingEntry.class);
                if (watchTimekeepingEntry != null) {
                    watchTimekeepingMap.getDataMap().put(watchTimekeepingEntry.getWatchDataEntry(), new ArrayList<>(watchTimekeepingEntry.getTimekeepingEntries()));
                }
            } catch (Exception ignore) {}
        }
        return watchTimekeepingMap;
    }

    @VisibleForTesting()
    public static WatchTimekeepingMap parseJsonAsString(Gson gson, String jsonString) {
        return gson.fromJson(jsonString, WatchTimekeepingMap.class);
    }
}
