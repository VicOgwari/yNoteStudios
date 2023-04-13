package com.midland.ynote.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.midland.ynote.Activities.VideoPlayer;
import com.midland.ynote.Dialogs.LogInSignUp;
import com.midland.ynote.Objects.SourceDocObject;
import com.midland.ynote.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class AllVidAdapter extends BaseAdapter {

    Context c;
    ArrayList<SourceDocObject> sourceDocObjects;

    public AllVidAdapter(Context c, ArrayList<SourceDocObject> sourceDocObjects) {
        this.c = c;
        this.sourceDocObjects = sourceDocObjects;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(c).inflate(R.layout.all_vid_object, parent, false);
        }

        final SourceDocObject sourceDocObject = (SourceDocObject)this.getItem(position);
        TextView vidName = convertView.findViewById(R.id.vidName);
        final TextView vidDate = convertView.findViewById(R.id.vidDate);
        TextView vidSize = convertView.findViewById(R.id.vidSize);
        Button docOptions = convertView.findViewById(R.id.doc_options);
        ImageView vidPreview = convertView.findViewById(R.id.vidPreview);
        Glide.with(c).load(sourceDocObject.getDocUri()).into(vidPreview);
        vidName.setText(sourceDocObject.getName());
        vidDate.setText(sourceDocObject.getFileDate());
        vidSize.setText(sourceDocObject.getFileSize());




        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(c.getApplicationContext(), VideoPlayer.class);
            intent.putExtra("vidUri", sourceDocObject.getDocUri().toString());
            intent.putExtra("vidTitle", sourceDocObject.getName());
            intent.putExtra("resource", "memory");
            c.startActivity(intent);
        });

        docOptions.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(c, docOptions);
            popupMenu.getMenuInflater().inflate(R.menu.lecture_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                switch (item.getItemId()){
                    case R.id.upload:
                        if (user != null){

                        }else {
                            LogInSignUp logInSignUp = new LogInSignUp(c);
                            logInSignUp.show();
                        }
                        break;

                    case R.id.delete:


                        break;
                }
                return false;
            });

            popupMenu.show();
        });
        return convertView;
    }


}
