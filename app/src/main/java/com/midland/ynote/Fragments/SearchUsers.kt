package com.midland.ynote.Fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.midland.ynote.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.midland.ynote.Activities.GeneralSearch
import com.midland.ynote.Adapters.UsersAdapter
import com.midland.ynote.Objects.User
import com.midland.ynote.Utilities.DocSorting
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [SearchUsers.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchUsers : Fragment(), AdapterView.OnItemSelectedListener {
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mainField: String? = null
    private var subField: String? = null
    private var institution: String? = null
    private val usersAdapter: UsersAdapter? = null
    private val users = ArrayList<User>()

    private var year: String? = null

    private var viewerRel: RelativeLayout? = null
    private var closeViewer: ImageButton? = null
    private var docComments: ImageButton? = null
    private var docCommentRV: RecyclerView? = null
    private var addComment: Button? = null
    private var searchSchool: TextView? = null
    private var docCommentET: EditText? = null
    private var bottomSheet: View? = null
    private var bottomSheetBehavior: BottomSheetBehavior<View?>? = null
    private var docViewer: WebView? = null
    private var resultsRV: RecyclerView? = null
    private var glowRel: RelativeLayout? = null
    var animatorSet: AnimatorSet? = null
    private var con: Context? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_search_users, container, false)
        viewerRel = v.findViewById(R.id.docViewerRel)
        docViewer = v.findViewById(R.id.docView)
        closeViewer = v.findViewById(R.id.closeDocView)
        docComments = v.findViewById(R.id.docComments)
        docCommentRV = v.findViewById(R.id.docCommentsRV)
        glowRel = v.findViewById(R.id.glowRel)
        addComment = v.findViewById(R.id.addCommentBtn)
        docCommentET = v.findViewById(R.id.docCommentET)
        searchSchool = v.findViewById(R.id.searchSchool)
        bottomSheet = v.findViewById(R.id.comments_sheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet!!)
        val searchDocs = v.findViewById<EditText>(R.id.searchDocs)
        val search = v.findViewById<ImageButton>(R.id.search)
        val performFilter = v.findViewById<Button>(R.id.performFilter)
        val performFilter1 = v.findViewById<Button>(R.id.performFilter1)
        val performFilter2 = v.findViewById<Button>(R.id.performFilter2)
        val institutionSpinner = v.findViewById<Spinner>(R.id.institutionSpinner)
        val subFieldSpinner = v.findViewById<Spinner>(R.id.subFieldSpinner)
        val mainFieldSpinner = v.findViewById<Spinner>(R.id.filterSpinner)
        val searchDocRel = v.findViewById<RelativeLayout>(R.id.searchDocRel)
        val searchDocLin = v.findViewById<RelativeLayout>(R.id.filterLayout)
        val topLayout = v.findViewById<RelativeLayout>(R.id.top_layout)
        resultsRV = v.findViewById(R.id.resultsRV)
        institutionSpinner.bringToFront()
        subFieldSpinner.bringToFront()
        mainFieldSpinner.bringToFront()
        searchDocRel.bringToFront()
        searchDocLin.bringToFront()
        searchDocs.bringToFront()
        search.bringToFront()
        topLayout.bringToFront()
        glowRel!!.bringToFront()

//        searchDocs.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                searchUsers(s.toString().lowercase(Locale.getDefault()))
//            }
//
//            override fun afterTextChanged(s: Editable) {}
//        })


        animatorSet = AnimatorSet()
        val fadeOut: android.animation.ObjectAnimator = android.animation.ObjectAnimator.ofFloat(glowRel, "alpha", 0.5f, 0.1f)
        fadeOut.duration = 500
        val fadeIn: android.animation.ObjectAnimator = android.animation.ObjectAnimator.ofFloat(glowRel, "alpha", 0.1f, 0.5f)
        fadeIn.duration = 500
        animatorSet!!.play(fadeIn).after(fadeOut)
        animatorSet!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                animatorSet!!.start()
                super.onAnimationEnd(animation)
            }
        })
        animatorSet!!.start()
        institutionSpinner.onItemSelectedListener = this
        subFieldSpinner.onItemSelectedListener = this
        mainFieldSpinner.onItemSelectedListener = this


        val mainFilterAdapter = context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.main_fields, android.R.layout.simple_spinner_item
            )
        }
        val institutionFilterAdapter = context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.institutions1, android.R.layout.simple_spinner_item
            )
        }



        institutionFilterAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        institutionSpinner.adapter = institutionFilterAdapter

        mainFilterAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mainFieldSpinner.adapter = mainFilterAdapter

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
        mainFieldSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                mainField = parent.getItemAtPosition(position).toString()
                val subFieldAdapter = context?.let {
                    ArrayAdapter(
                        it,
                        R.layout.spinner_drop_down_yangu1,
                        ArrayList(listOf(*DocSorting.getSubFields(position)))
                    )
                }
                subFieldAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                subFieldSpinner.adapter = subFieldAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
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


        resultsRV!!.hasFixedSize()
        resultsRV!!.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        docCommentRV!!.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

//        GeneralSearch.performSearch("User search", institution!!, mainField!!, subField!!,
//            year!!, searchDocs.text.toString().trim(), con!!, readBuffer!!, resultsRV)

        performFilter.setOnClickListener {
            context?.let { it1 ->
                GeneralSearch.performSearch("User search", institution!!, mainField!!, subField!!,
                    "",
                    searchDocs.text.toString().trim(),
                    it1,
                    glowRel!!,
                    resultsRV!!)
            }
        }
        performFilter1.setOnClickListener {
            context?.let { it1 ->
                GeneralSearch.performSearch("User search1", institution!!, mainField!!, subField!!,
                    "",
                    searchDocs.text.toString().trim(),
                    it1,
                    glowRel!!,
                    resultsRV!!)
            }
        }
        performFilter2.setOnClickListener {
            context?.let { it1 ->
                GeneralSearch.performSearch("User search2", institution!!, mainField!!, subField!!,
                    "",
                    searchDocs.text.toString().trim(),
                    it1,
                    glowRel!!,
                    resultsRV!!)
            }
        }

//        GeneralSearch.performSearch("User search", institution!!, mainField!!, subField!!,
//            "",
//            searchDocs.text.toString().trim(),
//            con!!,
//            glowRel!!,
//            resultsRV)

        return v
    }

    private fun searchUsers(s: String) {
        val query =
            FirebaseDatabase.getInstance().getReference("Users").orderByChild("username").startAt(s)
                .endAt(s + "\uf8ff")
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                users.clear()
                for (d in snapshot.children) {
                    val user = d.getValue(
                        User::class.java
                    )
                    users.add(user!!)
                }
                usersAdapter!!.notifyDataSetChanged()
                resultsRV!!.adapter = usersAdapter
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    companion object {
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
         * @return A new instance of fragment SearchUsers.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): SearchUsers {
            val fragment = SearchUsers()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}