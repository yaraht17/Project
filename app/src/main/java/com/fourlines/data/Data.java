package com.fourlines.data;

import com.fourlines.model.NotificationItem;
import com.fourlines.model.SickHistoryItem;
import com.fourlines.model.SickItem;
import com.fourlines.model.UserItem;

import java.util.ArrayList;

public class Data {
    public static ArrayList<SickItem>[] sicks = new ArrayList[6];
    public static ArrayList<SickItem> sickList;
    public static UserItem user;
    public static ArrayList<NotificationItem> notifList;

    public static ArrayList<SickHistoryItem> sickHistoryList;
}
