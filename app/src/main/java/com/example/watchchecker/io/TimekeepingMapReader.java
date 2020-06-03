package com.example.watchchecker.io;

import com.example.watchchecker.dataModel.WatchTimekeepingMap;
import com.example.watchchecker.util.IO_Util;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
        for (File timekeepingMapFile : IO_Util.getTimekeepingMapFiles(readDirectory)) {
            WatchTimekeepingMap readTimekeepingMap = gson.fromJson(new FileReader(timekeepingMapFile), WatchTimekeepingMap.class);
            watchTimekeepingMap = watchTimekeepingMap.union(readTimekeepingMap);
        }
        return watchTimekeepingMap;
    }
}
