package com.midland.ynote.Objects;

import java.io.Serializable;


public class PictorialObject implements Serializable {
    String picture;
    String picDescription;
    String narrationPath;


    public PictorialObject(String picture, String picDescription, String narrationPath) {
        this.picture = picture;
        this.picDescription = picDescription;
        this.narrationPath = narrationPath;
    }

    public String getPicDescription() {
        return picDescription;
    }

    public void setPicDescription(String picDescription) {
        this.picDescription = picDescription;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getNarrationPath() {
        return narrationPath;
    }


}
