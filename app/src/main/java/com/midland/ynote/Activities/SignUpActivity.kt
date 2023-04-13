package com.midland.ynote.Activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.preference.PreferenceManager
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.*
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.messaging.FirebaseMessaging
import com.midland.ynote.MainActivity
import com.midland.ynote.R
import com.midland.ynote.stkPush.Utils
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterLoginButton
import java.util.*

class SignUpActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private val mClient: GoogleSignInClient? = null
    private var auth: FirebaseAuth? = null
    private var authStateListener: AuthStateListener? = null
    private var register: Button? = null
    private var facebookLogin: LoginButton? = null
    private var titleSpinner: Spinner? = null
    private var mCallbackManager: CallbackManager? = null
    private var accessTokenTracker: AccessTokenTracker? = null
    private var userEmail: String? = null
    private var userPic: String? = null
    var fullName: EditText? = null
    var alias: EditText? = null
    var institutionET: EditText? = null
    var password: TextInputEditText? = null
    var courseET: TextInputEditText? = null
    var phoneNumber: TextInputEditText? = null
    var eMail: TextInputEditText? = null
    var passwordReDo: TextInputEditText? = null
    var logIn: TextView? = null
    var userTitle: String? = null
    var institution: String? = null
    var school: String? = null
    var gender: String? = null
    var maleCheck: CheckBox? = null
    var femaleCheck: CheckBox? = null
    var auth1: FirebaseAuth? = null
    var users: CollectionReference? = null
    var prg: ProgressDialog? = null
    var signUpScrollView: ScrollView? = null
    var institutionSp: Spinner? = null
    var schoolSp: Spinner? = null
    var fcmToken: String? = null
        get() {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task: Task<String?> ->
                if (!task.isSuccessful) {
                    Toast.makeText(this, "FCM's missing...", Toast.LENGTH_SHORT).show()
                } else {
                    field = task.result
                }
            }.addOnFailureListener { e: Exception ->
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                Toast.makeText(this, "FCM2's missing...", Toast.LENGTH_SHORT).show()
            }
            return field
        }
        private set
    private var mAuth: FirebaseAuth? = null
    private var mAuthListener: AuthStateListener? = null
    private var mTwitterBtn: TwitterLoginButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(applicationContext)
        val mTwitterAuthConfig = TwitterAuthConfig(
            getString(R.string.twitter_consumer_key),
            getString(R.string.twitter_consumer_secret)
        )
        val twitterConfig = TwitterConfig.Builder(this)
            .twitterAuthConfig(mTwitterAuthConfig)
            .build()
        Twitter.initialize(twitterConfig)
        setContentView(R.layout.activity_sign_up)
        users = FirebaseFirestore.getInstance().collection("Users")
        auth1 = FirebaseAuth.getInstance()
        signUpScrollView = findViewById(R.id.signUpRoot)
        fullName = findViewById(R.id.fullNameSignUp)
        eMail = findViewById(R.id.emailSignUp)
        passwordReDo = findViewById(R.id.passwordReDo)
        phoneNumber = findViewById(R.id.phoneNumber)
        password = findViewById(R.id.passwordSignUp)
        logIn = findViewById(R.id.login)
        register = findViewById(R.id.signUp)
        alias = findViewById(R.id.aliasNameSignUp)
        titleSpinner = findViewById(R.id.userTitleSpinner)
        institutionSp = findViewById(R.id.institutionSpinner)
        schoolSp = findViewById(R.id.schoolSpinner)
        institutionET = findViewById(R.id.institutionET)
        courseET = findViewById(R.id.courseET)
        maleCheck = findViewById(R.id.maleCheck)
        femaleCheck = findViewById(R.id.femaleCheck)
        titleSpinner!!.onItemSelectedListener = this
        institutionSp!!.onItemSelectedListener = this
        schoolSp!!.onItemSelectedListener = this
        maleCheck!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            femaleCheck!!.isChecked = false
            gender = "M"
        })
        femaleCheck!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            maleCheck!!.isChecked = false
            gender = "F"
        })
        val titleAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.user_titles, android.R.layout.simple_spinner_item
        )
        val institutionsAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.institutions, android.R.layout.simple_spinner_item
        )
        val schoolsAdapter = ArrayAdapter.createFromResource(
            applicationContext,
            R.array.sign_main_fields, android.R.layout.simple_spinner_item
        )
        titleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        institutionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        schoolsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        titleSpinner!!.adapter = titleAdapter
        institutionSp!!.adapter = institutionsAdapter
        schoolSp!!.adapter = schoolsAdapter
        titleSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                userTitle = parent.getItemAtPosition(position) as String
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        schoolSp!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                school = parent.getItemAtPosition(position) as String
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        institutionSp!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                institution = parent.getItemAtPosition(position) as String
                if (institution == "Type my institution") {
                    institutionET!!.visibility = View.VISIBLE
                } else {
                    institutionET!!.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        logIn!!.setOnClickListener { v: View? ->
            startActivity(
                Intent(
                    this@SignUpActivity,
                    LoginActivity::class.java
                )
            )
        }
        register!!.setOnClickListener { v: View? ->
            prg = ProgressDialog(this@SignUpActivity)
            prg!!.setMessage("Checking for alias duplicates...")
            val full_name = fullName!!.text.toString().trim { it <= ' ' }
            val e_mail = eMail!!.text.toString().trim { it <= ' ' }
            val course = courseET!!.text.toString().trim { it <= ' ' }
            val passWord = password!!.text.toString().trim { it <= ' ' }
            val phone_number = phoneNumber!!.text.toString().trim { it <= ' ' }
            val passWordRedo = passwordReDo!!.text.toString().trim { it <= ' ' }
            val alias_name1 = alias!!.text.toString().trim { it <= ' ' }
            val alias_name = alias_name1.replace(" ", ".").lowercase(Locale.getDefault())
            if (school == "-select a field-" || institution == "-select a field-"){
                Toast.makeText(
                    applicationContext,
                    "Please provide academic details.",
                    Toast.LENGTH_LONG
                ).show()

            }else
            if (passWord.split(" ".toRegex()).toTypedArray().size < 2) {
                Toast.makeText(
                    applicationContext,
                    "Password MUST contain a space",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (passWord != passWordRedo) {
                passwordReDo!!.error = "Password mismatch"
                Toast.makeText(applicationContext, "Password mismatch", Toast.LENGTH_SHORT).show()
            } else if (full_name.split(" ".toRegex()).toTypedArray().size < 2) {
                Toast.makeText(
                    applicationContext,
                    "At least two names required",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (gender == null) {
                Toast.makeText(applicationContext, "Please select your gender", Toast.LENGTH_SHORT)
                    .show()
            } else if (TextUtils.isEmpty(full_name) || TextUtils.isEmpty(e_mail) || TextUtils.isEmpty(
                    passWord
                )
                || TextUtils.isEmpty(alias_name) || TextUtils.isEmpty(phone_number)
            ) {
                Toast.makeText(applicationContext, "Required field(s) missing.", Toast.LENGTH_SHORT)
                    .show()
            } else if (passWord.length < 7) {
                Toast.makeText(applicationContext, "Weak password!", Toast.LENGTH_SHORT).show()
            } else {
                if (Utils.sanitizePhoneNumber(phone_number) == "") {
                    phoneNumber!!.error = "Something's missing..."
                    Toast.makeText(applicationContext, "Phone number error", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    if (institution == "Type my own" && institutionET!!.text.toString()
                            .trim { it <= ' ' } == ""
                    ) {
                        institutionET!!.error = "Missing institution."
                    } else if (institution == "Type my own" && institutionET!!.text.toString()
                            .trim { it <= ' ' } != ""
                    ) {
                        institution = institutionET!!.text.toString().trim { it <= ' ' }
                    }
                    val token = fcmToken
                    val searchableUser = (alias_name.replace("@ynStudios.com", "") + " "
                            + full_name)

                    users!!.whereEqualTo(
                        "alias",
                        alias_name.lowercase(Locale.getDefault()).replace("@ynstudios.com", "")
                    )
                        .get()
                        .addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
                            if (queryDocumentSnapshots.size() != 0) {
                                prg!!.dismiss()
                                val snackbar = Snackbar.make(
                                    signUpScrollView!!,
                                    "Alias name taken!",
                                    Snackbar.LENGTH_SHORT
                                )
                                snackbar.show()
                            } else {
                                val userDetails = ArrayList<String?>()
                                userDetails.add("$userTitle $full_name")
                                userDetails.add(alias_name)
                                userDetails.add(gender)
                                userDetails.add(e_mail)
                                userDetails.add(Utils.sanitizePhoneNumber(phone_number))
                                userDetails.add(passWord)
                                userDetails.add(institution)
                                userDetails.add(school)
                                userDetails.add(course)
                                userDetails.add(fcmToken)
                                val intent = Intent(applicationContext, Next::class.java)
                                intent.putStringArrayListExtra("userDetails", userDetails)
                                intent.putExtra("flag", "newUser")
                                startActivity(intent)
                            }
                        }

//                    registerUser(userTitle + " " + full_name,
//                            e_mail, passWord, alias_name + "@ynStudios.com",
//                            Utils.sanitizePhoneNumber(phone_number));
                }
            }
        }
        facebookLogin = findViewById(R.id.facebookLogIn)
        facebookLogin!!.setReadPermissions("email", "public_profile", "user_gender", "user_birthday")
        auth = FirebaseAuth.getInstance()

//        createRequest();
        mCallbackManager = CallbackManager.Factory.create()
         facebookLogin!!.registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d(TAG, "onSuccess$loginResult")
                handleFacebookToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d(TAG, "onCancel")
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "onError")
            }
        })
        authStateListener = AuthStateListener { firebaseAuth: FirebaseAuth? ->
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                updateUI(user)
            } else {
                updateUI(null)
            }
        }
        accessTokenTracker = object : AccessTokenTracker() {
            override fun onCurrentAccessTokenChanged(
                oldAccessToken: AccessToken,
                currentAccessToken: AccessToken
            ) {
                if (currentAccessToken == null) {
                    auth!!.signOut()
                }
            }
        }


        //Twitter
        mAuth = FirebaseAuth.getInstance()
        mTwitterBtn = findViewById(R.id.twitter_login_button)
        mAuthListener = AuthStateListener { firebaseAuth: FirebaseAuth ->
            if (firebaseAuth.currentUser != null) {
//                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                onBackPressed()
            }
        }
        UpdateTwitterButton()
        mTwitterBtn!!.callback = object : Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>) {
                Toast.makeText(
                    this@SignUpActivity,
                    "Signed in to twitter successful",
                    Toast.LENGTH_LONG
                ).show()
                signInToFirebaseWithTwitterSession(result.data)
                mTwitterBtn!!.visibility = View.VISIBLE
                //                prg.setMessage("Signing you up..");
    //                prg.show();
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
            }

            override fun failure(exception: TwitterException) {
                Toast.makeText(this@SignUpActivity, exception.message, Toast.LENGTH_LONG).show()
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                UpdateTwitterButton()
            }
        }
    }

    private fun handleFacebookToken(accessToken: AccessToken) {
        Log.d(TAG, "handleFacebookToken$accessToken")
        val credential = FacebookAuthProvider.getCredential(accessToken.token)
        auth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    val user = auth!!.currentUser
                    updateUI(user)

                    val userMap: MutableMap<String, Any?> = HashMap()
                    val userID = user!!.uid
                    userMap["userID"] = userID
                    userMap["alias"] = null
                    userMap["fullName"] = null
                    userMap["about"] = null
                    userMap["gender"] = null
                    userMap["registeredAs"] = null
                    userMap["email"] = eMail
                    userMap["phoneNumber"] = null
                    userMap["password"] = null
                    userMap["profilePicture"] = null
                    userMap["coverArt"] = null
                    userMap["institution"] = null
                    userMap["school"] = null
                    userMap["course"] = null
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

                    FirebaseFirestore.getInstance().collection("Users")
                        .document(userID).set(userMap).addOnSuccessListener { aVoid: Void? ->
                        val schools: MutableMap<String, List<String>?> = HashMap()
                        schools["schools"] = null
                        users!!.document(userID).collection("SchoolsPublished")
                            .document("Documents").set(schools)
                        users!!.document(userID).collection("SchoolsPublished")
                            .document("Lectures").set(schools)
                        if (task.isSuccessful) {
                            prg!!.dismiss()
                            Snackbar.make(signUpScrollView!!, "Welcome!", Snackbar.LENGTH_SHORT).show()
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
                    Snackbar.make(signUpScrollView!!, "Something went wrong!", Snackbar.LENGTH_SHORT).show()

                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            if (user.photoUrl != null) {
                userPic = user.photoUrl.toString()
                userPic = "$userPic?type=large"
            }
            if (user.email != null) {
                userEmail = user.email
            }
        }
    }

    //    private void createRequest() {
    //        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
    //                .requestIdToken(String.valueOf(R.string.default_web_client_id))
    //                .requestEmail()
    //                .build();
    //        mClient = GoogleSignIn.getClient(getApplicationContext(), gso);
    //
    //    }
    private fun signIn() {
        val signInIntent = mClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun updateUI() {
        Toast.makeText(this@SignUpActivity, "You're logged in", Toast.LENGTH_LONG)
        //Sending user to new screen after successful login
//        Intent mainActivity = new Intent(SignUpActivity.this, LoginActivity.class);
//        startActivity(mainActivity);
        onBackPressed()
        finish()
    }

    private fun UpdateTwitterButton() {
        if (TwitterCore.getInstance().sessionManager.activeSession == null) {
            mTwitterBtn!!.visibility = View.VISIBLE
        }
        //        else{
//            mTwitterBtn.setVisibility(View.GONE);
//        }
    }

    private fun signInToFirebaseWithTwitterSession(session: TwitterSession) {
        val credential = TwitterAuthProvider.getCredential(
            session.authToken.token,
            session.authToken.secret
        )
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) { task: Task<AuthResult?> ->
                val firebaseUser = auth1!!.currentUser
                if (firebaseUser != null) {
                    val userID = firebaseUser.uid
                    val userMap: MutableMap<String, Any?> = HashMap()
                    userMap["userID"] = userID
                    userMap["alias"] = null
                    userMap["fullName"] = null
                    userMap["about"] = null
                    userMap["gender"] = null
                    userMap["registeredAs"] = null
                    userMap["email"] = null
                    userMap["phoneNumber"] = null
                    userMap["password"] = null
                    userMap["profilePicture"] = null
                    userMap["coverArt"] = null
                    userMap["institution"] = null
                    userMap["school"] = null
                    userMap["course"] = null
                    userMap["birthday"] = null
                    userMap["coaches"] = null
                    userMap["students"] = null
                    userMap["cQ"] = null
                    userMap["fcmToken"] = null
                    userMap["sandBox"] = null
                    userMap["search_keyword"] = null
                    userMap["schoolsPublished"] = null
                    userMap["schoolsUploaded"] = null
                    users!!.document(userID).get()
                        .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                            if (documentSnapshot.exists()) {
                                startActivity(Intent(applicationContext, MainActivity::class.java))
                            } else {
                                users!!.document(userID).set(userMap)
                                    .addOnSuccessListener { aVoid: Void? ->
                                        val schools: MutableMap<String, List<String>?> = HashMap()
                                        schools["schools"] = null
                                        users!!.document(userID).collection("SchoolsPublished")
                                            .document("Documents").set(schools)
                                        users!!.document(userID).collection("SchoolsPublished")
                                            .document("Lectures").set(schools)
                                        if (task.isSuccessful) {
                                            val snackbar = Snackbar.make(
                                                signUpScrollView!!,
                                                "Welcome!",
                                                Snackbar.LENGTH_SHORT
                                            )
                                            snackbar.show()
                                            saveUid(this@SignUpActivity, userID)
                                            startActivity(
                                                Intent(
                                                    applicationContext,
                                                    MainActivity::class.java
                                                )
                                            )
                                        }
                                    }
                            }
                        }
                }
                if (!task.isSuccessful) {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Something's not right! Try again.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mCallbackManager!!.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        mTwitterBtn!!.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account: GoogleSignInAccount = task.getResult(
                    ApiException::class.java
                )!!
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth!!.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth!!.currentUser
                    onBackPressed()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Something isn't quite right.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun onStart() {
        super.onStart()
        auth!!.addAuthStateListener(authStateListener!!)
    }

    override fun onStop() {
        super.onStop()
        if (authStateListener != null) {
            auth!!.removeAuthStateListener(authStateListener!!)
        }
    }

    fun saveUid(c: Context?, s: String?): Boolean {
        val sp = PreferenceManager.getDefaultSharedPreferences(c)
        val mEdits = sp.edit()
        mEdits.putString("userID", s)
        return mEdits.commit()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}

    companion object {
        private const val RC_SIGN_IN = 123
        private const val TAG = "FacebookAuth"
    }
}