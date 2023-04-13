package com.midland.ynote.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.midland.ynote.Adapters.DocDetailAdt;
import com.midland.ynote.Adapters.TagsAdapter;
import com.midland.ynote.R;
import com.midland.ynote.Utilities.CustomScrollView;
import com.midland.ynote.Utilities.CustomViewPager;
import com.midland.ynote.Utilities.DocSorting;
import com.midland.ynote.Utilities.FilingSystem;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Arrays;

public class DocumentMetaData extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText docDetailsEditText, unitCodeEditText, institutionSpinnerET;
    private TextView mainFieldSpinner, subFiledSpinner, docTitle, noPreview;
    private Spinner mainFieldSpinnerTV, subFiledSpinnerTV, institutionSpinner;
    private ImageButton scrollMode;
    private CustomViewPager metaDataVP;
    private DocDetailAdt metaDataAdt;
    private CheckBox yr1, yr2, yr3, yr4, yr5, masters, phD, cert, dip, higherDip, bridging;
    private String docUri, coverPhotoUri, schoolName, dept, mainField, subField, flag;
    private String selectedDocName, docSize, docMetaData,
            selectedCover, knowledgeBase, institution, institution1, unitCode, docDetail;
    ProgressBar uploadProgress;
    TagsAdapter tagsAdapter;
    CustomScrollView docMetaDateSV;
    RelativeLayout metaDataRel;
    Spinner semSpinner;
    String semester;
    Button publishDoc;
    TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_meta_data);
        initViewSnActions();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initViewSnActions() {
        metaDataRel = findViewById(R.id.metaDataRel);
        metaDataVP = findViewById(R.id.metaDataVP);
        tabs = findViewById(R.id.tabs);
        yr1 = findViewById(R.id.yr1);
        yr2 = findViewById(R.id.yr2);
        yr3 = findViewById(R.id.yr3);
        yr4 = findViewById(R.id.yr4);
        yr5 = findViewById(R.id.yr5);
        cert = findViewById(R.id.cert);
        dip = findViewById(R.id.dip);
        masters = findViewById(R.id.masters);
        phD = findViewById(R.id.phd);
        higherDip = findViewById(R.id.higherDip);
        bridging = findViewById(R.id.bridging);

        semSpinner = findViewById(R.id.semSpinner);
        semSpinner.setOnItemSelectedListener(this);

        semSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                semester = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final ArrayAdapter<CharSequence> semAdapter = ArrayAdapter.createFromResource(this,
                R.array.semesters, R.layout.spinner_drop_down_yangu1);
        final ArrayAdapter<CharSequence> institutionsAdapter = ArrayAdapter.createFromResource(this,
                R.array.institutions, R.layout.spinner_drop_down_yangu1);
        ArrayAdapter < CharSequence > mainFieldAdapter = ArrayAdapter.createFromResource(DocumentMetaData.this,
                R.array.main_fields, R.layout.spinner_drop_down_yangu1);



        semAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mainFieldAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        semSpinner.setAdapter(semAdapter);

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                metaDataVP.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        metaDataVP.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                tabs.getTabAt(position).select();
                super.onPageSelected(position);
            }
        });
        metaDataVP.setPagingEnabled(false);

        yr1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            yr2.setChecked(false);
            yr3.setChecked(false);
            yr4.setChecked(false);
            yr5.setChecked(false);
            masters.setChecked(false);
            phD.setChecked(false);
            cert.setChecked(false);
            dip.setChecked(false);
            higherDip.setChecked(false);
            bridging.setChecked(false);
            knowledgeBase = "Yr 1";
        });

        yr2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            yr1.setChecked(false);
            yr3.setChecked(false);
            yr4.setChecked(false);
            yr5.setChecked(false);
            masters.setChecked(false);
            phD.setChecked(false);
            cert.setChecked(false);
            dip.setChecked(false);
            higherDip.setChecked(false);
            bridging.setChecked(false);
            knowledgeBase = "Yr 2";
        });

        yr3.setOnCheckedChangeListener((buttonView, isChecked) -> {
            yr2.setChecked(false);
            yr1.setChecked(false);
            yr4.setChecked(false);
            yr5.setChecked(false);
            masters.setChecked(false);
            phD.setChecked(false);
            cert.setChecked(false);
            dip.setChecked(false);
            higherDip.setChecked(false);
            bridging.setChecked(false);
            knowledgeBase = "Yr 3";
        });

        yr4.setOnCheckedChangeListener((buttonView, isChecked) -> {
            yr2.setChecked(false);
            yr3.setChecked(false);
            yr1.setChecked(false);
            yr5.setChecked(false);
            masters.setChecked(false);
            phD.setChecked(false);
            cert.setChecked(false);
            dip.setChecked(false);
            higherDip.setChecked(false);
            bridging.setChecked(false);
            knowledgeBase = "Yr 4";
        });

        yr5.setOnCheckedChangeListener((buttonView, isChecked) -> {
            yr2.setChecked(false);
            yr3.setChecked(false);
            yr4.setChecked(false);
            yr1.setChecked(false);
            masters.setChecked(false);
            phD.setChecked(false);
            cert.setChecked(false);
            dip.setChecked(false);
            higherDip.setChecked(false);
            bridging.setChecked(false);
            knowledgeBase = "Yr 5";
        });

        cert.setOnCheckedChangeListener((buttonView, isChecked) -> {
            yr2.setChecked(false);
            yr3.setChecked(false);
            yr4.setChecked(false);
            yr1.setChecked(false);
            masters.setChecked(false);
            phD.setChecked(false);
            yr5.setChecked(false);
            dip.setChecked(false);
            higherDip.setChecked(false);
            bridging.setChecked(false);
            knowledgeBase = "Certificate";
        });

        dip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            yr2.setChecked(false);
            yr3.setChecked(false);
            yr4.setChecked(false);
            yr1.setChecked(false);
            masters.setChecked(false);
            phD.setChecked(false);
            cert.setChecked(false);
            yr5.setChecked(false);
            higherDip.setChecked(false);
            bridging.setChecked(false);
            knowledgeBase = "Diploma";
        });

        masters.setOnCheckedChangeListener((buttonView, isChecked) -> {
            yr2.setChecked(false);
            yr3.setChecked(false);
            yr4.setChecked(false);
            yr1.setChecked(false);
            yr5.setChecked(false);
            phD.setChecked(false);
            cert.setChecked(false);
            dip.setChecked(false);
            higherDip.setChecked(false);
            bridging.setChecked(false);
            knowledgeBase = "Masters";
        });

        phD.setOnCheckedChangeListener((buttonView, isChecked) -> {
            phD.setChecked(true);
            yr2.setChecked(false);
            yr3.setChecked(false);
            yr4.setChecked(false);
            yr1.setChecked(false);
            yr5.setChecked(false);
            masters.setChecked(false);
            cert.setChecked(false);
            dip.setChecked(false);
            higherDip.setChecked(false);
            bridging.setChecked(false);
            knowledgeBase = "phD";
        });

        higherDip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            yr2.setChecked(false);
            yr3.setChecked(false);
            yr4.setChecked(false);
            yr1.setChecked(false);
            yr5.setChecked(false);
            masters.setChecked(false);
            phD.setChecked(false);
            cert.setChecked(false);
            dip.setChecked(false);
            bridging.setChecked(false);
            knowledgeBase = "Higher diploma";
        });

        bridging.setOnCheckedChangeListener((buttonView, isChecked) -> {
            yr2.setChecked(false);
            yr3.setChecked(false);
            yr4.setChecked(false);
            yr1.setChecked(false);
            yr5.setChecked(false);
            masters.setChecked(false);
            phD.setChecked(false);
            cert.setChecked(false);
            dip.setChecked(false);
            higherDip.setChecked(false);
            knowledgeBase = "Bridging";
        });

        institutionSpinner = findViewById(R.id.institutionSpinner);
        institutionSpinner.bringToFront();
        institutionSpinnerET = findViewById(R.id.institutionSpinnerET);
        institutionSpinner.setOnItemSelectedListener(this);


        institutionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        institutionSpinner.setAdapter(institutionsAdapter);

        institutionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                institution = (String) parent.getItemAtPosition(position);
                if (institution.equals("Type my institution")){
                    institutionSpinnerET.setVisibility(View.VISIBLE);
                }else {
                    institutionSpinnerET.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        flag = getIntent().getStringExtra("flag");
        if (flag != null){
            if (flag.equals("home")){
                mainFieldSpinnerTV = findViewById(R.id.mainFieldSpinnerTV);
                mainFieldSpinnerTV.bringToFront();
                subFiledSpinnerTV = findViewById(R.id.subFieldSpinnerTV);
                subFiledSpinnerTV.bringToFront();

                mainFieldSpinnerTV.setOnItemSelectedListener(this);
                subFiledSpinnerTV.setOnItemSelectedListener(this);

                mainFieldSpinnerTV.setVisibility(View.VISIBLE);
                subFiledSpinnerTV.setVisibility(View.VISIBLE);



                final ArrayAdapter<CharSequence> schoolsAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.main_fields, R.layout.spinner_drop_down_yangu1);

                schoolsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mainFieldSpinnerTV.setAdapter(schoolsAdapter);

                mainFieldSpinnerTV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mainField = parent.getItemAtPosition(position).toString();
                        ArrayAdapter<String> subFieldAdapter = new ArrayAdapter<>(getApplicationContext(),
                                R.layout.spinner_drop_down_yangu1,
                                new ArrayList<>(Arrays.asList(DocSorting.getSubFields(position))));
                        subFieldAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        subFiledSpinnerTV.setAdapter(subFieldAdapter);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                subFiledSpinnerTV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        subField = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }
        }

        if (getIntent().getStringExtra("selectedDocUri") != null &&
                getIntent().getStringExtra("selectedCover") == null) {
            if (getIntent().getStringExtra("SchoolName") != null){
                schoolName = getIntent().getStringExtra("SchoolName");
            }
            if (getIntent().getStringExtra("Department") != null){
                dept = getIntent().getStringExtra("Department");
            }
            docUri = getIntent().getStringExtra("selectedDocUri");
            docSize = getIntent().getStringExtra("selectedDocSize");
            selectedDocName = getIntent().getStringExtra("selectedDocName");


        } else if (getIntent().getStringExtra("selectedCover") != null) {
            coverPhotoUri = getIntent().getStringExtra("selectedCover");
        }



        docTitle = findViewById(R.id.docTitle);
        noPreview = findViewById(R.id.noPreview);
        docTitle.setText(selectedDocName);
        mainFieldSpinner = findViewById(R.id.mainFieldSpinner);

        subFiledSpinner = findViewById(R.id.subFieldSpinner);
        docDetailsEditText = findViewById(R.id.docDetails);
        unitCodeEditText = findViewById(R.id.unitCodeEditText);
        RecyclerView relevanceRV = findViewById(R.id.relevanceRV);
        publishDoc = findViewById(R.id.publishDoc);
        ImageView coverPhoto = findViewById(R.id.coverPhoto);
        uploadProgress = findViewById(R.id.uploadProgress1);
        docMetaDateSV = findViewById(R.id.docMetaDateSV);
        scrollMode = findViewById(R.id.scrollModeDoc);

        metaDataAdt = new DocDetailAdt(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, 2,
                DocumentMetaData.this, selectedDocName, docUri, docSize,
                schoolName, dept, mainField, subField, flag, knowledgeBase, semester, institution, unitCode,
                docDetail, semAdapter, institutionsAdapter, mainFieldAdapter, publishDoc, tabs);
        metaDataVP.setAdapter(metaDataAdt);

        scrollMode.setOnClickListener(v -> {
            if (docMetaDateSV.isEnableScrolling()) {
                scrollMode.setImageResource(R.drawable.ic_lock);
                docMetaDateSV.setEnableScrolling(false);
            } else {
                scrollMode.setImageResource(R.drawable.ic_lock_open);
                docMetaDateSV.setEnableScrolling(true);
            }
        });

        if (selectedDocName.endsWith(".pdf") || selectedDocName.endsWith(".PDF")){

            final PDFView docImage = findViewById(R.id.picBitmap);
            docImage.fromUri(Uri.parse(docUri))
                    .password(null)// IF PASSWORD PROTECTED
                    .defaultPage(1) // THIS VALUE CAN BE STORED TO BE OPEN FROM LAST TIME
                    .enableSwipe(true)
                    .swipeHorizontal(true)
                    .enableDoubletap(true)
                    .enableAnnotationRendering(false)
                    .enableAntialiasing(true)
                    .spacing(10)
                    .invalidPageColor(Color.WHITE)
                    .load();
        } else {
            if (selectedDocName.endsWith(".ppt") || selectedDocName.endsWith(".PPT") ||
                    selectedDocName.endsWith(".doc") || selectedDocName.endsWith(".DOC") ||
                    selectedDocName.endsWith(".pptx") || selectedDocName.endsWith(".PPTX") ||
                    selectedDocName.endsWith(".docx") || selectedDocName.endsWith(".DOCX")

            ) {
                noPreview.setVisibility(View.VISIBLE);
            }

        }


