package com.fourlines.model;

public class AccountItem {

    private String user;
    private String password;
    private String fullname;

    public AccountItem(String user, String password, String fullname) {
        this.user = user;
        this.password = password;
        this.fullname = fullname;
    }

    public AccountItem(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
