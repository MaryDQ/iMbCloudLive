package com.microsys.imbcloudlive.ui.activitys;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
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
    private List<String> mList= Arrays.asList(CONTENT);
    @BindView(R.id.home_viewpager)
    ViewPager mHomeViewPager;
    @BindView(R.id.home_indicator)
    RecyclerTabLayout mHomeIndicator;
    @BindView(R.id.tvLeft)
    TextView mTvLeft;
    @BindView(R.id.tvTitle)
    TextView mTvTitle;

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
//        initIndicator();//Indicator初始化
//        initListeners();//绑定adapter
        initRecyclerTabLayout();
        initViews();
    }

    private void initRecyclerTabLayout() {
        mRecyclerViewPagerAdapter=new RecyclerViewPagerAdapter(getSupportFragmentManager(),fragments);
        mHomeViewPager.setAdapter(mRecyclerViewPagerAdapter);

        mRecyclerTabLyoutAdapter=new RecyclerTabLyoutAdapter(mHomeViewPager,mList);
        mHomeIndicator.setUpWithAdapter(mRecyclerTabLyoutAdapter);
        mHomeIndicator.setPositionThreshold(0.5f);
    }

    private void initFragment() {
        HomeFragment mHomeFragment;
        for (int i = 0; i < CONTENT.length; i++) {
            mHomeFragment = new HomeFragment(i);
            mHomeFragment.setTabPos(i);
            fragments.add(i, mHomeFragment);
        }
    }

//    private void initIndicator() {
//        CommonNavigator commonNavigator = new CommonNavigator(this);
//        commonNavigator.setAdjustMode(true);
//        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
//
//            @Override
//            public int getCount() {
//                return fragments == null ? 0 : fragments.size();
//            }
//
//            @Override
//            public IPagerTitleView getTitleView(Context context, final int index) {
//                CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(context);
//
//                View customLayout = LayoutInflater.from(context).inflate(R.layout.item_pager_title, null);
//                final ImageView titleImg = (ImageView) customLayout.findViewById(R.id.iv_pager_pic);
//                final TextView titleText = (TextView) customLayout.findViewById(R.id.tv_pager_title);
//
//                titleText.setText(CONTENT[index]);
//
//                commonPagerTitleView.setContentView(customLayout);
//
//                commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {
//                    @Override
//                    public void onSelected(int i, int i1) {
//                        titleText.setTextColor(Color.BLACK);
//                    }
//
//                    @Override
//                    public void onDeselected(int i, int i1) {
//                        titleText.setTextColor(Color.GRAY);
//                    }
//
//                    @Override
//                    public void onLeave(int i, int i1, float v, boolean b) {
//
//                    }
//
//                    @Override
//                    public void onEnter(int i, int i1, float v, boolean b) {
//
//                    }
//                });
//
//                commonPagerTitleView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mHomeViewPager.setCurrentItem(index);
//                    }
//                });
//
//                return commonPagerTitleView;
//            }
//
//            @Override
//            public IPagerIndicator getIndicator(Context context) {
//                return null;
//            }
//        });
//        mHomeIndicator.setNavigator(commonNavigator);
//    }
//
//    private void initListeners() {
//        mFragmentPagerAdapter = new TempAdapter(getSupportFragmentManager());
//        mHomeViewPager.setAdapter(mFragmentPagerAdapter);
//        ViewPagerHelper.bind(mHomeIndicator, mHomeViewPager);
//        mHomeViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                HomeFragment fragment = (HomeFragment) mFragmentPagerAdapter.getItem(position);
//                fragment.sendMessage();
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//    }

    private void initViews() {
        mTvLeft.setText("关闭");
        mTvTitle.setText("易动导播");
    }

    class TempAdapter extends FragmentPagerAdapter {


        private TempAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
        }

        @Override
        public int getCount() {
            return CONTENT.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length].toUpperCase();
        }
    }
}
