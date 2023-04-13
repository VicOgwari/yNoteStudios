package com.midland.ynote.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.midland.ynote.R;
import com.midland.ynote.Utilities.Paint.PaintView;

public class PaintStudio extends Dialog {

    private PaintView paintView;
    private static int DISPLAY_WIDTH;
    private static int DISPLAY_HEIGHT;
    private int mScreenDensity;
    private TextView showWidth;
    private int paintWidth;
    private AlertDialog dialog;
    private int PICK_IMAGE = 99;
    private Context con;
    private Activity a;
    private Bitmap bitmap;

    public PaintStudio(@NonNull Context context, Context con, Activity a, Bitmap bitmap) {
        super(context);
        this.con = con;
        this.a = a;
        this.bitmap = bitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_studio);
    }

    private void initView() {

        //SCREEN RECORDER

        DisplayMetrics matrix = new DisplayMetrics();
        a.getWindowManager().getDefaultDisplay().getMetrics(matrix);

        mScreenDensity = matrix.densityDpi;

        //Get Screen
        DISPLAY_HEIGHT = matrix.heightPixels;
        DISPLAY_WIDTH = matrix.widthPixels;


        View dialogView = getLayoutInflater().inflate(R.layout.dialog_width_set, null);
        showWidth = dialogView.findViewById(R.id.textView1);
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
                showWidth.setText("Current Width：" + (progress + 1));
                paintWidth = progress + 1;
            }
        });
        paintView = findViewById(R.id.paintView);
        dialog = new AlertDialog.Builder(con).setIcon(android.R.drawable.ic_dialog_info).setTitle("Set the size of your Pen").setView(dialogView).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                paintView.setPaintWidth(paintWidth);
            }

        }).setNegativeButton("Cancel", null).create();

        ImageButton brushType = findViewById(R.id.brushType);
        ImageButton brushSize = findViewById(R.id.brushSize);
        final ImageButton brushColor = findViewById(R.id.brushColor);
        ImageButton eraser = findViewById(R.id.eraser);
        ImageButton undo = findViewById(R.id.undo);
        ImageButton redo = findViewById(R.id.redo);
        ImageButton moreOptions = findViewById(R.id.otherOptions);
        ImageButton saveBitmap = findViewById(R.id.saveBitmap);


        brushType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(con, brushColor);
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
                PopupMenu popupMenu = new PopupMenu(con, brushColor);
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
                paintView.saveBitmap(con);
            }
        });

        if (bitmap != null){
            paintView.setImage(bitmap, 2500, 2500);
        }

    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        a.startActivityForResult(intent, PICK_IMAGE);
    }


}