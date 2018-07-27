package com.microsys.imbcloudlive.ui.activitys;

import com.microsys.imbcloudlive.R;
import com.microsys.imbcloudlive.base.BaseNoTitleActivity;
import com.microsys.imbcloudlive.di.component.AppComponent;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import butterknife.BindView;

/**
 * 描述：
 * 作者: mlx
 * 创建时间： 2018/7/24
 */
public class TestActivity extends BaseNoTitleActivity {

    private TXLivePlayer mLivePlayer = null;
    @BindView(R.id.txVideoViewTest)
    TXCloudVideoView mTXCloudVideoView;
    private TXLivePlayConfig mPlayConfig;

    @Override
    public int getRootViewId() {
        return R.layout.activity_test;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initViewAndData() {
        mLivePlayer=new TXLivePlayer(mContext);
        mLivePlayer.setPlayerView(mTXCloudVideoView);
        mPlayConfig = new TXLivePlayConfig();
        mPlayConfig.setAutoAdjustCacheTime(true);
        mPlayConfig.setMinAutoAdjustCacheTime(1);
        mPlayConfig.setMaxAutoAdjustCacheTime(1);
        mLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
        mLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        mLivePlayer.enableHardwareDecode(false);
        mLivePlayer.startPlay("rtmp://live.hkstv.hk.lxdns.com/live/hks",TXLivePlayer.PLAY_TYPE_LIVE_RTMP);

    }


}
