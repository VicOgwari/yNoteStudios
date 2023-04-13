package com.midland.ynote.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.midland.ynote.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.midland.ynote.Adapters.TagsAdapter;
import com.midland.ynote.Objects.SelectedDoc;
import com.midland.ynote.Objects.SelectedVideo;
import com.midland.ynote.Utilities.DocSorting;
import com.midland.ynote.Utilities.FilingSystem;

import java.util.ArrayList;
import java.util.Arrays;

public class DocUploaderPopUp extends Dialog implements AdapterView.OnItemSelectedListener {

    private Context c;
    private Activity a;
    private Uri docUri;
    private SelectedDoc selectedDoc;
    private String selectedDocName, docSize, docMetaData, school, returnClass, selectedCover;
    private SelectedVideo incomplete;
    private Spinner mainFieldSpinner, subFiledSpinner, knowledgeBaseSpinner;
    private EditText docDetailsEditText, unitCodeEditText;
    private TextView docTitle;
    private FragmentManager fm;
    private TagsAdapter tagsAdapter;
    private int selectedMainFieldPos;
    private StorageReference shelfStorageRef;
    private DatabaseReference shelfDatabaseRef;
    StorageTask<UploadTask.TaskSnapshot> uploadTask;
    ProgressBar uploadProgress;


    public DocUploaderPopUp(@NonNull Context context, Context c, Activity a, Uri docUri, String docSize,
                            String selectedDocName , String school, FragmentManager fm, String returnClass, SelectedVideo incomplete, String selectedCover) {
        super(context);
        this.c = c;
        this.a = a;
        this.docUri = docUri;
        this.selectedDocName = selectedDocName;
        this.docSize = docSize;
        this.school = school;
        this.fm = fm;
        this.returnClass = returnClass;
        this.incomplete = incomplete;
        this.selectedCover = selectedCover;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_doc_uploader_pop_up);
        initViewSnActions();
    }




    private void initViewSnActions() {
        final PDFView docImage = findViewById(R.id.picBitmap);
        docImage.fromUri(Uri.parse(docUri.toString()))
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

        docTitle = findViewById(R.id.docTitle);
        docTitle.setText(selectedDocName);
        docDetailsEditText = findViewById(R.id.docDetails);
        unitCodeEditText = findViewById(R.id.unitCodeEditText);
        mainFieldSpinner = findViewById(R.id.mainFieldSpinner);
        subFiledSpinner = findViewById(R.id.subFieldSpinner);
        RecyclerView relevanceRV = findViewById(R.id.relevanceRV);
        Button publishDoc = findViewById(R.id.publishDoc);
        Button relevance = findViewById(R.id.relevance);
        Button coverPhotoBtn = findViewById(R.id.coverPhotoBtn);
        ImageView coverPhoto = findViewById(R.id.coverPhoto);
        TextView optionalCover = findViewById(R.id.optionalCoverPhoto);
        uploadProgress = findViewById(R.id.uploadProgress1);


        ArrayAdapter<CharSequence> mainFieldAdapter = ArrayAdapter.createFromResource(c, R.array.main_fields, R.layout.spinner_drop_down_yangu);
        mainFieldAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mainFieldSpinner.setAdapter(mainFieldAdapter);
        mainFieldSpinner.setOnItemSelectedListener(this);
        subFiledSpinner.setOnItemSelectedListener(this);


        //COMMENTED OUT SINCE ON ITEM SELECTED LISTENER ALL READY SELECTS A SUB CATEGORY ARRAY LIST
//        subFiledSpinner.setAdapter(new ArrayAdapter<>(c.getApplicatiKonContext(), R.layout.support_simple_spinner_dropdown_item, DocSorting.getSubFields(0)));

        ArrayAdapter<String> knowledgeBaseAdapter = new ArrayAdapter<>(c.getApplicationContext(), R.layout.dropdown_drop_down, DocSorting.knowledgeBase);
        knowledgeBaseSpinner.setAdapter(knowledgeBaseAdapter);

        mainFieldSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> subFieldAdapter = new ArrayAdapter<>(c.getApplicationContext(), R.layout.dropdown_drop_down, new ArrayList<>(Arrays.asList(DocSorting.getSubFields(position))));
                subFiledSpinner.setAdapter(subFieldAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        subFiledSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}
        });

        relevanceRV.setLayoutManager(new LinearLayoutManager(c.getApplicationContext(), RecyclerView.HORIZONTAL, false));
        if (FilingSystem.Companion.getAllTags().size() > 0){
            tagsAdapter = new TagsAdapter(c, FilingSystem.Companion.getAllTags());
            relevanceRV.setAdapter(tagsAdapter);
            tagsAdapter.notifyDataSetChanged();
        }
        docImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), CustomGallery.class);
