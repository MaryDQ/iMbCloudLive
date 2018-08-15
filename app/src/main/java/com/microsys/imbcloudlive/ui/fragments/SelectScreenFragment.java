package com.microsys.imbcloudlive.ui.fragments;

import android.annotation.SuppressLint;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.microsys.imbcloudlive.R;
import com.microsys.imbcloudlive.base.BaseFragment;
import com.microsys.imbcloudlive.di.component.AppComponent;
import com.microsys.imbcloudlive.model.ScreenTypeModel;
import com.microsys.imbcloudlive.ui.adapters.AbstractSimpleAdapter;
import com.microsys.imbcloudlive.ui.adapters.ViewHolder;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 描述：选择何种类型的分屏方式
 * 作者: mlx
 * 创建时间： 2018/8/15
 */
@SuppressLint("ValidFragment")
public class SelectScreenFragment extends BaseFragment {

    @BindView(R.id.rcv_select_screen)
    RecyclerView mRcvSelectScreen;

    private AbstractSimpleAdapter<ScreenTypeModel> mAdapter;

    private ArrayList<ScreenTypeModel> mList = new ArrayList<>();

    @SuppressLint("ValidFragment")
    public SelectScreenFragment(int serial) {
        super(serial);
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initViewAndData() {
        initList();
        initAdapters();

    }

    private void initList() {
        ScreenTypeModel model;

        model = new ScreenTypeModel();
        model.setImageId(R.mipmap.single);
        model.setScreenType("单分屏");
        mList.add(model);

        model = new ScreenTypeModel();
        model.setImageId(R.mipmap.double_split_screen);
        model.setScreenType("双分屏");
        mList.add(model);

        model = new ScreenTypeModel();
        model.setImageId(R.mipmap.pip_left_upper);
        model.setScreenType("画中画-左上");
        mList.add(model);

        model = new ScreenTypeModel();
        model.setImageId(R.mipmap.single);
        model.setScreenType("单分屏");
        mList.add(model);

        model = new ScreenTypeModel();
        model.setImageId(R.mipmap.single);
        model.setScreenType("单分屏");
        mList.add(model);

        model = new ScreenTypeModel();
        model.setImageId(R.mipmap.single);
        model.setScreenType("单分屏");
        mList.add(model);

        model = new ScreenTypeModel();
        model.setImageId(R.mipmap.single);
        model.setScreenType("单分屏");
        mList.add(model);

        model = new ScreenTypeModel();
        model.setImageId(R.mipmap.single);
        model.setScreenType("单分屏");
        mList.add(model);

        model = new ScreenTypeModel();
        model.setImageId(R.mipmap.single);
        model.setScreenType("单分屏");
        mList.add(model);


    }

    private void initAdapters() {
        mAdapter = new AbstractSimpleAdapter<ScreenTypeModel>(mContext, mList, R.layout.item_select_screen) {
            @Override
            protected void onBindViewHolder(ViewHolder holder, ScreenTypeModel data, int curPosition) {
                ImageView ivScreenType = holder.getView(R.id.ivScreenType);
                TextView tvScreenType = holder.getView(R.id.tvScreenType);
                ivScreenType.setImageResource(data.getImageId());
                tvScreenType.setText(data.getScreenType());
            }
        };
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
        mRcvSelectScreen.setLayoutManager(layoutManager);
        mRcvSelectScreen.setAdapter(mAdapter);
    }

    @Override
    public int getRootViewId() {
        return R.layout.fragment_select_screen;
    }
}
