package com.midland.ynote.Activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.midland.ynote.R;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.midland.ynote.Adapters.AllVidRecAdapter;
import com.midland.ynote.Adapters.CloudVideosAdapter;
import com.midland.ynote.Adapters.DocumentAdapter;
import com.midland.ynote.Adapters.GalleryAdt;
import com.midland.ynote.Adapters.GridRecyclerAdapter;
import com.midland.ynote.Adapters.PointsAdapter;
import com.midland.ynote.Fragments.Documents;
import com.midland.ynote.Objects.PendingLecObj;
import com.midland.ynote.Objects.PersonalGallery;
import com.midland.ynote.Objects.SelectedDoc;
import com.midland.ynote.Objects.SourceDocObject;
import com.midland.ynote.Utilities.FilingSystem;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileItemsList extends AppCompatActivity {

    RecyclerView shelfRV, publishedDocRV, publishedLecRV, completedLecRV, schoolsRV;
    RecyclerView pendingLecGrid;
    AllVidRecAdapter allVidAdapter;
    FloatingActionButton addToGallery;
    ProgressBar profileProgress;
    Button update, discard;
    EditText saySomething;
    ImageView selectedIm;
    CardView selectCard;
    ProgressBar uploadProg;
    private RatingBar docRatingBar;
    private ImageButton closeViewer, docComments, rateLecture;
    private RelativeLayout viewerRel;
    private Application app;
    AnimatorSet animatorSet;
    ArrayList<SelectedDoc> documents;
    StorageTask<UploadTask.TaskSnapshot> uploadTask;
    StorageReference galleryRef = FirebaseStorage.getInstance().getReference("PersonalGallery");
    CollectionReference users = FirebaseFirestore.getInstance().collection("Users");
    CropImage.ActivityResult result = null;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_items_list);
        Toolbar toolbar = findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);


        final ArrayList<String> newSchools = new ArrayList<>();

        newSchools.add("Please wait..");
        newSchools.add("Please wait..");
        String userID = "";
        if (getIntent().getStringExtra("userID") != null) {
            userID = getIntent().getStringExtra("userID");
        }
        PointsAdapter pointsAdapter = new PointsAdapter(ProfileItemsList.this, newSchools,
                profileProgress, publishedDocRV, userID, "Documents", null);
        pointsAdapter.notifyDataSetChanged();


        update = findViewById(R.id.updateBtn);
        discard = findViewById(R.id.discardBtn);
        uploadProg = findViewById(R.id.uploadProg);
        selectedIm = findViewById(R.id.selectedImage);
        saySomething = findViewById(R.id.saySomething);
        selectCard = findViewById(R.id.selectCard);
        profileProgress = findViewById(R.id.profileProgress);
        profileProgress.bringToFront();

        schoolsRV = findViewById(R.id.schoolsRV);
        addToGallery = findViewById(R.id.addToGallery);

        addToGallery.setOnClickListener(view -> {
            checkAndroidVersion();
        });
        RelativeLayout glowRel = findViewById(R.id.glowRel);
        animatorSet = new AnimatorSet();
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(glowRel, "alpha", 0.5f, 0.1f);
        fadeOut.setDuration(500);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(glowRel, "alpha", 0.1f, 0.5f);
        fadeIn.setDuration(500);

        animatorSet.play(fadeIn).after(fadeOut);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSet.start();
                super.onAnimationEnd(animation);
            }
        });

        animatorSet.start();
        schoolsRV.setAdapter(pointsAdapter);
        schoolsRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        switch (getIntent().getStringExtra("button")) {
            case "galleryBtn":
                addToGallery.setVisibility(View.VISIBLE);
                if (user != null) {
                    users.document(user.getUid()).collection("Personal Gallery").get().addOnSuccessListener(queryDocumentSnapshots -> {
                        ArrayList<PersonalGallery> galleryItems = new ArrayList<>();
                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                            String upVotes = String.valueOf(qds.get("upVotes"));
                            String downVotes = String.valueOf(qds.get("upVotes"));
                            String commentCount = String.valueOf(qds.get("commentsCount"));
                            String link = String.valueOf(qds.get("photo"));
                            ArrayList<String> uiDs = (ArrayList<String>) qds.get("uiDs");
                            Boolean comments = (Boolean) qds.get("comments");
                            DocumentReference ref = qds.getReference();

                            PersonalGallery galleryItem = new PersonalGallery(upVotes, downVotes, commentCount, link, comments, ref, uiDs);
                            galleryItems.add(galleryItem);
                        }

                        glowRel.setVisibility(View.GONE);
                        GalleryAdt galleryRV = new GalleryAdt(getApplicationContext(), galleryItems);
                        galleryRV.notifyDataSetChanged();

                        shelfRV = findViewById(R.id.shelfRV);
                        shelfRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
                        shelfRV.setVisibility(View.VISIBLE);
                        shelfRV.setAdapter(galleryRV);

                    }).addOnFailureListener(e -> {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }

                break;

            case "galleryBtnUser":
                addToGallery.setVisibility(View.GONE);
                users.document(userID).collection("Personal Gallery").get().addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<PersonalGallery> galleryItems = new ArrayList<>();
                    for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                        String upVotes = String.valueOf(qds.get("upVotes"));
                        String downVotes = String.valueOf(qds.get("upVotes"));
                        String commentCount = String.valueOf(qds.get("commentsCount"));
                        String link = String.valueOf(qds.get("photo"));
                        ArrayList<String> uiDs = (ArrayList<String>) qds.get("uiDs");
                        Boolean comments = (Boolean) qds.get("comments");
                        DocumentReference ref = qds.getReference();

                        PersonalGallery galleryItem = new PersonalGallery(upVotes, downVotes, commentCount, link, comments, ref, uiDs);
                        galleryItems.add(galleryItem);
                    }

                    glowRel.setVisibility(View.GONE);
                    GalleryAdt galleryRV = new GalleryAdt(getApplicationContext(), galleryItems);
                    galleryRV.notifyDataSetChanged();

                    shelfRV = findViewById(R.id.shelfRV);
                    shelfRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
                    shelfRV.setVisibility(View.VISIBLE);
                    shelfRV.setAdapter(galleryRV);

                }).addOnFailureListener(e -> {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                });

                break;

            case "shelfBtn":
                //pending
                shelfRV = findViewById(R.id.shelfRV);
