package com.midland.ynote.Adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.midland.ynote.Activities.DocumentLoader;
import com.midland.ynote.Activities.SchoolDepartmentDocuments;
import com.midland.ynote.Activities.UserProfile2;
import com.midland.ynote.Dialogs.LogInSignUp;
import com.midland.ynote.Objects.CommentsObject;
import com.midland.ynote.Objects.SelectedDoc;
import com.midland.ynote.R;
import com.midland.ynote.Utilities.FilingSystem;
import com.midland.ynote.Utilities.UserPowers;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocumentVH> implements Filterable {

    private final Context context, con;
    private final ArrayList<SelectedDoc> documents;
    private final ArrayList<SelectedDoc> documentsList;
    public int docPosition;
    private SelectedDoc cloudDoc, cloudDoc1;
    private CollectionReference commentsRef;
    private ArrayList<CommentsObject> comments;
    private SchoolDepartmentDocuments thisSchool;

    public DocumentAdapter(SchoolDepartmentDocuments thisSchool,
                           Context context,
                           Context con,
                           ArrayList<SelectedDoc> documents) {
        this.con = con;
        this.thisSchool = thisSchool;
        this.context = context;
        this.documents = documents;
        documentsList = new ArrayList<>(documents);
    }


    @NonNull
    @Override
    public DocumentVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (thisSchool != null){
            return new DocumentVH(LayoutInflater.from(context).inflate(R.layout.document, parent, false));
        }else {
            return new DocumentVH(LayoutInflater.from(context).inflate(R.layout.document1, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final DocumentVH holder, final int position) {

        docPosition = holder.getAbsoluteAdapterPosition();
        cloudDoc = documents.get(docPosition);
        cloudDoc1 = documents.get(holder.getLayoutPosition());
        holder.docTitle.setText(cloudDoc.getDocMetaData().split("_-_")[5]);
        holder.commentsCount.setText(String.valueOf(cloudDoc.getCommentsCount()));
        holder.downloadCount.setText(String.valueOf(cloudDoc.getSaveCount()));
        final String uID = cloudDoc.getDocMetaData().split("_-_")[8];
        final String displayName = cloudDoc.getDocMetaData().split("_-_")[9];
        final int endColor = Integer.parseInt(cloudDoc.getDocMetaData().split("_-_")[10]);
        String downloadLink = null;
        String downloadLink1 = null;
        try {
            downloadLink = cloudDoc.getDocDownloadLink().split("_-_")[0];
            downloadLink1 = cloudDoc.getThumbNail();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (downloadLink1 != null){
            Glide.with(context).load(downloadLink1).thumbnail((float)0.9).into(holder.docCover);
        }


        holder.publisher.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserProfile2.class);
            intent.putExtra("userID", uID);
            con.startActivity(intent);
        });


        final String finalDownloadLink = downloadLink;
        holder.itemView.setOnClickListener(v -> {
            String finalLink = "http://docs.google.com/gview?embedded=true&url=" + URLEncoder.encode(finalDownloadLink);
            Intent intent = new Intent(context, DocumentLoader.class);
            intent.putExtra("school", cloudDoc.getDocMetaData().split("_-_")[0]);
            intent.putExtra("publisherUid", cloudDoc.getDocMetaData().split("_-_")[8]);
            intent.putExtra("title", holder.docTitle.getText().toString());
            intent.putExtra("pinCount", holder.downloadCount.getText().toString());
            intent.putExtra("comCount", holder.commentsCount.getText().toString());
            intent.putExtra("docUrl", finalLink);
            intent.putExtra("endColor", String.valueOf(endColor));
            intent.putExtra("publisher", holder.publisher.getText().toString());
            con.startActivity(intent);

//                thisSchool.setRelVisibility(viewerRel);
//                getComments();
//
//                thisSchool.getDocViewer().getSettings().setJavaScriptEnabled(true);
//                thisSchool.getDocViewer().getSettings().setBuiltInZoomControls(false);
////                docViewer.getSettings().setJavaScriptEnabled(true);
////                docViewer.getSettings().setBuiltInZoomControls(false);
//
//                thisSchool.getDocViewer().setWebViewClient(new WebViewClient(){
//                    @Override
//                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            view.loadUrl(request.getUrl().toString());
//                        }else {
//                            Toast.makeText(context, "Api issues", Toast.LENGTH_SHORT).show();
//                        }
//                        return super.shouldOverrideUrlLoading(view, request);
//                    }
//                });
//                thisSchool.getDocViewer().setWebChromeClient(new WebChromeClient(){
//                    @Override
//                    public void onProgressChanged(WebView view, int newProgress) {
//                        super.onProgressChanged(view, newProgress);
//                        if (newProgress == 100){
//                            readBuffer.setVisibility(View.INVISIBLE);
//
//                        }
//                    }
//
//                });
//                thisSchool.getDocViewer().loadUrl(finalLink);

        });

        holder.publisher.setText(displayName);
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.WHITE, endColor});
        holder.relFeed.setBackground(gd);
