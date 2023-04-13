package com.midland.ynote.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.midland.ynote.Activities.LecturesList;
import com.midland.ynote.Objects.Schools;
import com.midland.ynote.R;

import java.util.ArrayList;

public class LectureFilterGrid extends BaseAdapter {

    Context c;
    ArrayList<Schools> lectureFilterObj;
    String flag;

    public LectureFilterGrid(Context c, ArrayList<Schools> lectureFilterObj, String flag) {
        this.c = c;
        this.lectureFilterObj = lectureFilterObj;
        this.flag = flag;
    }

    @Override
    public int getCount() {
        return lectureFilterObj.size();
    }

    @Override
    public Object getItem(int position) {
        return lectureFilterObj.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView != null){
            convertView = LayoutInflater.from(c).inflate(R.layout.lecture_filter_grid_item, parent, false);
            ImageView lecFilterImage = convertView.findViewById(R.id.schoolEmblem);
            TextView lecFilterTitle = convertView.findViewById(R.id.schoolName);
            Button filterCard = convertView.findViewById(R.id.filterCard);

            Glide.with(c).load(lectureFilterObj.get(position).getSchoolEmblem()).thumbnail((float) 0.9)
                    .placeholder(R.drawable.ic_launcher_background).into(lecFilterImage);
            lecFilterTitle.setText(lectureFilterObj.get(position).getSchoolName());

            filterCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(c, LecturesList.class);
                    intent.putExtra("child", lectureFilterObj.get(position).getSchoolName());
                    c.startActivity(intent);
                }
            });
        }

        return convertView;
    }
}
