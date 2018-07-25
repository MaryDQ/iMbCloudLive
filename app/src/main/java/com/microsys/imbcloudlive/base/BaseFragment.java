package com.microsys.imbcloudlive.base;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.microsys.imbcloudlive.R;
import com.microsys.imbcloudlive.app.MyApplication;
import com.microsys.imbcloudlive.base.mvp.IPresenter;
import com.microsys.imbcloudlive.di.component.AppComponent;
import com.microsys.imbcloudlive.utils.L;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * ============================
 * 作    者：mlx
 * 创建日期：2017/12/27.
 * 描    述：
 * 修改历史：
 * ===========================
 */

public abstract class BaseFragment<P extends IPresenter> extends Fragment {

    private static int mSerial = 0;
    protected Context mContext;
    @Inject
    protected P presenter;
    private Unbinder unbinder;
    private View rootView;
    private ProgressDialog dialog;
    private boolean IS_LOADED = false;
    private int mTabPos = 0;
    private boolean isFirst = true;

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(android.os.Message msg) {
            if (!IS_LOADED) {
                IS_LOADED = true;
                //这里执行加载数据的操作
                L.e("执行了加载数据操作,mTabPos="+mTabPos);
            }
        }
    };

    @SuppressLint("ValidFragment")
    protected BaseFragment(int serial) {
        mSerial = serial;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getRootViewId(), container, false);
        L.e("isFirst=" + isFirst + ",mTabPos=" + mTabPos + ",mSerial=" + mSerial);
        if (isFirst && mTabPos == mSerial) {
            isFirst = false;
            sendMessage();
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        setupActivityComponent(MyApplication.getInstance().getAppComponent());
        if (presenter != null) {
            presenter.attachView(this);
        }
        initViewAndData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (presenter != null) {
            presenter.detachView();
        }
        this.presenter = null;
    }

    protected abstract void setupActivityComponent(AppComponent appComponent);

    /**
     * 初始化View和Data
     *
     * @return
     */
    public abstract void initViewAndData();

    /**
     * 获取根ID
     *
     * @return
     */
    public abstract @LayoutRes
    int getRootViewId();

    public void sendMessage() {
        Message message = handler.obtainMessage();
        message.sendToTarget();
    }

    public void setTabPos(int mTabPos) {
        this.mTabPos = mTabPos;
    }

    protected void showLoading() {
        if (dialog == null) {
            dialog = new ProgressDialog(mContext);
            dialog.setMessage(getResString(R.string.loading));
            dialog.setCancelable(false);
        }
        dialog.show();
    }

    protected String getResString(@StringRes int id) {
        return mContext.getResources().getString(id);
    }

    protected void showLoading(String msg) {
        if (dialog == null) {
            dialog = new ProgressDialog(mContext);
            dialog.setMessage(msg);
            dialog.setCancelable(false);
        }
        dialog.show();
    }

    protected void dismissLoading() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * 关闭Activity
     */
    public void finish() {
        getActivity().finish();
    }

    /**
     * 获取Application
     *
     * @return
     */
    public MyApplication getApp() {
        return (MyApplication) getActivity().getApplication();
    }
}
