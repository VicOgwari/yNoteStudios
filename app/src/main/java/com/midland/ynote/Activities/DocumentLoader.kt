package com.midland.ynote.Activities

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.print.PrintAttributes
import android.print.PrintAttributes.Resolution
import android.print.PrintDocumentAdapter
import android.print.PrintJob
import android.print.PrintManager
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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
import com.google.android.gms.ads.AdView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
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
import com.midland.ynote.Dialogs.UserProducts
import com.midland.ynote.Objects.CommentsObject
import com.midland.ynote.R
import com.midland.ynote.Utilities.*
import com.midland.ynote.Utilities.FilingSystem.Companion.dubDocuments
import com.midland.ynote.Utilities.Transaction
import com.midland.ynote.stkPush.DarajaApiClient
import com.midland.ynote.stkPush.Utils
import com.ortiz.touchview.TouchImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


class DocumentLoader : AppCompatActivity(), MpesaListener {
    var images = ArrayList<String>()
    var photosRV: RecyclerView? = null
    var addBtn: ImageButton? = null
    var outSource: ImageButton? = null

    private lateinit var daraja: Daraja
    private var mProgressDialog: ProgressDialog? = null
    private var mApiClient: DarajaApiClient? = null
    private val BUSINESS_SHORT_CODE = "174379"
    private var animation: RotateAnimation? = null
    private var animation1: RotateAnimation? = null


    private var pinned = String()
    private var endColor: String? = null
    private var pinCount: Int? = null
    private var comCount: Int? = null
    lateinit var comments1: ArrayList<CommentsObject>
    var viewerRel: RelativeLayout? = null
    var redirect: Boolean = false
    var flag = String()
    var complete_loading: Boolean = true
    var adt: SelectedItemsAdt? = null
    private val generalDocRel: RelativeLayout? = null
    var docViewer: WebView? = null
    var docCommentET: EditText? = null


    private var docCommentRV: RecyclerView? = null
    private var readBuffer: ProgressBar? = null
    private var finalLink: String? = null
    private var imageCommentRV: RecyclerView? = null
    private var addToPins: ImageButton? = null
    private var refresh: ImageButton? = null

    private var publisher: String? = null
    private var publisherUid: String? = null
    private var PICK_IMAGE_INTENT = 99
    private var PICK_DOC_INTENT = 98

    lateinit var imageRel: RelativeLayout
    lateinit var docRel: RelativeLayout
    lateinit var lin: LinearLayout
    lateinit var strFileName: String
    lateinit var strDateTime: String
    lateinit var printJob: PrintJob
    lateinit var touchIV: TouchImageView
    lateinit var printManager: PrintManager
    lateinit var printDocumentAdapter: PrintDocumentAdapter
    lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    lateinit var bottomSheetBehavior1: BottomSheetBehavior<*>
    private var mAdView: AdView? = null

