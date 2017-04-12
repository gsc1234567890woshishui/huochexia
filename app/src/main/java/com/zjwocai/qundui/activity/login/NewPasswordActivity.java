package com.zjwocai.qundui.activity.login;

import android.app.Dialog;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.threeti.teamlibrary.activity.BaseProtocolActivity;
import com.threeti.teamlibrary.model.UserModel;
import com.threeti.teamlibrary.net.BaseModel;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.bill.ProtocolBill;
import com.zjwocai.qundui.fragment.MainActivity;
import com.zjwocai.qundui.util.DialogUtil;
import com.zjwocai.qundui.util.InputUtil;

import java.util.Map;

/**
 * 重置密码2
 * Created by NieLiQin on 2016/7/25.
 */
public class NewPasswordActivity extends BaseProtocolActivity implements View.OnClickListener {

    private EditText etNew, etConfirm;
    private TextView tvOk;
    private Map map;
    private String phone;
    private Dialog netDialog;

    public NewPasswordActivity() {
        super(R.layout.act_new_password);
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        map = (Map) getIntent().getSerializableExtra("data");
        phone = (String) map.get("phone");
        String tag = (String) map.get("tag");
        if ("login".equals(tag)) {
//            needFinish = true;
        }
    }

    @Override
    public void findIds() {
        etNew = (EditText) findViewById(R.id.et_new_pass);
        etConfirm = (EditText) findViewById(R.id.et_confirm);
        tvOk = (TextView) findViewById(R.id.tv_ok);
    }

    @Override
    public void initViews() {
        initTitle("设置密码");
        title.setLeftIcon(R.drawable.ic_title_left, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvOk.setOnClickListener(this);
        netDialog = DialogUtil.getProgressDialog(this,getString(R.string.ui_net_request));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ok:
                if (check()) {
                    netDialog.show();
                    ProtocolBill.getInstance().updatePassword(this, this, etNew.getText().toString().trim(), phone);
                }
                break;
        }
    }

    private boolean check() {
        String pass = etNew.getText().toString().trim();
        if (TextUtils.isEmpty(pass)) {
            showToast("请输入新密码");
            return false;
        }
        String code = etConfirm.getText().toString();
        if (TextUtils.isEmpty(code)) {
            showToast("请确认新密码");
            return false;
        }

        if (!pass.equals(code)) {
            showToast("两次输入的密码不一致");
            return false;
        }

        if (pass.length() < 6) {
            showToast("密码长度不能低于6位");
            return false;
        }

        if (pass.equals("123456")){
            showToast("请勿设置为初始密码");
            return false;
        }

        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        InputUtil.hideInputMethdView(NewPasswordActivity.this, etNew);
        InputUtil.hideInputMethdView(NewPasswordActivity.this, etConfirm);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onTaskSuccess(BaseModel result) {
        if (RQ_GET_UPDATE_PASSWORD.equals(result.getRequest_code())) {
            showToast("修改密码成功!");
            ProtocolBill.getInstance().login(this, this, phone, etNew.getText().toString().trim(), "", "1");
        } else if (RQ_LOGIN.equals(result.getRequest_code())) {
            UserModel model = (UserModel) result.getResult();
            saveUser(model);
            setAliasAndTags();
            startActivity(MainActivity.class);
        }
    }

    @Override
    public void onTaskFinished(String resuestCode) {
        super.onTaskFinished(resuestCode);
        if (netDialog.isShowing()){
            netDialog.dismiss();
        }
    }
}
