package com.microsys.imbcloudlive.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 */
public abstract class AbstractMultiTypeAdapter extends RecyclerView.Adapter<ViewHolder> {

    protected String TAG;
    protected Context mContext;
    protected List mDatas;

    public AbstractMultiTypeAdapter(Context mContext, List mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        this.TAG = getClass().getSimpleName();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = getLayoutIdByType(viewType);
        return ViewHolder.get(mContext,parent,layoutId);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        onBindViewHolder(holder,getItemViewType(position),mDatas.get(position),position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    /**子类需实现以下三个方法*/

    protected abstract int getLayoutIdByType(int viewType);

    @Override
    public abstract int getItemViewType(int position);

    protected abstract void onBindViewHolder(ViewHolder holder,int type,Object data,int curPosition);

}
