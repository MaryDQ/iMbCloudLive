package com.microsys.imbcloudlive.model;

public class PlayViewInfoModel {
    private ListTxPlayerModel txPlayerModel;
    private ScreenTypeAndView screenTypeAndView;

    public ListTxPlayerModel getTxPlayerModel() {
        return txPlayerModel;
    }

    public void setTxPlayerModel(ListTxPlayerModel txPlayerModel) {
        this.txPlayerModel = txPlayerModel;
    }

    public ScreenTypeAndView getScreenTypeAndView() {
        return screenTypeAndView;
    }

    public void setScreenTypeAndView(ScreenTypeAndView screenTypeAndView) {
        this.screenTypeAndView = screenTypeAndView;
    }
}