//                shelfRV.setLayoutManager(new LinearLayoutManager(ProfileItemsList.this, RecyclerView.VERTICAL, false));
                shelfRV.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                shelfRV.setVisibility(View.VISIBLE);

                if (user != null) {
                    documents = new ArrayList<>();
                    DocumentReference readList = FirebaseFirestore.getInstance().collection("Users")
                            .document(user.getUid())
                            .collection("ScheduledLists")
                            .document("ReadList");

                    readList.get().addOnSuccessListener(documentSnapshot -> {
                        ArrayList<String> readListRefs = (ArrayList<String>) documentSnapshot.get("readList");

                        if (readListRefs != null) {
                            for (String s : readListRefs) {
                                FirebaseFirestore.getInstance().collection("Content")
                                        .document("Documents")
                                        .collection(s.split("_-_")[0])
                                        .document(s.split("_-_")[1])
                                        .get()
                                        .addOnSuccessListener(documentSnapshot1 -> {
                                            glowRel.setVisibility(View.GONE);
                                            SelectedDoc document = documentSnapshot1.toObject(SelectedDoc.class);
                                            documents.add(document);
                                            if (document != null) {
                                                DocumentAdapter documentAdapter = new DocumentAdapter(null, getApplicationContext(),
                                                        ProfileItemsList.this, documents);
                                                documentAdapter.notifyDataSetChanged();
                                                shelfRV.setAdapter(documentAdapter);
                                                Toast.makeText(getApplicationContext(), document.getDocMetaData(), Toast.LENGTH_SHORT).show();
                                                Toast.makeText(getApplicationContext(), document.getKnowledgeBase(), Toast.LENGTH_SHORT).show();
                                            }else {
                                                Toast.makeText(getApplicationContext(), "Couldn't find none", Toast.LENGTH_SHORT).show();
                                            }

                                        })
                                        .addOnSuccessListener(queryDocumentSnapshots -> {

                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                                        });
                            }

                        }
                    });

                }
                break;

            case "shelfBtnUser":
                //pending
                shelfRV = findViewById(R.id.shelfRV);
