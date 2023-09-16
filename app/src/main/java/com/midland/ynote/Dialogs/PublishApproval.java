package com.midland.ynote.Dialogs;

import static java.lang.String.valueOf;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.midland.ynote.Activities.SchoolDepartmentDocuments;
import com.midland.ynote.Adapters.MotionPicturesSubCatRV;
import com.midland.ynote.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class PublishApproval extends Dialog {

    int endColor;
    ImageView coverImage, docEmblem;
    TextView docTitle, docDesc, userHandle;
    RecyclerView tagsRV;
    RelativeLayout relApproval;
    Button options, publish;
    String metaData, institution, semester;
    FirebaseUser user;
    StorageTask<UploadTask.TaskSnapshot> uploadTask;
    StorageTask<UploadTask.TaskSnapshot> uploadTask1;
    ProgressBar uploadProgress;
    Uri docUri, coverUri;
    String unitCode, docDetails;
    ArrayList<String> docTags;
    CardView metaDataApprovalCard;
    StorageReference shelfStorageRef = FirebaseStorage.getInstance().getReference("Documents");


    public PublishApproval(@NonNull Context context, String metaData, String institution, String semester,
                           Uri docUri, Uri coverUri, FirebaseUser user, String unitCode, String docDetails,
                           ArrayList<String> docTags) {
        super(context);
        this.metaData = metaData;//main field, sub field, know base, doc details, unit code, doc name, tags, size, uID, displayName, endColor
        this.institution = institution;
        this.docUri = docUri;
        this.coverUri = coverUri;
        this.semester = semester;
        this.user = user;
        this.unitCode = unitCode;
        this.docDetails = docDetails;
        this.docTags = docTags;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_publish_approval);

        coverImage = findViewById(R.id.objectCoverPhotoApproval);
        docEmblem = findViewById(R.id.docEmblem);
        docTitle = findViewById(R.id.docTitleApproval);
        options = findViewById(R.id.doc_options_approval);
        publish = findViewById(R.id.publishBtn);
        uploadProgress = findViewById(R.id.uploadProgressApprove);
        docDesc = findViewById(R.id.docDescApproval);
        userHandle = findViewById(R.id.publishedByApproval);
        tagsRV = findViewById(R.id.tagsRVApproval);
        metaDataApprovalCard = findViewById(R.id.metaDataApprovalCard);
        relApproval = findViewById(R.id.relFeedApproval);
        metaDataApprovalCard.bringToFront();


        tagsRV.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        MotionPicturesSubCatRV tagsAdapter = new MotionPicturesSubCatRV(getContext(), docTags, "");
        tagsAdapter.notifyDataSetChanged();
        tagsRV.setAdapter(tagsAdapter);

        userHandle.setText(user.getDisplayName());

        docTitle.setText(metaData.split("_-_")[5]);
        if (docUri.toString().endsWith("pdf") || docUri.toString().endsWith("PDF")) {
            Glide.with(getContext()).load(R.drawable.pdf).fitCenter().into(docEmblem);
        } else if (docUri.toString().endsWith("doc") || docUri.toString().endsWith("DOC")
                || docUri.toString().endsWith("docx") || docUri.toString().endsWith("DOCX")) {
            Glide.with(getContext()).load(R.drawable.microsoft_word).fitCenter().into(docEmblem);
        } else if (docUri.toString().endsWith("ppt") || docUri.toString().endsWith("PPT")
                || docUri.toString().endsWith("pptx") || docUri.toString().endsWith("PPTX")) {
            Glide.with(getContext()).load(R.drawable.powerpoint).fitCenter().into(docEmblem);
        }
        if (coverUri != null) {
            Glide.with(getContext()).load(coverUri).thumbnail((float) 0.9).into(coverImage);
        }

        options.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), docEmblem);
            popupMenu.getMenuInflater().inflate(R.menu.doc_approval_menu, popupMenu.getMenu());
            popupMenu.show();

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.docDetails) {
                    if (metaDataApprovalCard.getVisibility() == View.GONE) {
                        metaDataApprovalCard.setVisibility(View.VISIBLE);
                    } else if (metaDataApprovalCard.getVisibility() == View.VISIBLE) {
                        metaDataApprovalCard.setVisibility(View.GONE);
                    }
                }

                if (item.getItemId() == R.id.adjustCover) {
                    Toast.makeText(getContext(), "Adjust Cover", Toast.LENGTH_SHORT).show();
                }

                if (item.getItemId() == R.id.removeCover) {
                    Toast.makeText(getContext(), "Adjust Cover", Toast.LENGTH_SHORT).show();
                }
                return false;
            });
        });


        publish.setOnClickListener(v ->{
            uploadDoc();
        });

        relApproval.setOnClickListener(v -> {
            Random r = new Random();
            endColor = Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256));
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{Color.WHITE, endColor});
            relApproval.setBackground(gd);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.docDetails:
                if (metaDataApprovalCard.getVisibility() == View.GONE) {
                    metaDataApprovalCard.setVisibility(View.VISIBLE);
                } else if (metaDataApprovalCard.getVisibility() == View.VISIBLE) {
                    metaDataApprovalCard.setVisibility(View.GONE);
                }
                break;

            case R.id.adjustCover:

                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void uploadDoc() {
        if (uploadTask != null && uploadTask.isInProgress()) {
            Toast.makeText(getContext(), "Give me a sec!", Toast.LENGTH_SHORT).show();
        }else {
            final DocumentReference docRef = FirebaseFirestore.getInstance().collection("Field count")
                    .document(replacer(metaData.split("_-_")[0]));

            final DocumentReference uploadingUser = FirebaseFirestore.getInstance().collection("Users")
                    .document(user.getUid());

            final DocumentReference docCount = FirebaseFirestore.getInstance().collection("Count")
                    .document("Documents count");

            final DocumentReference publishedDocs = FirebaseFirestore.getInstance().collection("Content")
                    .document("Documents")
                    .collection(replacer(metaData.split("_-_")[0]))
                    .document(replacer(metaData.split("_-_")[5]));

            final StorageReference fileReference = shelfStorageRef.child(metaData.split("_-_")[5] + "." + getFileExtension(docUri));
            uploadProgress.setVisibility(View.VISIBLE);
            uploadProgress.bringToFront();

            publishedDocs.get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Toast.makeText(getContext(), "Duplicate found...", Toast.LENGTH_SHORT).show();
                        } else {
                            final Uri[] downloadLink = new Uri[1];
                            uploadTask = fileReference.putFile(docUri)
                                    .addOnSuccessListener(taskSnapshot -> {

                                        Task<Uri> uriTask = uploadTask.continueWithTask(task -> {
                                            if (!task.isSuccessful()) {
                                                throw task.getException();
                                            }
                                            return fileReference.getDownloadUrl();


                                        }).addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                if (coverUri == null) {
                                                    StringBuilder searchableDocDetail = new StringBuilder();
                                                    if (metaData.split("_-_")[5] != null) {
                                                        int size = metaData.split("_-_")[5].split(" ").length;
                                                        if (size > 9) {
                                                            for (int i = 0; i < 9; i++) {
                                                                searchableDocDetail.append(metaData.split("_-_")[5].split(" ")[i]);
                                                            }
                                                        } else {
                                                            for (int i = 0; i < size; i++) {
                                                                searchableDocDetail.append(metaData.split("_-_")[5].split(" ")[i]);
                                                            }
                                                        }
                                                    }

                                                    String searchableString = "";
                                                    if (metaData.split("_-_")[4] != null) {
                                                        searchableString = metaData.split("_-_")[4] + " " + searchableDocDetail;
                                                    } else {
                                                        searchableString = valueOf(searchableDocDetail);
                                                    }

//                                                List<String> searchKeywords = Publisher.Comp.generateSearchKeyWord(searchableString);

                                                    downloadLink[0] = task.getResult();

//                                                        final SelectedDoc selectedDoc = new SelectedDoc(metaData + "_-_"
//                                                                + user.getUid() + "_-_" + user.getDisplayName() + "_-_" + endColor,
//                                                                downloadLink[0].toString() + "_-_", user.getUid(),
//                                                                searchKeywords, metaData.split("_-_")[1], metaData.split("_-_")[2]);

                                                    Map<String, Object> docMap = new HashMap<>();
                                                    docMap.put("docMetaData", metaData + "_-_" + user.getUid() + "_-_" + user.getDisplayName() + "_-_" + endColor);
                                                    docMap.put("search_keyword", metaData.split("_-_")[5]);
                                                    docMap.put("docDownloadLink", downloadLink[0].toString());
                                                    docMap.put("coverDownloadLink", null);
                                                    docMap.put("uid", user.getUid());
                                                    docMap.put("institution", institution);
                                                    docMap.put("mainField", metaData.split("_-_")[0]);
                                                    docMap.put("subField", metaData.split("_-_")[1]);
                                                    docMap.put("knowledgeBase", metaData.split("_-_")[2]);
                                                    docMap.put("semester", semester);
                                                    docMap.put("unitCode", unitCode);
                                                    docMap.put("timeStamp", System.currentTimeMillis());
                                                    docMap.put("pastPaper", docTags.get(docTags.size() - 1));
                                                    docMap.put("docDetails", docDetails);
                                                    docMap.put("thumbNail", null);
                                                    docMap.put("commentsCount", 0);
                                                    docMap.put("saveCount", 0);
                                                    docMap.put("ratersCount", 0);
                                                    docMap.put("ratings", 0);
                                                    docMap.put("price", 23);
                                                    docMap.put("status", "active");


                                                    publishedDocs.set(docMap).addOnSuccessListener(aVoid -> {
                                                        uploadingUser.update("schoolsPublished", FieldValue.arrayUnion(replacer(metaData.split("_-_")[0])));
                                                        Map<String, Object> map = new HashMap<>();
                                                        map.put(metaData.split("_-_")[0], FieldValue.increment(1));
                                                        docCount.update(map).addOnSuccessListener(unused -> {
                                                            SystemClock.sleep(2000);
                                                            PublishApproval.this.dismiss();
                                                            Intent intent = new Intent(getContext(), SchoolDepartmentDocuments.class);
                                                            intent.putExtra("SchoolName", metaData.split("_-_")[0]);
                                                            intent.putExtra("snackBar", "Successful contribution to Sch. of " +
                                                                    metaData.split("_-_")[0]);
                                                            getContext().startActivity(intent);

                                                        }).addOnFailureListener(e -> {
                                                            Log.e("Failed docCount update", e.getMessage());
                                                        });

//                                                    uploadingUser.update("schoolsPublished", FieldValue.arrayUnion(replacer(metaData.split("_-_")[0])))
//                                                            .addOnSuccessListener(unused -> {
//                                                            })
//                                                            .addOnFailureListener(e -> {
//                                                                Toast.makeText(getContext(), "Something's missing...", Toast.LENGTH_SHORT).show();
//                                                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                                                            });
                                                    }).addOnFailureListener(e -> {
                                                        Toast.makeText(getContext(), "Something's up...", Toast.LENGTH_SHORT).show();
                                                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        SystemClock.sleep(1300);
                                                        onBackPressed();
                                                    });
                                                } else {
                                                    final StorageReference fileReferenceCover = shelfStorageRef.child(metaData.split("_-_")[5] + "_" + "Document cover");
                                                    uploadTask1 = fileReferenceCover.putFile(coverUri).addOnSuccessListener(taskSnapshot12 -> {
                                                                Task<Uri> uriTask1 = uploadTask1.continueWithTask(task12 -> {
                                                                    if (!task12.isSuccessful()) {
                                                                        throw task12.getException();
                                                                    }
                                                                    return fileReferenceCover.getDownloadUrl();


                                                                }).addOnCompleteListener(task1 -> {
                                                                    if (task1.isSuccessful()) {
//                                                String userID = FirebaseAuth.getInstance().getUid();
                                                                        Uri coverDownloadLink = task1.getResult();
//                                                            SelectedDoc selectedDoc = new SelectedDoc(metaData + "_-_"
//                                                                    + user.getUid() + "_-_" + user.getDisplayName() + "_-_" + endColor,
//                                                                    downloadLink[0].toString() + "_-_" + coverDownloadLink.toString(),
//                                                                    user.getUid());

                                                                        Map<String, Object> docMap = new HashMap<>();
                                                                        docMap.put("docMetaData", metaData + "_-_" + user.getUid() + "_-_" + user.getDisplayName() + "_-_" + endColor);
                                                                        docMap.put("search_keyword", "");
                                                                        docMap.put("docDownloadLink", downloadLink[0].toString());
                                                                        docMap.put("uid", user.getUid());
                                                                        docMap.put("institution", institution);
                                                                        docMap.put("mainField", metaData.split("_-_")[0]);
                                                                        docMap.put("subField", metaData.split("_-_")[1]);
                                                                        docMap.put("knowledgeBase", metaData.split("_-_")[2]);
                                                                        docMap.put("unitCode", unitCode);
                                                                        docMap.put("timeStamp", System.currentTimeMillis());
                                                                        docMap.put("pastPaper", docTags.get(docTags.size() - 1));
                                                                        docMap.put("docDetails", docDetails);
                                                                        docMap.put("thumbNail", coverDownloadLink.toString());
                                                                        docMap.put("commentsCount", 0);
                                                                        docMap.put("saveCount", 0);
                                                                        docMap.put("repostCount", 0);
                                                                        docMap.put("ratersCount", 0);
                                                                        docMap.put("ratings", 0);
                                                                        docMap.put("price", 23);
                                                                        docMap.put("status", "active");


                                                                        publishedDocs.set(docMap)
                                                                                .addOnSuccessListener(aVoid -> {
                                                                                    uploadingUser.update("schoolsPublished", FieldValue.arrayUnion(replacer(metaData.split("_-_")[0])));
                                                                                    Map<String, Object> map = new HashMap<>();
                                                                                    map.put(metaData.split("_-_")[0], FieldValue.increment(1));
                                                                                    docCount.update(map).addOnSuccessListener(unused -> {

                                                                                        Toast.makeText(getContext(), "Upload successful", Toast.LENGTH_SHORT).show();
                                                                                        SystemClock.sleep(2500);
                                                                                        PublishApproval.this.dismiss();
//                                                                    Intent intent = new Intent(getContext(), SchoolDepartmentDocuments.class);
//                                                                    intent.putExtra("SchoolName", metaData.split("_-_")[0]);
//                                                                    getContext().startActivity(intent);
                                                                                        onBackPressed();
                                                                                    }).addOnFailureListener(e -> {
                                                                                        Log.e("Failed docCount update", e.getMessage());
                                                                                    });

//                                                    uploadingUser.update("schoolsPublished", FieldValue.arrayUnion(replacer(metaData.split("_-_")[0])))
//                                                            .addOnSuccessListener(unused -> {
//                                                            })
//                                                            .addOnFailureListener(e -> {
//                                                                Toast.makeText(getContext(), "Something's missing...", Toast.LENGTH_SHORT).show();
//                                                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                                                            });
                                                                                }).addOnFailureListener(e -> {
                                                                                    Toast.makeText(getContext(), "Something's up...", Toast.LENGTH_SHORT).show();
                                                                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                    SystemClock.sleep(1300);
                                                                                    onBackPressed();
                                                                                });


                                                                    } else {
                                                                        Toast.makeText(getContext(), "Select something.", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                                Toast.makeText(getContext(), "Document & cover published", Toast.LENGTH_SHORT).show();
                                                                uploadProgress.setProgress(0);
//                            final String uploadID = shelfDatabaseRef.push().getKey(); //CREATES NEW UNIQUE ID


                                                            })
                                                            .addOnFailureListener(e -> {
                                                                Log.e("DocUploadError", e.getMessage());
                                                                Toast.makeText(getContext(), "Something's preventing your upload.", Toast.LENGTH_SHORT).show();
                                                            })
                                                            .addOnProgressListener(taskSnapshot1 -> {
                                                                double progress = (100.0 * taskSnapshot1.getBytesTransferred() / taskSnapshot1.getTotalByteCount());
                                                                uploadProgress.setProgress((int) progress);
                                                                //TO BE PLACED INSIDE A PROGRESS BAR LATER IN THE PROJECT
                                                                //PROGRESS BAR SHALL BE SET BACK TO ZERO ON SUCCESS
                                                            })
                                                            .addOnCompleteListener(task13 -> uploadProgress.setVisibility(View.INVISIBLE));
                                                }

                                            } else {
                                                Toast.makeText(getContext(), "Select something.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
//                            final String uploadID = shelfDatabaseRef.push().getKey(); //CREATES NEW UNIQUE ID

                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("DocUploadError", e.getMessage());
                                        Toast.makeText(getContext(), "Something's preventing your upload.", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnProgressListener(taskSnapshot -> {
                                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                        uploadProgress.setProgress((int) progress);
                                        //TO BE PLACED INSIDE A PROGRESS BAR LATER IN THE PROJECT
                                        //PROGRESS BAR SHALL BE SET BACK TO ZERO ON SUCCESS
                                    }).addOnCompleteListener(task -> uploadProgress.setVisibility(View.INVISIBLE));

                        }
                    })
                    .addOnFailureListener(e -> {

                    });

        }

    }

    private String replacer(String docName) {
        String docName1 = docName.replace("]", "");
        String docName2 = docName1.replace("[", "");
        String docName3 = docName2.replace(".", "");
        String docName4 = docName3.replace("$", "");
        String docName5 = docName4.replace("*", "");
        return docName5.replace("#", "");
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public void dismiss() {
        if (uploadTask == null || !uploadTask.isInProgress()) {
            super.dismiss();
        }
    }


    @Override
    public void setOnCancelListener(@Nullable OnCancelListener listener) {
        if (uploadTask == null || !uploadTask.isInProgress()) {
            super.setOnCancelListener(listener);
        }
    }
}