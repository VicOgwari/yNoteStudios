package com.midland.ynote.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.midland.ynote.R;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.midland.ynote.MainActivity;
import com.midland.ynote.Objects.User;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ProgressBar updateProgress;
    CheckBox lecCheck, studCheck, male, female;
    TextInputEditText course, purpose;
    Spinner schoolSpinner, institutionSpinner, userTitleSpinner;
    TextView cQ, students, coaches;
    ImageButton loadProfile, loadCoverArt;
    ImageView coverArt, profile;
    ProgressDialog prg;
    EditText userAlias, fullNameET, institutionETEditor, phoneNumberET;
    Button updateProfile;
    Uri profileUri, uri1;
    List<String> publishedSch = new ArrayList<>();
    List<String> uploadedSch = new ArrayList<>();
    ArrayList<String> registeredAs = new ArrayList<>();
    String otherSignUp, photo, institution, school, courseString, fullName, alias, gender, eMail, about, userID, phoneNumber, birthday, fcmToken;
    User editUser;
    StorageTask<UploadTask.TaskSnapshot> uploadTask, uploadTask1;
    DatePicker datePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        prg = new ProgressDialog(EditProfile.this);
        prg.setMessage("Just a sec...");



        otherSignUp = getIntent().getStringExtra("otherSignUp");
        if (otherSignUp != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getIntent().getStringExtra("otherSignUp"));
            builder.setTitle("Finish registration");
            builder.setPositiveButton("Continue",
                    (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        Toast.makeText(getApplicationContext(), "Give accurate details..", Toast.LENGTH_SHORT).show();
                    });
            builder.setNegativeButton("Cancel",
                    (dialogInterface, i) -> {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

        if (getIntent().getSerializableExtra("editUser") != null){
            editUser = (User) getIntent().getSerializableExtra("editUser");
            publishedSch = getIntent().getStringArrayListExtra("publishedSch");
            uploadedSch = getIntent().getStringArrayListExtra("uploadedSch");
        }else {
            Toast.makeText(this, "Something's missing...", Toast.LENGTH_SHORT).show();
        }


        loadCoverArt = findViewById(R.id.loadCoverArt);
        datePicker = findViewById(R.id.datePicker);
        institutionETEditor = findViewById(R.id.institutionETEditor);
        phoneNumberET = findViewById(R.id.phoneNumber);
        loadProfile = findViewById(R.id.loadImage);
        updateProgress = findViewById(R.id.updateProgress);
        coverArt = findViewById(R.id.userCoverArt);
        lecCheck = findViewById(R.id.checkboxLecturer);
        studCheck = findViewById(R.id.checkboxStudent);
        male = findViewById(R.id.checkboxMale);
        female = findViewById(R.id.checkboxFemale);
        course = findViewById(R.id.courseETEditor);
        purpose = findViewById(R.id.aboutETEditor);
        institutionSpinner = findViewById(R.id.institutionSpinnerEditor);
        userTitleSpinner = findViewById(R.id.userTitleSpinner);
        schoolSpinner = findViewById(R.id.schoolSpinnerEditor);
        cQ = findViewById(R.id.creativeQuotient);
        coaches = findViewById(R.id.coaches);
        students = findViewById(R.id.students);
        profile = findViewById(R.id.userProfilePicture);
        userAlias = findViewById(R.id.userAlias);
        fullNameET = findViewById(R.id.fullNameET);
        updateProfile = findViewById(R.id.updateProfile);
        updateProfile.bringToFront();

        if (editUser != null){

            Glide.with(getApplicationContext()).load(editUser.getProfileUrl()).into(profile);
            Glide.with(getApplicationContext()).load(editUser.getCoverArt()).into(coverArt);
            course.setText(editUser.getCourse());
            purpose.setText(editUser.getAbout());
            userAlias.setText(editUser.getAliasName());
            fullNameET.setText(editUser.getFullName().split(". ")[1]);
            phoneNumberET.setText(editUser.getPhoneNumber());
            cQ.setText(String.format("cQ: %s", editUser.getCQ()));
            coaches.setText(editUser.getCoaches() + " Coaches");
            students.setText(editUser.getStudents() + " Students");
            userID = editUser.getUid();
        }else {
            userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }


        institutionSpinner.setOnItemSelectedListener(this);
        schoolSpinner.setOnItemSelectedListener(this);
        userTitleSpinner.setOnItemSelectedListener(this);

        final ArrayAdapter<CharSequence> titleAdapter = ArrayAdapter.createFromResource(this,
                R.array.user_titles, android.R.layout.simple_spinner_item);
        final ArrayAdapter<CharSequence> institutionsAdapter = ArrayAdapter.createFromResource(this,
                R.array.institutions, R.layout.spinner_drop_down_yangu1);
        final ArrayAdapter<CharSequence> schoolsAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.main_fields, R.layout.spinner_drop_down_yangu1);

        schoolsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        institutionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        titleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        institutionSpinner.setAdapter(institutionsAdapter);
        schoolSpinner.setAdapter(schoolsAdapter);
        userTitleSpinner.setAdapter(titleAdapter);

        if (editUser != null) {
            institutionSpinner.setSelection(institutionsAdapter.getPosition(editUser.getInstitution()));
            schoolSpinner.setSelection(institutionsAdapter.getPosition(editUser.getSchool()));
            userTitleSpinner.setSelection(titleAdapter.getPosition(editUser.getFullName().split(". ")[0] + "."));
        }
        loadProfile.bringToFront();
        loadCoverArt.bringToFront();
        loadProfile.setOnClickListener(v -> {
            photo = "Profile";
            CropImage.startPickImageActivity(EditProfile.this);
        });
        loadCoverArt.setOnClickListener(v -> {
            photo = "CoverArt";
            CropImage.startPickImageActivity(EditProfile.this);
        });

        lecCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                registeredAs.add("Lecturer");
            }else {
                registeredAs.remove("Lecturer");
            }
        });

        studCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                registeredAs.add("Student");
            }else {
                registeredAs.remove("Student");
            }
        });

        male.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if (buttonView.isChecked()){
                female.setChecked(false);
                gender = "M";
            }
        }));
        female.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if (buttonView.isChecked()){
                male.setChecked(false);
                gender = "F";
            }
        }));

        institutionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                institution = parent.getItemAtPosition(position).toString();
                if (institution.equals("Type my institution")){
                    institutionETEditor.setVisibility(View.VISIBLE);
                }else {
                    institutionETEditor.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        schoolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                school = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        updateProfile.setOnClickListener(v -> {
            alias = userAlias.getText().toString().trim();
            fullName = fullNameET.getText().toString().trim();
            courseString = course.getText().toString().trim();
            about = purpose.getText().toString().trim();
            phoneNumber = phoneNumberET.getText().toString().trim();
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();
            int year = datePicker.getYear();
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            birthday = dateFormat.format(calendar.getTime());

            if (gender == null){
                Toast.makeText(this, "Select your gender...", Toast.LENGTH_SHORT).show();
            }
            else
            if (institution == null){
                institutionETEditor.setError("Missing entry.");
            } else
            if (alias == null){
                userAlias.setError("Missing entry.");
                Toast.makeText(this, "Something's missing.", Toast.LENGTH_SHORT).show();
            } else
            if (about.equals("")){
                purpose.setError("Say something");
                Toast.makeText(this, "Something's missing.", Toast.LENGTH_SHORT).show();
            } else
            if (fullName == null){
                fullNameET.setError("Missing entry.");
                Toast.makeText(this, "Something's missing.", Toast.LENGTH_SHORT).show();
            } else
            if (fullName.split(" ").length == 1){
                fullNameET.setError("Two names please.");
                Toast.makeText(this, "Something's missing.", Toast.LENGTH_SHORT).show();
            } else
            if (courseString == null){
                course.setError("Missing entry.");
                Toast.makeText(this, "Something's missing.", Toast.LENGTH_SHORT).show();
            } else
            if (phoneNumber == null || phoneNumber.length() < 10){
                phoneNumberET.setError("Missing entry.");
            } else
            if (registeredAs == null){
                Toast.makeText(this, "Register as?", Toast.LENGTH_SHORT).show();
            } else {
                if (uploadTask1 != null && uploadTask1.isInProgress()) {
                    Toast.makeText(getApplicationContext(), "Give me a sec!", Toast.LENGTH_SHORT).show();
                }else {
                    uploadUserImage(null, profileUri);
                }
            }
        });
    }

    private void uploadUserImage(Uri coverDownloadLink, Uri uri) {
        prg.show();
        Map<String, Object> userMap = new HashMap<>();
        if (institution.equals("Type my institution")){
            userMap.put("institution", institutionETEditor.getText().toString().trim());
        }else {
            userMap.put("institution", institution);
        }
        userMap.put("alias", alias);
        userMap.put("fullName", fullName);
        userMap.put("gender", gender);
        userMap.put("coverArt", String.valueOf(coverDownloadLink));
        userMap.put("school", school);
        userMap.put("about", about);
        userMap.put("course", courseString);
        userMap.put("registeredAs", registeredAs);
        userMap.put("userID", userID);
        userMap.put("email", eMail);
        userMap.put("phoneNumber", phoneNumber);
        userMap.put("password", "");
        userMap.put("birthday", birthday);
        userMap.put("fcmToken", getFcmToken());
        userMap.put("sandBox", null);
        userMap.put("search_keyword", getFcmToken());
        userMap.put("schoolsPublished", publishedSch);
        userMap.put("schoolsUploaded", uploadedSch);
        userMap.put("status", "active");

//        userMap.put("cQ", "active");
//        userMap.put("students", "active");
//        userMap.put("coaches", "active");
//        userMap.put("account", "active");
//        userMap.put("profilePicture", "active");



        if (uri != null){
            Toast.makeText(getApplicationContext(), uri.toString(), Toast.LENGTH_SHORT).show();
            StorageReference userImagesStorageRef = FirebaseStorage.getInstance()
                    .getReference("UserImages").child(System.currentTimeMillis() + "." + getFileExtension(uri));
            uploadTask1 = userImagesStorageRef.putFile(uri).addOnSuccessListener(taskSnapshot12 -> {
                Task<Uri> uriTask1 = uploadTask1.continueWithTask(task12 -> {
                    if (!task12.isSuccessful()) {
                        throw task12.getException();
                    }
                    return userImagesStorageRef.getDownloadUrl();


                }).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {

                        Uri userImageDownloadLink = task1.getResult();
                        userMap.put("profilePicture", String.valueOf(userImageDownloadLink));

                        DocumentReference user =  FirebaseFirestore.getInstance().collection("Users")
                                .document(userID);
                         user.update(userMap).addOnSuccessListener(unused -> {
                                Snackbar.make(findViewById(R.id.profileRoot), "Successful update!", BaseTransientBottomBar.LENGTH_LONG).show();
                                prg.dismiss();
                                SystemClock.sleep(1300);
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }).addOnFailureListener(e -> {
                             Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                Snackbar.make(findViewById(R.id.profileRoot), "Something's missing. Try again.", BaseTransientBottomBar.LENGTH_SHORT).show();
                            });

                    } else {
                        Toast.makeText(getApplicationContext(), "Select something.", Toast.LENGTH_SHORT).show();
                    }
                });
                updateProgress.setProgress(0);

            })
                    .addOnFailureListener(e -> {
                        Log.e("DocUploadError", e.getMessage());
                        Toast.makeText(getApplicationContext(), "Something's preventing your upload.", Toast.LENGTH_SHORT).show();
                    })
                    .addOnProgressListener(taskSnapshot1 -> {
                        double progress = (100.0 * taskSnapshot1.getBytesTransferred() / taskSnapshot1.getTotalByteCount());
                        updateProgress.setProgress((int) progress);
                        //TO BE PLACED INSIDE A PROGRESS BAR LATER IN THE PROJECT
                        //PROGRESS BAR SHALL BE SET BACK TO ZERO ON SUCCESS
                    })
                    .addOnCompleteListener(task13 -> updateProgress.setVisibility(View.INVISIBLE));
        }
        else {

            DocumentReference user =  FirebaseFirestore.getInstance().collection("Users")
                    .document(userID);
                user.update(userMap).addOnSuccessListener(unused -> {
                    Snackbar.make(findViewById(R.id.profileRoot), "Successful update!", BaseTransientBottomBar.LENGTH_LONG).show();
                    prg.dismiss();
                    SystemClock.sleep(1300);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Snackbar.make(findViewById(R.id.profileRoot), "Something's missing. Try again.", BaseTransientBottomBar.LENGTH_LONG).show();
                });
        }
    }

    private void uploadCoverArt(Uri coverUri) {
        StorageReference coverArtStorageRef = FirebaseStorage.getInstance().getReference("CoverImages").child(String.valueOf(System.currentTimeMillis()));
        uploadTask = coverArtStorageRef.putFile(coverUri).addOnSuccessListener(taskSnapshot12 -> {
            Task<Uri> uriTask1 = uploadTask.continueWithTask(task12 -> {
                if (!task12.isSuccessful()) {
                    throw task12.getException();
                }
                return coverArtStorageRef.getDownloadUrl();


            }).addOnCompleteListener(task1 -> {
                if (task1.isSuccessful()) {
                    Uri coverDownloadLink = task1.getResult();
                    uploadUserImage(coverDownloadLink, profileUri);

                } else {
                    Toast.makeText(getApplicationContext(), "Select something.", Toast.LENGTH_SHORT).show();
                }
            });
            Toast.makeText(getApplicationContext(), "Document & cover published", Toast.LENGTH_SHORT).show();
            updateProgress.setProgress(0);
//                            final String uploadID = shelfDatabaseRef.push().getKey(); //CREATES NEW UNIQUE ID


        })
                .addOnFailureListener(e -> {
                    Log.e("DocUploadError", e.getMessage());
                    Toast.makeText(getApplicationContext(), "Something's preventing your upload.", Toast.LENGTH_SHORT).show();
                })
                .addOnProgressListener(taskSnapshot1 -> {
                    double progress = (100.0 * taskSnapshot1.getBytesTransferred() / taskSnapshot1.getTotalByteCount());
                    updateProgress.setProgress((int) progress);
                    //TO BE PLACED INSIDE A PROGRESS BAR LATER IN THE PROJECT
                    //PROGRESS BAR SHALL BE SET BACK TO ZERO ON SUCCESS
                })
                .addOnCompleteListener(task13 -> updateProgress.setVisibility(View.INVISIBLE));

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (otherSignUp != null){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getApplicationContext(), "User signed out!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK){
            profileUri = CropImage.getPickImageResultUri(this, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(this, profileUri)){
               if (photo.equals("CoverArt")){
                    uri1 = profileUri;
                }else {
                    Toast.makeText(this, "Something's up...", Toast.LENGTH_SHORT).show();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }
            }else {
                startCrop(profileUri);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                assert result != null;
                if (photo.equals("Profile")){
                    Glide.with(getApplicationContext()).load(result.getUri()).into(profile);
                }else if (photo.equals("CoverArt")){
                    Glide.with(getApplicationContext()).load(result.getUri()).into(coverArt);
                }else {
                    Toast.makeText(this, "Something's up...", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    private void startCrop(Uri profileUri) {
        CropImage.activity(profileUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public String getFcmToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()){
                Toast.makeText(this, "FCM's missing...", Toast.LENGTH_SHORT).show();
            }else {
                fcmToken = task.getResult();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "FCM2's missing...", Toast.LENGTH_SHORT).show();
        });

        return fcmToken;
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}