package com.midland.ynote.Objects;

import android.net.Uri;

public class ImageObject {

    private String name;
    private Uri uri;
    private long dateModified;

    public ImageObject(String name, Uri uri) {
        this.name = name;
        this.uri = uri;
    }

    public ImageObject(String name, Uri uri, long dateModified) {
        this.name = name;
        this.uri = uri;
        this.dateModified = dateModified;
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

    public long getDateModified() {
        return dateModified;
    }
}
