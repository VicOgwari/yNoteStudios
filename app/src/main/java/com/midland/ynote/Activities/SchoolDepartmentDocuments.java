package com.midland.ynote.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ValueEventListener;
import com.hitomi.cmlibrary.CircleMenu;
import com.midland.ynote.Adapters.DocDepartmentAdapter;
import com.midland.ynote.MainActivity;
import com.midland.ynote.Objects.SelectedDoc;
import com.midland.ynote.R;
import com.midland.ynote.Utilities.DocRetrieval;
import com.midland.ynote.Utilities.DocSorting;
import com.midland.ynote.Utilities.FilingSystem;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class SchoolDepartmentDocuments extends AppCompatActivity {

    DocDepartmentAdapter docDepartmentAdapter;
    private static String dfd;
    private static  int pos;
    private ValueEventListener mDBListener;
    private ArrayList<SelectedDoc> documents;
    private CoordinatorLayout thisSchoolCrd;
    private RelativeLayout generalDocRel;
    private CircleMenu circleMenuMain;
    private static final int STORAGE_ACCESS_CODE = 99;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_department);

        Toolbar toolbar = findViewById(R.id.schDepToolbar);
        setSupportActionBar(toolbar);

        circleMenuMain = findViewById(R.id.circleMenuMain);
        thisSchoolCrd = findViewById(R.id.thisSchoolCrd);
        ImageView home = findViewById(R.id.home);
        ImageView search = findViewById(R.id.search);
        ImageView add = findViewById(R.id.add);

        add.setOnClickListener(v -> {
            Intent intent2 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent2.addCategory(Intent.CATEGORY_OPENABLE);
            intent2.setType("application/*");
            startActivityForResult(intent2, STORAGE_ACCESS_CODE);
        });
        home.setOnClickListener(v -> {
            SchoolDepartmentDocuments.this.finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        });


        if (getIntent().getStringExtra("snackBar") != null){
            showSnackBar(getIntent().getStringExtra("snackBar"));
        }

        String[] departments;
        final ArrayList<String> schoolDepartments = new ArrayList<>();
        RecyclerView departmentsRV = findViewById(R.id.generalDocRV);
        ProgressBar libProgress = findViewById(R.id.libraryProgress);
        generalDocRel = findViewById(R.id.generalDocRel);


//        final ProgressBar libProgress = findViewById(R.id.libraryProgress);
        departmentsRV.setLayoutManager(new LinearLayoutManager(SchoolDepartmentDocuments.this,
                RecyclerView.VERTICAL, false));

//        if (getIntent().getStringExtra("school") != null) {
        dfd = getIntent().getStringExtra("SchoolName");
