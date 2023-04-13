package com.midland.ynote.Objects;

import android.net.Uri;

import java.io.Serializable;
import java.util.List;

public class SelectedDoc implements Serializable {
    String docMetaData;
    String subField;
    String mainField;
    String knowledgeBase;
    int commentsCount;
    int saveCount;
    int ratersCount;
    int views;
    Double ratings;
    int price;
    String unitCode;
    String institution;
    String docDownloadLink;
    String videoDescription;
    String nodeName;
    String uid;
    List<String> search_keyword;

    String thumbNail, bitDescription;



    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public SelectedDoc() {
    }

    public SelectedDoc(String videoDescription) {
        this.videoDescription = videoDescription;
    }


    public SelectedDoc(String docMetaData, String docDownloadLink) {
        this.docMetaData = docMetaData;
        this.docDownloadLink = docDownloadLink;
    }

    public SelectedDoc(String docMetaData, String docDownloadLink, String uid, List<String> search_keyword) {
        this.docMetaData = docMetaData;
        this.docDownloadLink = docDownloadLink;
        this.uid = uid;
        this.search_keyword = search_keyword;
    }

    public SelectedDoc(String docMetaData, String docDownloadLink, String uid,
                       List<String> search_keyword, String subField, String knowledgeBase) {
        this.docMetaData = docMetaData;
        this.docDownloadLink = docDownloadLink;
        this.uid = uid;
        this.search_keyword = search_keyword;
        this.subField = subField;
        this.knowledgeBase = knowledgeBase;
    }

    public SelectedDoc(String docMetaData, String docDownloadLink, String uid, List<String> search_keyword,
                       String thumbNail, String subField, String knowledgeBase) {
        this.docMetaData = docMetaData;
        this.docDownloadLink = docDownloadLink;
        this.uid = uid;
        this.search_keyword = search_keyword;
        this.thumbNail = thumbNail;
        this.subField = subField;
        this.knowledgeBase = knowledgeBase;
    }


    public SelectedDoc(String docMetaData, String docDownloadLink, String uid, List<String> search_keyword,
                       String thumbNail) {
        this.docMetaData = docMetaData;
        this.docDownloadLink = docDownloadLink;
        this.uid = uid;
        this.search_keyword = search_keyword;
        this.thumbNail = thumbNail;
    }

    public SelectedDoc(String docMetaData, String docDownloadLink, String uid) {
        this.docMetaData = docMetaData;
        this.docDownloadLink = docDownloadLink;
        this.uid = uid;
    }

    public SelectedDoc(String docMetaData, String mainField, String subField, String knowledgeBase, int commentsCount, int ratersCount, int views, Double ratings, int price, String institution, String docDownloadLink, String uid, List<String> search_keyword, String thumbNail) {
        this.docMetaData = docMetaData;
        this.mainField = mainField;
        this.subField = subField;
        this.knowledgeBase = knowledgeBase;
        this.commentsCount = commentsCount;
        this.ratersCount = ratersCount;
        this.views = views;
        this.ratings = ratings;
        this.price = price;
        this.institution = institution;
        this.docDownloadLink = docDownloadLink;
        this.uid = uid;
        this.search_keyword = search_keyword;
        this.thumbNail = thumbNail;
    }

    public SelectedDoc(String newName, Uri fromFile, String valueOf, String valueOf1, Uri fromFile1) {
    }

    public String getDocMetaData() {
        return docMetaData;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(String thumbNail) {
        this.thumbNail = thumbNail;
    }

    public String getKnowledgeBase() {
        return knowledgeBase;
    }

    public String getSubField() {
        return subField;
    }

    public String getDocDownloadLink() {
        return docDownloadLink;
    }

    public List<String> getSearchKeyword() {
        return search_keyword;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public int getSaveCount() {
        return saveCount;
    }

    public int getRatersCount() {
        return ratersCount;
    }

    public Double getRatings() {
        return ratings;
    }

    public int getViews() {
        return views;
    }

    public int getPrice() {
        return price;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public String getInstitution() {
        return institution;
    }
}
