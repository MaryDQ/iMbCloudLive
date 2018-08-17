package com.microsys.imbcloudlive.ui.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.microsys.imbcloudlive.R;
import com.microsys.imbcloudlive.base.BaseFragment;
import com.microsys.imbcloudlive.common.Constants;
import com.microsys.imbcloudlive.di.component.AppComponent;
import com.microsys.imbcloudlive.eventbus_events.TransformCurPlayModelEvent;
import com.microsys.imbcloudlive.model.ListTxPlayerModel;
import com.microsys.imbcloudlive.model.SimpleModel;
import com.microsys.imbcloudlive.ui.adapters.AbstractSimpleAdapter;
import com.microsys.imbcloudlive.ui.adapters.ViewHolder;
import com.microsys.imbcloudlive.utils.DialogUtils;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import org.greenrobot.eventbus.EventBus;

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
    private List<ListTxPlayerModel> listTxLivePlayer = new ArrayList<>();

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
        for (int i = 0; i < 6; i++) {
            SimpleModel simpleModel = new SimpleModel();
            TXLivePlayer player =null;
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
                final int curPosition=position;
                final TXCloudVideoView videoView = holder.getView(R.id.txVideoView);
                final TextView tvClickToAdd = holder.getView(R.id.tvAddViewClickToAdd);
                TXLivePlayer player = ((SimpleModel) o).getTxLivePlayer();
                if (null==player) {
                    player=new TXLivePlayer(mContext);
                }
                player.setConfig(mPlayConfig);
                player.setPlayerView(videoView);
                player.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
                player.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
                player.enableHardwareDecode(false);
                //设备性能无误的时候
                if (true) {
                    final TXLivePlayer finalPlayer = player;
                    DialogUtils.showSelectableLiveStreamingDialog(mContext, true, new DialogUtils.MCallBack() {
                        @Override
                        public boolean OnCallBackDispath(Boolean bSucceed, String clickText) {
                            if (bSucceed) {
                                int result = finalPlayer.startPlay(clickText, TXLivePlayer.PLAY_TYPE_LIVE_RTMP);
                                if (result == 0) {
                                    boolean doNext=true;
                                    for (int i = 0; i < listTxLivePlayer.size(); i++) {
                                        ListTxPlayerModel playerModel=listTxLivePlayer.get(i);
                                        if (curPosition==playerModel.getPosition()) {
                                            doNext=false;
                                            break;
                                        }
                                    }
                                    if (!doNext) {
                                        return false;
                                    }

                                    ListTxPlayerModel model=new ListTxPlayerModel();
                                    model.setPosition(curPosition);
                                    model.setPlayUrl(clickText);
                                    model.setTxLivePlayer(finalPlayer);
                                    tvClickToAdd.setVisibility(View.GONE);
                                    listTxLivePlayer.add(model);

                                    ListTxPlayerModel modelTemp=new ListTxPlayerModel();
                                    modelTemp.setPosition(curPosition);
                                    modelTemp.setPlayUrl(clickText);
                                    modelTemp.setTxLivePlayer(null);
                                    Constants.curFragListTxPlayerModel.add(modelTemp);
                                    EventBus.getDefault().post(new TransformCurPlayModelEvent(true));
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
                final int curPosition=position;
                final TXLivePlayer[] curSelectedPlayer = {((SimpleModel) o).getTxLivePlayer()};
                final TXCloudVideoView[] curVideoView = {viewHolder.getView(R.id.txVideoView)};
                final TextView tvClickToAdd = viewHolder.getView(R.id.tvAddViewClickToAdd);
                DialogUtils.showStreamInputManagerDialog(mContext, true, new DialogUtils.MCallBack() {
                    @Override
                    public boolean OnCallBackDispath(Boolean bSucceed, String clickText) {
                        //静音
                        if (bSucceed) {

                        }//移除
                        else {

                            for (int i = 0; i < listTxLivePlayer.size(); i++) {
                                ListTxPlayerModel model=listTxLivePlayer.get(i);
                                if (model.getPosition()==curPosition) {
                                    TXLivePlayer player=model.getTxLivePlayer();
                                    player.stopPlay(true);
                                    player=null;

                                    curVideoView[0].setVisibility(View.GONE);
                                    curVideoView[0].setBackgroundColor(Color.BLACK);
                                    curVideoView[0].onDestroy();
                                    curVideoView[0] =null;
                                    tvClickToAdd.setVisibility(View.VISIBLE);

                                    listTxLivePlayer.remove(model);
                                    break;
                                }
                            }

                            for (int i = 0; i < Constants.curFragListTxPlayerModel.size(); i++) {
                                ListTxPlayerModel model=Constants.curFragListTxPlayerModel.get(i);
                                if (model.getPosition()==curPosition) {
                                    Constants.curFragListTxPlayerModel.remove(model);
                                    EventBus.getDefault().post(new TransformCurPlayModelEvent(true));
                                    break;
                                }
                            }
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
        for (ListTxPlayerModel model :
                listTxLivePlayer) {
            TXLivePlayer player=model.getTxLivePlayer();
            if (player != null && !player.isPlaying()) {
                player.resume();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        for (ListTxPlayerModel model :
                listTxLivePlayer) {
            TXLivePlayer player=model.getTxLivePlayer();
            if (player != null && player.isPlaying()) {
                player.pause();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (ListTxPlayerModel model :
                listTxLivePlayer) {
            TXLivePlayer player=model.getTxLivePlayer();
            if (player!=null) {
                player.stopPlay(true);
                player=null;
            }
        }
    }
}