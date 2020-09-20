package com.example.watchchecker.util;

import android.content.Context;

import com.example.watchchecker.data.WatchDataEntry;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class IO_Util {
    public static final String WATCH_TIMEKEEPING_ENTRY_EXTENSION = ".wte";
    public static final String WATCH_TIMEKEEPING_MAP_EXTENSION = ".wtkm";
    public static final FilenameFilter WTKM_FILENAME_FILTER = (dir, name) -> name.toLowerCase().endsWith(WATCH_TIMEKEEPING_MAP_EXTENSION);
    public static final FilenameFilter WTE_FILENAME_FILTER = (dir, name) -> name.toLowerCase().endsWith(WATCH_TIMEKEEPING_ENTRY_EXTENSION);

    public static String getWatchTimekeepingEntryFileName(WatchDataEntry watchDataEntry) {
        return watchDataEntry.fileName() + IO_Util.WATCH_TIMEKEEPING_ENTRY_EXTENSION;
    }

    public static File[] getWatchTimekeepingEntryFiles(File dir) {
        return dir.listFiles(IO_Util.WTE_FILENAME_FILTER);
    }

    public static File getImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getWatchPhotoDirectory(context);
        storageDir.mkdirs();
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );
        return image;
    }

    public static File getWatchPhotoDirectory(Context context) {
        File photoDirectory = new File(context.getFilesDir(), "watchPhotos");
        photoDirectory.mkdirs();
        return photoDirectory;
    }

    public static WatchDataEntry watchDataEntry = null;
    public static String imagePath = "";

    public static WatchDataEntry getWatchDataEntryForNewImage() {
        return watchDataEntry;
    }

    public static void setWatchDataEntryForNewImage(WatchDataEntry watchDataEntry) {
        IO_Util.watchDataEntry = watchDataEntry;
    }

    public static String getPathForNewImage() {
        return imagePath;
    }

    public static void setImagePathForNewImage(String imagePath) {
        IO_Util.imagePath = imagePath;
    }

    public static void removeWatchTimekeepingEntryFile(Context context, WatchDataEntry originalWatchDataEntry) {
        File directory = context.getFilesDir();
        Optional<File> originalWTEFile = getFileBelongingToWTE(directory, originalWatchDataEntry);
        originalWTEFile.ifPresent(File::delete);
    }

    private static Optional<File> getFileBelongingToWTE(File directory, WatchDataEntry originalWatchDataEntry) {
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().startsWith(originalWatchDataEntry.fileName().toLowerCase()));
        if (files != null && files.length > 0) {
            return Optional.of(files[0]);
        }
        return Optional.empty();
    }
}
