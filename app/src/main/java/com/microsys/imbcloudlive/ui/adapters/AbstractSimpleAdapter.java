package com.microsys.imbcloudlive.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/27.
 */
public abstract class AbstractSimpleAdapter<T> extends AbstractMultiTypeAdapter {

    protected int mLayoutId;

    public AbstractSimpleAdapter(Context context, ArrayList<T> datas, int layoutId) {
        super(context,datas);
        this.mLayoutId = layoutId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent,viewType);
    }


    @Override
    protected int getLayoutIdByType(int viewType) {
        return mLayoutId;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    protected void onBindViewHolder(final ViewHolder holder, int type, Object data) {
        onBindViewHolder(holder, (T)data);
        final Object o=data;
        if (null!=mOnItemClickListener) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onClickItem((T)o,holder.getAdapterPosition());
                }
            });
        }

    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener=listener;
    }

    public interface  OnItemClickListener{
        void onClickItem(Object o,int position);
    }



    /**子类需实现以下方法*/

    protected abstract void onBindViewHolder(ViewHolder holder,T data);



}
