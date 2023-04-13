package com.midland.ynote.Objects;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable{

    private String id;
    private String userName;
    private String aliasName;
    private String fullName;
    private String imageUrl, profileUrl, coverArt;
    private String email;
    private String school;
    private String course;
    private String coaches;
    private String students;
    private String CQ;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    private String phoneNumber;
    private String institution;
    private String about;
    private String gender;
    private String uid;

    public User(String alias, String fullName, String profileUrl, String coverArt,
                String institution, String school, String course, String about, String registeredAs,
                String studentsStr, String coachesStr, String gender, String cQ) {
        this.aliasName = alias;
        this.fullName = fullName;
        this.profileUrl = profileUrl;
        this.coverArt = coverArt;
        this.institution = institution;
        this.school = school;
        this.course = course;
        this.about = about;
        this.registeredAs = registeredAs;
        this.students = studentsStr;
        this.coaches = coachesStr;
        this.gender = gender;
        this.CQ = cQ;
    }

    public User(String fullName, String profileUrl, String course, String coaches, String students, String institution, String registeredAs, String uiD, String school, String about) {
        this.fullName = fullName;
        this.profileUrl = profileUrl;
        this.course = course;
        this.coaches = coaches;
        this.students = students;
        this.institution = institution;
        this.registeredAs = registeredAs;
        this.id = uiD;
        this.school = school;
        this.about = about;
    }

    public User(String alias, String fullName, String profileUrl, String coverArt, String institution, String school, String course, String about, ArrayList<String> registeredAs, String studentsStr, String coachesStr, String gender, String cQ) {
        this.aliasName = alias;
        this.fullName = fullName;
        this.profileUrl = profileUrl;
        this.coverArt = coverArt;
        this.institution = institution;
        this.school = school;
        this.course = course;
        this.about = about;
        this.registeredAs1 = registeredAs;
        this.students = studentsStr;
        this.coaches = coachesStr;
        this.gender = gender;
        this.CQ = cQ;
    }

    public User(String alias, String fullName, String profileUrl, String coverArt, String institution,
                String school, String course, String about, ArrayList<String> registeredAs, String studentsStr,
                String coachesStr, String gender, String cQ, String phoneNumber, String uid) {
        this.aliasName = alias;
        this.fullName = fullName;
        this.profileUrl = profileUrl;
        this.coverArt = coverArt;
        this.institution = institution;
        this.school = school;
        this.course = course;
        this.about = about;
        this.registeredAs1 = registeredAs;
        this.students = studentsStr;
        this.coaches = coachesStr;
        this.gender = gender;
        this.CQ = cQ;
        this.phoneNumber = phoneNumber;
        this.uid = uid;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getCoverArt() {
        return coverArt;
    }

    public void setCoverArt(String coverArt) {
        this.coverArt = coverArt;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getRegisteredAs() {
        return registeredAs;
    }

    public void setRegisteredAs(String registeredAs) {
        this.registeredAs = registeredAs;
    }

    private String registeredAs;
    private ArrayList<String> registeredAs1;


    public User(String id, String aliasName, String fullName,
                String imageUrl, String email, String institution, String school, String course, String coaches, String students, String CQ) {
        this.id = id;
        this.fullName = fullName;
        this.aliasName = aliasName;
        this.imageUrl = imageUrl;
        this.email = email;
        this.school = school;
        this.course = course;
        this.coaches = coaches;
        this.students = students;
        this.CQ = CQ;
        this.institution = institution;
    }



//    public User(String id, String fullName, String aliasName, String bio, String mainField, String subFields){
//        this.id = id;
//        this.fullName = fullName;
//        this.aliasName = aliasName;
//        this.bio = bio;
//        this.mainField = mainField;
//        this.subFields = subFields;
//    }

    public User(String alias, String fullName, String profileUrl, String coverArt, String institution, String school, String course, String about, String registeredAs){
        this.aliasName = alias;
        this.fullName = fullName;
        this.profileUrl = profileUrl;
        this.coverArt = coverArt;
        this.institution = institution;
        this.school = school;
        this.course = course;
        this.about = about;
        this.registeredAs = registeredAs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getCoaches() {
        return coaches;
    }

    public void setCoaches(String coaches) {
        this.coaches = coaches;
    }

    public String getStudents() {
        return students;
    }

    public void setStudents(String students) {
        this.students = students;
    }

    public String getCQ() {
        return CQ;
    }

    public void setCQ(String CQ) {
        this.CQ = CQ;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUid() {
        return uid;
    }
}
