package com.zjwocai.qundui.fragmenthome;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.threeti.teamlibrary.finals.ProjectConstant;
import com.threeti.teamlibrary.finals.RequestCodeSet;
import com.threeti.teamlibrary.model.UserModel;
import com.threeti.teamlibrary.net.BaseModel;
import com.threeti.teamlibrary.net.ProcotolCallBack;
import com.threeti.teamlibrary.utils.SPUtil;
import com.threeti.teamlibrary.widgets.CommTitleBar;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.activity.goods.MyBaseFragment;
import com.zjwocai.qundui.activity.login.LoginActivity;
import com.zjwocai.qundui.activity.money.IAEBFormActivity;
import com.zjwocai.qundui.activity.money.RechargeActivity;
import com.zjwocai.qundui.activity.money.WithdrawActivity;
import com.zjwocai.qundui.bill.ProtocolBill;
import com.zjwocai.qundui.model.BalanceModel;
import com.zjwocai.qundui.util.ToastUtil;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MessageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageFragment extends MyBaseFragment implements ProcotolCallBack, RequestCodeSet,View.OnClickListener {

    private RelativeLayout rlRecharge,rlDetail;
    private TextView tvChange,tvMoney;
    protected ImageLoader imageLoader;
    public CommTitleBar title;

    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getLayoutId() {
        View ret = LayoutInflater.from(getActivity()).inflate(R.layout.act_money2, null);
        return R.layout.act_money2;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        initData();
        rlRecharge = (RelativeLayout) root.findViewById(R.id.rl_recharge);
        rlDetail = (RelativeLayout) root.findViewById(R.id.rl_detail);
        tvChange = (TextView) root.findViewById(R.id.tv_change);
        tvMoney = (TextView) root.findViewById(R.id.tv_money);
        initViews();
    }
    public void initData(){
        imageLoader = ImageLoader.getInstance();
        //请求网络
        ProtocolBill.getInstance().getBalance(this,getActivity());
    }

    /**
     * 当界面重新展示时（fragment.show）,调用onrequest刷新界面
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);

        if (!hidden) {
            ProtocolBill.getInstance().getBalance(new ProcotolCallBack() {
                @Override
                public void onTaskSuccess(BaseModel result) {
                    if (RQ_GET_BALANCE.equals(result.getRequest_code())){
                        BalanceModel model = (BalanceModel) result.getResult();
                        if (model != null && !model.equals("")){
                            tvMoney.setText(model.getBalance());
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
                        //ToastUtil.toastShortShow("result.getMsg() + \"\"");
                        ToastUtil.toastShortShow(result.getMsg());
                    }
                }

                @Override
                public void onTaskFinished(String resuestCode) {

                }
            }, getActivity());
        }
    }

    public void initViews() {



        rlRecharge.setOnClickListener(this);
        rlDetail.setOnClickListener(this);
        tvChange.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_recharge:
                startActivity(new Intent(getActivity(),RechargeActivity.class));
                break;
            case R.id.rl_detail:
                startActivity(new Intent(getActivity(),IAEBFormActivity.class));
                break;
            case R.id.tv_change:
                startActivity(new Intent(getActivity(),WithdrawActivity.class));

                break;
        }
    }

    @Override
    public void onTaskSuccess(BaseModel result) {
        if (RQ_GET_BALANCE.equals(result.getRequest_code())){
            BalanceModel model = (BalanceModel) result.getResult();
            if (model != null && !model.equals("")){
                tvMoney.setText(model.getBalance());
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
            ToastUtil.toastShortShow(result.getMsg() + "\"\"");
        }
    }
    public void saveUser(UserModel model) {
        SPUtil.saveObjectToShare(ProjectConstant.KEY_USERINFO, model);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10086){
            ProtocolBill.getInstance().getBalance(this,getActivity());
        }
    }

    @Override
    public void onTaskFinished(String resuestCode) {

    }
}
