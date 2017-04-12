package com.zjwocai.qundui.activity.login;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.threeti.teamlibrary.activity.BaseProtocolActivity;
import com.threeti.teamlibrary.net.BaseModel;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.bill.ProtocolBill;
import com.zjwocai.qundui.util.InputUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * 重置初始密码密码1
 * Created by NieLiQin on 2016/7/25.
 */
public class ResetPasswordCheckActivity extends BaseProtocolActivity implements View.OnClickListener {
    private EditText etPhone, etPass;
    private TextView tvCode, tvNext;
    private TimeCount timeCount;
    private String code;
    private String phone;
    private String number;

    public ResetPasswordCheckActivity() {
        super(R.layout.act_set_password);
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        phone =  (String) getIntent().getSerializableExtra("data");
    }

    @Override
    public void findIds() {
        etPhone = (EditText) findViewById(R.id.et_phone);
        etPass = (EditText) findViewById(R.id.et_code);
        tvCode = (TextView) findViewById(R.id.tv_code);
        tvNext = (TextView) findViewById(R.id.tv_next);
    }

    @Override
    public void initViews() {
        initTitle("重置密码");
        title.setLeftIcon(R.drawable.ic_title_left, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvCode.setOnClickListener(this);
        tvNext.setOnClickListener(this);

        etPhone.setText(phone);
        etPhone.setEnabled(false);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        InputUtil.hideInputMethdView(ResetPasswordCheckActivity.this, etPass);
        InputUtil.hideInputMethdView(ResetPasswordCheckActivity.this, etPhone);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_code:
                if (TextUtils.isEmpty(etPhone.getText().toString())) {
                    showToast("请输入手机号");

                } else if (etPhone.getText().toString().length() < 11) {
                    showToast("请输入11位手机号");
                } else {
                    number = etPhone.getText().toString().trim();
                    ProtocolBill.getInstance().getCode(this, this, number, "1");
                    showCode();
                }
                break;
            case R.id.tv_next:
                if (check()) {
                    if (etPass.getText().toString().trim().length() == 4) {
                        phone = etPass.getText().toString().trim();
                        ProtocolBill.getInstance().checkCode(this, this, number, phone);
                    } else {
                        showToast("请输入正确的验证码");
                    }
                }
                break;
        }
    }

    private boolean check() {
        if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
            showToast("请输入手机号");
            return false;
        }
        if (etPhone.getText().toString().length() < 11) {
            showToast("请输入11位手机号");
        }
        String code = etPass.getText().toString();
        if (TextUtils.isEmpty(code)) {
            showToast("请输入验证码");
            return false;
        }
        return true;
    }

    public void showCode() {
        timeCount = new TimeCount(60000, 1000);
        tvCode.setSelected(false);
        tvCode.setClickable(false);
        timeCount.start();
    }

    @Override
    public void onTaskSuccess(BaseModel result) {
        if (RQ_CHECK_CODE.equals(result.getRequest_code())) {
            Map map = new HashMap();
            map.put("tag", "set");
            map.put("phone", number);
            startActivity(ResetPasswordActivity.class, map);
        }
    }


    /**
     * 计时器
     */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            tvCode.setSelected(true);
            tvCode.setClickable(true);
            tvCode.setText("获取验证码");
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            tvCode.setText(millisUntilFinished / 1000 + "s");
        }
    }
}
