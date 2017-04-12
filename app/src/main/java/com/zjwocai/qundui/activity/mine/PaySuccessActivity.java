package com.zjwocai.qundui.activity.mine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.threeti.teamlibrary.activity.BaseProtocolActivity;
import com.zjwocai.qundui.R;

public class PaySuccessActivity extends BaseProtocolActivity implements View.OnClickListener {

private TextView tvSuccess;
    private ImageView ivCancle;
    private RelativeLayout rlLeft;
    public PaySuccessActivity(){
        super(R.layout.activity_pay_success);
    }

    @Override
    public void findIds() {
        tvSuccess = (TextView) findViewById(R.id.tv_success);
        //ivCancle = (ImageView) findViewById(R.id.iv_cancle);
        rlLeft = (RelativeLayout) findViewById(R.id.rl_left);
    }

    @Override
    public void initViews() {

    tvSuccess.setOnClickListener(this);
        //ivCancle.setOnClickListener(this);
        rlLeft.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
          switch (view.getId()){

              case  R.id.rl_left:
                  finish();
                  break;
              case R.id.tv_success:
                  finish();
                  break;



          }
    }
}
