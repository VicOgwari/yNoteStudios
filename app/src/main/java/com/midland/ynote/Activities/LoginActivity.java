package com.midland.ynote.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.midland.ynote.MainActivity;
import com.midland.ynote.Objects.Schools;
import com.midland.ynote.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText eMail, password, recoveryEmail;
    TextView singUp, forgotPassword;
    FirebaseAuth auth;
    String userPic, userEmail;
    ProgressDialog prg;
    String returnActivity;
    private LoginButton facebookLogin;
    private CallbackManager mCallbackManager;
    private final static String TAG = "FacebookAuth";
    ScrollView loginScrollView;
    private String fcmToken;
    private TwitterLoginButton mTwitterBtn;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseAuth auth1;
    CollectionReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        TwitterAuthConfig mTwitterAuthConfig = new TwitterAuthConfig(getString(R.string.twitter_consumer_key),
                getString(R.string.twitter_consumer_secret));
        TwitterConfig twitterConfig = new TwitterConfig.Builder(this)
                .twitterAuthConfig(mTwitterAuthConfig)
                .build();
        Twitter.initialize(twitterConfig);

        setContentView(R.layout.activity_login);
        users = FirebaseFirestore.getInstance().collection("Users");

        final Button loginBtn = findViewById(R.id.login);
        String token = getFcmToken();
        loginBtn.bringToFront();
        eMail = findViewById(R.id.emailSignUp1);
        password = findViewById(R.id.passwordSignUp1);
        singUp = findViewById(R.id.signUpText);
        forgotPassword = findViewById(R.id.forgotPassword);
        recoveryEmail = findViewById(R.id.recoveryEmail);
        loginScrollView = findViewById(R.id.loginScrollView);

        singUp.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));

        forgotPassword.setOnClickListener(view -> {
            if (forgotPassword.getText().toString().equals("Forgot password?")) {
                forgotPassword.setText("Remembered password :)");
                recoveryEmail.setVisibility(View.VISIBLE);
                eMail.setVisibility(View.INVISIBLE);
                password.setVisibility(View.INVISIBLE);
                loginBtn.setText("Recover Password");
                loginBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resetPassword();
                    }
                });
            } else if (forgotPassword.getText().toString().equals("Remembered password :)")) {
                forgotPassword.setText("Forgot password?");
                recoveryEmail.setVisibility(View.GONE);
                eMail.setVisibility(View.VISIBLE);
                password.setVisibility(View.VISIBLE);
                loginBtn.setText("Log in");

            }
        });

        loginBtn.setOnClickListener(v -> {
            prg = new ProgressDialog(LoginActivity.this);
            prg.setMessage("Signing you in...");

            if (eMail.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Required field(s) missing.", Toast.LENGTH_SHORT).show();
            } else {
                String e_mail = eMail.getText().toString().trim() + "@ynstudios.com";
                String passWord = password.getText().toString().trim();
                prg.show();
                auth = FirebaseAuth.getInstance();
                auth.signInWithEmailAndPassword(e_mail, passWord)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    FirebaseFirestore.getInstance()
                                            .collection("Users")
                                            .document(user.getUid())
                                            .update("fcmToken", token);
                                    CollectionReference enrolments = FirebaseFirestore.getInstance()
                                            .collection("Enrolments")
                                            .document("User ID")
                                            .collection(user.getUid());
                                    enrolments.get().addOnSuccessListener(queryDocumentSnapshots -> {
                                        ArrayList<String> schools = new ArrayList<>();
                                        //Save subFields to shared pref for future use in customizing data
                                        ArrayList<String> subFields = new ArrayList<>();

                                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots){
                                            schools.add(qds.getString("school"));
                                        }
                                        ArrayList<com.midland.ynote.Objects.Schools> schools1 = PhotoDoc.Companion.loadSchools(getApplicationContext(), "schools");
                                        for (Schools s : schools1){
                                            if (schools.contains(s.getSchoolName())){
                                                s.setEnrolled(true);
                                            }
                                        }
                                        PhotoDoc.Companion.saveSchools(getApplicationContext(), "schools", schools1);

                                        prg.dismiss();
                                        Snackbar snackbar = Snackbar.make(loginScrollView, "Welcome back!", Snackbar.LENGTH_SHORT);
                                        snackbar.show();

                                        CountDownTimer count = new CountDownTimer(2000, 1000) {
                                            @Override
                                            public void onTick(long millisUntilFinished) {
                                            }

                                            @Override
                                            public void onFinish() {
//                                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                onBackPressed();
                                                LoginActivity.this.finish();
                                            }
                                        };
                                        count.start();
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }

//                                        SystemClock.sleep(1300);
                            } else {
                                prg.dismiss();
                                Toast.makeText(LoginActivity.this, "Try with another Email & Password.", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        facebookLogin = findViewById(R.id.facebookLogIn);
        facebookLogin.setReadPermissions("email", "public_profile");
        mCallbackManager = CallbackManager.Factory.create();
        facebookLogin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess" + loginResult);
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "onError");

            }
        });

        //Twitter
        mAuth = FirebaseAuth.getInstance();

        mTwitterBtn = findViewById(R.id.twitter_login_button);

        mAuthListener = firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() != null){
//                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                onBackPressed();
            }
        };

        UpdateTwitterButton();

        mTwitterBtn.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Toast.makeText(LoginActivity.this, "Signed in to twitter successful", Toast.LENGTH_LONG).show();
                signInToFirebaseWithTwitterSession(result.data);
                mTwitterBtn.setVisibility(View.VISIBLE);
