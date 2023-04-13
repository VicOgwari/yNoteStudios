package com.midland.ynote.Objects;

import android.net.Uri;

public class SelectedVideo extends SelectedDoc {

    String userID;
    String downloadLink;
    String sourceDocUri;
    String illustrationUri;
    String videoUri;
    String videoDescription;
    String nodeName;

    public SelectedVideo(){

    }




    public SelectedVideo(String videoDescription, String nodeName) {
        this.videoDescription = videoDescription;
        this.nodeName = nodeName;
    }

    public SelectedVideo(String newName, Uri fromFile, String valueOf, String valueOf1, Uri fromFile1) {
    }

    public SelectedVideo(String vidDesc, String userID, String nodeName) {
        this.videoDescription = vidDesc;
        this.userID = userID;
        this.nodeName = nodeName;
    }

    public SelectedVideo(String vidDesc, String downloadLink, String userID, String nodeName) {
        this.videoDescription = vidDesc;
        this.downloadLink = downloadLink;
        this.userID = userID;
        this.nodeName = nodeName;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public String getSourceDocUri() {
        return sourceDocUri;
    }

    public String getIllustrationUri() {
        return illustrationUri;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public String getNodeName() {
        return nodeName;
    }
}
