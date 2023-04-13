package com.midland.ynote.Adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.CountDownTimer
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.borjabravo.readmoretextview.ReadMoreTextView
import com.midland.ynote.Activities.Replies
import com.midland.ynote.Activities.UserProfile2
import com.midland.ynote.Adapters.CommentsAdapter.PointsVH
import com.midland.ynote.Dialogs.LogInSignUp
import com.midland.ynote.MainActivity
import com.midland.ynote.Objects.CommentsObject
import com.midland.ynote.R
import com.midland.ynote.Utilities.FilingSystem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.ortiz.touchview.TouchImageView
import com.squareup.picasso.Picasso

class CommentsAdapter(private val c: Context, private val commentsObjects: ArrayList<CommentsObject>, private val touchIV: TouchImageView?) : RecyclerView.Adapter<PointsVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointsVH {
        val v = LayoutInflater.from(c).inflate(R.layout.comment_item, parent, false)
        return PointsVH(v)
    }

    override fun onBindViewHolder(holder: PointsVH, position: Int) {
        val user = FirebaseAuth.getInstance().currentUser
        val commentsObject = commentsObjects[holder.absoluteAdapterPosition]

        holder.wholecomment.text = commentsObject.comment
        if (commentsObject.imCommentUrls != null){
            if (commentsObject.imCommentUrls.size == 0){
                commentsObject.imCommentUrls = null
            }
        }

        when {
            commentsObject.imCommentUrls != null -> {
                holder.imageCommentCard.visibility = View.VISIBLE
                holder.commentCard.visibility = View.GONE
                holder.docCommentCard.visibility = View.GONE

                if (user != null){
                    if (commentsObject.uiDs != null){
                        if (commentsObject.uiDs.contains(user.uid + "_-_" + "Up")){
                            holder.upVote2.setTextColor(Color.MAGENTA)
                            holder.upVote2.text = commentsObject.upVotes + " Ups"
                        }
                        if (commentsObject.uiDs.contains(user.uid + "_-_" + "Down")){
                            holder.downVote2.setTextColor(Color.MAGENTA)
                            holder.upVote2.text = commentsObject.downVotes + " Downs"
                        }

                    }

                }

                when (commentsObject.imCommentUrls.size) {
                    1 -> {
                        Picasso.get().load(commentsObject.imCommentUrls[0])
                            .placeholder(R.drawable.ic_hourglass_bottom_white)
                            .into(holder.imageComment)

                        holder.imageComment.setOnClickListener {
                            if (touchIV != null) {
                                MainActivity.touchIV!!.visibility = View.VISIBLE
                                MainActivity.touchIV!!.bringToFront()
                                MainActivity.touchIV!!.setImageResource(R.drawable.ic_hourglass)
                                FilingSystem.downloadImage(
                                    touchIV,
                                    commentsObject.imCommentUrls[0]
                                )
                            }
                        }
                    }
                    else -> {
                        val slideHandler: Handler?
                        val slideRunnable =
                            Runnable { holder.imageCommentVP.setCurrentItem(holder.imageCommentVP.currentItem + 1, true) }
                        holder.imageComment.visibility = View.GONE
                        holder.imageCommentVP.visibility = View.VISIBLE

                        val imCommentsAdt = HomeSliderAdt(c, null,null, commentsObject.imCommentUrls)
                        imCommentsAdt.notifyDataSetChanged()
                        holder.imageCommentVP.adapter = imCommentsAdt

                        slideHandler = Handler()
                        holder.imageCommentVP.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                            override fun onPageSelected(position: Int) {
                                super.onPageSelected(position)
                                slideHandler.removeCallbacks(slideRunnable)
                                slideHandler.postDelayed(slideRunnable, (3500 * 2).toLong())
                            }
                        })

                    }
                }

                holder.dateTimeIm.text = ((commentsObject.comment.split("_-_")[3] + " | " + commentsObject.comment.split("_-_")[4]))
                holder.userNameCommentIm.text = (commentsObject.comment.split("_-_")[2])
                holder.commenterIm.text = (commentsObject.comment.split("_-_")[0] )
                holder.cid2.text = commentsObject.cId
                holder.replies2.text = commentsObject.repliesCount + " Replies"
                holder.replies2.setOnClickListener {
                    val intent = Intent(c, Replies::class.java)
                    intent.putExtra("cId", holder.cid2.text.toString())
                    intent.putExtra("refStr", commentsObject.refStr)
                    intent.putExtra("comment", commentsObject)
                    c.startActivity(intent)
                }
                holder.commenterIm.setOnClickListener {
                    val intent = Intent(c, UserProfile2::class.java)
                    intent.putExtra("userID", holder.wholecomment.text.toString().split("_-_")[1])
                    c.startActivity(intent)
                }

            }
            commentsObject.docCommentNames != null -> {
                holder.commentCard.visibility = View.GONE
                holder.imageCommentCard.visibility = View.GONE
                holder.docCommentCard.visibility = View.VISIBLE



                holder.dateTimeDoc.text = ((commentsObject.comment.split("_-_")[3] + "|"
                        + commentsObject.comment.split("_-_")[4]))
                holder.userNameCommentDoc.text = (commentsObject.comment.split("_-_")[2])
                holder.commenterDoc.text = (commentsObject.comment.split("_-_")[0] )

//                holder.docName.text = commentsObject.docCommentNames[commentsObject.uploadPosition
//                        [holder.absoluteAdapterPosition]]
//
//                if (holder.docName.text.endsWith("pdf") || holder.docName.text.endsWith("PDF")) {
//                    Glide.with(c).load(R.drawable.pdf).fitCenter().into(holder.docEmblem)
//                } else if (holder.docName.text.endsWith("doc") || holder.docName.text.endsWith("DOC")
//                    || holder.docName.text.endsWith("docx") || holder.docName.text.endsWith("DOCX")
//                ) {
//                    Glide.with(c).load(R.drawable.microsoft_word).fitCenter()
//                        .into(holder.docEmblem)
//                } else if (holder.docName.text.endsWith("ppt") || holder.docName.text.endsWith("PPT")
//                    || holder.docName.text.endsWith("pptx") || holder.docName.text.endsWith("PPTX")
//                ) {
//                    Glide.with(c).load(R.drawable.powerpoint).fitCenter().into(holder.docEmblem)
//                }

            }
            else-> {
                holder.commentCard.visibility = View.VISIBLE
                holder.imageCommentCard.visibility = View.GONE
                holder.docCommentCard.visibility = View.GONE

                if (commentsObject.uiDs != null){

                    if (user != null){
                        if (commentsObject.uiDs.contains(user.uid + "_-_" + "Up")){
                            holder.upVote1.setTextColor(Color.MAGENTA)
                            holder.upVote1.text = commentsObject.upVotes + " Ups"
                        }
                        if (commentsObject.uiDs.contains(user.uid + "_-_" + "Down")){
                            holder.downVote1.setTextColor(Color.MAGENTA)
                            holder.downVote1.text = commentsObject.downVotes + " Downs"
                        }

                    }

                }

                holder.dateTime.text = ((commentsObject.comment.split("_-_")[3] + "|"
                        + commentsObject.comment.split("_-_")[4]))
                holder.userNameComment.text = (commentsObject.comment.split("_-_")[2])
                holder.commenter.text = (commentsObject.comment.split("_-_")[0] )

                holder.cid1.text = commentsObject.cId
                holder.replies1.text = commentsObject.repliesCount + " Replies"
                holder.replies1.setOnClickListener {
                    val updateComment: MutableMap<String, Any> = HashMap()
                    updateComment["repliesCount"] = FieldValue.increment(1)

                    val intent = Intent(c, Replies::class.java)
                    intent.putExtra("cId", holder.cid1.text.toString())
                    intent.putExtra("refStr", commentsObject.refStr)
                    intent.putExtra("comment", commentsObject)
                    c.startActivity(intent)
                }
                holder.commenter.setOnClickListener {
                    val intent = Intent(c, UserProfile2::class.java)
                    intent.putExtra("userID", holder.wholecomment.text.toString().split("_-_")[1])
                    c.startActivity(intent)
                }

            }
        }
        holder.upVote2.setOnClickListener {
            val newVotes: String = (Integer.parseInt(commentsObject.upVotes) + 1).toString()
            holder.upVote2.text = "$newVotes Ups"
            if (commentsObject.uiDs != null){

                if (user != null){
                    if (commentsObject.uiDs.contains(user.uid + "_-_" + "Up") || commentsObject.uiDs.contains(
                            user.uid + "_-_" + "Down")){
                        val count: CountDownTimer = object : CountDownTimer(1000, 1000) {
                            override fun onTick(millisUntilFinished: Long) {}
                            override fun onFinish() {
                                holder.upVote2.text = commentsObject.upVotes + " Ups"
                            }
                        }
                        count.start()
                    }
                    else{
                        commentsObject.uiDs.add(user.uid + "_-_" + "Up")
                        val commentUpdate = HashMap<String, Any>()
                        commentUpdate["upVotes"] = FieldValue.increment(1)
                        commentUpdate["uiDs"] = FieldValue.arrayUnion(user.uid + "_-_" + "Up")
                        commentsObject.ref.update(commentUpdate)
                            .addOnSuccessListener {
                                holder.upVote2.text = "$newVotes Ups"
                                holder.upVote2.setTextColor(Color.MAGENTA)
                            }
                    }

                }else{
                    val count: CountDownTimer = object : CountDownTimer(1000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {}
                        override fun onFinish() {
                            holder.upVote2.text = commentsObject.upVotes + " Ups"
                            val login = LogInSignUp(c)
                            login.show()
                        }
                    }
                    count.start()
                }
            }
        }

        holder.downVote2.setOnClickListener {
            val newVotes: String = (Integer.parseInt(commentsObject.downVotes ) + 1).toString()
            holder.downVote2.text = "$newVotes Downs"
            if (user != null){
                if (commentsObject.uiDs != null){

                    if (commentsObject.uiDs.contains(user.uid + "_-_" + "Up") || commentsObject.uiDs.contains(user.uid + "_-_" + "Down")){
                        val count: CountDownTimer = object : CountDownTimer(1000, 1000) {
                            override fun onTick(millisUntilFinished: Long) {}
                            override fun onFinish() {
                                holder.downVote2.text = commentsObject.downVotes + " Downs"
                            }
                        }
                        count.start()
                    }else{
                        commentsObject.uiDs.add(user.uid + "_-_" + "Down")
                        val commentUpdate = HashMap<String, Any>()
                        commentUpdate["downVotes"] = FieldValue.increment(1)
                        commentUpdate["uiDs"] = FieldValue.arrayUnion(user.uid + "_-_" + "Down")
                        commentsObject.ref.update(commentUpdate)
                            .addOnSuccessListener {
                                holder.downVote2.text = "$newVotes Downs"
                                holder.downVote2.setTextColor(Color.MAGENTA)
                            }
                    }
                }

            }else{
                val count: CountDownTimer = object : CountDownTimer(1000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        holder.downVote2.text = commentsObject.downVotes + " Downs"
                        val login = LogInSignUp(c)
                        login.show()
                    }
                }
                count.start()
            }

        }

        holder.upVote1.setOnClickListener {
            val newVotes: String = (Integer.parseInt(commentsObject.upVotes) + 1).toString()
            holder.upVote1.text = "$newVotes Ups"
            if (user != null){
                if (commentsObject.uiDs != null){
                    if (commentsObject.uiDs.contains(user.uid + "_-_" + "Up") || commentsObject.uiDs.contains(
                            user.uid + "_-_" + "Down")){
                        val count: CountDownTimer = object : CountDownTimer(1000, 1000) {
                            override fun onTick(millisUntilFinished: Long) {}
                            override fun onFinish() {
                                holder.upVote1.text = commentsObject.upVotes + " Ups"
                            }
                        }
                        count.start()
                    }else{
                        commentsObject.uiDs.add(user.uid + "_-_" + "Up")
                        val commentUpdate = HashMap<String, Any>()
                        commentUpdate["upVotes"] = FieldValue.increment(1)
                        commentUpdate["uiDs"] = FieldValue.arrayUnion(user.uid + "_-_" + "Up")
                        commentsObject.ref.update(commentUpdate)
                            .addOnSuccessListener {
                                holder.upVote1.text = "$newVotes Ups"
                                holder.upVote1.setTextColor(Color.MAGENTA)
                            }
                            .addOnFailureListener{
                                Toast.makeText(c, it.message, Toast.LENGTH_SHORT).show()
                                holder.upVote1.text = commentsObject.upVotes + " Ups"
                            }
                    }
                }

            }else{
                val count: CountDownTimer = object : CountDownTimer(1000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        holder.upVote1.text = commentsObject.upVotes + " Ups"
                        val login = LogInSignUp(c)
                        login.show()
                    }
                }
                count.start()
            }

        }

        holder.downVote1.setOnClickListener {
            val newVotes: String = (Integer.parseInt(commentsObject.downVotes ) + 1).toString()
            holder.downVote1.text = "$newVotes Downs"
            if (user != null){
                if (commentsObject.uiDs != null){
                    if (commentsObject.uiDs.contains(user.uid + "_-_" + "Up") || commentsObject.uiDs.contains(user.uid + "_-_" + "Down")){
                        val count: CountDownTimer = object : CountDownTimer(1000, 1000) {
                            override fun onTick(millisUntilFinished: Long) {}
                            override fun onFinish() {
                                holder.downVote1.text = commentsObject.downVotes + " Downs"
                            }
                        }
                        count.start()
                    }else{
                        commentsObject.uiDs.add(user.uid + "_-_" + "Down")
                        val commentUpdate = HashMap<String, Any>()
                        commentUpdate["downVotes"] = FieldValue.increment(1)
                        commentUpdate["uiDs"] = FieldValue.arrayUnion(user.uid + "_-_" + "Down")
                        commentsObject.ref.update(commentUpdate)
                            .addOnSuccessListener {
                                holder.downVote1.text = "$newVotes Downs"
                                holder.downVote1.setTextColor(Color.MAGENTA)
                            }
                    }
                }

            }else{
                val count: CountDownTimer = object : CountDownTimer(1000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        holder.downVote1.text = commentsObject.downVotes + " Downs"
                        val login = LogInSignUp(c)
                        login.show()
                    }
                }
                count.start()
            }

        }


    }

    override fun getItemCount(): Int {
        return commentsObjects.size
    }

    class PointsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var wholecomment: TextView = itemView.findViewById(R.id.wholecomment)

        var commentCard: CardView = itemView.findViewById(R.id.commentCard)
        var dateTime: TextView = itemView.findViewById(R.id.comment_time_date)
        var commenter: TextView = itemView.findViewById(R.id.commenter)
        var cid1: TextView = itemView.findViewById(R.id.cid1)
        var replies1: TextView = itemView.findViewById(R.id.replies1)
        var upVote1: TextView = itemView.findViewById(R.id.upVote1)
        var downVote1: TextView = itemView.findViewById(R.id.downVote1)
        var userNameComment: ReadMoreTextView = itemView.findViewById(R.id.comment_user_name)

        var imageCommentCard: CardView = itemView.findViewById(R.id.imageCommentCard)
        var imageComment: ImageView = itemView.findViewById(R.id.imageComment)
        var imageCommentVP: ViewPager2 = itemView.findViewById(R.id.imageCommentVP)
        var dateTimeIm: TextView = itemView.findViewById(R.id.im_comment_time_date)
        var commenterIm: TextView = itemView.findViewById(R.id.im_commenter)
        var cid2: TextView = itemView.findViewById(R.id.cid2)
        var replies2: TextView = itemView.findViewById(R.id.replies2)
        var upVote2: TextView = itemView.findViewById(R.id.upVote2)
        var downVote2: TextView = itemView.findViewById(R.id.downVote2)
        var userNameCommentIm: ReadMoreTextView = itemView.findViewById(R.id.im_comment_user_name)

        var docCommentCard: CardView = itemView.findViewById(R.id.docCommentCard)
        var docLy: LinearLayout = itemView.findViewById(R.id.doc_ly)
        var docEmblem: ImageView = itemView.findViewById(R.id.doc_emblem)
        var docName: TextView = itemView.findViewById(R.id.doc_name)
        var dateTimeDoc: TextView = itemView.findViewById(R.id.doc_comment_time_date)
        var commenterDoc: TextView = itemView.findViewById(R.id.doc_commenter)
        var userNameCommentDoc: ReadMoreTextView = itemView.findViewById(R.id.doc_comment_user_name)
    }
}