//        if (!cloudDoc.getDocDownloadLink().split("_-_")[1].equals("") || cloudDoc.getDocDownloadLink().split("_-_")[1] != null){
//            Glide.with(context).load(cloudDoc.getDocDownloadLink().split("_-_")[1]).thumbnail(0.8f).into(holder.docCover);
//        }


        String docTitle = holder.docTitle.getText().toString();

        if (docTitle.endsWith(".pdf") || docTitle.endsWith(".PDF")) {
            Glide.with(context).load(R.drawable.pdf).thumbnail(0.8f).into(holder.docImage);
        }
        if (docTitle.endsWith(".doc") || docTitle.endsWith(".DOC")) {
            Glide.with(context).load(R.drawable.microsoft_word).thumbnail(0.8f).into(holder.docImage);
        }
        if (docTitle.endsWith(".ppt") || docTitle.endsWith(".PPT")) {
            Glide.with(context).load(R.drawable.powerpoint).thumbnail(0.8f).into(holder.docImage);
        }
        if (docTitle.endsWith(".docx") || docTitle.endsWith(".DOCX")) {
            Glide.with(context).load(R.drawable.microsoft_word).thumbnail(0.8f).into(holder.docImage);
        }
        if (docTitle.endsWith(".pptx") || docTitle.endsWith(".PPTX")) {
            Glide.with(context).load(R.drawable.powerpoint).thumbnail(0.8f).into(holder.docImage);
        }

        holder.doc_options.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.doc_options);
            popupMenu.getMenuInflater().inflate(R.menu.doc_options_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.openDoc:
                        String finalLink = "http://docs.google.com/gview?embedded=true&url=" + URLEncoder.encode(finalDownloadLink);
                        Intent intent = new Intent(context, DocumentLoader.class);
                        intent.putExtra("school", cloudDoc.getDocMetaData().split("_-_")[0]);
                        intent.putExtra("title", cloudDoc.getDocMetaData().split("_-_")[5]);
                        intent.putExtra("docUrl", finalLink);
                        context.startActivity(intent);
                        break;
                    case R.id.downloadDoc:
                        downloadFile(con, documents.get(holder.getAbsoluteAdapterPosition()).getDocMetaData().split("_-_")[5], documents.get(holder.getAbsoluteAdapterPosition()).getDocMetaData().split("_-_")[0],
                                documents.get(holder.getAbsoluteAdapterPosition()).getDocMetaData().split("_-_")[1], FilingSystem.Companion.getDubDocuments().getAbsolutePath(),
                                finalDownloadLink);
                        break;

                    case R.id.misplaced:
                        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                        if (user1 != null) {
                            if (uID.equals(user1.getUid())){
                                DocumentReference toDelRef = FirebaseFirestore.getInstance().collection("ToDelete")
                                        .document(cloudDoc.getDocMetaData().split("_-_")[1]);
                                Map<String, String> toDelete = new HashMap<>();
                                toDelete.put("School", cloudDoc.getDocMetaData().split("_-_")[1]);
                                toDelete.put("Name", cloudDoc.getDocMetaData().split("_-_")[5]);
                                toDelRef.get().addOnSuccessListener(documentSnapshot -> {
                                    if (documentSnapshot.exists()){
                                        Toast.makeText(context, "We know about this case.", Toast.LENGTH_LONG).show();
                                    }else {
                                       toDelRef.set(toDelete)
                                                .addOnSuccessListener(documentReference -> {
                                                    Toast.makeText(context, "We will consider your application.", Toast.LENGTH_LONG).show();
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(context, "Try again later.", Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                });



                            }else {
                                Toast.makeText(context, "This is not your document", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            LogInSignUp logInSignUp = new LogInSignUp(con);
                            logInSignUp.show();
                        }

                        break;

                    case R.id.followPublisher:
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            UserPowers.Companion.isFollowing(uID, null, user, context, thisSchool);
                        } else {
                            LogInSignUp logInSignUp = new LogInSignUp(con);
                            logInSignUp.show();
                        }
                        break;
                }
                return false;
            });

            popupMenu.show();
        });

    }

    @Override
    public int getItemCount() {
        return documents.size();
    }


    public static class DocumentVH extends RecyclerView.ViewHolder {
        private final TextView docTitle;
        private final TextView publisher;
        private final TextView downloadCount;
        private final TextView commentsCount;
        private final ImageView docImage;
        private final ImageView docCover;
        private final Button doc_options;
        private final RelativeLayout relFeed;

        public DocumentVH(@NonNull View itemView) {
            super(itemView);
            docTitle = itemView.findViewById(R.id.docTitle);
            downloadCount = itemView.findViewById(R.id.downloadCount);
            commentsCount = itemView.findViewById(R.id.commentsCount);
            docImage = itemView.findViewById(R.id.docImage);
            docCover = itemView.findViewById(R.id.objectCoverPhoto);
            doc_options = itemView.findViewById(R.id.doc_options);
            publisher = itemView.findViewById(R.id.publishedByApproval);
            relFeed = itemView.findViewById(R.id.relFeed);
        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }



    public final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<SelectedDoc> yearFilteredDocs = new ArrayList<>();
            List<SelectedDoc> stringFilteredDocs = new ArrayList<>();
//            if (DocRetrieval.Companion.getYearFilterString().equals("All")) {
                if (constraint == null || constraint.length() == 0) {
                    stringFilteredDocs.addAll(documentsList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (SelectedDoc doc : documentsList) {
                        if (doc.getDocMetaData().toLowerCase().contains(filterPattern)) {
                            stringFilteredDocs.add(doc);
                        }
                    }

                }
//            }
//            else {
//                if (constraint == null || constraint.length() == 0) {
//                    for (SelectedDoc doc : documentsList) {
//                        if (doc.getDocMetaData().split("_-_")[2].toLowerCase().contains(DocRetrieval.Companion.getYearFilterString())) {
//                            yearFilteredDocs.add(doc);
//                        }
//                    }
//                    stringFilteredDocs.addAll(yearFilteredDocs);
//                } else {
//                    String filterPattern = constraint.toString().toLowerCase().trim();
//                    for (SelectedDoc doc : yearFilteredDocs) {
//                        if (doc.getDocMetaData().split("_-_")[5].toLowerCase().startsWith(filterPattern)) {
//                            stringFilteredDocs.add(doc);
//                        }
//                    }
//
//                }
//            }
            FilterResults results = new FilterResults();
            results.values = stringFilteredDocs;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            documents.clear();
            documents.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public final Filter filter1 = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<SelectedDoc> yearFilteredDocs = new ArrayList<>();
            List<SelectedDoc> stringFilteredDocs1 = new ArrayList<>();
//            if (DocRetrieval.Companion.getYearFilterString().equals("All")) {
            if (constraint == null || constraint.length() == 0) {
                stringFilteredDocs1.addAll(documentsList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (SelectedDoc doc : documentsList) {
                    if (doc.getDocMetaData().toLowerCase().contains(filterPattern)) {
                        stringFilteredDocs1.add(doc);
                    }
                }

            }
//            }
//            else {
//                if (constraint == null || constraint.length() == 0) {
//                    for (SelectedDoc doc : documentsList) {
//                        if (doc.getDocMetaData().split("_-_")[2].toLowerCase().contains(DocRetrieval.Companion.getYearFilterString())) {
//                            yearFilteredDocs.add(doc);
//                        }
//                    }
//                    stringFilteredDocs.addAll(yearFilteredDocs);
//                } else {
//                    String filterPattern = constraint.toString().toLowerCase().trim();
//                    for (SelectedDoc doc : yearFilteredDocs) {
//                        if (doc.getDocMetaData().split("_-_")[5].toLowerCase().startsWith(filterPattern)) {
//                            stringFilteredDocs.add(doc);
//                        }
//                    }
//
//                }
//            }
            FilterResults results = new FilterResults();
            results.values = stringFilteredDocs1;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            documents.clear();
            documents.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    private String replacer(String docName) {
        String docName1 = docName.replace("]", "");
        String docName2 = docName1.replace("[", "");
        String docName3 = docName2.replace(".", "");
        String docName4 = docName3.replace("$", "");
        String docName5 = docName4.replace("*", "");
        return docName5.replace("#", "");
    }


    private void downloadFile(Context c, String fileName, String school, String department, String destinationDirectory, String url){
        StorageReference shelfStorageRef = FirebaseStorage.getInstance()
                .getReference("Documents")
                .child(fileName);
        shelfStorageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            DownloadManager downloadManager = (DownloadManager) c.getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalFilesDir(c, String.valueOf(c.getExternalFilesDir(destinationDirectory)), fileName);
            downloadManager.enqueue(request);
            //Call the http trigger f(x)
        }).addOnFailureListener(e -> {

        });



    }

    private void downloadFile1(Context c, String fileName, String school, String department, String destinationDirectory, String url){
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
        File rootFile = new File(destinationDirectory);
        if (!rootFile.exists()){
            rootFile.mkdirs();
        }
        final File localFile = new File(rootFile, fileName + " -[" + school + " | " + department + "]");
        storageReference.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(c, "New doc alert!", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(c, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }



}
