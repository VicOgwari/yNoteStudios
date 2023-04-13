package com.midland.ynote.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.midland.ynote.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.midland.ynote.Adapters.PictorialAdapter
import com.midland.ynote.Objects.PictorialObject
import com.midland.ynote.Utilities.Projector
import com.ortiz.touchview.TouchImageView

/**
 * A simple [Fragment] subclass.
 * Use the [StudioBitmaps.newInstance] factory method to
 * create an instance of this fragment.
 */
class StudioBitmaps : Fragment {
    var studioPhotoDocsRV: RecyclerView? = null
    var pictorialLocale: String? = null
    private var pictorials: ArrayList<PictorialObject>? = null


    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null


    constructor() {
        // Required empty public constructor
    }

    constructor(pictorialLocale: String?) {
        this.pictorialLocale = pictorialLocale
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.studio_pictorial_fragment, container, false)

        studioPhotoDocsRV = v.findViewById(R.id.studioBitmapsRV)
        touchIV = v.findViewById(R.id.touchIV)

        var pictorialAdapter: PictorialAdapter? = null
        if (pictorialLocale != null){
            val sharedPreferences = context?.getSharedPreferences(pictorialLocale, Context.MODE_PRIVATE)
            if (sharedPreferences!!.contains(pictorialLocale)){
                val gson1 = Gson()
                val json1 = sharedPreferences.getString(pictorialLocale, "")
                val type1 = object : TypeToken<ArrayList<PictorialObject>>() {}.type
                pictorials = gson1.fromJson(json1, type1)!!
                pictorialAdapter = PictorialAdapter(
                    context, pictorials, pictorialLocale,
                    null, null, null, this, "studio", touchIV
                )
                pictorialAdapter.notifyDataSetChanged()
//                fab!!.visibility = View.INVISIBLE
//                textView!!.visibility = View.INVISIBLE
            }else{
//                fab!!.visibility = View.VISIBLE
//                textView!!.visibility = View.VISIBLE
            }
        }

        studioPhotoDocsRV!!.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        studioPhotoDocsRV!!.adapter = pictorialAdapter

        return v
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.photo_doc_menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.projector -> {
                val intent = Intent(context, Projector::class.java)
                if (pictorials!!.size > 0) {
                    intent.putExtra("photoDocs", pictorials)
                    startActivity(intent)
                } else {
                    Toast.makeText(context, "No items to project", Toast.LENGTH_SHORT).show()
                }
            }
            else -> return false
        }
        return super.onOptionsItemSelected(item)
    }
    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        var touchIV: TouchImageView? = null

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OtherVideos.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): StudioBitmaps {
            val fragment = StudioBitmaps()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }


}