package com.example.watchchecker.util;

import java.io.File;
import java.io.FilenameFilter;

public class IO_Util {
    public static final String WATCH_TIMEKEEPING_MAP_EXTENSION = ".wtkm";
    public static final String DEFAULT_TIMEKEEPING_MAP_FILENAME = "wtkm" + WATCH_TIMEKEEPING_MAP_EXTENSION;
    public static final FilenameFilter WTKM_FILENAME_FILTER = (dir, name) -> name.toLowerCase().endsWith(WATCH_TIMEKEEPING_MAP_EXTENSION);

    public static File[] getTimekeepingMapFiles(File dir) {
        return dir.listFiles(IO_Util.WTKM_FILENAME_FILTER);
    }
}
