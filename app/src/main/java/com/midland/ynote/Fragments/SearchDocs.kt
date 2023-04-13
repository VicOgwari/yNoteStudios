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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.midland.ynote.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.midland.ynote.Activities.GeneralSearch
import com.midland.ynote.Utilities.DocSorting

/**
 * A simple [Fragment] subclass.
 * Use the [SearchDocs.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchDocs() : Fragment(), AdapterView.OnItemSelectedListener {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mainField: String? = null
    private var subField: String? = null
    private var institution: String? = "All"
    private var year: String? = "All"

    private var viewerRel: RelativeLayout? = null
    private var glowRel: RelativeLayout? = null
    private var closeViewer: ImageButton? = null
    private var docComments: ImageButton? = null
    private var docCommentRV: RecyclerView? = null
    private var addComment: Button? = null
    var animatorSet: AnimatorSet? = null
    private var searchSchool: TextView? = null
    private var docCommentET: EditText? = null
    private var bottomSheet: View? = null
    private var bottomSheetBehavior: BottomSheetBehavior<View?>? = null
    private var docViewer: WebView? = null
    private var con: Context? = context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_search_docs, container, false)
        viewerRel = v.findViewById(R.id.docViewerRel)
        docViewer = v.findViewById(R.id.docView)
        closeViewer = v.findViewById(R.id.closeDocView)
        glowRel = v.findViewById(R.id.glowRel)
        docComments = v.findViewById(R.id.docComments)
        docCommentRV = v.findViewById(R.id.docCommentsRV)
        addComment = v.findViewById(R.id.addCommentBtn)
        docCommentET = v.findViewById(R.id.docCommentET)
        searchSchool = v.findViewById(R.id.searchSchool)
        bottomSheet = v.findViewById(R.id.comments_sheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet!!)
        val searchDocs = v.findViewById<EditText>(R.id.searchDocs)
        val search = v.findViewById<ImageButton>(R.id.search)
        val performFilter = v.findViewById<Button>(R.id.performFilter)
        val performFilter1 = v.findViewById<Button>(R.id.performFilter1)
        val yearSpinner = v.findViewById<Spinner>(R.id.filterSpinner)
        val institutionSpinner = v.findViewById<Spinner>(R.id.institutionSpinner)
        val subFieldSpinner = v.findViewById<Spinner>(R.id.subFieldSpinner)
        val mainFieldSpinner = v.findViewById<Spinner>(R.id.mainFieldSpinner)
        val searchDocRel = v.findViewById<RelativeLayout>(R.id.searchDocRel)
        val searchDocLin = v.findViewById<RelativeLayout>(R.id.filterLayout)
        val topLayout = v.findViewById<RelativeLayout>(R.id.top_layout)
        val resultsRV: RecyclerView = v.findViewById(R.id.resultsRV)
        institutionSpinner.bringToFront()
        subFieldSpinner.bringToFront()
        yearSpinner.bringToFront()
        mainFieldSpinner.bringToFront()
        searchDocRel.bringToFront()
        searchDocLin.bringToFront()
        searchDocs.bringToFront()
        search.bringToFront()
        topLayout.bringToFront()
        glowRel!!.bringToFront()

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
        yearSpinner.onItemSelectedListener = this
        institutionSpinner.onItemSelectedListener = this
        subFieldSpinner.onItemSelectedListener = this
        mainFieldSpinner.onItemSelectedListener = this


        val yrFilterAdapter = context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.yearFilter, android.R.layout.simple_spinner_item
            )
        }
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

        yrFilterAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearSpinner.adapter = yrFilterAdapter


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


        resultsRV.hasFixedSize()
        resultsRV.layoutManager = GridLayoutManager(context, 2)

//        context?.let {
//            GeneralSearch.performSearch("Doc search",
//                institution!!,
//                mainField!!,
//                subField!!,
//                year!!,
//                searchDocs.text.toString().trim(), it, readBuffer!!, resultsRV)
//        }

        performFilter.setOnClickListener {
            context?.let { it1 ->
                GeneralSearch.performSearch("Doc search", institution!!, mainField!!, subField!!,
                    year!!, searchDocs.text.toString().trim(), it1, glowRel!!, resultsRV)
            }
        }
        performFilter1.setOnClickListener {
            context?.let { it1 ->
                GeneralSearch.performSearch("Lecture search", institution!!, mainField!!, subField!!,
                    year!!, searchDocs.text.toString().trim(), it1, glowRel!!, resultsRV)
            }
        }

//        GeneralSearch.performSearch("Doc search",
//            institution!!,
//            mainField!!,
//            subField!!,
//            year!!, searchDocs.text.toString().trim(), con!!, readBuffer!!, resultsRV)

        return v
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
         * @return A new instance of fragment SearchDocs.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): SearchDocs {
            val fragment = SearchDocs()
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