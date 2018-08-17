package com.microsys.imbcloudlive.model;

public class LivePlayType {
    /**
     * 一画面，整个屏幕
     */
    public static final int SCREEN_SINGLE = 100;
    /**
     * 一大画面，右下小屏幕
     */
    public static final int SCREEN_SINGLE_AND_RIGHT_DONW = 201;
    /**
     * 一大画面，左下小屏幕
     */
    public static final int SCREEN_SINGLE_AND_LEFT_DOWN = 202;
    /**
     * 一大画面，右上小屏幕
     */
    public static final int SCREEN_SINGLE_AND_RIGHT_TOP = 203;
    /**
     * 一大画面，左上小屏幕
     */
    public static final int SCREEN_SINGLE_AND_LEFT_TOP = 204;
    /**
     * 上下黑屏，中间两屏幕
     */
    public static final int SCREEN_DOUBLE_CENTER = 205;
    /**
     * 一大画面，右边中间两小屏幕
     */
    public static final int SCREEN_SINGLE_AND_TWO_RIGHT = 301;
    /**
     * 一大画面，右上和右下小屏幕
     */
    public static final int SCREEN_SINGLE_AND_RIGHT_UP_AND_RIGHT_DOWN = 302;
    /**
     * 一大画面，左下和右下小屏幕
     */
    public static final int SCREEN_SINGLE_AND_LEFT_DOWN_AND_RIGHT_DOWN = 303;
    /**
     * 一大画面，中下和右下小屏幕
     */
    public static final int SCREEN_SINGLE_AND_CENTER_DOWN_AND_RIGHT_DOWN = 304;
    /**
     * 半个画面，右上和右下平分另一半画面
     */
    public static final int SCREEN_HALF_SINGLE_AND_RIGHT_UP_AND_RIGHT_DOWN = 305;
    /**
     * 一大画面，右上和中右和右下小屏幕
     */
    public static final int SCREEN_SINGLE_AND_RIGHT_UP_AND_CENTER_RIGHT_AND_RIGHT_DOWN = 401;
    /**
     * 一大画面，左下和中下和右下小屏幕
     */
    public static final int SCREEN_SINGLE_AND_LEFT_DOWN_AND_CENTER_DOWN_AND_RIGHT_DOWN = 402;
    /**
     * 画面四分
     */
    public static final int SCREEN_QUADRUPLE = 403;
    /**
     * 一大画面，左上和左下和右上和右下小屏幕
     */
    public static final int SCREEN_SINGLE_AND_FOUR_CORNERS = 500;
    /**
     * 一较大画面，剩余画面被5块小屏幕平分
     */
    public static final int SCREEN_SINGLE_AND_FIVE_SMALL_SCREENS = 600;
    /**
     * 一较大画面，右下小屏幕，右上和中右黑屏
     */
    public static final int SCREEN_THREE_QUARTERS_AND_RIGHT_DOWN = 001;
    /**
     * 一较大画面，右上和右下小屏幕，中右黑屏
     */
    public static final int SCREEN_HALF_AND_RIGHT_CENTER_BLACK_AND_RIGHT_TOP_AND_RIGHT_DOWN = 002;
    /**
     * 半个画面，右上和中右和右下平分剩余的画面
     */
    public static final int SCREEN_HALF_AND_RIGHT_UP_AND_CENTER_RIGHT_AND_RIGHT_DOWN = 003;

}
