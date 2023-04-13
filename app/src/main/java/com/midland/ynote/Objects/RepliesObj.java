package com.midland.ynote.Objects;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class RepliesObj {
    String reply, userName, uid, votes;
    ArrayList<String> voters;
    DocumentReference ref;

    public RepliesObj(String reply, String userName, String uid, String votes, ArrayList<String> voters, DocumentReference ref) {
        this.reply = reply;
        this.userName = userName;
        this.uid = uid;
        this.votes = votes;
        this.voters = voters;
        this.ref = ref;
    }

    public DocumentReference getRef() {
        return ref;
    }

    public String getReply() {
        return reply;
    }

    public String getUserName() {
        return userName;
    }

    public String getUid() {
        return uid;
    }

    public String getVotes() {
        return votes;
    }

    public ArrayList<String> getVoters() {
        return voters;
    }
}
