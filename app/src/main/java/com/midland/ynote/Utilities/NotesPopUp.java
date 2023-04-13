package com.midland.ynote.Utilities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.midland.ynote.Adapters.PointsAdapter;
import com.midland.ynote.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class NotesPopUp extends Dialog {

    public Activity a;
    public Context c;
    public Dialog d;
    RecyclerView pointAbsorber;
    ImageButton addPoint, attachPoint;
    EditText addContent;
    String docUriString, lectureTitle, flag;
    TextView point0, point1, point2, point3, point4, point5, point6, point7, point8, point9, point10, docUri;
    TextView point11, point12, point13, point14, point15, point16, point17, point18, point19, point20, point21;
    PointsAdapter pointsAdapter;
    ArrayList<String> points;
    Cursor cursor;


    public NotesPopUp(@NonNull Context context, Activity a, Context c, String docUriString, String lectureTitle, String flag) {
        super(context);
        this.a = a;
        this.c = c;
        this.docUriString = docUriString;
        this.lectureTitle = lectureTitle;
        this.flag = flag;
    }

    public void initViewSnActions() {
        pointAbsorber = findViewById(R.id.pointsRV);
        points = new ArrayList<>();
        addPoint = findViewById(R.id.addPoint);
//        attachPoint = findViewById(R.id.attachPoint);
        addContent = findViewById(R.id.addContent);

        docUri = findViewById(R.id.docUri);
        point0 = findViewById(R.id.point0);
        point1 = findViewById(R.id.point1);
        point2 = findViewById(R.id.point2);
        point3 = findViewById(R.id.point3);
        point4 = findViewById(R.id.point4);
        point5 = findViewById(R.id.point5);
        point6 = findViewById(R.id.point6);
        point7 = findViewById(R.id.point7);
        point8 = findViewById(R.id.point8);
        point9 = findViewById(R.id.point9);
        point10 = findViewById(R.id.point10);
        point11 = findViewById(R.id.point11);
        point12 = findViewById(R.id.point12);
        point13 = findViewById(R.id.point13);
        point14 = findViewById(R.id.point14);
        point15 = findViewById(R.id.point15);
        point16 = findViewById(R.id.point16);
        point17 = findViewById(R.id.point17);
        point18 = findViewById(R.id.point18);
        point19 = findViewById(R.id.point19);
        point20 = findViewById(R.id.point20);
        point21 = findViewById(R.id.point21);

        docUri.setText(docUriString);
        pointsAdapter = new PointsAdapter(c.getApplicationContext(), points,
                null, null, null, "Points", NotesPopUp.this);
        pointAbsorber.setLayoutManager(new LinearLayoutManager(c.getApplicationContext(), RecyclerView.VERTICAL, false));
        pointAbsorber.setAdapter(pointsAdapter);


        addPoint.setOnClickListener(v -> initFileDetailCapture(points));

//        attachPoint.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(c.getApplicationContext(), docUri.getText().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_notes_pop_up);
        initViewSnActions();
        docUri.setVisibility(View.GONE);

        if (flag.equals("LectureStudio")) {
            try {
                docUri.setText(docUriString);
                point0.setText(readFile(lectureTitle)[1]);
                points.add(point0.getText().toString());
                point1.setText(readFile(lectureTitle)[2]);
                points.add( point1.getText().toString());
                point2.setText(readFile(lectureTitle)[3]);
                points.add(point2.getText().toString());
                point3.setText(readFile(lectureTitle)[4]);
                points.add(point3.getText().toString());
                point4.setText(readFile(lectureTitle)[5]);
                points.add(point4.getText().toString());
                point5.setText(readFile(lectureTitle)[6]);
                points.add(point5.getText().toString());
                point6.setText(readFile(lectureTitle)[7]);
                points.add(point6.getText().toString());
                point7.setText(readFile(lectureTitle)[8]);
                points.add(point7.getText().toString());
                point8.setText(readFile(lectureTitle)[9]);
                points.add(point8.getText().toString());
                point9.setText(readFile(lectureTitle)[10]);
                points.add(point9.getText().toString());
                point10.setText(readFile(lectureTitle)[11]);
                points.add(point10.getText().toString());
                point11.setText(readFile(lectureTitle)[12]);
                points.add(point11.getText().toString());
                point12.setText(readFile(lectureTitle)[13]);
                points.add(point12.getText().toString());
                point13.setText(readFile(lectureTitle)[14]);
                points.add(point13.getText().toString());
                point14.setText(readFile(lectureTitle)[15]);
                points.add(point14.getText().toString());
                point15.setText(readFile(lectureTitle)[16]);
                points.add(point15.getText().toString());
                point16.setText(readFile(lectureTitle)[17]);
                points.add(point16.getText().toString());
                point17.setText(readFile(lectureTitle)[18]);
                points.add(point17.getText().toString());
                point18.setText(readFile(lectureTitle)[19]);
                points.add(point18.getText().toString());
                point19.setText(readFile(lectureTitle)[20]);
                points.add(point19.getText().toString());
                point20.setText(readFile(lectureTitle)[21]);
                points.add(point20.getText().toString());
                point21.setText(readFile(lectureTitle)[22]);
                points.add(point21.getText().toString());

            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } else if (flag.equals("PdfReadMode")) {
            Collections.addAll(points, readFile(lectureTitle));
        }

        pointsAdapter.notifyDataSetChanged();
    }


    //SAVING LECTURE ALGORITHM
    public void initFileDetailCapture(ArrayList<String> points) {
        if (readFile(lectureTitle) == null) {
            writeFile();
        }
        if (flag.equals("LectureStudio")) {
            if (point0.getText().toString().isEmpty()) {
                point0.setText(addContent.getText().toString().trim());
            } else if (!(point0.getText().toString().isEmpty()) && point1.getText().toString().isEmpty()) {
                point1.setText(addContent.getText().toString().trim());
            } else if (!(point0.getText().toString().isEmpty() && point1.getText().toString().isEmpty()) && point2.getText().toString().isEmpty()) {
                point2.setText(addContent.getText().toString().trim());
            } else if (!(point0.getText().toString().isEmpty() && point1.getText().toString().isEmpty() && point2.getText().toString().isEmpty())
                    && point3.getText().toString().isEmpty()) {
                point3.setText(addContent.getText().toString().trim());
            } else if (!(point0.getText().toString().isEmpty() && point1.getText().toString().isEmpty() && point1.getText().toString().isEmpty()
                    && point3.getText().toString().isEmpty()) && point4.getText().toString().isEmpty()) {
                point4.setText(addContent.getText().toString().trim());
            } else if (!(point0.getText().toString().isEmpty() && point1.getText().toString().isEmpty() && point2.getText().toString().isEmpty()
                    && point3.getText().toString().isEmpty() && point4.getText().toString().isEmpty()) && point5.getText().toString().isEmpty()) {
                point5.setText(addContent.getText().toString().trim());
            } else if (!(point0.getText().toString().isEmpty() && point1.getText().toString().isEmpty() && point21.getText().toString().isEmpty()
                    && point3.getText().toString().isEmpty() && point4.getText().toString().isEmpty() && point5.getText().toString().isEmpty())
                    && point6.getText().toString().isEmpty()) {
                point6.setText(addContent.getText().toString().trim());
            } else if (!(point0.getText().toString().isEmpty() && point1.getText().toString().isEmpty() && point21.getText().toString().isEmpty()
                    && point3.getText().toString().isEmpty() && point4.getText().toString().isEmpty() && point5.getText().toString().isEmpty()
                    && point6.getText().toString().isEmpty()) && point7.getText().toString().isEmpty()) {
                point7.setText(addContent.getText().toString().trim());
            } else if (!(point0.getText().toString().isEmpty() && point1.getText().toString().isEmpty() && point21.getText().toString().isEmpty()
                    && point3.getText().toString().isEmpty() && point4.getText().toString().isEmpty() && point5.getText().toString().isEmpty()
                    && point6.getText().toString().isEmpty() && point7.getText().toString().isEmpty()) && point8.getText().toString().isEmpty()) {
                point8.setText(addContent.getText().toString().trim());
            } else if (!(point0.getText().toString().isEmpty() && point1.getText().toString().isEmpty() && point21.getText().toString().isEmpty()
                    && point3.getText().toString().isEmpty() && point4.getText().toString().isEmpty() && point5.getText().toString().isEmpty()
                    && point6.getText().toString().isEmpty() && point7.getText().toString().isEmpty() && point8.getText().toString().isEmpty())
                    && point9.getText().toString().isEmpty()) {
                point9.setText(addContent.getText().toString().trim());
            } else if (!(point0.getText().toString().isEmpty() && point1.getText().toString().isEmpty() && point21.getText().toString().isEmpty()
                    && point3.getText().toString().isEmpty() && point4.getText().toString().isEmpty() && point5.getText().toString().isEmpty()
                    && point6.getText().toString().isEmpty() && point7.getText().toString().isEmpty() && point8.getText().toString().isEmpty()
                    && point9.getText().toString().isEmpty()) && point10.getText().toString().isEmpty()) {
                point10.setText(addContent.getText().toString().trim());
            } else if (!(point0.getText().toString().isEmpty() && point1.getText().toString().isEmpty() && point21.getText().toString().isEmpty()
                    && point3.getText().toString().isEmpty() && point4.getText().toString().isEmpty() && point5.getText().toString().isEmpty()
                    && point6.getText().toString().isEmpty() && point7.getText().toString().isEmpty() && point8.getText().toString().isEmpty()
                    && point9.getText().toString().isEmpty() && point10.getText().toString().isEmpty()) && point11.getText().toString().isEmpty()) {
                point11.setText(addContent.getText().toString().trim());
            } else if (!(point0.getText().toString().isEmpty() && point1.getText().toString().isEmpty() && point21.getText().toString().isEmpty()
                    && point3.getText().toString().isEmpty() && point4.getText().toString().isEmpty() && point5.getText().toString().isEmpty()
                    && point6.getText().toString().isEmpty() && point7.getText().toString().isEmpty() && point8.getText().toString().isEmpty()
                    && point9.getText().toString().isEmpty() && point10.getText().toString().isEmpty() && point11.getText().toString().isEmpty())
                    && point12.getText().toString().isEmpty()) {
                point12.setText(addContent.getText().toString().trim());
            } else if (!(point0.getText().toString().isEmpty() && point1.getText().toString().isEmpty() && point21.getText().toString().isEmpty()
                    && point3.getText().toString().isEmpty() && point4.getText().toString().isEmpty() && point5.getText().toString().isEmpty()
                    && point6.getText().toString().isEmpty() && point7.getText().toString().isEmpty() && point8.getText().toString().isEmpty()
                    && point9.getText().toString().isEmpty() && point10.getText().toString().isEmpty() && point11.getText().toString().isEmpty()
                    && point12.getText().toString().isEmpty()) && point13.getText().toString().isEmpty()) {
                point13.setText(addContent.getText().toString().trim());
            } else if (!(point0.getText().toString().isEmpty() && point1.getText().toString().isEmpty() && point21.getText().toString().isEmpty()
                    && point3.getText().toString().isEmpty() && point4.getText().toString().isEmpty() && point5.getText().toString().isEmpty()
                    && point6.getText().toString().isEmpty() && point7.getText().toString().isEmpty() && point8.getText().toString().isEmpty()
                    && point9.getText().toString().isEmpty() && point10.getText().toString().isEmpty() && point11.getText().toString().isEmpty()
                    && point12.getText().toString().isEmpty() && point13.getText().toString().isEmpty()) && point14.getText().toString().isEmpty()) {
                point14.setText(addContent.getText().toString().trim());
            } else if (!(point0.getText().toString().isEmpty() && point1.getText().toString().isEmpty() && point21.getText().toString().isEmpty()
                    && point3.getText().toString().isEmpty() && point4.getText().toString().isEmpty() && point5.getText().toString().isEmpty()
                    && point6.getText().toString().isEmpty() && point7.getText().toString().isEmpty() && point8.getText().toString().isEmpty()
                    && point9.getText().toString().isEmpty() && point10.getText().toString().isEmpty() && point11.getText().toString().isEmpty()
                    && point12.getText().toString().isEmpty() && point13.getText().toString().isEmpty() && point14.getText().toString().isEmpty())
                    && point15.getText().toString().isEmpty()) {
                point15.setText(addContent.getText().toString().trim());
            } else if (!(point0.getText().toString().isEmpty() && point1.getText().toString().isEmpty() && point21.getText().toString().isEmpty()
                    && point3.getText().toString().isEmpty() && point4.getText().toString().isEmpty() && point5.getText().toString().isEmpty()
                    && point6.getText().toString().isEmpty() && point7.getText().toString().isEmpty() && point8.getText().toString().isEmpty()
                    && point9.getText().toString().isEmpty() && point10.getText().toString().isEmpty() && point11.getText().toString().isEmpty()
                    && point12.getText().toString().isEmpty() && point13.getText().toString().isEmpty() && point14.getText().toString().isEmpty()
                    && point15.getText().toString().isEmpty()) && point16.getText().toString().isEmpty()) {
                point16.setText(addContent.getText().toString().trim());
            } else if (!(point0.getText().toString().isEmpty() && point1.getText().toString().isEmpty() && point21.getText().toString().isEmpty()
                    && point3.getText().toString().isEmpty() && point4.getText().toString().isEmpty() && point5.getText().toString().isEmpty()
                    && point6.getText().toString().isEmpty() && point7.getText().toString().isEmpty() && point8.getText().toString().isEmpty()
                    && point9.getText().toString().isEmpty() && point10.getText().toString().isEmpty() && point11.getText().toString().isEmpty()
                    && point12.getText().toString().isEmpty() && point13.getText().toString().isEmpty() && point14.getText().toString().isEmpty()
                    && point15.getText().toString().isEmpty() && point16.getText().toString().isEmpty()) && point17.getText().toString().isEmpty()) {
                point17.setText(addContent.getText().toString().trim());
            } else if (!(point0.getText().toString().isEmpty() && point1.getText().toString().isEmpty() && point21.getText().toString().isEmpty()
                    && point3.getText().toString().isEmpty() && point4.getText().toString().isEmpty() && point5.getText().toString().isEmpty()
                    && point6.getText().toString().isEmpty() && point7.getText().toString().isEmpty() && point8.getText().toString().isEmpty()
                    && point9.getText().toString().isEmpty() && point10.getText().toString().isEmpty() && point11.getText().toString().isEmpty()
                    && point12.getText().toString().isEmpty() && point13.getText().toString().isEmpty() && point14.getText().toString().isEmpty()
                    && point15.getText().toString().isEmpty() && point16.getText().toString().isEmpty() && point7.getText().toString().isEmpty())
                    && point18.getText().toString().isEmpty()) {
                point18.setText(addContent.getText().toString().trim());
            } else if (!(point0.getText().toString().isEmpty() && point1.getText().toString().isEmpty() && point21.getText().toString().isEmpty()
                    && point3.getText().toString().isEmpty() && point4.getText().toString().isEmpty() && point5.getText().toString().isEmpty()
                    && point6.getText().toString().isEmpty() && point7.getText().toString().isEmpty() && point8.getText().toString().isEmpty()
                    && point9.getText().toString().isEmpty() && point10.getText().toString().isEmpty() && point11.getText().toString().isEmpty()
                    && point12.getText().toString().isEmpty() && point13.getText().toString().isEmpty() && point14.getText().toString().isEmpty()
                    && point15.getText().toString().isEmpty() && point16.getText().toString().isEmpty() && point7.getText().toString().isEmpty()
                    && point18.getText().toString().isEmpty()) && point19.getText().toString().isEmpty()) {
                point19.setText(addContent.getText().toString().trim());
            } else if (!(point0.getText().toString().isEmpty() && point1.getText().toString().isEmpty() && point21.getText().toString().isEmpty()
                    && point3.getText().toString().isEmpty() && point4.getText().toString().isEmpty() && point5.getText().toString().isEmpty()
                    && point6.getText().toString().isEmpty() && point7.getText().toString().isEmpty() && point8.getText().toString().isEmpty()
                    && point9.getText().toString().isEmpty() && point10.getText().toString().isEmpty() && point11.getText().toString().isEmpty()
                    && point12.getText().toString().isEmpty() && point13.getText().toString().isEmpty() && point14.getText().toString().isEmpty()
                    && point15.getText().toString().isEmpty() && point16.getText().toString().isEmpty() && point7.getText().toString().isEmpty()
                    && point18.getText().toString().isEmpty() && point19.getText().toString().isEmpty()) && point20.getText().toString().isEmpty()) {
                point20.setText(addContent.getText().toString().trim());
            } else if (!(point0.getText().toString().isEmpty() && point1.getText().toString().isEmpty() && point21.getText().toString().isEmpty()
                    && point3.getText().toString().isEmpty() && point4.getText().toString().isEmpty() && point5.getText().toString().isEmpty()
                    && point6.getText().toString().isEmpty() && point7.getText().toString().isEmpty() && point8.getText().toString().isEmpty()
                    && point9.getText().toString().isEmpty() && point10.getText().toString().isEmpty() && point11.getText().toString().isEmpty()
                    && point12.getText().toString().isEmpty() && point13.getText().toString().isEmpty() && point14.getText().toString().isEmpty()
                    && point15.getText().toString().isEmpty() && point16.getText().toString().isEmpty() && point7.getText().toString().isEmpty()
                    && point18.getText().toString().isEmpty() && point19.getText().toString().isEmpty() && point20.getText().toString().isEmpty())
                    && point21.getText().toString().isEmpty()) {
                point21.setText(addContent.getText().toString().trim());
            } else if (!(point21.getText().toString().isEmpty() && point20.getText().toString().isEmpty() && point19.getText().toString().isEmpty()
                    && point18.getText().toString().isEmpty() && point17.getText().toString().isEmpty() && point16.getText().toString().isEmpty()
                    && point15.getText().toString().isEmpty() && point14.getText().toString().isEmpty() && point13.getText().toString().isEmpty()
                    && point12.getText().toString().isEmpty() && point11.getText().toString().isEmpty() && point10.getText().toString().isEmpty()
                    && point9.getText().toString().isEmpty() && point8.getText().toString().isEmpty() && point7.getText().toString().isEmpty()
                    && point6.getText().toString().isEmpty() && point5.getText().toString().isEmpty() && point4.getText().toString().isEmpty()
                    && point3.getText().toString().isEmpty() && point2.getText().toString().isEmpty() && point1.getText().toString().isEmpty()
                    && point0.getText().toString().isEmpty())) {
                Toast.makeText(c.getApplicationContext(), "Too many points", Toast.LENGTH_SHORT).show();
            }
        }

        points.add(addContent.getText().toString());
        pointsAdapter.notifyDataSetChanged();
        addContent.setText("");

    }


    public void writeFile() {
        if (FilingSystem.Companion.isExternalStorageReadWritable() && FilingSystem.Companion.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, c.getApplicationContext())) {
            File savedLecture = null;
            ContextWrapper contextWrapper = new ContextWrapper(getContext());
            if (flag.equals("PdfReadMode")) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    File docDir = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                    savedLecture = new File(docDir, lectureTitle + ".sn");
                } else {
                    savedLecture = new File(FilingSystem.Companion.getDubDocuments(), lectureTitle + ".sn");
                }
            } else if (flag.equals("LectureStudio")) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    File docDir = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                    savedLecture = new File(docDir, lectureTitle + ".pl");
                } else {
                    savedLecture = new File(FilingSystem.Companion.getDubDocuments(), lectureTitle + ".pl");
                }
            }
            try {
                FileOutputStream fos = new FileOutputStream(savedLecture);
                if (flag.equals("PdfReadMode")) {
                    StringBuilder stringBuilder = new StringBuilder();
                    if (readFile(lectureTitle) != null) {
                        for (String s : points) {
                            stringBuilder.append(s).append("_-_");
                        }
                    }
                    fos.write(stringBuilder.toString().getBytes());
                } else if (flag.equals("LectureStudio")) {
                    fos.write((docUri.getText().toString() + "_-_").getBytes());
                    fos.write((point0.getText().toString() + "_-_").getBytes());
                    fos.write((point1.getText().toString() + "_-_").getBytes());
                    fos.write((point2.getText().toString() + "_-_").getBytes());
                    fos.write((point3.getText().toString() + "_-_").getBytes());
                    fos.write((point4.getText().toString() + "_-_").getBytes());
                    fos.write((point5.getText().toString() + "_-_").getBytes());
                    fos.write((point6.getText().toString() + "_-_").getBytes());
                    fos.write((point7.getText().toString() + "_-_").getBytes());
                    fos.write((point8.getText().toString() + "_-_").getBytes());
                    fos.write((point9.getText().toString() + "_-_").getBytes());
                    fos.write((point10.getText().toString() + "_-_").getBytes());
                    fos.write((point11.getText().toString() + "_-_").getBytes());
                    fos.write((point12.getText().toString() + "_-_").getBytes());
                    fos.write((point13.getText().toString() + "_-_").getBytes());
                    fos.write((point14.getText().toString() + "_-_").getBytes());
                    fos.write((point15.getText().toString() + "_-_").getBytes());
                    fos.write((point16.getText().toString() + "_-_").getBytes());
                    fos.write((point17.getText().toString() + "_-_").getBytes());
                    fos.write((point17.getText().toString() + "_-_").getBytes());
                    fos.write((point19.getText().toString() + "_-_").getBytes());
                    fos.write((point20.getText().toString() + "_-_").getBytes());
                    fos.write((point21.getText().toString() + "_-_").getBytes());
                }
                fos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(c.getApplicationContext(), "Something happened. You might lose your points.", Toast.LENGTH_LONG).show();
        }
    }

    public String[] readFile(String fileName) {
        File file = null;
        ContextWrapper contextWrapper = new ContextWrapper(c);

        if (flag.equals("PdfReadMode")) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                File audioBooks = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                file = new File(audioBooks, fileName + ".sn");
            } else {
                file = new File(FilingSystem.Companion.getDubDocuments(), fileName + ".sn");
            }
        } else if (flag.equals("LectureStudio")) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                File audioBooks = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                file = new File(audioBooks, fileName + ".pl");
            } else {
                file = new File(FilingSystem.Companion.getDubDocuments(), fileName + ".pl");
            }
        }
        String[] textArray = new String[0];
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                textArray = line.split("_-_");
            }
            br.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(c.getApplicationContext(), "File not found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textArray;
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        writeFile();
        super.onDetachedFromWindow();
    }


}
