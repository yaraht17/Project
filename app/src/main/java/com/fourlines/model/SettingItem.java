package com.fourlines.model;


public class SettingItem {
    private String nameSettings;
    private String desSettings;
    private boolean status;

    public String getNameSettings() {
        return nameSettings;
    }

    public void setNameSettings(String nameSettings) {
        this.nameSettings = nameSettings;
    }

    public String getDesSettings() {
        return desSettings;
    }

    public void setDesSettings(String desSettings) {
        this.desSettings = desSettings;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public SettingItem(String nameSettings, String desSettings, boolean status) {

        this.nameSettings = nameSettings;
        this.desSettings = desSettings;
        this.status = status;
    }
}
