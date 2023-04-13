package com.midland.ynote.Activities

import android.app.Application
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.webkit.WebView
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidstudy.daraja.Daraja
import com.androidstudy.daraja.DarajaListener
import com.androidstudy.daraja.model.AccessToken
import com.androidstudy.daraja.model.LNMExpress
import com.androidstudy.daraja.model.LNMResult
import com.androidstudy.daraja.util.Env
import com.androidstudy.daraja.util.TransactionType
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.*
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.Util
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
import com.midland.ynote.Adapters.CommentsAdapter
import com.midland.ynote.Adapters.MoreIdeasAdt
import com.midland.ynote.Adapters.SelectedItemsAdt
import com.midland.ynote.Dialogs.LogInSignUp
import com.midland.ynote.Objects.CommentsObject
import com.midland.ynote.Objects.SelectedDoc
import com.midland.ynote.R
import com.midland.ynote.Utilities.*
import com.midland.ynote.Utilities.AdMob.Companion.checkConnection
import com.midland.ynote.Utilities.Constants.BUSINESS_SHORT_CODE
import com.midland.ynote.stkPush.Utils
import com.ortiz.touchview.TouchImageView
import java.text.SimpleDateFormat
import java.util.*

class StreamingActivity : AppCompatActivity(), Player.EventListener {
    private lateinit var watchList: DocumentReference
    private lateinit var daraja: Daraja
    var images = ArrayList<String>()
    var photosRV: RecyclerView? = null
    var addBtn: ImageButton? = null
    var touchIV: TouchImageView? = null
    var outSource: ImageButton? = null
    private var exoPlayer: SimpleExoPlayer? = null
    private var playerView: PlayerView? = null
    private var flag = String()
    private var selectedVideo: String? = null
    private val ONE_MIN: Long = 60000
    private var timeInMillis: Long? = null
    private var timeLeft = ONE_MIN
    private var PICK_IMAGE_INTENT = 99
    private var PICK_DOC_INTENT = 98
    private var nodeName: String? = null
    private var generalDesc: String? = null
    private val streamVidName: TextView? = null
    private var loadProgress: ProgressBar? = null
    private val app: Application? = null
    private var closeDialog: ImageView? = null
    private var vidRating: ImageView? = null
    private var vidRatingTV: TextView? = null
    private var vidComments: ImageView? = null
    private var vidCommentsTV: TextView? = null
    var videos: ArrayList<SelectedDoc>? = ArrayList()
    private var ratingRel: RelativeLayout? = null
    private var submitButton: ImageButton? = null
    var ratingBar: RatingBar? = null
    var scrollTo = 0
    var adt: SelectedItemsAdt? = null
    private var publisherUid: String? = null
    private var commentsCount: String? = null
    private var ratings: String? = null
    private var saveCount: String? = null
    private lateinit var moreLikeIt: ImageView
    private lateinit var watchLater: ImageView
    private lateinit var pinCount: TextView
    private lateinit var bottomSheet: View
    private lateinit var bottomSheet1: View
    private lateinit var addComment: Button
    private lateinit var docCommentET: EditText
    private lateinit var addCommentFab: FloatingActionButton
    private lateinit var discardImage: ImageButton
    private lateinit var addDocFab: FloatingActionButton
    private lateinit var addPhotoFab: FloatingActionButton
    private var bottomSheetBehavior: BottomSheetBehavior<*>? = null
    private var bottomSheetBehavior1: BottomSheetBehavior<*>? = null
    lateinit var lectureDbRef: DocumentReference
    lateinit var raters: DocumentReference
    lateinit var comments1: ArrayList<CommentsObject>
    private var docCommentRV: RecyclerView? = null
    private var imageCommentRV: RecyclerView? = null
    private var readBuffer: ProgressBar? = null
    lateinit var lin: LinearLayout
    lateinit var imageRel: RelativeLayout
    private var countDownTimer: CountDownTimer? = null
    private var timerRunning = false
    var docViewer: WebView? = null
    val thisUser = FirebaseAuth.getInstance().currentUser


