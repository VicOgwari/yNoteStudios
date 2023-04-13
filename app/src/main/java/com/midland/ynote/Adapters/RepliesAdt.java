package com.midland.ynote.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.midland.ynote.Activities.UserProfile2;
import com.midland.ynote.Objects.RepliesObj;
import com.midland.ynote.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RepliesAdt extends RecyclerView.Adapter<RepliesAdt.RepliesVH> {

    private Context c;
    private ArrayList<RepliesObj> replies;

    public RepliesAdt(Context c, ArrayList<RepliesObj> replies) {
        this.c = c;
        this.replies = replies;
    }

    @NonNull
    @Override
    public RepliesVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RepliesVH(LayoutInflater.from(c).inflate(R.layout.replies_obj, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RepliesVH holder, int position) {
        RepliesObj obj = replies.get(holder.getAbsoluteAdapterPosition());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null && obj.getVoters().contains(user.getUid() + "_-_" + "Up")) {
            holder.plus.setBackgroundColor(Color.GREEN);
        }
        if (user != null && obj.getVoters().contains(user.getUid() + "_-_" + "Down")) {
            holder.minus.setBackgroundColor(Color.GREEN);
        }

        holder.plus.setOnClickListener(view -> {
            if (user != null && (!obj.getVoters().contains(user.getUid() + "_-_" + "Up") || !obj.getVoters().contains(user.getUid() + "_-_" + "Down"))) {
                Map<String, Object> vote = new HashMap();
                vote.put("votes", FieldValue.increment(1));
                vote.put("voters", FieldValue.arrayUnion(user.getUid() + "_-_" + "Up"));
                obj.getRef().update(vote)
                        .addOnSuccessListener(unused -> {
                            String newVotes = String.valueOf((Integer.parseInt(holder.votes.getText().toString()) + 1));
                            holder.votes.setText(newVotes);
                            holder.plus.setBackgroundColor(Color.GREEN);
                        });
            }

        });
        holder.minus.setOnClickListener(view -> {
            if (user != null && (!obj.getVoters().contains(user.getUid() + "_-_" + "Up") || !obj.getVoters().contains(user.getUid() + "_-_" + "Down"))) {
                Map<String, Object> vote = new HashMap();
                vote.put("votes", FieldValue.increment(-1));
                vote.put("voters", FieldValue.arrayUnion(user.getUid() + "_-_" + "Down"));
                obj.getRef().update(vote)
                        .addOnSuccessListener(unused -> {
                            String newVotes = String.valueOf((Integer.parseInt(holder.votes.getText().toString()) - 1));
                            holder.votes.setText(newVotes);
                            holder.minus.setBackgroundColor(Color.GREEN);
                        });
            }

        });

        holder.userName.setText(obj.getUserName());
        holder.votes.setText(obj.getVotes());
        holder.reply.setText(obj.getReply());
        holder.uid.setText(obj.getUid());

        holder.userName.setOnClickListener(view -> {
            Intent intent = new Intent(c, UserProfile2.class);
            intent.putExtra("userID", holder.uid.getText().toString());
            c.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return replies.size();
    }

    public static class RepliesVH extends RecyclerView.ViewHolder{
        ImageButton plus, minus;
        TextView userName, reply, votes, uid;
        public RepliesVH(@NonNull View itemView) {
            super(itemView);
            plus = itemView.findViewById(R.id.plus);
            uid = itemView.findViewById(R.id.uid);
            minus = itemView.findViewById(R.id.minus);
            userName = itemView.findViewById(R.id.userName);
            reply = itemView.findViewById(R.id.reply);
            votes = itemView.findViewById(R.id.votes);
        }
    }
}
