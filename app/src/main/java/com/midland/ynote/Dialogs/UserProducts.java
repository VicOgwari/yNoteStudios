package com.midland.ynote.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.midland.ynote.Adapters.AllVidRecAdapter;
import com.midland.ynote.Adapters.CloudVideosAdapter;
import com.midland.ynote.Adapters.DocumentAdapter;
import com.midland.ynote.Adapters.GridRecyclerAdapter;
import com.midland.ynote.Adapters.SchoolsAdapter;
import com.midland.ynote.Adapters.UsersAdapter;
import com.midland.ynote.Objects.PendingLecObj;
import com.midland.ynote.Objects.SelectedDoc;
import com.midland.ynote.Objects.SourceDocObject;
import com.midland.ynote.Objects.User;
import com.midland.ynote.R;
import com.midland.ynote.Utilities.UserPowers;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class UserProducts extends Dialog {

    RecyclerView contentRV;
    ProgressBar productsProgress;
    TextView what;
    String whatText;
    String uid;
    FirebaseUser user;
    ArrayList<String> schools;

    public UserProducts(@NonNull Context context, String what, FirebaseUser user, ArrayList<String> schools) {
        super(context);
        this.whatText = what;
        this.user = user;
        this.schools = schools;
    }

    public UserProducts(@NonNull Context context, String what, String uid, FirebaseUser user) {
        super(context);
        this.whatText = what;
        this.uid = uid;
        this.user = user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        contentRV = findViewById(R.id.contentRV);
        productsProgress = findViewById(R.id.productsProgress);
        what = findViewById(R.id.what);

        DocumentReference documents = FirebaseFirestore.getInstance().collection("Content")
                .document("Documents");

        DocumentReference lectures = FirebaseFirestore.getInstance().collection("Content")
                .document("Lectures");


        what.setText(whatText);
        contentRV.setLayoutManager(new GridLayoutManager(getContext(), 2));

        if (whatText.equals("Read list")){
            DocumentReference pinsRef = FirebaseFirestore.getInstance().collection("Users")
                    .document(user.getUid())
                    .collection("ScheduledLists")
                    .document("ReadList");

            pinsRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()){
                    ArrayList<String> readListArray = (ArrayList<String>) documentSnapshot.get("readList");
                    if (readListArray != null){
                        ArrayList<SelectedDoc> publishedDocs = new ArrayList<>();
                        for (String s : readListArray){
                            documents.collection(s.split("_-_")[0])
                                    .document(s.split("_-_")[1])
                                    .get()
                                    .addOnSuccessListener(documentSnapshot1 -> {
                                        productsProgress.setVisibility(View.GONE);
                                        if (documentSnapshot1.exists()){
                                            productsProgress.setVisibility(View.GONE);
                                            publishedDocs.add(documentSnapshot1.toObject(SelectedDoc.class));
                                            DocumentAdapter docAdt = new DocumentAdapter(null, getContext(), getContext(), publishedDocs);
                                            docAdt.notifyDataSetChanged();
                                            contentRV.setAdapter(docAdt);
                                        }
                                    });
                        }
                    }
                }
            });
        }
        if (whatText.equals("Watch list")){
            DocumentReference pinsRef = FirebaseFirestore.getInstance().collection("Users")
                    .document(user.getUid())
                    .collection("ScheduledLists")
                    .document("WatchList");

            pinsRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()){
                    ArrayList<String> readListArray = (ArrayList<String>) documentSnapshot.get("watchList");
                    if (readListArray != null){
                        ArrayList<SelectedDoc> publishedDocs = new ArrayList<>();
                        for (String s : readListArray){
                            lectures.collection(s.split("_-_")[0])
                                    .document(s.split("_-_")[1])
                                    .get()
                                    .addOnSuccessListener(documentSnapshot1 -> {
                                        productsProgress.setVisibility(View.GONE);
                                        if (documentSnapshot1.exists()){
                                            productsProgress.setVisibility(View.GONE);
                                            publishedDocs.add(documentSnapshot1.toObject(SelectedDoc.class));
                                        }else {
                                            Toast.makeText(getContext(), "Nothing to show", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        CloudVideosAdapter docAdt = new CloudVideosAdapter(null, null, null, getContext(), getContext(), publishedDocs, null, null);
                        docAdt.notifyDataSetChanged();
                        contentRV.setAdapter(docAdt);
                    }
                }
            });
        }
        if (whatText.equals("shortCut") || whatText.equals("shortCut1")){
            productsProgress.setVisibility(View.GONE);
            SchoolsAdapter schoolsAdapter = new SchoolsAdapter(getContext(), UserPowers.Companion.setUpSchools(), "contentSelection");
            schoolsAdapter.notifyDataSetChanged();
            contentRV.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            contentRV.setAdapter(schoolsAdapter);
        }
        if (whatText.equals("shortCut1")){
            productsProgress.setVisibility(View.GONE);
            SchoolsAdapter schoolsAdapter = new SchoolsAdapter(getContext(), UserPowers.Companion.setUpSchools(), "contentSelection1");
            schoolsAdapter.notifyDataSetChanged();
            contentRV.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            contentRV.setAdapter(schoolsAdapter);
        }
        if (whatText.equals("Published documents")){

            ArrayList<SelectedDoc> publishedDocs = new ArrayList<>();
            for (String s : schools){
                documents.collection(s)
                        .whereEqualTo("uid", user.getUid())
                        .get()
                        .addOnCompleteListener(task -> {
                            productsProgress.setVisibility(View.GONE);
                            if (task.isSuccessful()){
                                productsProgress.setVisibility(View.GONE);
                                publishedDocs.addAll(task.getResult().toObjects(SelectedDoc.class));
                                DocumentAdapter docAdt = new DocumentAdapter(null, getContext(), getContext(), publishedDocs);
                                docAdt.notifyDataSetChanged();
                                contentRV.setAdapter(docAdt);
                            }else {
                                Snackbar.make(findViewById(R.id.rootLayout), "Something's up! This is our fault.", Snackbar.LENGTH_SHORT).show();
                            }
                        });
            }
        }
        if (whatText.equals("Published lectures")){
            ArrayList<SelectedDoc> uploadedVid = new ArrayList<>();
            for (String s : schools){
                lectures.collection(s)
                        .whereEqualTo("uid", user.getUid())
                        .get()
                        .addOnCompleteListener(task -> {
                            productsProgress.setVisibility(View.GONE);
                            if (task.isSuccessful()){
                                productsProgress.setVisibility(View.GONE);
                                uploadedVid.addAll(task.getResult().toObjects(SelectedDoc.class));
                                CloudVideosAdapter docAdt = new CloudVideosAdapter(null, null, null, getContext(), getContext(),
                                        uploadedVid, null, null);
                                docAdt.notifyDataSetChanged();
                                contentRV.setAdapter(docAdt);
                            }else {
                                Snackbar.make(findViewById(R.id.rootLayout), "Something's up! This is our fault.", Snackbar.LENGTH_SHORT).show();
                            }
                        });
            }
        }
        if (whatText.equals("Pending lectures")){
            productsProgress.setVisibility(View.GONE);
            if (pendingLecturesObj() == null){
                Snackbar.make(findViewById(R.id.rootLayout), "No lectures yet!", Snackbar.LENGTH_SHORT).show();
            }else {
                GridRecyclerAdapter grid = new GridRecyclerAdapter(getContext(), null,
                        pendingLecturesObj(), null, "ProfileItemsList",
                        null);
                grid.notifyDataSetChanged();
                contentRV.setAdapter(grid);
            }
        }
        if (whatText.equals("Completed lectures")){
            productsProgress.setVisibility(View.GONE);
            if (getVideoFileObjects() == null){
                Snackbar.make(findViewById(R.id.rootLayout), "No lectures yet!", Snackbar.LENGTH_SHORT).show();
            }else {
                AllVidRecAdapter allVidAdapter = new AllVidRecAdapter(getContext(), getVideoFileObjects(), "offline", null);
                allVidAdapter.notifyDataSetChanged();
                contentRV.setAdapter(allVidAdapter);
            }
        }
        if (whatText.equals("Coaches")){
            ArrayList<User> users = new ArrayList<>();
            contentRV.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

            String id = "";
            if(user == null){
                id = uid;
            }else {
                id = user.getUid();
            }
            CollectionReference coachesList = FirebaseFirestore.getInstance()
                    .collection("Users").document(id).collection("squad");
            coachesList.document("coaches").get()
                    .addOnSuccessListener(documentSnapshot -> {
                        ArrayList<String> uiDs = (ArrayList<String>) documentSnapshot.get("userIDs");

                        if (uiDs != null){

                            for (String s : uiDs){
                                FirebaseFirestore.getInstance().collection("Users")
                                        .document(s).get().addOnSuccessListener(documentSnapshot1 -> {
                                    String alias = documentSnapshot1.getString("alias");
                                    String fullName = documentSnapshot1.getString("fullName");
                                    String userImage = documentSnapshot1.getString("profilePicture");
                                    String institution = documentSnapshot1.getString("institution");
                                    String school = documentSnapshot1.getString("school");
                                    String philosophy = documentSnapshot1.getString("about");
//                                    ArrayList<String> registeredAs = (ArrayList<String>) documentSnapshot1.get("registeredAs");
                                    String userId = documentSnapshot1.getString("userID");
                                    String coaches = String.valueOf(documentSnapshot1.get("coaches"));
                                    String students = String.valueOf(documentSnapshot1.get("students"));
                                    String course = documentSnapshot1.getString("course");

                                    User user = new User(fullName, userImage, course, coaches, students, institution,
                                            "", userId, school, philosophy);
                                    users.add(user);

                                    UsersAdapter usersAdapter = new UsersAdapter(getContext(), users, "follow");
                                    usersAdapter.notifyDataSetChanged();
                                    contentRV.setAdapter(usersAdapter);
                                    productsProgress.setVisibility(View.GONE);

                                }).addOnFailureListener(e -> {
                                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            }

                        }else {
                        }
                    }).addOnFailureListener(e -> {

            });

        }
        if (whatText.equals("Students")){
            ArrayList<User> users1 = new ArrayList<>();
            contentRV.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            String id = "";
            if(user == null){
                id = uid;
            }else {
                id = user.getUid();
            }
            CollectionReference coachesList = FirebaseFirestore.getInstance()
                    .collection("Users").document(id).collection("squad");
            coachesList.document("students").get()
                    .addOnSuccessListener(documentSnapshot -> {
                        ArrayList<String> uiDs = (ArrayList<String>) documentSnapshot.get("userIDs");

                        if (uiDs != null){

                            for (String s : uiDs){

                                FirebaseFirestore.getInstance().collection("Users")
                                        .document(s).get().addOnSuccessListener(documentSnapshot1 -> {
                                    String alias = documentSnapshot1.getString("alias");
                                    String fullName = documentSnapshot1.getString("fullName");
                                    String userImage = documentSnapshot1.getString("profilePicture");
                                    String institution = documentSnapshot1.getString("institution");
                                    String school = documentSnapshot1.getString("school");
                                    String philosophy = documentSnapshot1.getString("about");
//                                    ArrayList<String> registeredAs = (ArrayList<String>) documentSnapshot1.get("registeredAs");
                                    String userId = documentSnapshot1.getString("userID");
                                    String coaches = String.valueOf(documentSnapshot1.get("coaches"));
                                    String students = String.valueOf(documentSnapshot1.get("students"));
                                    String course = documentSnapshot1.getString("course");

                                    User user = new User(fullName, userImage, course, coaches, students, institution,
                                            "", userId, school, philosophy);
                                    users1.add(user);

                                    UsersAdapter usersAdapter = new UsersAdapter(getContext(), users1, "follow");
                                    usersAdapter.notifyDataSetChanged();
                                    contentRV.setAdapter(usersAdapter);
                                    productsProgress.setVisibility(View.GONE);

                                }).addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                            }

                        }else {
                        }
                    }).addOnFailureListener(e -> {

            });


        }

    }

    private ArrayList<PendingLecObj> pendingLecturesObj() {
        SharedPreferences preferences = getContext().getSharedPreferences("pendingLectures", Context.MODE_PRIVATE);
        String json = preferences.getString("pendingLectures", "");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<PendingLecObj>>(){}.getType();

        return gson.fromJson(json, type);
    }


    public ArrayList<SourceDocObject> getVideoFileObjects() {
        ArrayList<SourceDocObject> sourceDocObjects = new ArrayList<>();
        File[] list =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).listFiles();
            if (list != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                for (File file : list) {
                    int fileSize = Integer.parseInt(String.valueOf(file.length() / 1024));
                    if (fileSize != 0) {
                        String newName = file.getName().replace("dubLecture", "mp4");
                        SourceDocObject sourceDocObject = new SourceDocObject(newName, Uri.fromFile(file), String.valueOf(fileSize),
                                dateFormat.format(file.lastModified()), Uri.fromFile(file), file.lastModified());
                        sourceDocObjects.add(sourceDocObject);
                    }
                }
            }

        return sourceDocObjects;
    }


}