package com.zjwocai.qundui.activity.mine;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.threeti.teamlibrary.activity.BaseActivity;
import com.threeti.teamlibrary.activity.BaseProtocolActivity;
import com.threeti.teamlibrary.net.BaseModel;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.activity.login.LoginActivity;
import com.zjwocai.qundui.bill.ProtocolBill;
import com.zjwocai.qundui.util.DialogUtil;
import com.zjwocai.qundui.util.InputUtil;


/**
 * 用户反馈
 * Created by NieLiQin on 2016/7/26.
 */
public class FeedbackActivity extends BaseProtocolActivity implements TextWatcher, View.OnClickListener {
    private EditText etContent;
    private LinearLayout llHint;
    private TextView tvEnter;
    private ImageView ivCancel;

    private Dialog netdialog;

    public FeedbackActivity() {
        super(R.layout.act_feedback);
    }

    @Override
    public void findIds() {
        etContent = (EditText) findViewById(R.id.et_feedback);
        llHint = (LinearLayout) findViewById(R.id.ll_hint);
        tvEnter = (TextView) findViewById(R.id.tv_enter);
        ivCancel = (ImageView) findViewById(R.id.iv_cancel);
    }

    @Override
    public void initViews() {
//        initTitle("意见反馈");
//        title.setLeftIcon(R.drawable.ic_title_left, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        etContent.addTextChangedListener(this);
        tvEnter.setOnClickListener(this);
        ivCancel.setOnClickListener(this);

        netdialog = DialogUtil.getProgressDialog(this,getString(R.string.ui_net_request));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(s)){
            llHint.setVisibility(View.VISIBLE);
        } else {
            llHint.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_enter:
                if (!TextUtils.isEmpty(etContent.getText().toString().trim())){
                    netdialog.show();
                    ProtocolBill.getInstance().feedback(this,this,etContent.getText().toString().trim());
                } else {
                    showToast("反馈内容不能为空!");
                }
                break;
            case R.id.iv_cancel:
                finish();
                break;
        }
    }

    @Override
    protected void onPause() {
        InputUtil.hideInputMethdView(FeedbackActivity.this, etContent);
        super.onPause();
    }

    @Override
    public void onTaskSuccess(BaseModel result) {
        showToast("反馈成功!");
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (netdialog != null && netdialog.isShowing()){
                    netdialog.dismiss();
                }
                finish();
            }
        }, 1000);
    }

    @Override
    public void onTaskFail(@SuppressWarnings("rawtypes") BaseModel result) {
//        super.onTaskFail(result);
        if (result.getMsgtype() != null && result.getMsgtype().equals("2")){
            showToast("登录失效，请重新登录");
            saveUser(null);
            startActivity(LoginActivity.class);
        }else if (!TextUtils.isEmpty(result.getMsg())) {
            showToast(result.getMsg() + "");
        }
    }

    @Override
    public void onTaskFinished(String resuestCode) {
        super.onTaskFinished(resuestCode);
        if (netdialog != null && netdialog.isShowing()){
            netdialog.dismiss();
        }
    }
}
