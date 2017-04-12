package com.zjwocai.qundui.activity.mine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.zjwocai.qundui.R;
import com.zjwocai.qundui.activity.BaseProjectActivity;
import com.zjwocai.qundui.util.CallDialogUtil;

public class EtcAskingActivity extends BaseProjectActivity implements View.OnClickListener {

private LinearLayout llFinish;
    public EtcAskingActivity() {
        super(R.layout.activity_etc_asking);
    }

    @Override
    public void findIds() {
        llFinish = (LinearLayout) findViewById(R.id.ll_finish);
    }

    @Override
    public void initViews() {
        initTitle("ETC申办");
        title.setLeftIcon(R.drawable.ic_title_left, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EtcAskingActivity.this.finish();
            }
        });
        title.setRightText("客服", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceNumber = "0571-87620191";
                new CallDialogUtil().showCallDialog(serviceNumber,EtcAskingActivity.this);
            }
        });
        llFinish.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
           finish();

    }
}
