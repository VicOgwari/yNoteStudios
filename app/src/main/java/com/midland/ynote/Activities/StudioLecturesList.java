package com.midland.ynote.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.midland.ynote.Adapters.AllVidRecAdapter;
import com.midland.ynote.Fragments.Documents;
import com.midland.ynote.Objects.SourceDocObject;
import com.midland.ynote.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class StudioLecturesList extends AppCompatActivity {

    private RecyclerView lecturesListRV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studio_lectures_list);
        lecturesListRV = findViewById(R.id.lecturesListRV);
        String school;
        if (getIntent().getStringExtra("school") != null){
            school = getIntent().getStringExtra("school");
        }else {
            school = "";
        }
        lecturesListRV.setLayoutManager(new LinearLayoutManager(StudioLecturesList.this, RecyclerView.VERTICAL, false));
        AllVidRecAdapter allVidAdapter = new AllVidRecAdapter(StudioLecturesList.this, getVideoFileObjects(), "upload", school);
        allVidAdapter.notifyDataSetChanged();
        lecturesListRV.setAdapter(allVidAdapter);
    }

    //GETTING ALL VIDEOS FROM EXTERNAL STORAGE
    public ArrayList<SourceDocObject> getVideoFileObjects() {
        ArrayList<SourceDocObject> sourceDocObjects = new ArrayList<>();
        if (checkAndRequestPermission()){
            File[] list =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).listFiles();
            if (list != null) {
                for (File file : list) {
                    if (file.getName().endsWith("mp4")) {
                        int fileSize = Integer.parseInt(String.valueOf(file.length() / 1024));
                        if (fileSize != 0) {
                            String newName = file.getName().replace("dubLecture", "mp4");
                            SourceDocObject sourceDocObject = new SourceDocObject(newName, Uri.fromFile(file), String.valueOf(fileSize), String.valueOf(file.lastModified()), Uri.fromFile(file), file.lastModified());
                            sourceDocObjects.add(sourceDocObject);
                        }
                    }
                }
            }
        }

        return sourceDocObjects;
    }

    private boolean checkAndRequestPermission() {
        int readPermission = ContextCompat.checkSelfPermission(StudioLecturesList.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePermission = ContextCompat.checkSelfPermission(StudioLecturesList.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        String[] neededPermission = new String[2];
        if (readPermission != PackageManager.PERMISSION_GRANTED){
            neededPermission[0] = Manifest.permission.READ_EXTERNAL_STORAGE;
        }
        if (writePermission != PackageManager.PERMISSION_GRANTED){
            neededPermission[1] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        }

        if (Objects.equals(neededPermission[0], Manifest.permission.READ_EXTERNAL_STORAGE) ||
                Objects.equals(neededPermission[1], Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            ActivityCompat.requestPermissions(getParent(),
                    neededPermission,
                    Documents.REQUEST_MULTI_PERMISSION_ID);
            return false;
        }
        return true;
    }
}