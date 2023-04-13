package com.midland.ynote.Activities

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.midland.ynote.Adapters.*
import com.midland.ynote.Dialogs.LogInSignUp
import com.midland.ynote.MainActivity
import com.midland.ynote.Objects.*
import com.midland.ynote.Objects.Schools
import com.midland.ynote.R
import com.midland.ynote.Utilities.DocSorting
import com.midland.ynote.Utilities.FilingSystem
import com.midland.ynote.Utilities.FilingSystem.Companion.checkPermission
import com.midland.ynote.Utilities.FilingSystem.Companion.dubDocuments
import com.midland.ynote.Utilities.FilingSystem.Companion.isExternalStorageReadWritable
import com.midland.ynote.Utilities.Projector
import com.ortiz.touchview.TouchImageView
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class PhotoDoc : AppCompatActivity() {


    private val SELECT_AUD_REQUEST: Int = 999
    var mediaRecorder: MediaRecorder? = null
    var mediaPlayer: MediaPlayer? = null
    var pathSave = ""
    private val REQUEST_PERMISSION_CODE = 1000
    var viewRel: CoordinatorLayout? = null
    var photoDocsRV: RecyclerView? = null
    var photosRV: RecyclerView? = null
    var addBtn: ImageButton? = null
    var outSource: ImageButton? = null
    var docComments: ImageView? = null
    var moreIdeas: TextView? = null
    var docCommentET: EditText? = null
    var imageView: ImageView? = null
    var addPic: ImageButton? = null
    var currentPhotoPath: String? = null
    var bottomSheet: CardView? = null
    var commentsSheet: CardView? = null
    var currentSit = ArrayList<BitMapTitle>()
    lateinit var fileName: String
    lateinit var commentsCount: String
    lateinit var narPath: String
    lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    lateinit var bottomSheetBehavior1: BottomSheetBehavior<*>
    var pstn: Int? = null
    private var docCommentRV: RecyclerView? = null
    var adt: SelectedItemsAdt? = null
    private var imageCommentRV: RecyclerView? = null
    private var PICK_IMAGE_INTENT = 99
    private var PICK_DOC_INTENT = 98
    lateinit var lin: LinearLayout
    lateinit var imageRel: RelativeLayout
    var flag = String()
    private var glowRel: RelativeLayout? = null
    var animatorSet: AnimatorSet? = null


    var photoUri: Uri? = null

    companion object {
        private const val REQUEST_TAKE_PHOTO = 1
        var photoDocsAdapter: PhotoDocsAdapter? = null
        var pictorialAdapter: PictorialAdapter? = null
        private lateinit var receivedUri: ArrayList<String>
        var photoDesc = java.util.ArrayList<String>()
        var images = java.util.ArrayList<String>()
        var photoDocUris = java.util.ArrayList<Uri>()
        var photoDocUrisStrings = java.util.ArrayList<String>()
        var pictorials = java.util.ArrayList<PictorialObject>()
        var pictorials2 = java.util.ArrayList<PictorialObject>()
        var schools = java.util.ArrayList<Schools>()
        var bitMapTitles = java.util.ArrayList<BitMapTitle>()
        var pictorials1 = java.util.ArrayList<PictorialObject>()
        var stringUris = java.util.ArrayList<String>()
        var oldDescr = java.util.ArrayList<String>()
        private var pictorialsArray = emptyArray<String?>()
        lateinit var comments1: ArrayList<CommentsObject>
        private var mediaUriList: ArrayList<String>? = null


        var fileName: String? = null
        var touchIV: TouchImageView? = null
        var savedPictorials: File = File(dubDocuments, "$fileName.yNote")


        fun savePictorials(con: Context, fileName1: String, pictures: ArrayList<PictorialObject>) {
            val sharedPreferences = con.getSharedPreferences(fileName1, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            val gson = Gson()
            val json = gson.toJson(pictures)
            editor.putString(fileName1, json)
            editor.apply()
        }

        fun saveSchools(con: Context, schoolFileName: String, schools: ArrayList<Schools>) {
            val sharedPreferences = con.getSharedPreferences(schoolFileName, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            val gson = Gson()
            val json = gson.toJson(schools)
            editor.putString(schoolFileName, json)
            editor.apply()
        }

        fun loadSchools(con: Context, schoolFileName: String): ArrayList<Schools> {
            val sharedPreferences = con.getSharedPreferences(schoolFileName, Context.MODE_PRIVATE)
            schools = if (sharedPreferences.contains(schoolFileName)) {
                val gson = Gson()
                val json = sharedPreferences.getString(schoolFileName, "")
                val type = object : TypeToken<ArrayList<Schools>>() {}.type
                gson.fromJson(json, type)!!
            } else {
                ArrayList()
            }
            return schools
        }

        fun savePictorials1(con: Context, fileName1: String, pictures: ArrayList<BitMapTitle>) {
            val sharedPreferences = con.getSharedPreferences(fileName1, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            val gson = Gson()
            val json = gson.toJson(pictures)
            editor.putString(fileName1, json)
            editor.apply()
        }

        fun loadPictorials(con: Context, fileName1: String): ArrayList<PictorialObject> {
            val sharedPreferences = con.getSharedPreferences(fileName1, Context.MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreferences.getString(fileName1, "")
            val type = object : TypeToken<ArrayList<PictorialObject>>() {}.type
            pictorials = try {
                gson.fromJson(json, type)!!
            } catch (e: NullPointerException) {
                e.printStackTrace()
                ArrayList()
            }
            return pictorials
        }


        fun loadPictorials1(con: Context, fileName1: String): ArrayList<BitMapTitle> {
            val sharedPreferences = con.getSharedPreferences(fileName1, Context.MODE_PRIVATE)
            if (sharedPreferences.contains(fileName1)) {
                val gson = Gson()
                val json = sharedPreferences.getString(fileName1, "")
                val type = object : TypeToken<ArrayList<BitMapTitle>>() {}.type
                bitMapTitles = gson.fromJson(json, type)!!
            }
            return bitMapTitles
        }


        fun editDesc(
            con: Context,
            fileName: String,
            pos: Int,
            description: String,
            pictorialAdapter: PictorialAdapter?,
            narrationPath: String?
        ) {
            val toBeEditedArray = loadPictorials(con, fileName)
            val toBeEdited = toBeEditedArray[pos]
            val oldDesc = toBeEdited.picDescription
            val edit = PictorialObject(
                toBeEdited.picture,
                description,
                narrationPath!!
            )
            toBeEditedArray[pos] = edit
            savePictorials(con, fileName, toBeEditedArray)
//            pictorialAdapter!!.notifyDataSetChanged()
//            val pictorialAdapter = PictorialAdapter(con, loadPictorials(con, fileName), fileName)
//            pictorialAdapter.notifyDataSetChanged()
        }

        fun deletePictorial(con: Context, fileName: String, pos: Int) {
            val toBeDeletedArray = loadPictorials(con, fileName)
            val toBeDeleted = toBeDeletedArray[pos]
            toBeDeletedArray.remove(toBeDeleted)
            savePictorials(con, fileName, toBeDeletedArray)

        }


        fun theFile(con: Context): File {
            if (isExternalStorageReadWritable() &&
                checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, con)
            ) {
                savedPictorials = File(dubDocuments, "$fileName.yNote")

            }
            return savedPictorials
        }


        fun writeFile(
            theFile: String?,
            uris: ArrayList<String>?,
            con: Context,
            flag: String,
            editedInput: Array<String?>
        ) {
            if (isExternalStorageReadWritable() &&
                checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, con)
            ) {
                savedPictorials = File(dubDocuments, "$theFile.yNote")

                if (flag == "descEdit") {

                    try {
                        val fos1 = FileOutputStream(savedPictorials)
                        for (e in editedInput) {
                            fos1.write(e!!.toByteArray())
                        }
                        fos1.close()
                        Toast.makeText(con, "Edits saved", Toast.LENGTH_SHORT).show()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                } else
                    if (flag == "newAddition") {

                        try {
                            val br1 = BufferedReader(FileReader(savedPictorials))
                            val line = br1.readLine()
                            val fos = FileOutputStream(savedPictorials)
                            if (line != null) {
                                fos.write(line.toByteArray())
                            }
                            for (i in uris!!) {
                                fos.write("${Uri.parse(i)}!desc_-_".toByteArray())
                            }
                            fos.close()

                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }

            } else {
                Toast.makeText(
                    con,
                    "Something happened. You might lose your points.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


        fun readFile(fileName: String?, con: Context): Array<String?> {
            var textArray = arrayOfNulls<String>(0)
            savedPictorials = File(dubDocuments, "$fileName.yNote")

            try {
                val br = BufferedReader(FileReader(savedPictorials))
                val line = br.readLine()
                textArray = line.split("_-_".toRegex()).toTypedArray()
                br.close()
            } catch (e: FileNotFoundException) {
                Toast.makeText(con, "File not found", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            } catch (e: IOException) {
                Toast.makeText(con, "Something's up...", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }


            return textArray
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_doc)
        //Update the security provider when the activity is created.

        val addComment = findViewById<Button>(R.id.addCommentBtn)
        val addCommentFab = findViewById<FloatingActionButton>(R.id.addComment)
        val addDocFab = findViewById<FloatingActionButton>(R.id.addDocument)
        val addPhotoFab = findViewById<FloatingActionButton>(R.id.addPhoto)


        viewRel = findViewById(R.id.photoDocRel)
        glowRel = findViewById(R.id.glowRel)
        addPic = findViewById(R.id.addPic)
        photoDocsRV = findViewById(R.id.photoDocsRV)
        photosRV = findViewById(R.id.photosRV)
        touchIV = findViewById(R.id.touchIV)
        docCommentET = findViewById(R.id.docCommentET)
        addBtn = findViewById(R.id.addBtn)
        outSource = findViewById(R.id.outSource)
        moreIdeas = findViewById(R.id.addToSlide)
        imageView = findViewById(R.id.imageView)
        docCommentRV = findViewById(R.id.docCommentsRV)
        lin = findViewById(R.id.lin)
        imageRel = findViewById(R.id.imageRel)
        docComments = findViewById(R.id.docComments)
        photoDocsRV!!.layoutManager =
            LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        photoDocUris = ArrayList()
        photoDesc = ArrayList()
        pictorials = ArrayList()
        pictorials1 = ArrayList()
        pictorials2 = ArrayList()
        images = ArrayList()
        fileName = ""

        glowRel!!.bringToFront()

        animatorSet = AnimatorSet()
        val fadeOut: android.animation.ObjectAnimator = android.animation.ObjectAnimator.ofFloat(glowRel, "alpha", 0.5f, 0.1f)
        fadeOut.duration = 900
        val fadeIn: android.animation.ObjectAnimator = android.animation.ObjectAnimator.ofFloat(glowRel, "alpha", 0.1f, 0.5f)
        fadeIn.duration = 900
        animatorSet!!.play(fadeIn).after(fadeOut)
        animatorSet!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                animatorSet!!.start()
                super.onAnimationEnd(animation)
            }
        })
        animatorSet!!.start()

//        val grid = GridRecyclerAdapter(
//            this@PhotoDoc, DocSorting.getFileObjects(applicationContext), null,
//            null, "moreIdeas", null)


        val grid = MoreIdeasAdt(
            addBtn!!,
            DocSorting.getFileObjects(applicationContext),
            "moreIdeas",
            this,
            images
        )
        photosRV!!.layoutManager = GridLayoutManager(this@PhotoDoc, 3)
        grid.notifyDataSetChanged()
        photosRV!!.adapter = grid
        photosRV!!.bringToFront()

        addBtn!!.setOnClickListener {
            reloadPictorials(images)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            bottomSheetBehavior1.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        outSource!!.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, PICK_IMAGE_INTENT)
        }

        docCommentRV!!.layoutManager = LinearLayoutManager(
            applicationContext,
            RecyclerView.VERTICAL, false
        )

        bottomSheet = findViewById(R.id.ideas_sheet)
        commentsSheet = findViewById(R.id.comments_sheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet!!)
        bottomSheetBehavior1 = BottomSheetBehavior.from(commentsSheet!!)

        moreIdeas!!.setOnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
                //Retrieve comments from db & place them on RV
            } else
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
        }

        imageCommentRV = findViewById(R.id.imageComment)
        imageCommentRV!!.layoutManager = LinearLayoutManager(
            applicationContext,
            LinearLayoutManager.HORIZONTAL,
            false
        )


        docComments!!.setOnClickListener {
            if (bottomSheetBehavior1.state == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED)
                //Retrieve comments from db & place them on RV
            } else
                if (bottomSheetBehavior1.state == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior1.state = BottomSheetBehavior.STATE_COLLAPSED
                }
        }

        addComment!!.setOnClickListener { v: View? ->
            if (docCommentET!!.text.toString().isEmpty()) {
                Toast.makeText(applicationContext, "Say something...", Toast.LENGTH_SHORT).show()
            } else {
                val pictorialsRef =
                    FirebaseFirestore.getInstance().collection("Pictorials").document(fileName)

                if (mediaUriList != null) {
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user == null) {
                        val logInSignUp =
                            LogInSignUp(this@PhotoDoc)
                        logInSignUp.show()
                    } else {
                        postComment(user, docCommentET!!, mediaUriList, flag, pictorialsRef)
                    }
                } else {
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user == null) {
                        val logInSignUp =
                            LogInSignUp(this@PhotoDoc)
                        logInSignUp.show()
                    } else {
                        postComment(user, docCommentET!!, null, flag, pictorialsRef)
                    }
                }

            }
        }
        addDocFab.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user == null) {
                val logInSignUp =
                    LogInSignUp(this@PhotoDoc)
                logInSignUp.show()
            } else {
                val intent1 = Intent()
                intent1.type = "document/*"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    intent1.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                }
                intent1.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(intent1, PICK_DOC_INTENT)

            }
        }
        addPhotoFab.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user == null) {
                val logInSignUp =
                    LogInSignUp(this@PhotoDoc)
                logInSignUp.show()
            } else {
                val intent = Intent()
                intent.type = "image/*"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                }
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(intent, PICK_IMAGE_INTENT)

            }
        }
        addCommentFab.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user == null) {
                val logInSignUp =
                    LogInSignUp(this@PhotoDoc)
                logInSignUp.show()
            } else {
//                val addCom = EnrollmentDialog(this@DocumentLoader, user,
//                    null, 1, school + "_-_" + title, null)
//                addCom.show()
                if (lin.visibility == View.GONE) {
                    lin.visibility = View.VISIBLE

                } else {
                    lin.visibility = View.GONE

                }

            }
        }


        val home = findViewById<ImageView>(R.id.home)
        val play = findViewById<ImageView>(R.id.play)
        val add = findViewById<ImageView>(R.id.add)


        if (intent.getStringExtra("newDoc") != null) {
            photoDocNameDialog()
        }
        if (intent.getStringArrayListExtra("pictures") != null) {
            add.visibility = View.GONE
            docComments!!.visibility = View.VISIBLE
            bottomSheet!!.visibility = View.GONE
            val cloudPictures = intent.getStringArrayListExtra("pictures")
            val narrations = intent.getStringArrayListExtra("narrations")
            val descriptions = intent.getStringArrayListExtra("descriptions")
            fileName = intent.getStringExtra("fileName").toString()
            commentsCount = intent.getStringExtra("commentsCount").toString()

            for (cloudPic in cloudPictures!!) {
                try {
                    val picObj = PictorialObject(
                        cloudPic,
                        descriptions!![cloudPictures.indexOf(cloudPic)],
                        narrations!![cloudPictures.indexOf(cloudPic)]
                    )

                    pictorials2.add(picObj)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            pictorialAdapter = PictorialAdapter(
                this@PhotoDoc, pictorials2,
                null, addPic, parent,
                this@PhotoDoc, null, "online", touchIV
            )
            pictorialAdapter!!.notifyDataSetChanged()
            photoDocsRV!!.adapter = pictorialAdapter

            comments
        }


        //FROM CUSTOM GALLERY
        if (intent.getStringExtra("bitmaps") == "gallery"
            && intent.getStringExtra("SelectedPic") != null
        ) {
            fileName = intent.getStringExtra("fileName")!!
            val uriString = intent.getStringExtra("SelectedPic")
            val obj = PictorialObject(
                uriString,
                "Add a description",
                ""
            )
            val sharedPreferences =
                this@PhotoDoc.getSharedPreferences(fileName, Context.MODE_PRIVATE)
            if (sharedPreferences.contains(fileName)) {
                pictorials1 = loadPictorials(this@PhotoDoc, fileName)
                pictorials1.add(obj)
                savePictorials(this@PhotoDoc, fileName, pictorials1)
            } else {
                pictorials.add(obj)
                savePictorials(this@PhotoDoc, fileName, pictorials)
            }
            pictorials = ArrayList()
            pictorials = loadPictorials(this@PhotoDoc, fileName)
            pictorialAdapter = PictorialAdapter(
                this@PhotoDoc, pictorials, fileName, addPic, parent,
                this@PhotoDoc, null, "offline", touchIV
            )

            photoDocsRV!!.adapter = pictorialAdapter
            pictorialAdapter!!.notifyDataSetChanged()

            setRecyclerViewItemTouchListener(photoDocsRV!!)


        } else
        //FROM LIBRARY FRAGMENT
            if (intent.getStringExtra("bitmaps") == "bitmaps" && intent.getStringExtra("SelectedPic") == null) {

                fileName = intent?.getStringExtra("docName").toString()

//            if (isExternalStorageReadWritable() &&
//                    checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, this@PhotoDoc)) {
//                savedPictorials = File(dubDocuments, "$fileName.yNote")
//            }
                //            for (String s : readFile(fileName)){
//                photoDocUris1.add(Uri.parse(s));
//            }
//            photoDocUrisStrings = intent.getStringArrayListExtra("photos")
//            photoDesc = intent.getStringArrayListExtra("photoDesc")
//            for (p in photoDocUrisStrings){
//                photoDocUris.add(Uri.parse(p))
//            }


                val sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE)
                if (sharedPreferences.contains(fileName)) {
                    val gson1 = Gson()
                    val json1 = sharedPreferences.getString(fileName, "")
                    val type1 = object : TypeToken<ArrayList<PictorialObject>>() {}.type
                    pictorials.clear()
                    pictorials = gson1.fromJson(json1, type1)!!
                    pictorialAdapter =
                        PictorialAdapter(
                            this@PhotoDoc, pictorials, fileName, addPic, this@PhotoDoc,
                            this@PhotoDoc, null, "offline", touchIV
                        )
                    setRecyclerViewItemTouchListener(photoDocsRV!!)

                    photoDocsRV!!.adapter = pictorialAdapter
                    pictorialAdapter!!.notifyDataSetChanged()
                } else {
                    pictorials.clear()
                    pictorialAdapter =
                        PictorialAdapter(
                            this@PhotoDoc, pictorials, fileName, addPic, this@PhotoDoc,
                            this@PhotoDoc, null, "offline", touchIV
                        )
                    photoDocsRV!!.adapter = pictorialAdapter
                    pictorialAdapter!!.notifyDataSetChanged()
                    Toast.makeText(applicationContext, "Empty!", Toast.LENGTH_SHORT).show()

                }
                photoDocsRV!!.adapter = pictorialAdapter

//            photoDocsAdapter = PhotoDocsAdapter(applicationContext, photoDocUris, photoDesc, fileName)
            } else {
                pictorials = ArrayList()
            }
//        fab!!.setOnClickListener {
//            dispatchTakePicIntent()
//        }

        add.setOnClickListener { v: View? ->
//            val intent = Intent(this@PhotoDoc, CustomGallery::class.java)
//            intent.putExtra("IntentSelector", "PhotoDocs")
//            intent.putExtra("fileName", fileName)
//            startActivity(intent)

            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
            } else
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
        }
        home.setOnClickListener { v: View? ->
            this@PhotoDoc.finish()
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }
        play.setOnClickListener { v: View? ->
            val intent = Intent(this@PhotoDoc, Projector::class.java)
            if (pictorials.size > 0) {
                intent.putExtra("photoDocs", pictorials)
                startActivity(intent)
            } else if (pictorials2.size > 0) {
                intent.putExtra("photoDocs", pictorials2)
                startActivity(intent)
            } else {
                Toast.makeText(this@PhotoDoc, "No items to project", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_TAKE_PHOTO -> {
                if (resultCode == Activity.RESULT_OK && data != null) {

                    if (requestCode == PICK_IMAGE_INTENT) {
                        mediaUriList = ArrayList()

                        if (
                            data.clipData == null) {
                            mediaUriList!!.add(data.data.toString())
                        } else {
                            for (i in 0 until data.clipData!!.itemCount) {
                                mediaUriList!!.add(data.clipData!!.getItemAt(i).uri.toString())
                            }
                        }
                        flag = "Images"
                        adt = SelectedItemsAdt(
                            applicationContext,
                            mediaUriList,
                            flag
                        )
                        adt!!.notifyDataSetChanged()
                        imageCommentRV!!.adapter = adt

                        if (lin.visibility == View.GONE) {
                            lin.visibility = View.VISIBLE
                            imageRel.visibility = View.VISIBLE
                        }
                    } else
                        if (requestCode == PICK_DOC_INTENT) {
                            mediaUriList = ArrayList()

                            if (data.clipData == null) {
                                mediaUriList!!.add(data.data.toString())
                            } else {
                                for (i in 0 until data.clipData!!.itemCount) {
                                    mediaUriList!!.add(data.clipData!!.getItemAt(i).uri.toString())
                                }
                            }
                            flag = "Docs"
                            adt = SelectedItemsAdt(
                                applicationContext,
                                mediaUriList,
                                flag
                            )
                            adt!!.notifyDataSetChanged()
                            imageCommentRV!!.adapter = adt

                            if (lin.visibility == View.GONE) {
                                lin.visibility = View.VISIBLE
                                imageRel.visibility = View.VISIBLE
                            }
                        } else {

                            val bitmap = data.extras!!["data"] as Bitmap?
                            photoDocsAdapter!!.notifyDataSetChanged()
                            galleryAddPic()

                        }
                }

            }

            SELECT_AUD_REQUEST -> {
                if (resultCode == RESULT_OK && data != null && data.data != null) {
                    val audUri = data.data
                    val audPath = FilingSystem.getRealPathFromURI(audUri!!, applicationContext)
                    editDesc(applicationContext, fileName, pstn!!, narPath, null, audPath)
                }

            }


        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.photo_doc_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.projector -> {
                val intent = Intent(this@PhotoDoc, Projector::class.java)
                if (pictorials.size > 0) {
                    intent.putExtra("photoDocs", pictorials)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@PhotoDoc, "No items to project", Toast.LENGTH_SHORT).show()
                }
            }
            else -> return false
        }
        return super.onOptionsItemSelected(item)
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = timeStamp + "yNote" + "_"
        val storageDir = dubDocuments
        val yNotDoc = File.createTempFile(
            imageFileName,  /*prefix*/
            ".jpg",  /*suffix*/
            storageDir /*directory*/
        )
        currentPhotoPath = yNotDoc.absolutePath
        return yNotDoc
    }

    private fun dispatchTakePicIntent() {
        val takePicIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //Ensuring there is a camera activity to handle this intent
        if (takePicIntent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            //Continue only after file is crated
            if (photoFile != null) {
                photoUri = if (Build.VERSION_CODES.N <= Build.VERSION.SDK_INT) {
                    FileProvider.getUriForFile(
                        this,
                        "com.example.ynot.Activities.PhotoDoc", photoFile
                    )
                } else {
                    Uri.fromFile(photoFile)
                }
                takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(takePicIntent, REQUEST_TAKE_PHOTO)
            }
        }
    }

    //Refreshing gallery to include newly save photos
    private fun galleryAddPic() {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val f = File(currentPhotoPath)
        val contentUri = Uri.fromFile(f)
        mediaScanIntent.data = contentUri
        this.sendBroadcast(mediaScanIntent)
    }

    //Matching image with destination view
    private fun setView(imageView: ImageView) {
        //getting view dimensions
        val targetW = imageView.width
        val targetH = imageView.height

        //getting bitmap dimensions
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(currentPhotoPath)
        val photoW = bmOptions.outWidth
        val photoH = bmOptions.outHeight

        //determine scale down ratio
        val scaleFactor = 1.coerceAtLeast((photoW / targetW).coerceAtMost(photoH / targetH))

        //decode image to fit into view
        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor
        bmOptions.inPurgeable = true
        val bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions)
        imageView.setImageBitmap(bitmap)
    }


    private fun photoDocNameDialog() {
        val linearLayout = LinearLayout(this@PhotoDoc)
        linearLayout.orientation = LinearLayout.VERTICAL
        val lp = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lp.setMargins(50, 0, 50, 100)
        val input = EditText(this@PhotoDoc)
        input.layoutParams = lp
        input.gravity = Gravity.TOP or Gravity.START
        linearLayout.addView(input, lp)
        val fileNameDialog = AlertDialog.Builder(this@PhotoDoc)
        fileNameDialog.setTitle("Pictorial title")
        fileNameDialog.setMessage("e.g 'Critical Thinking by...'")
        fileNameDialog.setView(linearLayout)
        fileNameDialog.setNegativeButton("cancel") { dialog, which ->
            startActivity(
                Intent(
                    this@PhotoDoc,
                    Library::class.java
                )
            )
        }
        fileNameDialog.setPositiveButton("submit") { dialog, which ->
            if (input.text.toString().trim { it <= ' ' } == "") {
                dialog.dismiss()
                photoDocNameDialog()
            } else {
                fileName = input.text.toString().trim { it <= ' ' }
                if (isExternalStorageReadWritable && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    val sharedPreferences =
                        getSharedPreferences("bitmapTitle", Context.MODE_PRIVATE)
                    currentSit = loadPictorials1(this@PhotoDoc, "bitmapTitle")
                    currentSit.add(BitMapTitle(fileName))

                    savePictorials1(this@PhotoDoc, "bitmapTitle", currentSit)

                }
            }
        }
        fileNameDialog.setOnDismissListener { dialog ->
            if (input.text.toString().trim { it <= ' ' } == "") {
                photoDocNameDialog()
            } else {
                fileName = input.text.toString().trim { it <= ' ' }
                if (isExternalStorageReadWritable && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    val savedLecture = File(dubDocuments, "$fileName.yNote")
                    try {
                        val fos = FileOutputStream(savedLecture)
                        fos.close()
                        Toast.makeText(
                            applicationContext,
                            "New photoDoc created!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    dialog.dismiss()
                }
            }
        }
        fileNameDialog.setOnCancelListener { dialog ->
            startActivity(Intent(this@PhotoDoc, Library::class.java))
        }
        fileNameDialog.show()
    }

    private val isExternalStorageReadWritable: Boolean
        get() = if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            Log.i("State", "writable")
            true
        } else {
            false
        }

    private fun checkPermission(permission: String?): Boolean {
        val check = ContextCompat.checkSelfPermission(applicationContext, permission!!)
        return check == PackageManager.PERMISSION_GRANTED
    }

    override fun onBackPressed() {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            bottomSheetBehavior1.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        else
            if (touchIV!!.visibility == View.VISIBLE) {
                touchIV!!.visibility = View.GONE
            } else {
                super.onBackPressed()
                finish()
            }
    }

    override fun onDestroy() {
        this.finish()
        super.onDestroy()
    }

    override fun onDetachedFromWindow() {
        this.finish()
        super.onDetachedFromWindow()
    }

    //Audio recording functions
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO
            ), REQUEST_PERMISSION_CODE
        )
    }

    private fun checkPermissionFromDevice(): Boolean {
        val writeExternalStorageResult =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val recordResult = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
        return writeExternalStorageResult == PackageManager.PERMISSION_GRANTED &&
                recordResult == PackageManager.PERMISSION_GRANTED
    }

    private fun initRecorder() {

        if (checkPermissionFromDevice()) {

            pathSave =
                Environment.getExternalStorageDirectory().absolutePath + "/" + UUID.randomUUID()
                    .toString() + "_yNote_sounds.3gp"
            setUpMediaRecorder()
            try {
                mediaRecorder!!.prepare()
                mediaRecorder!!.start()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            Toast.makeText(this@PhotoDoc, "Recording...", Toast.LENGTH_SHORT).show()
        } else {
            requestPermission()
        }
    }

    private fun playMedia() {
        mediaPlayer = MediaPlayer()
        try {
            mediaPlayer!!.setDataSource(pathSave)
            mediaPlayer!!.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        mediaPlayer!!.start()
    }

    private fun stopMedia() {
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            setUpMediaRecorder()
        }
    }

    private fun setUpMediaRecorder() {
        mediaRecorder = MediaRecorder()
        mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder!!.setOutputFile(pathSave)
    }

    fun outSource(con: Context?, pos: Int, narrationPath: String) {
        pstn = pos
        narPath = narrationPath
        if (ContextCompat.checkSelfPermission(con!!, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                con,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    parent,
                    Manifest.permission.CAMERA
                )
                && ActivityCompat.shouldShowRequestPermissionRationale(
                    parent,
                    Manifest.permission.RECORD_AUDIO
                )
            ) {
                Snackbar.make(viewRel!!, "Permission", Snackbar.LENGTH_INDEFINITE)
                    .setAction("ENABLE") {
                        ActivityCompat.requestPermissions(
                            parent, arrayOf(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ), SELECT_AUD_REQUEST
                        )
                    }.show()
            } else {
                ActivityCompat.requestPermissions(
                    parent, arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ), SELECT_AUD_REQUEST
                )
            }
        } else {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "audio/*" //THE FILE CHOOSER WILL ONLY  SEE AUDIOS
            this.startActivityForResult(intent, SELECT_AUD_REQUEST)
        }
    }

    private fun getFileObjects(): java.util.ArrayList<ImageObject?>? {
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        )
        var c: Cursor? = null
        val dirList: SortedSet<String> = TreeSet()
        val imageObjects = java.util.ArrayList<ImageObject?>()
        var directories: Array<String?>? = null
        var orderBy: String? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            orderBy = MediaStore.Video.Media.DATE_TAKEN
        }
        if (uri != null) {
//            c = managedQuery(uri, projection, null, null, orderBy + " DESC");
            c = applicationContext.contentResolver.query(
                uri, projection, null, null,
                "$orderBy DESC"
            )
        }
        if (c != null && c.moveToFirst()) {
            do {
                var tempDir = c.getString(0)
                tempDir = tempDir.substring(0, tempDir.lastIndexOf("/"))
                try {
                    dirList.add(tempDir)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } while (c.moveToNext())
            directories = arrayOfNulls(dirList.size)
//            dirList.toArray<String>(directories)
            directories = dirList.toTypedArray()

        }
        for (i in dirList.indices) {
            val imageDir = File(directories!![i])
            var imageList = imageDir.listFiles()
            if (imageList != null) {
                for (imagePath in imageList!!) {
                    try {
                        if (imagePath.isDirectory) {
                            imageList = imagePath.listFiles()
                        }
                        if (imagePath.name.contains(".jpg") || imagePath.name.contains(".JPG") ||
                            imagePath.name.contains(".jpeg") || imagePath.name.contains(".JPEG") ||
                            imagePath.name.contains(".png") || imagePath.name.contains(".PNG") ||
                            imagePath.name.contains(".gif") || imagePath.name.contains(".GIF") ||
                            imagePath.name.contains(".bpm") || imagePath.name.contains(".BPM")
                        ) {
                            val imageFile = imagePath.absoluteFile
                            val imageObject =
                                ImageObject(
                                    imageFile.name,
                                    Uri.fromFile(imageFile)
                                )
                            imageObjects.add(imageObject)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } else {
                Toast.makeText(applicationContext, "Gallery empty!", Toast.LENGTH_SHORT).show()
            }
        }
        imageObjects.reverse()
        return imageObjects
    }

    private fun reloadPictorials(images: ArrayList<String>) {
        val sharedPreferences = this@PhotoDoc.getSharedPreferences(fileName, Context.MODE_PRIVATE)

        for (image in images) {
            val obj = PictorialObject(
                image,
                "Add a description",
                ""
            )
            if (sharedPreferences.contains(fileName)) {
                pictorials1 = loadPictorials(this@PhotoDoc, fileName)
                pictorials1.add(obj)
                savePictorials(this@PhotoDoc, fileName, pictorials1)
            } else {
                pictorials.add(obj)
                savePictorials(this@PhotoDoc, fileName, pictorials)
            }
        }
        pictorials = ArrayList()
        pictorials = loadPictorials(this@PhotoDoc, fileName)
        pictorialAdapter = PictorialAdapter(
            this@PhotoDoc, pictorials, fileName, addPic, parent,
            this@PhotoDoc, null, "offline", touchIV
        )

        photoDocsRV!!.adapter = pictorialAdapter
        pictorialAdapter!!.notifyDataSetChanged()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        images.clear()

        val callback: ItemTouchHelper.Callback =
            com.midland.ynote.Utilities.ItemTouchHelper(
                pictorialAdapter,
                this@PhotoDoc
            )
        val itemTouch = ItemTouchHelper(callback)
        pictorialAdapter!!.setTouchHelper(itemTouch)
        itemTouch.attachToRecyclerView(photoDocsRV)
    }

    private fun setRecyclerViewItemTouchListener(recyclerView: RecyclerView) {

        //1
        val itemTouchCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                viewHolder1: RecyclerView.ViewHolder
            ): Boolean {
                //2
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                //3
                val position = viewHolder.adapterPosition
                removePictorial(position, this@PhotoDoc)
                recyclerView.adapter!!.notifyItemRemoved(position)
            }
        }

        //4
        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    fun removePictorial(objPos: Int, con: PhotoDoc) {
        val sharedPreferences = con.getSharedPreferences(fileName, Context.MODE_PRIVATE)

        if (sharedPreferences.contains(fileName)) {
            pictorials1 = loadPictorials(con, fileName)
            pictorials1.removeAt(objPos)
            savePictorials(con, fileName, pictorials1)
        } else {
            Toast.makeText(con, "Something's not right..", Toast.LENGTH_SHORT).show()
            savePictorials(con, fileName, pictorials)
        }

        pictorials = ArrayList()
        pictorials = loadPictorials(con, fileName)
        pictorialAdapter = PictorialAdapter(
            con, pictorials, fileName, addPic, parent,
            con as PhotoDoc?, null, "offline", touchIV
        )

        photoDocsRV!!.adapter = pictorialAdapter
        pictorialAdapter!!.notifyDataSetChanged()
        setRecyclerViewItemTouchListener(photoDocsRV!!)

//        val callback: ItemTouchHelper.Callback = com.example.ynote.Utilities.ItemTouchHelper(pictorialAdapter, this@PhotoDoc)
//        val itemTouch = ItemTouchHelper(callback)
//        pictorialAdapter!!.setTouchHelper(itemTouch)
//        itemTouch.attachToRecyclerView(photoDocsRV)
    }

    private val comments: Unit
        get() {
            val justASec = findViewById<TextView>(R.id.justASec)

            val commentsRef =
                FirebaseFirestore.getInstance().collection("Pictorials").document(fileName)
                    .collection("Comments")


            comments1 = ArrayList()
            commentsRef.get().addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
                justASec.visibility = View.GONE
                for (qds in queryDocumentSnapshots) {
                    val comment = qds.getString("comment")
//                    val commentsObject = qds.toObject(CommentsObject::class.java)
                    comments1.add(
                        CommentsObject(
                            comment
                        )
                    )
                }
                comments1.reverse()
                val commentsAdapter = CommentsAdapter(this@PhotoDoc, comments1, null)
                commentsAdapter.notifyDataSetChanged()
                docCommentRV!!.adapter = commentsAdapter
                docCommentRV!!.bringToFront()
            }.addOnFailureListener { e: Exception? -> }
        }

    private fun postComment(
        user: FirebaseUser,
        docCommentET: EditText,
        mediaUriList: ArrayList<String>?,
        flag: String,
        pictorialsRef: DocumentReference
    ) {

        val mProgressDialog = ProgressDialog(this)
        mProgressDialog.setMessage("Adding your comment")
        mProgressDialog.setTitle("Please Wait...")
        mProgressDialog.isIndeterminate = true
        mProgressDialog.show()


        val commentsRef = pictorialsRef.collection("Comments")
        val comment = docCommentET.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(comment)) {
            Toast.makeText(applicationContext, "Say something....", Toast.LENGTH_SHORT).show()
        } else {

            val callForDate = Calendar.getInstance()
            val currentDate = SimpleDateFormat("dd-MM-yyyy")
            val saveCurrentDate = currentDate.format(callForDate.time)
            val callForTime = Calendar.getInstance()
            val currentTime = SimpleDateFormat("HH:mm")
            val saveCurrentTime = currentTime.format(callForTime.time)
            val uId = user.uid
            val userName = user.displayName
            val commentObj =
                CommentsObject(userName + "_-_" + uId + "_-_" + comment + "_-_" + saveCurrentDate + "_-_" + saveCurrentTime)
            comments1.add(commentObj)
            val pos = comments1.indexOf(commentObj)
            val commentsAdapter = CommentsAdapter(this@PhotoDoc, comments1, null)
            commentsAdapter.notifyDataSetChanged()
            docCommentRV!!.adapter = commentsAdapter
            docCommentRV!!.bringToFront()
            if (mediaUriList != null) {
                docCommentET.setText("")
            }

            if (mediaUriList == null) {

                commentsRef.document(System.currentTimeMillis().toString())
                    .set(commentObj)
                    .addOnSuccessListener { aVoid: Void? ->
                        val map: MutableMap<String, Any> = HashMap()
                        map["commentsCount"] = FieldValue.increment(1)
                        pictorialsRef.update(map)
                        mProgressDialog.dismiss()
                        val commentsAdapter1 = CommentsAdapter(this@PhotoDoc, comments1, null)
                        commentsAdapter1.notifyDataSetChanged()
                        docCommentRV!!.adapter = commentsAdapter1
                        docCommentRV!!.bringToFront()
                        Toast.makeText(applicationContext, "Comment posted!", Toast.LENGTH_SHORT)
                            .show()
                        FirebaseMessaging.getInstance().subscribeToTopic("VictorFocus")
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Subscribed!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        applicationContext,
                                        "Subscription failed...",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
                    .addOnFailureListener { e: Exception? ->
                        mProgressDialog.dismiss()
                        Toast.makeText(
                            applicationContext,
                            "Something's interrupting your post.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                var uploadTask: StorageTask<UploadTask.TaskSnapshot>? = null
                val uploadedList = ArrayList<String>()
                val uploadPos = ArrayList<Int>()

                for (s in mediaUriList) {
                    val fileReference1 = FirebaseStorage.getInstance().getReference("ImageComments")
                    uploadTask = fileReference1.putFile(Uri.parse(s))
                        .addOnSuccessListener {
                            uploadTask!!.continueWithTask { task ->
                                if (!task.isSuccessful) {
                                    throw task.exception!!
                                }
                                fileReference1.downloadUrl
                            }.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    //String userID = FirebaseAuth.getInstance().getUid();
                                    val downloadLink = task.result.toString()
                                    uploadedList.add(downloadLink)
                                    uploadPos.add(mediaUriList.indexOf(s))
                                    mediaUriList.remove(s)
                                    adt!!.notifyItemRemoved(mediaUriList.indexOf(s))
                                    Toast.makeText(
                                        applicationContext,
                                        "Uploaded!",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    if (mediaUriList.size == 0) {
                                        val commentObj =
                                            CommentsObject(
                                                userName + "_-_" +
                                                        uId + "_-_" + comment + "_-_" + saveCurrentDate + "_-_" +
                                                        saveCurrentTime, uploadedList, uploadPos
                                            )

                                        commentsRef.document(System.currentTimeMillis().toString())
                                            .set(commentObj)
                                            .addOnSuccessListener { aVoid: Void? ->
                                                val map: MutableMap<String, Any> = HashMap()
                                                map["commentsCount"] = FieldValue.increment(1)
                                                pictorialsRef.update(map)
                                                mProgressDialog.dismiss()
                                                val commentsAdapter1 =
                                                    CommentsAdapter(this@PhotoDoc, comments1, null)
                                                commentsAdapter1.notifyDataSetChanged()
                                                docCommentRV!!.adapter = commentsAdapter1
                                                docCommentRV!!.bringToFront()
                                                Toast.makeText(
                                                    applicationContext,
                                                    "Comment posted!",
                                                    Toast.LENGTH_SHORT
                                                )
                                                    .show()
                                                docCommentET.setText("")

                                                FirebaseMessaging.getInstance()
                                                    .subscribeToTopic("VictorFocus")
                                                    .addOnCompleteListener {
                                                        if (it.isSuccessful) {
                                                            Toast.makeText(
                                                                applicationContext,
                                                                "Subscribed!",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        } else {
                                                            Toast.makeText(
                                                                applicationContext,
                                                                "Subscription failed...",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                    }
                                            }

                                    }
                                }
                            }
                        }
                        .addOnFailureListener { e ->

                        }
                        .addOnProgressListener { taskSnapshot ->

                        }
                }
            }
        }

    }


}