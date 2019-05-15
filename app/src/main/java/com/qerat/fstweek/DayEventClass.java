package com.qerat.fstweek;

import android.app.usage.EventStats;

import java.util.Map;

public class DayEventClass {
    private Map<String, EventClass> map;

    DayEventClass(){

    }

    DayEventClass(Map<String,EventClass> map){
        this.map=map
                ;
    }

    public Map<String, EventClass> getMap() {
        return map;
    }

    public void setMap(Map<String, EventClass> map) {
        this.map = map;
    }
}
