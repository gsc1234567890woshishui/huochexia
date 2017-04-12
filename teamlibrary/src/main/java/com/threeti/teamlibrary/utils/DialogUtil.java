package com.threeti.teamlibrary.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

public class DialogUtil {

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
        alert.setCancelable(true);
        alert.setButton(DialogInterface.BUTTON_POSITIVE, btName, onclickListener);
        return alert;
    }

    public static AlertDialog getAlertDialog(Context context, String msg, String btName,
                                             DialogInterface.OnClickListener onclickListener, boolean canCancelable) {
        final AlertDialog alert = new AlertDialog.Builder(context).create();
        alert.setMessage(msg);
        alert.setCancelable(true);
        alert.setButton(DialogInterface.BUTTON_POSITIVE, btName, onclickListener);
        return alert;
    }

    public static AlertDialog getAlertDialog(Context context, String title, String msg, String btName,
                                             DialogInterface.OnClickListener onclickListener) {
        final AlertDialog alert = new AlertDialog.Builder(context).create();
        alert.setTitle(title);
        alert.setMessage(msg);
        alert.setCancelable(true);
        alert.setButton(DialogInterface.BUTTON_POSITIVE, btName, onclickListener);
        return alert;
    }

    public static AlertDialog getAlertDialog(Context context, String title, String msg, String commitName,
                                             String cancelName, DialogInterface.OnClickListener onClick) {
        AlertDialog alert = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
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
                                             String cancelName, DialogInterface.OnClickListener onClick, boolean canCanceable) {
        AlertDialog alert = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setCancelable(canCanceable);
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
        builder.setCancelable(true);
        builder.setPositiveButton(commitName, onClick);
        builder.setNegativeButton(cancelName, oncanClick);
        alert = builder.create();
        return alert;
    }

    public static AlertDialog getAlertDialog(Context context, String title, String msg, String commitName,
                                             String cancelName, DialogInterface.OnClickListener onClick, DialogInterface.OnClickListener oncanClick, boolean canCancelable) {
        AlertDialog alert = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setCancelable(canCancelable);
        builder.setPositiveButton(commitName, onClick);
        builder.setNegativeButton(cancelName, oncanClick);
        alert = builder.create();
        return alert;
    }

    public static AlertDialog getAlertDialog(Context context, String msg, String items[],
                                             DialogInterface.OnClickListener onclickListener) {
        final AlertDialog alert = new AlertDialog.Builder(context).setItems(items, onclickListener).create();
        alert.setTitle(msg);
        alert.setCancelable(true);
        return alert;
    }

    public static AlertDialog getAlertDialog(Context context, String title, String items[], int selectItem,
                                             String cancelName, DialogInterface.OnClickListener onClick) {
        AlertDialog alert = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setCancelable(true);
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

    public static Dialog getDialog(Context context, View view) {
        final Dialog dialog = new AlertDialog.Builder(context).create();
        dialog.setCancelable(false);
        dialog.show();
        dialog.getWindow().setContentView(view);
        return dialog;
    }

    public static Dialog getDialog(Context context, View view, boolean cancel) {
        final Dialog dialog = new AlertDialog.Builder(context).create();
        dialog.setCancelable(cancel);
        dialog.show();
        dialog.getWindow().setContentView(view);
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
}
