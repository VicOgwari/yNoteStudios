package com.midland.ynote.Adapters

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.borjabravo.readmoretextview.ReadMoreTextView
import com.bumptech.glide.Glide
import com.midland.ynote.R
import com.midland.ynote.Objects.PictorialObject
import com.midland.ynote.Utilities.Projector
import com.squareup.picasso.Picasso
import java.util.*

class SliderAdapter internal constructor(
    private val context: Context,
    private val slideItems: ArrayList<PictorialObject>,
    private val viewPager2: ViewPager2,
    private val projector: Projector?
) :
        RecyclerView.Adapter<SliderViewHolder>() {

    private val slideRunnable1 = Runnable { viewPager2.setCurrentItem(viewPager2.currentItem + 1, true) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return if (projector != null){
            SliderViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.slide_item_container, parent, false)
            )
        }else{
            SliderViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.projector_item, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {

        if (projector != null){
//            Picasso.get()
//                .load(Uri.parse(slideItems[position].picture))
//                .centerCrop().fit().into(holder.imageView)

            Glide.with(context).load(slideItems[position].picture)
                .fitCenter()
                .into(holder.imageView);

            holder.description.text = slideItems[position].picDescription
//        playNarrationList(slideItems)
//        val narrationFile = File(slideItems[position].narrationPath)
//        if (narrationFile.exists()) {
//            playNarration(narrationFile, narrationTitle, pausePlayNar, slideHandler, slideRunnable)
//        }
            if (position == slideItems.size - 2) {
                viewPager2.post(slideRunnable)
            }
        }else{
            Picasso.get().load(Uri.parse(slideItems[position].picture)).centerCrop().fit().into(holder.imageView)
        }
    }

    override fun getItemCount(): Int {
        return slideItems.size
    }


    private val slideRunnable = Runnable {
        slideItems.addAll(slideItems)
        notifyDataSetChanged()
    }

}

class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var imageView: ImageView = itemView.findViewById(R.id.imageSlide)
    var description: ReadMoreTextView = itemView.findViewById(R.id.pictorialDescription)


}

private var mediaPlayer = MediaPlayer()
private var isPlaying = false
private var timer: Timer = Timer()
private var i = 0

private fun playNarrationList(slideItems: ArrayList<PictorialObject>) {
    timer = Timer()
    timer.schedule(object : TimerTask() {
        override fun run() {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(slideItems[++i].narrationPath)
            mediaPlayer.prepare()
            mediaPlayer.start()

            if (slideItems.size > i + 1){
                playNarrationList(slideItems)
            }
        }
    }, (mediaPlayer.duration + 100).toLong())
}
//
//fun playNarration(narration: File, narrationTitle: TextView, pausePlayNar: ImageButton, slideHandler: Handler, slideRunnable: Runnable) {
//    mediaPlayer = MediaPlayer()
//    try {
//        mediaPlayer.setDataSource(narration.absolutePath)
//        mediaPlayer.prepare()
//        mediaPlayer.start()
//    } catch (e: IOException) {
//    }
//
//    pausePlayNar.setBackgroundResource(R.drawable.ic_pause)
//    narrationTitle.text = "Playing"
//    isPlaying = true
//
//    mediaPlayer.setOnCompletionListener {
//        stopNarration(narrationTitle, pausePlayNar)
//        narrationTitle.text = "Finished"
//        slideHandler.removeCallbacks(slideRunnable)
//        slideHandler.postDelayed(slideRunnable, 5000)
//    }
//
//}
//
//private fun stopNarration(narrationTitle: TextView, pausePlayNar: ImageButton) {
//    pausePlayNar!!.setBackgroundResource(R.drawable.ic_play_narration)
//    narrationTitle!!.text = "Stopped"
//    isPlaying = false
//}