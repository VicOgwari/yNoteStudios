package com.midland.ynote.Objects;

import android.net.Uri;

public class CompletedLectures {

    private String name;
    private String fileSize;
    private String fileDate;
    private Uri uri;
    private Uri lectureDocUri;

    public CompletedLectures(String name, Uri uri, String fileSize, String fileDate) {
        this.name = name;
        this.uri = uri;
        this.fileSize = fileSize;
        this.fileDate = fileDate;
    }

    public Uri getLectureDocUri() {
        return lectureDocUri;
    }

    public void setLectureDocUri(Uri lectureDocUri) {
        this.lectureDocUri = lectureDocUri;
    }

    public String getFileDate() {
        return fileDate;
    }

    public void setFileDate(String fileDate) {
        this.fileDate = fileDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public Uri getUri() {
        return uri;
    }

    public String getFileSize() {
        return fileSize;
    }

}

