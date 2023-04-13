package com.midland.ynote.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.midland.ynote.R
import com.midland.ynote.Objects.SearchModel
import de.hdodenhof.circleimageview.CircleImageView

class SearchResultsAdapter(
    var con: Context,
    var searchList: ArrayList<SearchModel>,
    var profilePics: ArrayList<String>?, var flag: String): RecyclerView.Adapter<SearchResultsAdapter.SearchResultVH>() {


    class SearchResultVH(itemView: View): RecyclerView.ViewHolder(itemView) {
        var searchItemTV: TextView = itemView.findViewById(R.id.pointItem)
        var userName: TextView = itemView.findViewById(R.id.userNameSearch)
        var studentsTV: TextView = itemView.findViewById(R.id.studentsTV)
        var coachesTV: TextView = itemView.findViewById(R.id.coachesTV)
        var profilePic: CircleImageView = itemView.findViewById(R.id.userProfileSearch)

        fun bind(searchModel: SearchModel){
            searchItemTV.text = searchModel.title
        }

        fun bind1(con: Context, searchModel: SearchModel, profileUrl: String){
            userName.text = searchModel.userName
//            userName.text = searchModel.profile
            Glide.with(con).load(profileUrl).into(profilePic)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultVH {
        var view: View? = null
        when (flag) {
            "User search" -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.user_search_result, parent, false)
            }
            "Doc search" -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.point_item, parent, false)
            }
            "Lecture search" -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.point_item, parent, false)
            }
        }
        return SearchResultVH(view!!)
    }

    override fun onBindViewHolder(holder: SearchResultVH, position: Int) {
        if (flag == "Doc search" || flag == "Lecture search"){
            holder.bind(searchList[position])
        }else{
            holder.bind1(con, searchList[position], profilePics!![position])
        }
    }

    override fun getItemCount(): Int {
        return searchList.size
    }
}