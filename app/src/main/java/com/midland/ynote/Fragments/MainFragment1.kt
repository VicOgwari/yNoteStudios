package com.midland.ynote.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.midland.ynote.R
import com.google.firebase.firestore.FirebaseFirestore
import com.midland.ynote.Adapters.HomeBitmapsAdapter
import com.midland.ynote.Objects.BitMapTitle
import com.ortiz.touchview.TouchImageView
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val mColumnCount = 1

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment1.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment1 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main2, container, false)
        val pictorialProgress = view.findViewById<ProgressBar>(R.id.pictorialProgress)
        val pictorialsRV = view.findViewById<RecyclerView>(R.id.pictorialsRV)
        touchIV = view.findViewById(R.id.touchIV)
        pictorialsRV.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        pictorialsRV.bringToFront()
        pictorialProgress.bringToFront()
        val cloudMaps = ArrayList<BitMapTitle>()
        FirebaseFirestore.getInstance().collection("Pictorials").get()
            .addOnSuccessListener{ queryDocumentSnapshots ->
                pictorialProgress.visibility = View.GONE
                    for (qds in queryDocumentSnapshots) {
                        try {
                            val title = qds.getString("title")
                            val thumbnail = qds.getString("thumbnail")
                            val relevance = qds.getString("relevance")
                            val pictures = qds.get("pictures") as ArrayList<String>
                            val narrations = qds.get("narrations") as ArrayList<String>
                            val descriptions = qds.get("descriptions") as ArrayList<String>
                            val viewsCount = qds.get("viewCount").toString()
                            val ratings: Double = qds.get("ratings").toString().toDouble()
                            val ratersCount: Int = Integer.parseInt(qds.get("ratersCount").toString())
                            val commentsCount = qds["commentsCount"].toString()
                            val savesCount = qds["saveCount"].toString()
                            val slideCount = qds["slideCount"].toString()
                            val uid = qds["uid"].toString()
                            val displayName = qds["displayName"].toString()

                            val bt = BitMapTitle(
                                title,
                                relevance,
                                thumbnail,
                                pictures,
                                narrations,
                                descriptions,
                                viewsCount,
                                commentsCount,
                                savesCount,
                                slideCount,
                                uid,
                                displayName,
                                ratersCount,
                                ratings
                            )
                            cloudMaps.add(bt)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

//                    for (i in picOrder){
//                        newPicOrder.add(pictures[i])
//                    }
//                    for (j in narOrder){
//                        newNarOrder.add(narrations[j])
//                    }


                    }

            }
            .addOnCompleteListener {
                val cloudAdapter = HomeBitmapsAdapter(
                    cloudMaps,
                    context,
                    activity
                )
                cloudAdapter.notifyDataSetChanged()
                pictorialsRV.adapter = cloudAdapter
                if (context != null){
                    Toast.makeText(context, "Loaded!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
            }

        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment2.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        var touchIV: TouchImageView? = null
        fun newInstance(param1: String, param2: String) =
            MainFragment1().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}