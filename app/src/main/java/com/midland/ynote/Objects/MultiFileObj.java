package com.midland.ynote.Objects;

public class MultiFileObj {
    public int fileSize;
    public String fileName, fileUri;

    public MultiFileObj(int fileSize, String fileName, String fileUri) {
        this.fileSize = fileSize;
        this.fileName = fileName;
        this.fileUri = fileUri;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUri() {
        return fileUri;
    }

    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
    }
}
