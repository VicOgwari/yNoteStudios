package com.midland.ynote.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.midland.ynote.Objects.Publishable;
import com.midland.ynote.R;

import java.util.ArrayList;

public class ToBePublishedAdapter extends RecyclerView.Adapter<ToBePublishedAdapter.ToBePublishedVH> {
    private Context c;
    private ArrayList<Publishable> selections;
    private Publishable selection;
    private int endColor;


    public ToBePublishedAdapter(Context c, ArrayList<Publishable> selections) {
        this.c = c;
        this.selections = selections;
    }

    @NonNull
    @Override
    public ToBePublishedAdapter.ToBePublishedVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.to_be_published, parent, false);
        return new ToBePublishedVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ToBePublishedAdapter.ToBePublishedVH holder, int position) {
        selection = selections.get(holder.getAbsoluteAdapterPosition());
        holder.docTitleApproval.setText(selection.getDocMetaData().split("_-_")[5]);
        holder.publishedByApproval.setText(selection.getDocMetaData().split("_-_")[9]);

        if (holder.docTitleApproval.getText().toString().endsWith("pdf") || holder.docTitleApproval.getText().toString().endsWith("PDF")) {
            Glide.with(c).load(R.drawable.pdf).fitCenter().into(holder.docEmblem);
        } else if (holder.docTitleApproval.getText().toString().endsWith("doc") || holder.docTitleApproval.getText().toString().endsWith("DOC")
                || holder.docTitleApproval.getText().toString().endsWith("docx") || holder.docTitleApproval.getText().toString().endsWith("DOCX")) {
            Glide.with(c).load(R.drawable.microsoft_word).fitCenter().into(holder.docEmblem);
        } else if (holder.docTitleApproval.getText().toString().endsWith("ppt") || holder.docTitleApproval.getText().toString().endsWith("PPT")
                || holder.docTitleApproval.getText().toString().endsWith("pptx") || holder.docTitleApproval.getText().toString().endsWith("PPTX")) {
            Glide.with(c).load(R.drawable.powerpoint).fitCenter().into(holder.docEmblem);
        }

        holder.relFeedApproval.setBackgroundColor(Integer.parseInt(selection.getDocMetaData().split("_-_")[10]));

    }



    @Override
    public int getItemCount() {
        return selections.size();
    }


    public static class ToBePublishedVH extends RecyclerView.ViewHolder{

        private TextView docTitleApproval, publishedByApproval;
        private ImageView docEmblem;
        private RelativeLayout relFeedApproval;
        public ToBePublishedVH(@NonNull View itemView) {
            super(itemView);
            docTitleApproval = itemView.findViewById(R.id.docTitleApproval);
            publishedByApproval = itemView.findViewById(R.id.publishedByApproval);
            docEmblem = itemView.findViewById(R.id.docEmblem);
            relFeedApproval = itemView.findViewById(R.id.relFeedApproval);
        }
    }
}
