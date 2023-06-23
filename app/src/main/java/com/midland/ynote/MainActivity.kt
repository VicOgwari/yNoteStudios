package com.midland.ynote

import android.Manifest
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.provider.MediaStore
import android.text.InputType
import android.view.*
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidstudy.daraja.Daraja
import com.androidstudy.daraja.DarajaListener
import com.androidstudy.daraja.model.LNMExpress
import com.androidstudy.daraja.model.LNMResult
import com.androidstudy.daraja.util.Env
import com.androidstudy.daraja.util.TransactionType
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.security.ProviderInstaller
import com.google.android.gms.security.ProviderInstaller.ProviderInstallListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.midland.ynote.Activities.DocumentLoader
import com.midland.ynote.Activities.EditProfile
import com.midland.ynote.Activities.GeneralSearch
import com.midland.ynote.Activities.UserProfile2
import com.midland.ynote.Adapters.SchoolsAdapter
import com.midland.ynote.Dialogs.LogInSignUp
import com.midland.ynote.Dialogs.UserProducts
import com.midland.ynote.Fragments.Documents
import com.midland.ynote.Fragments.MainFragment1
import com.midland.ynote.Objects.Schools
import com.midland.ynote.Objects.SourceDocObject
import com.midland.ynote.Utilities.Constants
import com.midland.ynote.Utilities.MpesaListener
import com.midland.ynote.Utilities.Transaction
import com.midland.ynote.stkPush.AccessToken
import com.midland.ynote.stkPush.DarajaApiClient
import com.midland.ynote.stkPush.Utils
import com.ortiz.touchview.TouchImageView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.net.URLEncoder
import java.util.*


class MainActivity : AppCompatActivity(), ProviderInstallListener, MpesaListener {

    private var mProgressDialog: ProgressDialog? = null
    private var mApiClient: DarajaApiClient? = null
    private var mAppBarConfiguration: AppBarConfiguration? = null
    private var bottomSheetBehavior: BottomSheetBehavior<*>? = null
    private var user: FirebaseUser? = null
    var coachesStr: String? = null
    var studentsStr: String? = null
    var cQ: String? = null
    var profileUrl: String? = null
    var course: String? = null
    var fcmToken: String? = null
    var institution: String? = null
    var school: String? = null
    var gender: String? = null
    var alias: String? = null
    var phoneNumber: String? = null
    var email: String? = null
    var fullName: String? = null
    var coverArt: String? = null
    var about: String? = null
    var publishedSch: List<String>? = ArrayList()
    var uploadedSch: List<String>? = ArrayList()
    var userDetails: ArrayList<String?>? = null
    var registeredAs: ArrayList<String>? = ArrayList()
    var appUpdateManager: AppUpdateManager? = null
    var newSchools0: ArrayList<Schools>? = null
    var newSchools1: ArrayList<Schools>? = null
    private var retryProviderInstall = false
    val DAYS_FOR_FLEXIBLE_UPDATE = 14
    val MY_REQUEST_CODE = 9090
    private val BUSINESS_SHORT_CODE = "174379"
    private lateinit var daraja: Daraja

    @JvmName("setNotifications1")
    fun setNotifications(notifications: ArrayList<ArrayList<String>>?) {
        this.notifications = notifications
    }

