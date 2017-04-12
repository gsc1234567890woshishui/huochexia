package com.zjwocai.qundui.activity.money;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pingplusplus.android.Pingpp;
import com.threeti.teamlibrary.activity.BaseActivity;
import com.threeti.teamlibrary.activity.BaseProtocolActivity;
import com.threeti.teamlibrary.model.UserModel;
import com.threeti.teamlibrary.net.BaseModel;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.activity.login.LoginActivity;
import com.zjwocai.qundui.bill.ProtocolBill;
import com.zjwocai.qundui.fragment.MainActivity;
import com.zjwocai.qundui.model.BalanceModel;
import com.zjwocai.qundui.util.DialogUtil;
import com.zjwocai.qundui.util.InputUtil;
import com.zjwocai.qundui.util.ToastUtil;


/**
 * 充值
 * Created by NieLiQin on 2016/7/25.
 */
public class RechargeActivity extends BaseProtocolActivity implements View.OnClickListener {
    private TextView tvMoney, tvPay, tvNumber;
    private EditText etMoney;
    private LinearLayout llWechat, llAlipay;
    private int way = 2; //1支付宝 2微信
    private String count = "";
    private UserModel user;
    private Dialog netdialog;

    public RechargeActivity() {
        super(R.layout.act_recharge);
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        ProtocolBill.getInstance().getBalance(this, this);
        user = ProtocolBill.getInstance().getNowUser();
    }

    @Override
    public void findIds() {
        tvMoney = (TextView) findViewById(R.id.tv_money);
        tvPay = (TextView) findViewById(R.id.tv_pay);
        tvNumber = (TextView) findViewById(R.id.tv_account);
        etMoney = (EditText) findViewById(R.id.et_count);
        llWechat = (LinearLayout) findViewById(R.id.ll_wechat);
        //llAlipay = (LinearLayout) findViewById(R.id.ll_alipay);
    }

    @Override
    public void initViews() {
        initTitle("充值");
        title.setLeftIcon(R.drawable.ic_title_left, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //llAlipay.setOnClickListener(this);
        llWechat.setOnClickListener(this);
        tvPay.setOnClickListener(this);

        llWechat.setSelected(true);
        way = 2;

        tvNumber.setText(user.getMobile().substring(0, 3) + "****"
                + user.getMobile().substring(user.getMobile().length() - 4
                , user.getMobile().length()));

        netdialog = DialogUtil.getProgressDialog(this, getString(R.string.ui_net_request));
    }

    private void setSelect(int data) {
        llWechat.setSelected(false);
        //llAlipay.setSelected(false);
        switch (data) {
//            case 1:
//                llAlipay.setSelected(true);
//                way = 1;
//                break;
            case 2:
                llWechat.setSelected(true);
                way = 2;
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_wechat:
                setSelect(2);
                break;
//            case R.id.ll_alipay:
//                setSelect(1);
//                break;
            case R.id.tv_pay:
                if (check()){
                    switch (way) {
                        case 1:
                            //支付宝支付
                            netdialog.show();
                            ProtocolBill.getInstance().getCharge(this, this, count, "1", "","","","","","","","","");
                            break;
                        case 2:
                            //微信支付
                            netdialog.show();
                            ProtocolBill.getInstance().getCharge(this, this, count, "2", "","","","","","","","","");
                            break;
                    }
                }
                break;
        }
    }

    private boolean check() {
        count = etMoney.getText().toString().trim();
        if (TextUtils.isEmpty(count)) {
            showToast("请输入充值金额");
            return false;
        }
        if (Double.parseDouble(count) <= 0) {
            showToast("充值金额不能小于或等于0元");
            return false;
        }
        return true;
    }

    @Override
    public void onTaskSuccess(BaseModel result) {
        if (RQ_GET_BALANCE.equals(result.getRequest_code())) {
            tvMoney.setText(((BalanceModel) result.getResult()).getBalance());
        } else if (RQ_GET_CHARGE.equals(result.getRequest_code())){
            netdialog.dismiss();
            String data = (String) result.getResult();
            Pingpp.createPayment(RechargeActivity.this, data);
        }
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
        if (netdialog.isShowing()){
            netdialog.dismiss();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        InputUtil.hideInputMethdView(RechargeActivity.this, etMoney);
        return super.dispatchTouchEvent(ev);
    }

    /**
     * onActivityResult 获得支付结果，如果支付成功，服务器会收到ping++ 服务器发送的异步通知。
     * 最终支付成功根据异步通知为准
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                if (result.equals("success")){
                    ToastUtil.toastShortShow("支付成功！");
                    startActivity(MainActivity.class,null,0);
                } else if (result.equals("fail")){
                    ToastUtil.toastShortShow("支付失败！");
                } else if (result.equals("cancel")){
                    ToastUtil.toastShortShow("支付取消！");
                } else if (result.equals("invalid")){
                    ToastUtil.toastShortShow("微信未安装！");
                } else {
                    ToastUtil.toastShortShow("支付服务暂时无法使用!");
                }
//                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
//                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
//                showMsg(result, errorMsg, extraMsg);
            }
        }if (requestCode == 10086){
            ProtocolBill.getInstance().getBalance(this,this);
        }
    }

    public void showMsg(String title, String msg1, String msg2) {
        String str = title;
        if (null != msg1 && msg1.length() != 0) {
            str += "\n" + msg1;
        }
        if (null != msg2 && msg2.length() != 0) {
            str += "\n" + msg2;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(RechargeActivity.this);
        builder.setMessage(str);
        builder.setTitle("提示");
        builder.setPositiveButton("OK", null);
        builder.create().show();
    }
}
