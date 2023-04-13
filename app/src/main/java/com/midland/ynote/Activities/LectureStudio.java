 package com.midland.ynote.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.abedelazizshe.lightcompressorlibrary.CompressionListener;
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor;
import com.midland.ynote.Objects.LectureObject;
import com.midland.ynote.Objects.PendingLecObj;
import com.midland.ynote.R;
import com.midland.ynote.Services.BackgroundService;
import com.midland.ynote.Utilities.CameraPreview;
import com.midland.ynote.Utilities.CustomHorizontalScrollView;
import com.midland.ynote.Utilities.CustomScrollView;
import com.midland.ynote.Utilities.FilingSystem;
import com.midland.ynote.Utilities.NotesPopUp;
import com.midland.ynote.Utilities.Paint.PaintSaveViewUtil;
import com.midland.ynote.Utilities.Paint.PaintView;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.midland.ynote.Objects.LectureObject;
import com.midland.ynote.Objects.PendingLecObj;
import com.midland.ynote.Services.BackgroundService;
import com.midland.ynote.Utilities.CameraPreview;
import com.midland.ynote.Utilities.CustomHorizontalScrollView;
import com.midland.ynote.Utilities.CustomScrollView;
import com.midland.ynote.Utilities.FilingSystem;
import com.midland.ynote.Utilities.NotesPopUp;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class LectureStudio extends AppCompatActivity {

    private Bitmap bitmap;
    private Surface surface;
    private Camera mCamera;
    private CameraPreview mPreview;
    private CardView cameraCard;
    private ViewGroup rootLayout;
    private int xDelta, yDelta;
    private static float move = 200;
    float ratio = 1.0f;
    int baseDist;
    float baseRatio;

    private final static int REQUEST_CODE = 1000;
    private final static int REQUEST_PERMISSION = 1001;

    private final static SparseIntArray ORIENTATION = new SparseIntArray();
    private static final int PICK_IMAGE = 999;


    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;
    private MediaProjectionCallback mediaProjectionCallback;
    private VirtualDisplay virtualDisplay;
    private MediaRecorder mediaRecorder;


    private int mScreenDensity;
    private static int DISPLAY_WIDTH;
    private static int DISPLAY_HEIGHT;

    static {
        ORIENTATION.append(Surface.ROTATION_0, 90);
        ORIENTATION.append(Surface.ROTATION_90, 0);
        ORIENTATION.append(Surface.ROTATION_180, 270);
        ORIENTATION.append(Surface.ROTATION_270, 180);


    }

    private ToggleButton toggleButton;
    private CoordinatorLayout coordinatorLayout;
    private String videoUri, compressedVideo;
    private String sourceDocUri;
    private String sourceBitmap;


    private BottomSheetBehavior bottomSheetBehavior;
    private PaintView paintView;
    private AlertDialog dialog;
    private TextView shouWidth;
    private int paintWidth;
    private ImageButton brushColor, recordLecture;
    public ArrayList<Uri> pointUri = new ArrayList<>();


    //Timer
    private static final long START_TIME_IN_MILLIS = 600000;
    private Button textViewCountDown;
    private CountDownTimer countDownTimer;
    private Boolean timerRunning = false;
    private long timeLeftInMillis = START_TIME_IN_MILLIS;
    public String fileName;
    public LectureObject lectureObject;
    private FrameLayout preview;

    //Compression
    private ProgressBar compressionProgress;
    private RelativeLayout progressRel;
    private TextView progressText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_studio);
        preview = findViewById(R.id.cameraPreview);
        checkCameraHardware(LectureStudio.this);
        cameraCard = findViewById(R.id.cameraCard);

        Intent intent = new Intent(this, BackgroundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }

        if (getIntent().getStringExtra("fileName") != null) {
            fileName = getIntent().getStringExtra("fileName");
        } else if (getIntent().getStringExtra("savedLecture") != null) {
            PendingLecObj pendingLecObj = (PendingLecObj) getIntent().getSerializableExtra("savedLecture");
            fileName = pendingLecObj.getPendingLecTitle();
            Uri imagePath = Uri.parse(pendingLecObj.getIllustrationLocale());
            sourceDocUri = pendingLecObj.getSourceDoc();

            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(imagePath, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePathString = cursor.getString(cursor.getColumnIndex(filePath[0]));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeFile(imagePathString, options);
            PaintStudio paintStudio = new PaintStudio(LectureStudio.this, LectureStudio.this, getParent(), bitmap);
            paintStudio.show();
            paintView.setImage(bitmap, 2500, 2500);
            cursor.close();
        }


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findViewById(R.id.cameraCard).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.cameraCard).setVisibility(View.GONE);
                } else if (findViewById(R.id.cameraCard).getVisibility() == View.GONE) {
                    findViewById(R.id.cameraCard).setVisibility(View.VISIBLE);
                }
            }
        });

        textViewCountDown = findViewById(R.id.text_countDown);
        updateCountDownText();

        toggleButton = findViewById(R.id.toggleButton);
        coordinatorLayout = findViewById(R.id.rootLayout1);
        final CustomScrollView paintScrollView = findViewById(R.id.paintScrollView);
        final CustomHorizontalScrollView paintHorizontalView = findViewById(R.id.paintHorizontalView);

        DisplayMetrics matrix = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(matrix);

        mScreenDensity = matrix.densityDpi;

        //Get Screen
        DISPLAY_HEIGHT = matrix.heightPixels;
        DISPLAY_WIDTH = matrix.widthPixels;

        mediaRecorder = new MediaRecorder();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        }

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(LectureStudio.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        + ContextCompat.checkSelfPermission(LectureStudio.this, Manifest.permission.RECORD_AUDIO)
                        + ContextCompat.checkSelfPermission(LectureStudio.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(LectureStudio.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            || ActivityCompat.shouldShowRequestPermissionRationale(LectureStudio.this, Manifest.permission.RECORD_AUDIO)) {
                        toggleButton.setChecked(false);
                        toggleButton.setTextOff("Record");

                        Snackbar.make(coordinatorLayout, "Permission", Snackbar.LENGTH_INDEFINITE)
                                .setAction("ENABLE", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ActivityCompat.requestPermissions(LectureStudio.this, new String[]{
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                Manifest.permission.RECORD_AUDIO
                                        }, REQUEST_PERMISSION);
                                    }
                                }).show();
                    } else {
                        ActivityCompat.requestPermissions(LectureStudio.this, new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.RECORD_AUDIO
                        }, REQUEST_PERMISSION);
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        toggleScreenShare(v);
                    } else {
                        toggleButton.setTextOff("Record");
                        Toast.makeText(getApplicationContext(), "Your API level can't support this action." +
                                " Save lecture & try again on a higher phone", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        View bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        //PAINT
        initView();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintView.init(metrics, 2500, 2500);

        final ImageButton scrollMode = findViewById(R.id.scrollMode);

        scrollMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paintScrollView.isEnableScrolling() && paintHorizontalView.isEnableScrolling()) {
                    scrollMode.setImageResource(R.drawable.ic_lock);
                    paintScrollView.setEnableScrolling(false);
                    paintHorizontalView.setEnableScrolling(false);
                    Toast.makeText(getApplicationContext(), "disabled", Toast.LENGTH_SHORT).show();
                } else {
                    scrollMode.setImageResource(R.drawable.ic_lock_open);
                    paintScrollView.setEnableScrolling(true);
                    paintHorizontalView.setEnableScrolling(true);
                    Toast.makeText(getApplicationContext(), "enabled", Toast.LENGTH_SHORT).show();

                }


            }
        });


        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:

                    case BottomSheetBehavior.STATE_EXPANDED:

                        break;

                    case BottomSheetBehavior.STATE_DRAGGING:
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;

                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;

                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void toggleScreenShare(View v) {
        if (((ToggleButton) v).isChecked()) {
            initRecorder();
            recordScreen();
            startTimer();
        } else {

//            mediaRecorder.stop(); //Produces a runtime error since it is called after a start()
            pauseTimer();
            mediaRecorder.reset();
            stopScreenRecord();
            resetTimer();

            //Play in Video View
//            videoView.setVisibility(View.VISIBLE);
//            videoView.setVideoURI(Uri.parse(videoUri));
//            videoView.requestFocus();
//            videoView.start();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void recordScreen() {
        if (mediaProjection == null) {
            startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
            return;
        }
        virtualDisplay = createVirtualDisplay();
        mediaRecorder.start();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private VirtualDisplay createVirtualDisplay() {
        return mediaProjection.createVirtualDisplay("Main Activity", DISPLAY_WIDTH, DISPLAY_HEIGHT, mScreenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, surface, null, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SimpleDateFormat")
    private void initRecorder() {
        try {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

            mediaRecorder.setAudioEncodingBitRate(16);
            mediaRecorder.setAudioSamplingRate(44100);

            //VIDEO RATES
            mediaRecorder.setVideoEncodingBitRate(512 * 1000);
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264); //Be very careful with the Encoder, it should match the VideoSource
            mediaRecorder.setVideoSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
            mediaRecorder.setVideoFrameRate(30);
            //AUDIO RATES
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
            mediaRecorder.setAudioEncodingBitRate(96000);
            mediaRecorder.setAudioSamplingRate(44100);

            videoUri = FilingSystem.Companion.getCompletedLectures()
                    + "/" + fileName + ".mp4";

            compressedVideo = FilingSystem.Companion.getCompletedLectures()
                    + "/" + fileName +
                    ".mp4";

//            videoUri = FilingSystem.Companion.getCompletedLectures()
//                    + "/" + fileName + new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss")
//                    .format(new Date()) +
//                    ".mp4";
            mediaRecorder.setOutputFile(videoUri);


            //Kuna setPreviewDisplay missing here that sounds interesting


            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            int orientation = ORIENTATION.get(rotation + 90);
            mediaRecorder.setOrientationHint(orientation);
            mediaRecorder.prepare();
            surface = mediaRecorder.getSurface();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 911 && data != null && resultCode == RESULT_OK) {
            Uri paintImagePicked = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(paintImagePicked, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeFile(imagePath, options);
            PaintStudio paintStudio = new PaintStudio(LectureStudio.this, LectureStudio.this, getParent(), bitmap);
            paintStudio.show();
            paintView.setImage(bitmap, 2500, 2500);
            cursor.close();
        }
        if (requestCode == PICK_IMAGE && data != null && resultCode == RESULT_OK) {
            Uri paintImagePicked = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(paintImagePicked, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
            paintView.setImage(bitmap, 2500, 2500);
            cursor.close();
        }
        if (requestCode != REQUEST_CODE) {
            Toast.makeText(this, "Unknown error :(", Toast.LENGTH_SHORT).show();
            return;

        }
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            mediaRecorder.reset();
            toggleButton.setChecked(false);
            return;
        }
        mediaProjectionCallback = new MediaProjectionCallback();

        assert data != null;
        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
        mediaProjection.registerCallback(mediaProjectionCallback, null);
        virtualDisplay = createVirtualDisplay();
        mediaRecorder.start();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public class MediaProjectionCallback extends MediaProjection.Callback {
        @Override
        public void onStop() {
            if (toggleButton.isChecked()) {
                toggleButton.setChecked(false);
//                mediaRecorder.stop();
                mediaRecorder.reset();
//                mediaRecorder.release();
            }
            mediaProjection = null;
            stopScreenRecord();
            super.onStop();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void stopScreenRecord() {
        if (virtualDisplay != null) {
            virtualDisplay.release();
            destroyMediaProjection();
//            compressor(videoUri, compressedVideo, compressionProgress, progressText, progressRel);
//            compressor1(videoUri, compressedVideo);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void destroyMediaProjection() {
        if (mediaProjection != null) {
            mediaProjection.unregisterCallback(mediaProjectionCallback);
            mediaProjection.stop();
            mediaProjection = null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (REQUEST_CODE) {
            case REQUEST_PERMISSION: {
                if (grantResults.length > 0 && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                    toggleScreenShare(toggleButton);
                } else {
                    toggleButton.setChecked(false);

                    Snackbar.make(coordinatorLayout, "Permission", Snackbar.LENGTH_INDEFINITE)
                            .setAction("ENABLE", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ActivityCompat.requestPermissions(LectureStudio.this, new String[]{
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                            Manifest.permission.RECORD_AUDIO
                                    }, REQUEST_PERMISSION);
                                }
                            }).show();


                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.paint_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.illustrate:
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                break;

            case R.id.notes:
                NotesPopUp notesPopUp = new NotesPopUp(LectureStudio.this, getParent(), getApplicationContext(), sourceDocUri, fileName, "LectureStudio");
                notesPopUp.show();
                break;

            case R.id.blinders:
                blinders();
                break;

            case R.id.studyMode:
                // TODO: 7/13/2020 add blinders
                break;

            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void blinders() {
        if (findViewById(R.id.topBlinder).getVisibility() == View.GONE) {
            findViewById(R.id.topBlinder).setVisibility(View.VISIBLE);
            findViewById(R.id.bottomBlinder).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.topBlinder).setVisibility(View.GONE);
            findViewById(R.id.bottomBlinder).setVisibility(View.GONE);
        }
    }

    private void initView() {

        //Compression
        progressRel = findViewById(R.id.compressionProgressLayout);
        progressText = findViewById(R.id.compressionProgressText);
        compressionProgress = findViewById(R.id.compressionProgress);

        //SCREEN RECORDER
        toggleButton = findViewById(R.id.toggleButton);

        DisplayMetrics matrix = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(matrix);

        mScreenDensity = matrix.densityDpi;

        //Get Screen
        DISPLAY_HEIGHT = matrix.heightPixels;
        DISPLAY_WIDTH = matrix.widthPixels;


        //GETS INTENT FROM PICS GRID CLASS TO  NOTES POP UP FOR RV ADAPTER
        Intent intent = getIntent();
        if (intent.getStringExtra("SelectedPic") != null) {
            pointUri.add(Uri.parse(intent.getStringExtra("SelectedPic")));
        }


        View dialogView = getLayoutInflater().inflate(R.layout.dialog_width_set, null);
        shouWidth = dialogView.findViewById(R.id.textView1);
        SeekBar widthSb = dialogView.findViewById(R.id.seekBar1);
        widthSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                shouWidth.setText("Current Width：" + (progress + 1));
                paintWidth = progress + 1;
            }
        });
        paintView = findViewById(R.id.paintView);
        dialog = new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_info).setTitle("Set the size of your Pen").setView(dialogView).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                paintView.setPaintWidth(paintWidth);
            }

        }).setNegativeButton("Cancel", null).create();

        PDFView pdfView = findViewById(R.id.pdfView);

        // TODO: 8/13/2020 THIS URI MUST BE SET FROM EITHER A NEW SELECTION OF FROM THE LECTURE MANIFEST
        sourceDocUri = getIntent().getStringExtra("selectedDoc");

//        int savedPage = pdfView.getCurrentPage();
        if (sourceDocUri != null)
            pdfView.fromUri(Uri.parse(sourceDocUri))
                    .password(null)// IF PASSWORD PROTECTED
//                    .defaultPage(savedPage) // THIS VALUE CAN BE STORED TO BE OPEN FROM LAST TIME
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
                    .scrollHandle(new DefaultScrollHandle(this))
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


        ImageButton brushType = findViewById(R.id.brushType);
        ImageButton brushSize = findViewById(R.id.brushSize);
        brushColor = findViewById(R.id.brushColor);
        recordLecture = findViewById(R.id.record);
        ImageButton eraser = findViewById(R.id.eraser);
        ImageButton undo = findViewById(R.id.undo);
        ImageButton redo = findViewById(R.id.redo);
        ImageButton moreOptions = findViewById(R.id.otherOptions);
        ImageButton saveBitmap = findViewById(R.id.saveBitmap);
        ImageButton openInStudio = findViewById(R.id.paintStudio);


        brushType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(LectureStudio.this, brushColor);
                popupMenu.inflate(R.menu.brush_type_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        paintView.disableEraser();
                        switch (item.getItemId()) {
                            case R.id.emboss:
                                paintView.emboss();
                                break;
                            case R.id.blur:
                                paintView.blur();
                                break;
                            case R.id.normal:
                                paintView.normal();
                                break;
                            case R.id.clear:
                                paintView.clear();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        brushSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });


        brushColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(LectureStudio.this, brushColor);
                popupMenu.inflate(R.menu.brush_color_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.red:
                                paintView.setColor(Color.RED);
                                break;
                            case R.id.green:
                                paintView.setColor(Color.GREEN);
                                break;
                            case R.id.blue:
                                paintView.setColor(Color.BLUE);
                                break;
                            case R.id.purple:
                                paintView.setColor(Color.MAGENTA);
                                break;
                            case R.id.yellow:
                                paintView.setColor(Color.YELLOW);
                                break;
                            case R.id.black:
                                paintView.setColor(Color.BLACK);
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        eraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.enableEraser();
            }
        });

        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.undo();
            }
        });

        redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.redo();
            }
        });

        moreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        saveBitmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.saveBitmap(getApplicationContext());
            }
        });

        openInStudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                PaintStudio paintStudio = new PaintStudio(LectureStudio.this, LectureStudio.this, getParent(), )
            }
        });

    }


    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    //TIMER CODE
    private void resetTimer() {
        timeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();

    }

    //java.lang.NullPointerException: Attempt to invoke virtual method 'void android.os.CountDownTimer.cancel()' on a null object reference
    private void pauseTimer() {
        countDownTimer.cancel();
        timerRunning = false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                mediaRecorder.reset();
                stopScreenRecord();
            }
        }.start();

        timerRunning = true;
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        textViewCountDown.setText(timeLeftFormatted);
    }


    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // Create an instance of Camera
            mCamera = getCameraInstance();
            // Create our Preview view and set it as the content of our activity.
            mPreview = new CameraPreview(this, mCamera);
            preview.addView(mPreview);
            Toast.makeText(LectureStudio.this, "Camera exception 2", Toast.LENGTH_SHORT).show();

            return true;
        } else {
            Toast.makeText(getApplicationContext(), "Couldn't find a camera", Toast.LENGTH_SHORT).show();
            // no camera on this device
            return false;
        }
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    private android.hardware.Camera openFrontFacingCameraGingerbread() {
        int cameraCount = 0;
        android.hardware.Camera camera = null;
        android.hardware.Camera.CameraInfo cameraInfo = new android.hardware.Camera.CameraInfo();
        cameraCount = android.hardware.Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            android.hardware.Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    camera = android.hardware.Camera.open(camIdx);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }

        return camera;
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public android.hardware.Camera getCameraInstance() {
        android.hardware.Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            Toast.makeText(LectureStudio.this, "Camera exception", Toast.LENGTH_SHORT).show();
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        paintView.saveBitmap(getApplicationContext());
        String illustrationName = PaintSaveViewUtil.getIllustrationName();
        Uri uri = PaintSaveViewUtil.getUri();

        File savedLecture = new File(FilingSystem.Companion.getPendingLectures(), fileName + ".pl");
        String toBeSaved = sourceDocUri + "_-_" + null + "_-_" + uri.toString() + "_-_" + fileName;
        try {
            FileOutputStream fos = new FileOutputStream(savedLecture);
            fos.write((toBeSaved).getBytes());
            fos.close();
            Toast.makeText(LectureStudio.this, "Progress saved!", Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "File not found.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Storage issue blocked your save.", Toast.LENGTH_SHORT).show();
        }
    }


