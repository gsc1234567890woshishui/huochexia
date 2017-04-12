package com.zjwocai.qundui.activity.mine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.zjwocai.qundui.R;
import com.zjwocai.qundui.activity.BaseProjectActivity;
import com.zjwocai.qundui.util.CallDialogUtil;

public class EtcCheckFailActivity extends BaseProjectActivity implements View.OnClickListener {

    private LinearLayout llFinish;
    private String carNumber,carNumber2;
    public EtcCheckFailActivity() {
        super(R.layout.activity_etc_check_fail);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_finish:
                //重新上传证件

                Intent intent = new Intent();
                intent.putExtra("checkfail",carNumber);

                intent.setClass(EtcCheckFailActivity.this, EtcAskActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void findIds() {
        llFinish = (LinearLayout) findViewById(R.id.ll_finish);
    }

    @Override
    public void initViews() {
        initTitle("ETC申办");
        Intent intent = new Intent();

        carNumber = intent.getStringExtra("checkfail");//车牌号


        title.setLeftIcon(R.drawable.ic_title_left, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EtcCheckFailActivity.this.finish();
            }
        });
        title.setRightText("客服", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceNumber = "0571-87620191";
                new CallDialogUtil().showCallDialog(serviceNumber,EtcCheckFailActivity.this);
            }
        });
        llFinish.setOnClickListener(this);
    }
}
