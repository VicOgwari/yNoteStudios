package com.midland.ynote.Dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.midland.ynote.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.midland.ynote.Activities.PhotoDoc
import com.midland.ynote.Adapters.SubFieldAdt
import com.midland.ynote.Objects.CommentsObject
import com.midland.ynote.Objects.Schools
import com.midland.ynote.Utilities.DocSorting
import com.midland.ynote.Utilities.FilingSystem
import com.midland.ynote.Utilities.UserPowers
import java.text.SimpleDateFormat
import java.util.*

class EnrollmentDialog(context: Context, private var user: FirebaseUser, private var school: Schools?,
                       private var position: Int, private var flag: String, private var title: String?) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enrollment_dialog)
        val selectedSchool = findViewById<TextView>(R.id.selectedSchool)
        val relevanceTV = findViewById<TextView>(R.id.relevanceTV)
        val relevanceET = findViewById<EditText>(R.id.relevanceET)
        val subFieldsRV = findViewById<RecyclerView>(R.id.subFieldsRV)
        val enrolButton = findViewById<Button>(R.id.enrolButton)
        val updateRelBtn = findViewById<Button>(R.id.updateRelevance)
        val enrolRel = findViewById<RelativeLayout>(R.id.enrollmentRel)
        val relevanceRel = findViewById<RelativeLayout>(R.id.relevanceRel)

        when {
            flag == "enrollment" -> {
                enrolRel.visibility = View.VISIBLE
                relevanceRel.visibility = View.GONE
                if (school != null){
                    selectedSchool.text = school!!.schoolName
                }
                subFieldsRV.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

                val subFields = ArrayList<String>()
                Collections.addAll(subFields, *DocSorting.getSubFields(position))
                val subFieldAdt = SubFieldAdt(context, subFields, null, null)
                subFieldsRV.adapter = subFieldAdt

                enrolButton.setOnClickListener{
                    UserPowers.enrol("Documents", school!!, user, FilingSystem.selectedSubFields, context)
                }
            }
            flag == "nullRelevance" -> {
                relevanceTV.text = "A brief description"
                relevanceTV.visibility = View.VISIBLE
                relevanceET.visibility = View.GONE
                updateRelBtn.text = "Edit"
            }
            flag == "relevance" -> {
                relevanceRel.visibility = View.VISIBLE
                enrolRel.visibility = View.GONE
                val bitmapTitles = PhotoDoc.loadPictorials1(context, "bitmapTitle")
                for (btTitle in bitmapTitles){
                    if (btTitle.title.equals(title)){
                        if (btTitle.relevance == null || btTitle.relevance == ""){
                            relevanceET.visibility = View.VISIBLE
                            relevanceTV.visibility = View.GONE
                            updateRelBtn.text = "Save"
                            relevanceTV.text = btTitle.relevance
                        }else{
                            relevanceET.visibility = View.GONE
                            relevanceTV.visibility = View.VISIBLE
                            updateRelBtn.text = "Edit"
                            relevanceTV.text = btTitle.relevance
                        }

                    }
                }


                updateRelBtn.setOnClickListener{
                    if (relevanceET.visibility == View.VISIBLE){
                        if (relevanceET.text.trim() == ""){
                            Toast.makeText(context, "Describe the content of this pictorial...", Toast.LENGTH_SHORT).show()
                        }else{
                            for (btTitle in bitmapTitles){
                                if (btTitle.title.equals(title)){
                                    btTitle.relevance = relevanceET.text.toString().trim()
                                }
                            }
                            PhotoDoc.savePictorials1(context, "bitmapTitle", bitmapTitles)
                            relevanceTV.visibility = View.VISIBLE
                            relevanceET.visibility = View.GONE
                            relevanceTV.text = relevanceET.text.toString().trim()
                            updateRelBtn.text = "Edit"


                        }
                    }else if (relevanceET.visibility == View.GONE){
                        val str = relevanceTV.text.toString()
                        relevanceET.setText(str)
                        relevanceTV.visibility = View.GONE
                        relevanceET.visibility = View.VISIBLE
                        updateRelBtn.text = "Save"

                    }
                }


            }
            flag.contains("_-_") -> {
                relevanceRel.visibility = View.VISIBLE
                enrolRel.visibility = View.GONE
                relevanceTV.text = "Keep it clean, friendly and most importantly, resourceful"
                updateRelBtn.text = "Post comment"
                updateRelBtn.setOnClickListener {
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user != null){
                        postComment(user, relevanceET, flag.split("_-_")[0], flag.split("_-_")[1])
                    }
                }

            }
        }

    }

    private fun postComment(user: FirebaseUser, docCommentET: EditText, school1: String, title1: String) {
        val documentReference = FirebaseFirestore.getInstance()
            .collection("Content")
            .document("Documents")
            .collection(replacer(school1))
            .document(replacer(title1))
        val commentsRef = documentReference.collection("Comments")
        val comment = docCommentET.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(comment)) {
            Toast.makeText(context, "Say something....", Toast.LENGTH_SHORT).show()
        }
        val callForDate = Calendar.getInstance()
        val currentDate = SimpleDateFormat("dd-MM-yyyy")
        val saveCurrentDate = currentDate.format(callForDate.time)
        val callForTime = Calendar.getInstance()
        val currentTime = SimpleDateFormat("HH:mm")
        val saveCurrentTime = currentTime.format(callForTime.time)
        val uId = user.uid
        val userName = user.displayName
        commentsRef.document(System.currentTimeMillis().toString())
            .set(CommentsObject(userName + "_-_" + uId + "_-_" + comment + "_-_" + saveCurrentDate + "_-_" + saveCurrentTime))
            .addOnSuccessListener {
                val map: MutableMap<String, Any> = HashMap()
                map["commentsCount"] = FieldValue.increment(1)
                documentReference.update(map)
                Toast.makeText(context, "Posted!", Toast.LENGTH_SHORT).show()
                docCommentET.setText("")
                dismiss()
            }
            .addOnFailureListener { e: Exception? ->
                Toast.makeText(
                    context,
                    "Something's interrupting your post.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun replacer(docName: String?): String {
        val docName1 = docName!!.replace("]", "")
        val docName2 = docName1.replace("[", "")
        val docName3 = docName2.replace(".", "")
        val docName4 = docName3.replace("$", "")
        val docName5 = docName4.replace("*", "")
        return docName5.replace("#", "")
    }
}