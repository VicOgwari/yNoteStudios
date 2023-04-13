package com.midland.ynote.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class BitMapTitle {
    private ArrayList<String> pictures1;
    private ArrayList<String> narrations1;
    private ArrayList<String> descriptions1;
    String title;
    String relevance;
    String thumbNail;
    String narrations;
    String descriptions;
    String viewsCount;
    String commentsCount;
    String savesCount;
    String pictures;
    String slideCount;
    String uid;
    double ratings;
    int ratersCount;

    public ArrayList<String> getPictures1() {
        return pictures1;
    }

    public void setPictures1(ArrayList<String> pictures1) {
        this.pictures1 = pictures1;
    }

    public ArrayList<String> getNarrations1() {
        return narrations1;
    }

    public void setNarrations1(ArrayList<String> narrations1) {
        this.narrations1 = narrations1;
    }

    public ArrayList<String> getDescriptions1() {
        return descriptions1;
    }

    public void setDescriptions1(ArrayList<String> descriptions1) {
        this.descriptions1 = descriptions1;
    }

    public BitMapTitle(@Nullable String title, @Nullable String relevance, @Nullable String thumbnail,
                       @Nullable ArrayList<String> pictures, @Nullable ArrayList<String> narrations,
                       @Nullable ArrayList<String> descriptions, @NotNull String viewsCount,
                       @NotNull String commentsCount, @NotNull String savesCount,
                       @NotNull String slideCount, @NotNull String uid, @NotNull String displayName) {
        this.title = title;
        this.relevance = relevance;
        this.thumbNail = thumbnail;
        this.pictures1 = pictures;
        this.narrations1 = narrations;
        this.descriptions1 = descriptions;
        this.viewsCount = viewsCount;
        this.commentsCount = commentsCount;
        this.savesCount = savesCount;
        this.slideCount = slideCount;
        this.uid = uid;
        this.displayName = displayName;
    }

    public BitMapTitle(@Nullable String title, @Nullable String relevance,
                       @Nullable String thumbnail, @Nullable ArrayList<String> pictures,
                       @Nullable ArrayList<String> narrations, @Nullable ArrayList<String> descriptions,
                       @NotNull String viewsCount, @NotNull String commentsCount, @NotNull String savesCount,
                       @NotNull String slideCount, @NotNull String uid, @NotNull String displayName,
                       @NotNull int ratersCount, @NotNull double ratings) {
        this.title = title;
        this.relevance = relevance;
        this.thumbNail = thumbnail;
        this.pictures1 = pictures;
        this.narrations1 = narrations;
        this.descriptions1 = descriptions;
        this.viewsCount = viewsCount;
        this.commentsCount = commentsCount;
        this.savesCount = savesCount;
        this.slideCount = slideCount;
        this.uid = uid;
        this.displayName = displayName;
        this.ratersCount = ratersCount;
        this.ratings = ratings;
    }

    public String getSlideCount() {
        return slideCount;
    }

    public void setSlideCount(String slideCount) {
        this.slideCount = slideCount;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    String displayName;

    public BitMapTitle(String title, String relevance, String thumbNail) {
        this.title = title;
        this.relevance = relevance;
        this.thumbNail = thumbNail;
    }

    public BitMapTitle(String title, String relevance) {
        this.title = title;
        this.relevance = relevance;
    }

    public BitMapTitle(String title) {
        this.title = title;
    }

    public BitMapTitle(String title, String relevance, String thumbNail,
                       String pictures, String narrations, String descriptions,
                       String viewsCount, String commentsCount, String savesCount,
                       String slideCount, String uid, String displayName) {

        this.title = title;
        this.relevance = relevance;
        this.thumbNail = thumbNail;
        this.pictures = pictures;
        this.narrations = narrations;
        this.descriptions = descriptions;
        this.viewsCount = viewsCount;
        this.commentsCount = commentsCount;
        this.savesCount = savesCount;
        this.slideCount = slideCount;
        this.uid = uid;
        this.displayName = displayName;
    }


    public String getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(String thumbNail) {
        this.thumbNail = thumbNail;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getRelevance() {
        return relevance;
    }

    public void setRelevance(String relevance) {
        this.relevance = relevance;
    }

    public String getNarrations() {
        return narrations;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public String getViewsCount() {
        return viewsCount;
    }

    public String getCommentsCount() {
        return commentsCount;
    }

    public String getSavesCount() {
        return savesCount;
    }

    public String getPictures() {
        return pictures;
    }

    public double getRatings() {
        return ratings;
    }

    public int getRatersCount() {
        return ratersCount;
    }

    public BitMapTitle(ArrayList<String> pictures1) {
        this.pictures1 = pictures1;
    }
}
