package com.midland.ynote.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.midland.ynote.Adapters.MotionPicturesSubCatRV;
import com.midland.ynote.Objects.SelectedDoc;
import com.midland.ynote.Objects.SelectedVideo;
import com.midland.ynote.R;
import com.midland.ynote.Utilities.DocRetrieval;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.midland.ynote.Adapters.MotionPicturesSubCatRV;
import com.midland.ynote.Objects.SelectedDoc;
import com.midland.ynote.Objects.SelectedVideo;
import com.midland.ynote.Utilities.DocRetrieval;

import java.util.ArrayList;
import java.util.Collections;

public class MotionPictures extends AppCompatActivity {

    private static final int SELECT_VID_REQUEST = 76;
    private RecyclerView humanitiesRV, humanitiesSubRV, agrRV, agrSubRV, appHumanRV, appHumanSubRV, bizRV, bizSubRV, econRV, econSubRV, engRV, engSubRV;
    private RecyclerView envRV, envSubRV, hospRV, hospSubRV, lawRV, lawSubRV, medicineRV, medicineSubRV, publicHealthRV, publicHealthSubRV, pureAppliedSciRV, pureAppliedSciSubRV;
    private RecyclerView visualRV, visualSubRV, confuciusRV, confuciusSubRV, creativeArtRV, creativeArtSubRV, peaceRV, peaceSubRV, architectureRV, architectureSubRV;
    private RecyclerView eduRV,eduSubRV;
    private Uri vidUri;
    public static String outsourced;
    ArrayList<String> agrTags = new ArrayList<>();
    ArrayList<String> appHumanTags = new ArrayList<>();
    ArrayList<String> bizTags = new ArrayList<>();
    ArrayList<String> econTags = new ArrayList<>();
    ArrayList<String> eduTags = new ArrayList<>();
    ArrayList<String> engTags = new ArrayList<>();
    ArrayList<String> envTags = new ArrayList<>();
    ArrayList<String> hospTags = new ArrayList<>();
    ArrayList<String> humanitiesTags = new ArrayList<>();
    ArrayList<String> lawTags = new ArrayList<>();
    ArrayList<String> medTags = new ArrayList<>();
    ArrayList<String> publicHealthTags = new ArrayList<>();
    ArrayList<String> pureTags = new ArrayList<>();
    ArrayList<String> visualTags = new ArrayList<>();
    ArrayList<String> confuciusTags = new ArrayList<>();
    ArrayList<String> peaceTags = new ArrayList<>();
    ArrayList<String> creativeArtTags = new ArrayList<>();
    ArrayList<String> archTags = new ArrayList<>();

    private ValueEventListener mDBListener;
    private DatabaseReference mDBRef0;
    private ArrayList<DatabaseReference> databaseReferences = new ArrayList<>();
    ArrayList<SelectedVideo> allLectures;
    ProgressBar fetchProgress;
//    SimpleExoPlayer exoPlayer;
//    PlayerView playerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion_pictures);
        //        playerView = findViewById(R.id.selectedVideoPlayer);
        FloatingActionButton fab = findViewById(R.id.fabMotionPics);
        fab.bringToFront();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                VidType vidType = new VidType(MotionPictures.this, getParent(), SELECT_VID_REQUEST);
//                vidType.show();
            }
        });

        initRVz();
        initDBRefs();

        fetchProgress = findViewById(R.id.fetchProgress);

