package com.midland.ynote.Objects;

import java.util.List;

public class HomeSliderObj {
    String userName;
    String userMail;

    public String getStudents() {
        return students;
    }

    public void setStudents(String students) {
        this.students = students;
    }

    public String getCoaches() {
        return coaches;
    }

    public void setCoaches(String coaches) {
        this.coaches = coaches;
    }

    String philosophy;
    String userImage;
    String backgroundImage;
    String userId;
    String articleLink;
    String institution;
    String students;
    String coaches;
    String school;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    String flag;
    List<String> pubDocs, pubLecs;

    public HomeSliderObj(String userName, String userMail, String philosophy, String userImage, String backgroundImage, String userId, String institution) {
        this.userName = userName;
        this.userMail = userMail;
        this.philosophy = philosophy;
        this.userImage = userImage;
        this.backgroundImage = backgroundImage;
        this.userId = userId;
        this.institution = institution;
    }

    public HomeSliderObj(String userName, String userMail, String philosophy, String userImage, String backgroundImage,
                         String userId, String institution, String students, String coaches, String school) {
        this.userName = userName;
        this.userMail = userMail;
        this.philosophy = philosophy;
        this.userImage = userImage;
        this.backgroundImage = backgroundImage;
        this.userId = userId;
        this.institution = institution;
        this.students = students;
        this.coaches = coaches;
        this.school = school;
    }

    public HomeSliderObj(String userName, String userMail, String philosophy, String userImage, String backgroundImage,
                         String userId, String institution, String students, String coaches, String school, String flag) {
        this.userName = userName;
        this.userMail = userMail;
        this.philosophy = philosophy;
        this.userImage = userImage;
        this.backgroundImage = backgroundImage;
        this.userId = userId;
        this.institution = institution;
        this.students = students;
        this.coaches = coaches;
        this.school = school;
        this.flag = flag;
    }

    public HomeSliderObj(String userName, String userMail, String philosophy, String userImage, String backgroundImage,
                         String userId, String institution, String students, String coaches, String school, List<String> pubDocs, List<String> pubLecs) {
        this.userName = userName;
        this.userMail = userMail;
        this.philosophy = philosophy;
        this.userImage = userImage;
        this.backgroundImage = backgroundImage;
        this.userId = userId;
        this.institution = institution;
        this.students = students;
        this.coaches = coaches;
        this.school = school;
        this.pubDocs = pubDocs;
        this.pubLecs = pubLecs;
    }

    public HomeSliderObj(String philosophy, String userId, String articleLink) {
        this.philosophy = philosophy;
        this.userId = userId;
        this.articleLink = articleLink;
    }

    public String getUserName() {
        return userName;
    }

    public String getInstitution() {
        return institution;
    }

    public String getSchool() {
        return school;
    }

    public String getUserMail() {
        return userMail;
    }

    public String getPhilosophy() {
        return philosophy;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public String getUserId() {
        return userId;
    }

    public String getArticleLink() {
        return articleLink;
    }

    public List<String> getPubDocs() {
        return pubDocs;
    }

    public List<String> getPubLecs() {
        return pubLecs;
    }
}
