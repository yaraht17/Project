package com.fourlines.model;


public class MedicalHistory {
    private String id;
    private String name;
    private String datetime;

    public MedicalHistory(String id, String name, String datetime) {
        this.id = id;
        this.name = name;
        this.datetime = datetime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

}
