package com.midland.ynote.Fragments

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.bumptech.glide.Glide
import com.github.barteksc.pdfviewer.PDFView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.midland.ynote.Activities.*
import com.midland.ynote.Adapters.HomeSliderAdt
import com.midland.ynote.Adapters.PinnedDocsGridAdapter
import com.midland.ynote.Dialogs.BiggerPicture
import com.midland.ynote.Objects.HomeSliderObj
import com.midland.ynote.R
import com.midland.ynote.Utilities.AdMob.Companion.checkConnection
import com.ortiz.touchview.TouchImageView

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    //    SearchView searchLocalFiles;
    var navRight: ImageView? = null
    var navLeft: ImageView? = null
    var navRight1: ImageView? = null
    var navLeft1: ImageView? = null
    var searchDocs: SearchView? = null
    var searchRecentDocs: SearchView? = null
    private var homeSliderObjs: ArrayList<HomeSliderObj>? = null
    var netDetails: BooleanArray? = null
    var animatorSet: AnimatorSet? = null
    var pageCounter: PDFView? = null
    var savedDocsRV: RecyclerView? = null
    var homeSliderRV: RecyclerView? = null
    var recentDocsRV: RecyclerView? = null
    var homeVP: ViewPager2? = null
    var homeCoord: CoordinatorLayout? = null
    var slideHandler: Handler? = null
    var localContentRel: RelativeLayout? = null
    var recentDocsRel: RelativeLayout? = null
    var pinned: PinnedDocsGridAdapter? = null
    private var flag: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            // TODO: Rename and change types of parameters
            val mParam1 = arguments!!.getString(ARG_PARAM1)
            val mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        homeSliderObjs = ArrayList()
        localContentRel = root.findViewById(R.id.localContentRel)
        recentDocsRel = root.findViewById(R.id.recentDocsRel)
        touchIV = root.findViewById(R.id.touchIV)
        val libraryButton = root.findViewById<Button>(R.id.libraryButton)
        val schoolsButton = root.findViewById<Button>(R.id.schoolsButton)
        val studioButton = root.findViewById<Button>(R.id.studioButton)
        val lecturesButton = root.findViewById<Button>(R.id.lecturesButton)
        val profileButton = root.findViewById<Button>(R.id.profileButton)
        val userDisplayImage = root.findViewById<ImageView>(R.id.userDisplayImage)
        val schoolsOptions = root.findViewById<Button>(R.id.schools_options)
        val lectureOptions = root.findViewById<Button>(R.id.lectures_options)
        val homeSliderOptions = root.findViewById<Button>(R.id.home_slider_options)
        val displayName = root.findViewById<TextView>(R.id.displayName)
        val userEmail = root.findViewById<TextView>(R.id.userEmail)
        schoolsOptions.bringToFront()
        lectureOptions.bringToFront()
        profileButton.bringToFront()
        val preferences = context!!.getSharedPreferences("User", Context.MODE_PRIVATE)
        val json = preferences.getString("User", "")
        val gson = Gson()
        val type = object : TypeToken<ArrayList<String?>?>() {}.type
        val userDetail = gson.fromJson<ArrayList<String>>(json, type)

