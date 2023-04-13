package com.midland.ynote.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.midland.ynote.Adapters.GridRecyclerAdapter;
import com.midland.ynote.Objects.ImageObject;
import com.midland.ynote.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

public class CustomGallery extends AppCompatActivity {

    public String intentSelector = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_gallery);
        Intent intent = getIntent();
        intentSelector = intent.getStringExtra("IntentSelector");
        final RecyclerView gridView = findViewById(R.id.gridView);
        gridView.setLayoutManager(new GridLayoutManager(CustomGallery.this, 3));
        GridRecyclerAdapter grid = new GridRecyclerAdapter(CustomGallery.this, getFileObjects(), null,
                null, intentSelector, getIntent().getStringExtra("fileName"));
        grid.notifyDataSetChanged();
        gridView.setAdapter(grid);

    }


    //GETTING ALL IMAGES FROM EXTERNAL STORAGE
    private ArrayList<ImageObject> getFileObjects(){
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
        Cursor c = null;
        SortedSet<String> dirList = new TreeSet<>();
        ArrayList<ImageObject> imageObjects = new ArrayList<>();
        String[] directories = null;
        String orderBy = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            orderBy = MediaStore.Video.Media.DATE_TAKEN;
        }

        if (uri != null){
//            c = managedQuery(uri, projection, null, null, orderBy + " DESC");
            c = getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
        }

        if ((c != null) && (c.moveToFirst())){
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

        for (int i = 0; i < dirList.size(); i++){
            File imageDir = new File(directories[i]);
            File[] imageList = imageDir.listFiles();

            if (imageList != null){
                for (File imagePath : imageList){
                    try {
                        if (imagePath.isDirectory()){
                            imageList = imagePath.listFiles();
                        }
                        if (imagePath.getName().contains(".jpg") || imagePath.getName().contains(".JPG") ||
                                imagePath.getName().contains(".jpeg") || imagePath.getName().contains(".JPEG") ||
                                imagePath.getName().contains(".png") || imagePath.getName().contains(".PNG") ||
                                imagePath.getName().contains(".gif") || imagePath.getName().contains(".GIF") ||
                                imagePath.getName().contains(".bpm") || imagePath.getName().contains(".BPM")){

                            File imageFile = imagePath.getAbsoluteFile();
                            ImageObject imageObject = new ImageObject(imageFile.getName(), Uri.fromFile(imageFile));
                            imageObjects.add(imageObject);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "Gallery empty!", Toast.LENGTH_SHORT).show();
            }
        }

        Collections.reverse(imageObjects);
        return imageObjects;
    }

    //GETTING DATA FROM A SPECIFIC FOLDER
    private ArrayList<ImageObject> getData() {
        ArrayList<ImageObject> imageObjects = new ArrayList<>();
        File downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        ImageObject imageObject;

        if (downloadFolder.exists()){
            File[] files = downloadFolder.listFiles();
            for (int i = 0; i < files.length; i++){
                File file = files[i];
                imageObject = new ImageObject(file.getName(), Uri.fromFile(file));
                imageObjects.add(imageObject);
            }
        }else {
            Toast.makeText(this, "Not available.", Toast.LENGTH_SHORT).show();
        }

        return imageObjects;
    }

    @Override
    public void onBackPressed() {
        CustomGallery.this.finish();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.finish();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.finish();
    }

}
