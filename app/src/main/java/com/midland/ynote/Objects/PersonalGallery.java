package com.midland.ynote.Objects;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class PersonalGallery {

    String upVotes, downVotes, commentsCount, link;
    Boolean comments;
    DocumentReference reference;
    ArrayList<String> uiDs;


    public PersonalGallery(String upVotes, String downVotes, String commentsCount, String link, Boolean comments, DocumentReference reference, ArrayList<String> uiDs) {
        this.upVotes = upVotes;
        this.downVotes = downVotes;
        this.commentsCount = commentsCount;
        this.link = link;
        this.comments = comments;
        this.reference = reference;
        this.uiDs = uiDs;
    }

    public ArrayList<String> getUiDs() {
        return uiDs;
    }

    public String getUpVotes() {
        return upVotes;
    }

    public String getDownVotes() {
        return downVotes;
    }

    public String getCommentsCount() {
        return commentsCount;
    }

    public String getLink() {
        return link;
    }

    public Boolean getComments() {
        return comments;
    }

    public DocumentReference getReference() {
        return reference;
    }
}
