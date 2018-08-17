package com.microsys.imbcloudlive.model;

import com.tencent.rtmp.ui.TXCloudVideoView;

public class ScreenTypeAndView {
    private TXCloudVideoView txCloudVideoView;
    private String cloudViewPosition = "";

    public String getCloudViewPosition() {
        return cloudViewPosition == null ? "" : cloudViewPosition;
    }

    public void setCloudViewPosition(String cloudViewPosition) {
        this.cloudViewPosition = cloudViewPosition;
    }

    public TXCloudVideoView getTxCloudVideoView() {
        return txCloudVideoView;
    }

    public void setTxCloudVideoView(TXCloudVideoView txCloudVideoView) {
        this.txCloudVideoView = txCloudVideoView;
    }


}
