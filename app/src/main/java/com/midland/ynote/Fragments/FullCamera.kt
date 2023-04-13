package com.midland.ynote.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.midland.ynote.R
import com.priyankvasa.android.cameraviewex.CameraView
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FullCamera.newInstance] factory method to
 * create an instance of this fragment.
 */
typealias CornersListener = () -> Unit

class FullCamera : Fragment() {

    lateinit var camera: CameraView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_full_camera, container, false)
        camera = v.findViewById(R.id.outSourcedCamera)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        camera.addCameraOpenedListener {

        }
            .addCameraClosedListener {

            }
            .addCameraErrorListener { t, errorLevel ->
                Toast.makeText(
                    context,
                    t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
    }


    override fun onResume() {
        super.onResume()
        camera.start()
    }

    override fun onPause() {
        camera.stop()
        super.onPause()
    }

    override fun onDestroyView() {
        camera.destroy()
        super.onDestroyView()
    }


}