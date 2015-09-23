package com.fourlines.model;

/**
 * Created by Admin on 9/23/2015.
 */
public class InfoItem {
    private String image;
    private String content;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public InfoItem(String image, String content) {

        this.image = image;
        this.content = content;
    }
}
