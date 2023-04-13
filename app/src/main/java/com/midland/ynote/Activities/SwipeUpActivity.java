package com.midland.ynote.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.midland.ynote.Adapters.LectureSwipeAdt;
import com.midland.ynote.Objects.SelectedDoc;
import com.midland.ynote.R;

import java.util.ArrayList;

public class SwipeUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_up);
        ArrayList<SelectedDoc> videos = new ArrayList<>();
        ArrayList<String> vidDetails = new ArrayList<>();
        String nodeName = "";

        Intent i = getIntent();
        if (i.getSerializableExtra("videos") != null){
            videos = (ArrayList<SelectedDoc>) i.getSerializableExtra("videos");
            nodeName = i.getStringExtra("nodeName");
        }

        for (SelectedDoc doc : videos){
            vidDetails.add(doc.getDocMetaData().split("_-_")[0] + "_-_" +
                    doc.getDocMetaData().split("_-_")[7] + "_-_" +
                    doc.getDocMetaData().split("_-_")[5] + "_-_" +
                    doc.getDocMetaData().split("_-_")[6] + "_-_" +
                    doc.getDocMetaData().split("_-_")[1] + "_-_" +
                    nodeName);
        }

        LectureSwipeAdt adt = new LectureSwipeAdt(SwipeUpActivity.this, getApplication(), this, vidDetails);
        adt.notifyDataSetChanged();
        ViewPager2 swipeUpVP = findViewById(R.id.swipeUpVP);
        swipeUpVP.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        swipeUpVP.setAdapter(adt);
    }
}