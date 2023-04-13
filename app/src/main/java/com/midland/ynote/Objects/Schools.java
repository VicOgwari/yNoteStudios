package com.midland.ynote.Objects;

public class Schools {
    private String schoolName;
    private Integer schoolEmblem;
    private Integer docCount;
    private Integer userCount;
    private Boolean enrolled, hidden;

    public Schools(String schoolName, Integer schoolEmblem) {
        this.schoolName = schoolName;
        this.schoolEmblem = schoolEmblem;
    }

    public Schools(String schoolName, Integer schoolEmblem, Integer docCount) {
        this.schoolName = schoolName;
        this.schoolEmblem = schoolEmblem;
        this.docCount = docCount;
    }
    public Schools(String schoolName, Integer schoolEmblem, Integer docCount, Integer userCount) {
        this.schoolName = schoolName;
        this.schoolEmblem = schoolEmblem;
        this.docCount = docCount;
        this.userCount = userCount;
    }

    public Schools(String schoolName, Integer schoolEmblem, Integer docCount, Integer userCount, Boolean enrolled) {
        this.schoolName = schoolName;
        this.schoolEmblem = schoolEmblem;
        this.docCount = docCount;
        this.userCount = userCount;
        this.enrolled = enrolled;
    }

    public Schools(String schoolName, Integer schoolEmblem, Integer docCount, Integer userCount, Boolean enrolled, Boolean hidden) {
        this.schoolName = schoolName;
        this.schoolEmblem = schoolEmblem;
        this.docCount = docCount;
        this.userCount = userCount;
        this.enrolled = enrolled;
        this.hidden = hidden;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public Integer getSchoolEmblem() {
        return schoolEmblem;
    }

    public Integer getDocCount() {
        return docCount;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public Boolean getEnrolled() {
        return enrolled;
    }

    public void setEnrolled(Boolean enrolled) {
        this.enrolled = enrolled;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }
}
