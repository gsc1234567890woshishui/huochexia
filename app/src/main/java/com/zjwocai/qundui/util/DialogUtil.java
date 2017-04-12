package com.zjwocai.qundui.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.threeti.teamlibrary.ApplicationEx;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.activity.mine.CostActivity;
import com.zjwocai.qundui.widgets.PhoneTextWatcher;

public class DialogUtil {

    private static ProgressDialog _waitDialog;

    public static AlertDialog getAlertDialog(Context context, String msg, String btName) {
        final AlertDialog alert = new AlertDialog.Builder(context).create();
        alert.setMessage(msg);
        alert.setButton(DialogInterface.BUTTON_NEGATIVE, btName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alert.dismiss();
            }
        });
        return alert;
    }

    public static AlertDialog getAlertDialog(Context context, String msg, String btName,
                                             DialogInterface.OnClickListener onclickListener) {
        final AlertDialog alert = new AlertDialog.Builder(context).create();
        alert.setMessage(msg);
        alert.setCancelable(false);
        alert.setButton(DialogInterface.BUTTON_POSITIVE, btName, onclickListener);
        return alert;
    }

    public static AlertDialog getAlertDialog(Context context, String title, String msg, String btName,
                                             DialogInterface.OnClickListener onclickListener) {
        final AlertDialog alert = new AlertDialog.Builder(context).create();
        alert.setTitle(title);
        alert.setMessage(msg);
        alert.setCancelable(false);
        alert.setButton(DialogInterface.BUTTON_POSITIVE, btName, onclickListener);
        return alert;
    }

    public static AlertDialog getAlertDialog(Context context, String title, String msg, String commitName,
                                             String cancelName, DialogInterface.OnClickListener onClick) {
        AlertDialog alert = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton(commitName, onClick);
        builder.setNegativeButton(cancelName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert = builder.create();
        return alert;
    }

    public static AlertDialog getAlertNoTitleDialog(Context context, String msg, String commitName,
                                                    String cancelName, DialogInterface.OnClickListener onClick) {
        AlertDialog alert = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setCancelable(true);
        builder.setPositiveButton(commitName, onClick);
        builder.setNegativeButton(cancelName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert = builder.create();
        return alert;
    }

    public static AlertDialog getAlertDialog(Context context, String title, String msg, String commitName,
                                             String cancelName, DialogInterface.OnClickListener onClick, DialogInterface.OnClickListener oncanClick) {
        AlertDialog alert = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton(commitName, onClick);
        builder.setNegativeButton(cancelName, oncanClick);
        alert = builder.create();
        return alert;
    }

    public static AlertDialog getAlertDialog(Context context, String msg, String items[],
                                             DialogInterface.OnClickListener onclickListener) {
        final AlertDialog alert = new AlertDialog.Builder(context).setItems(items, onclickListener).create();
        alert.setTitle(msg);
        alert.setCancelable(false);
        return alert;
    }

    public static AlertDialog getAlertDialog(Context context, String title, String items[], int selectItem,
                                             String cancelName, DialogInterface.OnClickListener onClick) {
        AlertDialog alert = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setCancelable(false);
        builder.setSingleChoiceItems(items, selectItem, onClick);
        builder.setNegativeButton(cancelName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert = builder.create();
        return alert;
    }

    public static Dialog getDialog(Context context, View view, boolean cancel) {
        final Dialog dialog = new AlertDialog.Builder(context).create();
        dialog.setCancelable(cancel);
        dialog.show();
        dialog.getWindow().setContentView(view);
        return dialog;
    }

    /**
     * 弹出视图的提示框
     *
     * @param context
     * @param view
     * @return
     */
    public static Dialog getDialog(Context context, View view, int i, boolean isCancelable) {
        final Dialog dialog = new Dialog(context, R.style.MyDialogStyleBottom);
        dialog.setCancelable(isCancelable);
        dialog.getWindow().setContentView(view);
        dialog.getWindow().setGravity(i);
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
        //(int) (ApplicationEx.getInstance().getDeviceWidth() * 0.8);
        p.width = (int) ApplicationEx.getInstance().getDeviceWidth();
        p.height = WindowManager.LayoutParams.WRAP_CONTENT;
        return dialog;
    }

    public static ProgressDialog getProgressDialog(Context context, String msg) {
        return getProgressDialog(context, msg, false);
    }

    public static ProgressDialog getProgressDialog(Context context, String msg, boolean isCancelable) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(false);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(isCancelable);
        return progressDialog;
    }

    /***
     * 获取一个系统的Progress耗时等待对话框
     *
     * @param context
     * @param message
     * @return
     */
    public static ProgressDialog getWaitDialog(Context context, String message) {
        ProgressDialog waitDialog = new ProgressDialog(context);
        waitDialog.setCancelable(false);
        waitDialog.setCanceledOnTouchOutside(false);
        if (!TextUtils.isEmpty(message)) {
            waitDialog.setMessage(message);
        }
        return waitDialog;
    }

    /**
     * @param context
     * @param message
     * @return ProgressDialog
     * @Description:TODO 显示系统的ProgressDialog
     */
    public static ProgressDialog showWaitDialog(Context context, String message) {
        if (_waitDialog == null) {
            _waitDialog = getWaitDialog(context, message);
        }
        if (_waitDialog != null) {
            _waitDialog.setMessage(message);
            _waitDialog.show();
        }
        return _waitDialog;
    }

    /**
     * @Description:TODO 隐藏系统的ProgressDialog
     * void
     */
    public static void hideWaitDialog() {
        if (_waitDialog != null) {
            try {
                _waitDialog.dismiss();
                _waitDialog = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


}
