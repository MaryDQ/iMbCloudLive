package com.microsys.imbcloudlive.eventbus_events;

public class TransformCurPlayModelEvent {
    private boolean refresh=false;

    public TransformCurPlayModelEvent(boolean refresh) {
        this.refresh = refresh;
    }

    public boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }


}
