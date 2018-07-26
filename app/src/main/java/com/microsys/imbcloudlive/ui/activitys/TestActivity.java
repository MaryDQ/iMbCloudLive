package com.microsys.imbcloudlive.ui.activitys;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
    private TXLivePlayConfig mPlayConfig;
    @BindView(R.id.txVideoViewTest)
    TXCloudVideoView mTXCloudVideoView;

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
        mPlayConfig = new TXLivePlayConfig();
        mPlayConfig.setAutoAdjustCacheTime(true);
        mPlayConfig.setMinAutoAdjustCacheTime(1);
        mPlayConfig.setMaxAutoAdjustCacheTime(1);
        mLivePlayer.setConfig(mPlayConfig);
        mLivePlayer.setPlayerView(mTXCloudVideoView);
        mLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        mLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        mLivePlayer.enableHardwareDecode(false);
        mLivePlayer.startPlay("rtmp://live.hkstv.hk.lxdns.com/live",TXLivePlayer.PLAY_TYPE_LIVE_RTMP);

    }


}