//                intent.putExtra("IntentSelector", "DocUploader");
//                getContext().startActivity(intent);
                Toast.makeText(getContext(), "Learn how to pass objects via intents", Toast.LENGTH_SHORT).show();
            }
        });

        relevance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = fm.beginTransaction();
                Fragment prev = fm.findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                DialogFragment dialogFragment = new LectureSubCategoryDialog(DocUploaderPopUp.this);
                dialogFragment.show(fm, "dialog");
            }
        });

        coverPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        publishDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null){
                    uploadDoc();
                }else {
                    LogInSignUp logInSignUp = new LogInSignUp(c);
                    logInSignUp.show();
                }
            }
        });


    }

    private void uploadDoc() {
        if (uploadTask != null && uploadTask.isInProgress()) {
            Toast.makeText(c.getApplicationContext(), "Give me a sec!", Toast.LENGTH_SHORT).show();
        }

        if (FilingSystem.Companion.getAllTags().isEmpty()){
            Toast.makeText(c.getApplicationContext(), "Please add at least 5 relevance tags", Toast.LENGTH_SHORT).show();
        }else {

            if (mainFieldSpinner.getSelectedItem().toString().isEmpty()){
                Toast.makeText(c.getApplicationContext(), "Please select a school", Toast.LENGTH_SHORT).show();
            }else {
                if (docUri != null) {
                    shelfStorageRef = FirebaseStorage.getInstance().getReference("Documents");
                    shelfDatabaseRef = FirebaseDatabase.getInstance().getReference("Documents");
                    final StorageReference fileReference = shelfStorageRef.child(selectedDocName + "." + getFileExtension(docUri));
                    uploadProgress.setVisibility(View.VISIBLE);
                    uploadProgress.bringToFront();


                    //CREATES NEW UNIQUE ID
                    //ACCEPTS lecture METADATA INTO THE KEY
                    //HASH MAP SHALL BE USED TO ADD CREATOR'S NAME LATER
                    //TO BE PLACED INSIDE A PROGRESS BAR LATER IN THE PROJECT
                    //PROGRESS BAR SHALL BE SET BACK TO ZERO ON SUCCESS
                    uploadTask = fileReference.putFile(docUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                        @Override
                                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                            if (!task.isSuccessful()){
                                                throw task.getException();
                                            }
                                            return fileReference.getDownloadUrl();
                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()){
//                                                String userID = FirebaseAuth.getInstance().getUid();
                                                Uri downloadLink = task.getResult();
                                                if (subFiledSpinner.getSelectedItem() == null){
                                                    docMetaData = mainFieldSpinner.getSelectedItem().toString() + "_-_"
                                                            + "null" + "_-_" + knowledgeBaseSpinner.getSelectedItem().toString()
                                                            + "_-_" + docDetailsEditText.getText().toString().trim()
                                                            + "_-_" + unitCodeEditText.getText().toString().trim()
                                                            + "_-_" + selectedDocName + "_-_"
                                                            + FilingSystem.Companion.getAllTags().toString() + "_-_" + docSize;
                                                }else {
                                                    docMetaData = mainFieldSpinner.getSelectedItem().toString() + "_-_"
                                                            + subFiledSpinner.getSelectedItem().toString()
                                                            + "_-_" + knowledgeBaseSpinner.getSelectedItem().toString()
                                                            + "_-_" + docDetailsEditText.getText().toString().trim()
                                                            + "_-_" + unitCodeEditText.getText().toString().trim()
                                                            + "_-_" + selectedDocName + "_-_" +
                                                            FilingSystem.Companion.getAllTags().toString() + "_-_" + docSize;
                                                }

                                                selectedDoc = new SelectedDoc(docMetaData, downloadLink.toString());

                                                shelfDatabaseRef.child(mainFieldSpinner.getSelectedItem().toString())
                                                        .child(replacer(selectedDocName))
                                                        .setValue(selectedDoc);
                                                Toast.makeText(getContext(), "Upload successful", Toast.LENGTH_SHORT).show();
                                                uploadProgress.setProgress(0);

                                            }else {
                                                Toast.makeText(getContext(), "Select something.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    FilingSystem.Companion.writeUploadedShelfManifest(c, docUri);
                                    Toast.makeText(c.getApplicationContext(), "Upload successful", Toast.LENGTH_SHORT).show();
                                    uploadProgress.setProgress(0);
//                            final String uploadID = shelfDatabaseRef.push().getKey(); //CREATES NEW UNIQUE ID


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(c.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    uploadProgress.setProgress((int) progress);
                                    //TO BE PLACED INSIDE A PROGRESS BAR LATER IN THE PROJECT
                                    //PROGRESS BAR SHALL BE SET BACK TO ZERO ON SUCCESS
                                }
                            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    uploadProgress.setVisibility(View.INVISIBLE);
                                }
                            });
                } else {
                    Toast.makeText(c.getApplicationContext(), "Select something.", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private String replacer(String docName){
        String docName1 = docName.replace("]", "");
        String docName2 = docName1.replace("[", "");
        String docName3 = docName2.replace(".", "");
        String docName4 = docName3.replace("$", "");
        String docName5 = docName4.replace("*", "");
        return docName5.replace("#", "");
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = c.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void docDuplicatePrevention(){

        final SelectedVideo selectedDoc1 = new SelectedVideo("", "");
        String description;
        Query query1 = shelfDatabaseRef.orderByChild(selectedDocName).equalTo(selectedDocName);
        Query query2 = shelfDatabaseRef.orderByChild(docSize).equalTo(docSize);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ArrayAdapter<String> subFieldAdapter = new ArrayAdapter<>(c.getApplicationContext(), R.layout.dropdown_drop_down, new ArrayList<>(Arrays.asList(DocSorting.getSubCategories(mainFieldSpinner.getSelectedItem().toString()))));
        subFiledSpinner.setAdapter(subFieldAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public TagsAdapter getTagsAdapter() {
        return tagsAdapter;
    }

    public void setTagsAdapter(TagsAdapter tagsAdapter) {
        this.tagsAdapter = tagsAdapter;
    }
}