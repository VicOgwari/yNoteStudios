package com.midland.ynote.Utilities

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.midland.ynote.R
import com.midland.ynote.Adapters.SliderAdapter
import com.midland.ynote.Objects.PictorialObject
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

class Projector : AppCompatActivity() {
    private var viewPager2: ViewPager2? = null
    private var backgroundImage: ImageView? = null
    private var narrationTitle: TextView? = null
    private var durationDisplay: TextView? = null
    private var seekBar: SeekBar? = null
    private val slideHandler = Handler()
    private var mediaPlayer = MediaPlayer()
    private var isPlaying = false
    private var timer: Timer? = null
    private var playList = ArrayList<File>()
    private var i = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager)

        viewPager2 = findViewById(R.id.viewPagerSlider)
        backgroundImage = findViewById(R.id.backgroundImage)
        seekBar = findViewById(R.id.narSeekBar)
        narrationTitle = findViewById(R.id.narrationTitle)
        durationDisplay = findViewById(R.id.textView4)

        //Get objects
        val uriList: ArrayList<PictorialObject>?
        val intent = intent
        uriList = intent.getSerializableExtra("photoDocs") as ArrayList<PictorialObject>
        playList = ArrayList()
        for (p in uriList){
            playList.add(File(p.narrationPath))
        }

        viewPager2!!.adapter = SliderAdapter(applicationContext, uriList, viewPager2!!, Projector())
        viewPager2!!.clipToPadding = false
        viewPager2!!.clipChildren = false
        viewPager2!!.offscreenPageLimit = 3
        viewPager2!!.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }
        viewPager2!!.setPageTransformer(compositePageTransformer)

        viewPager2!!.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                stopNarration()
                if (uriList[position].narrationPath != null){
                    playNarration(File(uriList[position].narrationPath))
                }
                Glide.with(applicationContext).load(uriList[position].picture)
//                    .thumbnail(1.0f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop()
                    .placeholder(R.drawable.ic_hourglass).into(backgroundImage!!)
//                Glide.with(applicationContext).load(uriList[position].picture).centerCrop().into(backgroundImage!!)
                slideHandler.removeCallbacks(slideRunnable)
                if (mediaPlayer.duration < 100.toLong() || uriList[position].narrationPath == null
                    || uriList[position].narrationPath == ""){
                    slideHandler.postDelayed(slideRunnable, 3000.toLong())
                }else{
                    slideHandler.postDelayed(slideRunnable, (mediaPlayer.duration + 100).toLong())
                }

            }
        })

    }

    override fun onDestroy() {
        viewPager2!!.adapter = null
        if (mediaPlayer.isPlaying){
            stopNarration()
            mediaPlayer.reset()
        }
        super.onDestroy()
    }

    override fun onStop() {
        viewPager2!!.adapter = null
        if (mediaPlayer.isPlaying){
            stopNarration()
            mediaPlayer.reset()
        }
        super.onStop()
    }

    override fun onBackPressed() {
        viewPager2!!.adapter = null
        if (mediaPlayer.isPlaying){
            stopNarration()
            mediaPlayer.reset()
        }
        super.onBackPressed()
    }

    private fun playNarration(narration: File) {
        if (mediaPlayer.isPlaying){
            mediaPlayer.reset()
        }
        mediaPlayer = MediaPlayer()
        try {
            mediaPlayer.setDataSource(narration.absolutePath)
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: IOException) {
        }

        val duration = mediaPlayer.duration
        val strDuration = covertFormat(duration)
        durationDisplay!!.text = strDuration + " / " + mediaPlayer.duration
        narrationTitle!!.text = "Playing"
        isPlaying = true

        seekBar!!.progress = mediaPlayer.currentPosition
        mediaPlayer.setOnCompletionListener {
            stopNarration()
            narrationTitle!!.text = "Finished"

        }

    }

    private fun covertFormat(duration: Int): String {
        return String().format("%02d:%02d",
        TimeUnit.MICROSECONDS.toMillis(duration.toLong()),
        TimeUnit.MICROSECONDS.toMillis(duration.toLong()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toSeconds(duration.toLong()))
        )
    }

//    private fun playNext() {
//        timer = Timer()
//        timer!!.schedule(object : TimerTask() {
//            override fun run() {
//                mediaPlayer.reset()U
//                mediaPlayer = MediaPlayer.create(this@ViewPager, Uri.parse(playList[++i].na));
//                mediaPlayer.start()
//
//                if (playList.size > i + 1){
//                    playNext()
//                }
//            }
//        }, (mediaPlayer.duration + 100).toLong())
//    }

    private fun stopNarration(){
        narrationTitle!!.text = "Stopped"
        isPlaying = false
    }
    private val slideRunnable = Runnable { viewPager2!!.setCurrentItem(viewPager2!!.currentItem + 1, true) }


}


