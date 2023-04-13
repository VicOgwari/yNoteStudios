package com.midland.ynote.Fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.midland.ynote.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.midland.ynote.Adapters.PicsAdapter
import com.midland.ynote.Objects.BitMapTitle

/**
 * A simple [Fragment] subclass.
 * Use the [BitMaps.newInstance] factory method to
 * create an instance of this fragment.
 */
class BitMaps : Fragment() {
    private var bitMapAdapter: PicsAdapter? = null

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    lateinit var bitMaps: ArrayList<BitMapTitle>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_pictorials, container, false)
        val bitmapRV = v.findViewById<RecyclerView>(R.id.bitmapsRV)
        val sharedPreferences = context?.getSharedPreferences("bitmapTitle", Context.MODE_PRIVATE)

        bitMaps = ArrayList()
        bitMaps = if (sharedPreferences!!.contains("bitmapTitle")) {
            val gson = Gson()
            val json = sharedPreferences.getString("bitmapTitle", "")
            val type = object : TypeToken<ArrayList<BitMapTitle>>() {}.type
            gson.fromJson(json, type)!!
        }else{
            ArrayList()
        }

        if (bitMaps.size > 0){
            bitMaps.reverse()
            bitMapAdapter = PicsAdapter(
                context,
                bitMaps,
                "fragment",
                null,
                null
            )
            bitMapAdapter!!.notifyDataSetChanged()
        }
        bitmapRV.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        bitmapRV.adapter = bitMapAdapter

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
         * @return A new instance of fragment Pictorials.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): BitMaps {
            val fragment = BitMaps()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}



