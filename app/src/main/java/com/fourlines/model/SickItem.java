package com.fourlines.model;

import java.io.Serializable;
import java.util.ArrayList;

public class SickItem implements Serializable {
    private String id;
    private String name;
    private String image;
    private String type;
    private String reason;
    private ArrayList<String> foods;
    private ArrayList<String> banFoods;
    private ArrayList<String> symptoms;

    public SickItem(String id, String name, String type, String reason, ArrayList<String> foods, ArrayList<String> banFoods, ArrayList<String> symptoms) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.reason = reason;
        this.foods = foods;
        this.banFoods = banFoods;
        this.symptoms = symptoms;
    }

    public SickItem(String id, String name, String image, String type, String reason, ArrayList<String> foods, ArrayList<String> banFoods, ArrayList<String> symptoms) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.type = type;
        this.reason = reason;
        this.foods = foods;
        this.banFoods = banFoods;
        this.symptoms = symptoms;
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

    public ArrayList<String> getFoods() {
        return foods;
    }

    public void setFoods(ArrayList<String> foods) {
        this.foods = foods;
    }

    public ArrayList<String> getBanFoods() {
        return banFoods;
    }

    public void setBanFoods(ArrayList<String> banFoods) {
        this.banFoods = banFoods;
    }

    public ArrayList<String> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(ArrayList<String> symptoms) {
        this.symptoms = symptoms;
    }
}
