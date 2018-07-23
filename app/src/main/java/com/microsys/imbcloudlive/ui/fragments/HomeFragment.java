package com.microsys.imbcloudlive.ui.fragments;

import android.annotation.SuppressLint;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.microsys.imbcloudlive.R;
import com.microsys.imbcloudlive.base.BaseFragment;
import com.microsys.imbcloudlive.di.component.AppComponent;
import com.microsys.imbcloudlive.model.SimpleModel;
import com.microsys.imbcloudlive.ui.adapters.SimpleAdapter;
import com.microsys.imbcloudlive.ui.adapters.ViewHolder;
import com.microsys.imbcloudlive.utils.GlideUtils;
import com.microsys.imbcloudlive.utils.banner_image_loader.BannerImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 *
 * 描述：主页Fragment
 * 作者: mlx
 * 创建时间： 2018/7/23
 *
 */
@SuppressLint("ValidFragment")
public class HomeFragment extends BaseFragment {

    private static volatile HomeFragment mHomeFragment;


    public static HomeFragment getInstance(){
        if (null==mHomeFragment) {
            synchronized (HomeFragment.class){
                if (null==mHomeFragment) {
                    mHomeFragment=new HomeFragment(-1);
                }
            }
        }
        return mHomeFragment;
    }


    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.rcv_home_fragment)
    RecyclerView mRecyclerView;

    private ArrayList<SimpleModel> testList = new ArrayList<>();

    private SimpleAdapter<SimpleModel> mRecyclerAdapter;

    private int number;

    private List<Object> images = new ArrayList<>();

    @SuppressLint("ValidFragment")
    public HomeFragment(int i) {
        super(i);
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
        for (int i = 0; i < 20; i++) {
            testList.add(simpleModel);
        }
    }

    private void initBanner() {
        images.add(R.mipmap.banner01);
        images.add(R.mipmap.banner02);
        images.add(R.mipmap.banner03);
        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        banner.setImageLoader(new BannerImageLoader());
        banner.setViewPagerIsScroll(false);
        banner.update(images);
        banner.start();
    }

    private void initAdapter() {
        mRecyclerAdapter = new SimpleAdapter<SimpleModel>(mContext, testList, R.layout.item_recycler_normal) {
            @Override
            protected void onBindViewHolder(ViewHolder holder, SimpleModel data) {
                GlideUtils.loadImageView(mContext, data.getPicUrl(), holder.<ImageView>getView(R.id.rcv_iv_item));
                holder.<TextView>getView(R.id.rcv_tv_item).setText(data.getContent());
            }
        };
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, 2, RecyclerView.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(mRecyclerAdapter);

    }

    @Override
    public void onStart() {
        banner.startAutoPlay();
        super.onStart();
    }

    @Override
    public void onPause() {
        banner.stopAutoPlay();
        super.onPause();
    }

}
