package com.midland.ynote.Utilities.Paint;

import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.midland.ynote.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PaintFileListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paint_activity_file_list);
        
        initToolBar();
        initViews();
    }

    private void initViews() {
        RecyclerView recyclerView = findViewById(R.id.filesRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        PaintFileAdapter fileAdapter = new PaintFileAdapter(this, loadFile());
        recyclerView.setAdapter(fileAdapter);
    }

    private List<File> loadFile() {
        ArrayList<File> inFiles = new ArrayList<>();
        File parentDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator +
                getString(R.string.app_name));

        File[] files = parentDir.listFiles();

        for (File file : files){
            if (file.getName().endsWith(".png"))
                inFiles.add(file);
        }

        if (files.length > 0){
            TextView textView = findViewById(R.id.status_empty);
            textView.setVisibility(View.GONE);
        }
        return inFiles;
    }

    private void initToolBar() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pictures");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home){

            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}