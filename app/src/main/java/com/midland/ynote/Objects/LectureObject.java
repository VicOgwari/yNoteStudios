package com.midland.ynote.Objects;

import android.net.Uri;

public class LectureObject {
    String lectureTitle;
    String docUri;
    Uri lectureDocument;

    public LectureObject(String lectureTitle, String docUri) {
        this.lectureTitle = lectureTitle;
        this.docUri = docUri;
    }

    //TO BE USED WHEN UPLOADING LECTURE
    public LectureObject(String lectureTitle, String docUri, Uri lectureDocument) {
        this.lectureTitle = lectureTitle;
        this.docUri = docUri;
        this.lectureDocument = lectureDocument;
    }

    public void setLectureTitle(String lectureTitle) {
        this.lectureTitle = lectureTitle;
    }

    public void setDocUri(String docUri) {
        this.docUri = docUri;
    }

    public void setLectureDocument(Uri lectureDocument) {
        this.lectureDocument = lectureDocument;
    }

    public String getLectureTitle() {
        return lectureTitle;
    }

    public String getDocUri() {
        return docUri;
    }

    public Uri getLectureDocument() {
        return lectureDocument;
    }
}
