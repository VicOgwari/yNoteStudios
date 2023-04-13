package com.midland.ynote.Utilities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.midland.ynote.Adapters.TagsAdapter;
import com.midland.ynote.Dialogs.LectureSubCategoryDialog;
import com.midland.ynote.Dialogs.LogInSignUp;
import com.midland.ynote.Objects.SelectedVideo;
import com.midland.ynote.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class VidUploadPopUp extends Dialog implements AdapterView.OnItemSelectedListener {

    Activity a;
    Context con;
    EditText videoDescription, videoTags, videoTitle;
    VideoView vidUploadPrev;
    Button addTag;
    Button publishVid;
    RecyclerView tagsRV;
    Spinner generalVidDescriptionSpinner, lectureSubCategorySpinner;
    String selectedVidUri, flag, vidMetaData, returnClass;
    Uri videoUri;
    ArrayList<String> tags = new ArrayList<>();
    StorageTask<UploadTask.TaskSnapshot> uploadTask;
    TextView uploadedVidUri, uploadedVidName, uploadProgressText;
    String fileName;
    private FragmentManager fm;
    private NotificationManagerCompat notificationManagerCompat;
    private FirebaseUser user;


    ProgressBar uploadProgress;


    public VidUploadPopUp(@NonNull Context context, Context con, Activity activity, String selectedVidUri,
                          String flag, String fileName, FragmentManager fm, String returnClass) {
        super(context);
        this.con = con;
        this.a = activity;
        this.selectedVidUri = selectedVidUri;
        this.flag = flag;
        this.fileName = fileName;
        this.fm = fm;
        this.returnClass = returnClass;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.vid_upload_pop_up);
        final ArrayList<String> general = new ArrayList<>();


        notificationManagerCompat = NotificationManagerCompat.from(con);

        generalVidDescriptionSpinner = findViewById(R.id.generalVidSpinner);
        lectureSubCategorySpinner = findViewById(R.id.subCategory1);
        final ArrayAdapter<CharSequence> generalLectureDescription = ArrayAdapter.createFromResource(con.getApplicationContext(),
                R.array.main_fields, R.layout.spinner_drop_down_yangu);

        generalLectureDescription.notifyDataSetChanged();
        generalVidDescriptionSpinner.setAdapter(generalLectureDescription);

        generalVidDescriptionSpinner.setOnItemSelectedListener(this);


        final CardView tagsCard = findViewById(R.id.tagsRVCardView);

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
        videoTitle = findViewById(R.id.docTitle);
        uploadProgressText = findViewById(R.id.uploadProgressText);
        uploadedVidUri = findViewById(R.id.vidUploadedUri);
        uploadedVidName = findViewById(R.id.vidUploadedName);
        videoTitle.setText(fileName);

        videoTitle.requestFocus();
        InputMethodManager imm = (InputMethodManager) con.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        final TagsAdapter tagsAdapter = new TagsAdapter(con.getApplicationContext(), tags);
        tagsRV.setLayoutManager(new LinearLayoutManager(con.getApplicationContext(), RecyclerView.HORIZONTAL, false));
        tagsRV.setAdapter(tagsAdapter);

        generalVidDescriptionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> subFieldAdapter = new ArrayAdapter<>(con.getApplicationContext(), R.layout.dropdown_drop_down, new ArrayList<>(Arrays.asList(DocSorting.getSubFields(position))));
                lectureSubCategorySpinner.setAdapter(subFieldAdapter);
                subFieldAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        lectureSubCategorySpinner.setOnItemSelectedListener(this);
        lectureSubCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        videoUri = Uri.parse(selectedVidUri);
        vidUploadPrev.setVideoURI(videoUri);
        vidUploadPrev.start();
        vidUploadPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vidUploadPrev.isPlaying()) {
                    vidUploadPrev.pause();
                } else {
                    vidUploadPrev.resume();
                }
            }
        });

        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = fm.beginTransaction();
                Fragment prev = fm.findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                DialogFragment dialogFragment = new LectureSubCategoryDialog(VidUploadPopUp.this);
                dialogFragment.show(fm, "dialog");
            }
        });

        publishVid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                if (flag.equals("OtherVideo")) {
                    if (videoDescription.getText().toString().trim().equals("")) {
                        Toast.makeText(con.getApplicationContext(), "A brief description is required.", Toast.LENGTH_SHORT).show();
                    } else if (uploadTask != null && uploadTask.isInProgress()) {
                        Toast.makeText(con.getApplicationContext(), "Give me a sec!", Toast.LENGTH_SHORT).show();

                    }
                    Toast.makeText(con.getApplicationContext(), readVideoFileMetaData("CloudVideos.dl")[0], Toast.LENGTH_SHORT).show();
                    Toast.makeText(con.getApplicationContext(), readVideoFileMetaData("CloudVideos.dl")[1], Toast.LENGTH_SHORT).show();
                    uploadOtherVideo();
                } else if (flag.equals("CompletedLecture")) {
                    if (videoDescription.getText().toString().trim().equals("")) {
                        Toast.makeText(con.getApplicationContext(), "A brief description is required.", Toast.LENGTH_SHORT).show();
                    } else if (uploadTask != null && uploadTask.isInProgress()) {
                        Toast.makeText(con.getApplicationContext(), "Give me a sec!", Toast.LENGTH_SHORT).show();

                    }
                    if (user != null) {
                        uploadCompletedLecture();
                    } else {
                        LogInSignUp logInSignUp = new LogInSignUp(con);
                        logInSignUp.show();
                    }
                }
            }
        });

    }


    private void uploadOtherVideo() {
        if (videoUri != null) {
            final DatabaseReference otherVidDbRef = FirebaseDatabase.getInstance().getReference(generalVidDescriptionSpinner.getSelectedItem().toString());
            StorageReference otherVidStoreRef = FirebaseStorage.getInstance().getReference(generalVidDescriptionSpinner.getSelectedItem().toString());
            StorageReference fileReference = otherVidStoreRef.child(System.currentTimeMillis() + "." + getFileExtension(videoUri));
            findViewById(R.id.progressLayout).setVisibility(View.VISIBLE);
            uploadProgress.bringToFront();
            //CREATES NEW UNIQUE ID
            //ACCEPTS lecture METADATA INTO THE KEY
            //HASH MAP SHALL BE USED TO ADD CREATOR'S NAME LATER
            //TO BE PLACED INSIDE A PROGRESS BAR LATER IN THE PROJECT
            //PROGRESS BAR SHALL BE SET BACK TO ZERO ON SUCCESS
            uploadTask = fileReference.putFile(videoUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            FilingSystem.Companion.writeUploadedVideosManifest(con, videoUri);
                            Toast.makeText(con.getApplicationContext(), "Upload successful", Toast.LENGTH_SHORT).show();
                            uploadProgress.setProgress(0);
                            vidMetaData = stringEveryMetaData(generalVidDescriptionSpinner.getSelectedItem().toString(), videoDescription, tags);
                            SelectedVideo selectedVideo = new SelectedVideo(videoUri.toString(), vidMetaData);
                            otherVidDbRef.child("Lectures").setValue(selectedVideo);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(con.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            uploadProgressChannel(fileName, (int) taskSnapshot.getTotalByteCount(), (int) taskSnapshot.getBytesTransferred());
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            uploadProgress.setProgress((int) progress);
                            uploadProgressText.setText(progress + "%");
                            //TO BE PLACED INSIDE A PROGRESS BAR LATER IN THE PROJECT
                            //PROGRESS BAR SHALL BE SET BACK TO ZERO ON SUCCESS
                        }
                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            findViewById(R.id.progressLayout).setVisibility(View.INVISIBLE);
                        }
                    });
        } else {
            Toast.makeText(con.getApplicationContext(), "Select something.", Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadCompletedLecture() {

        if (videoUri != null) {
            final DatabaseReference lectureDbRef = FirebaseDatabase.getInstance().getReference("Lectures");
            final DatabaseReference publishedLectures = FirebaseDatabase.getInstance().getReference().child("Users").child("Published lectures");

            final String lectureKey = lectureDbRef.push().getKey();
            StorageReference lectureStoreRef = FirebaseStorage.getInstance().getReference("Lectures");
            final StorageReference fileReference = lectureStoreRef.child(fileName + System.currentTimeMillis() + "." + getFileExtension(videoUri));
            findViewById(R.id.progressLayout).setVisibility(View.VISIBLE);
            uploadProgress.bringToFront();

            //Name, general, sub, desc, tags
            final String vidDesc = videoTitle.getText().toString() + "_-_" + generalVidDescriptionSpinner.getSelectedItem().toString() + "_-_"
                    + lectureSubCategorySpinner.getSelectedItem().toString() + videoDescription.getText().toString() +
                    "_-_" + FilingSystem.Companion.getAllTags().toString() + "_-_" + videoUri.toString();

            uploadTask = fileReference.putFile(videoUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }
                                    return fileReference.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        String userID = user.getUid();
                                        String nodeName = replacer(videoTitle.getText().toString().trim()) + "_-_" + System.currentTimeMillis();
                                        Uri downloadLink = task.getResult();
                                        SelectedVideo selectedVideo = new SelectedVideo(vidDesc, downloadLink.toString(), userID, nodeName);
//                                        writePublishedToManifest(videoUri, fileReference.getDownloadUrl().toString());
                                        Toast.makeText(con.getApplicationContext(), "Upload successful", Toast.LENGTH_SHORT).show();
                                        uploadProgress.setProgress(0);

                                        publishedLectures.setValue(nodeName);
                                        lectureDbRef.child(nodeName).
                                                setValue(selectedVideo);


                                    } else {
                                        Toast.makeText(con.getApplicationContext(), "Select something.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(con.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            uploadProgressChannel(fileName, (int) taskSnapshot.getTotalByteCount(), (int) taskSnapshot.getBytesTransferred());
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            uploadProgress.setProgress((int) progress);
                            uploadProgressText.setText(progress + "%");
                            //TO BE PLACED INSIDE A PROGRESS BAR LATER IN THE PROJECT
                            //PROGRESS BAR SHALL BE SET BACK TO ZERO ON SUCCESS
                        }
                    });
        } else {
            Toast.makeText(con.getApplicationContext(), "Select something.", Toast.LENGTH_SHORT).show();
        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = con.getContentResolver();
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
        if (FilingSystem.Companion.isExternalStorageReadWritable() && FilingSystem.Companion.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, con.getApplicationContext())) {
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
            Toast.makeText(con.getApplicationContext(), "File not found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return metaDataArray;
    }

    public void uploadProgressChannel(String fileName, final int totalByteCount, final int bytesTransferred) {
        final NotificationCompat.Builder notification = new NotificationCompat.Builder(con, App.CHANNEL_2_ID)
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
        ArrayAdapter<String> subFieldAdapter = new ArrayAdapter<>(con, R.layout.dropdown_drop_down,
                new ArrayList<>(Arrays.asList(DocSorting.getSubCategories(generalVidDescriptionSpinner.getSelectedItem().toString()))));
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
            Toast.makeText(getContext(), "Something happened. You might lose your points.", Toast.LENGTH_LONG).show();
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
            Toast.makeText(getContext(), "File not found", Toast.LENGTH_SHORT).show();
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
        int check = ActivityCompat.checkSelfPermission(con, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }
}
