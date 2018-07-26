package com.microsys.imbcloudlive.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.microsys.imbcloudlive.R;

/**
 * 描述：Dialog工具类
 * 作者: mlx
 * 创建时间： 2018/7/25
 */
public class DialogUtils {
    /**
     * 带有2个button的Dialog
     *
     * @param context       上下文
     * @param dialogTitle   dialog显示的标题
     * @param dialogContent dialog显示的文本
     * @param confirmText   确认按钮文本
     * @param cancelable    点击dialog外是否可取消
     * @param cancelText    取消按钮文本
     * @param callBack      按钮点击完毕回调
     */
    public static void twoButtonsDialog(final Context context, String dialogTitle, String dialogContent, String confirmText, String cancelText, boolean cancelable, final MCallBack callBack) {

        final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog_default_style).create();
        alertDialog.setCancelable(cancelable);
        alertDialog.setView(new EditText(context));
        alertDialog.show();
        Window window = alertDialog.getWindow();
        if (window == null) {
            throw new IllegalArgumentException("获取window为空");
        }
        window.setContentView(R.layout.dialog_two_buttons);
        TextView tvDialogTitle = window.findViewById(R.id.tvDialogTitle);
        TextView tvDialogContent = window.findViewById(R.id.tvDialogContent);
        TextView tvCancel = window.findViewById(R.id.tv_cancel);
        TextView tvConfirm = window.findViewById(R.id.tv_ok);

        tvDialogTitle.setText(dialogTitle);
        tvDialogContent.setText(dialogContent);
        tvConfirm.setText(confirmText);
        tvCancel.setText(cancelText);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack.OnCallBackDispath(false, null)) {
                    alertDialog.dismiss();
                }

            }
        });


        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!callBack.OnCallBackDispath(true, null)) {
                    alertDialog.dismiss();
                }

            }
        });
    }

    public static void showSelectableLiveStreamingDialog(Context context, boolean cancelable) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog_default_style).create();
        alertDialog.setCancelable(cancelable);
        alertDialog.setView(new EditText(context));
        alertDialog.show();
        Window window = alertDialog.getWindow();
        if (window == null) {
            throw new IllegalArgumentException("获取window为空");
        }
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity= Gravity.BOTTOM;
        window.setAttributes(params);
        window.setContentView(R.layout.dialog_selectable_live_streaming);

    }

    public interface MCallBack {
        /**
         * 弹窗dialog点击确定或者取消的回调
         *
         * @param bSucceed  点击确定返回true,点击取消返回false
         * @param clickText 点击文案
         * @return 回调成功返回true, 根据返回继续下一步操作
         */
        boolean OnCallBackDispath(Boolean bSucceed, String clickText);
    }
}