    //    public StreamingDialog(@NonNull Context context, Context c, Application app, SelectedDoc selectedVideo) {
    //        super(context);
    //        this.c = c;
    //        this.selectedVideo = selectedVideo;
    //        this.app = app;
    //    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.stream_dialog)
        initViewSnActions()
        initTheVid()
        hideSystemBars()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent!!.getStringExtra("notification") != null) {
            val lecComment = intent.getStringExtra("notification")
            var theComment = lecComment!!.split("+crucialData+")[0]
            val crucialData = lecComment.split("+crucialData+")[1]
            nodeName = crucialData.split("_-_")[0]
            publisherUid = crucialData.split("_-_")[1]
            generalDesc = crucialData.split("_-_")[2]
            commentsCount = crucialData.split("_-_")[3]
            ratings = crucialData.split("_-_")[4]
            saveCount = crucialData.split("_-_")[5]

            vidRatingTV!!.text = ratings
            vidCommentsTV!!.text = commentsCount
            lectureDbRef = FirebaseFirestore.getInstance()
                .collection("Content")
                .document("Lectures")
                .collection(generalDesc!!)
                .document(nodeName!!)
            raters = lectureDbRef.collection("RatersUid")
                .document()
            closeDialog!!.setOnClickListener { v: View? ->
                releasePlayer()
                onBackPressed()
            }
            moreLikeIt.setOnClickListener { v: View? ->
                val intent1 = Intent(applicationContext, SwipeUpActivity::class.java)
                intent1.putExtra("videos", videos)
                intent1.putExtra("nodeName", nodeName)
                startActivity(intent1)
            }
            vidRating!!.setOnClickListener {
                if (ratingRel!!.visibility == View.GONE) {
                    ratingRel!!.visibility = View.VISIBLE
                    ratingRel!!.bringToFront()
                } else {
                    ratingRel!!.visibility = View.GONE
                }
            }
            vidComments!!.setOnClickListener {
                if (bottomSheetBehavior!!.state == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior!!.setState(BottomSheetBehavior.STATE_EXPANDED)
                    //Retrieve comments from db & place them on RV
                } else
                    if (bottomSheetBehavior!!.state == BottomSheetBehavior.STATE_EXPANDED) {
                        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
            }
            addComment.setOnClickListener { v: View? ->
                if (docCommentET.text.toString().isEmpty()) {
                    Toast.makeText(applicationContext, "Say something...", Toast.LENGTH_SHORT)
                        .show()
                } else {

                    val user = FirebaseAuth.getInstance().currentUser
                    if (user == null) {
                        val logInSignUp =
                            LogInSignUp(this@StreamingActivity)
                        logInSignUp.show()
                    } else {
                        val crucialData =
                            nodeName + "_-_" + publisherUid + "_-_" + generalDesc + "_-_" + commentsCount + "_-_" + ratings + "_-_" + saveCount
                        postComment(user, docCommentET, images, crucialData)
                    }

                }
            }
            addCommentFab.setOnClickListener {
                val user = FirebaseAuth.getInstance().currentUser
                if (user == null) {
                    val logInSignUp =
                        LogInSignUp(this@StreamingActivity)
                    logInSignUp.show()
                } else {
//                val addCom = EnrollmentDialog(this@StreamingActivity, user,
//                    null, 1, school + "_-_" + title, null)
//                addCom.show()
                    if (lin.visibility == View.GONE) {
                        lin.visibility = View.VISIBLE

                    } else {
                        lin.visibility = View.GONE

                    }

                }
            }
            addDocFab.setOnClickListener {
                val user = FirebaseAuth.getInstance().currentUser
                if (user == null) {
                    val logInSignUp =
                        LogInSignUp(this@StreamingActivity)
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

            discardImage.setOnClickListener {
                if (imageRel.visibility == View.VISIBLE) {
                    imageRel.visibility = View.GONE
                }
            }

            comments
        }

    }

    private fun initTheVid() {
        val netDetails = checkConnection(applicationContext)
        if (netDetails != null) {
            if (netDetails[0] == java.lang.Boolean.TRUE) {
                if (intent.getSerializableExtra("cloudVideo") != null) {
                    selectedVideo = intent.getStringExtra("cloudVideo")
                }
                try {
                    val retriever = MediaMetadataRetriever()
                    retriever.setDataSource(selectedVideo, HashMap())
                    val time =
                        retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                    timeInMillis = time!!.toLong()

                    retriever.release()
                    runOnUiThread {

                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }

                setExoPlayer(application, selectedVideo)
                if (selectedVideo != null) {
                    setUp(Uri.parse(selectedVideo))
                } else {
                    Toast.makeText(applicationContext, "Null vid", Toast.LENGTH_SHORT).show()
                }
            } else {
                Snackbar.make(
                    findViewById(R.id.coordStrm),
                    "Check your network connection..",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("Done") {
                    initTheVid()
                }.show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemBars()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_INTENT) {
                images = ArrayList()

                if (
                    data!!.clipData == null) {
                    images.add(data.data.toString())
                } else {
                    for (i in 0 until data.clipData!!.itemCount) {
                        images.add(data.clipData!!.getItemAt(i).uri.toString())
                    }
                }
                flag = "Images"
                adt = SelectedItemsAdt(
                    applicationContext,
                    images,
                    flag
                )
                adt!!.notifyDataSetChanged()
                imageCommentRV!!.adapter = adt

                lin.visibility = View.VISIBLE
                imageRel.visibility = View.VISIBLE
            }

            if (requestCode == PICK_DOC_INTENT) {
                images = ArrayList()

                if (data!!.clipData == null) {
                    images.add(data.data.toString())
                } else {
                    for (i in 0 until data.clipData!!.itemCount) {
                        images.add(data.clipData!!.getItemAt(i).uri.toString())
                    }
                }
                flag = "Docs"
                adt = SelectedItemsAdt(
                    applicationContext,
                    images,
                    flag
                )
                adt!!.notifyDataSetChanged()
                imageCommentRV!!.adapter = adt

                lin.visibility = View.VISIBLE
                imageRel.visibility = View.VISIBLE
            }
        }

    }

    override fun onBackPressed() {
        if (touchIV != null) {
            if (touchIV!!.visibility == View.VISIBLE) {
                touchIV!!.visibility = View.GONE
            }
        }

        if (bottomSheetBehavior != null) {
            if (bottomSheetBehavior!!.state == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
                bottomSheetBehavior1!!.state = BottomSheetBehavior.STATE_COLLAPSED
            } else
                if (ratingRel!!.visibility == View.VISIBLE) {
                    ratingRel!!.visibility = View.GONE
                } else
                    if (countDownTimer != null) {
                        countDownTimer!!.cancel()
                        countDownTimer = null
                    }else {
                        super.onBackPressed()
                    }
        } else {
            super.onBackPressed()
        }

    }


    private fun initViewSnActions() {
//        streamVidName = findViewById(com.example.ynote.R.id.streamVidName);
        AdMob.runRewardAd(getApplicationContext(), this, "");
        photosRV = findViewById(R.id.photosRV)
        addBtn = findViewById(R.id.addBtn)
        touchIV = findViewById(R.id.touchIV)
        outSource = findViewById(R.id.outSource)
        loadProgress = findViewById(R.id.loadProgress)
        playerView = findViewById(R.id.streamExoPlayer)
        closeDialog = findViewById(R.id.imageViewExit)
        imageRel = findViewById(R.id.imageRel)
        lin = findViewById(R.id.lin)
        moreLikeIt = findViewById(R.id.moreLikeIt)
        watchLater = findViewById(R.id.watchLater)
        pinCount = findViewById(R.id.pinCount)
        bottomSheet = findViewById(R.id.comments_sheet)
        bottomSheet1 = findViewById(R.id.ideas_sheet)
        addComment = findViewById(R.id.addCommentBtn)
        docCommentET = findViewById(R.id.docCommentET)
        addCommentFab = findViewById(R.id.addComment)
        discardImage = findViewById(R.id.discardImage)
        addDocFab = findViewById(R.id.addDocument)
        addPhotoFab = findViewById(R.id.addPhoto)
        docCommentRV = findViewById(R.id.docCommentsRV)
        docCommentRV!!.layoutManager = LinearLayoutManager(
            applicationContext,
            RecyclerView.VERTICAL, false
        )
        imageCommentRV = findViewById(R.id.imageComment)
        imageCommentRV!!.layoutManager = LinearLayoutManager(
            applicationContext,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        vidRating = findViewById(R.id.vidRatings)
        vidRatingTV = findViewById(R.id.vidRatingTextView)
        readBuffer = findViewById(R.id.readBuffer)
        vidComments = findViewById(R.id.vidComments)
        vidCommentsTV = findViewById(R.id.vidCommentCountExo)
        //        openInActivity = findViewById(com.example.ynote.R.id.openInActivity);
        ratingRel = findViewById(R.id.ratingRel)
        ratingBar = findViewById(R.id.ratingBar)
        submitButton = findViewById(R.id.submitRate)

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet1)
        vidRating!!.bringToFront()
        vidComments!!.bringToFront()


        val grid = MoreIdeasAdt(
            addBtn!!,
            DocSorting.getFileObjects(applicationContext),
            "moreIdeas",
            this,
            images
        )
        photosRV!!.layoutManager = GridLayoutManager(this@StreamingActivity, 3)
        grid.notifyDataSetChanged()
        photosRV!!.adapter = grid
        photosRV!!.bringToFront()

        addBtn!!.setOnClickListener {
            flag = "Images"
            adt = SelectedItemsAdt(
                applicationContext,
                MoreIdeasAdt.images1,
                flag
            )
            adt!!.notifyDataSetChanged()
            imageCommentRV!!.adapter = adt

            lin.visibility = View.VISIBLE
            imageRel.visibility = View.VISIBLE

            bottomSheetBehavior1!!.state = BottomSheetBehavior.STATE_COLLAPSED
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

        if (intent.getSerializableExtra("cloudVideo") != null) {
            nodeName = intent.getStringExtra("nodeName")
            publisherUid = intent.getStringExtra("publisherId")
            generalDesc = intent.getStringExtra("generalDesc")
            commentsCount = intent.getStringExtra("commentsCount")
            ratings = intent.getStringExtra("ratings")
            saveCount = intent.getStringExtra("saveCount")
            videos = intent.getSerializableExtra("videos") as ArrayList<SelectedDoc>?
            scrollTo = intent.getIntExtra("scrollTo", 0)


        } else
            if (intent.getStringExtra("notification") != null) {
                val lecComment = intent.getStringExtra("notification")
                var theComment = lecComment!!.split("+crucialData+")[0]
                val crucialData = lecComment.split("+crucialData+")[1]
                nodeName = crucialData.split("_-_")[0]
                publisherUid = crucialData.split("_-_")[1]
                generalDesc = crucialData.split("_-_")[2]
                commentsCount = crucialData.split("_-_")[3]
                ratings = crucialData.split("_-_")[4]
                saveCount = crucialData.split("_-_")[5]
            }

        vidRatingTV!!.text = ratings
        vidCommentsTV!!.text = commentsCount
        lectureDbRef = FirebaseFirestore.getInstance()
            .collection("Content")
            .document("Lectures")
            .collection(generalDesc!!)
            .document(nodeName!!)
        raters = lectureDbRef.collection("RatersUid")
            .document()
        if (thisUser != null) {
            watchList = FirebaseFirestore.getInstance().collection("Users")
                .document(thisUser.uid)
                .collection("ScheduledLists")
                .document("WatchList")

            watchList.get().addOnSuccessListener {
                if (it.exists()) {
                    val watchListArray = it.get("watchList") as ArrayList<String>
                    if (watchListArray.contains(generalDesc!! + "_-_" + nodeName!!)) {
                        watchLater.visibility = View.GONE
                    }
                }
            }
        }

        closeDialog!!.setOnClickListener { v: View? ->
            releasePlayer()
            onBackPressed()
        }
        moreLikeIt.setOnClickListener { v: View? ->
            val intent1 = Intent(applicationContext, SwipeUpActivity::class.java)
            intent1.putExtra("videos", videos)
            intent1.putExtra("nodeName", nodeName)
            startActivity(intent1)
        }
        vidRating!!.setOnClickListener {
            if (ratingRel!!.visibility == View.GONE) {
                ratingRel!!.visibility = View.VISIBLE
                ratingRel!!.bringToFront()
            } else {
                ratingRel!!.visibility = View.GONE
            }
        }
        vidComments!!.setOnClickListener {
            if (bottomSheetBehavior!!.state == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehavior!!.setState(BottomSheetBehavior.STATE_EXPANDED)
                //Retrieve comments from db & place them on RV
            } else
                if (bottomSheetBehavior!!.state == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
                }
        }
        addComment.setOnClickListener { v: View? ->
            if (docCommentET.text.toString().isEmpty()) {
                Toast.makeText(applicationContext, "Say something...", Toast.LENGTH_SHORT).show()
            } else {

                val user = FirebaseAuth.getInstance().currentUser
                if (user == null) {
                    val logInSignUp =
                        LogInSignUp(this@StreamingActivity)
                    logInSignUp.show()
                } else {
                    val crucialData =
                        nodeName + "_-_" + publisherUid + "_-_" + generalDesc + "_-_" + commentsCount + "_-_" + ratings + "_-_" + saveCount
                    postComment(user, docCommentET, MoreIdeasAdt.images1, crucialData)
                }

            }
        }
        addCommentFab.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user == null) {
                val logInSignUp =
                    LogInSignUp(this@StreamingActivity)
                logInSignUp.show()
            } else {
//                val addCom = EnrollmentDialog(this@StreamingActivity, user,
//                    null, 1, school + "_-_" + title, null)
//                addCom.show()
                if (lin.visibility == View.GONE) {
                    lin.visibility = View.VISIBLE

                } else {
                    lin.visibility = View.GONE

                }

            }
        }
        addDocFab.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user == null) {
                val logInSignUp =
                    LogInSignUp(this@StreamingActivity)
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
                    LogInSignUp(this@StreamingActivity)
                logInSignUp.show()
            } else {
                if (bottomSheetBehavior1!!.state == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior1!!.state = BottomSheetBehavior.STATE_EXPANDED
                    images = ArrayList()
                    //Retrieve comments from db & place them on RV
                }

            }


        }
        discardImage.setOnClickListener {
            if (imageRel.visibility == View.VISIBLE) {
                imageRel.visibility = View.GONE
            }
        }
        submitButton!!.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user == null) {
                val logInSignUp =
                    LogInSignUp(applicationContext)
                logInSignUp.show()
            } else {
                lectureDbRef.collection("RatersUid")
                    .whereArrayContains("uIds", user.uid)
                    .get()
                    .addOnSuccessListener {
                        if (it.isEmpty) {
                            val ratings: MutableMap<String, Any> = HashMap()
                            ratings["ratersCount"] = FieldValue.increment(1)
                            ratings["ratings"] = FieldValue.increment(ratingBar!!.rating.toDouble())
                            lectureDbRef.update(ratings).addOnSuccessListener {
                                val newId: MutableMap<String, Any> = HashMap()
                                newId["uIds"] = FieldValue.arrayUnion(user.uid)
                                newId["uIdsCount"] = FieldValue.increment(1)

                                lectureDbRef.collection("RatersUid")
                                    .whereLessThan("uIdsCount", 50000)
                                    .limit(1)
                                    .get()
                                    .addOnSuccessListener { it ->
                                        if (it.isEmpty) {
                                            raters.set(newId)
                                        } else {
                                            raters.update(newId)
                                        }
                                    }.addOnFailureListener {
                                        Toast.makeText(
                                            applicationContext,
                                            it.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                val s = ratingBar!!.rating.toString()
                                Toast.makeText(applicationContext, "$s stars", Toast.LENGTH_SHORT)
                                    .show()
                                SystemClock.sleep(1300)
                                ratingRel!!.visibility = View.GONE
                            }.addOnFailureListener { e: Exception? ->
                                Toast.makeText(
                                    applicationContext,
                                    "Something's up! Try again.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Can't rate twice!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
        watchLater.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user == null) {
                LogInSignUp(this@StreamingActivity).show()
            } else {
                if (Integer.parseInt(commentsCount!!) >= 20 || Integer.parseInt(saveCount!!) >= 20) {
                    val price =
                        if (Integer.parseInt(commentsCount!!) > Integer.parseInt(saveCount!!)) {
                            DocRetrieval.priceGenerator(Integer.parseInt(commentsCount!!))!!
                        } else {
                            DocRetrieval.priceGenerator(Integer.parseInt(saveCount!!))!!
                        }
                    val pinOnPurchase = AlertDialog.Builder(this@StreamingActivity)
                        .setTitle("Pin on purchase")
                        .setIcon(R.drawable.ic_shopping_cart)
                        .setMessage(
                            "This document is otherwise available on purchase only. Please authorize a ksh." +
                                    price + " support to the publisher"
                        )
                        .setPositiveButton("Proceed!") { _: DialogInterface?, _: Int ->
                            val preferences: SharedPreferences =
                                applicationContext.getSharedPreferences(
                                    "User",
                                    MODE_PRIVATE
                                )
                            val json = preferences.getString("User", "")
                            val gson = Gson()
                            val type = object :
                                TypeToken<ArrayList<String?>?>() {}.type
                            val userDetail =
                                gson.fromJson<ArrayList<String>>(json, type)

                            if (userDetail != null) {
                                val phoneNumber = userDetail[12]
                                val userPhoneNumber =
                                    AlertDialog.Builder(this@StreamingActivity)
                                        .setTitle("Mpesa No. $phoneNumber")
                                        .setIcon(R.drawable.ic_shopping_cart)
                                        .setMessage(
                                            "Ksh." +
                                                    price + " shall be credited from account number (" + phoneNumber + ") \n " +
                                                    "Tap on 'Other' to send the request to another account."
                                        )
                                        .setPositiveButton("Proceed!") { _: DialogInterface?, _: Int ->
                                            performSTKPush(
                                                phoneNumber,
                                                "1"
                                            )

                                        }
                                        .setNegativeButton("Other") { dialog: DialogInterface, which: Int ->
                                            dialog.dismiss()

                                            val linearLayout = LinearLayout(this@StreamingActivity)
                                            linearLayout.orientation = LinearLayout.VERTICAL
                                            val lp = LinearLayout.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT
                                            )
                                            lp.setMargins(50, 0, 50, 100)
                                            val input = EditText(this@StreamingActivity)
                                            input.layoutParams = lp
                                            input.gravity = Gravity.TOP or Gravity.START
                                            linearLayout.addView(input, lp)
                                            val otherNumberDialog =
                                                AlertDialog.Builder(this@StreamingActivity)
                                            otherNumberDialog.setTitle("Enter phone number")
                                            otherNumberDialog.setMessage("Number format: 07....")
                                            otherNumberDialog.setView(linearLayout)
                                            otherNumberDialog.setNegativeButton("dismiss") { dialog, which -> dialog.dismiss() }
                                            otherNumberDialog.setPositiveButton("Proceed!") { dialog, which ->
                                                if (input.text.toString()
                                                        .trim { it <= ' ' } == ""
                                                ) {
                                                    Toast.makeText(
                                                        applicationContext,
                                                        "Can't leave blanks",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                } else {
                                                    performSTKPush(
                                                        input.text.toString().trim(),
                                                        "1"
                                                    )

                                                }
                                            }
                                            otherNumberDialog.show()
                                        }
                                userPhoneNumber.show()
                            }


                        }
                        .setNegativeButton("Later") { dialog: DialogInterface, which: Int -> dialog.dismiss() }
                        .create()

                    pinOnPurchase.show()
                } else {

                    val map: MutableMap<String, Any> = HashMap()
                    val map1: MutableMap<String, Any> = HashMap()
                    map["saveCount"] = FieldValue.increment(1)
                    map1["watchList"] =
                        FieldValue.arrayUnion(
                            generalDesc!! + "_-_" + nodeName!!
                        )
                    lectureDbRef.update(map)

                    if (thisUser != null) {
                        watchList.get().addOnSuccessListener {
                            if (it.exists()) {
                                watchList.update(map1)
                            } else {
                                watchList.set(map1)
                            }
                            Snackbar.make(
                                findViewById(R.id.coordStrm),
                                "Document added to watch later.",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }

        comments

        daraja = Daraja.with(
            Constants.CONSUMER_KEY,
            Constants.CONSUMER_SECRET,
            Env.SANDBOX,
            object : DarajaListener<AccessToken> {
                override fun onResult(result: AccessToken) {

                }

                override fun onError(error: String?) {
                    Toast.makeText(
                        applicationContext,
                        error.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
    }

    private fun hideSystemBars() {
        val controller = ViewCompat.getWindowInsetsController(window.decorView) ?: return
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        controller.hide(WindowInsetsCompat.Type.statusBars())
    }

    fun getSavedVid(name: String, c: Context): ArrayList<String>? {
        val preferences: SharedPreferences =
            c.getSharedPreferences(name, MODE_PRIVATE)
        val json = preferences.getString(name, "")
        val gson = Gson()
        val type = object : TypeToken<ArrayList<String?>?>() {}.type
        return gson.fromJson(json, type)
    }

    private fun postComment(
        user: FirebaseUser,
        docCommentET: EditText,
        mediaUriList: ArrayList<String>?,
        crucialData: String
    ) {

        val mProgressDialog = ProgressDialog(this)
        mProgressDialog.setMessage("Adding your comment")
        mProgressDialog.setTitle("Please Wait...")
        mProgressDialog.isIndeterminate = true
        mProgressDialog.show()

        val commentsRef = lectureDbRef.collection("Comments")
        val comment = docCommentET.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(comment)) {
            Toast.makeText(applicationContext, "Say something....", Toast.LENGTH_SHORT).show()
        } else {
            val cid = System.currentTimeMillis().toString()
            val refStr =
                generalDesc + "_-_" + replacer(nodeName) + "_-_Comments_-_" + cid + "_-_Lectures"

            val callForDate = Calendar.getInstance()
            val currentDate = SimpleDateFormat("dd-MM-yyyy")
            val saveCurrentDate = currentDate.format(callForDate.time)
            val callForTime = Calendar.getInstance()
            val currentTime = SimpleDateFormat("HH:mm")
            val saveCurrentTime = currentTime.format(callForTime.time)
            val uId = user.uid
            val userName = user.displayName
            val commentObj = CommentsObject(
                userName + "_-_" + uId + "_-_" + comment + "_-_"
                        + saveCurrentDate + "_-_" + saveCurrentTime + "_-_" + publisherUid,
                mediaUriList, System.currentTimeMillis().toString(), "0", "0",
                "0", null, null, refStr
            )

            comments1.add(commentObj)
            var commentsAdapter = CommentsAdapter(this@StreamingActivity, comments1, null)
            commentsAdapter.notifyDataSetChanged()
            docCommentRV!!.adapter = commentsAdapter
            docCommentRV!!.bringToFront()
            docCommentET.setText("")



            if (mediaUriList!!.size == 0) {

                val commentMap1: MutableMap<String, Any> = HashMap()
                commentMap1["comment"] =
                    userName + "_-_" + uId + "_-_" + comment + "_-_" + saveCurrentDate + "_-_" + saveCurrentTime + "_-_" + publisherUid + "+crucialData+" + crucialData
                commentMap1["repliesCount"] = "0"
                commentMap1["upVotes"] = "0"
                commentMap1["downVotes"] = "0"
                commentMap1["uiDs"] = ArrayList<String>()
                commentMap1["imCommentUrls"] = ArrayList<String>()
                commentMap1["cId"] = cid
                commentMap1["ref"] = refStr

                val commentObj0 = CommentsObject(
                    userName + "_-_" + uId + "_-_" + comment + "_-_"
                            + saveCurrentDate + "_-_" + saveCurrentTime + "_-_" +
                            publisherUid,
                    ArrayList<String>(), cid,
                    "0", "0", "0", null, null, refStr
                )

                comments1[0] = commentObj0
                commentsAdapter = CommentsAdapter(this@StreamingActivity, comments1, null)
                commentsAdapter.notifyDataSetChanged()
                docCommentRV!!.adapter = commentsAdapter

                commentsRef.document(cid)
                    .set(commentMap1)
                    .addOnSuccessListener {
                        val map: MutableMap<String, Any> = HashMap()
                        map["commentsCount"] = FieldValue.increment(1)
                        lectureDbRef.update(map)
                        mProgressDialog.dismiss()
                        val commentsAdapter1 =
                            CommentsAdapter(this@StreamingActivity, comments1, null)
                        commentsAdapter1.notifyDataSetChanged()
                        docCommentRV!!.adapter = commentsAdapter1
                        docCommentRV!!.bringToFront()
                        Toast.makeText(applicationContext, "Comment posted!", Toast.LENGTH_SHORT)
                            .show()

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


                var i = 0
                val pics = ArrayList<String>()
                val picLocale = ArrayList<String>()
                val picDoneList = ArrayList<String>()

                do {
                    var uT: StorageTask<UploadTask.TaskSnapshot>? = null
                    val fileReference1 = FirebaseStorage.getInstance().getReference("ImageComments")
                    val count = i
                    picLocale.add(mediaUriList[i])
                    pics.add("PendingImage")
                    picDoneList.add("Uploading")
                    val time = System.currentTimeMillis().toString()
                    val finalRef = fileReference1.child(time)

                    uT = finalRef.putFile(Uri.parse(picLocale[count]))
                        .addOnSuccessListener {
                            uT!!.continueWithTask {
                                finalRef.downloadUrl
                            }.addOnCompleteListener {
                                if (it.isSuccessful) {
                                    //String userID = FirebaseAuth.getInstance().getUid();
                                    picDoneList.removeAt(count)
                                    picDoneList.add(count, "Done")

                                    var downloadLink = it.result.toString()
                                    pics.removeAt(count)
                                    pics.add(count, downloadLink)

                                    uT = null
                                    downloadLink = ""


                                    mediaUriList.remove(picLocale[count])
                                    adt!!.notifyItemRemoved(count)

                                    if (mediaUriList.size == 0) {
                                        val commentObj1 =
                                            CommentsObject(
                                                userName + "_-_" + uId + "_-_" + comment + "_-_"
                                                        + saveCurrentDate + "_-_" + saveCurrentTime + "_-_" +
                                                        publisherUid,
                                                pics, cid,
                                                "0", "0", "0", null, null, refStr
                                            )

                                        comments1[0] = commentObj1
                                        commentsAdapter =
                                            CommentsAdapter(this@StreamingActivity, comments1, null)
                                        commentsAdapter.notifyDataSetChanged()
                                        docCommentRV!!.adapter = commentsAdapter

                                        val commentMap: MutableMap<String, Any> = HashMap()
                                        commentMap["comment"] =
                                            userName + "_-_" + uId + "_-_" + comment + "_-_" + saveCurrentDate + "_-_" + saveCurrentTime + "_-_" + publisherUid + "+crucialData+" + crucialData
                                        commentMap["repliesCount"] = "0"
                                        commentMap["upVotes"] = "0"
                                        commentMap["downVotes"] = "0"
                                        commentMap["uiDs"] = ArrayList<String>()
                                        commentMap["imCommentUrls"] = pics
                                        commentMap["cId"] = cid
                                        commentMap["ref"] = refStr

                                        commentsRef.document(cid)
                                            .set(commentMap)
                                            .addOnSuccessListener {
                                                val map: MutableMap<String, Any> = HashMap()
                                                map["commentsCount"] = FieldValue.increment(1)
                                                lectureDbRef.update(map)
                                                mProgressDialog.dismiss()

                                                docCommentRV!!.bringToFront()
                                                Toast.makeText(
                                                    applicationContext,
                                                    "Comment posted!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                lin.visibility = View.GONE
                                                imageRel.visibility = View.GONE

                                                docCommentET.setText("")

                                            }

                                    }

                                }
                            }
                        }

                    i++

                } while (i < mediaUriList.size)

//                for (s in mediaUriList) {
//                    val fileReference1 = FirebaseStorage.getInstance().getReference("ImageComments")
//                    uploadTask = fileReference1.putFile(Uri.parse(s))
//                        .addOnSuccessListener {
//                            uploadTask!!.continueWithTask { task ->
//                                if (!task.isSuccessful) {
//                                    throw task.exception!!
//                                }
//                                fileReference1.downloadUrl
//                            }.addOnCompleteListener { task ->
//                                if (task.isSuccessful) {
//                                    //String userID = FirebaseAuth.getInstance().getUid();
//                                    val downloadLink = task.result.toString()
//                                    uploadedList.add(downloadLink)
//                                    uploadPos.add(mediaUriList.indexOf(s))
//                                    mediaUriList.remove(s)
//                                    adt!!.notifyItemRemoved(mediaUriList.indexOf(s))
//                                    Toast.makeText(applicationContext, "Uploaded!", Toast.LENGTH_SHORT).show()
//
//                                    if (mediaUriList.size == 0){
//                                        val commentObj = CommentsObject(userName + "_-_" +
//                                                uId + "_-_" + comment + "_-_" + saveCurrentDate + "_-_" +
//                                                saveCurrentTime, uploadedList, uploadPos)
//
//                                        commentsRef.document(System.currentTimeMillis().toString())
//                                            .set(commentObj)
//                                            .addOnSuccessListener { aVoid: Void? ->
//                                                val map: MutableMap<String, Any> = HashMap()
//                                                map["commentsCount"] = FieldValue.increment(1)
//                                                documentReference.update(map)
//                                                mProgressDialog.dismiss()
//                                                val commentsAdapter1 = CommentsAdapter(this@StreamingActivity, comments1)
//                                                commentsAdapter1.notifyDataSetChanged()
//                                                docCommentRV!!.adapter = commentsAdapter1
//                                                docCommentRV!!.bringToFront()
//                                                Toast.makeText(applicationContext, "Comment posted!", Toast.LENGTH_SHORT)
//                                                    .show()
//                                                docCommentET.setText("")
//
//                                            }
//
//                                    }
//                                }
//                            }
//                        }
//                        .addOnFailureListener { e ->
//
//                        }
//                        .addOnProgressListener { taskSnapshot ->
//
//                        }
//                }
            }
        }

    }

    private val comments: Unit
        get() {
            val justASec = findViewById<TextView>(R.id.justASec)
            val commentsRef = lectureDbRef.collection("Comments")

            comments1 = ArrayList()
            commentsRef.get().addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
                justASec.visibility = View.GONE
                for (qds in queryDocumentSnapshots) {
                    var docId: String?
                    var upVotes: String?
                    var downVotes: String?
                    var replies: String?
                    var comment: String?
                    var imUrls: ArrayList<String>?
                    var uids: ArrayList<String>?
                    var ref: String?

                    try {
                        comment = qds.getString("comment")
                        replies = qds.getString("repliesCount")
                        upVotes = qds.getString("upVotes")
                        downVotes = qds.get("downVotes").toString()
                        docId = qds.get("cId").toString()
                        uids = qds.get("uiDs") as ArrayList<String>?
                        imUrls = qds.get("imCommentUrls") as ArrayList<String>
                        ref = qds.get("ref").toString()

                        val reference = FirebaseFirestore.getInstance().collection("Content")
                            .document("Lectures")
                            .collection(ref.split("_-_")[0])
                            .document(ref.split("_-_")[1])
                            .collection(ref.split("_-_")[2])
                            .document(ref.split("_-_")[3])



                        comments1.add(
                            CommentsObject(
                                comment,
                                imUrls,
                                docId,
                                upVotes,
                                downVotes,
                                replies,
                                uids, reference, ref
                            )
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


                }
                comments1.reverse()
                val commentsAdapter = CommentsAdapter(this@StreamingActivity, comments1, touchIV)
                commentsAdapter.notifyDataSetChanged()
                docCommentRV!!.adapter = commentsAdapter
                docCommentRV!!.bringToFront()
            }.addOnFailureListener { e: Exception? -> }
        }

    private fun performSTKPush(
        phone_number: String?,
        amount: String
    ) {

        val lnmExpress = LNMExpress(
            BUSINESS_SHORT_CODE,
            Constants.PASSKEY1,
            TransactionType.CustomerPayBillOnline,
            "1",
            Utils.sanitizePhoneNumber(phone_number),
            Constants.PARTYB,
            Utils.sanitizePhoneNumber(phone_number),
            Constants.CALLBACKURL,
            "yNote Studios",
            "Goods Payment"
        )



        daraja.requestMPESAExpress(lnmExpress, object : DarajaListener<LNMResult> {
            override fun onResult(result: LNMResult) {
                FirebaseMessaging.getInstance()
                    .subscribeToTopic(result.CheckoutRequestID.toString())
                    .addOnSuccessListener {
                        Toast.makeText(
                            applicationContext,
                            "Processing purchase..",
                            Toast.LENGTH_LONG
                        ).show()

                        val pendingPurchases =
                            DocumentLoader.loadPendingPurchase(applicationContext)
                        if (DocumentLoader.finalPin != null) {
                            pendingPurchases.add(nodeName!! + "_-_" + saveCount + "_-_" + commentsCount + "_checkoutID_" + result.CheckoutRequestID.toString())
                            DocumentLoader.savePendingPurchase(applicationContext, pendingPurchases)
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Something's a miss.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }

            override fun onError(error: String?) {
                Toast.makeText(applicationContext, "An Error Occurred: $error", Toast.LENGTH_SHORT)
                    .show()
            }
        })

//        daraja.requestMPESAExpress(lnmExpress, object : DarajaListener<LNMResult> {
//            override fun onResult(result: LNMResult) {
//                FirebaseMessaging.getInstance().subscribeToTopic(result.CheckoutRequestID.toString())
//                try {
//                    val pins = ArrayList<String>()
//                    val user = FirebaseAuth.getInstance().currentUser
//                    pins.add(finalPin!!)
//                    val preferences: SharedPreferences =
//                        applicationContext.getSharedPreferences("docPins", MODE_PRIVATE)
//                    val editor = preferences.edit()
//                    val gson = Gson()
//                    val json = gson.toJson(pins)
//                    editor.putString("docPins", json)
//                    editor.apply()
//                    val map: MutableMap<String, Any> = HashMap()
//                    val map1: MutableMap<String, Any> = HashMap()
//                    map["saveCount"] = FieldValue.increment(1)
//                    map1["readList"] =
//                        FieldValue.arrayUnion(replacer(school) + "_-_" + replacer(title))
//                    documentReference.update(map)
//                    val readList = FirebaseFirestore.getInstance().collection("Users")
//                        .document(user!!.uid)
//                        .collection("ScheduledLists")
//                        .document("ReadList")
//
//
//                    readList.get().addOnSuccessListener {
//                        if (it.exists()) {
//                            readList.update(map1)
//                        } else {
//                            readList.set(map1)
//                        }
//                    }
//                    val count: CountDownTimer = object : CountDownTimer(1500, 1000) {
//                        override fun onTick(millisUntilFinished: Long) {}
//                        override fun onFinish() {
//                            animation.duration = 1500
//                            addToPins.startAnimation(animation)
//                            Snackbar.make(
//                                findViewById(R.id.docViewCoordinator),
//                                "Document pinned!",
//                                Snackbar.LENGTH_SHORT
//                            ).show()
//                            mProgressDialog!!.dismiss()
//                        }
//                    }
//                    count.start()
//
//                } catch (e: java.lang.Exception) {
//                    e.printStackTrace()
//                }
//            }
//
//            override fun onError(error: String?) {
//                Toast.makeText(applicationContext, "An Error: $error", Toast.LENGTH_SHORT).show()
//            }
//        })


//        val stkPush = STKPush(
//            BUSINESS_SHORT_CODE,
//            Constants.PASSKEY,
//            timestamp,
//            Constants.TRANSACTION_TYPE, amount,
//            Utils.sanitizePhoneNumber(phone_number),
//            Constants.PARTYB,
//            Utils.sanitizePhoneNumber(phone_number),
//            Constants.CALLBACKURL,
//            "yNote Studios",  //Account reference
//            "Pin purchase" //Transaction description
//        )
//        mApiClient!!.setGetAccessToken(false)
//
//        //Sending the data to the Mpesa API, remember to remove the logging when in production.
//        mApiClient!!.mpesaService().sendPush(stkPush).enqueue(object : Callback<STKPush?> {
//            override fun onResponse(call: Call<STKPush?>, response: Response<STKPush?>) {
//                mProgressDialog!!.dismiss()
//                try {
//                    if (response.isSuccessful) {
//                        // TODO: 6/18/2021 add user to access list on Firestore after user pays
//                        val pins = ArrayList<String>()
//                        val user = FirebaseAuth.getInstance().currentUser
//                        pins.add(finalPin!!)
//                        val preferences: SharedPreferences =
//                            applicationContext.getSharedPreferences("docPins", MODE_PRIVATE)
//                        val editor = preferences.edit()
//                        val gson = Gson()
//                        val json = gson.toJson(pins)
//                        editor.putString("docPins", json)
//                        editor.apply()
//                        val map: MutableMap<String, Any> = HashMap()
//                        val map1: MutableMap<String, Any> = HashMap()
//                        map["saveCount"] = FieldValue.increment(1)
//                        map1["readList"] =
//                            FieldValue.arrayUnion(replacer(school) + "_-_" + replacer(title))
//                        documentReference.update(map)
//                        val readList = FirebaseFirestore.getInstance().collection("Users")
//                            .document(user!!.uid)
//                            .collection("ScheduledLists")
//                            .document("ReadList")
//
//
//                        readList.get().addOnSuccessListener {
//                            if (it.exists()) {
//                                readList.update(map1)
//                            } else {
//                                readList.set(map1)
//                            }
//                        }
//                        val count: CountDownTimer = object : CountDownTimer(1500, 1000) {
//                            override fun onTick(millisUntilFinished: Long) {}
//                            override fun onFinish() {
//                                animation.duration = 1500
//                                addToPins.startAnimation(animation)
//                                Snackbar.make(
//                                    findViewById(R.id.docViewCoordinator),
//                                    "Document pinned!",
//                                    Snackbar.LENGTH_SHORT
//                                ).show()
//                                mProgressDialog!!.dismiss()
//                            }
//                        }
//                        count.start()
//
//                    } else {
//                        if (response.errorBody() != null) {
//                            Toast.makeText(
//                                this@StreamingActivity, "Response: "
//                                        + response.errorBody().toString(), Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//                } catch (e: java.lang.Exception) {
//                    e.printStackTrace()
//                }
//            }
//
//            override fun onFailure(call: Call<STKPush?>, t: Throwable) {
//                mProgressDialog!!.dismiss()
//                Toast.makeText(this@StreamingActivity, "Error: " + t.message, Toast.LENGTH_SHORT)
//                    .show()
//            }
//        })

    }

    private fun replacer(docName: String?): String {
        val docName1 = docName!!.replace("]", "")
        val docName2 = docName1.replace("[", "")
        val docName3 = docName2.replace(".", "")
        val docName4 = docName3.replace("$", "")
        val docName5 = docName4.replace("*", "")
        return docName5.replace("#", "")
    }

    private fun getPins(name: String): ArrayList<String> {
        val preferences: SharedPreferences =
            applicationContext.getSharedPreferences(name, MODE_PRIVATE)
        val json = preferences.getString(name, "")
        val gson = Gson()
        val type = object : TypeToken<ArrayList<String?>?>() {}.type
        return gson.fromJson(json, type)
    }

    private fun setExoPlayer(app: Application, selectedVideo: String?) {
//        streamVidName.setText(selectedVideo.getVideoDescription().split("_-_")[0]);
        try {
            val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter.Builder(app).build()
            val trackSelector: TrackSelector =
                DefaultTrackSelector(AdaptiveTrackSelection.Factory(bandwidthMeter))
            exoPlayer = ExoPlayerFactory.newSimpleInstance(app)
            val sourceFactory = DefaultHttpDataSourceFactory("video")
            val extractorsFactory: ExtractorsFactory = DefaultExtractorsFactory()
            val mediaSource: MediaSource = ExtractorMediaSource(
                Uri.parse(selectedVideo),
                sourceFactory, extractorsFactory, null, null
            )

            val audioFocusChangeListener =
                AudioManager.OnAudioFocusChangeListener { focusChange: Int ->
                    when (focusChange) {
                        AudioManager.AUDIOFOCUS_GAIN -> {
                            playerView!!.player = exoPlayer
                            playerView!!.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                            if (exoPlayer != null) {
                                exoPlayer!!.prepare(mediaSource)
                                exoPlayer!!.videoScalingMode =
                                    C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
                                exoPlayer!!.playWhenReady = false
                            }
                        }
                        AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                            if (exoPlayer != null) {
                                exoPlayer!!.stop()
                                exoPlayer!!.seekTo(0)
                            }
                        }
                        AudioManager.AUDIOFOCUS_LOSS -> {

                        }
                    }
                }

            val audioManager: AudioManager = getSystemService(AUDIO_SERVICE) as AudioManager
            val playbackAttributes: AudioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(playbackAttributes)
                    .setAcceptsDelayedFocusGain(true)
                    .setOnAudioFocusChangeListener(audioFocusChangeListener)
                    .build()
                val audioFocusRequest = audioManager.requestAudioFocus(focusRequest)
                if (audioFocusRequest == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    playerView!!.player = exoPlayer
                    playerView!!.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                    exoPlayer!!.prepare(mediaSource)
                    exoPlayer!!.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
                    exoPlayer!!.playWhenReady = false
                }
            } else {
                playerView!!.player = exoPlayer
                playerView!!.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                exoPlayer!!.prepare(mediaSource)
                exoPlayer!!.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
                exoPlayer!!.playWhenReady = false
            }


        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("StreamingDialog", "exoPlayer error $e")
        }
        if (exoPlayer!!.isLoading) {
            loadProgress!!.visibility = View.VISIBLE
        } else if (exoPlayer!!.isPlaying) {
            loadProgress!!.visibility = View.GONE
        }
    }

    private fun releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer!!.release()
            exoPlayer = null
        }
    }

    private fun pausePlayer() {
        if (exoPlayer != null) {
            exoPlayer!!.playWhenReady = false
            exoPlayer!!.playbackState
        }
    }

    private fun resumePlayer() {
        if (exoPlayer != null) {
            exoPlayer!!.playWhenReady = true
            exoPlayer!!.playbackState
        }
    }

    private fun setUp(videoUri: Uri?) {
        initializePlayer()
        if (videoUri == null) {
            return
        }
        buildMediaSource(videoUri)
    }

    private fun initializePlayer() {
        if (exoPlayer == null) {
            // 1. Create a default TrackSelector
            val loadControl: LoadControl = DefaultLoadControl(
                DefaultAllocator(true, 16),
                VideoPlayerConfig.MIN_BUFFER_DURATION,
                VideoPlayerConfig.MAX_BUFFER_DURATION,
                VideoPlayerConfig.MIN_PLAYBACK_START_BUFFER,
                VideoPlayerConfig.MIN_PLAYBACK_RESUME_BUFFER, -1, true
            )
            val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter() //Diff
            val videoTrackSelectionFactory: TrackSelection.Factory =
                AdaptiveTrackSelection.Factory(bandwidthMeter)
            val trackSelector: TrackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
            // 2. Create the player
            exoPlayer = ExoPlayerFactory.newSimpleInstance(
                applicationContext,
                DefaultRenderersFactory(applicationContext), trackSelector, loadControl
            )
            playerView!!.player = exoPlayer
        }
    }

    private fun buildMediaSource(mUri: Uri) {
        // Measures bandwidth during playback. Can be null if not required.
        val bandwidthMeter = DefaultBandwidthMeter()
        val sourceFactory = DefaultHttpDataSourceFactory("video")
        val extractorsFactory: ExtractorsFactory = DefaultExtractorsFactory()
        // Produces DataSource instances through which media data is loaded.
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
            applicationContext,
            Util.getUserAgent(
                applicationContext,
                applicationContext.getString(R.string.app_name)
            ),
            bandwidthMeter
        )
        // This is the MediaSource representing the media to be played.
        val videoSource: MediaSource =
            ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mUri)
        //        MediaSource videoSource = new ExtractorMediaSource(mUri, sourceFactory, extractorsFactory, null, null);

        // Prepare the player with the source.
        exoPlayer!!.prepare(videoSource)
        exoPlayer!!.playWhenReady = true
        exoPlayer!!.addListener(this@StreamingActivity)
    }


    //    @Override
    //    protected void onPause() {
    //        super.onPause();
    //        pausePlayer();
    //        if (mRunnable != null) {
    //            mHandler.removeCallbacks(mRunnable);
    //        }
    //    }
    //
    //    @Override
    //    protected void onRestart() {
    //        super.onRestart();
    //        resumePlayer();
    //    }
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        releasePlayer()
    }

    override fun onTimelineChanged(timeline: Timeline, manifest: Any?, reason: Int) {

    }

    override fun onTracksChanged(
        trackGroups: TrackGroupArray,
        trackSelections: TrackSelectionArray
    ) {
    }

    override fun onLoadingChanged(isLoading: Boolean) {}
    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        when (playbackState) {
            Player.STATE_BUFFERING -> {
                loadProgress!!.visibility = View.VISIBLE
            }
            Player.STATE_ENDED -> {}
            Player.STATE_IDLE -> {}
            Player.STATE_READY -> {
                loadProgress!!.visibility = View.GONE
                if (timerRunning) {
                    pauseTimer()
                } else {

                    if (timeInMillis!! > 120000) {
                        startTimer(timeInMillis!! / 10)
                    } else {
                        countDownTimer = object : CountDownTimer(timeInMillis!! / 2, 1000) {
                            override fun onTick(l: Long) {
                                timeLeft = l
                            }

                            override fun onFinish() {
                                timerRunning = false
                                val vidMap: MutableMap<String, Any> = HashMap()
                                vidMap["views"] = FieldValue.increment(1)
                                lectureDbRef.update(vidMap).addOnSuccessListener {
                                }

                                //update views
                            }
                        }.start()
                    }
                }
            }
            else -> {}
        }
    }


    private fun pauseTimer() {
        countDownTimer!!.cancel()
        timerRunning = false
    }

    private fun startTimer(timeMillis: Long) {
        countDownTimer = object : CountDownTimer(timeMillis, 1000) {
            override fun onTick(l: Long) {
                timeLeft = l
            }

            override fun onFinish() {
                timerRunning = false
                val vidMap: MutableMap<String, Any> = HashMap()
                vidMap["views"] = FieldValue.increment(1)
                lectureDbRef.update(vidMap).addOnSuccessListener {
                }
            }
        }.start()
        timerRunning = true
    }

    override fun onRepeatModeChanged(repeatMode: Int) {}
    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {}
    override fun onPlayerError(error: ExoPlaybackException) {}
    override fun onPositionDiscontinuity(reason: Int) {}
    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {}
    override fun onSeekProcessed() {}
}