package com.qerat.fstweek;

import java.util.ArrayList;

public class ConferenceInformation {
    private String tier, jobDes;
    private ArrayList<String> areaInterest;


    ConferenceInformation(){

    }

    ConferenceInformation(String tier, String jobDes, ArrayList<String> areaInterest){
        this.tier=tier;
        this.jobDes=jobDes;
        this.areaInterest=areaInterest;
    }
    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getJobDes() {
        return jobDes;
    }

    public void setJobDes(String jobDes) {
        this.jobDes = jobDes;
    }

    public ArrayList<String> getAreaInterest() {
        return areaInterest;
    }

    public void setAreaInterest(ArrayList<String> areaInterest) {
        this.areaInterest = areaInterest;
    }
}