//                shelfRV.setLayoutManager(new LinearLayoutManager(ProfileItemsList.this, RecyclerView.VERTICAL, false));
                shelfRV.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                shelfRV.setVisibility(View.VISIBLE);
                documents = new ArrayList<>();
                DocumentReference readList = FirebaseFirestore.getInstance()
                        .collection("Users")
                        .document(userID)
                        .collection("ScheduledLists")
                        .document("ReadList");

                readList.get().addOnSuccessListener(documentSnapshot -> {
                    ArrayList<String> readListRefs = (ArrayList<String>) documentSnapshot.get("readList");

                    if (readListRefs != null) {
                        for (String s : readListRefs) {
                            FirebaseFirestore.getInstance().collection("Content")
                                    .document("Documents")
                                    .collection(s.split("_-_")[0])
                                    .document(s.split("_-_")[1])
                                    .get()
                                    .addOnSuccessListener(documentSnapshot1 -> {
                                        glowRel.setVisibility(View.GONE);
                                        SelectedDoc document = documentSnapshot1.toObject(SelectedDoc.class);
                                        documents.add(document);
                                        DocumentAdapter documentAdapter = new DocumentAdapter(null, getApplicationContext(),
                                                ProfileItemsList.this, documents);
                                        documentAdapter.notifyDataSetChanged();
                                        shelfRV.setAdapter(documentAdapter);
                                    })
                                    .addOnSuccessListener(queryDocumentSnapshots -> {

                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                                    });
                        }
                        DocumentAdapter documentAdapter = new DocumentAdapter(null, getApplicationContext(),
                                ProfileItemsList.this, documents);
                        documentAdapter.notifyDataSetChanged();
                        shelfRV.setAdapter(documentAdapter);
                    }

                });


                break;

                case "watchLater":
                //pending
                shelfRV = findViewById(R.id.shelfRV);
//                shelfRV.setLayoutManager(new LinearLayoutManager(ProfileItemsList.this, RecyclerView.VERTICAL, false));
                shelfRV.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                shelfRV.setVisibility(View.VISIBLE);

                if (user != null) {
                    documents = new ArrayList<>();
                    DocumentReference watchList = FirebaseFirestore.getInstance().collection("Users")
                            .document(user.getUid())
                            .collection("ScheduledLists")
                            .document("ReadList");

                    watchList.get().addOnSuccessListener(documentSnapshot -> {
                        ArrayList<String> readListRefs = (ArrayList<String>) documentSnapshot.get("watchList");

                        if (readListRefs != null) {
                            for (String s : readListRefs) {
                                FirebaseFirestore.getInstance().collection("Content")
                                        .document("Lectures")
                                        .collection(s.split("_-_")[0])
                                        .document(s.split("_-_")[1])
                                        .get()
                                        .addOnSuccessListener(queryDocumentSnapshots -> {
                                            glowRel.setVisibility(View.GONE);
                                            SelectedDoc document = queryDocumentSnapshots.toObject(SelectedDoc.class);
                                            documents.add(document);
                                        });
                            }
                            CloudVideosAdapter docAdt = new CloudVideosAdapter(null, null, null, getApplicationContext(), getApplicationContext(),
                                    documents, null, null);
                            docAdt.notifyDataSetChanged();

                            shelfRV.setAdapter(docAdt);
                        }
                    });

                }
                break;

            case "watchLaterUser":
                //pending
                shelfRV = findViewById(R.id.shelfRV);
