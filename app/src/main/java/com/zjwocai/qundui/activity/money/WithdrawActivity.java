package com.zjwocai.qundui.activity.money;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.threeti.teamlibrary.activity.BaseActivity;
import com.threeti.teamlibrary.activity.BaseProtocolActivity;
import com.threeti.teamlibrary.model.UserModel;
import com.threeti.teamlibrary.net.BaseModel;
import com.threeti.teamlibrary.utils.StringUtil;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.activity.login.LoginActivity;
import com.zjwocai.qundui.activity.mine.CardAddActivity;
import com.zjwocai.qundui.adapter.WithdrawBankAdapter;
import com.zjwocai.qundui.bill.ProtocolBill;
import com.zjwocai.qundui.fragment.MainActivity;
import com.zjwocai.qundui.model.BalanceModel;
import com.zjwocai.qundui.model.CardModel;
import com.zjwocai.qundui.util.DialogUtil;
import com.zjwocai.qundui.util.InputUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 提现
 * Created by NieLiQin on 2016/7/25.
 */
public class WithdrawActivity extends BaseProtocolActivity implements View.OnClickListener {
    private RelativeLayout rlBind;
    private TextView tvAccount, tvMoney, tvPay;
    private EditText etCount;
    private ListView lv;
    private List<CardModel> cardModels;
    private WithdrawBankAdapter mAdapter;
    private UserModel user;
    private Dialog netdialog;
    private PopupWindows pop;
    //    private

    public WithdrawActivity() {
        super(R.layout.act_withdraw);
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        ProtocolBill.getInstance().getCards(this, this, "1");
        ProtocolBill.getInstance().getBalance(this, this);
        user = ProtocolBill.getInstance().getNowUser();
    }

    @Override
    public void findIds() {

        rlBind = (RelativeLayout) findViewById(R.id.rl_bind);
        tvAccount = (TextView) findViewById(R.id.tv_account);
        tvMoney = (TextView) findViewById(R.id.tv_money);
        etCount = (EditText) findViewById(R.id.et_count);
        lv = (ListView) findViewById(R.id.lv_withdraw);
        tvPay = (TextView) findViewById(R.id.tv_pay);

        rlBind.setVisibility(View.GONE);
    }

