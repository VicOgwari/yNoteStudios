package com.midland.ynote.Utilities.Paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class PaintSaveViewUtil {
    public static File rootDir = new File(Environment.getExternalStorageDirectory() + File.separator + "illustrations/");
    private static String illustrationName;
    private static Uri uri;

    public static String getIllustrationName() {
        return illustrationName;
    }

    public static Uri getUri() {
        return uri;
    }

    /**
     * Save picture to file
     */
    public static boolean saveScreen(Bitmap b, Context c) {
        //determine if SDCARD is available
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return false;
        }
        rootDir = c.getExternalFilesDir("illustrations");
        if (!rootDir.exists()) {
            rootDir.mkdir();
        }

        try {
            illustrationName = String.valueOf(System.currentTimeMillis());
            File illustration = new File(rootDir, illustrationName + ".jpg");
            b.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(illustration));
            uri = Uri.fromFile(illustration);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } finally {
            b = null;
        }
    }
}
