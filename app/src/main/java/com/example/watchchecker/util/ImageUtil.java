package com.example.watchchecker.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import com.example.watchchecker.data.WatchDataEntry;
import com.google.common.base.Preconditions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Utility class for methods involving {@link Bitmap}s and operations involving them.
 */
public class ImageUtil {

    public static final String TAKE_PHOTO_CHOICE = "Capture image";
    public static final String ATTACH_PHOTO_CHOICE = "Choose from gallery";
    public static final String[] imageChoices = {TAKE_PHOTO_CHOICE, ATTACH_PHOTO_CHOICE};

    public static final Map<String, Bitmap> bitmapStorageMap = new HashMap<>();

    public static Bitmap getWatchDataEntryImage(Context context, WatchDataEntry watchDataEntry) {
        String imagePath = watchDataEntry.getImagePath();
        // Attempt to find the display image in the cache
        Optional<Bitmap> storedBitmap = bitmapStorageMap.entrySet().stream()
                .filter(entry -> entry.getKey().equals(imagePath))
                .map(Map.Entry::getValue)
                .findAny();
        if (storedBitmap.isPresent()) {
            return storedBitmap.get();
        } else {
            // Decode bitmap from image path and return it
            Bitmap bitmapToReturn;
            Bitmap bmp = BitmapFactory.decodeFile(watchDataEntry.getImagePath());
            if (bmp != null) {
                // If the bitmap has been read, create a square image out of it
                bitmapToReturn = ImageUtil.createSquaredBitmap(bmp);
            } else {
                // Fall back to default image as the path is invalid or the image doesn't exist
                watchDataEntry.setImagePath("");
                bitmapToReturn = BitmapFactory.decodeResource(context.getResources(), ThemeUtil.getWatchPlaceholderImageID(ThemeUtil.getThemeFromPreferences(context)));
            }
            // Put the image in the cache
            bitmapStorageMap.put(watchDataEntry.getImagePath(), bitmapToReturn);
            return bitmapToReturn;
        }
    }

    public static void removeBitmapFromMap(String pathToRemove) {
        bitmapStorageMap.remove(pathToRemove);
        Log.i("ImageUtil", "Removed bitmap from storage map");
    }

    /**
     * Method overload if the image to be processed has already been written to app storage
     */
    public static void rotateAndRewriteBitmap(String filePath) {
        rotateAndRewriteBitmap(BitmapFactory.decodeFile(filePath), filePath);
    }

    /**
     * Takes the {@link Bitmap} {@param readPath} and processes it as a display photo for a
     * {@link WatchDataEntry}, and writes it out to {@param writePath}.
     */
    public static void rotateAndRewriteBitmap(Bitmap bitmapToProcess, String writePath) {
        Preconditions.checkNotNull(bitmapToProcess);
        File imageFile = new File(writePath);
        Bitmap rotatedBitmap = bitmapToProcess;
        // Determine the rotation necessary for this image depending on the orientation of the
        // photo taken
        int rotation = neededRotation(imageFile);
        // If rotation is 0, then nothing is needed
        if (rotation != 0) {
            Matrix m = new Matrix();
            m.postRotate(rotation);
            rotatedBitmap = Bitmap.createBitmap(rotatedBitmap,
                    0, 0, rotatedBitmap.getWidth(), rotatedBitmap.getHeight(),
                    m, true);
        }
        // Write image file to storage
        try (FileOutputStream fileOutputStream = new FileOutputStream(imageFile)) {
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        } catch (Exception e) {
            Log.e("ImageUtil", "Rotated bitmap could not be written to storage.");
        }
    }

    private static int neededRotation(File imageFile) {
        try {
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                return 270;
            }
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                return 180;
            }
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                return 90;
            }
            return 0;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Bitmap createSquaredBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width) ? height - (height - width) : height;
        int cropW = (width - height) / 2;
        cropW = (cropW < 0) ? 0 : cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH < 0) ? 0 : cropH;
        return Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);
    }

}
