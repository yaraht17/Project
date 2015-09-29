package com.fourlines.model;

import java.util.ArrayList;

/**
 * Created by Admin on 9/29/2015.
 */
public class HistoryItem {
    private UserItem userItem;
    private ArrayList<SickItem> sicks;
    private ArrayList<String> datetimes;

    public HistoryItem() {
    }

    public HistoryItem(UserItem userItem, ArrayList<SickItem> sicks, ArrayList<String> datetimes) {
        this.userItem = userItem;
        this.sicks = sicks;
        this.datetimes = datetimes;
    }

    public UserItem getUserItem() {
        return userItem;
    }

    public void setUserItem(UserItem userItem) {
        this.userItem = userItem;
    }

    public ArrayList<SickItem> getSicks() {
        return sicks;
    }

    public void setSicks(ArrayList<SickItem> sicks) {
        this.sicks = sicks;
    }

    public ArrayList<String> getDatetimes() {
        return datetimes;
    }

    public void setDatetimes(ArrayList<String> datetimes) {
        this.datetimes = datetimes;
    }
}
