package com.midland.ynote.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.midland.ynote.Adapters.DocDetailAdt;
import com.midland.ynote.Adapters.TagsAdapter;
import com.midland.ynote.Dialogs.LectureSubCategoryDialog;
import com.midland.ynote.Dialogs.LogInSignUp;
import com.midland.ynote.Dialogs.UploadApproval;
import com.midland.ynote.R;
import com.midland.ynote.Utilities.App;
import com.midland.ynote.Utilities.CustomScrollView;
import com.midland.ynote.Utilities.DocSorting;
import com.midland.ynote.Utilities.FilingSystem;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class VideoUploader extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText videoDescription, institutionSpinnerET, videoTitle;
    VideoView vidUploadPrev;
    Button addTag;
    Button publishVid;
    RecyclerView tagsRV;
    private CheckBox yr1, yr2, yr3, yr4, yr5, masters, phD, cert, dip;
    Spinner generalVidDescriptionSpinner, lectureSubCategorySpinner, institutionVidSpinner;
    String selectedVidUri, flag, vidMetaData,
            returnClass, generalDesc, subCat, knowledgeBase, school, vidTitle, institution;
    private String selectedDocName, docSize, docMetaData,
            selectedCover, institution1, unitCode, docDetail;
    Uri videoUri;
    ArrayList<String> tags = new ArrayList<>();
    StorageTask<UploadTask.TaskSnapshot> uploadTask;
    TextView customThumbnail, customThumbnail1;
    ImageView vidThumbnail;
    ImageFilterView imageFilterView;
    String semester;
    Bitmap thumbnail;

    CustomScrollView vidUploaderSV;
    private NotificationManagerCompat notificationManagerCompat;
    private FirebaseUser user;
    ImageButton scrollMode;
    Uri result;
    private DocDetailAdt metaDataAdt;
    private Boolean outsourced = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_uploader);
        final ArrayList<String> general = new ArrayList<>();

        yr1 = findViewById(R.id.yr1);
        yr2 = findViewById(R.id.yr2);
        yr3 = findViewById(R.id.yr3);
        yr4 = findViewById(R.id.yr4);
        yr5 = findViewById(R.id.yr5);
        cert = findViewById(R.id.cert);
        dip = findViewById(R.id.dip);
        masters = findViewById(R.id.masters);
        phD = findViewById(R.id.phd);
        videoTitle = findViewById(R.id.docTitle);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        String json = preferences.getString("User", "");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        ArrayList<String> userDetail = gson.fromJson(json, type);
        if (userDetail != null) {
            institution = userDetail.get(5);
        }
        yr1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            yr2.setChecked(false);
            yr3.setChecked(false);
            yr4.setChecked(false);
            yr5.setChecked(false);
            masters.setChecked(false);
            phD.setChecked(false);
            cert.setChecked(false);
            dip.setChecked(false);
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
            knowledgeBase = "Masters";
        });

        phD.setOnCheckedChangeListener((buttonView, isChecked) -> {
            yr2.setChecked(false);
            yr3.setChecked(false);
            yr4.setChecked(false);
            yr1.setChecked(false);
            masters.setChecked(false);
            cert.setChecked(false);
            dip.setChecked(false);
            knowledgeBase = "phD";
        });

        if (getIntent().getStringExtra("selectedVideo") != null){
            selectedVidUri = getIntent().getStringExtra("selectedVideo");
            school = getIntent().getStringExtra("school");
            vidTitle = getIntent().getStringExtra("fileName");
            flag = getIntent().getStringExtra("outsourced");
            videoTitle.setText(vidTitle);
            byte[] bytes = getIntent().getByteArrayExtra("thumbnail");
            if (bytes != null){
                thumbnail = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
        }else {
            Toast.makeText(VideoUploader.this, "Something's missing!", Toast.LENGTH_SHORT).show();
        }
        notificationManagerCompat = NotificationManagerCompat.from(VideoUploader.this);

        generalVidDescriptionSpinner = findViewById(R.id.generalVidSpinner);
        lectureSubCategorySpinner = findViewById(R.id.subCategory1);
        institutionVidSpinner = findViewById(R.id.institutionVidSpinner);
        institutionSpinnerET = findViewById(R.id.institutionSpinnerET);

        generalVidDescriptionSpinner.setOnItemSelectedListener(this);
        lectureSubCategorySpinner.setOnItemSelectedListener(this);
        institutionVidSpinner.setOnItemSelectedListener(this);

        final ArrayAdapter<CharSequence> generalLectureDescription = ArrayAdapter.createFromResource(this,
                R.array.main_fields, android.R.layout.simple_spinner_item);

        final ArrayAdapter<CharSequence> institutionAdt = ArrayAdapter.createFromResource(this,
                R.array.institutions, android.R.layout.simple_spinner_item);

        generalLectureDescription.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        generalVidDescriptionSpinner.setAdapter(generalLectureDescription);
        generalVidDescriptionSpinner.setSelection(generalLectureDescription.getPosition(school));

        institutionAdt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        institutionVidSpinner.setAdapter(institutionAdt);
        institutionVidSpinner.setSelection(institutionAdt.getPosition(institution));

        institutionVidSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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



//        if (flag.equals("OtherVideo")){
//            lectureSubCategorySpinner.setVisibility(View.GONE);
//            generalDescription.notifyDataSetChanged();
//            generalVidDescriptionSpinner.setAdapter(generalDescription);
//
//
//        }else if (flag.equals("CompletedLecture")){
//            lectureSubCategorySpinner.setVisibility(View.VISIBLE);
//
//
//        }
//
        tagsRV = findViewById(R.id.vidTagsRV);
        publishVid = findViewById(R.id.publishVid);
        addTag = findViewById(R.id.addTagButton);
        vidUploadPrev = findViewById(R.id.vidPrev);
        videoDescription = findViewById(R.id.descriptionEditText);
        customThumbnail = findViewById(R.id.customThumbnail);
        customThumbnail1 = findViewById(R.id.customThumbnail1);
        vidThumbnail = findViewById(R.id.videoThumbnail);
        imageFilterView = findViewById(R.id.imageViewFilter);
        vidUploaderSV = findViewById(R.id.vidUploaderSV);

        final ArrayAdapter<CharSequence> semAdapter = ArrayAdapter.createFromResource(this,
                R.array.semesters, R.layout.spinner_drop_down_yangu1);
        final ArrayAdapter<CharSequence> institutionsAdapter = ArrayAdapter.createFromResource(this,
                R.array.institutions, R.layout.spinner_drop_down_yangu1);
        ArrayAdapter < CharSequence > mainFieldAdapter = ArrayAdapter.createFromResource(this,
                R.array.main_fields, R.layout.spinner_drop_down_yangu);




        scrollMode = findViewById(R.id.scrollModeVidUploader);

        scrollMode.setOnClickListener(v -> {
            if (vidUploaderSV.isEnableScrolling()) {
                scrollMode.setImageResource(R.drawable.ic_lock);
                vidUploaderSV.setEnableScrolling(false);
            } else {
                scrollMode.setImageResource(R.drawable.ic_lock_open);
                vidUploaderSV.setEnableScrolling(true);
            }
        });

//        Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(getVideoPath(videoUri), MediaStore.Video.Thumbnails.MINI_KIND);
//        if (thumbnail != null){
//            Glide.with(getApplicationContext()).load(thumbnail).thumbnail((float)0.9).into(vidThumbnail);
//        }
//        Glide.with(getApplicationContext()).load(videoUri).thumbnail((float)0.9).into(imageFilterView);
//        thumbnailBitmap = imageFilterView.getDrawingCache();


        videoTitle.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        final TagsAdapter tagsAdapter = new TagsAdapter(getApplicationContext(), tags);
        tagsRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        tagsRV.setAdapter(tagsAdapter);

        customThumbnail.setOnClickListener(v -> checkAndroidVersion());
        customThumbnail1.setOnClickListener(v -> checkAndroidVersion());

        generalVidDescriptionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<String> subFields = new ArrayList<>(Arrays.asList(DocSorting.getSubFields(position)));
                if (subFields.size() == 0){
                    subFields.add("-select a field-");
                }else{
                    subFields.add(0, "-select a field-");
                }
                if (subFields.size() == 1){
                    findViewById(R.id.department).setVisibility(View.GONE);
                    findViewById(R.id.subCatCard).setVisibility(View.GONE);
                }else {
                    findViewById(R.id.department).setVisibility(View.VISIBLE);
                    findViewById(R.id.subCatCard).setVisibility(View.VISIBLE);
                }
                ArrayAdapter<String> subFieldAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_drop_down,
                        new ArrayList<>(subFields));
                subFieldAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                lectureSubCategorySpinner.setAdapter(subFieldAdapter);
                subFieldAdapter.notifyDataSetChanged();
                generalDesc = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lectureSubCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subCat = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        videoUri = Uri.parse(selectedVidUri);
        vidUploadPrev.setVideoURI(videoUri);
        vidUploadPrev.start();

        MediaController controller = new MediaController(this);
        controller.setMediaPlayer(vidUploadPrev);
        vidUploadPrev.setMediaController(controller);

        addTag.setOnClickListener(v -> {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            DialogFragment dialogFragment = new LectureSubCategoryDialog(VideoUploader.this);
            dialogFragment.show(getSupportFragmentManager(), "dialog");
        });
        publishVid.setOnClickListener(v -> {
            user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    if (institution.equals("Type my own") && institutionSpinnerET.getText().toString().trim().equals("")){
                        institutionSpinnerET.setError("Missing institution.");
                    }else if (subCat != null && subCat.equals("-select a field-") && findViewById(R.id.department).getVisibility() != View.GONE){
                        Toast.makeText(getApplicationContext(), "Please provide department details.", Toast.LENGTH_LONG).show();
                    }
                    else if (institution.equals("Type my own") && !institutionSpinnerET.getText().toString().trim().isEmpty()){
                        institution1 = institutionSpinnerET.getText().toString().trim();
                        createVidObjectNPopUp();                    }
                    else {
                        institution1 = institution;
                        createVidObjectNPopUp();                    }
                } else {
                    LogInSignUp logInSignUp = new LogInSignUp(VideoUploader.this);
                    logInSignUp.show();
                }
            });

    }


    @Override
    protected void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            vidUploadPrev.pause();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private void releasePlayer() {
        vidUploadPrev.stopPlayback();
    }



    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private String stringEveryMetaData(String generalDescription, EditText videoDescription, ArrayList<String> videoTags) {
        StringBuilder stringedTags = new StringBuilder();
        for (String s : videoTags) {
            String s1 = s + ", ";
            stringedTags.append(s1);
        }
        return generalDescription + "_-_" + videoDescription.getText().toString() + "_-_" + stringedTags;
    }

    public void writeUploadedFile() {
        //STORING A MANIFEST OF ALL UPLOADED VIDEOS. TITLES, URIS, VIEWS & COMMENTS
        //PERHAPS I DON'T NEED THIS GIVEN THAT FIREBASE HAS 10MBz WORTH OF OFFLINE CAPABILITIES
        if (FilingSystem.Companion.isExternalStorageReadWritable() && FilingSystem.Companion.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getApplicationContext())) {
            File savedLecture = new File(FilingSystem.Companion.getPendingLectures(), "CloudVideos" + ".dl");
            try {
                String titles = readVideoFileMetaData("CloudVideos.dl")[0];
                String uris = readVideoFileMetaData("CloudVideos.dl")[1];
                String views = readVideoFileMetaData("CloudVideos.dl")[2];
                String comments = readVideoFileMetaData("CloudVideos.dl")[3];
                FileOutputStream fos = new FileOutputStream(savedLecture);
                fos.write((videoTitle.getText().toString() + "_docTitle_" + titles + "_-_").getBytes());
                fos.write((selectedVidUri + "_docUri_" + uris + "_-_").getBytes());
                fos.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private ArrayList<String> metaDataShredder(String flag) {
        ArrayList<String> docTitlesArray = new ArrayList<>();
        ArrayList<String> docUrisArray = new ArrayList<>();
        ArrayList<String> requestedData = new ArrayList<>();

        String[] docTitles;
        String[] docUris;

        String titles = readVideoFileMetaData("CloudVideos.dl")[0];
        String uris = readVideoFileMetaData("CloudVideos.dl")[1];

        docTitles = titles.split("_docTitle_");
        docUris = uris.split("_docUri_");

        // TODO: 8/30/2020 Check how lists are sized
        docTitlesArray.addAll(Arrays.asList(docTitles).subList(0, docTitles.length));
        docUrisArray.addAll(Arrays.asList(uris).subList(0, docUris.length));

        if (flag.equals("docTitles")) {
            requestedData = docTitlesArray;
        } else if (flag.equals("docUris")) {
            requestedData = docUrisArray;
        }

        return requestedData;
    }

    private String replacer(String docName) {
        String docName1 = docName.replace("]", "");
        String docName2 = docName1.replace("[", "");
        String docName3 = docName2.replace(".", "");
        String docName4 = docName3.replace("$", "");
        String docName5 = docName4.replace("*", "");
        return docName5.replace("#", "");
    }

    public String[] readVideoFileMetaData(String fileName) {
        //RETURNS VID TITLES[0], VID URIs[1], VID VIEWS[2], VID COMMENTS[3] AND SO MUCH MORE

        File file = new File(FilingSystem.Companion.getPendingLectures(), fileName + ".dl");
        String metaDataArray[] = new String[0];

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                metaDataArray = line.split("_-_");
            }
            br.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(getApplicationContext(), "File not found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return metaDataArray;
    }

    public void uploadProgressChannel(String fileName, final int totalByteCount, final int bytesTransferred) {
        final NotificationCompat.Builder notification = new NotificationCompat.Builder(VideoUploader.this, App.CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_baseline_cloud_uploaded)
                .setContentTitle(fileName)
                .setContentText("Uploading...")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .setOnlyAlertOnce(true);
        notificationManagerCompat.notify(2, notification.build());

        new Thread(new Runnable() {
            @Override
            public void run() {
                notification.setProgress(totalByteCount, bytesTransferred, false);
                notification.setContentText("Done!").setOngoing(false)
                        .setProgress(totalByteCount, bytesTransferred, false);
                notificationManagerCompat.notify(2, notification.build());
            }
        });



//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                SystemClock.sleep(2000);
//                for (int progress = 0; progress <= 100; progress += 10){
//                    SystemClock.sleep(1000);
//                }
//
//            }
//        }).start();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        ArrayAdapter<String> subFieldAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_drop_down,
                new ArrayList<>(Arrays.asList(DocSorting.getSubFields(position))));

        subFieldAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lectureSubCategorySpinner.setAdapter(subFieldAdapter);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void writePublishedToManifest(Uri uri, String url) {
        if (isExternalStorageReadWritable() && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            File savedLecture = new File(FilingSystem.Companion.getUploadedLecturesManifest(), "publishedLectures.yNoteManifest|encrypt.yn");
            try {
                FileOutputStream fos = new FileOutputStream(savedLecture);
                if (readPublishedManifest() != null) {
                    fos.write((readPublishedManifest() + uri.toString() + "zyau" + url + "_-_").getBytes());
                } else {
                    fos.write((uri.toString() + "zyau" + url + "_-_").getBytes());
                }
                fos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(VideoUploader.this, "Something happened. You might lose your points.", Toast.LENGTH_LONG).show();
        }
    }

    public String readPublishedManifest() {
        File file = new File(FilingSystem.Companion.getUploadedLecturesManifest(), "publishedLectures.yNoteManifest|encrypt.yn");
        String line = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            line = br.readLine();
//            while ((line = br.readLine()) != null) {
//                textArray = line.split("_-_");
//            }
            br.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(VideoUploader.this, "File not found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;

    }

    public boolean isExternalStorageReadWritable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.i("State", "writable");
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPermission(String permission) {
        int check = ActivityCompat.checkSelfPermission(VideoUploader.this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    private void createVidObjectNPopUp() {

        if (videoDescription.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "A brief description is required.", Toast.LENGTH_SHORT).show();
        } else {
            String nodeName = replacer(videoTitle.getText().toString().trim()) + "_-_" + System.currentTimeMillis();
            //Name, general, sub, desc, tags, downloadLink, uID, userName
//            ArrayList<String> descriptionArray = new ArrayList<>();
//            descriptionArray.add(0, videoTitle.getText().toString());
//            descriptionArray.add(1, generalDesc);
//            descriptionArray.add(2, subCat);
//            descriptionArray.add(3, videoDescription.getText().toString());
//            descriptionArray.add(4, FilingSystem.Companion.getAllTags().toString());


            if (flag == null || flag.equals("")){
                outsourced = false;
                FilingSystem.Companion.getAllTags().add("Studio lecture");
            }else {
                outsourced = true;
                FilingSystem.Companion.getAllTags().add("Outsourced");
            }
            final String vidDesc = videoTitle.getText().toString() + "_-_" + generalDesc + "_-_"
                    + subCat + "_-_" + videoDescription.getText().toString() +
                    "_-_" + FilingSystem.Companion.getAllTags();
            UploadApproval uploadApproval = new UploadApproval(VideoUploader.this, videoUri,
                    institution1, vidDesc, user, nodeName, videoTitle.getText().toString(),
                    notificationManagerCompat, result, thumbnail, knowledgeBase, VideoUploader.this, outsourced);
            uploadApproval.show();
        }
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
                    result = CropImage.getActivityResult(data).getUri();
                    ImageView bitmapTester = findViewById(R.id.bitmapTester);
                    bitmapTester.setVisibility(View.VISIBLE);
                    Glide.with(VideoUploader.this).load(result).thumbnail((float)0.9)
                            .placeholder(R.color.com_facebook_device_auth_text).into(bitmapTester);

                    Glide.with(VideoUploader.this).load(result).thumbnail((float)0.9)
                            .placeholder(R.color.com_facebook_device_auth_text).into(imageFilterView);
                }
                break;

        }
    }

    private void checkAndroidVersion() {
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getParent(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        && ActivityCompat.shouldShowRequestPermissionRationale(getParent(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Snackbar.make(findViewById(R.id.homeRel), "Permission", Snackbar.LENGTH_INDEFINITE)
                            .setAction("ENABLE", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ActivityCompat.requestPermissions(getParent(), new String[]{
                                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    }, 8);
                                }
                            }).show();
                } else {
                    ActivityCompat.requestPermissions(getParent(), new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, 8);
                }
            }
            else {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE);
//                pickImage();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE);        }
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
        Snackbar snackbar = Snackbar.make(findViewById(R.id.homeRel), s, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

//    private String getVideoPath(Uri uri){
//        String[] projections = {MediaStore.Images.Media.DATA};
//        Cursor c = null;
//        if (uri != null) {
//            c = getContentResolver().query(uri, projections, null, null, null);
//        }
//
//        int column_index = c.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
//        c.moveToFirst();
//        return c.getString(column_index);
//    }
}