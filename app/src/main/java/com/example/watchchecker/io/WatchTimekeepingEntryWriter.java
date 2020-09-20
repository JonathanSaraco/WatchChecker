package com.example.watchchecker.io;

import android.content.Context;

import androidx.annotation.VisibleForTesting;

import com.example.watchchecker.data.UserData;
import com.example.watchchecker.data.WatchDataEntry;
import com.example.watchchecker.data.WatchTimekeepingEntry;
import com.example.watchchecker.data.WatchTimekeepingMap;
import com.example.watchchecker.util.IO_Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Class that serializes a {@link WatchTimekeepingEntry} and persists it to a file in internal storage
 */
public class WatchTimekeepingEntryWriter {

    private final Context context;

    public WatchTimekeepingEntryWriter(Context context) {
        this.context = context;
    }

    public void writeAll(WatchTimekeepingMap watchTimekeepingMap) throws IOException {
        for (WatchDataEntry watchDataEntry : watchTimekeepingMap.getDataMap().keySet()) {
            write(watchDataEntry);
        }
    }

    public void write(WatchDataEntry watchDataEntry) throws IOException {
        String entryAsJson = getWatchTimekeepingEntryAsJson(watchDataEntry);
        FileOutputStream fileOutputStream = context.openFileOutput(IO_Util.getWatchTimekeepingEntryFileName(watchDataEntry), 0);
        fileOutputStream.write(entryAsJson.getBytes());
        fileOutputStream.close();
    }

    @VisibleForTesting
    public static String getWatchTimekeepingEntryAsJson(WatchDataEntry watchDataEntry) {
        Gson gson = new GsonBuilder().setPrettyPrinting().enableComplexMapKeySerialization().create();
        return gson.toJson(new WatchTimekeepingEntry(watchDataEntry, UserData.getTimekeepingEntries(watchDataEntry)), WatchTimekeepingEntry.class);
    }

    @VisibleForTesting()
    public static String getMapAsJson(WatchTimekeepingMap watchTimekeepingMap) {
        Gson gson = new GsonBuilder().setPrettyPrinting().enableComplexMapKeySerialization().create();
        return gson.toJson(watchTimekeepingMap, WatchTimekeepingMap.class);
    }
}
