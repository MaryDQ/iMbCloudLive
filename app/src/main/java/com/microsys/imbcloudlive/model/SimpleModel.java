package com.microsys.imbcloudlive.model;

import com.tencent.rtmp.TXLivePlayer;

public class SimpleModel {
    private String picUrl;
    private String content;
    private TXLivePlayer txLivePlayer;

    public TXLivePlayer getTxLivePlayer() {
        return txLivePlayer;
    }

    public void setTxLivePlayer(TXLivePlayer txLivePlayer) {
        this.txLivePlayer = txLivePlayer;
    }

    public String getPicUrl() {
        return picUrl == null ? "" : picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getContent() {
        return content == null ? "" : content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
