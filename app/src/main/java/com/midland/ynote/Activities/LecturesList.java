package com.midland.ynote.Activities;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.midland.ynote.R;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.midland.ynote.Adapters.DocDepartmentAdapter;
import com.midland.ynote.Dialogs.VidType;
import com.midland.ynote.MainActivity;
import com.midland.ynote.Utilities.AdMob;
import com.midland.ynote.Utilities.DocRetrieval;
import com.midland.ynote.Utilities.DocSorting;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class LecturesList extends AppCompatActivity {

    private static final int SELECT_VID_REQUEST = 76;
    public RelativeLayout ratingRel;
    private RatingBar ratingBar;
    private SimpleExoPlayer exoPlayer;
    private PlayerView playerView;
    private String dept;
    private static  int pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectures_list);

        Toolbar toolbar = findViewById(R.id.lecListToolbar);
        setSupportActionBar(toolbar);

        View bottomSheet = findViewById(R.id.vid_comments_sheet);
        playerView = findViewById(R.id.lecturePlayer);
        ratingRel = findViewById(R.id.ratingRelActivity);
        ratingBar = findViewById(R.id.ratingBarActivity);
        Button submitButton = findViewById(R.id.submitRateActivity);
        ImageView vidRating = findViewById(R.id.lecRatings);
        ImageView home = findViewById(R.id.home);
        ImageView search = findViewById(R.id.search);
        ImageView add = findViewById(R.id.add);

        add.setOnClickListener(v ->{
            VidType vidType = new VidType(LecturesList.this, null, LecturesList.this, dept);
            vidType.show();
        });
        home.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            this.finish();
        });


        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);


        submitButton.setOnClickListener(v -> {
            String s = String.valueOf(ratingBar.getRating());
            Toast.makeText(getApplicationContext(), s + " stars", Toast.LENGTH_SHORT).show();
            SystemClock.sleep(1300);
            ratingRel.setVisibility(View.GONE);
        });

        vidRating.setOnClickListener(v -> {
            if (ratingRel.getVisibility() == View.GONE){
                ratingRel.bringToFront();
                ratingRel.setVisibility(View.VISIBLE);
            }else {
                ratingRel.setVisibility(View.GONE);
            }

        });


        RecyclerView deptLecturesRV = findViewById(R.id.deptLecturesRV);
        ProgressBar fetchProgress = findViewById(R.id.lecFetchProgress);
        fetchProgress.bringToFront();
        fetchProgress.setVisibility(View.VISIBLE);

        FloatingActionButton fab = findViewById(R.id.fabLectureList);
        fab.bringToFront();
        fab.setOnClickListener(v -> {
            VidType vidType = new VidType(LecturesList.this, null, LecturesList.this, dept);
            vidType.show();
        });
        deptLecturesRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

        dept = getIntent().getStringExtra("child");
        ArrayList<String> schoolDepartments;

        if (getIntent().getStringExtra("snackBar") != null){
            Snackbar.make(findViewById(R.id.lecListCord), getIntent().getStringExtra("snackBar"), Snackbar.LENGTH_SHORT).show();
        }

        if (dept != null){
            schoolDepartments = new ArrayList<>();
            switch (dept){
                case "Agriculture & Enterprise Development":
                    pos = 0;
                    String[] departments0 = DocSorting.getSubFields(pos);
                    schoolDepartments.addAll(Arrays.asList(departments0));
                    DocDepartmentAdapter docDepartmentAdapter0 = new DocDepartmentAdapter(null, LecturesList.this,
                            LecturesList.this, getApplicationContext(),
                            schoolDepartments, dept, null, DocRetrieval.Companion.getAgrAdapters(), null, getApplication(), getParent(), fetchProgress, "LecturesList");
                    docDepartmentAdapter0.notifyDataSetChanged();
                    deptLecturesRV.setAdapter(docDepartmentAdapter0);
                    break;

                case "Applied Human Sciences":
                    pos = 1;
                    String[] departments1 = DocSorting.getSubFields(pos);
                    schoolDepartments.addAll(Arrays.asList(departments1));
                    DocDepartmentAdapter docDepartmentAdapter1 = new DocDepartmentAdapter(null, LecturesList.this,
                            LecturesList.this, getApplicationContext(),
                            schoolDepartments, dept, null, DocRetrieval.Companion.getAppHumanAdapters(), null, getApplication(), getParent(), fetchProgress, "LecturesList");
                    docDepartmentAdapter1.notifyDataSetChanged();
                    deptLecturesRV.setAdapter(docDepartmentAdapter1);
                    break;

                case "Business":
                    pos = 2;
                    String[] departments2 = DocSorting.getSubFields(pos);
                    schoolDepartments.addAll(Arrays.asList(departments2));
                    DocDepartmentAdapter docDepartmentAdapter2 = new DocDepartmentAdapter(null, LecturesList.this,
                            LecturesList.this, getApplicationContext(),
                            schoolDepartments, dept, null, DocRetrieval.Companion.getBizAdapters(), null, getApplication(), getParent(), fetchProgress, "LecturesList");
                    docDepartmentAdapter2.notifyDataSetChanged();
                    deptLecturesRV.setAdapter(docDepartmentAdapter2);
                    break;

                case "Economics":
                    pos = 3;
                    String[] departments3 = DocSorting.getSubFields(pos);
                    schoolDepartments.addAll(Arrays.asList(departments3));
                    DocDepartmentAdapter docDepartmentAdapter3 = new DocDepartmentAdapter(null, LecturesList.this,
                            LecturesList.this, getApplicationContext(),
                            schoolDepartments, dept, null, DocRetrieval.Companion.getEconAdapters(), null, getApplication(), getParent(), fetchProgress, "LecturesList");
                    docDepartmentAdapter3.notifyDataSetChanged();
                    deptLecturesRV.setAdapter(docDepartmentAdapter3);
                    break;

                case "Education":
                    pos = 4;
                    String[] departments4 = DocSorting.getSubFields(pos);
                    schoolDepartments.addAll(Arrays.asList(departments4));
                    DocDepartmentAdapter docDepartmentAdapter4 = new DocDepartmentAdapter(null, LecturesList.this,
                            LecturesList.this, getApplicationContext(),
                            schoolDepartments, dept, null, DocRetrieval.Companion.getEduAdapters(), null, getApplication(), getParent(), fetchProgress, "LecturesList");
                    docDepartmentAdapter4.notifyDataSetChanged();
                    deptLecturesRV.setAdapter(docDepartmentAdapter4);
                    break;

                case "Engineering & Technology":
                    pos = 5;
                    String[] departments5 = DocSorting.getSubFields(pos);
                    schoolDepartments.addAll(Arrays.asList(departments5));
                    DocDepartmentAdapter docDepartmentAdapter5 = new DocDepartmentAdapter(null, LecturesList.this,
                            LecturesList.this, getApplicationContext(),
                            schoolDepartments, dept, null, DocRetrieval.Companion.getEngAdapters(), null, getApplication(), getParent(), fetchProgress, "LecturesList");
                    docDepartmentAdapter5.notifyDataSetChanged();
                    deptLecturesRV.setAdapter(docDepartmentAdapter5);
                    break;

                case "Environmental Studies":
                    pos = 6;
                    String[] departments6 = DocSorting.getSubFields(pos);
                    schoolDepartments.addAll(Arrays.asList(departments6));
                    DocDepartmentAdapter docDepartmentAdapter6 = new DocDepartmentAdapter(null, LecturesList.this,
                            LecturesList.this, getApplicationContext(),
                            schoolDepartments, dept, null, DocRetrieval.Companion.getEnvAdapters(), null, getApplication(), getParent(), fetchProgress, "LecturesList");
                    docDepartmentAdapter6.notifyDataSetChanged();
                    deptLecturesRV.setAdapter(docDepartmentAdapter6);
                    break;

                case "Hospitality & Tourism":
                    pos = 7;
                    String[] departments7 = DocSorting.getSubFields(pos);
                    schoolDepartments.addAll(Arrays.asList(departments7));
                    DocDepartmentAdapter docDepartmentAdapter7 = new DocDepartmentAdapter(null, LecturesList.this,
                            LecturesList.this, getApplicationContext(),
                            schoolDepartments, dept, null, DocRetrieval.Companion.getHospitalityAdapters(), null, getApplication(), getParent(), fetchProgress, "LecturesList");
                    docDepartmentAdapter7.notifyDataSetChanged();
                    deptLecturesRV.setAdapter(docDepartmentAdapter7);
                    break;

                case "Humanities & Social Sciences":
                    pos = 8;
                    String[] departments8 = DocSorting.getSubFields(pos);
                    schoolDepartments.addAll(Arrays.asList(departments8));
                    DocDepartmentAdapter docDepartmentAdapter8 = new DocDepartmentAdapter(null, LecturesList.this,
                            LecturesList.this, getApplicationContext(),
                            schoolDepartments, dept, null, DocRetrieval.Companion.getHumanitiesAdapters(), null, getApplication(), getParent(), fetchProgress, "LecturesList");
                    docDepartmentAdapter8.notifyDataSetChanged();
                    deptLecturesRV.setAdapter(docDepartmentAdapter8);
                    break;

                case "Law":
                    pos = 9;
                    String[] departments9 = DocSorting.getSubFields(pos);
                    schoolDepartments.addAll(Arrays.asList(departments9));
                    DocDepartmentAdapter docDepartmentAdapter9 = new DocDepartmentAdapter(null, LecturesList.this,
                            LecturesList.this, getApplicationContext(),
                            schoolDepartments, dept, null, DocRetrieval.Companion.getLawAdapters(), null, getApplication(), getParent(), fetchProgress, "LecturesList");
                    docDepartmentAdapter9.notifyDataSetChanged();
                    deptLecturesRV.setAdapter(docDepartmentAdapter9);
                    break;

                case "Medicine":
                    pos = 10;
                    String[] departments10 = DocSorting.getSubFields(pos);
                    schoolDepartments.addAll(Arrays.asList(departments10));
                    DocDepartmentAdapter docDepartmentAdapter10 = new DocDepartmentAdapter(null, LecturesList.this,
                            LecturesList.this, getApplicationContext(),
                            schoolDepartments, dept, null, DocRetrieval.Companion.getMedAdapters(),
                            null, getApplication(), getParent(), fetchProgress, "LecturesList");
                    docDepartmentAdapter10.notifyDataSetChanged();
                    deptLecturesRV.setAdapter(docDepartmentAdapter10);
                    break;

                case "Public Health":
                    pos = 11;
                    String[] departments11 = DocSorting.getSubFields(pos);
                    schoolDepartments.addAll(Arrays.asList(departments11));
                    DocDepartmentAdapter docDepartmentAdapter11 = new DocDepartmentAdapter(null, LecturesList.this,
                            LecturesList.this, getApplicationContext(),
                            schoolDepartments, dept, null, DocRetrieval.Companion.getPublicHealthAdapters(), null, getApplication(), getParent(), fetchProgress, "LecturesList");
                    docDepartmentAdapter11.notifyDataSetChanged();
                    deptLecturesRV.setAdapter(docDepartmentAdapter11);
                    break;

                case "Pure & Applied Sciences":
                    pos = 12;
                    String[] departments12 = DocSorting.getSubFields(pos);
                    schoolDepartments.addAll(Arrays.asList(departments12));
                    DocDepartmentAdapter docDepartmentAdapter12 = new DocDepartmentAdapter(null, LecturesList.this,
                            LecturesList.this, getApplicationContext(),
                            schoolDepartments, dept, null, DocRetrieval.Companion.getPureAdapters(), null, getApplication(), getParent(), fetchProgress, "LecturesList");
                    docDepartmentAdapter12.notifyDataSetChanged();
                    deptLecturesRV.setAdapter(docDepartmentAdapter12);
                    break;

                case "Visual & Performing Art":
                    pos = 13;
                    String[] departments13 = DocSorting.getSubFields(pos);
                    schoolDepartments.addAll(Arrays.asList(departments13));
                    DocDepartmentAdapter docDepartmentAdapter13 = new DocDepartmentAdapter(null, LecturesList.this,
                            LecturesList.this, getApplicationContext(),
                            schoolDepartments, dept, null, DocRetrieval.Companion.getVisualAdapters(), null, getApplication(), getParent(), fetchProgress, "LecturesList");
                    docDepartmentAdapter13.notifyDataSetChanged();
                    deptLecturesRV.setAdapter(docDepartmentAdapter13);
                    break;

                case "Confucius Institute":
                    pos = 14;
                    String[] departments14 = DocSorting.getSubFields(pos);
                    schoolDepartments.addAll(Arrays.asList(departments14));
                    DocDepartmentAdapter docDepartmentAdapter14 = new DocDepartmentAdapter(null, LecturesList.this,
                            LecturesList.this, getApplicationContext(),
                            schoolDepartments, dept, null, null, null, getApplication(), getParent(), fetchProgress, "LecturesList");
                    docDepartmentAdapter14.notifyDataSetChanged();
                    deptLecturesRV.setAdapter(docDepartmentAdapter14);
                    break;

                case "Peace & Security Studies":
                    pos = 15;
                    String[] departments15 = DocSorting.getSubFields(pos);
                    schoolDepartments.addAll(Arrays.asList(departments15));
                    DocDepartmentAdapter docDepartmentAdapter15 = new DocDepartmentAdapter(null, LecturesList.this,
                            LecturesList.this, getApplicationContext(),
                            schoolDepartments, dept, null, null, null, getApplication(), getParent(), fetchProgress, "LecturesList");
                    docDepartmentAdapter15.notifyDataSetChanged();
                    deptLecturesRV.setAdapter(docDepartmentAdapter15);
                    break;

                case "Creative Arts, Film & Media Studies":
                    pos = 16;
                    String[] departments16 = DocSorting.getSubFields(pos);
                    schoolDepartments.addAll(Arrays.asList(departments16));
                    DocDepartmentAdapter docDepartmentAdapter16 = new DocDepartmentAdapter(null, LecturesList.this,
                            LecturesList.this, getApplicationContext(),
                            schoolDepartments, dept, null, DocRetrieval.Companion.getCreativeArtAdapters(), null, getApplication(), getParent(), fetchProgress, "LecturesList");
                    docDepartmentAdapter16.notifyDataSetChanged();
                    deptLecturesRV.setAdapter(docDepartmentAdapter16);
                    break;

                case "Architecture":
                    pos = 17;
                    String[] departments17 = DocSorting.getSubFields(pos);
                    schoolDepartments.addAll(Arrays.asList(departments17));
                    DocDepartmentAdapter docDepartmentAdapter17 = new DocDepartmentAdapter(null, LecturesList.this,
                            LecturesList.this, getApplicationContext(),
                            schoolDepartments, dept, null, null, null, getApplication(), getParent(), fetchProgress, "LecturesList");
                    docDepartmentAdapter17.notifyDataSetChanged();
                    deptLecturesRV.setAdapter(docDepartmentAdapter17);
                    break;


            }
        }

        search.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SearchResults.class);
            intent.putExtra("school", dept);
            intent.putExtra("pos", pos);
            intent.putExtra("flag", "Lecture search");
            startActivity(intent);
        });
        AdMob.Companion.interstitialAd(getApplicationContext(), this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_VID_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri vidUri = data.getData();
            File vidFile = new File(vidUri.toString());
            String vidTitle = vidFile.getName();
//            if (vidUri.toString().startsWith("content://")){
//                Cursor c = null;
//                try {
//                    c = getParent().getContentResolver().query(vidUri, null, null, null, null);
//                    if (c != null && c.moveToFirst()) {
//                        vidTitle = c.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME));
//                    }
//                } finally {
//                    c.close();
//                }
//            }else if (vidUri.toString().startsWith("file://")){
//                vidTitle = vidFile.getName();
//            }
            Intent intent = new Intent(getApplicationContext(), VideoUploader.class);
            intent.putExtra("selectedVideo", vidUri.toString());
            intent.putExtra("fileName", vidTitle);
            intent.putExtra("school", dept);
            intent.putExtra("outsourced", "True");
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(LecturesList.this);
        inflater.inflate(R.menu.doc_display_menu, menu);
//        inflater.inflate(R.menu.lectures_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.searchDocsMenu:
                Intent intent = new Intent(getApplicationContext(), SearchResults.class);
                intent.putExtra("school", dept);
                intent.putExtra("pos", pos);
                intent.putExtra("flag", "Lecture search");
                startActivity(intent);

                break;

            case R.id.addDoc:
                VidType vidType = new VidType(LecturesList.this, null, LecturesList.this, dept);
                vidType.show();
                break;

            case R.id.navigation:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void outSource(Context con){

        if (ContextCompat.checkSelfPermission(con, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(con,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(getParent(), Manifest.permission.CAMERA)
                    && ActivityCompat.shouldShowRequestPermissionRationale(getParent(), Manifest.permission.RECORD_AUDIO)) {
                Snackbar.make(findViewById(R.id.motionPicsRel), "Permission", Snackbar.LENGTH_INDEFINITE)
                        .setAction("ENABLE", v -> ActivityCompat.requestPermissions(getParent(), new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
                        }, SELECT_VID_REQUEST)).show();
            } else {
                ActivityCompat.requestPermissions(getParent(), new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
                }, SELECT_VID_REQUEST);
            }
        }else {
            Intent intent = new Intent();
            intent.setType("video/*"); //THE FILE CHOOSER WILL ONLY  SEE VIDEOS
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, SELECT_VID_REQUEST);
        }
    }

    private void setExoPlayer(Application app, Uri videoUri){
//        streamVidName.setText(videoName);
        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(app).build();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = ExoPlayerFactory.newSimpleInstance(app);

            DefaultHttpDataSourceFactory sourceFactory = new DefaultHttpDataSourceFactory("video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(videoUri,
                    sourceFactory, extractorsFactory, null, null);
            playerView.setPlayer(exoPlayer);
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            exoPlayer.setPlayWhenReady(true);


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("LectureList", "exoPlayer error " + e.toString());
        }
    }


    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    private void pausePlayer() {
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.getPlaybackState();
        }
    }

    private void resumePlayer() {
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(true);
            exoPlayer.getPlaybackState();
        }
    }

}