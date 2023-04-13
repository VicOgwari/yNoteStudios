package com.midland.ynote.Tests;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.midland.ynote.R;

public class DualCamActivity extends Activity {

    private Camera mBackCamera;
    private Camera mFrontCamera;
    private BackCameraPreview mBackCamPreview;
    private FrontCameraPreview mFrontCamPreview;

    public static String TAG = "DualCamActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dual_cam);

        Log.i(TAG, "Number of cameras: " + Camera.getNumberOfCameras());

        // Create an instance of Camera
        mBackCamera = getCameraInstance(0, getApplicationContext());
        // Create back camera Preview view and set it as the content of our activity.
        mBackCamPreview = new BackCameraPreview(this, mBackCamera);
        FrameLayout backPreview = (FrameLayout) findViewById(R.id.back_camera_preview);
        backPreview.addView(mBackCamPreview);

        mFrontCamera = getCameraInstance(1, getApplicationContext());
        mFrontCamPreview = new FrontCameraPreview(this, mFrontCamera);
        FrameLayout frontPreview = (FrameLayout) findViewById(R.id.front_camera_preview);
        frontPreview.addView(mFrontCamPreview);


    }


    public static Camera getCameraInstance(int cameraId, Context con){
        Camera c = null;
        try {
            c = Camera.open(cameraId); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
            Log.e(TAG,"Camera " + cameraId + " not available! " + e.toString() );
            Toast.makeText(con, "", Toast.LENGTH_SHORT).show();
        }
        return c; // returns null if camera is unavailable
    }
}