//                shelfRV.setLayoutManager(new LinearLayoutManager(ProfileItemsList.this, RecyclerView.VERTICAL, false));
                shelfRV.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                shelfRV.setVisibility(View.VISIBLE);
                documents = new ArrayList<>();
                DocumentReference watchList = FirebaseFirestore.getInstance()
                        .collection("Users")
                        .document(userID)
                        .collection("ScheduledLists")
                        .document("ReadList");

                watchList.get().addOnSuccessListener(documentSnapshot -> {
                    ArrayList<String> readListRefs = (ArrayList<String>) documentSnapshot.get("watchList");

                    if (readListRefs != null) {
                        for (String s : readListRefs) {
                            FirebaseFirestore.getInstance().collection("Content")
                                    .document("Lectures")
                                    .collection(s.split("_-_")[0])
                                    .document(s.split("_-_")[1])
                                    .get()
                                    .addOnSuccessListener(queryDocumentSnapshots -> {
                                        glowRel.setVisibility(View.GONE);
                                        SelectedDoc document = queryDocumentSnapshots.toObject(SelectedDoc.class);
                                        documents.add(document);
                                    });
                        }
                        CloudVideosAdapter docAdt = new CloudVideosAdapter(null, null, null, getApplicationContext(), getApplicationContext(),
                                documents, null, null);
                        docAdt.notifyDataSetChanged();

                        shelfRV.setAdapter(docAdt);
                    }

                });


                break;

            case "pendingLecBtn":
                //done kinda
                pendingLecGrid = findViewById(R.id.pendingLecGrid);
                pendingLecGrid.setLayoutManager(new GridLayoutManager(ProfileItemsList.this, 2));

                if (pendingLecturesObj() == null) {
                    Snackbar.make(findViewById(R.id.layoutCoord), "No lectures yet!", Snackbar.LENGTH_SHORT).show();
                } else {
                    GridRecyclerAdapter grid = new GridRecyclerAdapter(ProfileItemsList.this, null,
                            pendingLecturesObj(), null, "ProfileItemsList", getIntent().getStringExtra("fileName"));
                    grid.notifyDataSetChanged();
                    pendingLecGrid.setAdapter(grid);
                    pendingLecGrid.setVisibility(View.VISIBLE);
                    glowRel.setVisibility(View.GONE);
                }
                break;

            case "publishLecBtn":
                //done
                profileProgress.setVisibility(View.VISIBLE);
                publishedLecRV = findViewById(R.id.publishedLecRV);
                PlayerView playerView = findViewById(R.id.streamExoPlayerActivity);
                RatingBar lecRatingBar = findViewById(R.id.ratingBarActivity);
                View bottomSheet = findViewById(R.id.vid_comments_sheet);
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                RecyclerView docCommentRV = findViewById(R.id.vidCommentsRV);
                Button addComment = findViewById(R.id.vidAddCommentBtn);
                EditText docCommentET = findViewById(R.id.vidCommentET);

                publishedLecRV.setLayoutManager(new LinearLayoutManager(ProfileItemsList.this, RecyclerView.VERTICAL, false));
                final ArrayList<SelectedDoc> selectedVideos = new ArrayList<>();


                String finalUserID = userID;
                FirebaseFirestore.getInstance().collection("Users")
                        .document(userID).collection("Schools")
                        .document("Lectures").get().addOnSuccessListener(documentSnapshot -> {
                    String[] schools = (String[]) documentSnapshot.get("schools");
                    ArrayList<String> newSchools1 = new ArrayList<>();
                    if (schools != null) {
                        Collections.addAll(newSchools1, schools);
                        PointsAdapter pointsAdapter12 = new PointsAdapter(ProfileItemsList.this,
                                newSchools1, profileProgress, publishedLecRV, finalUserID, "Lectures", null);
                        pointsAdapter12.notifyDataSetChanged();
                        schoolsRV.setAdapter(pointsAdapter12);
                        schoolsRV.setVisibility(View.VISIBLE);
                    }

                });

//                publishedLectures.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
//                            SelectedVideo selectedVideo = qds.toObject(SelectedVideo.class);
//                            selectedVideos.add(selectedVideo);
//                        }
//                        cloudVideosAdapter.notifyDataSetChanged();
//                        profileProgress.setVisibility(View.INVISIBLE);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(app, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//                publishedLecRV.setAdapter(cloudVideosAdapter);
                publishedLecRV.setVisibility(View.VISIBLE);
                glowRel.setVisibility(View.GONE);

                break;

            case "publishDocBtn":
                //done :- learn about shared pref to save on cost
                publishedDocRV = findViewById(R.id.publishedDocsRV);
                schoolsRV.setVisibility(View.VISIBLE);
