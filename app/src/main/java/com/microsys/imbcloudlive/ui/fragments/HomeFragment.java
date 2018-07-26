package com.microsys.imbcloudlive.ui.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.microsys.imbcloudlive.R;
import com.microsys.imbcloudlive.base.BaseFragment;
import com.microsys.imbcloudlive.di.component.AppComponent;
import com.microsys.imbcloudlive.model.SimpleModel;
import com.microsys.imbcloudlive.ui.adapters.AbstractSimpleAdapter;
import com.microsys.imbcloudlive.ui.adapters.ViewHolder;
import com.microsys.imbcloudlive.utils.DialogUtils;
import com.microsys.imbcloudlive.utils.L;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 描述：主页Fragment
 * 作者: mlx
 * 创建时间： 2018/7/23
 */
@SuppressLint("ValidFragment")
public class HomeFragment extends BaseFragment {

    private static volatile HomeFragment mHomeFragment;
    @BindView(R.id.rcv_home_fragment)
    RecyclerView mRecyclerView;
    private ArrayList<SimpleModel> testList = new ArrayList<>();
    private AbstractSimpleAdapter<SimpleModel> mRecyclerAdapter;

    @SuppressLint("ValidFragment")
    public HomeFragment(int i) {
        super(i);
    }

    public static HomeFragment getInstance() {
        if (null == mHomeFragment) {
            synchronized (HomeFragment.class) {
                if (null == mHomeFragment) {
                    mHomeFragment = new HomeFragment(-1);
                }
            }
        }
        return mHomeFragment;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initViewAndData() {
        initList();
        initAdapter();

    }

    @Override
    public int getRootViewId() {
        return R.layout.fragment_home;
    }

    public void initList() {
        SimpleModel simpleModel = new SimpleModel();
        simpleModel.setContent("nihao");
        simpleModel.setPicUrl("http://g.hiphotos.baidu.com/image/pic/item/4610b912c8fcc3cec52444bf9e45d688d53f2051.jpg");
        testList.clear();
        for (int i = 0; i < 16; i++) {
            testList.add(simpleModel);
        }
    }

    private TXLivePlayer mLivePlayer = null;
    private TXLivePlayConfig mPlayConfig;


    private void initAdapter() {
        mLivePlayer=new TXLivePlayer(mContext);
        mPlayConfig = new TXLivePlayConfig();
        mPlayConfig.setAutoAdjustCacheTime(true);
        mPlayConfig.setMinAutoAdjustCacheTime(1);
        mPlayConfig.setMaxAutoAdjustCacheTime(1);
        mRecyclerAdapter = new AbstractSimpleAdapter<SimpleModel>(mContext, testList, R.layout.item_add_views) {
            @Override
            protected void onBindViewHolder(ViewHolder holder, SimpleModel data, int curPostion) {
                TextView tvNum =holder.getView(R.id.tvAddViewNums);
                tvNum.setText(curPostion + "");


            }
        };
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, 2, RecyclerView.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerAdapter.setOnItemClickListener(new AbstractSimpleAdapter.OnItemClickListener() {
            @Override
            public void onClickItem(Object o, int position,ViewHolder holder) {
                //设备性能无误的时候
                if (true) {
                        TXCloudVideoView videoView=holder.<TXCloudVideoView>getView(R.id.txVideoView);
                    mLivePlayer.setConfig(mPlayConfig);
                    mLivePlayer.setPlayerView(videoView);
                    mLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
                    mLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
                    mLivePlayer.enableHardwareDecode(false);
                    mLivePlayer.setPlayListener(new ITXLivePlayListener() {
                        @Override
                        public void onPlayEvent(int i, Bundle bundle) {

                        }

                        @Override
                        public void onNetStatus(Bundle bundle) {

                        }
                    });
                   int result= mLivePlayer.startPlay("rtmp://live.hkstv.hk.lxdns.com/live",TXLivePlayer.PLAY_TYPE_LIVE_RTMP);
                    L.e("play rtmp result code:"+result);
//                    DialogUtils.showSelectableLiveStreamingDialog(mContext, true);
                } else {
                    DialogUtils.twoButtonsDialog(mContext, "无法添加", getString(R.string.deviceNotSupport), "了解更多", "取消添加", true, new DialogUtils.MCallBack() {
                        @Override
                        public boolean OnCallBackDispath(Boolean bSucceed, String clickText) {
                            if (bSucceed) {
                                return true;
                            }
                            return false;
                        }
                    });

                }
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}