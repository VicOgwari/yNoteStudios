package com.midland.ynote.Objects;

import java.io.Serializable;

public class PendingLecObj implements Serializable {

    String sourceDoc, sourcePictorials, illustrationLocale, pendingLecTitle;
    String creationTime;
    String studio;
    long lastModified;

    public String getPendingLecTitle() {
        return pendingLecTitle;
    }

    public PendingLecObj(String sourceDoc, String sourcePictorials, String illustrationLocale, String pendingLecTitle, long l) {
        this.sourceDoc = sourceDoc;
        this.sourcePictorials = sourcePictorials;
        this.illustrationLocale = illustrationLocale;
        this.pendingLecTitle = pendingLecTitle;
        this.lastModified = l;
    }

    public String getStudio() {
        return studio;
    }

    public PendingLecObj(String sourceDoc, String sourcePictorials, String illustrationLocale, String pendingLecTitle, String creationTime, String studio) {
        this.sourceDoc = sourceDoc;
        this.sourcePictorials = sourcePictorials;
        this.illustrationLocale = illustrationLocale;
        this.pendingLecTitle = pendingLecTitle;
        this.creationTime = creationTime;
        this.studio = studio;
    }

    public String getSourceDoc() {
        return sourceDoc;
    }

    public String getSourcePictorials() {
        return sourcePictorials;
    }

    public String getIllustrationLocale() {
        return illustrationLocale;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public long getLastModified() {
        return lastModified;
    }
}
