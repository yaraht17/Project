package com.fourlines.model;

public class NotificationItem {
    private String id;
    private int imgID;
    private String title;
    private String body;

    public NotificationItem(String id, int imgID, String title, String time, String body) {
        this.id = id;
        this.imgID = imgID;
        this.title = title;
        this.body = body;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getImgID() {
        return imgID;
    }

    public void setImgID(int imgID) {
        this.imgID = imgID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
