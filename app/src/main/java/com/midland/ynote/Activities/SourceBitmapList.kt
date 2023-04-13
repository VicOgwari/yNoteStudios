package com.midland.ynote.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.midland.ynote.Adapters.PicsAdapter
import com.midland.ynote.Objects.BitMapTitle
import com.midland.ynote.R

class SourceBitmapList : AppCompatActivity() {

    var bitMaps = ArrayList<BitMapTitle>()
    private var bitMapAdapter: PicsAdapter? = null
    private var sourceDocUri: String? = null
    private var fileName: String? = null
    private var skip: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_source_bitmap_list)

        val intent = intent
        if (intent.getStringExtra("selectedDoc") != null &&
            intent.getStringExtra("fileName") != null){

            sourceDocUri = intent.getStringExtra("selectedDoc")
            fileName = intent.getStringExtra("fileName")
        }else{
            Toast.makeText(applicationContext, "Something's missing...", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }

        val bitmapRV = findViewById<RecyclerView>(R.id.bitmapsListRV)
        skip = findViewById(R.id.skipButton1)

        skip!!.setOnClickListener {
            val intent1 = Intent(this@SourceBitmapList, LectureStudio2::class.java)
            intent1.putExtra("fileName", fileName)
            intent1.putExtra("selectedDoc", sourceDocUri)
            intent.putExtra("studio", "1")
            startActivity(intent1)
        }


        val sharedPreferences = this@SourceBitmapList.getSharedPreferences("bitmapTitle", Context.MODE_PRIVATE)

        bitMaps = if (sharedPreferences!!.contains("bitmapTitle")) {
            val gson = Gson()
            val json = sharedPreferences.getString("bitmapTitle", "")
            val type = object : TypeToken<ArrayList<BitMapTitle>>() {}.type
            gson.fromJson(json, type)!!
        }else{
            ArrayList()
        }

        if (bitMaps.size > 0){
            bitMapAdapter = PicsAdapter(
                this@SourceBitmapList,
                bitMaps,
                "activity",
                sourceDocUri,
                fileName
            )
            bitMapAdapter!!.notifyDataSetChanged()
        }else{

        }
        bitmapRV.layoutManager = LinearLayoutManager(this@SourceBitmapList, RecyclerView.VERTICAL, false)
        bitmapRV.adapter = bitMapAdapter

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        finish()
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        startActivity(Intent(applicationContext, SourceDocList::class.java))
    }
}