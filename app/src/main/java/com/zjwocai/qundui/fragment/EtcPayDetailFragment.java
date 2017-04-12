package com.zjwocai.qundui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pingplusplus.android.Pingpp;
import com.threeti.teamlibrary.finals.ProjectConstant;
import com.threeti.teamlibrary.model.UserModel;
import com.threeti.teamlibrary.net.BaseModel;
import com.threeti.teamlibrary.net.ProcotolCallBack;
import com.threeti.teamlibrary.utils.SPUtil;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.activity.login.LoginActivity;
import com.zjwocai.qundui.activity.mine.EtcPaySuccessActivity;
import com.zjwocai.qundui.activity.mine.PaySuccessActivity;
import com.zjwocai.qundui.bill.ProtocolBill;
import com.zjwocai.qundui.model.BalanceModel;
import com.zjwocai.qundui.util.DialogUtil;
import com.zjwocai.qundui.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

import static com.threeti.teamlibrary.finals.RequestCodeSet.RQ_GET_BALANCE;
import static com.threeti.teamlibrary.finals.RequestCodeSet.RQ_GET_ETCCHARE;


/**
 * 底部弹窗Fragment
 */
public class EtcPayDetailFragment extends DialogFragment implements ProcotolCallBack,RadioGroup.OnCheckedChangeListener {
    private RelativeLayout rePayWay, rePayDetail, reBalance;
    private LinearLayout LinPayWay,linPass,llWeChat,llBalance;
    private ListView lv;
    private Button btnPay;
    private EditText gridPasswordView;
    private CheckBox cbWeChat,cbBalance;

    private UserModel user;
    private PopupWindows pop;
    private TextView phone,tvMoney,tvCarClick,tvCarNumber,tvEtcNumber,tvReality;
    private String faceprice,salePrice,carId;
    private String s2;
    private String phonenumber;
    private String s4;
    private View tvPay;
    private RadioButton rbTag1, rbTag2;
    private BalanceModel model;
    private String amount;
    private String cz="1";
    private  String carClick,carNumber,etcNumber;