//        } else {
//
        DocRetrieval.Companion.getEnvironmental0().clear();
        DocRetrieval.Companion.getEnvironmental1().clear();
        DocRetrieval.Companion.getEnvironmental2().clear();
        DocRetrieval.Companion.getEnvironmental3().clear();

        switch (dfd) {

            case "Agriculture & Enterprise Development": {
                pos = 0;
                departments = DocSorting.getSubFields(pos);
                schoolDepartments.addAll(Arrays.asList(departments));

                docDepartmentAdapter = new DocDepartmentAdapter(SchoolDepartmentDocuments.this, null, SchoolDepartmentDocuments.this, getApplicationContext(),
                        schoolDepartments, dfd, documents, DocRetrieval.Companion.getAgrAdapters(), null, getApplication(), getParent(), libProgress, "SchoolDptDocuments");

            }
            break;
            case "Applied Human Sciences": {
                pos = 1;
                departments = DocSorting.getSubFields(pos);
                schoolDepartments.addAll(Arrays.asList(departments));
                docDepartmentAdapter = new DocDepartmentAdapter(SchoolDepartmentDocuments.this, null, SchoolDepartmentDocuments.this, getApplicationContext(), schoolDepartments, dfd, documents,
                        DocRetrieval.Companion.getAppHumanAdapters(), null, getApplication(), getParent(), libProgress, "SchoolDptDocuments");

            }
            break;
            case "Business": {
                pos = 2;
                departments = DocSorting.getSubFields(pos);
                schoolDepartments.addAll(Arrays.asList(departments));
                docDepartmentAdapter = new DocDepartmentAdapter(SchoolDepartmentDocuments.this, null, SchoolDepartmentDocuments.this, getApplicationContext(),
                        schoolDepartments, dfd, documents, DocRetrieval.Companion.getBizAdapters(),
                        null, getApplication(), getParent(), libProgress, "SchoolDptDocuments");

            }
            break;
            case "Economics": {
                pos = 3;
                departments = DocSorting.getSubFields(pos);
                schoolDepartments.addAll(Arrays.asList(departments));

                docDepartmentAdapter = new DocDepartmentAdapter(SchoolDepartmentDocuments.this, null, SchoolDepartmentDocuments.this, getApplicationContext(),
                        schoolDepartments, dfd, documents, DocRetrieval.Companion.getEconAdapters(), null, getApplication(), getParent(), libProgress, "SchoolDptDocuments");
            }
            break;
            case "Education": {
                pos = 4;
                departments = DocSorting.getSubFields(pos);
                schoolDepartments.addAll(Arrays.asList(departments));
                docDepartmentAdapter = new DocDepartmentAdapter(SchoolDepartmentDocuments.this, null, SchoolDepartmentDocuments.this, getApplicationContext(),
                        schoolDepartments, dfd, documents, DocRetrieval.Companion.getEduAdapters(), null, getApplication(), getParent(), libProgress, "SchoolDptDocuments");
            }
            break;
            case "Engineering & Technology": {
                pos = 5;
                departments = DocSorting.getSubFields(pos);
                schoolDepartments.addAll(Arrays.asList(departments));
                docDepartmentAdapter = new DocDepartmentAdapter(SchoolDepartmentDocuments.this, null, SchoolDepartmentDocuments.this, getApplicationContext(),
                        schoolDepartments, dfd, documents, DocRetrieval.Companion.getEngAdapters(), null, getApplication(), getParent(), libProgress, "SchoolDptDocuments");

            }
            break;
            case "Environmental Studies": {
                pos = 6;
                departments = DocSorting.getSubFields(pos);
                schoolDepartments.addAll(Arrays.asList(departments));
                docDepartmentAdapter = new DocDepartmentAdapter(SchoolDepartmentDocuments.this, null, SchoolDepartmentDocuments.this, getApplicationContext(),
                        schoolDepartments, dfd, documents, DocRetrieval.Companion.getEnvAdapters(), null, getApplication(), getParent(), libProgress, "SchoolDptDocuments");
            }
            break;
            case "Hospitality & Tourism": {
                pos = 7;
                departments = DocSorting.getSubFields(pos);
                schoolDepartments.addAll(Arrays.asList(departments));
                docDepartmentAdapter = new DocDepartmentAdapter(SchoolDepartmentDocuments.this, null, SchoolDepartmentDocuments.this, getApplicationContext(), schoolDepartments, dfd, documents,
                        DocRetrieval.Companion.getHospitalityAdapters(), null, getApplication(), getParent(), libProgress, "SchoolDptDocuments");
            }
            break;
            case "Humanities & Social Sciences": {
                pos = 8;
                departments = DocSorting.getSubFields(pos);
                schoolDepartments.addAll(Arrays.asList(departments));
                docDepartmentAdapter = new DocDepartmentAdapter(SchoolDepartmentDocuments.this, null, SchoolDepartmentDocuments.this, getApplicationContext(), schoolDepartments, dfd, documents,
                        DocRetrieval.Companion.getHumanitiesAdapters(), null, getApplication(), getParent(), libProgress, "SchoolDptDocuments");
            }
            break;
            case "Law": {
                pos = 9;
                departments = DocSorting.getSubFields(pos);
                schoolDepartments.addAll(Arrays.asList(departments));
                docDepartmentAdapter = new DocDepartmentAdapter(SchoolDepartmentDocuments.this, null, SchoolDepartmentDocuments.this, getApplicationContext(),
                        schoolDepartments, dfd, documents, DocRetrieval.Companion.getLawAdapters(), null, getApplication(), getParent(), libProgress, "SchoolDptDocuments");
            }
            break;
            case "Medicine": {
                pos = 10;
                departments = DocSorting.getSubFields(pos);
                schoolDepartments.addAll(Arrays.asList(departments));
                docDepartmentAdapter = new DocDepartmentAdapter(SchoolDepartmentDocuments.this, null, SchoolDepartmentDocuments.this, getApplicationContext(),
                        schoolDepartments, dfd, documents, DocRetrieval.Companion.getMedAdapters(), null, getApplication(), getParent(), libProgress, "SchoolDptDocuments");

            }
            break;
            case "Public Health": {
                pos = 11;
                departments = DocSorting.getSubFields(pos);
                schoolDepartments.addAll(Arrays.asList(departments));
                docDepartmentAdapter = new DocDepartmentAdapter(SchoolDepartmentDocuments.this, null, SchoolDepartmentDocuments.this, getApplicationContext(),
                        schoolDepartments, dfd, documents, DocRetrieval.Companion.getPublicHealthAdapters(), null, getApplication(), getParent(), libProgress, "SchoolDptDocuments");

            }
            break;
            case "Pure & Applied Sciences": {
                pos = 12;
                departments = DocSorting.getSubFields(pos);
                schoolDepartments.addAll(Arrays.asList(departments));

                docDepartmentAdapter = new DocDepartmentAdapter(SchoolDepartmentDocuments.this, null, SchoolDepartmentDocuments.this, getApplicationContext(),
                        schoolDepartments, dfd, documents, DocRetrieval.Companion.getPureAdapters(), null, getApplication(), getParent(), libProgress, "SchoolDptDocuments");

            }
            break;
            case "Visual & Performing Art": {
                pos = 13;
                departments = DocSorting.getSubFields(pos);
                schoolDepartments.addAll(Arrays.asList(departments));

                docDepartmentAdapter = new DocDepartmentAdapter(SchoolDepartmentDocuments.this, null, SchoolDepartmentDocuments.this, getApplicationContext(),
                        schoolDepartments, dfd, documents, DocRetrieval.Companion.getVisualAdapters(), null, getApplication(), getParent(), libProgress, "SchoolDptDocuments");
            }
            break;
            case "Confucius Institute": {
                pos = 14;
                departments = DocSorting.getSubFields(pos);
                schoolDepartments.addAll(Arrays.asList(departments));

            }
            break;
            case "Peace & Security Studies": {
                pos = 15;
                departments = DocSorting.getSubFields(pos);
                schoolDepartments.addAll(Arrays.asList(departments));
            }
            break;
            case "Creative Arts, Film & Media Studies": {
                DocRetrieval.Companion.getCreativeArt0().clear();
                DocRetrieval.Companion.getCreativeArt1().clear();
                pos = 16;
                departments = DocSorting.getSubFields(pos);
                schoolDepartments.addAll(Arrays.asList(departments));

                docDepartmentAdapter = new DocDepartmentAdapter(SchoolDepartmentDocuments.this, null, SchoolDepartmentDocuments.this, getApplicationContext(), schoolDepartments,
                        dfd, documents, DocRetrieval.Companion.getCreativeArtAdapters(), null, getApplication(), getParent(), libProgress, "SchoolDptDocuments");
            }
            break;
            case "Architecture": {
                pos = 17;
                departments = DocSorting.getSubFields(pos);
                schoolDepartments.addAll(Arrays.asList(departments));
                docDepartmentAdapter = new DocDepartmentAdapter(SchoolDepartmentDocuments.this, null, SchoolDepartmentDocuments.this, getApplicationContext(),
                        schoolDepartments, dfd, documents, DocRetrieval.Companion.getArchitectureAdapters(), null, getApplication(), getParent(), libProgress, "SchoolDptDocuments");

            }
            break;

            default:
                throw new IllegalStateException("Unexpected value: " + getIntent().getStringExtra("SchoolName"));
        }
        search.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SearchResults.class);
            intent.putExtra("school", dfd);
            intent.putExtra("pos", pos);
            intent.putExtra("flag", "Doc search");
            startActivity(intent);
        });

        departmentsRV.setAdapter(docDepartmentAdapter);
        departmentsRV.getRecycledViewPool().clear();