    companion object Companion {
        var finalPin: String? = null
        var pendingFinalPin: String? = null
        lateinit var documentReference: DocumentReference
        lateinit var commentsRef: CollectionReference
        lateinit var mpesaListener: MpesaListener
        var school: String? = null
        var title1: String? = null

        fun completePurchase(
            applicationContext: Context, pendingFinalPin: String,
            animation: RotateAnimation?, addToPins: ImageButton?
        ): Boolean {
            try {
                val pins =
                    getPins(
                        "docPins",
                        applicationContext
                    )
                val user = FirebaseAuth.getInstance().currentUser
                pins!!.add(pendingFinalPin)
                val preferences: SharedPreferences =
                    applicationContext.getSharedPreferences("docPins", MODE_PRIVATE)
                val editor = preferences.edit()
                val gson = Gson()
                val json = gson.toJson(pins)
                editor.putString("docPins", json)
                editor.apply()
                val map: MutableMap<String, Any> = HashMap()
                val map1: MutableMap<String, Any> = HashMap()
                map["saveCount"] = FieldValue.increment(1)
                map1["readList"] =
                    FieldValue.arrayUnion(
                        replacer(
                            pendingFinalPin.split("_-_")[0]
                        )
                                + "_-_" + replacer(
                            pendingFinalPin.split("_-_")[1]
                        )
                    )
                documentReference.update(map)
                val readList = FirebaseFirestore.getInstance().collection("Users")
                    .document(user!!.uid)
                    .collection("Lists")
                    .document("ReadList")


                readList.get().addOnSuccessListener {
                    if (it.exists()) {
                        readList.update(map1)
                    } else {
                        readList.set(map1)
                    }
                }
                val count: CountDownTimer = object : CountDownTimer(1500, 1000) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        animation!!.duration = 1500
                        addToPins!!.startAnimation(animation)
                        Toast.makeText(
                            applicationContext,
                            "Document pinned! Check your homepage.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                count.start()
                return true

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                return false
            }
        }

        fun savePendingPurchase(con: Context, pp: ArrayList<String>) {
            val sharedPreferences =
                con.getSharedPreferences("pendingPurchases", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            val gson = Gson()
            val json = gson.toJson(pp)
            editor.putString("pendingPurchases", json)
            editor.apply()
        }

        fun loadPendingPurchase(con: Context): ArrayList<String> {
            val pendingPurchases: ArrayList<String>
            val sharedPreferences =
                con.getSharedPreferences("pendingPurchases", Context.MODE_PRIVATE)
            pendingPurchases = if (sharedPreferences.contains("pendingPurchases")) {
                val gson = Gson()
                val json = sharedPreferences.getString("pendingPurchases", "")
                val type = object : TypeToken<ArrayList<String>>() {}.type
                gson.fromJson(json, type)!!
            } else {
                ArrayList()
            }
            return pendingPurchases
        }

        fun replacer(docName: String?): String {
            val docName1 = docName!!.replace("]", "")
            val docName2 = docName1.replace("[", "")
            val docName3 = docName2.replace(".", "")
            val docName4 = docName3.replace("$", "")
            val docName5 = docName4.replace("*", "")
            return docName5.replace("#", "")
        }

        fun getPins(name: String, c: Context): ArrayList<String>? {
            val preferences: SharedPreferences =
                c.getSharedPreferences(name, MODE_PRIVATE)
            val json = preferences.getString(name, "")
            val gson = Gson()
            val type = object : TypeToken<ArrayList<String?>?>() {}.type
            return gson.fromJson(json, type)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document_loader)

        AdMob.interstitialAd(this@DocumentLoader, this)
        mpesaListener = this
        mProgressDialog = ProgressDialog(this)
        mApiClient = DarajaApiClient()
        mApiClient!!.setIsDebug(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getAccessToken()
        } else {
            Toast.makeText(this, "Your device can't support this feature...", Toast.LENGTH_SHORT)
                .show()
        }

        val animationSet = AnimationSet(true)
        animationSet.interpolator = DecelerateInterpolator()
        animationSet.fillAfter = true
        animationSet.isFillEnabled = true

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

        animation = RotateAnimation(
            0.0f, 180.0f,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f
        )

        animation1 = RotateAnimation(
            0.0f, 360.0f,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f
        )
        animation!!.duration = 1
        animation!!.fillAfter = true
        animation1!!.duration = 1
        animation1!!.fillAfter = true

        photosRV = findViewById(R.id.photosRV)
        addBtn = findViewById(R.id.addBtn)
        outSource = findViewById(R.id.outSource)
        val closeViewer = findViewById<ImageButton>(R.id.closeDocView)
        val discardImage = findViewById<ImageButton>(R.id.discardImage)
        val docComments = findViewById<ImageButton>(R.id.docComments)
        refresh = findViewById(R.id.refresh)
        val saveDoc = findViewById<ImageButton>(R.id.saveDoc)
        val justASec = findViewById<TextView>(R.id.justASec)
        addToPins = findViewById(R.id.addToPins)
        docCommentRV = findViewById(R.id.docCommentsRV)
        val addComment = findViewById<Button>(R.id.addCommentBtn)
        val docCommentET = findViewById<EditText>(R.id.docCommentET)
        val bottomSheet = findViewById<View>(R.id.comments_sheet)
        val bottomSheet1 = findViewById<View>(R.id.ideas_sheet)
        viewerRel = findViewById(R.id.docViewerRel)
        docViewer = findViewById(R.id.docView)
        readBuffer = findViewById(R.id.readBuffer)
        val addCommentFab = findViewById<FloatingActionButton>(R.id.addComment)
        val addDocFab = findViewById<FloatingActionButton>(R.id.addDocument)
        val addPhotoFab = findViewById<FloatingActionButton>(R.id.addPhoto)
        val subscribe = findViewById<ImageButton>(R.id.subscribe)
        val docTitle = findViewById<TextView>(R.id.docTitle)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet1)


        val grid = MoreIdeasAdt(
            addBtn!!,
            DocSorting.getFileObjects(applicationContext),
            "moreIdeas",
            this,
            images
        )
        photosRV!!.layoutManager = GridLayoutManager(this@DocumentLoader, 3)
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

        val stkPayLoad = intent.getStringExtra("payload")
        if (stkPayLoad != null && stkPayLoad != "") {
            val gson = Gson()
            val mpesaResponse: Transaction = gson.fromJson(stkPayLoad, Transaction::class.java)
            val topicID = mpesaResponse.body.stkCallback.checkoutRequestID
            if (mpesaResponse.body.stkCallback.resultCode != 0) {
                val cause = mpesaResponse.body.stkCallback.resultDesc
                val purchaseResults = AlertDialog.Builder(this@DocumentLoader)
                    .setTitle("Transaction failed!")
                    .setIcon(R.drawable.tw__composer_close)
                    .setMessage(cause)
                    .setPositiveButton("Ok") { it: DialogInterface?, _: Int ->
                        it?.dismiss()
                    }
                purchaseResults.show()

                val pendingPurchases =
                    loadPendingPurchase(
                        applicationContext
                    )
                pendingPurchases.forEach {
                    if (it.endsWith(topicID)) {
                        pendingFinalPin = it.split("_checkoutID_")[0]
                        finalPin =
                            pendingFinalPin
                        school = finalPin!!.split("_-_")[0]
                        title1 = finalPin!!.split("_-_")[1]
                        finalLink = finalPin!!.split("_-_")[4]
                        pinCount = Integer.parseInt(finalPin!!.split("_-_")[5])
                        comCount = Integer.parseInt(finalPin!!.split("_-_")[6])
                        docViewer!!.loadUrl(finalLink!!)
                        documentReference = FirebaseFirestore.getInstance()
                            .collection("Content")
                            .document("Documents")
                            .collection(
                                replacer(
                                    school
                                )
                            )
                            .document(
                                replacer(
                                    title1
                                )
                            )

                        commentsRef =
                            FirebaseFirestore.getInstance().collection("Content")
                                .document("Documents")
                                .collection(
                                    replacer(
                                        school
                                    )
                                )
                                .document(
                                    replacer(
                                        title1
                                    )
                                ).collection("Comments")
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(topicID)
                            .addOnSuccessListener {
                            }
                    }
                }

            } else {
                val infoList: List<Transaction.Body.StkCallback.CallbackMetadata.Item> =
                    mpesaResponse.body.stkCallback.callbackMetadata.item

                var dateOfTransaction = ""
                var amountTransacted = ""
                var receiptNo = ""
                var phoneNumber = ""

                infoList.forEach { transaction ->
                    if (transaction.name == "MpesaReceiptNumber") {
                        receiptNo = transaction.value
                    }
                    if (transaction.name == "TransactionDate") {
                        dateOfTransaction = transaction.value
                    }
                    if (transaction.name == "PhoneNumber") {
                        phoneNumber = transaction.value
                    }
                    if (transaction.name == "Amount") {
                        amountTransacted = transaction.value
                    }
                }

                val pendingPurchases =
                    loadPendingPurchase(
                        applicationContext
                    )
                pendingPurchases.forEach {
                    if (it.endsWith(topicID)) {
                        pendingFinalPin = it.split("_checkoutID_")[0]
                        finalPin =
                            pendingFinalPin
                        school = finalPin!!.split("_-_")[0]
                        title1 = finalPin!!.split("_-_")[1]
                        finalLink = finalPin!!.split("_-_")[4]
                        pinCount = Integer.parseInt(finalPin!!.split("_-_")[5])
                        comCount = Integer.parseInt(finalPin!!.split("_-_")[6])
                        docViewer!!.loadUrl(finalLink!!)
                        documentReference = FirebaseFirestore.getInstance()
                            .collection("Content")
                            .document("Documents")
                            .collection(
                                replacer(
                                    school
                                )
                            )
                            .document(
                                replacer(
                                    title1
                                )
                            )

                        commentsRef =
                            FirebaseFirestore.getInstance().collection("Content")
                                .document("Documents")
                                .collection(
                                    replacer(
                                        school
                                    )
                                )
                                .document(
                                    replacer(
                                        title1
                                    )
                                ).collection("Comments")
                        val bool =
                            completePurchase(
                                applicationContext,
                                pendingFinalPin!!,
                                animation!!,
                                addToPins!!
                            )
                        if (bool) {
                            pendingPurchases.remove(it)
                            savePendingPurchase(
                                applicationContext,
                                pendingPurchases
                            )
                            FirebaseMessaging.getInstance().unsubscribeFromTopic(topicID)
                                .addOnSuccessListener {
                                }
                            val purchaseResults = AlertDialog.Builder(this@DocumentLoader)
                                .setTitle("Transaction successful.")
                                .setIcon(R.drawable.ic_check_green)
                                .setMessage("$amountTransacted paid by (+$phoneNumber) on  $dateOfTransaction\nReceipt id: $receiptNo")
                                .setPositiveButton("Ok") { it: DialogInterface?, _: Int ->
                                    it!!.dismiss()
                                }
                            purchaseResults.show()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Something's not right..",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
            comments
            refresh!!.setOnClickListener { v: View? ->
                comments
                docViewer!!.loadUrl(finalLink!!)
            }
            addToPins!!.setOnClickListener {
                pinTheDoc(animation!!, animation1!!, addToPins!!)
            }
            docTitle.text =
                title1
        }

        if (intent.getStringExtra("notification") != null) {
            val theDoc = intent.getStringExtra("notification")

            try {
                publisherUid = theDoc!!.split("_-_")[5]
                school = theDoc.split("_-_")[6]
                title1 = theDoc.split("_-_")[7]
                finalLink = theDoc.split("_-_")[8]
                pinCount = Integer.parseInt(theDoc.split("_-_")[9])
                comCount = Integer.parseInt(theDoc.split("_-_")[10])
                docViewer!!.loadUrl(finalLink!!)

                documentReference = FirebaseFirestore.getInstance()
                    .collection("Content")
                    .document("Documents")
                    .collection(
                        replacer(
                            school
                        )
                    )
                    .document(
                        replacer(
                            title1
                        )
                    )

                commentsRef =
                    FirebaseFirestore.getInstance().collection("Content").document("Documents")
                        .collection(
                            replacer(
                                school
                            )
                        )
                        .document(
                            replacer(
                                title1
                            )
                        ).collection("Comments")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            comments
            refresh!!.setOnClickListener { v: View? ->
                comments
                docViewer!!.loadUrl(finalLink!!)
            }
            docTitle.text =
                title1
        }

        if (intent.getStringExtra("docUrl") != null) {
            finalLink = intent.getStringExtra("docUrl")
            docViewer!!.loadUrl(finalLink!!)

        }
        if (intent.getStringExtra("school") != null && intent.getStringExtra("title") != null) {
            school = intent.getStringExtra("school")
            title1 = intent.getStringExtra("title")
            publisher = intent.getStringExtra("publisher")
            publisherUid = intent.getStringExtra("publisherUid")
            endColor = intent.getStringExtra("endColor")

            finalPin =
                school + "_-_" + title1 + "_-_" + publisher + "_-_" + endColor + "_-_" + finalLink
            documentReference = FirebaseFirestore.getInstance()
                .collection("Content")
                .document("Documents")
                .collection(
                    replacer(
                        school
                    )
                )
                .document(
                    replacer(
                        title1
                    )
                )

            commentsRef =
                FirebaseFirestore.getInstance().collection("Content").document("Documents")
                    .collection(
                        replacer(
                            school
                        )
                    )
                    .document(
                        replacer(
                            title1
                        )
                    ).collection("Comments")
            comments
            refresh!!.setOnClickListener { v: View? ->
                comments
                docViewer!!.loadUrl(finalLink!!)
            }
            docTitle.text =
                title1
        }
        if (intent.getStringExtra("pinned") != null) {
            pinned = intent.getStringExtra("pinned")!!
        }
        if (intent.getStringExtra("pinCount") != null) {
            pinCount = intent.getStringExtra("pinCount")!!.toInt()
        }
        if (intent.getStringExtra("pinCount") != null) {
            comCount = intent.getStringExtra("comCount")!!.toInt()
        }

        subscribe.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser != null) {
                FirebaseMessaging.getInstance().subscribeToTopic(school!! + "_-_" + title1!!)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(applicationContext, "Subscribed!", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Subscription failed...",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                val logInSignUp =
                    LogInSignUp(applicationContext)
                logInSignUp.show()
            }

        }
        addToPins!!.setOnClickListener {
            pinTheDoc(animation!!, animation1!!, addToPins!!)
        }
        docRel = findViewById(R.id.docRel)
        touchIV = findViewById(R.id.touchIV)
        imageRel = findViewById(R.id.imageRel)
        lin = findViewById(R.id.lin)

        imageCommentRV = findViewById(R.id.imageComment)
        imageCommentRV!!.layoutManager = LinearLayoutManager(
            applicationContext,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        try {
            if (getPins(
                    "docPins",
                    applicationContext
                )!!.contains(finalPin) || pinned == "true") {
                addToPins!!.rotation = 180.0f
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }


        docCommentRV!!.layoutManager = LinearLayoutManager(
            applicationContext,
            RecyclerView.VERTICAL, false
        )

        closeViewer.setOnClickListener { v: View? -> onBackPressed() }
        docViewer!!.settings.javaScriptEnabled = true

        docViewer!!.settings.builtInZoomControls = true
        docViewer!!.zoomIn()
        docViewer!!.zoomOut()



        docViewer!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (!complete_loading) {
                        redirect = true
                    }
                    complete_loading = false
                    view.loadUrl(request.url.toString())
                } else {
                    Toast.makeText(applicationContext, "Api issues", Toast.LENGTH_SHORT).show()
                }
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                complete_loading = false
            }

//            override fun onPageFinished(view: WebView?, url: String?) {
//                super.onPageFinished(view, url)
//                if (!redirect){
//                    complete_loading = true
//                }
//                if (complete_loading && !redirect){
//                    Toast.makeText(applicationContext, "Loading Complete!", Toast.LENGTH_SHORT).show()
//                }else{
//                    Toast.makeText(applicationContext, "Loading all pages...", Toast.LENGTH_SHORT).show()
//                }
//            }

        }
        docViewer!!.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress == 100) {
                    readBuffer!!.visibility = View.INVISIBLE
                } else {
                    readBuffer!!.visibility = View.VISIBLE
                }
            }
        }

        discardImage.setOnClickListener {
            if (imageRel.visibility == View.VISIBLE) {
                imageRel.visibility = View.GONE
            }
        }
        saveDoc.setOnClickListener { v: View? ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                createWebPrintJob(docViewer)
            } else {
            }
        }
        addToPins!!.setOnClickListener {
            pinTheDoc(animation!!, animation1!!, addToPins!!)
        }
        docComments.setOnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
                //Retrieve comments from db & place them on RV
            } else
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
        }
        addComment.setOnClickListener { v: View? ->
            if (docCommentET.text.toString().isEmpty()) {
                Toast.makeText(applicationContext, "Say something...", Toast.LENGTH_SHORT).show()
            } else {

                val user = FirebaseAuth.getInstance().currentUser
                if (user == null) {
                    val logInSignUp =
                        LogInSignUp(this@DocumentLoader)
                    logInSignUp.show()
                } else {
                    postComment(user, docCommentET, MoreIdeasAdt.images1, flag)
                }

            }
        }
        addCommentFab.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user == null) {
                val logInSignUp =
                    LogInSignUp(this@DocumentLoader)
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
        addDocFab.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user == null) {
                val logInSignUp =
                    LogInSignUp(this@DocumentLoader)
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
                    LogInSignUp(this@DocumentLoader)
                logInSignUp.show()
            } else {
                if (bottomSheetBehavior1.state == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED)
                    //Retrieve comments from db & place them on RV
                } else
                    if (bottomSheetBehavior1.state == BottomSheetBehavior.STATE_EXPANDED) {
                        bottomSheetBehavior1.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                images = java.util.ArrayList()

            }

        }


        if (getPins(
                "docPins",
                applicationContext
            ) != null) {
            if (!getPins(
                    "docPins",
                    applicationContext
                )!!.contains(finalPin)) {
                val count: CountDownTimer = object : CountDownTimer(120000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        Snackbar.make(
                            findViewById(R.id.docViewCoordinator),
                            "Wish to add this document to quick access?",
                            Snackbar.LENGTH_INDEFINITE
                        )
                            .setAction("ADD") { v: View? ->
                                pinTheDoc(animation!!, animation1!!, addToPins!!)
                            }.show()
                    }
                }
                count.start()
            }
        }
        mAdView = findViewById(R.id.adView)
        AdMob.bannerAd(this, mAdView!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_INTENT) {
                images = ArrayList()

                if (data!!.clipData == null) {
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
        if (docViewer!!.canGoBack()) {
            docViewer!!.goBack()
        } else {
             if (touchIV.visibility == View.VISIBLE) {
                    touchIV.visibility = View.GONE
            }else
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                bottomSheetBehavior1.state = BottomSheetBehavior.STATE_COLLAPSED

            } else {
                super.onBackPressed()
                finish()
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val extras = intent?.extras
        if (extras != null) {
            val gson = Gson()
            val stkPayLoad = extras.getString("payload")
            val docComment = extras.getString("notification")
            if (stkPayLoad != null && stkPayLoad != "") {
                val mpesaResponse: Transaction = gson.fromJson(stkPayLoad, Transaction::class.java)
                val topicID = mpesaResponse.body.stkCallback.checkoutRequestID
                if (mpesaResponse.body.stkCallback.resultCode != 0) {
                    val cause = mpesaResponse.body.stkCallback.resultDesc
                    val purchaseResults = AlertDialog.Builder(this@DocumentLoader)
                        .setTitle("Transaction failed!")
                        .setIcon(R.drawable.tw__composer_close)
                        .setMessage(cause)
                        .setPositiveButton("Ok") { it: DialogInterface?, _: Int ->
                            it?.dismiss()
                        }
                    purchaseResults.show()

                    val pendingPurchases =
                        loadPendingPurchase(
                            applicationContext
                        )
                    pendingPurchases.forEach {
                        if (it.endsWith(topicID)) {
                            pendingFinalPin = it.split("_checkoutID_")[0]
                            finalPin =
                                pendingFinalPin
                            school = finalPin!!.split("_-_")[0]
                            title1 = finalPin!!.split("_-_")[1]
                            finalLink = finalPin!!.split("_-_")[4]
                            docViewer!!.loadUrl(finalLink!!)
                            documentReference = FirebaseFirestore.getInstance()
                                .collection("Content")
                                .document("Documents")
                                .collection(
                                    replacer(
                                        school
                                    )
                                )
                                .document(
                                    replacer(
                                        title1
                                    )
                                )

                            commentsRef =
                                FirebaseFirestore.getInstance().collection("Content")
                                    .document("Documents")
                                    .collection(
                                        replacer(
                                            school
                                        )
                                    )
                                    .document(
                                        replacer(
                                            title1
                                        )
                                    ).collection("Comments")
                            FirebaseMessaging.getInstance().unsubscribeFromTopic(topicID)
                                .addOnSuccessListener {
                                }
                        }
                    }
                } else {
                    val infoList: List<Transaction.Body.StkCallback.CallbackMetadata.Item> =
                        mpesaResponse.body.stkCallback.callbackMetadata.item

                    var dateOfTransaction = ""
                    var amountTransacted = ""
                    var receiptNo = ""
                    var phoneNumber = ""

                    infoList.forEach { transaction ->
                        if (transaction.name == "MpesaReceiptNumber") {
                            receiptNo = transaction.value
                        }
                        if (transaction.name == "TransactionDate") {
                            dateOfTransaction = transaction.value
                        }
                        if (transaction.name == "PhoneNumber") {
                            phoneNumber = transaction.value
                        }
                        if (transaction.name == "Amount") {
                            amountTransacted = transaction.value
                        }
                    }

                    val pendingPurchases =
                        loadPendingPurchase(
                            applicationContext
                        )
                    pendingPurchases.forEach {
                        if (it.endsWith(topicID)) {
                            pendingFinalPin = it.split("_checkoutID_")[0]
                            finalPin =
                                pendingFinalPin
                            school = finalPin!!.split("_-_")[0]
                            title1 = finalPin!!.split("_-_")[1]
                            finalLink = finalPin!!.split("_-_")[4]
                            pinCount = Integer.parseInt(finalPin!!.split("_-_")[5])
                            comCount = Integer.parseInt(finalPin!!.split("_-_")[6])
                            docViewer!!.loadUrl(finalLink!!)
                            documentReference = FirebaseFirestore.getInstance()
                                .collection("Content")
                                .document("Documents")
                                .collection(
                                    replacer(
                                        school
                                    )
                                )
                                .document(
                                    replacer(
                                        title1
                                    )
                                )

                            commentsRef =
                                FirebaseFirestore.getInstance().collection("Content")
                                    .document("Documents")
                                    .collection(
                                        replacer(
                                            school
                                        )
                                    )
                                    .document(
                                        replacer(
                                            title1
                                        )
                                    ).collection("Comments")
                            val bool =
                                completePurchase(
                                    applicationContext,
                                    pendingFinalPin!!,
                                    animation!!,
                                    addToPins!!
                                )
                            if (bool) {
                                pendingPurchases.remove(it)
                                savePendingPurchase(
                                    applicationContext,
                                    pendingPurchases
                                )
                                FirebaseMessaging.getInstance().unsubscribeFromTopic(topicID)
                                    .addOnSuccessListener {
                                    }
                                val purchaseResults = AlertDialog.Builder(this@DocumentLoader)
                                    .setTitle("Transaction successful.")
                                    .setIcon(R.drawable.ic_check_green)
                                    .setMessage("$amountTransacted paid by (+$phoneNumber) on  $dateOfTransaction\nReceipt id: $receiptNo")
                                    .setPositiveButton("Ok") { it: DialogInterface?, _: Int ->
                                        it!!.dismiss()
                                    }
                                purchaseResults.show()
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "Something's not right..",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }

                }
                comments
                refresh!!.setOnClickListener { v: View? ->
                    comments
                    docViewer!!.loadUrl(finalLink!!)
                }
                addToPins!!.setOnClickListener {
                    pinTheDoc(animation!!, animation1!!, addToPins!!)
                }
            }
            if (docComment != null && docComment != "") {

                try {
                    publisherUid = docComment.split("_-_")[5]
                    school = docComment.split("_-_")[6]
                    title1 = docComment.split("_-_")[7]
                    finalLink = docComment.split("_-_")[8]
                    pinCount = Integer.parseInt(docComment.split("_-_")[9])
                    comCount = Integer.parseInt(docComment.split("_-_")[10])
                    docViewer!!.loadUrl(finalLink!!)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

                    documentReference = FirebaseFirestore.getInstance()
                        .collection("Content")
                        .document("Documents")
                        .collection(
                            replacer(
                                school
                            )
                        )
                        .document(
                            replacer(
                                title1
                            )
                        )

                    commentsRef =
                        FirebaseFirestore.getInstance().collection("Content").document("Documents")
                            .collection(
                                replacer(
                                    school
                                )
                            )
                            .document(
                                replacer(
                                    title1
                                )
                            ).collection("Comments")

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


    private fun postComment(
        user: FirebaseUser,
        docCommentET: EditText,
        mediaUriList: ArrayList<String>?,
        flag: String
    ) {

        val mProgressDialog = ProgressDialog(this)
        mProgressDialog.setMessage("Adding your comment")
        mProgressDialog.setTitle("Please Wait...")
        mProgressDialog.isIndeterminate = true
        mProgressDialog.show()


        val commentsRef = documentReference.collection("Comments")
        val comment = docCommentET.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(comment)) {
            Toast.makeText(applicationContext, "Say something....", Toast.LENGTH_SHORT).show()
        } else {
            val cid = System.currentTimeMillis().toString()
            val refStr = replacer(
                school
            ) + "_-_" + replacer(
                title1
            ) + "_-_Comments_-_" + cid

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
                        + saveCurrentDate + "_-_" + saveCurrentTime + "_-_" + publisherUid + "_-_" + finalPin,
                mediaUriList, System.currentTimeMillis().toString(), "0", "0",
                "0", null, null, refStr
            )

            comments1.add(commentObj)
            val commentsAdapter = CommentsAdapter(this@DocumentLoader, comments1, null)
            commentsAdapter.notifyDataSetChanged()
            docCommentRV!!.adapter = commentsAdapter
            docCommentRV!!.bringToFront()
            docCommentET.setText("")



            if (mediaUriList!!.size == 0) {

                val commentMap1: MutableMap<String, Any> = HashMap()
                commentMap1["comment"] =
                    userName + "_-_" + uId + "_-_" + comment + "_-_" + saveCurrentDate + "_-_" + saveCurrentTime + "_-_" + publisherUid + "_-_" + school + "_-_" + title1 + "_-_" + finalLink + "_-_" + pinCount + "_-_" + comCount
                commentMap1["repliesCount"] = "0"
                commentMap1["upVotes"] = "0"
                commentMap1["downVotes"] = "0"
                commentMap1["uiDs"] = ArrayList<String>()
                commentMap1["imCommentUrls"] = ArrayList<String>()
                commentMap1["cId"] = cid
                commentMap1["ref"] = refStr



                commentsRef.document(cid)
                    .set(commentMap1)
                    .addOnSuccessListener {
                        val map: MutableMap<String, Any> = HashMap()
                        map["commentsCount"] = FieldValue.increment(1)
                        documentReference.update(map)
                        mProgressDialog.dismiss()
                        val commentsAdapter1 = CommentsAdapter(this@DocumentLoader, comments1, null)
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
                                        val commentMap: MutableMap<String, Any> = HashMap()
                                        commentMap["comment"] =
                                            userName + "_-_" + uId + "_-_" + comment + "_-_" + saveCurrentDate + "_-_" + saveCurrentTime + "_-_" + publisherUid + "_-_" + school + "_-_" + title1 + "_-_" + finalLink + "_-_" + pinCount + "_-_" + comCount
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
                                                documentReference.update(map)
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
//                                                val commentsAdapter1 = CommentsAdapter(this@DocumentLoader, comments1)
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

            if (intent.getStringExtra("school") != null) {
                school = intent.getStringExtra("school")
            }
            if (intent.getStringExtra("title") != null) {
                title1 = intent.getStringExtra("title")
            }

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
                        replies = qds.get("repliesCount").toString()
                        upVotes = qds.get("upVotes").toString()
                        downVotes = qds.get("downVotes").toString()
                        uids = qds.get("uiDs") as ArrayList<String>?
                        imUrls = qds.get("imCommentUrls") as ArrayList<String>
                        docId = qds.getString("cId")
                        ref = qds.get("ref").toString()

                        val reference = FirebaseFirestore.getInstance().collection("Content")
                            .document("Documents")
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

                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    }


                }
                comments1.reverse()
                val commentsAdapter = CommentsAdapter(this@DocumentLoader, comments1, touchIV)
                commentsAdapter.notifyDataSetChanged()
                docCommentRV!!.adapter = commentsAdapter
                docCommentRV!!.bringToFront()
            }.addOnFailureListener { e: Exception? -> }
        }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun createWebPrintJob(wv: WebView?) {
        val jobName = "yN_$title1"
        val printAttributes = PrintAttributes.Builder()
            .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
            .setResolution(Resolution("pdf", "pdf", 600, 600))
            .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
            .build()
        val pdfPrint = PdfPrint(printAttributes)
        pdfPrint.print(
            wv!!.createPrintDocumentAdapter(jobName),
            dubDocuments,
            title1
        )
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getPrintPage(webView: WebView) {
        printManager = this.getSystemService(PRINT_SERVICE) as PrintManager
        strDateTime = SimpleDateFormat("yyyy-MM-dd").format(Date())
        strFileName = "File doc $strDateTime"
        printDocumentAdapter = webView.createPrintDocumentAdapter(strFileName)
        printJob =
            printManager.print(strFileName, printDocumentAdapter, PrintAttributes.Builder().build())

    }


    fun performSTKPush(
        phone_number: String?,
        amount: String,
        animation: RotateAnimation,
        addToPins: ImageButton
    ) {
        mProgressDialog!!.setMessage("Processing your request")
        mProgressDialog!!.setTitle("Please Wait...")
        mProgressDialog!!.isIndeterminate = true
        mProgressDialog!!.show()
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
                        try {
                            mProgressDialog!!.dismiss()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        val pendingPurchases =
                            loadPendingPurchase(
                                applicationContext
                            )
                        if (finalPin != null) {
                            pendingPurchases.add(finalPin!! + "_-_" + pinCount + "_-_" + comCount + "_checkoutID_" + result.CheckoutRequestID.toString())
                            savePendingPurchase(
                                applicationContext,
                                pendingPurchases
                            )
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
                mProgressDialog!!.dismiss()
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
//                                this@DocumentLoader, "Response: "
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
//                Toast.makeText(this@DocumentLoader, "Error: " + t.message, Toast.LENGTH_SHORT)
//                    .show()
//            }
//        })

    }

    private fun getAccessToken() {
        mApiClient!!.setGetAccessToken(true)
//        mApiClient!!.mpesaService().accessToken.enqueue(object : Callback<AccessToken?> {
//            override fun onResponse(call: Call<AccessToken?>, response: Response<AccessToken?>) {
//                if (response.isSuccessful) {
//                    if (response.body() != null) {
//                        mApiClient!!.setAuthToken(response.body()!!.accessToken)
//                    } else {
//                        Toast.makeText(
//                            this@DocumentLoader,
//                            "Null body @ getAccessToken",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<AccessToken?>, t: Throwable) {}
//        })
    }

    private fun pinTheDoc(
        animation: RotateAnimation,
        animation1: RotateAnimation,
        addToPins: ImageButton
    ) {
        val linearLayout = LinearLayout(this@DocumentLoader)
        linearLayout.orientation = LinearLayout.VERTICAL
        val lp = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lp.setMargins(50, 0, 50, 100)
        val input = EditText(this@DocumentLoader)
        input.layoutParams = lp
        input.gravity = Gravity.TOP or Gravity.START
        linearLayout.addView(input, lp)
        val otherNumberDialog = AlertDialog.Builder(this@DocumentLoader)
        otherNumberDialog.setTitle("Enter phone number")
        otherNumberDialog.setMessage("Number format: 07....")
        otherNumberDialog.setView(linearLayout)
        otherNumberDialog.setNegativeButton("dismiss") { dialog, which -> dialog.dismiss() }
        otherNumberDialog.setPositiveButton("Proceed!") { dialog, which ->
            if (input.text.toString().trim { it <= ' ' } == "") {
                Toast.makeText(applicationContext, "Can't leave blanks", Toast.LENGTH_SHORT).show()
            } else {
                performSTKPush(
                    input.text.toString().trim(),
                    "1",
                    animation,
                    addToPins
                )

            }
        }

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            val logInSignUp =
                LogInSignUp(this@DocumentLoader)
            logInSignUp.show()
        } else {
            if (pinned != "true") {
                val mProgressDialog = ProgressDialog(this)
                mProgressDialog.setMessage("Processing your request")
                mProgressDialog.setTitle("Please Wait...")
                mProgressDialog.isIndeterminate = true
                mProgressDialog.show()

                var pins = ArrayList<String>()
                try {
                    pins = getPins(
                        "docPins",
                        applicationContext
                    )!!

                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
                if (!pins.contains(finalPin)) {
                    if (pinCount!! >= 20 || comCount!! >= 20) {
                        animation.duration = 1500
                        animation1.duration = 1500
                        addToPins.startAnimation(animation1)

                        val count1: CountDownTimer = object : CountDownTimer(1500, 1000) {
                            override fun onTick(millisUntilFinished: Long) {}
                            override fun onFinish() {
                                mProgressDialog.dismiss()
                                val price = if (pinCount!! > comCount!!) {
                                    DocRetrieval.priceGenerator(pinCount!!)!!
                                } else {
                                    DocRetrieval.priceGenerator(comCount!!)!!
                                }
                                val pinOnPurchase = AlertDialog.Builder(this@DocumentLoader)
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
                                            TypeToken<java.util.ArrayList<String?>?>() {}.type
                                        val userDetail =
                                            gson.fromJson<java.util.ArrayList<String>>(json, type)

                                        if (userDetail != null) {
                                            val phoneNumber = userDetail[12]
                                            val userPhoneNumber =
                                                AlertDialog.Builder(this@DocumentLoader)
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
                                                            "1",
                                                            animation,
                                                            addToPins
                                                        )

                                                    }
                                                    .setNegativeButton("Other") { dialog: DialogInterface, which: Int ->
                                                        dialog.dismiss()
                                                        otherNumberDialog.show()
                                                    }
                                            if (phoneNumber != null || phoneNumber != "") {
                                                userPhoneNumber.show()
                                            } else {
                                                otherNumberDialog.show()
                                            }
                                        }


                                    }
                                    .setNegativeButton("Later") { dialog: DialogInterface, which: Int -> dialog.dismiss() }
                                    .create()

                                pinOnPurchase.show()
                            }
                        }
                        count1.start()

                    } else {
                        pins.add(finalPin!!)
                        val preferences: SharedPreferences =
                            applicationContext.getSharedPreferences("docPins", MODE_PRIVATE)
                        val editor = preferences.edit()
                        val gson = Gson()
                        val json = gson.toJson(pins)
                        editor.putString("docPins", json)
                        editor.apply()
                        val map: MutableMap<String, Any> = HashMap()
                        val map1: MutableMap<String, Any> = HashMap()
                        map["saveCount"] = FieldValue.increment(1)
                        map1["readList"] =
                            FieldValue.arrayUnion(
                                replacer(
                                    school
                                ) + "_-_" + replacer(
                                    title1
                                )
                            )
                        documentReference.update(map)
                        val readList = FirebaseFirestore.getInstance().collection("Users")
                            .document(user.uid)
                            .collection("ScheduledLists")
                            .document("ReadList")


                        readList.get().addOnSuccessListener {
                            if (it.exists()) {
                                readList.update(map1)
                            } else {
                                readList.set(map1)
                            }
                        }
                        val count: CountDownTimer = object : CountDownTimer(1500, 1000) {
                            override fun onTick(millisUntilFinished: Long) {}
                            override fun onFinish() {
                                animation.duration = 1500
                                addToPins.startAnimation(animation)
                                Snackbar.make(
                                    findViewById(R.id.docViewCoordinator),
                                    "Document pinned!",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                mProgressDialog.dismiss()
                            }
                        }
                        count.start()
                    }


                } else {
                    mProgressDialog.dismiss()
                    Snackbar.make(
                        findViewById(R.id.docViewCoordinator),
                        "You are already pinned with this document.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    val areYouSure = AlertDialog.Builder(this)
                        .setTitle("Remove from pins?")
                        .setMessage("Are you done reading this document?")
                        .setNegativeButton("Nope!") { dialog: DialogInterface, _: Int -> dialog.dismiss() }
                        .setPositiveButton(
                            "Yup!"
                        ) { _: DialogInterface?, _: Int ->
                            val map: MutableMap<String, Any> = HashMap()
                            if (pinCount!! > 0) {
                                map["saveCount"] = FieldValue.increment(-1)
                                documentReference.update(map)
                                    .addOnSuccessListener { o: Any? ->
                                        val pins1 =
                                            getPins(
                                                "docPins",
                                                applicationContext
                                            )
                                        pins1!!.remove(finalPin)
                                        val preferences: SharedPreferences =
                                            applicationContext.getSharedPreferences(
                                                "docPins",
                                                MODE_PRIVATE
                                            )
                                        val editor = preferences.edit()
                                        val g = Gson()
                                        val j = g.toJson(pins)
                                        editor.putString("docPins", j)
                                        editor.apply()
                                        Toast.makeText(
                                            applicationContext,
                                            "Document unpinned!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            } else {
                                val pins1 =
                                    getPins(
                                        "docPins",
                                        applicationContext
                                    )
                                pins1!!.remove(finalPin)
                                val preferences: SharedPreferences =
                                    applicationContext.getSharedPreferences(
                                        "docPins",
                                        MODE_PRIVATE
                                    )
                                val editor = preferences.edit()
                                val g = Gson()
                                val j = g.toJson(pins)
                                editor.putString("docPins", j)
                                editor.apply()
                                Toast.makeText(
                                    applicationContext,
                                    "Document unpinned!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    areYouSure.show()
                }
            }

            val pinsRef = FirebaseFirestore.getInstance().collection("Users")
                .document(user.uid)
                .collection("Lists")
                .document("ReadList")

            pinsRef.get().addOnSuccessListener {
                if (it.exists()) {
                    val pins = it.get("readList") as ArrayList<String>
                    if (pins.contains(school + "_-_" + title1)) {
                        Snackbar.make(
                            findViewById(R.id.docViewCoordinator),
                            "You already have a copy.",
                            Snackbar.LENGTH_LONG
                        )
                            .setAction("Check") {
                                val userProducts =
                                    UserProducts(
                                        this@DocumentLoader,
                                        "Read list",
                                        user,
                                        null
                                    )
                                userProducts.show()
                            }.show()

                        mProgressDialog!!.dismiss()
                    } else {
                        pinsRef.update(
                            "readList",
                            FieldValue.arrayUnion(school + "_-_" + title1)
                        )
                            .addOnSuccessListener {
                                Snackbar.make(
                                    findViewById(R.id.docViewCoordinator),
                                    "Read list updated!",
                                    Snackbar.LENGTH_LONG
                                )
                                    .setAction("Check") {
                                        val userProducts =
                                            UserProducts(
                                                this@DocumentLoader,
                                                "Read list",
                                                user,
                                                null
                                            )
                                        userProducts.show()
                                    }.show()

                                mProgressDialog!!.dismiss()
                            }
                    }
                }
            }
        }
    }

    override fun sendingSuccessful(
        transactionAmount: String,
        phoneNumber: String,
        transactionDate: String,
        MPesaReceiptNo: String,
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(
                applicationContext,
                "Transaction Successful\nM-Pesa Receipt No: $MPesaReceiptNo\nTransaction Date: $transactionDate\nTransacting Phone Number: $phoneNumber\nAmount Transacted: $transactionAmount",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun sendingFailed(cause: String) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(
                applicationContext, "Transaction Failed\nReason: $cause", Toast.LENGTH_LONG
            ).show()
        }
    }

//    private fun generatePDF(webView: WebView, docName: String?) {
//        val pdfFile = File(Environment.getExternalStorageDirectory(), docName)
//        try {
//            pdfFile.createNewFile()
//            val outputStream: OutputStream = FileOutputStream(pdfFile)
//            val document = PdfDocument()
//
//            // Get the WebView content as a bitmap
//            val bitmap = Bitmap.createBitmap(
//                webView.width,
//                webView.height,
//                Bitmap.Config.ARGB_8888
//            )
//            var canvas = Canvas(bitmap)
//            webView.draw(canvas)
//
//            // Convert the bitmap to a PDF page
//            val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
//            val page: PdfDocument.Page = document.startPage(pageInfo)
//            canvas = page.canvas
//            canvas.drawBitmap(bitmap, 0, 0, null)
//            document.finishPage(page)
//
//            // Save the PDF file
//            document.writeTo(outputStream)
//            document.close()
//            outputStream.flush()
//            outputStream.close()
//            Toast.makeText(this, "PDF saved to " + pdfFile.absolutePath, Toast.LENGTH_SHORT)
//                .show()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }


//    private fun downloadPdfFromInternet(url: String, dirPath: String, fileName: String) {
//        PRDownloader.initialize(applicationContext)
//
//        PRDownloader.download(
//            url,
//            dirPath,
//            fileName
//        ).build()
//            .start(object : OnDownloadListener {
//                override fun onDownloadComplete() {
//                    Toast.makeText(this@DocumentLoader, "downloadComplete", Toast.LENGTH_LONG)
//                        .show()
//                    val downloadedFile = File(dirPath, fileName)
//                    progressBar.visibility = View.GONE
//                    showPdfFromFile(downloadedFile)
//                }
//
//                override fun onError(error: Error?) {
//                    Toast.makeText(
//                        this@DocumentLoader,
//                        "Error in downloading file : $error",
//                        Toast.LENGTH_LONG
//                    )
//                        .show()                }
//
//            })
//    }
//    private fun showPdfFromFile(file: File) {
//        pdfView.fromFile(file)
//            .password(null)
//            .defaultPage(0)
//            .enableSwipe(true)
//            .swipeHorizontal(false)
//            .enableDoubletap(true)
//            .onPageError { page, _ ->
//                Toast.makeText(
//                    this@PdfViewActivity,
//                    "Error at page: $page", Toast.LENGTH_LONG
//                ).show()
//            }
//            .load()
//    }
}