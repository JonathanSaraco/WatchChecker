package com.example.watchchecker.io;

import android.content.Context;

import androidx.annotation.VisibleForTesting;

import com.example.watchchecker.data.WatchTimekeepingMap;
import com.example.watchchecker.util.IO_Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Class that serializes a {@link WatchTimekeepingMap} and persists it to a file in internal storage
 */
public class TimekeepingMapWriter {

    private final Context context;

    public TimekeepingMapWriter(Context context) {
        this.context = context;
    }

    public void write(WatchTimekeepingMap watchTimekeepingMap) throws IOException {
        String mapAsJson = getMapAsJson(watchTimekeepingMap);
        FileOutputStream fileOutputStream = context.openFileOutput(IO_Util.DEFAULT_TIMEKEEPING_MAP_FILENAME, 0);
        fileOutputStream.write(mapAsJson.getBytes());
        fileOutputStream.close();
    }

    @VisibleForTesting()
    public static String getMapAsJson(WatchTimekeepingMap watchTimekeepingMap) {
        Gson gson = new GsonBuilder().setPrettyPrinting().enableComplexMapKeySerialization().create();
        return gson.toJson(watchTimekeepingMap, WatchTimekeepingMap.class);
    }
}
