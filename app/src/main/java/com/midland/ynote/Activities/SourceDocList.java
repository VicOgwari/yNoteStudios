package com.midland.ynote.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.midland.ynote.R;
import com.midland.ynote.Adapters.SourceDocAdapter;
import com.midland.ynote.Objects.SourceDocObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class SourceDocList extends AppCompatActivity {

    public static final int REQUEST_MULTI_PERMISSION_ID = 1;
    String flag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_doc_list);
        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final GridView docRecycler = findViewById(R.id.docRecycler);
        if (getIntent().getStringExtra("flag") != null) {
            flag = getIntent().getStringExtra("flag");
        }

        if (checkAndRequestPermission()) {
            if (getIntent().getStringExtra("addDoc") != null) {
                SourceDocAdapter sourceDocAdapterUpload = new SourceDocAdapter(SourceDocList.this, getFileObjects(), "addDoc",
                        getIntent().getStringExtra("SchoolName"),
                        getIntent().getStringExtra("Department"));
                sourceDocAdapterUpload.notifyDataSetChanged();
                docRecycler.setAdapter(sourceDocAdapterUpload);
            } else if (getIntent().getStringExtra("lecStud") != null) {
                SourceDocAdapter sourceDocAdapter = new SourceDocAdapter(SourceDocList.this, getFileObjects(), "activity");
                sourceDocAdapter.notifyDataSetChanged();
                docRecycler.setAdapter(sourceDocAdapter);
            } else if (getIntent().getStringExtra("userProfile") != null) {
                SourceDocAdapter sourceDocAdapter = new SourceDocAdapter(SourceDocList.this, getFileObjects(), "home");
                sourceDocAdapter.notifyDataSetChanged();
                docRecycler.setAdapter(sourceDocAdapter);
            } else if (flag != null) {
                SourceDocAdapter sourceDocAdapter = new SourceDocAdapter(SourceDocList.this, getFileObjects(), flag);
                sourceDocAdapter.notifyDataSetChanged();
                docRecycler.setAdapter(sourceDocAdapter);
            }
        }
    }

    private boolean checkAndRequestPermission() {
        int readPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> neededPermission = new ArrayList<>();
        if (readPermission != PackageManager.PERMISSION_GRANTED) {
            neededPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            neededPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!neededPermission.isEmpty()) {
            ActivityCompat.requestPermissions(this, neededPermission.toArray(new String[neededPermission.size()]), REQUEST_MULTI_PERMISSION_ID);
            return false;
        }
        return true;
    }


    //GETTING ALL IMAGES FROM EXTERNAL STORAGE
    private ArrayList<SourceDocObject> getFileObjects() {
        Uri uri = MediaStore.Files.getContentUri("external");
        String[] projection = {MediaStore.Files.FileColumns.DATA};
        Cursor c = null;
        SortedSet<String> dirList = new TreeSet<>();
        ArrayList<SourceDocObject> sourceDocObjects = new ArrayList<>();
        String[] directories = null;
        String orderBy = MediaStore.Images.Media.DATE_TAKEN;

        //USED AS ARGUMENTS FOR CURSOR
        String pdf = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf");
        String doc = MimeTypeMap.getSingleton().getMimeTypeFromExtension("doc");
        String docx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("docx");
        String xls = MimeTypeMap.getSingleton().getMimeTypeFromExtension("xls");
        String xlsx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("xlsx");
        String ppt = MimeTypeMap.getSingleton().getMimeTypeFromExtension("ppt");
        String pptx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pptx");
        String txt = MimeTypeMap.getSingleton().getMimeTypeFromExtension("txt");
        String rtx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("rtx");
        String rtf = MimeTypeMap.getSingleton().getMimeTypeFromExtension("rtf");
        String html = MimeTypeMap.getSingleton().getMimeTypeFromExtension("html");

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
        String[] args = new String[]{pdf, doc, docx, xls, xlsx, ppt, pptx};


        if (uri != null) {
            c = getContentResolver().query(uri, projection, where, args, orderBy);
        }

        if ((c != null) && (c.moveToFirst())) {
            do {
                String tempDir = c.getString(0);
                tempDir = tempDir.substring(0, tempDir.lastIndexOf("/"));
                try {
                    dirList.add(tempDir);
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            while (c.moveToNext());
            directories = new String[dirList.size()];
            dirList.toArray(directories);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM, yyyy");
        for (int i = 0; i < dirList.size(); i++) {
            File imageDir = new File(directories[i]);
            File[] docList = imageDir.listFiles();
            if (imageDir == null)
                continue;
            if (docList != null)
                for (File docPath : docList) {
                    try {
                        if (docPath.isDirectory()) {
                            docList = docPath.listFiles();
                        }


                        File docFile = docPath.getAbsoluteFile();
                        Uri docUri = Uri.fromFile(docFile);
                        int fileSize = Integer.parseInt(String.valueOf(docFile.length() / 1024));
                        SourceDocObject sourceDocObject = new SourceDocObject(docFile.getName(), Uri.fromFile(docFile), String.valueOf(fileSize),
                                dateFormat.format(docFile.lastModified()), docUri, docFile.lastModified());
                        if (sourceDocObject.getName().endsWith("pdf") || sourceDocObject.getName().endsWith("PDF")
                                || sourceDocObject.getName().endsWith("doc") || sourceDocObject.getName().endsWith("DOC")
                                || sourceDocObject.getName().endsWith("docx") || sourceDocObject.getName().endsWith("DOCX")
                                || sourceDocObject.getName().endsWith("ppt") || sourceDocObject.getName().endsWith("PPT")
                                || sourceDocObject.getName().endsWith("pptx") || sourceDocObject.getName().endsWith("PPTX")
                                || sourceDocObject.getName().endsWith("xls") || sourceDocObject.getName().endsWith("XLS")
                                || sourceDocObject.getName().endsWith("xlsx") || sourceDocObject.getName().endsWith("XLSX")) {

                            sourceDocObjects.add(sourceDocObject);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sourceDocObjects.sort(Comparator.comparing(SourceDocObject::getDateModified));
        }
        Collections.reverse(sourceDocObjects);
        return sourceDocObjects;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.finish();
    }

    //PERMISSION CALLBACK TO HANDLE USER PERMISSION INPUT
}