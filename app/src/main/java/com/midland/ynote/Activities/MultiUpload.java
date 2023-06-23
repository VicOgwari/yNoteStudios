package com.midland.ynote.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.midland.ynote.Dialogs.LogInSignUp;
import com.midland.ynote.Dialogs.PublishApproval;
import com.midland.ynote.Objects.MultiFileObj;
import com.midland.ynote.Objects.Publishable;
import com.midland.ynote.R;
import com.midland.ynote.Utilities.CustomScrollView;
import com.midland.ynote.Utilities.FilingSystem;

import java.util.ArrayList;
import java.util.Random;

public class MultiUpload extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private PDFView selectionsPrev;
    private String schName, department, knowledgeBase = "", pastPaper, semester = "-select a field-", uid, displayName;
    private String unitCode, docDetail, docName, docUri, docSize;
    private ArrayList<String> objs;
    private TextView indexTv, docSizeTv, docNameTv;
    private ImageView docIm;
    private ArrayList<MultiFileObj> objs1;
    private ArrayList<String> completed;
    private final ArrayList<Publishable> publishables = new ArrayList<>();
    private CustomScrollView sv;
    private CheckBox yes, no, yr1, yr2, yr3, yr4, yr5, masters, phD, cert, dip, higherDip, bridging;
    private Spinner semSpinner;
    private EditText unitCodeEditText, docDetails;
    private Button next, prev, selectColor;
    private int index, endColor = 0;
    private ArrayAdapter<CharSequence> semAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_upload);

        selectionsPrev = findViewById(R.id.selectionsPrev);
        sv = findViewById(R.id.sv);
        no = findViewById(R.id.checkboxNo);
        yes = findViewById(R.id.checkboxYes);
        yr1 = findViewById(R.id.yr1);
        yr2 = findViewById(R.id.yr2);
        yr3 = findViewById(R.id.yr3);
        yr4 = findViewById(R.id.yr4);
        yr5 = findViewById(R.id.yr5);
        cert = findViewById(R.id.cert);
        next = findViewById(R.id.next);
        selectColor = findViewById(R.id.selectColor);
        prev = findViewById(R.id.prev);
        dip = findViewById(R.id.dip);
        masters = findViewById(R.id.masters);
        phD = findViewById(R.id.phd);
        higherDip = findViewById(R.id.higherDip);
        bridging = findViewById(R.id.bridging);
        semSpinner = findViewById(R.id.semSpinner);
        unitCodeEditText = findViewById(R.id.unitCodeEditText);
        docDetails = findViewById(R.id.docDetails);
        indexTv = findViewById(R.id.indexTv);
        docNameTv = findViewById(R.id.docNameTv);
        docSizeTv = findViewById(R.id.docSizeTv);
        docIm = findViewById(R.id.docIm);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            LogInSignUp logInSignUp = new LogInSignUp(getApplicationContext());
            logInSignUp.show();
        }else {
           uid = user.getUid();
           displayName = user.getDisplayName();
        }

        no.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if (buttonView.isChecked()){
                yes.setChecked(false);
                pastPaper = "False";
            }
        }));

        yes.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if (buttonView.isChecked()){
                no.setChecked(false);
                pastPaper = "True";
            }
        }));

        yr1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            knowledgeBase = "";
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
            knowledgeBase = "";
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
            knowledgeBase = "";
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
            knowledgeBase = "";
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
            knowledgeBase = "";
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
            knowledgeBase = "";
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
            knowledgeBase = "";
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
            knowledgeBase = "";
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
            knowledgeBase = "";
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
            knowledgeBase = "";
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
            knowledgeBase = "";
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
        semAdapter = ArrayAdapter.createFromResource(this, R.array.semesters1, R.layout.spinner_drop_down_yangu1);
        semAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        semSpinner.setAdapter(semAdapter);


        objs = new ArrayList<>();
        objs1 = new ArrayList<>();
        completed = new ArrayList<>();

        Intent intent = getIntent();
        if (intent.getStringExtra("SchoolName") != null){
            schName = intent.getStringExtra("SchoolName");
            department = intent.getStringExtra("Department");
            FilingSystem.Companion.setMainField(schName);
            FilingSystem.Companion.setSubField(department);
            objs = intent.getStringArrayListExtra("selections");
//            schTV.setText(String.format("School: %s\n Department: %s", schName, department));
        }
        for (String s : objs){
            completed.add("Incomplete");
            objs1.add(new MultiFileObj(Integer.parseInt(s.split("_-_")[0]), s.split("_-_")[1], s.split("_-_")[2]));
        }
        index = 1;
        indexTv.setText(index + " of " + objs1.size());
        docSizeTv.setText(objs1.get(0).getFileSize() + " kBz");
        docNameTv.setText(objs1.get(0).getFileName());
        selectionsPrev.fromUri(Uri.parse(objs1.get(0).getFileUri()))
                .password(null)// IF PASSWORD PROTECTED
