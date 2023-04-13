package com.midland.ynote.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.midland.ynote.R;
import com.midland.ynote.Adapters.CompletedLecturesAdapter;
import com.midland.ynote.Utilities.FilingSystem;

import java.io.File;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

public class CompletedLectures extends Dialog {

    private Activity a;
    public CompletedLectures(@NonNull Context context, Activity a) {
        super(context);
        this.a = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_lectures);
        CompletedLecturesAdapter completedLectures = new CompletedLecturesAdapter(getContext(), getFileObjects());
        completedLectures.notifyDataSetChanged();

        if (getFileObjects().size() == 0){
            Toast.makeText(getContext(), "No completed lectures found.", Toast.LENGTH_SHORT).show();
        }

        GridView completedLecturesRV = findViewById(R.id.completedLectureGrid);
        completedLecturesRV.setAdapter(completedLectures);
    }

    private ArrayList<com.midland.ynote.Objects.CompletedLectures> getFileObjects() {
        Uri uri = Uri.fromFile(FilingSystem.Companion.getCompletedLectures());
        String[] projection = {MediaStore.Files.FileColumns.DATA};
        Cursor c = null;
        SortedSet<String> dirList = new TreeSet<>();
        ArrayList<com.midland.ynote.Objects.CompletedLectures> docObjects = new ArrayList<>();
        String[] directories = null;
        String orderBy = MediaStore.Images.Media.DATE_TAKEN;

        //USED AS ARGUMENTS FOR CURSOR
        String mp4 = MimeTypeMap.getSingleton().getMimeTypeFromExtension("mp4");


        //Where
        String where = MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                + " OR " + MediaStore.Files.FileColumns.MIME_TYPE
                + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE
                + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE
                + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE
                + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE
                + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE
                + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?" + " OR "
                + MediaStore.Files.FileColumns.MIME_TYPE + "=?" + " OR "
                + MediaStore.Files.FileColumns.MIME_TYPE + "=?";

        //args
        String[] args = new String[]{mp4};


        if (uri != null) {
            c = a.managedQuery(uri, projection, where, args, orderBy + " DESC");
        }

        if ((c != null) && (c.moveToFirst())) {
            do {
                String tempDir = c.getString(0);
                tempDir = tempDir.substring(0, tempDir.lastIndexOf("/"));
                try {
                    dirList.add(tempDir);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            while (c.moveToNext());
            directories = new String[dirList.size()];
            dirList.toArray(directories);
        }

        for (int i = 0; i < dirList.size(); i++) {
            File completedLectureDir = new File(directories[i]);
            File[] docList = completedLectureDir.listFiles();
            if (completedLectureDir == null)
                continue;

            for (File docPath : docList) {
                try {
                    if (docPath.isDirectory()) {
                        docList = docPath.listFiles();
                    }


                    File docFile = docPath.getAbsoluteFile();
                    int fileSize = Integer.parseInt(String.valueOf(docFile.length()/1024));
                    com.midland.ynote.Objects.CompletedLectures docObject = new com.midland.ynote.Objects.CompletedLectures(docFile.getName(), Uri.fromFile(docFile), String.valueOf(fileSize), String.valueOf(docFile.lastModified()));
                    docObjects.add(docObject);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return docObjects;
    }

}