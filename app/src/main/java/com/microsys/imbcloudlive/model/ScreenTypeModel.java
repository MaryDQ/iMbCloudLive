package com.microsys.imbcloudlive.model;

public class ScreenTypeModel {
    private String screenType;
    private int imageId;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getScreenType() {
        return screenType == null ? "" : screenType;
    }

    public void setScreenType(String screenType) {
        this.screenType = screenType;
    }
}
