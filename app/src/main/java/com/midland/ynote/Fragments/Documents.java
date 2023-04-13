package com.midland.ynote.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.midland.ynote.Activities.PdfViewerReadMode;
import com.midland.ynote.Adapters.DocumentHistoryAdapter;
import com.midland.ynote.Adapters.LocalDocsAdapter;
import com.midland.ynote.Objects.SourceDocObject;
import com.midland.ynote.R;
import com.midland.ynote.Utilities.FilingSystem;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Documents#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Documents extends Fragment {

    public static final int REQUEST_MULTI_PERMISSION_ID = 1;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int READ_PERM = 55;
    private static final int STORAGE_ACCESS_CODE = 99;
    public ArrayList<SourceDocObject> yNotes;
    RecyclerView gridView, docHistoryRV;
    FrameLayout frameLY;
    PDFView pageCounter;
    Button findDocs;
    RelativeLayout docHistoryRel, allDocsRel;
    public LocalDocsAdapter sourceDocAdapter;
    DocumentHistoryAdapter historyAdapter;
    String selected = "False";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean read = false;

    public Documents() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Documents newInstance(String param1, String param2) {
        Documents fragment = new Documents();
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

//        if (FilingSystem.Companion.checkAndRequestPermission(getActivity())){
//            sourceDocAdapter = new SourceDocAdapter(getContext(), getVideoFileObjects(), "fragment");
//        }
//        sourceDocAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_docs, container, false);

        gridView = v.findViewById(R.id.docRecycler);
        docHistoryRV = v.findViewById(R.id.docHistoryRV);
        pageCounter = v.findViewById(R.id.pageCounter);
        frameLY = v.findViewById(R.id.frameLY);
        docHistoryRel = v.findViewById(R.id.docHistoryRel);
        allDocsRel = v.findViewById(R.id.allDocsRel);
        findDocs = v.findViewById(R.id.findDocs);

        sourceDocAdapter = new LocalDocsAdapter(getContext(), getFileObjects(), "home", pageCounter);
        historyAdapter = new DocumentHistoryAdapter(getContext(), "",
                FilingSystem.Companion.loadHistory(getContext(), "documentHistory"));
        sourceDocAdapter.notifyDataSetChanged();
        historyAdapter.notifyDataSetChanged();
        gridView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        docHistoryRV.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        gridView.setAdapter(sourceDocAdapter);
        docHistoryRV.setAdapter(historyAdapter);
        SearchView searchDocs;
        searchDocs = v.findViewById(R.id.searchDocs);

        searchDocs.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                sourceDocAdapter.getFilter().filter(newText);
                return false;
            }
        });

        findDocs.setOnClickListener(v1 -> {
            Intent intent1 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent1.addCategory(Intent.CATEGORY_OPENABLE);
            intent1.setType("application/*");
            startActivityForResult(intent1, STORAGE_ACCESS_CODE);
        });

        docHistoryRel.setVisibility(View.VISIBLE);
        allDocsRel.setVisibility(View.GONE);


        // Inflate the layout for this fragment
        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (selected.equals("False")) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/*");
            Toast.makeText(getContext(), "Select a document.", Toast.LENGTH_SHORT).show();
            startActivityForResult(intent, STORAGE_ACCESS_CODE);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == STORAGE_ACCESS_CODE) {
            if (data == null){
                getActivity().onBackPressed();
                Intent intent = new Intent();
                intent.putExtra("Nothing selected", "Nothing selected");
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
            if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                selected = "True";
                Uri selectedPdf = data.getData();
                File selectedPdfFile = new File(FilingSystem.Companion.getRealPathFromURI(selectedPdf, getContext()));

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM, yyyy");
                Random r = new Random();
                int endColor = Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256));
                GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.WHITE, endColor});
                int fileSize = Integer.parseInt(String.valueOf(selectedPdfFile.length() / 1024));
                SourceDocObject object = new SourceDocObject(selectedPdfFile.getName(), Uri.fromFile(selectedPdfFile), String.valueOf(fileSize), dateFormat.format(selectedPdfFile.lastModified()), gd);
                ArrayList<SourceDocObject> sourceDocObjects = FilingSystem.Companion.loadHistory(getContext(), "documentHistory");
                if (!sourceDocObjects.contains(object)) {
                    FilingSystem.Companion.saveHistory(sourceDocObjects, getContext());
                }

                Intent intent = new Intent(getContext(), PdfViewerReadMode.class);
                intent.putExtra("selectedDoc", selectedPdf.toString());
                intent.putExtra("selectedDocName", selectedPdfFile.getName());
                intent.putExtra("selectedDocSize", String.valueOf(fileSize));
                Toast.makeText(getContext(), selectedPdfFile.getName(), Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        }
    }

    //    //GETTING ALL IMAGES FROM EXTERNAL STORAGE
//    private ArrayList<SourceDocObject> getFileObjects() {
//        Uri uri = MediaStore.Files.getContentUri("external");
//        String[] projection = {MediaStore.Files.FileColumns.DATA};
//        Cursor c = null;
//        SortedSet<String> dirList = new TreeSet<>();
//        ArrayList<SourceDocObject> sourceDocObjects = new ArrayList<>();
//        String[] directories = null;
//        String orderBy = MediaStore.Images.Media.DATE_TAKEN;
//
//        //USED AS ARGUMENTS FOR CURSOR
//        String pdf = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf");
//        String doc = MimeTypeMap.getSingleton().getMimeTypeFromExtension("doc");
//        String docx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("docx");
//        String xls = MimeTypeMap.getSingleton().getMimeTypeFromExtension("xls");
//        String xlsx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("xlsx");
//        String ppt = MimeTypeMap.getSingleton().getMimeTypeFromExtension("ppt");
//        String pptx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pptx");
//        String txt = MimeTypeMap.getSingleton().getMimeTypeFromExtension("txt");
//        String rtx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("rtx");
//        String rtf = MimeTypeMap.getSingleton().getMimeTypeFromExtension("rtf");
//        String html = MimeTypeMap.getSingleton().getMimeTypeFromExtension("html");
//
//        //Where
//        String where = MediaStore.Files.FileColumns.MIME_TYPE + "=?"
//                + " OR " + MediaStore.Files.FileColumns.MIME_TYPE
//                + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE
//                + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE
//                + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE
//                + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE
//                + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE
//                + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
//                + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?" + " OR "
//                + MediaStore.Files.FileColumns.MIME_TYPE + "=?" + " OR "
//                + MediaStore.Files.FileColumns.MIME_TYPE + "=?";
//
//        //args
//        String[] args = new String[]{pdf};
//
//
//        if (uri != null) {
//            c = getContext().getContentResolver().query(uri, projection, where, args, orderBy);
//        }
//
//        if ((c != null) && (c.moveToFirst())) {
//            do {
//                String tempDir = c.getString(0);
//                tempDir = tempDir.substring(0, tempDir.lastIndexOf("/"));
//                try {
//                    dirList.add(tempDir);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            while (c.moveToNext());
//            directories = new String[dirList.size()];
//            dirList.toArray(directories);
//        }
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM, yyyy");
//        for (int i = 0; i < dirList.size(); i++) {
//            File imageDir = new File(directories[i]);
//            File[] docList = imageDir.listFiles();
//            if (imageDir == null)
//                continue;
//
//            for (File docPath : docList) {
//                try {
//                    if (docPath.isDirectory()) {
//                        docList = docPath.listFiles();
//                    }
//
//                    File docFile = docPath.getAbsoluteFile();
//                    Uri docUri = Uri.fromFile(docFile);
//                    int fileSize = Integer.parseInt(String.valueOf(docFile.length()/1024));
//                    SourceDocObject sourceDocObject = new SourceDocObject(docFile.getName(), Uri.fromFile(docFile),
//                            String.valueOf(fileSize), dateFormat.format(docFile.lastModified()), docUri);
//                    if (sourceDocObject.getName().endsWith("pdf") || sourceDocObject.getName().endsWith("PDF")
//                            || sourceDocObject.getName().endsWith("doc") || sourceDocObject.getName().endsWith("DOC")
//                            || sourceDocObject.getName().endsWith("docx") || sourceDocObject.getName().endsWith("DOCX")
//                            || sourceDocObject.getName().endsWith("ppt") || sourceDocObject.getName().endsWith("PPT")
//                            || sourceDocObject.getName().endsWith("yNote")
//                            || sourceDocObject.getName().endsWith("pptx") || sourceDocObject.getName().endsWith("PPTX")){
//
//                        sourceDocObjects.add(sourceDocObject);
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        return sourceDocObjects;
//    }


    public ArrayList<SourceDocObject> getyNote() {
        ArrayList<SourceDocObject> sourceDocObjects = new ArrayList<>();
        if (FilingSystem.Companion.checkAndRequestPermission(getActivity())) {
            File[] list = FilingSystem.Companion.getInsideDubDocs().listFiles();
            for (File file : list) {

                int fileSize = Integer.parseInt(String.valueOf(file.length() / 1024));
                if (fileSize != 0) {
                    File docFile = file.getAbsoluteFile();
                    Uri docUri = Uri.fromFile(docFile);
                    SourceDocObject sourceDocObject = new SourceDocObject(docFile.getName(), Uri.fromFile(docFile), String.valueOf(fileSize), String.valueOf(docFile.lastModified()), docUri, docFile.lastModified());
                    sourceDocObjects.add(sourceDocObject);
                }
            }
        }

        return sourceDocObjects;
    }

    public ArrayList<SourceDocObject> getVideoFileObjects() {
        ArrayList<SourceDocObject> sourceDocObjects = new ArrayList<>();
        if (FilingSystem.Companion.checkAndRequestPermission(getActivity())) {
//            File[] lecList = FilingSystem.Companion.getInsidePendingLec().listFiles();
            File[] docList = FilingSystem.Companion.getDubDocuments().listFiles();

            if (docList != null) {
                for (File file : docList) {
                    int fileSize = Integer.parseInt(String.valueOf(file.length() / 1024));
                    if (fileSize != 0) {
                        SourceDocObject sourceDocObject = new SourceDocObject(file.getName(), Uri.fromFile(file), String.valueOf(fileSize),
                                String.valueOf(file.lastModified()), Uri.fromFile(file), file.lastModified());
                        sourceDocObjects.add(sourceDocObject);
                    }
                }
            }

//            if (lecList != null) {
//                for (File file : lecList) {
//                    int fileSize = Integer.parseInt(String.valueOf(file.length()/1024));
//                    if (fileSize != 0){
//                        String newName = file.getName().replace("dubLecture", "mp4");
//                        SourceDocObject sourceDocObject = new SourceDocObject(newName, Uri.fromFile(file), String.valueOf(fileSize), String.valueOf(file.lastModified()), Uri.fromFile(file));
//                        sourceDocObjects.add(sourceDocObject);
//                    }
//                }
//            }

        }

        return sourceDocObjects;
    }

//    private boolean checkAndRequestPermission() {
//        int readPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
//        int writePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        List<String> neededPermission = new ArrayList<>();
//        if (readPermission != PackageManager.PERMISSION_GRANTED) {
//            neededPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
//        }
//        if (writePermission != PackageManager.PERMISSION_GRANTED) {
//            neededPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        }
//
//        if (!neededPermission.isEmpty()) {
//            ActivityCompat.requestPermissions(getActivity(), neededPermission.toArray(new String[neededPermission.size()]), REQUEST_MULTI_PERMISSION_ID);
//            return false;
//        }
//        return true;
//    }

    @Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    private ArrayList<String> getPins() {
        SharedPreferences preferences = getContext().getSharedPreferences("docPins", Context.MODE_PRIVATE);
        String json = preferences.getString("docPins", "");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();

        return gson.fromJson(json, type);
    }

    //GETTING ALL IMAGES FROM EXTERNAL STORAGE
    private ArrayList<SourceDocObject> getFileObjects() {
        Uri uri;
        uri = MediaStore.Files.getContentUri("external");
        String[] projection = {MediaStore.Files.FileColumns.DATA};
        Cursor c = null;
        SortedSet<String> dirList = new TreeSet<>();
        ArrayList<SourceDocObject> sourceDocObjects = new ArrayList<>();
        String[] directories = null;
        String orderBy = MediaStore.Images.Media.DATE_TAKEN;

        //USED AS ARGUMENTS FOR CURSOR
        String pdf = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf");
        String doc = MimeTypeMap.getSingleton().getMimeTypeFromExtension("doc");
        String docx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("docx");
        String xls = MimeTypeMap.getSingleton().getMimeTypeFromExtension("xls");
        String xlsx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("xlsx");
        String ppt = MimeTypeMap.getSingleton().getMimeTypeFromExtension("ppt");
        String pptx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pptx");
        String txt = MimeTypeMap.getSingleton().getMimeTypeFromExtension("txt");
        String rtx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("rtx");
        String rtf = MimeTypeMap.getSingleton().getMimeTypeFromExtension("rtf");
        String html = MimeTypeMap.getSingleton().getMimeTypeFromExtension("html");

        //Where
        String where = MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                + " OR " + MediaStore.Files.FileColumns.MIME_TYPE
                + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE
                + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE
                + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE
                + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE
                + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE
                + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?" + " OR "
                + MediaStore.Files.FileColumns.MIME_TYPE + "=?" + " OR "
                + MediaStore.Files.FileColumns.MIME_TYPE + "=?";

        //args
        String[] args = new String[]{pdf};
        c = getContext().getContentResolver().query(uri, projection, where, args, orderBy);

        if (c != null && c.moveToFirst()) {

            do {
                String tempDir = c.getString(0);
                tempDir = tempDir.substring(0, tempDir.lastIndexOf("/"));
                try {
                    dirList.add(tempDir);
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            while (c.moveToNext());
            directories = new String[dirList.size()];
            dirList.toArray(directories);

        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM, yyyy");
        for (int i = 0; i < dirList.size(); i++) {
            File imageDir = new File(directories[i]);
            File[] docList = imageDir.listFiles();

            if (docList != null) {
                for (File docPath : docList) {
                    try {
                        if (docPath.isDirectory()) {
                            docList = docPath.listFiles();
                        }


                        Random r = new Random();
                        int endColor = Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256));
                        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                new int[]{Color.WHITE, endColor});


                        File docFile = docPath.getAbsoluteFile();
                        Uri docUri = Uri.fromFile(docFile);
                        int fileSize = Integer.parseInt(String.valueOf(docFile.length() / 1024));
                        SourceDocObject sourceDocObject = new SourceDocObject(docFile.getName(), Uri.fromFile(docFile),
                                String.valueOf(fileSize), dateFormat.format(docFile.lastModified()), docUri, docFile.lastModified());
                        if (sourceDocObject.getName().endsWith("pdf") || sourceDocObject.getName().endsWith("PDF")
                                || sourceDocObject.getName().endsWith("doc") || sourceDocObject.getName().endsWith("DOC")
                                || sourceDocObject.getName().endsWith("docx") || sourceDocObject.getName().endsWith("DOCX")
                                || sourceDocObject.getName().endsWith("ppt") || sourceDocObject.getName().endsWith("PPT")
                                || sourceDocObject.getName().endsWith("pptx") || sourceDocObject.getName().endsWith("PPTX")
                                || sourceDocObject.getName().endsWith("xls") || sourceDocObject.getName().endsWith("XLS")
                                || sourceDocObject.getName().endsWith("xlsx") || sourceDocObject.getName().endsWith("XLSX")) {
                            sourceDocObject.setGd(gd);

                            sourceDocObjects.add(sourceDocObject);
                        }

                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(getContext(), "Doc list empty!", Toast.LENGTH_SHORT).show();
            }

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sourceDocObjects.sort(Comparator.comparing(SourceDocObject::getDateModified));
        }
        Collections.reverse(sourceDocObjects);

        return sourceDocObjects;
    }

    private ArrayList<SourceDocObject> getPdfFiles(File file) {
        ArrayList<SourceDocObject> pdfs = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM, yyyy");
        File[] files = file.listFiles();
        for (File singleFile : files) {
            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                pdfs.addAll(getPdfFiles(singleFile));
            } else {
                if (singleFile.getName().endsWith("PDF") || singleFile.getName().endsWith("pdf")) {
                    Uri docUri = Uri.fromFile(singleFile);
                    SourceDocObject sourceDocObject = new SourceDocObject(singleFile.getName(), Uri.fromFile(singleFile),
                            String.valueOf(singleFile), dateFormat.format(singleFile.lastModified()), docUri, singleFile.lastModified());
                    pdfs.add(sourceDocObject);
                }
            }
        }
        return pdfs;
    }
}