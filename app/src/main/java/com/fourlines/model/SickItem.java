package com.fourlines.model;

import java.io.Serializable;
import java.util.ArrayList;

public class SickItem implements Serializable {
    private String id;
    private String name;
    private String image;
    private String type;
    private String reason;
    private String foods;
    private String banFoods;
    private ArrayList<String> symptoms;
    private String treatment;
    private String description;
    private String prevention;

    public SickItem(String id, String name, String type, String reason,
                    String foods, String banFoods, ArrayList<String> symptoms, String treatment, String description, String prevention) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.reason = reason;
        this.foods = foods;
        this.banFoods = banFoods;
        this.symptoms = symptoms;
        this.treatment = treatment;
        this.prevention = prevention;
        this.description = description;
    }

    public SickItem(String id, String name, String image, String type, String reason,
                    String foods, String banFoods, ArrayList<String> symptoms, String treatment, String description, String prevention) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.type = type;
        this.reason = reason;
        this.foods = foods;
        this.banFoods = banFoods;
        this.symptoms = symptoms;
        this.treatment = treatment;
        this.prevention = prevention;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrevention() {
        return prevention;
    }

    public void setPrevention(String prevention) {
        this.prevention = prevention;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getFoods() {
        return foods;
    }

    public void setFoods(String foods) {
        this.foods = foods;
    }

    public String getBanFoods() {
        return banFoods;
    }

    public void setBanFoods(String banFoods) {
        this.banFoods = banFoods;
    }

    public ArrayList<String> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(ArrayList<String> symptoms) {
        this.symptoms = symptoms;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }
}
