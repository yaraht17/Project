package com.fourlines.model;

/**
 * Created by Admin on 9/29/2015.
 */
public class SickHistoryItem {
    private SickItem sickItem;
    private String datetime;

    public SickHistoryItem() {

    }

    public SickHistoryItem(SickItem sickItem, String datetime) {
        this.sickItem = sickItem;
        this.datetime = datetime;
    }

    public SickItem getSickItem() {
        return sickItem;
    }

    public void setSickItem(SickItem sickItem) {
        this.sickItem = sickItem;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
