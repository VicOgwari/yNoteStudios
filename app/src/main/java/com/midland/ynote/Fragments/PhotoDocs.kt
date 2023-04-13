package com.midland.ynote.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.midland.ynote.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.midland.ynote.Adapters.PictorialAdapter
import com.midland.ynote.Adapters.SelectedItemsAdt
import com.midland.ynote.Objects.PictorialObject
import com.midland.ynote.Utilities.Projector
import com.ortiz.touchview.TouchImageView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PhotoDocs.newInstance] factory method to
 * create an instance of this fragment.
 */
class PhotoDocs(
    var cloudPictures: ArrayList<String>,
    var narrations: ArrayList<String>,
    var descriptions: ArrayList<String>,
    var fileName: String
) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    var viewRel: CoordinatorLayout? = null
    var photoDocsRV: RecyclerView? = null
    var addBtn: ImageButton? = null
    var play: FloatingActionButton? = null
    var docCommentET: EditText? = null
    var imageView: ImageView? = null
    var addPic: ImageButton? = null
    lateinit var commentsCount: String
    var adt: SelectedItemsAdt? = null
    var flag = String()

    var pictorials = ArrayList<PictorialObject>()


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

        val v: View = inflater.inflate(R.layout.fragment_photo_docs, container, false)



        viewRel = v.findViewById(R.id.photoDocRel)
        addPic = v.findViewById(R.id.addPic)
        touchIV = v.findViewById(R.id.touchIV)
        photoDocsRV = v.findViewById(R.id.photoDocsRV)
        docCommentET = v.findViewById(R.id.docCommentET)
        addBtn = v.findViewById(R.id.addBtn)
        play = v.findViewById(R.id.play)
        imageView = v.findViewById(R.id.imageView)
        photoDocsRV!!.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        pictorials = ArrayList()


        if (cloudPictures.size > 0) {
            for (cloudPic in cloudPictures) {
                try {
                    val picObj = PictorialObject(
                        cloudPic,
                        descriptions[cloudPictures.indexOf(cloudPic)],
                        narrations[cloudPictures.indexOf(cloudPic)]
                    )

                    pictorials.add(picObj)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                val pictorialAdapter =
                    PictorialAdapter(
                        context, pictorials,
                        null, addPic, activity,
                        null, null, "online", touchIV
                    )
                pictorialAdapter.notifyDataSetChanged()
                photoDocsRV!!.adapter = pictorialAdapter
            }
        }else{
            FirebaseFirestore.getInstance().collection("Pictorials").document(fileName)
                .get().addOnSuccessListener { it1 ->
                    val pictures = it1.get("pictures") as java.util.ArrayList<String>
                    val narrations = it1.get("narrations") as java.util.ArrayList<String>
                    val descriptions = it1.get("descriptions") as java.util.ArrayList<String>

                    pictures.forEach {
                        val picObj = PictorialObject(
                            it,
                            descriptions[pictures.indexOf(it)],
                            narrations[pictures.indexOf(it)]
                        )

                        pictorials.add(picObj)
                    }
                    val pictorialAdapter =
                        PictorialAdapter(
                            context, pictorials,
                            null, addPic, activity,
                            null, null, "online", touchIV
                        )
                    pictorialAdapter.notifyDataSetChanged()
                    photoDocsRV!!.adapter = pictorialAdapter
            }
        
        }


        play!!.bringToFront()
        play!!.setOnClickListener { v: View? ->
            val intent = Intent(context, Projector::class.java)
            if (pictorials.size > 0) {
                intent.putExtra("photoDocs", pictorials)
                startActivity(intent)
            }else {
                Toast.makeText(context, "No items to project", Toast.LENGTH_SHORT).show()
            }
        }

        return v
    }



    companion object {
        var touchIV: TouchImageView? = null
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PhotoDocs.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PhotoDocs(cloudPictures = ArrayList(), narrations = ArrayList(), descriptions = ArrayList(), fileName = String()).apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}