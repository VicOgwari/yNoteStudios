package com.midland.ynote.Activities

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.SimpleOnPageChangeListener
import com.midland.ynote.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.firebase.firestore.FirebaseFirestore
import com.midland.ynote.Adapters.CloudVideosAdapter
import com.midland.ynote.Adapters.DocumentAdapter
import com.midland.ynote.Adapters.FragmentsSwitchAdapter
import com.midland.ynote.Adapters.HomeSliderAdt
import com.midland.ynote.Objects.HomeSliderObj
import com.midland.ynote.Objects.SearchModel
import com.midland.ynote.Objects.SelectedDoc
import com.ortiz.touchview.TouchImageView

class GeneralSearch : AppCompatActivity() {

    private var adapter: FragmentsSwitchAdapter? = null
    private var tabs: TabLayout? = null
    companion object{
        var searchList: ArrayList<SearchModel> = ArrayList()
        var profilePics = ArrayList<String>()
        var touchIV: TouchImageView? = null

        private var publishedDocs = FirebaseFirestore.getInstance().collection("Content")
            .document("Documents")

        private var uploadedLectures = FirebaseFirestore.getInstance().collection("Content")
            .document("Lectures")

        private var registeredUsers = FirebaseFirestore.getInstance().collection("Users")


        fun performSearch(flag: String,
                          institution: String,
                          mainField: String,
                          subField: String,
                          year: String,
                          searchInput: String,
                          con: Context,
                          readBuffer: RelativeLayout,
                          resultsRV: RecyclerView) {

            readBuffer.visibility = View.VISIBLE
            when(flag){
                "User search" -> {
                    if (institution == "All"){
                        registeredUsers
                            .whereEqualTo("school", mainField)
                            .get()
                            .addOnSuccessListener {
                                if (!it.isEmpty){
                                    val homeSliderObjs = ArrayList<HomeSliderObj>()
                                    for (qds in it) {
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
                                        val homeSliderObj =
                                            HomeSliderObj(
                                                fullName,
                                                alias,
                                                philosophy,
                                                userImage,
                                                backgroundImage,
                                                userId,
                                                institution,
                                                s,
                                                c,
                                                school
                                            )
                                        homeSliderObjs.add(homeSliderObj)
                                    }
                                    val homeSliderAdt = HomeSliderAdt(
                                        con,
                                        null,
                                        homeSliderObjs,
                                        null
                                    )
                                    homeSliderAdt.notifyDataSetChanged()
                                    resultsRV.adapter = homeSliderAdt
                                    readBuffer.visibility = View.GONE
                                }
                            }
//                            .addOnCompleteListener{
//                                readBuffer.visibility = View.GONE
//                                if (it.isSuccessful){
//
//                                    val users: ArrayList<User> = it.result!!.toObjects(User::class.java) as ArrayList<User>
//                                    val usersAdapter = UsersAdapter(con, users, "search")
//                                    usersAdapter.notifyDataSetChanged()
//                                    resultsRV.adapter = usersAdapter
//                                }
//                            }
                    }else{
                        registeredUsers
                            .whereEqualTo("institution", institution)
                            .whereEqualTo("school", mainField)
                            .get()
                            .addOnSuccessListener {
                                if (!it.isEmpty){
                                    //Save the school object
                                    val homeSliderObjs = ArrayList<HomeSliderObj>()
                                    for (qds in it) {
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
                                        val homeSliderObj =
                                            HomeSliderObj(
                                                fullName,
                                                alias,
                                                philosophy,
                                                userImage,
                                                backgroundImage,
                                                userId,
                                                institution,
                                                s,
                                                c,
                                                school
                                            )
                                        homeSliderObjs.add(homeSliderObj)
                                    }
                                    val homeSliderAdt = HomeSliderAdt(
                                        con,
                                        null,
                                        homeSliderObjs,
                                        null
                                    )
                                    homeSliderAdt.notifyDataSetChanged()
                                    resultsRV.adapter = homeSliderAdt
                                    readBuffer.visibility = View.GONE

                                }
                            }
//                            .addOnCompleteListener{
//                                readBuffer.visibility = View.GONE
//                                if (it.isSuccessful){
//                                    searchList = it.result!!.toObjects(SearchModel::class.java) as ArrayList<SearchModel>
//                                    val results = SearchResultsAdapter(con, searchList, profilePics, "User search")
//                                    results.notifyDataSetChanged()
//                                    resultsRV.adapter = results
//                                    readBuffer.visibility = View.GONE
//                                }
//                            }
                    }

                }
                "User search1" -> {
                    if (institution == "All"){
                        registeredUsers
                            .whereEqualTo("school", mainField)
                            .whereArrayContains("registeredAs", "Student")
                            .get()
                            .addOnSuccessListener {
                                if (!it.isEmpty){
                                    //Save the school object
                                    val homeSliderObjs = ArrayList<HomeSliderObj>()
                                    for (qds in it) {
                                        val alias = qds.getString("alias")
                                        val institution = qds.getString("institution")
                                        val fullName = qds.getString("fullName")
                                        val c = qds["coaches"].toString()
                                        val s = qds["students"].toString()
                                        val publishedSch = qds.get("schoolsPublished") as List<String>?
                                        val uploadedSch = qds.get("schoolsUploaded") as List<String>?
                                        val userImage = qds.getString("profilePicture")
                                        val backgroundImage = qds.getString("backgroundImage")
                                        val philosophy = qds.getString("about")
                                        val userId = qds.getString("userID")
                                        val school = qds.getString("school")
                                        val homeSliderObj =
                                            HomeSliderObj(
                                                fullName,
                                                alias,
                                                philosophy,
                                                userImage,
                                                backgroundImage,
                                                userId,
                                                institution,
                                                s,
                                                c,
                                                school,
                                                publishedSch,
                                                uploadedSch
                                            )
                                        homeSliderObjs.add(homeSliderObj)
                                    }
                                    val homeSliderAdt = HomeSliderAdt(
                                        con,
                                        null,
                                        homeSliderObjs,
                                        null
                                    )
                                    homeSliderAdt.notifyDataSetChanged()
                                    resultsRV.adapter = homeSliderAdt
                                    readBuffer.visibility = View.GONE
                                }
                            }
//                            .addOnCompleteListener{
//                                readBuffer.visibility = View.GONE
//                                if (it.isSuccessful){
//
//                                    val users: ArrayList<User> = it.result!!.toObjects(User::class.java) as ArrayList<User>
//                                    val usersAdapter = UsersAdapter(con, users, "search")
//                                    usersAdapter.notifyDataSetChanged()
//                                    resultsRV.adapter = usersAdapter
//                                }
//                            }
                    }else{
                        registeredUsers
                            .whereEqualTo("institution", institution)
                            .whereEqualTo("school", mainField)
                            .whereArrayContains("registeredAs", "Student")
                            .get()
                            .addOnSuccessListener {
                                if (!it.isEmpty){
                                    val homeSliderObjs = ArrayList<HomeSliderObj>()
                                    for (qds in it) {
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
                                        val homeSliderObj =
                                            HomeSliderObj(
                                                fullName,
                                                alias,
                                                philosophy,
                                                userImage,
                                                backgroundImage,
                                                userId,
                                                institution,
                                                s,
                                                c,
                                                school
                                            )
                                        homeSliderObjs.add(homeSliderObj)
                                    }
                                    val homeSliderAdt = HomeSliderAdt(
                                        con,
                                        null,
                                        homeSliderObjs,
                                        null
                                    )
                                    homeSliderAdt.notifyDataSetChanged()
                                    resultsRV.adapter = homeSliderAdt
                                    readBuffer.visibility = View.GONE

                                }
                            }
//                            .addOnCompleteListener{
//                                readBuffer.visibility = View.GONE
//                                if (it.isSuccessful){
//                                    searchList = it.result!!.toObjects(SearchModel::class.java) as ArrayList<SearchModel>
//                                    val results = SearchResultsAdapter(con, searchList, profilePics, "User search")
//                                    results.notifyDataSetChanged()
//                                    resultsRV.adapter = results
//                                    readBuffer.visibility = View.GONE
//                                }
//                            }
                    }

                }
                "User search2" -> {
                    if (institution == "All"){
                        registeredUsers
                            .whereEqualTo("school", mainField)
                            .whereArrayContains("registeredAs", "Lecturer")
                            .get()
                            .addOnSuccessListener {
                                if (!it.isEmpty){
                                    val homeSliderObjs = ArrayList<HomeSliderObj>()
                                    for (qds in it) {
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
                                        val homeSliderObj =
                                            HomeSliderObj(
                                                fullName,
                                                alias,
                                                philosophy,
                                                userImage,
                                                backgroundImage,
                                                userId,
                                                institution,
                                                s,
                                                c,
                                                school,
                                                null
                                            )
                                        homeSliderObjs.add(homeSliderObj)
                                    }
                                    val homeSliderAdt = HomeSliderAdt(
                                        con,
                                        null,
                                        homeSliderObjs,
                                        null
                                    )
                                    homeSliderAdt.notifyDataSetChanged()
                                    resultsRV.adapter = homeSliderAdt
                                    readBuffer.visibility = View.GONE

                                }
                            }
//                            .addOnCompleteListener{
//                                readBuffer.visibility = View.GONE
//                                if (it.isSuccessful){
//
//                                    val users: ArrayList<User> = it.result!!.toObjects(User::class.java) as ArrayList<User>
//                                    val usersAdapter = UsersAdapter(con, users, "search")
//                                    usersAdapter.notifyDataSetChanged()
//                                    resultsRV.adapter = usersAdapter
//                                }
//                            }
                    }else{
                        registeredUsers
                            .whereEqualTo("institution", institution)
                            .whereEqualTo("school", mainField)
                            .whereArrayContains("registeredAs", "Lecturer")
                            .get()
                            .addOnSuccessListener {
                                if (!it.isEmpty){
                                    val homeSliderObjs = ArrayList<HomeSliderObj>()
                                    for (qds in it) {
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
                                        val homeSliderObj =
                                            HomeSliderObj(
                                                fullName,
                                                alias,
                                                philosophy,
                                                userImage,
                                                backgroundImage,
                                                userId,
                                                institution,
                                                s,
                                                c,
                                                school
                                            )
                                        homeSliderObjs.add(homeSliderObj)
                                    }
                                    val homeSliderAdt = HomeSliderAdt(
                                        con,
                                        null,
                                        homeSliderObjs,
                                        null
                                    )
                                    homeSliderAdt.notifyDataSetChanged()
                                    resultsRV.adapter = homeSliderAdt
                                    readBuffer.visibility = View.GONE

                                }
                            }
//                            .addOnCompleteListener{
//                                readBuffer.visibility = View.GONE
//                                if (it.isSuccessful){
//                                    searchList = it.result!!.toObjects(SearchModel::class.java) as ArrayList<SearchModel>
//                                    val results = SearchResultsAdapter(con, searchList, profilePics, "User search")
//                                    results.notifyDataSetChanged()
//                                    resultsRV.adapter = results
//                                    readBuffer.visibility = View.GONE
//                                }
//                            }
                    }

                }
                "Doc search" -> {
                    if (institution == "All" && year == "All" && searchInput == ""){
                        publishedDocs.collection(mainField)
                            .whereEqualTo("subField", subField)
                            .get()
                            .addOnSuccessListener {
                                if (!it.isEmpty){
                                    //Save the school object
                                    val documents = ArrayList<SelectedDoc>()
                                    for (qds in it) {
                                        val document = qds.toObject(SelectedDoc::class.java)
                                        documents.add(document)
                                    }
                                    val docAdt =
                                        DocumentAdapter(
                                            null,
                                            con,
                                            con,
                                            documents
                                        )
                                    docAdt.notifyDataSetChanged()
                                    resultsRV.adapter = docAdt
                                    readBuffer.visibility = View.GONE
                                }
                            }
//                            .addOnCompleteListener{
//                                if (it.isSuccessful){
//                                    readBuffer.visibility = View.GONE
//                                    val document: ArrayList<SelectedDoc> = it.result!!.toObjects(
//                                        SelectedDoc::class.java) as ArrayList<SelectedDoc>
//                                    val docAdt = DocumentAdapter(
//                                        null,
//                                        con,
//                                        con,
//                                        document
//                                    )
//                                    docAdt.notifyDataSetChanged()
//                                    resultsRV.adapter = docAdt
//                                }
//
//                            }
                    }else
                        if (institution != "All" && year == "All" && searchInput == ""){
                            publishedDocs.collection(mainField).whereEqualTo("institution", institution)
                                .whereEqualTo("subField", subField)
                                .get()
                                .addOnSuccessListener {
                                    if (!it.isEmpty){
                                        //Save the school object
                                        val documents = ArrayList<SelectedDoc>()
                                        for (qds in it) {
                                            val document = qds.toObject(SelectedDoc::class.java)
                                            documents.add(document)
                                        }
                                        val docAdt =
                                            DocumentAdapter(
                                                null,
                                                con,
                                                con,
                                                documents
                                            )
                                        docAdt.notifyDataSetChanged()
                                        resultsRV.adapter = docAdt
                                        readBuffer.visibility = View.GONE
                                    }
                                }
//                                .addOnCompleteListener{
//                                    if (it.isSuccessful){
//                                        readBuffer.visibility = View.GONE
//                                        val document: ArrayList<SelectedDoc> = it.result!!.toObjects(
//                                            SelectedDoc::class.java) as ArrayList<SelectedDoc>
//                                        val docAdt = DocumentAdapter(
//                                            null,
//                                            con, con,
//                                            document
//                                        )
//                                        docAdt.notifyDataSetChanged()
//                                        resultsRV.adapter = docAdt
//
//
//                                    }
//
//                                }
                        }else
                            if (institution == "All" && year != "All" && searchInput == ""){
                                publishedDocs.collection(mainField).whereEqualTo("knowledgeBase", year)
                                    .whereEqualTo("subField", subField)
                                    .get()
                                    .addOnSuccessListener {
                                        if (!it.isEmpty){
                                            //Save the school object
                                            val documents = ArrayList<SelectedDoc>()
                                            for (qds in it) {
                                                val document = qds.toObject(SelectedDoc::class.java)
                                                documents.add(document)
                                            }
                                            val docAdt =
                                                DocumentAdapter(
                                                    null,
                                                    con,
                                                    con,
                                                    documents
                                                )
                                            docAdt.notifyDataSetChanged()
                                            resultsRV.adapter = docAdt
                                            readBuffer.visibility = View.GONE
                                        }
                                    }
//                                    .addOnCompleteListener{
//                                        if (it.isSuccessful){
//                                            readBuffer.visibility = View.GONE
//                                            val document: ArrayList<SelectedDoc> = it.result!!.toObjects(
//                                                SelectedDoc::class.java) as ArrayList<SelectedDoc>
//                                            val docAdt = DocumentAdapter(
//                                                null,
//                                                con,
//                                                con,
//                                                document
//                                            )
//                                            docAdt.notifyDataSetChanged()
//                                            resultsRV.adapter = docAdt
//
//
//                                        }
//
//                                    }
                            }else
                                if (institution != "All" && year != "All" && searchInput == ""){
                                    publishedDocs.collection(mainField).whereEqualTo("institution", institution).whereEqualTo("knowledgeBase", year)
                                        .whereEqualTo("subField", subField)
                                        .get()
                                        .addOnSuccessListener {
                                            if (!it.isEmpty){
                                                //Save the school object
                                                val documents = ArrayList<SelectedDoc>()
                                                for (qds in it) {
                                                    val document = qds.toObject(SelectedDoc::class.java)
                                                    documents.add(document)
                                                }
                                                val docAdt =
                                                    DocumentAdapter(
                                                        null,
                                                        con,
                                                        con,
                                                        documents
                                                    )
                                                docAdt.notifyDataSetChanged()
                                                resultsRV.adapter = docAdt
                                                readBuffer.visibility = View.GONE
                                            }
                                        }
//                                        .addOnCompleteListener{
//                                            if (it.isSuccessful){
//                                                readBuffer.visibility = View.GONE
//                                                val document: ArrayList<SelectedDoc> = it.result!!.toObjects(
//                                                    SelectedDoc::class.java) as ArrayList<SelectedDoc>
//                                                val docAdt = DocumentAdapter(
//                                                    null,
//                                                    con,
//                                                    con,
//                                                    document
//                                                )
//                                                docAdt.notifyDataSetChanged()
//                                                resultsRV.adapter = docAdt
//
//
//                                            }
//
//                                        }
                                }
                }
                "Lecture search" -> {
                    if (institution == "All" && year == "All" && searchInput == ""){
                        uploadedLectures.collection(mainField)
                            .whereEqualTo("subField", subField)
                            .get()
                            .addOnCompleteListener{
                                if (it.isSuccessful){
                                    readBuffer.visibility = View.GONE
                                    val document: ArrayList<SelectedDoc> = it.result!!.toObjects(
                                        SelectedDoc::class.java) as ArrayList<SelectedDoc>
                                    val results =
                                        CloudVideosAdapter(
                                            null,
                                            null,
                                            null,
                                            con,
                                            con,
                                            document,
                                            null,
                                            null
                                        )
                                    results.notifyDataSetChanged()
                                    resultsRV.adapter = results
                                }
                            }
                    }else
                        if (institution != "All" && year == "All" && searchInput == ""){
                            uploadedLectures.collection(mainField)
                                .whereEqualTo("institution", institution)
                                .whereEqualTo("subField", subField)
                                .get()
                                .addOnCompleteListener{
                                    if (it.isSuccessful){
                                        readBuffer.visibility = View.GONE
                                        val document: ArrayList<SelectedDoc> = it.result!!.toObjects(
                                            SelectedDoc::class.java) as ArrayList<SelectedDoc>
                                        val results =
                                            CloudVideosAdapter(
                                                null,
                                                null,
                                                null,
                                                con,
                                                con,
                                                document,
                                                null,
                                                null
                                            )
                                        results.notifyDataSetChanged()
                                        resultsRV.adapter = results
                                    }
                                }
                        }else
                            if (institution == "All" && year != "All" && searchInput == ""){
                                uploadedLectures.collection(mainField)
                                    .whereEqualTo("subField", subField)
                                    .whereEqualTo("knowledgeBase", year)
                                    .get()
                                    .addOnCompleteListener{
                                        if (it.isSuccessful){
                                            readBuffer.visibility = View.GONE
                                            val document: ArrayList<SelectedDoc> = it.result!!.toObjects(
                                                SelectedDoc::class.java) as ArrayList<SelectedDoc>
                                            val results =
                                                CloudVideosAdapter(
                                                    null,
                                                    null,
                                                    null,
                                                    con,
                                                    con,
                                                    document,
                                                    null,
                                                    null
                                                )
                                            results.notifyDataSetChanged()
                                            resultsRV.adapter = results
                                        }
                                    }
                            }else
                                if (institution != "All" && year != "All" && searchInput == ""){
                                    uploadedLectures.collection(mainField)
                                        .whereEqualTo("institution", institution)
                                        .whereEqualTo("subField", subField)
                                        .whereEqualTo("knowledgeBase", year)
                                        .get()
                                        .addOnCompleteListener{
                                            if (it.isSuccessful){
                                                readBuffer.visibility = View.GONE
                                                val document: ArrayList<SelectedDoc> = it.result!!.toObjects(
                                                    SelectedDoc::class.java) as ArrayList<SelectedDoc>
                                                val results =
                                                    CloudVideosAdapter(
                                                        null,
                                                        null,
                                                        null,
                                                        con,
                                                        con,
                                                        document,
                                                        null,
                                                        null
                                                    )
                                                results.notifyDataSetChanged()
                                                resultsRV.adapter = results
                                            }
                                        }
                                }

                    readBuffer.visibility = View.GONE
                }
            }
        }

    }

    private fun initTabsNStuff() {
        tabs = findViewById(R.id.tabs)
        touchIV = findViewById(R.id.touchIV)
        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        viewPager.setOnPageChangeListener(object : SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                tabs!!.getTabAt(position)!!.select()
                super.onPageSelected(position)
            }
        })




        adapter = FragmentsSwitchAdapter(
            supportFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
            tabs!!.tabCount,
            "Search"
        )
        viewPager.adapter = adapter
        if (intent.getStringExtra("bitmaps") != null) {
            viewPager.currentItem = 1
        }
        tabs!!.setOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.setCurrentItem(tab.position, true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general_search)
        val toolbar = findViewById<Toolbar>(R.id.libToolbar)
        toolbar.title = "yNote"
        setSupportActionBar(toolbar)
        initTabsNStuff()
    }

    override fun onBackPressed() {
        if (touchIV!!.visibility == View.VISIBLE){
            touchIV!!.visibility = View.GONE
        }else {
            super.onBackPressed()
        }
    }
}