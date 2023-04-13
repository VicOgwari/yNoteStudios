package com.midland.ynote.Adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.midland.ynote.R
import com.midland.ynote.Adapters.MoreIdeasAdt.ImagesVH
import com.midland.ynote.Objects.ImageObject

class MoreIdeasAdt(
    private val add: ImageButton,
    var imageObjects: ArrayList<ImageObject>,
    var flag: String,
    var con: Context,
    var images: ArrayList<String>
) : RecyclerView.Adapter<ImagesVH>() {
    companion object{
        var images1 = ArrayList<String>()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesVH {
        return ImagesVH(LayoutInflater.from(con).inflate(R.layout.gallery_object1, parent, false))
    }

    override fun onBindViewHolder(holder: ImagesVH, position: Int) {
        val imageObject = imageObjects[position]
        Glide.with(con).load(imageObject.uri).thumbnail(1f).into(holder.imageView)
        holder.itemView.setOnClickListener { v: View? ->
            if (flag == "moreIdeas") {
                if (images.contains(imageObject.uri.toString())) {
                    holder.itemView.isSelected = false
                    holder.imageRel.setBackgroundColor(Color.WHITE)
                    images.remove(imageObject.uri.toString())
                    images1.remove(imageObject.uri.toString())
                } else {
                    holder.itemView.isSelected = true
                    holder.imageRel.setBackgroundColor(Color.BLUE)
                    images.add(imageObject.uri.toString())
                    images1.add(imageObject.uri.toString())
                    add.visibility = View.VISIBLE
                }
                if (images.size > 0) {
                    add.visibility = View.VISIBLE
                } else {
                    add.visibility = View.GONE
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return imageObjects.size
    }

    class ImagesVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        var imageRel: RelativeLayout

        init {
            imageView = itemView.findViewById(R.id.photo)
            imageRel = itemView.findViewById(R.id.imageRel)
        }
    }
}