//        mainFieldAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        if (schoolName != null){
            mainFieldSpinner.setText(schoolName);
            subFiledSpinner.setText(dept);
        }

//        mainFieldSpinner.setOnItemSelectedListener(this);


        //COMMENTED OUT SINCE ON ITEM SELECTED LISTENER ALL READY SELECTS A SUB CATEGORY ARRAY LIST
//        subFiledSpinner.setAdapter(new ArrayAdapter<>(c.getApplicatiKonContext(), R.layout.support_simple_spinner_dropdown_item, DocSorting.getSubFields(0)));

//        knowledgeBaseSpinner.setOnItemSelectedListener(this);


//        mainFieldSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                if (dept != null){
////                    Toast.makeText(DocumentMetaData.this, "...", Toast.LENGTH_SHORT).show();
////                    ArrayAdapter<String> subFieldAdapter = new ArrayAdapter<>(DocumentMetaData.this, R.layout.dropdown_drop_down,
////                            new ArrayList<>(Arrays.asList(DocSorting.getSubFields(position))));
////                    int subFieldAdapterPosition = subFieldAdapter.getPosition(dept);
////                    subFiledSpinner.setAdapter(subFieldAdapter);
////                    subFiledSpinner.setSelection(subFieldAdapterPosition);
////                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

