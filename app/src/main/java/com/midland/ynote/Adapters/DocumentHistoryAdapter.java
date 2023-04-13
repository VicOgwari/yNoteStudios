package com.midland.ynote.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.midland.ynote.Activities.DocumentMetaData;
import com.midland.ynote.Activities.PdfViewerReadMode;
import com.midland.ynote.Dialogs.LogInSignUp;
import com.midland.ynote.Objects.SourceDocObject;
import com.midland.ynote.R;

import java.util.ArrayList;

public class DocumentHistoryAdapter extends RecyclerView.Adapter<DocumentHistoryAdapter.HistoryVH> {

    Context c;
    ArrayList<SourceDocObject> sourceDocObjects;
    String flag, school, dept;


    public DocumentHistoryAdapter(Context c, String flag, ArrayList<SourceDocObject> sourceDocObjects) {
        this.c = c;
        this.flag = flag;
        this.sourceDocObjects = sourceDocObjects;
    }

    @NonNull
    @Override
    public DocumentHistoryAdapter.HistoryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.doc_object, parent, false);
        return new HistoryVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentHistoryAdapter.HistoryVH holder, int position) {

        SourceDocObject sourceDocObject = sourceDocObjects.get(holder.getAbsoluteAdapterPosition());
        final Uri uri = sourceDocObject.getDocUri();

        holder.relFeed.setBackground(sourceDocObject.getGd());
        holder.docName.setText(sourceDocObject.getName());
        holder.docDate.setText(sourceDocObject.getFileDate());
        holder.docUri.setText(sourceDocObject.getDocUri().toString());
        if (Integer.parseInt(sourceDocObject.getFileSize()) < 1024){
            holder.docSize.setText(sourceDocObject.getFileSize() + "\nKBz");
        }else {
            holder.docSize.setText(Integer.parseInt(sourceDocObject.getFileSize()) / 1024 + "\nMBz");
        }

        holder.options.setOnClickListener(v -> {
            PopupMenu homeDocMenu = new PopupMenu(c, holder.options);
            homeDocMenu.getMenuInflater().inflate(R.menu.home_doc_menu, homeDocMenu.getMenu());
            homeDocMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.homeOpen:
                        Intent intent1 = new Intent(c.getApplicationContext(), PdfViewerReadMode.class);
                        intent1.putExtra("selectedDoc", holder.docUri.getText().toString());
                        intent1.putExtra("selectedDocName", holder.docName.getText().toString());
                        c.startActivity(intent1);
                        break;


                    case R.id.homePublish:
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                Intent intent = new Intent(c.getApplicationContext(), DocumentMetaData.class);
                                intent.putExtra("flag", "home");
                                intent.putExtra("selectedDocUri", uri.toString());
                                intent.putExtra("selectedDocName", holder.docName.getText().toString());
                                intent.putExtra("selectedDocSize", holder.docSize.getText().toString());

                            }else {
                                LogInSignUp logInSignUp = new LogInSignUp(c);
                                logInSignUp.show();
                            }

                        break;
                }
                return false;
            });

            homeDocMenu.show();
        });

        if (sourceDocObject.getName().contains(".doc") || sourceDocObject.getName().contains(".DOC") || sourceDocObject.getName().contains(".docx") || sourceDocObject.getName().contains(".DOCX")){
            holder.docImage.setImageResource(R.drawable.microsoft_word);
        }else
        if (sourceDocObject.getName().contains(".ppt") || sourceDocObject.getName().contains(".PPT") || sourceDocObject.getName().contains(".pptx") || sourceDocObject.getName().contains(".PPTX")){
            holder.docImage.setImageResource(R.drawable.powerpoint);
        }else
        if (sourceDocObject.getName().contains(".xlsx") || sourceDocObject.getName().contains(".XLSX") || sourceDocObject.getName().contains(".xls") || sourceDocObject.getName().contains(".XLS")){
            holder.docImage.setImageResource(R.drawable.powerpoint);
        }else
        if (sourceDocObject.getName().contains(".pdf") || sourceDocObject.getName().contains(".PDF")){
            holder.docImage.setImageResource(R.drawable.pdf);

        }else {
            holder.docImage.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(c, PdfViewerReadMode.class);
            intent.putExtra("selectedDoc", holder.docUri.getText().toString());
            intent.putExtra("selectedDocName", holder.docName.getText().toString());
            Toast.makeText(c, holder.docName.getText().toString(), Toast.LENGTH_SHORT).show();
            c.startActivity(intent);        });
    }

    @Override
    public int getItemCount() {
        return sourceDocObjects.size();
    }

    public class HistoryVH extends RecyclerView.ViewHolder{
        TextView docName, docDate, docUri, docSize;
        RelativeLayout relFeed;
        Button options;
        ImageView docImage;
        public HistoryVH(@NonNull View itemView) {
            super(itemView);
            docName = itemView.findViewById(R.id.bitmapDescription);
            docDate = itemView.findViewById(R.id.docDate);
            docSize = itemView.findViewById(R.id.docSize);
            docUri = itemView.findViewById(R.id.uriStore);
            options = itemView.findViewById(R.id.doc_options);
            docImage = itemView.findViewById(R.id.picBitmap);
            relFeed = itemView.findViewById(R.id.relFeed);
        }
    }
}
