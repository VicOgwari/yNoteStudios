package com.midland.ynote.Activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.midland.ynote.Activities.PhotoDoc.Companion.loadSchools
import com.midland.ynote.Activities.PhotoDoc.Companion.saveSchools
import com.midland.ynote.Adapters.CloudVideosAdapter
import com.midland.ynote.Adapters.DocumentAdapter
import com.midland.ynote.Adapters.HomeBitmapsAdapter
import com.midland.ynote.Dialogs.BiggerPicture
import com.midland.ynote.Dialogs.LogInSignUp
import com.midland.ynote.Dialogs.UserProducts
import com.midland.ynote.MainActivity
import com.midland.ynote.Objects.BitMapTitle
import com.midland.ynote.Objects.Schools
import com.midland.ynote.Objects.SelectedDoc
import com.midland.ynote.Objects.User
import com.midland.ynote.R
import com.midland.ynote.Utilities.UserPowers.Companion.isFollowing
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

class UserProfile2 : AppCompatActivity() {
    var shelfButton: Button? = null
    var publishedDocsBtn: Button? = null
    var pendingLecBtn: Button? = null
    var publishedLecBtn: Button? = null
    var completedLecBtn: Button? = null
    var addToLib: Button? = null
    var galleryBtn: Button? = null
    var watchLaterBtn: Button? = null
    var userNameProfile: CardView? = null
    var userImage: ImageView? = null
    var shelfImage: ImageView? = null
    var publishedDocsImage: ImageView? = null
    var pendingLecturesImage: ImageView? = null
    var completedLecturesImage: ImageView? = null
    var publishedLecturesImage: ImageView? = null
    var galleryImage: ImageView? = null
    var loadUserImage: ProgressBar? = null
    var publishedSchoolsRV: RecyclerView? = null
    var userLecturesRV: RecyclerView? = null
    var pictorialsRV: RecyclerView? = null
    var searchUserLectures: SearchView? = null
    var coaches: TextView? = null
    var students: TextView? = null
    var creativeQuotient: TextView? = null
    var userName: TextView? = null
    var userAlias: TextView? = null
    var publishedSchools: TextView? = null
    var userLectures: TextView? = null
    var users = FirebaseFirestore.getInstance().collection("Users")
    var userID: String? = null
    var otherUser: String? = null
    var profilePictureCard: CardView? = null
    var firebaseUser: FirebaseUser? = null
    var userImageUrl: String? = null
    var coachesStr: String? = null
    var studentsStr: String? = null
    var cQ: String? = null
    var profileUrl: String? = null
    var course: String? = null
    var institution: String? = null
    var school: String? = null
    var gender: String? = null
    var alias: String? = null
    var email: String? = null
    var fullName: String? = null
    var coverArt: String? = null
    var about: String? = null
    var phoneNumber: String? = null
    var auth1: FirebaseAuth? = null
    var publishedSch: List<String>? = ArrayList()
    var uploadedSch: List<String>? = ArrayList()
    var filter: LectureFilter? = null
    var newSchools0: ArrayList<Schools>? = null
    var newSchools1: ArrayList<Schools>? = null
    var registeredAs: ArrayList<String>? = ArrayList()
    var prg1: ProgressBar? = null
    var prg3: ProgressBar? = null
    var prg2: ProgressBar? = null
    var pendingCard: CardView? = null
    var completedCard: CardView? = null
    var fitToScreen: Boolean? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile2)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        fitToScreen = false
        prg1 = findViewById(R.id.prog1)
        prg3 = findViewById(R.id.prog3)
        prg2 = findViewById(R.id.prog2)
        completedCard = findViewById(R.id.completedCard)
        pendingCard = findViewById(R.id.pendingCard)
        loadUserImage = findViewById(R.id.loadImage)
        userImage = findViewById(R.id.userProfilePicture)
        galleryImage = findViewById(R.id.galleryImage)
        coaches = findViewById(R.id.coaches)
        students = findViewById(R.id.students)
        creativeQuotient = findViewById(R.id.creativeQuotient)
        userNameProfile = findViewById(R.id.userNameProfileCard)
        userName = findViewById(R.id.userNameProfile)
        profilePictureCard = findViewById(R.id.profilePictureCard)
        publishedSchoolsRV = findViewById(R.id.publishedSchoolsRV)
        pictorialsRV = findViewById(R.id.pictorialsRV)
        publishedSchools = findViewById(R.id.publishedSchools)
        publishedSchoolsRV!!.layoutManager = LinearLayoutManager(
            applicationContext,
            RecyclerView.HORIZONTAL,
            false
        )
        userLecturesRV = findViewById(R.id.userLecturesRV)
        userLectures = findViewById(R.id.publishedLectures)
        userLecturesRV!!.layoutManager = LinearLayoutManager(
            applicationContext,
            RecyclerView.HORIZONTAL,
            false
        )
        pictorialsRV!!.layoutManager = LinearLayoutManager(
            applicationContext,
            RecyclerView.VERTICAL,
            false
        )
        searchUserLectures = findViewById(R.id.searchUserLectures)
        userImage!!.bringToFront()
        userImage!!.setOnClickListener(View.OnClickListener { v: View? ->
            if (fitToScreen!!) {
                fitToScreen = false
                userImage!!.layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                userImage!!.adjustViewBounds = true
            } else {
                fitToScreen = true
                userImage!!.layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT
                )
            }
        })
        shelfButton = findViewById(R.id.shelfBtn)
        watchLaterBtn = findViewById(R.id.watchLaterBtn)
        //        publishedDocsBtn = findViewById(R.id.publishedDocsBtn);
        pendingLecBtn = findViewById(R.id.pendingLecturesBtn)
        //        publishedLecBtn = findViewById(R.id.publishedLecturesBtn);
        completedLecBtn = findViewById(R.id.completedLecturesBtn)
        galleryBtn = findViewById(R.id.galleryBtn)
        userAlias = findViewById(R.id.userAlias)
        addToLib = findViewById(R.id.btnFollowProfile)
        //        publishedLecturesImage = findViewById(R.id.publishedLecturesImage);
        completedLecturesImage = findViewById(R.id.completedLecturesImage)
        pendingLecturesImage = findViewById(R.id.pendingLecturesImage)
        //        publishedDocsImage = findViewById(R.id.publishedDocsImage);
        shelfImage = findViewById(R.id.shelfImage)
        Glide.with(applicationContext).load(R.drawable.profile_shelf).thumbnail(0.9.toFloat())
            .into(shelfImage!!)
        Glide.with(applicationContext).load(R.drawable.ic_account_circle).thumbnail(0.9.toFloat())
            .into(galleryImage!!)
        Glide.with(applicationContext).load(R.drawable.pending_lectures).thumbnail(0.9.toFloat())
            .into(pendingLecturesImage!!)
        //        Glide.with(getApplicationContext())
