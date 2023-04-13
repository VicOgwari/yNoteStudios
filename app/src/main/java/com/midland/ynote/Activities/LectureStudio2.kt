package com.midland.ynote.Activities

import android.Manifest
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.hardware.Camera
import android.media.MediaScannerConnection
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.util.SparseIntArray
import android.view.Menu
import android.view.MenuItem
import android.view.Surface
import android.view.View
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.security.ProviderInstaller
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hbisoft.hbrecorder.HBRecorder
import com.hbisoft.hbrecorder.HBRecorderListener
import com.midland.ynote.Adapters.ProjectorAdt
import com.midland.ynote.Adapters.StudioPageAdapter
import com.midland.ynote.Fragments.Documents
import com.midland.ynote.Fragments.StudioBitmaps
import com.midland.ynote.MainActivity
import com.midland.ynote.Objects.LectureObject
import com.midland.ynote.Objects.PendingLecObj
import com.midland.ynote.R
import com.midland.ynote.Services.BackgroundService
import com.midland.ynote.Utilities.*
import com.midland.ynote.Utilities.Paint.PaintSaveViewUtil
import com.midland.ynote.Utilities.Paint.PaintView
import com.priyankvasa.android.cameraviewex.CameraView
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class LectureStudio2 : AppCompatActivity(), ProviderInstaller.ProviderInstallListener,
    HBRecorderListener {
    private lateinit var areYouSure: AlertDialog
    var studio: String? = ""
    private var retryProviderInstall = false

    private val SCREEN_RECORD_REQUEST_CODE = 100
    private val PERMISSION_REQ_ID_RECORD_AUDIO = 101
    private val PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE = 102
    var hbRecorder: HBRecorder? = null
    var btnStart: Button? = null
    var btnStop:Button? = null
    var hasPermissions = false
    var contentValues: ContentValues? = null
    var resolver: ContentResolver? = null
    var mUri: Uri? = null


    companion object {
        private const val move = 200f
        private const val REQUEST_CODE = 1000
        private const val REQUEST_PERMISSION = 1001
        private const val SETTINGS_PERMISSION = 1002
        private const val ERROR_DIALOG_REQUEST_CODE = 1
        private val ORIENTATION = SparseIntArray()
        private const val PICK_IMAGE = 999
        private var DISPLAY_WIDTH = 0
        private var DISPLAY_HEIGHT = 0

        //Timer
        private const val START_TIME_IN_MILLIS: Long = 900000

        init {
            ORIENTATION.append(Surface.ROTATION_0, 90)
            ORIENTATION.append(Surface.ROTATION_90, 0)
            ORIENTATION.append(Surface.ROTATION_180, 270)
            ORIENTATION.append(Surface.ROTATION_270, 180)
        }
    }

    private var record: ImageButton? = null
    private var toggleButton: Button? = null
    private var coordinatorLayout: CoordinatorLayout? = null
    private var videoUri: String? = null
    private var uri: String? = null
    private var pictorialsLocale: String? = null
    private var paintView: PaintView? = null
    private var dialog: AlertDialog? = null
    private var shouWidth: TextView? = null
    private var paintWidth = 0
    private var paint_sheet: CardView? = null
    private var recordLecture: ImageButton? = null
    private var closeDocView: ImageButton? = null
    private var closeCameraView: ImageButton? = null
    private var cameraBtn: ImageButton? = null
    private var blinder: ImageButton? = null
    var pointUri = ArrayList<Uri>()
    private var textViewCountDown: Button? = null
    private var countDownTimer: CountDownTimer? = null
    private var timerRunning = false
    private var timeLeftInMillis = START_TIME_IN_MILLIS
    var fileName: String? = null
    private var switchFragments: ImageButton? = null
    private var backCamera: CameraView? = null
    var lectureObject: LectureObject? = null
    private var preview: FrameLayout? = null
    var toggleCam: ImageButton? = null
    private var projectorRel: RelativeLayout? = null
    private var cameraRel: RelativeLayout? = null
    private val illustrate: ImageButton? = null
    private var projectorRV: RecyclerView? = null
    private var cameraCard: CardView? = null
    private var viewPager: ViewPager? = null
    private var mCamera: Camera? = null
    private var mPreview: CameraPreview? = null
    private val photos = ArrayList<Uri?>()
    private var illustration: Uri? = null
    private var camera: Camera? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture_studio2)
        val toolbar = findViewById<Toolbar>(R.id.studioToolbar)
        toolbar.title = "yNote"
        setSupportActionBar(toolbar)
        ProviderInstaller.installIfNeededAsync(this, this)
        switchFragments = findViewById(R.id.switchFragments)
        cameraBtn = findViewById(R.id.cameraBtn)
        backCamera = findViewById(R.id.outSourcedCamera)
        toggleCam = findViewById(R.id.toggleCam)
        preview = findViewById(R.id.cameraPreview)
        projectorRel = findViewById(R.id.projectorRel)
        cameraRel = findViewById(R.id.cameraRel)
        viewPager = findViewById(R.id.studio_view_pager)
        projectorRV = findViewById(R.id.projectorRV)
        projectorRV!!.layoutManager = LinearLayoutManager(
            applicationContext,
            RecyclerView.HORIZONTAL, false
        )
        cameraCard = findViewById(R.id.cameraCard)
        cameraCard!!.bringToFront()
        if (intent.getStringExtra("studio") != null) {
            studio = intent.getStringExtra("studio")
            if ((studio == "0")) {
                switchFragments!!.visibility = View.GONE
                checkCameraHardware(applicationContext)
                uri = intent.getStringExtra("selectedDoc")
                val pagerAdapter = StudioPageAdapter(
                    supportFragmentManager,
                    FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                    1,
                    uri,
                    pictorialsLocale,
                    "0",
                    applicationContext,
                    this
                )
                viewPager!!.adapter = pagerAdapter
                toggleCam!!.setOnClickListener { _: View? ->
                    if (cameraCard!!.visibility == View.VISIBLE) {
                        findViewById<View>(R.id.camRel).visibility = View.GONE
                        cameraCard!!.visibility = View.GONE
                    } else if (cameraCard!!.visibility == View.GONE) {
                        findViewById<View>(R.id.camRel).visibility = View.VISIBLE
                        cameraCard!!.visibility = View.VISIBLE
                    }
                }
            }
            if ((studio == "1")) {
                switchFragments!!.visibility = View.GONE
                blinder!!.visibility = View.GONE
                checkCameraHardware(applicationContext)
                pictorialsLocale = intent.getStringExtra("bitmapTitle")
                val pagerAdapter = StudioPageAdapter(
                    supportFragmentManager,
                    FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                    1,
                    uri,
                    pictorialsLocale,
                    "1",
                    applicationContext,
                    this
                )
                viewPager!!.adapter = pagerAdapter
                toggleCam!!.setOnClickListener {
                    if (cameraCard!!.visibility == View.VISIBLE) {
                        findViewById<View>(R.id.camRel).visibility = View.GONE
                        cameraCard!!.visibility = View.GONE
                    } else if (cameraCard!!.visibility == View.GONE) {
                        findViewById<View>(R.id.camRel).visibility = View.VISIBLE
                        cameraCard!!.visibility = View.VISIBLE
                    }
                }
            }
            if ((studio == "3")) {
                uri = intent.getStringExtra("selectedDoc")
                cameraBtn!!.visibility = View.GONE
                val pagerAdapter = StudioPageAdapter(
                    supportFragmentManager,
                    FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                    2,
                    uri,
                    pictorialsLocale,
                    "3",
                    applicationContext,
                    this
                )
                viewPager!!.adapter = pagerAdapter
            }
            if ((studio == "4")) {
                cameraBtn!!.visibility = View.GONE
                pictorialsLocale = intent.getStringExtra("bitmapTitle")
                Toast.makeText(this, studio, Toast.LENGTH_SHORT).show()
                val pagerAdapter = StudioPageAdapter(
                    supportFragmentManager,
                    FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                    2,
                    uri,
                    pictorialsLocale,
                    "4",
                    applicationContext,
                    this
                )
                viewPager!!.adapter = pagerAdapter
            }
            if ((studio == "5")) {
                cameraBtn!!.visibility = View.GONE
                pictorialsLocale = intent.getStringExtra("bitmapTitle")
                uri = intent.getStringExtra("selectedDoc")
                Toast.makeText(this, studio, Toast.LENGTH_SHORT).show()
                val pagerAdapter = StudioPageAdapter(
                    supportFragmentManager,
                    FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                    3,
                    uri,
                    pictorialsLocale,
                    "5",
                    applicationContext,
                    this
                )
                viewPager!!.adapter = pagerAdapter
            }
        }
        if ((intent.getStringExtra("selectedDoc") != null
                    && intent.getSerializableExtra("savedLecture") == null)
        ) {
        } else {
            if (intent.getStringExtra("camOnly") != null) {
                cameraBtn!!.visibility = View.GONE
                switchFragments!!.visibility = View.GONE
                blinder!!.visibility = View.GONE
                val pagerAdapter = StudioPageAdapter(
                    supportFragmentManager,
                    FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                    1,
                    uri,
                    pictorialsLocale,
                    "2",
                    this@LectureStudio2,
                    this
                )
                viewPager!!.adapter = pagerAdapter
            } else if (intent.getSerializableExtra("savedLecture") != null) {
                val obj = intent.getSerializableExtra("savedLecture") as PendingLecObj?
                uri = obj!!.sourceDoc
                illustration = Uri.parse(obj.illustrationLocale)
                pictorialsLocale = obj.sourcePictorials
                fileName = obj.pendingLecTitle
                studio = obj.studio
                if (studio == "0") {
                    switchFragments!!.visibility = View.GONE
                    checkCameraHardware(applicationContext)
                    val pagerAdapter =
                        StudioPageAdapter(
                            supportFragmentManager,
                            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                            1,
                            uri,
                            pictorialsLocale,
                            "0",
                            applicationContext,
                            this
                        )
                    viewPager!!.adapter = pagerAdapter
                    toggleCam!!.setOnClickListener {
                        if (cameraCard!!.visibility == View.VISIBLE) {
                            findViewById<View>(R.id.camRel).visibility = View.GONE
                            cameraCard!!.visibility = View.GONE
                        } else if (cameraCard!!.visibility == View.GONE) {
                            findViewById<View>(R.id.camRel).visibility = View.VISIBLE
                            cameraCard!!.visibility = View.VISIBLE
                        }
                    }
                }
                if (studio == "1") {
                    switchFragments!!.visibility = View.GONE
                    checkCameraHardware(applicationContext)
                    val pagerAdapter =
                        StudioPageAdapter(
                            supportFragmentManager,
                            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                            1,
                            uri,
                            pictorialsLocale,
                            "1",
                            applicationContext,
                            this
                        )
                    viewPager!!.adapter = pagerAdapter
                    toggleCam!!.setOnClickListener {
                        if (cameraCard!!.visibility == View.VISIBLE) {
                            findViewById<View>(R.id.camRel).visibility = View.GONE
                            cameraCard!!.visibility = View.GONE
                        } else if (cameraCard!!.visibility == View.GONE) {
                            findViewById<View>(R.id.camRel).visibility = View.VISIBLE
                            cameraCard!!.visibility = View.VISIBLE
                        }
                    }
                }
                if (studio == "3") {
                    cameraBtn!!.visibility = View.GONE
                    val pagerAdapter =
                        StudioPageAdapter(
                            supportFragmentManager,
                            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                            2,
                            uri,
                            pictorialsLocale,
                            "3",
                            applicationContext,
                            this
                        )
                    viewPager!!.adapter = pagerAdapter
                }
                if (studio == "4") {
                    cameraBtn!!.visibility = View.GONE
                    val pagerAdapter =
                        StudioPageAdapter(
                            supportFragmentManager,
                            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                            2,
                            uri,
                            pictorialsLocale,
                            "4",
                            applicationContext,
                            this
                        )
                    viewPager!!.adapter = pagerAdapter
                }
                if (studio == "5") {
                    cameraBtn!!.visibility = View.GONE
                    val pagerAdapter =
                        StudioPageAdapter(
                            supportFragmentManager,
                            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                            3,
                            uri,
                            pictorialsLocale,
                            "5",
                            applicationContext,
                            this
                        )
                    viewPager!!.adapter = pagerAdapter
                }
                if (studio == "camOnly") {
                    cameraBtn!!.visibility = View.GONE
                    switchFragments!!.visibility = View.GONE
                    val pagerAdapter =
                        StudioPageAdapter(
                            supportFragmentManager,
                            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                            1,
                            uri,
                            pictorialsLocale,
                            "2",
                            this@LectureStudio2,
                            this
                        )
                    viewPager!!.adapter = pagerAdapter
                }
            }
        }
        if (intent.getStringExtra("fileName") != null) {
            fileName = intent.getStringExtra("fileName")
        }
        textViewCountDown = findViewById(R.id.text_countDown)
        updateCountDownText()
        toggleButton = findViewById(R.id.toggleButton)
        hbRecorder = HBRecorder(this, this)
        hbRecorder!!.setVideoEncoder("H264")
        hbRecorder!!.fileName = fileName
        record = findViewById(R.id.record)
        coordinatorLayout = findViewById(R.id.rootLayout1)
        val paintScrollView = findViewById<CustomScrollView>(R.id.paintScrollView)
        val paintHorizontalView = findViewById<CustomHorizontalScrollView>(R.id.paintHorizontalView)
        val matrix = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(matrix)



        val intent = Intent(this, BackgroundService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        }

        //PAINT
        initView()
        areYouSure = AlertDialog.Builder(this@LectureStudio2)
            .setTitle("Exiting studio.")
            .setMessage("Your are leaving the studio. Do you wish to save your lecture? Saving will half the dimensions of your illustration.")
            .setPositiveButton("Yes") { _: DialogInterface?, _: Int ->
                val areYourReallySure = AlertDialog.Builder(this@LectureStudio2)
                    .setTitle("Are you sure?")
                    .setTitle("Click yes to exit.")
                    .setPositiveButton("Yes") { dialog1: DialogInterface, _: Int ->
                        dialog1.dismiss()
                        val intent = Intent(applicationContext, BackgroundService::class.java)
                        stopService(intent)
                        if (checkAndRequestPermission()) {
                            paintView!!.saveBitmap(applicationContext)
                            val illustrationName: String =
                                PaintSaveViewUtil.getIllustrationName()
                            val uri1: Uri = PaintSaveViewUtil.getUri()
                            val callForDate: Calendar = Calendar.getInstance()
                            val currentDate: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
                            val saveCurrentDate: String = currentDate.format(callForDate.time)
                            val pendingLecObj: PendingLecObj
                            if (getIntent().getStringExtra("camOnly") != null) {
                                pendingLecObj =
                                    PendingLecObj(
                                        uri,
                                        pictorialsLocale,
                                        uri1.toString(),
                                        fileName,
                                        saveCurrentDate, "camOnly"
                                    )
                            } else {
                                pendingLecObj =
                                    PendingLecObj(
                                        uri,
                                        pictorialsLocale,
                                        uri1.toString(),
                                        fileName,
                                        saveCurrentDate, studio
                                    )
                            }
                            if (pendingLecturesObj() == null) {
                                val pendingLecObjs: ArrayList<PendingLecObj> = ArrayList()
                                pendingLecObjs.add(pendingLecObj)
                                saveLecture(
                                    applicationContext,
                                    "pendingLectures",
                                    pendingLecObjs
                                )
                            } else {
                                val pendingLecObjs1: ArrayList<PendingLecObj>? = pendingLecturesObj()
                                for (obj: PendingLecObj in pendingLecObjs1!!) {
                                    if (obj.pendingLecTitle != null && obj.pendingLecTitle == fileName) {
                                        pendingLecObjs1.remove(obj)
                                    }
                                }
                                pendingLecObjs1.add(pendingLecObj)
                                saveLecture(
                                    applicationContext,
                                    "pendingLectures",
                                    pendingLecObjs1
                                )
                            }

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (Settings.System.canWrite(applicationContext)) {
                                    if ((ContextCompat.checkSelfPermission(
                                            applicationContext,
                                            Manifest.permission.WRITE_SETTINGS
                                        )
                                                != PackageManager.PERMISSION_GRANTED)
                                    ) {
                                        requestPermissions(
                                            arrayOf(Manifest.permission.WRITE_SETTINGS),
                                            SETTINGS_PERMISSION
                                        )
                                    } else {
                                        Settings.System.putInt(
                                            applicationContext.contentResolver,
                                            "show_touches", 1
                                        )
                                    }
                                }
                            }
                            startActivity(Intent(this@LectureStudio2, Production::class.java))
                            finish()
                        }
                    }
                    .setNegativeButton("No") { dialog1: DialogInterface, _: Int ->
                        dialog1.dismiss()
                    }.create()
                areYourReallySure.show()
            }
            .setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
                val areYourReallySure = AlertDialog.Builder(this@LectureStudio2)
                    .setTitle("Are you sure?")
                    .setTitle("Click yes to exit.")
                    .setPositiveButton("Yes") { dialog1: DialogInterface, _: Int ->
                        dialog1.dismiss()
                        startActivity(Intent(this@LectureStudio2, MainActivity::class.java))
                        finish()
                    }
                    .setNegativeButton("No") { dialog1: DialogInterface, _: Int ->
                        dialog1.dismiss()
                    }.create()
                areYourReallySure.show()
            }
            .create()
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        paintView!!.init(metrics, 2500, 2500)
        val scrollMode = findViewById<ImageButton>(R.id.scrollMode)

        var flag = "Stop"
        toggleButton!!.setOnClickListener {
            if (flag == "Stop") {
                flag = "Record"
                if (checkSelfPermission(
                        Manifest.permission.RECORD_AUDIO, PERMISSION_REQ_ID_RECORD_AUDIO
                    ) && checkSelfPermission(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE
                    )
                ) {
                    hasPermissions = true
                }
                if (hasPermissions) {
                    startRecordingScreen()
                    toggleButton!!.text = "Stop"
                }
            }else{
                hbRecorder!!.stopScreenRecording()
                areYouSure.show()
            }
        }
        scrollMode.setOnClickListener {
            if (paintScrollView.isEnableScrolling && paintHorizontalView.isEnableScrolling) {
                scrollMode.setImageResource(R.drawable.ic_lock)
                paintScrollView.isEnableScrolling = false
                paintHorizontalView.isEnableScrolling = false
                Toast.makeText(applicationContext, "disabled", Toast.LENGTH_SHORT).show()
            } else {
                scrollMode.setImageResource(R.drawable.ic_lock_open)
                paintScrollView.isEnableScrolling = true
                paintHorizontalView.isEnableScrolling = true
                Toast.makeText(applicationContext, "enabled", Toast.LENGTH_SHORT).show()
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(applicationContext)) {
                if ((ContextCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.WRITE_SETTINGS
                    )
                            != PackageManager.PERMISSION_GRANTED)
                ) {
                    requestPermissions(
                        arrayOf(Manifest.permission.WRITE_SETTINGS),
                        SETTINGS_PERMISSION
                    )
                } else {
                    Settings.System.putInt(
                        applicationContext.contentResolver,
                        "show_touches", 1
                    )
                }
            }
        }
    }


    override fun HBRecorderOnStart() {
        Toast.makeText(this, "Started", Toast.LENGTH_SHORT).show()
    }

    override fun HBRecorderOnComplete() {
        Toast.makeText(this, "Completed", Toast.LENGTH_SHORT).show()
        //Update gallery depending on SDK Level
        if (hbRecorder!!.wasUriSet()) {
            updateGalleryUri()
        } else {
            refreshGalleryFile()
        }
    }

    override fun HBRecorderOnError(errorCode: Int, reason: String) {
        Toast.makeText(this, "$errorCode: $reason", Toast.LENGTH_SHORT).show()
    }

    private fun startRecordingScreen() {
        val mediaProjectionManager =
            getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        val permissionIntent = mediaProjectionManager.createScreenCaptureIntent()
        startActivityForResult(permissionIntent, SCREEN_RECORD_REQUEST_CODE)
    }

    private fun setOutputPath() {
        val filename = System.currentTimeMillis().toString()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            resolver = contentResolver
            contentValues = ContentValues()
            contentValues!!.put(MediaStore.Video.Media.RELATIVE_PATH, "yNoteStudios/" + "CompletedLectures")
            contentValues!!.put(MediaStore.Video.Media.TITLE, filename)
            contentValues!!.put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            contentValues!!.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            mUri = resolver!!.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues)
            //FILE NAME SHOULD BE THE SAME
            hbRecorder!!.fileName = filename
            hbRecorder!!.setOutputUri(mUri)
        } else {
            createFolder()
            hbRecorder!!.setOutputPath(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
                    .toString() + "/HBRecorder"
            )
        }
    }

    private fun checkSelfPermission(permission: String, requestCode: Int): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            return false
        }
        return true
    }

    private fun updateGalleryUri() {
        contentValues!!.clear()
        contentValues!!.put(MediaStore.Video.Media.IS_PENDING, 0)
        contentResolver.update(mUri!!, contentValues, null, null)
    }

    private fun refreshGalleryFile() {
        MediaScannerConnection.scanFile(
            this, arrayOf(hbRecorder!!.filePath), null
        ) { path, uri ->
            Log.i("ExternalStorage", "Scanned $path:")
            Log.i("ExternalStorage", "-> uri=$uri")
        }
    }

    //Generate a timestamp to be used as a file name
    private fun generateFileName(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault())
        val curDate = Date(System.currentTimeMillis())
        return formatter.format(curDate).replace(" ", "")
    }

    private fun drawable2ByteArray(@DrawableRes drawableId: Int): ByteArray? {
        val icon = BitmapFactory.decodeResource(resources, drawableId)
        val stream = ByteArrayOutputStream()
        icon.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun createFolder() {
        val f1 = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),
            "CompletedLectures"
        )
        if (!f1.exists()) {
            if (f1.mkdirs()) {
                Log.i("Folder ", "created")
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SCREEN_RECORD_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                hbRecorder!!.startScreenRecording(data, resultCode, this)
                startTimer()
            }
        }
        if (requestCode == 767 && resultCode == RESULT_OK && data != null) {
            if (data.clipData == null) {
                photos.add(data.data)
            } else {
                for (q in 0 until data.clipData!!.itemCount) {
                    photos.add(data.clipData!!.getItemAt(q).uri)
                }
            }
            val projectorAdt = ProjectorAdt(
                applicationContext,
                photos
            )
            projectorAdt.notifyDataSetChanged()
            projectorRV!!.adapter = projectorAdt
        }

    }

    override fun onResume() {
        super.onResume()
        val studio = intent.getStringExtra("studio")
        if (intent.getSerializableExtra("savedLecture") == null) {
            uri = intent.getStringExtra("selectedDoc")
            pictorialsLocale = intent.getStringExtra("bitmapTitle")
        } else {
            val obj = (intent.getSerializableExtra("savedLecture") as PendingLecObj?)!!
            uri = obj.sourceDoc
            if (studio != null) {
                if (studio == "2") {
                    illustration = Uri.parse(obj.illustrationLocale)
                }
            }
            pictorialsLocale = obj.sourcePictorials
        }


//        StudioPageAdapter pagerAdapter = null;
//
//        if (studio != null) {
//
//            if (studio.equals("1")) {
//                pagerAdapter = new StudioPageAdapter(getSupportFragmentManager()
//                        , FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, 2, uri, pictorialsLocale, getIntent().getStringExtra("studio"), getApplicationContext());
//
//            } else if (studio.equals("2")) {
//                pagerAdapter = new StudioPageAdapter(getSupportFragmentManager()
//                        , FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, 3, uri, pictorialsLocale, getIntent().getStringExtra("studio"), getApplicationContext());
//
//            }
//
//        }
//
//        viewPager.setAdapter(pagerAdapter);
    }

    override fun onProviderInstalled() {}
    override fun onProviderInstallFailed(i: Int, intent: Intent?) {
        val apiAvailability = GoogleApiAvailability.getInstance()
        if (apiAvailability.isUserResolvableError(i)) {
            // Recoverable error. Show a dialog prompting the user to
            // install/update/enable Google Play services.
            apiAvailability.showErrorDialogFragment(
                this,
                i,
                ERROR_DIALOG_REQUEST_CODE
            ) {
                // The user chose not to take the recovery action
                onProviderInstallerNotAvailable()
            }
        } else {
            // Google Play services is not available.
            onProviderInstallerNotAvailable()
        }
    }

    override fun onPostResume() {
        super.onPostResume()
        if (retryProviderInstall) {
            // We can now safely retry installation.
            ProviderInstaller.installIfNeededAsync(this, this)
        }
        retryProviderInstall = false
    }

    private fun onProviderInstallerNotAvailable() {
        // This is reached if the provider cannot be updated for some reason.
        // App should consider all HTTP communication to be vulnerable, and take
        // appropriate action.
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION -> {

            }
            SETTINGS_PERMISSION -> {
                Settings.System.putInt(
                    applicationContext.contentResolver,
                    "show_touches", 1
                )
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.paint_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.illustrate -> paint_sheet!!.visibility = View.VISIBLE
            R.id.notes -> {
                val notesPopUp = NotesPopUp(
                    this@LectureStudio2,
                    parent,
                    applicationContext,
                    uri!!,
                    fileName!!,
                    "LectureStudio"
                )
                notesPopUp.show()
            }
            R.id.blinders -> blinders()
            R.id.studyMode -> {}
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        val intent = Intent(applicationContext, BackgroundService::class.java)
        stopService(intent)
    }

    private fun blinders() {
        if (findViewById<View>(R.id.topBlinder).visibility == View.GONE && findViewById<View>(R.id.topBlinder2).visibility == View.GONE) {
            findViewById<View>(R.id.topBlinder).visibility = View.VISIBLE
            findViewById<View>(R.id.bottomBlinder).visibility = View.VISIBLE
            findViewById<View>(R.id.topBlinder2).visibility = View.GONE
            findViewById<View>(R.id.bottomBlinder2).visibility = View.GONE
        } else
            if (findViewById<View>(R.id.topBlinder).visibility == View.VISIBLE && findViewById<View>(
                    R.id.topBlinder2
                ).visibility == View.GONE
            ) {
                findViewById<View>(R.id.topBlinder).visibility = View.GONE
                findViewById<View>(R.id.bottomBlinder).visibility = View.GONE
                findViewById<View>(R.id.topBlinder2).visibility = View.VISIBLE
                findViewById<View>(R.id.bottomBlinder2).visibility = View.VISIBLE
            } else
                if (findViewById<View>(R.id.topBlinder2).visibility == View.VISIBLE && findViewById<View>(
                        R.id.topBlinder
                    ).visibility == View.GONE
                ) {
                    findViewById<View>(R.id.topBlinder).visibility = View.GONE
                    findViewById<View>(R.id.bottomBlinder).visibility = View.GONE
                    findViewById<View>(R.id.topBlinder2).visibility = View.GONE
                    findViewById<View>(R.id.bottomBlinder2).visibility = View.GONE
                }
    }

    private fun initView() {
        backCamera!!.bringToFront()
        backCamera!!.addCameraOpenedListener {

        }
            .addCameraClosedListener {

            }
            .addCameraErrorListener { t, _ ->
                Toast.makeText(
                    applicationContext,
                    t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        backCamera!!.start()
        //SCREEN RECORDER


        //Get Screen
//        DISPLAY_HEIGHT = matrix.heightPixels;
//        DISPLAY_WIDTH = matrix.widthPixels;


        //GETS INTENT FROM PICS GRID CLASS TO  NOTES POP UP FOR RV ADAPTER
        val intent = intent
        if (intent.getStringExtra("SelectedPic") != null) {
            pointUri.add(Uri.parse(intent.getStringExtra("SelectedPic")))
        }
        val dialogView = layoutInflater.inflate(R.layout.dialog_width_set, null)
        shouWidth = dialogView.findViewById(R.id.textView1)
        val widthSb = dialogView.findViewById<SeekBar>(R.id.seekBar1)
        widthSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                shouWidth!!.text = "Current Width：" + (progress + 1)
                paintWidth = progress + 1
            }
        })
        paintView = findViewById(R.id.paintView)
        if (illustration != null) {
            processBitmap(illustration, paintView)
        }
        val illustrate = findViewById<ImageButton>(R.id.illustrate)
        val brushSize = findViewById<ImageButton>(R.id.brushSize)
        val brushSizeTV = findViewById<TextView>(R.id.brushSizeTV)
        val brushTypeTV = findViewById<TextView>(R.id.brushTypeTV)
        closeDocView = findViewById(R.id.closeDocView)
        closeCameraView = findViewById(R.id.closeCameraView)
        blinder = findViewById(R.id.blinders)
        val addImage = findViewById<FloatingActionButton>(R.id.addImage)
        recordLecture = findViewById(R.id.record)
        val background = findViewById<ImageButton>(R.id.background)
        val black = findViewById<ImageButton>(R.id.black)
        val red = findViewById<ImageButton>(R.id.red)
        val blue = findViewById<ImageButton>(R.id.blue)
        val green = findViewById<ImageButton>(R.id.green)
        val yellow = findViewById<ImageButton>(R.id.yellow)
        val purple = findViewById<ImageButton>(R.id.purple)
        val redo = findViewById<ImageButton>(R.id.redo)
        val undo = findViewById<ImageButton>(R.id.undo)
        val projector = findViewById<ImageButton>(R.id.projector)
        val eraser = findViewById<ImageButton>(R.id.eraser)
        val saveBitmap = findViewById<ImageButton>(R.id.saveBitmap)
        val brushType = findViewById<ImageButton>(R.id.brushType)
        val indicator = findViewById<ImageButton>(R.id.indicator)
        val closePaintView = findViewById<ImageButton>(R.id.closePaintView)
        paint_sheet = findViewById(R.id.paint_sheet)
        dialog = AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_info)
            .setTitle("Set the size of your Pen").setView(dialogView)
            .setPositiveButton("Confirm") { _, _ ->
                paintView!!.setPaintWidth(paintWidth)
                brushSizeTV.text = "$paintWidth / 30"
            }.setNegativeButton("Cancel", null).create()
        closeDocView!!.setOnClickListener {
            projectorRel!!.visibility = View.GONE
        }
        closeCameraView!!.setOnClickListener {
            cameraRel!!.visibility = View.GONE
        }
        projector.setOnClickListener {
            if (projectorRel!!.visibility == View.VISIBLE) {
                projectorRel!!.visibility = View.GONE
            } else {
                projectorRel!!.visibility = View.VISIBLE
            }
        }
        addImage.setOnClickListener {
            val intent1 = Intent()
            intent1.type = "image/*"
            intent1.action = Intent.ACTION_GET_CONTENT
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                intent1.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
            startActivityForResult(Intent.createChooser(intent1, "Lecture aid item(s)"), 767)
        }
        closePaintView.setOnClickListener { paint_sheet!!.visibility = View.GONE }



        cameraBtn!!.setOnClickListener {
            if (cameraRel!!.visibility == View.VISIBLE) {
                cameraRel!!.visibility = View.GONE
            } else {
                cameraRel!!.visibility = View.VISIBLE
            }
        }
        blinder!!.setOnClickListener {
            blinders()
        }
        switchFragments!!.setOnClickListener {
            if ((getIntent().getStringExtra("studio") == "1")) {
                if (viewPager!!.currentItem == 0) {
                    viewPager!!.currentItem = 1
                } else if (viewPager!!.currentItem == 1) {
                    viewPager!!.currentItem = 0
                }
            } else if ((getIntent().getStringExtra("studio") == "2")) {
                when (viewPager!!.currentItem) {
                    0 -> {
                        viewPager!!.currentItem = 1
                    }
                    1 -> {
                        viewPager!!.currentItem = 2
                    }
                    2 -> {
                        viewPager!!.currentItem = 0
                    }
                }
            }
        }
        illustrate.setOnClickListener {
            if (paint_sheet!!.visibility == View.VISIBLE) {
                paint_sheet!!.visibility = View.GONE
            } else {
                paint_sheet!!.visibility = View.VISIBLE
            }
        }
        toggleCam!!.setOnClickListener {
            if (cameraCard!!.visibility == View.VISIBLE) {
                findViewById<View>(R.id.camRel).visibility = View.GONE
                cameraCard!!.visibility = View.GONE
            } else if (cameraCard!!.visibility == View.GONE) {
                findViewById<View>(R.id.camRel).visibility = View.VISIBLE
                cameraCard!!.visibility = View.VISIBLE
            }
        }
        brushType.setOnClickListener {
            val popupMenu = PopupMenu(this@LectureStudio2, brushType)
            popupMenu.inflate(R.menu.brush_type_menu)
            popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                paintView!!.disableEraser()
                when (item.itemId) {
                    R.id.emboss -> {
                        paintView!!.emboss()
                        brushTypeTV.text = "Emboss"
                    }
                    R.id.blur -> {
                        paintView!!.blur()
                        brushTypeTV.text = "Blur"
                    }
                    R.id.normal -> {
                        paintView!!.normal()
                        brushTypeTV.text = "Normal"
                    }
                    R.id.clear -> paintView!!.clear()
                }
                false
            }
            popupMenu.show()
        }
        background.setOnClickListener {
            paintView!!.setBackgroundClr()
        }
        black.setOnClickListener {
            paintView!!.setColor(Color.BLACK)
            indicator.setImageResource(R.drawable.ic_black)
            Toast.makeText(applicationContext, "Black..", Toast.LENGTH_SHORT).show()
        }
        red.setOnClickListener {
            paintView!!.setColor(Color.RED)
            indicator.setImageResource(R.drawable.ic_red)
            Toast.makeText(applicationContext, "Red..", Toast.LENGTH_SHORT).show()
        }
        blue.setOnClickListener {
            paintView!!.setColor(Color.BLUE)
            indicator.setImageResource(R.drawable.ic_blue)
            Toast.makeText(applicationContext, "Blue..", Toast.LENGTH_SHORT).show()
        }
        yellow.setOnClickListener {
            paintView!!.setColor(Color.YELLOW)
            indicator.setImageResource(R.drawable.ic_yellow)
            Toast.makeText(applicationContext, "Yellow..", Toast.LENGTH_SHORT).show()
        }
        green.setOnClickListener {
            paintView!!.setColor(Color.GREEN)
            indicator.setImageResource(R.drawable.ic_green)
            Toast.makeText(applicationContext, "Green..", Toast.LENGTH_SHORT).show()
        }
        purple.setOnClickListener {
            paintView!!.setColor(Color.MAGENTA)
            indicator.setImageResource(R.drawable.ic_purple)
            Toast.makeText(applicationContext, "Magenta..", Toast.LENGTH_SHORT).show()
        }
        indicator.setOnClickListener {
            Toast.makeText(
                this@LectureStudio2,
                "Ink indicator..",
                Toast.LENGTH_SHORT
            ).show()
        }
        brushSize.setOnClickListener { dialog!!.show() }
        eraser.setOnClickListener {
//            paintView!!.enableEraser()
            paintView!!.setColor(Color.BLACK)
            indicator.setImageResource(R.drawable.ic_black)
            Toast.makeText(applicationContext, "Black..", Toast.LENGTH_SHORT).show()
        }
        undo.setOnClickListener { paintView!!.undo() }
        redo.setOnClickListener { paintView!!.redo() }
        saveBitmap.setOnClickListener { paintView!!.saveBitmap(applicationContext) }
    }

    //TIMER CODE
    private fun resetTimer() {
        timeLeftInMillis = START_TIME_IN_MILLIS
        updateCountDownText()
    }

    //java.lang.NullPointerException: Attempt to invoke virtual method 'void android.os.CountDownTimer.cancel()' on a null object reference
    private fun pauseTimer() {
        countDownTimer!!.cancel()
        timerRunning = false
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                timerRunning = false
                hbRecorder!!.stopScreenRecording()
                areYouSure.show()
            }
        }.start()
        timerRunning = true
    }

    private fun updateCountDownText() {
        val minutes = (timeLeftInMillis / 1000).toInt() / 60
        val seconds = (timeLeftInMillis / 1000).toInt() % 60
        when (minutes) {
            9 -> {
                textViewCountDown!!.setBackgroundColor(Color.BLUE)
                textViewCountDown!!.setBackgroundColor(Color.GREEN)
                textViewCountDown!!.setBackgroundColor(Color.YELLOW)
                textViewCountDown!!.setBackgroundColor(Color.BLACK)
                textViewCountDown!!.setBackgroundColor(Color.CYAN)
                textViewCountDown!!.setBackgroundColor(Color.MAGENTA)
                textViewCountDown!!.setBackgroundColor(Color.LTGRAY)
                textViewCountDown!!.setBackgroundColor(Color.DKGRAY)
                textViewCountDown!!.setBackgroundColor(Color.RED)
            }
            8 -> {
                textViewCountDown!!.setBackgroundColor(Color.GREEN)
                textViewCountDown!!.setBackgroundColor(Color.YELLOW)
                textViewCountDown!!.setBackgroundColor(Color.BLACK)
                textViewCountDown!!.setBackgroundColor(Color.CYAN)
                textViewCountDown!!.setBackgroundColor(Color.MAGENTA)
                textViewCountDown!!.setBackgroundColor(Color.LTGRAY)
                textViewCountDown!!.setBackgroundColor(Color.DKGRAY)
                textViewCountDown!!.setBackgroundColor(Color.RED)
            }
            7 -> {
                textViewCountDown!!.setBackgroundColor(Color.YELLOW)
                textViewCountDown!!.setBackgroundColor(Color.BLACK)
                textViewCountDown!!.setBackgroundColor(Color.CYAN)
                textViewCountDown!!.setBackgroundColor(Color.MAGENTA)
                textViewCountDown!!.setBackgroundColor(Color.LTGRAY)
                textViewCountDown!!.setBackgroundColor(Color.DKGRAY)
                textViewCountDown!!.setBackgroundColor(Color.RED)
            }
            6 -> {
                textViewCountDown!!.setBackgroundColor(Color.BLACK)
                textViewCountDown!!.setBackgroundColor(Color.CYAN)
                textViewCountDown!!.setBackgroundColor(Color.MAGENTA)
                textViewCountDown!!.setBackgroundColor(Color.LTGRAY)
                textViewCountDown!!.setBackgroundColor(Color.DKGRAY)
                textViewCountDown!!.setBackgroundColor(Color.RED)
            }
            5 -> {
                textViewCountDown!!.setBackgroundColor(Color.CYAN)
                textViewCountDown!!.setBackgroundColor(Color.MAGENTA)
                textViewCountDown!!.setBackgroundColor(Color.LTGRAY)
                textViewCountDown!!.setBackgroundColor(Color.DKGRAY)
                textViewCountDown!!.setBackgroundColor(Color.RED)
            }
            4 -> {
                textViewCountDown!!.setBackgroundColor(Color.MAGENTA)
                textViewCountDown!!.setBackgroundColor(Color.LTGRAY)
                textViewCountDown!!.setBackgroundColor(Color.DKGRAY)
                textViewCountDown!!.setBackgroundColor(Color.RED)
            }
            3 -> {
                textViewCountDown!!.setBackgroundColor(Color.LTGRAY)
                textViewCountDown!!.setBackgroundColor(Color.DKGRAY)
                textViewCountDown!!.setBackgroundColor(Color.RED)
            }
            2 -> {
                textViewCountDown!!.setBackgroundColor(Color.DKGRAY)
                textViewCountDown!!.setBackgroundColor(Color.RED)
            }
            1 -> textViewCountDown!!.setBackgroundColor(Color.RED)
        }
        val timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        textViewCountDown!!.text = timeLeftFormatted
    }

    /**
     * Check if this device has a camera
     */
    private fun checkCameraHardware(context: Context): Boolean {
        return if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            // Create an instance of Camera
            mCamera = openFrontFacingCameraGingerbread()
            mCamera!!.setDisplayOrientation(90)
            // Create our Preview view and set it as the content of our activity.
            mPreview = CameraPreview(this, mCamera)
            preview!!.addView(mPreview)

            true
        } else {
            Toast.makeText(
                applicationContext,
                "Couldn't find a camera",
                Toast.LENGTH_SHORT
            ).show()
            // no camera on this device
            false
        }
    }


    private fun releaseCamPrev() {
        if (camera != null) {
            camera!!.release()
            camera = null
        }
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    private fun openFrontFacingCameraGingerbread(): Camera? {
        var cameraCount = 0
        var camera: Camera? = null
        val cameraInfo = Camera.CameraInfo()
        cameraCount = Camera.getNumberOfCameras()
        for (camIdx in 0 until cameraCount) {
            Camera.getCameraInfo(camIdx, cameraInfo)
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    camera = Camera.open(camIdx)
                } catch (e: RuntimeException) {
                    e.printStackTrace()
                }
            }
        }
        return camera
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    override fun onBackPressed() {
        if (StudioBitmaps.touchIV != null && StudioBitmaps.touchIV!!.visibility == View.VISIBLE) {
            StudioBitmaps.touchIV!!.visibility = View.GONE
        } else {
            areYouSure.show()
        }

    }


    override fun onStop() {
        super.onStop()
        val illustrationName = PaintSaveViewUtil.getIllustrationName()
        val uri = PaintSaveViewUtil.getUri()
    }

    private fun processBitmap(illustration: Uri?, paintView: PaintView?) {
//        String[] filePath = {MediaStore.Images.Media.DATA};
//        Cursor cursor = getContentResolver().query(illustration, filePath, null, null, null);
//        cursor.moveToFirst();
//        String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
        val imagePath = RealPathUtil.getRealPath( this@LectureStudio2, illustration!!)
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        val bitmap = BitmapFactory.decodeFile(imagePath, options)
        paintView!!.setImage(bitmap, 2500, 2500)
        //        cursor.close();
    }

    private fun checkAndRequestPermission(): Boolean {
        val readPermission = ContextCompat.checkSelfPermission(
            this@LectureStudio2,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val writePermission = ContextCompat.checkSelfPermission(
            this@LectureStudio2,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val audioPermission =
            ContextCompat.checkSelfPermission(this@LectureStudio2, Manifest.permission.RECORD_AUDIO)
        val cameraPermission =
            ContextCompat.checkSelfPermission(this@LectureStudio2, Manifest.permission.CAMERA)
        val neededPermission: MutableList<String> = ArrayList()
        if (readPermission != PackageManager.PERMISSION_GRANTED) {
            neededPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            neededPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (audioPermission != PackageManager.PERMISSION_GRANTED) {
            neededPermission.add(Manifest.permission.RECORD_AUDIO)
        }
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            neededPermission.add(Manifest.permission.CAMERA)
        }
        if (neededPermission.isNotEmpty()) {
            Snackbar.make(coordinatorLayout!!, "Permission", Snackbar.LENGTH_INDEFINITE)
                .setAction("ENABLE") {
                    ActivityCompat.requestPermissions(
                        parent,
                        neededPermission.toTypedArray(),
                        Documents.REQUEST_MULTI_PERMISSION_ID
                    )
                }
                .show()
            return false
        }
        return true
    }

    private fun saveLecture(
        con: Context,
        lectureTitle: String?,
        lectureDetails: ArrayList<PendingLecObj>?
    ) {
        val preferences = con.getSharedPreferences(lectureTitle, MODE_PRIVATE)
        val editor = preferences.edit()
        val gson = Gson()
        val json = gson.toJson(lectureDetails)
        editor.putString(lectureTitle, json)
        editor.apply()
        Snackbar.make(coordinatorLayout!!, "Progress saved!", Snackbar.LENGTH_SHORT).show()
    }

    private fun pendingLecturesObj(): ArrayList<PendingLecObj>? {
        val preferences = applicationContext.getSharedPreferences("pendingLectures", MODE_PRIVATE)
        val json = preferences.getString("pendingLectures", "")
        val gson = Gson()
        val type = object : TypeToken<ArrayList<PendingLecObj?>?>() {}.type
        return gson.fromJson(json, type)
    }


}