    var notifications: ArrayList<ArrayList<String>>? = null


    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mpesaListener = this

        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)

        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager!!.appUpdateInfo

        // Checks whether the platform allows the specified type of update,
        // and current version staleness.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && (appUpdateInfo.clientVersionStalenessDays() ?: -1) >= DAYS_FOR_FLEXIBLE_UPDATE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                // Request the update.
                appUpdateManager!!.startUpdateFlowForResult(
                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,
                    // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                    AppUpdateType.IMMEDIATE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    MY_REQUEST_CODE
                )
            }
        }

        // Create a listener to track request state updates.
        val listener = InstallStateUpdatedListener { state ->
            if (state.installStatus() == InstallStatus.DOWNLOADING) {
                val bytesDownloaded = state.bytesDownloaded()
                val totalBytesToDownload = state.totalBytesToDownload()
                // Show update progress bar.
            }
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate(appUpdateManager!!)
            }
        }

        // Before starting an update, register a listener for updates.
        appUpdateManager!!.registerListener(listener)

        // Start an update.

        // When status updates are no longer needed, unregister the listener.
        appUpdateManager!!.unregisterListener(listener)


        notifications = ArrayList()
        user = FirebaseAuth.getInstance().currentUser
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        mApiClient = DarajaApiClient()
        mProgressDialog = ProgressDialog(this)
        mApiClient!!.setIsDebug(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            accessToken
        } else {
            Toast.makeText(this, "Your device can't support this feature...", Toast.LENGTH_SHORT)
                .show()
        }


        //Update the security provider when the activity is created.
        ProviderInstaller.installIfNeededAsync(this, this)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        val bottomSheet = findViewById<View>(R.id.profile_sheet)
        val navImage = findViewById<ImageView>(R.id.navImage)
        val userNameNav = findViewById<TextView>(R.id.userNameNav)
        val ynsNav = findViewById<TextView>(R.id.ynsNav)
        val coaches = findViewById<TextView>(R.id.coachesCount)
        val students = findViewById<TextView>(R.id.studCount)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        val card1 = findViewById<CardView>(R.id.card1)
        val card2 = findViewById<CardView>(R.id.card2)
        val card3 = findViewById<CardView>(R.id.card3)
        val card4 = findViewById<CardView>(R.id.card4)
        val card5 = findViewById<CardView>(R.id.card5)
        val card6 = findViewById<CardView>(R.id.card6)
        val card7 = findViewById<CardView>(R.id.card7)
        val userName = findViewById<TextView>(R.id.userName)
        val userEmail = findViewById<TextView>(R.id.userEmail)
        val philosophy = findViewById<TextView>(R.id.philosophy)
        val userPreview = findViewById<CircleImageView>(R.id.userPreview)
        val educationRV = findViewById<RecyclerView>(R.id.educationRV)
        val popularRV = findViewById<RecyclerView>(R.id.popularRV)
        touchIV = findViewById(R.id.touchIV)
        close = findViewById(R.id.close)
        card1.bringToFront()
        card2.bringToFront()
        card3.bringToFront()
        card4.bringToFront()
        card5.bringToFront()
        card6.bringToFront()
        card7.bringToFront()
        close!!.setOnClickListener {
            touchIV!!.visibility = View.GONE
            close!!.visibility = View.GONE
        }
        popularRV.layoutManager =
            LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        educationRV.layoutManager =
            LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        val schools = SchoolsAdapter(
            this@MainActivity,
            setUpSchools(),
            "navDrawer"
        )
        schools.notifyDataSetChanged()
        educationRV.adapter = schools
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        //        ImageView notification = findViewById(R.id.notifications);
//        CircleImageView profile = findViewById(R.id.profile);
        setSupportActionBar(toolbar)
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        if (user != null) {
            val mail = user!!.email
            if (mail != null) {
                val mailName = mail.split("@".toRegex()).toTypedArray()[0]
                userEmail.text = "$mailName@ynStudios"
                ynsNav.text = "$mailName@yns.co.ke"
                userName.text = user!!.displayName
                userNameNav.text = user!!.displayName
            }
            val thisUser = FirebaseFirestore.getInstance().collection("Users")
                .document(user!!.uid)
            thisUser.get()
                .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                    philosophy.text = "...."
                    if (documentSnapshot.exists()) {
                        alias = documentSnapshot.getString("alias")
                        phoneNumber = documentSnapshot.getString("phoneNumber")
                        fullName = documentSnapshot.getString("fullName")
                        coachesStr = documentSnapshot["coaches"].toString()
                        studentsStr = documentSnapshot["students"].toString()
                        about = documentSnapshot.getString("about")
                        registeredAs = documentSnapshot["registeredAs"] as ArrayList<String>?
                        institution = documentSnapshot.getString("institution")
                        school = documentSnapshot.getString("school")
                        course = documentSnapshot.getString("course")
                        fcmToken = documentSnapshot.getString("fcmToken")
                        profileUrl = documentSnapshot.getString("profilePicture")
                        coverArt = documentSnapshot.getString("coverArt")
                        gender = documentSnapshot.getString("gender")
                        email = documentSnapshot.getString("email")
                        publishedSch = documentSnapshot["schoolsPublished"] as List<String>?
                        uploadedSch = documentSnapshot["schoolsUploaded"] as List<String>?

                        if (fcmToken == null || fcmToken.equals("")) {
                            getFcmToken(thisUser)
                        }
                        userDetails = ArrayList()
                        userDetails!!.add(alias)
                        userDetails!!.add(fullName)
                        userDetails!!.add(coachesStr)
                        userDetails!!.add(studentsStr)
                        userDetails!!.add(about)
                        //                            userDetails.add(registeredAs);
                        userDetails!!.add(institution)
                        userDetails!!.add(school)
                        userDetails!!.add(course)
                        userDetails!!.add(profileUrl)
                        userDetails!!.add(coverArt)
                        userDetails!!.add(gender)
                        userDetails!!.add(email)
                        userDetails!!.add(phoneNumber)
                        val preferences =
                            applicationContext.getSharedPreferences("User", MODE_PRIVATE)
                        val editor = preferences.edit()
                        val gson = Gson()
                        val json = gson.toJson(userDetails)
                        editor.putString("User", json)
                        editor.apply()
                        if (about == null) {
                            philosophy.text =
                                "For without strong philosophy to live off from, of what purpose is man?"
                        } else {
                            philosophy.text = about
                        }
                        coaches.text = "$coachesStr Coaches"
                        students.text = "$studentsStr Students"
                        if (profileUrl != null) {
                            Glide.with(applicationContext).load(Uri.parse(profileUrl))
                                .thumbnail(0.5f)
                                .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop()
                                .placeholder(R.drawable.ic_account_circle).into(navImage)
                            Glide.with(applicationContext).load(Uri.parse(profileUrl))
                                .thumbnail(0.5f)
                                .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop()
                                .placeholder(R.drawable.ic_account_circle).into(userPreview)
                        } else {
                            Picasso.get().load(R.drawable.ic_account_circle)
                                .placeholder(R.drawable.ic_account_circle).into(userPreview)
                        }
                    }
                }.addOnFailureListener { e: Exception? ->
                    philosophy.text = "...."
                }
        }
        userPreview.setOnClickListener { v: View? -> }
        card1.setOnClickListener { v: View? ->
            val intent: Intent
            if (philosophy.text.toString() == "...") {
                Toast.makeText(applicationContext, "Give me a sec!", Toast.LENGTH_SHORT).show()
            } else {
                if (alias == null || institution == null || school == null || gender == null) {
                    intent = Intent(applicationContext, EditProfile::class.java)
                    intent.putExtra(
                        "otherSignUp",
                        "You still are required to provide a little bit more information regarding your studies. " +
                                "Otherwise, you will be signed out automatically."
                    )
                } else {
                    intent = Intent(applicationContext, UserProfile2::class.java)
                }
                startActivity(intent)
            }
        }
        card2.setOnClickListener { v: View? ->
            val userProducts = UserProducts(
                this@MainActivity,
                "Read list",
                user,
                null
            )
            userProducts.show()
        }
        card3.setOnClickListener { v: View? ->
            val userProducts = UserProducts(
                this@MainActivity,
                "Watch list",
                user,
                null
            )
            userProducts.show()
        }
        card4.setOnClickListener { v: View? ->
            val userProducts = UserProducts(
                this@MainActivity,
                "Pending lectures",
                user,
                null
            )
            userProducts.show()
        }
        card5.setOnClickListener { v: View? ->
            if (checkAndRequestPermission()) {
                val userProducts = UserProducts(
                    this@MainActivity,
                    "Completed lectures",
                    user,
                    null
                )
                userProducts.show()
            }
        }
        card6.setOnClickListener { v: View? ->
            if (publishedSch != null) {
                val userProducts = UserProducts(
                    this@MainActivity,
                    "Published documents",
                    user,
                    publishedSch as ArrayList<String>?
                )
                userProducts.show()
            } else {
            }
        }
        card7.setOnClickListener { v: View? ->
            if (uploadedSch != null) {
                val userProducts = UserProducts(
                    this@MainActivity,
                    "Published lectures",
                    user,
                    uploadedSch as ArrayList<String>?
                )
                userProducts.show()
            } else {
            }
        }



