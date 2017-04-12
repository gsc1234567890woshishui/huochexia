package com.zjwocai.qundui.activity.mine;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.pingplusplus.android.Pingpp;
import com.threeti.teamlibrary.activity.BaseProtocolActivity;
import com.threeti.teamlibrary.model.GridviewModel;
import com.threeti.teamlibrary.net.BaseModel;
import com.threeti.teamlibrary.net.ProcotolCallBack;
import com.threeti.teamlibrary.utils.SPUtil;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.adapter.GridAdapter;
import com.zjwocai.qundui.adapter.RecordListAdapter;
import com.zjwocai.qundui.bill.ProtocolBill;
import com.zjwocai.qundui.fragment.EtcPayDetailFragment;
import com.zjwocai.qundui.fragment.EventMessage;
import com.zjwocai.qundui.fragment.FirstEvent;
import com.zjwocai.qundui.model.CarModel;
import com.zjwocai.qundui.model.RecoderModel;
import com.zjwocai.qundui.util.CallDialogUtil;
import com.zjwocai.qundui.util.DialogUtil;
import com.zjwocai.qundui.util.RefreshUtil;
import com.zjwocai.qundui.util.ToastUtil;
import com.zjwocai.qundui.widgets.ScrollDisabledListView;
import com.zjwocai.qundui.widgets.SpinerPopWindow;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EtcActivity extends BaseProtocolActivity implements View.OnClickListener ,RadioGroup.OnCheckedChangeListener {
    private GridView gridView;
    private ListView listView;
    private GridAdapter mGridAdapter;
    private LinearLayout llTv,llRecord1,llRecord2,rlRecharge,llEtcask,llNo;
    private RelativeLayout rlQuery;
    private RadioButton rbTag1, rbTag2,etcTag1,etcTag2;
    private RadioGroup rgTags,etcTags;
    private  EtcPayDetailFragment EtcPayDetailFragment1;
    private ImageView ivNo;
    private  View viewLine;
    private int time = 0;
    private Dialog netdialog;

    private  TextView tvPay,tvCarvalue,tvNo,tvEtcask,tvEtc;
    private SpinerPopWindow<String> mSpinerPopWindow;
    private List<String> list;
    List<GridviewModel> li = new ArrayList<>();
    //List<String> lis = new ArrayList<>();
    private List<CarModel> models;
    private List<String> listCarNumber ,listCarNumber0,listState;
    private  String carNumber,etcNumber,carClick,amount,carId,carNumber0,etcNumber0,state;
    private Map<String,String> carMap,carAndId,carAndState;
    private  String ss = "1";
    private  int indexMark;
    private  RecordListAdapter mAdapter;
    private PullToRefreshScrollView ptrsv;
    private ScrollDisabledListView sdlv;
    private int page = 1;
    private List<RecoderModel> listRecoder;



    private String[] itemTexts = new String[] { "500元", "1000元", "2000元", "3000元", "4000元", "5000元" };
    public EtcActivity() {
        super(R.layout.activity_etc);
    }

    @Override
    public void findIds() {
        ProtocolBill.getInstance().getCarList(this,this,"1");

        rgTags = (RadioGroup) findViewById(R.id.rg_login_tab);
        etcTags = (RadioGroup) findViewById(R.id.rg_etc_tab);
        rbTag1 = (RadioButton) findViewById(R.id.rb_tag1);
        rbTag2 = (RadioButton) findViewById(R.id.rb_tag2);
        etcTag1 = (RadioButton) findViewById(R.id.etc_tag1);
        etcTag2 = (RadioButton) findViewById(R.id.etc_tag2);
        gridView = (GridView) findViewById(R.id.gridView);
         //listView = (ListView) findViewById(R.id.ListView01);
        llRecord1 = (LinearLayout) findViewById(R.id.ll_record1);
        llRecord2 = (LinearLayout) findViewById(R.id.ll_record2);
        llEtcask = (LinearLayout) findViewById(R.id.ll_etcask);
        ivNo = (ImageView) findViewById(R.id.iv_no);
        tvNo = (TextView) findViewById(R.id.tv_no);
        tvPay = (TextView) findViewById(R.id.tv_pay);
        tvEtc = (TextView) findViewById(R.id.tv_etc);
        tvEtcask = (TextView) findViewById(R.id.tv_etcask);
        tvCarvalue = (TextView) findViewById(R.id.tv_carvalue);
        rlRecharge = (LinearLayout) findViewById(R.id.ll_recharge);
        rlQuery = (RelativeLayout) findViewById(R.id.rl_query);
        ptrsv = (PullToRefreshScrollView) findViewById(R.id.ptrsv_record);
        sdlv = (ScrollDisabledListView) findViewById(R.id.sdlv_record);
        llNo = (LinearLayout) findViewById(R.id.ll_no);
       // viewLine = findViewById(R.id.view_line);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));

    }

    @Override
    public void initViews() {
        initTitle("ETC服务");

        title.setLeftIcon(R.drawable.ic_title_left, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EtcActivity.this.finish();
            }
        });
        title.setRightText("客服", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceNumber = "0571-87620191";
                new CallDialogUtil().showCallDialog(serviceNumber,EtcActivity.this);
            }
        });
        for (int i = 0; i <itemTexts.length ; i++) {
            GridviewModel gridviewModel = new GridviewModel();
            gridviewModel.setMoney(itemTexts[i]);
            if(i==0){
                gridviewModel.setSelect(true);
            }else{
                gridviewModel.setSelect(false);
            }
            li.add(gridviewModel);
        }

        ptrsv.setMode(PullToRefreshBase.Mode.BOTH);
        RefreshUtil.setPullText(this, ptrsv);
        ptrsv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> pullToRefreshBase) {
                //viewLine.setVisibility(View.VISIBLE);
                page = 1;
                if(etcTag1.isChecked()){ //查询1
                    ProtocolBill.getInstance().getEtcRecord(EtcActivity.this,
                            EtcActivity.this, String.valueOf(page),"2","1",tvCarvalue.getText().toString());
                }else{
                    ProtocolBill.getInstance().getEtcRecord(EtcActivity.this,
                            EtcActivity.this, String.valueOf(page),"2","2",tvCarvalue.getText().toString());
                }

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> pullToRefreshBase) {
                page++;
                if(etcTag1.isChecked()){//查询1
                    ProtocolBill.getInstance().getEtcRecord(EtcActivity.this,
                            EtcActivity.this, String.valueOf(page),"2","1",tvCarvalue.getText().toString());
                }else{
                    ProtocolBill.getInstance().getEtcRecord(EtcActivity.this,
                            EtcActivity.this, String.valueOf(page),"2","2",tvCarvalue.getText().toString());
                }

            }
        });
        netdialog = DialogUtil.getProgressDialog(this, getString(R.string.ui_net_request));
        //netdialog.show();

        listRecoder = new ArrayList<>();
        mAdapter = new RecordListAdapter(EtcActivity.this,listRecoder);

        sdlv.setAdapter(mAdapter);

        tvPay.setOnClickListener(this);
        rgTags.setOnCheckedChangeListener(this);
        etcTags.setOnCheckedChangeListener(this);
        llEtcask.setOnClickListener(this);

        tvEtcask.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvCarvalue.setOnClickListener(clickListener);

        //设置数据匹配器
        mGridAdapter = new GridAdapter(EtcActivity.this,li,gridView);
        gridView.setAdapter(mGridAdapter);
        amount = li.get(0).getMoney();
        // 只是显示是没用用的，这里我们在添加单击item时的监听事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //Toast.makeText(EtcActivity.this, index + "", 2).show();
                //mGridAdapter.setFocus(index);
                for (int i = 0; i < li.size(); i++) {
                 if(position==i){
                     li.get(i).setSelect(true);
                 }else{
                     li.get(i).setSelect(false);
                 }

                }
                amount = li.get(position).getMoney();
                mGridAdapter.notifyDataSetChanged();
                arg1.setSelected(true);
               // ToastUtil.toastShortShow(EtcActivity.this,li.get(position).getMoney() + "");
            }
        });
    }
    /**
     * 监听popupwindow取消
     */
    private PopupWindow.OnDismissListener dismissListener=new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
