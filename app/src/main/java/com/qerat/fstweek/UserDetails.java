package com.qerat.fstweek;

public class UserDetails {
    private String email, age, level, phnNo, name;
    private Boolean mentorship;

    UserDetails(String name, String age, String phnNo, String level, String email, Boolean mentorship) {
        this.email = email;
        this.age = age;
        this.level = level;
        this.phnNo = phnNo;
        this.name = name;
        this.mentorship = mentorship;
    }

    UserDetails() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPhnNo() {
        return phnNo;
    }

    public void setPhnNo(String phnNo) {
        this.phnNo = phnNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getMentorship() {
        return mentorship;
    }

    public void setMentorship(Boolean mentorship) {
        this.mentorship = mentorship;
    }
}
