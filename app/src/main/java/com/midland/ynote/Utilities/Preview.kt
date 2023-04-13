package com.midland.ynote.Utilities

import android.content.Context
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import java.io.IOException


private var mCamera: android.hardware.Camera? = null

class Preview(context: Context, surfaceView: SurfaceView = SurfaceView(context)) : ViewGroup(context), SurfaceHolder.Callback {

    var mHolder: SurfaceHolder = surfaceView.holder.apply {
        addCallback(this@Preview)
        setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
    }


    fun setCamera(camera: android.hardware.Camera?) {
        if (mCamera == camera) {
            return
        }

        stopPreviewAndFreeCamera()

        mCamera = camera

        mCamera?.apply {
            var mSupportedPreviewSizes = parameters.supportedPreviewSizes
            requestLayout()

            try {
                setPreviewDisplay(mHolder)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            // Important: Call startPreview() to start updating the preview
            // surface. Preview must be started before you can take a picture.
            startPreview()
        }
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        TODO("Not yet implemented")
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, w: Int, h: Int) {
        mCamera?.apply {
            // Now that the size is known, set up the camera parameters and begin
            // the preview.
            parameters?.also { params ->
                params.setPreviewSize(w, h)
                requestLayout()
                parameters = params
            }

            // Important: Call startPreview() to start updating the preview surface.
            // Preview must be started before you can take a picture.
            startPreview()
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        // Surface will be destroyed when we return, so stop the preview.
        // Call stopPreview() to stop updating the preview surface.
        mCamera?.stopPreview()
    }

    /**
     * When this function returns, mCamera will be null.
     */
    private fun stopPreviewAndFreeCamera() {
        mCamera?.apply {
            // Call stopPreview() to stop updating the preview surface.
            stopPreview()

            // Important: Call release() to release the camera for use by other
            // applications. Applications should release the camera immediately
            // during onPause() and re-open() it during onResume()).
            release()

            mCamera = null
        }
    }

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {

    }


}