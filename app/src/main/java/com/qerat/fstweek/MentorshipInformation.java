package com.qerat.fstweek;

import java.util.ArrayList;

public class MentorshipInformation {
    private String purpose;
    private Boolean mentorship = false, crossDisciplinary=false;
    private String areaStudy;


    MentorshipInformation() {

    }

    MentorshipInformation(Boolean mentorship, String purpose, Boolean crossDisciplinary, String areaStudy) {
        this.mentorship = mentorship;
        if(!mentorship){
            this.purpose = null;
            this.areaStudy = null;
            this.crossDisciplinary = null;
        }else {
            this.purpose = purpose;
            this.areaStudy = areaStudy;
            this.crossDisciplinary = crossDisciplinary;
        }
    }


    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Boolean getMentorship() {
        return mentorship;
    }

    public void setMentorship(Boolean mentorship) {
        this.mentorship = mentorship;
    }


    public String getAreaStudy() {
        return areaStudy;
    }

    public void setAreaStudy(String areaStudy) {
        this.areaStudy = areaStudy;
    }
}
