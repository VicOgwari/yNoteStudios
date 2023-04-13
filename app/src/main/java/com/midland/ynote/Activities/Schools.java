package com.midland.ynote.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.midland.ynote.R;
import com.midland.ynote.Adapters.SchoolsAdapter;
import com.midland.ynote.MainActivity;

import java.util.ArrayList;
import java.util.Collections;

public class Schools extends AppCompatActivity {

    private SearchView searchSchool;
    private ArrayList<com.midland.ynote.Objects.Schools> schools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schools);
        Toolbar toolbar = findViewById(R.id.schToolbar);
        setSupportActionBar(toolbar);

        schools = new ArrayList<>();
        searchSchool = findViewById(R.id.searchSchools);

        SharedPreferences sp = getSharedPreferences("schools", Context.MODE_PRIVATE);
//        if (!sp.contains("schools")) {
//            PhotoDoc.Companion.saveSchools(getApplicationContext(), "schools", setUpSchools());
//        }
//        schools = PhotoDoc.Companion.loadSchools(getApplicationContext(), "schools");
        String userSch = getIntent().getStringExtra("userSch");
        schools = setUpSchools(userSch);


        final SchoolsAdapter schoolsAdapter = new SchoolsAdapter(Schools.this, schools, null);
        schoolsAdapter.notifyDataSetChanged();
        RecyclerView schoolsRV = findViewById(R.id.schoolsRV);
        schoolsRV.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        schoolsRV.setAdapter(schoolsAdapter);



        searchSchool.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                schoolsAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.schools_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.searchSchools:
                if (searchSchool.getVisibility() == View.GONE){
                    searchSchool.setVisibility(View.VISIBLE);
                }else if (searchSchool.getVisibility() == View.VISIBLE){
                    searchSchool.setVisibility(View.GONE);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Schools.this, MainActivity.class));
    }

    public ArrayList<com.midland.ynote.Objects.Schools> setUpSchools(String userSchool){
        ArrayList<com.midland.ynote.Objects.Schools> schools = new ArrayList<>();
        com.midland.ynote.Objects.Schools school15 = new com.midland.ynote.Objects.Schools("Confucius Institute", R.drawable.philosophy_socrates, null, null);
        schools.add(school15);
        com.midland.ynote.Objects.Schools school1 = new com.midland.ynote.Objects.Schools("Agriculture & Enterprise Development", R.drawable.agriculture_nature_vegetable, null, null );
        schools.add(school1);
        com.midland.ynote.Objects.Schools school2 = new com.midland.ynote.Objects.Schools("Applied Human Sciences", R.drawable.health, null, null);
        schools.add(school2);
        com.midland.ynote.Objects.Schools school3 = new com.midland.ynote.Objects.Schools("Business", R.drawable.business_economics_finance, null, null);
        schools.add(school3);
        com.midland.ynote.Objects.Schools school4 = new com.midland.ynote.Objects.Schools("Economics", R.drawable.business_economics_currency, null, null);
        schools.add(school4);
        com.midland.ynote.Objects.Schools school5 = new com.midland.ynote.Objects.Schools("Education", R.drawable.education_class, null, null);
        schools.add(school5);
        com.midland.ynote.Objects.Schools school6 = new com.midland.ynote.Objects.Schools("Engineering & Technology", R.drawable.engineering_electrical_energy_innovation, null, null);
        schools.add(school6);
        com.midland.ynote.Objects.Schools school7 = new com.midland.ynote.Objects.Schools("Environmental Studies", R.drawable.energy_plant, null, null);
        schools.add(school7);
        com.midland.ynote.Objects.Schools school8 = new com.midland.ynote.Objects.Schools("Hospitality & Tourism", R.drawable.hospitality_claw, null, null);
        schools.add(school8);
        com.midland.ynote.Objects.Schools school9 = new com.midland.ynote.Objects.Schools("Humanities & Social Sciences", R.drawable.humanities, null, null);
        schools.add(school9);
        com.midland.ynote.Objects.Schools school10 = new com.midland.ynote.Objects.Schools("Law", R.drawable.law_book, null, null);
        schools.add(school10);
        com.midland.ynote.Objects.Schools school11 = new com.midland.ynote.Objects.Schools("Medicine", R.drawable.medicine_health_hygieia1, null, null);
        schools.add(school11);
        com.midland.ynote.Objects.Schools school12 = new com.midland.ynote.Objects.Schools("Public Health", R.drawable.coronavirus, null, null);
        schools.add(school12);
        com.midland.ynote.Objects.Schools school13 = new com.midland.ynote.Objects.Schools("Pure & Applied Sciences", R.drawable.chemistry_network, null, null);
        schools.add(school13);
        com.midland.ynote.Objects.Schools school14 = new com.midland.ynote.Objects.Schools("Visual & Performing Art", R.drawable.theatre_shakespeare, null, null);
        schools.add(school14);
        com.midland.ynote.Objects.Schools school16 = new com.midland.ynote.Objects.Schools("Peace & Security Studies", R.drawable.education_class, null, null);
        schools.add(school16);
        com.midland.ynote.Objects.Schools school17 = new com.midland.ynote.Objects.Schools("Creative Arts, Film & Media Studies", R.drawable.theatre_movie_camera, null, null);
        schools.add(school17);
        com.midland.ynote.Objects.Schools school18 = new com.midland.ynote.Objects.Schools("Architecture", R.drawable.architecture_sketch, null, null);
        schools.add(school18);

        if (userSchool != null){

            for (com.midland.ynote.Objects.Schools s : schools){
                if (s.getSchoolName().equals(userSchool)){
                    Collections.swap(schools, schools.indexOf(s), 3);
                }
            }

        }

        return schools;
    }
}