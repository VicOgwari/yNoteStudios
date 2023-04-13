 package com.midland.ynote.Activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.webkit.WebView
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.midland.ynote.Adapters.CloudVideosAdapter
import com.midland.ynote.Adapters.DocumentAdapter
import com.midland.ynote.Adapters.SearchResultsAdapter
import com.midland.ynote.Dialogs.VidType
import com.midland.ynote.Objects.SearchModel
import com.midland.ynote.Objects.SelectedDoc
import com.midland.ynote.R
import com.midland.ynote.Utilities.DocRetrieval.Companion.userDocAdt
import com.midland.ynote.Utilities.DocRetrieval.Companion.userLecAdt
import com.midland.ynote.Utilities.DocSorting
import java.util.*

 class SearchResults : AppCompatActivity(), AdapterView.OnItemSelectedListener {
     private var searchInput: String? = null
     private var searchList: ArrayList<SearchModel> = ArrayList()
    private var flag: String? = null
    private var addDocBtn: String? = null
    private var school: String? = null
    private var pos: Int? = null
    private var mainField: String? = null
    private var subField: String? = null
    private var institution: String? = null
    private var year: String? = null
    private var knowledgeBase: String? = null
    private var resultsRV: RecyclerView? = null
    private var pp: Boolean = false

    private var closeViewer: ImageButton ? = null
    private var docComments: ImageButton ? = null
    private var docCommentRV: RecyclerView? = null
    private var addComment: Button? = null
    private var searchSchool: TextView? = null
    private var docCommentET: EditText? = null
    private var bottomSheet: View? = null
     private var addDoc: FloatingActionButton? = null
     private var searchProgress: ProgressBar? = null
    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null
    var docViewer: WebView? = null
    var viewerRel: RelativeLayout? = null

    var profilePics = ArrayList<String>()
    private var publishedDocs = FirebaseFirestore.getInstance().collection("Content")
    .document("Documents")

    private var uploadedLectures = FirebaseFirestore.getInstance().collection("Content")
    .document("Lectures")

    private var registeredUsers = FirebaseFirestore.getInstance().collection("Users")

    private var results = SearchResultsAdapter(this, searchList, profilePics, "ToDo")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        flag = intent.getStringExtra("flag")
        addDocBtn = intent.getStringExtra("addDocBtn")
        school = intent.getStringExtra("school")
        pos = intent.getIntExtra("pos", 99)

        viewerRel = findViewById(R.id.docViewerRel)
        docViewer = findViewById(R.id.docView)
        addDoc = findViewById(R.id.addDoc)
        searchProgress = findViewById(R.id.searchProgress)

        if (addDocBtn == "set"){
            if (flag.equals("Lecture search")){
                uploadedLectures.collection(school!!).limit(20).get()
                    .addOnCompleteListener{
                        if (it.isSuccessful){
                            searchProgress!!.visibility = View.GONE
                            val document: ArrayList<SelectedDoc> = it.result!!.toObjects(
                                SelectedDoc::class.java) as ArrayList<SelectedDoc>
                            val results =
                                CloudVideosAdapter(
                                    null,
                                    null,
                                    null,
                                    applicationContext,
                                    this@SearchResults,
                                    document,
                                    null,
                                    null
                                )
                            results.notifyDataSetChanged()
                            resultsRV!!.adapter = results
                        }

                    }
            }else{
                publishedDocs.collection(school!!)
                    .limit(20)
                    .get()
                    .addOnCompleteListener{
                        if (it.isSuccessful){
                            searchProgress!!.visibility = View.GONE
                            val document: ArrayList<SelectedDoc> = it.result!!.toObjects(
                                SelectedDoc::class.java) as ArrayList<SelectedDoc>
                            val docAdt =
                                DocumentAdapter(
                                    null,
                                    applicationContext,
                                    this@SearchResults,
                                    document
                                )
                            docAdt.notifyDataSetChanged()
                            resultsRV!!.adapter = docAdt


                        }

                    }
            }

            addDoc!!.visibility = View.VISIBLE
        }

        closeViewer = findViewById(R.id.closeDocView)
        docComments = findViewById(R.id.docComments)
        docCommentRV = findViewById(R.id.docCommentsRV)
        addComment = findViewById(R.id.addCommentBtn)
        docCommentET = findViewById(R.id.docCommentET)
        searchSchool = findViewById(R.id.searchSchool)
        bottomSheet = findViewById(R.id.comments_sheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet!!)

        val searchDocs = findViewById<EditText>(R.id.searchDocs)
        val performFilter = findViewById<Button>(R.id.performFilter)
        val search = findViewById<ImageButton>(R.id.search)
        val yearSpinner = findViewById<Spinner>(R.id.filterSpinner)
        val institutionSpinner = findViewById<Spinner>(R.id.institutionSpinner)
        val subFieldSpinner = findViewById<Spinner>(R.id.subFieldSpinner)
        val searchDocRel = findViewById<RelativeLayout>(R.id.searchDocRel)
        val searchDocLin = findViewById<RelativeLayout>(R.id.filterLayout)
        val topLayout = findViewById<RelativeLayout>(R.id.top_layout)
        val ppCheck = findViewById<CheckBox>(R.id.ppCheck)

        resultsRV = findViewById(R.id.resultsRV)

        institutionSpinner.bringToFront()
        subFieldSpinner.bringToFront()
        yearSpinner.bringToFront()
        searchDocRel.bringToFront()
        searchDocLin.bringToFront()
        searchDocs.bringToFront()
        search.bringToFront()
        topLayout.bringToFront()

        yearSpinner.onItemSelectedListener = this
        institutionSpinner.onItemSelectedListener = this
        subFieldSpinner.onItemSelectedListener = this

        searchSchool!!.text = school

        val yrFilterAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.yearFilter, android.R.layout.simple_spinner_item
        )
        val mainFilterAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.main_fields, android.R.layout.simple_spinner_item
        )
        val institutionFilterAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.institutions1, android.R.layout.simple_spinner_item
        )

        yrFilterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearSpinner.adapter = yrFilterAdapter


        institutionFilterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        institutionSpinner.adapter = institutionFilterAdapter

        if (pos != null && pos != 99) {
            val subFieldAdapter = ArrayAdapter(
                applicationContext,
                R.layout.spinner_drop_down_yangu1,
                DocSorting.getSubFields(pos!!)
            )
            subFieldAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            subFieldSpinner.adapter = subFieldAdapter
        }

        subFieldSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                subField = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                year = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        institutionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                institution = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        resultsRV!!.hasFixedSize()
        resultsRV!!.layoutManager = GridLayoutManager(this, 2)

        performFilter.setOnClickListener{
            if (ppCheck.isChecked){
                performSearch(flag!!, searchProgress!!, true)
            }else{
                performSearch(flag!!, searchProgress!!, false)
            }
        }

        ppCheck.setOnCheckedChangeListener { buttonView: CompoundButton, _: Boolean ->
            pp = buttonView.isChecked
        }
        addDoc!!.setOnClickListener {
            if (flag.equals("Lecture search")){
                val vidType = VidType(
                    this@SearchResults,
                    null,
                    null,
                    school
                )
                vidType.show()
            }else{
                val intent1 = Intent(
                    this@SearchResults,
                    SourceDocList::class.java
                )
                intent1.putExtra("addDoc", "addDoc")
                intent1.putExtra("SchoolName", school)
                startActivity(intent1)
            }
        }

        search.setOnClickListener{
            searchInput = searchDocs.text.toString()
            searchFireStore(searchInput!!.lowercase(Locale.getDefault()), flag!!, school!!)
        }

        if (intent.getStringExtra("userProfile") != null){
//            searchDocLin!!.visibility = View.GONE
            val documents = ArrayList<SelectedDoc>()
            val lectures = ArrayList<SelectedDoc>()
            val user = FirebaseAuth.getInstance().currentUser
            val school = intent.getStringExtra("school")
            if (intent.getStringExtra("userProfile").equals("UserProfile")){
                publishedDocs.collection(school.toString())
                    .whereEqualTo("uid", user!!.uid)
                    .get().addOnSuccessListener {
                        //Save the school object
                        for (qds in it) {
                            val document = qds.toObject(SelectedDoc::class.java)
                            documents.add(document)
                        }
                        userDocAdt = DocumentAdapter(
                            null,
                            this@SearchResults,
                            applicationContext,
                            documents
                        )
                        userDocAdt!!.notifyDataSetChanged()
                        resultsRV!!.adapter = userDocAdt

                    }.addOnFailureListener{

                    }
            }else if (intent.getStringExtra("userProfile").equals("UserProfile1")){
                uploadedLectures.collection(school.toString())
                    .whereEqualTo("uid", user!!.uid)
                    .get().addOnSuccessListener {
                        for (qds in it) {
                            val lecture = qds.toObject(SelectedDoc::class.java)
                            lectures.add(lecture)
                        }
                        userLecAdt =
                            CloudVideosAdapter(
                                null,
                                null,
                                null,
                                this@SearchResults,
                                applicationContext,
                                lectures,
                                application,
                                parent
                            )

                        userDocAdt!!.notifyDataSetChanged()
                        resultsRV!!.adapter = userDocAdt
                    }.addOnFailureListener{

                    }
            }
        }

        searchDocs.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

     private fun searchFireStore(searchInput: String, flag: String, school: String) {
        val docRef = publishedDocs.collection(school)
        val docRef1 = uploadedLectures.collection(school)
        when(flag){
            "User search" -> {
                Toast.makeText(applicationContext, "Performing search", Toast.LENGTH_SHORT).show()
                registeredUsers.whereArrayContains("search_keyword", searchInput).get()
                        .addOnCompleteListener{
                            if (it.isSuccessful){
                                searchList = it.result!!.toObjects(SearchModel::class.java) as ArrayList<SearchModel>
                                results.searchList  = searchList
                                results.notifyDataSetChanged()
                            }
                        }
            }
            "Doc search" -> {
                Toast.makeText(applicationContext, "Performing search", Toast.LENGTH_SHORT).show()
                if (knowledgeBase == "All"){
                    docRef
                        .whereGreaterThanOrEqualTo("mainField", searchInput)
                        .whereLessThanOrEqualTo("mainField", searchInput + "\uf8ff")
                        .get()
                        .addOnCompleteListener{
                            if (it.isSuccessful){
                                val document: ArrayList<SelectedDoc> = it.result!!.toObjects(
                                    SelectedDoc::class.java) as ArrayList<SelectedDoc>
                                val docAdt =
                                    DocumentAdapter(
                                        null,
                                        applicationContext,
                                        this@SearchResults,
                                        document
                                    )
                                docAdt.notifyDataSetChanged()
                                resultsRV!!.adapter = docAdt

//                                searchList = it.result!!.toObjects(SearchModel::class.java) as ArrayList<SearchModel>
//                                results = SearchResultsAdapter(applicationContext, searchList, null, "Doc search")
//                                results.notifyDataSetChanged()
                            }
                        }
                }else{
                    docRef.whereEqualTo("knowledgeBase", knowledgeBase)
                        .whereArrayContains("search_keyword", searchInput).get()
                        .addOnCompleteListener{
                            if (it.isSuccessful){
                                val document: ArrayList<SelectedDoc> = it.result!!.toObjects(
                                    SelectedDoc::class.java) as ArrayList<SelectedDoc>
                                val docAdt =
                                    DocumentAdapter(
                                        null,
                                        applicationContext,
                                        this@SearchResults,
                                        document
                                    )
                                docAdt.notifyDataSetChanged()
                                resultsRV!!.adapter = docAdt


                            }
                        }
                }

            }
            "Lecture search" -> {
                Toast.makeText(applicationContext, "Performing search", Toast.LENGTH_SHORT).show()
                docRef1
                    .whereEqualTo("knowledgeBase", knowledgeBase)
                    .whereArrayContains("search_keyword", searchInput).get()
                        .addOnCompleteListener{
                            if (it.isSuccessful){
                                searchList = it.result!!.toObjects(SearchModel::class.java) as ArrayList<SearchModel>
                                results.searchList  = searchList
                                results.notifyDataSetChanged()
                            }
                        }
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        knowledgeBase = parent!!.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

     override fun onResume() {
         super.onResume()
         window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
     }

     private fun performSearch(flag: String, readBuffer: ProgressBar, pastPaper: Boolean) {
         when(flag){
             "User search" -> {
                 registeredUsers
                     .whereEqualTo("knowledgeBase", institution)
                     .whereEqualTo("mainField", mainField)
                     .whereEqualTo("subField", subField)
                     .whereEqualTo("knowledgeBase", year)
//                      .whereArrayContains("search_keyword", searchInput)
                     .get()
                     .addOnCompleteListener{
                         readBuffer.visibility = View.GONE
                         if (it.isSuccessful){
                             searchList = it.result!!.toObjects(SearchModel::class.java) as ArrayList<SearchModel>
                             results.searchList  = searchList
                             results.notifyDataSetChanged()
                         }
                     }
             }
             "Doc search" -> {
                 if (institution == "All" && year == "All" && searchInput == null){
                     if (!pastPaper){
                         publishedDocs
                             .collection(school!!)
                             .whereEqualTo("subField", subField)
                             .get()
                             .addOnCompleteListener{
                                 if (it.isSuccessful){
                                     readBuffer.visibility = View.GONE
                                     val document: ArrayList<SelectedDoc> = it.result!!.toObjects(
                                         SelectedDoc::class.java) as ArrayList<SelectedDoc>
                                     val docAdt =
                                         DocumentAdapter(
                                             null,
                                             applicationContext,
                                             this@SearchResults,
                                             document
                                         )
                                     docAdt.notifyDataSetChanged()
                                     resultsRV!!.adapter = docAdt


                                 }

                             }
                     }else{
                         publishedDocs
                             .collection(school!!)
                             .whereEqualTo("subField", subField)
                             .whereEqualTo("pastPaper", "True")
                             .get()
                             .addOnCompleteListener{
                                 if (it.isSuccessful){
                                     readBuffer.visibility = View.GONE
                                     val document: ArrayList<SelectedDoc> = it.result!!.toObjects(
                                         SelectedDoc::class.java) as ArrayList<SelectedDoc>
                                     val docAdt =
                                         DocumentAdapter(
                                             null,
                                             applicationContext,
                                             this@SearchResults,
                                             document
                                         )
                                     docAdt.notifyDataSetChanged()
                                     resultsRV!!.adapter = docAdt


                                 }

                             }
                     }

                 }else
                     if (institution != "All" && year == "All" && searchInput == null){
                        if (!pastPaper){
                            publishedDocs
                                .collection(school!!)
                                .whereEqualTo("institution", institution)
                                .whereEqualTo("subField", subField)
                                .get()
                                .addOnCompleteListener{
                                    if (it.isSuccessful){
                                        readBuffer.visibility = View.GONE
                                        val document: ArrayList<SelectedDoc> = it.result!!.toObjects(
                                            SelectedDoc::class.java) as ArrayList<SelectedDoc>
                                        val docAdt =
                                            DocumentAdapter(
                                                null,
                                                applicationContext,
                                                this@SearchResults,
                                                document
                                            )
                                        docAdt.notifyDataSetChanged()
                                        resultsRV!!.adapter = docAdt


                                    }

                                }
                        }else{
                            publishedDocs
                                .collection(school!!)
                                .whereEqualTo("institution", institution)
                                .whereEqualTo("subField", subField)
                                .whereEqualTo("pastPaper", "True")
                                .get()
                                .addOnCompleteListener{
                                    if (it.isSuccessful){
                                        readBuffer.visibility = View.GONE
                                        val document: ArrayList<SelectedDoc> = it.result!!.toObjects(
                                            SelectedDoc::class.java) as ArrayList<SelectedDoc>
                                        val docAdt =
                                            DocumentAdapter(
                                                null,
                                                applicationContext,
                                                this@SearchResults,
                                                document
                                            )
                                        docAdt.notifyDataSetChanged()
                                        resultsRV!!.adapter = docAdt


                                    }

                                }
                        }
                 }else
                     if (institution == "All" && year != "All" && searchInput == null){
                        if (!pastPaper){
                            publishedDocs
                                .collection(school!!)
                                .whereEqualTo("knowledgeBase", year)
                                .whereEqualTo("subField", subField)
                                .get()
                                .addOnCompleteListener{
                                    if (it.isSuccessful){
                                        readBuffer.visibility = View.GONE
                                        val document: ArrayList<SelectedDoc> = it.result!!.toObjects(
                                            SelectedDoc::class.java) as ArrayList<SelectedDoc>
                                        val docAdt =
                                            DocumentAdapter(
                                                null,
                                                applicationContext,
                                                this@SearchResults,
                                                document
                                            )
                                        docAdt.notifyDataSetChanged()
                                        resultsRV!!.adapter = docAdt


                                    }

                                }
                        }else{
                            publishedDocs
                                .collection(school!!)
                                .whereEqualTo("knowledgeBase", year)
                                .whereEqualTo("subField", subField)
                                .whereEqualTo("PastPaper", "True")
                                .get()
                                .addOnCompleteListener{
                                    if (it.isSuccessful){
                                        readBuffer.visibility = View.GONE
                                        val document: ArrayList<SelectedDoc> = it.result!!.toObjects(
                                            SelectedDoc::class.java) as ArrayList<SelectedDoc>
                                        val docAdt =
                                            DocumentAdapter(
                                                null,
                                                applicationContext,
                                                this@SearchResults,
                                                document
                                            )
                                        docAdt.notifyDataSetChanged()
                                        resultsRV!!.adapter = docAdt


                                    }

                                }
                        }
                 }else
                     if (institution != "All" && year != "All" && searchInput == null){
                        if (!pastPaper){
                            publishedDocs
                                .collection(school!!)
                                .whereEqualTo("institution", institution)
                                .whereEqualTo("subField", subField)
                                .whereEqualTo("knowledgeBase", year)
                                .get()
                                .addOnCompleteListener{
                                    if (it.isSuccessful){
                                        readBuffer.visibility = View.GONE
                                        val document: ArrayList<SelectedDoc> = it.result!!.toObjects(
                                            SelectedDoc::class.java) as ArrayList<SelectedDoc>
                                        val docAdt =
                                            DocumentAdapter(
                                                null,
                                                applicationContext,
                                                this@SearchResults,
                                                document
                                            )
                                        docAdt.notifyDataSetChanged()
                                        resultsRV!!.adapter = docAdt


                                    }

                                }
                        }else{
                            publishedDocs
                                .collection(school!!)
                                .whereEqualTo("institution", institution)
                                .whereEqualTo("subField", subField)
                                .whereEqualTo("knowledgeBase", year)
                                .whereEqualTo("pastPaper", "True")
                                .get()
                                .addOnCompleteListener{
                                    if (it.isSuccessful){
                                        readBuffer.visibility = View.GONE
                                        val document: ArrayList<SelectedDoc> = it.result!!.toObjects(
                                            SelectedDoc::class.java) as ArrayList<SelectedDoc>
                                        val docAdt =
                                            DocumentAdapter(
                                                null,
                                                applicationContext,
                                                this@SearchResults,
                                                document
                                            )
                                        docAdt.notifyDataSetChanged()
                                        resultsRV!!.adapter = docAdt


                                    }

                                }
                        }
                 }


             }
             "Lecture search" -> {
                 if (institution == "All" && year == "All" && searchInput == null){
                    if (!pastPaper){
                        uploadedLectures
                            .collection(school!!)
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
                                            applicationContext,
                                            this@SearchResults,
                                            document,
                                            null,
                                            null
                                        )
                                    results.notifyDataSetChanged()
                                    resultsRV!!.adapter = results

                                }

                            }
                    }else{
                        uploadedLectures
                            .collection(school!!)
                            .whereEqualTo("subField", subField)
                            .whereEqualTo("pastPaper", "True")
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
                                            applicationContext,
                                            this@SearchResults,
                                            document,
                                            null,
                                            null
                                        )
                                    results.notifyDataSetChanged()
                                    resultsRV!!.adapter = results

                                }

                            }
                    }
                 }else
                     if (institution != "All" && year == "All" && searchInput == null){
                         if (!pastPaper){
                             uploadedLectures
                                 .collection(school!!)
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
                                                 applicationContext,
                                                 this@SearchResults,
                                                 document,
                                                 null,
                                                 null
                                             )
                                         results.notifyDataSetChanged()
                                         resultsRV!!.adapter = results
                                     }

                                 }
                         }else{
                             uploadedLectures
                                 .collection(school!!)
                                 .whereEqualTo("institution", institution)
                                 .whereEqualTo("subField", subField)
                                 .whereEqualTo("pastPaper", "True")
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
                                                 applicationContext,
                                                 this@SearchResults,
                                                 document,
                                                 null,
                                                 null
                                             )
                                         results.notifyDataSetChanged()
                                         resultsRV!!.adapter = results
                                     }

                                 }
                         }
                     }else
                         if (institution == "All" && year != "All" && searchInput == null){
                            if (!pastPaper){
                                uploadedLectures
                                    .collection(school!!)
                                    .whereEqualTo("knowledgeBase", year)
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
                                                    applicationContext,
                                                    this@SearchResults,
                                                    document,
                                                    null,
                                                    null
                                                )
                                            results.notifyDataSetChanged()
                                            resultsRV!!.adapter = results
                                        }

                                    }
                            }else{
                                uploadedLectures
                                    .collection(school!!)
                                    .whereEqualTo("knowledgeBase", year)
                                    .whereEqualTo("subField", subField)
                                    .whereEqualTo("pastPaper", "True")
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
                                                    applicationContext,
                                                    this@SearchResults,
                                                    document,
                                                    null,
                                                    null
                                                )
                                            results.notifyDataSetChanged()
                                            resultsRV!!.adapter = results
                                        }

                                    }
                            }
                         }else
                             if (institution != "All" && year != "All" && searchInput == null){
                                 if (!pastPaper){
                                     uploadedLectures
                                         .collection(school!!)
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
                                                         applicationContext,
                                                         this@SearchResults,
                                                         document,
                                                         null,
                                                         null
                                                     )
                                                 results.notifyDataSetChanged()
                                                 resultsRV!!.adapter = results
                                             }

                                         }
                                 }else{
                                     uploadedLectures
                                         .collection(school!!)
                                         .whereEqualTo("institution", institution)
                                         .whereEqualTo("subField", subField)
                                         .whereEqualTo("knowledgeBase", year)
                                         .whereEqualTo("pastPaper", "True")
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
                                                         applicationContext,
                                                         this@SearchResults,
                                                         document,
                                                         null,
                                                         null
                                                     )
                                                 results.notifyDataSetChanged()
                                                 resultsRV!!.adapter = results
                                             }

                                         }
                                 }
                             }

             }
         }
     }
}