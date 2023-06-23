package com.midland.ynote.Adapters;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.midland.ynote.Activities.LecturesList;
import com.midland.ynote.Activities.SchoolDepartmentDocuments;
import com.midland.ynote.Objects.SelectedDoc;
import com.midland.ynote.Objects.SelectedVideo;
import com.midland.ynote.R;
import com.midland.ynote.Utilities.DocRetrieval;
import com.midland.ynote.Utilities.DocSorting;
import com.midland.ynote.Utilities.FilingSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class DocDepartmentAdapter extends RecyclerView.Adapter<DocDepartmentAdapter.DocDepartmentVH> implements AdapterView.OnItemSelectedListener {

    private final Context context;
    private LecturesList lecturesList;
    private SchoolDepartmentDocuments thisSchool;
    private final Context c;
    private final ArrayList<ArrayList<String>> tags;
    private final ArrayList<String> departments;
    private String[] departmentsArray;
    private ArrayList<SelectedDoc> documents, lectures;
    private final ArrayList<DocumentAdapter> documentAdapters;
    private ArrayList<SubFieldAdt> subFieldAdts1;
    final ArrayList<String> schoolDepartments = new ArrayList<>();
    private final String dfd;
    private final String flag;
    private DatabaseReference mDBRef;
    private ValueEventListener mDBListener;
    private final Activity a;
    private final Application app;
    private final ProgressBar fetchProgress;
    private PlayerView playerView;
    private ArrayList<SelectedDoc> documentsList;


    SubFieldAdt subFieldAdt0, subFieldAdt1, subFieldAdt2, subFieldAdt3, subFieldAdt4, subFieldAdt5, subFieldAdt6, subFieldAdt7,
            subFieldAdt8, subFieldAdt9, subFieldAdt10, subFieldAdt11, subFieldAdt12, subFieldAdt13, subFieldAdt14, subFieldAdt15;
    ArrayList<SelectedVideo> docs0 = new ArrayList<>();
    ArrayList<SelectedVideo> docs1 = new ArrayList<>();
    ArrayList<SelectedVideo> docs2 = new ArrayList<>();
    ArrayList<SelectedVideo> docs3 = new ArrayList<>();
    ArrayList<SelectedVideo> docs4 = new ArrayList<>();
    ArrayList<SelectedVideo> docs5 = new ArrayList<>();
    ArrayList<SelectedVideo> docs6 = new ArrayList<>();
    ArrayList<SelectedVideo> docs7 = new ArrayList<>();
    private int STORAGE_ACCESS_CODE = 99;

    public DocDepartmentAdapter(SchoolDepartmentDocuments thisSchool, LecturesList lecturesList,
                                Context c, Context context, ArrayList<String> departments, String dfd,
                                ArrayList<SelectedDoc> documents,
                                ArrayList<DocumentAdapter> documentAdapters,
                                ArrayList<ArrayList<String>> tags, Application app, Activity a, ProgressBar fetchProgress, String flag) {

        this.thisSchool = thisSchool;
        this.lecturesList = lecturesList;
        this.c = c;
        this.context = context;
        this.departments = departments;
        this.dfd = dfd;
        this.documents = documents;
        this.documentAdapters = documentAdapters;
        this.tags = tags;
        this.app = app;
        this.a = a;
        this.fetchProgress = fetchProgress;
        this.flag = flag;
    }

    @NonNull
    @Override
    public DocDepartmentVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        switch (flag) {
            case "SchoolDptDocuments":
                v = LayoutInflater.from(c).inflate(R.layout.doc_department_list_item, parent, false);
                break;

            case "LecturesList":
                v = LayoutInflater.from(c).inflate(R.layout.lec_department_list_item, parent, false);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + flag);
        }


        return new DocDepartmentVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final DocDepartmentVH holder, final int position) {

        ArrayList<String> tagsArrayList = new ArrayList<>();
        final String deptTitle = departments.get(position);
        fetchProgress.setVisibility(View.GONE);

        final DocumentReference publishedDocs = FirebaseFirestore.getInstance().collection("Content")
                .document("Documents");

        if (flag.equals("LecturesList")) {
            holder.navRight.setVisibility(View.GONE);
            holder.lecTags.setLayoutManager(new LinearLayoutManager(c, RecyclerView.HORIZONTAL, false));
            holder.lecDisplayRV.setLayoutManager(new LinearLayoutManager(c, RecyclerView.HORIZONTAL, false));
            holder.deptTitle.setText(deptTitle);

            final ArrayList<String> agrTags = new ArrayList<>();

            final DocumentReference lectureDbRef = FirebaseFirestore.getInstance().collection("Content")
                    .document("Lectures");


            switch (dfd) {
                case "Agriculture & Enterprise Development": {
                    lectureDbRef.collection(dfd)
//                            .whereEqualTo("subFiled", DocSorting.getSubFields(0)[0])
//                            .whereEqualTo("subFiled", DocSorting.getSubFields(0)[1])
//                            .whereEqualTo("subFiled", DocSorting.getSubFields(0)[2])
                            .get().addOnSuccessListener(queryDocumentSnapshots -> {

                        DocRetrieval.Companion.setAgr0(new ArrayList<>());
                        DocRetrieval.Companion.setAgr1(new ArrayList<>());
                        DocRetrieval.Companion.setAgr2(new ArrayList<>());

                        DocRetrieval.Companion.setAgrTags0(new ArrayList<>());
                        DocRetrieval.Companion.setAgrTags1(new ArrayList<>());
                        DocRetrieval.Companion.setAgrTags2(new ArrayList<>());

                        lectures = new ArrayList<>();
                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                            SelectedDoc vid = qds.toObject(SelectedDoc.class);
                            lectures.add(vid);
                        }
                        for (SelectedDoc vid : lectures) {
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(0)[0])) {
                                DocRetrieval.Companion.getAgr0().add(vid);
                                DocRetrieval.Companion.setAgrLecAdapter0(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getAgr0(), app, a));

                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");

                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getAgrTags0().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getAgrTags0().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getAgrTags0(), DocRetrieval.Companion.getAgrLecAdapter0(), null);
                                subFieldAdt0.notifyDataSetChanged();
                                DocRetrieval.Companion.getAgrLecAdapter0().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(0)[1])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getAgr1().add(vid);
                                DocRetrieval.Companion.setAgrLecAdapter1(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getAgr1(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getAgrTags1().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getAgrTags1().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getAgrTags1(), DocRetrieval.Companion.getAgrLecAdapter1(), null);
                                subFieldAdt1.notifyDataSetChanged();
                                DocRetrieval.Companion.getAgrLecAdapter1().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(0)[2])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getAgr2().add(vid);
                                DocRetrieval.Companion.setAgrLecAdapter2(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getAgr2(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getAgrTags2().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getAgrTags2().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getAgrTags2(), DocRetrieval.Companion.getAgrLecAdapter2(), null);
                                subFieldAdt2.notifyDataSetChanged();
                                DocRetrieval.Companion.getAgrLecAdapter2().notifyDataSetChanged();
                            }
                        }

                        holder.readProgress1.setVisibility(View.INVISIBLE);
                        DocRetrieval.Companion.getAgrLecAdapters().add(DocRetrieval.Companion.getAgrLecAdapter0());
                        DocRetrieval.Companion.getAgrLecAdapters().add(DocRetrieval.Companion.getAgrLecAdapter1());
                        DocRetrieval.Companion.getAgrLecAdapters().add(DocRetrieval.Companion.getAgrLecAdapter2());

                        DocRetrieval.Companion.getAgrTagsArray().add(subFieldAdt0);
                        DocRetrieval.Companion.getAgrTagsArray().add(subFieldAdt1);
                        DocRetrieval.Companion.getAgrTagsArray().add(subFieldAdt2);


                        try {
                            holder.lecDisplayRV.setAdapter(DocRetrieval.Companion.getAgrLecAdapters().get(holder.getAbsoluteAdapterPosition()));
                            holder.lecTags.setAdapter(DocRetrieval.Companion.getAgrTagsArray().get(holder.getAbsoluteAdapterPosition()));

                            holder.navRight.setOnClickListener(v -> {
                                holder.lecDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getAgrLecAdapters().get(position).vidPosition++);
                                holder.lecTags.smoothScrollToPosition(DocRetrieval.Companion.getAgrTagsArray().get(position).getTagPosition() + 1);
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).addOnFailureListener(e -> {

                    });


                }
                break;
                case "Applied Human Sciences": {
                    //APPLIED HUMAN SCI
                    lectureDbRef.collection(dfd).get().addOnSuccessListener(queryDocumentSnapshots -> {
                        DocRetrieval.Companion.setAppHumanSci0(new ArrayList<>());
                        DocRetrieval.Companion.setAppHumanSci1(new ArrayList<>());
                        DocRetrieval.Companion.setAppHumanSci2(new ArrayList<>());
                        DocRetrieval.Companion.setAppHumanSci3(new ArrayList<>());
                        DocRetrieval.Companion.setAppHumanSci4(new ArrayList<>());


                        DocRetrieval.Companion.setAppHumanSciTags0(new ArrayList<>());
                        DocRetrieval.Companion.setAppHumanSciTags1(new ArrayList<>());
                        DocRetrieval.Companion.setAppHumanSciTags2(new ArrayList<>());
                        DocRetrieval.Companion.setAppHumanSciTags3(new ArrayList<>());
                        DocRetrieval.Companion.setAppHumanSciTags4(new ArrayList<>());

                        lectures = new ArrayList<>();
                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                            SelectedDoc vid = qds.toObject(SelectedDoc.class);
                            lectures.add(vid);
                        }
                        for (SelectedDoc vid : lectures) {

                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(1)[0])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getAppHumanSci0().add(vid);
                                DocRetrieval.Companion.setAppLecHumanAdapter0(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getAppHumanSci0(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getAppHumanSciTags0().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getAppHumanSciTags0().add(tagsProcessor.get(i));
                                        }
                                    }
                                    subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getAppHumanSciTags0(), DocRetrieval.Companion.getAppLecHumanAdapter0(), null);
                                    subFieldAdt0.notifyDataSetChanged();
                                }
                                DocRetrieval.Companion.getAppLecHumanAdapter0().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(1)[1])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getAppHumanSci1().add(vid);
                                DocRetrieval.Companion.setAppLecHumanAdapter1(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getAppHumanSci1(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getAppHumanSciTags1().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getAppHumanSciTags1().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getAppHumanSciTags1(), DocRetrieval.Companion.getAppLecHumanAdapter1(), null);
                                subFieldAdt1.notifyDataSetChanged();
                                DocRetrieval.Companion.getAppLecHumanAdapter1().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(1)[2])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getAppHumanSci2().add(vid);
                                DocRetrieval.Companion.setAppLecHumanAdapter2(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getAppHumanSci2(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getAppHumanSciTags2().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getAppHumanSciTags2().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getAppHumanSciTags2(), DocRetrieval.Companion.getAppLecHumanAdapter2(), null);
                                subFieldAdt2.notifyDataSetChanged();
                                DocRetrieval.Companion.getAppLecHumanAdapter2().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(1)[3])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getAppHumanSci3().add(vid);
                                DocRetrieval.Companion.setAppLecHumanAdapter3(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getAppHumanSci3(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getAppHumanSciTags3().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getAppHumanSciTags3().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt3 = new SubFieldAdt(c, DocRetrieval.Companion.getAppHumanSciTags3(), DocRetrieval.Companion.getAppLecHumanAdapter3(), null);
                                subFieldAdt3.notifyDataSetChanged();
                                DocRetrieval.Companion.getAppLecHumanAdapter3().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(1)[4])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getAppHumanSci4().add(vid);
                                DocRetrieval.Companion.setAppLecHumanAdapter4(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getAppHumanSci4(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getAppHumanSciTags4().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getAppHumanSciTags4().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt4 = new SubFieldAdt(c, DocRetrieval.Companion.getAppHumanSciTags4(), DocRetrieval.Companion.getAppLecHumanAdapter4(), null);
                                subFieldAdt4.notifyDataSetChanged();
                                DocRetrieval.Companion.getAppLecHumanAdapter4().notifyDataSetChanged();
                            }
                        }


                        holder.readProgress1.setVisibility(View.INVISIBLE);
                        DocRetrieval.Companion.getAppLecHumanAdapters().add(DocRetrieval.Companion.getAppLecHumanAdapter0());
                        DocRetrieval.Companion.getAppLecHumanAdapters().add(DocRetrieval.Companion.getAppLecHumanAdapter1());
                        DocRetrieval.Companion.getAppLecHumanAdapters().add(DocRetrieval.Companion.getAppLecHumanAdapter2());
                        DocRetrieval.Companion.getAppLecHumanAdapters().add(DocRetrieval.Companion.getAppLecHumanAdapter3());
                        DocRetrieval.Companion.getAppLecHumanAdapters().add(DocRetrieval.Companion.getAppLecHumanAdapter4());

                        DocRetrieval.Companion.getAppHumanSciTagsArray().add(subFieldAdt0);
                        DocRetrieval.Companion.getAppHumanSciTagsArray().add(subFieldAdt1);
                        DocRetrieval.Companion.getAppHumanSciTagsArray().add(subFieldAdt2);
                        DocRetrieval.Companion.getAppHumanSciTagsArray().add(subFieldAdt3);
                        DocRetrieval.Companion.getAppHumanSciTagsArray().add(subFieldAdt4);


                        try {
                            holder.lecDisplayRV.setAdapter(DocRetrieval.Companion.getAppLecHumanAdapters().get(holder.getAbsoluteAdapterPosition()));
                            holder.lecTags.setAdapter(DocRetrieval.Companion.getAppHumanSciTagsArray().get(holder.getAbsoluteAdapterPosition()));

                            holder.navRight.setOnClickListener(v -> {
                                holder.lecDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getAppLecHumanAdapters().get(position).vidPosition++);
                                holder.lecTags.smoothScrollToPosition(DocRetrieval.Companion.getAppHumanSciTagsArray().get(position).getTagPosition() + 1);
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });


                }
                break;
                case "Business": {
                    //BUSINESS
                    lectureDbRef.collection(dfd).get().addOnSuccessListener(queryDocumentSnapshots -> {
                        DocRetrieval.Companion.setBiz0(new ArrayList<>());
                        DocRetrieval.Companion.setBiz1(new ArrayList<>());
                        DocRetrieval.Companion.setBiz2(new ArrayList<>());

                        DocRetrieval.Companion.setBizTags0(new ArrayList<>());
                        DocRetrieval.Companion.setBizTags1(new ArrayList<>());
                        DocRetrieval.Companion.setBizTags2(new ArrayList<>());


                        lectures = new ArrayList<>();
                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                            SelectedDoc vid = qds.toObject(SelectedDoc.class);
                            lectures.add(vid);
                        }

                        for (SelectedDoc vid : lectures) {
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(2)[0])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getBiz0().add(vid);
                                DocRetrieval.Companion.setBizLecAdapter0(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getBiz0(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getBizTags0().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getBizTags0().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getBizTags0(), DocRetrieval.Companion.getBizLecAdapter0(), null);
                                subFieldAdt0.notifyDataSetChanged();
                                DocRetrieval.Companion.getBizLecAdapter0().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(2)[1])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getBiz1().add(vid);
                                DocRetrieval.Companion.setBizLecAdapter1(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getBiz1(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getBizTags1().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getBizTags1().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getBizTags1(), DocRetrieval.Companion.getBizLecAdapter1(), null);
                                subFieldAdt1.notifyDataSetChanged();
                                DocRetrieval.Companion.getBizLecAdapter1().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(2)[2])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getBiz2().add(vid);
                                DocRetrieval.Companion.setBizLecAdapter2(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getBiz2(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getBizTags2().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getBizTags2().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getBizTags2(), DocRetrieval.Companion.getBizLecAdapter2(), null);
                                subFieldAdt2.notifyDataSetChanged();
                                DocRetrieval.Companion.getBizLecAdapter2().notifyDataSetChanged();
                            }
                        }

                        holder.readProgress1.setVisibility(View.INVISIBLE);
                        DocRetrieval.Companion.getBizTagsArray().add(subFieldAdt0);
                        DocRetrieval.Companion.getBizTagsArray().add(subFieldAdt1);
                        DocRetrieval.Companion.getBizTagsArray().add(subFieldAdt2);


                        DocRetrieval.Companion.getBizLecAdapters().add(DocRetrieval.Companion.getBizLecAdapter0());
                        DocRetrieval.Companion.getBizLecAdapters().add(DocRetrieval.Companion.getBizLecAdapter1());
                        DocRetrieval.Companion.getBizLecAdapters().add(DocRetrieval.Companion.getBizLecAdapter2());

                        try {
                            holder.lecDisplayRV.setAdapter(DocRetrieval.Companion.getBizLecAdapters().get(holder.getAbsoluteAdapterPosition()));
                            holder.lecTags.setAdapter(DocRetrieval.Companion.getBizTagsArray().get(holder.getAbsoluteAdapterPosition()));

                            holder.navRight.setOnClickListener(v -> {
                                holder.lecDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getBizLecAdapters().get(position).vidPosition++);
                                holder.lecTags.smoothScrollToPosition(DocRetrieval.Companion.getBizTagsArray().get(position).getTagPosition() + 1);
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });


                }
                break;
                case "Economics": {
                    //ECONOMICS
                    lectureDbRef.collection(dfd).get().addOnSuccessListener(queryDocumentSnapshots -> {

                        DocRetrieval.Companion.setEcon0(new ArrayList<>());
                        DocRetrieval.Companion.setEcon1(new ArrayList<>());
                        DocRetrieval.Companion.setEcon2(new ArrayList<>());

                        DocRetrieval.Companion.setEconTags0(new ArrayList<>());
                        DocRetrieval.Companion.setEconTags1(new ArrayList<>());
                        DocRetrieval.Companion.setEconTags2(new ArrayList<>());

                        lectures = new ArrayList<>();
                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                            SelectedDoc vid = qds.toObject(SelectedDoc.class);
                            lectures.add(vid);
                        }


                        for (SelectedDoc vid : lectures) {
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(3)[0])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getEcon0().add(vid);
                                DocRetrieval.Companion.setEconLecAdapter0(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getEcon0(), app, a));

                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getEconTags0().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getEconTags0().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getEconTags0(), DocRetrieval.Companion.getEconLecAdapter0(), null);
                                subFieldAdt0.notifyDataSetChanged();
                                DocRetrieval.Companion.getEconLecAdapter0().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(3)[1])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getEcon1().add(vid);
                                DocRetrieval.Companion.setEconLecAdapter1(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getEcon2(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getEconTags1().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getEconTags1().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getEconTags1(), DocRetrieval.Companion.getEconLecAdapter1(), null);
                                subFieldAdt1.notifyDataSetChanged();
                                DocRetrieval.Companion.getEconLecAdapter1().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(3)[2])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getEcon2().add(vid);
                                DocRetrieval.Companion.setEconLecAdapter2(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getEcon2(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getEconTags2().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getEconTags2().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getEconTags2(), DocRetrieval.Companion.getEconLecAdapter0(), null);
                                subFieldAdt2.notifyDataSetChanged();
                                DocRetrieval.Companion.getEconLecAdapter2().notifyDataSetChanged();
                            }
                        }

                        holder.readProgress1.setVisibility(View.GONE);
                        DocRetrieval.Companion.getEconTagsArray().add(subFieldAdt0);
                        DocRetrieval.Companion.getEconTagsArray().add(subFieldAdt1);
                        DocRetrieval.Companion.getEconTagsArray().add(subFieldAdt2);


                        DocRetrieval.Companion.getEconLecAdapters().add(DocRetrieval.Companion.getEconLecAdapter0());
                        DocRetrieval.Companion.getEconLecAdapters().add(DocRetrieval.Companion.getEconLecAdapter1());
                        DocRetrieval.Companion.getEconLecAdapters().add(DocRetrieval.Companion.getEconLecAdapter2());


                        try {
                            holder.lecDisplayRV.setAdapter(DocRetrieval.Companion.getEconLecAdapters().get(holder.getAbsoluteAdapterPosition()));
                            holder.lecTags.setAdapter(DocRetrieval.Companion.getEconTagsArray().get(holder.getAbsoluteAdapterPosition()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });


                }
                break;
                case "Education": {
                    //EDUCATION
                    lectureDbRef.collection(dfd).get().addOnSuccessListener(queryDocumentSnapshots -> {

                        DocRetrieval.Companion.setEducation0(new ArrayList<>());
                        DocRetrieval.Companion.setEducation1(new ArrayList<>());
                        DocRetrieval.Companion.setEducation2(new ArrayList<>());
                        DocRetrieval.Companion.setEducation3(new ArrayList<>());
                        DocRetrieval.Companion.setEducation4(new ArrayList<>());
                        DocRetrieval.Companion.setEducation5(new ArrayList<>());
                        DocRetrieval.Companion.setEducation6(new ArrayList<>());

                        DocRetrieval.Companion.setEduTags0(new ArrayList<>());
                        DocRetrieval.Companion.setEduTags1(new ArrayList<>());
                        DocRetrieval.Companion.setEduTags2(new ArrayList<>());
                        DocRetrieval.Companion.setEduTags3(new ArrayList<>());
                        DocRetrieval.Companion.setEduTags4(new ArrayList<>());
                        DocRetrieval.Companion.setEduTags5(new ArrayList<>());
                        DocRetrieval.Companion.setEduTags6(new ArrayList<>());

                        lectures = new ArrayList<>();
                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                            SelectedDoc vid = qds.toObject(SelectedDoc.class);
                            lectures.add(vid);
                        }

                        for (SelectedDoc vid : lectures) {
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(4)[0])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getEducation0().add(vid);
                                DocRetrieval.Companion.setEduLecAdapter0(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getEducation0(), app, a));
                                DocRetrieval.Companion.getEduLecAdapter0().notifyDataSetChanged();
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getEduTags0().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getEduTags0().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getEduTags0(), DocRetrieval.Companion.getEduLecAdapter0(), null);
                                subFieldAdt0.notifyDataSetChanged();
                                DocRetrieval.Companion.getEduLecAdapter0().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(4)[1])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getEducation1().add(vid);
                                DocRetrieval.Companion.setEduLecAdapter1(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getEducation1(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getEduTags1().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getEduTags1().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getEduTags1(), DocRetrieval.Companion.getEduLecAdapter1(), null);
                                subFieldAdt1.notifyDataSetChanged();
                                DocRetrieval.Companion.getEduLecAdapter1().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(4)[2])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getEducation2().add(vid);
                                DocRetrieval.Companion.setEduLecAdapter2(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getEducation2(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getEduTags2().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getEduTags2().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getEduTags2(), DocRetrieval.Companion.getEduLecAdapter2(), null);
                                subFieldAdt2.notifyDataSetChanged();
                                DocRetrieval.Companion.getEduLecAdapter2().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(4)[3])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getEducation3().add(vid);
                                DocRetrieval.Companion.setEduLecAdapter3(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getEducation3(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getEduTags3().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getEduTags3().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt3 = new SubFieldAdt(c, DocRetrieval.Companion.getEduTags3(), DocRetrieval.Companion.getEduLecAdapter3(), null);
                                subFieldAdt3.notifyDataSetChanged();
                                DocRetrieval.Companion.getEduLecAdapter3().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(4)[4])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getEducation4().add(vid);
                                DocRetrieval.Companion.setEduLecAdapter4(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getEducation4(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getEduTags4().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getEduTags4().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt4 = new SubFieldAdt(c, DocRetrieval.Companion.getEduTags4(), DocRetrieval.Companion.getEduLecAdapter4(), null);
                                subFieldAdt4.notifyDataSetChanged();
                                DocRetrieval.Companion.getEduLecAdapter4().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(4)[5])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getEducation5().add(vid);
                                DocRetrieval.Companion.setEduLecAdapter5(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getEducation5(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getEduTags5().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getEduTags5().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt5 = new SubFieldAdt(c, DocRetrieval.Companion.getEduTags5(), DocRetrieval.Companion.getEduLecAdapter5(), null);
                                subFieldAdt5.notifyDataSetChanged();
                                DocRetrieval.Companion.getEduLecAdapter5().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(4)[6])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getEducation6().add(vid);
                                DocRetrieval.Companion.setEduLecAdapter6(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getEducation6(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getEduTags6().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getEduTags6().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt6 = new SubFieldAdt(c, DocRetrieval.Companion.getEduTags6(), DocRetrieval.Companion.getEduLecAdapter6(), null);
                                subFieldAdt6.notifyDataSetChanged();
                                DocRetrieval.Companion.getEduLecAdapter6().notifyDataSetChanged();
                            }
                        }

                        holder.readProgress1.setVisibility(View.GONE);
                        DocRetrieval.Companion.getEducationTagsArray().add(subFieldAdt0);
                        DocRetrieval.Companion.getEducationTagsArray().add(subFieldAdt1);
                        DocRetrieval.Companion.getEducationTagsArray().add(subFieldAdt2);
                        DocRetrieval.Companion.getEducationTagsArray().add(subFieldAdt3);
                        DocRetrieval.Companion.getEducationTagsArray().add(subFieldAdt4);
                        DocRetrieval.Companion.getEducationTagsArray().add(subFieldAdt5);
                        DocRetrieval.Companion.getEducationTagsArray().add(subFieldAdt6);
                        DocRetrieval.Companion.getEducationTagsArray().add(subFieldAdt7);


                        DocRetrieval.Companion.getEduLecAdapters().add(DocRetrieval.Companion.getEduLecAdapter0());
                        DocRetrieval.Companion.getEduLecAdapters().add(DocRetrieval.Companion.getEduLecAdapter1());
                        DocRetrieval.Companion.getEduLecAdapters().add(DocRetrieval.Companion.getEduLecAdapter2());
                        DocRetrieval.Companion.getEduLecAdapters().add(DocRetrieval.Companion.getEduLecAdapter3());
                        DocRetrieval.Companion.getEduLecAdapters().add(DocRetrieval.Companion.getEduLecAdapter4());
                        DocRetrieval.Companion.getEduLecAdapters().add(DocRetrieval.Companion.getEduLecAdapter5());
                        DocRetrieval.Companion.getEduLecAdapters().add(DocRetrieval.Companion.getEduLecAdapter6());


                        try {
                            holder.lecDisplayRV.setAdapter(DocRetrieval.Companion.getEducationTagsArray().get(holder.getAbsoluteAdapterPosition()));
                            holder.lecDisplayRV.setAdapter(DocRetrieval.Companion.getEduLecAdapters().get(holder.getAbsoluteAdapterPosition()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }).addOnFailureListener(e -> {

                    });

                }
                break;
                case "Engineering & Technology": {
                    //ENGINEERING
                    lectureDbRef.collection(dfd).get().addOnSuccessListener(queryDocumentSnapshots -> {

                        DocRetrieval.Companion.setEngineering0(new ArrayList<>());
                        DocRetrieval.Companion.setEngineering1(new ArrayList<>());
                        DocRetrieval.Companion.setEngineering2(new ArrayList<>());
                        DocRetrieval.Companion.setEngineering3(new ArrayList<>());
                        DocRetrieval.Companion.setEngineering4(new ArrayList<>());
                        DocRetrieval.Companion.setEngineering5(new ArrayList<>());

                        DocRetrieval.Companion.setEngTags0(new ArrayList<>());
                        DocRetrieval.Companion.setEngTags1(new ArrayList<>());
                        DocRetrieval.Companion.setEngTags2(new ArrayList<>());
                        DocRetrieval.Companion.setEngTags3(new ArrayList<>());
                        DocRetrieval.Companion.setEngTags4(new ArrayList<>());
                        DocRetrieval.Companion.setEngTags5(new ArrayList<>());

                        lectures = new ArrayList<>();
                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                            SelectedDoc vid = qds.toObject(SelectedDoc.class);
                            lectures.add(vid);
                        }

                        for (SelectedDoc vid : lectures) {
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(5)[0])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getEngineering0().add(vid);
                                DocRetrieval.Companion.setEngLecAdapter0(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getEngineering0(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getEngTags0().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getEngTags0().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getEngTags0(), DocRetrieval.Companion.getEngLecAdapter0(), null);
                                subFieldAdt0.notifyDataSetChanged();
                                DocRetrieval.Companion.getEngLecAdapter0().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(5)[1])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getEngineering1().add(vid);
                                DocRetrieval.Companion.setEngLecAdapter1(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getEngineering1(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getEngTags1().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getEngTags1().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getEngTags1(), DocRetrieval.Companion.getEngLecAdapter1(), null);
                                subFieldAdt1.notifyDataSetChanged();
                                DocRetrieval.Companion.getEngLecAdapter1().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(5)[2])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getEngineering2().add(vid);
                                DocRetrieval.Companion.setEngLecAdapter2(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getEngineering2(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getEngTags2().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getEngTags2().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getEngTags2(), DocRetrieval.Companion.getEngLecAdapter2(), null);
                                subFieldAdt2.notifyDataSetChanged();
                                DocRetrieval.Companion.getEngLecAdapter2().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(5)[3])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getEngineering3().add(vid);
                                DocRetrieval.Companion.setEngLecAdapter3(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getEngineering3(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getEngTags3().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getEngTags3().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt3 = new SubFieldAdt(c, DocRetrieval.Companion.getEngTags3(), DocRetrieval.Companion.getEngLecAdapter3(), null);
                                subFieldAdt3.notifyDataSetChanged();
                                DocRetrieval.Companion.getEngLecAdapter3().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(5)[4])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getEngineering4().add(vid);
                                DocRetrieval.Companion.setEngLecAdapter4(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getEngineering4(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getEngTags4().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getEngTags4().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt4 = new SubFieldAdt(c, DocRetrieval.Companion.getEngTags4(), DocRetrieval.Companion.getEngLecAdapter4(), null);
                                subFieldAdt4.notifyDataSetChanged();
                                DocRetrieval.Companion.getEngLecAdapter4().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(5)[5])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getEngineering5().add(vid);
                                DocRetrieval.Companion.setEngLecAdapter5(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getEngineering5(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getEngTags5().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getEngTags5().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt5 = new SubFieldAdt(c, DocRetrieval.Companion.getEngTags5(), DocRetrieval.Companion.getEngLecAdapter5(), null);
                                subFieldAdt5.notifyDataSetChanged();
                                DocRetrieval.Companion.getEngLecAdapter5().notifyDataSetChanged();
                            }
                        }

                        holder.readProgress1.setVisibility(View.GONE);
                        DocRetrieval.Companion.getEngTagsArray().add(subFieldAdt0);
                        DocRetrieval.Companion.getEngTagsArray().add(subFieldAdt1);
                        DocRetrieval.Companion.getEngTagsArray().add(subFieldAdt2);
                        DocRetrieval.Companion.getEngTagsArray().add(subFieldAdt3);
                        DocRetrieval.Companion.getEngTagsArray().add(subFieldAdt4);
                        DocRetrieval.Companion.getEngTagsArray().add(subFieldAdt5);


                        DocRetrieval.Companion.getEngLecAdapters().add(DocRetrieval.Companion.getEngLecAdapter0());
                        DocRetrieval.Companion.getEngLecAdapters().add(DocRetrieval.Companion.getEngLecAdapter1());
                        DocRetrieval.Companion.getEngLecAdapters().add(DocRetrieval.Companion.getEngLecAdapter2());
                        DocRetrieval.Companion.getEngLecAdapters().add(DocRetrieval.Companion.getEngLecAdapter3());
                        DocRetrieval.Companion.getEngLecAdapters().add(DocRetrieval.Companion.getEngLecAdapter4());
                        DocRetrieval.Companion.getEngLecAdapters().add(DocRetrieval.Companion.getEngLecAdapter5());


                        try {
                            holder.lecDisplayRV.setAdapter(DocRetrieval.Companion.getEngLecAdapters().get(holder.getAbsoluteAdapterPosition()));
                            holder.lecTags.setAdapter(DocRetrieval.Companion.getEngTagsArray().get(holder.getAbsoluteAdapterPosition()));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }
                break;
                case "Environmental Studies": {
                    //ENVIRONMENTAL STUDIES
                    lectureDbRef.collection(dfd).get().addOnSuccessListener(queryDocumentSnapshots -> {

                        DocRetrieval.Companion.setEnvironmental0(new ArrayList<>());
                        DocRetrieval.Companion.setEnvironmental1(new ArrayList<>());
                        DocRetrieval.Companion.setEnvironmental2(new ArrayList<>());
                        DocRetrieval.Companion.setEnvironmental3(new ArrayList<>());

                        DocRetrieval.Companion.setEnvTags0(new ArrayList<>());
                        DocRetrieval.Companion.setEnvTags1(new ArrayList<>());
                        DocRetrieval.Companion.setEnvTags2(new ArrayList<>());
                        DocRetrieval.Companion.setEnvTags3(new ArrayList<>());

                        lectures = new ArrayList<>();
                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                            SelectedDoc vid = qds.toObject(SelectedDoc.class);
                            lectures.add(vid);
                        }

                        for (SelectedDoc vid : lectures) {
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(6)[0])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getEnvironmental0().add(vid);
                                DocRetrieval.Companion.setEnvLecAdapter0(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getEnvironmental0(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getEnvTags0().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getEnvTags0().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getEnvTags0(), DocRetrieval.Companion.getEnvLecAdapter0(), null);
                                subFieldAdt0.notifyDataSetChanged();
                                DocRetrieval.Companion.getEnvLecAdapter0().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(6)[1])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getEnvironmental1().add(vid);
                                DocRetrieval.Companion.setEnvLecAdapter1(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getEnvironmental1(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getEnvTags1().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getEnvTags1().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getEnvTags1(), DocRetrieval.Companion.getEngLecAdapter1(), null);
                                subFieldAdt1.notifyDataSetChanged();
                                DocRetrieval.Companion.getEnvLecAdapter1().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(6)[2])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getEnvironmental2().add(vid);
                                DocRetrieval.Companion.setEnvLecAdapter2(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getEnvironmental2(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getEnvTags2().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getEnvTags2().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getEnvTags2(), DocRetrieval.Companion.getEngLecAdapter2(), null);
                                subFieldAdt2.notifyDataSetChanged();
                                DocRetrieval.Companion.getEnvLecAdapter2().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(6)[3])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getEnvironmental3().add(vid);
                                DocRetrieval.Companion.setEnvLecAdapter3(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getEnvironmental3(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getEnvTags3().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getEnvTags3().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt3 = new SubFieldAdt(c, DocRetrieval.Companion.getEnvTags3(), DocRetrieval.Companion.getEngLecAdapter3(), null);
                                subFieldAdt3.notifyDataSetChanged();
                                DocRetrieval.Companion.getEnvLecAdapter3().notifyDataSetChanged();
                            }
                        }

                        holder.readProgress1.setVisibility(View.GONE);
                        DocRetrieval.Companion.getEnvTagsArray().add(subFieldAdt0);
                        DocRetrieval.Companion.getEnvTagsArray().add(subFieldAdt1);
                        DocRetrieval.Companion.getEnvTagsArray().add(subFieldAdt2);
                        DocRetrieval.Companion.getEnvTagsArray().add(subFieldAdt3);

                        DocRetrieval.Companion.getEnvLecAdapters().add(DocRetrieval.Companion.getEnvLecAdapter0());
                        DocRetrieval.Companion.getEnvLecAdapters().add(DocRetrieval.Companion.getEnvLecAdapter1());
                        DocRetrieval.Companion.getEnvLecAdapters().add(DocRetrieval.Companion.getEnvLecAdapter2());
                        DocRetrieval.Companion.getEnvLecAdapters().add(DocRetrieval.Companion.getEnvLecAdapter3());


                        try {
                            holder.lecDisplayRV.setAdapter(DocRetrieval.Companion.getEnvLecAdapters().get(holder.getAbsoluteAdapterPosition()));
                            holder.lecTags.setAdapter(DocRetrieval.Companion.getEnvTagsArray().get(holder.getAbsoluteAdapterPosition()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }
                break;
                case "Hospitality & Tourism": {
                    //HOSPITALITY
                    lectureDbRef.collection(dfd).get().addOnSuccessListener(queryDocumentSnapshots -> {

                        DocRetrieval.Companion.setHospitality0(new ArrayList<>());
                        DocRetrieval.Companion.setHospitality1(new ArrayList<>());

                        DocRetrieval.Companion.setHospitalityTags0(new ArrayList<>());
                        DocRetrieval.Companion.setHospitalityTags1(new ArrayList<>());

                        lectures = new ArrayList<>();
                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                            SelectedDoc vid = qds.toObject(SelectedDoc.class);
                            lectures.add(vid);
                        }

                        for (SelectedDoc vid : lectures) {
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(7)[0])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getHospitality0().add(vid);
                                DocRetrieval.Companion.setHospitalityLecAdapter0(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getHospitality0(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getHospitalityTags0().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getHospitalityTags0().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getHospitalityTags0(), DocRetrieval.Companion.getHospitalityLecAdapter0(), null);
                                subFieldAdt0.notifyDataSetChanged();
                                DocRetrieval.Companion.getHospitalityLecAdapter0().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(7)[1])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getHospitality1().add(vid);
                                DocRetrieval.Companion.setHospitalityLecAdapter1(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getHospitality1(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getHospitalityTags1().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getHospitalityTags1().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getHospitalityTags1(), DocRetrieval.Companion.getHospitalityLecAdapter0(), null);
                                subFieldAdt1.notifyDataSetChanged();
                                DocRetrieval.Companion.getHospitalityLecAdapter1().notifyDataSetChanged();
                            }
                        }

                        holder.readProgress1.setVisibility(View.GONE);
                        DocRetrieval.Companion.getHospitalityTagsArray().add(subFieldAdt0);
                        DocRetrieval.Companion.getHospitalityTagsArray().add(subFieldAdt1);


                        DocRetrieval.Companion.getHospitalityLecAdapters().add(DocRetrieval.Companion.getHospitalityLecAdapter0());
                        DocRetrieval.Companion.getHospitalityLecAdapters().add(DocRetrieval.Companion.getHospitalityLecAdapter1());


                        try {
                            holder.lecDisplayRV.setAdapter(DocRetrieval.Companion.getHospitalityLecAdapters().get(holder.getAbsoluteAdapterPosition()));
                            holder.lecTags.setAdapter(DocRetrieval.Companion.getHospitalityTagsArray().get(holder.getAbsoluteAdapterPosition()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }
                break;
                case "Humanities & Social Sciences": {
                    //HUMANITIES SOCIAL SCI
                    lectureDbRef.collection(dfd).get().addOnSuccessListener(queryDocumentSnapshots -> {

                        DocRetrieval.Companion.setHumanities0(new ArrayList<>());
                        DocRetrieval.Companion.setHumanities1(new ArrayList<>());
                        DocRetrieval.Companion.setHumanities2(new ArrayList<>());
                        DocRetrieval.Companion.setHumanities3(new ArrayList<>());
                        DocRetrieval.Companion.setHumanities4(new ArrayList<>());
                        DocRetrieval.Companion.setHumanities5(new ArrayList<>());
                        DocRetrieval.Companion.setHumanities6(new ArrayList<>());
                        DocRetrieval.Companion.setHumanities7(new ArrayList<>());
                        DocRetrieval.Companion.setHumanities8(new ArrayList<>());
                        DocRetrieval.Companion.setHumanities9(new ArrayList<>());
                        DocRetrieval.Companion.setHumanities10(new ArrayList<>());

                        DocRetrieval.Companion.setHumanitiesTags0(new ArrayList<>());
                        DocRetrieval.Companion.setHumanitiesTags1(new ArrayList<>());
                        DocRetrieval.Companion.setHumanitiesTags2(new ArrayList<>());
                        DocRetrieval.Companion.setHumanitiesTags3(new ArrayList<>());
                        DocRetrieval.Companion.setHumanitiesTags4(new ArrayList<>());
                        DocRetrieval.Companion.setHumanitiesTags5(new ArrayList<>());
                        DocRetrieval.Companion.setHumanitiesTags6(new ArrayList<>());
                        DocRetrieval.Companion.setHumanitiesTags7(new ArrayList<>());
                        DocRetrieval.Companion.setHumanitiesTags8(new ArrayList<>());
                        DocRetrieval.Companion.setHumanitiesTags9(new ArrayList<>());
                        DocRetrieval.Companion.setHumanitiesTags10(new ArrayList<>());

                        lectures = new ArrayList<>();
                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                            SelectedDoc vid = qds.toObject(SelectedDoc.class);
                            lectures.add(vid);
                        }

                        for (SelectedDoc vid : lectures) {
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(8)[0])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getHumanities0().add(vid);
                                DocRetrieval.Companion.setHumanitiesLecAdapter0(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getHumanities0(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getHumanitiesTags0().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getHumanitiesTags0().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getHumanitiesTags0(), DocRetrieval.Companion.getHumanitiesLecAdapter0(), null);
                                subFieldAdt0.notifyDataSetChanged();
                                DocRetrieval.Companion.getHumanitiesLecAdapter0().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(8)[1])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getHumanities1().add(vid);
                                DocRetrieval.Companion.setHumanitiesLecAdapter1(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getHumanities1(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getHumanitiesTags1().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getHumanitiesTags1().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getHumanitiesTags1(), DocRetrieval.Companion.getHumanitiesLecAdapter1(), null);
                                subFieldAdt1.notifyDataSetChanged();
                                DocRetrieval.Companion.getHumanitiesLecAdapter1().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(8)[2])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getHumanities2().add(vid);
                                DocRetrieval.Companion.setHumanitiesLecAdapter2(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getHumanities2(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getHumanitiesTags2().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getHumanitiesTags2().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getHumanitiesTags2(), DocRetrieval.Companion.getHumanitiesLecAdapter2(), null);
                                subFieldAdt2.notifyDataSetChanged();
                                DocRetrieval.Companion.getHumanitiesLecAdapter2().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(8)[3])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getHumanities3().add(vid);
                                DocRetrieval.Companion.setHumanitiesLecAdapter3(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getHumanities3(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getHumanitiesTags3().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getHumanitiesTags3().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt3 = new SubFieldAdt(c, DocRetrieval.Companion.getHumanitiesTags3(), DocRetrieval.Companion.getHumanitiesLecAdapter3(), null);
                                subFieldAdt3.notifyDataSetChanged();
                                DocRetrieval.Companion.getHumanitiesLecAdapter3().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(8)[4])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getHumanities4().add(vid);
                                DocRetrieval.Companion.setHumanitiesLecAdapter4(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getHumanities4(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getHumanitiesTags4().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getHumanitiesTags4().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt4 = new SubFieldAdt(c, DocRetrieval.Companion.getHumanitiesTags4(), DocRetrieval.Companion.getHumanitiesLecAdapter4(), null);
                                subFieldAdt4.notifyDataSetChanged();
                                DocRetrieval.Companion.getHumanitiesLecAdapter4().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(8)[5])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getHumanities5().add(vid);
                                DocRetrieval.Companion.setHumanitiesLecAdapter5(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getHumanities5(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getHumanitiesTags5().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getHumanitiesTags5().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt5 = new SubFieldAdt(c, DocRetrieval.Companion.getHumanitiesTags5(), DocRetrieval.Companion.getHumanitiesLecAdapter5(), null);
                                subFieldAdt5.notifyDataSetChanged();
                                DocRetrieval.Companion.getHumanitiesLecAdapter5().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(8)[6])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getHumanities6().add(vid);
                                DocRetrieval.Companion.setHumanitiesLecAdapter6(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getHumanities6(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getHumanitiesTags6().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getHumanitiesTags6().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt6 = new SubFieldAdt(c, DocRetrieval.Companion.getHumanitiesTags6(), DocRetrieval.Companion.getHumanitiesLecAdapter6(), null);
                                subFieldAdt6.notifyDataSetChanged();
                                DocRetrieval.Companion.getHumanitiesLecAdapter6().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(8)[7])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getHumanities7().add(vid);
                                DocRetrieval.Companion.setHumanitiesLecAdapter7(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getHumanities7(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getHumanitiesTags7().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getHumanitiesTags7().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt7 = new SubFieldAdt(c, DocRetrieval.Companion.getHumanitiesTags7(), DocRetrieval.Companion.getHumanitiesLecAdapter7(), null);
                                subFieldAdt7.notifyDataSetChanged();
                                DocRetrieval.Companion.getHumanitiesLecAdapter7().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(8)[8])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getHumanities8().add(vid);
                                DocRetrieval.Companion.setHumanitiesLecAdapter8(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getHumanities8(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getHumanitiesTags8().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getHumanitiesTags8().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt8 = new SubFieldAdt(c, DocRetrieval.Companion.getHumanitiesTags8(), DocRetrieval.Companion.getHumanitiesLecAdapter8(), null);
                                subFieldAdt8.notifyDataSetChanged();
                                DocRetrieval.Companion.getHumanitiesLecAdapter8().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(8)[9])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getHumanities9().add(vid);
                                DocRetrieval.Companion.setHumanitiesLecAdapter9(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getHumanities9(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getHumanitiesTags9().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getHumanitiesTags9().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt9 = new SubFieldAdt(c, DocRetrieval.Companion.getHumanitiesTags9(), DocRetrieval.Companion.getHumanitiesLecAdapter9(), null);
                                subFieldAdt9.notifyDataSetChanged();
                                DocRetrieval.Companion.getHumanitiesLecAdapter9().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(8)[10])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getHumanities10().add(vid);
                                DocRetrieval.Companion.setHumanitiesLecAdapter10(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getHumanities10(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getHumanitiesTags10().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getHumanitiesTags10().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt10 = new SubFieldAdt(c, DocRetrieval.Companion.getHumanitiesTags10(), DocRetrieval.Companion.getHumanitiesLecAdapter10(), null);
                                subFieldAdt10.notifyDataSetChanged();
                                DocRetrieval.Companion.getHumanitiesLecAdapter10().notifyDataSetChanged();
                            }
                        }

                        holder.readProgress1.setVisibility(View.GONE);
                        DocRetrieval.Companion.getHumanitiesTagsArray().add(subFieldAdt0);
                        DocRetrieval.Companion.getHumanitiesTagsArray().add(subFieldAdt1);
                        DocRetrieval.Companion.getHumanitiesTagsArray().add(subFieldAdt2);
                        DocRetrieval.Companion.getHumanitiesTagsArray().add(subFieldAdt3);
                        DocRetrieval.Companion.getHumanitiesTagsArray().add(subFieldAdt4);
                        DocRetrieval.Companion.getHumanitiesTagsArray().add(subFieldAdt5);
                        DocRetrieval.Companion.getHumanitiesTagsArray().add(subFieldAdt6);
                        DocRetrieval.Companion.getHumanitiesTagsArray().add(subFieldAdt7);
                        DocRetrieval.Companion.getHumanitiesTagsArray().add(subFieldAdt8);
                        DocRetrieval.Companion.getHumanitiesTagsArray().add(subFieldAdt9);
                        DocRetrieval.Companion.getHumanitiesTagsArray().add(subFieldAdt10);

                        DocRetrieval.Companion.getHumanitiesLecAdapters().add(DocRetrieval.Companion.getHumanitiesLecAdapter0());
                        DocRetrieval.Companion.getHumanitiesLecAdapters().add(DocRetrieval.Companion.getHumanitiesLecAdapter1());
                        DocRetrieval.Companion.getHumanitiesLecAdapters().add(DocRetrieval.Companion.getHumanitiesLecAdapter2());
                        DocRetrieval.Companion.getHumanitiesLecAdapters().add(DocRetrieval.Companion.getHumanitiesLecAdapter3());
                        DocRetrieval.Companion.getHumanitiesLecAdapters().add(DocRetrieval.Companion.getHumanitiesLecAdapter4());
                        DocRetrieval.Companion.getHumanitiesLecAdapters().add(DocRetrieval.Companion.getHumanitiesLecAdapter5());
                        DocRetrieval.Companion.getHumanitiesLecAdapters().add(DocRetrieval.Companion.getHumanitiesLecAdapter6());
                        DocRetrieval.Companion.getHumanitiesLecAdapters().add(DocRetrieval.Companion.getHumanitiesLecAdapter7());
                        DocRetrieval.Companion.getHumanitiesLecAdapters().add(DocRetrieval.Companion.getHumanitiesLecAdapter8());
                        DocRetrieval.Companion.getHumanitiesLecAdapters().add(DocRetrieval.Companion.getHumanitiesLecAdapter9());
                        DocRetrieval.Companion.getHumanitiesLecAdapters().add(DocRetrieval.Companion.getHumanitiesLecAdapter10());


                        try {
                            holder.lecDisplayRV.setAdapter(DocRetrieval.Companion.getHumanitiesLecAdapters().get(holder.getAbsoluteAdapterPosition()));
                            holder.lecTags.setAdapter(DocRetrieval.Companion.getHumanitiesTagsArray().get(holder.getAbsoluteAdapterPosition()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });


                }
                break;
                case "Law": {
                    //LAW
                    lectureDbRef.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            DocRetrieval.Companion.setLaw0(new ArrayList<>());
                            DocRetrieval.Companion.setLaw1(new ArrayList<>());
                            DocRetrieval.Companion.setLaw2(new ArrayList<>());
                            DocRetrieval.Companion.setLaw3(new ArrayList<>());
                            DocRetrieval.Companion.setLaw4(new ArrayList<>());
                            DocRetrieval.Companion.setLaw5(new ArrayList<>());

                            DocRetrieval.Companion.setLawTags0(new ArrayList<>());
                            DocRetrieval.Companion.setLawTags1(new ArrayList<>());
                            DocRetrieval.Companion.setLawTags2(new ArrayList<>());
                            DocRetrieval.Companion.setLawTags3(new ArrayList<>());
                            DocRetrieval.Companion.setLawTags4(new ArrayList<>());
                            DocRetrieval.Companion.setLawTags5(new ArrayList<>());

                            lectures = new ArrayList<>();
                            for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                SelectedDoc vid = qds.toObject(SelectedDoc.class);
                                lectures.add(vid);
                            }

                            for (SelectedDoc vid : lectures) {
                                if (vid.getDocMetaData().contains(DocSorting.getSubFields(9)[0])) {
                                    String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                    DocRetrieval.Companion.getLaw0().add(vid);
                                    DocRetrieval.Companion.setLawLecAdapter0(new CloudVideosAdapter(lecturesList,
                                            playerView,
                                            app, context, c, DocRetrieval.Companion.getLaw0(), app, a));
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getLawTags0().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getLawTags0().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getLawTags0(), DocRetrieval.Companion.getLawLecAdapter0(), null);
                                    subFieldAdt0.notifyDataSetChanged();
                                    DocRetrieval.Companion.getLawLecAdapter0().notifyDataSetChanged();
                                }

                                if (vid.getDocMetaData().contains(DocSorting.getSubFields(9)[1])) {
                                    String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                    DocRetrieval.Companion.getLaw1().add(vid);
                                    DocRetrieval.Companion.setLawLecAdapter1(new CloudVideosAdapter(lecturesList,
                                            playerView,
                                            app, context, c, DocRetrieval.Companion.getLaw1(), app, a));
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getLawTags1().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getLawTags1().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getLawTags1(), DocRetrieval.Companion.getLawLecAdapter0(), null);
                                    subFieldAdt1.notifyDataSetChanged();
                                    DocRetrieval.Companion.getLawLecAdapter1().notifyDataSetChanged();
                                }

                                if (vid.getDocMetaData().contains(DocSorting.getSubFields(9)[2])) {
                                    String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                    DocRetrieval.Companion.getLaw2().add(vid);
                                    DocRetrieval.Companion.setLawLecAdapter2(new CloudVideosAdapter(lecturesList,
                                            playerView,
                                            app, context, c, DocRetrieval.Companion.getLaw2(), app, a));
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getLawTags2().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getLawTags2().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getLawTags2(), DocRetrieval.Companion.getLawLecAdapter2(), null);
                                    subFieldAdt2.notifyDataSetChanged();
                                    DocRetrieval.Companion.getLawLecAdapter2().notifyDataSetChanged();
                                }

                                if (vid.getDocMetaData().contains(DocSorting.getSubFields(9)[3])) {
                                    String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                    DocRetrieval.Companion.getLaw3().add(vid);
                                    DocRetrieval.Companion.setLawLecAdapter3(new CloudVideosAdapter(lecturesList,
                                            playerView,
                                            app, context, c, DocRetrieval.Companion.getLaw3(), app, a));
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getLawTags3().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getLawTags3().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt3 = new SubFieldAdt(c, DocRetrieval.Companion.getLawTags3(), DocRetrieval.Companion.getLawLecAdapter3(), null);
                                    subFieldAdt3.notifyDataSetChanged();
                                    DocRetrieval.Companion.getLawLecAdapter3().notifyDataSetChanged();
                                }

                                if (vid.getDocMetaData().contains(DocSorting.getSubFields(9)[4])) {
                                    String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                    DocRetrieval.Companion.getLaw4().add(vid);
                                    DocRetrieval.Companion.setLawLecAdapter4(new CloudVideosAdapter(lecturesList,
                                            playerView,
                                            app, context, c, DocRetrieval.Companion.getLaw4(), app, a));
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getLawTags4().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getLawTags4().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt4 = new SubFieldAdt(c, DocRetrieval.Companion.getLawTags4(), DocRetrieval.Companion.getLawLecAdapter4(), null);
                                    subFieldAdt4.notifyDataSetChanged();
                                    DocRetrieval.Companion.getLawLecAdapter4().notifyDataSetChanged();
                                }

                                if (vid.getDocMetaData().contains(DocSorting.getSubFields(9)[5])) {
                                    String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                    DocRetrieval.Companion.getLaw5().add(vid);
                                    DocRetrieval.Companion.setLawLecAdapter5(new CloudVideosAdapter(lecturesList,
                                            playerView,
                                            app, context, c, DocRetrieval.Companion.getLaw5(), app, a));
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getLawTags5().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getLawTags5().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt5 = new SubFieldAdt(c, DocRetrieval.Companion.getLawTags1(), DocRetrieval.Companion.getLawLecAdapter5(), null);
                                    subFieldAdt5.notifyDataSetChanged();
                                    DocRetrieval.Companion.getLawLecAdapter5().notifyDataSetChanged();
                                }
                            }

                            holder.readProgress1.setVisibility(View.GONE);
                            DocRetrieval.Companion.getLawTagsArray().add(subFieldAdt0);
                            DocRetrieval.Companion.getLawTagsArray().add(subFieldAdt1);
                            DocRetrieval.Companion.getLawTagsArray().add(subFieldAdt2);
                            DocRetrieval.Companion.getLawTagsArray().add(subFieldAdt3);
                            DocRetrieval.Companion.getLawTagsArray().add(subFieldAdt4);
                            DocRetrieval.Companion.getLawTagsArray().add(subFieldAdt5);


                            DocRetrieval.Companion.getLawLecAdapters().add(DocRetrieval.Companion.getLawLecAdapter0());
                            DocRetrieval.Companion.getLawLecAdapters().add(DocRetrieval.Companion.getLawLecAdapter1());
                            DocRetrieval.Companion.getLawLecAdapters().add(DocRetrieval.Companion.getLawLecAdapter2());
                            DocRetrieval.Companion.getLawLecAdapters().add(DocRetrieval.Companion.getLawLecAdapter3());
                            DocRetrieval.Companion.getLawLecAdapters().add(DocRetrieval.Companion.getLawLecAdapter4());
                            DocRetrieval.Companion.getLawLecAdapters().add(DocRetrieval.Companion.getLawLecAdapter5());


                            try {
                                holder.lecDisplayRV.setAdapter(DocRetrieval.Companion.getLawLecAdapters().get(holder.getAbsoluteAdapterPosition()));
                                holder.lecTags.setAdapter(DocRetrieval.Companion.getLawTagsArray().get(holder.getAbsoluteAdapterPosition()));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }
                break;
                case "Medicine": {
                    //MEDICINE
                    lectureDbRef.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            DocRetrieval.Companion.setMedicine0(new ArrayList<>());
                            DocRetrieval.Companion.setMedicine1(new ArrayList<>());
                            DocRetrieval.Companion.setMedicine2(new ArrayList<>());
                            DocRetrieval.Companion.setMedicine3(new ArrayList<>());
                            DocRetrieval.Companion.setMedicine4(new ArrayList<>());
                            DocRetrieval.Companion.setMedicine5(new ArrayList<>());
                            DocRetrieval.Companion.setMedicine6(new ArrayList<>());
                            DocRetrieval.Companion.setMedicine7(new ArrayList<>());
                            DocRetrieval.Companion.setMedicine8(new ArrayList<>());
                            DocRetrieval.Companion.setMedicine9(new ArrayList<>());

                            DocRetrieval.Companion.setMedTags0(new ArrayList<>());
                            DocRetrieval.Companion.setMedTags1(new ArrayList<>());
                            DocRetrieval.Companion.setMedTags2(new ArrayList<>());
                            DocRetrieval.Companion.setMedTags3(new ArrayList<>());
                            DocRetrieval.Companion.setMedTags4(new ArrayList<>());
                            DocRetrieval.Companion.setMedTags5(new ArrayList<>());
                            DocRetrieval.Companion.setMedTags6(new ArrayList<>());
                            DocRetrieval.Companion.setMedTags7(new ArrayList<>());
                            DocRetrieval.Companion.setMedTags8(new ArrayList<>());
                            DocRetrieval.Companion.setMedTags9(new ArrayList<>());

                            lectures = new ArrayList<>();
                            for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                SelectedDoc vid = qds.toObject(SelectedDoc.class);
                                lectures.add(vid);
                            }

                            for (SelectedDoc vid : lectures) {

                                if (vid.getDocMetaData().contains(DocSorting.getSubFields(10)[0])) {
                                    String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                    DocRetrieval.Companion.getMedicine0().add(vid);
                                    DocRetrieval.Companion.setMedLecAdapter0(new CloudVideosAdapter(lecturesList,
                                            playerView,
                                            app, context, c, DocRetrieval.Companion.getMedicine0(), app, a));
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getMedTags0().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getMedTags0().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getMedTags0(), DocRetrieval.Companion.getMedLecAdapter0(), null);
                                    subFieldAdt0.notifyDataSetChanged();
                                    DocRetrieval.Companion.getMedLecAdapter0().notifyDataSetChanged();
                                }
                                if (vid.getDocMetaData().contains(DocSorting.getSubFields(10)[1])) {
                                    String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                    DocRetrieval.Companion.getMedicine0().add(vid);
                                    DocRetrieval.Companion.setMedLecAdapter1(new CloudVideosAdapter(lecturesList,
                                            playerView,
                                            app, context, c, DocRetrieval.Companion.getMedicine1(), app, a));
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getMedTags0().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getMedTags0().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getMedTags1(), DocRetrieval.Companion.getMedLecAdapter1(), null);
                                    subFieldAdt1.notifyDataSetChanged();
                                    DocRetrieval.Companion.getMedLecAdapter0().notifyDataSetChanged();
                                }
                                if (vid.getDocMetaData().contains(DocSorting.getSubFields(10)[2])) {
                                    String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                    DocRetrieval.Companion.getMedicine2().add(vid);
                                    DocRetrieval.Companion.setMedLecAdapter2(new CloudVideosAdapter(lecturesList,
                                            playerView,
                                            app, context, c, DocRetrieval.Companion.getMedicine2(), app, a));
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getMedTags2().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getMedTags2().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getMedTags2(), DocRetrieval.Companion.getMedLecAdapter2(), null);
                                    subFieldAdt2.notifyDataSetChanged();
                                    DocRetrieval.Companion.getMedLecAdapter2().notifyDataSetChanged();
                                }
                                if (vid.getDocMetaData().contains(DocSorting.getSubFields(10)[3])) {
                                    String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                    DocRetrieval.Companion.getMedicine3().add(vid);
                                    DocRetrieval.Companion.setMedLecAdapter3(new CloudVideosAdapter(lecturesList,
                                            playerView,
                                            app, context, c, DocRetrieval.Companion.getMedicine3(), app, a));
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getMedTags3().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getMedTags3().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt3 = new SubFieldAdt(c, DocRetrieval.Companion.getMedTags3(), DocRetrieval.Companion.getMedLecAdapter3(), null);
                                    subFieldAdt3.notifyDataSetChanged();
                                    DocRetrieval.Companion.getMedLecAdapter3().notifyDataSetChanged();
                                }
                                if (vid.getDocMetaData().contains(DocSorting.getSubFields(10)[4])) {
                                    String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                    DocRetrieval.Companion.getMedicine4().add(vid);
                                    DocRetrieval.Companion.setMedLecAdapter4(new CloudVideosAdapter(lecturesList,
                                            playerView,
                                            app, context, c, DocRetrieval.Companion.getMedicine4(), app, a));
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getMedTags4().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getMedTags4().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt4 = new SubFieldAdt(c, DocRetrieval.Companion.getMedTags4(), DocRetrieval.Companion.getMedLecAdapter4(), null);
                                    subFieldAdt4.notifyDataSetChanged();
                                    DocRetrieval.Companion.getMedLecAdapter4().notifyDataSetChanged();
                                }
                                if (vid.getDocMetaData().contains(DocSorting.getSubFields(10)[5])) {
                                    String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                    DocRetrieval.Companion.getMedicine5().add(vid);
                                    DocRetrieval.Companion.setMedLecAdapter5(new CloudVideosAdapter(lecturesList,
                                            playerView,
                                            app, context, c, DocRetrieval.Companion.getMedicine5(), app, a));
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getMedTags5().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getMedTags5().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt5 = new SubFieldAdt(c, DocRetrieval.Companion.getMedTags5(), DocRetrieval.Companion.getMedLecAdapter5(), null);
                                    subFieldAdt5.notifyDataSetChanged();
                                    DocRetrieval.Companion.getMedLecAdapter5().notifyDataSetChanged();
                                }
                                if (vid.getDocMetaData().contains(DocSorting.getSubFields(10)[6])) {
                                    String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                    DocRetrieval.Companion.getMedicine6().add(vid);
                                    DocRetrieval.Companion.setMedLecAdapter6(new CloudVideosAdapter(lecturesList,
                                            playerView,
                                            app, context, c, DocRetrieval.Companion.getMedicine6(), app, a));
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getMedTags6().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getMedTags6().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt6 = new SubFieldAdt(c, DocRetrieval.Companion.getMedTags6(), DocRetrieval.Companion.getMedLecAdapter6(), null);
                                    subFieldAdt6.notifyDataSetChanged();
                                    DocRetrieval.Companion.getMedLecAdapter6().notifyDataSetChanged();
                                }
                                if (vid.getDocMetaData().contains(DocSorting.getSubFields(10)[7])) {
                                    String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                    DocRetrieval.Companion.getMedicine7().add(vid);
                                    DocRetrieval.Companion.setMedLecAdapter7(new CloudVideosAdapter(lecturesList,
                                            playerView,
                                            app, context, c, DocRetrieval.Companion.getMedicine7(), app, a));
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getMedTags7().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getMedTags7().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt7 = new SubFieldAdt(c, DocRetrieval.Companion.getMedTags7(), DocRetrieval.Companion.getMedLecAdapter7(), null);
                                    subFieldAdt7.notifyDataSetChanged();
                                    DocRetrieval.Companion.getMedLecAdapter7().notifyDataSetChanged();
                                }
                                if (vid.getDocMetaData().contains(DocSorting.getSubFields(10)[8])) {
                                    String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                    DocRetrieval.Companion.getMedicine8().add(vid);
                                    DocRetrieval.Companion.setMedLecAdapter8(new CloudVideosAdapter(lecturesList,
                                            playerView,
                                            app, context, c, DocRetrieval.Companion.getMedicine8(), app, a));
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getMedTags8().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getMedTags8().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt8 = new SubFieldAdt(c, DocRetrieval.Companion.getMedTags8(), DocRetrieval.Companion.getMedLecAdapter8(), null);
                                    subFieldAdt8.notifyDataSetChanged();
                                    DocRetrieval.Companion.getMedLecAdapter8().notifyDataSetChanged();
                                }
                                if (vid.getDocMetaData().contains(DocSorting.getSubFields(10)[9])) {
                                    String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                    DocRetrieval.Companion.getMedicine9().add(vid);
                                    DocRetrieval.Companion.setMedLecAdapter9(new CloudVideosAdapter(lecturesList,
                                            playerView,
                                            app, context, c, DocRetrieval.Companion.getMedicine9(), app, a));
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getMedTags9().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getMedTags9().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt9 = new SubFieldAdt(c, DocRetrieval.Companion.getMedTags9(), DocRetrieval.Companion.getMedLecAdapter9(), null);
                                    subFieldAdt9.notifyDataSetChanged();
                                    DocRetrieval.Companion.getMedLecAdapter9().notifyDataSetChanged();
                                }
                            }


                            holder.readProgress1.setVisibility(View.GONE);
                            DocRetrieval.Companion.getMedTagsArray().add(subFieldAdt0);
                            DocRetrieval.Companion.getMedTagsArray().add(subFieldAdt1);
                            DocRetrieval.Companion.getMedTagsArray().add(subFieldAdt2);
                            DocRetrieval.Companion.getMedTagsArray().add(subFieldAdt3);
                            DocRetrieval.Companion.getMedTagsArray().add(subFieldAdt4);
                            DocRetrieval.Companion.getMedTagsArray().add(subFieldAdt5);
                            DocRetrieval.Companion.getMedTagsArray().add(subFieldAdt6);
                            DocRetrieval.Companion.getMedTagsArray().add(subFieldAdt7);
                            DocRetrieval.Companion.getMedTagsArray().add(subFieldAdt8);
                            DocRetrieval.Companion.getMedTagsArray().add(subFieldAdt9);

                            DocRetrieval.Companion.getMedLecAdapters().add(DocRetrieval.Companion.getMedLecAdapter0());
                            DocRetrieval.Companion.getMedLecAdapters().add(DocRetrieval.Companion.getMedLecAdapter1());
                            DocRetrieval.Companion.getMedLecAdapters().add(DocRetrieval.Companion.getMedLecAdapter2());
                            DocRetrieval.Companion.getMedLecAdapters().add(DocRetrieval.Companion.getMedLecAdapter3());
                            DocRetrieval.Companion.getMedLecAdapters().add(DocRetrieval.Companion.getMedLecAdapter4());
                            DocRetrieval.Companion.getMedLecAdapters().add(DocRetrieval.Companion.getMedLecAdapter5());
                            DocRetrieval.Companion.getMedLecAdapters().add(DocRetrieval.Companion.getMedLecAdapter6());
                            DocRetrieval.Companion.getMedLecAdapters().add(DocRetrieval.Companion.getMedLecAdapter7());
                            DocRetrieval.Companion.getMedLecAdapters().add(DocRetrieval.Companion.getMedLecAdapter8());
                            DocRetrieval.Companion.getMedLecAdapters().add(DocRetrieval.Companion.getMedLecAdapter9());


                            try {
                                holder.lecDisplayRV.setAdapter(DocRetrieval.Companion.getMedLecAdapters().get(holder.getAbsoluteAdapterPosition()));
                                holder.lecTags.setAdapter(DocRetrieval.Companion.getMedTagsArray().get(holder.getAbsoluteAdapterPosition()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }
                break;
                case "Public Health": {
                    //PUBLIC HEALTH
                    lectureDbRef.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            DocRetrieval.Companion.setPublicHealth0(new ArrayList<>());
                            DocRetrieval.Companion.setPublicHealth1(new ArrayList<>());
                            DocRetrieval.Companion.setPublicHealth2(new ArrayList<>());

                            DocRetrieval.Companion.setPublicHealthTags0(new ArrayList<>());
                            DocRetrieval.Companion.setPublicHealthTags1(new ArrayList<>());
                            DocRetrieval.Companion.setPublicHealthTags2(new ArrayList<>());

                            lectures = new ArrayList<>();
                            for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                SelectedDoc vid = qds.toObject(SelectedDoc.class);
                                lectures.add(vid);
                            }

                            for (SelectedDoc vid : lectures) {
                                if (vid.getDocMetaData().contains(DocSorting.getSubFields(11)[0])) {
                                    String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                    DocRetrieval.Companion.getPublicHealth0().add(vid);
                                    DocRetrieval.Companion.setPublicHealthLecAdapter0(new CloudVideosAdapter(lecturesList,
                                            playerView,
                                            app, context, c, DocRetrieval.Companion.getPublicHealth0(), app, a));
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getPublicHealthTags0().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getPublicHealthTags0().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getPublicHealthTags0(), DocRetrieval.Companion.getPublicHealthLecAdapter1(), null);
                                    subFieldAdt0.notifyDataSetChanged();
                                    DocRetrieval.Companion.getPublicHealthLecAdapter0().notifyDataSetChanged();
                                }
                                if (vid.getDocMetaData().contains(DocSorting.getSubFields(11)[1])) {
                                    String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                    DocRetrieval.Companion.getPublicHealth1().add(vid);
                                    DocRetrieval.Companion.setPublicHealthLecAdapter1(new CloudVideosAdapter(lecturesList,
                                            playerView,
                                            app, context, c, DocRetrieval.Companion.getPublicHealth1(), app, a));
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getPublicHealthTags1().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getPublicHealthTags1().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getPublicHealthTags1(), DocRetrieval.Companion.getPublicHealthLecAdapter1(), null);
                                    subFieldAdt1.notifyDataSetChanged();
                                    DocRetrieval.Companion.getPublicHealthLecAdapter1().notifyDataSetChanged();
                                }
                                if (vid.getDocMetaData().contains(DocSorting.getSubFields(11)[2])) {
                                    String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                    DocRetrieval.Companion.getPublicHealth2().add(vid);
                                    DocRetrieval.Companion.setPublicHealthLecAdapter2(new CloudVideosAdapter(lecturesList,
                                            playerView,
                                            app, context, c, DocRetrieval.Companion.getPublicHealth2(), app, a));
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getPublicHealthTags2().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getPublicHealthTags2().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getPublicHealthTags2(), DocRetrieval.Companion.getPublicHealthLecAdapter1(), null);
                                    subFieldAdt2.notifyDataSetChanged();
                                    DocRetrieval.Companion.getPublicHealthLecAdapter2().notifyDataSetChanged();
                                }
                                if (vid.getDocMetaData().contains(DocSorting.getSubFields(11)[3])) {
                                    String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                    DocRetrieval.Companion.getPublicHealth3().add(vid);
                                    DocRetrieval.Companion.setPublicHealthLecAdapter3(new CloudVideosAdapter(lecturesList,
                                            playerView,
                                            app, context, c, DocRetrieval.Companion.getPublicHealth3(), app, a));
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getPublicHealthTags3().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getPublicHealthTags3().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt3 = new SubFieldAdt(c, DocRetrieval.Companion.getPublicHealthTags3(), DocRetrieval.Companion.getPublicHealthLecAdapter3(), null);
                                    subFieldAdt3.notifyDataSetChanged();
                                    DocRetrieval.Companion.getPublicHealthLecAdapter3().notifyDataSetChanged();
                                }
                            }

                            holder.readProgress1.setVisibility(View.GONE);
                            DocRetrieval.Companion.getPublicHealthTagsArray().add(subFieldAdt0);
                            DocRetrieval.Companion.getPublicHealthTagsArray().add(subFieldAdt1);
                            DocRetrieval.Companion.getPublicHealthTagsArray().add(subFieldAdt2);
                            DocRetrieval.Companion.getPublicHealthTagsArray().add(subFieldAdt3);

                            DocRetrieval.Companion.getPublicHealthLecAdapters().add(DocRetrieval.Companion.getPublicHealthLecAdapter0());
                            DocRetrieval.Companion.getPublicHealthLecAdapters().add(DocRetrieval.Companion.getPublicHealthLecAdapter1());
                            DocRetrieval.Companion.getPublicHealthLecAdapters().add(DocRetrieval.Companion.getPublicHealthLecAdapter2());
                            DocRetrieval.Companion.getPublicHealthLecAdapters().add(DocRetrieval.Companion.getPublicHealthLecAdapter3());


                            try {
                                holder.lecDisplayRV.setAdapter(DocRetrieval.Companion.getPublicHealthLecAdapters().get(holder.getAbsoluteAdapterPosition()));
                                holder.lecTags.setAdapter(DocRetrieval.Companion.getPublicHealthTagsArray().get(holder.getAbsoluteAdapterPosition()));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });


                }
                break;
                case "Pure & Applied Sciences": {
                    //PURE AND APPLIED SCI
                    lectureDbRef.collection(dfd).get().addOnSuccessListener(queryDocumentSnapshots -> {

                        DocRetrieval.Companion.setPureAppSci0(new ArrayList<>());
                        DocRetrieval.Companion.setPureAppSci1(new ArrayList<>());
                        DocRetrieval.Companion.setPureAppSci2(new ArrayList<>());
                        DocRetrieval.Companion.setPureAppSci3(new ArrayList<>());
                        DocRetrieval.Companion.setPureAppSci4(new ArrayList<>());
                        DocRetrieval.Companion.setPureAppSci5(new ArrayList<>());
                        DocRetrieval.Companion.setPureAppSci6(new ArrayList<>());
                        DocRetrieval.Companion.setPureAppSci7(new ArrayList<>());

                        DocRetrieval.Companion.setPureAppTags0(new ArrayList<>());
                        DocRetrieval.Companion.setPureAppTags1(new ArrayList<>());
                        DocRetrieval.Companion.setPureAppTags2(new ArrayList<>());
                        DocRetrieval.Companion.setPureAppTags3(new ArrayList<>());
                        DocRetrieval.Companion.setPureAppTags4(new ArrayList<>());
                        DocRetrieval.Companion.setPureAppTags5(new ArrayList<>());
                        DocRetrieval.Companion.setPureAppTags6(new ArrayList<>());
                        DocRetrieval.Companion.setPureAppTags7(new ArrayList<>());

                        lectures = new ArrayList<>();

                        lectures = new ArrayList<>();
                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                            SelectedDoc vid = qds.toObject(SelectedDoc.class);
                            lectures.add(vid);
                        }


                        for (SelectedDoc vid : lectures) {

                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(12)[0])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getPureAppSci0().add(vid);
                                DocRetrieval.Companion.setDocLecAdapter0(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getPureAppSci0(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getPureAppTags0().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getPureAppTags0().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getPureAppTags0(), DocRetrieval.Companion.getDocLecAdapter0(), null);
                                subFieldAdt0.notifyDataSetChanged();
                                DocRetrieval.Companion.getDocLecAdapter0().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(12)[1])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getPureAppSci1().add(vid);
                                DocRetrieval.Companion.setDocLecAdapter1(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getPureAppSci1(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getPureAppTags1().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getPureAppTags1().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getPureAppTags1(), DocRetrieval.Companion.getDocLecAdapter1(), null);
                                subFieldAdt1.notifyDataSetChanged();
                                DocRetrieval.Companion.getDocLecAdapter1().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(12)[2])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getPureAppSci2().add(vid);
                                DocRetrieval.Companion.setDocLecAdapter2(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getPureAppSci2(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getPureAppTags2().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getPureAppTags2().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getPureAppTags2(), DocRetrieval.Companion.getDocLecAdapter2(), null);
                                subFieldAdt2.notifyDataSetChanged();
                                DocRetrieval.Companion.getDocLecAdapter2().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(12)[3])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getPureAppSci3().add(vid);
                                DocRetrieval.Companion.setDocLecAdapter3(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getPureAppSci3(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getPureAppTags3().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getPureAppTags3().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt3 = new SubFieldAdt(c, DocRetrieval.Companion.getPureAppTags3(), DocRetrieval.Companion.getDocLecAdapter3(), null);
                                subFieldAdt3.notifyDataSetChanged();
                                DocRetrieval.Companion.getDocLecAdapter3().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(12)[4])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getPureAppSci4().add(vid);
                                DocRetrieval.Companion.setDocLecAdapter4(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getPureAppSci4(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getPureAppTags4().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getPureAppTags4().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt4 = new SubFieldAdt(c, DocRetrieval.Companion.getPureAppTags4(), DocRetrieval.Companion.getDocLecAdapter4(), null);
                                subFieldAdt4.notifyDataSetChanged();
                                DocRetrieval.Companion.getDocLecAdapter4().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(12)[5])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getPureAppSci5().add(vid);
                                DocRetrieval.Companion.setDocLecAdapter5(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getPureAppSci5(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getPureAppTags5().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getPureAppTags5().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt5 = new SubFieldAdt(c, DocRetrieval.Companion.getPureAppTags5(), DocRetrieval.Companion.getDocLecAdapter5(), null);
                                subFieldAdt5.notifyDataSetChanged();
                                DocRetrieval.Companion.getDocLecAdapter5().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(12)[6])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getPureAppSci6().add(vid);
                                DocRetrieval.Companion.setDocLecAdapter6(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getPureAppSci6(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getPureAppTags6().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getPureAppTags6().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt6 = new SubFieldAdt(c, DocRetrieval.Companion.getPureAppTags6(), DocRetrieval.Companion.getDocLecAdapter0(), null);
                                subFieldAdt6.notifyDataSetChanged();
                                DocRetrieval.Companion.getDocLecAdapter6().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(12)[7])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getPureAppSci7().add(vid);
                                DocRetrieval.Companion.setDocLecAdapter7(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getPureAppSci7(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getPureAppTags7().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getPureAppTags7().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt7 = new SubFieldAdt(c, DocRetrieval.Companion.getPureAppTags7(), DocRetrieval.Companion.getDocLecAdapter7(), null);
                                subFieldAdt7.notifyDataSetChanged();
                                DocRetrieval.Companion.getDocLecAdapter7().notifyDataSetChanged();
                            }
                        }

                        holder.readProgress1.setVisibility(View.GONE);
                        DocRetrieval.Companion.getPureAppTagsArray().add(subFieldAdt0);
                        DocRetrieval.Companion.getPureAppTagsArray().add(subFieldAdt1);
                        DocRetrieval.Companion.getPureAppTagsArray().add(subFieldAdt2);
                        DocRetrieval.Companion.getPureAppTagsArray().add(subFieldAdt3);
                        DocRetrieval.Companion.getPureAppTagsArray().add(subFieldAdt4);
                        DocRetrieval.Companion.getPureAppTagsArray().add(subFieldAdt5);
                        DocRetrieval.Companion.getPureAppTagsArray().add(subFieldAdt6);
                        DocRetrieval.Companion.getPureAppTagsArray().add(subFieldAdt7);

                        DocRetrieval.Companion.getPureLecAdapters().add(DocRetrieval.Companion.getDocLecAdapter0());
                        DocRetrieval.Companion.getPureLecAdapters().add(DocRetrieval.Companion.getDocLecAdapter1());
                        DocRetrieval.Companion.getPureLecAdapters().add(DocRetrieval.Companion.getDocLecAdapter2());
                        DocRetrieval.Companion.getPureLecAdapters().add(DocRetrieval.Companion.getDocLecAdapter3());
                        DocRetrieval.Companion.getPureLecAdapters().add(DocRetrieval.Companion.getDocLecAdapter4());
                        DocRetrieval.Companion.getPureLecAdapters().add(DocRetrieval.Companion.getDocLecAdapter5());
                        DocRetrieval.Companion.getPureLecAdapters().add(DocRetrieval.Companion.getDocLecAdapter6());
                        DocRetrieval.Companion.getPureLecAdapters().add(DocRetrieval.Companion.getDocLecAdapter7());


                        try {
                            holder.lecDisplayRV.setAdapter(DocRetrieval.Companion.getPureLecAdapters().get(holder.getAbsoluteAdapterPosition()));
                            holder.lecTags.setAdapter(DocRetrieval.Companion.getPureAppTagsArray().get(holder.getAbsoluteAdapterPosition()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }
                break;
                case "Visual & Performing Art": {
                    //VISUAL AND PERFORMING ARTS
                    lectureDbRef.collection(dfd).get().addOnSuccessListener(queryDocumentSnapshots -> {

                        DocRetrieval.Companion.setVisualArt0(new ArrayList<>());
                        DocRetrieval.Companion.setVisualArt1(new ArrayList<>());
                        DocRetrieval.Companion.setVisualArt2(new ArrayList<>());

                        DocRetrieval.Companion.setVisualArtTags0(new ArrayList<>());
                        DocRetrieval.Companion.setVisualArtTags1(new ArrayList<>());
                        DocRetrieval.Companion.setVisualArtTags2(new ArrayList<>());

                        lectures = new ArrayList<>();
                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                            SelectedDoc vid = qds.toObject(SelectedDoc.class);
                            lectures.add(vid);
                        }

                        for (SelectedDoc vid : lectures) {
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(13)[0])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getVisualArt0().add(vid);
                                DocRetrieval.Companion.setVisualLecAdapter0(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getVisualArt0(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getVisualArtTags0().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getVisualArtTags0().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getVisualArtTags0(), DocRetrieval.Companion.getVisualLecAdapter0(), null);
                                subFieldAdt0.notifyDataSetChanged();
                                DocRetrieval.Companion.getVisualLecAdapter0().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(13)[1])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getVisualArt1().add(vid);
                                DocRetrieval.Companion.setVisualLecAdapter1(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getVisualArt1(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getVisualArtTags1().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getVisualArtTags1().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getVisualArtTags1(), DocRetrieval.Companion.getVisualLecAdapter1(), null);
                                subFieldAdt1.notifyDataSetChanged();
                                DocRetrieval.Companion.getVisualLecAdapter1().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(13)[2])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getVisualArt2().add(vid);
                                DocRetrieval.Companion.setVisualLecAdapter2(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getVisualArt2(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getVisualArtTags2().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getVisualArtTags2().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getVisualArtTags2(), DocRetrieval.Companion.getVisualLecAdapter2(), null);
                                subFieldAdt2.notifyDataSetChanged();
                                DocRetrieval.Companion.getVisualLecAdapter2().notifyDataSetChanged();
                            }
                        }

                        holder.readProgress1.setVisibility(View.GONE);
                        DocRetrieval.Companion.getVisualArtTagsArray().add(subFieldAdt0);
                        DocRetrieval.Companion.getVisualArtTagsArray().add(subFieldAdt1);
                        DocRetrieval.Companion.getVisualArtTagsArray().add(subFieldAdt2);


                        DocRetrieval.Companion.getVisualLecAdapters().add(DocRetrieval.Companion.getVisualLecAdapter0());
                        DocRetrieval.Companion.getVisualLecAdapters().add(DocRetrieval.Companion.getVisualLecAdapter1());
                        DocRetrieval.Companion.getVisualLecAdapters().add(DocRetrieval.Companion.getVisualLecAdapter2());


                        try {
                            holder.lecDisplayRV.setAdapter(DocRetrieval.Companion.getVisualLecAdapters().get(holder.getAbsoluteAdapterPosition()));
                            holder.lecTags.setAdapter(DocRetrieval.Companion.getVisualArtTagsArray().get(holder.getAbsoluteAdapterPosition()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });


                }
                break;
                case "Confucius Institute": {
                    //PHILOSOPHY
                    lectureDbRef.collection(dfd).get().addOnSuccessListener(queryDocumentSnapshots -> {
                        lectures = new ArrayList<>();
                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                            SelectedDoc vid = qds.toObject(SelectedDoc.class);
                            DocRetrieval.Companion.getConfucius().add(vid);
                        }

                        DocRetrieval.Companion.setConfuciusLecAdapter(new CloudVideosAdapter(lecturesList,
                                playerView,
                                app, context, c, DocRetrieval.Companion.getConfucius(), app, a));


                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });


                }
                break;
                case "Peace & Security Studies": {
                    //PEACE SECURITY
                    lectureDbRef.collection(dfd).get().addOnSuccessListener(queryDocumentSnapshots -> {
                        lectures = new ArrayList<>();
                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                            SelectedDoc vid = qds.toObject(SelectedDoc.class);
                            DocRetrieval.Companion.getPeaceSecurityLectures().add(vid);
                        }

                        DocRetrieval.Companion.setPeaceLecAdapter(new CloudVideosAdapter(lecturesList,
                                playerView,
                                app, context, c, DocRetrieval.Companion.getPeaceSecurityLectures(), app, a));

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }
                break;
                case "Creative Arts, Film & Media Studies": {
                    //CREATIVE ARTS
                    lectureDbRef.collection(dfd).get().addOnSuccessListener(queryDocumentSnapshots -> {
                        DocRetrieval.Companion.setCreativeArt0(new ArrayList<>());
                        DocRetrieval.Companion.setCreativeArt1(new ArrayList<>());

                        DocRetrieval.Companion.setCreativeArtTags0(new ArrayList<>());
                        DocRetrieval.Companion.setCreativeArtTags1(new ArrayList<>());

                        lectures = new ArrayList<>();
                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                            SelectedDoc vid = qds.toObject(SelectedDoc.class);
                            lectures.add(vid);
                        }

                        for (SelectedDoc vid : lectures) {

                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(16)[0])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getCreativeArt0().add(vid);
                                DocRetrieval.Companion.setCreativeArtAdapter0(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getCreativeArt0()));

                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getCreativeArtTags0().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getCreativeArtTags0().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getCreativeArtTags0(), DocRetrieval.Companion.getCreativeArtLecAdapter0(), null);
                                subFieldAdt0.notifyDataSetChanged();
                                DocRetrieval.Companion.getCreativeArtLecAdapter0().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(16)[1])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getCreativeArt1().add(vid);
                                DocRetrieval.Companion.setCreativeArtAdapter1(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getCreativeArt1()));

                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getCreativeArtTags1().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getCreativeArtTags1().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getCreativeArtTags1(), DocRetrieval.Companion.getCreativeArtLecAdapter1(), null);
                                subFieldAdt1.notifyDataSetChanged();
                                DocRetrieval.Companion.getCreativeArtLecAdapter1().notifyDataSetChanged();
                            }
                        }

                        holder.readProgress1.setVisibility(View.GONE);
                        DocRetrieval.Companion.getCreativeArtTagsArray().add(subFieldAdt0);
                        DocRetrieval.Companion.getCreativeArtTagsArray().add(subFieldAdt1);

                        DocRetrieval.Companion.getCreativeArtAdapters().add(DocRetrieval.Companion.getCreativeArtAdapter0());
                        DocRetrieval.Companion.getCreativeArtAdapters().add(DocRetrieval.Companion.getCreativeArtAdapter1());


                        try {
                            holder.lecDisplayRV.setAdapter(DocRetrieval.Companion.getCreativeArtAdapters().get(holder.getAbsoluteAdapterPosition()));
                            holder.lecTags.setAdapter(DocRetrieval.Companion.getCreativeArtTagsArray().get(holder.getAbsoluteAdapterPosition()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });


                }
                break;
                case "Architecture": {
                    //ARCHITECTURE
                    lectureDbRef.collection(dfd).get().addOnSuccessListener(queryDocumentSnapshots -> {

                        DocRetrieval.Companion.setArchitecture0(new ArrayList<>());
                        DocRetrieval.Companion.setArchitecture1(new ArrayList<>());
                        DocRetrieval.Companion.setArchitecture2(new ArrayList<>());

                        DocRetrieval.Companion.setArchitectureTags0(new ArrayList<>());
                        DocRetrieval.Companion.setArchitectureTags1(new ArrayList<>());
                        DocRetrieval.Companion.setArchitectureTags2(new ArrayList<>());

                        lectures = new ArrayList<>();
                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                            SelectedDoc vid = qds.toObject(SelectedDoc.class);
                            lectures.add(vid);
                        }

                        for (SelectedDoc vid : lectures) {
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(11)[0])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getArchitecture0().add(vid);
                                DocRetrieval.Companion.setArchitectureLecAdapter0(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getArchitecture0(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getArchitectureTags0().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getArchitectureTags0().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getArchitectureTags0(), DocRetrieval.Companion.getArchitectureLecAdapter1(), null);
                                subFieldAdt0.notifyDataSetChanged();
                                DocRetrieval.Companion.getArchitectureLecAdapter0().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(11)[1])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getArchitecture1().add(vid);
                                DocRetrieval.Companion.setArchitectureLecAdapter1(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getArchitecture1(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getArchitectureTags1().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getArchitectureTags1().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getArchitectureTags1(), DocRetrieval.Companion.getArchitectureLecAdapter1(), null);
                                subFieldAdt1.notifyDataSetChanged();
                                DocRetrieval.Companion.getArchitectureLecAdapter1().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(11)[2])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getArchitecture2().add(vid);
                                DocRetrieval.Companion.setArchitectureLecAdapter2(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getArchitecture2(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getArchitectureTags2().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getArchitectureTags2().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getArchitectureTags2(), DocRetrieval.Companion.getArchitectureLecAdapter1(), null);
                                subFieldAdt2.notifyDataSetChanged();
                                DocRetrieval.Companion.getArchitectureLecAdapter2().notifyDataSetChanged();
                            }
                            if (vid.getDocMetaData().contains(DocSorting.getSubFields(11)[3])) {
                                String tags = vid.getDocMetaData().split("_-_")[4].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getArchitecture3().add(vid);
                                DocRetrieval.Companion.setArchitectureLecAdapter3(new CloudVideosAdapter(lecturesList,
                                        playerView,
                                        app, context, c, DocRetrieval.Companion.getArchitecture3(), app, a));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, vid.getDocMetaData().split("_-_")[4].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getArchitectureTags3().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getArchitectureTags3().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt3 = new SubFieldAdt(c, DocRetrieval.Companion.getArchitectureTags3(), DocRetrieval.Companion.getArchitectureLecAdapter3(), null);
                                subFieldAdt3.notifyDataSetChanged();
                                DocRetrieval.Companion.getArchitectureLecAdapter3().notifyDataSetChanged();
                            }
                        }

                        holder.readProgress1.setVisibility(View.GONE);
                        DocRetrieval.Companion.getArchitectureTagsArray().add(subFieldAdt0);
                        DocRetrieval.Companion.getArchitectureTagsArray().add(subFieldAdt1);
                        DocRetrieval.Companion.getArchitectureTagsArray().add(subFieldAdt2);
                        DocRetrieval.Companion.getArchitectureTagsArray().add(subFieldAdt3);

                        DocRetrieval.Companion.getArchitectureLecAdapters().add(DocRetrieval.Companion.getArchitectureLecAdapter0());
                        DocRetrieval.Companion.getArchitectureLecAdapters().add(DocRetrieval.Companion.getArchitectureLecAdapter1());
                        DocRetrieval.Companion.getArchitectureLecAdapters().add(DocRetrieval.Companion.getArchitectureLecAdapter2());
                        DocRetrieval.Companion.getArchitectureLecAdapters().add(DocRetrieval.Companion.getArchitectureLecAdapter3());


                        try {
                            holder.lecDisplayRV.setAdapter(DocRetrieval.Companion.getArchitectureLecAdapters().get(holder.getAbsoluteAdapterPosition()));
                            holder.lecTags.setAdapter(DocRetrieval.Companion.getArchitectureTagsArray().get(holder.getAbsoluteAdapterPosition()));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }
                break;

                default:
                    throw new IllegalStateException("Unexpected value: " + dfd);
            }


        } else
            if (flag.equals("SchoolDptDocuments")) {
            holder.addDocBtn.bringToFront();
            holder.addDocBtn.setOnClickListener(v -> {
                Intent intent2 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent2.addCategory(Intent.CATEGORY_OPENABLE);
                intent2.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent2.setType("application/*");
                FilingSystem.Companion.setDept(deptTitle);
                ((Activity)c).startActivityForResult(intent2, STORAGE_ACCESS_CODE);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                    Intent intent2 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                    intent2.addCategory(Intent.CATEGORY_OPENABLE);
//                    intent2.setType("application/*");
//                    FilingSystem.Companion.setDept(deptTitle);
//                    ((Activity)c).startActivityForResult(intent2, STORAGE_ACCESS_CODE);
//                }else
//                {
//                    Intent intent = new Intent(c, SourceDocList.class);
//                    intent.putExtra("addDoc", "addDoc");
//                    intent.putExtra("SchoolName", dfd);
//                    intent.putExtra("Department", deptTitle);
//                    c.startActivity(intent);
//                    Toast.makeText(c, dfd + ":\n" + deptTitle, Toast.LENGTH_SHORT).show();                }

            });

            switch (dfd) {

                case "Agriculture & Enterprise Development": {

                    departmentsArray = DocSorting.getSubFields(0);
                    schoolDepartments.addAll(Arrays.asList(departmentsArray));


//                for (String department : departmentsArray){
//                    publishedDocs.collection(dfd).whereEqualTo("subField", department)
//                            .orderBy("downloadCount")
//                            .limit(10)
//                            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                        @Override
//                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                            for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
//                                SelectedDoc document = qds.toObject(SelectedDoc.class);
//                                documents.add(document);
//                            }
//                            documentsList = new ArrayList<>(documents);
//                            Toast.makeText(context, "10 query complete", Toast.LENGTH_SHORT).show();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//
//                        }
//                    });
//                }


                    publishedDocs.collection(dfd).get().addOnSuccessListener(queryDocumentSnapshots -> {
                        DocRetrieval.Companion.setAgrTags0(new ArrayList<>());
                        DocRetrieval.Companion.setAgrTags1(new ArrayList<>());
                        DocRetrieval.Companion.setAgrTags2(new ArrayList<>());


                        DocRetrieval.Companion.setAgr0(new ArrayList<>());
                        DocRetrieval.Companion.setAgr1(new ArrayList<>());
                        DocRetrieval.Companion.setAgr2(new ArrayList<>());

                        documents = new ArrayList<>();
                        //Save the school object
                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                            SelectedDoc document = qds.toObject(SelectedDoc.class);
                            documents.add(document);
                        }

                        documentsList = new ArrayList<>(documents);


                        for (SelectedDoc document : documents) {
                            if (document.getDocMetaData().contains(DocSorting.getSubFields(0)[0])) {
                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getAgr0().add(document);
                                DocRetrieval.Companion.setAgrAdapter0(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getAgr0()));

                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getAgrTags0().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getAgrTags0().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getAgrTags0(), null, DocRetrieval.Companion.getAgrAdapter0());
                                subFieldAdt0.notifyDataSetChanged();
                                DocRetrieval.Companion.getAgrAdapter0().notifyDataSetChanged();
                            }

                            if (document.getDocMetaData().contains(DocSorting.getSubFields(0)[1])) {
                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getAgr1().add(document);
                                DocRetrieval.Companion.setAgrAdapter1(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getAgr1()));

                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getAgrTags1().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getAgrTags1().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getAgrTags1(), null, DocRetrieval.Companion.getAgrAdapter1());
                                subFieldAdt1.notifyDataSetChanged();
                                DocRetrieval.Companion.getAgrAdapter1().notifyDataSetChanged();
                            }

                            if (document.getDocMetaData().contains(DocSorting.getSubFields(0)[2])) {
                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                DocRetrieval.Companion.getAgr2().add(document);
                                DocRetrieval.Companion.setAgrAdapter2(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getAgr2()));
                                if (tags.split(",").length > 0) {
                                    ArrayList<String> tagsProcessor = new ArrayList<>();
                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                    for (int i = 0; i < tagsProcessor.size(); i++) {
                                        if (!(DocRetrieval.Companion.getAgrTags2().contains(tagsProcessor.get(i)))) {
                                            DocRetrieval.Companion.getAgrTags2().add(tagsProcessor.get(i));
                                        }
                                    }
                                }
                                subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getAgrTags0(), null, DocRetrieval.Companion.getAgrAdapter2());
                                subFieldAdt2.notifyDataSetChanged();
                                DocRetrieval.Companion.getAgrAdapter2().notifyDataSetChanged();
                            }
                        }

                        holder.readProgress.setVisibility(View.INVISIBLE);
                        DocRetrieval.Companion.getAgrTagsArray().add(subFieldAdt0);
                        DocRetrieval.Companion.getAgrTagsArray().add(subFieldAdt1);
                        DocRetrieval.Companion.getAgrTagsArray().add(subFieldAdt2);

                        DocRetrieval.Companion.getAgrAdapters().add(DocRetrieval.Companion.getAgrAdapter0());
                        DocRetrieval.Companion.getAgrAdapters().add(DocRetrieval.Companion.getAgrAdapter1());
                        DocRetrieval.Companion.getAgrAdapters().add(DocRetrieval.Companion.getAgrAdapter2());


                        try {
                            holder.docDisplayRV.setAdapter(DocRetrieval.Companion.getAgrAdapters().get(holder.getAbsoluteAdapterPosition()));
                            holder.docTags.setAdapter(DocRetrieval.Companion.getAgrTagsArray().get(holder.getAbsoluteAdapterPosition()));
                            holder.departmentIndicator.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    try {
                                        holder.docDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getAgrAdapters().get(holder.getAbsoluteAdapterPosition()).docPosition++);
                                        holder.docTags.smoothScrollToPosition(DocRetrieval.Companion.getAgrTagsArray().get(holder.getAbsoluteAdapterPosition()).getTagPosition() + 1);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).addOnFailureListener(e -> {

                    });

                }
                break;
                case "Applied Human Sciences": {
                    departmentsArray = DocSorting.getSubFields(1);
                    schoolDepartments.addAll(Arrays.asList(departmentsArray));
                    publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            DocRetrieval.Companion.setAppHumanSciTags0(new ArrayList<>());
                            DocRetrieval.Companion.setAppHumanSciTags1(new ArrayList<>());
                            DocRetrieval.Companion.setAppHumanSciTags2(new ArrayList<>());
                            DocRetrieval.Companion.setAppHumanSciTags3(new ArrayList<>());
                            DocRetrieval.Companion.setAppHumanSciTags4(new ArrayList<>());

                            DocRetrieval.Companion.setAppHumanSci0(new ArrayList<>());
                            DocRetrieval.Companion.setAppHumanSci1(new ArrayList<>());
                            DocRetrieval.Companion.setAppHumanSci2(new ArrayList<>());
                            DocRetrieval.Companion.setAppHumanSci3(new ArrayList<>());
                            DocRetrieval.Companion.setAppHumanSci4(new ArrayList<>());

                            subFieldAdts1 = new ArrayList<>();
                            documents = new ArrayList<>();
                            for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                SelectedDoc document = qds.toObject(SelectedDoc.class);
                                documents.add(document);
                            }


                            for (SelectedDoc document : documents) {
                                if (document.getDocMetaData().contains(DocSorting.getSubFields(1)[0])) {
                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    DocRetrieval.Companion.getAppHumanSci0().add(document);
                                    DocRetrieval.Companion.setAppHumanAdapter0(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getAppHumanSci0()));

                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getAppHumanSciTags0().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getAppHumanSciTags0().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    DocRetrieval.Companion.getAppHumanAdapter0().notifyDataSetChanged();
                                    subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getAppHumanSciTags0(), null, DocRetrieval.Companion.getAppHumanAdapter0());
                                    subFieldAdt0.notifyDataSetChanged();
                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(1)[1])) {
                                    DocRetrieval.Companion.getAppHumanSci1().add(document);
                                    DocRetrieval.Companion.setAppHumanAdapter1(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getAppHumanSci1()));

                                    if (!(document.getDocMetaData().split("_-_")[5].split(",").length == 0)) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getAppHumanSciTags1().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getAppHumanSciTags1().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    DocRetrieval.Companion.getAppHumanAdapter1().notifyDataSetChanged();
                                    subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getAppHumanSciTags1(), null, DocRetrieval.Companion.getAppHumanAdapter1());
                                    subFieldAdt1.notifyDataSetChanged();
                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(1)[2])) {
                                    DocRetrieval.Companion.getAppHumanSci2().add(document);
                                    DocRetrieval.Companion.setAppHumanAdapter2(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getAppHumanSci2()));

                                    if (!(document.getDocMetaData().split("_-_")[5].split(",").length == 0)) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getAppHumanSciTags2().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getAppHumanSciTags2().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    DocRetrieval.Companion.getAppHumanAdapter2().notifyDataSetChanged();
                                    subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getAppHumanSciTags2(), null, DocRetrieval.Companion.getAppHumanAdapter2());
                                    subFieldAdt2.notifyDataSetChanged();
                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(1)[3])) {
                                    DocRetrieval.Companion.getAppHumanSci3().add(document);
                                    DocRetrieval.Companion.setAppHumanAdapter3(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getAppHumanSci3()));

                                    if (!(document.getDocMetaData().split("_-_")[5].split(",").length == 0)) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getAppHumanSciTags3().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getAppHumanSciTags3().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    DocRetrieval.Companion.getAppHumanAdapter3().notifyDataSetChanged();
                                    subFieldAdt3 = new SubFieldAdt(c, DocRetrieval.Companion.getAppHumanSciTags3(), null, DocRetrieval.Companion.getAppHumanAdapter3());
                                    subFieldAdt3.notifyDataSetChanged();
                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(1)[4])) {
                                    DocRetrieval.Companion.getAppHumanSci4().add(document);
                                    DocRetrieval.Companion.setAppHumanAdapter4(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getAppHumanSci4()));

                                    if (!(document.getDocMetaData().split("_-_")[5].split(",").length == 0)) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getAppHumanSciTags4().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getAppHumanSciTags4().add(tagsProcessor.get(i).replace("[", "").replace("]", ""));
                                            }
                                        }
                                    }
                                    DocRetrieval.Companion.getAppHumanAdapter4().notifyDataSetChanged();
                                    subFieldAdt4 = new SubFieldAdt(c, DocRetrieval.Companion.getAppHumanSciTags4(), null, DocRetrieval.Companion.getAppHumanAdapter4());
                                    subFieldAdt4.notifyDataSetChanged();
                                }
                            }
                            DocRetrieval.Companion.getAppHumanAdapters().add(DocRetrieval.Companion.getAppHumanAdapter0());
                            DocRetrieval.Companion.getAppHumanAdapters().add(DocRetrieval.Companion.getAppHumanAdapter1());
                            DocRetrieval.Companion.getAppHumanAdapters().add(DocRetrieval.Companion.getAppHumanAdapter2());
                            DocRetrieval.Companion.getAppHumanAdapters().add(DocRetrieval.Companion.getAppHumanAdapter3());
                            DocRetrieval.Companion.getAppHumanAdapters().add(DocRetrieval.Companion.getAppHumanAdapter4());

                            DocRetrieval.Companion.getAppHumanSciTagsArray().add(subFieldAdt0);
                            DocRetrieval.Companion.getAppHumanSciTagsArray().add(subFieldAdt1);
                            DocRetrieval.Companion.getAppHumanSciTagsArray().add(subFieldAdt2);
                            DocRetrieval.Companion.getAppHumanSciTagsArray().add(subFieldAdt3);
                            DocRetrieval.Companion.getAppHumanSciTagsArray().add(subFieldAdt4);


                            holder.readProgress.setVisibility(View.INVISIBLE);

                            try {
                                holder.docDisplayRV.setAdapter(DocRetrieval.Companion.getAppHumanAdapters().get(holder.getAbsoluteAdapterPosition()));
                                holder.docTags.setAdapter(DocRetrieval.Companion.getAppHumanSciTagsArray().get(holder.getAbsoluteAdapterPosition()));

                                holder.departmentIndicator.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        try {
                                            holder.docDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getAppHumanAdapters().get(holder.getAbsoluteAdapterPosition()).docPosition++);
                                            holder.docTags.smoothScrollToPosition(DocRetrieval.Companion.getAppHumanSciTagsArray().get(holder.getAbsoluteAdapterPosition()).getTagPosition() + 1);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }
                break;
                case "Business": {

                    departmentsArray = DocSorting.getSubFields(2);
                    schoolDepartments.addAll(Arrays.asList(departmentsArray));
                    publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            DocRetrieval.Companion.setBizTags0(new ArrayList<>());
                            DocRetrieval.Companion.setBizTags1(new ArrayList<>());
                            DocRetrieval.Companion.setBizTags2(new ArrayList<>());

                            DocRetrieval.Companion.setBiz0(new ArrayList<>());
                            DocRetrieval.Companion.setBiz1(new ArrayList<>());
                            DocRetrieval.Companion.setBiz2(new ArrayList<>());

                            documents = new ArrayList<>();
                            for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                SelectedDoc document = qds.toObject(SelectedDoc.class);
                                documents.add(document);

                            }

                            for (SelectedDoc document : documents) {

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(2)[0])) {

                                    DocRetrieval.Companion.getBiz0().add(document);
                                    DocRetrieval.Companion.setBizAdapter0(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getBiz0()));
                                    DocRetrieval.Companion.getBizAdapter0().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getBizTags0().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getBizTags0().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getBizTags0(), null, DocRetrieval.Companion.getBizAdapter0());
                                    subFieldAdt0.notifyDataSetChanged();
                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(2)[1])) {
                                    DocRetrieval.Companion.getBiz1().add(document);
                                    DocRetrieval.Companion.setBizAdapter1(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getBiz1()));
                                    DocRetrieval.Companion.getBizAdapter1().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getBizTags1().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getBizTags1().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getBizTags1(), null, DocRetrieval.Companion.getBizAdapter1());
                                    subFieldAdt1.notifyDataSetChanged();
                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(2)[2])) {
                                    DocRetrieval.Companion.getBiz2().add(document);
                                    DocRetrieval.Companion.setBizAdapter2(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getBiz2()));
                                    DocRetrieval.Companion.getBizAdapter2().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getBizTags2().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getBizTags2().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getBizTags2(), null, DocRetrieval.Companion.getBizAdapter2());
                                    subFieldAdt2.notifyDataSetChanged();
                                }
                            }

                            holder.readProgress.setVisibility(View.INVISIBLE);

                            DocRetrieval.Companion.getBizTagsArray().add(subFieldAdt0);
                            DocRetrieval.Companion.getBizTagsArray().add(subFieldAdt1);
                            DocRetrieval.Companion.getBizTagsArray().add(subFieldAdt2);


                            DocRetrieval.Companion.getBizAdapters().add(DocRetrieval.Companion.getBizAdapter0());
                            DocRetrieval.Companion.getBizAdapters().add(DocRetrieval.Companion.getBizAdapter1());
                            DocRetrieval.Companion.getBizAdapters().add(DocRetrieval.Companion.getBizAdapter2());


                            try {

                                holder.docDisplayRV.setAdapter(DocRetrieval.Companion.getBizAdapters().get(holder.getAbsoluteAdapterPosition()));
                                holder.docTags.setAdapter(DocRetrieval.Companion.getBizTagsArray().get(holder.getAbsoluteAdapterPosition()));

                                holder.departmentIndicator.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        try {
                                            holder.docDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getBizAdapters().get(holder.getAbsoluteAdapterPosition()).docPosition++);
                                            holder.docTags.smoothScrollToPosition(DocRetrieval.Companion.getBizTagsArray().get(holder.getAbsoluteAdapterPosition()).getTagPosition() + 1);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }
                break;
                case "Economics": {

                    departmentsArray = DocSorting.getSubFields(3);
                    schoolDepartments.addAll(Arrays.asList(departmentsArray));
                    publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            documents = new ArrayList<>();
                            for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                SelectedDoc document = qds.toObject(SelectedDoc.class);
                                documents.add(document);

                            }

                            DocRetrieval.Companion.setEconTags0(new ArrayList<>());
                            DocRetrieval.Companion.setEconTags1(new ArrayList<>());
                            DocRetrieval.Companion.setEconTags2(new ArrayList<>());

                            DocRetrieval.Companion.setEcon0(new ArrayList<>());
                            DocRetrieval.Companion.setEcon1(new ArrayList<>());
                            DocRetrieval.Companion.setEcon2(new ArrayList<>());

                            for (SelectedDoc document : documents) {

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(3)[0])) {
                                    DocRetrieval.Companion.getEcon0().add(document);
                                    DocRetrieval.Companion.setEconAdapter0(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEcon0()));
                                    DocRetrieval.Companion.getEconAdapter0().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getEconTags0().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getEconTags0().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getEconTags0(), null, DocRetrieval.Companion.getEconAdapter0());
                                    subFieldAdt0.notifyDataSetChanged();

                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(3)[1])) {
                                    DocRetrieval.Companion.getEcon1().add(document);
                                    DocRetrieval.Companion.setEconAdapter1(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEcon1()));
                                    DocRetrieval.Companion.getEconAdapter1().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getEconTags1().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getEconTags1().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getEconTags1(), null, DocRetrieval.Companion.getEconAdapter1());
                                    subFieldAdt1.notifyDataSetChanged();

                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(3)[2])) {
                                    DocRetrieval.Companion.getEcon2().add(document);
                                    DocRetrieval.Companion.setEconAdapter2(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEcon2()));
                                    DocRetrieval.Companion.getEconAdapter2().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getEconTags2().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getEconTags2().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getEconTags2(), null, DocRetrieval.Companion.getEconAdapter2());
                                    subFieldAdt2.notifyDataSetChanged();
                                }
                            }
                            holder.readProgress.setVisibility(View.INVISIBLE);
                            DocRetrieval.Companion.getEconTagsArray().add(subFieldAdt0);
                            DocRetrieval.Companion.getEconTagsArray().add(subFieldAdt1);
                            DocRetrieval.Companion.getEconTagsArray().add(subFieldAdt2);

                            DocRetrieval.Companion.getEconAdapters().add(DocRetrieval.Companion.getEconAdapter0());
                            DocRetrieval.Companion.getEconAdapters().add(DocRetrieval.Companion.getEconAdapter1());
                            DocRetrieval.Companion.getEconAdapters().add(DocRetrieval.Companion.getEconAdapter2());

                            try {

                                holder.docDisplayRV.setAdapter(DocRetrieval.Companion.getEconAdapters().get(position));
                                holder.docTags.setAdapter(DocRetrieval.Companion.getEconTagsArray().get(position));

                                holder.departmentIndicator.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        try {
                                            holder.docDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getEconAdapters().get(holder.getAbsoluteAdapterPosition()).docPosition++);
                                            holder.docTags.smoothScrollToPosition(DocRetrieval.Companion.getEconTagsArray().get(holder.getAbsoluteAdapterPosition()).getTagPosition() + 1);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                break;
                case "Education": {
                    departmentsArray = DocSorting.getSubFields(4);
                    schoolDepartments.addAll(Arrays.asList(departmentsArray));

                    publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            documents = new ArrayList<>();
                            for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                SelectedDoc document = qds.toObject(SelectedDoc.class);
                                documents.add(document);

                            }

                            DocRetrieval.Companion.setEduTags0(new ArrayList<>());
                            DocRetrieval.Companion.setEduTags1(new ArrayList<>());
                            DocRetrieval.Companion.setEduTags2(new ArrayList<>());
                            DocRetrieval.Companion.setEduTags3(new ArrayList<>());
                            DocRetrieval.Companion.setEduTags4(new ArrayList<>());
                            DocRetrieval.Companion.setEduTags5(new ArrayList<>());
                            DocRetrieval.Companion.setEduTags6(new ArrayList<>());

                            DocRetrieval.Companion.setEducation0(new ArrayList<>());
                            DocRetrieval.Companion.setEducation1(new ArrayList<>());
                            DocRetrieval.Companion.setEducation2(new ArrayList<>());
                            DocRetrieval.Companion.setEducation3(new ArrayList<>());
                            DocRetrieval.Companion.setEducation4(new ArrayList<>());
                            DocRetrieval.Companion.setEducation5(new ArrayList<>());
                            DocRetrieval.Companion.setEducation6(new ArrayList<>());

                            for (SelectedDoc document : documents) {
                                if (document.getDocMetaData().contains(DocSorting.getSubFields(4)[0])) {
                                    DocRetrieval.Companion.getEducation0().add(document);
                                    DocRetrieval.Companion.setEduAdapter0(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEducation0()));
                                    DocRetrieval.Companion.getEduAdapter0().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getEduTags0().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getEduTags0().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getEduTags0(), null, DocRetrieval.Companion.getEduAdapter0());
                                    subFieldAdt0.notifyDataSetChanged();

                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(4)[1])) {
                                    DocRetrieval.Companion.getEducation1().add(document);
                                    DocRetrieval.Companion.setEduAdapter1(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEducation1()));
                                    DocRetrieval.Companion.getEduAdapter1().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getEduTags1().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getEduTags1().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getEduTags1(), null, DocRetrieval.Companion.getEduAdapter1());
                                    subFieldAdt1.notifyDataSetChanged();
                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(4)[2])) {
                                    DocRetrieval.Companion.getEducation2().add(document);
                                    DocRetrieval.Companion.setEduAdapter2(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEducation2()));
                                    DocRetrieval.Companion.getEduAdapter2().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getEduTags2().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getEduTags2().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getEduTags2(), null, DocRetrieval.Companion.getEduAdapter2());
                                    subFieldAdt2.notifyDataSetChanged();
                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(4)[3])) {
                                    DocRetrieval.Companion.getEducation3().add(document);
                                    DocRetrieval.Companion.setEduAdapter3(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEducation3()));
                                    DocRetrieval.Companion.getEduAdapter3().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getEduTags3().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getEduTags3().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt3 = new SubFieldAdt(c, DocRetrieval.Companion.getEduTags3(), null, DocRetrieval.Companion.getEduAdapter3());
                                    subFieldAdt3.notifyDataSetChanged();
                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(4)[4])) {
                                    DocRetrieval.Companion.getEducation4().add(document);
                                    DocRetrieval.Companion.setEduAdapter4(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEducation4()));
                                    DocRetrieval.Companion.getEduAdapter4().notifyDataSetChanged();


                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getEduTags4().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getEduTags4().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt4 = new SubFieldAdt(c, DocRetrieval.Companion.getEduTags4(), null, DocRetrieval.Companion.getEduAdapter4());
                                    subFieldAdt4.notifyDataSetChanged();

                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(4)[5])) {
                                    DocRetrieval.Companion.getEducation5().add(document);
                                    DocRetrieval.Companion.setEduAdapter5(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEducation5()));
                                    DocRetrieval.Companion.getEduAdapter5().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getEduTags5().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getEduTags5().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt5 = new SubFieldAdt(c, DocRetrieval.Companion.getEduTags5(), null, DocRetrieval.Companion.getEduAdapter5());
                                    subFieldAdt5.notifyDataSetChanged();
                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(4)[6])) {
                                    DocRetrieval.Companion.getEducation6().add(document);
                                    DocRetrieval.Companion.setEduAdapter6(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEducation6()));
                                    DocRetrieval.Companion.getEduAdapter6().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getEduTags6().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getEduTags6().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt6 = new SubFieldAdt(c, DocRetrieval.Companion.getEduTags6(), null, DocRetrieval.Companion.getEduAdapter6());
                                    subFieldAdt6.notifyDataSetChanged();
                                }

                            }

                            holder.readProgress.setVisibility(View.INVISIBLE);
                            DocRetrieval.Companion.getEducationTagsArray().add(subFieldAdt0);
                            DocRetrieval.Companion.getEducationTagsArray().add(subFieldAdt1);
                            DocRetrieval.Companion.getEducationTagsArray().add(subFieldAdt2);
                            DocRetrieval.Companion.getEducationTagsArray().add(subFieldAdt3);
                            DocRetrieval.Companion.getEducationTagsArray().add(subFieldAdt4);
                            DocRetrieval.Companion.getEducationTagsArray().add(subFieldAdt5);
                            DocRetrieval.Companion.getEducationTagsArray().add(subFieldAdt6);

                            DocRetrieval.Companion.getEduAdapters().add(DocRetrieval.Companion.getEduAdapter0());
                            DocRetrieval.Companion.getEduAdapters().add(DocRetrieval.Companion.getEduAdapter1());
                            DocRetrieval.Companion.getEduAdapters().add(DocRetrieval.Companion.getEduAdapter2());
                            DocRetrieval.Companion.getEduAdapters().add(DocRetrieval.Companion.getEduAdapter3());
                            DocRetrieval.Companion.getEduAdapters().add(DocRetrieval.Companion.getEduAdapter4());
                            DocRetrieval.Companion.getEduAdapters().add(DocRetrieval.Companion.getEduAdapter5());
                            DocRetrieval.Companion.getEduAdapters().add(DocRetrieval.Companion.getEduAdapter6());

                            try {

                                holder.docDisplayRV.setAdapter(DocRetrieval.Companion.getEduAdapters().get(position));
                                holder.docTags.setAdapter(DocRetrieval.Companion.getEducationTagsArray().get(position));

                                holder.departmentIndicator.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        try {
                                            holder.docDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getEduAdapters().get(holder.getAbsoluteAdapterPosition()).docPosition++);
                                            holder.docTags.smoothScrollToPosition(DocRetrieval.Companion.getEducationTagsArray().get(holder.getAbsoluteAdapterPosition()).getTagPosition() + 1);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                break;
                case "Engineering & Technology": {

                    departmentsArray = DocSorting.getSubFields(5);
                    schoolDepartments.addAll(Arrays.asList(departmentsArray));

                    publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            documents = new ArrayList<>();
                            for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                SelectedDoc document = qds.toObject(SelectedDoc.class);
                                documents.add(document);

                            }

                            DocRetrieval.Companion.setEngTags0(new ArrayList<>());
                            DocRetrieval.Companion.setEngTags1(new ArrayList<>());
                            DocRetrieval.Companion.setEngTags2(new ArrayList<>());
                            DocRetrieval.Companion.setEngTags3(new ArrayList<>());
                            DocRetrieval.Companion.setEngTags4(new ArrayList<>());
                            DocRetrieval.Companion.setEngTags5(new ArrayList<>());

                            DocRetrieval.Companion.setEngineering0(new ArrayList<>());
                            DocRetrieval.Companion.setEngineering1(new ArrayList<>());
                            DocRetrieval.Companion.setEngineering2(new ArrayList<>());
                            DocRetrieval.Companion.setEngineering3(new ArrayList<>());
                            DocRetrieval.Companion.setEngineering4(new ArrayList<>());
                            DocRetrieval.Companion.setEngineering5(new ArrayList<>());

                            for (SelectedDoc document : documents) {
                                if (document.getDocMetaData().contains(DocSorting.getSubFields(5)[0])) {
                                    DocRetrieval.Companion.getEngineering0().add(document);
                                    DocRetrieval.Companion.setEngAdapter0(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEngineering0()));
                                    DocRetrieval.Companion.getEngAdapter0().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getEngTags0().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getEngTags0().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getEngTags0(), null, DocRetrieval.Companion.getEngAdapter0());
                                    subFieldAdt0.notifyDataSetChanged();


                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(5)[1])) {
                                    DocRetrieval.Companion.getEngineering1().add(document);
                                    DocRetrieval.Companion.setEngAdapter1(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEngineering1()));
                                    DocRetrieval.Companion.getEngAdapter1().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getEngTags1().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getEngTags1().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getEngTags1(), null, DocRetrieval.Companion.getEngAdapter1());
                                    subFieldAdt1.notifyDataSetChanged();
                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(5)[2])) {
                                    DocRetrieval.Companion.getEngineering2().add(document);
                                    DocRetrieval.Companion.setEngAdapter2(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEngineering2()));
                                    DocRetrieval.Companion.getEngAdapter2().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getEngTags2().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getEngTags2().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getEngTags2(), null, DocRetrieval.Companion.getEngAdapter2());
                                    subFieldAdt2.notifyDataSetChanged();

                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(5)[3])) {
                                    DocRetrieval.Companion.getEngineering3().add(document);
                                    DocRetrieval.Companion.setEngAdapter3(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEngineering3()));
                                    DocRetrieval.Companion.getEngAdapter3().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getEngTags3().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getEngTags3().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt3 = new SubFieldAdt(c, DocRetrieval.Companion.getEngTags3(), null, DocRetrieval.Companion.getEngAdapter3());
                                    subFieldAdt3.notifyDataSetChanged();
                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(5)[4])) {
                                    DocRetrieval.Companion.getEngineering4().add(document);
                                    DocRetrieval.Companion.setEngAdapter4(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEngineering4()));
                                    DocRetrieval.Companion.getEngAdapter4().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getEngTags4().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getEngTags4().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt4 = new SubFieldAdt(c, DocRetrieval.Companion.getEngTags4(), null, DocRetrieval.Companion.getEngAdapter4());
                                    subFieldAdt4.notifyDataSetChanged();

                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(5)[5])) {
                                    DocRetrieval.Companion.getEngineering5().add(document);
                                    DocRetrieval.Companion.setEngAdapter5(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEngineering5()));
                                    DocRetrieval.Companion.getEngAdapter5().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getEngTags5().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getEngTags5().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt5 = new SubFieldAdt(c, DocRetrieval.Companion.getEngTags5(), null, DocRetrieval.Companion.getEngAdapter5());
                                    subFieldAdt5.notifyDataSetChanged();
                                }
                            }

                            holder.readProgress.setVisibility(View.INVISIBLE);
                            DocRetrieval.Companion.getEngTagsArray().add(subFieldAdt0);
                            DocRetrieval.Companion.getEngTagsArray().add(subFieldAdt1);
                            DocRetrieval.Companion.getEngTagsArray().add(subFieldAdt2);
                            DocRetrieval.Companion.getEngTagsArray().add(subFieldAdt3);
                            DocRetrieval.Companion.getEngTagsArray().add(subFieldAdt4);
                            DocRetrieval.Companion.getEngTagsArray().add(subFieldAdt5);

                            DocRetrieval.Companion.getEngAdapters().add(DocRetrieval.Companion.getEngAdapter0());
                            DocRetrieval.Companion.getEngAdapters().add(DocRetrieval.Companion.getEngAdapter1());
                            DocRetrieval.Companion.getEngAdapters().add(DocRetrieval.Companion.getEngAdapter2());
                            DocRetrieval.Companion.getEngAdapters().add(DocRetrieval.Companion.getEngAdapter3());
                            DocRetrieval.Companion.getEngAdapters().add(DocRetrieval.Companion.getEngAdapter4());
                            DocRetrieval.Companion.getEngAdapters().add(DocRetrieval.Companion.getEngAdapter5());

                            try {

                                holder.docDisplayRV.setAdapter(DocRetrieval.Companion.getEngAdapters().get(holder.getAbsoluteAdapterPosition()));
                                holder.docTags.setAdapter(DocRetrieval.Companion.getEngTagsArray().get(holder.getAbsoluteAdapterPosition()));

                                holder.departmentIndicator.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        try {
                                            holder.docDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getEngAdapters().get(holder.getAbsoluteAdapterPosition()).docPosition++);
                                            holder.docTags.smoothScrollToPosition(DocRetrieval.Companion.getEngTagsArray().get(holder.getAbsoluteAdapterPosition()).getTagPosition() + 1);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                break;
                case "Environmental Studies": {
                    departmentsArray = DocSorting.getSubFields(6);
                    schoolDepartments.addAll(Arrays.asList(departmentsArray));

                    publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            documents = new ArrayList<>();
                            for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                SelectedDoc document = qds.toObject(SelectedDoc.class);
                                documents.add(document);
                            }

                            DocRetrieval.Companion.setEnvTags0(new ArrayList<>());
                            DocRetrieval.Companion.setEnvTags1(new ArrayList<>());
                            DocRetrieval.Companion.setEnvTags2(new ArrayList<>());
                            DocRetrieval.Companion.setEnvTags3(new ArrayList<>());

                            DocRetrieval.Companion.setEnvironmental0(new ArrayList<>());
                            DocRetrieval.Companion.setEnvironmental1(new ArrayList<>());
                            DocRetrieval.Companion.setEnvironmental2(new ArrayList<>());
                            DocRetrieval.Companion.setEnvironmental3(new ArrayList<>());

                            for (SelectedDoc document : documents) {
                                if (document.getDocMetaData().contains(DocSorting.getSubFields(6)[0])) {
                                    DocRetrieval.Companion.getEnvironmental0().add(document);
                                    DocRetrieval.Companion.setEnvAdapter0(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEnvironmental0()));
                                    DocRetrieval.Companion.getEnvAdapter0().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getEnvTags0().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getEnvTags0().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getEnvTags0(), null, DocRetrieval.Companion.getEnvAdapter0());
                                    subFieldAdt0.notifyDataSetChanged();
                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(6)[1])) {
                                    DocRetrieval.Companion.getEnvironmental1().add(document);
                                    DocRetrieval.Companion.setEnvAdapter1(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEnvironmental1()));
                                    DocRetrieval.Companion.getEnvAdapter1().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getEnvTags1().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getEnvTags1().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getEnvTags1(), null, DocRetrieval.Companion.getEnvAdapter1());
                                    subFieldAdt1.notifyDataSetChanged();
                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(6)[2])) {
                                    DocRetrieval.Companion.getEnvironmental2().add(document);
                                    DocRetrieval.Companion.setEnvAdapter2(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEnvironmental2()));
                                    DocRetrieval.Companion.getEnvAdapter2().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getEnvTags2().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getEnvTags2().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getEnvTags2(), null, DocRetrieval.Companion.getEnvAdapter2());
                                    subFieldAdt2.notifyDataSetChanged();
                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(6)[3])) {
                                    DocRetrieval.Companion.getEnvironmental3().add(document);
                                    DocRetrieval.Companion.setEnvAdapter3(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEnvironmental3()));
                                    DocRetrieval.Companion.getEnvAdapter3().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getEnvTags3().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getEnvTags3().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt3 = new SubFieldAdt(c, DocRetrieval.Companion.getEnvTags3(), null, DocRetrieval.Companion.getEnvAdapter3());
                                    subFieldAdt3.notifyDataSetChanged();
                                }
                            }

                            holder.readProgress.setVisibility(View.INVISIBLE);

                            DocRetrieval.Companion.getEnvTagsArray().add(subFieldAdt0);
                            DocRetrieval.Companion.getEnvTagsArray().add(subFieldAdt1);
                            DocRetrieval.Companion.getEnvTagsArray().add(subFieldAdt2);
                            DocRetrieval.Companion.getEnvTagsArray().add(subFieldAdt3);

                            DocRetrieval.Companion.getEnvAdapters().add(DocRetrieval.Companion.getEnvAdapter0());
                            DocRetrieval.Companion.getEnvAdapters().add(DocRetrieval.Companion.getEnvAdapter1());
                            DocRetrieval.Companion.getEnvAdapters().add(DocRetrieval.Companion.getEnvAdapter2());
                            DocRetrieval.Companion.getEnvAdapters().add(DocRetrieval.Companion.getEnvAdapter3());

                            try {

                                holder.docDisplayRV.setAdapter(DocRetrieval.Companion.getEnvAdapters().get(holder.getAbsoluteAdapterPosition()));
                                holder.docTags.setAdapter(DocRetrieval.Companion.getEnvTagsArray().get(holder.getAbsoluteAdapterPosition()));

                                holder.departmentIndicator.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        try {
                                            holder.docDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getEnvAdapters().get(holder.getAbsoluteAdapterPosition()).docPosition++);
                                            holder.docTags.smoothScrollToPosition(DocRetrieval.Companion.getEnvTagsArray().get(holder.getAbsoluteAdapterPosition()).getTagPosition() + 1);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                break;
                case "Hospitality & Tourism": {

                    departmentsArray = DocSorting.getSubFields(7);
                    schoolDepartments.addAll(Arrays.asList(departmentsArray));

                    publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            documents = new ArrayList<>();
                            for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                SelectedDoc document = qds.toObject(SelectedDoc.class);
                                documents.add(document);
                            }

                            DocRetrieval.Companion.setHospitalityTags0(new ArrayList<>());
                            DocRetrieval.Companion.setHospitalityTags1(new ArrayList<>());

                            DocRetrieval.Companion.setHospitality0(new ArrayList<>());
                            DocRetrieval.Companion.setHospitality1(new ArrayList<>());

                            for (SelectedDoc document : documents) {
                                if (document.getDocMetaData().contains(DocSorting.getSubFields(7)[0])) {
                                    DocRetrieval.Companion.getHospitality0().add(document);
                                    DocRetrieval.Companion.setHospitalityAdapter0(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getHospitality0()));
                                    DocRetrieval.Companion.getHospitalityAdapter0().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getHospitalityTags0().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getHospitalityTags0().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getHospitalityTags0(), null, DocRetrieval.Companion.getHospitalityAdapter0());
                                    subFieldAdt0.notifyDataSetChanged();
                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(7)[1])) {
                                    DocRetrieval.Companion.getHospitality1().add(document);
                                    DocRetrieval.Companion.setHospitalityAdapter1(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getHospitality1()));
                                    DocRetrieval.Companion.getHospitalityAdapter1().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getHospitalityTags1().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getHospitalityTags1().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getHospitalityTags1(), null, DocRetrieval.Companion.getHospitalityAdapter1());
                                    subFieldAdt1.notifyDataSetChanged();
                                }
                            }

                            holder.readProgress.setVisibility(View.INVISIBLE);

                            DocRetrieval.Companion.getHospitalityAdapters().add(DocRetrieval.Companion.getHospitalityAdapter0());
                            DocRetrieval.Companion.getHospitalityAdapters().add(DocRetrieval.Companion.getHospitalityAdapter1());

                            DocRetrieval.Companion.getHospitalityTagsArray().add(subFieldAdt0);
                            DocRetrieval.Companion.getHospitalityTagsArray().add(subFieldAdt1);

                            try {

                                holder.docDisplayRV.setAdapter(DocRetrieval.Companion.getHospitalityAdapters().get(holder.getAbsoluteAdapterPosition()));
                                holder.docTags.setAdapter(DocRetrieval.Companion.getHospitalityTagsArray().get(holder.getAbsoluteAdapterPosition()));

                                holder.departmentIndicator.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        try {
                                            holder.docDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getHospitalityAdapters().get(holder.getAbsoluteAdapterPosition()).docPosition++);
                                            holder.docTags.smoothScrollToPosition(DocRetrieval.Companion.getHospitalityTagsArray().get(holder.getAbsoluteAdapterPosition()).getTagPosition() + 1);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                break;
                case "Humanities & Social Sciences": {

                    departmentsArray = DocSorting.getSubFields(8);
                    schoolDepartments.addAll(Arrays.asList(departmentsArray));
                    publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            documents = new ArrayList<>();
                            for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                SelectedDoc document = qds.toObject(SelectedDoc.class);
                                documents.add(document);
                            }

                            DocRetrieval.Companion.setHumanitiesTags0(new ArrayList<>());
                            DocRetrieval.Companion.setHumanitiesTags1(new ArrayList<>());
                            DocRetrieval.Companion.setHumanitiesTags2(new ArrayList<>());
                            DocRetrieval.Companion.setHumanitiesTags3(new ArrayList<>());
                            DocRetrieval.Companion.setHumanitiesTags4(new ArrayList<>());
                            DocRetrieval.Companion.setHumanitiesTags5(new ArrayList<>());
                            DocRetrieval.Companion.setHumanitiesTags6(new ArrayList<>());
                            DocRetrieval.Companion.setHumanitiesTags7(new ArrayList<>());
                            DocRetrieval.Companion.setHumanitiesTags8(new ArrayList<>());
                            DocRetrieval.Companion.setHumanitiesTags9(new ArrayList<>());
                            DocRetrieval.Companion.setHumanitiesTags10(new ArrayList<>());

                            DocRetrieval.Companion.setHumanities0(new ArrayList<>());
                            DocRetrieval.Companion.setHumanities1(new ArrayList<>());
                            DocRetrieval.Companion.setHumanities2(new ArrayList<>());
                            DocRetrieval.Companion.setHumanities3(new ArrayList<>());
                            DocRetrieval.Companion.setHumanities4(new ArrayList<>());
                            DocRetrieval.Companion.setHumanities5(new ArrayList<>());
                            DocRetrieval.Companion.setHumanities6(new ArrayList<>());
                            DocRetrieval.Companion.setHumanities7(new ArrayList<>());
                            DocRetrieval.Companion.setHumanities8(new ArrayList<>());
                            DocRetrieval.Companion.setHumanities9(new ArrayList<>());
                            DocRetrieval.Companion.setHumanities10(new ArrayList<>());

                            for (SelectedDoc document : documents) {
                                if (document.getDocMetaData().contains(DocSorting.getSubFields(8)[0])) {
                                    DocRetrieval.Companion.getHumanities0().add(document);
                                    DocRetrieval.Companion.setHumanitiesAdapter0(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getHumanities0()));
                                    DocRetrieval.Companion.getHumanitiesAdapter0().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getHumanitiesTags0().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getHumanitiesTags0().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getHumanitiesTags0(), null, DocRetrieval.Companion.getHumanitiesAdapter0());
                                    subFieldAdt0.notifyDataSetChanged();

                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(8)[1])) {
                                    DocRetrieval.Companion.getHumanities1().add(document);
                                    DocRetrieval.Companion.setHumanitiesAdapter1(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getHumanities1()));
                                    DocRetrieval.Companion.getHumanitiesAdapter1().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getHumanitiesTags1().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getHumanitiesTags1().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getHumanitiesTags1(), null, DocRetrieval.Companion.getHumanitiesAdapter1());
                                    subFieldAdt1.notifyDataSetChanged();

                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(8)[2])) {
                                    DocRetrieval.Companion.getHumanities2().add(document);
                                    DocRetrieval.Companion.setHumanitiesAdapter2(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getHumanities2()));
                                    DocRetrieval.Companion.getHumanitiesAdapter2().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getHumanitiesTags2().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getHumanitiesTags2().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getHumanitiesTags2(), null, DocRetrieval.Companion.getHumanitiesAdapter2());
                                    subFieldAdt2.notifyDataSetChanged();

                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(8)[3])) {
                                    DocRetrieval.Companion.getHumanities3().add(document);
                                    DocRetrieval.Companion.setHumanitiesAdapter3(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getHumanities3()));
                                    DocRetrieval.Companion.getHumanitiesAdapter3().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getHumanitiesTags3().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getHumanitiesTags3().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt3 = new SubFieldAdt(c, DocRetrieval.Companion.getHumanitiesTags3(), null, DocRetrieval.Companion.getHumanitiesAdapter3());
                                    subFieldAdt3.notifyDataSetChanged();

                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(8)[4])) {
                                    DocRetrieval.Companion.getHumanities4().add(document);
                                    DocRetrieval.Companion.setHumanitiesAdapter4(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getHumanities4()));
                                    DocRetrieval.Companion.getHumanitiesAdapter4().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getHumanitiesTags4().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getHumanitiesTags4().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt4 = new SubFieldAdt(c, DocRetrieval.Companion.getHumanitiesTags4(), null, DocRetrieval.Companion.getHumanitiesAdapter4());
                                    subFieldAdt4.notifyDataSetChanged();

                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(8)[5])) {
                                    DocRetrieval.Companion.getHumanities5().add(document);
                                    DocRetrieval.Companion.setHumanitiesAdapter5(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getHumanities5()));
                                    DocRetrieval.Companion.getHumanitiesAdapter5().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getHumanitiesTags5().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getHumanitiesTags5().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt5 = new SubFieldAdt(c, DocRetrieval.Companion.getHumanitiesTags5(), null, DocRetrieval.Companion.getHumanitiesAdapter5());
                                    subFieldAdt5.notifyDataSetChanged();

                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(8)[6])) {
                                    DocRetrieval.Companion.getHumanities6().add(document);
                                    DocRetrieval.Companion.setHumanitiesAdapter6(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getHumanities6()));
                                    DocRetrieval.Companion.getHumanitiesAdapter6().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getHumanitiesTags6().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getHumanitiesTags6().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt6 = new SubFieldAdt(c, DocRetrieval.Companion.getHumanitiesTags6(), null, DocRetrieval.Companion.getHumanitiesAdapter6());
                                    subFieldAdt6.notifyDataSetChanged();

                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(8)[7])) {
                                    DocRetrieval.Companion.getHumanities7().add(document);
                                    DocRetrieval.Companion.setHumanitiesAdapter7(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getHumanities7()));
                                    DocRetrieval.Companion.getHumanitiesAdapter7().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getHumanitiesTags7().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getHumanitiesTags7().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt7 = new SubFieldAdt(c, DocRetrieval.Companion.getHumanitiesTags7(), null, DocRetrieval.Companion.getHumanitiesAdapter7());
                                    subFieldAdt7.notifyDataSetChanged();

                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(8)[8])) {
                                    DocRetrieval.Companion.getHumanities8().add(document);
                                    DocRetrieval.Companion.setHumanitiesAdapter8(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getHumanities8()));
                                    DocRetrieval.Companion.getHumanitiesAdapter8().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getHumanitiesTags8().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getHumanitiesTags8().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt8 = new SubFieldAdt(c, DocRetrieval.Companion.getHumanitiesTags8(), null, DocRetrieval.Companion.getHumanitiesAdapter8());
                                    subFieldAdt8.notifyDataSetChanged();


                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(8)[9])) {
                                    DocRetrieval.Companion.getHumanities9().add(document);
                                    DocRetrieval.Companion.setHumanitiesAdapter9(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getHumanities9()));
                                    DocRetrieval.Companion.getHumanitiesAdapter9().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getHumanitiesTags9().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getHumanitiesTags9().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt9 = new SubFieldAdt(c, DocRetrieval.Companion.getHumanitiesTags9(), null, DocRetrieval.Companion.getHumanitiesAdapter9());
                                    subFieldAdt9.notifyDataSetChanged();

                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(8)[10])) {
                                    DocRetrieval.Companion.getHumanities10().add(document);
                                    DocRetrieval.Companion.setHumanitiesAdapter10(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getHumanities10()));
                                    DocRetrieval.Companion.getHumanitiesAdapter10().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getHumanitiesTags10().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getHumanitiesTags10().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt10 = new SubFieldAdt(c, DocRetrieval.Companion.getHumanitiesTags10(), null, DocRetrieval.Companion.getHumanitiesAdapter10());
                                    subFieldAdt10.notifyDataSetChanged();
                                }

                            }

                            holder.readProgress.setVisibility(View.INVISIBLE);

                            DocRetrieval.Companion.getHumanitiesTagsArray().add(subFieldAdt0);
                            DocRetrieval.Companion.getHumanitiesTagsArray().add(subFieldAdt1);
                            DocRetrieval.Companion.getHumanitiesTagsArray().add(subFieldAdt2);
                            DocRetrieval.Companion.getHumanitiesTagsArray().add(subFieldAdt3);
                            DocRetrieval.Companion.getHumanitiesTagsArray().add(subFieldAdt4);
                            DocRetrieval.Companion.getHumanitiesTagsArray().add(subFieldAdt6);
                            DocRetrieval.Companion.getHumanitiesTagsArray().add(subFieldAdt5);
                            DocRetrieval.Companion.getHumanitiesTagsArray().add(subFieldAdt7);
                            DocRetrieval.Companion.getHumanitiesTagsArray().add(subFieldAdt8);
                            DocRetrieval.Companion.getHumanitiesTagsArray().add(subFieldAdt9);
                            DocRetrieval.Companion.getHumanitiesTagsArray().add(subFieldAdt10);

                            DocRetrieval.Companion.getHumanitiesAdapters().add(DocRetrieval.Companion.getHumanitiesAdapter0());
                            DocRetrieval.Companion.getHumanitiesAdapters().add(DocRetrieval.Companion.getHumanitiesAdapter1());
                            DocRetrieval.Companion.getHumanitiesAdapters().add(DocRetrieval.Companion.getHumanitiesAdapter2());
                            DocRetrieval.Companion.getHumanitiesAdapters().add(DocRetrieval.Companion.getHumanitiesAdapter3());
                            DocRetrieval.Companion.getHumanitiesAdapters().add(DocRetrieval.Companion.getHumanitiesAdapter4());
                            DocRetrieval.Companion.getHumanitiesAdapters().add(DocRetrieval.Companion.getHumanitiesAdapter5());
                            DocRetrieval.Companion.getHumanitiesAdapters().add(DocRetrieval.Companion.getHumanitiesAdapter6());
                            DocRetrieval.Companion.getHumanitiesAdapters().add(DocRetrieval.Companion.getHumanitiesAdapter7());
                            DocRetrieval.Companion.getHumanitiesAdapters().add(DocRetrieval.Companion.getHumanitiesAdapter8());
                            DocRetrieval.Companion.getHumanitiesAdapters().add(DocRetrieval.Companion.getHumanitiesAdapter9());
                            DocRetrieval.Companion.getHumanitiesAdapters().add(DocRetrieval.Companion.getHumanitiesAdapter10());


                            try {

                                holder.docDisplayRV.setAdapter(DocRetrieval.Companion.getHumanitiesAdapters().get(holder.getAbsoluteAdapterPosition()));
                                holder.docTags.setAdapter(DocRetrieval.Companion.getHumanitiesTagsArray().get(holder.getAbsoluteAdapterPosition()));

                                holder.departmentIndicator.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        try {
                                            holder.docDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getHumanitiesAdapters().get(holder.getAbsoluteAdapterPosition()).docPosition++);
                                            holder.docTags.smoothScrollToPosition(DocRetrieval.Companion.getHumanitiesTagsArray().get(holder.getAbsoluteAdapterPosition()).getTagPosition() + 1);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                break;
                case "Law": {

                    departmentsArray = DocSorting.getSubFields(9);
                    schoolDepartments.addAll(Arrays.asList(departmentsArray));

                    publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            documents = new ArrayList<>();
                            for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                SelectedDoc document = qds.toObject(SelectedDoc.class);
                                documents.add(document);
                            }

                            DocRetrieval.Companion.setLawTags0(new ArrayList<>());
                            DocRetrieval.Companion.setLawTags1(new ArrayList<>());
                            DocRetrieval.Companion.setLawTags2(new ArrayList<>());
                            DocRetrieval.Companion.setLawTags3(new ArrayList<>());
                            DocRetrieval.Companion.setLawTags4(new ArrayList<>());
                            DocRetrieval.Companion.setLawTags5(new ArrayList<>());

                            DocRetrieval.Companion.setLaw0(new ArrayList<>());
                            DocRetrieval.Companion.setLaw1(new ArrayList<>());
                            DocRetrieval.Companion.setLaw2(new ArrayList<>());
                            DocRetrieval.Companion.setLaw3(new ArrayList<>());
                            DocRetrieval.Companion.setLaw4(new ArrayList<>());
                            DocRetrieval.Companion.setLaw5(new ArrayList<>());

                            for (SelectedDoc document : documents) {

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(9)[0])) {
                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");

                                    DocRetrieval.Companion.getLaw0().add(document);
                                    DocRetrieval.Companion.setLawAdapter0(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getLaw0()));
                                    DocRetrieval.Companion.getLawAdapter0().notifyDataSetChanged();
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getLawTags0().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getLawTags0().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getLawTags0(), null, DocRetrieval.Companion.getLawAdapter0());
                                    subFieldAdt0.notifyDataSetChanged();
                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(9)[1])) {
                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");

                                    DocRetrieval.Companion.getLaw1().add(document);
                                    DocRetrieval.Companion.setLawAdapter1(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getLaw1()));
                                    DocRetrieval.Companion.getLawAdapter1().notifyDataSetChanged();

                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getLawTags1().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getLawTags1().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getLawTags1(), null, DocRetrieval.Companion.getLawAdapter1());
                                    subFieldAdt1.notifyDataSetChanged();

                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(9)[2])) {
                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");

                                    DocRetrieval.Companion.getLaw2().add(document);
                                    DocRetrieval.Companion.setLawAdapter2(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getLaw2()));
                                    DocRetrieval.Companion.getLawAdapter2().notifyDataSetChanged();

                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getLawTags2().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getLawTags2().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getLawTags2(), null, DocRetrieval.Companion.getLawAdapter2());
                                    subFieldAdt2.notifyDataSetChanged();
                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(9)[3])) {
                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");


                                    DocRetrieval.Companion.getLaw3().add(document);
                                    DocRetrieval.Companion.setLawAdapter3(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getLaw3()));
                                    DocRetrieval.Companion.getLawAdapter3().notifyDataSetChanged();

                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getLawTags3().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getLawTags3().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt3 = new SubFieldAdt(c, DocRetrieval.Companion.getLawTags3(), null, DocRetrieval.Companion.getLawAdapter3());
                                    subFieldAdt3.notifyDataSetChanged();
                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(9)[4])) {
                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");

                                    DocRetrieval.Companion.getLaw4().add(document);
                                    DocRetrieval.Companion.setLawAdapter4(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getLaw4()));
                                    DocRetrieval.Companion.getLawAdapter4().notifyDataSetChanged();

                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getLawTags4().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getLawTags4().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt4 = new SubFieldAdt(c, DocRetrieval.Companion.getLawTags4(), null, DocRetrieval.Companion.getLawAdapter4());
                                    subFieldAdt4.notifyDataSetChanged();
                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(9)[5])) {
                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");

                                    DocRetrieval.Companion.getLaw5().add(document);
                                    DocRetrieval.Companion.setLawAdapter5(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getLaw5()));
                                    DocRetrieval.Companion.getLawAdapter5().notifyDataSetChanged();

                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getLawTags5().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getLawTags5().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt5 = new SubFieldAdt(c, DocRetrieval.Companion.getLawTags5(), null, DocRetrieval.Companion.getLawAdapter5());
                                    subFieldAdt5.notifyDataSetChanged();
                                }

                            }

                            holder.readProgress.setVisibility(View.INVISIBLE);
                            DocRetrieval.Companion.getLawTagsArray().add(subFieldAdt0);
                            DocRetrieval.Companion.getLawTagsArray().add(subFieldAdt1);
                            DocRetrieval.Companion.getLawTagsArray().add(subFieldAdt2);
                            DocRetrieval.Companion.getLawTagsArray().add(subFieldAdt3);
                            DocRetrieval.Companion.getLawTagsArray().add(subFieldAdt4);
                            DocRetrieval.Companion.getLawTagsArray().add(subFieldAdt5);
                            DocRetrieval.Companion.getLawTagsArray().add(subFieldAdt6);

                            DocRetrieval.Companion.getLawAdapters().add(DocRetrieval.Companion.getLawAdapter0());
                            DocRetrieval.Companion.getLawAdapters().add(DocRetrieval.Companion.getLawAdapter1());
                            DocRetrieval.Companion.getLawAdapters().add(DocRetrieval.Companion.getLawAdapter2());
                            DocRetrieval.Companion.getLawAdapters().add(DocRetrieval.Companion.getLawAdapter3());
                            DocRetrieval.Companion.getLawAdapters().add(DocRetrieval.Companion.getLawAdapter4());
                            DocRetrieval.Companion.getLawAdapters().add(DocRetrieval.Companion.getLawAdapter5());

                            try {

                                holder.docDisplayRV.setAdapter(DocRetrieval.Companion.getLawAdapters().get(holder.getAbsoluteAdapterPosition()));
                                holder.docTags.setAdapter(DocRetrieval.Companion.getLawTagsArray().get(holder.getAbsoluteAdapterPosition()));

                                holder.departmentIndicator.setOnClickListener(v -> {

                                    try {
                                        holder.docDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getLawAdapters().get(holder.getAbsoluteAdapterPosition()).docPosition++);
                                        holder.docTags.smoothScrollToPosition(DocRetrieval.Companion.getLawTagsArray().get(holder.getAbsoluteAdapterPosition()).getTagPosition() + 1);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).addOnFailureListener(e -> Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show());

                }
                break;
                case "Medicine": {

                    departmentsArray = DocSorting.getSubFields(10);
                    schoolDepartments.addAll(Arrays.asList(departmentsArray));

                    publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            documents = new ArrayList<>();
                            for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                SelectedDoc document = qds.toObject(SelectedDoc.class);
                                documents.add(document);
                            }

                            DocRetrieval.Companion.setMedTags0(new ArrayList<>());
                            DocRetrieval.Companion.setMedTags1(new ArrayList<>());
                            DocRetrieval.Companion.setMedTags2(new ArrayList<>());
                            DocRetrieval.Companion.setMedTags3(new ArrayList<>());
                            DocRetrieval.Companion.setMedTags4(new ArrayList<>());
                            DocRetrieval.Companion.setMedTags5(new ArrayList<>());
                            DocRetrieval.Companion.setMedTags6(new ArrayList<>());
                            DocRetrieval.Companion.setMedTags7(new ArrayList<>());
                            DocRetrieval.Companion.setMedTags8(new ArrayList<>());
                            DocRetrieval.Companion.setMedTags9(new ArrayList<>());

                            DocRetrieval.Companion.setMedicine0(new ArrayList<>());
                            DocRetrieval.Companion.setMedicine1(new ArrayList<>());
                            DocRetrieval.Companion.setMedicine2(new ArrayList<>());
                            DocRetrieval.Companion.setMedicine3(new ArrayList<>());
                            DocRetrieval.Companion.setMedicine4(new ArrayList<>());
                            DocRetrieval.Companion.setMedicine5(new ArrayList<>());
                            DocRetrieval.Companion.setMedicine6(new ArrayList<>());
                            DocRetrieval.Companion.setMedicine7(new ArrayList<>());
                            DocRetrieval.Companion.setMedicine8(new ArrayList<>());
                            DocRetrieval.Companion.setMedicine9(new ArrayList<>());

                            for (SelectedDoc document : documents) {
                                if (document.getDocMetaData().contains(DocSorting.getSubFields(10)[0])) {
                                    DocRetrieval.Companion.getMedicine0().add(document);
                                    DocRetrieval.Companion.setMedAdapter0(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getMedicine0()));
                                    DocRetrieval.Companion.getMedAdapter0().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getMedTags0().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getMedTags0().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt10 = new SubFieldAdt(c, DocRetrieval.Companion.getMedTags0(), null, DocRetrieval.Companion.getMedAdapter4());
                                    subFieldAdt10.notifyDataSetChanged();
                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(10)[1])) {
                                    DocRetrieval.Companion.getMedicine1().add(document);
                                    DocRetrieval.Companion.setMedAdapter1(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getMedicine1()));
                                    DocRetrieval.Companion.getMedAdapter1().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getMedTags1().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getMedTags1().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getMedTags1(), null, DocRetrieval.Companion.getMedAdapter1());
                                    subFieldAdt1.notifyDataSetChanged();

                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(10)[2])) {
                                    DocRetrieval.Companion.getMedicine2().add(document);
                                    DocRetrieval.Companion.setMedAdapter2(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getMedicine2()));
                                    DocRetrieval.Companion.getMedAdapter2().notifyDataSetChanged();


                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getMedTags2().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getMedTags2().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getMedTags2(), null, DocRetrieval.Companion.getMedAdapter2());
                                    subFieldAdt2.notifyDataSetChanged();

                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(10)[3])) {
                                    DocRetrieval.Companion.getMedicine3().add(document);
                                    DocRetrieval.Companion.setMedAdapter3(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getMedicine3()));
                                    DocRetrieval.Companion.getMedAdapter3().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getMedTags3().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getMedTags3().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt3 = new SubFieldAdt(c, DocRetrieval.Companion.getMedTags3(), null, DocRetrieval.Companion.getMedAdapter3());
                                    subFieldAdt3.notifyDataSetChanged();

                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(10)[4])) {
                                    DocRetrieval.Companion.getMedicine4().add(document);
                                    DocRetrieval.Companion.setMedAdapter4(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getMedicine4()));
                                    DocRetrieval.Companion.getMedAdapter4().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getMedTags4().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getMedTags4().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt4 = new SubFieldAdt(c, DocRetrieval.Companion.getMedTags4(), null, DocRetrieval.Companion.getMedAdapter4());
                                    subFieldAdt4.notifyDataSetChanged();


                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(10)[5])) {
                                    DocRetrieval.Companion.getMedicine5().add(document);
                                    DocRetrieval.Companion.setMedAdapter5(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getMedicine5()));
                                    DocRetrieval.Companion.getMedAdapter5().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getMedTags5().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getMedTags5().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt5 = new SubFieldAdt(c, DocRetrieval.Companion.getMedTags5(), null, DocRetrieval.Companion.getMedAdapter5());
                                    subFieldAdt5.notifyDataSetChanged();

                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(10)[6])) {
                                    DocRetrieval.Companion.getMedicine6().add(document);
                                    DocRetrieval.Companion.setMedAdapter6(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getMedicine6()));
                                    DocRetrieval.Companion.getMedAdapter6().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getMedTags6().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getMedTags6().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt6 = new SubFieldAdt(c, DocRetrieval.Companion.getMedTags6(), null, DocRetrieval.Companion.getMedAdapter6());
                                    subFieldAdt6.notifyDataSetChanged();

                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(10)[7])) {
                                    DocRetrieval.Companion.getMedicine7().add(document);
                                    DocRetrieval.Companion.setMedAdapter7(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getMedicine7()));
                                    DocRetrieval.Companion.getMedAdapter7().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getMedTags7().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getMedTags7().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt7 = new SubFieldAdt(c, DocRetrieval.Companion.getMedTags7(), null, DocRetrieval.Companion.getMedAdapter7());
                                    subFieldAdt7.notifyDataSetChanged();
                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(10)[8])) {
                                    DocRetrieval.Companion.getMedicine8().add(document);
                                    DocRetrieval.Companion.setMedAdapter8(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getMedicine8()));
                                    DocRetrieval.Companion.getMedAdapter8().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getMedTags8().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getMedTags8().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt8 = new SubFieldAdt(c, DocRetrieval.Companion.getMedTags8(), null, DocRetrieval.Companion.getMedAdapter8());
                                    subFieldAdt8.notifyDataSetChanged();

                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(10)[9])) {
                                    DocRetrieval.Companion.getMedicine9().add(document);
                                    DocRetrieval.Companion.setMedAdapter9(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getMedicine9()));
                                    DocRetrieval.Companion.getMedAdapter9().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getMedTags9().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getMedTags9().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt9 = new SubFieldAdt(c, DocRetrieval.Companion.getMedTags9(), null, DocRetrieval.Companion.getMedAdapter9());
                                    subFieldAdt9.notifyDataSetChanged();
                                }

                            }

                            holder.readProgress.setVisibility(View.INVISIBLE);

                            DocRetrieval.Companion.getMedTagsArray().add(subFieldAdt0);
                            DocRetrieval.Companion.getMedTagsArray().add(subFieldAdt1);
                            DocRetrieval.Companion.getMedTagsArray().add(subFieldAdt2);
                            DocRetrieval.Companion.getMedTagsArray().add(subFieldAdt3);
                            DocRetrieval.Companion.getMedTagsArray().add(subFieldAdt4);
                            DocRetrieval.Companion.getMedTagsArray().add(subFieldAdt5);
                            DocRetrieval.Companion.getMedTagsArray().add(subFieldAdt6);
                            DocRetrieval.Companion.getMedTagsArray().add(subFieldAdt7);
                            DocRetrieval.Companion.getMedTagsArray().add(subFieldAdt8);
                            DocRetrieval.Companion.getMedTagsArray().add(subFieldAdt9);

                            DocRetrieval.Companion.getMedAdapters().add(DocRetrieval.Companion.getMedAdapter0());
                            DocRetrieval.Companion.getMedAdapters().add(DocRetrieval.Companion.getMedAdapter1());
                            DocRetrieval.Companion.getMedAdapters().add(DocRetrieval.Companion.getMedAdapter2());
                            DocRetrieval.Companion.getMedAdapters().add(DocRetrieval.Companion.getMedAdapter3());
                            DocRetrieval.Companion.getMedAdapters().add(DocRetrieval.Companion.getMedAdapter4());
                            DocRetrieval.Companion.getMedAdapters().add(DocRetrieval.Companion.getMedAdapter5());
                            DocRetrieval.Companion.getMedAdapters().add(DocRetrieval.Companion.getMedAdapter6());
                            DocRetrieval.Companion.getMedAdapters().add(DocRetrieval.Companion.getMedAdapter7());
                            DocRetrieval.Companion.getMedAdapters().add(DocRetrieval.Companion.getMedAdapter8());
                            DocRetrieval.Companion.getMedAdapters().add(DocRetrieval.Companion.getMedAdapter9());

                            try {

                                holder.docDisplayRV.setAdapter(DocRetrieval.Companion.getMedAdapters().get(position));
                                holder.docTags.setAdapter(DocRetrieval.Companion.getMedTagsArray().get(position));

                                holder.departmentIndicator.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        try {
                                            holder.docDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getMedAdapters().get(holder.getAbsoluteAdapterPosition()).docPosition++);
                                            holder.docTags.smoothScrollToPosition(DocRetrieval.Companion.getMedTagsArray().get(holder.getAbsoluteAdapterPosition()).getTagPosition() + 1);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }
                break;
                case "Public Health": {

                    departmentsArray = DocSorting.getSubFields(11);
                    schoolDepartments.addAll(Arrays.asList(departmentsArray));

                    publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            documents = new ArrayList<>();
                            for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                SelectedDoc document = qds.toObject(SelectedDoc.class);
                                documents.add(document);
                            }

                            DocRetrieval.Companion.setPublicHealthTags0(new ArrayList<>());
                            DocRetrieval.Companion.setPublicHealthTags1(new ArrayList<>());
                            DocRetrieval.Companion.setPublicHealthTags2(new ArrayList<>());
                            DocRetrieval.Companion.setPublicHealthTags3(new ArrayList<>());

                            DocRetrieval.Companion.setPublicHealth0(new ArrayList<>());
                            DocRetrieval.Companion.setPublicHealth1(new ArrayList<>());
                            DocRetrieval.Companion.setPublicHealth2(new ArrayList<>());
                            DocRetrieval.Companion.setPublicHealth3(new ArrayList<>());

                            for (SelectedDoc document : documents) {
                                if (document.getDocMetaData().contains(DocSorting.getSubFields(11)[0])) {
                                    DocRetrieval.Companion.getPublicHealth0().add(document);
                                    DocRetrieval.Companion.setPublicHealthAdapter0(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getPublicHealth0()));
                                    DocRetrieval.Companion.getPublicHealthAdapter0().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getPublicHealthTags0().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getPublicHealthTags0().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getPublicHealthTags0(), null, DocRetrieval.Companion.getPublicHealthAdapter0());
                                    subFieldAdt0.notifyDataSetChanged();

                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(11)[1])) {
                                    DocRetrieval.Companion.getPublicHealth1().add(document);
                                    DocRetrieval.Companion.setPublicHealthAdapter1(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getPublicHealth1()));
                                    DocRetrieval.Companion.getPublicHealthAdapter1().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getPublicHealthTags1().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getPublicHealthTags1().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getPublicHealthTags1(), null, DocRetrieval.Companion.getPublicHealthAdapter1());
                                    subFieldAdt1.notifyDataSetChanged();

                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(11)[2])) {
                                    DocRetrieval.Companion.getPublicHealth2().add(document);
                                    DocRetrieval.Companion.setPublicHealthAdapter2(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getPublicHealth2()));
                                    DocRetrieval.Companion.getPublicHealthAdapter2().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getPublicHealthTags2().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getPublicHealthTags2().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getPublicHealthTags2(), null, DocRetrieval.Companion.getPublicHealthAdapter2());
                                    subFieldAdt2.notifyDataSetChanged();
                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(11)[3])) {
                                    DocRetrieval.Companion.getPublicHealth3().add(document);
                                    DocRetrieval.Companion.setPublicHealthAdapter3(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getPublicHealth3()));
                                    DocRetrieval.Companion.getPublicHealthAdapter3().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getPublicHealthTags3().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getPublicHealthTags3().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt3 = new SubFieldAdt(c, DocRetrieval.Companion.getPublicHealthTags3(), null, DocRetrieval.Companion.getPublicHealthAdapter3());
                                    subFieldAdt3.notifyDataSetChanged();
                                }

                            }

                            holder.readProgress.setVisibility(View.INVISIBLE);

                            DocRetrieval.Companion.getPublicHealthTagsArray().add(subFieldAdt0);
                            DocRetrieval.Companion.getPublicHealthTagsArray().add(subFieldAdt1);
                            DocRetrieval.Companion.getPublicHealthTagsArray().add(subFieldAdt2);
                            DocRetrieval.Companion.getPublicHealthTagsArray().add(subFieldAdt3);

                            DocRetrieval.Companion.getPublicHealthAdapters().add(DocRetrieval.Companion.getPublicHealthAdapter0());
                            DocRetrieval.Companion.getPublicHealthAdapters().add(DocRetrieval.Companion.getPublicHealthAdapter1());
                            DocRetrieval.Companion.getPublicHealthAdapters().add(DocRetrieval.Companion.getPublicHealthAdapter2());
                            DocRetrieval.Companion.getPublicHealthAdapters().add(DocRetrieval.Companion.getPublicHealthAdapter3());


                            try {

                                holder.docDisplayRV.setAdapter(DocRetrieval.Companion.getPublicHealthAdapters().get(holder.getAbsoluteAdapterPosition()));
                                holder.docTags.setAdapter(DocRetrieval.Companion.getPublicHealthTagsArray().get(holder.getAbsoluteAdapterPosition()));

                                holder.departmentIndicator.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        try {
                                            holder.docDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getPublicHealthAdapters().get(holder.getAbsoluteAdapterPosition()).docPosition++);
                                            holder.docTags.smoothScrollToPosition(DocRetrieval.Companion.getPublicHealthTagsArray().get(holder.getAbsoluteAdapterPosition()).getTagPosition() + 1);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }
                break;
                case "Pure & Applied Sciences": {

                    departmentsArray = DocSorting.getSubFields(12);
                    schoolDepartments.addAll(Arrays.asList(departmentsArray));

                    publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            documents = new ArrayList<>();
                            for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                SelectedDoc document = qds.toObject(SelectedDoc.class);
                                documents.add(document);
                            }
                            DocRetrieval.Companion.setPureAppTags0(new ArrayList<>());
                            DocRetrieval.Companion.setPureAppTags1(new ArrayList<>());
                            DocRetrieval.Companion.setPureAppTags2(new ArrayList<>());
                            DocRetrieval.Companion.setPureAppTags3(new ArrayList<>());
                            DocRetrieval.Companion.setPureAppTags4(new ArrayList<>());
                            DocRetrieval.Companion.setPureAppTags5(new ArrayList<>());
                            DocRetrieval.Companion.setPureAppTags6(new ArrayList<>());
                            DocRetrieval.Companion.setPureAppTags7(new ArrayList<>());

                            DocRetrieval.Companion.setPureAppSci0(new ArrayList<>());
                            DocRetrieval.Companion.setPureAppSci1(new ArrayList<>());
                            DocRetrieval.Companion.setPureAppSci2(new ArrayList<>());
                            DocRetrieval.Companion.setPureAppSci3(new ArrayList<>());
                            DocRetrieval.Companion.setPureAppSci4(new ArrayList<>());
                            DocRetrieval.Companion.setPureAppSci5(new ArrayList<>());
                            DocRetrieval.Companion.setPureAppSci6(new ArrayList<>());
                            DocRetrieval.Companion.setPureAppSci7(new ArrayList<>());

                            for (SelectedDoc document : documents) {
                                if (document.getDocMetaData().contains(DocSorting.getSubFields(12)[0])) {
                                    DocRetrieval.Companion.getPureAppSci0().add(document);
                                    DocRetrieval.Companion.setDocAdapter0(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getPureAppSci0()));
                                    DocRetrieval.Companion.getDocAdapter0().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getPureAppTags0().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getPureAppTags0().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getPureAppTags0(), null, DocRetrieval.Companion.getDocAdapter0());
                                    subFieldAdt0.notifyDataSetChanged();

                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(12)[1])) {
                                    DocRetrieval.Companion.getPureAppSci1().add(document);
                                    DocRetrieval.Companion.setDocAdapter1(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getPureAppSci1()));
                                    DocRetrieval.Companion.getDocAdapter1().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getPureAppTags1().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getPureAppTags1().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getPureAppTags1(), null, DocRetrieval.Companion.getDocAdapter1());
                                    subFieldAdt1.notifyDataSetChanged();

                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(12)[2])) {
                                    DocRetrieval.Companion.getPureAppSci2().add(document);
                                    DocRetrieval.Companion.setDocAdapter2(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getPureAppSci2()));
                                    DocRetrieval.Companion.getDocAdapter2().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getPureAppTags2().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getPureAppTags2().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getPureAppTags2(), null, DocRetrieval.Companion.getDocAdapter2());
                                    subFieldAdt2.notifyDataSetChanged();


                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(12)[3])) {
                                    DocRetrieval.Companion.getPureAppSci3().add(document);
                                    DocRetrieval.Companion.setDocAdapter3(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getPureAppSci3()));
                                    DocRetrieval.Companion.getDocAdapter3().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getPureAppTags3().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getPureAppTags3().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt3 = new SubFieldAdt(c, DocRetrieval.Companion.getPureAppTags3(), null, DocRetrieval.Companion.getDocAdapter3());
                                    subFieldAdt3.notifyDataSetChanged();


                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(12)[4])) {
                                    DocRetrieval.Companion.getPureAppSci4().add(document);
                                    DocRetrieval.Companion.setDocAdapter4(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getPureAppSci4()));
                                    DocRetrieval.Companion.getDocAdapter4().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getPureAppTags4().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getPureAppTags4().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt4 = new SubFieldAdt(c, DocRetrieval.Companion.getPureAppTags4(), null, DocRetrieval.Companion.getDocAdapter4());
                                    subFieldAdt4.notifyDataSetChanged();

                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(12)[5])) {
                                    DocRetrieval.Companion.getPureAppSci5().add(document);
                                    DocRetrieval.Companion.setDocAdapter5(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getPureAppSci5()));
                                    DocRetrieval.Companion.getDocAdapter5().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getPureAppTags5().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getPureAppTags5().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt5 = new SubFieldAdt(c, DocRetrieval.Companion.getPureAppTags5(), null, DocRetrieval.Companion.getDocAdapter5());
                                    subFieldAdt5.notifyDataSetChanged();
                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(12)[6])) {
                                    DocRetrieval.Companion.getPureAppSci6().add(document);
                                    DocRetrieval.Companion.setDocAdapter6(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getPureAppSci6()));
                                    DocRetrieval.Companion.getDocAdapter6().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getPureAppTags6().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getPureAppTags6().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt6 = new SubFieldAdt(c, DocRetrieval.Companion.getPureAppTags6(), null, DocRetrieval.Companion.getDocAdapter6());
                                    subFieldAdt6.notifyDataSetChanged();


                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(12)[7])) {
                                    DocRetrieval.Companion.getPureAppSci7().add(document);
                                    DocRetrieval.Companion.setDocAdapter7(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getPureAppSci7()));
                                    DocRetrieval.Companion.getDocAdapter7().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getPureAppTags7().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getPureAppTags7().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt7 = new SubFieldAdt(c, DocRetrieval.Companion.getPureAppTags7(), null, DocRetrieval.Companion.getDocAdapter7());
                                    subFieldAdt7.notifyDataSetChanged();
                                }

                            }


                            holder.readProgress.setVisibility(View.INVISIBLE);

                            DocRetrieval.Companion.getPureAppTagsArray().add(subFieldAdt0);
                            DocRetrieval.Companion.getPureAppTagsArray().add(subFieldAdt1);
                            DocRetrieval.Companion.getPureAppTagsArray().add(subFieldAdt2);
                            DocRetrieval.Companion.getPureAppTagsArray().add(subFieldAdt3);
                            DocRetrieval.Companion.getPureAppTagsArray().add(subFieldAdt4);
                            DocRetrieval.Companion.getPureAppTagsArray().add(subFieldAdt5);
                            DocRetrieval.Companion.getPureAppTagsArray().add(subFieldAdt6);
                            DocRetrieval.Companion.getPureAppTagsArray().add(subFieldAdt7);

                            DocRetrieval.Companion.getPureAdapters().add(DocRetrieval.Companion.getDocAdapter0());
                            DocRetrieval.Companion.getPureAdapters().add(DocRetrieval.Companion.getDocAdapter1());
                            DocRetrieval.Companion.getPureAdapters().add(DocRetrieval.Companion.getDocAdapter2());
                            DocRetrieval.Companion.getPureAdapters().add(DocRetrieval.Companion.getDocAdapter3());
                            DocRetrieval.Companion.getPureAdapters().add(DocRetrieval.Companion.getDocAdapter4());
                            DocRetrieval.Companion.getPureAdapters().add(DocRetrieval.Companion.getDocAdapter5());
                            DocRetrieval.Companion.getPureAdapters().add(DocRetrieval.Companion.getDocAdapter6());
                            DocRetrieval.Companion.getPureAdapters().add(DocRetrieval.Companion.getDocAdapter7());


                            try {

                                holder.docDisplayRV.setAdapter(DocRetrieval.Companion.getPureAdapters().get(holder.getAbsoluteAdapterPosition()));
                                holder.docTags.setAdapter(DocRetrieval.Companion.getPureAppTagsArray().get(holder.getAbsoluteAdapterPosition()));

                                holder.departmentIndicator.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        try {
                                            holder.docDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getPureAdapters().get(holder.getAbsoluteAdapterPosition()).docPosition++);
                                            holder.docTags.smoothScrollToPosition(DocRetrieval.Companion.getPureAppTagsArray().get(holder.getAbsoluteAdapterPosition()).getTagPosition() + 1);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }
                break;
                case "Visual & Performing Art": {

                    departmentsArray = DocSorting.getSubFields(13);
                    schoolDepartments.addAll(Arrays.asList(departmentsArray));

                    publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            documents = new ArrayList<>();
                            for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                SelectedDoc document = qds.toObject(SelectedDoc.class);
                                documents.add(document);
                            }

                            DocRetrieval.Companion.setVisualArtTags0(new ArrayList<>());
                            DocRetrieval.Companion.setVisualArtTags1(new ArrayList<>());
                            DocRetrieval.Companion.setVisualArtTags2(new ArrayList<>());

                            DocRetrieval.Companion.setVisualArt0(new ArrayList<>());
                            DocRetrieval.Companion.setVisualArt1(new ArrayList<>());
                            DocRetrieval.Companion.setVisualArt2(new ArrayList<>());

                            for (SelectedDoc document : documents) {

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(13)[0])) {
                                    DocRetrieval.Companion.getVisualArt0().add(document);
                                    DocRetrieval.Companion.setVisualAdapter0(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getVisualArt0()));
                                    DocRetrieval.Companion.getVisualAdapter0().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getVisualArtTags0().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getVisualArtTags0().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getVisualArtTags0(), null, DocRetrieval.Companion.getVisualAdapter0());
                                    subFieldAdt0.notifyDataSetChanged();

                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(13)[1])) {
                                    DocRetrieval.Companion.getVisualArt0().add(document);
                                    DocRetrieval.Companion.setVisualAdapter1(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getVisualArt1()));
                                    DocRetrieval.Companion.getVisualAdapter1().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getVisualArtTags1().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getVisualArtTags1().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getVisualArtTags1(), null, DocRetrieval.Companion.getVisualAdapter1());
                                    subFieldAdt1.notifyDataSetChanged();

                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(13)[2])) {
                                    DocRetrieval.Companion.getVisualArt0().add(document);
                                    DocRetrieval.Companion.setVisualAdapter2(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getVisualArt2()));
                                    DocRetrieval.Companion.getVisualAdapter2().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getVisualArtTags2().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getVisualArtTags2().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getVisualArtTags2(), null, DocRetrieval.Companion.getVisualAdapter2());
                                    subFieldAdt2.notifyDataSetChanged();
                                }

                            }

                            holder.readProgress.setVisibility(View.INVISIBLE);

                            DocRetrieval.Companion.getVisualArtTagsArray().add(subFieldAdt0);
                            DocRetrieval.Companion.getVisualArtTagsArray().add(subFieldAdt1);
                            DocRetrieval.Companion.getVisualArtTagsArray().add(subFieldAdt2);

                            DocRetrieval.Companion.getVisualAdapters().add(DocRetrieval.Companion.getVisualAdapter0());
                            DocRetrieval.Companion.getVisualAdapters().add(DocRetrieval.Companion.getVisualAdapter1());
                            DocRetrieval.Companion.getVisualAdapters().add(DocRetrieval.Companion.getVisualAdapter2());

                            try {

                                holder.docDisplayRV.setAdapter(DocRetrieval.Companion.getVisualAdapters().get(position));
                                holder.docTags.setAdapter(DocRetrieval.Companion.getVisualArtTagsArray().get(position));

                                holder.departmentIndicator.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        try {
                                            holder.docDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getVisualAdapters().get(holder.getAbsoluteAdapterPosition()).docPosition++);
                                            holder.docTags.smoothScrollToPosition(DocRetrieval.Companion.getVisualArtTagsArray().get(holder.getAbsoluteAdapterPosition()).getTagPosition() + 1);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                break;
                case "Confucius Institute": {

                    departmentsArray = DocSorting.getSubFields(14);
                    schoolDepartments.addAll(Arrays.asList(departmentsArray));

                    publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                SelectedDoc document = qds.toObject(SelectedDoc.class);
                                documents.add(document);
                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                break;
                case "Peace & Security Studies": {
                    departmentsArray = DocSorting.getSubFields(15);
                    schoolDepartments.addAll(Arrays.asList(departmentsArray));

                    publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                SelectedDoc document = qds.toObject(SelectedDoc.class);
                                documents.add(document);
                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }
                break;
                case "Creative Arts, Film & Media Studies": {

                    departmentsArray = DocSorting.getSubFields(16);
                    schoolDepartments.addAll(Arrays.asList(departmentsArray));

                    publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            documents = new ArrayList<>();
                            for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                SelectedDoc document = qds.toObject(SelectedDoc.class);
                                documents.add(document);
                            }

                            DocRetrieval.Companion.setCreativeArtTags0(new ArrayList<>());
                            DocRetrieval.Companion.setCreativeArtTags1(new ArrayList<>());

                            DocRetrieval.Companion.setCreativeArt0(new ArrayList<>());
                            DocRetrieval.Companion.setCreativeArt1(new ArrayList<>());

                            for (SelectedDoc document : documents) {
                                if (document.getDocMetaData().contains(DocSorting.getSubFields(16)[0])) {
                                    DocRetrieval.Companion.getCreativeArt0().add(document);
                                    DocRetrieval.Companion.setCreativeArtAdapter0(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getCreativeArt0()));
                                    DocRetrieval.Companion.getCreativeArtAdapter0().notifyDataSetChanged();


                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getCreativeArtTags0().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getCreativeArtTags0().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getCreativeArtTags0(), null, DocRetrieval.Companion.getCreativeArtAdapter0());
                                    subFieldAdt0.notifyDataSetChanged();
                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(16)[1])) {
                                    DocRetrieval.Companion.getCreativeArt1().add(document);
                                    DocRetrieval.Companion.setCreativeArtAdapter1(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getCreativeArt1()));
                                    DocRetrieval.Companion.getCreativeArtAdapter1().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getCreativeArtTags1().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getCreativeArtTags1().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getCreativeArtTags1(), null, DocRetrieval.Companion.getCreativeArtAdapter1());
                                    subFieldAdt1.notifyDataSetChanged();
                                }

                            }

                            holder.readProgress.setVisibility(View.INVISIBLE);

                            DocRetrieval.Companion.getCreativeArtTagsArray().add(subFieldAdt0);
                            DocRetrieval.Companion.getCreativeArtTagsArray().add(subFieldAdt1);

                            DocRetrieval.Companion.getCreativeArtAdapters().add(DocRetrieval.Companion.getCreativeArtAdapter0());
                            DocRetrieval.Companion.getCreativeArtAdapters().add(DocRetrieval.Companion.getCreativeArtAdapter1());


                            try {

                                holder.docDisplayRV.setAdapter(DocRetrieval.Companion.getCreativeArtAdapters().get(holder.getAbsoluteAdapterPosition()));
                                holder.docTags.setAdapter(DocRetrieval.Companion.getCreativeArtTagsArray().get(holder.getAbsoluteAdapterPosition()));

                                holder.departmentIndicator.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        try {
                                            holder.docDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getCreativeArtAdapters().get(holder.getAbsoluteAdapterPosition()).docPosition++);
                                            holder.docTags.smoothScrollToPosition(DocRetrieval.Companion.getCreativeArtTagsArray().get(holder.getAbsoluteAdapterPosition()).getTagPosition() + 1);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                break;
                case "Architecture": {
                    departmentsArray = DocSorting.getSubFields(17);
                    schoolDepartments.addAll(Arrays.asList(departmentsArray));
                    publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            documents = new ArrayList<>();

                            for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                SelectedDoc document = qds.toObject(SelectedDoc.class);
                                documents.add(document);
                            }

                            DocRetrieval.Companion.setArchitectureTags0(new ArrayList<>());
                            DocRetrieval.Companion.setArchitectureTags1(new ArrayList<>());
                            DocRetrieval.Companion.setArchitectureTags2(new ArrayList<>());

                            DocRetrieval.Companion.setArchitecture0(new ArrayList<>());
                            DocRetrieval.Companion.setArchitecture1(new ArrayList<>());
                            DocRetrieval.Companion.setArchitecture2(new ArrayList<>());

                            for (SelectedDoc document : documents) {
                                if (document.getDocMetaData().contains(DocSorting.getSubFields(17)[0])) {
                                    DocRetrieval.Companion.getArchitecture0().add(document);
                                    DocRetrieval.Companion.setArchitectureAdapter0(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getArchitecture0()));
                                    DocRetrieval.Companion.getArchitectureAdapter0().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getArchitectureTags0().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getArchitectureTags0().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getArchitectureTags0(), null, DocRetrieval.Companion.getArchitectureAdapter0());
                                    subFieldAdt0.notifyDataSetChanged();

                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(17)[1])) {
                                    DocRetrieval.Companion.getArchitecture1().add(document);
                                    DocRetrieval.Companion.setArchitectureAdapter1(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getArchitecture1()));
                                    DocRetrieval.Companion.getArchitectureAdapter1().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getArchitectureTags1().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getArchitectureTags1().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getArchitectureTags1(), null, DocRetrieval.Companion.getArchitectureAdapter1());
                                    subFieldAdt1.notifyDataSetChanged();

                                }

                                if (document.getDocMetaData().contains(DocSorting.getSubFields(17)[2])) {
                                    DocRetrieval.Companion.getArchitecture2().add(document);
                                    DocRetrieval.Companion.setArchitectureAdapter2(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getArchitecture2()));
                                    DocRetrieval.Companion.getArchitectureAdapter2().notifyDataSetChanged();

                                    String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
                                    if (tags.split(",").length > 0) {
                                        ArrayList<String> tagsProcessor = new ArrayList<>();
                                        Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
                                        for (int i = 0; i < tagsProcessor.size(); i++) {
                                            if (!(DocRetrieval.Companion.getArchitectureTags2().contains(tagsProcessor.get(i)))) {
                                                DocRetrieval.Companion.getArchitectureTags2().add(tagsProcessor.get(i));
                                            }
                                        }
                                    }
                                    subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getArchitectureTags2(), null, DocRetrieval.Companion.getArchitectureAdapter2());
                                    subFieldAdt2.notifyDataSetChanged();
                                }

                            }

                            holder.readProgress.setVisibility(View.INVISIBLE);

                            DocRetrieval.Companion.getArchitectureTagsArray().add(subFieldAdt0);
                            DocRetrieval.Companion.getArchitectureTagsArray().add(subFieldAdt1);
                            DocRetrieval.Companion.getArchitectureTagsArray().add(subFieldAdt2);

                            DocRetrieval.Companion.getArchitectureAdapters().add(DocRetrieval.Companion.getArchitectureAdapter0());
                            DocRetrieval.Companion.getArchitectureAdapters().add(DocRetrieval.Companion.getArchitectureAdapter1());
                            DocRetrieval.Companion.getArchitectureAdapters().add(DocRetrieval.Companion.getArchitectureAdapter2());


                            try {

                                holder.docDisplayRV.setAdapter(DocRetrieval.Companion.getArchitectureAdapters().get(holder.getAbsoluteAdapterPosition()));
                                holder.docTags.setAdapter(DocRetrieval.Companion.getArchitectureTagsArray().get(holder.getAbsoluteAdapterPosition()));

                                holder.departmentIndicator.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        try {
                                            holder.docDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getArchitectureAdapters().get(holder.getAbsoluteAdapterPosition()).docPosition++);
                                            holder.docTags.smoothScrollToPosition(DocRetrieval.Companion.getArchitectureTagsArray().get(holder.getAbsoluteAdapterPosition()).getTagPosition() + 1);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;

            }

            //YEAR FILTER
            final ArrayAdapter<CharSequence> yearFilters = ArrayAdapter.createFromResource(c,
                    R.array.yearFilter, R.layout.spinner_drop_down_yangu1);
            yearFilters.setDropDownViewResource(R.layout.spinner_drop_down_yangu1);

            holder.docDisplayRV.setLayoutManager(new LinearLayoutManager(c, RecyclerView.HORIZONTAL, false));
            if (documentAdapters.size() != 0) {
                holder.docDisplayRV.setAdapter(documentAdapters.get(position));
            }
            holder.docTags.setLayoutManager(new LinearLayoutManager(c, RecyclerView.HORIZONTAL, false));

            holder.departmentIndicator.setText(departments.get(position));

        }