//                prg.setMessage("Signing you up..");
//                prg.show();
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(LoginActivity.this, "Login failed. No internet or No Twitter app found on your phone", Toast.LENGTH_LONG).show();
                Toast.makeText(LoginActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                UpdateTwitterButton();
            }
        });
    }

    private void UpdateTwitterButton(){
        if (TwitterCore.getInstance().getSessionManager().getActiveSession() == null){
            mTwitterBtn.setVisibility(View.VISIBLE);
        }
//        else{
//            mTwitterBtn.setVisibility(View.GONE);
//        }
    }

    private void signInToFirebaseWithTwitterSession(TwitterSession session){
        AuthCredential credential = TwitterAuthProvider.getCredential(session.getAuthToken().token,
                session.getAuthToken().secret);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    FirebaseUser firebaseUser = auth1.getCurrentUser();
                    if (firebaseUser != null) {
                        final String userID = firebaseUser.getUid();

                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("userID", userID);
                        userMap.put("alias", null);
                        userMap.put("fullName", null);
                        userMap.put("about", null);
                        userMap.put("gender", null);
                        userMap.put("registeredAs", null);
                        userMap.put("email", null);
                        userMap.put("phoneNumber", null);
                        userMap.put("password", null);
                        userMap.put("profilePicture", null);
                        userMap.put("coverArt", null);
                        userMap.put("institution", null);
                        userMap.put("school", null);
                        userMap.put("course", null);
                        userMap.put("birthday", null);
                        userMap.put("coaches", null);
                        userMap.put("students", null);
                        userMap.put("cQ", null);
                        userMap.put("fcmToken", null);
                        userMap.put("sandBox", null);
                        userMap.put("search_keyword", null);
                        userMap.put("schoolsPublished", null);
                        userMap.put("schoolsUploaded", null);

                        users.document(userID).get().addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()){
                                CountDownTimer count = new CountDownTimer(1000, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                    }

                                    @Override
                                    public void onFinish() {
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    }
                                };
                                count.start();
                            }else {
                                users.document(userID).set(userMap).addOnSuccessListener(aVoid -> {
                                    Map<String, List<String>> schools = new HashMap<>();
                                    schools.put("schools", null);
                                    users.document(userID).collection("SchoolsPublished")
                                            .document("Documents").set(schools);

                                    users.document(userID).collection("SchoolsPublished")
                                            .document("Lectures").set(schools);

                                    if (task.isSuccessful()) {
                                        Snackbar snackbar = Snackbar.make(loginScrollView, "Welcome!", Snackbar.LENGTH_SHORT);
                                        snackbar.show();
                                        CountDownTimer count = new CountDownTimer(1000, 1000) {
                                            @Override
                                            public void onTick(long millisUntilFinished) {

                                            }

                                            @Override
                                            public void onFinish() {
                                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                            }
                                        };
                                        count.start();
                                    }
                                });
                            }
                        });

                    }

                    if (!task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Something's not right! Try again.", Toast.LENGTH_LONG).show();
                    }
                });
    }


    private void resetPassword() {
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final String email = recoveryEmail.getText().toString();
        if (email.isEmpty()) {
            recoveryEmail.setError("Recovery email required!");
            recoveryEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            recoveryEmail.setError("Use a valid email address!");
            recoveryEmail.requestFocus();
            return;
        }
        prg = new ProgressDialog(LoginActivity.this);
        prg.setMessage("Sending password...");
        prg.show();

        Query recoveryEmail = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("email");
        recoveryEmail.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            prg.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Check your email.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Please try again later.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else if (snapshot.getChildrenCount() == 0) {
                    Toast.makeText(LoginActivity.this, "Your Email is not on our system.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public String getFcmToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()){
                Toast.makeText(this, "Something's missing...", Toast.LENGTH_SHORT).show();
            }else {
                fcmToken = task.getResult();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });

        return fcmToken;
    }

    private void handleFacebookToken(AccessToken accessToken) {
        Log.d(TAG, "handleFacebookToken" + accessToken);
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    updateUI(user);
                } else {

                }
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            if (user.getPhotoUrl() != null) {
                userPic = user.getPhotoUrl().toString();
                userPic = userPic + "?type=large";
            }

            if (user.getEmail() != null) {
                userEmail = user.getEmail();
            }
        }
    }
}