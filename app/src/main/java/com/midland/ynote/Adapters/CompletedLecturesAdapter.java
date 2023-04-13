package com.midland.ynote.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;
import com.midland.ynote.Objects.CompletedLectures;
import com.midland.ynote.R;

import java.util.ArrayList;

public class CompletedLecturesAdapter extends BaseAdapter {

    private Context c;
    private ArrayList<CompletedLectures> completedLectures;
    CompletedLectures completedLecture;

    public CompletedLecturesAdapter(Context c, ArrayList<CompletedLectures> completedLectures) {
        this.c = c;
        this.completedLectures = completedLectures;
    }

    @Override
    public int getCount() {
        return completedLectures.size();
    }

    @Override
    public Object getItem(int position) {
        return completedLectures.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(c).inflate(R.layout.completed_lectures, parent, false);
        }

        completedLecture = (CompletedLectures) this.getItem(position);
        VideoView videoView = convertView.findViewById(R.id.completedLecturesVideoView);
        Button editLecture = convertView.findViewById(R.id.editButton);
        Button publishLecture = convertView.findViewById(R.id.publishButton);

        videoView.setVideoURI(completedLecture.getUri());
        editLecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(c.getApplicationContext(), "To do", Toast.LENGTH_SHORT).show();
            }
        });
        publishLecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c.getApplicationContext(), VideoView.class);
                intent.putExtra("videoUri", completedLecture.getUri());
                intent.putExtra("uploadSelect", 2);
            }
        });


        return null;
    }


}
