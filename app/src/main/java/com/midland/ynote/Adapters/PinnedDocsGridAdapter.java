package com.midland.ynote.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.midland.ynote.Activities.DocumentLoader;
import com.midland.ynote.Activities.PdfViewerReadMode;
import com.midland.ynote.Activities.SchoolDepartmentDocuments;
import com.midland.ynote.Activities.UserProfile2;
import com.midland.ynote.Objects.SelectedDoc;
import com.midland.ynote.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PinnedDocsGridAdapter extends RecyclerView.Adapter<PinnedDocsGridAdapter.PinnedDocRecAdVH> implements Filterable {

    Context c;
    ArrayList<String> pins;
    String flag;
    public int docPosition;
    private final ArrayList<String> documentsList;


    public PinnedDocsGridAdapter(Context c, ArrayList<String> pins, String flag) {
        this.c = c;
        this.pins = pins;
        this.flag = flag;
        documentsList = new ArrayList<>(pins);

    }

    @NonNull
    @Override
    public PinnedDocRecAdVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PinnedDocRecAdVH(LayoutInflater.from(c).inflate(R.layout.document1, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PinnedDocRecAdVH holder, int position) {
        docPosition = holder.getAbsoluteAdapterPosition();

        if (flag.equals("docPins")){
            holder.countCarrierLY.setVisibility(View.GONE);
            holder.school.setVisibility(View.VISIBLE);
            String pin = pins.get(holder.getAbsoluteAdapterPosition());
            holder.school.setText(pins.get(holder.getAbsoluteAdapterPosition()).split("_-_")[0]);
            holder.school.setOnClickListener(view -> {
                Intent intent = new Intent(c, SchoolDepartmentDocuments.class);
                intent.putExtra("SchoolName", holder.school.getText().toString());
                c.startActivity(intent);
            });
            holder.publishedByApproval.setText(pins.get(holder.getAbsoluteAdapterPosition()).split("_-_")[2]);
            holder.docTitle.setText(pins.get(holder.getAbsoluteAdapterPosition()).split("_-_")[1]);

            String docTitle = holder.docTitle.getText().toString();
            if (docTitle.endsWith(".pdf") || docTitle.endsWith(".PDF")) {
                Glide.with(c).load(R.drawable.pdf).thumbnail(0.8f).into(holder.docImage);
            }
            if (docTitle.endsWith(".doc") || docTitle.endsWith(".DOC")) {
                Glide.with(c).load(R.drawable.microsoft_word).thumbnail(0.8f).into(holder.docImage);
            }
            if (docTitle.endsWith(".ppt") || docTitle.endsWith(".PPT")) {
                Glide.with(c).load(R.drawable.powerpoint).thumbnail(0.8f).into(holder.docImage);
            }
            if (docTitle.endsWith(".docx") || docTitle.endsWith(".DOCX")) {
                Glide.with(c).load(R.drawable.microsoft_word).thumbnail(0.8f).into(holder.docImage);
            }
            if (docTitle.endsWith(".pptx") || docTitle.endsWith(".PPTX")) {
                Glide.with(c).load(R.drawable.powerpoint).thumbnail(0.8f).into(holder.docImage);
            }

            if (!pin.split("_-_")[3].equals("")){
                final int endColor = Integer.parseInt(pin.split("_-_")[3]);
                GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{Color.WHITE, endColor});
                holder.relFeed.setBackground(gd);
            }

            holder.doc_options.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(c, holder.doc_options);
                popupMenu.getMenuInflater().inflate(R.menu.pin_doc_options, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.openDoc:
                            String finalLink = pin.split("_-_")[4];
                            Intent intent = new Intent(c, DocumentLoader.class);
                            intent.putExtra("school", pin.split("_-_")[0]);
                            intent.putExtra("title", docTitle);
                            intent.putExtra("docUrl", finalLink);
                            intent.putExtra("pinned", "true");
                            c.startActivity(intent);
                            break;

                        case R.id.unpin:
                            AlertDialog.Builder areYouSure = new AlertDialog.Builder(c)
                                    .setTitle("Remove from pins?")
                                    .setMessage("Are you done reading this document?")
                                    .setNegativeButton("Nope!", (dialog, which) -> {
                                        dialog.dismiss();
                                    }).setPositiveButton("Yup!", (dialog, which) -> {

//                                        DocumentReference documentReference = FirebaseFirestore.getInstance()
//                                                .collection("Content")
//                                                .document("Documents")
//                                                .collection(pin.split("_-_")[0])
//                                                .document(pin.split("_-_")[5]);
//                                        Map map = new HashMap<String, Any>();
//                                        map.put("saveCount", FieldValue.increment(-1));
//                                        documentReference.update(map).addOnSuccessListener(o -> {
//                                            ArrayList<String> pins = getPins();
//                                            pins.remove(pin);
//                                            SharedPreferences preferences = c.getSharedPreferences("docPins", Context.MODE_PRIVATE);
//                                            SharedPreferences.Editor editor = preferences.edit();
//                                            Gson g = new Gson();
//                                            String j = g.toJson(pins);
//                                            editor.putString("docPins", j);
//                                            editor.apply();
//                                            Toast.makeText(c, "Document unpinned!", Toast.LENGTH_SHORT).show();
//                                        });

                                        ArrayList<String> pins = getPins();
                                        pins.remove(pin);
                                        SharedPreferences preferences = c.getSharedPreferences("docPins", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        Gson g = new Gson();
                                        String j = g.toJson(pins);
                                        editor.putString("docPins", j);
                                        editor.apply();
                                        Toast.makeText(c, "Document unpinned!", Toast.LENGTH_SHORT).show();
                                    });

                            areYouSure.show();

                            break;

                        case R.id.publisher:
                            Intent intent1 = new Intent(c, UserProfile2.class);
                            intent1.putExtra("userID", pin.split("_-_")[8]);
                            c.startActivity(intent1);
                            break;
                    }
                    return false;
                });

                popupMenu.show();
            });

            holder.itemView.setOnClickListener(v->{
                String finalLink = pin.split("_-_")[4];
                Intent intent = new Intent(c, DocumentLoader.class);
                intent.putExtra("school", pin.split("_-_")[0]);
                intent.putExtra("title", docTitle);
                intent.putExtra("docUrl", finalLink);
                intent.putExtra("pinned", "true");
                c.startActivity(intent);
            });
        }else
            if (flag.equals("lecPins")){

        }else if (flag.equals("recentDocs")){
                holder.docTitle.setText(pins.get(holder.getAbsoluteAdapterPosition()).split("_-_")[0]);
                Uri docUri = Uri.parse(pins.get(holder.getAbsoluteAdapterPosition()).split("_-_")[1]);

                String docTitle = holder.docTitle.getText().toString();
                if (docTitle.endsWith(".pdf") || docTitle.endsWith(".PDF")) {
                    Glide.with(c).load(R.drawable.pdf).thumbnail(0.8f).into(holder.docImage);
                }
                if (docTitle.endsWith(".doc") || docTitle.endsWith(".DOC")) {
                    Glide.with(c).load(R.drawable.microsoft_word).thumbnail(0.8f).into(holder.docImage);
                }
                if (docTitle.endsWith(".ppt") || docTitle.endsWith(".PPT")) {
                    Glide.with(c).load(R.drawable.powerpoint).thumbnail(0.8f).into(holder.docImage);
                }
                if (docTitle.endsWith(".docx") || docTitle.endsWith(".DOCX")) {
                    Glide.with(c).load(R.drawable.microsoft_word).thumbnail(0.8f).into(holder.docImage);
                }
                if (docTitle.endsWith(".pptx") || docTitle.endsWith(".PPTX")) {
                    Glide.with(c).load(R.drawable.powerpoint).thumbnail(0.8f).into(holder.docImage);
                }

                holder.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(c, PdfViewerReadMode.class);
                    intent.putExtra("selectedDoc", docUri);
                    intent.putExtra("selectedDocName", holder.docTitle.getText().toString());
                    c.startActivity(intent);
                });
            }
    }

    @Override
    public int getItemCount() {
        if (pins != null){
            return pins.size();
        }else {
            return 0;
        }
    }

    public class PinnedDocRecAdVH extends RecyclerView.ViewHolder{
        TextView docTitle, publishedByApproval, school;
        ImageView docImage;
        LinearLayout countCarrierLY;
        RelativeLayout relFeed;
        private final Button doc_options;
        public PinnedDocRecAdVH(@NonNull View itemView) {
            super(itemView);
            publishedByApproval = itemView.findViewById(R.id.publishedByApproval);
            docImage = itemView.findViewById(R.id.docImage);
            relFeed = itemView.findViewById(R.id.relFeed);
            doc_options = itemView.findViewById(R.id.doc_options);
            docTitle = itemView.findViewById(R.id.docTitle);
            school = itemView.findViewById(R.id.school);
            countCarrierLY = itemView.findViewById(R.id.countCarrierLY);
        }
    }

    private ArrayList<String> getPins() {
        SharedPreferences preferences = c.getSharedPreferences("docPins", Context.MODE_PRIVATE);
        String json = preferences.getString("docPins", "");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>(){}.getType();

        return gson.fromJson(json, type);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }



    public final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<SelectedDoc> yearFilteredDocs = new ArrayList<>();
            List<String> stringFilteredDocs = new ArrayList<>();
//            if (DocRetrieval.Companion.getYearFilterString().equals("All")) {
            if (constraint == null || constraint.length() == 0) {
                stringFilteredDocs.addAll(documentsList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (String doc : documentsList) {
                    if (doc.toLowerCase().contains(filterPattern)) {
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
            pins.clear();
            pins.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
