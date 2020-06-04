package com.example.watchchecker;

import com.example.watchchecker.data.TimekeepingEntry;
import com.example.watchchecker.data.WatchDataEntry;
import com.example.watchchecker.data.WatchTimekeepingMap;
import com.example.watchchecker.io.TimekeepingMapReader;
import com.example.watchchecker.io.TimekeepingMapWriter;
import com.google.gson.Gson;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

/**
 * Tests to ensure that {@link TimekeepingMapWriter} produces a Json file that is compatible with
 * and parsable by {@link TimekeepingMapReader}.
 */
public class WatchTimekeepingMapSerializationTest {

    @Test
    public void readerAndWriterAreCompatible() {
        WatchTimekeepingMap testMap = createDummyMap();
        try {
            // Get test map as String formatted as a Json
            String mapAsJson = TimekeepingMapWriter.getMapAsJson(testMap);
            // Parse it and create a copy object
            WatchTimekeepingMap watchTimekeepingMap = TimekeepingMapReader.parseJsonAsString(new Gson(), mapAsJson);
            // Check that the new map isn't empty, probably needs a more comprehensive test but I can't be bothered right now
            assertFalse(watchTimekeepingMap.getDataMap().isEmpty());
        } catch (Exception e) {
            fail();
        }
    }

    private WatchTimekeepingMap createDummyMap() {
        // Create dummy data for testing
        WatchTimekeepingMap dummyMap = new WatchTimekeepingMap();
        WatchDataEntry sarbEntry = new WatchDataEntry("Seiko",
                "SARB033",
                "6R15C",
                new Date(),
                new Date());
        dummyMap.put(sarbEntry, new TimekeepingEntry());
        return dummyMap;
    }
}
