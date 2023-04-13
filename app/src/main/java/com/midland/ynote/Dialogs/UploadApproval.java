package com.midland.ynote.Dialogs;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.midland.ynote.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.midland.ynote.Activities.LecturesList;
import com.midland.ynote.Activities.VideoUploader;
import com.midland.ynote.Utilities.App;
import com.midland.ynote.Utilities.Publisher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class UploadApproval extends Dialog {

    private ImageView vidPrev;
    private final Uri videoUri;
    private final Uri thumbnail;
    private final String vidDesc;
    private final String nodeName;
    private final String fileName;
    private final String knw;
    private StorageTask<UploadTask.TaskSnapshot> uploadTask, uploadTask1;
    private final NotificationManagerCompat managerCompat;
    private TextView uploadProgressText, vidTitle, userHandleUpload;
    private ImageButton options;
    private final FirebaseUser user;
    private Button upload, hide, cancel;
    private LinearLayout hideLayout;
    private RelativeLayout colorRel;
    private final Bitmap bitmap;
    private Boolean goAhead, outsourced;
    private int endColor;
    private String institution;
    ProgressBar uploadProgress;
    VideoUploader videoUploader;

    public UploadApproval(@NonNull Context context, Uri videoUri,
                          String institution, String vidDesc, FirebaseUser user,
                          String nodeName, String fileName,
                          NotificationManagerCompat managerCompat, Uri thumbnail,
                          Bitmap bitmap, String knw, VideoUploader videoUploader, Boolean outsourced) {
        super(context);
        this.videoUri = videoUri;
        this.user = user;
        this.institution = institution;
        this.vidDesc = vidDesc;             //Name, general, sub, desc, tags, downloadLink, uID, displayName
        this.nodeName = nodeName;
        this.fileName = fileName;
        this.thumbnail = thumbnail;
        this.videoUploader = videoUploader;
        this.bitmap = bitmap;
        this.managerCompat = managerCompat;
        this.knw = knw;
        this.outsourced = outsourced;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_upload_approval);
        vidPrev = findViewById(R.id.videoThumbnail);
        vidTitle = findViewById(R.id.videoTitle);
        userHandleUpload = findViewById(R.id.userHandleUpload);
        uploadProgress = findViewById(R.id.uploadProgress);
        uploadProgressText = findViewById(R.id.uploadProgressText);
        options = findViewById(R.id.videoOptions);
        upload = findViewById(R.id.upload);
        hide = findViewById(R.id.hide);
        colorRel = findViewById(R.id.colorRel);
        cancel = findViewById(R.id.cancel);
        hideLayout = findViewById(R.id.hideLayout);
        goAhead = Boolean.FALSE;

        if (bitmap != null) {
            Glide.with(getContext()).load(bitmap).thumbnail((float) 0.9).into(vidPrev);
        } else {
            Glide.with(getContext()).load(thumbnail).thumbnail((float) 0.9).into(vidPrev);
        }

        colorRel.setOnClickListener(v -> {
            Random r = new Random();
            endColor = Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256));
            colorRel.setBackgroundColor(endColor);
        });
        userHandleUpload.setText(user.getDisplayName());
        upload.setOnClickListener(v -> {
            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(getContext(), "Give me a sec!", Toast.LENGTH_SHORT).show();

            } else {
                 uploadCompletedLecture();
            }
        });

        hide.setOnClickListener(v -> {
            goAhead = Boolean.TRUE;
            UploadApproval.this.dismiss();
        });

        cancel.setOnClickListener(v -> {
            if (cancel.getText().equals("Cancel")) {

                if (uploadTask != null && uploadTask.isInProgress()) {
                    uploadTask.cancel();
                    uploadTask = null;
                    Toast.makeText(getContext(), "Upload stopped.", Toast.LENGTH_SHORT).show();
                    cancel.setText("Retry");
                }
            } else if (cancel.getText().equals("Retry")) {
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(getContext(), "Give me a sec!", Toast.LENGTH_SHORT).show();

                } else {
                    uploadCompletedLecture();
                    cancel.setText("Cancel");
                }
            }
        });
        vidTitle.setText(fileName);

        options.setOnClickListener(v -> {
            Toast.makeText(getContext(), user.getUid(), Toast.LENGTH_SHORT).show();
            Toast.makeText(getContext(), user.getDisplayName(), Toast.LENGTH_SHORT).show();
        });

    }


    private void uploadCompletedLecture() {
        final DocumentReference vidCount = FirebaseFirestore.getInstance().collection("Count")
                .document("Lectures count");
        if (videoUri != null) {
            final DocumentReference lectureDbRef = FirebaseFirestore.getInstance()
                    .collection("Content")
                    .document("Lectures")
                    .collection(vidDesc.split("_-_")[1])
                    .document(nodeName);
            StorageReference lectureStoreRef = FirebaseStorage.getInstance().getReference("Lectures");
            StorageReference thumbnailStoreRef = FirebaseStorage.getInstance().getReference("Thumbnails");
            final StorageReference fileReference = lectureStoreRef.child(fileName + System.currentTimeMillis() + "." + getFileExtension(videoUri));
            StorageReference thumbReference = null;
            if (thumbnail != null) {
                thumbReference = thumbnailStoreRef.child(fileName + System.currentTimeMillis() + "." + getFileExtension(thumbnail));
            }

            uploadProgress.setVisibility(View.VISIBLE);
            uploadProgressText.setVisibility(View.VISIBLE);
            uploadProgress.bringToFront();
            upload.setText("Uploading...");
            hideLayout.setVisibility(View.VISIBLE);

            final NotificationCompat.Builder uploadProg = new NotificationCompat.Builder(getContext(), "uploadProg");
            uploadProg.setSmallIcon(R.drawable.ic_cloud_upload_black);
            uploadProg.setContentTitle(nodeName);
            uploadProg.setContentText("Stay online ...");
            uploadProg.setPriority(NotificationCompat.PRIORITY_DEFAULT);


            StorageReference finalThumbReference = thumbReference;
            uploadTask = fileReference.putFile(videoUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Task<Uri> uriTask = uploadTask.continueWithTask(task -> {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return fileReference.getDownloadUrl();
                        }).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {

                                        if (thumbnail != null) {
                                            uploadTask1 = finalThumbReference.putFile(thumbnail)
                                                    .addOnSuccessListener(taskSnapshot1 -> {
                                                        Task<Uri> uriTask1 = uploadTask1.continueWithTask(task12 -> {
                                                            if (!task12.isSuccessful()) {
                                                                throw task12.getException();
                                                            }
                                                            return finalThumbReference.getDownloadUrl();
                                                        }).addOnCompleteListener(task1 -> {
                                                            List<String> searchKeywords;
                                                            String searchableString;


                                                            searchableString = vidDesc.split("_-_")[0];
                                                            searchKeywords = Publisher.Comp.generateSearchKeyWord(searchableString);

                                                            Uri downloadLink = task.getResult();
                                                            Uri thumbnailLink = task1.getResult();


                                                            Map<String, Object> docMap = new HashMap<>();
                                                            docMap.put("docMetaData", vidDesc + "_-_" + downloadLink.toString() + "_-_" + user.getUid() + "_-_" + user.getDisplayName() + "_-_" + endColor);
                                                            docMap.put("search_keyword", searchKeywords);
                                                            docMap.put("docDownloadLink", nodeName);
                                                            docMap.put("uid", user.getUid());
                                                            docMap.put("mainField", vidDesc.split("_-_")[1]);
                                                            docMap.put("subField", vidDesc.split("_-_")[2]);
                                                            docMap.put("knowledgeBase", knw);
                                                            docMap.put("institution", institution);
                                                            docMap.put("thumbNail", thumbnailLink.toString());
                                                            docMap.put("timeStamp", System.currentTimeMillis());
                                                            docMap.put("price", 35);
                                                            docMap.put("views", 0);
                                                            docMap.put("commentsCount", 0);
                                                            docMap.put("saveCount", 0);
                                                            docMap.put("ratersCount", 0);
                                                            docMap.put("ratings", 0);
                                                            docMap.put("outsourced", outsourced.toString());
                                                            docMap.put("status", "active");

                                                            lectureDbRef.set(docMap).addOnSuccessListener(aVoid -> {
                                                                FirebaseFirestore.getInstance().collection("Users")
                                                                        .document(user.getUid())
                                                                        .update("schoolsUploaded", FieldValue.arrayUnion(vidDesc.split("_-_")[1]))
                                                                        .addOnSuccessListener(unused -> {
                                                                            Map<String, Object> map = new HashMap<>();
                                                                            map.put(vidDesc.split("_-_")[1], FieldValue.increment(1));
                                                                            vidCount.update(map).addOnSuccessListener(unused1 -> {
                                                                                videoUploader.showSnackBar("Lecture uploaded successfully!");
                                                                                onBackPressed();
                                                                            }).addOnFailureListener(e -> {
                                                                                Log.e("Failed lecCount update", e.getMessage());
                                                                            });
                                                                        })
                                                                        .addOnFailureListener(e -> {
                                                                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                        });

                                                                uploadProgressText.setText("Upload successful");
                                                                Intent intent = new Intent(getContext(), LecturesList.class);
                                                                intent.putExtra("child", vidDesc.split("_-_")[1]);
                                                                intent.putExtra("snackBar", "Successful contribution to Sch. of " +
                                                                        vidDesc.split("_-_")[1]);
                                                                getContext().startActivity(intent);
                                                            }).addOnFailureListener(e -> {
                                                                Toast.makeText(getContext(), "Something's missing...", Toast.LENGTH_SHORT).show();
                                                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                uploadProgress.setProgress(0);
                                                                uploadProgressText.setText("Something's missing...");
                                                            });
                                                            UploadApproval.this.dismiss();
                                                            SystemClock.sleep(1300);
                                                            onBackPressed();
                                                        });
                                                    });
                                        } else {
                                            List<String> searchKeywords;
                                            String searchableString;


                                            searchableString = vidDesc.split("_-_")[0];
                                            searchKeywords = Publisher.Comp.generateSearchKeyWord(searchableString);

                                            Uri downloadLink = task.getResult();

                                            Map<String, Object> docMap = new HashMap<>();
                                            docMap.put("docMetaData", vidDesc + "_-_" + downloadLink.toString() + "_-_" + user.getUid() + "_-_" + user.getDisplayName() + "_-_" + endColor);
                                            docMap.put("search_keyword", searchKeywords);
                                            docMap.put("docDownloadLink", nodeName);
                                            docMap.put("uid", user.getUid());
                                            docMap.put("mainField", vidDesc.split("_-_")[1]);
                                            docMap.put("subField", vidDesc.split("_-_")[2]);
                                            docMap.put("knowledgeBase", knw);
                                            docMap.put("institution", institution);
                                            docMap.put("thumbNail", null);
                                            docMap.put("timeStamp", System.currentTimeMillis());
                                            docMap.put("price", 35);
                                            docMap.put("views", 0);
                                            docMap.put("commentsCount", 0);
                                            docMap.put("saveCount", 0);
                                            docMap.put("ratersCount", 0);
                                            docMap.put("ratings", 0);
                                            docMap.put("outsourced", outsourced.toString());
                                            docMap.put("status", "active");

//                                        writePublishedToManifest(videoUri, fileReference.getDownloadUrl().toString());
                                            lectureDbRef.set(docMap).addOnSuccessListener(aVoid -> {
                                                final DocumentReference uploadingUser = FirebaseFirestore.getInstance().collection("Users")
                                                        .document(user.getUid());
                                                uploadingUser.update("schoolsUploaded", FieldValue.arrayUnion(vidDesc.split("_-_")[1]));
                                                Map<String, Object> map = new HashMap<>();
                                                map.put(vidDesc.split("_-_")[1], FieldValue.increment(1));
                                                vidCount.update(map).addOnSuccessListener(unused1 -> {
                                                    videoUploader.showSnackBar("Lecture uploaded successfully!");
                                                }).addOnFailureListener(e -> {
                                                    Log.e("Failed lecCount update", e.getMessage());
                                                });
                                                uploadProgressText.setText("Upload successful");
                                            }).addOnFailureListener(e -> {
                                                Toast.makeText(getContext(), "Something's missing...", Toast.LENGTH_SHORT).show();
                                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                uploadProgress.setProgress(0);
                                                uploadProgressText.setText("Something's missing...");
                                            });
                                            UploadApproval.this.dismiss();
                                            SystemClock.sleep(1300);
                                            onBackPressed();
                                        }

                                    } else {
                                        Toast.makeText(getContext(), "Select something.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show())
                    .addOnProgressListener(taskSnapshot -> {
                        uploadProgressChannel(fileName, (int) taskSnapshot.getTotalByteCount(), (int) taskSnapshot.getBytesTransferred());
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        uploadProgress.setProgress((int) progress);
                        uploadProgressText.setText(progress + "%");
                        uploadProg.setProgress((int) taskSnapshot.getTotalByteCount(), (int) taskSnapshot.getBytesTransferred(), false);
                        final NotificationManagerCompat compat = NotificationManagerCompat.from(getContext());
                        compat.notify(101, uploadProg.build());
                        //TO BE PLACED INSIDE A PROGRESS BAR LATER IN THE PROJECT
                        //PROGRESS BAR SHALL BE SET BACK TO ZERO ON SUCCESS
                    });
        } else {
            Toast.makeText(getContext(), "Select something.", Toast.LENGTH_SHORT).show();
        }

    }

    public void uploadProgressChannel(String fileName, final int totalByteCount, final int bytesTransferred) {
        final NotificationCompat.Builder notification = new NotificationCompat.Builder(getContext(), App.CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_baseline_cloud_uploaded)
                .setContentTitle(fileName)
                .setContentText("Uploading...")
                .setProgress(totalByteCount, bytesTransferred, false)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .setContentText("Done!")
                .setOnlyAlertOnce(true);
        managerCompat.notify(2, notification.build());

//        new Thread(() -> {
//            notification.setProgress(totalByteCount, bytesTransferred, false);
//            notification.setContentText("Done!").setOngoing(false)
//                    .setProgress(totalByteCount, bytesTransferred, false);
//            managerCompat.notify(2, notification.build());
//        });


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

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public void dismiss() {
        if (uploadTask == null || !uploadTask.isInProgress() || goAhead == Boolean.TRUE) {
            super.dismiss();
        }
    }


    @Override
    public void setOnCancelListener(@Nullable OnCancelListener listener) {
        if (uploadTask == null || !uploadTask.isInProgress() || goAhead == Boolean.TRUE) {
            super.setOnCancelListener(listener);
        }
    }


}