package com.microsys.imbcloudlive.model;

import com.tencent.rtmp.TXLivePlayer;

public class ListTxPlayerModel {
    /**
     * 播放地址
     */
    private String playUrl="";
    /**
     * 对应的列表的位置
     */
    private int position=-1;
    /**
     * 对应的播放器
     */
    private TXLivePlayer txLivePlayer;

    public String getPlayUrl() {
        return playUrl == null ? "" : playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public TXLivePlayer getTxLivePlayer() {
        return txLivePlayer;
    }

    public void setTxLivePlayer(TXLivePlayer txLivePlayer) {
        this.txLivePlayer = txLivePlayer;
    }
}
