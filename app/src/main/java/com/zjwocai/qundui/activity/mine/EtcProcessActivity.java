package com.zjwocai.qundui.activity.mine;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zjwocai.qundui.R;
import com.zjwocai.qundui.activity.BaseProjectActivity;
import com.zjwocai.qundui.adapter.DemoAdapter;
import com.zjwocai.qundui.util.CallDialogUtil;
import com.zjwocai.qundui.util.DialogUtil;
import com.zjwocai.qundui.util.StringUtil;
import com.zjwocai.qundui.util.ToastUtil;
import com.zjwocai.qundui.util.WidgetController;

import static com.zjwocai.qundui.util.AppApplication.getContext;

public class EtcProcessActivity extends BaseProjectActivity implements View.OnClickListener {

    private Dialog callDialog;
    private TextView tvDownload;
    public EtcProcessActivity() {
        super(R.layout.activity_etc_process);
    }

    @Override
    public void findIds() {

        tvDownload  = (TextView) findViewById(R.id.tv_download);


    }

    @Override
    public void initViews() {
        initTitle("ETC充值流程");
        title.setLeftIcon(R.drawable.ic_title_left, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EtcProcessActivity.this.finish();
            }
        });
        title.setRightText("客服", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceNumber = "0571-87620191";
                new CallDialogUtil().showCallDialog(serviceNumber,EtcProcessActivity.this);
            }
        });
        tvDownload.setOnClickListener(this);
        WindowManager wm = this.getWindowManager();

        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        TextView tv = new TextView(this);
        if(tv==null){
            System.out.println("00000000011111000000000000000000000");

        }
        tv.setBackgroundResource(R.drawable.shape_home_list_btn);
        tv.setText("shibushishibuhsfdhsadfhsdihf");
        WidgetController wid = new WidgetController();
        //wid.setLayout(tv,100/2,200/2);

    }



    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tv_download:
                //跳进去app下载市场
                Intent intent= new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://fir.im/1q3g");
                intent.setData(content_url);
                startActivity(intent);
                break;

        }

    }
}
