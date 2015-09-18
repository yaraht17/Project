package com.fourlines.model;

public class SickType {
    private int id;
    private int imgResID;
    private String nameSickType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImgResID() {
        return imgResID;
    }

    public void setImgResID(int imgResID) {
        this.imgResID = imgResID;
    }

    public String getNameSickType() {
        return nameSickType;
    }

    public void setNameSickType(String nameSickType) {
        this.nameSickType = nameSickType;
    }

    public SickType(int id, int imgResID, String nameSickType) {

        this.id = id;
        this.imgResID = imgResID;
        this.nameSickType = nameSickType;
    }
}
