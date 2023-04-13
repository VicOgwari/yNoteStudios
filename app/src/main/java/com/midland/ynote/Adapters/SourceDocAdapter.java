package com.midland.ynote.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.midland.ynote.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.midland.ynote.Activities.DocumentMetaData;
import com.midland.ynote.Activities.LectureStudio2;
import com.midland.ynote.Activities.PdfViewerReadMode;
import com.midland.ynote.Activities.PhotoDoc;
import com.midland.ynote.Activities.SourceBitmapList;
import com.midland.ynote.Objects.PictorialObject;
import com.midland.ynote.Objects.SourceDocObject;
import com.midland.ynote.Utilities.FilingSystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SourceDocAdapter extends BaseAdapter {

    Context c;
    ArrayList<SourceDocObject> sourceDocObjects;
    String flag, school, dept;
    SourceDocObject sourceDocObject;
    PDFView pageCounter;

    public SourceDocAdapter(Context c, ArrayList<SourceDocObject> sourceDocObjects, String flag) {
        this.c = c;
        this.sourceDocObjects = sourceDocObjects;
        this.flag = flag;
    }


    public SourceDocAdapter(Context c, ArrayList<SourceDocObject> sourceDocObjects, String flag, String school, String dept) {
        this.c = c;
        this.sourceDocObjects = sourceDocObjects;
        this.flag = flag;
        this.school = school;
        this.dept = dept;
    }

    public SourceDocAdapter(Context c, ArrayList<SourceDocObject> sourceDocObjects, String flag, PDFView pageCounter) {
        this.c = c;
        this.sourceDocObjects = sourceDocObjects;
        this.flag = flag;
        this.pageCounter = pageCounter;
    }

    @Override
    public int getCount() {
        return sourceDocObjects.size();
    }

    @Override
    public Object getItem(int position) {
        return sourceDocObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(c).inflate(R.layout.doc_object, parent, false);
        }

        sourceDocObject = (SourceDocObject)this.getItem(position);
        final TextView docName = convertView.findViewById(R.id.bitmapDescription);
        TextView docDate = convertView.findViewById(R.id.docDate);
        TextView uriStore = convertView.findViewById(R.id.uriStore);
        final TextView docSize = convertView.findViewById(R.id.docSize);
        ImageView docImage = convertView.findViewById(R.id.picBitmap);
        RelativeLayout relView = convertView.findViewById(R.id.relFeed);
        final Button doc_options = convertView.findViewById(R.id.doc_options);

        relView.setBackground(sourceDocObject.getGd());
        docName.setText(sourceDocObject.getName());
        docDate.setText(sourceDocObject.getFileDate());
        uriStore.setText(sourceDocObject.getDocUri().toString());
        if (Integer.parseInt(sourceDocObject.getFileSize()) < 1024){
            docSize.setText(sourceDocObject.getFileSize() + "\nKBz");
        }else {
            docSize.setText(Integer.parseInt(sourceDocObject.getFileSize()) / 1024 + "\nMBz");
        }
        final Uri uri = sourceDocObject.getDocUri();

        if (!flag.equals("home")){
            doc_options.setVisibility(View.GONE);
        }
        doc_options.setOnClickListener(v -> {
            if (flag.equals("home")){
                PopupMenu homeDocMenu = new PopupMenu(c, doc_options);
                homeDocMenu.getMenuInflater().inflate(R.menu.home_doc_menu, homeDocMenu.getMenu());
                homeDocMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.homeOpen:
                            Intent intent1 = new Intent(c.getApplicationContext(), PdfViewerReadMode.class);
                            intent1.putExtra("selectedDoc", uri.toString());
                            intent1.putExtra("selectedDocName", docName.getText().toString());
                            c.startActivity(intent1);
                            break;


                        case R.id.homePublish:
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                            if (user != null) {
//
//                            }else {
//                                LogInSignUp logInSignUp = new LogInSignUp(parent.getContext());
//                                logInSignUp.show();
//                            }
                            Intent intent = new Intent(c.getApplicationContext(), DocumentMetaData.class);
                            intent.putExtra("flag", "home");
                            intent.putExtra("selectedDocUri", uri.toString());
                            intent.putExtra("selectedDocName", docName.getText().toString());
                            intent.putExtra("selectedDocSize", docSize.getText().toString());

                            break;
                    }
                    return false;
                });

                homeDocMenu.show();
            }
        });

        if (sourceDocObject.getName().contains(".doc") || sourceDocObject.getName().contains(".DOC") || sourceDocObject.getName().contains(".docx") || sourceDocObject.getName().contains(".DOCX")){
            docImage.setImageResource(R.drawable.microsoft_word);
        }else

        if (sourceDocObject.getName().contains(".ppt") || sourceDocObject.getName().contains(".PPT") || sourceDocObject.getName().contains(".pptx") || sourceDocObject.getName().contains(".PPTX")){
            docImage.setImageResource(R.drawable.powerpoint);
        }else
            if (sourceDocObject.getName().contains(".xlsx") || sourceDocObject.getName().contains(".XLSX") || sourceDocObject.getName().contains(".xls") || sourceDocObject.getName().contains(".XLS")){
            docImage.setImageResource(R.drawable.powerpoint);
        }else

        if (sourceDocObject.getName().contains(".pdf") || sourceDocObject.getName().contains(".PDF")){
            docImage.setImageResource(R.drawable.pdf);
            if (flag.equals("home")){
//                pageCounter.fromUri(sourceDocObject.getDocUri());
//                String pages = pageCounter.getPageCount() + "\npages";
//                pageCount.setText(pages);
            }

        }else {
            docImage.setImageResource(R.drawable.ic_launcher_background);
        }

        convertView.setOnClickListener(v -> {
//                if (sourceDocObject.getName().contains(".doc") || sourceDocObject.getName().contains(".DOC") || sourceDocObject.getName().contains(".docx") || sourceDocObject.getName().contains(".DOCX") ||
//                        sourceDocObject.getName().contains(".ppt") || sourceDocObject.getName().contains(".PPT") || sourceDocObject.getName().contains(".pptx") || sourceDocObject.getName().contains(".PPTX")){
//
//                }

            switch (flag) {
                case "fragment": {
                    if (sourceDocObject.getName().contains(".yNote")) {
                        //READ THE FILE, SPLIT THE CONTENT INTO AN ARRAY LIST OF URLS THEN SEND IT INTO
                        //THE PHOTO DOCS ADAPTER
                        ArrayList<String> photos = new ArrayList<>();
                        ArrayList<String> photoDesc = new ArrayList<>();
                        ArrayList<PictorialObject> pictorialObjects = new ArrayList<>();

                        Intent intent = new Intent(c.getApplicationContext(), PhotoDoc.class);
                        for (String s : PhotoDoc.Companion.readFile(sourceDocObject.getName(), c)) {
//                            PictorialObject pictorialObject = new PictorialObject(Uri.parse(s.split("!")[0]), s.split("!")[1]);
//                            pictorialObjects.add(pictorialObject);

                            photos.add(s.split("!")[0]);
                            photoDesc.add(s.split("!")[1]);
                        }

                        intent.putStringArrayListExtra("photos", photos);
                        intent.putStringArrayListExtra("photoDesc", photoDesc);
                        Toast.makeText(c.getApplicationContext(), photos.toString(), Toast.LENGTH_SHORT).show();
                        intent.putExtra("docName", sourceDocObject.getName());
                        c.startActivity(intent);
                    }
                    Intent intent = new Intent(c.getApplicationContext(), PdfViewerReadMode.class);
                    intent.putExtra("selectedDoc", uri.toString());
                    intent.putExtra("selectedDocName", docName.getText().toString());
                    c.startActivity(intent);
                    break;
                }
                case "docCamCard":
                case "shelfCard":
                case "docOnly":
                case "activity":
                    lectureNameDialog(uriStore.getText().toString());
                    break;
                case "addDoc": {
                    Intent intent = new Intent(c.getApplicationContext(), DocumentMetaData.class);
                    intent.putExtra("flag", "home");
                    intent.putExtra("SchoolName", school);
                    intent.putExtra("Department", dept);
                    intent.putExtra("selectedDocUri", uri.toString());
                    intent.putExtra("selectedDocName", docName.getText().toString());
                    intent.putExtra("selectedDocSize", docSize.getText().toString());
                    c.startActivity(intent);
                    break;
                }
                case "home":{
                    Intent intent = new Intent(c.getApplicationContext(), DocumentMetaData.class);
                    intent.putExtra("flag", "mainHome");
                    intent.putExtra("selectedDocUri", uri.toString());
                    intent.putExtra("selectedDocName", docName.getText().toString());
                    intent.putExtra("selectedDocSize", docSize.getText().toString());
                }
            }
        });
        return convertView;
    }

    public String[] readFile(String fileName) {
        File file = new File(FilingSystem.Companion.getDubDocuments(), fileName + ".yNoteDocs");
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

    private void lectureNameDialog(String docUri) {

        LinearLayout linearLayout = new LinearLayout(c);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(50, 0, 50, 100);
        final EditText input = new EditText(c);
        input.setLayoutParams(lp);
        input.setGravity(Gravity.TOP | Gravity.START);
        linearLayout.addView(input, lp);

        final AlertDialog.Builder fileNameDialog = new AlertDialog.Builder(c);
        fileNameDialog.setTitle("Lecture title");
        fileNameDialog.setMessage("e.g 'Critical Thinking by...'");
        fileNameDialog.setView(linearLayout);
        fileNameDialog.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());
        fileNameDialog.setPositiveButton("submit", (dialog, which) -> {
            String lectureName;
            if (input.getText().toString().trim().equals("")) {
                lectureNameDialog(docUri);
            } else {
                switch (flag) {
                    case "docOnly": {
                        lectureName = input.getText().toString().trim();
                        Toast.makeText(c, "New lecture created!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        Intent intent = new Intent(c.getApplicationContext(), LectureStudio2.class);
                        intent.putExtra("selectedDoc", docUri);
                        intent.putExtra("fileName", lectureName);
                        intent.putExtra("studio", "0");
                        c.startActivity(intent);

                        break;
                    }
                    case "docCamCard":
                        lectureName = input.getText().toString().trim();
                        Toast.makeText(c, "New lecture created!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        Intent intent1 = new Intent(c.getApplicationContext(), LectureStudio2.class);
                        intent1.putExtra("selectedDoc", docUri);
                        intent1.putExtra("fileName", lectureName);
                        intent1.putExtra("studio", "3");
                        c.startActivity(intent1);

                        break;
                    case "shelfCard": {
                        lectureName = input.getText().toString().trim();
                        Toast.makeText(c, "New lecture created!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        Intent intent2 = new Intent(c.getApplicationContext(), SourceBitmapList.class);
                        intent2.putExtra("selectedDoc", docUri);
                        intent2.putExtra("fileName", lectureName);
                        intent2.putExtra("studio", "5");
                        c.startActivity(intent2);

                        break;
                    }
                }

            }
        });


        fileNameDialog.setOnCancelListener(dialog -> dialog.dismiss());
        fileNameDialog.show();
    }


    public boolean isExternalStorageReadWritable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.i("State", "writable");
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPermission(String permission) {
        int check = ActivityCompat.checkSelfPermission(c, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }


}
