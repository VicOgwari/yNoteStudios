package com.midland.ynote.Adapters;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.midland.ynote.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.midland.ynote.Activities.DocumentMetaData;
import com.midland.ynote.Activities.PdfViewerReadMode;
import com.midland.ynote.Activities.PhotoDoc;
import com.midland.ynote.Activities.SourceBitmapList;
import com.midland.ynote.Dialogs.LogInSignUp;
import com.midland.ynote.Objects.PictorialObject;
import com.midland.ynote.Objects.SourceDocObject;
import com.midland.ynote.Utilities.FilingSystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocalDocsAdapter extends RecyclerView.Adapter<LocalDocsAdapter.LocalDocVH>
        implements Filterable {


    Context c;
    ArrayList<SourceDocObject> sourceDocObjects;
    String flag, school, dept;
    SourceDocObject sourceDocObject;
    PDFView pageCounter;
    public int docPosition;

    public LocalDocsAdapter(Context c, ArrayList<SourceDocObject> sourceDocObjects, String flag, PDFView pageCounter) {
        this.c = c;
        this.sourceDocObjects = sourceDocObjects;
        this.flag = flag;
        this.pageCounter = pageCounter;
    }

    @NonNull
    @Override
    public LocalDocVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LocalDocVH(LayoutInflater.from(c).inflate(R.layout.doc_object1, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final LocalDocVH holder, int position) {
        docPosition = holder.getAbsoluteAdapterPosition();
        sourceDocObject = sourceDocObjects.get(position);
        holder.relView.setBackground(sourceDocObject.getGd());
        holder.docName.setText(sourceDocObject.getName());
        holder.docDate.setText(sourceDocObject.getFileDate());
        holder.docSize.setText(sourceDocObject.getFileSize());
        final Uri uri = sourceDocObject.getDocUri();
        PopupMenu homeDocMenu = new PopupMenu(c, holder.doc_options);
        homeDocMenu.getMenuInflater().inflate(R.menu.home_doc_menu, homeDocMenu.getMenu());
        homeDocMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.homeOpen:
                    Intent intent1 = new Intent(c, PdfViewerReadMode.class);
                    intent1.putExtra("selectedDoc", uri.toString());
                    intent1.putExtra("selectedDocName", holder.docName.getText().toString());
                    c.startActivity(intent1);
                    break;


                case R.id.homePublish:
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {

                        Intent intent = new Intent(c, DocumentMetaData.class);
                        intent.putExtra("flag", "mainHome");
                        intent.putExtra("selectedDocUri", uri.toString());
                        intent.putExtra("selectedDocName", holder.docName.getText().toString());
                        intent.putExtra("selectedDocSize", holder.docSize.getText().toString());
                        c.startActivity(intent);
                    } else {
                        LogInSignUp logInSignUp = new LogInSignUp(c);
                        logInSignUp.show();
                    }
                    break;
            }
            return false;
        });

        holder.docCard.setOnClickListener(v -> {
            if (holder.docName.getText().toString().endsWith(".docx") ||
                    holder.docName.getText().toString().endsWith(".DOCX")
                    || holder.docName.getText().toString().endsWith(".ppt") ||
                    holder.docName.getText().toString().endsWith(".PPT")
                    || holder.docName.getText().toString().endsWith(".xls") ||
                    holder.docName.getText().toString().endsWith(".XLS")
                    || holder.docName.getText().toString().endsWith(".pptx") ||
                    holder.docName.getText().toString().endsWith(".PPTX")) {

                AlertDialog publishDoc = new AlertDialog.Builder(c)
                        .setTitle("Microsoft document")
                        .setMessage("Such documents can't be opened from an offline service. Please " +
                                "publish this document first, then access it from your published documents.")
                        .setPositiveButton("Publish!", (dialog, which) -> {

                            Intent intent = new Intent(c, DocumentMetaData.class);
                            intent.putExtra("flag", "home");
                            intent.putExtra("selectedDocUri", uri.toString());
                            intent.putExtra("selectedDocName", holder.docName.getText().toString());
                            intent.putExtra("selectedDocSize", holder.docSize.getText().toString());
                            c.startActivity(intent);
                        })
                        .setNegativeButton("Cancel!", (dialog, which) -> dialog.dismiss())
                        .create();

                publishDoc.show();

            } else if (holder.docName.getText().toString().endsWith(".pdf")
                    || holder.docName.getText().toString().endsWith(".PDF")){

                Intent intent1 = new Intent(c, PdfViewerReadMode.class);
                intent1.putExtra("selectedDoc", uri.toString());
                intent1.putExtra("selectedDocName", holder.docName.getText().toString());
                c.startActivity(intent1);
            }
        });

        holder.doc_options.setOnClickListener(v -> {
            if (flag.equals("home")) {
                homeDocMenu.show();
            }
        });

        if (sourceDocObject.getName().contains(".doc") || sourceDocObject.getName().contains(".DOC") || sourceDocObject.getName().contains(".docx") || sourceDocObject.getName().contains(".DOCX")) {
            holder.docImage.setImageResource(R.drawable.microsoft_word);
        } else if (sourceDocObject.getName().contains(".ppt") || sourceDocObject.getName().contains(".PPT") || sourceDocObject.getName().contains(".pptx") || sourceDocObject.getName().contains(".PPTX")) {
            holder.docImage.setImageResource(R.drawable.powerpoint);
        } else if (sourceDocObject.getName().contains(".pdf") || sourceDocObject.getName().contains(".PDF")) {
            holder.docImage.setImageResource(R.drawable.pdf);
            if (flag.equals("home")) {
//                pageCounter.fromUri(sourceDocObject.getDocUri());
//                String pages = pageCounter.getPageCount() + "\npages";
//                pageCount.setText(pages);
            }

        } else {
            holder.docImage.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.itemView.setOnClickListener(v -> {

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
                        Toast.makeText(c, photos.toString(), Toast.LENGTH_SHORT).show();
                        intent.putExtra("docName", sourceDocObject.getName());
                        c.startActivity(intent);
                    }
                    Intent intent = new Intent(c, PdfViewerReadMode.class);
                    intent.putExtra("selectedDoc", uri.toString());
                    intent.putExtra("selectedDocName", holder.docName.getText().toString());
                    c.startActivity(intent);
                    break;
                }
                case "activity":
                    lectureNameDialog();
                    break;
                case "mainHome": {
                    Intent intent = new Intent(c, DocumentMetaData.class);
                    intent.putExtra("SchoolName", school);
                    intent.putExtra("Department", dept);
                    intent.putExtra("selectedDocUri", uri.toString());
                    intent.putExtra("selectedDocName", holder.docName.getText().toString());
                    intent.putExtra("selectedDocSize", holder.docSize.getText().toString());
                    c.startActivity(intent);
                    break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return sourceDocObjects.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public class LocalDocVH extends RecyclerView.ViewHolder {

        TextView docDate, docName;
        TextView pageCount;
        TextView docSize;
        ImageView docImage;
        RelativeLayout relView;
        CardView docCard;
        Button doc_options;

        public LocalDocVH(@NonNull View itemView) {
            super(itemView);
            docName = itemView.findViewById(R.id.bitmapDescription);
            docDate = itemView.findViewById(R.id.docDate);
            pageCount = itemView.findViewById(R.id.pageCount);
            docSize = itemView.findViewById(R.id.docSize);
            docCard = itemView.findViewById(R.id.lectureCard);
            docImage = itemView.findViewById(R.id.picBitmap);
            relView = itemView.findViewById(R.id.relFeed);
            doc_options = itemView.findViewById(R.id.doc_options);
        }
    }

    private void lectureNameDialog() {

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
        fileNameDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        fileNameDialog.setPositiveButton("submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String lectureName;
                if (input.getText().toString().trim().equals("")) {
                    lectureNameDialog();
                } else {
                    lectureName = input.getText().toString().trim();
                    if (isExternalStorageReadWritable() && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        File savedLecture = new File(FilingSystem.Companion.getPendingLectures(), lectureName + ".pl");
                        try {
                            FileOutputStream fos = new FileOutputStream(savedLecture);
                            fos.write((sourceDocObject.getDocUri() + "_-_").getBytes());
                            fos.close();
                            Toast.makeText(c, "New lecture created!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            Intent intent = new Intent(c, SourceBitmapList.class);
                            intent.putExtra("selectedDoc", sourceDocObject.getDocUri().toString());
                            intent.putExtra("fileName", lectureName);
                            c.startActivity(intent);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });


        fileNameDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
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

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<SourceDocObject> filteredSchools = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredSchools.addAll(sourceDocObjects);
            } else {
                String filteredPattern = constraint.toString().toLowerCase().trim();
                for (SourceDocObject docObject : sourceDocObjects) {
                    if (docObject.getName().toLowerCase().startsWith(filteredPattern)) {
                        filteredSchools.add(docObject);
                    }
                    if (docObject.getName().toLowerCase().contains(filteredPattern)) {
                        filteredSchools.add(docObject);
                    }

                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredSchools;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            sourceDocObjects.clear();
            sourceDocObjects.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