//    else
//            if (flag.equals("SchoolDptDocuments")) {
//        holder.addDocBtn.bringToFront();
//        holder.addDocBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(c, SourceDocList.class);
//                intent.putExtra("addDoc", "addDoc");
//                intent.putExtra("SchoolName", dfd);
//                intent.putExtra("Department", deptTitle);
//                c.startActivity(intent);
//                Toast.makeText(c, dfd + ":\n" + deptTitle, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        switch (dfd) {
//
//            case "Agriculture & Enterprise Development": {
//
//                departmentsArray = DocSorting.getSubFields(0);
//                schoolDepartments.addAll(Arrays.asList(departmentsArray));
//                publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        DocRetrieval.Companion.setAgrTags0(new ArrayList<>());
//                        DocRetrieval.Companion.setAgrTags1(new ArrayList<>());
//                        DocRetrieval.Companion.setAgrTags2(new ArrayList<>());
//
//                        DocRetrieval.Companion.setAgr0(new ArrayList<>());
//                        DocRetrieval.Companion.setAgr1(new ArrayList<>());
//                        DocRetrieval.Companion.setAgr2(new ArrayList<>());
//
//                        documents = new ArrayList<>();
//                        //Save the school object
//                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
//                            SelectedDoc document = qds.toObject(SelectedDoc.class);
//                            documents.add(document);
//                        }
//
//                        documentsList = new ArrayList<>(documents);
//
//
//                        for (SelectedDoc document : documents) {
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(0)[0])) {
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                DocRetrieval.Companion.getAgr0().add(document);
//                                DocRetrieval.Companion.setAgrAdapter0(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getAgr0()));
//
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getAgrTags0().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getAgrTags0().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getAgrTags0());
//                                subFieldAdt0.notifyDataSetChanged();
//                                DocRetrieval.Companion.getAgrAdapter0().notifyDataSetChanged();
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(0)[1])) {
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                DocRetrieval.Companion.getAgr1().add(document);
//                                DocRetrieval.Companion.setAgrAdapter1(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getAgr1()));
//
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getAgrTags1().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getAgrTags1().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getAgrTags1());
//                                subFieldAdt1.notifyDataSetChanged();
//                                DocRetrieval.Companion.getAgrAdapter1().notifyDataSetChanged();
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(0)[2])) {
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                DocRetrieval.Companion.getAgr2().add(document);
//                                DocRetrieval.Companion.setAgrAdapter2(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getAgr2()));
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getAgrTags2().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getAgrTags2().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getAgrTags0());
//                                subFieldAdt2.notifyDataSetChanged();
//                                DocRetrieval.Companion.getAgrAdapter2().notifyDataSetChanged();
//                            }
//                        }
//
//                        holder.readProgress.setVisibility(View.INVISIBLE);
//                        DocRetrieval.Companion.getAgrTagsArray().add(subFieldAdt0);
//                        DocRetrieval.Companion.getAgrTagsArray().add(subFieldAdt1);
//                        DocRetrieval.Companion.getAgrTagsArray().add(subFieldAdt2);
//
//                        DocRetrieval.Companion.getAgrAdapters().add(DocRetrieval.Companion.getAgrAdapter0());
//                        DocRetrieval.Companion.getAgrAdapters().add(DocRetrieval.Companion.getAgrAdapter1());
//                        DocRetrieval.Companion.getAgrAdapters().add(DocRetrieval.Companion.getAgrAdapter2());
//
//
//                        try {
//                            holder.docDisplayRV.setAdapter(DocRetrieval.Companion.getAgrAdapters().get(holder.getAbsoluteAdapterPosition()));
//                            holder.docTags.setAdapter(DocRetrieval.Companion.getAgrTagsArray().get(holder.getAbsoluteAdapterPosition()));
//                            holder.departmentIndicator.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    holder.docDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getAgrAdapters().get(holder.getAbsoluteAdapterPosition()).docPosition++);
//                                    holder.docTags.smoothScrollToPosition(DocRetrieval.Companion.getAgrTagsArray().get(holder.getAbsoluteAdapterPosition()).getTagPosition() + 1);
//                                }
//                            });
//
//                            searchDocs.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                                @Override
//                                public boolean onQueryTextSubmit(String query) {
//                                    return false;
//                                }
//
//                                @Override
//                                public boolean onQueryTextChange(String newText) {
//                                    for (DocumentAdapter a : DocRetrieval.Companion.getAgrAdapters()) {
//                                        if (a != null) {
//                                            a.getFilter().filter(newText);
//                                        }
//                                    }
//                                    for (SubFieldAdt subFieldAdt : DocRetrieval.Companion.getAgrTagsArray()) {
//                                        if (subFieldAdt != null) {
//                                            subFieldAdt.getFilter().filter(newText);
//                                        }
//                                    }
//                                    return false;
//                                }
//                            });
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                });
//
//            }
//            break;
//            case "Applied Human Sciences": {
//                departmentsArray = DocSorting.getSubFields(1);
//                schoolDepartments.addAll(Arrays.asList(departmentsArray));
//                publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        DocRetrieval.Companion.setAppHumanSciTags0(new ArrayList<>());
//                        DocRetrieval.Companion.setAppHumanSciTags1(new ArrayList<>());
//                        DocRetrieval.Companion.setAppHumanSciTags2(new ArrayList<>());
//                        DocRetrieval.Companion.setAppHumanSciTags3(new ArrayList<>());
//                        DocRetrieval.Companion.setAppHumanSciTags4(new ArrayList<>());
//
//                        DocRetrieval.Companion.setAppHumanSci0(new ArrayList<>());
//                        DocRetrieval.Companion.setAppHumanSci1(new ArrayList<>());
//                        DocRetrieval.Companion.setAppHumanSci2(new ArrayList<>());
//                        DocRetrieval.Companion.setAppHumanSci3(new ArrayList<>());
//                        DocRetrieval.Companion.setAppHumanSci4(new ArrayList<>());
//
//                        subFieldAdts1 = new ArrayList<>();
//                        documents = new ArrayList<>();
//                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
//                            SelectedDoc document = qds.toObject(SelectedDoc.class);
//                            documents.add(document);
//                        }
//
//
//                        for (SelectedDoc document : documents) {
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(1)[0])) {
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                DocRetrieval.Companion.getAppHumanSci0().add(document);
//                                DocRetrieval.Companion.setAppHumanAdapter0(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getAppHumanSci0()));
//
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getAppHumanSciTags0().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getAppHumanSciTags0().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                DocRetrieval.Companion.getAppHumanAdapter0().notifyDataSetChanged();
//                                subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getAppHumanSciTags0());
//                                subFieldAdt0.notifyDataSetChanged();
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(1)[1])) {
//                                DocRetrieval.Companion.getAppHumanSci1().add(document);
//                                DocRetrieval.Companion.setAppHumanAdapter1(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getAppHumanSci1()));
//
//                                if (!(document.getDocMetaData().split("_-_")[5].split(",").length == 0)) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getAppHumanSciTags1().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getAppHumanSciTags1().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                DocRetrieval.Companion.getAppHumanAdapter1().notifyDataSetChanged();
//                                subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getAppHumanSciTags1());
//                                subFieldAdt1.notifyDataSetChanged();
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(1)[2])) {
//                                DocRetrieval.Companion.getAppHumanSci2().add(document);
//                                DocRetrieval.Companion.setAppHumanAdapter2(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getAppHumanSci2()));
//
//                                if (!(document.getDocMetaData().split("_-_")[5].split(",").length == 0)) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getAppHumanSciTags2().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getAppHumanSciTags2().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                DocRetrieval.Companion.getAppHumanAdapter2().notifyDataSetChanged();
//                                subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getAppHumanSciTags2());
//                                subFieldAdt2.notifyDataSetChanged();
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(1)[3])) {
//                                DocRetrieval.Companion.getAppHumanSci3().add(document);
//                                DocRetrieval.Companion.setAppHumanAdapter3(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getAppHumanSci3()));
//
//                                if (!(document.getDocMetaData().split("_-_")[5].split(",").length == 0)) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getAppHumanSciTags3().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getAppHumanSciTags3().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                DocRetrieval.Companion.getAppHumanAdapter3().notifyDataSetChanged();
//                                subFieldAdt3 = new SubFieldAdt(c, DocRetrieval.Companion.getAppHumanSciTags3());
//                                subFieldAdt3.notifyDataSetChanged();
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(1)[4])) {
//                                DocRetrieval.Companion.getAppHumanSci4().add(document);
//                                DocRetrieval.Companion.setAppHumanAdapter4(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getAppHumanSci4()));
//
//                                if (!(document.getDocMetaData().split("_-_")[5].split(",").length == 0)) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getAppHumanSciTags4().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getAppHumanSciTags4().add(tagsProcessor.get(i).replace("[", "").replace("]", ""));
//                                        }
//                                    }
//                                }
//                                DocRetrieval.Companion.getAppHumanAdapter4().notifyDataSetChanged();
//                                subFieldAdt4 = new SubFieldAdt(c, DocRetrieval.Companion.getAppHumanSciTags4());
//                                subFieldAdt4.notifyDataSetChanged();
//                            }
//                        }
//                        DocRetrieval.Companion.getAppHumanAdapters().add(DocRetrieval.Companion.getAppHumanAdapter0());
//                        DocRetrieval.Companion.getAppHumanAdapters().add(DocRetrieval.Companion.getAppHumanAdapter1());
//                        DocRetrieval.Companion.getAppHumanAdapters().add(DocRetrieval.Companion.getAppHumanAdapter2());
//                        DocRetrieval.Companion.getAppHumanAdapters().add(DocRetrieval.Companion.getAppHumanAdapter3());
//                        DocRetrieval.Companion.getAppHumanAdapters().add(DocRetrieval.Companion.getAppHumanAdapter4());
//
//                        DocRetrieval.Companion.getAppHumanSciTagsArray().add(subFieldAdt0);
//                        DocRetrieval.Companion.getAppHumanSciTagsArray().add(subFieldAdt1);
//                        DocRetrieval.Companion.getAppHumanSciTagsArray().add(subFieldAdt2);
//                        DocRetrieval.Companion.getAppHumanSciTagsArray().add(subFieldAdt3);
//                        DocRetrieval.Companion.getAppHumanSciTagsArray().add(subFieldAdt4);
//
//
//                        holder.readProgress.setVisibility(View.INVISIBLE);
//
//                        try {
//                            holder.docDisplayRV.setAdapter(DocRetrieval.Companion.getAppHumanAdapters().get(holder.getAbsoluteAdapterPosition()));
//                            holder.docTags.setAdapter(DocRetrieval.Companion.getAppHumanSciTagsArray().get(holder.getAbsoluteAdapterPosition()));
//
//                            holder.departmentIndicator.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    holder.docDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getAppHumanAdapters().get(holder.getAbsoluteAdapterPosition()).docPosition++);
//                                    holder.docTags.smoothScrollToPosition(DocRetrieval.Companion.getAppHumanSciTagsArray().get(holder.getAbsoluteAdapterPosition()).getTagPosition() + 1);
//                                }
//                            });
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                        searchDocs.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                            @Override
//                            public boolean onQueryTextSubmit(String query) {
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onQueryTextChange(String newText) {
//                                for (DocumentAdapter a : DocRetrieval.Companion.getAppHumanAdapters()) {
//                                    if (a != null) {
//                                        a.getFilter().filter(newText);
//                                    }
//                                }
//                                for (SubFieldAdt subFieldAdt : DocRetrieval.Companion.getAppHumanSciTagsArray()) {
//                                    if (subFieldAdt != null) {
//                                        subFieldAdt.getFilter().filter(newText);
//                                    }
//                                }
//                                return false;
//                            }
//                        });
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//
//            }
//            break;
//            case "Business": {
//
//                departmentsArray = DocSorting.getSubFields(2);
//                schoolDepartments.addAll(Arrays.asList(departmentsArray));
//                publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        DocRetrieval.Companion.setBizTags0(new ArrayList<>());
//                        DocRetrieval.Companion.setBizTags1(new ArrayList<>());
//                        DocRetrieval.Companion.setBizTags2(new ArrayList<>());
//
//                        DocRetrieval.Companion.setBiz0(new ArrayList<>());
//                        DocRetrieval.Companion.setBiz1(new ArrayList<>());
//                        DocRetrieval.Companion.setBiz2(new ArrayList<>());
//
//                        documents = new ArrayList<>();
//                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
//                            SelectedDoc document = qds.toObject(SelectedDoc.class);
//                            documents.add(document);
//
//                        }
//
//                        for (SelectedDoc document : documents) {
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(2)[0])) {
//
//                                DocRetrieval.Companion.getBiz0().add(document);
//                                DocRetrieval.Companion.setBizAdapter0(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getBiz0()));
//                                DocRetrieval.Companion.getBizAdapter0().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getBizTags0().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getBizTags0().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getBizTags0());
//                                subFieldAdt0.notifyDataSetChanged();
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(2)[1])) {
//                                DocRetrieval.Companion.getBiz1().add(document);
//                                DocRetrieval.Companion.setBizAdapter1(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getBiz1()));
//                                DocRetrieval.Companion.getBizAdapter1().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getBizTags1().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getBizTags1().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getBizTags1());
//                                subFieldAdt1.notifyDataSetChanged();
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(2)[2])) {
//                                DocRetrieval.Companion.getBiz2().add(document);
//                                DocRetrieval.Companion.setBizAdapter2(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getBiz2()));
//                                DocRetrieval.Companion.getBizAdapter2().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getBizTags2().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getBizTags2().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getBizTags1());
//                                subFieldAdt2.notifyDataSetChanged();
//                            }
//                        }
//
//                        holder.readProgress.setVisibility(View.INVISIBLE);
//
//                        DocRetrieval.Companion.getBizTagsArray().add(subFieldAdt0);
//                        DocRetrieval.Companion.getBizTagsArray().add(subFieldAdt1);
//                        DocRetrieval.Companion.getBizTagsArray().add(subFieldAdt2);
//
//
//                        DocRetrieval.Companion.getBizAdapters().add(DocRetrieval.Companion.getBizAdapter0());
//                        DocRetrieval.Companion.getBizAdapters().add(DocRetrieval.Companion.getBizAdapter1());
//                        DocRetrieval.Companion.getBizAdapters().add(DocRetrieval.Companion.getBizAdapter2());
//
//
//                        try {
//
//                            holder.docDisplayRV.setAdapter(DocRetrieval.Companion.getBizAdapters().get(holder.getAbsoluteAdapterPosition()));
//                            holder.docTags.setAdapter(DocRetrieval.Companion.getBizTagsArray().get(holder.getAbsoluteAdapterPosition()));
//
//                            holder.departmentIndicator.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    holder.docDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getBizAdapters().get(holder.getAbsoluteAdapterPosition()).docPosition++);
//                                    holder.docTags.smoothScrollToPosition(DocRetrieval.Companion.getBizTagsArray().get(holder.getAbsoluteAdapterPosition()).getTagPosition() + 1);
//                                }
//                            });
//
//                            searchDocs.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                                @Override
//                                public boolean onQueryTextSubmit(String query) {
//                                    return false;
//                                }
//
//                                @Override
//                                public boolean onQueryTextChange(String newText) {
//                                    for (DocumentAdapter a : DocRetrieval.Companion.getBizAdapters()) {
//                                        if (a != null) {
//                                            a.getFilter().filter(newText);
//                                        }
//                                    }
//                                    for (SubFieldAdt subFieldAdt : DocRetrieval.Companion.getBizTagsArray()) {
//                                        if (subFieldAdt != null) {
//                                            subFieldAdt.getFilter().filter(newText);
//                                        }
//                                    }
//                                    return false;
//                                }
//                            });
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                });
//
//            }
//            break;
//            case "Economics": {
//
//                departmentsArray = DocSorting.getSubFields(3);
//                schoolDepartments.addAll(Arrays.asList(departmentsArray));
//                publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        documents = new ArrayList<>();
//                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
//                            SelectedDoc document = qds.toObject(SelectedDoc.class);
//                            documents.add(document);
//
//                        }
//
//                        DocRetrieval.Companion.setEconTags0(new ArrayList<>());
//                        DocRetrieval.Companion.setEconTags1(new ArrayList<>());
//                        DocRetrieval.Companion.setEconTags2(new ArrayList<>());
//
//                        DocRetrieval.Companion.setEcon0(new ArrayList<>());
//                        DocRetrieval.Companion.setEcon1(new ArrayList<>());
//                        DocRetrieval.Companion.setEcon2(new ArrayList<>());
//
//                        for (SelectedDoc document : documents) {
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(3)[0])) {
//                                DocRetrieval.Companion.getEcon0().add(document);
//                                DocRetrieval.Companion.setEconAdapter0(new DocumentAdapter(thisSchool, fetchProgress,  context, c, DocRetrieval.Companion.getEcon0()));
//                                DocRetrieval.Companion.getEconAdapter0().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getEconTags0().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getEconTags0().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getEconTags0());
//                                subFieldAdt0.notifyDataSetChanged();
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(3)[1])) {
//                                DocRetrieval.Companion.getEcon1().add(document);
//                                DocRetrieval.Companion.setEconAdapter1(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEcon1()));
//                                DocRetrieval.Companion.getEconAdapter1().notifyDataSetChanged();
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(3)[2])) {
//                                DocRetrieval.Companion.getEcon2().add(document);
//                                DocRetrieval.Companion.setEconAdapter2(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEcon2()));
//                                DocRetrieval.Companion.getEconAdapter2().notifyDataSetChanged();
//                            }
//                        }
//                        holder.readProgress.setVisibility(View.INVISIBLE);
//                        DocRetrieval.Companion.getEconTagsArray().add(subFieldAdt0);
//                        DocRetrieval.Companion.getEconTagsArray().add(subFieldAdt1);
//                        DocRetrieval.Companion.getEconTagsArray().add(subFieldAdt2);
//
//                        DocRetrieval.Companion.getEconAdapters().add(DocRetrieval.Companion.getEconAdapter0());
//                        DocRetrieval.Companion.getEconAdapters().add(DocRetrieval.Companion.getEconAdapter1());
//                        DocRetrieval.Companion.getEconAdapters().add(DocRetrieval.Companion.getEconAdapter2());
//
//                        try {
//
//                            holder.docDisplayRV.setAdapter(DocRetrieval.Companion.getEconAdapters().get(position));
//                            holder.docTags.setAdapter(DocRetrieval.Companion.getEconTagsArray().get(position));
//
//                            holder.departmentIndicator.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    holder.docDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getEconAdapters().get(holder.getAbsoluteAdapterPosition()).docPosition++);
//                                    holder.docTags.smoothScrollToPosition(DocRetrieval.Companion.getEconTagsArray().get(holder.getAbsoluteAdapterPosition()).getTagPosition() + 1);
//                                }
//                            });
//
//                            searchDocs.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                                @Override
//                                public boolean onQueryTextSubmit(String query) {
//                                    return false;
//                                }
//
//                                @Override
//                                public boolean onQueryTextChange(String newText) {
//                                    for (DocumentAdapter a : DocRetrieval.Companion.getEconAdapters()) {
//                                        if (a != null) {
//                                            a.getFilter().filter(newText);
//                                        }
//                                    }
//                                    for (SubFieldAdt subFieldAdt : DocRetrieval.Companion.getEconTagsArray()) {
//                                        if (subFieldAdt != null) {
//                                            subFieldAdt.getFilter().filter(newText);
//                                        }
//                                    }
//                                    return false;
//                                }
//                            });
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            }
//            break;
//            case "Education": {
//                departmentsArray = DocSorting.getSubFields(4);
//                schoolDepartments.addAll(Arrays.asList(departmentsArray));
//
//                publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        documents = new ArrayList<>();
//                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
//                            SelectedDoc document = qds.toObject(SelectedDoc.class);
//                            documents.add(document);
//
//                        }
//
//                        DocRetrieval.Companion.setEduTags0(new ArrayList<>());
//                        DocRetrieval.Companion.setEduTags1(new ArrayList<>());
//                        DocRetrieval.Companion.setEduTags2(new ArrayList<>());
//                        DocRetrieval.Companion.setEduTags3(new ArrayList<>());
//                        DocRetrieval.Companion.setEduTags4(new ArrayList<>());
//                        DocRetrieval.Companion.setEduTags5(new ArrayList<>());
//                        DocRetrieval.Companion.setEduTags6(new ArrayList<>());
//
//                        DocRetrieval.Companion.setEducation0(new ArrayList<>());
//                        DocRetrieval.Companion.setEducation1(new ArrayList<>());
//                        DocRetrieval.Companion.setEducation2(new ArrayList<>());
//                        DocRetrieval.Companion.setEducation3(new ArrayList<>());
//                        DocRetrieval.Companion.setEducation4(new ArrayList<>());
//                        DocRetrieval.Companion.setEducation5(new ArrayList<>());
//                        DocRetrieval.Companion.setEducation6(new ArrayList<>());
//
//                        for (SelectedDoc document : documents) {
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(4)[0])) {
//                                DocRetrieval.Companion.getEducation0().add(document);
//                                DocRetrieval.Companion.setEduAdapter0(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEducation0()));
//                                DocRetrieval.Companion.getEduAdapter0().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getEduTags0().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getEduTags0().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getEduTags0());
//                                subFieldAdt0.notifyDataSetChanged();
//
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(4)[1])) {
//                                DocRetrieval.Companion.getEducation1().add(document);
//                                DocRetrieval.Companion.setEduAdapter1(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEducation1()));
//                                DocRetrieval.Companion.getEduAdapter1().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getEduTags1().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getEduTags1().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getEduTags1());
//                                subFieldAdt1.notifyDataSetChanged();
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(4)[2])) {
//                                DocRetrieval.Companion.getEducation2().add(document);
//                                DocRetrieval.Companion.setEduAdapter2(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEducation2()));
//                                DocRetrieval.Companion.getEduAdapter2().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getEduTags2().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getEduTags2().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getEduTags2());
//                                subFieldAdt2.notifyDataSetChanged();
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(4)[3])) {
//                                DocRetrieval.Companion.getEducation3().add(document);
//                                DocRetrieval.Companion.setEduAdapter3(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEducation3()));
//                                DocRetrieval.Companion.getEduAdapter3().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getEduTags3().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getEduTags3().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt3 = new SubFieldAdt(c, DocRetrieval.Companion.getEduTags3());
//                                subFieldAdt3.notifyDataSetChanged();
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(4)[4])) {
//                                DocRetrieval.Companion.getEducation4().add(document);
//                                DocRetrieval.Companion.setEduAdapter4(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEducation4()));
//                                DocRetrieval.Companion.getEduAdapter4().notifyDataSetChanged();
//
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getEduTags4().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getEduTags4().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt4 = new SubFieldAdt(c, DocRetrieval.Companion.getEduTags4());
//                                subFieldAdt4.notifyDataSetChanged();
//
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(4)[5])) {
//                                DocRetrieval.Companion.getEducation5().add(document);
//                                DocRetrieval.Companion.setEduAdapter5(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEducation5()));
//                                DocRetrieval.Companion.getEduAdapter5().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getEduTags5().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getEduTags5().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt5 = new SubFieldAdt(c, DocRetrieval.Companion.getEduTags5());
//                                subFieldAdt5.notifyDataSetChanged();
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(4)[6])) {
//                                DocRetrieval.Companion.getEducation6().add(document);
//                                DocRetrieval.Companion.setEduAdapter6(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEducation6()));
//                                DocRetrieval.Companion.getEduAdapter6().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getEduTags6().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getEduTags6().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt6 = new SubFieldAdt(c, DocRetrieval.Companion.getEduTags6());
//                                subFieldAdt6.notifyDataSetChanged();
//                            }
//
//                        }
//
//                        holder.readProgress.setVisibility(View.INVISIBLE);
//                        DocRetrieval.Companion.getEducationTagsArray().add(subFieldAdt0);
//                        DocRetrieval.Companion.getEducationTagsArray().add(subFieldAdt6);
//                        DocRetrieval.Companion.getEducationTagsArray().add(subFieldAdt0);
//                        DocRetrieval.Companion.getEducationTagsArray().add(subFieldAdt3);
//                        DocRetrieval.Companion.getEducationTagsArray().add(subFieldAdt4);
//                        DocRetrieval.Companion.getEducationTagsArray().add(subFieldAdt5);
//
//                        DocRetrieval.Companion.getEduAdapters().add(DocRetrieval.Companion.getEduAdapter0());
//                        DocRetrieval.Companion.getEduAdapters().add(DocRetrieval.Companion.getEduAdapter6());
//                        DocRetrieval.Companion.getEduAdapters().add(DocRetrieval.Companion.getEduAdapter0());
//                        DocRetrieval.Companion.getEduAdapters().add(DocRetrieval.Companion.getEduAdapter3());
//                        DocRetrieval.Companion.getEduAdapters().add(DocRetrieval.Companion.getEduAdapter4());
//                        DocRetrieval.Companion.getEduAdapters().add(DocRetrieval.Companion.getEduAdapter5());
//
//                        try {
//
//                            holder.docDisplayRV.setAdapter(DocRetrieval.Companion.getEduAdapters().get(position));
//                            holder.docTags.setAdapter(DocRetrieval.Companion.getEducationTagsArray().get(position));
//
//                            holder.departmentIndicator.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    holder.docDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getEduAdapters().get(holder.getAbsoluteAdapterPosition()).docPosition++);
//                                    holder.docTags.smoothScrollToPosition(DocRetrieval.Companion.getEducationTagsArray().get(holder.getAbsoluteAdapterPosition()).getTagPosition() + 1);
//                                }
//                            });
//
//                            searchDocs.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                                @Override
//                                public boolean onQueryTextSubmit(String query) {
//                                    return false;
//                                }
//
//                                @Override
//                                public boolean onQueryTextChange(String newText) {
//                                    for (DocumentAdapter a : DocRetrieval.Companion.getEduAdapters()) {
//                                        if (a != null) {
//                                            a.getFilter().filter(newText);
//                                        }
//                                    }
//                                    for (SubFieldAdt subFieldAdt : DocRetrieval.Companion.getEducationTagsArray()) {
//                                        if (subFieldAdt != null) {
//                                            subFieldAdt.getFilter().filter(newText);
//                                        }
//                                    }
//                                    return false;
//                                }
//                            });
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            }
//            break;
//            case "Engineering & Technology": {
//
//                departmentsArray = DocSorting.getSubFields(5);
//                schoolDepartments.addAll(Arrays.asList(departmentsArray));
//
//                publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        documents = new ArrayList<>();
//                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
//                            SelectedDoc document = qds.toObject(SelectedDoc.class);
//                            documents.add(document);
//
//                        }
//
//                        DocRetrieval.Companion.setEngTags0(new ArrayList<>());
//                        DocRetrieval.Companion.setEngTags1(new ArrayList<>());
//                        DocRetrieval.Companion.setEngTags2(new ArrayList<>());
//                        DocRetrieval.Companion.setEngTags3(new ArrayList<>());
//                        DocRetrieval.Companion.setEngTags4(new ArrayList<>());
//                        DocRetrieval.Companion.setEngTags5(new ArrayList<>());
//
//                        DocRetrieval.Companion.setEngineering0(new ArrayList<>());
//                        DocRetrieval.Companion.setEngineering1(new ArrayList<>());
//                        DocRetrieval.Companion.setEngineering2(new ArrayList<>());
//                        DocRetrieval.Companion.setEngineering3(new ArrayList<>());
//                        DocRetrieval.Companion.setEngineering4(new ArrayList<>());
//                        DocRetrieval.Companion.setEngineering5(new ArrayList<>());
//
//                        for (SelectedDoc document : documents) {
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(5)[0])) {
//                                DocRetrieval.Companion.getEngineering0().add(document);
//                                DocRetrieval.Companion.setEngAdapter0(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEngineering0()));
//                                DocRetrieval.Companion.getEngAdapter0().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getEngTags0().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getEngTags0().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getEngTags0());
//                                subFieldAdt0.notifyDataSetChanged();
//
//
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(5)[1])) {
//                                DocRetrieval.Companion.getEngineering1().add(document);
//                                DocRetrieval.Companion.setEngAdapter1(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEngineering1()));
//                                DocRetrieval.Companion.getEngAdapter1().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getEngTags1().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getEngTags1().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getEngTags1());
//                                subFieldAdt1.notifyDataSetChanged();
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(5)[2])) {
//                                DocRetrieval.Companion.getEngineering2().add(document);
//                                DocRetrieval.Companion.setEngAdapter2(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEngineering2()));
//                                DocRetrieval.Companion.getEngAdapter2().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getEngTags2().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getEngTags2().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getEngTags2());
//                                subFieldAdt2.notifyDataSetChanged();
//
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(5)[3])) {
//                                DocRetrieval.Companion.getEngineering3().add(document);
//                                DocRetrieval.Companion.setEngAdapter3(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEngineering3()));
//                                DocRetrieval.Companion.getEngAdapter3().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getEngTags3().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getEngTags3().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt3 = new SubFieldAdt(c, DocRetrieval.Companion.getEngTags3());
//                                subFieldAdt3.notifyDataSetChanged();
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(5)[4])) {
//                                DocRetrieval.Companion.getEngineering4().add(document);
//                                DocRetrieval.Companion.setEngAdapter4(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEngineering4()));
//                                DocRetrieval.Companion.getEngAdapter4().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getEngTags4().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getEngTags4().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt4 = new SubFieldAdt(c, DocRetrieval.Companion.getEngTags4());
//                                subFieldAdt4.notifyDataSetChanged();
//
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(5)[5])) {
//                                DocRetrieval.Companion.getEngineering5().add(document);
//                                DocRetrieval.Companion.setEngAdapter5(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEngineering5()));
//                                DocRetrieval.Companion.getEngAdapter5().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getEngTags5().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getEngTags5().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt5 = new SubFieldAdt(c, DocRetrieval.Companion.getEngTags5());
//                                subFieldAdt5.notifyDataSetChanged();
//                            }
//                        }
//
//                        holder.readProgress.setVisibility(View.INVISIBLE);
//                        DocRetrieval.Companion.getEngTagsArray().add(subFieldAdt0);
//                        DocRetrieval.Companion.getEngTagsArray().add(subFieldAdt1);
//                        DocRetrieval.Companion.getEngTagsArray().add(subFieldAdt2);
//                        DocRetrieval.Companion.getEngTagsArray().add(subFieldAdt3);
//                        DocRetrieval.Companion.getEngTagsArray().add(subFieldAdt4);
//                        DocRetrieval.Companion.getEngTagsArray().add(subFieldAdt5);
//
//                        DocRetrieval.Companion.getEngAdapters().add(DocRetrieval.Companion.getEngAdapter0());
//                        DocRetrieval.Companion.getEngAdapters().add(DocRetrieval.Companion.getEngAdapter1());
//                        DocRetrieval.Companion.getEngAdapters().add(DocRetrieval.Companion.getEngAdapter2());
//                        DocRetrieval.Companion.getEngAdapters().add(DocRetrieval.Companion.getEngAdapter3());
//                        DocRetrieval.Companion.getEngAdapters().add(DocRetrieval.Companion.getEngAdapter4());
//                        DocRetrieval.Companion.getEngAdapters().add(DocRetrieval.Companion.getEngAdapter5());
//
//                        try {
//
//                            holder.docDisplayRV.setAdapter(DocRetrieval.Companion.getEngAdapters().get(holder.getAbsoluteAdapterPosition()));
//                            holder.docTags.setAdapter(DocRetrieval.Companion.getEngTagsArray().get(holder.getAbsoluteAdapterPosition()));
//
//                            holder.departmentIndicator.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    holder.docDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getEngAdapters().get(holder.getAbsoluteAdapterPosition()).docPosition++);
//                                    holder.docTags.smoothScrollToPosition(DocRetrieval.Companion.getEngTagsArray().get(holder.getAbsoluteAdapterPosition()).getTagPosition() + 1);
//                                }
//                            });
//
//                            searchDocs.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                                @Override
//                                public boolean onQueryTextSubmit(String query) {
//                                    return false;
//                                }
//
//                                @Override
//                                public boolean onQueryTextChange(String newText) {
//                                    for (DocumentAdapter a : DocRetrieval.Companion.getEngAdapters()) {
//                                        if (a != null) {
//                                            a.getFilter().filter(newText);
//                                        }
//                                    }
//                                    for (SubFieldAdt subFieldAdt : DocRetrieval.Companion.getEngTagsArray()) {
//                                        if (subFieldAdt != null) {
//                                            subFieldAdt.getFilter().filter(newText);
//                                        }
//                                    }
//                                    return false;
//                                }
//                            });
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            }
//            break;
//            case "Environmental Studies": {
//                departmentsArray = DocSorting.getSubFields(6);
//                schoolDepartments.addAll(Arrays.asList(departmentsArray));
//
//                publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        documents = new ArrayList<>();
//                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
//                            SelectedDoc document = qds.toObject(SelectedDoc.class);
//                            documents.add(document);
//                        }
//
//                        DocRetrieval.Companion.setEnvTags0(new ArrayList<>());
//                        DocRetrieval.Companion.setEnvTags1(new ArrayList<>());
//                        DocRetrieval.Companion.setEnvTags2(new ArrayList<>());
//                        DocRetrieval.Companion.setEnvTags3(new ArrayList<>());
//
//                        DocRetrieval.Companion.setEnvironmental0(new ArrayList<>());
//                        DocRetrieval.Companion.setEnvironmental1(new ArrayList<>());
//                        DocRetrieval.Companion.setEnvironmental2(new ArrayList<>());
//                        DocRetrieval.Companion.setEnvironmental3(new ArrayList<>());
//
//                        for (SelectedDoc document : documents) {
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(6)[0])) {
//                                DocRetrieval.Companion.getEnvironmental0().add(document);
//                                DocRetrieval.Companion.setEnvAdapter0(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEnvironmental0()));
//                                DocRetrieval.Companion.getEnvAdapter0().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getEnvTags0().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getEnvTags0().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getEnvTags0());
//                                subFieldAdt0.notifyDataSetChanged();
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(6)[1])) {
//                                DocRetrieval.Companion.getEnvironmental1().add(document);
//                                DocRetrieval.Companion.setEnvAdapter1(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEnvironmental1()));
//                                DocRetrieval.Companion.getEnvAdapter1().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getEnvTags1().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getEnvTags1().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getEnvTags1());
//                                subFieldAdt1.notifyDataSetChanged();
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(6)[2])) {
//                                DocRetrieval.Companion.getEnvironmental2().add(document);
//                                DocRetrieval.Companion.setEnvAdapter2(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEnvironmental2()));
//                                DocRetrieval.Companion.getEnvAdapter2().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getEnvTags2().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getEnvTags2().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getEnvTags2());
//                                subFieldAdt2.notifyDataSetChanged();
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(6)[3])) {
//                                DocRetrieval.Companion.getEnvironmental3().add(document);
//                                DocRetrieval.Companion.setEnvAdapter3(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getEnvironmental3()));
//                                DocRetrieval.Companion.getEnvAdapter3().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getEnvTags3().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getEnvTags3().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt3 = new SubFieldAdt(c, DocRetrieval.Companion.getEnvTags3());
//                                subFieldAdt3.notifyDataSetChanged();
//                            }
//                        }
//
//                        holder.readProgress.setVisibility(View.INVISIBLE);
//
//                        DocRetrieval.Companion.getEnvTagsArray().add(subFieldAdt0);
//                        DocRetrieval.Companion.getEnvTagsArray().add(subFieldAdt1);
//                        DocRetrieval.Companion.getEnvTagsArray().add(subFieldAdt2);
//                        DocRetrieval.Companion.getEnvTagsArray().add(subFieldAdt3);
//
//                        DocRetrieval.Companion.getEnvAdapters().add(DocRetrieval.Companion.getEnvAdapter0());
//                        DocRetrieval.Companion.getEnvAdapters().add(DocRetrieval.Companion.getEnvAdapter1());
//                        DocRetrieval.Companion.getEnvAdapters().add(DocRetrieval.Companion.getEnvAdapter2());
//                        DocRetrieval.Companion.getEnvAdapters().add(DocRetrieval.Companion.getEnvAdapter3());
//
//                        try {
//
//                            holder.docDisplayRV.setAdapter(DocRetrieval.Companion.getEnvAdapters().get(holder.getAbsoluteAdapterPosition()));
//                            holder.docTags.setAdapter(DocRetrieval.Companion.getEnvTagsArray().get(holder.getAbsoluteAdapterPosition()));
//
//                            holder.departmentIndicator.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    holder.docDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getEnvAdapters().get(holder.getAbsoluteAdapterPosition()).docPosition++);
//                                    holder.docTags.smoothScrollToPosition(DocRetrieval.Companion.getEnvTagsArray().get(holder.getAbsoluteAdapterPosition()).getTagPosition() + 1);
//                                }
//                            });
//
//                            searchDocs.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                                @Override
//                                public boolean onQueryTextSubmit(String query) {
//                                    return false;
//                                }
//
//                                @Override
//                                public boolean onQueryTextChange(String newText) {
//                                    for (DocumentAdapter a : DocRetrieval.Companion.getEnvAdapters()) {
//                                        if (a != null) {
//                                            a.getFilter().filter(newText);
//                                        }
//                                    }
//                                    for (SubFieldAdt subFieldAdt : DocRetrieval.Companion.getEnvTagsArray()) {
//                                        if (subFieldAdt != null) {
//                                            subFieldAdt.getFilter().filter(newText);
//                                        }
//                                    }
//                                    return false;
//                                }
//                            });
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            }
//            break;
//            case "Hospitality & Tourism": {
//
//                departmentsArray = DocSorting.getSubFields(7);
//                schoolDepartments.addAll(Arrays.asList(departmentsArray));
//
//                publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        documents = new ArrayList<>();
//                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
//                            SelectedDoc document = qds.toObject(SelectedDoc.class);
//                            documents.add(document);
//                        }
//
//                        DocRetrieval.Companion.setHospitalityTags0(new ArrayList<>());
//                        DocRetrieval.Companion.setHospitalityTags1(new ArrayList<>());
//
//                        DocRetrieval.Companion.setHospitality0(new ArrayList<>());
//                        DocRetrieval.Companion.setHospitality1(new ArrayList<>());
//
//                        for (SelectedDoc document : documents) {
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(7)[0])) {
//                                DocRetrieval.Companion.getHospitality0().add(document);
//                                DocRetrieval.Companion.setHospitalityAdapter0(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getHospitality0()));
//                                DocRetrieval.Companion.getHospitalityAdapter0().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getHospitalityTags0().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getHospitalityTags0().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getHospitalityTags0());
//                                subFieldAdt0.notifyDataSetChanged();
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(7)[1])) {
//                                DocRetrieval.Companion.getHospitality1().add(document);
//                                DocRetrieval.Companion.setHospitalityAdapter1(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getHospitality1()));
//                                DocRetrieval.Companion.getHospitalityAdapter1().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getHospitalityTags1().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getHospitalityTags1().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getHospitalityTags1());
//                                subFieldAdt1.notifyDataSetChanged();
//                            }
//                        }
//
//                        holder.readProgress.setVisibility(View.INVISIBLE);
//
//                        DocRetrieval.Companion.getHospitalityAdapters().add(DocRetrieval.Companion.getHospitalityAdapter0());
//                        DocRetrieval.Companion.getHospitalityAdapters().add(DocRetrieval.Companion.getHospitalityAdapter1());
//
//                        DocRetrieval.Companion.getHospitalityTagsArray().add(subFieldAdt0);
//                        DocRetrieval.Companion.getHospitalityTagsArray().add(subFieldAdt1);
//
//                        try {
//
//                            holder.docDisplayRV.setAdapter(DocRetrieval.Companion.getHospitalityAdapters().get(holder.getAbsoluteAdapterPosition()));
//                            holder.docTags.setAdapter(DocRetrieval.Companion.getHospitalityTagsArray().get(holder.getAbsoluteAdapterPosition()));
//
//                            holder.departmentIndicator.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    holder.docDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getHospitalityAdapters().get(holder.getAbsoluteAdapterPosition()).docPosition++);
//                                    holder.docTags.smoothScrollToPosition(DocRetrieval.Companion.getHospitalityTagsArray().get(holder.getAbsoluteAdapterPosition()).getTagPosition() + 1);
//                                }
//                            });
//
//                            searchDocs.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                                @Override
//                                public boolean onQueryTextSubmit(String query) {
//                                    return false;
//                                }
//
//                                @Override
//                                public boolean onQueryTextChange(String newText) {
//                                    for (DocumentAdapter a : DocRetrieval.Companion.getHospitalityAdapters()) {
//                                        if (a != null) {
//                                            a.getFilter().filter(newText);
//                                        }
//                                    }
//                                    for (SubFieldAdt subFieldAdt : DocRetrieval.Companion.getHospitalityTagsArray()) {
//                                        if (subFieldAdt != null) {
//                                            subFieldAdt.getFilter().filter(newText);
//                                        }
//                                    }
//                                    return false;
//                                }
//                            });
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            }
//            break;
//            case "Humanities & Social Sciences": {
//
//                departmentsArray = DocSorting.getSubFields(8);
//                schoolDepartments.addAll(Arrays.asList(departmentsArray));
//                publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        documents = new ArrayList<>();
//                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
//                            SelectedDoc document = qds.toObject(SelectedDoc.class);
//                            documents.add(document);
//                        }
//
//                        DocRetrieval.Companion.setHumanitiesTags0(new ArrayList<>());
//                        DocRetrieval.Companion.setHumanitiesTags1(new ArrayList<>());
//                        DocRetrieval.Companion.setHumanitiesTags2(new ArrayList<>());
//                        DocRetrieval.Companion.setHumanitiesTags3(new ArrayList<>());
//                        DocRetrieval.Companion.setHumanitiesTags4(new ArrayList<>());
//                        DocRetrieval.Companion.setHumanitiesTags5(new ArrayList<>());
//                        DocRetrieval.Companion.setHumanitiesTags6(new ArrayList<>());
//                        DocRetrieval.Companion.setHumanitiesTags7(new ArrayList<>());
//                        DocRetrieval.Companion.setHumanitiesTags8(new ArrayList<>());
//                        DocRetrieval.Companion.setHumanitiesTags9(new ArrayList<>());
//                        DocRetrieval.Companion.setHumanitiesTags10(new ArrayList<>());
//
//                        DocRetrieval.Companion.setHumanities0(new ArrayList<>());
//                        DocRetrieval.Companion.setHumanities1(new ArrayList<>());
//                        DocRetrieval.Companion.setHumanities2(new ArrayList<>());
//                        DocRetrieval.Companion.setHumanities3(new ArrayList<>());
//                        DocRetrieval.Companion.setHumanities4(new ArrayList<>());
//                        DocRetrieval.Companion.setHumanities5(new ArrayList<>());
//                        DocRetrieval.Companion.setHumanities6(new ArrayList<>());
//                        DocRetrieval.Companion.setHumanities7(new ArrayList<>());
//                        DocRetrieval.Companion.setHumanities8(new ArrayList<>());
//                        DocRetrieval.Companion.setHumanities9(new ArrayList<>());
//                        DocRetrieval.Companion.setHumanities10(new ArrayList<>());
//
//                        for (SelectedDoc document : documents) {
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(8)[0])) {
//                                DocRetrieval.Companion.getHumanities0().add(document);
//                                DocRetrieval.Companion.setHumanitiesAdapter0(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getHumanities0()));
//                                DocRetrieval.Companion.getHumanitiesAdapter0().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getHumanitiesTags0().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getHumanitiesTags0().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getHumanitiesTags0());
//                                subFieldAdt0.notifyDataSetChanged();
//
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(8)[1])) {
//                                DocRetrieval.Companion.getHumanities1().add(document);
//                                DocRetrieval.Companion.setHumanitiesAdapter1(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getHumanities1()));
//                                DocRetrieval.Companion.getHumanitiesAdapter1().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getHumanitiesTags1().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getHumanitiesTags1().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getHumanitiesTags1());
//                                subFieldAdt1.notifyDataSetChanged();
//
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(8)[2])) {
//                                DocRetrieval.Companion.getHumanities2().add(document);
//                                DocRetrieval.Companion.setHumanitiesAdapter2(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getHumanities2()));
//                                DocRetrieval.Companion.getHumanitiesAdapter2().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getHumanitiesTags2().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getHumanitiesTags2().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getHumanitiesTags2());
//                                subFieldAdt2.notifyDataSetChanged();
//
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(8)[3])) {
//                                DocRetrieval.Companion.getHumanities3().add(document);
//                                DocRetrieval.Companion.setHumanitiesAdapter3(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getHumanities3()));
//                                DocRetrieval.Companion.getHumanitiesAdapter3().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getHumanitiesTags3().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getHumanitiesTags3().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt3 = new SubFieldAdt(c, DocRetrieval.Companion.getHumanitiesTags3());
//                                subFieldAdt3.notifyDataSetChanged();
//
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(8)[4])) {
//                                DocRetrieval.Companion.getHumanities4().add(document);
//                                DocRetrieval.Companion.setHumanitiesAdapter4(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getHumanities4()));
//                                DocRetrieval.Companion.getHumanitiesAdapter4().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getHumanitiesTags4().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getHumanitiesTags4().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt4 = new SubFieldAdt(c, DocRetrieval.Companion.getHumanitiesTags4());
//                                subFieldAdt4.notifyDataSetChanged();
//
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(8)[5])) {
//                                DocRetrieval.Companion.getHumanities5().add(document);
//                                DocRetrieval.Companion.setHumanitiesAdapter5(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getHumanities5()));
//                                DocRetrieval.Companion.getHumanitiesAdapter5().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getHumanitiesTags5().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getHumanitiesTags5().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt5 = new SubFieldAdt(c, DocRetrieval.Companion.getHumanitiesTags5());
//                                subFieldAdt5.notifyDataSetChanged();
//
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(8)[6])) {
//                                DocRetrieval.Companion.getHumanities6().add(document);
//                                DocRetrieval.Companion.setHumanitiesAdapter6(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getHumanities6()));
//                                DocRetrieval.Companion.getHumanitiesAdapter6().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getHumanitiesTags6().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getHumanitiesTags6().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt6 = new SubFieldAdt(c, DocRetrieval.Companion.getHumanitiesTags6());
//                                subFieldAdt6.notifyDataSetChanged();
//
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(8)[7])) {
//                                DocRetrieval.Companion.getHumanities7().add(document);
//                                DocRetrieval.Companion.setHumanitiesAdapter7(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getHumanities7()));
//                                DocRetrieval.Companion.getHumanitiesAdapter7().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getHumanitiesTags7().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getHumanitiesTags7().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt7 = new SubFieldAdt(c, DocRetrieval.Companion.getHumanitiesTags7());
//                                subFieldAdt7.notifyDataSetChanged();
//
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(8)[8])) {
//                                DocRetrieval.Companion.getHumanities8().add(document);
//                                DocRetrieval.Companion.setHumanitiesAdapter8(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getHumanities8()));
//                                DocRetrieval.Companion.getHumanitiesAdapter8().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getHumanitiesTags8().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getHumanitiesTags8().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt8 = new SubFieldAdt(c, DocRetrieval.Companion.getHumanitiesTags8());
//                                subFieldAdt8.notifyDataSetChanged();
//
//
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(8)[9])) {
//                                DocRetrieval.Companion.getHumanities9().add(document);
//                                DocRetrieval.Companion.setHumanitiesAdapter9(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getHumanities9()));
//                                DocRetrieval.Companion.getHumanitiesAdapter9().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getHumanitiesTags9().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getHumanitiesTags9().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt9 = new SubFieldAdt(c, DocRetrieval.Companion.getHumanitiesTags9());
//                                subFieldAdt9.notifyDataSetChanged();
//
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(8)[10])) {
//                                DocRetrieval.Companion.getHumanities10().add(document);
//                                DocRetrieval.Companion.setHumanitiesAdapter10(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getHumanities10()));
//                                DocRetrieval.Companion.getHumanitiesAdapter10().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getHumanitiesTags10().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getHumanitiesTags10().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt10 = new SubFieldAdt(c, DocRetrieval.Companion.getHumanitiesTags10());
//                                subFieldAdt10.notifyDataSetChanged();
//                            }
//
//                        }
//
//                        holder.readProgress.setVisibility(View.INVISIBLE);
//
//                        DocRetrieval.Companion.getHumanitiesTagsArray().add(subFieldAdt0);
//                        DocRetrieval.Companion.getHumanitiesTagsArray().add(subFieldAdt1);
//                        DocRetrieval.Companion.getHumanitiesTagsArray().add(subFieldAdt2);
//                        DocRetrieval.Companion.getHumanitiesTagsArray().add(subFieldAdt3);
//                        DocRetrieval.Companion.getHumanitiesTagsArray().add(subFieldAdt4);
//                        DocRetrieval.Companion.getHumanitiesTagsArray().add(subFieldAdt5);
//                        DocRetrieval.Companion.getHumanitiesTagsArray().add(subFieldAdt7);
//                        DocRetrieval.Companion.getHumanitiesTagsArray().add(subFieldAdt8);
//                        DocRetrieval.Companion.getHumanitiesTagsArray().add(subFieldAdt9);
//                        DocRetrieval.Companion.getHumanitiesTagsArray().add(subFieldAdt10);
//
//                        DocRetrieval.Companion.getHumanitiesAdapters().add(DocRetrieval.Companion.getHumanitiesAdapter0());
//                        DocRetrieval.Companion.getHumanitiesAdapters().add(DocRetrieval.Companion.getHumanitiesAdapter1());
//                        DocRetrieval.Companion.getHumanitiesAdapters().add(DocRetrieval.Companion.getHumanitiesAdapter2());
//                        DocRetrieval.Companion.getHumanitiesAdapters().add(DocRetrieval.Companion.getHumanitiesAdapter3());
//                        DocRetrieval.Companion.getHumanitiesAdapters().add(DocRetrieval.Companion.getHumanitiesAdapter4());
//                        DocRetrieval.Companion.getHumanitiesAdapters().add(DocRetrieval.Companion.getHumanitiesAdapter5());
//                        DocRetrieval.Companion.getHumanitiesAdapters().add(DocRetrieval.Companion.getHumanitiesAdapter7());
//                        DocRetrieval.Companion.getHumanitiesAdapters().add(DocRetrieval.Companion.getHumanitiesAdapter8());
//                        DocRetrieval.Companion.getHumanitiesAdapters().add(DocRetrieval.Companion.getHumanitiesAdapter9());
//                        DocRetrieval.Companion.getHumanitiesAdapters().add(DocRetrieval.Companion.getHumanitiesAdapter10());
//
//
//                        try {
//
//                            holder.docDisplayRV.setAdapter(DocRetrieval.Companion.getHumanitiesAdapters().get(holder.getAbsoluteAdapterPosition()));
//                            holder.docTags.setAdapter(DocRetrieval.Companion.getHumanitiesTagsArray().get(holder.getAbsoluteAdapterPosition()));
//
//                            holder.departmentIndicator.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    holder.docDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getHumanitiesAdapters().get(holder.getAbsoluteAdapterPosition()).docPosition++);
//                                    holder.docTags.smoothScrollToPosition(DocRetrieval.Companion.getHumanitiesTagsArray().get(holder.getAbsoluteAdapterPosition()).getTagPosition() + 1);
//                                }
//                            });
//
//                            searchDocs.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                                @Override
//                                public boolean onQueryTextSubmit(String query) {
//                                    return false;
//                                }
//
//                                @Override
//                                public boolean onQueryTextChange(String newText) {
//                                    for (DocumentAdapter a : DocRetrieval.Companion.getHumanitiesAdapters()) {
//                                        if (a != null) {
//                                            a.getFilter().filter(newText);
//                                        }
//                                    }
//                                    for (SubFieldAdt subFieldAdt : DocRetrieval.Companion.getHumanitiesTagsArray()) {
//                                        if (subFieldAdt != null) {
//                                            subFieldAdt.getFilter().filter(newText);
//                                        }
//                                    }
//                                    return false;
//                                }
//                            });
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            }
//            break;
//            case "Law": {
//
//                departmentsArray = DocSorting.getSubFields(9);
//                schoolDepartments.addAll(Arrays.asList(departmentsArray));
//
//                publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        documents = new ArrayList<>();
//                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
//                            SelectedDoc document = qds.toObject(SelectedDoc.class);
//                            documents.add(document);
//                        }
//
//                        DocRetrieval.Companion.setLawTags0(new ArrayList<>());
//                        DocRetrieval.Companion.setLawTags1(new ArrayList<>());
//
//                        DocRetrieval.Companion.setLaw0(new ArrayList<>());
//                        DocRetrieval.Companion.setLaw1(new ArrayList<>());
//
//                        for (SelectedDoc document : documents) {
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(9)[0])) {
//                                DocRetrieval.Companion.getLaw0().add(document);
//                                DocRetrieval.Companion.setLawAdapter0(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getLaw0()));
//                                DocRetrieval.Companion.getLawAdapter0().notifyDataSetChanged();
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(9)[1])) {
//                                DocRetrieval.Companion.getLaw1().add(document);
//                                DocRetrieval.Companion.setLawAdapter1(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getLaw1()));
//                                DocRetrieval.Companion.getLawAdapter1().notifyDataSetChanged();
//                            }
//
//                        }
//
//                        holder.readProgress.setVisibility(View.INVISIBLE);
//                        DocRetrieval.Companion.getLawTagsArray().add(subFieldAdt0);
//                        DocRetrieval.Companion.getLawTagsArray().add(subFieldAdt1);
//
//                        DocRetrieval.Companion.getLawAdapters().add(DocRetrieval.Companion.getLawAdapter0());
//                        DocRetrieval.Companion.getLawAdapters().add(DocRetrieval.Companion.getLawAdapter1());
//
//                        try {
//
//                            holder.docDisplayRV.setAdapter(DocRetrieval.Companion.getLawAdapters().get(holder.getAbsoluteAdapterPosition()));
//                            holder.docTags.setAdapter(DocRetrieval.Companion.getLawTagsArray().get(holder.getAbsoluteAdapterPosition()));
//
//                            holder.departmentIndicator.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    holder.docDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getLawAdapters().get(holder.getAbsoluteAdapterPosition()).docPosition++);
//                                    holder.docTags.smoothScrollToPosition(DocRetrieval.Companion.getLawTagsArray().get(holder.getAbsoluteAdapterPosition()).getTagPosition() + 1);
//                                }
//                            });
//
//                            searchDocs.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                                @Override
//                                public boolean onQueryTextSubmit(String query) {
//                                    return false;
//                                }
//
//                                @Override
//                                public boolean onQueryTextChange(String newText) {
//                                    for (DocumentAdapter a : DocRetrieval.Companion.getLawAdapters()) {
//                                        if (a != null) {
//                                            a.getFilter().filter(newText);
//                                        }
//                                    }
//                                    for (SubFieldAdt subFieldAdt : DocRetrieval.Companion.getLawTagsArray()) {
//                                        if (subFieldAdt != null) {
//                                            subFieldAdt.getFilter().filter(newText);
//                                        }
//                                    }
//                                    return false;
//                                }
//                            });
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            }
//            break;
//            case "Medicine": {
//
//                departmentsArray = DocSorting.getSubFields(10);
//                schoolDepartments.addAll(Arrays.asList(departmentsArray));
//
//                publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        documents = new ArrayList<>();
//                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
//                            SelectedDoc document = qds.toObject(SelectedDoc.class);
//                            documents.add(document);
//                        }
//
//                        DocRetrieval.Companion.setMedTags0(new ArrayList<>());
//                        DocRetrieval.Companion.setMedTags1(new ArrayList<>());
//                        DocRetrieval.Companion.setMedTags2(new ArrayList<>());
//                        DocRetrieval.Companion.setMedTags3(new ArrayList<>());
//                        DocRetrieval.Companion.setMedTags4(new ArrayList<>());
//                        DocRetrieval.Companion.setMedTags5(new ArrayList<>());
//                        DocRetrieval.Companion.setMedTags6(new ArrayList<>());
//                        DocRetrieval.Companion.setMedTags7(new ArrayList<>());
//                        DocRetrieval.Companion.setMedTags8(new ArrayList<>());
//                        DocRetrieval.Companion.setMedTags9(new ArrayList<>());
//
//                        DocRetrieval.Companion.setMedicine0(new ArrayList<>());
//                        DocRetrieval.Companion.setMedicine1(new ArrayList<>());
//                        DocRetrieval.Companion.setMedicine2(new ArrayList<>());
//                        DocRetrieval.Companion.setMedicine3(new ArrayList<>());
//                        DocRetrieval.Companion.setMedicine4(new ArrayList<>());
//                        DocRetrieval.Companion.setMedicine5(new ArrayList<>());
//                        DocRetrieval.Companion.setMedicine6(new ArrayList<>());
//                        DocRetrieval.Companion.setMedicine7(new ArrayList<>());
//                        DocRetrieval.Companion.setMedicine8(new ArrayList<>());
//                        DocRetrieval.Companion.setMedicine9(new ArrayList<>());
//
//                        for (SelectedDoc document : documents) {
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(10)[0])) {
//                                DocRetrieval.Companion.getMedicine0().add(document);
//                                DocRetrieval.Companion.setMedAdapter0(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getMedicine0()));
//                                DocRetrieval.Companion.getMedAdapter0().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getMedTags0().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getMedTags0().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt10 = new SubFieldAdt(c, DocRetrieval.Companion.getMedTags0());
//                                subFieldAdt10.notifyDataSetChanged();
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(10)[1])) {
//                                DocRetrieval.Companion.getMedicine1().add(document);
//                                DocRetrieval.Companion.setMedAdapter1(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getMedicine1()));
//                                DocRetrieval.Companion.getMedAdapter1().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getMedTags1().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getMedTags1().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getMedTags1());
//                                subFieldAdt1.notifyDataSetChanged();
//
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(10)[2])) {
//                                DocRetrieval.Companion.getMedicine2().add(document);
//                                DocRetrieval.Companion.setMedAdapter2(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getMedicine2()));
//                                DocRetrieval.Companion.getMedAdapter2().notifyDataSetChanged();
//
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getMedTags2().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getMedTags2().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getMedTags2());
//                                subFieldAdt2.notifyDataSetChanged();
//
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(10)[3])) {
//                                DocRetrieval.Companion.getMedicine3().add(document);
//                                DocRetrieval.Companion.setMedAdapter3(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getMedicine3()));
//                                DocRetrieval.Companion.getMedAdapter3().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getMedTags3().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getMedTags3().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt3 = new SubFieldAdt(c, DocRetrieval.Companion.getMedTags3());
//                                subFieldAdt3.notifyDataSetChanged();
//
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(10)[4])) {
//                                DocRetrieval.Companion.getMedicine4().add(document);
//                                DocRetrieval.Companion.setMedAdapter4(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getMedicine4()));
//                                DocRetrieval.Companion.getMedAdapter4().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getMedTags4().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getMedTags4().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt4 = new SubFieldAdt(c, DocRetrieval.Companion.getMedTags4());
//                                subFieldAdt4.notifyDataSetChanged();
//
//
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(10)[5])) {
//                                DocRetrieval.Companion.getMedicine5().add(document);
//                                DocRetrieval.Companion.setMedAdapter5(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getMedicine5()));
//                                DocRetrieval.Companion.getMedAdapter5().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getMedTags5().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getMedTags5().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt5 = new SubFieldAdt(c, DocRetrieval.Companion.getMedTags5());
//                                subFieldAdt5.notifyDataSetChanged();
//
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(10)[6])) {
//                                DocRetrieval.Companion.getMedicine6().add(document);
//                                DocRetrieval.Companion.setMedAdapter6(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getMedicine6()));
//                                DocRetrieval.Companion.getMedAdapter6().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getMedTags6().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getMedTags6().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt6 = new SubFieldAdt(c, DocRetrieval.Companion.getMedTags6());
//                                subFieldAdt6.notifyDataSetChanged();
//
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(10)[7])) {
//                                DocRetrieval.Companion.getMedicine7().add(document);
//                                DocRetrieval.Companion.setMedAdapter7(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getMedicine7()));
//                                DocRetrieval.Companion.getMedAdapter7().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getMedTags7().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getMedTags7().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt7 = new SubFieldAdt(c, DocRetrieval.Companion.getMedTags7());
//                                subFieldAdt7.notifyDataSetChanged();
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(10)[8])) {
//                                DocRetrieval.Companion.getMedicine8().add(document);
//                                DocRetrieval.Companion.setMedAdapter8(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getMedicine8()));
//                                DocRetrieval.Companion.getMedAdapter8().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getMedTags8().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getMedTags8().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt8 = new SubFieldAdt(c, DocRetrieval.Companion.getMedTags8());
//                                subFieldAdt8.notifyDataSetChanged();
//
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(10)[9])) {
//                                DocRetrieval.Companion.getMedicine9().add(document);
//                                DocRetrieval.Companion.setMedAdapter9(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getMedicine9()));
//                                DocRetrieval.Companion.getMedAdapter9().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getMedTags9().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getMedTags9().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt9 = new SubFieldAdt(c, DocRetrieval.Companion.getMedTags9());
//                                subFieldAdt9.notifyDataSetChanged();
//                            }
//
//                        }
//
//                        holder.readProgress.setVisibility(View.INVISIBLE);
//
//                        DocRetrieval.Companion.getMedTagsArray().add(subFieldAdt0);
//                        DocRetrieval.Companion.getMedTagsArray().add(subFieldAdt1);
//                        DocRetrieval.Companion.getMedTagsArray().add(subFieldAdt2);
//                        DocRetrieval.Companion.getMedTagsArray().add(subFieldAdt3);
//                        DocRetrieval.Companion.getMedTagsArray().add(subFieldAdt4);
//                        DocRetrieval.Companion.getMedTagsArray().add(subFieldAdt5);
//                        DocRetrieval.Companion.getMedTagsArray().add(subFieldAdt6);
//                        DocRetrieval.Companion.getMedTagsArray().add(subFieldAdt7);
//                        DocRetrieval.Companion.getMedTagsArray().add(subFieldAdt8);
//                        DocRetrieval.Companion.getMedTagsArray().add(subFieldAdt9);
//
//                        DocRetrieval.Companion.getMedAdapters().add(DocRetrieval.Companion.getMedAdapter0());
//                        DocRetrieval.Companion.getMedAdapters().add(DocRetrieval.Companion.getMedAdapter1());
//                        DocRetrieval.Companion.getMedAdapters().add(DocRetrieval.Companion.getMedAdapter2());
//                        DocRetrieval.Companion.getMedAdapters().add(DocRetrieval.Companion.getMedAdapter3());
//                        DocRetrieval.Companion.getMedAdapters().add(DocRetrieval.Companion.getMedAdapter4());
//                        DocRetrieval.Companion.getMedAdapters().add(DocRetrieval.Companion.getMedAdapter5());
//                        DocRetrieval.Companion.getMedAdapters().add(DocRetrieval.Companion.getMedAdapter6());
//                        DocRetrieval.Companion.getMedAdapters().add(DocRetrieval.Companion.getMedAdapter7());
//                        DocRetrieval.Companion.getMedAdapters().add(DocRetrieval.Companion.getMedAdapter8());
//                        DocRetrieval.Companion.getMedAdapters().add(DocRetrieval.Companion.getMedAdapter9());
//
//                        try {
//
//                            holder.docDisplayRV.setAdapter(DocRetrieval.Companion.getMedAdapters().get(position));
//                            holder.docTags.setAdapter(DocRetrieval.Companion.getMedTagsArray().get(position));
//
//                            holder.departmentIndicator.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    holder.docDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getMedAdapters().get(holder.getAbsoluteAdapterPosition()).docPosition++);
//                                    holder.docTags.smoothScrollToPosition(DocRetrieval.Companion.getMedTagsArray().get(holder.getAbsoluteAdapterPosition()).getTagPosition() + 1);
//                                }
//                            });
//
//                            searchDocs.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                                @Override
//                                public boolean onQueryTextSubmit(String query) {
//                                    return false;
//                                }
//
//                                @Override
//                                public boolean onQueryTextChange(String newText) {
//                                    for (DocumentAdapter a : DocRetrieval.Companion.getMedAdapters()) {
//                                        if (a != null) {
//                                            a.getFilter().filter(newText);
//                                        }
//                                    }
//                                    for (SubFieldAdt subFieldAdt : DocRetrieval.Companion.getMedTagsArray()) {
//                                        if (subFieldAdt != null) {
//                                            subFieldAdt.getFilter().filter(newText);
//                                        }
//                                    }
//                                    return false;
//                                }
//                            });
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//
//            }
//            break;
//            case "Public Health": {
//
//                departmentsArray = DocSorting.getSubFields(11);
//                schoolDepartments.addAll(Arrays.asList(departmentsArray));
//
//                publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        documents = new ArrayList<>();
//                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
//                            SelectedDoc document = qds.toObject(SelectedDoc.class);
//                            documents.add(document);
//                        }
//
//                        DocRetrieval.Companion.setPublicHealthTags0(new ArrayList<>());
//                        DocRetrieval.Companion.setPublicHealthTags1(new ArrayList<>());
//                        DocRetrieval.Companion.setPublicHealthTags2(new ArrayList<>());
//
//                        DocRetrieval.Companion.setPublicHealth0(new ArrayList<>());
//                        DocRetrieval.Companion.setPublicHealth1(new ArrayList<>());
//                        DocRetrieval.Companion.setPublicHealth2(new ArrayList<>());
//
//                        for (SelectedDoc document : documents) {
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(11)[0])) {
//                                DocRetrieval.Companion.getPureAppSci0().add(document);
//                                DocRetrieval.Companion.setPublicHealthAdapter0(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getPublicHealth0()));
//                                DocRetrieval.Companion.getPublicHealthAdapter0().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getPublicHealthTags0().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getPublicHealthTags0().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getPublicHealthTags0());
//                                subFieldAdt0.notifyDataSetChanged();
//
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(11)[1])) {
//                                DocRetrieval.Companion.getPureAppSci1().add(document);
//                                DocRetrieval.Companion.setPublicHealthAdapter1(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getPublicHealth1()));
//                                DocRetrieval.Companion.getPublicHealthAdapter1().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getPublicHealthTags1().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getPublicHealthTags1().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getPublicHealthTags1());
//                                subFieldAdt1.notifyDataSetChanged();
//
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(11)[2])) {
//                                DocRetrieval.Companion.getPureAppSci2().add(document);
//                                DocRetrieval.Companion.setPublicHealthAdapter2(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getPublicHealth2()));
//                                DocRetrieval.Companion.getPublicHealthAdapter2().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getPublicHealthTags2().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getPublicHealthTags2().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getPublicHealthTags2());
//                                subFieldAdt2.notifyDataSetChanged();
//                            }
//
//                        }
//
//                        holder.readProgress.setVisibility(View.INVISIBLE);
//
//                        DocRetrieval.Companion.getPublicHealthTagsArray().add(subFieldAdt0);
//                        DocRetrieval.Companion.getPublicHealthTagsArray().add(subFieldAdt1);
//                        DocRetrieval.Companion.getPublicHealthTagsArray().add(subFieldAdt2);
//
//                        DocRetrieval.Companion.getPublicHealthAdapters().add(DocRetrieval.Companion.getPublicHealthAdapter0());
//                        DocRetrieval.Companion.getPublicHealthAdapters().add(DocRetrieval.Companion.getPublicHealthAdapter1());
//                        DocRetrieval.Companion.getPublicHealthAdapters().add(DocRetrieval.Companion.getPublicHealthAdapter2());
//
//
//                        try {
//
//                            holder.docDisplayRV.setAdapter(DocRetrieval.Companion.getPublicHealthAdapters().get(holder.getAbsoluteAdapterPosition()));
//                            holder.docTags.setAdapter(DocRetrieval.Companion.getPublicHealthTagsArray().get(holder.getAbsoluteAdapterPosition()));
//
//                            holder.departmentIndicator.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    holder.docDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getPublicHealthAdapters().get(holder.getAbsoluteAdapterPosition()).docPosition++);
//                                    holder.docTags.smoothScrollToPosition(DocRetrieval.Companion.getPublicHealthTagsArray().get(holder.getAbsoluteAdapterPosition()).getTagPosition() + 1);
//                                }
//                            });
//
//                            searchDocs.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                                @Override
//                                public boolean onQueryTextSubmit(String query) {
//                                    return false;
//                                }
//
//                                @Override
//                                public boolean onQueryTextChange(String newText) {
//                                    for (DocumentAdapter a : DocRetrieval.Companion.getPublicHealthAdapters()) {
//                                        if (a != null) {
//                                            a.getFilter().filter(newText);
//                                        }
//                                    }
//                                    for (SubFieldAdt subFieldAdt : DocRetrieval.Companion.getPublicHealthTagsArray()) {
//                                        if (subFieldAdt != null) {
//                                            subFieldAdt.getFilter().filter(newText);
//                                        }
//                                    }
//                                    return false;
//                                }
//                            });
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//
//            }
//            break;
//            case "Pure & Applied Sciences": {
//
//                departmentsArray = DocSorting.getSubFields(12);
//                schoolDepartments.addAll(Arrays.asList(departmentsArray));
//
//                publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        documents = new ArrayList<>();
//                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
//                            SelectedDoc document = qds.toObject(SelectedDoc.class);
//                            documents.add(document);
//                        }
//                        DocRetrieval.Companion.setPureAppTags0(new ArrayList<>());
//                        DocRetrieval.Companion.setPureAppTags1(new ArrayList<>());
//                        DocRetrieval.Companion.setPureAppTags2(new ArrayList<>());
//                        DocRetrieval.Companion.setPureAppTags3(new ArrayList<>());
//                        DocRetrieval.Companion.setPureAppTags4(new ArrayList<>());
//                        DocRetrieval.Companion.setPureAppTags5(new ArrayList<>());
//                        DocRetrieval.Companion.setPureAppTags6(new ArrayList<>());
//                        DocRetrieval.Companion.setPureAppTags7(new ArrayList<>());
//
//                        DocRetrieval.Companion.setPureAppSci0(new ArrayList<>());
//                        DocRetrieval.Companion.setPureAppSci1(new ArrayList<>());
//                        DocRetrieval.Companion.setPureAppSci2(new ArrayList<>());
//                        DocRetrieval.Companion.setPureAppSci3(new ArrayList<>());
//                        DocRetrieval.Companion.setPureAppSci4(new ArrayList<>());
//                        DocRetrieval.Companion.setPureAppSci5(new ArrayList<>());
//                        DocRetrieval.Companion.setPureAppSci6(new ArrayList<>());
//                        DocRetrieval.Companion.setPureAppSci7(new ArrayList<>());
//
//                        for (SelectedDoc document : documents) {
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(12)[0])) {
//                                DocRetrieval.Companion.getPureAppSci0().add(document);
//                                DocRetrieval.Companion.setDocAdapter0(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getPureAppSci0()));
//                                DocRetrieval.Companion.getDocAdapter0().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getPureAppTags0().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getPureAppTags0().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getPureAppTags0());
//                                subFieldAdt0.notifyDataSetChanged();
//
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(12)[1])) {
//                                DocRetrieval.Companion.getPureAppSci1().add(document);
//                                DocRetrieval.Companion.setDocAdapter1(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getPureAppSci1()));
//                                DocRetrieval.Companion.getDocAdapter1().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getPureAppTags1().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getPureAppTags1().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getPureAppTags1());
//                                subFieldAdt1.notifyDataSetChanged();
//
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(12)[2])) {
//                                DocRetrieval.Companion.getPureAppSci2().add(document);
//                                DocRetrieval.Companion.setDocAdapter2(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getPureAppSci2()));
//                                DocRetrieval.Companion.getDocAdapter2().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getPureAppTags2().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getPureAppTags2().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getPureAppTags2());
//                                subFieldAdt2.notifyDataSetChanged();
//
//
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(12)[3])) {
//                                DocRetrieval.Companion.getPureAppSci3().add(document);
//                                DocRetrieval.Companion.setDocAdapter3(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getPureAppSci3()));
//                                DocRetrieval.Companion.getDocAdapter3().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getPureAppTags3().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getPureAppTags3().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt3 = new SubFieldAdt(c, DocRetrieval.Companion.getPureAppTags3());
//                                subFieldAdt3.notifyDataSetChanged();
//
//
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(12)[4])) {
//                                DocRetrieval.Companion.getPureAppSci4().add(document);
//                                DocRetrieval.Companion.setDocAdapter4(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getPureAppSci4()));
//                                DocRetrieval.Companion.getDocAdapter4().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getPureAppTags4().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getPureAppTags4().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt4 = new SubFieldAdt(c, DocRetrieval.Companion.getPureAppTags4());
//                                subFieldAdt4.notifyDataSetChanged();
//
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(12)[5])) {
//                                DocRetrieval.Companion.getPureAppSci5().add(document);
//                                DocRetrieval.Companion.setDocAdapter5(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getPureAppSci5()));
//                                DocRetrieval.Companion.getDocAdapter5().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getPureAppTags5().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getPureAppTags5().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt5 = new SubFieldAdt(c, DocRetrieval.Companion.getPureAppTags5());
//                                subFieldAdt5.notifyDataSetChanged();
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(12)[6])) {
//                                DocRetrieval.Companion.getPureAppSci6().add(document);
//                                DocRetrieval.Companion.setDocAdapter6(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getPureAppSci6()));
//                                DocRetrieval.Companion.getDocAdapter6().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getPureAppTags6().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getPureAppTags6().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt6 = new SubFieldAdt(c, DocRetrieval.Companion.getPureAppTags6());
//                                subFieldAdt6.notifyDataSetChanged();
//
//
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(12)[7])) {
//                                DocRetrieval.Companion.getPureAppSci7().add(document);
//                                DocRetrieval.Companion.setDocAdapter7(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getPureAppSci7()));
//                                DocRetrieval.Companion.getDocAdapter7().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getPureAppTags7().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getPureAppTags7().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt7 = new SubFieldAdt(c, DocRetrieval.Companion.getPureAppTags7());
//                                subFieldAdt7.notifyDataSetChanged();
//                            }
//
//                        }
//
//
//                        holder.readProgress.setVisibility(View.INVISIBLE);
//
//                        DocRetrieval.Companion.getPureAppTagsArray().add(subFieldAdt0);
//                        DocRetrieval.Companion.getPureAppTagsArray().add(subFieldAdt1);
//                        DocRetrieval.Companion.getPureAppTagsArray().add(subFieldAdt2);
//                        DocRetrieval.Companion.getPureAppTagsArray().add(subFieldAdt3);
//                        DocRetrieval.Companion.getPureAppTagsArray().add(subFieldAdt4);
//                        DocRetrieval.Companion.getPureAppTagsArray().add(subFieldAdt5);
//                        DocRetrieval.Companion.getPureAppTagsArray().add(subFieldAdt6);
//                        DocRetrieval.Companion.getPureAppTagsArray().add(subFieldAdt7);
//
//                        DocRetrieval.Companion.getPureAdapters().add(DocRetrieval.Companion.getDocAdapter0());
//                        DocRetrieval.Companion.getPureAdapters().add(DocRetrieval.Companion.getDocAdapter1());
//                        DocRetrieval.Companion.getPureAdapters().add(DocRetrieval.Companion.getDocAdapter2());
//                        DocRetrieval.Companion.getPureAdapters().add(DocRetrieval.Companion.getDocAdapter3());
//                        DocRetrieval.Companion.getPureAdapters().add(DocRetrieval.Companion.getDocAdapter4());
//                        DocRetrieval.Companion.getPureAdapters().add(DocRetrieval.Companion.getDocAdapter5());
//                        DocRetrieval.Companion.getPureAdapters().add(DocRetrieval.Companion.getDocAdapter6());
//                        DocRetrieval.Companion.getPureAdapters().add(DocRetrieval.Companion.getDocAdapter7());
//
//
//                        try {
//
//                            holder.docDisplayRV.setAdapter(DocRetrieval.Companion.getPureAdapters().get(holder.getAbsoluteAdapterPosition()));
//                            holder.docTags.setAdapter(DocRetrieval.Companion.getPureAppTagsArray().get(holder.getAbsoluteAdapterPosition()));
//
//                            holder.departmentIndicator.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    holder.docDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getPureAdapters().get(holder.getAbsoluteAdapterPosition()).docPosition++);
//                                    holder.docTags.smoothScrollToPosition(DocRetrieval.Companion.getPureAppTagsArray().get(holder.getAbsoluteAdapterPosition()).getTagPosition() + 1);
//                                }
//                            });
//
//                            searchDocs.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                                @Override
//                                public boolean onQueryTextSubmit(String query) {
//                                    return false;
//                                }
//
//                                @Override
//                                public boolean onQueryTextChange(String newText) {
//                                    for (DocumentAdapter a : DocRetrieval.Companion.getPureAdapters()) {
//                                        if (a != null) {
//                                            a.getFilter().filter(newText);
//                                        }
//                                    }
//                                    for (SubFieldAdt subFieldAdt : DocRetrieval.Companion.getPureAppTagsArray()) {
//                                        if (subFieldAdt != null) {
//                                            subFieldAdt.getFilter().filter(newText);
//                                        }
//                                    }
//                                    return false;
//                                }
//                            });
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//
//            }
//            break;
//            case "Visual & Performing Art": {
//
//                departmentsArray = DocSorting.getSubFields(13);
//                schoolDepartments.addAll(Arrays.asList(departmentsArray));
//
//                publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        documents = new ArrayList<>();
//                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
//                            SelectedDoc document = qds.toObject(SelectedDoc.class);
//                            documents.add(document);
//                        }
//
//                        DocRetrieval.Companion.setVisualArtTags0(new ArrayList<>());
//                        DocRetrieval.Companion.setVisualArtTags1(new ArrayList<>());
//                        DocRetrieval.Companion.setVisualArtTags2(new ArrayList<>());
//
//                        DocRetrieval.Companion.setVisualArt0(new ArrayList<>());
//                        DocRetrieval.Companion.setVisualArt1(new ArrayList<>());
//                        DocRetrieval.Companion.setVisualArt2(new ArrayList<>());
//
//                        for (SelectedDoc document : documents) {
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(13)[0])) {
//                                DocRetrieval.Companion.getVisualArt0().add(document);
//                                DocRetrieval.Companion.setVisualAdapter0(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getVisualArt0()));
//                                DocRetrieval.Companion.getVisualAdapter0().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getVisualArtTags0().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getVisualArtTags0().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getVisualArtTags0());
//                                subFieldAdt0.notifyDataSetChanged();
//
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(13)[1])) {
//                                DocRetrieval.Companion.getVisualArt0().add(document);
//                                DocRetrieval.Companion.setVisualAdapter1(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getVisualArt1()));
//                                DocRetrieval.Companion.getVisualAdapter1().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getVisualArtTags1().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getVisualArtTags1().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getVisualArtTags1());
//                                subFieldAdt1.notifyDataSetChanged();
//
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(13)[2])) {
//                                DocRetrieval.Companion.getVisualArt0().add(document);
//                                DocRetrieval.Companion.setVisualAdapter2(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getVisualArt2()));
//                                DocRetrieval.Companion.getVisualAdapter2().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getVisualArtTags2().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getVisualArtTags2().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt2 = new SubFieldAdt(c, DocRetrieval.Companion.getVisualArtTags2());
//                                subFieldAdt2.notifyDataSetChanged();
//                            }
//
//                        }
//
//                        holder.readProgress.setVisibility(View.INVISIBLE);
//
//                        DocRetrieval.Companion.getVisualArtTagsArray().add(subFieldAdt0);
//                        DocRetrieval.Companion.getVisualArtTagsArray().add(subFieldAdt1);
//                        DocRetrieval.Companion.getVisualArtTagsArray().add(subFieldAdt2);
//
//                        DocRetrieval.Companion.getVisualAdapters().add(DocRetrieval.Companion.getVisualAdapter0());
//                        DocRetrieval.Companion.getVisualAdapters().add(DocRetrieval.Companion.getVisualAdapter1());
//                        DocRetrieval.Companion.getVisualAdapters().add(DocRetrieval.Companion.getVisualAdapter2());
//
//                        try {
//
//                            holder.docDisplayRV.setAdapter(DocRetrieval.Companion.getVisualAdapters().get(position));
//                            holder.docTags.setAdapter(DocRetrieval.Companion.getVisualArtTagsArray().get(position));
//
//                            holder.departmentIndicator.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    holder.docDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getVisualAdapters().get(holder.getAbsoluteAdapterPosition()).docPosition++);
//                                    holder.docTags.smoothScrollToPosition(DocRetrieval.Companion.getVisualArtTagsArray().get(holder.getAbsoluteAdapterPosition()).getTagPosition() + 1);
//                                }
//                            });
//
//                            searchDocs.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                                @Override
//                                public boolean onQueryTextSubmit(String query) {
//                                    return false;
//                                }
//
//                                @Override
//                                public boolean onQueryTextChange(String newText) {
//                                    for (DocumentAdapter a : DocRetrieval.Companion.getVisualAdapters()) {
//                                        if (a != null) {
//                                            a.getFilter().filter(newText);
//                                        }
//                                    }
//                                    for (SubFieldAdt subFieldAdt : DocRetrieval.Companion.getVisualArtTagsArray()) {
//                                        if (subFieldAdt != null) {
//                                            subFieldAdt.getFilter().filter(newText);
//                                        }
//                                    }
//                                    return false;
//                                }
//                            });
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            }
//            break;
//            case "Confucius Institute": {
//
//                departmentsArray = DocSorting.getSubFields(14);
//                schoolDepartments.addAll(Arrays.asList(departmentsArray));
//
//                publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
//                            SelectedDoc document = qds.toObject(SelectedDoc.class);
//                            documents.add(document);
//                        }
//
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            }
//            break;
//            case "Peace & Security Studies": {
//                departmentsArray = DocSorting.getSubFields(15);
//                schoolDepartments.addAll(Arrays.asList(departmentsArray));
//
//                publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
//                            SelectedDoc document = qds.toObject(SelectedDoc.class);
//                            documents.add(document);
//                        }
//
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//
//            }
//            break;
//            case "Creative Arts, Film & Media Studies": {
//
//                departmentsArray = DocSorting.getSubFields(16);
//                schoolDepartments.addAll(Arrays.asList(departmentsArray));
//
//                publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        documents = new ArrayList<>();
//                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
//                            SelectedDoc document = qds.toObject(SelectedDoc.class);
//                            documents.add(document);
//                        }
//
//                        DocRetrieval.Companion.setCreativeArtTags0(new ArrayList<>());
//                        DocRetrieval.Companion.setCreativeArtTags1(new ArrayList<>());
//
//                        DocRetrieval.Companion.setCreativeArt0(new ArrayList<>());
//                        DocRetrieval.Companion.setCreativeArt1(new ArrayList<>());
//
//                        for (SelectedDoc document : documents) {
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(16)[0])) {
//                                DocRetrieval.Companion.getCreativeArt0().add(document);
//                                DocRetrieval.Companion.setCreativeArtAdapter0(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getCreativeArt0()));
//                                DocRetrieval.Companion.getCreativeArtAdapter0().notifyDataSetChanged();
//
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getCreativeArtTags0().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getCreativeArtTags0().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt0 = new SubFieldAdt(c, DocRetrieval.Companion.getCreativeArtTags0());
//                                subFieldAdt0.notifyDataSetChanged();
//                            }
//
//                            if (document.getDocMetaData().contains(DocSorting.getSubFields(16)[1])) {
//                                DocRetrieval.Companion.getCreativeArt1().add(document);
//                                DocRetrieval.Companion.setCreativeArtAdapter1(new DocumentAdapter(thisSchool, context, c, DocRetrieval.Companion.getCreativeArt1()));
//                                DocRetrieval.Companion.getCreativeArtAdapter1().notifyDataSetChanged();
//
//                                String tags = document.getDocMetaData().split("_-_")[6].replace("[", "").replace("]", "");
//                                if (tags.split(",").length > 0) {
//                                    ArrayList<String> tagsProcessor = new ArrayList<>();
//                                    Collections.addAll(tagsProcessor, document.getDocMetaData().split("_-_")[6].split(","));
//                                    for (int i = 0; i < tagsProcessor.size(); i++) {
//                                        if (!(DocRetrieval.Companion.getCreativeArtTags1().contains(tagsProcessor.get(i)))) {
//                                            DocRetrieval.Companion.getCreativeArtTags1().add(tagsProcessor.get(i));
//                                        }
//                                    }
//                                }
//                                subFieldAdt1 = new SubFieldAdt(c, DocRetrieval.Companion.getCreativeArtTags1());
//                                subFieldAdt1.notifyDataSetChanged();
//                            }
//
//                        }
//
//                        holder.readProgress.setVisibility(View.INVISIBLE);
//
//                        DocRetrieval.Companion.getCreativeArtTagsArray().add(subFieldAdt0);
//                        DocRetrieval.Companion.getCreativeArtTagsArray().add(subFieldAdt1);
//
//                        DocRetrieval.Companion.getCreativeArtAdapters().add(DocRetrieval.Companion.getCreativeArtAdapter0());
//                        DocRetrieval.Companion.getCreativeArtAdapters().add(DocRetrieval.Companion.getCreativeArtAdapter1());
//
//
//                        try {
//
//                            holder.docDisplayRV.setAdapter(DocRetrieval.Companion.getCreativeArtAdapters().get(holder.getAbsoluteAdapterPosition()));
//                            holder.docTags.setAdapter(DocRetrieval.Companion.getCreativeArtTagsArray().get(holder.getAbsoluteAdapterPosition()));
//
//                            holder.departmentIndicator.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    holder.docDisplayRV.smoothScrollToPosition(DocRetrieval.Companion.getCreativeArtAdapters().get(holder.getAbsoluteAdapterPosition()).docPosition++);
//                                    holder.docTags.smoothScrollToPosition(DocRetrieval.Companion.getCreativeArtTagsArray().get(holder.getAbsoluteAdapterPosition()).getTagPosition() + 1);
//                                }
//                            });
//
//                            searchDocs.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                                @Override
//                                public boolean onQueryTextSubmit(String query) {
//                                    return false;
//                                }
//
//                                @Override
//                                public boolean onQueryTextChange(String newText) {
//                                    for (DocumentAdapter a : DocRetrieval.Companion.getCreativeArtAdapters()) {
//                                        if (a != null) {
//                                            a.getFilter().filter(newText);
//                                        }
//                                    }
//                                    for (SubFieldAdt subFieldAdt : DocRetrieval.Companion.getCreativeArtTagsArray()) {
//                                        if (subFieldAdt != null) {
//                                            subFieldAdt.getFilter().filter(newText);
//                                        }
//                                    }
//                                    return false;
//                                }
//                            });
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            }
//            break;
//            case "Architecture": {
//                departmentsArray = DocSorting.getSubFields(17);
//                schoolDepartments.addAll(Arrays.asList(departmentsArray));
//                publishedDocs.collection(dfd).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
//                            SelectedDoc document = qds.toObject(SelectedDoc.class);
//                            documents.add(document);
//                        }
//
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//            break;
//
//        }
//
//        //YEAR FILTER
//        final ArrayAdapter<CharSequence> yearFilters = ArrayAdapter.createFromResource(c,
//                R.array.yearFilter, R.layout.spinner_drop_down_yangu1);
//        yearFilters.setDropDownViewResource(R.layout.spinner_drop_down_yangu1);
//
//        holder.docDisplayRV.setLayoutManager(new LinearLayoutManager(c, RecyclerView.HORIZONTAL, false));
//        if (documentAdapters.size() != 0) {
//            holder.docDisplayRV.setAdapter(documentAdapters.get(position));
//        }
//        holder.docTags.setLayoutManager(new LinearLayoutManager(c, RecyclerView.HORIZONTAL, false));
//
//        holder.departmentIndicator.setText(departments.get(position));
//
//
//    }

    }

    @Override
    public int getItemCount() {
        return departments.size();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static class DocDepartmentVH extends RecyclerView.ViewHolder {
        Button departmentIndicator;
        RecyclerView docDisplayRV, docTags, lecDisplayRV, lecTags;
        ImageButton addDocBtn;
        LinearLayout searchFilterLayout;
        TextView deptTitle;
        ImageView navRight;
        ProgressBar readProgress, readProgress1;

        public DocDepartmentVH(@NonNull View itemView) {
            super(itemView);
            departmentIndicator = itemView.findViewById(R.id.departmentIndicator);
            docDisplayRV = itemView.findViewById(R.id.documentRV);
            docTags = itemView.findViewById(R.id.docTags);
            searchFilterLayout = itemView.findViewById(R.id.searchFilterLayout);
            addDocBtn = itemView.findViewById(R.id.addDocButton);
            readProgress = itemView.findViewById(R.id.readProgress);

            lecDisplayRV = itemView.findViewById(R.id.lecDisplayRV);
            lecTags = itemView.findViewById(R.id.lecTags);
            deptTitle = itemView.findViewById(R.id.deptTitle);
            navRight = itemView.findViewById(R.id.navRight);
            readProgress1 = itemView.findViewById(R.id.readProgress1);
        }
    }

}
