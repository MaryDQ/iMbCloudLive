package com.microsys.imbcloudlive.ui.fragments;

import android.annotation.SuppressLint;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.microsys.imbcloudlive.R;
import com.microsys.imbcloudlive.base.BaseFragment;
import com.microsys.imbcloudlive.di.component.AppComponent;
import com.microsys.imbcloudlive.model.SimpleModel;
import com.microsys.imbcloudlive.ui.adapters.AbstractSimpleAdapter;
import com.microsys.imbcloudlive.ui.adapters.ViewHolder;
import com.microsys.imbcloudlive.utils.DialogUtils;

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
    private int number;
    private List<Object> images = new ArrayList<>();

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
    public int getRootViewId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initViewAndData() {
        initList();
        initBanner();
        initAdapter();

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

    private void initBanner() {
        images.add(R.mipmap.banner01);
        images.add(R.mipmap.banner02);
        images.add(R.mipmap.banner03);
    }

    private void initAdapter() {
        mRecyclerAdapter = new AbstractSimpleAdapter<SimpleModel>(mContext, testList, R.layout.item_add_views) {
            @Override
            protected void onBindViewHolder(ViewHolder holder, SimpleModel data) {
            }
        };
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, 2, RecyclerView.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerAdapter.setOnItemClickListener(new AbstractSimpleAdapter.OnItemClickListener() {
            @Override
            public void onClickItem(Object o, int position) {
                //设备性能无误的时候
                if (true) {
                    DialogUtils.showSelectableLiveStreamingDialog(mContext,true);
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