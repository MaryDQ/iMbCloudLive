package com.microsys.imbcloudlive.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 * 作者: mlx
 * 创建时间： 2018/7/23
 */
public class RecyclerViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mItems = new ArrayList<>();

    public RecyclerViewPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.mItems = list;
    }

    @Override
    public Fragment getItem(int position) {
        return mItems.get(position);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }


}