//        subFiledSpinner.setOnItemSelectedListener(this);
//        subFiledSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });




        relevanceRV.setLayoutManager(new LinearLayoutManager(DocumentMetaData.this, RecyclerView.HORIZONTAL, false));
        if (FilingSystem.Companion.getAllTags().size() > 0) {
            tagsAdapter = new TagsAdapter(DocumentMetaData.this, FilingSystem.Companion.getAllTags());
            relevanceRV.setAdapter(tagsAdapter);
            tagsAdapter.notifyDataSetChanged();
        }




        publishDoc.setOnClickListener(v -> {
//                if (institution.equals("Type my own") && institutionSpinnerET.getText().toString().trim().equals("")){
//                    institutionSpinnerET.setError("Missing institution.");
//                }else if (institution.equals("Type my own") && !institutionSpinnerET.getText().toString().trim().isEmpty()){
//                    institution1 = institutionSpinnerET.getText().toString().trim();
//                    createDocObject();
//                }
//                else {
//                    institution1 = institution;
//                    createDocObject();
//                }
//                Intent intent = new Intent(getApplicationContext(), Next.class);
//                ArrayList<String> docStrings = new ArrayList<>();
//                docStrings.add(docMetaData);
//                docStrings.add(institution1);
//                docStrings.add(docUri);
//                docStrings.add(semester);
//                intent.putStringArrayListExtra("docStrings", docStrings);
//                intent.putExtra("flag", "newDocument");
//                startActivity(intent);

            if (knowledgeBase == null){
                Toast.makeText(this, "Select a knowledge base.", Toast.LENGTH_SHORT).show();
            }else {
                tabs.getTabAt(1).select();
            }

        });


    }

    private void createDocObject() {
        //main field, sub field, know base, doc details, unit code, doc name, tags, size, uID, displayName, endColor

        if (flag != null && flag.equals("home")){
            if (subField == null) {
                if (unitCodeEditText.getText().toString().trim().equals("")){
                    docMetaData = mainField + "_-_"
                            + null + "_-_" + knowledgeBase
                            + "_-_" + docDetailsEditText.getText().toString().trim()
                            + "_-_" + null
                            + "_-_" + selectedDocName + "_-_"
                            + FilingSystem.Companion.getAllTags() + "_-_" + docSize;
                }else {
                    docMetaData = mainField + "_-_"
                            + null + "_-_" + knowledgeBase
                            + "_-_" + docDetailsEditText.getText().toString().trim()
                            + "_-_" + unitCodeEditText.getText().toString().trim()
                            + "_-_" + selectedDocName + "_-_"
                            + FilingSystem.Companion.getAllTags() + "_-_" + docSize;
                }

            } else {
                if (unitCodeEditText.getText().toString().trim().equals("")){

                    docMetaData = mainField + "_-_"
                            + subField
                            + "_-_" + knowledgeBase
                            + "_-_" + docDetailsEditText.getText().toString().trim()
                            + "_-_" + null
                            + "_-_" + selectedDocName + "_-_" +
                            FilingSystem.Companion.getAllTags() + "_-_" + docSize;
                }else {
                    docMetaData = mainField + "_-_"
                            + subField
                            + "_-_" + knowledgeBase
                            + "_-_" + docDetailsEditText.getText().toString().trim()
                            + "_-_" + unitCodeEditText.getText().toString().trim()
                            + "_-_" + selectedDocName + "_-_" +
                            FilingSystem.Companion.getAllTags() + "_-_" + docSize;
                }

            }
        }else {

            if (subFiledSpinner.getText() == null) {
                if (unitCodeEditText.getText().toString().trim().equals("")){
                    docMetaData = mainFieldSpinner.getText().toString() + "_-_"
                            + null + "_-_" + knowledgeBase
                            + "_-_" + docDetailsEditText.getText().toString().trim()
                            + "_-_" + null
                            + "_-_" + selectedDocName + "_-_"
                            + FilingSystem.Companion.getAllTags() + "_-_" + docSize;
                }else {
                    docMetaData = mainFieldSpinner.getText().toString() + "_-_"
                            + null + "_-_" + knowledgeBase
                            + "_-_" + docDetailsEditText.getText().toString().trim()
                            + "_-_" + unitCodeEditText.getText().toString().trim()
                            + "_-_" + selectedDocName + "_-_"
                            + FilingSystem.Companion.getAllTags() + "_-_" + docSize;
                }

            } else {
                if (unitCodeEditText.getText().toString().trim().equals("")){

                    docMetaData = mainFieldSpinner.getText().toString() + "_-_"
                            + subFiledSpinner.getText().toString()
                            + "_-_" + knowledgeBase
                            + "_-_" + docDetailsEditText.getText().toString().trim()
                            + "_-_" + null
                            + "_-_" + selectedDocName + "_-_" +
                            FilingSystem.Companion.getAllTags() + "_-_" + docSize;
                }else {
                    docMetaData = mainFieldSpinner.getText().toString() + "_-_"
                            + subFiledSpinner.getText().toString()
                            + "_-_" + knowledgeBase
                            + "_-_" + docDetailsEditText.getText().toString().trim()
                            + "_-_" + unitCodeEditText.getText().toString().trim()
                            + "_-_" + selectedDocName + "_-_" +
                            FilingSystem.Companion.getAllTags() + "_-_" + docSize;
                }

            }
        }
    }

    public String getKnowledgeBase() {
        return knowledgeBase;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        if (dept != null){
//            Toast.makeText(this, dept, Toast.LENGTH_SHORT).show();
//            ArrayAdapter<String> subFieldAdapter = new ArrayAdapter<>(DocumentMetaData.this, R.layout.dropdown_drop_down,
//                    new ArrayList<>(Arrays.asList(DocSorting.getSubFields(position))));
//            int subFieldAdapterPosition = subFieldAdapter.getPosition(dept);
//            subFiledSpinner.setSelection(subFieldAdapterPosition);
//            subFiledSpinner.setAdapter(subFieldAdapter);
//            subFieldAdapter.notifyDataSetChanged();
//        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                    Uri uri1 = data.getData();
                    cropRequest(uri1);
                }
                break;

            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK){
                    Uri result = CropImage.getActivityResult(data).getUri();
                    //Do som with this data
                }
                break;

        }
    }

    private void checkAndroidVersion() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 2);
            } else {
                pickImage();
            }
        } catch (Exception e) {
            pickImage();
        }
    }


    private void pickImage() {
        CropImage.startPickImageActivity(this);
    }
    private void cropRequest(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }

    public void showSnackBar(String s){
        Snackbar snackbar = Snackbar.make(metaDataRel, s, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

}