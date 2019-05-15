package com.qerat.fstweek;

public class UserDetails {
    private String email, age, level, phnNo, name;

    private ConferenceInformation conferenceInformation;
    private MentorshipInformation mentorshipInformation;

    UserDetails(String name, String age, String phnNo, String level, String email) {
        this.email = email;
        this.age = age;
        this.level = level;
        this.phnNo = phnNo;
        this.name = name;
    }

    UserDetails(String name, String age, String phnNo, String level, String email,  ConferenceInformation conferenceInformation) {
        this.email = email;
        this.age = age;
        this.level = level;
        this.phnNo = phnNo;
        this.name = name;

        this.conferenceInformation=conferenceInformation;
    }
    UserDetails(String name, String age, String phnNo, String level, String email,  MentorshipInformation mentorshipInformation) {
        this.email = email;
        this.age = age;
        this.level = level;
        this.phnNo = phnNo;
        this.name = name;

        this.mentorshipInformation=mentorshipInformation;
    }

    UserDetails(String name, String age, String phnNo, String level, String email,  ConferenceInformation conferenceInformation, MentorshipInformation mentorshipInformation) {
        this.email = email;
        this.age = age;
        this.level = level;
        this.phnNo = phnNo;
        this.name = name;
        this.mentorshipInformation=mentorshipInformation;
        this.conferenceInformation=conferenceInformation;
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



    public ConferenceInformation getConferenceInformation() {
        return conferenceInformation;
    }

    public void setConferenceInformation(ConferenceInformation conferenceInformation) {
        this.conferenceInformation = conferenceInformation;
    }

    public MentorshipInformation getMentorshipInformation() {
        return mentorshipInformation;
    }

    public void setMentorshipInformation(MentorshipInformation mentorshipInformation) {
        this.mentorshipInformation = mentorshipInformation;
    }
}
