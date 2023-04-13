package com.midland.ynote.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.midland.ynote.R;
import com.midland.ynote.Utilities.CustomHorizontalScrollView;
import com.midland.ynote.Utilities.CustomScrollView;
import com.midland.ynote.Utilities.FilingSystem;
import com.midland.ynote.Utilities.Paint.PaintFingerPath;
import com.midland.ynote.Utilities.Paint.PaintView;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class Illustration extends Dialog {

    private Uri illustration;
    private PaintView paintView;
    private AlertDialog dialog;
    private TextView shouWidth;
    private int paintWidth;
    private ImageButton brushColor;
    private String fileName;

    public Illustration(@NonNull Context context, Uri illustration, String fileName) {
        super(context);
        this.illustration = illustration;
        this.fileName = fileName;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_illustration);
        paintView = findViewById(R.id.paintView);
//        paintView.paths = new ArrayList<>();
//
//        if (getDrawing() != null){
//            paintView.paths.addAll(getDrawing());
//            paintView.drawSavedPaths();
//        }

        DisplayMetrics metrics = new DisplayMetrics();
        paintView.init(metrics, 2500, 2500);
        initView();

    }


    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


    @NonNull
    @Override
    public Bundle onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    private void initView() {

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
        dialog = new AlertDialog.Builder(getContext()).setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("Set the size of your Pen").setView(dialogView).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                paintView.setPaintWidth(paintWidth);
            }

        }).setNegativeButton("Cancel", null).create();

        if (illustration != null) {
            processBitmap(illustration,
                    paintView);
        }

        ImageButton brushType = findViewById(R.id.brushType);
        ImageButton scrollMode = findViewById(R.id.scrollMode);
        ImageButton brushSize = findViewById(R.id.brushSize);
        brushColor = findViewById(R.id.brushColor);
        ImageButton eraser = findViewById(R.id.eraser);
        ImageButton undo = findViewById(R.id.undo);
        ImageButton google = findViewById(R.id.google);
        ImageButton redo = findViewById(R.id.redo);
        ImageButton moreOptions = findViewById(R.id.otherOptions);
        ImageButton saveBitmap = findViewById(R.id.saveBitmap);
        ImageButton openInStudio = findViewById(R.id.paintStudio);
        final CustomScrollView paintScrollView = findViewById(R.id.paintScrollView);
        final CustomHorizontalScrollView paintHorizontalView = findViewById(R.id.paintHorizontalView);


        scrollMode.setOnClickListener(view -> {
            if (paintScrollView.isEnableScrolling() && paintHorizontalView.isEnableScrolling()) {
                scrollMode.setImageResource(R.drawable.ic_lock);
                paintScrollView.setEnableScrolling(false);
                paintHorizontalView.setEnableScrolling(false);
                Toast.makeText(getContext(), "disabled", Toast.LENGTH_SHORT).show();
            } else {
                scrollMode.setImageResource(R.drawable.ic_lock_open);
                paintScrollView.setEnableScrolling(true);
                paintHorizontalView.setEnableScrolling(true);
                Toast.makeText(getContext(), "enabled", Toast.LENGTH_SHORT).show();

            }
        });

        brushType.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), brushColor);
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
        });

        brushSize.setOnClickListener(v -> dialog.show());


        brushColor.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), brushColor);
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
        });

        eraser.setOnClickListener(v -> paintView.enableEraser());

        undo.setOnClickListener(v -> paintView.undo());

        redo.setOnClickListener(v -> paintView.redo());

        moreOptions.setOnClickListener(v -> {
        });

        saveBitmap.setOnClickListener(v -> paintView.saveBitmap(getContext().getApplicationContext()));

        openInStudio.setOnClickListener(v -> {

        });

    }

    public void processBitmap(Uri illustration, PaintView paintView) {
//        String[] filePath = {MediaStore.Images.Media.DATA};
//        Cursor cursor = getContentResolver().query(illustration, filePath, null, null, null);
//        cursor.moveToFirst();
//        String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
        String imagePath = FilingSystem.Companion.getRealPathFromURI(illustration, getContext());
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        paintView.setImage(bitmap, 2500, 2500);
//        cursor.close();
        Toast.makeText(getContext(), "Bitmap processed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        saveDrawing();
    }


//    private void saveDrawing(){
//        SharedPreferences preferences = getContext().getSharedPreferences(fileName + "_Illustration", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(paintView.paths);
//        editor.putString(fileName + "_Illustration", json);
//        editor.apply();
//    }

    private ArrayList<PaintFingerPath> getDrawing() {
        SharedPreferences preferences = getContext().getSharedPreferences(fileName + "_Illustration", Context.MODE_PRIVATE);
        ArrayList<PaintFingerPath> paths;
        if (preferences.contains(fileName + "_Illustration")){
            String json = preferences.getString(fileName + "_Illustration", "");
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<PaintFingerPath>>(){}.getType();
            paths = gson.fromJson(json, type);
        }else {
            paths = new ArrayList<>();
        }

        return paths;
    }
}