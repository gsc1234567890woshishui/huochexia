package com.zjwocai.qundui.util;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zjwocai.qundui.R;
import com.zjwocai.qundui.activity.mine.EtcPresentActivity;

/**
 * Created by qundui on 2017/3/13.
 */

public class CallDialogUtil {

    public Dialog callDialog;

    /**
     * 拨打电话
     *
     * @param number 电话号码
     */
    public   void showCallDialog(final String number, final Context context) {
        LayoutInflater factory = LayoutInflater.from(context);
        View view = factory.inflate(R.layout.dialog_enter_call3, null);
        final TextView des = (TextView) view.findViewById(R.id.tv_des);
        final TextView enter = (TextView) view.findViewById(R.id.tv_enter);
        final TextView cancel = (TextView) view.findViewById(R.id.tv_cancel);
        final TextView content = (TextView) view.findViewById(R.id.tv_content);
        //des.setText("欢迎致电：");
        content.setText(number);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtil.isNO(number.replace("-","").trim())) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    Uri data = Uri.parse("tel:" + number);
                    intent.setData(data);
                    context.startActivity(intent);
                } else {
                    //showToast("号码无效！");
                    ToastUtil.toastShortShow("号码无效!");
                }
                callDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDialog.dismiss();
            }
        });
        callDialog = DialogUtil.getDialog(context, view, Gravity.CENTER, true);
        callDialog.show();
    }
}
