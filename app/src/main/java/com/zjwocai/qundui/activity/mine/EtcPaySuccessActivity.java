package com.zjwocai.qundui.activity.mine;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjwocai.qundui.R;
import com.zjwocai.qundui.activity.BaseProjectActivity;
import com.zjwocai.qundui.util.CallDialogUtil;

import static com.zjwocai.qundui.R.id.tv_pay;

public class EtcPaySuccessActivity extends BaseProjectActivity implements View.OnClickListener {

private TextView tvEtcask;
    private LinearLayout llEtcask;
    private LinearLayout llFinish;
    public EtcPaySuccessActivity() {
        super(R.layout.activity_etc_pay_success);
    }

    @Override
    public void findIds() {
        tvEtcask = (TextView) findViewById(R.id.tv_etcask);
        llFinish = (LinearLayout) findViewById(R.id.ll_finish);
        llEtcask = (LinearLayout) findViewById(R.id.ll_etcask);
    }

    @Override
    public void initViews() {
        initTitle("ETC申办");
        tvEtcask.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        title.setLeftIcon(R.drawable.ic_title_left, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 转到开卡界面
                Intent intent = new Intent();

                intent.putExtra("success","success");
                intent.setClass(EtcPaySuccessActivity.this, EtcProcessActivity.class);
                startActivity(intent);

                EtcPaySuccessActivity.this.finish();
            }
        });
        title.setRightText("客服", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceNumber = "0571-87620191";
                new CallDialogUtil().showCallDialog(serviceNumber,EtcPaySuccessActivity.this);


            }
        });

        llFinish.setOnClickListener(this);
        llEtcask.setOnClickListener(this);
        tvEtcask.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_etcask:
                //跳进去充值流程
                startActivity(new Intent(EtcPaySuccessActivity.this,EtcAskActivity.class));
finish();
                break;
            case R.id.ll_finish:
                //跳进去充值界面
                // 转到开卡界面
                Intent intent = new Intent();

                intent.putExtra("success","success");
                intent.setClass(EtcPaySuccessActivity.this, EtcActivity.class);
                startActivity(intent);

                finish();
                break;

        }

    }
}