//        if (getIntent().getStringExtra("selectedVideo") != null && getIntent().getStringExtra("fileName") != null) {
//            Intent intent = new Intent(MotionPictures.this, VideoUploader.class);
//            intent.putExtra("selectedVideo", getIntent().getStringExtra("selectedVideo"));
//            startActivity(intent);
////            VidUploadPopUp vidUploadPopUp = new VidUploadPopUp(MotionPictures.this, MotionPictures.this, getParent(),
////                    getIntent().getStringExtra("selectedVideo"),
////                    "CompletedLecture", getIntent().getStringExtra("fileName"), getSupportFragmentManager(), "MotionPictures");
////            vidUploadPopUp.show();
//        } else if (vidUri != null) {
//
//        }

    }

    private void initDBRefs() {
        allLectures = new ArrayList<>();
        mDBRef0 = FirebaseDatabase.getInstance().getReference().child("Lectures");
        mDBRef0.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot d : snapshot.getChildren()) {
                    SelectedDoc vid = d.getValue(SelectedVideo.class);
                    if (vid.getVideoDescription().contains("Agriculture & Enterprise Development")) {
                        DocRetrieval.Companion.getAgrLectures().add(vid);
                        if (vid.getVideoDescription().split("_-_")[4].split(",").length == 0){
                            ArrayList<String> tagsProcessor = new ArrayList<>();
                            Collections.addAll(tagsProcessor, vid.getVideoDescription().split("_-_")[4].split(","));
                            for (int i = 0; i < tagsProcessor.size(); i++){
                                if (!agrTags.contains(tagsProcessor.get(i))){
                                    agrTags.add(tagsProcessor.get(i));
                                    Toast.makeText(getApplicationContext(), tagsProcessor.get(i), Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(MotionPictures.this, "no tags", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                    if (vid.getVideoDescription().contains("Applied Human Sciences")) {
                        DocRetrieval.Companion.getAppHumanSciLectures().add(vid);
                    }
                    if (vid.getVideoDescription().contains("Business")) {
                        DocRetrieval.Companion.getBizLectures().add(vid);
                    }
                    if (vid.getVideoDescription().contains("Economics")) {
                        DocRetrieval.Companion.getEconLectures().add(vid);
                    }
                    if (vid.getVideoDescription().contains("Education")) {
                        DocRetrieval.Companion.getEducationLectures().add(vid);
                    }
                    if (vid.getVideoDescription().contains("Engineering & Technology")) {
                        DocRetrieval.Companion.getEngineeringLectures().add(vid);
                    }
                    if (vid.getVideoDescription().contains("Environmental Studies")) {
                        DocRetrieval.Companion.getEnvironmentalLectures().add(vid);
                    }
                    if (vid.getVideoDescription().contains("Hospitality & Tourism")) {
                        DocRetrieval.Companion.getHospitalityLectures().add(vid);
                    }
                    if (vid.getVideoDescription().contains("Humanities & Social Sciences")) {
                        DocRetrieval.Companion.getHumanitiesLectures().add(vid);
                    }
                    if (vid.getVideoDescription().contains("Law")) {
                        DocRetrieval.Companion.getLawLectures().add(vid);
                    }
                    if (vid.getVideoDescription().contains("Medicine")) {
                        DocRetrieval.Companion.getMedicineLectures().add(vid);
                    }
                    if (vid.getVideoDescription().contains("Public Health")) {
                        DocRetrieval.Companion.getPublicHealthLectures().add(vid);
                    }
                    if (vid.getVideoDescription().contains("Pure & Applied Sciences")) {
                        DocRetrieval.Companion.getPureAppSciLectures().add(vid);
                    }
                    if (vid.getVideoDescription().contains("Visual & Performing Art")) {
                        DocRetrieval.Companion.getVisualArtLectures().add(vid);
                    }
                    if (vid.getVideoDescription().contains("Confucius Institute")) {
                        DocRetrieval.Companion.getConfuciusLectures().add(vid);
                    }
                    if (vid.getVideoDescription().contains("Peace & Security Studies")) {
                        DocRetrieval.Companion.getPeaceSecurityLectures().add(vid);
                    }
                    if (vid.getVideoDescription().contains("Creative Arts, Film & Media Studies")) {
                        DocRetrieval.Companion.getCreativeArtLectures().add(vid);
                    }
                    if (vid.getVideoDescription().contains("Architecture")) {
                        DocRetrieval.Companion.getArchitectureLectures().add(vid);
                    }

//                    CloudVideosAdapter cloudVideosAdapter0 = new CloudVideosAdapter(MotionPictures.this, DocRetrieval.Companion.getAgrLectures(), getApplication(), getParent());
//                    cloudVideosAdapter0.notifyDataSetChanged();
//                    agrRV.setAdapter(cloudVideosAdapter0);
//                    CloudVideosAdapter cloudVideosAdapter1 = new CloudVideosAdapter(MotionPictures.this, DocRetrieval.Companion.getAppHumanSciLectures(), getApplication(), getParent());
//                    cloudVideosAdapter1.notifyDataSetChanged();
//                    appHumanRV.setAdapter(cloudVideosAdapter1);
//                    CloudVideosAdapter cloudVideosAdapter2 = new CloudVideosAdapter(MotionPictures.this, DocRetrieval.Companion.getBizLectures(), getApplication(), getParent());
//                    cloudVideosAdapter2.notifyDataSetChanged();
//                    bizRV.setAdapter(cloudVideosAdapter2);
//                    CloudVideosAdapter cloudVideosAdapter3 = new CloudVideosAdapter(MotionPictures.this, DocRetrieval.Companion.getEconLectures(), getApplication(), getParent());
//                    cloudVideosAdapter3.notifyDataSetChanged();
//                    econRV.setAdapter(cloudVideosAdapter3);
//                    CloudVideosAdapter cloudVideosAdapter = new CloudVideosAdapter(MotionPictures.this, DocRetrieval.Companion.getEducationLectures(), getApplication(), getParent());
//                    cloudVideosAdapter.notifyDataSetChanged();
//                    eduRV.setAdapter(cloudVideosAdapter);
//                    CloudVideosAdapter cloudVideosAdapter4 = new CloudVideosAdapter(MotionPictures.this, DocRetrieval.Companion.getEngineeringLectures(), getApplication(), getParent());
//                    cloudVideosAdapter4.notifyDataSetChanged();
//                    engRV.setAdapter(cloudVideosAdapter4);
//                    CloudVideosAdapter cloudVideosAdapter5 = new CloudVideosAdapter(MotionPictures.this, DocRetrieval.Companion.getEnvironmentalLectures(), getApplication(), getParent());
//                    cloudVideosAdapter5.notifyDataSetChanged();
//                    envRV.setAdapter(cloudVideosAdapter5);
//                    CloudVideosAdapter cloudVideosAdapter6 = new CloudVideosAdapter(MotionPictures.this, DocRetrieval.Companion.getHospitalityLectures(), getApplication(), getParent());
//                    cloudVideosAdapter6.notifyDataSetChanged();
//                    hospRV.setAdapter(cloudVideosAdapter6);
//                    CloudVideosAdapter cloudVideosAdapter7 = new CloudVideosAdapter(MotionPictures.this, DocRetrieval.Companion.getHumanitiesLectures(), getApplication(), getParent());
//                    cloudVideosAdapter7.notifyDataSetChanged();
//                    humanitiesRV.setAdapter(cloudVideosAdapter7);
//                    CloudVideosAdapter cloudVideosAdapter8 = new CloudVideosAdapter(MotionPictures.this, DocRetrieval.Companion.getLawLectures(), getApplication(), getParent());
//                    cloudVideosAdapter8.notifyDataSetChanged();
//                    lawRV.setAdapter(cloudVideosAdapter8);
//                    CloudVideosAdapter cloudVideosAdapter9 = new CloudVideosAdapter(MotionPictures.this, DocRetrieval.Companion.getMedicineLectures(), getApplication(), getParent());
//                    cloudVideosAdapter9.notifyDataSetChanged();
//                    medicineRV.setAdapter(cloudVideosAdapter9);
//                    CloudVideosAdapter cloudVideosAdapter10 = new CloudVideosAdapter(MotionPictures.this, DocRetrieval.Companion.getPublicHealthLectures(), getApplication(), getParent());
//                    cloudVideosAdapter10.notifyDataSetChanged();
//                    publicHealthRV.setAdapter(cloudVideosAdapter10);
//                    CloudVideosAdapter cloudVideosAdapter11 = new CloudVideosAdapter(MotionPictures.this, DocRetrieval.Companion.getPureAppSciLectures(), getApplication(), getParent());
//                    cloudVideosAdapter11.notifyDataSetChanged();
//                    pureAppliedSciRV.setAdapter(cloudVideosAdapter11);
//                    CloudVideosAdapter cloudVideosAdapter12 = new CloudVideosAdapter(MotionPictures.this, DocRetrieval.Companion.getVisualArtLectures(), getApplication(), getParent());
//                    cloudVideosAdapter12.notifyDataSetChanged();
//                    visualRV.setAdapter(cloudVideosAdapter12);
//                    CloudVideosAdapter cloudVideosAdapter13 = new CloudVideosAdapter(MotionPictures.this, DocRetrieval.Companion.getConfuciusLectures(), getApplication(), getParent());
//                    cloudVideosAdapter13.notifyDataSetChanged();
//                    confuciusRV.setAdapter(cloudVideosAdapter13);
//                    CloudVideosAdapter cloudVideosAdapter14 = new CloudVideosAdapter(MotionPictures.this, DocRetrieval.Companion.getPeaceSecurityLectures(), getApplication(), getParent());
//                    cloudVideosAdapter14.notifyDataSetChanged();
//                    peaceRV.setAdapter(cloudVideosAdapter14);
//                    CloudVideosAdapter cloudVideosAdapter15 = new CloudVideosAdapter(MotionPictures.this, DocRetrieval.Companion.getCreativeArtLectures(), getApplication(), getParent());
//                    cloudVideosAdapter15.notifyDataSetChanged();
//                    creativeArtRV.setAdapter(cloudVideosAdapter15);
//                    CloudVideosAdapter cloudVideosAdapter16 = new CloudVideosAdapter(MotionPictures.this, DocRetrieval.Companion.getArchitectureLectures(), getApplication(), getParent());
//                    cloudVideosAdapter16.notifyDataSetChanged();
//                    architectureRV.setAdapter(cloudVideosAdapter16);

                    fetchProgress.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initRVz() {
        String[] humanitiesSub = {"Visual arts", "Performing arts", "History", "Home eMotionPictures.thisomics", "Literature", "Law", "Philosophy", "Theology"};
        String[] socialSciSub = {"Anthropology", "Archaeology", "EMotionPictures.thisomics", "Geography", "Political science", "Psychology", "Sociology", "Social work"};
        String[] naturalSciSub = {"Biology", "Chemistry", "Physics", "Space sciences"};
        String[] formalSciSub = {"Computer science", "Mathematics", "Statistics"};
        String[] appliedSciSub = {"Business", "Engineering & Technology", "Medicine & health"};


//        ArrayList<String> humanitiesSubCat = new ArrayList<>();
//        Collections.addAll(humanitiesSubCat, humanitiesSub);
        MotionPicturesSubCatRV subCatRV1 = new MotionPicturesSubCatRV(MotionPictures.this, agrTags, "motionPictures");
        subCatRV1.notifyDataSetChanged();

        ArrayList<String> socialSciSubCat = new ArrayList<>();
        Collections.addAll(socialSciSubCat, socialSciSub);
        MotionPicturesSubCatRV subCatRV2 = new MotionPicturesSubCatRV(MotionPictures.this, socialSciSubCat, "motionPictures");
        subCatRV2.notifyDataSetChanged();

        ArrayList<String> naturalSciSubCat = new ArrayList<>();
        Collections.addAll(naturalSciSubCat, naturalSciSub);
        MotionPicturesSubCatRV subCatRV3 = new MotionPicturesSubCatRV(MotionPictures.this, naturalSciSubCat, "motionPictures");
        subCatRV3.notifyDataSetChanged();

        ArrayList<String> formalSciSubCat = new ArrayList<>();
        Collections.addAll(formalSciSubCat, formalSciSub);
        MotionPicturesSubCatRV subCatRV4 = new MotionPicturesSubCatRV(MotionPictures.this, formalSciSubCat, "motionPictures");
        subCatRV4.notifyDataSetChanged();

        ArrayList<String> appliedSciSubCat = new ArrayList<>();
        Collections.addAll(appliedSciSubCat, appliedSciSub);
        MotionPicturesSubCatRV subCatRV5 = new MotionPicturesSubCatRV(MotionPictures.this, appliedSciSubCat, "motionPictures");
        subCatRV5.notifyDataSetChanged();


        agrRV = findViewById(R.id.agriMotionPicsRV);
        agrSubRV = findViewById(R.id.agriSubCategoriesRV);
        agrRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        agrSubRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        agrSubRV.setAdapter(subCatRV1);

        envRV = findViewById(R.id.envMotionPicsRV);
        envSubRV = findViewById(R.id.envSubCategoriesRV);
        envRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        envSubRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        envSubRV.setAdapter(subCatRV1);

        bizRV = findViewById(R.id.bizMotionPicsRV);
        bizSubRV = findViewById(R.id.bizSubCategoriesRV);
        bizRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        bizSubRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        bizSubRV.setAdapter(subCatRV1);

        econRV = findViewById(R.id.econMotionPicsRV);
        econSubRV = findViewById(R.id.econSubCategoriesRV);
        econRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        econSubRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        econSubRV.setAdapter(subCatRV1);

        eduRV = findViewById(R.id.eduMotionPicsRV);
        eduSubRV = findViewById(R.id.eduSubCategoriesRV);
        econRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        econSubRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        econSubRV.setAdapter(subCatRV1);

        engRV = findViewById(R.id.engineeringMotionPicsRV);
        engSubRV = findViewById(R.id.engineeringSubCategoriesRV);
        engRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        engSubRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));

        appHumanRV = findViewById(R.id.appliedHumanSciencesMotionPicsRV);
        appHumanSubRV = findViewById(R.id.appliedHumanSciencesSubCategoriesRV);
        appHumanRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        appHumanSubRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        appHumanSubRV.setAdapter(subCatRV1);

        hospRV = findViewById(R.id.hospitalityMotionPicsRV);
        hospSubRV = findViewById(R.id.hospitalitySubCategoriesRV);
        hospRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        hospSubRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        hospSubRV.setAdapter(subCatRV1);

        lawRV = findViewById(R.id.lawMotionPicsRV);
        lawSubRV = findViewById(R.id.lawSubCategoriesRV);
        lawRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        lawSubRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        lawSubRV.setAdapter(subCatRV1);

        medicineRV = findViewById(R.id.medicineMotionPicsRV);
        medicineSubRV = findViewById(R.id.medicineSubCategoriesRV);
        medicineRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        medicineSubRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        medicineSubRV.setAdapter(subCatRV1);

        publicHealthRV = findViewById(R.id.publicHealthMotionPicsRV);
        publicHealthSubRV = findViewById(R.id.publicHealthSubCategoriesRV);
        publicHealthRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        publicHealthSubRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        publicHealthSubRV.setAdapter(subCatRV1);

        visualRV = findViewById(R.id.visualMotionPicsRV);
        visualSubRV = findViewById(R.id.visualSubCategoriesRV);
        visualRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        visualSubRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        visualSubRV.setAdapter(subCatRV1);

        confuciusRV = findViewById(R.id.confuciusMotionPicsRV);
        confuciusSubRV = findViewById(R.id.confuciusSubCategoriesRV);
        confuciusRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        confuciusSubRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        confuciusSubRV.setAdapter(subCatRV1);

        peaceRV = findViewById(R.id.peaceSecurityMotionPicsRV);
        peaceSubRV = findViewById(R.id.peaceSecuritySubCategoriesRV);
        peaceRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        peaceSubRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        peaceSubRV.setAdapter(subCatRV1);

        architectureRV = findViewById(R.id.architectureMotionPicsRV);
        architectureSubRV = findViewById(R.id.architectureSubCategoriesRV);
        architectureRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        architectureSubRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        architectureSubRV.setAdapter(subCatRV1);

        pureAppliedSciRV = findViewById(R.id.pureAppliedSciMotionPicsRV);
        pureAppliedSciSubRV = findViewById(R.id.pureAppliedSciSubCategoriesRV);
        pureAppliedSciRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        pureAppliedSciSubRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        pureAppliedSciSubRV.setAdapter(subCatRV1);

        creativeArtRV = findViewById(R.id.creativeArtMotionPicsRV);
        creativeArtSubRV = findViewById(R.id.creativeArtSubCategoriesRV);
        creativeArtRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        creativeArtSubRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        creativeArtSubRV.setAdapter(subCatRV1);

        humanitiesRV = findViewById(R.id.humanitiesMotionPicsRV);
        humanitiesSubRV = findViewById(R.id.humanitiesSubCategoriesRV);
        humanitiesRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        humanitiesSubRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        humanitiesSubRV.setAdapter(subCatRV1);


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_VID_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            vidUri = data.getData();
        }
    }

//    public void setExoPlayer(Application application, String vidName, String vidUrl){
//        try {
//            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(application).build();
//            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
//            exoPlayer = ExoPlayerFactory.newSimpleInstance(application);
//            Uri videoUri = Uri.parse(vidUrl);
//            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("video");
//            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
//            MediaSource mediaSource = new ExtractorMediaSource(videoUri, dataSourceFactory, extractorsFactory, null, null);
//            playerView.setPlayer(exoPlayer);
//            exoPlayer.prepare(mediaSource);
//            exoPlayer.setPlayWhenReady(false);
//        } catch (Exception e) {
//            Log.e("MotionPictures", "Exoplayer error " + e.toString());
//            e.printStackTrace();
//        }
//    }
}