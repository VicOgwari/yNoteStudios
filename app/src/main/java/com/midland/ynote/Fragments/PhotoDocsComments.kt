package com.midland.ynote.Fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.midland.ynote.Adapters.CommentsAdapter
import com.midland.ynote.Adapters.MoreIdeasAdt
import com.midland.ynote.Adapters.SelectedItemsAdt
import com.midland.ynote.Dialogs.LogInSignUp
import com.midland.ynote.Objects.CommentsObject
import com.midland.ynote.R
import com.midland.ynote.Utilities.DocSorting
import com.ortiz.touchview.TouchImageView
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PhotoDocsComments.newInstance] factory method to
 * create an instance of this fragment.
 */
class PhotoDocsComments(var con: Context, var fileName: String, var publisherId: String) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var PICK_IMAGE_INTENT = 99
    private var PICK_DOC_INTENT = 98
    private val REQUEST_PERMISSION_CODE = 1000
    private var comments: ArrayList<CommentsObject>? = null
    private var docCommentRV: RecyclerView? = null
    private var imageCommentRV: RecyclerView? = null
    private lateinit var bottomSheet1: View
    private lateinit var discardImage: ImageButton
    var addBtn: ImageButton? = null
    var outSource: ImageButton? = null
    var images = ArrayList<String>()
    var photosRV: RecyclerView? = null
    private var mediaUriList: ArrayList<String>? = null
    var adt: SelectedItemsAdt? = null
    private val REQUEST_TAKE_PHOTO = 1
    private var bottomSheetBehavior1: BottomSheetBehavior<*>? = null
    lateinit var justASec : TextView
    var flag = String()






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
        val v: View = inflater.inflate(R.layout.fragment_phot_docs_comments, container, false)
        val addComment = v.findViewById<Button>(R.id.addCommentBtn)
        imageRel = v.findViewById(R.id.imageRel)
        val addCommentFab = v.findViewById<FloatingActionButton>(R.id.addComment)
        val addDocFab = v.findViewById<FloatingActionButton>(R.id.addDocument)
        val docCommentET = v.findViewById<EditText>(R.id.docCommentET)
        val rate = v.findViewById<ImageButton>(R.id.rate)
        val submit = v.findViewById<Button>(R.id.submitRate)
        ratingRel = v.findViewById(R.id.ratingRel)
        val ratingBar = v.findViewById<RatingBar>(R.id.ratingBar)
        val addPhotoFab = v.findViewById<FloatingActionButton>(R.id.addPhoto)
        lin = v.findViewById(R.id.lin)
        justASec = v.findViewById(R.id.justASec)
        imageCommentRV = v.findViewById(R.id.imageComment)
        bottomSheet1 = v.findViewById(R.id.ideas_sheet)
        addBtn = v.findViewById(R.id.addBtn)
        discardImage = v.findViewById(R.id.discardImage)
        outSource = v.findViewById(R.id.outSource)
        bottomSheet1.bringToFront()

        imageCommentRV!!.layoutManager = LinearLayoutManager(
            con,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        addBtn!!.setOnClickListener {
            flag = "Images"
            adt = SelectedItemsAdt(
                con,
                MoreIdeasAdt.images1,
                flag
            )
            adt!!.notifyDataSetChanged()
            imageCommentRV!!.adapter = adt

            lin.visibility = View.VISIBLE
            imageRel.visibility = View.VISIBLE

            bottomSheetBehavior1!!.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        outSource!!.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, PICK_IMAGE_INTENT)
        }

        discardImage.setOnClickListener {
            if (imageRel.visibility == View.VISIBLE) {
                imageRel.visibility = View.GONE
            }
        }

        photosRV = v.findViewById(R.id.photosRV)
        val grid = MoreIdeasAdt(
            addBtn!!,
            DocSorting.getFileObjects(context),
            "moreIdeas",
            con,
            images
        )
        photosRV!!.layoutManager = GridLayoutManager(context, 3)
        grid.notifyDataSetChanged()
        photosRV!!.adapter = grid
        photosRV!!.bringToFront()

        docCommentRV = v.findViewById(R.id.docCommentsRV)
        docCommentRV!!.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        bottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet1)

        rate.setOnClickListener{
            if (ratingRel!!.visibility == View.VISIBLE){
                ratingRel!!.visibility = View.GONE
            }else
                if (ratingRel!!.visibility == View.GONE){
                    ratingRel!!.visibility = View.VISIBLE
                }
        }

        submit.setOnClickListener{
            val user = FirebaseAuth.getInstance().currentUser
            if (user == null){
                val logInSignUp = LogInSignUp(con)
                logInSignUp.show()
            }else{
                val bitmapsDatabaseRef = FirebaseFirestore.getInstance()
                    .collection("Pictorials").document(fileName)

                val raters = bitmapsDatabaseRef.collection("RatersUid")
                    .document()

                bitmapsDatabaseRef.collection("RatersUid")
                    .whereArrayContains("uIds", user.uid)
                    .get()
                    .addOnSuccessListener { it ->
                        if (it.isEmpty){
                            val ratings: MutableMap<String, Any> = HashMap()
                            ratings["ratersCount"] = FieldValue.increment(1)
                            ratings["ratings"] = FieldValue.increment(ratingBar!!.rating.toDouble())
                            bitmapsDatabaseRef.update(ratings).addOnSuccessListener {
                                val newId: MutableMap<String, Any> = HashMap()
                                newId["uIds"] = FieldValue.arrayUnion(user.uid)
                                newId["uIdsCount"] = FieldValue.increment(1);

                                bitmapsDatabaseRef.collection("RatersUid")
                                    .whereLessThan("uIdsCount", 50000)
                                    .get()
                                    .addOnSuccessListener {
                                        if (it.isEmpty){
                                            raters.set(newId)
                                        }else{
                                            raters.update(newId)
                                        }
                                    }
                                val s = ratingBar.rating.toString()
                                Toast.makeText(con, "$s stars", Toast.LENGTH_SHORT).show()
                                SystemClock.sleep(1300)
                                ratingRel!!.visibility = View.GONE
                            }.addOnFailureListener { e: Exception? ->
                                Toast.makeText(
                                    con,
                                    "Something's up! Try again.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }else{
                            Toast.makeText(con, "Can't rate twice!", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        addComment!!.setOnClickListener { v: View? ->
            if (docCommentET!!.text.toString().isEmpty()) {
                Toast.makeText(context, "Say something...", Toast.LENGTH_SHORT).show()
            } else {
                val pictorialsRef = FirebaseFirestore.getInstance().collection("Pictorials").document(fileName)

                if (mediaUriList != null) {
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user == null) {
                        val logInSignUp =
                            LogInSignUp(con)
                        logInSignUp.show()
                    } else {
                        postComment(user, docCommentET, mediaUriList, pictorialsRef)
                    }
                } else {
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user == null) {
                        val logInSignUp =
                            LogInSignUp(con)
                        logInSignUp.show()
                    } else {
                        postComment(user, docCommentET, null, pictorialsRef)
                    }
                }

            }
        }
        addDocFab.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user == null) {
                val logInSignUp = LogInSignUp(con)
                logInSignUp.show()
            } else {
                val intent1 = Intent()
                intent1.type = "document/*"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    intent1.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                }
                intent1.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(intent1, PICK_DOC_INTENT)

            }
        }
        addPhotoFab.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user == null) {
                val logInSignUp = LogInSignUp(con)
                logInSignUp.show()
            } else {
                if (bottomSheetBehavior1!!.state == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior1!!.state = BottomSheetBehavior.STATE_EXPANDED
                    images = ArrayList()
                    //Retrieve comments from db & place them on RV
                }
            }
        }
        addCommentFab.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user == null) {
                val logInSignUp = LogInSignUp(con)
                logInSignUp.show()
            } else {
                if (lin.visibility == View.GONE) {
                    lin.visibility = View.VISIBLE

                } else {
                    lin.visibility = View.GONE

                }

            }
        }

        comments1

        return v
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_TAKE_PHOTO -> {
                if (resultCode == Activity.RESULT_OK && data != null) {

                    if (requestCode == PICK_IMAGE_INTENT) {
                        mediaUriList = ArrayList()

                        if (
                            data.clipData == null) {
                            mediaUriList!!.add(data.data.toString())
                        } else {
                            for (i in 0 until data.clipData!!.itemCount) {
                                mediaUriList!!.add(data.clipData!!.getItemAt(i).uri.toString())
                            }
                        }
                        flag = "Images"
                        adt = SelectedItemsAdt(
                            con,
                            mediaUriList,
                            flag
                        )
                        adt!!.notifyDataSetChanged()
                        imageCommentRV!!.adapter = adt

                        if (lin.visibility == View.GONE) {
                            lin.visibility = View.VISIBLE
                            imageRel.visibility = View.VISIBLE
                        }
                    }
                    else
                        if (requestCode == PICK_DOC_INTENT) {
                            mediaUriList = ArrayList()

                            if (data.clipData == null) {
                                mediaUriList!!.add(data.data.toString())
                            } else {
                                for (i in 0 until data.clipData!!.itemCount) {
                                    mediaUriList!!.add(data.clipData!!.getItemAt(i).uri.toString())
                                }
                            }
                            flag = "Docs"
                            adt = SelectedItemsAdt(
                                con,
                                mediaUriList,
                                flag
                            )
                            adt!!.notifyDataSetChanged()
                            imageCommentRV!!.adapter = adt

                            if (lin.visibility == View.GONE) {
                                lin.visibility = View.VISIBLE
                                imageRel.visibility = View.VISIBLE
                            }
                        }

                }

            }
            PICK_IMAGE_INTENT -> {
                mediaUriList = ArrayList()

                if (
                    data!!.clipData == null) {
                    mediaUriList!!.add(data.data.toString())
                } else {
                    for (i in 0 until data.clipData!!.itemCount) {
                        mediaUriList!!.add(data.clipData!!.getItemAt(i).uri.toString())
                    }
                }
                flag = "Images"
                adt = SelectedItemsAdt(
                    con,
                    mediaUriList,
                    flag
                )
                adt!!.notifyDataSetChanged()
                imageCommentRV!!.adapter = adt

                lin.visibility = View.VISIBLE
                imageRel.visibility = View.VISIBLE
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(con, "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(con, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PhotDocsComments.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String, con: Context) =
            PhotoDocsComments(con, fileName = String(), publisherId = String()).apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        var touchIV: TouchImageView? = null
        var ratingRel: RelativeLayout? = null
        lateinit var imageRel: RelativeLayout
        lateinit var lin: LinearLayout

    }

    private val comments1: Unit
        get() {

            val commentsRef = FirebaseFirestore.getInstance().collection("Pictorials").document(fileName).collection("Comments")
            comments = ArrayList()

            commentsRef.get().addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
                justASec.visibility = View.GONE
                for (qds in queryDocumentSnapshots) {
                    var docId: String?
                    var upVotes: String?
                    var downVotes: String?
                    var replies: String?
                    var comment: String?
                    var imUrls: java.util.ArrayList<String>?
                    var uids: ArrayList<String>?
                    var ref: String?

                    try {
                        comment = qds.getString("comment")
                        replies = qds.get("repliesCount").toString()
                        upVotes = qds.get("upVotes").toString()
                        downVotes = qds.get("downVotes").toString()
                        uids = qds.get("uiDs") as ArrayList<String>?
                        imUrls = qds.get("imCommentUrls") as java.util.ArrayList<String>
                        docId = qds.getString("cId")
                        ref = qds.get("ref").toString()

                        val reference = FirebaseFirestore.getInstance().collection("Pictorials")
                            .document(ref.split("_-_")[1])
                            .collection(ref.split("_-_")[2])
                            .document(ref.split("_-_")[3])

                        comments!!.add(
                            CommentsObject(
                                comment,
                                imUrls,
                                docId,
                                upVotes,
                                downVotes,
                                replies,
                                uids, reference, ref
                            )
                        )

                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    }

                }
                comments!!.reverse()
                val commentsAdapter = CommentsAdapter(con, comments!!, null)
                commentsAdapter.notifyDataSetChanged()
                docCommentRV!!.adapter = commentsAdapter
                docCommentRV!!.bringToFront()
            }.addOnFailureListener { e: Exception? -> }
        }



    private fun postComment(
        user: FirebaseUser,
        docCommentET: EditText,
        mediaUriList: ArrayList<String>?,
        pictorialsRef: DocumentReference
    ) {

        val mProgressDialog = ProgressDialog(con)
        mProgressDialog.setMessage("Adding your comment")
        mProgressDialog.setTitle("Please Wait...")
        mProgressDialog.isIndeterminate = true
        mProgressDialog.show()


        val commentsRef = pictorialsRef.collection("Comments")
        val comment = docCommentET.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(comment)) {
            Toast.makeText(context, "Say something....", Toast.LENGTH_SHORT).show()
        } else {
            val cid = System.currentTimeMillis().toString()
            val refStr = "Pictorials" + "_-_" + fileName + "_-_Comments_-_" + cid

            val callForDate = Calendar.getInstance()
            val currentDate = SimpleDateFormat("dd-MM-yyyy")
            val saveCurrentDate = currentDate.format(callForDate.time)
            val callForTime = Calendar.getInstance()
            val currentTime = SimpleDateFormat("HH:mm")
            val saveCurrentTime = currentTime.format(callForTime.time)
            val uId = user.uid
            val userName = user.displayName

            if (mediaUriList != null){
                docCommentET.setText("")
            }

            if (mediaUriList == null) {
                val commentMap1: MutableMap<String, Any> = HashMap()
                commentMap1["comment"] = userName + "_-_" + uId + "_-_" + comment + "_-_" + saveCurrentDate + "_-_" + saveCurrentTime + "_-_" + publisherId + "_-_" + fileName
                commentMap1["repliesCount"] = "0"
                commentMap1["upVotes"] = "0"
                commentMap1["downVotes"] = "0"
                commentMap1["uiDs"] = ArrayList<String>()
                commentMap1["imCommentUrls"] = ArrayList<String>()
                commentMap1["cId"] = cid
                commentMap1["ref"] = refStr

                val commentObj0 = CommentsObject(
                    userName + "_-_" + uId + "_-_" + comment + "_-_"
                            + saveCurrentDate + "_-_" + saveCurrentTime,
                    ArrayList<String>(), cid,
                    "0", "0", "0", null, null, refStr
                )

                comments!!.add(commentObj0)
                docCommentET.setText("")
                val commentsAdapter = CommentsAdapter(con, comments!!, null)
                commentsAdapter.notifyDataSetChanged()
                docCommentRV!!.adapter = commentsAdapter
                docCommentRV!!.bringToFront()

                commentsRef.document(cid)
                    .set(commentMap1)
                    .addOnSuccessListener { aVoid: Void? ->
                        val map: MutableMap<String, Any> = HashMap()
                        map["commentsCount"] = FieldValue.increment(1)
                        pictorialsRef.update(map)
                        mProgressDialog.dismiss()
                        val commentsAdapter1 = CommentsAdapter(con, comments!!, null)
                        commentsAdapter1.notifyDataSetChanged()
                        docCommentRV!!.adapter = commentsAdapter1
                        docCommentRV!!.bringToFront()
                        Toast.makeText(context, "Comment posted!", Toast.LENGTH_SHORT)
                            .show()
//                        FirebaseMessaging.getInstance().subscribeToTopic("VictorFocus")
//                            .addOnCompleteListener {
//                                if (it.isSuccessful) {
//                                    Toast.makeText(context, "Subscribed!", Toast.LENGTH_SHORT).show()
//                                }else{
//                                    Toast.makeText(context, "Subscription failed...", Toast.LENGTH_SHORT).show()
//                                }
//                            }
                    }
                    .addOnFailureListener { e: Exception? ->
                        mProgressDialog.dismiss()
                        Toast.makeText(
                            context,
                            "Something's interrupting your post.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                var uploadTask: StorageTask<UploadTask.TaskSnapshot>? = null
                val uploadedList = ArrayList<String>()
                val uploadPos = ArrayList<Int>()

                for (s in mediaUriList) {
                    val fileReference1 = FirebaseStorage.getInstance().getReference("ImageComments")
                    uploadTask = fileReference1.putFile(Uri.parse(s))
                        .addOnSuccessListener {
                            uploadTask!!.continueWithTask { task ->
                                if (!task.isSuccessful) {
                                    throw task.exception!!
                                }
                                fileReference1.downloadUrl
                            }.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    //String userID = FirebaseAuth.getInstance().getUid();
                                    val downloadLink = task.result.toString()
                                    uploadedList.add(downloadLink)
                                    uploadPos.add(mediaUriList.indexOf(s))
                                    mediaUriList.remove(s)
                                    adt!!.notifyItemRemoved(mediaUriList.indexOf(s))
                                    Toast.makeText(context, "Uploaded!", Toast.LENGTH_SHORT).show()

                                    if (mediaUriList.size == 0){
                                        val commentMap1: MutableMap<String, Any> = HashMap()
                                        commentMap1["comment"] = userName + "_-_" + uId + "_-_" + comment + "_-_" + saveCurrentDate + "_-_" + saveCurrentTime
                                        commentMap1["repliesCount"] = "0"
                                        commentMap1["upVotes"] = "0"
                                        commentMap1["downVotes"] = "0"
                                        commentMap1["uiDs"] = ArrayList<String>()
                                        commentMap1["imCommentUrls"] = uploadedList
                                        commentMap1["cId"] = cid
                                        commentMap1["ref"] = refStr

                                        val commentObj0 =
                                            CommentsObject(
                                                userName + "_-_" + uId + "_-_" + comment + "_-_"
                                                        + saveCurrentDate + "_-_" + saveCurrentTime,
                                                uploadedList, cid,
                                                "0", "0", "0", null, null, refStr
                                            )

                                        comments!!.add(commentObj0)
                                        val commentsAdapter = CommentsAdapter(con, comments!!, null)
                                        commentsAdapter.notifyDataSetChanged()
                                        docCommentRV!!.adapter = commentsAdapter
                                        docCommentRV!!.bringToFront()


                                        commentsRef.document(cid)
                                            .set(commentMap1)
                                            .addOnSuccessListener {
                                                val map: MutableMap<String, Any> = HashMap()
                                                map["commentsCount"] = FieldValue.increment(1)
                                                pictorialsRef.update(map)
                                                mProgressDialog.dismiss()
                                                val commentsAdapter1 = CommentsAdapter(con, comments!!, null)
                                                commentsAdapter1.notifyDataSetChanged()
                                                docCommentRV!!.adapter = commentsAdapter1
                                                docCommentRV!!.bringToFront()
                                                Toast.makeText(context, "Comment posted!", Toast.LENGTH_SHORT)
                                                    .show()
                                                docCommentET.setText("")

//                                                FirebaseMessaging.getInstance().subscribeToTopic(fileName)
//                                                    .addOnCompleteListener {
//                                                        if (it.isSuccessful) {
//                                                            Toast.makeText(context, "Subscribed!", Toast.LENGTH_SHORT).show()
//                                                        }else{
//                                                            Toast.makeText(context, "Subscription failed...", Toast.LENGTH_SHORT).show()
//                                                        }
//                                                    }
                                            }

                                    }
                                }
                            }
                        }
                        .addOnFailureListener { e ->

                        }
                        .addOnProgressListener { taskSnapshot ->

                        }
                }
            }
        }

    }
}