//        AdMob.Companion.runRewardAd(getApplicationContext(), this, "");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(SchoolDepartmentDocuments.this);
        inflater.inflate(R.menu.doc_display_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.searchDocsMenu:
                Intent intent = new Intent(getApplicationContext(), SearchResults.class);
                intent.putExtra("school", dfd);
                intent.putExtra("pos", pos);
                intent.putExtra("flag", "Doc search");
                startActivity(intent);

                break;

            case R.id.addDoc:
                Intent intent2 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent2.addCategory(Intent.CATEGORY_OPENABLE);
                intent2.setType("application/*");
                startActivityForResult(intent2, STORAGE_ACCESS_CODE);
//                Intent intent1 = new Intent(SchoolDepartmentDocuments.this, SourceDocList.class);
//                intent1.putExtra("addDoc", "addDoc");
//                intent1.putExtra("SchoolName", dfd);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                    Intent intent2 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                    intent2.addCategory(Intent.CATEGORY_OPENABLE);
//                    intent2.setType("application/*");
//                    startActivityForResult(intent2, STORAGE_ACCESS_CODE);
//                }else{
//                    startActivity(intent1);
//                }

                break;

            case R.id.navigation:
                if (circleMenuMain.getVisibility() == View.INVISIBLE) {
                    circleMenuMain.setVisibility(View.VISIBLE);
                    circleMenuMain.setMainMenu(Color.BLACK, R.drawable.ic_add_box_black, R.drawable.com_facebook_close)
                            .addSubMenu(Color.WHITE, R.drawable.architecture_government)
                            .addSubMenu(Color.BLUE, R.drawable.chemistry_network)
                            .addSubMenu(Color.GREEN, R.drawable.biology_medicine_dna)
                            .addSubMenu(Color.YELLOW, R.drawable.education_class)
                            .setOnMenuSelectedListener(index -> {
                                switch (index) {
                                    case 0: {
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    }
                                    break;

                                    case 1: {
                                    }
                                    break;

                                    case 2: {
                                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                                                && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                                                && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {
                                            if (ActivityCompat.shouldShowRequestPermissionRationale(getParent(), Manifest.permission.CAMERA)
                                                    && ActivityCompat.shouldShowRequestPermissionRationale(getParent(), Manifest.permission.RECORD_AUDIO)) {
                                                Snackbar.make(findViewById(R.id.homeRel), "Permission", Snackbar.LENGTH_INDEFINITE)
                                                        .setAction("ENABLE", new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                ActivityCompat.requestPermissions(getParent(), new String[]{
                                                                        Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO
                                                                }, 7);
                                                            }
                                                        }).show();
                                            } else {
                                                ActivityCompat.requestPermissions(getParent(), new String[]{
                                                        Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO
                                                }, 7);
                                            }
                                        } else {
                                            Intent intent3 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                            intent3.addCategory(Intent.CATEGORY_OPENABLE);
                                            intent3.setType("application/*");
                                            startActivityForResult(intent3, STORAGE_ACCESS_CODE);
                                        }
                                    }
                                    break;

                                    case 3: {
                                        if (ContextCompat.checkSelfPermission(SchoolDepartmentDocuments.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                                                && ContextCompat.checkSelfPermission(SchoolDepartmentDocuments.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                                            if (ActivityCompat.shouldShowRequestPermissionRationale(SchoolDepartmentDocuments.this, Manifest.permission.CAMERA)
                                                    && ActivityCompat.shouldShowRequestPermissionRationale(SchoolDepartmentDocuments.this, Manifest.permission.RECORD_AUDIO)) {
                                                Snackbar.make(findViewById(R.id.rootLayout1), "Permission", Snackbar.LENGTH_INDEFINITE)
                                                        .setAction("ENABLE", new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                ActivityCompat.requestPermissions(SchoolDepartmentDocuments.this, new String[]{
                                                                        Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO
                                                                }, 7);
                                                            }
                                                        }).show();
                                            } else {
                                                ActivityCompat.requestPermissions(SchoolDepartmentDocuments.this, new String[]{
                                                        Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO
                                                }, 7);
                                            }
                                        } else {
                                            Intent intent3 = new Intent(SchoolDepartmentDocuments.this, PhotoDoc.class);
                                            intent3.putExtra("newDoc", "newDoc");
                                            startActivity(intent3);

                                        }
                                    }
                                    break;

                                }
                            });


                } else if (circleMenuMain.getVisibility() == View.VISIBLE) {
                    circleMenuMain.setVisibility(View.INVISIBLE);
                }

                if (circleMenuMain.isOpened()) {
                    circleMenuMain.closeMenu();
                } else {
                    circleMenuMain.openMenu();
                }
                circleMenuMain.setVisibility(View.VISIBLE);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == STORAGE_ACCESS_CODE){
            if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

                File selectFile = new File(FilingSystem.Companion.getRealPathFromURI(data.getData(), getApplicationContext()));
                Intent intent = new Intent(getApplicationContext(), DocumentMetaData.class);
                intent.putExtra("flag", "home");
                intent.putExtra("SchoolName", dfd);
                intent.putExtra("Department", FilingSystem.Companion.getDept());
                intent.putExtra("selectedDocUri", data.getData().toString());
                intent.putExtra("selectedDocName", selectFile.getName());
                int fileSize = Integer.parseInt(String.valueOf(selectFile.length() / 1024));
                intent.putExtra("selectedDocSize", String.valueOf(fileSize));

                startActivity(intent);
            }
        }
    }

    public static void setDfd(String dfd) {
        SchoolDepartmentDocuments.dfd = dfd;
    }


    public void setRelVisibility(RelativeLayout rel) {
        rel.setVisibility(View.VISIBLE);
    }

    public void showSnackBar(String s) {
        Snackbar snackbar = Snackbar.make(thisSchoolCrd, s, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}