    @Override
    public void initViews() {
        initTitle("提现");
        title.setLeftIcon(R.drawable.ic_title_left, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cardModels = new ArrayList<>();
        mAdapter = new WithdrawBankAdapter(this, cardModels);
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < cardModels.size(); i++) {
                    cardModels.get(i).setSelected(false);
                }
                cardModels.get((int) id).setSelected(true);
                mAdapter.notifyDataSetChanged();
            }
        });
        tvPay.setOnClickListener(this);

        tvAccount.setText(user.getMobile().substring(0, 3) + "****"
                + user.getMobile().substring(user.getMobile().length() - 4
                , user.getMobile().length()));

        netdialog = DialogUtil.getProgressDialog(this, getResources().getString(R.string.ui_net_request));
    }

    private CardModel getSelectedCard() {
        CardModel model = null;
        for (CardModel car : cardModels) {
            if (car.isSelected()) {
                model = car;
            }
        }
        return model;
    }

    @Override
    public void onTaskSuccess(BaseModel result) {
        if (RQ_GET_BALANCE.equals(result.getRequest_code())) {
            tvMoney.setText(((BalanceModel) result.getResult()).getBalance());
        } else if (RQ_GET_CARDS.equals(result.getRequest_code())) {
            List<CardModel> models = (List<CardModel>) result.getResult();
            if (models != null && !models.isEmpty()) {
                cardModels.addAll(models);
                cardModels.get(0).setSelected(true);
                mAdapter.notifyDataSetChanged();
            } else {
                rlBind.setVisibility(View.VISIBLE);
                rlBind.setOnClickListener(this);
            }

        }else if (RQ_CHECK_WITHDRAW_CODE.equals(result.getRequest_code())){
            ProtocolBill.getInstance().withdraw(this, this, getSelectedCard().getId(), etCount.getText().toString().trim());
        }else if (RQ_GET_WITHDRAW.equals(result.getRequest_code())) {
            pop.dismiss();
            showToast("提现申请提交成功");
            netdialog.dismiss();
            startActivity(MainActivity.class, null, 0);
            finish();
        }
    }

    @Override
    public void onTaskFail(@SuppressWarnings("rawtypes") BaseModel result) {
//        super.onTaskFail(result);
        if (result.getMsgtype() != null && result.getMsgtype().equals("2")) {
            showToast("登录失效，请重新登录");
            saveUser(null);
            startActivity(LoginActivity.class);
        } else if (!TextUtils.isEmpty(result.getMsg())) {
            showToast(result.getMsg() + "");
        }
    }

    @Override
    public void onTaskFinished(String resuestCode) {
        super.onTaskFinished(resuestCode);
        if (netdialog.isShowing()) {
            netdialog.dismiss();
        }
    }

    private boolean check() {
        String count = etCount.getText().toString().trim();
        if (TextUtils.isEmpty(count)) {
            showToast("请输入提现金额");
            return false;
        }
        if (Double.parseDouble(count) <= 0) {
            showToast("提现金额不能小于或等于0元");
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_pay:
                if (check()) {
                    CardModel model = getSelectedCard();
                    if (model != null) {
                        pop = new PopupWindows(this,tvAccount);
                    } else {
                        showToast("请您先绑定银行卡");
                    }
                }
                break;
            case R.id.rl_bind:
                startActivityForResult(CardAddActivity.class, null, 0);
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        InputUtil.hideInputMethdView(WithdrawActivity.this, etCount);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            boolean flag = data.getBooleanExtra("data", false);
            if (flag) {
                rlBind.setVisibility(View.GONE);
                rlBind.setOnClickListener(null);
                ProtocolBill.getInstance().getCards(this, this, "1");
            }
        } else if (requestCode == 10086){
            ProtocolBill.getInstance().getCards(this, this, "1");
            ProtocolBill.getInstance().getBalance(this, this);
        }
    }

    class PopupWindows extends PopupWindow {

        public PopupWindows(Context mContext, View parent) {

            View view = View
                    .inflate(mContext, R.layout.dialog_item_code, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
//            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
//                    R.anim.push_bottom_in_2));

            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.TOP, 0, 0);
            update();

            Button bt1 = (Button) view
                    .findViewById(R.id.btn_popupwindows_reward_cancel);
            Button bt2 = (Button) view
                    .findViewById(R.id.btn_popupwindows_reward_enter);
            final EditText et = (EditText) view
                    .findViewById(R.id.et_popupwindows_code);
            final TextView tv = (TextView) view
                    .findViewById(R.id.tv_popupwindows_time);
            final TextView tvNumber = (TextView) view
                    .findViewById(R.id.tv_popupwindows_number);
            tvNumber.setText(user.getMobile().toString());
            ProtocolBill.getInstance().getCode(WithdrawActivity.this,WithdrawActivity.this,user.getMobile(),"2");
            showToast("已发送验证码至您的手机");

            /**
             * 计时器
             */
            class TimeCount extends CountDownTimer {
                public TimeCount(long millisInFuture, long countDownInterval) {
                    super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
                }

                @Override
                public void onFinish() {//计时完毕时触发
                    if(WithdrawActivity.this==null){
                        return;
                    }
                    if(tv!=null){
                        tv.setSelected(true);
                        tv.setClickable(true);
                        tv.setText("获取验证码");
                        //tv.setTextColor(getActivity().getResources().getColor(R.color.t12b7f5));
                        tv.setTextColor(ContextCompat.getColor(WithdrawActivity.this, R.color.t12b7f5));
                    }
//                    tv.setSelected(true);
//                    tv.setClickable(true);
//                    tv.setText("获取验证码");
//                    tv.setTextColor(WithdrawActivity.this.getResources().getColor(R.color.t12b7f5));
                }

                @Override
                public void onTick(long millisUntilFinished) {//计时过程显示
                    tv.setText(millisUntilFinished / 1000 + "s");
                }
            }

            TimeCount timeCount = new TimeCount(60000, 1000);
            tv.setSelected(false);
            tv.setClickable(false);
            timeCount.start();

            InputMethodManager imm = (InputMethodManager) WithdrawActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

            bt1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    InputMethodManager imm = (InputMethodManager) WithdrawActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
                    dismiss();
                }
            });
            bt2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String content = et.getText().toString().trim();
                    if (content != null && !content.equals("")) {
                        ProtocolBill.getInstance().checkWithdrawCode(WithdrawActivity.this,WithdrawActivity.this,user.getMobile(),content);
                        //netdialog.show();
                        InputMethodManager imm = (InputMethodManager) WithdrawActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);

                    } else {
                        showToast("验证码不能为空");
                    }
                }
            });
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProtocolBill.getInstance().getCode(WithdrawActivity.this,WithdrawActivity.this,user.getMobile(),"2");
                    showToast("已发送验证码至您的手机");
                    TimeCount timeCount = new TimeCount(60000, 1000);
                    tv.setSelected(false);
                    tv.setClickable(false);
                    tv.setTextColor(WithdrawActivity.this.getResources().getColor(R.color.tb5b5b5));
                    timeCount.start();
                }
            });
        }
    }
}
