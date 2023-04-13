package com.midland.ynote.Utilities;

import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PdfPrint {

    private static final String TAG = PdfPrint.class.getSimpleName();
    private final PrintAttributes printAttributes;


    public PdfPrint(PrintAttributes printAttributes) {
        this.printAttributes = printAttributes;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void print(PrintDocumentAdapter printAdapter, final File path, final String fileName) {
//        printAdapter.onLayout(null, printAttributes, null,
//                new PrintDocumentAdapter.LayoutResultCallback() {
//            @Override
//            public void onLayoutFinished(PrintDocumentInfo info, boolean changed) {
//                printAdapter.onWrite(new PageRange[]{PageRange.ALL_PAGES},
//                        getOutputFile(path, fileName),
//                        new CancellationSignal(),
//                        new PrintDocumentAdapter.WriteResultCallback() {
//
//                        });
//            }
//        }, null);
    }

    private ParcelFileDescriptor getOutputFile(File path, String fileName){
        if (!path.exists()){
            path.mkdirs();
        }
        File file = new File(path, fileName);
        try {
            file.createNewFile();
            return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
