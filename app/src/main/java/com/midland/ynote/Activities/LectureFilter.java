package com.midland.ynote.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.midland.ynote.R;
import com.midland.ynote.Adapters.GridRecyclerAdapter;
import com.midland.ynote.Objects.Schools;
import com.midland.ynote.Utilities.FilingSystem;

import java.util.ArrayList;
import java.util.Collections;

public class LectureFilter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_filter);

        Toolbar toolbar = findViewById(R.id.lecFilterToolbar);
        setSupportActionBar(toolbar);
        RecyclerView lectureFilterGrid = findViewById(R.id.differentSchoolsGrid);

        lectureFilterGrid.setLayoutManager(new GridLayoutManager(LectureFilter.this, 2));

        String userSch = getIntent().getStringExtra("userSch");
        ArrayList<com.midland.ynote.Objects.Schools> schools = setUpSchools(userSch);

        GridRecyclerAdapter grid = new GridRecyclerAdapter(this,
                null, null, schools, "LectureFilter", "");
        grid.notifyDataSetChanged();
        lectureFilterGrid.setAdapter(grid);

//        RecyclerView lectureFilterRV = findViewById(R.id.differentSchoolsRV);
//        lectureFilterRV.setLayoutManager(new GridLayoutManager(LectureFilter.this, 2));
//        final SchoolsAdapter schoolsAdapter = new SchoolsAdapter(LectureFilter.this, FilingSystem.Companion.getSchools(), "filter");
//        schoolsAdapter.notifyDataSetChanged();
//        lectureFilterRV.setAdapter(schoolsAdapter);
    }

    public ArrayList<Schools> setUpSchools(String userSch){
        Schools school15 = new Schools("Confucius Institute", R.drawable.philosophy_socrates);
        FilingSystem.Companion.getSchools().add(school15);
        Schools school1 = new Schools("Agriculture & Enterprise Development", R.drawable.agriculture_nature_vegetable);
        FilingSystem.Companion.getSchools().add(school1);
        Schools school2 = new Schools("Applied Human Sciences", R.drawable.health);
        FilingSystem.Companion.getSchools().add(school2);
        Schools school3 = new Schools("Business", R.drawable.business_economics_finance);
        FilingSystem.Companion.getSchools().add(school3);
        Schools school4 = new Schools("Economics", R.drawable.business_economics_currency);
        FilingSystem.Companion.getSchools().add(school4);
        Schools school5 = new Schools("Education", R.drawable.education_class);
        FilingSystem.Companion.getSchools().add(school5);
        Schools school6 = new Schools("Engineering & Technology", R.drawable.engineering_electrical_energy_innovation);
        FilingSystem.Companion.getSchools().add(school6);
        Schools school7 = new Schools("Environmental Studies", R.drawable.energy_plant);
        FilingSystem.Companion.getSchools().add(school7);
        Schools school8 = new Schools("Hospitality & Tourism", R.drawable.hospitality_claw);
        FilingSystem.Companion.getSchools().add(school8);
        Schools school9 = new Schools("Humanities & Social Sciences", R.drawable.humanities);
        FilingSystem.Companion.getSchools().add(school9);
        Schools school10 = new Schools("Law", R.drawable.law_book);
        FilingSystem.Companion.getSchools().add(school10);
        Schools school11 = new Schools("Medicine", R.drawable.medicine_health_hygieia1);
        FilingSystem.Companion.getSchools().add(school11);
        Schools school12 = new Schools("Public Health", R.drawable.coronavirus);
        FilingSystem.Companion.getSchools().add(school12);
        Schools school13 = new Schools("Pure & Applied Sciences", R.drawable.chemistry_network);
        FilingSystem.Companion.getSchools().add(school13);
        Schools school14 = new Schools("Visual & Performing Art", R.drawable.theatre_shakespeare);
        FilingSystem.Companion.getSchools().add(school14);
        Schools school16 = new Schools("Peace & Security Studies", R.drawable.education_class);
        FilingSystem.Companion.getSchools().add(school16);
        Schools school17 = new Schools("Creative Arts, Film & Media Studies", R.drawable.theatre_movie_camera);
        FilingSystem.Companion.getSchools().add(school17);
        Schools school18 = new Schools("Architecture", R.drawable.architecture_sketch);
        FilingSystem.Companion.getSchools().add(school18);

        if (userSch != null){

            for (Schools s : FilingSystem.Companion.getSchools()){
                if (s.getSchoolName().equals(userSch)){
                    Collections.swap(FilingSystem.Companion.getSchools(), FilingSystem.Companion.getSchools().indexOf(s), 3);
                }
            }

        }
        return FilingSystem.Companion.getSchools();
    }
}