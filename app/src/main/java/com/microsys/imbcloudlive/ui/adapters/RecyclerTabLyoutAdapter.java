package com.microsys.imbcloudlive.ui.adapters;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.microsys.imbcloudlive.R;
import com.microsys.imbcloudlive.widget.RecyclerTabLayout;

import java.util.List;


/**
 * 描述：
 * 作者: mlx
 * 创建时间： 2018/7/23
 */
public class RecyclerTabLyoutAdapter
        extends RecyclerTabLayout.AbstractAdapter<RecyclerTabLyoutAdapter.ViewHolder> {

    private RecyclerViewPagerAdapter mAdapater;
    private List<String> mList;

    public RecyclerTabLyoutAdapter(ViewPager viewPager, List<String> mList) {
        super(viewPager);
        mAdapater = (RecyclerViewPagerAdapter) mViewPager.getAdapter();
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pager_title, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imageView.setSelected(position == getCurrentIndicatorPosition());
        holder.textView.setText(mList.get(position));
        if (position == getCurrentIndicatorPosition()) {
            holder.imageView.setImageResource(R.mipmap.ic_launcher);
            holder.textView.setTextColor(Color.BLACK);
        } else {
            holder.imageView.setImageResource(R.mipmap.ic_launcher);
            holder.textView.setTextColor(Color.WHITE);
        }

    }


    @Override
    public int getItemCount() {
        return mAdapater.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_pager_pic);
            textView=itemView.findViewById(R.id.tv_pager_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getViewPager().setCurrentItem(getAdapterPosition());
                }
            });
        }
    }
}