//        NavDrawerAdapter profileAdt = new NavDrawerAdapter("profiles", FirebaseMessaging.Notifications.getNotifications(), getApplicationContext());

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = AppBarConfiguration.Builder(
            R.id.nav_home, R.id.nav_gallery
        )
            .setDrawerLayout(drawer)
            .build()
        val navController = findNavController(this, R.id.nav_host_fragment)
        setupActionBarWithNavController(this, navController, mAppBarConfiguration!!)
        setupWithNavController(navigationView, navController)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            1
        )
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            1
        )

        daraja = Daraja.with(
            Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET, Env.SANDBOX,
            object : DarajaListener<com.androidstudy.daraja.model.AccessToken> {
                override fun onError(error: String?) {
                }

                override fun onResult(result: com.androidstudy.daraja.model.AccessToken) {
                }
            })

//        if (checkAndRequestReadPermission()) {
//            if (ContextCompat.checkSelfPermission(
//                    applicationContext,
//                    Manifest.permission.READ_EXTERNAL_STORAGE
//                )
//                != PackageManager.PERMISSION_GRANTED
//            ) {
//                requestPermissions(
//                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//                    READ_PERMISSION
//                )
//            } else {
////                getFileObjects();
////                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
////                int pendingIntentID = 1234;
////                PendingIntent pendingIntent = PendingIntent.getParent()(getApplicationContext(), pendingIntentID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
////                AlarmManager mgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
////                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);
////                System.exit(0);
//            }
//            file = applicationContext.getExternalFilesDir("yNoteStudios")!!
//            if (!file.exists()) {
//                Toast.makeText(applicationContext, "Couldn't create folders!", Toast.LENGTH_SHORT)
//                    .show()
//                FilingSystem.createRelevantFolders()
//            }
//
//        }

       askNotificationPermission()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val extras = intent?.extras
        if (extras != null) {
            val gson = Gson()
            val donation = extras.getString("notification")
            if (donation != null && donation != "") {
                val mpesaResponse: Transaction = gson.fromJson(donation, Transaction::class.java)
                val topicID = mpesaResponse.body.stkCallback.checkoutRequestID
                if (mpesaResponse.body.stkCallback.resultCode != 0) {
                    val cause = mpesaResponse.body.stkCallback.resultDesc
                    val purchaseResults = AlertDialog.Builder(this@MainActivity)
                        .setTitle("Couldn't process your donation")
                        .setIcon(R.drawable.tw__composer_close)
                        .setMessage(cause)
                        .setPositiveButton("Ok") { it: DialogInterface?, _: Int ->
                            it?.dismiss()
                        }
                    purchaseResults.show()

                } else {

                    user = FirebaseAuth.getInstance().currentUser
                    val userDonations = FirebaseFirestore.getInstance().collection("Users")
                        .document(user!!.uid).collection("Donations")
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
                    val donationMessage =
                        "$amountTransacted donated by (+$phoneNumber) on  $dateOfTransaction\nReceipt id: $receiptNo"
                    userDonations.document(System.currentTimeMillis().toString())
                        .set(donationMessage)
                    val purchaseResults = AlertDialog.Builder(this@MainActivity)
                        .setTitle("We will not forget this.")
                        .setIcon(R.drawable.ic_check_green)
                        .setMessage(donationMessage)
                        .setPositiveButton("Ok") { it: DialogInterface?, _: Int ->
                            it!!.dismiss()
                        }
                    purchaseResults.show()

                }

            }
        }
    }

    override fun onResume() {
        super.onResume()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        appUpdateManager!!
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                // If the update is downloaded but not installed,
                // notify the user to complete the update.
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate(appUpdateManager!!)
                }
                if (appUpdateInfo.updateAvailability()
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                ) {
                    // If an in-app update is already running, resume the update.
                    appUpdateManager!!.startUpdateFlowForResult(
                        appUpdateInfo,
                        IMMEDIATE,
                        this,
                        MY_REQUEST_CODE
                    )
                }
            }
    }

    private fun popupSnackbarForCompleteUpdate(appUpdateManager: AppUpdateManager) {
        Snackbar.make(
            findViewById(R.id.drawer_layout1),
            "An update has just been downloaded.",
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction("RESTART") { appUpdateManager.completeUpdate() }
            show()
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                Snackbar.make(
                    findViewById(R.id.rootLayout1),
                    "Permission",
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction(
                        "ENABLE"
                    ) {
                        ActivityCompat.requestPermissions(
                            this@MainActivity, arrayOf(
                                Manifest.permission.POST_NOTIFICATIONS
                            ), 7
                        )
                    }.show()
            } else {
                ActivityCompat.requestPermissions(
                    this@MainActivity, arrayOf(
                        Manifest.permission.POST_NOTIFICATIONS
                    ), 7
                )
            }
        }
    }

    //STORING ARRAY LISTS OF CUSTOM OBJECTS TO MEMORY
    //    private void saveFile(){
    //        SharedPreferences sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE);
    //        SharedPreferences.Editor editor = sharedPreferences.edit();
    //        Gson gson = new Gson();

    //        String json = gson.toJson(dfdfdf);
    //        editor.putString("task list", json);
    //    }
    //
    //    private void loadData(){
    //        SharedPreferences sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE);
    //        Gson gson = new Gson();
    //        String json = sharedPreferences.getString("task list", null);
    //        Type type = new TypeToken<ArrayList<PictorialObject>>() {}.getType();
    //        dfdfdf = gson.fromJson(json, type);
    //
    //    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        menu.getItem(1).setIcon(R.drawable.biology_medicine_dna)
        return true
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (bottomSheetBehavior!!.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val user = FirebaseAuth.getInstance().currentUser
        when (item.itemId) {
            R.id.action_donations -> {
                if (FirebaseAuth.getInstance().currentUser != null) {
                    val linearLayout = LinearLayout(this@MainActivity)
                    linearLayout.orientation = LinearLayout.VERTICAL
                    val lp = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    lp.setMargins(50, 0, 50, 100)
                    val input = EditText(this@MainActivity)
                    input.layoutParams = lp
                    input.gravity = Gravity.TOP or Gravity.START
                    input.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
                    linearLayout.addView(input, lp)
                    val addReply = AlertDialog.Builder(this@MainActivity)
                    addReply.setTitle("Y-note Studios Donations")
                    addReply.setMessage(
                        "Support the development of better educational tools of the future"
                                + "\n\nInput format:\nPhone Number, Amount\ne.g 0700000000, 50"
                    )
                    addReply.setView(linearLayout)
                    addReply.setNegativeButton("dismiss") { dialog, _ -> dialog.dismiss() }
                    addReply.setPositiveButton("Proceed") { dialog, _ ->
                        val donation = input.text.toString().trim()
                        if (donation == "" || donation.split(",").size != 2) {
                            Toast.makeText(applicationContext, "Blank reply", Toast.LENGTH_LONG)
                                .show()
                            dialog.dismiss()
                        } else {
                            performSTKPush(donation.split(",")[0], donation.split(",")[1])
                        }
                    }
                    addReply.show()
                } else {
                    LogInSignUp(this@MainActivity).show()
                }
            }
            R.id.action_disclaimer -> {
                mProgressDialog!!.setMessage("Processing your request")
                mProgressDialog!!.setTitle("Please Wait...")
                mProgressDialog!!.isIndeterminate = true
                mProgressDialog!!.show()
                FirebaseFirestore.getInstance().collection("Content")
                    .document("Documents")
                    .collection("yNoteDocs")
                    .document("Disclaimerpdf").get().addOnSuccessListener {
                        if (it.exists()) {
                            val mD = it.getString("docMetaData")
                            val dL = it.getString("docDownloadLink")
                            val sC = it.get("saveCount").toString()
                            val cC = it.get("commentsCount").toString()
                            val finalLink = "http://docs.google.com/gview?embedded=true&url=" +
                                    URLEncoder.encode(dL!!.split("_-_")[0])
                            val endColor: Int =
                                mD!!.split("_-_".toRegex()).toTypedArray()[10].toInt()

                            val intent = Intent(applicationContext, DocumentLoader::class.java)
                            intent.putExtra(
                                "school",
                                mD.split("_-_".toRegex()).toTypedArray()[0]
                            )
                            intent.putExtra(
                                "publisherUid",
                                mD.split("_-_".toRegex()).toTypedArray()[8]
                            )
                            intent.putExtra("title", mD.split("_-_".toRegex()).toTypedArray()[5])
                            intent.putExtra("pinCount", sC)
                            intent.putExtra("comCount", cC)
                            intent.putExtra("docUrl", finalLink)
                            intent.putExtra("endColor", endColor.toString())
                            intent.putExtra("publisher", "N/A")
                            startActivity(intent)
                        } else {
                            Toast.makeText(applicationContext, "Nothing found.", Toast.LENGTH_SHORT)
                                .show()
                        }
                        mProgressDialog!!.dismiss()

                    }
                    .addOnFailureListener {
                        mProgressDialog!!.dismiss()
                        Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                    }

            }
            R.id.action_suggestions -> {
                if (user == null) {
                    LogInSignUp(this@MainActivity).show()
                } else {
                    val linearLayout = LinearLayout(this@MainActivity)
                    linearLayout.orientation = LinearLayout.VERTICAL
                    val lp = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    lp.setMargins(50, 0, 50, 100)
                    val input = EditText(this@MainActivity)
                    input.layoutParams = lp
                    input.gravity = Gravity.TOP or Gravity.START
                    linearLayout.addView(input, lp)
                    val addReply = AlertDialog.Builder(this@MainActivity)
                    addReply.setTitle("Creating better products.")
                    addReply.setMessage("Control the development of future versions of yNote Studios.")
                    addReply.setView(linearLayout)
                    addReply.setNegativeButton("dismiss") { dialog, which -> dialog.dismiss() }
                    addReply.setPositiveButton("suggest") { dialog, which ->
                        val suggestion = HashMap<String, String>()
                        suggestion["suggestion"] = input.text.toString().trim()
                        FirebaseFirestore.getInstance().collection("Development Suggestions")
                            .document(System.currentTimeMillis().toString()).set(suggestion)
                            .addOnSuccessListener {
                                Toast.makeText(applicationContext, "Noted!", Toast.LENGTH_LONG)
                                    .show()
                                dialog.dismiss()
                            }
                    }
                    addReply.show()
                }

            }
            R.id.notification -> {
                val preferences: SharedPreferences =
                    applicationContext.getSharedPreferences("User", MODE_PRIVATE)
                val json = preferences.getString("User", "")
                val gson = Gson()
                val type = object : TypeToken<ArrayList<String?>?>() {}.type
                val userDetail = gson.fromJson<ArrayList<String>>(json, type)

                val intent = Intent(applicationContext, GeneralSearch::class.java)
                if (userDetail != null) {
                    intent.putExtra("userSch", userDetail[6])
                }

                startActivity(intent)
            }
            R.id.profile -> {
                if (user != null) {
                    if (bottomSheetBehavior!!.state == BottomSheetBehavior.STATE_COLLAPSED) {
                        bottomSheetBehavior!!.setState(BottomSheetBehavior.STATE_EXPANDED)
                    } else if (bottomSheetBehavior!!.state == BottomSheetBehavior.STATE_EXPANDED) {
                        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                } else {
                    val logInSignUp =
                        LogInSignUp(this@MainActivity)
                    logInSignUp.show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(this, R.id.nav_host_fragment)
        return (navigateUp(navController, mAppBarConfiguration!!)
                || super.onSupportNavigateUp())
    }

    private fun checkAndRequestReadPermission(): Boolean {
        val readPermission = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val writePermission = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val setPermission = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.WRITE_SETTINGS
        )
        val neededPermission: MutableList<String> = ArrayList()
        if (readPermission != PackageManager.PERMISSION_GRANTED) {
            neededPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            neededPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (setPermission != PackageManager.PERMISSION_GRANTED) {
            neededPermission.add(Manifest.permission.WRITE_SETTINGS)
        }
        return true
    }

    //USED AS ARGUMENTS FOR CURSOR
    private val fileObjects:

    //Where

    //args
            ArrayList<SourceDocObject>
        get() {
            val uri = MediaStore.Files.getContentUri("external")
            val projection = arrayOf(MediaStore.Files.FileColumns.DATA)
            var c: Cursor? = null
            val dirList: SortedSet<String> = TreeSet()
            val sourceDocObjects = ArrayList<SourceDocObject>()
            var directories: Array<String?>? = null
            val orderBy = MediaStore.Images.Media.DATE_TAKEN

            //USED AS ARGUMENTS FOR CURSOR
            val pdf = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf")
            val doc = MimeTypeMap.getSingleton().getMimeTypeFromExtension("doc")
            val docx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("docx")
            val xls = MimeTypeMap.getSingleton().getMimeTypeFromExtension("xls")
            val xlsx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("xlsx")
            val ppt = MimeTypeMap.getSingleton().getMimeTypeFromExtension("ppt")
            val pptx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pptx")
            val txt = MimeTypeMap.getSingleton().getMimeTypeFromExtension("txt")
            val rtx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("rtx")
            val rtf = MimeTypeMap.getSingleton().getMimeTypeFromExtension("rtf")
            val html = MimeTypeMap.getSingleton().getMimeTypeFromExtension("html")

            //Where
            val where = (MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                    + " OR " + MediaStore.Files.FileColumns.MIME_TYPE
                    + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE
                    + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE
                    + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE
                    + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE
                    + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE
                    + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                    + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?" + " OR "
                    + MediaStore.Files.FileColumns.MIME_TYPE + "=?" + " OR "
                    + MediaStore.Files.FileColumns.MIME_TYPE + "=?")

            //args
            val args = arrayOf(pdf)
            if (uri != null) {
                c = applicationContext.contentResolver.query(uri, projection, where, args, orderBy)
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
//                dirList.toArray<String>(directories)
            }
            for (i in dirList.indices) {
                val imageDir = File(directories!![i])
                var docList = imageDir.listFiles()
                if (imageDir == null) continue
                if (docList != null) for (docPath in docList!!) {
                    try {
                        if (docPath.isDirectory) {
                            docList = docPath.listFiles()
                        }
                        val docFile = docPath.absoluteFile
                        val docUri = Uri.fromFile(docFile)
                        val fileSize = (docFile.length() / 1024).toString().toInt()
                        val sourceDocObject =
                            SourceDocObject(
                                docFile.name,
                                Uri.fromFile(docFile),
                                fileSize.toString(),
                                docFile.lastModified().toString(),
                                docUri,
                                docFile.lastModified()
                            )
                        if (sourceDocObject.name.endsWith("pdf") || sourceDocObject.name.endsWith("PDF")
                            || sourceDocObject.name.endsWith("doc") || sourceDocObject.name.endsWith(
                                "DOC"
                            )
                            || sourceDocObject.name.endsWith("docx") || sourceDocObject.name.endsWith(
                                "DOCX"
                            )
                            || sourceDocObject.name.endsWith("ppt") || sourceDocObject.name.endsWith(
                                "PPT"
                            )
                            || sourceDocObject.name.endsWith("pptx") || sourceDocObject.name.endsWith(
                                "PPTX"
                            )
                        ) {
                            sourceDocObjects.add(sourceDocObject)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            return sourceDocObjects
        }

    override fun onBackPressed() {
        if (MainFragment1.touchIV!!.visibility == View.VISIBLE || touchIV!!.visibility == View.VISIBLE) {
            MainFragment1.touchIV!!.visibility = View.GONE
            touchIV!!.visibility = View.GONE
            close!!.visibility = View.GONE
        } else {
            val count = supportFragmentManager.backStackEntryCount
            if (count == 0) {
                super.onBackPressed()
            } else {
                supportFragmentManager.popBackStack()
            }
        }
    }

    /**
     * This method is only called if the provider is successfully updated
     * (or is already up-to-date).
     */
    override fun onProviderInstalled() {
        // Provider is up-to-date, app can make secure network calls.
    }

    /**
     * This method is called if updating fails; the error code indicates
     * whether the error is recoverable.
     */
    override fun onProviderInstallFailed(i: Int, intent: Intent?) {
        val apiAvailability = GoogleApiAvailability.getInstance()
        if (apiAvailability.isUserResolvableError(i)) {
            // Recoverable error. Show a dialog prompting the user to
            // install/update/enable Google Play services.
            apiAvailability.showErrorDialogFragment(
                this,
                i,
                ERROR_DIALOG_REQUEST_CODE
            ) { // The user chose not to take the recovery action
                onProviderInstallerNotAvailable()
            }
        } else {
            // Google Play services is not available.
            onProviderInstallerNotAvailable()
        }
    }

    /**
     * On resume, check to see if we flagged that we need to reinstall the
     * provider.
     */
    override fun onPostResume() {
        super.onPostResume()
        if (retryProviderInstall) {
            // We can now safely retry installation.
            ProviderInstaller.installIfNeededAsync(this, this)
        }
        retryProviderInstall = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ERROR_DIALOG_REQUEST_CODE) {
            // Adding a fragment via GoogleApiAvailability.showErrorDialogFragment
            // before the instance state is restored throws an error. So instead,
            // set a flag here, which will cause the fragment to delay until
            // onPostResume.
            retryProviderInstall = true
        }
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
        }
    }

    override fun onPointerCaptureChanged(hasCapture: Boolean) {}
    private fun onProviderInstallerNotAvailable() {
        // This is reached if the provider cannot be updated for some reason.
        // App should consider all HTTP communication to be vulnerable, and take
        // appropriate action.
    }

    private val accessToken: Unit
        get() {
            mApiClient!!.setGetAccessToken(true)
            mApiClient!!.mpesaService().accessToken.enqueue(object : Callback<AccessToken?> {
                override fun onResponse(
                    call: Call<AccessToken?>,
                    response: Response<AccessToken?>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            mApiClient!!.setAuthToken(response.body()!!.accessToken)
                        } else {

                        }
                    }
                }

                override fun onFailure(call: Call<AccessToken?>, t: Throwable) {}
            })
        }


    private fun performSTKPush(phone_number: String?, amount: String) {
        mProgressDialog!!.setMessage("Processing your request")
        mProgressDialog!!.setTitle("Please Wait...")
        mProgressDialog!!.isIndeterminate = true
        mProgressDialog!!.show()


        val lnmExpress = LNMExpress(
            BUSINESS_SHORT_CODE,
            Constants.PASSKEY1,
            TransactionType.CustomerPayBillOnline,
            amount.trim(),
            Utils.sanitizePhoneNumber(phone_number!!.trim()),
            Constants.PARTYB,
            Utils.sanitizePhoneNumber(phone_number.trim()),
            Constants.CALLBACKURL,
            "yNote Studios",
            "Public Donations"
        )

        daraja.requestMPESAExpress(lnmExpress, object : DarajaListener<LNMResult> {
            override fun onResult(result: LNMResult) {
                FirebaseMessaging.getInstance()
                    .subscribeToTopic(result.CheckoutRequestID.toString())
                    .addOnSuccessListener {
                        Toast.makeText(applicationContext, "Processing..", Toast.LENGTH_LONG).show()
                        try {
                            mProgressDialog!!.dismiss()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }
            }

            override fun onError(error: String?) {
                Toast.makeText(applicationContext, "An Error Occurred: $error", Toast.LENGTH_SHORT)
                    .show()
                mProgressDialog!!.dismiss()
            }
        })

    }

    @JvmName("getFcmToken1")
    fun getFcmToken(thisUser: DocumentReference): String? {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task: Task<String> ->
            if (!task.isSuccessful) {
                Toast.makeText(
                    this,
                    "FCM's missing...",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                fcmToken = task.result
                val userMap1: MutableMap<String, Any> = HashMap()
                userMap1["fcmToken"] = fcmToken!!
                thisUser.update(userMap1).addOnSuccessListener {
                }
            }
        }.addOnFailureListener { e: java.lang.Exception ->
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            Toast.makeText(this, "FCM2's missing...", Toast.LENGTH_SHORT)
                .show()
        }
        return fcmToken
    }


    fun setUpSchools(): ArrayList<Schools> {
        val schools = ArrayList<Schools>()
        val school1 = Schools(
            "Agriculture & Enterprise Development",
            R.drawable.agriculture_nature_vegetable,
            null,
            null
        )
        schools.add(school1)
        val school2 = Schools(
            "Applied Human Sciences",
            R.drawable.health,
            null,
            null
        )
        schools.add(school2)
        val school3 = Schools(
            "Business",
            R.drawable.business_economics_finance,
            null,
            null
        )
        schools.add(school3)
        val school4 = Schools(
            "Economics",
            R.drawable.business_economics_currency,
            null,
            null
        )
        schools.add(school4)
        val school5 = Schools(
            "Education",
            R.drawable.education_class,
            null,
            null
        )
        schools.add(school5)
        val school6 = Schools(
            "Engineering & Technology",
            R.drawable.engineering_electrical_energy_innovation,
            null,
            null
        )
        schools.add(school6)
        val school7 = Schools(
            "Environmental Studies",
            R.drawable.energy_plant,
            null,
            null
        )
        schools.add(school7)
        val school8 = Schools(
            "Hospitality & Tourism",
            R.drawable.hospitality_claw,
            null,
            null
        )
        schools.add(school8)
        val school9 = Schools(
            "Humanities & Social Sciences",
            R.drawable.humanities,
            null,
            null
        )
        schools.add(school9)
        val school10 = Schools(
            "Law",
            R.drawable.law_book,
            null,
            null
        )
        schools.add(school10)
        val school11 = Schools(
            "Medicine",
            R.drawable.medicine_health_hygieia1,
            null,
            null
        )
        schools.add(school11)
        val school12 = Schools(
            "Public Health",
            R.drawable.coronavirus,
            null,
            null
        )
        schools.add(school12)
        val school13 = Schools(
            "Pure & Applied Sciences",
            R.drawable.chemistry_network,
            null,
            null
        )
        schools.add(school13)
        val school14 =
            Schools(
                "Visual & Performing Art",
                R.drawable.theatre_shakespeare,
                null,
                null
            )
        schools.add(school14)
        val school15 = Schools(
            "Confucius Institute",
            R.drawable.philosophy_socrates,
            null,
            null
        )
        schools.add(school15)
        val school16 = Schools(
            "Peace & Security Studies",
            R.drawable.education_class,
            null,
            null
        )
        schools.add(school16)
        val school17 = Schools(
            "Creative Arts, Film & Media Studies",
            R.drawable.theatre_movie_camera,
            null,
            null
        )
        schools.add(school17)
        val school18 = Schools(
            "Architecture",
            R.drawable.architecture_sketch,
            null,
            null
        )
        schools.add(school18)
        return schools
    }

    private fun checkAndRequestPermission(): Boolean {
        val readPermission = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val writePermission = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val neededPermission: MutableList<String> = ArrayList()
        if (readPermission != PackageManager.PERMISSION_GRANTED) {
            neededPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            neededPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!neededPermission.isEmpty()) {
            ActivityCompat.requestPermissions(
                parent,
                neededPermission.toTypedArray(),
                Documents.REQUEST_MULTI_PERMISSION_ID
            )
            return false
        }
        return true
    }

    companion object {
        lateinit var mpesaListener: MpesaListener
        var touchIV: TouchImageView? = null
        var close: TextView? = null
        private const val READ_PERMISSION = 99
        private const val ERROR_DIALOG_REQUEST_CODE = 1
    }

    override fun sendingSuccessful(
        transactionAmount: String,
        phoneNumber: String,
        transactionDate: String,
        MPesaReceiptNo: String
    ) {
        TODO("Not yet implemented")
    }

    override fun sendingFailed(cause: String) {
        TODO("Not yet implemented")
    }
}