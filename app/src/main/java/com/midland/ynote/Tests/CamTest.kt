package com.midland.ynote.Tests

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.midland.ynote.R
import com.priyankvasa.android.cameraviewex.CameraView

class CamTest : AppCompatActivity() {
    lateinit var camera: CameraView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cam_test)
        camera = findViewById(R.id.outSourcedCamera)

        camera.addCameraOpenedListener {

        }
            .addCameraClosedListener {

            }
            .addCameraErrorListener { t, errorLevel -> Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show() }
    }

    override fun onResume() {
        super.onResume()
        camera.start()
    }

    override fun onDestroy() {
        camera.stop()
        super.onDestroy()
    }

}