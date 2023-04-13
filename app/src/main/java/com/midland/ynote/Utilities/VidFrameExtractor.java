package com.midland.ynote.Utilities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;

public class VidFrameExtractor extends Activity {
    /**
     * Called when the activity is first created.
     */
    File file;
    ImageView img;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
//
//
//        img = (ImageView) findViewById(R.id.img);
//
//        File sdcard = Environment.getExternalStorageDirectory();
//        file = new File(sdcard, "myvid.mp4");
//
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//
//        try {
//            retriever.setDataSource(file.getAbsolutePath());
//
//            img.setImageBitmap(retriever.getFrameAtTime(10000, MediaMetadataRetriever.OPTION_CLOSEST));
//
//
//        } catch (IllegalArgumentException ex) {
//            ex.printStackTrace();
//        } catch (RuntimeException ex) {
//            ex.printStackTrace();
//        } finally {
//            try {
//                retriever.release();
//            } catch (RuntimeException ex) {
//            }
//        }
//
//    }

    }
}
