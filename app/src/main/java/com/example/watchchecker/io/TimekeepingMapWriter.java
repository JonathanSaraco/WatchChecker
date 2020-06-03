package com.example.watchchecker.io;

import com.example.watchchecker.dataModel.TimekeepingEntry;
import com.example.watchchecker.dataModel.WatchDataEntry;
import com.example.watchchecker.dataModel.WatchTimekeepingMap;
import com.example.watchchecker.dataModel.WatchType;
import com.example.watchchecker.util.IO_Util;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Class that serializes a {@link WatchTimekeepingMap} and persists it to a file in internal storage
 */
public class TimekeepingMapWriter {

    private final File writeDirectory;

    public TimekeepingMapWriter(File writeDirectory) {
        this.writeDirectory = writeDirectory;
    }

    public void write(WatchTimekeepingMap watchTimekeepingMap) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().enableComplexMapKeySerialization().create();
        String mapAsJson = gson.toJson(watchTimekeepingMap, WatchTimekeepingMap.class);
        FileOutputStream fileOutputStream = new FileOutputStream(new File(writeDirectory, IO_Util.DEFAULT_TIMEKEEPING_MAP_FILENAME));
        fileOutputStream.write(mapAsJson.getBytes());
    }

    /**
     * Test writer with sample data TODO use as unit test eventually
     */
    public static void main(String[] args) {
        // Create dummary data for testing
        WatchTimekeepingMap dummyMap = new WatchTimekeepingMap();
        WatchDataEntry sarbEntry = new WatchDataEntry("Seiko",
                "SARB033",
                "6R15C",
                WatchType.DRESS,
                new Date(),
                new Date());
        dummyMap.put(sarbEntry, new TimekeepingEntry());
        try {
            new TimekeepingMapWriter(new File("C:\\Users\\JonathanSaraco\\Documents")).write(dummyMap);

            for (int i=0; i < 1_000_000_000; i++);

            // Verify it was read correctly
            WatchTimekeepingMap watchTimekeepingMap = new TimekeepingMapReader(new File("C:\\Users\\JonathanSaraco\\Documents"))
                    .read();

            Preconditions.checkArgument(watchTimekeepingMap.equals(dummyMap));
        } catch (Exception e) {
            int i=2;
        }
    }
}
