package com.midland.ynote.Utilities

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.midland.ynote.R

class NavHeaderMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_header_main)
        val im = findViewById<ImageView>(R.id.navImage)
        im.setOnClickListener{
            Toast.makeText(applicationContext, "Every time is party time", Toast.LENGTH_SHORT).show()
        }
    }
}