    private ImageView imageCloseTwo,imageCloseThree;
    private RelativeLayout imageCloseOne;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.etc_fragment_pay_detail);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        final Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.AnimBottom);
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.height = getActivity().getWindowManager().getDefaultDisplay().getHeight() * 7 / 10;
        window.setAttributes(lp);

        initView(dialog);

        if (getDialog() != null) {
            getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface anInterface, int keyCode, KeyEvent event) {
                    if(keyCode== KeyEvent.KEYCODE_ENTER&&event.getAction()== KeyEvent.ACTION_DOWN){
                        if(!TextUtils.isEmpty(gridPasswordView.getText().toString().trim())){
                            if("123456".equals(gridPasswordView.getText().toString().trim())){
                                //TODO 跳转支付宝支付
                            }
                        }
                    }else{
                        Toast.makeText(getContext(),"密码不能为空", Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });
        }
        return dialog;
    }
    public void saveUser(UserModel model) {
        SPUtil.saveObjectToShare(ProjectConstant.KEY_USERINFO, model);
    }

    private void initView(Dialog dialog) {
        EventBus.getDefault().register(this);
        user = ProtocolBill.getInstance().getNowUser();
        tvPay = dialog.findViewById(R.id.tv_pay);

        rePayDetail = (RelativeLayout) dialog.findViewById(R.id.re_pay_detail);//付款详情
        LinPayWay = (LinearLayout) dialog.findViewById(R.id.lin_pay_way);//付款方式
        reBalance = (RelativeLayout) dialog.findViewById(R.id.re_balance);//付款方式（余额）
        tvReality = (TextView) dialog.findViewById(R.id.tv_reality);
        btnPay = (Button) dialog.findViewById(R.id.btn_confirm_pay);
        gridPasswordView = (EditText) dialog.findViewById(R.id.pass_view);
        linPass = (LinearLayout)dialog.findViewById(R.id.lin_pass);
        rbTag1 = (RadioButton) dialog.findViewById(R.id.rb_tag1);
        rbTag2 = (RadioButton) dialog.findViewById(R.id.rb_tag2);
        imageCloseOne= (RelativeLayout) dialog.findViewById(R.id.close_one);
        imageCloseTwo= (ImageView) dialog.findViewById(R.id.close_two);
        imageCloseThree= (ImageView) dialog.findViewById(R.id.close_three);
        phone = (TextView) dialog.findViewById(R.id.tv_account);
        llWeChat = (LinearLayout) dialog.findViewById(R.id.ll_wechat);
        llBalance = (LinearLayout) dialog.findViewById(R.id.ll_balance);
        tvMoney = (TextView) dialog.findViewById(R.id.tv_money);

        tvCarClick = (TextView) dialog.findViewById(R.id.tv_carclick);
        tvCarNumber = (TextView) dialog.findViewById(R.id.tv_carnumber);
        tvEtcNumber = (TextView) dialog.findViewById(R.id.tv_etcnumber);

        reBalance.setOnClickListener(listener);
        btnPay.setOnClickListener(listener);
        imageCloseOne.setOnClickListener(listener);
        imageCloseTwo.setOnClickListener(listener);
        imageCloseThree.setOnClickListener(listener);
        llWeChat.setOnClickListener(listener);
        llBalance.setOnClickListener(listener);
        Bundle bundle = this.getArguments();
         carClick = (String) bundle.get("carClick");
         carId = (String) bundle.get("carId");
        Log.v("cccccccccccccccccccc",carId);
         carNumber = (String) bundle.get("carNumber");
         etcNumber = (String) bundle.get("etcNumber");

        tvCarClick.setText(carClick);
        tvReality.setText(carClick);
        tvCarNumber.setText(carNumber);
        tvEtcNumber.setText(etcNumber);

        amount =  carClick.substring(0,carClick.length()-1);

        //得到司机余额
        ProtocolBill.getInstance().getBalance(new ProcotolCallBack() {
            @Override
            public void onTaskSuccess(BaseModel result) {
                if (RQ_GET_BALANCE.equals(result.getRequest_code())){
                    model = (BalanceModel) result.getResult();
                    if (model != null && !model.equals("")){
                        tvMoney.setText("("+"剩余 ¥ "+model.getBalance()+")");
                    }
                }
            }

            @Override
            public void onTaskFail(BaseModel result) {
//        super.onTaskFail(result);
                if (result.getMsgtype() != null && result.getMsgtype().equals("2")){
                    ToastUtil.toastShortShow("登录失效，请重新登录");
                    saveUser(null);
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                }else if (!TextUtils.isEmpty(result.getMsg())) {
                    ToastUtil.toastShortShow("result.getMsg() + \"\"");
                }
            }

            @Override
            public void onTaskFinished(String resuestCode) {

            }
        }, getActivity());


        phone.setText(user.getMobile().substring(0, 3) + "****"
                + user.getMobile().substring(user.getMobile().length() - 4
                , user.getMobile().length()));



    }
    private void goSelect(int tag) {
        EventMessage eventMessage = new EventMessage();
        eventMessage.setTag(tag);
        EventBus.getDefault().post(eventMessage);

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setGoIndex(EventMessage eventMessage){

        if(eventMessage!=null){
            int tag = eventMessage.getTag();

            if(tag== EventMessage.EventMessageAction.TAG_RECHARGE_DIALOG){//恢复可点击
                btnPay.setClickable(true);//恢复可点击
            }else if(tag== EventMessage.EventMessageAction.TAG_REMOVE_DIALOG){ //关闭dialogframent
                getDialog().dismiss();
            }
        }
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Animation slide_left_to_left = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_left_to_left);
            Animation slide_right_to_left = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_right_to_left);
            Animation slide_left_to_right = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_left_to_right);
            Animation slide_left_to_left_in = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_left_to_left_in);
            switch (view.getId()) {
                case R.id.ll_wechat:
                    rbTag1.setChecked(true);

                    break;
                case R.id.ll_balance:
                    rbTag2.setChecked(true);
                    break;
                case R.id.re_balance:
                    rePayDetail.startAnimation(slide_left_to_left_in);
                    rePayDetail.setVisibility(View.VISIBLE);
                    LinPayWay.startAnimation(slide_left_to_right);
                    LinPayWay.setVisibility(View.GONE);
                    break;
                case R.id.btn_confirm_pay://确认付款
                        if(rbTag1.isChecked()){ //如果是微信付款
                            btnPay.setClickable(false);//设置不可点击
                            DialogUtil.showWaitDialog(getActivity(),"加载中...");
                            final TimerTask task = new TimerTask(){
                                public void run(){
                                    DialogUtil.hideWaitDialog();
                                }
                            };
                            Timer timer = new Timer();
                            timer.schedule(task, 4000);
                            Log.e("gggggggggggg========",amount+"=="+carId+"=="+carNumber+"=="+etcNumber);
                                ProtocolBill.getInstance().getEtcCharge(new ProcotolCallBack() {
                                                                            @Override
                                                                            public void onTaskSuccess(BaseModel result) {

                                                                                    String data = (String) result.getResult();
                                                                                    System.out.println("ddddddddddddddd"+data);
                                                                                    Pingpp.createPayment(getActivity(), data);
                                                                                    //startActivity(new Intent(getActivity(), EtcPaySuccessActivity.class));
                                                                            }

                                                                            @Override
                                                                            public void onTaskFail(BaseModel result) {

                                                                            }

                                                                            @Override
                                                                            public void onTaskFinished(String resuestCode) {

                                                                            }
                                                                        }, getActivity(), "2", amount, "", carId, "3", "", amount, "", amount, "", "",
                                        carNumber, etcNumber);
                        }else{ //余额支付
                            if(Double.parseDouble(model.getBalance())<Double.parseDouble(amount)){//如果余额不足
                                ToastUtil.toastShortShow("余额不足");
                            }else {
                                pop = new EtcPayDetailFragment.PopupWindows(getActivity(), phone);
                            }
                        }

                    break;
                case R.id.close_one:
                    getDialog().dismiss();
                    break;
                case R.id.close_two:
                    getDialog().dismiss();
                    break;
                case R.id.close_three:
                    getDialog().dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onTaskSuccess(BaseModel result) {
        System.out.println("ddddddddddddddd");
       if(rbTag1.isChecked()){
           if(RQ_GET_ETCCHARE==result.getRequest_code()){//微信充值成功
               String data = (String) result.getResult();
               System.out.println("ddddddddddddddd"+data);
               Pingpp.createPayment(getActivity(), data);
               startActivity(new Intent(getActivity(), EtcPaySuccessActivity.class));
       }
       }else{//余额充值成功
           //弹窗消失
           if(RQ_GET_ETCCHARE==result.getRequest_code()){
               getDialog().dismiss();
               DialogUtil.hideWaitDialog();
               startActivity(new Intent(getActivity(),EtcPaySuccessActivity.class));
           }


       }

    }

    @Override
    public void onTaskFail(BaseModel result) {

    }

    @Override
    public void onTaskFinished(String resuestCode) {

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        //判断哪个按钮被选中
        if (i == rbTag1.getId()) {  //微信充值

        } else if (i == rbTag2.getId()) { //余额充值

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
            ProtocolBill.getInstance().getCode(EtcPayDetailFragment.this,getActivity(),user.getMobile(),"2");
            ToastUtil.toastShortShow("已发送验证码至您的手机");

            /**
             * 计时器
             */
            class TimeCount extends CountDownTimer {
                public TimeCount(long millisInFuture, long countDownInterval) {
                    super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
                }

                @Override
                public void onFinish() {//计时完毕时触发
                    if(getActivity()==null){
                        return;
                    }
                    if(tv!=null){
                        tv.setSelected(true);
                        tv.setClickable(true);
                        tv.setText("获取验证码");
                        //tv.setTextColor(getActivity().getResources().getColor(R.color.t12b7f5));
                        tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.t12b7f5));
                    }

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

            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

            bt1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
                    dismiss();
                    //fragment消失
                     //getDialog().dismiss();
                }
            });
            bt2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String content = et.getText().toString().trim();
                    if (content != null && !content.equals("")) {


                        ProtocolBill.getInstance().checkWithdrawCode(new ProcotolCallBack() {
                            @Override
                            public void onTaskSuccess(BaseModel result) {
                                //if (RQ_CHECK_WITHDRAW_CODE.equals(result.getRequest_code())){//如果验证码校验成功
                                    pop.dismiss();
                                DialogUtil.showWaitDialog(getActivity(),"加载中...");
                                final TimerTask task = new TimerTask(){
                                    public void run(){
                                        DialogUtil.hideWaitDialog();
                                         //ToastUtil.toastShortShow("支付失败");
                                    }
                                };
                                Timer timer = new Timer();
                                timer.schedule(task, 14000);
                                ProtocolBill.getInstance().getEtcCharge(new ProcotolCallBack() {
                                                                            @Override
                                                                            public void onTaskSuccess(BaseModel result) {
                                                                                getDialog().dismiss();
                                                                                DialogUtil.hideWaitDialog();
                                                                                startActivity(new Intent(getActivity(),EtcPaySuccessActivity.class));
                                                                            }

                                                                            @Override
                                                                            public void onTaskFail(BaseModel result) {

                                                                            }

                                                                            @Override
                                                                            public void onTaskFinished(String resuestCode) {

                                                                            }
                                                                        }, getActivity(), "3", amount, "", carId, "3", "", amount, "", amount, "", "",
                                        carNumber, etcNumber);

                            }
                            //}
                            @Override
                            public void onTaskFail(BaseModel result) {
                                //短信校验失败
                                if (!TextUtils.isEmpty(result.getMsg())) {
                                    ToastUtil.toastShortShow(result.getMsg() + "");
                                }
                            }

                            @Override
                            public void onTaskFinished(String resuestCode) {

                            }
                        }, getActivity(), user.getMobile(), content);
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
                    } else {

                        ToastUtil.toastShortShow("验证码不能为空");
                    }
                }
            });
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProtocolBill.getInstance().getCode(EtcPayDetailFragment.this,getActivity(),user.getMobile(),"2");
                    //showToast("已发送验证码至您的手机");
                    ToastUtil.toastShortShow("已发送验证码至您的手机");
                    TimeCount timeCount = new TimeCount(60000, 1000);
                    tv.setSelected(false);
                    tv.setClickable(false);
                    tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.tb5b5b5));

                    timeCount.start();
                }
            });
        }
}
}
