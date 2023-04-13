package com.midland.ynote.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.midland.ynote.R;
import com.midland.ynote.Adapters.CloudVideosAdapter;
import com.midland.ynote.Fragments.Documents;
import com.midland.ynote.Objects.SelectedDoc;
import com.midland.ynote.Utilities.FilingSystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class UserProfile extends AppCompatActivity {

    CloudVideosAdapter completedLectureAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

//        completedLectureAdapter = new CloudVideosAdapter(UserProfile.this, getVideoFileObjects(), getApplication(), getParent());
//        completedLectureAdapter.notifyDataSetChanged();


    }

    //GETTING ALL VIDEOS FROM EXTERNAL STORAGE
    public ArrayList<SelectedDoc> getVideoFileObjects() {
        ArrayList<SelectedDoc> sourceDocObjects = new ArrayList<>();
        if (checkAndRequestPermission()) {
            File[] list = FilingSystem.Companion.getInsidePendingLec().listFiles();
            if (list != null) {
                for (File file : list) {
                    int fileSize = Integer.parseInt(String.valueOf(file.length() / 1024));
                    if (fileSize != 0) {
                        String newName = file.getName().replace("dubLecture", "mp4");
                        SelectedDoc sourceDocObject = new SelectedDoc(newName, Uri.fromFile(file), String.valueOf(fileSize), String.valueOf(file.lastModified()), Uri.fromFile(file));
                        sourceDocObjects.add(sourceDocObject);
                    }
                }
            }
        }

        return sourceDocObjects;
    }

    private boolean checkAndRequestPermission() {
        int readPermission = ContextCompat.checkSelfPermission(UserProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePermission = ContextCompat.checkSelfPermission(UserProfile.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> neededPermission = new ArrayList<>();
        if (readPermission != PackageManager.PERMISSION_GRANTED) {
            neededPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            neededPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!neededPermission.isEmpty()) {
            ActivityCompat.requestPermissions(getParent(), neededPermission.toArray(new String[neededPermission.size()]), Documents.REQUEST_MULTI_PERMISSION_ID);
            return false;
        }
        return true;
    }

    public String[] readPublishedManifest() {
        File file = new File(FilingSystem.Companion.getUploadedLecturesManifest(), "publishedLectures.yNoteManifest|encrypt.yn");
        String line = null;
        String[] textArray = new String[0];
        if (file.exists()){
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                line = br.readLine();
            while ((line = br.readLine()) != null) {
                textArray = line.split("_-_");
            }
                br.close();
            } catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(), "File not found", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return textArray;

    }

}