//                .load(R.drawable.uploaded_lectures)
//                .thumbnail((float) 0.9)
//                .into(publishedLecturesImage);
//        Glide.with(getApplicationContext()).load(R.drawable.published_docs).thumbnail((float) 0.9).into(publishedDocsImage);
        Glide.with(applicationContext).load(R.drawable.completed_lectures).thumbnail(0.9.toFloat())
            .into(completedLecturesImage!!)
        auth1 = FirebaseAuth.getInstance()
        firebaseUser = auth1!!.currentUser
        otherUser = intent.getStringExtra("userID")
        if (otherUser == null) {
            if (firebaseUser != null) {
                userID = firebaseUser!!.uid
                queryUser(userID)
                userAlias!!.text = firebaseUser!!.displayName
            } else {
                val logInSignUp =
                    LogInSignUp(this@UserProfile2)
                logInSignUp.show()
            }
        } else
        {
            userID = intent.getStringExtra("userID")
            if (userID != null || userID != "") {
                queryUser(userID)
            }
            //            pendingCard.setVisibility(View.GONE);
//            completedCard.setVisibility(View.GONE);
            if (firebaseUser != null) {
                isFollowing(userID!!, addToLib, firebaseUser!!, this@UserProfile2, null)
            }
            //Update UI
        }
        if (intent.getStringExtra("SelectedPic") != null) {
            //Bigger picture
            val biggerPicture = BiggerPicture(
                this@UserProfile2, parent,
                null, firebaseUser, Uri.parse(intent.getStringExtra("SelectedPic")), true
            )
            biggerPicture.show()
        }
        profilePictureCard!!.setOnClickListener {
            if (userImageUrl != null) {
                val biggerPicture = BiggerPicture(
                    this@UserProfile2, parent,
                    findViewById(R.id.profileRoot), firebaseUser, Uri.parse(userImageUrl), false
                )
                biggerPicture.show()
            }
        }
        galleryBtn!!.setOnClickListener {
            if (otherUser == null) {
                if (firebaseUser != null) {
                    val intent = Intent(this@UserProfile2, ProfileItemsList::class.java)
                    intent.putExtra("button", "galleryBtn")
                    startActivity(intent)
                } else {
                    val logInSignUp =
                        LogInSignUp(this@UserProfile2)
                    logInSignUp.show()
                }
            } else {
                val intent = Intent(this@UserProfile2, ProfileItemsList::class.java)
                intent.putExtra("button", "galleryBtnUser")
                intent.putExtra("userID", otherUser)
                startActivity(intent)
            }
        }


        shelfButton!!.setOnClickListener {
            if (otherUser == null) {
                if (firebaseUser != null) {
                    val intent = Intent(this@UserProfile2, ProfileItemsList::class.java)
                    intent.putExtra("button", "shelfBtn")
                    startActivity(intent)
                } else {
                    val logInSignUp =
                        LogInSignUp(this@UserProfile2)
                    logInSignUp.show()
                }
            } else {
                val intent = Intent(this@UserProfile2, ProfileItemsList::class.java)
                intent.putExtra("button", "shelfBtnUser")
                intent.putExtra("userID", otherUser)
                startActivity(intent)
            }
        }
        watchLaterBtn!!.setOnClickListener {
            if (otherUser == null) {
                if (firebaseUser != null) {
                    val intent = Intent(this@UserProfile2, ProfileItemsList::class.java)
                    intent.putExtra("button", "watchLater")
                    startActivity(intent)
                } else {
                    val logInSignUp =
                        LogInSignUp(this@UserProfile2)
                    logInSignUp.show()
                }
            } else {
                val intent = Intent(this@UserProfile2, ProfileItemsList::class.java)
                intent.putExtra("button", "watchLaterUser")
                intent.putExtra("userID", otherUser)
                startActivity(intent)
            }
        }
        pendingLecBtn!!.setOnClickListener({ v: View? ->
            if (otherUser == null) {
                if ((ContextCompat.checkSelfPermission(
                        this@UserProfile2,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(
                        this@UserProfile2,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED)
                ) {
                    if ((ActivityCompat.shouldShowRequestPermissionRationale(
                            parent,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                                && ActivityCompat.shouldShowRequestPermissionRationale(
                            parent,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ))
                    ) {
                        Snackbar.make(
                            findViewById(R.id.profileRoot),
                            "Permission",
                            Snackbar.LENGTH_INDEFINITE
                        )
                            .setAction("ENABLE", View.OnClickListener {
                                ActivityCompat.requestPermissions(
                                    parent, arrayOf(
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    ), 7
                                )
                            }).show()
                    } else {
                        ActivityCompat.requestPermissions(
                            parent, arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ), 7
                        )
                    }
                } else {
                    val intent = Intent(this@UserProfile2, ProfileItemsList::class.java)
                    intent.putExtra("button", "pendingLecBtn")
                    startActivity(intent)
                }
            } else {
                val snackbar = Snackbar.make(
                    findViewById(R.id.profileRoot),
                    "Can't access user's pending files!",
                    Snackbar.LENGTH_SHORT
                )
                snackbar.show()
            }
        })

//        publishedLecBtn.setOnClickListener(v -> {
//            if (userID == null){
//                if (firebaseUser != null) {
//                    Intent intent = new Intent(UserProfile2.this, ProfileItemsList.class);
//                    intent.putExtra("button", "publishLecBtn");
//                    intent.putExtra("userID", userID);
//                    startActivity(intent);
//                } else {
//                    LogInSignUp logInSignUp = new LogInSignUp(UserProfile2.this);
//                    logInSignUp.show();
//                }
//            }else {
//                Intent intent = new Intent(UserProfile2.this, ProfileItemsList.class);
//                intent.putExtra("button", "publishLecBtnUser");
//                intent.putExtra("userID", userID);
//                startActivity(intent);
//            }
//        });

//        publishedDocsBtn.setOnClickListener(v -> {
//            if (otherUser == null){
//                if (firebaseUser != null) {
//                    Intent intent = new Intent(UserProfile2.this, ProfileItemsList.class);
//                    intent.putExtra("button", "publishDocBtn");
//                    intent.putExtra("userID", userID);
//                    startActivity(intent);
//                } else {
//                    LogInSignUp logInSignUp = new LogInSignUp(UserProfile2.this);
//                    logInSignUp.show();
//                }
//            }else {
//                Intent intent = new Intent(UserProfile2.this, ProfileItemsList.class);
//                intent.putExtra("button", "publishDocBtnUser");
//                intent.putExtra("userID", userID);
//                startActivity(intent);
//            }
//        });
        completedLecBtn!!.setOnClickListener {
            if (otherUser == null) {
                val intent = Intent(this@UserProfile2, ProfileItemsList::class.java)
                intent.putExtra("button", "completedLecBtn")
                startActivity(intent)
            } else {
                val snackbar = Snackbar.make(
                    findViewById(R.id.profileRoot),
                    "Can't access user's completed lectures!",
                    Snackbar.LENGTH_SHORT
                )
                snackbar.show()
            }
        }
        //fix error
        if (firebaseUser != null) {
            val thisUser = FirebaseFirestore.getInstance()
                .collection("Users").document(firebaseUser!!.uid)

            val thisCoach = FirebaseFirestore.getInstance()
                .collection("Users").document(userID!!)

            addToLib!!.setOnClickListener {
                if ((addToLib!!.text.toString() == "Follow")) {
                    if (firebaseUser != null) {
                        val map1: MutableMap<String, Any> = java.util.HashMap()
                        map1["coachesCount"] = FieldValue.increment(1)
                        map1["userIDs"] = FieldValue.arrayUnion(userID)

                        val map2: MutableMap<String, Any> = java.util.HashMap()
                        map2["studentsCount"] = FieldValue.increment(1)
                        map2["userIDs"] = FieldValue.arrayUnion(firebaseUser!!.uid)

                        val coachesList = thisUser.collection("squad")
                        val studentsList = thisCoach.collection("squad")
                        coachesList
                            .whereLessThan("coachesCount", 50000).limit(1)
                            .get()
                            .addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
                                if (!queryDocumentSnapshots.isEmpty) {
                                    for (qds in queryDocumentSnapshots) {
                                        coachesList.document("coaches")
                                            .update(map1)
                                            .addOnSuccessListener {
                                                thisUser.update(
                                                    "coaches",
                                                    FieldValue.increment(1)
                                                )
//                                                        thisSchool?.showSnackBar("Done!")
                                            }
                                            .addOnFailureListener { }
                                    }
                                } else {
                                    coachesList
                                        .document("coaches").set(map1)
                                        .addOnSuccessListener {
                                            thisUser.update(
                                                "coaches",
                                                FieldValue.increment(1)
                                            )
                                            val snackbar = Snackbar.make(
                                                findViewById(R.id.profileRoot),
                                                "Done.",
                                                Snackbar.LENGTH_SHORT
                                            )
                                            snackbar.show()
                                            addToLib!!.text = "Unfollow"
                                        }
                                        .addOnFailureListener {
                                            val snackbar = Snackbar.make(
                                                findViewById(R.id.profileRoot),
                                                "Something went wrong..",
                                                Snackbar.LENGTH_SHORT
                                            )
                                            snackbar.show()                                        }
                                }
                            }
                            .addOnFailureListener {
                                val snackbar = Snackbar.make(
                                    findViewById(R.id.profileRoot),
                                    "Something went wrong..",
                                    Snackbar.LENGTH_SHORT
                                )
                                snackbar.show()
                            }

                        studentsList
                            .whereLessThan("studentsCount", 50000).limit(1)
                            .get()
                            .addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
                                if (!queryDocumentSnapshots.isEmpty) {
                                    for (qds in queryDocumentSnapshots) {
                                        studentsList.document("students")
                                            .update(map2)
                                            .addOnSuccessListener {
                                                thisCoach.update(
                                                    "students",
                                                    FieldValue.increment(1)
                                                )
                                                val snackbar = Snackbar.make(
                                                    findViewById(R.id.profileRoot),
                                                    "Done.",
                                                    Snackbar.LENGTH_SHORT
                                                )
                                                snackbar.show()
                                                addToLib!!.text = "Unfollow"
                                            }
                                            .addOnFailureListener {
                                                val snackbar = Snackbar.make(
                                                    findViewById(R.id.profileRoot),
                                                    "Something went wrong..",
                                                    Snackbar.LENGTH_SHORT
                                                )
                                                snackbar.show()                                            }
                                    }
                                } else {
                                    studentsList
                                        .document("students").set(map2)
                                        .addOnSuccessListener {
                                            thisCoach.update(
                                                "students",
                                                FieldValue.increment(1)
                                            )
                                            val snackbar = Snackbar.make(
                                                findViewById(R.id.profileRoot),
                                                "Done.",
                                                Snackbar.LENGTH_SHORT
                                            )
                                            snackbar.show()
                                            addToLib!!.text = "Unfollow"
                                        }
                                        .addOnFailureListener {
                                            val snackbar = Snackbar.make(
                                                findViewById(R.id.profileRoot),
                                                "Something went wrong..",
                                                Snackbar.LENGTH_SHORT
                                            )
                                            snackbar.show()                                        }
                                }
                            }
                            .addOnFailureListener {
                                val snackbar = Snackbar.make(
                                    findViewById(R.id.profileRoot),
                                    "Something went wrong..",
                                    Snackbar.LENGTH_SHORT
                                )
                                snackbar.show()                            }


                    } else {
                        val logInSignUp = LogInSignUp(
                            applicationContext
                        )
                        logInSignUp.show()
                    }
                } else {


                    val map1: MutableMap<String, Any> = HashMap()
                    map1["coachesCount"] = FieldValue.increment(-1)
                    map1["userIDs"] = FieldValue.arrayRemove(userID)

                    val map2: MutableMap<String, Any> = HashMap()
                    map2["studentsCount"] = FieldValue.increment(-1)
                    map2["userIDs"] = FieldValue.arrayRemove(firebaseUser!!.uid)

                    val coachesList = thisUser.collection("squad").document("coaches")
                    val studentsList = thisCoach.collection("squad").document("students")
                    coachesList.update(map1)
                        .addOnSuccessListener {
                            thisUser.update(
                                "coaches",
                                FieldValue.increment(-1)
                            )
                        }
                    studentsList.update(map2)
                        .addOnSuccessListener {
                            thisCoach.update(
                                "students",
                                FieldValue.increment(-1)
                            )
                            val snackbar = Snackbar.make(findViewById(R.id.profileRoot), "User unfollowed", Snackbar.LENGTH_SHORT)
                            snackbar.show()
                            addToLib!!.text = "Follow"
                        }


                }
            }
        }

        val toolBarLayout = findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout)
        toolBarLayout.title = title
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logOut -> {
                FirebaseAuth.getInstance().signOut()
                val schools = loadSchools(this@UserProfile2, "schools")
                for (s in schools) {
                    if (s.enrolled === java.lang.Boolean.TRUE) {
                        s.enrolled = java.lang.Boolean.FALSE
                    }
                }
                saveSchools(this@UserProfile2, "schools", schools)
                val snackbar = Snackbar.make(
                    findViewById(R.id.profileRoot),
                    "Come back soon!",
                    Snackbar.LENGTH_SHORT
                )
                snackbar.show()
                val count: CountDownTimer = object : CountDownTimer(1600, 1000) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        startActivity(Intent(this@UserProfile2, MainActivity::class.java))
                        finish()
                    }
                }
                count.start()
            }
            R.id.editProfile -> if (auth1!!.currentUser == null) {
                val logInSignUp =
                    LogInSignUp(this@UserProfile2)
                logInSignUp.show()
            } else {
                if(auth1!!.currentUser!!.uid != userID){
                    Toast.makeText(applicationContext, "Not your profile!", Toast.LENGTH_SHORT).show()
                }else{
                    val intent = Intent(this@UserProfile2, EditProfile::class.java)
                    val editUser = User(
                        alias, fullName, profileUrl, coverArt, institution,
                        school, course, about, registeredAs, studentsStr,
                        coachesStr, gender, cQ, phoneNumber, auth1!!.currentUser!!.uid
                    )
                    intent.putExtra("editUser", editUser)
                    intent.putStringArrayListExtra("publishedSch",
                        publishedSch as java.util.ArrayList<String>?
                    )
                    intent.putStringArrayListExtra("uploadedSch",
                        uploadedSch as java.util.ArrayList<String>?
                    )
                    startActivity(intent)
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun queryUser(userID: String?) {
        val user = users.document(userID!!)
        user.get()
            .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                if (documentSnapshot.exists()) {
                    alias = documentSnapshot.getString("alias")
                    fullName = documentSnapshot.getString("fullName")
                    coachesStr = documentSnapshot.get("coaches").toString()
                    studentsStr = documentSnapshot.get("students").toString()
                    cQ = documentSnapshot.get("cQ").toString()
                    registeredAs = documentSnapshot.get("registeredAs") as ArrayList<String>?
                    about = documentSnapshot.getString("about")
                    phoneNumber = documentSnapshot.getString("phoneNumber")
                    institution = documentSnapshot.getString("institution")
                    school = documentSnapshot.getString("school")
                    course = documentSnapshot.getString("course")
                    profileUrl = documentSnapshot.getString("profilePicture")
                    coverArt = documentSnapshot.getString("coverArt")
                    gender = documentSnapshot.getString("gender")
                    email = documentSnapshot.getString("email")
                    publishedSch = documentSnapshot.get("schoolsPublished") as List<String>?
                    uploadedSch = documentSnapshot.get("schoolsUploaded") as List<String>?
                    coaches!!.text = String.format("%s Coaches", coachesStr)
                    students!!.text = String.format("%s Students", studentsStr)
                    val uploadedVid: ArrayList<SelectedDoc> = ArrayList()
                    val lectures: DocumentReference =
                        FirebaseFirestore.getInstance().collection("Content")
                            .document("Lectures")
                    if (otherUser == null) {
                        if ((alias == null) || (institution == null) || (school == null) || (gender == null)) {
                            val intent =
                                Intent(applicationContext, EditProfile::class.java)
                            intent.putExtra(
                                "otherSignUp",
                                "You still are required to provide a little bit more information regarding your studies. " +
                                        "Otherwise, you will be signed out automatically."
                            )
                            startActivity(intent)
                        }
                    }
                    if (uploadedSch != null) {
                        for (s: String? in uploadedSch!!) {
                            lectures.collection((s)!!)
                                .whereEqualTo("uid", userID).limit(20)
                                .get()
                                .addOnCompleteListener { task: Task<QuerySnapshot> ->
                                    prg2!!.visibility = View.GONE
                                    if (task.isSuccessful) {
                                        prg2!!.visibility = View.GONE
                                        uploadedVid.addAll(
                                            task.result.toObjects(SelectedDoc::class.java)
                                        )
                                        val docAdt =
                                            CloudVideosAdapter(
                                                null,
                                                null,
                                                null,
                                                applicationContext,
                                                applicationContext,
                                                uploadedVid,
                                                null,
                                                null
                                            )
                                        docAdt.notifyDataSetChanged()
                                        userLecturesRV!!.adapter = docAdt
                                    } else {
                                        Snackbar.make(
                                            findViewById(R.id.rootLayout),
                                            "Something's up! This is our fault.",
                                            Snackbar.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        }
                    }
                    val publishedDocs: ArrayList<SelectedDoc> = ArrayList()
                    val documents: DocumentReference =
                        FirebaseFirestore.getInstance().collection("Content")
                            .document("Documents")
                    if (publishedSch != null) {
                        for (s: String? in publishedSch!!) {
                            documents.collection((s)!!)
                                .whereEqualTo("uid", userID).limit(20)
                                .get()
                                .addOnCompleteListener { task: Task<QuerySnapshot> ->
                                    prg1!!.visibility = View.GONE
                                    if (task.isSuccessful) {
                                        prg1!!.visibility = View.GONE
                                        publishedDocs.addAll(
                                            task.result.toObjects(SelectedDoc::class.java)
                                        )
                                        val docAdt =
                                            DocumentAdapter(
                                                null,
                                                applicationContext,
                                                this@UserProfile2,
                                                publishedDocs
                                            )
                                        docAdt.notifyDataSetChanged()
                                        publishedSchoolsRV!!.adapter = docAdt
                                    } else {
                                        Snackbar.make(
                                            findViewById(R.id.rootLayout),
                                            "Something's up! This is our fault.",
                                            Snackbar.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        }
                    }
                    val cloudMaps = ArrayList<BitMapTitle>()
                    FirebaseFirestore.getInstance().collection("Pictorials")
                        .whereEqualTo("uid", userID).get()
                        .addOnSuccessListener{ queryDocumentSnapshots ->
                            prg3!!.visibility = View.GONE
                            for (qds in queryDocumentSnapshots) {
                                try {
                                    val title = qds.getString("title")
                                    val thumbnail = qds.getString("thumbnail")
                                    val relevance = qds.getString("relevance")
                                    val pictures = qds.get("pictures") as ArrayList<String>
                                    val narrations = qds.get("narrations") as ArrayList<String>
                                    val descriptions = qds.get("descriptions") as ArrayList<String>
                                    val viewsCount = qds.get("viewCount").toString()
                                    val ratings: Double = qds.get("ratings").toString().toDouble()
                                    val ratersCount: Int = Integer.parseInt(qds.get("ratersCount").toString())
                                    val commentsCount = qds["commentsCount"].toString()
                                    val savesCount = qds["saveCount"].toString()
                                    val slideCount = qds["slideCount"].toString()
                                    val uid = qds["uid"].toString()
                                    val displayName = qds["displayName"].toString()

                                    val bt =
                                        BitMapTitle(
                                            title,
                                            relevance,
                                            thumbnail,
                                            pictures,
                                            narrations,
                                            descriptions,
                                            viewsCount,
                                            commentsCount,
                                            savesCount,
                                            slideCount,
                                            uid,
                                            displayName,
                                            ratersCount,
                                            ratings
                                        )
                                    cloudMaps.add(bt)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }

//                    for (i in picOrder){
//                        newPicOrder.add(pictures[i])
//                    }
//                    for (j in narOrder){
//                        newNarOrder.add(narrations[j])
//                    }


                            }

                        }
                        .addOnCompleteListener {
                            val cloudAdapter =
                                HomeBitmapsAdapter(
                                    cloudMaps,
                                    this@UserProfile2,
                                    parent
                                )
                            cloudAdapter.notifyDataSetChanged()
                            pictorialsRV!!.adapter = cloudAdapter

                        }
                        .addOnFailureListener {
                        }

                    coaches!!.setOnClickListener { v: View? ->
                        if (otherUser == null) {
                            val userProducts =
                                UserProducts(
                                    this@UserProfile2,
                                    "Coaches",
                                    firebaseUser,
                                    null
                                )
                            userProducts.show()
                        } else {
                            val userProducts1 =
                                UserProducts(
                                    this@UserProfile2,
                                    "Coaches",
                                    otherUser,
                                    null
                                )
                            userProducts1.show()
                        }
                    }
                    students!!.setOnClickListener { v: View? ->
                        if (otherUser == null) {
                            val userProducts =
                                UserProducts(
                                    this@UserProfile2,
                                    "Students",
                                    firebaseUser,
                                    null
                                )
                            userProducts.show()
                        } else {
                            val userProducts1 =
                                UserProducts(
                                    this@UserProfile2,
                                    "Students",
                                    otherUser,
                                    null
                                )
                            userProducts1.show()
                        }
                    }
                    creativeQuotient!!.text = about
                    if (profileUrl != null) {
                        Glide.with(applicationContext).load(profileUrl)
                            .placeholder(R.drawable.ic_hourglass_bottom_white)
                            .thumbnail(0.9.toFloat()).into((userImage)!!)
                    } else {
                        Glide.with(applicationContext).load(R.drawable.ic_account_circle)
                            .thumbnail(0.9.toFloat()).into((userImage)!!)
                    }
                    userImage!!.bringToFront()
                    userName!!.text = alias + "@ynstudios.com"
                    loadUserImage!!.visibility = View.GONE
                    newSchools0 = ArrayList()

//                        Schools nothingYet = new Schools("Nothing yet!", R.drawable.ic_add_black);
//                        if (publishedSch == null){
//                            newSchools0.add(nothingYet);
//                        }else {
//                            filter = new LectureFilter();
//                            for (Schools s : filter.setUpSchools(null)){
//                                if (publishedSch.contains(s.getSchoolName())){
//                                    newSchools0.add(s);
//                                }
////                            if (uploadedSch.contains(s.getSchoolName())){
////                                newSchools1.add(s);
////                            }
//                            }
//                        }
////                        if (uploadedSch.isEmpty()){
////                            newSchools1.add(nothingYet);
////                        }
//
//
//
//                        GridRecyclerAdapter publishedAdt = new GridRecyclerAdapter(getApplicationContext(),
//                                null, null, newSchools0, "UserProfile2", null);
//                        publishedAdt.notifyDataSetChanged();
//                        publishedSchoolsRV.setAdapter(publishedAdt);

//                        GridRecyclerAdapter uploadedAdt = new GridRecyclerAdapter(getApplicationContext(),
//                                null, null, newSchools0, "UserProfile2", null);
//                        uploadedAdt.notifyDataSetChanged();
//                        userLecturesRV.setAdapter(uploadedAdt);
                }
            }.addOnFailureListener { e: Exception? -> }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 83) {
            val intent = Intent(this@UserProfile2, CustomGallery::class.java)
            intent.putExtra("IntentSelector", "biggerPicture")
            startActivity(intent)
        }
        if (requestCode == 7) {
            val intent = Intent(this@UserProfile2, ProfileItemsList::class.java)
            intent.putExtra("button", "pendingLecBtn")
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(resultCode, resultCode, data)
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val uri1 = data.data
            cropRequest(uri1)
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val biggerPicture = BiggerPicture(
                    this@UserProfile2, parent, findViewById(R.id.profileRoot), firebaseUser,
                    result.uri, true
                )
                biggerPicture.show()
                Picasso.get().load(result.uri).centerCrop().fit().into(userImage)
            }
        }
    }

    private fun cropRequest(imageUri: Uri?) {
        CropImage.activity(imageUri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setMultiTouchEnabled(true)
            .start(this)
    }
}