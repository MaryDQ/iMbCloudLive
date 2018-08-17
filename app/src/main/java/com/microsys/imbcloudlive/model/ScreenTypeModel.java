package com.microsys.imbcloudlive.model;

public class ScreenTypeModel {
    private int screenType;
    private String screenTypeName;
    private int imageId;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getScreenType() {
        return screenType;
    }

    public void setScreenType(int screenType) {
        this.screenType = screenType;
    }

    public String getScreenTypeName() {
        return screenTypeName == null ? "" : screenTypeName;
    }

    public void setScreenTypeName(String screenTypeName) {
        this.screenTypeName = screenTypeName;
    }
}
