package com.example.watchchecker.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Utility class for methods involving {@link Bitmap}s and operations involving them.
 */
public class BitmapUtil {

    /**
     * Operate this on an image file written by a camera intent, as they come out in landscape even
     * if the photo was taken in portrait
     */
    public static void rotateAndRewriteBitmap(String filePath) {
        Bitmap rotatedBitmap;
        File imageFile = new File(filePath);
        if (imageFile.length() > 0) {
            rotatedBitmap = BitmapFactory.decodeFile(filePath);
            if (rotatedBitmap == null) throw new IllegalArgumentException();
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
                // Rewrite image file to storage
                try (FileOutputStream fileOutputStream = new FileOutputStream(imageFile)) {
                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                } catch (Exception e) {
                    Log.e("BitmapUtil", "Rotated bitmap could not be written to storage.");
                }
            }
        }
        Log.e("BitmapUtil", "Image file could not be found");
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