//                FirebaseFirestore.getInstance().collection("Users")
//                        .document(userID).collection("SchoolsPublished")
//                        .document("Documents").get().addOnSuccessListener(documentSnapshot -> {
//                            profileProgress.setVisibility(View.GONE);
//                            if (documentSnapshot.exists()) {
//                                List<String> schools = (List<String>) documentSnapshot.get("schools");
//                                if (schools != null) {
//                                    newSchools.clear();
//                                    newSchools.addAll(schools);
//                                    PointsAdapter pointsAdapter1 = new PointsAdapter(ProfileItemsList.this, newSchools, profileProgress, publishedDocRV, userID, "Documents");
//                                    pointsAdapter1.notifyDataSetChanged();
//                                    schoolsRV.setAdapter(pointsAdapter1);
//                                }
//                            } else {
//                                newSchools.clear();
//                                newSchools.add("Nothing...");
//                                newSchools.add("To");
//                                newSchools.add("Show...");
//                                PointsAdapter pointsAdapter1 = new PointsAdapter(ProfileItemsList.this, newSchools, profileProgress, publishedDocRV, userID, "Documents");
//                                pointsAdapter1.notifyDataSetChanged();
//                                schoolsRV.setAdapter(pointsAdapter1);
//                                Snackbar.make(findViewById(R.id.layoutCoord), "You're yet to publish documents.", BaseTransientBottomBar.LENGTH_INDEFINITE)
//                                        .show();
//                            }
//
//                        });


                profileProgress.setVisibility(View.VISIBLE);
                publishedDocRV.setLayoutManager(new GridLayoutManager(ProfileItemsList.this, 2));
                final ArrayList<SelectedDoc> selectedDocs = new ArrayList<>();
                DocumentReference documents = FirebaseFirestore.getInstance().collection("Content")
                        .document("Documents");
                for (String s : newSchools) {
                    documents.collection(s)
                            .whereEqualTo("uid", userID)
                            .get()
                            .addOnCompleteListener(task -> {
                                profileProgress.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    selectedDocs.addAll(task.getResult().toObjects(SelectedDoc.class));
                                    DocumentAdapter docAdt = new DocumentAdapter(null, getApplicationContext(), getApplicationContext(), selectedDocs);
                                    docAdt.notifyDataSetChanged();
                                    publishedDocRV.setAdapter(docAdt);
                                } else {
                                    Snackbar.make(findViewById(R.id.rootLayout), "Something's up! This is our fault.", Snackbar.LENGTH_SHORT).show();
                                }
                            });
                }


