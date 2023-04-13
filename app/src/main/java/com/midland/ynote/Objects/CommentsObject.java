package com.midland.ynote.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class CommentsObject implements Parcelable {
    String comment;
    String imCommentUrl;
    String cId;
    String repliesCount;
    String upVotes, downVotes;
    ArrayList<String> imCommentUrls, uiDs;
    String docCommentName;
    ArrayList<String> docCommentNames;
    String docCommentUrl;
    ArrayList<Integer> uploadPosition;
    DocumentReference ref;
    String refStr;


    public CommentsObject(String comment) {
        this.comment = comment;
    }

    public CommentsObject(String comment, String imCommentUrl) {
        this.comment = comment;
        this.imCommentUrl = imCommentUrl;
    }

    public CommentsObject(String comment, String docCommentName, String docCommentUrl) {
        this.comment = comment;
        this.docCommentName = docCommentName;
        this.docCommentUrl = docCommentUrl;
    }

    public CommentsObject(String comment, ArrayList<String> imCommentUrls, String cId) {
        this.comment = comment;
        this.imCommentUrls = imCommentUrls;
        this.cId = cId;
    }

    public CommentsObject(String comment, ArrayList<String> imCommentUrls, String cId, String upVotes,
                          String downVotes, String repliesCount, ArrayList<String> uiDs) {
        this.comment = comment;
        this.imCommentUrls = imCommentUrls;
        this.cId = cId;
        this.upVotes = upVotes;
        this.downVotes = downVotes;
        this.repliesCount = repliesCount;
        this.uiDs = uiDs;
    }

    public CommentsObject(String comment, ArrayList<String> imCommentUrls, String cId, String upVotes,
                          String downVotes, String repliesCount, ArrayList<String> uiDs, DocumentReference ref, String refStr) {
        this.comment = comment;
        this.imCommentUrls = imCommentUrls;
        this.cId = cId;
        this.upVotes = upVotes;
        this.downVotes = downVotes;
        this.repliesCount = repliesCount;
        this.uiDs = uiDs;
        this.ref = ref;
        this.refStr = refStr;
    }

    public CommentsObject(String comment, ArrayList<String> docCommentNames, ArrayList<Integer> docCommentUrls) {
        this.comment = comment;
        this.docCommentNames = docCommentNames;
        this.uploadPosition = docCommentUrls;
    }

    protected CommentsObject(Parcel in) {
        comment = in.readString();
        imCommentUrl = in.readString();
        cId = in.readString();
        repliesCount = in.readString();
        upVotes = in.readString();
        downVotes = in.readString();
        imCommentUrls = in.createStringArrayList();
        uiDs = in.createStringArrayList();
        docCommentName = in.readString();
        docCommentNames = in.createStringArrayList();
        docCommentUrl = in.readString();
    }

    public static final Creator<CommentsObject> CREATOR = new Creator<CommentsObject>() {
        @Override
        public CommentsObject createFromParcel(Parcel in) {
            return new CommentsObject(in);
        }

        @Override
        public CommentsObject[] newArray(int size) {
            return new CommentsObject[size];
        }
    };


    public String getComment() {
        return comment;
    }

    public String getCId() {
        return cId;
    }

    public String getImCommentUrl() {
        return imCommentUrl;
    }

    public String getRepliesCount() {
        return repliesCount;
    }

    public String getDocCommentName() {
        return docCommentName;
    }

    public String getDocCommentUrl() {
        return docCommentUrl;
    }

    public ArrayList<Integer> getUploadPosition() {
        return uploadPosition;
    }

    public ArrayList<String> getImCommentUrls() {
        return imCommentUrls;
    }

    public void setImCommentUrls(ArrayList<String> imCommentUrls) {
        this.imCommentUrls = imCommentUrls;
    }

    public String getRefStr() {
        return refStr;
    }

    public ArrayList<String> getDocCommentNames() {
        return docCommentNames;
    }

    public DocumentReference getRef() {
        return ref;
    }

    public ArrayList<String> getUiDs() {
        return uiDs;
    }

    public String getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(String downVotes) {
        this.downVotes = downVotes;
    }

    public String getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(String upVotes) {
        this.upVotes = upVotes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(comment);
        parcel.writeString(imCommentUrl);
        parcel.writeString(cId);
        parcel.writeString(repliesCount);
        parcel.writeString(upVotes);
        parcel.writeString(downVotes);
        parcel.writeStringList(imCommentUrls);
        parcel.writeStringList(uiDs);
        parcel.writeString(docCommentName);
        parcel.writeStringList(docCommentNames);
        parcel.writeString(docCommentUrl);
    }
}
