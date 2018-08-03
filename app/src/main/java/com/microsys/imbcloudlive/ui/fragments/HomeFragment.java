package com.microsys.imbcloudlive.ui.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.microsys.imbcloudlive.R;
import com.microsys.imbcloudlive.base.BaseFragment;
import com.microsys.imbcloudlive.di.component.AppComponent;
import com.microsys.imbcloudlive.model.SimpleModel;
import com.microsys.imbcloudlive.ui.adapters.AbstractSimpleAdapter;
import com.microsys.imbcloudlive.ui.adapters.ViewHolder;
import com.microsys.imbcloudlive.utils.DialogUtils;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.ArrayList;
import java.util.List;

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
    /**
     * TXPlayer播放配置
     */
    private TXLivePlayConfig mPlayConfig;
    /**
     * List中的TxLivePlayer对象集合,用来控制界面隐藏和重新进入的播放或者暂停
     */
    private List<TXLivePlayer> listTxLivePlayer = new ArrayList<>();

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
        initTXPlayConfig();
    }

    @Override
    public int getRootViewId() {
        return R.layout.fragment_home;
    }

    public void initList() {

        testList.clear();
        for (int i = 0; i < 16; i++) {
            SimpleModel simpleModel = new SimpleModel();
            TXLivePlayer player = new TXLivePlayer(mContext);
            simpleModel.setTxLivePlayer(player);
            testList.add(simpleModel);
        }
    }

    private void initAdapter() {

        mRecyclerAdapter = new AbstractSimpleAdapter<SimpleModel>(mContext, testList, R.layout.item_add_views) {
            @Override
            protected void onBindViewHolder(ViewHolder holder, SimpleModel data, int curPostion) {
                TextView tvNum = holder.getView(R.id.tvAddViewNums);
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
            public void onClickItem(Object o, int position, ViewHolder holder) {
                final TXCloudVideoView videoView = holder.getView(R.id.txVideoView);
                final TextView tvClickToAdd = holder.getView(R.id.tvAddViewClickToAdd);
                final TXLivePlayer player = ((SimpleModel) o).getTxLivePlayer();
                player.setConfig(mPlayConfig);
                player.setPlayerView(videoView);
                player.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
                player.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
                player.enableHardwareDecode(false);
                //设备性能无误的时候
                if (true) {
                    DialogUtils.showSelectableLiveStreamingDialog(mContext, true, new DialogUtils.MCallBack() {
                        @Override
                        public boolean OnCallBackDispath(Boolean bSucceed, String clickText) {
                            if (bSucceed) {
                                int result = player.startPlay(clickText, TXLivePlayer.PLAY_TYPE_LIVE_RTMP);
                                if (result == 0) {
                                    tvClickToAdd.setVisibility(View.GONE);
                                    listTxLivePlayer.add(player);
                                }
                            }
                            return false;
                        }
                    });
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

        mRecyclerAdapter.setOnItemLongClickListener(new AbstractSimpleAdapter.OnItemLongClickListener() {
            @Override
            public void onLongClickItem(Object o, int position, ViewHolder viewHolder) {
               final TXLivePlayer curSelectedPlayer=((SimpleModel)o).getTxLivePlayer();
               final TXCloudVideoView curVideoView=viewHolder.getView(R.id.txVideoView);
                final TextView tvClickToAdd = viewHolder.getView(R.id.tvAddViewClickToAdd);
                DialogUtils.showStreamInputManagerDialog(mContext, true, new DialogUtils.MCallBack() {
                    @Override
                    public boolean OnCallBackDispath(Boolean bSucceed, String clickText) {
                        //静音
                        if (bSucceed) {

                        }//移除
                        else {
                            curSelectedPlayer.stopPlay(true);
                            curVideoView.setVisibility(View.GONE);
                            curVideoView.setBackgroundColor(Color.BLACK);
                            tvClickToAdd.setVisibility(View.VISIBLE);
                            listTxLivePlayer.remove(curSelectedPlayer);
                        }
                        return false;
                    }
                });
            }
        });


    }

    private void initTXPlayConfig() {
        mPlayConfig = new TXLivePlayConfig();
        mPlayConfig.setAutoAdjustCacheTime(true);
        mPlayConfig.setMinAutoAdjustCacheTime(1);
        mPlayConfig.setMaxAutoAdjustCacheTime(1);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {

        super.onResume();
        for (TXLivePlayer player :
                listTxLivePlayer) {
            if (player != null && !player.isPlaying()) {
                player.resume();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        for (TXLivePlayer player :
                listTxLivePlayer) {
            if (player != null && player.isPlaying()) {
                player.pause();
            }
        }
    }


}