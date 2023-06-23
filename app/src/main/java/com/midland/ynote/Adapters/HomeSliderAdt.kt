package com.midland.ynote.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.flaviofaria.kenburnsview.KenBurnsView
import com.midland.ynote.Activities.GeneralSearch
import com.midland.ynote.Activities.UserProfile2
import com.midland.ynote.Adapters.HomeSliderAdt.HomeSliderVH
import com.midland.ynote.MainActivity
import com.midland.ynote.Objects.HomeSliderObj
import com.midland.ynote.R
import com.midland.ynote.Utilities.FilingSystem
import de.hdodenhof.circleimageview.CircleImageView

private var a1: Activity? = null
class HomeSliderAdt(
    private val c: Context,
    private val a: Activity?,
    private val homeSliderObjs: ArrayList<HomeSliderObj>?,
    private val imCommentUrls: ArrayList<String>?
) : RecyclerView.Adapter<HomeSliderVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeSliderVH {
        a1 = a
        return if (a1 == null) {
            HomeSliderVH(
                LayoutInflater.from(c)
                    .inflate(R.layout.search_result_users, parent, false)
            )
        } else {
            HomeSliderVH(
                LayoutInflater.from(c)
                    .inflate(R.layout.home_slider, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: HomeSliderVH, position: Int) {
        if (imCommentUrls == null) {
            val homeSliderObj = homeSliderObjs!![holder.absoluteAdapterPosition]
            holder.userName.visibility = View.VISIBLE
            holder.university.visibility = View.VISIBLE
            holder.userImage.visibility = View.VISIBLE

            if (homeSliderObj.philosophy == null || homeSliderObj.philosophy.trim { it <= ' ' } == "") {
                holder.philosophyRel.visibility = View.GONE
            } else {
                holder.philosophy.text = homeSliderObj.philosophy
                holder.philosophy.visibility = View.VISIBLE
            }
            holder.userMail.text = homeSliderObj.userMail
            holder.userName.text = homeSliderObj.userName
            holder.university.text = homeSliderObj.institution
            holder.uid.text = homeSliderObj.userId
            holder.department.text = homeSliderObj.school
            holder.coaches.text = homeSliderObj.coaches + " C"
            holder.students.text = homeSliderObj.students + " S"


            try {
                Glide.with(c).load(homeSliderObj.userImage).thumbnail(0.9.toFloat())
                    .placeholder(R.drawable.ic_account_circle).into(holder.userImage)
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
            holder.userImage.setOnClickListener {
                if (homeSliderObj.userImage != null) {
                    if (a == null) {
                        GeneralSearch.touchIV!!.visibility = View.VISIBLE
                        GeneralSearch.touchIV!!.bringToFront()
                        GeneralSearch.touchIV!!.setImageResource(R.drawable.ic_hourglass)
                        FilingSystem.downloadImage(GeneralSearch.touchIV!!, homeSliderObj.userImage)
                    } else {
                        MainActivity.touchIV!!.visibility = View.VISIBLE
                        MainActivity.close!!.visibility = View.VISIBLE
                        MainActivity.touchIV!!.bringToFront()
                        MainActivity.touchIV!!.setImageResource(R.drawable.ic_hourglass)
                        FilingSystem.downloadImage(MainActivity.touchIV!!, homeSliderObj.userImage)
                    }
                }
            }
            if (homeSliderObj.backgroundImage != null) {
                Glide.with(c).load(homeSliderObj.backgroundImage).thumbnail(0.9.toFloat())
                    .centerInside().into(holder.backgroundImage)
            }
            holder.itemView.setOnClickListener {
                val intent = Intent(c, UserProfile2::class.java)
                intent.putExtra("userID", holder.uid.text.toString())
                c.startActivity(intent)
            }
        } else {
            val imComment = imCommentUrls[holder.absoluteAdapterPosition]
            holder.homeSliderCard.visibility = View.GONE
            holder.imCommentObj.visibility = View.VISIBLE
            Glide.with(c).load(imComment)
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(holder.imCommentObj)

            holder.imCommentObj.setOnClickListener {

            }
        }
    }

    override fun getItemCount(): Int {
        var size = 0
        if (homeSliderObjs == null && imCommentUrls != null) {
            size = imCommentUrls.size
        } else if (homeSliderObjs != null) {
            size = homeSliderObjs.size
        }
        return size
    }

    class HomeSliderVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var userName: TextView
        lateinit var userMail: TextView
        lateinit var philosophy: TextView
        lateinit var university: TextView
        lateinit var students: TextView
        lateinit var coaches: TextView
        lateinit var department: TextView
        lateinit var uid: TextView
        lateinit var userImage: CircleImageView
        lateinit var backgroundImage: KenBurnsView
        lateinit var philosophyRel: RelativeLayout
        lateinit var homeSliderCard: CardView
        lateinit var imCommentObj: ImageView

        init {
            try {
                imCommentObj = itemView.findViewById(R.id.imCommentObj)
                students = itemView.findViewById(R.id.studCount)
                coaches = itemView.findViewById(R.id.coachCount)
                homeSliderCard = itemView.findViewById(R.id.homeSliderCard)
                university = itemView.findViewById(R.id.university)
                uid = itemView.findViewById(R.id.uid)
                department = itemView.findViewById(R.id.department)
                userImage = itemView.findViewById(R.id.userDisplayImageHM)
                backgroundImage = itemView.findViewById(R.id.backgroundImageHM)
                userName = itemView.findViewById(R.id.displayNameHM)
                philosophyRel = itemView.findViewById(R.id.philosophyRel)
                userMail = itemView.findViewById(R.id.userEmailHM)
                philosophy = itemView.findViewById(R.id.philosophyHM)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}