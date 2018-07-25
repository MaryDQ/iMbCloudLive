package com.microsys.imbcloudlive.ui.activitys;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.microsys.imbcloudlive.R;
import com.microsys.imbcloudlive.base.BaseNoTitleActivity;
import com.microsys.imbcloudlive.di.component.AppComponent;
import com.microsys.imbcloudlive.ui.adapters.RecyclerTabLyoutAdapter;
import com.microsys.imbcloudlive.ui.adapters.RecyclerViewPagerAdapter;
import com.microsys.imbcloudlive.ui.fragments.HomeFragment;
import com.microsys.imbcloudlive.widget.RecyclerTabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

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
    }

    private void initFragment() {
        HomeFragment mHomeFragment;
        for (int i = 0; i < CONTENT.length; i++) {
            mHomeFragment = new HomeFragment(0);
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
                HomeFragment homeFragment = (HomeFragment) fragments.get(position);
                homeFragment.sendMessage();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initViews() {
        mTvLeft.setText("关闭");
        mTvTitle.setText("易动导播");
    }

}