//                    .defaultPage(savedPage) // THIS VALUE CAN BE STORED TO BE OPEN FROM LAST TIME
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true).load();
        setIm(docNameTv, getApplicationContext());


        next.setOnClickListener(v -> {
            unitCode = unitCodeEditText.getText().toString().trim();
            docDetail = docDetails.getText().toString().trim();
            docName = objs1.get(index - 1).getFileName();
            docUri = objs1.get(index - 1).getFileUri();
            docSize = String.valueOf(objs1.get(index - 1).getFileSize());

            if (next.getText().equals("Finish")){
                if (pastPaper == null || pastPaper.equals("")){
                    Toast.makeText(getApplicationContext(), "Is this document a past paper?.", Toast.LENGTH_SHORT).show();
                }else
                if (semester == null || semester.equals("-select a field-")){
                    Toast.makeText(getApplicationContext(), "-select a field-", Toast.LENGTH_SHORT).show();
                }else
                if (endColor == 0){
                    Toast.makeText(getApplicationContext(), "Click on me", Toast.LENGTH_SHORT).show();
                }else
                if (unitCode.equals("") && docDetail.equals("")){
                    Toast.makeText(getApplicationContext(), "Fill in at least one slot.", Toast.LENGTH_SHORT).show();
                }else
                if (knowledgeBase.equals("")){
                    Toast.makeText(getApplicationContext(), "Knowledge base..", Toast.LENGTH_SHORT).show();
                }else {
                    FilingSystem.Companion.setAllTags(new ArrayList<>());
                    FilingSystem.Companion.setUnitCode(unitCode);
                    FilingSystem.Companion.setKnowledgeBase(knowledgeBase);
                    FilingSystem.Companion.setDocDetail(docDetail);
                    if (!unitCode.trim().equals("")) {
                        FilingSystem.Companion.getAllTags().add(unitCode);
                    }
                    if (!docDetail.trim().equals("")) {
                        FilingSystem.Companion.getAllTags().add(docDetail);
                    }
                    if (pastPaper.equals("True")){
                        FilingSystem.Companion.getAllTags().add("Past papers");
                    }
                    String docMetaData = FilingSystem.Companion.getMainField() + "_-_"
                            + FilingSystem.Companion.getSubField() + "_-_" + FilingSystem.Companion.getKnowledgeBase()
                            + "_-_" + FilingSystem.Companion.getDocDetail()
                            + "_-_" + FilingSystem.Companion.getUnitCode()
                            + "_-_" + docName + "_-_"
                            + FilingSystem.Companion.getAllTags() + "_-_"
                            + docSize + "_-_" + uid + "_-_" + displayName + "_-_" + endColor;

                    Publishable publishable = new Publishable(docMetaData,
                            FilingSystem.Companion.getInstitution(),
                            FilingSystem.Companion.getSemester(), docUri, null,
                            FilingSystem.Companion.getUnitCode(),
                            FilingSystem.Companion.getDocDetail(), FilingSystem.Companion.getAllTags());

                    if (!publishables.contains(publishable)) {
                        publishables.add(publishable);
                    }
                    PublishApproval publishApproval1 = new PublishApproval(MultiUpload.this,
                            publishables);
                    publishApproval1.show();
                }
            }else
            {
                if (pastPaper == null || pastPaper.equals("")){
                    Toast.makeText(getApplicationContext(), "Is this document a past paper?.", Toast.LENGTH_SHORT).show();
                }else
                if (semester == null || semester.equals("-select a field-")){
                    Toast.makeText(getApplicationContext(), "-select a field-", Toast.LENGTH_SHORT).show();
                }else
                if (endColor == 0){
                    Toast.makeText(getApplicationContext(), "Click on me", Toast.LENGTH_SHORT).show();
                }else
                if (unitCode.equals("") && docDetail.equals("")){
                    Toast.makeText(getApplicationContext(), "Fill in at least one slot.", Toast.LENGTH_SHORT).show();
                }else
                if (knowledgeBase.equals("")){
                    Toast.makeText(getApplicationContext(), "Knowledge base..", Toast.LENGTH_SHORT).show();
                } else
                {
                    FilingSystem.Companion.setAllTags(new ArrayList<>());
                    FilingSystem.Companion.setUnitCode(unitCode);
                    FilingSystem.Companion.setKnowledgeBase(knowledgeBase);
                    FilingSystem.Companion.setDocDetail(docDetail);
                    if (!unitCode.trim().equals("")) {
                        FilingSystem.Companion.getAllTags().add(unitCode);
                    }
                    if (!docDetail.trim().equals("")) {
                        FilingSystem.Companion.getAllTags().add(docDetail);
                    }
                    if (pastPaper.equals("True")){
                        FilingSystem.Companion.getAllTags().add("Past papers");
                    }
                    String docMetaData = FilingSystem.Companion.getMainField() + "_-_"
                            + FilingSystem.Companion.getSubField() + "_-_" +
                            FilingSystem.Companion.getKnowledgeBase()
                            + "_-_" + FilingSystem.Companion.getDocDetail()
                            + "_-_" + FilingSystem.Companion.getUnitCode()
                            + "_-_" + docName + "_-_"
                            + FilingSystem.Companion.getAllTags() + "_-_" + docSize + "_-_" + uid + "_-_" + displayName + "_-_" + endColor;


                    Publishable publishable = new Publishable(docMetaData,
                            FilingSystem.Companion.getInstitution(),
                            FilingSystem.Companion.getSemester(), docUri, null,
                            FilingSystem.Companion.getUnitCode(),
                            FilingSystem.Companion.getDocDetail(), FilingSystem.Companion.getAllTags());

                    if (!publishables.contains(publishable)) {
                        publishables.add(publishable);
                    }

                    index = index + 1;
                    if (index == objs1.size()){
                        next.setText("Finish");
                    }
                    indexTv.setText(index + " of " + objs1.size());
                    unitCodeEditText.setText("");
                    docDetails.setText("");
                    knowledgeBase = "";
                    pastPaper = "";
                    semester = "-select a field-";
                    endColor = 0;
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
                    higherDip.setChecked(false);
                    yes.setChecked(false);
                    no.setChecked(false);
                    sv.smoothScrollTo(0, 0);
                    semAdapter = ArrayAdapter.createFromResource(this, R.array.semesters1, R.layout.spinner_drop_down_yangu1);
                    semAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    semSpinner.setAdapter(semAdapter);
                    selectionsPrev.fromUri(Uri.parse(objs1.get(index - 1).getFileUri()))
                            .password(null)// IF PASSWORD PROTECTED
//                    .defaultPage(savedPage) // THIS VALUE CAN BE STORED TO BE OPEN FROM LAST TIME
                            .enableSwipe(true)
                            .swipeHorizontal(false)
                            .enableDoubletap(true).load();
                    docSizeTv.setText(objs1.get(index - 1).getFileSize() + " kBz");
                    docNameTv.setText(objs1.get(index - 1).getFileName());
                    setIm(docNameTv, getApplicationContext());
                }
            }
        });

        prev.setOnClickListener(v -> {
            index = index - 1;

            if (index > 0) {
                next.setText("Next");
                indexTv.setText(index + " of " + objs1.size());
                unitCodeEditText.setText(publishables.get(index-1).getUnitCode());
                docDetails.setText(publishables.get(index-1).getDocDetails());
                knowledgeBase = publishables.get(index-1).getDocMetaData().split("_-_")[2];
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
                higherDip.setChecked(false);
                yes.setChecked(false);
                no.setChecked(false);
                semAdapter = ArrayAdapter.createFromResource(this, R.array.semesters1, R.layout.spinner_drop_down_yangu1);
                semAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                semSpinner.setAdapter(semAdapter);
                switch (publishables.get(index - 1).getSemester()) {
                    case "Semester 1":
                        semSpinner.setSelection(1);
                        break;
                    case "Semester 2":
                        semSpinner.setSelection(2);
                        break;
                    case "Semester 3":
                        semSpinner.setSelection(3);
                        break;
                    case "Not applicable":
                        semSpinner.setSelection(4);
                        break;
                }
                selectionsPrev.fromUri(Uri.parse(objs1.get(index - 1).getFileUri()))
                        .password(null)// IF PASSWORD PROTECTED
//                    .defaultPage(savedPage) // THIS VALUE CAN BE STORED TO BE OPEN FROM LAST TIME
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .enableDoubletap(true).load();
                docSizeTv.setText(objs1.get(index - 1).getFileSize() + " kBz");
                docNameTv.setText(objs1.get(index - 1).getFileName());
            }
            else {
                Toast.makeText(getApplicationContext(), "Next", Toast.LENGTH_SHORT).show();
            }

        });

        prev.setVisibility(View.GONE);

        selectColor.setOnClickListener(v -> {
            Random r = new Random();
            endColor = Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256));
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{Color.WHITE, endColor});
            selectColor.setBackground(gd);
        });
    }

    public void setIm(TextView docNameTv, Context c){
        if (docNameTv.getText().toString().endsWith("pdf") || docNameTv.getText().toString().endsWith("PDF")) {
            Glide.with(c).load(R.drawable.pdf).fitCenter().into(docIm);
        } else if (docNameTv.getText().toString().endsWith("doc") || docNameTv.getText().toString().endsWith("DOC")
                || docNameTv.getText().toString().endsWith("docx") || docNameTv.getText().toString().endsWith("DOCX")) {
            Glide.with(c).load(R.drawable.microsoft_word).fitCenter().into(docIm);
        } else if (docNameTv.getText().toString().endsWith("ppt") || docNameTv.getText().toString().endsWith("PPT")
                || docNameTv.getText().toString().endsWith("pptx") || docNameTv.getText().toString().endsWith("PPTX")) {
            Glide.with(c).load(R.drawable.powerpoint).fitCenter().into(docIm);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}