package com.fourlines.model;

import java.io.Serializable;

public class UserItem implements Serializable {
    private String id;
    private String email;
    private String fullname;
    private String avatarUrl;

    public UserItem(String id, String email, String fullname, String avatarUrl) {
        this.id = id;
        this.email = email;
        this.fullname = fullname;
        this.avatarUrl = avatarUrl;
    }

    public UserItem(String id, String email, String fullname) {
        this.id = id;
        this.email = email;
        this.fullname = fullname;
    }

    public UserItem() {

    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }


}
