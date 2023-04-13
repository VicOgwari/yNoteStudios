package com.midland.ynote.Activities

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.preference.PreferenceManager
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.midland.ynote.Adapters.TagsAdapter
import com.midland.ynote.Dialogs.LectureSubCategoryDialog
import com.midland.ynote.Dialogs.PublishApproval
import com.midland.ynote.MainActivity
import com.midland.ynote.R
import com.midland.ynote.Utilities.DocRetrieval.Companion.photoFlag
import com.midland.ynote.Utilities.DocRetrieval.Companion.photoFlag1
import com.midland.ynote.Utilities.FilingSystem.Companion.allTags
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

class Next : AppCompatActivity() {
    var addImage: ImageButton? = null
    var addImage1: ImageButton? = null
    var selectedImage: ImageView? = null
    var selectedImage1: ImageView? = null
    var selectA: TextView? = null
    var userName: TextView? = null
    var finishingUp: TextView? = null
    var philosophy: EditText? = null
    var unitCodeEditText: EditText? = null
    var docDetails: EditText? = null
    var signUp: Button? = null
    var relevance: Button? = null
    var uri: Uri? = null
    var uri1: Uri? = null
    var flag: String? = null
    var philosophyStr: String? = null
    var users: CollectionReference? = null
    var uploadTask: StorageTask<UploadTask.TaskSnapshot?>? = null
    var uploadTask1: StorageTask<UploadTask.TaskSnapshot>? = null
    var userProfileRef = FirebaseStorage.getInstance().getReference("UserProfiles")
    var userBanner = FirebaseStorage.getInstance().getReference("UserBanners")
    var nextRelView: RelativeLayout? = null
    var prg: ProgressDialog? = null
    var userMap: Map<String, Any> = HashMap()
    var userDetails: ArrayList<String>? = ArrayList()
    var docUri: String? = null
    var institution: String? = null
    var docMetaData: String? = null
    var coverArt: String? = null
    var profilePic: String? = null
    var semester: String? = null
    var lecCheck: CheckBox? = null
    var studCheck: CheckBox? = null
    var registeredAs = ArrayList<String>()
    var roleLY: LinearLayout? = null
    var docLy: LinearLayout? = null
    var selectB: RelativeLayout? = null
    var signUpRel: RelativeLayout? = null
    var result: CropImage.ActivityResult? = null
    var result1: CropImage.ActivityResult? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next)
        val intent = intent
        users = FirebaseFirestore.getInstance().collection("Users")
        flag = getIntent().getStringExtra("flag")
        addImage = findViewById(R.id.addImage)
        addImage1 = findViewById(R.id.addImage1)
        selectedImage = findViewById(R.id.selectedImage)
        selectedImage1 = findViewById(R.id.selectedImage1)
        selectA = findViewById(R.id.selectA)
        selectB = findViewById(R.id.selectB)
        userName = findViewById(R.id.userName)
        finishingUp = findViewById(R.id.finishingUp)
        philosophy = findViewById(R.id.philosophy)
        unitCodeEditText = findViewById(R.id.unitCodeEditText)
        docDetails = findViewById(R.id.docDetails)
        signUp = findViewById(R.id.signUp)
        relevance = findViewById(R.id.relevance)
        nextRelView = findViewById(R.id.nextRel)
        studCheck = findViewById(R.id.studCheck)
        lecCheck = findViewById(R.id.lecCheck)
        roleLY = findViewById(R.id.roleLY)
        docLy = findViewById(R.id.docLy)
        signUpRel = findViewById(R.id.signUpRel)
        studCheck!!.setOnClickListener(View.OnClickListener { v: View? ->
            if (!registeredAs.contains("Student")) {
                registeredAs.add("Student")
            }
        })
        relevance!!.setOnClickListener(View.OnClickListener { v: View? ->
            val tagsAdapter =
                TagsAdapter(this@Next, allTags)
            val ft = supportFragmentManager.beginTransaction()
            val prev = supportFragmentManager.findFragmentByTag("dialog")
            if (prev != null) {
                ft.remove(prev)
            }
            ft.addToBackStack(null)
            val dialogFragment: DialogFragment =
                LectureSubCategoryDialog(
                    this@Next,
                    tagsAdapter
                )
            dialogFragment.show(supportFragmentManager, "dialog")
        })
        lecCheck!!.setOnClickListener(View.OnClickListener { v: View? ->
            if (!registeredAs.contains("Lecturer")) {
                registeredAs.add("Lecturer")
            }
        })
        if (intent.getStringExtra("flag") == "newUser") {
            selectA!!.text = "Add a profile image & banner"
            userDetails = intent.getStringArrayListExtra("userDetails")
            userName!!.text = userDetails!![0]
        } else if (intent.getStringExtra("flag") == "newDocument") {
            val docStrings = intent.getStringArrayListExtra("docStrings")
            if (docStrings != null) {
                docMetaData = docStrings[0]
                institution = docStrings[1]
                docUri = docStrings[2]
                semester = docStrings[3]
            }
            finishingUp!!.text = "Finishing up: " + docMetaData!!.split("_-_".toRegex()).toTypedArray()[5]
            signUp!!.text = "Finish"
            signUpRel!!.visibility = View.INVISIBLE
            docLy!!.visibility = View.VISIBLE
        }
        addImage!!.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE)
        }
        addImage1!!.setOnClickListener { v: View? ->
            photoFlag1 = "addImage1"
            checkAndroidVersion()
        }
        signUp!!.setOnClickListener { v: View? ->
            philosophyStr = philosophy!!.text.toString().trim { it <= ' ' }
            if (flag == "newUser") {
                if (registeredAs.size == 0) {
                    Toast.makeText(applicationContext, "Select a role", Toast.LENGTH_SHORT).show()
                } else {
                    val addReply = AlertDialog.Builder(this@Next)
                    addReply.setTitle("Ads Notice")
                    addReply.setMessage(
                        "This is an agreement that lets you understand " +
                                "that signing up on this site will expose you to potentially annoying ads." +
                                " They are a necessary support in the maintenance and development of the platform"
                    )
                    addReply.setNegativeButton("dismiss") { dialog, _ -> dialog.dismiss() }
                    addReply.setPositiveButton("Proceed") { _, _ ->
                        prg = ProgressDialog(this@Next)
                        prg!!.setMessage("Signing you up...")
                        prg!!.show()

                        if (result != null) {
                            val ctm = System.currentTimeMillis().toString()
                            val profileRef = userProfileRef.child(
                                "$ctm." + getFileExtension(
                                    result!!.uri
                                )
                            )
                            uploadTask = profileRef.putFile(result!!.uri)
                                .addOnSuccessListener { _: UploadTask.TaskSnapshot? ->
                                    val uriTask =
                                        uploadTask!!.continueWithTask({ task: Task<UploadTask.TaskSnapshot?> ->
                                            if (!task.isSuccessful) {
                                                throw task.exception!!
                                            }
                                            profileRef.downloadUrl
                                        })
                                            .addOnCompleteListener { task: Task<Uri> ->
                                                if (task.isSuccessful) {
                                                    val downloadLink = task.result
                                                    registerUser(
                                                        userDetails!![0],
                                                        userDetails!![3],
                                                        userDetails!![5],
                                                        userDetails!![1],
                                                        userDetails!![4],
                                                        userDetails!![2],
                                                        userDetails!![7],
                                                        userDetails!![6],
                                                        userDetails!![8],
                                                        userDetails!![9],
                                                        downloadLink.toString(),
                                                        "",
                                                        philosophyStr!!,
                                                        registeredAs
                                                    )
                                                }
                                            }
                                }
                                .addOnFailureListener { e: Exception? -> }
                        } else if (result1 == null) {
                            registerUser(
                                userDetails!![0],
                                userDetails!![3],
                                userDetails!![5],
                                userDetails!![1],
                                userDetails!![4],
                                userDetails!![2],
                                userDetails!![7],
                                userDetails!![6],
                                userDetails!![8],
                                userDetails!![9],
                                null,
                                null,
                                philosophyStr!!,
                                registeredAs
                            )
                        }
                    }
                    addReply.show()

                }
            } else if (flag == "newDocument") {
                if (result == null) {
                    val publishApproval1 =
                        PublishApproval(
                            this@Next,
                            docMetaData,
                            institution,
                            semester,
                            Uri.parse(docUri),
                            null,
                            FirebaseAuth.getInstance().currentUser,
                            unitCodeEditText!!.text.toString().trim { it <= ' ' },
                            docDetails!!.text.toString().trim { it <= ' ' },
                            allTags
                        )
                    publishApproval1.show()
                } else {
                    val publishApproval =
                        PublishApproval(
                            this@Next,
                            docMetaData,
                            institution,
                            semester,
                            Uri.parse(docUri),
                            result!!.uri,
                            FirebaseAuth.getInstance().currentUser,
                            unitCodeEditText!!.text.toString().trim { it <= ' ' },
                            docDetails!!.text.toString().trim { it <= ' ' },
                            allTags
                        )
                    publishApproval.show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
                val profileUri = CropImage.getPickImageResultUri(this, data)
                if (CropImage.isReadExternalStoragePermissionsRequired(this, profileUri)) {
                    if (photoFlag == "addImage") {
                        uri = profileUri
                    } else if (photoFlag1 == "addImage1") {
                        uri1 = profileUri
                    } else {
                        Toast.makeText(this, "Something's up...", Toast.LENGTH_SHORT).show()
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
                    }
                } else {
                    cropRequest(profileUri)
                }
            }
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    assert(result != null)
                    Glide.with(this@Next).load(result!!.uri).thumbnail(0.9.toFloat())
                        .placeholder(R.color.colorAccent).into(selectedImage!!)


//                    if (DocRetrieval.Companion.getPhotoFlag().equals("addFlag")) {
//                        result = CropImage.getActivityResult(data);
//                        assert result != null;
//                        Toast.makeText(getApplicationContext(), "addFlag" + result.getUri(), Toast.LENGTH_SHORT).show();
//                        Glide.with(Next.this).load(result).thumbnail((float) 0.9)
//                                .placeholder(R.color.com_facebook_device_auth_text).into(selectedImage);
//                    } else if (DocRetrieval.Companion.getPhotoFlag1().equals("addFlag1")) {
//                        result1 = CropImage.getActivityResult(data);
//                        assert result1 != null;
//                        Toast.makeText(getApplicationContext(), "addFlag1" + result1.getUri(), Toast.LENGTH_SHORT).show();
//                        Glide.with(Next.this).load(result1).thumbnail((float) 0.9)
//                                .placeholder(R.color.com_facebook_device_auth_text).into(selectedImage1);
//                    }
                }
            }
        }
    }

    fun saveUid(c: Context?, s: String?): Boolean {
        val sp = PreferenceManager.getDefaultSharedPreferences(c)
        val mEdits = sp.edit()
        mEdits.putString("userID", s)
        return mEdits.commit()
    }

    private fun checkAndroidVersion() {
        try {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        parent,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                    && ActivityCompat.shouldShowRequestPermissionRationale(
                        parent,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ) {
                    Snackbar.make(
                        findViewById(R.id.homeRel),
                        "Permission",
                        Snackbar.LENGTH_INDEFINITE
                    )
                        .setAction("ENABLE") { v: View? ->
                            ActivityCompat.requestPermissions(
                                parent, arrayOf(
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                                ), 8
                            )
                        }.show()
                } else {
                    ActivityCompat.requestPermissions(
                        parent, arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ), 8
                    )
                }
            } else {
                pickImage()
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            pickImage()
        }
    }

    private fun pickImage() {
        CropImage.startPickImageActivity(this)
    }

    private fun cropRequest(imageUri: Uri) {
        CropImage.activity(imageUri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setMultiTouchEnabled(true)
            .start(this)
    }

    private fun registerUser(
        fullName: String, eMail: String,
        passWord: String, alias: String, phoneNumber: String,
        gender: String, school: String, institution: String, course: String, fcmToken: String,
        profilePic: String?, coverArt: String?, philosophy: String, registeredAs: ArrayList<String>
    ) {
        val auth1 = FirebaseAuth.getInstance()
        users = FirebaseFirestore.getInstance().collection("Users")
        auth1.createUserWithEmailAndPassword("$alias@ynStudios.com", passWord)
            .addOnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    val searchableUser = (alias.replace("@ynStudios.com", "") + " "
                            + fullName)

                    val firebaseUser = auth1.currentUser
                    val userID = firebaseUser!!.uid
                    val profileUpdate =
                        UserProfileChangeRequest.Builder().setDisplayName(fullName).build()
                    firebaseUser.updateProfile(profileUpdate)
                    firebaseUser.updateEmail(alias)
                    val userMap: MutableMap<String, Any?> = HashMap()
                    userMap["userID"] = userID
                    userMap["alias"] = alias
                    userMap["fullName"] = fullName
                    userMap["about"] = philosophy
                    userMap["gender"] = gender
                    userMap["registeredAs"] = registeredAs
                    userMap["email"] = eMail
                    userMap["phoneNumber"] = phoneNumber
                    userMap["password"] = passWord
                    userMap["profilePicture"] = profilePic
                    userMap["coverArt"] = coverArt
                    userMap["institution"] = institution
                    userMap["school"] = school
                    userMap["course"] = course
                    userMap["account"] = "Active"
                    userMap["birthday"] = null
                    userMap["coaches"] = "0"
                    userMap["students"] = "0"
                    userMap["cQ"] = "0.000"
                    userMap["fcmToken"] = fcmToken
                    userMap["sandBox"] = null
                    userMap["search_keyword"] = null
                    userMap["schoolsPublished"] = null
                    userMap["schoolsUploaded"] = null
                    userMap["private"] = false
                    userMap["status"] = "active"
                    userMap["timestamp"] = System.currentTimeMillis()
                    users!!.document(userID).set(userMap).addOnSuccessListener {
                        val schools: MutableMap<String, List<String>?> = HashMap()
                        schools["schools"] = null
                        users!!.document(userID).collection("SchoolsPublished")
                            .document("Documents").set(schools)
                        users!!.document(userID).collection("SchoolsPublished")
                            .document("Lectures").set(schools)

                        val userCount = FirebaseFirestore.getInstance().collection("Count")
                            .document("Enrollment count")
                        val map: MutableMap<String, Any> = java.util.HashMap()
                        map[school] = FieldValue.increment(1)
                        userCount.update(map)
                        if (task.isSuccessful) {
                            prg!!.dismiss()
                            Snackbar.make(nextRelView!!, "Welcome!", Snackbar.LENGTH_SHORT).show()
                            saveUid(this@Next, userID)
                            val count: CountDownTimer = object : CountDownTimer(1000, 3000) {
                                override fun onTick(millisUntilFinished: Long) {}
                                override fun onFinish() {
                                    startActivity(
                                        Intent(
                                            applicationContext,
                                            MainActivity::class.java
                                        )
                                    )
                                }
                            }
                            count.start()
                        }
                    }
                } else {
                    prg!!.dismiss()
                    Toast.makeText(this@Next, task.exception!!.message, Toast.LENGTH_SHORT).show()
                    Toast.makeText(this@Next, alias, Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
            }
    }

    val fcmToken: String?
        get() {
            val fcmToken = arrayOfNulls<String>(1)
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task: Task<String?> ->
                if (!task.isSuccessful) {
                    Toast.makeText(this, "FCM's missing...", Toast.LENGTH_SHORT).show()
                } else {
                    fcmToken[0] = task.result
                }
            }.addOnFailureListener { e: Exception ->
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                Toast.makeText(this, "FCM2's missing...", Toast.LENGTH_SHORT).show()
            }
            return fcmToken[0]
        }

    private fun getFileExtension(uri: Uri): String? {
        val cR = applicationContext.contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }
}