//        schoolsOptions.setOnClickListener(v -> {
//            UserProducts shortCut = new UserProducts(getContext(), "shortCut", null, null);
//            shortCut.show();
//        });
//        lectureOptions.setOnClickListener(v -> {
//            UserProducts shortCut = new UserProducts(getContext(), "shortCut1", null, null);
//            shortCut.show();
//        });
        homeSliderOptions.setOnClickListener { v: View? -> flag = "lectureSearch" }
        savedDocsRV = root.findViewById(R.id.savedDocsRV)
        recentDocsRV = root.findViewById(R.id.recentDocsRV)
        homeSliderRV = root.findViewById(R.id.homeSliderRV)
        homeVP = root.findViewById(R.id.homeViewPager1)
        homeCoord = root.findViewById(R.id.homeCoord)
        navLeft = root.findViewById(R.id.navLeft)
        navRight = root.findViewById(R.id.navRight)
        navLeft1 = root.findViewById(R.id.navLeft1)
        navRight1 = root.findViewById(R.id.navRight1)
        searchDocs = root.findViewById(R.id.searchDocs)
        searchRecentDocs = root.findViewById(R.id.searchRecentDocs)
        homeVP!!.bringToFront()
        val glowLay = root.findViewById<LinearLayout>(R.id.linLayGlow)
        animatorSet = AnimatorSet()
        slideHandler = Handler()
        homeVP!!.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                slideHandler!!.removeCallbacks(slideRunnable)
                if (position % 4 == 0 && position != 0) {
                    slideHandler!!.postDelayed(slideRunnable, 3000)
                } else {
                    slideHandler!!.postDelayed(slideRunnable, (3500 * 2).toLong())
                }
            }
        })
        val fadeOut = ObjectAnimator.ofFloat(glowLay, "alpha", 0.5f, 0.1f)
        fadeOut.duration = 500
        val fadeIn = ObjectAnimator.ofFloat(glowLay, "alpha", 0.1f, 0.5f)
        fadeIn.duration = 500
        animatorSet!!.play(fadeIn).after(fadeOut)
        animatorSet!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                animatorSet!!.start()
                super.onAnimationEnd(animation)
            }
        })
        animatorSet!!.start()
        homeSliderRV!!.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        savedDocsRV!!.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        if (getPins("docPins") == null) {
            localContentRel!!.visibility = View.GONE
        } else {
            localContentRel!!.visibility = View.VISIBLE
            pinned = PinnedDocsGridAdapter(
                context,
                getPins("docPins"),
                "docPins"
            )
            pinned!!.notifyDataSetChanged()
            savedDocsRV!!.adapter = pinned
        }
        if (getPins("recentDocs") == null) {
            recentDocsRel!!.visibility = View.GONE
        } else {
            recentDocsRV!!.visibility = View.VISIBLE
            pinned = PinnedDocsGridAdapter(
                context,
                getPins("recentDocs"),
                "recentDocs"
            )
            pinned!!.notifyDataSetChanged()
            recentDocsRV!!.adapter = pinned
        }
        searchDocs!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (pinned != null) {
                    pinned!!.getFilter().filter(newText)
                }
                return false
            }
        })
        navRight!!.setOnClickListener(View.OnClickListener { v: View? ->
            try {
                savedDocsRV!!.smoothScrollToPosition(pinned!!.docPosition++)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
        navLeft!!.setOnClickListener(View.OnClickListener { v: View? ->
            try {
                savedDocsRV!!.smoothScrollToPosition(pinned!!.docPosition--)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })

//        searchToggle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (searchLocalFiles.getVisibility() == View.VISIBLE){
//                    searchLocalFiles.setVisibility(View.GONE);
//                }else {
//                    searchLocalFiles.setVisibility(View.VISIBLE);
//                }
//            }
//        });
        val user = FirebaseAuth.getInstance().currentUser
        //        userDisplayImage.bringToFront();
        userDisplayImage.setOnClickListener { v: View? ->
            val userImageUrl = "" //GET IT FROM SHARED PREF
            val biggerPicture = BiggerPicture(
                context!!, activity,
                null, user, Uri.parse(userImageUrl), false
            )
            biggerPicture.show()
        }
        if (user != null) {
            displayName.text = user.displayName
            userEmail.text = user.email

        }
        val users = FirebaseFirestore.getInstance().collection("Users")
        //Get random people
        users.limit(20).get().addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
            for (qds in queryDocumentSnapshots) {
                val alias = qds.getString("alias")
                val institution = qds.getString("institution")
                val fullName = qds.getString("fullName")
                val c = qds["coaches"].toString()
                val s = qds["students"].toString()
                val userImage = qds.getString("profilePicture")
                val backgroundImage = qds.getString("backgroundImage")
                val philosophy = qds.getString("about")
                val userId = qds.getString("userID")
                val school = qds.getString("school")
                val homeSliderObj = HomeSliderObj(
                    fullName, alias,
                    philosophy, userImage, backgroundImage, userId,
                    institution, s, c, school, "notNull"
                )
                homeSliderObjs!!.add(homeSliderObj)
            }
            homeSliderObjs?.shuffle()
            val homeSliderAdt = HomeSliderAdt(context!!, activity, homeSliderObjs, null)
            homeSliderAdt.notifyDataSetChanged()
            homeSliderRV!!.adapter = homeSliderAdt
            homeVP!!.adapter = homeSliderAdt
        }.addOnFailureListener { e: Exception? -> }
        profileButton.setOnClickListener { v: View? ->
//            Snackbar.make(v.findViewById(R.id.homeRel), "You can't flex like this..", BaseTransientBottomBar.LENGTH_LONG).show();
            val intent = Intent(context, UserProfile2::class.java)
            context!!.startActivity(intent)
        }
        libraryButton.setOnClickListener { v: View? ->
//            Snackbar.make(v.findViewById(R.id.homeRel), "The power resides within..", BaseTransientBottomBar.LENGTH_LONG).show();
            if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE)
                    && ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ) {
                    Snackbar.make(
                        homeCoord!!,
                        "Permission",
                        Snackbar.LENGTH_INDEFINITE
                    )
                        .setAction("ENABLE") { v12: View? ->
                            ActivityCompat.requestPermissions(
                                activity!!, arrayOf(
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                                ), 8
                            )
                        }.show()
                } else {
                    ActivityCompat.requestPermissions(
                        activity!!, arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ), 8
                    )
                }
            }
            else {
                val intent = Intent(context, Library::class.java)
                startActivityForResult(intent, 33)
            }
        }
        schoolsButton.setOnClickListener { v: View? ->
            netDetails = checkConnection(context!!)
            if (netDetails != null) {
                if (netDetails!![0] == java.lang.Boolean.TRUE) {
                    val intent = Intent(context, Schools::class.java)
                    if (userDetail != null) {
                        intent.putExtra("userSch", userDetail[6])
                    }
                    startActivity(intent)
                } else {
                    Snackbar.make(
                        activity!!.findViewById(android.R.id.content),
                        "Check your network connection..",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
        Glide.with(context!!).load(R.drawable.lib1).thumbnail(0.9.toFloat())
            .into((root.findViewById<View>(R.id.libraryImage) as ImageView))
        Glide.with(context!!).load(R.drawable.stud_background22).thumbnail(0.9.toFloat())
            .into((root.findViewById<View>(R.id.studioImage) as ImageView))
        Glide.with(context!!).load(R.drawable.lecturehall).thumbnail(0.9.toFloat())
            .into((root.findViewById<View>(R.id.lecturesImage) as ImageView))
        Glide.with(context!!).load(R.drawable.schools_acad_disciplines).thumbnail(0.9.toFloat())
            .into((root.findViewById<View>(R.id.academicDis) as ImageView))
        studioButton.setOnClickListener { v: View? ->
            if (ContextCompat.checkSelfPermission(
                    context!!, Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    context!!, Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    context!!, Manifest.permission.WRITE_SETTINGS
                ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    context!!, Manifest.permission.FOREGROUND_SERVICE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        activity!!,
                        Manifest.permission.CAMERA
                    )
                    && ActivityCompat.shouldShowRequestPermissionRationale(
                        activity!!,
                        Manifest.permission.RECORD_AUDIO
                    )
                    && ActivityCompat.shouldShowRequestPermissionRationale(
                        activity!!,
                        Manifest.permission.WRITE_SETTINGS
                    )
                ) {
                    Snackbar.make(
                        root.findViewById(R.id.homeRel),
                        "Permission",
                        Snackbar.LENGTH_INDEFINITE
                    )
                        .setAction("ENABLE") { v1: View? ->
                            ActivityCompat.requestPermissions(
                                activity!!, arrayOf(
                                    Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO
                                ), 7
                            )
                        }.show()
                } else {
                    ActivityCompat.requestPermissions(
                        activity!!, arrayOf(
                            Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO
                        ), 7
                    )
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Settings.System.canWrite(context)) {
                        if ((ContextCompat.checkSelfPermission(
                                context!!,
                                Manifest.permission.WRITE_SETTINGS
                            )
                                    != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(
                                context!!, Manifest.permission.CAMERA
                            )
                                    != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(
                                context!!, Manifest.permission.RECORD_AUDIO
                            )
                                    != PackageManager.PERMISSION_GRANTED)
                        ) {
                            Toast.makeText(
                                context,
                                Build.VERSION.SDK_INT.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            requestPermissions(
                                arrayOf(
                                    Manifest.permission.WRITE_SETTINGS,
                                    Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO
                                ), READ_PERMISSION
                            )
                        } else {
                            startActivity(Intent(context, Production::class.java))
                        }
                    } else {
                        val intent = Intent(context, Production::class.java)
                        intent.putExtra("stylus", "False")
                        startActivity(intent)
                    }
                } else {
                    val intent = Intent(context, Production::class.java)
                    startActivity(intent)
                }
            }
        }
        lecturesButton.setOnClickListener { v: View? ->
            netDetails = checkConnection(context!!)
            if (netDetails != null) {
                if (netDetails!![0] == java.lang.Boolean.TRUE) {
                    val intent = Intent(context, LectureFilter::class.java)
                    if (userDetail != null) {
                        intent.putExtra("userSch", userDetail[6])
                    }
                    startActivity(intent)
                } else {
                    Snackbar.make(
                        activity!!.findViewById(android.R.id.content),
                        "Check your network connection..",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 33){
            Snackbar.make(
                homeCoord!!,
                "Nothing was selected.",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 7) {
            startActivity(Intent(context, SourceDocList::class.java))
        }
        if (requestCode == READ_PERMISSION) {
            startActivity(Intent(context, Production::class.java))
        }
        if (requestCode == 8) {
            startActivity(Intent(context, SourceDocList::class.java))
        }
    }

    private val slideRunnable = Runnable { homeVP!!.setCurrentItem(homeVP!!.currentItem + 1, true) }
    private fun getPins(flag: String): ArrayList<String>? {
        val preferences = context!!.getSharedPreferences(flag, Context.MODE_PRIVATE)
        val json = preferences.getString(flag, "")
        val gson = Gson()
        val type = object : TypeToken<ArrayList<String?>?>() {}.type
        return gson.fromJson(json, type)
    }

    companion object {
        var touchIV: TouchImageView? = null
        private const val READ_PERMISSION = 99

        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): MainFragment {
            val fragment = MainFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}