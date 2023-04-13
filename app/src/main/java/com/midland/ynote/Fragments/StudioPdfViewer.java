package com.midland.ynote.Fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.midland.ynote.R;
import com.midland.ynote.Utilities.CameraPreview;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudioPdfViewer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudioPdfViewer extends Fragment {

    PDFView studioPdfView;
    String pdfLocale;
    private FrameLayout preview;
    private CardView cameraCard;
    private RelativeLayout frontRel;
    private Camera mCamera;
    private CameraPreview mPreview;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StudioPdfViewer() {
        // Required empty public constructor
    }

    public StudioPdfViewer(String pdfLocale) {
        this.pdfLocale = pdfLocale;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OtherVideos.
     */
    // TODO: Rename and change types and number of parameters
    public static StudioPdfViewer newInstance(String param1, String param2) {
        StudioPdfViewer fragment = new StudioPdfViewer();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.studio_pdf_fragment, container, false);
        studioPdfView = v.findViewById(R.id.studioPdfView);
        preview = v.findViewById(R.id.cameraPreviewFragment);
        cameraCard = v.findViewById(R.id.cameraCardFragment);
        frontRel = v.findViewById(R.id.frontRel);

        checkCameraHardware(getContext());


        int savedPage = studioPdfView.getCurrentPage();
        if (pdfLocale != null)
        studioPdfView.fromUri(Uri.parse(pdfLocale))
                .password(null)// IF PASSWORD PROTECTED
                .defaultPage(savedPage) // THIS VALUE CAN BE STORED TO BE OPEN FROM LAST TIME
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .onPageScroll(new OnPageScrollListener() {
                    @Override
                    public void onPageScrolled(int page, float positionOffset) {

                    }
                })
                .onRender(new OnRenderListener() {
                    @Override
                    public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {

                    }
                })
                .enableAnnotationRendering(false)
                .enableAntialiasing(true)
                .spacing(10)
                .onDraw(new OnDrawListener() {
                    @Override
                    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

                    }
                })
                .onDrawAll(new OnDrawListener() {
                    @Override
                    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

                    }
                })
                .onPageError(new OnPageErrorListener() {
                    @Override
                    public void onPageError(int page, Throwable t) {

                    }
                })
                .onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {
                    }
                })
                .scrollHandle(new DefaultScrollHandle(getContext()))
                .onTap(new OnTapListener() {
                    @Override
                    public boolean onTap(MotionEvent e) {
                        return true;
                    }
                })
                .onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {

                    }
                })
                .invalidPageColor(Color.WHITE)
                .load();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }



    @Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // Create an instance of Camera
            mCamera =  getCameraInstance();
            // Create our Preview view and set it as the content of our activity.
            mPreview = new CameraPreview(getContext(), mCamera);
            preview.addView(mPreview);

            return true;
        } else {
            Toast.makeText(getContext(), "Couldn't find a camera", Toast.LENGTH_SHORT).show();
            // no camera on this device
            return false;
        }
    }

    public Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
}