//            setTextImage(R.drawable.icon_down);
            setTextImage(R.drawable.icon_pull_down);
        }
    };
    /**
     * popupwindow显示的ListView的item点击事件
     */
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,  int position, long id) {
            mSpinerPopWindow.dismiss();

            tvEtc.setText(carMap.get(carNumber));
            String tvCarvalue2 = tvCarvalue.getText().toString();
            state = carAndState.get(listCarNumber.get(position));

            //Toast.makeText(EtcActivity.this, "点击了:" + listCarNumber.get(position),Toast.LENGTH_LONG).show();

            tvCarvalue.setText(listCarNumber.get(position));//车牌号
            if(!carMap.get(listCarNumber.get(position)).equals("")){
                tvEtc.setText(carMap.get(listCarNumber.get(position)));//etc卡号
                ss = "1";
                listCarNumber.set(position,tvCarvalue2);
                mSpinerPopWindow.refresh();
            }else{//提示开通etc服务

                //判断
                if(!state.equals("")){
                    if(state.equals("0") ||  state.equals("1")){//申请中

                        startActivity(new Intent(EtcActivity.this,EtcAskingActivity.class));
                        reSet(listCarNumber0);
                        return;
                    }else if(state.equals("-1")){//跳转到申请失败界面

                        //保存
                        SPUtil.saveString("failcar",tvCarvalue.getText().toString());
                        Intent intent = new Intent(EtcActivity.this, EtcCheckFailActivity.class);

                        startActivity(intent);
                        reSet(listCarNumber0);
                        return;
                    }
                }
                AlertDialog.Builder da = new AlertDialog.Builder(EtcActivity.this);
                da.setTitle("提示：");
                da.setMessage("该车辆尚未开通ETC服务，是否开通？");
                da.setCancelable(false);
                //设置左边按钮监听
                da.setPositiveButton("确定",
                        new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                SPUtil.saveString("failcar","");
                                // 转到开卡界面
                                Intent intent = new Intent(EtcActivity.this,EtcAskActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("one",tvCarvalue.getText().toString());
                                bundle.putString("two", carAndId.get(tvCarvalue.getText().toString()));
                                intent.putExtras(bundle);
                                startActivity(intent);

                                reSet(listCarNumber0);

                                return;
                            }
                        });
                //设置右边按钮监听
                da.setNegativeButton("取消",
                        new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                arg0.dismiss();
                                ss = "0";
                                reSet(listCarNumber0);

                            }
                        });
                da.show();

            }

            //当点击了这一个item从集合里面移除  把框内的文本设置进去
        }
    };


    /**
     * 显示PopupWindow
     */
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_carvalue:
                    mSpinerPopWindow.setWidth(tvCarvalue.getWidth());
                    mSpinerPopWindow.showAsDropDown(tvCarvalue);
                   // setTextImage(R.drawable.icon_up);//icon_pull_down
                    setTextImage(R.drawable.icon_pull_down);
                    break;
            }
        }
    };
    /**
     * 给TextView右边设置图片
     * @param resId
     */
    private void setTextImage(int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());// 必须设置图片大小，否则不显示
        tvCarvalue.setCompoundDrawables(null, null, drawable, null);
    }
    /**
     * 初始化数据
     */
    private void initData() {
        list = new ArrayList<String>();
        for (int i = 0; i < 5; i++) {
            list.add("test:" + i);
        }
    }
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();


        //判断哪个按钮被选中
        if(i == rbTag1.getId()){//充值
            rlRecharge.setVisibility(View.VISIBLE);
            rlQuery.setVisibility(View.GONE);


        }else if(i == rbTag2.getId()){ //查询
            netdialog.show();
            rlQuery.setVisibility(View.VISIBLE);
            rlRecharge.setVisibility(View.GONE);
            if(etcTag1.isChecked()){
                //请求网络查询数据
                getRecoder("1");
            }else if(etcTag2.isChecked()){
                getRecoder("2");
            }

        }else if(i == etcTag1.getId()){ //充值记录
            netdialog.show();


            getRecoder("1");
            if(etcTag1.isChecked()){

            }else{
                getRecoder("1");
            }

        }else if(i == etcTag2.getId()){//消费记录
            netdialog.show();

            getRecoder("2");

            if(etcTag2.isChecked()){

            }else{
                getRecoder("2");
            }

        }
    }


  public  void getRecoder(String type){
      page = 1;
      ProtocolBill.getInstance().getEtcRecord(this,
         this, String.valueOf(page),"5",type,tvCarvalue.getText().toString());
  }
    @Override
    public void onTaskSuccess(BaseModel result) {

        if(RQ_GET_ALL_CAR_LIST.equals(result.getRequest_code())){ //获取车辆列表
            models = (List<CarModel>) result.getResult();
            listCarNumber = new ArrayList<>();
            listCarNumber0 = new ArrayList<>();
            listState = new ArrayList<>();
            List<String> listEtcNumber = new ArrayList<>();
            List<String> lsitCarId = new ArrayList<>();
            carMap = new HashMap<>();
            carAndId = new HashMap<>();
            carAndState = new HashMap<>();
            if(models != null && !models.isEmpty()){  //有车辆
                for (int i = 0; i <models.size() ; i++) {
                    carNumber = models.get(i).getCarnumber();//车牌号
                    listCarNumber.add(carNumber);
                    etcNumber = models.get(i).getEtc_number();//etc卡号
                    listEtcNumber.add(etcNumber);
                    String state = models.get(i).getAudit_state();
                    carAndState.put(carNumber,state);
                    carId = models.get(i).getId();//carId
                    carAndId.put(carNumber,carId);
                    carMap.put(carNumber,etcNumber);
                }
                for (int i = 0; i <listEtcNumber.size() ; i++) { //遍历etc卡号
                    if(!listEtcNumber.get(i).equals("")){ //如果一旦存在etc卡号不是空就设置进去，并结束for循环
                        listCarNumber.remove(i);
                        //tvCarvalue.setText(models.get(i).getCarnumber());
                        carNumber0 = models.get(i).getCarnumber();
                        //tvEtc.setText(models.get(i).getEtc_number());
                        etcNumber0 = models.get(i).getEtc_number();
                        break;
                    }
                }
                listCarNumber0 = listCarNumber;
//                mSpinerPopWindow = new SpinerPopWindow<String>(this, listCarNumber,itemClickListener);
//                mSpinerPopWindow.setOnDismissListener(dismissListener);
                reSet(listCarNumber);

            }
        }else if(RQ_GET_ETCRECODER.equals(result.getRequest_code())){ //查询列表
            //得到数据
            List<RecoderModel> models2 = (List<RecoderModel>) result.getResult();//得到数据
            if(page == 1){
                listRecoder.clear();
                if(models2 != null && !models2.isEmpty()){//有数据
                    llNo.setVisibility(View.GONE);
                    listRecoder.addAll(models2);
                    if(etcTag1.isChecked() && rbTag2.isChecked()){
                        llRecord1.setVisibility(View.VISIBLE);
                        llRecord2.setVisibility(View.GONE);
                    }else if(etcTag2.isChecked() && rbTag2.isChecked()){
                        llRecord1.setVisibility(View.GONE);
                        llRecord2.setVisibility(View.VISIBLE);
                    }
                }else{
                    //显示图片
                    if(etcTag1.isChecked() && rbTag2.isChecked()){//充值无记录图片展示
                        llRecord1.setVisibility(View.GONE);
                        llRecord2.setVisibility(View.GONE);
                        llNo.setVisibility(View.VISIBLE);
                        ivNo.setImageResource(R.drawable.bg_blank_page);
                        tvNo.setText("暂无充值记录");

                        llRecord1.setVisibility(View.GONE);
                        llRecord2.setVisibility(View.GONE);

                    }else if(etcTag2.isChecked() && rbTag2.isChecked()){
                        llRecord1.setVisibility(View.GONE);
                        llRecord2.setVisibility(View.GONE);
                        llNo.setVisibility(View.VISIBLE);
                        ivNo.setImageResource(R.drawable.bg_blank_page);
                        tvNo.setText("暂无消费记录");
                        llRecord1.setVisibility(View.GONE);
                        llRecord2.setVisibility(View.GONE);
                    }

                }
            }else{

                if(models2 != null && !models2.isEmpty()){
                    listRecoder.addAll(models2);
                }else{
                    ToastUtil.toastShortShow("没有更多记录");
                }

            }
            mAdapter.notifyDataSetChanged();


        }

    }

    @Override
    public void onTaskFail(@SuppressWarnings("rawtypes") BaseModel result) {
//        super.onTaskFail(result);
        if (result.getMsgtype() != null && result.getMsgtype().equals("2")) {
            showToast("登录失效，请重新登录");
            saveUser(null);
            startActivity(EtcActivity.class);
        } else if (!TextUtils.isEmpty(result.getMsg())) {
            showToast(result.getMsg() + "");
        }
    }

    @Override
    public void onTaskFinished(String resuestCode) {
        super.onTaskFinished(resuestCode);
        if (ptrsv.isRefreshing()) {
            ptrsv.onRefreshComplete();
        }
        netdialog.dismiss();
        if (netdialog.isShowing()) {
            time++;
            if (time != 1) {
                netdialog.dismiss();
            }
        }
    }
    public  void reSet(List list){
        tvCarvalue.setText(carNumber0);
        tvEtc.setText(etcNumber0);
        mSpinerPopWindow = new SpinerPopWindow<String>(this, list,itemClickListener);
        mSpinerPopWindow.setOnDismissListener(dismissListener);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_pay:
                if(tvEtc.getText().equals("")){ //如果是空
                    AlertDialog.Builder da = new AlertDialog.Builder(EtcActivity.this);
                    da.setTitle("提示：");
                    da.setMessage("该车辆尚未开通ETC服务，是否开通？");
                    da.setCancelable(false);
                    //设置左边按钮监听
                    da.setPositiveButton("确定",
                            new android.content.DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    // 转到开卡界面
                                    Intent intent = new Intent();
                                    intent.putExtra("one",tvCarvalue.getText());
                                    intent.setClass(EtcActivity.this, EtcAskActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                            });
                    //设置右边按钮监听
                    da.setNegativeButton("取消",
                            new android.content.DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    arg0.dismiss();


                                }
                            });
                    da.show();
                }else{
                    //支付
                    EtcPayDetailFragment1=new EtcPayDetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("carClick",amount);//金额
                    bundle.putString("carNumber",tvCarvalue.getText().toString());//车牌
                    bundle.putString("etcNumber",tvEtc.getText().toString());//etc卡号
                    bundle.putString("carId",carAndId.get(tvCarvalue.getText().toString()));//carID
                    EtcPayDetailFragment1.setArguments(bundle);
                    EtcPayDetailFragment1.show(getSupportFragmentManager(),"etcPayDetailFragment");
                }
                break;
            case R.id.ll_etcask:
                //充值流程
                startActivity(new Intent(EtcActivity.this,EtcProcessActivity.class));
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
                    goSelect(EventMessage.EventMessageAction.TAG_REMOVE_DIALOG);
                    startActivity(new Intent(EtcActivity.this, EtcPaySuccessActivity.class));

                } else if (result.equals("fail")){
                    ToastUtil.toastShortShow("支付失败！");
                    //向dialogfragment发消息通知可以点击
                    goSelect(EventMessage.EventMessageAction.TAG_RECHARGE_DIALOG);
                } else if (result.equals("cancel")){
                    ToastUtil.toastShortShow("支付取消！");
                    goSelect(EventMessage.EventMessageAction.TAG_RECHARGE_DIALOG);
                    //payDetailFragment2.getShowsDialog();
                } else if (result.equals("invalid")){
                    ToastUtil.toastShortShow("微信未安装！");
                } else {
                    ToastUtil.toastShortShow("支付服务暂时无法使用!");
                }
            }
        }
    }
    private void goSelect(int tag) {
        EventMessage eventMessage = new EventMessage();
        eventMessage.setTag(tag);
        EventBus.getDefault().post(eventMessage);

    }
}

