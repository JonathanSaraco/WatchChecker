package com.example.watchchecker.io;

import android.support.annotation.VisibleForTesting;

import com.example.watchchecker.dataModel.WatchTimekeepingMap;
import com.example.watchchecker.util.IO_Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Class that serializes a {@link WatchTimekeepingMap} and persists it to a file in internal storage
 */
public class TimekeepingMapWriter {

    private final File writeDirectory;

    public TimekeepingMapWriter(File writeDirectory) {
        this.writeDirectory = writeDirectory;
    }

    public void write(WatchTimekeepingMap watchTimekeepingMap) throws IOException {
        String mapAsJson = getMapAsJson(watchTimekeepingMap);
        FileOutputStream fileOutputStream = new FileOutputStream(new File(writeDirectory, IO_Util.DEFAULT_TIMEKEEPING_MAP_FILENAME));
        fileOutputStream.write(mapAsJson.getBytes());
    }

    @VisibleForTesting()
    public static String getMapAsJson(WatchTimekeepingMap watchTimekeepingMap) {
        Gson gson = new GsonBuilder().setPrettyPrinting().enableComplexMapKeySerialization().create();
        return gson.toJson(watchTimekeepingMap, WatchTimekeepingMap.class);
    }
}
