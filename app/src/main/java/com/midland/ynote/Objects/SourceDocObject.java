package com.midland.ynote.Objects;

import android.graphics.drawable.GradientDrawable;
import android.net.Uri;

public class SourceDocObject {

    private GradientDrawable gd;
    private String name;
    private Uri uri;
    private String fileSize;
    private String fileDate;
    private Uri docUri;

    public long getDateModified() {
        return dateModified;
    }

    public void setDateModified(long dateModified) {
        this.dateModified = dateModified;
    }

    private long dateModified;

    public SourceDocObject(String name, Uri uri, String fileSize, String fileDate, GradientDrawable gd) {
        this.name = name;
        this.uri = uri;
        this.fileSize = fileSize;
        this.fileDate = fileDate;
        this.gd = gd;
    }

    public SourceDocObject(String name, Uri uri, String fileSize, String fileDate, Uri docUri, long dateModified) {
        this.name = name;
        this.uri = uri;
        this.fileSize = fileSize;
        this.fileDate = fileDate;
        this.docUri = docUri;
        this.dateModified = dateModified;
    }

    public Uri getDocUri() {
        return docUri;
    }

    public void setDocUri(Uri docUri) {
        this.docUri = docUri;
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

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public GradientDrawable getGd() {
        return gd;
    }

    public void setGd(GradientDrawable gd) {
        this.gd = gd;
    }
}
