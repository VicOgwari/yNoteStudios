package com.midland.ynote.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.midland.ynote.Activities.LecturesList;
import com.midland.ynote.Activities.StudioLecturesList;
import com.midland.ynote.R;

public class VidType extends Dialog {

    private final Activity a;
    private final LecturesList lecturesList;
    private final String school;
    public VidType(@NonNull Context context, Activity a, LecturesList lecturesList, String school) {
        super(context);
        this.a = a;
        this.lecturesList = lecturesList;
        this.school = school;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_vid_type);
        Button studioVid = findViewById(R.id.studioLecturesBtn);
        Button outSourcedVid = findViewById(R.id.outSourcedBtn);

        Intent intent = new Intent(getContext(), StudioLecturesList.class);
        intent.putExtra("school", school);
        studioVid.setOnClickListener(v -> getContext().startActivity(intent));

        outSourcedVid.setOnClickListener(v -> {
            if(lecturesList != null) {
                lecturesList.outSource(getContext());
            }
            VidType.this.dismiss();
        });
    }
}