//                publishedDocs.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
//                            SelectedDoc selectedDoc = qds.toObject(SelectedDoc.class);
//                            selectedDocs.add(selectedDoc);
//                        }
//                        documentAdapter.notifyDataSetChanged();
//                        profileProgress.setVisibility(View.INVISIBLE);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(app, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//                publishedDocRV.setAdapter(documentAdapter);
                publishedDocRV.setVisibility(View.VISIBLE);
                glowRel.setVisibility(View.GONE);

                break;

            case "completedLecBtn": {
                //done
                completedLecRV = findViewById(R.id.completedLecRV);
                completedLecRV.setLayoutManager(new LinearLayoutManager(ProfileItemsList.this, RecyclerView.VERTICAL, false));
                allVidAdapter = new AllVidRecAdapter(ProfileItemsList.this,
                        getVideoFileObjects(), "offline", null);
                allVidAdapter.notifyDataSetChanged();
                completedLecRV.setAdapter(allVidAdapter);
                completedLecRV.setVisibility(View.VISIBLE);
                glowRel.setVisibility(View.GONE);

                break;
            }

        }


        update.setOnClickListener(view -> {

            if (user != null) {
                if (result != null) {
                    if (saySomething.getText().toString().trim().equals("")) {
                        Toast.makeText(getApplicationContext(), "Say something..", Toast.LENGTH_SHORT).show();
                    } else {
                        uploadProg.setVisibility(View.VISIBLE);
                        StorageReference finalRef = galleryRef.child(user.getUid()).child(String.valueOf(System.currentTimeMillis()));
                        uploadTask = finalRef.putFile(result.getUri())
                                .addOnSuccessListener(taskSnapshot -> {
                                    Task<Uri> uriTask = uploadTask
                                            .continueWithTask(task -> {
                                                if (!task.isSuccessful()) {
                                                    throw task.getException();
                                                }
                                                return finalRef.getDownloadUrl();
                                            })
                                            .addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    Uri downloadLink = task.getResult();
                                                    Map<String, Object> personalPic = new HashMap<>();
                                                    personalPic.put("photo", downloadLink);
                                                    personalPic.put("upVotes", 0);
                                                    personalPic.put("downVotes", 0);
                                                    personalPic.put("commentsCount", 0);
                                                    personalPic.put("comments", true);

                                                    users.document(user.getUid())
                                                            .collection("Personal Gallery")
                                                            .document().set(personalPic)
                                                            .addOnSuccessListener(unused -> {
                                                                Toast.makeText(getApplicationContext(), "Gallery updated!", Toast.LENGTH_SHORT).show();
                                                                selectCard.setVisibility(View.GONE);
                                                            })
                                                            .addOnFailureListener(e -> {
                                                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            });
                                                }

                                            });
                                })
                                .addOnFailureListener(e -> {

                                });
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Something's not right..", Toast.LENGTH_SHORT).show();
                }

            }
        });
        discard.setOnClickListener(view -> {
            selectCard.setVisibility(View.GONE);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                    Uri profileUri = data.getData();
                    cropRequest(profileUri);
                }

                break;

            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    assert result != null;
                    Toast.makeText(getApplicationContext(), "addFlag" + result.getUri(), Toast.LENGTH_SHORT).show();
                    Glide.with(getApplicationContext()).load(result.getUri())
                            .thumbnail((float) 0.9)
                            .placeholder(R.drawable.ic_hourglass_bottom_white)
                            .into(selectedIm);

                    selectCard.setVisibility(View.VISIBLE);

                }
                break;

        }
    }

    private void checkAndroidVersion() {
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getParent(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        && ActivityCompat.shouldShowRequestPermissionRationale(getParent(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Snackbar.make(findViewById(R.id.homeRel), "Permission", Snackbar.LENGTH_INDEFINITE)
                            .setAction("ENABLE", v -> ActivityCompat.requestPermissions(getParent(), new String[]{
                                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                            }, 8)).show();
                } else {
                    ActivityCompat.requestPermissions(getParent(), new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, 8);
                }
            } else {
                pickImage();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    //GETTING ALL VIDEOS FROM EXTERNAL LOCALE
    public ArrayList<SourceDocObject> getVideoFileObjects() {
        ArrayList<SourceDocObject> sourceDocObjects = new ArrayList<>();
        if (checkAndRequestPermission()) {
            File[] list = FilingSystem.Companion.getInsidePendingLec().listFiles();
            if (list != null) {
                for (File file : list) {
                    int fileSize = Integer.parseInt(String.valueOf(file.length() / 1024));
                    if (fileSize != 0) {
                        String newName = file.getName().replace("dubLecture", "mp4");
                        SourceDocObject sourceDocObject = new SourceDocObject(newName, Uri.fromFile(file), String.valueOf(fileSize), String.valueOf(file.lastModified()), Uri.fromFile(file), file.lastModified());
                        sourceDocObjects.add(sourceDocObject);
                    }
                }
            }
        }

        return sourceDocObjects;
    }

    private ArrayList<PendingLecObj> pendingLecturesObj() {

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("pendingLectures", Context.MODE_PRIVATE);
        String json = preferences.getString("pendingLectures", "");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<PendingLecObj>>() {
        }.getType();

//        ArrayList<PendingLecObj> pendingLectures = new ArrayList<>();
//        if (checkAndRequestPermission()) {
//            File[] fileList = FilingSystem.Companion.getPendingLectures().listFiles();
//            assert fileList != null;
//            for (File file : fileList) {
//                String[] lecDetails = readFile(file);
//                try {
//                    PendingLecObj pendingLecObj = new PendingLecObj(lecDetails[0], lecDetails[1], lecDetails[2],
//                            lecDetails[3], file.lastModified());
//                    pendingLectures.add(pendingLecObj);
//                } catch (IndexOutOfBoundsException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(),
//                            file.getName() + " appears to be having issues, Delete", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }

        return gson.fromJson(json, type);
    }


    public String[] readFile(File file) {
        String[] textArray = new String[0];
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                textArray = line.split("_-_");
            }
            br.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(ProfileItemsList.this, "File not found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textArray;
    }

    private boolean checkAndRequestPermission() {
        int readPermission = ContextCompat.checkSelfPermission(ProfileItemsList.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePermission = ContextCompat.checkSelfPermission(ProfileItemsList.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> neededPermission = new ArrayList<>();
        if (readPermission != PackageManager.PERMISSION_GRANTED) {
            neededPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            neededPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!neededPermission.isEmpty()) {
            ActivityCompat.requestPermissions(getParent(), neededPermission.toArray(new String[neededPermission.size()]), Documents.REQUEST_MULTI_PERMISSION_ID);
            return false;
        }
        return true;
    }


    public ArrayList<String> readShelfManifest() {
        File file = FilingSystem.Companion.getShelfManifest();
        String[] textArray = new String[0];
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                textArray = line.split("_-_");
            }
            br.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(ProfileItemsList.this, "File not found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String> textArrayList = new ArrayList<>();
        Collections.addAll(textArrayList, textArray);
        return textArrayList;
    }

}