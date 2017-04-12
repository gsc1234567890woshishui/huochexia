package com.zjwocai.qundui.activity.mine;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.threeti.teamlibrary.activity.BaseActivity;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.bill.ProtocolBill;

/**
 * 选择性别
 * Created by NieLiQin on 2016/7/26.
 */
public class SexActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivMale, ivFemale;
    private RelativeLayout rlMale, rlFemale;
    private String type;

    public SexActivity() {
        super(R.layout.act_sex);
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        type = getIntent().getStringExtra("data");
    }

    @Override
    public void findIds() {
        rlMale = (RelativeLayout) findViewById(R.id.rl_male);
        rlFemale = (RelativeLayout) findViewById(R.id.rl_female);
        ivMale = (ImageView) findViewById(R.id.iv_male_select);
        ivFemale = (ImageView) findViewById(R.id.iv_female_select);
    }

    @Override
    public void initViews() {
        initTitle("性别");
        title.setLeftIcon(R.drawable.ic_title_left, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (type.equals("1")) {
            ivMale.setVisibility(View.VISIBLE);
            ivFemale.setVisibility(View.GONE);
        } else if (type.equals("2")) {
            ivMale.setVisibility(View.GONE);
            ivFemale.setVisibility(View.VISIBLE);
        } else {
            ivMale.setVisibility(View.GONE);
            ivFemale.setVisibility(View.GONE);
        }

        rlMale.setOnClickListener(this);
        rlFemale.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_male:
                ivMale.setVisibility(View.VISIBLE);
                ivFemale.setVisibility(View.GONE);
                break;
            case R.id.rl_female:
                ivMale.setVisibility(View.GONE);
                ivFemale.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void finish() {
        String state = "0";
        if (ivMale.getVisibility() == View.VISIBLE) {
            state = "1";
        } else if (ivFemale.getVisibility() == View.VISIBLE) {
            state = "2";
        }
        Intent intent = new Intent();
        intent.putExtra("state", state);
        intent.putExtra("type", "sex");
        setResult(Activity.RESULT_OK, intent);
        super.finish();
    }
}
