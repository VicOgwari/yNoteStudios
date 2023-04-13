package com.midland.ynote.Utilities.Paint;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.midland.ynote.MainActivity;

import java.util.ArrayList;

public class PaintView extends View {

    public static int BRUSH_SIZE = 5;
    public static final int DEFAULT_COLOR = Color.RED;
    public static final int DEFAULT_BG_COLOR = Color.BLACK;
    private static final float TOUCH_TOLERANCE = 4;
    private float mX, mY;
    private Path mPath;
    private Paint mPaint;
    private ArrayList<PaintFingerPath> paths = new ArrayList<>();
    private ArrayList<PaintFingerPath> removedPaths = new ArrayList<>();
    private ArrayList<Bitmap> currentMaps = new ArrayList<>();
    private Bitmap image, originalImage;
    PaintFingerPath btmView;
    private int currentColor;
    private int backgroundColor = DEFAULT_BG_COLOR;
    private int strokeWidth;
    private boolean emboss;
    private boolean blur;
    private MaskFilter mEmboss;
    private MaskFilter mBlur;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    private int leftImage = 50, topImage = 50;

    private float refX, refY;

    public PaintView(Context context) {
        this(context, null);
    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(DEFAULT_COLOR);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xff);
        mEmboss = new EmbossMaskFilter(new float[]{1, 1, 1}, 0.4f, 6, 3.5f);
        mBlur = new BlurMaskFilter(5, BlurMaskFilter.Blur.NORMAL);
    }

    public void init(DisplayMetrics metrics, int h, int w) {
//        int height = (int) (metrics.widthPixels * 2.8);
//        int width = (int) (metrics.widthPixels * 4);
        mBitmap =  Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        currentColor = DEFAULT_COLOR;
        strokeWidth = BRUSH_SIZE;
    }

    public void normal() {
        emboss = false;
        blur = false;
    }

    public void emboss() {
        emboss = true;
        blur = false;
    }

    public void blur() {
        emboss = false;
        blur = true;
    }

    public void clear() {
        backgroundColor = DEFAULT_BG_COLOR;
        paths.clear();
        normal();
        invalidate();
    }

    public void undo() {

        if (paths.size() > 0){
            removedPaths.add(paths.get(paths.size() - 1));
            paths.remove(paths.size() - 1);
        }else {
            mBitmap = Bitmap.createBitmap(2000, 2000, Bitmap.Config.ARGB_8888);
        }
        try {
            mCanvas = new Canvas(mBitmap);
            invalidate();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getContext(), "Something's up!", Toast.LENGTH_SHORT).show();
            Toast.makeText(getContext(), "Try using a different title.", Toast.LENGTH_LONG).show();
            getContext().startActivity(new Intent(getContext(), MainActivity.class));
        }

    }

    public void redo() {
        if (removedPaths.size() > 0){
            paths.add(removedPaths.get(removedPaths.size() - 1));
        }
    }

    public void enableEraser(){
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    public void disableEraser(){
        mPaint.setXfermode(null);
        mPaint.setShader(null);
        mPaint.setMaskFilter(null);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        try {
            mCanvas.drawColor(backgroundColor);
            if (image != null){
                mCanvas.drawBitmap(image, leftImage, topImage, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (PaintFingerPath fp : paths) {
            mPaint.setColor(fp.color);
            mPaint.setStrokeWidth(fp.strokeWidth);
            mPaint.setMaskFilter(null);
            if (fp.emboss) mPaint.setMaskFilter(mEmboss);
            else if (fp.blur) mPaint.setMaskFilter(mBlur);
            mCanvas.drawPath(fp.path, mPaint);
        }
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.restore();
    }

    private void touchStart(float x, float y) {
        mPath = new Path();
        PaintFingerPath fp = new PaintFingerPath(currentColor, emboss, blur, strokeWidth, mPath);
        paths.add(fp);
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touchUp() {
        mPath.lineTo(mX, mY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                refX = x;
                refY = y;
                touchStart(x, y);
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                float nX = event.getX();
                float nY = event.getY();
                touchMove(x, y);
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
        }
        return true;
    }

    public void setBackgroundClr(){
        switch (backgroundColor){
            case Color.BLACK:
                backgroundColor = Color.WHITE;
                break;
            case Color.WHITE:
                backgroundColor = Color.YELLOW;
                break;
            case Color.YELLOW:
                backgroundColor = Color.DKGRAY;
                break;
            case Color.DKGRAY:
                backgroundColor = Color.CYAN;
                break;
            case Color.CYAN:
                backgroundColor = Color.BLACK;
                break;
        }
        mCanvas.drawColor(backgroundColor);
    }

    public void setColor(int color) {
        currentColor = color;
        mPaint.setColor(currentColor);
    }


    public void setPaintWidth(int paintWidth) {
        strokeWidth = paintWidth;
        mPaint.setStrokeWidth(strokeWidth);
    }

    public void setImage(Bitmap bitmap, int w, int h) {
        image = Bitmap.createScaledBitmap(bitmap, (int) (w/1.5), (int) (h/1.5), true);
        invalidate();
    }

    public void saveBitmap(Context c) {
        if (!PaintSaveViewUtil.saveScreen(mBitmap, c)) {
            Toast.makeText(getContext(), "Save drawing fail. Please check your SD card", Toast.LENGTH_SHORT).show();
        }

    }
}

