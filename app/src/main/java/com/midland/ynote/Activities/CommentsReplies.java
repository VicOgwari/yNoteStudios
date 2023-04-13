package com.midland.ynote.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager2.widget.ViewPager2;

import com.midland.ynote.Objects.CommentsObject;
import com.midland.ynote.R;
import com.midland.ynote.databinding.ActivityCommentsRepliesBinding;
import com.squareup.picasso.Picasso;

public class CommentsReplies extends AppCompatActivity {

    private ActivityCommentsRepliesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_comments_replies);
        binding = ActivityCommentsRepliesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView userName = binding.commenter;
        TextView comment = binding.commentUserName;
        TextView comment_time_date = binding.commentTimeDate;
        TextView im_comment_time_date = binding.imCommentTimeDate;
        TextView im_comment = binding.imCommentUserName;
        TextView imCommenter = binding.imCommenter;
        ImageView imageComment = binding.imageComment;
        ViewPager2 imageCommentVP = binding.imageCommentVP;
        CardView imageCommentCard = binding.imageCommentCard;
        CardView commentCard = binding.commentCard;

        Intent i = getIntent();
        String cid = i.getStringExtra("cid");

        if (cid != null){
            CommentsObject commentsObject = i.getParcelableExtra("comment");

            Toast.makeText(this, commentsObject.getComment(), Toast.LENGTH_SHORT).show();

            if (commentsObject.getImCommentUrls().size() != 0){
                imageCommentCard.setVisibility(View.VISIBLE);
                im_comment.setText(commentsObject.getComment().split("_-_")[2]);
                imCommenter.setText(commentsObject.getComment().split("_-_")[0]);
                im_comment_time_date.setText((commentsObject.getComment().split("_-_")[3] + " | " + commentsObject.getComment().split("_-_")[4]));

                if (commentsObject.getImCommentUrls().size() == 1){

                    Picasso.get().load(commentsObject.getImCommentUrls().get(0))
                            .placeholder(R.drawable.ic_hourglass_bottom_white)
                            .into(imageComment);
                }else{
                    imageComment.setVisibility(View.GONE);
                    imageCommentVP.setVisibility(View.VISIBLE);
                }
            }
            else {
                commentCard.setVisibility(View.VISIBLE);
                comment.setText(commentsObject.getComment().split("_-_")[2]);
                userName.setText(commentsObject.getComment().split("_-_")[0]);
                comment_time_date.setText((commentsObject.getComment().split("_-_")[3] + " | " + commentsObject.getComment().split("_-_")[4]));
            }

        }
    }
}