//    private void compressor1(String vidPath, String compressionPath) {
//        Log.d("doFileUpload ", vidPath);
//        FFmpeg ffmpeg = FFmpeg.getInstance(this);
//        try {
//            //Load the binary
//            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
//                @Override
//                public void onStart() {
//                }
//
//                @Override
//                public void onFailure() {
//                }
//
//                @Override
//                public void onSuccess() {
//                }
//
//                @Override
//                public void onFinish() {
//
//                }
//            });
//        } catch (
//                FFmpegNotSupportedException e) {
//            // Handle if FFmpeg is not supported by device
//        }
//        try {
////             to execute "ffmpeg -version" command you just need to pass "version"
//            String[] commandArray;
//            commandArray = new String[]{"-y", "-i", vidPath, "-s", "720x480", "-r", "25", "-vcodec", "mpeg4", "-b:v", "300k", "-b:a", "48000", "ac", "2", "-ar", "22050", compressionPath};
//            final ProgressDialog dialog = new ProgressDialog(LectureStudio.this);
//            ffmpeg.execute(commandArray, new ExecuteBinaryResponseHandler() {
//                @Override
//                public void onStart() {
//                    Log.e("FFmpeg", "onStart");
//                    dialog.setMessage("Compressing... please wait");
//                    dialog.show();
//                }
//
//                @Override
//                public void onProgress(String message) {
//                    Log.e("FFmpeg onProgress? ", message);
//                }
//
//                @Override
//                public void onFailure(String message) {
//                    Log.e("FFmpeg onFailure? ", message);
//                }
//
//                @Override
//                public void onSuccess(String message) {
//                    Log.e("FFmpeg onSuccess? ", message);
//                }
//
//                @Override
//                public void onFinish() {
//                    Log.e("FFmpeg", "onFinish");
//                    if (dialog.isShowing())
//                        dialog.dismiss();
////                    playVideoOnVideoView(Uri.parse(compressionPath));
////                    isCompressed = true;
////                    count = count + 1;
//                }
//            });
//        } catch (FFmpegCommandAlreadyRunningException e) {
//            e.printStackTrace();
//            // Handle if FFmpeg is already running
//        }
//    }


    private void compressor(String vidPath, String compressionPath,
                            final ProgressBar progressBar, final TextView progress,
                            final RelativeLayout progressRel) {
        Toast.makeText(this, "Compression started!", Toast.LENGTH_SHORT).show();
        VideoCompressor.start(vidPath, compressionPath, new CompressionListener() {
            @Override
            public void onStart() {
                progressRel.setVisibility(View.VISIBLE);
                Toast.makeText(LectureStudio.this, "Compressing file...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess() {
                progressRel.setVisibility(View.GONE);
                Intent intent = new Intent(LectureStudio.this, VideoPlayer.class);
                intent.putExtra("videoUri", compressedVideo);
                startActivity(intent);
                Toast.makeText(LectureStudio.this, "Successful compression!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NotNull String s) {

            }

            @Override
            public void onProgress(final float v) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        progress.setText(v + "%");
                        progressBar.setProgress((int) v);
                    }
                });
            }

            @Override
            public void onCancelled() {

            }
        });
    }

    public String[] readFile(File file) {
        String[] textArray = new String[0];
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                textArray = line.split("_-_");
            }
            br.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(LectureStudio.this, "File not found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textArray;
    }
}
