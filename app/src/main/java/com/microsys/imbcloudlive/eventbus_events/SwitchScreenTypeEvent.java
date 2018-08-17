package com.microsys.imbcloudlive.eventbus_events;

public class SwitchScreenTypeEvent {
    private int screenType;
    private boolean isRefresh;

    public int getScreenType() {
        return screenType;
    }

    public void setScreenType(int screenType) {
        this.screenType = screenType;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }

    public SwitchScreenTypeEvent(int screenType, boolean isRefresh) {
        this.screenType = screenType;
        this.isRefresh = isRefresh;
    }
}
