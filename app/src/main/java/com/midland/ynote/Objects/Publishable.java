package com.midland.ynote.Objects;

import java.util.ArrayList;

public class Publishable {
    public String docMetaData, institution, semester, docUri, coverUri, unitCode, docDetails;
    public ArrayList<String> allTags;

    public Publishable(String docMetaData, String institution, String semester, String docUri, String coverUri, String unitCode, String docDetails, ArrayList<String> allTags) {
        this.docMetaData = docMetaData;
        this.institution = institution;
        this.semester = semester;
        this.docUri = docUri;
        this.coverUri = coverUri;
        this.unitCode = unitCode;
        this.docDetails = docDetails;
        this.allTags = allTags;
    }

    public String getDocMetaData() {
        return docMetaData;
    }

    public void setDocMetaData(String docMetaData) {
        this.docMetaData = docMetaData;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getDocUri() {
        return docUri;
    }

    public void setDocUri(String docUri) {
        this.docUri = docUri;
    }

    public String getCoverUri() {
        return coverUri;
    }

    public void setCoverUri(String coverUri) {
        this.coverUri = coverUri;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getDocDetails() {
        return docDetails;
    }

    public void setDocDetails(String docDetails) {
        this.docDetails = docDetails;
    }

    public ArrayList<String> getAllTags() {
        return allTags;
    }

    public void setAllTags(ArrayList<String> allTags) {
        this.allTags = allTags;
    }
}
