package com.zjwocai.qundui.activity.goods;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pingplusplus.android.Pingpp;
import com.threeti.teamlibrary.activity.BaseProtocolActivity;
import com.threeti.teamlibrary.net.BaseModel;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.activity.login.LoginActivity;
import com.zjwocai.qundui.bill.ProtocolBill;
import com.zjwocai.qundui.model.BalanceModel;
import com.zjwocai.qundui.util.DialogUtil;
import com.zjwocai.qundui.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单支付
 * Created by NieLiQin on 2016/7/25.
 */
public class OrderPayActivity extends BaseProtocolActivity implements View.OnClickListener {
    private TextView tvCount, tvBalance, tvPay;
    private LinearLayout llBalance, llWeChat, llAliPay;
    private String carid;
    private String orderid;
    private String fee,from;
    private int way = 1; //支付方式：1：余额支付，2支付宝支付，3微信支付
    private boolean isOk = false;
    private Dialog netdialog;

    public OrderPayActivity() {
        super(R.layout.act_order_pay);
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        Map map = (Map) getIntent().getSerializableExtra("data");
        carid = (String) map.get("carid");
        orderid = (String) map.get("orderid");
        fee = (String) map.get("fee");
        from = (String) map.get("from");

        ProtocolBill.getInstance().getBalance(this, this);
    }

    @Override
    public void findIds() {
        tvCount = (TextView) findViewById(R.id.tv_count);
        tvBalance = (TextView) findViewById(R.id.tv_balance);
        llBalance = (LinearLayout) findViewById(R.id.ll_balance);
        llWeChat = (LinearLayout) findViewById(R.id.ll_wechat);
        llAliPay = (LinearLayout) findViewById(R.id.ll_alipay);
        tvPay = (TextView) findViewById(R.id.tv_pay);
    }

    @Override
    public void initViews() {
        initTitle("订单支付");
        title.setLeftIcon(R.drawable.ic_title_left, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvCount.setText(fee);

        llAliPay.setOnClickListener(this);
        llWeChat.setOnClickListener(this);
        llBalance.setOnClickListener(this);
        tvPay.setOnClickListener(this);

        netdialog = DialogUtil.getProgressDialog(this, getString(R.string.ui_net_request));
    }

    @Override
    public void onTaskSuccess(BaseModel result) {
        if (RQ_GET_BALANCE.equals(result.getRequest_code())) {
            BalanceModel balance = (BalanceModel) result.getResult();
            double ba = Double.parseDouble(balance.getBalance());
            double fe = Double.parseDouble(fee);
            if (ba >= fe) {
                isOk = true;
                llBalance.setSelected(true);
                way = 1;
            } else {
                isOk = false;
                llBalance.setSelected(false);
                llBalance.setEnabled(false);
                way = 2;
                llAliPay.setSelected(true);
            }
            tvBalance.setText(balance.getBalance());
        } else if (RQ_PAY_BY_BALANCE.equals(result.getRequest_code())) {
            showToast("支付成功");
            netdialog.dismiss();
            Map map = new HashMap();
            map.put("state",2);
            map.put("id",orderid);
            map.put("from",from);
            startActivity(OrderDetailActivity.class, map, 0);
            finish();
        } else if (RQ_GET_CHARGE.equals(result.getRequest_code())){
            netdialog.dismiss();
            String data = (String) result.getResult();
            Pingpp.createPayment(OrderPayActivity.this, data);
        }
    }

    @Override
    public void onTaskFail(@SuppressWarnings("rawtypes") BaseModel result) {
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_balance:
                setSelect(1);
                break;
            case R.id.ll_alipay:
                setSelect(2);
                break;
            case R.id.ll_wechat:
                setSelect(3);
                break;
            case R.id.tv_pay:
                switch (way) {
                    case 1:
                        netdialog.show();
                        ProtocolBill.getInstance().payByBalance(this, this, orderid, carid, fee);
                        break;
                    case 2:
                        //支付宝支付
                        netdialog.show();
                        ProtocolBill.getInstance().getCharge(this,this,fee,"1",orderid,carid,"","","","","","","");
                        break;
                    case 3:
                        //微信支付
                        netdialog.show();
                        ProtocolBill.getInstance().getCharge(this,this,fee,"2",orderid,carid,"","","","","","","");
                        break;
                }
                break;
        }
    }

    private void setSelect(int data) {
        llBalance.setSelected(false);
        llWeChat.setSelected(false);
        llAliPay.setSelected(false);
        switch (data) {
            case 1:
                if (isOk) {
                    llBalance.setSelected(true);
                    way = 1;
                } else {
                    if (way == 2) {
                        llAliPay.setSelected(true);
                    } else {
                        llWeChat.setSelected(true);
                    }
                }
                break;
            case 2:
                llAliPay.setSelected(true);
                way = 2;
                break;
            case 3:
                llWeChat.setSelected(true);
                way = 3;
                break;
        }
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
                    Map map = new HashMap();
                    map.put("state",2);
                    map.put("id",orderid);
                    map.put("from",from);
                    startActivity(OrderDetailActivity.class, map, 0);
                    finish();
                } else if (result.equals("fail")){
                    ToastUtil.toastShortShow("支付失败！");
                } else if (result.equals("cancel")){
                    ToastUtil.toastShortShow("支付取消！");
                } else if (result.equals("invalid")){
                    ToastUtil.toastShortShow("微信未安装！");
                } else {
                    ToastUtil.toastShortShow("支付服务暂时无法使用!");
                }
            }
        } else if (requestCode == 10086){
            ProtocolBill.getInstance().getBalance(this, this);
        }
    }
}
