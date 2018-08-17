package com.microsys.imbcloudlive.ui.activitys;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.microsys.imbcloudlive.R;
import com.microsys.imbcloudlive.base.BaseNoTitleActivity;
import com.microsys.imbcloudlive.common.Constants;
import com.microsys.imbcloudlive.di.component.AppComponent;
import com.microsys.imbcloudlive.eventbus_events.SwitchScreenTypeEvent;
import com.microsys.imbcloudlive.eventbus_events.TransformCurPlayModelEvent;
import com.microsys.imbcloudlive.model.ListTxPlayerModel;
import com.microsys.imbcloudlive.model.LivePlayType;
import com.microsys.imbcloudlive.model.PlayViewInfoModel;
import com.microsys.imbcloudlive.model.ScreenTypeAndView;
import com.microsys.imbcloudlive.ui.adapters.RecyclerTabLyoutAdapter;
import com.microsys.imbcloudlive.ui.adapters.RecyclerViewPagerAdapter;
import com.microsys.imbcloudlive.ui.fragments.HomeFragment;
import com.microsys.imbcloudlive.ui.fragments.SelectScreenFragment;
import com.microsys.imbcloudlive.widget.RecyclerTabLayout;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeActivity extends BaseNoTitleActivity {
    private static final String[] CONTENT = new String[]{"视频源", "画面", "图片", "文字", "部件", "图片", "文字", "部件"};
    @BindView(R.id.home_viewpager)
    ViewPager mHomeViewPager;
    @BindView(R.id.home_indicator)
    RecyclerTabLayout mHomeIndicator;
    @BindView(R.id.tvLeft)
    TextView mTvLeft;
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    private List<String> mList = Arrays.asList(CONTENT);
    private RecyclerTabLyoutAdapter mRecyclerTabLyoutAdapter;
    private RecyclerViewPagerAdapter mRecyclerViewPagerAdapter;

    private ArrayList<Fragment> fragments = new ArrayList<>();

    private FragmentPagerAdapter mFragmentPagerAdapter;


    /**
     * 当前的播放控件和模式绑定在一起
     */
    private List<ScreenTypeAndView> curScreenTypeAndViewsList = new ArrayList<>();
    /**
     * 屏幕默认的播放模式
     */
    private int curScreenType = LivePlayType.SCREEN_SINGLE;

    /**
     * 顶部播放器的高度和宽度
     */
    private int itemFullHeight, itemFullWidth;
    private Map<String, View> screenViewMap = new HashMap<>(6);
    /**
     * 当前正在播放的View和播放器绑定在一起
     */
    private List<PlayViewInfoModel> mPlayViewInfoModelList = new ArrayList<>();
    /**
     * TXPlayer播放配置
     */
    private TXLivePlayConfig mPlayConfig;

    @Override
    public int getRootViewId() {
        return R.layout.activity_home;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initViewAndData() {
        initFragment();
        initRecyclerTabLayout();
        initViews();
        initTXPlayConfig();
    }

    private void initFragment() {
        HomeFragment mHomeFragment;
        SelectScreenFragment mSelectScreenFragment = new SelectScreenFragment(0);
        mSelectScreenFragment.setTabPos(0);
        fragments.add(0, mSelectScreenFragment);
        for (int i = 1; i < CONTENT.length; i++) {
            mHomeFragment = new HomeFragment(1);
            mHomeFragment.setTabPos(i);
            fragments.add(i, mHomeFragment);
        }
    }

    private void initRecyclerTabLayout() {
        mRecyclerViewPagerAdapter = new RecyclerViewPagerAdapter(getSupportFragmentManager(), fragments);
        mHomeViewPager.setAdapter(mRecyclerViewPagerAdapter);

        mRecyclerTabLyoutAdapter = new RecyclerTabLyoutAdapter(mHomeViewPager, mList);
        mHomeIndicator.setUpWithAdapter(mRecyclerTabLyoutAdapter);
        mHomeIndicator.setPositionThreshold(0.5f);
        mHomeViewPager.setCurrentItem(0);
        mHomeViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // TODO: 2018/7/25 可能需要有多个fragment进行判断
                if (position > 0) {
                    HomeFragment homeFragment = (HomeFragment) fragments.get(position);
                    homeFragment.sendMessage();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initViews() {
        mTvLeft.setText("关闭");
        mTvTitle.setText("易动导播");
        final View view = findViewById(R.id.view_video_view);
        view.post(new Runnable() {
            @Override
            public void run() {
                itemFullHeight = view.getHeight();
                itemFullWidth = view.getWidth();
            }
        });

    }

    private void initTXPlayConfig() {
        mPlayConfig = new TXLivePlayConfig();
        mPlayConfig.setAutoAdjustCacheTime(true);
        mPlayConfig.setMinAutoAdjustCacheTime(1);
        mPlayConfig.setMaxAutoAdjustCacheTime(1);
        chooseLayoutByPlayType(curScreenType);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void switchScreenType(SwitchScreenTypeEvent event) {
        if (event.isRefresh()) {
            chooseLayoutByPlayType(event.getScreenType());
        }
    }

    /**
     * 根据选择的播放模式，动态加载布局
     *
     * @param type 播放模式
     */
    private void chooseLayoutByPlayType(int type) {
        curScreenType = type;
        TXCloudVideoView screenView00 = findViewById(R.id.txVideoViewScreen00);
        TXCloudVideoView screenView01 = findViewById(R.id.txVideoViewScreen01);
        TXCloudVideoView screenView02 = findViewById(R.id.txVideoViewScreen02);
        TXCloudVideoView screenView03 = findViewById(R.id.txVideoViewScreen03);
        TXCloudVideoView screenView04 = findViewById(R.id.txVideoViewScreen04);
        TXCloudVideoView screenView05 = findViewById(R.id.txVideoViewScreen05);
        FrameLayout.LayoutParams screenParams00 = (FrameLayout.LayoutParams) screenView00.getLayoutParams();
        FrameLayout.LayoutParams screenParams01 = (FrameLayout.LayoutParams) screenView01.getLayoutParams();
        FrameLayout.LayoutParams screenParams02 = (FrameLayout.LayoutParams) screenView02.getLayoutParams();
        FrameLayout.LayoutParams screenParams03 = (FrameLayout.LayoutParams) screenView03.getLayoutParams();
        FrameLayout.LayoutParams screenParams04 = (FrameLayout.LayoutParams) screenView04.getLayoutParams();
        FrameLayout.LayoutParams screenParams05 = (FrameLayout.LayoutParams) screenView05.getLayoutParams();
        if (screenViewMap.size() < 6) {
            screenViewMap.put("00", screenView00);
            screenViewMap.put("01", screenView01);
            screenViewMap.put("02", screenView02);
            screenViewMap.put("03", screenView03);
            screenViewMap.put("04", screenView04);
            screenViewMap.put("05", screenView05);
        }

        switch (type) {
            case LivePlayType.SCREEN_SINGLE:

                initScreenView00(screenParams00, screenView00);
                screenVisibility("00");

                break;
            case LivePlayType.SCREEN_SINGLE_AND_RIGHT_DONW:

                initScreenView00(screenParams00, screenView00);
                initScreenViewRightDownWithMargins01(screenParams01, screenView01, Color.GREEN);
                screenVisibility("00", "01");

                break;
            case LivePlayType.SCREEN_SINGLE_AND_LEFT_DOWN:

                initScreenView00(screenParams00, screenView00);
                initScreenViewLeftDownWithMargins01(screenParams01, screenView01, Color.GREEN);
                screenVisibility("00", "01");

                break;
            case LivePlayType.SCREEN_SINGLE_AND_RIGHT_TOP:

                initScreenView00(screenParams00, screenView00);
                initScreenViewRightTopWithMargins01(screenParams01, screenView01, Color.GREEN);
                screenVisibility("00", "01");

                break;
            case LivePlayType.SCREEN_SINGLE_AND_LEFT_TOP:

                initScreenView00(screenParams00, screenView00);
                initScreenViewLeftTopWithMargins01(screenParams01, screenView01, Color.GREEN);
                screenVisibility("00", "01");

                break;
            case LivePlayType.SCREEN_DOUBLE_CENTER:

                initScreenViewLeftCenterWithMargins00(screenParams00, screenView00);
                initScreenViewRightCenterWithMargins01(screenParams01, screenView01, Color.GREEN);
                screenVisibility("00", "01");

                break;
            case LivePlayType.SCREEN_SINGLE_AND_TWO_RIGHT:

                initScreenView00(screenParams00, screenView00);
                initScreenViewRightTopWithBigMargins01(screenParams02, screenView02, Color.GREEN);
                initScreenViewRightDownWithBigMargins02(screenParams01, screenView01, Color.BLUE);
                screenVisibility("00", "01", "02");

                break;
            case LivePlayType.SCREEN_SINGLE_AND_RIGHT_UP_AND_RIGHT_DOWN:

                initScreenView00(screenParams00, screenView00);
                initScreenViewRightTopWithMargins01(screenParams02, screenView02, Color.GREEN);
                initScreenViewRightDownWithMargins01(screenParams01, screenView01, Color.BLUE);
                screenVisibility("00", "01", "02");

                break;
            case LivePlayType.SCREEN_SINGLE_AND_LEFT_DOWN_AND_RIGHT_DOWN:

                initScreenView00(screenParams00, screenView00);
                initScreenViewLeftDownWithMargins01(screenParams02, screenView02, Color.GREEN);
                initScreenViewRightDownWithMargins01(screenParams01, screenView01, Color.BLUE);
                screenVisibility("00", "01", "02");

                break;
            case LivePlayType.SCREEN_SINGLE_AND_CENTER_DOWN_AND_RIGHT_DOWN:

                initScreenView00(screenParams00, screenView00);
                initScreenViewCenterDownWithMargins01(screenParams02, screenView02, Color.GREEN);
                initScreenViewRightDownWithMargins01(screenParams01, screenView01, Color.BLUE);
                screenVisibility("00", "01", "02");

                break;
            case LivePlayType.SCREEN_HALF_SINGLE_AND_RIGHT_UP_AND_RIGHT_DOWN:

                initScreenViewHalf00(screenParams00, screenView00);
                initScreenViewRightTopWithQuarterView01(screenParams02, screenView02, Color.GREEN);
                initScreenViewRightDownWithQuarterView01(screenParams01, screenView01, Color.BLUE);
                screenVisibility("00", "01", "02");

                break;
            case LivePlayType.SCREEN_SINGLE_AND_RIGHT_UP_AND_CENTER_RIGHT_AND_RIGHT_DOWN:

                initScreenView00(screenParams00, screenView00);
                initScreenViewRightTopWithMargins01(screenParams02, screenView02, Color.GREEN);
                initScreenViewCenterRightWithMargins01(screenParams03, screenView03, Color.BLUE);
                initScreenViewRightDownWithMargins01(screenParams01, screenView01, Color.YELLOW);
                screenVisibility("00", "01", "02", "03");

                break;
            case LivePlayType.SCREEN_SINGLE_AND_LEFT_DOWN_AND_CENTER_DOWN_AND_RIGHT_DOWN:

                initScreenView00(screenParams00, screenView00);
                initScreenViewLeftDownWithMargins01(screenParams02, screenView02, Color.GREEN);
                initScreenViewCenterDownWithMargins01(screenParams03, screenView03, Color.BLUE);
                initScreenViewRightDownWithMargins01(screenParams01, screenView01, Color.YELLOW);
                screenVisibility("00", "01", "02", "03");

                break;
            case LivePlayType.SCREEN_QUADRUPLE:

                initScreenViewLeftTopWithQuarterView01(screenParams00, screenView00, Color.RED);
                initScreenViewRightTopWithQuarterView01(screenParams01, screenView01, Color.GREEN);
                initScreenViewLeftDownWithQuarterView01(screenParams02, screenView02, Color.BLUE);
                initScreenViewRightDownWithQuarterView01(screenParams03, screenView03, Color.YELLOW);
                screenVisibility("00", "01", "02", "03");

                break;
            case LivePlayType.SCREEN_SINGLE_AND_FOUR_CORNERS:

                initScreenView00(screenParams00, screenView00);
                initScreenViewRightTopWithoutMargins01(screenParams03, screenView03, Color.GREEN);
                initScreenViewRightDownWithoutMargins01(screenParams01, screenView01, Color.BLUE);
                initScreenViewLeftTopWithoutMargins01(screenParams02, screenView02, Color.YELLOW);
                initScreenViewLeftDownWithoutMargins01(screenParams04, screenView04, Color.CYAN);
                screenVisibility("00", "01", "02", "03", "04");

                break;
            case LivePlayType.SCREEN_SINGLE_AND_FIVE_SMALL_SCREENS:

                initScreenViewFourNinethsLeftTop00(screenParams00, screenView00);
                initScreenViewRightTopWithoutMargins01(screenParams01, screenView01, Color.GREEN);
                initScreenViewCenterRightWithoutMargins01(screenParams02, screenView02, Color.BLUE);
                initScreenViewRightDownWithoutMargins01(screenParams05, screenView05, Color.YELLOW);
                initScreenViewCenterDownWithoutMargins01(screenParams04, screenView04, Color.CYAN);
                initScreenViewLeftDownWithoutMargins01(screenParams03, screenView03, Color.GRAY);
                screenVisibility("00", "01", "02", "03", "04", "05");

                break;
            case LivePlayType.SCREEN_THREE_QUARTERS_AND_RIGHT_DOWN:

                initScreenViewTwoThirdsLeftTop00(screenParams00, screenView00);
                initScreenViewRightDownWithoutMargins01(screenParams01, screenView01, Color.GREEN);
                screenVisibility("00", "01");

                break;
            case LivePlayType.SCREEN_HALF_AND_RIGHT_CENTER_BLACK_AND_RIGHT_TOP_AND_RIGHT_DOWN:

                initScreenViewHalf00(screenParams00, screenView00);
                initScreenViewSixthRightTop(screenParams02, screenView02, Color.GREEN);
                initScreenViewSixthRightDown(screenParams01, screenView01, Color.BLUE);
                screenVisibility("00", "01", "02");

                break;
            case LivePlayType.SCREEN_HALF_AND_RIGHT_UP_AND_CENTER_RIGHT_AND_RIGHT_DOWN:

                initScreenViewHalf00(screenParams00, screenView00);
                initScreenViewSixthRightTop(screenParams02, screenView02, Color.GREEN);
                initScreenViewSixthCenterRight(screenParams03, screenView03, Color.BLUE);
                initScreenViewSixthRightDown(screenParams01, screenView01, Color.YELLOW);
                screenVisibility("00", "01", "02", "03");

                break;
            default:
                break;
        }
        playRtmpVideos();
    }

    private void playRtmpVideos() {
        destroyCurPlayers();

        // TODO: 2018/8/17 播放视频
        for (int i = 0; i < curScreenTypeAndViewsList.size(); i++) {
            if (Constants.curFragListTxPlayerModel == null && Constants.curFragListTxPlayerModel.size() == 0) {
                return;
            }
            if (i < Constants.curFragListTxPlayerModel.size()) {
                final ScreenTypeAndView model = curScreenTypeAndViewsList.get(i);
                ListTxPlayerModel listTxPlayerModel = Constants.curFragListTxPlayerModel.get(i);
                PlayViewInfoModel playViewInfoModel = new PlayViewInfoModel();
                final TXCloudVideoView videoView = model.getTxCloudVideoView();
                final TXLivePlayer player = new TXLivePlayer(mContext);
                player.setConfig(mPlayConfig);
                player.setPlayerView(videoView);
                player.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
                player.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
                player.enableHardwareDecode(false);
                int result = player.startPlay(listTxPlayerModel.getPlayUrl(), TXLivePlayer.PLAY_TYPE_LIVE_RTMP);
                if (result == 0) {
                    ListTxPlayerModel playerModel = new ListTxPlayerModel();
                    playerModel.setTxLivePlayer(player);
                    playerModel.setPosition(listTxPlayerModel.getPosition());
                    playerModel.setPlayUrl(listTxPlayerModel.getPlayUrl());
                    //播放成功就将播放器和播放的控件绑定在一起
                    playViewInfoModel.setScreenTypeAndView(model);
                    playViewInfoModel.setTxPlayerModel(playerModel);
                    mPlayViewInfoModelList.add(playViewInfoModel);
                }
            }
        }
    }

    private void destroyCurPlayers(){
        if (mPlayViewInfoModelList != null && mPlayViewInfoModelList.size() > 0) {
            for (int i = 0; i < mPlayViewInfoModelList.size(); i++) {
                TXLivePlayer mLivePlayer = mPlayViewInfoModelList.get(i).getTxPlayerModel().getTxLivePlayer();
                TXCloudVideoView mPlayerView = mPlayViewInfoModelList.get(i).getScreenTypeAndView().getTxCloudVideoView();

                if (mLivePlayer != null) {
                    mLivePlayer.stopPlay(true);
                    mLivePlayer = null;
                }
                if (mPlayerView != null) {
                    mPlayerView.setVisibility(View.GONE);
                    mPlayerView.onDestroy();
                    mPlayerView = null;
                }
            }
            mPlayViewInfoModelList=new ArrayList<>();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveCurListPlay(TransformCurPlayModelEvent event){
        if (event.isRefresh()) {
            playRtmpVideos();
            chooseLayoutByPlayType(curScreenType);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyCurPlayers();
    }

    /**
     * 加载一个大画面
     */
    private void initScreenView00(FrameLayout.LayoutParams screenParams, TXCloudVideoView screenView) {
        screenParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
        screenParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
        screenParams.topMargin = 0;
        screenParams.leftMargin = 0;
        screenParams.rightMargin = 0;
        screenParams.bottomMargin = 0;
        screenView.setLayoutParams(screenParams);
        screenView.setVisibility(View.VISIBLE);
        screenView.setBackgroundColor(Color.RED);
    }

    private void screenVisibility(String... viewIds) {
        //将播放的控件和position绑定添加到list中

        curScreenTypeAndViewsList = new ArrayList<>();

        String[] ids = viewIds.clone();
        for (int j = 0; j < ids.length; j++) {

            View view = screenViewMap.get(ids[j]);
            view.setVisibility(View.VISIBLE);

            //将播放的控件和position绑定添加到list中
            ScreenTypeAndView model = new ScreenTypeAndView();
            model.setCloudViewPosition(ids[j]);
            model.setTxCloudVideoView(((TXCloudVideoView) view));
            curScreenTypeAndViewsList.add(model);

            screenViewMap.remove(ids[j]);
        }

        Iterator iterator = screenViewMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String id = (String) entry.getKey();
            View view = (View) entry.getValue();
            view.setVisibility(View.GONE);
        }
        screenViewMap = null;
        screenViewMap = new HashMap<>(6);

    }

    /**
     * 加载一个右下的具有Margin的小画面
     *
     * @param screenParams parms设置控件位置
     * @param screenView   控件对象
     */
    private void initScreenViewRightDownWithMargins01(FrameLayout.LayoutParams screenParams, TXCloudVideoView screenView, int colorId) {
        screenParams.height = itemFullHeight * 8 / 27;
        screenParams.width = itemFullWidth * 10 / 32;
        screenParams.rightMargin = 8;
        screenParams.bottomMargin = 8;
        screenParams.topMargin = 0;
        screenParams.leftMargin = 0;
        screenParams.gravity = Gravity.END | Gravity.BOTTOM;
        screenView.setLayoutParams(screenParams);
        screenView.setVisibility(View.VISIBLE);
        screenView.setBackgroundColor(colorId);
    }

    /**
     * 加载一个左下的具有Margin的小画面
     *
     * @param screenParams parms设置控件位置
     * @param screenView   控件对象
     */
    private void initScreenViewLeftDownWithMargins01(FrameLayout.LayoutParams screenParams, TXCloudVideoView screenView, int colorId) {
        screenParams.height = itemFullHeight * 8 / 27;
        screenParams.width = itemFullWidth * 10 / 32;
        screenParams.leftMargin = 8;
        screenParams.bottomMargin = 8;
        screenParams.topMargin = 0;
        screenParams.rightMargin = 0;
        screenParams.gravity = Gravity.START | Gravity.BOTTOM;
        screenView.setLayoutParams(screenParams);
        screenView.setVisibility(View.VISIBLE);
        screenView.setBackgroundColor(colorId);
    }

    /**
     * 加载一个右上的具有Margin的小画面
     *
     * @param screenParams parms设置控件位置
     * @param screenView   控件对象
     */
    private void initScreenViewRightTopWithMargins01(FrameLayout.LayoutParams screenParams, TXCloudVideoView screenView, int colorId) {
        screenParams.height = itemFullHeight * 8 / 27;
        screenParams.width = itemFullWidth * 10 / 32;
        screenParams.rightMargin = 8;
        screenParams.topMargin = 8;
        screenParams.leftMargin = 0;
        screenParams.bottomMargin = 0;
        screenParams.gravity = Gravity.END | Gravity.TOP;
        screenView.setLayoutParams(screenParams);
        screenView.setVisibility(View.VISIBLE);
        screenView.setBackgroundColor(colorId);
    }

    /**
     * 加载一个左上的具有Margin的小画面
     *
     * @param screenParams parms设置控件位置
     * @param screenView   控件对象
     */
    private void initScreenViewLeftTopWithMargins01(FrameLayout.LayoutParams screenParams, TXCloudVideoView screenView, int colorId) {
        screenParams.height = itemFullHeight * 8 / 27;
        screenParams.width = itemFullWidth * 10 / 32;
        screenParams.rightMargin = 0;
        screenParams.bottomMargin = 0;
        screenParams.topMargin = 8;
        screenParams.leftMargin = 8;
        screenParams.gravity = Gravity.START | Gravity.TOP;
        screenView.setLayoutParams(screenParams);
        screenView.setVisibility(View.VISIBLE);
        screenView.setBackgroundColor(colorId);
    }

    /**
     * 加载一个中左具有margin的较大画面
     *
     * @param screenParams parms设置控件位置
     * @param screenView   控件对象
     */
    private void initScreenViewLeftCenterWithMargins00(FrameLayout.LayoutParams screenParams, TXCloudVideoView screenView) {
        screenParams.height = itemFullHeight * 20 / 27;
        screenParams.width = itemFullWidth * 16 / 32;
        screenParams.topMargin = 0;
        screenParams.leftMargin = 0;
        screenParams.rightMargin = 0;
        screenParams.bottomMargin = 0;
        screenParams.gravity = Gravity.CENTER_VERTICAL | Gravity.START;
        screenView.setLayoutParams(screenParams);
        screenView.setVisibility(View.VISIBLE);
        screenView.setBackgroundColor(Color.RED);
    }

    /**
     * 加载一个中右具有margin的较大画面
     *
     * @param screenParams parms设置控件位置
     * @param screenView   控件对象
     */
    private void initScreenViewRightCenterWithMargins01(FrameLayout.LayoutParams screenParams, TXCloudVideoView screenView, int colorId) {
        screenParams.height = itemFullHeight * 20 / 27;
        screenParams.width = itemFullWidth * 16 / 32;
        screenParams.gravity = Gravity.CENTER_VERTICAL | Gravity.END;
        screenParams.topMargin = 0;
        screenParams.leftMargin = 0;
        screenParams.rightMargin = 0;
        screenParams.bottomMargin = 0;
        screenView.setLayoutParams(screenParams);
        screenView.setVisibility(View.VISIBLE);
        screenView.setBackgroundColor(colorId);
    }

    /**
     * 加载一个右上的具有较大Margin的小屏幕
     *
     * @param screenParams 控件params
     * @param screenView   控件
     * @param colorId      控件颜色
     */
    private void initScreenViewRightTopWithBigMargins01(FrameLayout.LayoutParams screenParams, TXCloudVideoView screenView, int colorId) {
        screenParams.height = itemFullHeight * 8 / 27;
        screenParams.width = itemFullWidth * 10 / 32;
        screenParams.rightMargin = 8;
        screenParams.topMargin = itemFullHeight / 8;
        screenParams.leftMargin = 0;
        screenParams.bottomMargin = 0;
        screenParams.gravity = Gravity.END | Gravity.TOP;
        screenView.setLayoutParams(screenParams);
        screenView.setVisibility(View.VISIBLE);
        screenView.setBackgroundColor(colorId);
    }

    /**
     * 加载一个右下的具有较大Margin的小屏幕
     *
     * @param screenParams 控件params
     * @param screenView   控件
     */
    private void initScreenViewRightDownWithBigMargins02(FrameLayout.LayoutParams screenParams, TXCloudVideoView screenView, int colorId) {
        screenParams.height = itemFullHeight * 8 / 27;
        screenParams.width = itemFullWidth * 10 / 32;
        screenParams.rightMargin = 8;
        screenParams.bottomMargin = itemFullHeight / 8;
        screenParams.topMargin = 0;
        screenParams.leftMargin = 0;
        screenParams.gravity = Gravity.END | Gravity.BOTTOM;
        screenView.setLayoutParams(screenParams);
        screenView.setVisibility(View.VISIBLE);
        screenView.setBackgroundColor(colorId);
    }

    /**
     * 加载一个中下的具有margin的小屏幕
     *
     * @param screenParams 控件params
     * @param screenView   控件
     * @param colorId      控件背景
     */
    private void initScreenViewCenterDownWithMargins01(FrameLayout.LayoutParams screenParams, TXCloudVideoView screenView, int colorId) {
        screenParams.height = itemFullHeight * 8 / 27;
        screenParams.width = itemFullWidth * 10 / 32;
        screenParams.bottomMargin = 8;
        screenParams.topMargin = 0;
        screenParams.leftMargin = 0;
        screenParams.rightMargin = 0;
        screenParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        screenView.setLayoutParams(screenParams);
        screenView.setVisibility(View.VISIBLE);
        screenView.setBackgroundColor(colorId);
    }

    /**
     * 加载一个半屏的画面
     *
     * @param screenParams 控件params
     * @param screenView   控件
     */
    private void initScreenViewHalf00(FrameLayout.LayoutParams screenParams, TXCloudVideoView screenView) {
        screenParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
        screenParams.width = itemFullWidth / 2;
        screenParams.topMargin = 0;
        screenParams.leftMargin = 0;
        screenParams.rightMargin = 0;
        screenParams.bottomMargin = 0;
        screenView.setLayoutParams(screenParams);
        screenView.setVisibility(View.VISIBLE);
        screenView.setBackgroundColor(Color.RED);
    }

    /**
     * 加载一个具有1/4屏幕右上大小的小画面
     *
     * @param screenParams 控件params
     * @param screenView   控件
     * @param colorId      控件背景
     */
    private void initScreenViewRightTopWithQuarterView01(FrameLayout.LayoutParams screenParams, TXCloudVideoView screenView, int colorId) {
        screenParams.height = itemFullHeight / 2;
        screenParams.width = itemFullWidth / 2;
        screenParams.gravity = Gravity.END | Gravity.TOP;
        screenParams.topMargin = 0;
        screenParams.leftMargin = 0;
        screenParams.rightMargin = 0;
        screenParams.bottomMargin = 0;
        screenView.setLayoutParams(screenParams);
        screenView.setVisibility(View.VISIBLE);
        screenView.setBackgroundColor(colorId);
    }

    /**
     * 加载一个具有1/4屏幕右下大小的小画面
     *
     * @param screenParams 控件params
     * @param screenView   控件
     * @param colorId      控件背景
     */
    private void initScreenViewRightDownWithQuarterView01(FrameLayout.LayoutParams screenParams, TXCloudVideoView screenView, int colorId) {
        screenParams.height = itemFullHeight / 2;
        screenParams.width = itemFullWidth / 2;
        screenParams.gravity = Gravity.END | Gravity.BOTTOM;
        screenParams.topMargin = 0;
        screenParams.leftMargin = 0;
        screenParams.rightMargin = 0;
        screenParams.bottomMargin = 0;
        screenView.setLayoutParams(screenParams);
        screenView.setVisibility(View.VISIBLE);
        screenView.setBackgroundColor(colorId);
    }

    /**
     * 加载一个中右的小画面
     *
     * @param screenParams 控件params
     * @param screenView   控件
     * @param colorId      控件颜色
     */
    private void initScreenViewCenterRightWithMargins01(FrameLayout.LayoutParams screenParams, TXCloudVideoView screenView, int colorId) {
        screenParams.height = itemFullHeight * 8 / 27;
        screenParams.width = itemFullWidth * 10 / 32;
        screenParams.rightMargin = 8;
        screenParams.topMargin = 0;
        screenParams.leftMargin = 0;
        screenParams.bottomMargin = 0;
        screenParams.gravity = Gravity.CENTER_VERTICAL | Gravity.END;
        screenView.setLayoutParams(screenParams);
        screenView.setVisibility(View.VISIBLE);
        screenView.setBackgroundColor(colorId);
    }

    /**
     * 加载一个具有1/4屏幕左上大小的小画面
     *
     * @param screenParams 控件params
     * @param screenView   控件
     * @param colorId      控件背景
     */
    private void initScreenViewLeftTopWithQuarterView01(FrameLayout.LayoutParams screenParams, TXCloudVideoView screenView, int colorId) {
        screenParams.height = itemFullHeight / 2;
        screenParams.width = itemFullWidth / 2;
        screenParams.gravity = Gravity.START | Gravity.TOP;
        screenParams.topMargin = 0;
        screenParams.leftMargin = 0;
        screenParams.rightMargin = 0;
        screenParams.bottomMargin = 0;
        screenView.setLayoutParams(screenParams);
        screenView.setVisibility(View.VISIBLE);
        screenView.setBackgroundColor(colorId);
    }

    /**
     * 加载一个具有1/4屏幕左下大小的小画面
     *
     * @param screenParams 控件params
     * @param screenView   控件
     * @param colorId      控件背景
     */
    private void initScreenViewLeftDownWithQuarterView01(FrameLayout.LayoutParams screenParams, TXCloudVideoView screenView, int colorId) {
        screenParams.height = itemFullHeight / 2;
        screenParams.width = itemFullWidth / 2;
        screenParams.gravity = Gravity.START | Gravity.BOTTOM;
        screenParams.topMargin = 0;
        screenParams.leftMargin = 0;
        screenParams.rightMargin = 0;
        screenParams.bottomMargin = 0;
        screenView.setLayoutParams(screenParams);
        screenView.setVisibility(View.VISIBLE);
        screenView.setBackgroundColor(colorId);
    }

    /**
     * 加载一个右上的没有Margin的小屏幕
     *
     * @param screenParams 控件params
     * @param screenView   控件
     * @param colorId      控件背景
     */
    private void initScreenViewRightTopWithoutMargins01(FrameLayout.LayoutParams screenParams, TXCloudVideoView screenView, int colorId) {
        screenParams.height = itemFullHeight / 3;
        screenParams.width = itemFullWidth / 3;
        screenParams.gravity = Gravity.TOP | Gravity.END;
        screenParams.topMargin = 0;
        screenParams.leftMargin = 0;
        screenParams.rightMargin = 0;
        screenParams.bottomMargin = 0;
        screenView.setLayoutParams(screenParams);
        screenView.setVisibility(View.VISIBLE);
        screenView.setBackgroundColor(colorId);
    }

    /**
     * 加载一个右下的没有Margin的小屏幕
     *
     * @param screenParams 控件params
     * @param screenView   控件
     * @param colorId      控件背景
     */
    private void initScreenViewRightDownWithoutMargins01(FrameLayout.LayoutParams screenParams, TXCloudVideoView screenView, int colorId) {
        screenParams.height = itemFullHeight / 3;
        screenParams.width = itemFullWidth / 3;
        screenParams.gravity = Gravity.BOTTOM | Gravity.END;
        screenParams.topMargin = 0;
        screenParams.leftMargin = 0;
        screenParams.rightMargin = 0;
        screenParams.bottomMargin = 0;
        screenView.setLayoutParams(screenParams);
        screenView.setVisibility(View.VISIBLE);
        screenView.setBackgroundColor(colorId);
    }

    /**
     * 加载一个左上的没有Margin的小屏幕
     *
     * @param screenParams 控件params
     * @param screenView   控件
     * @param colorId      控件背景
     */
    private void initScreenViewLeftTopWithoutMargins01(FrameLayout.LayoutParams screenParams, TXCloudVideoView screenView, int colorId) {
        screenParams.height = itemFullHeight / 3;
        screenParams.width = itemFullWidth / 3;
        screenParams.gravity = Gravity.TOP | Gravity.START;
        screenParams.topMargin = 0;
        screenParams.leftMargin = 0;
        screenParams.rightMargin = 0;
        screenParams.bottomMargin = 0;
        screenView.setLayoutParams(screenParams);
        screenView.setVisibility(View.VISIBLE);
        screenView.setBackgroundColor(colorId);
    }

    /**
     * 加载一个左下的没有Margin的小屏幕
     *
     * @param screenParams 控件params
     * @param screenView   控件
     * @param colorId      控件背景
     */
    private void initScreenViewLeftDownWithoutMargins01(FrameLayout.LayoutParams screenParams, TXCloudVideoView screenView, int colorId) {
        screenParams.height = itemFullHeight / 3;
        screenParams.width = itemFullWidth / 3;
        screenParams.gravity = Gravity.BOTTOM | Gravity.START;
        screenParams.topMargin = 0;
        screenParams.leftMargin = 0;
        screenParams.rightMargin = 0;
        screenParams.bottomMargin = 0;
        screenView.setLayoutParams(screenParams);
        screenView.setVisibility(View.VISIBLE);
        screenView.setBackgroundColor(colorId);
    }

    /**
     * 加载一个具有4/9全屏幕的大画面在左上
     *
     * @param screenParams
     * @param screenView
     */
    private void initScreenViewFourNinethsLeftTop00(FrameLayout.LayoutParams screenParams, TXCloudVideoView screenView) {
        screenParams.height = itemFullHeight * 2 / 3;
        screenParams.width = itemFullWidth * 2 / 3;
        screenParams.gravity = Gravity.TOP | Gravity.START;
        screenParams.topMargin = 0;
        screenParams.leftMargin = 0;
        screenParams.rightMargin = 0;
        screenParams.bottomMargin = 0;
        screenView.setLayoutParams(screenParams);
        screenView.setVisibility(View.VISIBLE);
        screenView.setBackgroundColor(Color.RED);
    }

    /**
     * 加载一个在中右的没有margin的小画面
     *
     * @param screenParams 控件params
     * @param screenView   控件
     * @param colorId      控件颜色
     */
    private void initScreenViewCenterRightWithoutMargins01(FrameLayout.LayoutParams screenParams, TXCloudVideoView screenView, int colorId) {
        screenParams.height = itemFullHeight / 3;
        screenParams.width = itemFullWidth / 3;
        screenParams.gravity = Gravity.END | Gravity.CENTER_VERTICAL;
        screenParams.topMargin = 0;
        screenParams.leftMargin = 0;
        screenParams.rightMargin = 0;
        screenParams.bottomMargin = 0;
        screenView.setLayoutParams(screenParams);
        screenView.setVisibility(View.VISIBLE);
        screenView.setBackgroundColor(colorId);
    }

    /**
     * 加载一个在中下的没有margin的小画面
     *
     * @param screenParams 控件params
     * @param screenView   控件
     * @param colorId      控件颜色
     */
    private void initScreenViewCenterDownWithoutMargins01(FrameLayout.LayoutParams screenParams, TXCloudVideoView screenView, int colorId) {
        screenParams.height = itemFullHeight / 3;
        screenParams.width = itemFullWidth / 3;
        screenParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        screenParams.topMargin = 0;
        screenParams.leftMargin = 0;
        screenParams.rightMargin = 0;
        screenParams.bottomMargin = 0;
        screenView.setLayoutParams(screenParams);
        screenView.setVisibility(View.VISIBLE);
        screenView.setBackgroundColor(colorId);
    }

    private void initScreenViewTwoThirdsLeftTop00(FrameLayout.LayoutParams screenParams, TXCloudVideoView screenView) {
        screenParams.height = itemFullHeight;
        screenParams.width = itemFullWidth * 2 / 3;
        screenParams.gravity = Gravity.START | Gravity.TOP;
        screenParams.topMargin = 0;
        screenParams.leftMargin = 0;
        screenParams.rightMargin = 0;
        screenParams.bottomMargin = 0;
        screenView.setLayoutParams(screenParams);
        screenView.setVisibility(View.VISIBLE);
        screenView.setBackgroundColor(Color.RED);
    }

    /**
     * 加载一个右上的1/6的小画面
     *
     * @param screenParams 控件params
     * @param screenView   控件
     * @param colorId      控件背景
     */
    private void initScreenViewSixthRightTop(FrameLayout.LayoutParams screenParams, TXCloudVideoView screenView, int colorId) {
        screenParams.height = itemFullHeight / 3;
        screenParams.width = itemFullWidth / 2;
        screenParams.gravity = Gravity.END | Gravity.TOP;
        screenParams.topMargin = 0;
        screenParams.leftMargin = 0;
        screenParams.rightMargin = 0;
        screenParams.bottomMargin = 0;
        screenView.setLayoutParams(screenParams);
        screenView.setVisibility(View.VISIBLE);
        screenView.setBackgroundColor(colorId);
    }

    /**
     * 加载一个右下的1/6的小画面
     *
     * @param screenParams 控件params
     * @param screenView   控件
     * @param colorId      控件背景
     */
    private void initScreenViewSixthRightDown(FrameLayout.LayoutParams screenParams, TXCloudVideoView screenView, int colorId) {
        screenParams.height = itemFullHeight / 3;
        screenParams.width = itemFullWidth / 2;
        screenParams.gravity = Gravity.END | Gravity.BOTTOM;
        screenParams.topMargin = 0;
        screenParams.leftMargin = 0;
        screenParams.rightMargin = 0;
        screenParams.bottomMargin = 0;
        screenView.setLayoutParams(screenParams);
        screenView.setVisibility(View.VISIBLE);
        screenView.setBackgroundColor(colorId);
    }

    /**
     * 加载一个中右的1/6的小画面
     *
     * @param screenParams 控件params
     * @param screenView   控件
     * @param colorId      控件背景
     */
    private void initScreenViewSixthCenterRight(FrameLayout.LayoutParams screenParams, TXCloudVideoView screenView, int colorId) {
        screenParams.height = itemFullHeight / 3;
        screenParams.width = itemFullWidth / 2;
        screenParams.gravity = Gravity.END | Gravity.CENTER_VERTICAL;
        screenParams.topMargin = 0;
        screenParams.leftMargin = 0;
        screenParams.rightMargin = 0;
        screenParams.bottomMargin = 0;
        screenView.setLayoutParams(screenParams);
        screenView.setVisibility(View.VISIBLE);
        screenView.setBackgroundColor(colorId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (PlayViewInfoModel model :
                mPlayViewInfoModelList) {
            TXLivePlayer player = model.getTxPlayerModel().getTxLivePlayer();
            if (player != null && player.isPlaying()) {
                player.pause();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (PlayViewInfoModel model :
                mPlayViewInfoModelList) {
            TXLivePlayer player = model.getTxPlayerModel().getTxLivePlayer();
            if (player != null && !player.isPlaying()) {
                player.resume();
            }
        }
    }

    @OnClick(R.id.tvLeft)
    void tvLeft() {
//        chooseLayoutByPlayType(LivePlayType.SCREEN_THREE_QUARTERS_AND_RIGHT_DOWN);
    }

}
