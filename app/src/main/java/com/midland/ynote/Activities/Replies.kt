package com.midland.ynote.Activities

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.midland.ynote.R
import com.midland.ynote.databinding.ActivityRepliesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.midland.ynote.Adapters.RepliesAdt
import com.midland.ynote.Dialogs.LogInSignUp
import com.midland.ynote.Objects.CommentsObject
import com.midland.ynote.Objects.RepliesObj
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class Replies : AppCompatActivity() {
    private var refStr = ""
    private var binding: ActivityRepliesBinding? = null
    private var commentsObject: CommentsObject? = null
    private var justASec: TextView? = null
    private var repliesRV: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRepliesBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        val toolbar = binding!!.toolbar
        setSupportActionBar(toolbar)
        toolbar.title = "Replies"
        justASec = binding!!.justASec
        val commentCard = binding!!.commentCard
        val addReply = binding!!.addReply
        val dateTime = binding!!.commentTimeDate
        repliesRV = binding!!.repliesRV
        val commenter = binding!!.commenter
        val cid1 = binding!!.cid1
        val replies1 = binding!!.replies1
        val upVote1 = binding!!.upVote1
        val downVote1 = binding!!.downVote1
        val userNameComment: TextView = binding!!.commentUserName
        val imCommentCard = binding!!.imageCommentCard
        val imDateTime = binding!!.imCommentTimeDate
        val imCommenter = binding!!.imCommenter
        val cid2 = binding!!.cid2
        val replies2 = binding!!.replies2
        val upVote2 = binding!!.upVote2
        val downVote2 = binding!!.downVote2
        val imageComment = binding!!.imageComment
        val imageCommentVP = binding!!.imageCommentVP
        val imUserNameComment: TextView = binding!!.imCommentUserName

        repliesRV!!.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)

        if (intent.getStringExtra("cId") != null) {
            refStr = intent.getStringExtra("refStr")!!
            commentsObject = intent.getParcelableExtra("comment") as CommentsObject?
            if (commentsObject!!.imCommentUrls == null) {
                commentCard.visibility = View.VISIBLE
                imCommentCard.visibility = View.GONE
                dateTime.text = commentsObject!!.comment.split("_-_")
                    .toTypedArray()[3] + " | " + commentsObject!!.comment.split("_-_")
                    .toTypedArray()[4]
                commenter.text = commentsObject!!.comment.split("_-_").toTypedArray()[0]
                cid1.text = commentsObject!!.cId
                replies1.text = commentsObject!!.repliesCount + " Replies"
                upVote1.text = commentsObject!!.upVotes + " Ups"
                downVote1.text = commentsObject!!.downVotes + " Downs"
                userNameComment.text = commentsObject!!.comment.split("_-_").toTypedArray()[2]
            } else {
                commentCard.visibility = View.GONE
                imCommentCard.visibility = View.VISIBLE
                imDateTime.text = commentsObject!!.comment.split("_-_")
                    .toTypedArray()[3] + " | " + commentsObject!!.comment.split("_-_")
                    .toTypedArray()[4]
                imCommenter.text = commentsObject!!.comment.split("_-_").toTypedArray()[0]
                cid2.text = commentsObject!!.cId
                replies2.text = commentsObject!!.repliesCount + " Replies"
                upVote2.text = commentsObject!!.upVotes + " Ups"
                downVote2.text = commentsObject!!.downVotes + " Downs"
                imUserNameComment.text = commentsObject!!.comment.split("_-_").toTypedArray()[2]
                if (commentsObject!!.imCommentUrls.size == 1) {
                    imageCommentVP.visibility = View.GONE
                    imageComment.visibility = View.VISIBLE
                    Picasso.get().load(commentsObject!!.imCommentUrls[0])
                        .placeholder(R.drawable.ic_hourglass_bottom_white)
                        .into(imageComment)
                } else {
                    imageCommentVP.visibility = View.VISIBLE
                    imageComment.visibility = View.GONE
                }
            }
        }

        addReply.setOnClickListener{
            repliesDialog()
        }

        replies()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val extras = intent?.extras
        if (extras != null){

        }
    }
    private fun repliesDialog() {
        val linearLayout = LinearLayout(this@Replies)
        linearLayout.orientation = LinearLayout.VERTICAL
        val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        lp.setMargins(50, 0, 50, 100)
        val input = EditText(this@Replies)
        input.layoutParams = lp
        input.gravity = Gravity.TOP or Gravity.START
        linearLayout.addView(input, lp)
        val addReply = AlertDialog.Builder(this@Replies)
        addReply.setTitle("Add a reply")
        addReply.setMessage("Be objective, kind and respectful..")
        addReply.setView(linearLayout)
        addReply.setNegativeButton("dismiss") { dialog, which -> dialog.dismiss() }
        addReply.setPositiveButton("reply") { dialog, which ->
            if (input.text.toString().trim { it <= ' ' } == "") {
                Toast.makeText(applicationContext, "Blank reply", Toast.LENGTH_SHORT).show()
            } else {
                val user = FirebaseAuth.getInstance().currentUser
                if (user == null) {
                    val logInSignUp =
                        LogInSignUp(this@Replies)
                    logInSignUp.show()
                } else {
                    val callForDate = Calendar.getInstance()
                    val currentDate = SimpleDateFormat("dd-MM-yyyy")
                    val saveCurrentDate = currentDate.format(callForDate.time)
                    val callForTime = Calendar.getInstance()
                    val currentTime = SimpleDateFormat("HH:mm")
                    val saveCurrentTime = currentTime.format(callForTime.time)

                    val time = "$saveCurrentTime | $saveCurrentDate"
                    val reply: String = input.text.toString().trim { it <= ' ' }
                    val userName = user.displayName
                    val uid = user.uid

                    val replyMap: MutableMap<String, Any> = HashMap()
                    val updateComment: MutableMap<String, Any> = HashMap()
                    updateComment["repliesCount"] = FieldValue.increment(1)
                    replyMap["time"] = time
                    replyMap["reply"] = reply
                    replyMap["votes"] = 0
                    replyMap["voters"] = ArrayList<String>()
                    replyMap["userName"] = userName!!
                    replyMap["uid"] = uid

                    val reference: DocumentReference

                    if (refStr.startsWith("Pictorials")){
                        reference = FirebaseFirestore.getInstance()
                            .collection(refStr.split("_-_")[0])
                            .document(refStr.split("_-_")[1])
                            .collection(refStr.split("_-_")[2])
                            .document(refStr.split("_-_")[3])
                    }else
                    if(refStr.endsWith("Lectures")){
                        reference = FirebaseFirestore.getInstance().collection("Content")
                            .document("Lectures")
                            .collection(refStr.split("_-_")[0])
                            .document(refStr.split("_-_")[1])
                            .collection(refStr.split("_-_")[2])
                            .document(refStr.split("_-_")[3])
                    }else
                        {
                        reference = FirebaseFirestore.getInstance().collection("Content")
                                .document("Documents")
                                .collection(refStr.split("_-_")[0])
                                .document(refStr.split("_-_")[1])
                                .collection(refStr.split("_-_")[2])
                                .document(refStr.split("_-_")[3])

                    }


                    reference
                        .collection("Replies")
                        .document()
                        .set(replyMap)
                        .addOnSuccessListener {
                            reference.update(updateComment)
                            dialog.dismiss()
                            Toast.makeText(applicationContext, "Reply added..", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
        addReply.show()
    }


    private fun replies() {
        val replies = ArrayList<RepliesObj>()

        val reference: DocumentReference = if (refStr.startsWith("Pictorials")){
            FirebaseFirestore.getInstance()
                .collection(refStr.split("_-_")[0])
                .document(refStr.split("_-_")[1])
                .collection(refStr.split("_-_")[2])
                .document(refStr.split("_-_")[3])
        }else{

            FirebaseFirestore.getInstance().collection("Content")
                .document("Documents")
                .collection(refStr.split("_-_")[0])
                .document(refStr.split("_-_")[1])
                .collection(refStr.split("_-_")[2])
                .document(refStr.split("_-_")[3])
        }


        reference.collection("Replies").get().addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
            justASec!!.visibility = View.GONE
            for (qds in queryDocumentSnapshots) {
                var uid: String? = null
                var userName: String? = null
                var votes: String? = null
                var reply: String? = null
                var ref: DocumentReference? = null
                var voters: ArrayList<String>? = null
                try {
                    uid = qds.getString("uid")
                    reply = qds.getString("reply")
                    votes = qds.get("votes").toString()
                    userName = qds.getString("userName")
                    voters = qds.get("voters") as ArrayList<String>
                    ref = qds.reference

                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }

                replies.add(
                    RepliesObj(
                        reply,
                        userName,
                        uid,
                        votes,
                        voters,
                        ref
                    )
                )

            }
            replies.reverse()
            val repliesAdt =
                RepliesAdt(this@Replies, replies)
            repliesAdt.notifyDataSetChanged()
            repliesRV!!.adapter = repliesAdt
        }.addOnFailureListener { e: Exception? -> }
    }

}