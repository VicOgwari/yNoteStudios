package com.midland.ynote.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.midland.ynote.Activities.LectureStudio;
import com.midland.ynote.Activities.LectureStudio2;
import com.midland.ynote.Activities.LecturesList;
import com.midland.ynote.Activities.PhotoDoc;
import com.midland.ynote.Activities.UserProfile2;
import com.midland.ynote.Objects.ImageObject;
import com.midland.ynote.Objects.PendingLecObj;
import com.midland.ynote.Objects.Schools;
import com.midland.ynote.R;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

    Context c;
    ArrayList<ImageObject> imageObjects;
    ArrayList<PendingLecObj> pendingLecObj;
    ArrayList<Schools> lectureFilterObj;
    String flag;
    String fileName;

    public GridAdapter(Context c, ArrayList<ImageObject> imageObjects, ArrayList<PendingLecObj> pendingLecObj, ArrayList<Schools> lectureFilterObj, String flag, String fileName) {
        this.c = c;
        this.imageObjects = imageObjects;
        this.pendingLecObj = pendingLecObj;
        this.lectureFilterObj = lectureFilterObj;
        this.flag = flag;
        this.fileName = fileName;
    }

    @Override
    public int getCount() {
        if (flag.equals("ProfileItemsList")) {
            return pendingLecObj.size();
        }else  if (flag.equals("LectureFilter")){
            return lectureFilterObj.size();
        }else {
            return imageObjects.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (flag.equals("ProfileItemsList")) {
            return pendingLecObj.get(position);
        }else  if (flag.equals("LectureFilter")){
            return lectureFilterObj.get(position);
        }else {
            return imageObjects.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            if (flag.equals("ProfileItemsList")) {
                convertView = LayoutInflater.from(c).inflate(R.layout.pending_lec_object, parent, false);
                final PendingLecObj pendingLecObj = (PendingLecObj) this.getItem(position);
                ImageView pendingLecImage = convertView.findViewById(R.id.pendingLecImage);
                TextView pendingLecTitle = convertView.findViewById(R.id.pendingLecTitle);

                if (Uri.parse(pendingLecObj.getIllustrationLocale()) != null){
                    Glide.with(c).load(Uri.parse(pendingLecObj.getIllustrationLocale())).thumbnail((float) 0.9)
                            .placeholder(R.drawable.ic_launcher_background).into(pendingLecImage);
                }else {
                    Glide.with(c).load(R.drawable.ic_launcher_background).thumbnail((float) 0.9).into(pendingLecImage);
                }
                pendingLecTitle.setText(pendingLecObj.getPendingLecTitle());

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent;
                        intent = new Intent(c.getApplicationContext(), LectureStudio2.class);
                        intent.putExtra("savedLecture", pendingLecObj);
                        intent.putExtra("studio", 2);
                        c.startActivity(intent);
                    }
                });
            }
            else if(flag.equals("LectureFilter")){
                convertView = LayoutInflater.from(c).inflate(R.layout.lecture_filter_grid_item, parent, false);
                final Schools school = (Schools) this.getItem(position);
                ImageView lecFilterImage = convertView.findViewById(R.id.schoolEmblem);
                TextView lecFilterTitle = convertView.findViewById(R.id.schoolName);
                Button filterCard = convertView.findViewById(R.id.filterCard);

                Glide.with(c).load(lectureFilterObj.get(position).getSchoolEmblem()).thumbnail((float) 0.9)
                        .placeholder(R.drawable.ic_launcher_background).into(lecFilterImage);
                lecFilterTitle.setText(lectureFilterObj.get(position).getSchoolName());

                filterCard.setOnClickListener(v -> {
                    Intent intent = new Intent(c, LecturesList.class);
                    intent.putExtra("child", lectureFilterObj.get(position).getSchoolName());
                    c.startActivity(intent);
                });


            } else {
                convertView = LayoutInflater.from(c).inflate(R.layout.gallery_object, parent, false);
                ImageView imageView = convertView.findViewById(R.id.photo);
                CardView imageCard = convertView.findViewById(R.id.imageCard);

                imageCard.setVisibility(View.GONE);
                final ImageObject imageObject = (ImageObject) this.getItem(position);
                Glide.with(c).load(imageObject.getUri()).thumbnail((float) 0.9).into(imageView);

                convertView.setOnClickListener(v -> {

                    switch (flag) {
                        case "ProfileItemsList":
                            break;

                        case "NotesPopUp":
                            Intent intent1 = new Intent(c.getApplicationContext(), LectureStudio.class);
                            intent1.putExtra("SelectedPic", imageObject.getUri().toString());
                            c.startActivity(intent1);
                            break;

                        case "biggerPicture":
                            Intent intent2 = new Intent(c.getApplicationContext(), UserProfile2.class);
                            intent2.putExtra("SelectedPic", imageObject.getUri().toString());
                            c.startActivity(intent2);
                            break;

                    }


                    if (flag.equals("PhotoDocs")) {
                        Intent intent2 = new Intent(c.getApplicationContext(), PhotoDoc.class);
                        intent2.putExtra("SelectedPic", imageObject.getUri().toString());
                        intent2.putExtra("prevDesc", "desc");
                        intent2.putExtra("bitmaps", "gallery");
                        intent2.putExtra("fileName", fileName);
                        c.startActivity(intent2);
                    }
                });

            }
        }


        return convertView;
    }
}
