package com.example.watchchecker.init;

import android.content.Context;
import android.util.Log;

import com.example.watchchecker.data.UserData;
import com.example.watchchecker.data.WatchDataEntry;
import com.example.watchchecker.util.IO_Util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Goes to {@link IO_Util#getWatchPhotoDirectory(Context)} and deletes any image files that do not
 * belong to a WatchDataEntry. Just exists to ensure that storage used for watch photos does not
 * become exorbitantly large.
 */
public class WatchPhotoCleaner implements Runnable {

    private final Context context;

    public WatchPhotoCleaner(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        File watchPhotoDirectory = IO_Util.getWatchPhotoDirectory(context);
        List<String> allowedImagePaths = UserData.getWatchDataEntries().stream()
                .map(WatchDataEntry::getImagePath)
                .collect(Collectors.toList());
        for (File file : watchPhotoDirectory.listFiles()) {
            if (!allowedImagePaths.contains(file.getPath())) {
                try {
                    FileUtils.forceDelete(file);
                    Log.i(this.getClass().getSimpleName(),  String.format("Successfully deleted image file %s without WatchDataEntry.", file.getName()));
                } catch (Exception e) {
                    Log.e(this.getClass().getSimpleName(), "Failed to delete image file without WatchDataEntry.");
                }
            }
        }
    }
}
