package com.midland.ynote.Adapters

import android.content.Context
import android.graphics.Color
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.midland.ynote.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.midland.ynote.Adapters.GalleryAdt.GalleryVH
import com.midland.ynote.Dialogs.LogInSignUp
import com.midland.ynote.Objects.PersonalGallery

class GalleryAdt(var context: Context, var galleryItems: ArrayList<PersonalGallery>) :
    RecyclerView.Adapter<GalleryVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryVH {
        return GalleryVH(
            LayoutInflater.from(context).inflate(R.layout.gallery_object, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GalleryVH, position: Int) {
        val user = FirebaseAuth.getInstance().currentUser
        val galleryItem = galleryItems[holder.absoluteAdapterPosition]
        Glide.with(context).load(galleryItem.link).thumbnail(0.5f)
            .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop()
            .placeholder(R.drawable.ic_hourglass_bottom_white)
            .into(holder.photo)
        holder.upVotes.text = galleryItem.upVotes
        holder.downVotes.text = galleryItem.downVotes
        holder.commentsCount.text = galleryItem.commentsCount
        holder.up.setOnClickListener {
            val newVotes: String = (Integer.parseInt(galleryItem.upVotes) + 1).toString()
            holder.upVotes.text = newVotes
            if (galleryItem.uiDs != null){

                if (user != null){
                    if (galleryItem.uiDs.contains(user.uid + "_-_" + "Up") || galleryItem.uiDs.contains(
                            user.uid + "_-_" + "Down")){
                        val count: CountDownTimer = object : CountDownTimer(1000, 1000) {
                            override fun onTick(millisUntilFinished: Long) {}
                            override fun onFinish() {
                                holder.upVotes.text = galleryItem.upVotes + " Ups"
                            }
                        }
                        count.start()
                    }
                    else{
                        galleryItem.uiDs.add(user.uid + "_-_" + "Up")
                        val commentUpdate = HashMap<String, Any>()
                        commentUpdate["upVotes"] = FieldValue.increment(1)
                        commentUpdate["uiDs"] = FieldValue.arrayUnion(user.uid + "_-_" + "Up")
                        galleryItem.reference.update(commentUpdate)
                            .addOnSuccessListener {
                                holder.upVotes.text = newVotes
                                holder.upVotes.setTextColor(Color.MAGENTA)
                            }
                    }

                }else{
                    val count: CountDownTimer = object : CountDownTimer(1000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {}
                        override fun onFinish() {
                            holder.upVotes.text = galleryItem.upVotes
                            val login =
                                LogInSignUp(context)
                            login.show()
                        }
                    }
                    count.start()
                }
            }
        }
        holder.down.setOnClickListener {
            val newVotes: String = (Integer.parseInt(galleryItem.downVotes ) + 1).toString()
            holder.downVotes.text = "$newVotes"
            if (user != null){
                if (galleryItem.uiDs != null){

                    if (galleryItem.uiDs.contains(user.uid + "_-_" + "Up") || galleryItem.uiDs.contains(user.uid + "_-_" + "Down")){
                        val count: CountDownTimer = object : CountDownTimer(1000, 1000) {
                            override fun onTick(millisUntilFinished: Long) {}
                            override fun onFinish() {
                                holder.downVotes.text = galleryItem.downVotes
                            }
                        }
                        count.start()
                    }else{
                        galleryItem.uiDs.add(user.uid + "_-_" + "Down")
                        val commentUpdate = HashMap<String, Any>()
                        commentUpdate["downVotes"] = FieldValue.increment(1)
                        commentUpdate["uiDs"] = FieldValue.arrayUnion(user.uid + "_-_" + "Down")
                        galleryItem.reference.update(commentUpdate)
                            .addOnSuccessListener {
                                holder.downVotes.text = "$newVotes"
                                holder.downVotes.setTextColor(Color.MAGENTA)
                            }
                    }
                }

            }else{
                val count: CountDownTimer = object : CountDownTimer(1000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        holder.downVotes.text = galleryItem.downVotes + " Downs"
                        val login =
                            LogInSignUp(context)
                        login.show()
                    }
                }
                count.start()
            }
        }
        holder.comment.setOnClickListener { view: View? -> }
    }

    override fun getItemCount(): Int {
        return galleryItems.size
    }

    class GalleryVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var photo: ImageView
        var upVotes: TextView
        var downVotes: TextView
        var commentsCount: TextView
        var up: LinearLayout
        var down: LinearLayout
        var comment: LinearLayout

        init {
            photo = itemView.findViewById(R.id.photo)
            up = itemView.findViewById(R.id.up)
            down = itemView.findViewById(R.id.down)
            comment = itemView.findViewById(R.id.comment)
            upVotes = itemView.findViewById(R.id.upVotes)
            downVotes = itemView.findViewById(R.id.downVotes)
            commentsCount = itemView.findViewById(R.id.commentsCount)
        }
    }
}