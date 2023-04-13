package com.midland.ynote.Dialogs;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.midland.ynote.Activities.CustomGallery;
import com.midland.ynote.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class BiggerPicture extends Dialog {

    ImageView biggerPicture;
    Button options, updateImage;
    FirebaseUser user;
    StorageTask<UploadTask.TaskSnapshot> uploadTask;
    private DatabaseReference shelfDatabaseRef;
    ProgressBar uploadProgress;
    Uri newProfileUri;
    View root;
    Activity a;
    Boolean update;

    public BiggerPicture(@NonNull Context context, Activity a, View root, FirebaseUser user, Uri newProfileUri, Boolean update) {
        super(context);
        this.user = user;
        this.newProfileUri = newProfileUri;
        this.a = a;
        this.root = root;
        this.update = update;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_bigger_picture);

        options = findViewById(R.id.doc_options_approval);
        updateImage = findViewById(R.id.updateBtn);
        uploadProgress = findViewById(R.id.uploadProgressApprove);
        biggerPicture = findViewById(R.id.biggerPicture);

        if (update){
            Glide.with(getContext())
                    .load(newProfileUri).thumbnail((float)0.9).placeholder(R.drawable.ic_account_circle).into(biggerPicture);
            updateImage.setVisibility(View.VISIBLE);
        }else {
            Glide.with(getContext())
                    .load(R.drawable.ic_account_circle).thumbnail((float)0.9).into(biggerPicture);
            updateImage.setVisibility(View.GONE);
        }

        options.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), biggerPicture);
            popupMenu.getMenuInflater().inflate(R.menu.bigger_picture_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.changePhoto){
                        checkAndroidVersion(a);
                    }else
                        if (item.getItemId() == R.id.removePhoto) {

                    }
                    return false;
                }
            });
            popupMenu.show();
        });



        updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDoc();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.changePhoto:
                selectImage();
                break;

            case R.id.removePhoto:
                Toast.makeText(getContext(), "Remove photo", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void selectImage(){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(a, Manifest.permission.READ_EXTERNAL_STORAGE)
                    && ActivityCompat.shouldShowRequestPermissionRationale(a, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Snackbar.make(root, "Permission", Snackbar.LENGTH_INDEFINITE)
                        .setAction("ENABLE", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(a, new String[]{
                                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                                }, 83);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(a, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 83);
            }
        } else {
            Intent intent = new Intent(getContext(), CustomGallery.class);
            intent.putExtra("IntentSelector", "biggerPicture");
            getContext().startActivity(intent);
        }
    }

    private void uploadDoc() {
        if (uploadTask != null && uploadTask.isInProgress()) {
            Toast.makeText(getContext(), "Give me a sec!", Toast.LENGTH_SHORT).show();
        }

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        final DatabaseReference profilePicQuery = databaseReference.child(user.getUid());
        final StorageReference profileStorageRef = FirebaseStorage.getInstance().getReference("Profile pictures");
        uploadProgress.setVisibility(View.VISIBLE);
        uploadProgress.bringToFront();
        uploadTask = profileStorageRef.putFile(newProfileUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                return profileStorageRef.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri downloadLink = task.getResult();
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("image", downloadLink);
                                    profilePicQuery.updateChildren(hashMap);
                                    Toast.makeText(getContext(), "Upload successful", Toast.LENGTH_SHORT).show();
                                    uploadProgress.setProgress(0);

                                } else {
                                    Toast.makeText(getContext(), "Select something.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        Toast.makeText(getContext(), "Upload successful", Toast.LENGTH_SHORT).show();
                        uploadProgress.setProgress(0);
//                            final String uploadID = shelfDatabaseRef.push().getKey(); //CREATES NEW UNIQUE ID


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("DocUploadError", e.getMessage());
                        Toast.makeText(getContext(), "Something's preventing your upload.", Toast.LENGTH_SHORT).show();
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

    public void checkAndroidVersion(Activity a) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                a.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 2);
            } else {
                pickImage(a);
            }
        } catch (Exception e) {
            pickImage(a);
        }

    }

    public void pickImage(Activity a) {
        CropImage.startPickImageActivity(a);
    }

}