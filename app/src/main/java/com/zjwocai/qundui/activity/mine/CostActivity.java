package com.zjwocai.qundui.activity.mine;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;

import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.pingplusplus.android.Pingpp;
import com.threeti.teamlibrary.activity.BaseProtocolActivity;

import com.threeti.teamlibrary.model.UserModel;
import com.threeti.teamlibrary.net.BaseModel;
import com.threeti.teamlibrary.net.ProcotolCallBack;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.activity.login.LoginActivity;
import com.zjwocai.qundui.adapter.DemoAdapter;
import com.zjwocai.qundui.bill.ProtocolBill;
import com.zjwocai.qundui.fragment.EventMessage;
import com.zjwocai.qundui.fragment.PayDetailFragment;
import com.zjwocai.qundui.model.CoastModel2;
import com.zjwocai.qundui.model.FlowModel;
import com.zjwocai.qundui.model.ItemModel2;
import com.zjwocai.qundui.util.DialogUtil;
import com.zjwocai.qundui.util.StringUtil;
import com.zjwocai.qundui.util.ToastUtil;
import com.zjwocai.qundui.widgets.FullyGridLayoutManager;
import com.zjwocai.qundui.widgets.PhoneTextWatcher;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CostActivity extends BaseProtocolActivity implements RadioGroup.OnCheckedChangeListener,View.OnClickListener {

    //    private ContainsEmojiEditText edPhoneNumber;
    private EditText edPhoneNumber;
    private RadioGroup rgTags;
    private DemoAdapter adapter;
    private RadioButton rbTag1, rbTag2;
    private ImageView ivContact, ivCancle;
    private boolean isDelete = false;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private String flag = "0";
    private LinearLayout ll1, ll2;
    private TextView tvPay, tvBuy1, tvBuy2, phone, ivService, tvInputNumber, tvLocal, tvCountry;
    private UserModel user;
    private Boolean flag2 = true;
    private String mobile, mobile2, faceprice, saleprice, carrier, thirdprice;
    private ArrayList<ItemModel2> dataList = new ArrayList<>();
    private RelativeLayout rlNative, rlCountry;
    private ItemModel2 item2;
    private List<CoastModel2.ItemListBean> lis11;
    private List<FlowModel.ItemListBean> lis22;



    private Dialog callDialog;
    private String dataType, czType;
    private String datacount, llfaceprice, qgprice, snprice, llthirdprice;
    private CoastModel2 coast11;
    private FlowModel flow11;
    private PayDetailFragment payDetailFragment1,payDetailFragment2,payDetailFragment3;
private  RelativeLayout rlLeft;
    String username, usernumber;
    private Uri uri_DATA = Uri.parse("content://com.android.contacts/data");

    public CostActivity() {
        super(R.layout.activity_cost);
    }

    @Override
    public void findIds() {
        tvInputNumber = (TextView) findViewById(R.id.tv_inputnumber);
        //edPhoneNumber = (ContainsEmojiEditText) findViewById(R.id.et_phonenumber);
        ivContact = (ImageView) findViewById(R.id.iv_contact);
        recyclerView = (RecyclerView) findViewById(R.id.recylerview);
        rgTags = (RadioGroup) findViewById(R.id.rg_login_tab);
        rbTag1 = (RadioButton) findViewById(R.id.rb_tag1);
        rbTag2 = (RadioButton) findViewById(R.id.rb_tag2);
        ll1 = (LinearLayout) findViewById(R.id.ll_1);
        ll2 = (LinearLayout) findViewById(R.id.ll_2);
        tvPay = (TextView) findViewById(R.id.tvPay);
        tvBuy1 = (TextView) findViewById(R.id.tv_buy1);
        tvBuy2 = (TextView) findViewById(R.id.tv_buy2);
        phone = (TextView) findViewById(R.id.tv_account);
        ivCancle = (ImageView) findViewById(R.id.iv_cancle);
        ivService = (TextView) findViewById(R.id.iv_service);
        tvLocal = (TextView) findViewById(R.id.tv_local);
        tvCountry = (TextView) findViewById(R.id.tv_country);
        rlNative = (RelativeLayout) findViewById(R.id.rl_native);
        rlCountry = (RelativeLayout) findViewById(R.id.rl_country);
        edPhoneNumber = (EditText) findViewById(R.id.et_phonenumber);
        rlLeft = (RelativeLayout) findViewById(R.id.rl_left);
        //EditText edit=(EditText)findViewById(R.id.edit);
//
        //edPhoneNumber.addTextChangedListener(new PhoneTextWatcher(edPhoneNumber));


//        edPhoneNumber.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_DEL) {
//                    isDelete = true;
//                }
//                return false;
//            }
//        });
    }


//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void setGoIndex(EventMessage eventMessage) {
//        //Log.d(TAG, "setGoIndex: "+eventMessage.getTag());
//        if (eventMessage != null) {
//            int tag = eventMessage.getTag();
//            if (tag == EventMessage.EventMessageAction.TAG_MESS_CODE) {
//                //弹窗验证码
//                //pop = new CostActivity.PopupWindows(CostActivity.this,phone);
//            }
//        }
//    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        //判断哪个按钮被选中
        if (i == rbTag1.getId()) {  //话费充值
            if (flag.equals("1")) {
                flag = "0";
                //adapter.replaceAll(getData(flag),flag2);
                getData(flag);
            }
            //rgBgs.check(rbBg1.getId());
            ll1.setVisibility(View.VISIBLE);
            ll2.setVisibility(View.GONE);
        } else if (i == rbTag2.getId()) { //流量充值
            //rgBgs.check(rbBg2.getId());
            if (flag.equals("0")) {
                flag = "1";
                //adapter.replaceAll(getData(flag),flag2);
                getData(flag);
            }
            ll2.setVisibility(View.VISIBLE);
            ll1.setVisibility(View.GONE);
        }
    }

    public void getData( String flag) {

        final TimerTask task = new TimerTask(){
            public void run(){
                DialogUtil.hideWaitDialog();

            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 3000);

               if(flag.equals("0")){
                   if((edPhoneNumber.getText().toString().length()<13 || edPhoneNumber.getText().toString().replace(" ", "").substring(0,3).equals("170") ) && dataList.size()>0 && coast11!=null ){
                       //就拿上一次请求到的数据刷新列表
                       lis11 = coast11.getItemList();
                       ArrayList<ItemModel2> list = new ArrayList<>();
                       for (int i = 0; i < lis11.size(); i++) {
                           DialogUtil.hideWaitDialog();
                           String faceprice = lis11.get(i).getFacePrice();//面值
                           String saleprice = lis11.get(i).getSalePrice();//售价salePrice
                           String thirdprice = lis11.get(i).getThirdPrice();//三方售价
                           list.add(new ItemModel2(ItemModel2.ONE, "0",faceprice, saleprice, thirdprice, "", ""));
                       }
                       //设置数据
                       adapter.replaceAll(list, flag2);
                       tvPay.setBackgroundResource(R.drawable.recharge_grey);

                   }
        }else{
                       if(flow11==null){//如果170号码之前还没有请求过流量数据就置空订单
                           ArrayList<ItemModel2> list = new ArrayList<>();
                           adapter.replaceAll(list, false);
                           rlNative.setVisibility(View.GONE);
                           rlCountry.setVisibility(View.GONE);
                       }else{
                           if((edPhoneNumber.getText().toString().length()<13 || edPhoneNumber.getText().toString().replace(" ", "").substring(0,3).equals("170"))&& dataList.size()>0  && flow11!=null){
                               //carrier = flow11.getCarrier();
                               //tvInputNumber.setText(carrier);
                               lis22 = flow11.getItemList();
                               ArrayList<ItemModel2> list = new ArrayList<>();
                               for (int i = 0; i < lis22.size(); i++) {
                                   DialogUtil.hideWaitDialog();
                                   String datacount = lis22.get(i).getData();//流量面值
                                   String qgPrice = lis22.get(i).getQgPrice();//全国售价salePrice
                                   String llthirdprice = lis22.get(i).getThirdPrice();//三方售价
                                   String snPrice = lis22.get(i).getSnPrice();//省内售价
                                   String llfacePrice = lis22.get(i).getFacePrice();//流量面值
                                   if (Integer.parseInt(datacount) > 1000) {//如果data单位是g

                                       list.add(new ItemModel2(ItemModel2.ONE,"2" ,datacount, "售价" + snPrice + "元", "售价" + qgPrice + "元", llfacePrice, llthirdprice));
                                   } else {//如果单位是M
                                       list.add(new ItemModel2(ItemModel2.ONE, "1",datacount, "售价" + snPrice + "元", "售价" + qgPrice + "元", llfacePrice, llthirdprice));
                                   }
                               }
                               adapter.replaceAll(list, flag2);
                               if(dataList.size()>0){
                                   item2 =  dataList.get(0);
                                   faceprice = (String) item2.data2;//面值
                                   saleprice = (String) item2.data3;//实际价格
                                   thirdprice = (String) item2.data4;//第三方价格
                               }

                           }

                       }

               }
        if(edPhoneNumber.getText().toString().replace(" ", "").length()==11  && !edPhoneNumber.getText().toString().replace(" ", "").substring(0,3).equals("170") ){
            //如果是11位数号码,并且不是170号段才请求网络
            if (flag.equals("0") ) { //话费充值
                //请求话费接口请求网络获取数据
                ProtocolBill.getInstance().rechargeCoast(new ProcotolCallBack() {
                    @Override
                    public void onTaskSuccess(BaseModel result) {
                        coast11 = (CoastModel2) result.getResult();
                        carrier = coast11.getCarrier();
                        //tvInputNumber.setTextColor(getResources().getColor(R.color.blacktext));
                        tvInputNumber.setTextColor(ContextCompat.getColor(CostActivity.this, R.color.blacktext));

                        if(mobile2.equals(edPhoneNumber.getText().toString())){//如果是绑定的手机号
                            tvInputNumber.setText("("+"绑定手机号"+")"+carrier);
                        }else{
                            tvInputNumber.setText(carrier);
                        }
                        lis11 = coast11.getItemList();
                        if(lis11.size()<0){//如果没有请求到返回套餐
                            tvPay.setBackgroundResource(R.drawable.recharge_grey);
                            tvPay.setEnabled(false);

                        }
                        ArrayList<ItemModel2> list = new ArrayList<>();
                        for (int i = 0; i < lis11.size(); i++) {
                            DialogUtil.hideWaitDialog();
                            String faceprice1 = lis11.get(i).getFacePrice();//面值
                            String saleprice1 = lis11.get(i).getSalePrice();//售价salePrice
                            String thirdprice1 = lis11.get(i).getThirdPrice();//三方售价
                            //充值话费
                            list.add(new ItemModel2(ItemModel2.ONE, "0",faceprice1, saleprice1 , thirdprice1, "", ""));
                        }
                        //设置数据
                        adapter.replaceAll(list, flag2);
                        //让话费选中
                        if(dataList.size()>0){
                            item2 =  dataList.get(0);
                            faceprice = (String) item2.data2;//面值
                            saleprice = (String) item2.data3;//实际价格
                            thirdprice = (String) item2.data4;//第三方价格
                            tvPay.setVisibility(View.VISIBLE);
                        }else{
                            tvPay.setBackgroundResource(R.drawable.recharge_grey);
                            tvPay.setEnabled(false);
                        }
                    }

                    @Override
                    public void onTaskFail(BaseModel result) {

                        if (result.getMsgtype() != null && result.getMsgtype().equals("2")) {

                            ToastUtil.toastShortShow("登录失效，请重新登录");
                            saveUser(null);
                            Intent intent = new Intent(CostActivity.this,LoginActivity.class);
                            startActivity(intent);
                        } else if (!TextUtils.isEmpty(result.getMsg())) {
                           // ToastUtil.toastShortShow(result.getMsg() + "");

                        }

                        if(result.getMsg().equals("请输入正确的手机号码")){
                            //tvInputNumber.setTextColor(getResources().getColor(R.color.blacktext));
                            tvInputNumber.setTextColor(ContextCompat.getColor(CostActivity.this, R.color.blacktext));

                            tvInputNumber.setText("请输入正确的手机号码");
                            flag2 = false;
                            getData("0");
                            //把充值按钮置灰色
                            tvPay.setBackgroundResource(R.drawable.recharge_grey);
                            tvPay.setEnabled(false);

                        }else{

                                tvInputNumber.setText("没有搜索到对应的话费商品");
                                flag2 = false;
                                getData("0");
                                //把充值按钮置灰色
                                tvPay.setBackgroundResource(R.drawable.recharge_grey);
                                tvPay.setEnabled(false);

                        }
                    }
                    @Override
                    public void onTaskFinished(String resuestCode) {


                    }
                }, this, edPhoneNumber.getText().toString().replace(" ", ""));

            } else {//流量充值
                ProtocolBill.getInstance().rechargeFlow(new ProcotolCallBack() {
                    @Override
                    public void onTaskSuccess(BaseModel result) {

                        flow11 = (FlowModel) result.getResult();
                        carrier = flow11.getCarrier();
                        //tvInputNumber.setTextColor(getResources().getColor(R.color.blacktext));
                        tvInputNumber.setTextColor(ContextCompat.getColor(CostActivity.this, R.color.blacktext));

                        if(mobile2.equals(edPhoneNumber.getText().toString())){//如果是绑定的手机号
                            tvInputNumber.setText("("+"绑定手机号"+")"+carrier);
                        }else{
                            tvInputNumber.setText(carrier);
                        }

                        List<FlowModel.ItemListBean> lis = flow11.getItemList();
                        ArrayList<ItemModel2> list = new ArrayList<>();
                        for (int i = 0; i < lis.size(); i++) {
                            DialogUtil.hideWaitDialog();
                            String datacount = lis.get(i).getData();//流量面值
                            String qgPrice = lis.get(i).getQgPrice();//全国售价salePrice
                            String llthirdprice = lis.get(i).getThirdPrice();//三方售价
                            String snPrice = lis.get(i).getSnPrice();//省内售价
                            String llfacePrice = lis.get(i).getFacePrice();//流量面值
                            //充值流量
                            if (Integer.parseInt(datacount) > 1023) {//如果data大于1000M
                                list.add(new ItemModel2(ItemModel2.ONE, "2",datacount, "售价" + snPrice + "元", "售价" + qgPrice + "元", llfacePrice, llthirdprice));
                            } else {
                                list.add(new ItemModel2(ItemModel2.ONE, "1",datacount, "售价" + snPrice + "元", "售价" + qgPrice + "元", llfacePrice, llthirdprice));
                            }
                        }
                        adapter.replaceAll(list, flag2);
                        //把第一个信息默认选中
                        if(dataList.size()>0){
                            item2 =  dataList.get(0);
                            datacount = (String) item2.data2;//流量数
                            System.out.println("ssssssssssssseeeeeeeeeeeeeeeeeeeeee"+datacount);
                            qgprice = (String) item2.data4;//流量全国价格
                            System.out.println("sssssssssssssqqqqqqqqqqqqqqqqqqqqqqq"+qgprice);
                            snprice = (String) item2.data3;//省内流量价格
                            System.out.println("ssssssssssssswwwwwwwwwwwwwwwwwwwwwww"+snprice);
                            llfaceprice = (String) item2.data5;//流量面额
                            System.out.println("sssssssssssssttttttttttttttttttttttttt"+faceprice);
                            llthirdprice = (String) item2.data6;//第三方价格
                            System.out.println("sssssssssssssrrrrrrrrrrrrrrrrrrrrrrrr"+thirdprice);

                            if(!snprice.equals("售价"+""+"元")){//显示本地流量布局
                                rlNative.setVisibility(View.VISIBLE);
                            }
                            if(!qgprice.equals("售价"+""+"元")){//显示全国布局
                                rlCountry.setVisibility(View.VISIBLE);
                            }
                            if(snprice.equals("售价"+""+"元")){//隐藏
                                rlNative.setVisibility(View.GONE);
                            }
                            if(qgprice.equals("售价"+""+"元")){//隐藏全国布局
                                rlCountry.setVisibility(View.GONE);
                            }

                            tvLocal.setText(snprice);//底部设置流量本地价格
                            tvCountry.setText(qgprice);//底部显示流量全国价格
                        }else{
                            rlCountry.setVisibility(View.GONE);
                            rlNative.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onTaskFail(BaseModel result) {
                        if(result.getMsg().equals("请输入正确的手机号码")){
                           // tvInputNumber.setTextColor(getResources().getColor(R.color.blacktext));
                            tvInputNumber.setTextColor(ContextCompat.getColor(CostActivity.this, R.color.blacktext));

                            tvInputNumber.setText("请输入正确的手机号码");
                        }else{
                           // tvInputNumber.setTextColor(getResources().getColor(R.color.blacktext));
                            tvInputNumber.setTextColor(ContextCompat.getColor(CostActivity.this, R.color.blacktext));

                            tvInputNumber.setText("没有搜索到对应的流量商品");

                        }
                        flag2 = false;
                        getData("0");
                        //把充值按钮置灰色
                        tvPay.setBackgroundResource(R.drawable.recharge_grey);
                        tvPay.setEnabled(false);
                        tvBuy1.setEnabled(false);
                        tvBuy2.setEnabled(false);
                    }

                    @Override
                    public void onTaskFinished(String resuestCode) {

                    }
                }, this, edPhoneNumber.getText().toString().replace(" ", ""));

            }

        }

    }

    @Override
    public void initViews(){
       // EventBus.getDefault().register(this);
        user = ProtocolBill.getInstance().getNowUser();
         mobile = user.getMobile();
        ivContact.setOnClickListener(this);
        rgTags.setOnCheckedChangeListener(this);
        tvPay.setOnClickListener(this);
        tvBuy1.setOnClickListener(this);
        tvBuy2.setOnClickListener(this);
        //ivCancle.setOnClickListener(this);
        ivService.setOnClickListener(this);
        recyclerView.setHasFixedSize(true);
        rlLeft.setOnClickListener(this);

        recyclerView.setLayoutManager(new FullyGridLayoutManager(this, 3));

        adapter = new DemoAdapter(dataList);
        recyclerView.setAdapter(adapter);
        System.out.println("sssssssssssssssssssssssssssss222222");
        DialogUtil.showWaitDialog(this, "加载中...");

        //把获取到的本季号码设置给输入框
        if(mobile!=null && !mobile.isEmpty()){//如果不为空就设置
            mobile2 = mobile.substring(0,3)+" "+mobile.substring(3,7)+" "+mobile.substring(7);
            edPhoneNumber.setText(mobile2);
            edPhoneNumber.setSelection(mobile2.length());
            edPhoneNumber.setCursorVisible(false);

        }
        getData(flag);
        edPhoneNumber.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        if(!edPhoneNumber.isCursorVisible()){//如果光标是不可见的
                            edPhoneNumber.setText("");
                            edPhoneNumber.setCursorVisible(true);// 再次点击显示光标
                            flag2 = false;
                            getData(flag);
                        }
                    }else{
                        edPhoneNumber.setText("");
                        edPhoneNumber.setCursorVisible(true);// 再次点击显示光标
                        flag2 = false;
                        getData(flag);
                    }
                    //tvPay.setBackgroundResource(R.drawable.recharge_grey);

                }
                return false;
            }
        });
        edPhoneNumber.addTextChangedListener(new PhoneTextWatcher(edPhoneNumber) {
            //private EditText _text;
            int beforeLen = 0;
            int afterLen = 0;
            private CharSequence temp;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeLen = s.length();
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String txt = edPhoneNumber.getText().toString();
                afterLen = txt.length();
                temp = s;
                if (s == null || s.length() == 0)
                    return;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < s.length(); i++) {
                    if (i != 3 && i != 8 && s.charAt(i) == ' ') {
                        continue;
                    } else {
                        sb.append(s.charAt(i));
                        if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
                            sb.insert(sb.length() - 1, ' ');
                        }
                    }
                }
                if (!sb.toString().equals(s.toString())) {
                    int index = start + 1;
                    if (sb.charAt(start) == ' ') {
                        if (before == 0) {
                            index++;
                        } else {
                            index--;
                        }
                    } else {
                        if (before == 1) {
                            index--;
                        }
                    }
                    edPhoneNumber.setText(sb.toString());
                    edPhoneNumber.setSelection(index);
                }

                if (afterLen < beforeLen) {
                    if (txt.endsWith(" ")) {
                        edPhoneNumber.setText(new StringBuffer(txt).deleteCharAt(txt.lastIndexOf(" ")).toString());
                        edPhoneNumber.setSelection(edPhoneNumber.getText().length());
                    }
                }

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(temp.length() == 3){
                    if(edPhoneNumber.getText().toString().equals("170")){
                        //tvInputNumber.setTextColor(getResources().getColor(R.color.red));
                        tvInputNumber.setTextColor(ContextCompat.getColor(CostActivity.this, R.color.red));

                        tvInputNumber.setText("不支持虚拟运营商170号段");
                    }
                }
                if(edPhoneNumber.getText().toString().equals("")){
                    tvInputNumber.setText("");
                }
                if (temp.length() == 12) {//监听号码减少后调用置灰套餐按钮

                    flag2 = false;
                    getData(flag);
                    //把充值按钮置灰色
                    tvPay.setBackgroundResource(R.drawable.recharge_grey);

                }
                if(temp.length() <= 12){
                    tvPay.setEnabled(false);
                    tvBuy1.setEnabled(false);
                    tvBuy2.setEnabled(false);

                }

                if(temp.length() == 13){//就调用接口，请求订单
                    if(edPhoneNumber.getText().toString().replace(" ", "").substring(0,3).equals("170")){//如果是170号码就不做处理

                    }else{
                        DialogUtil.showWaitDialog(CostActivity.this, "加载中...");
                        flag2 = true;
                        getData(flag);
                        tvPay.setBackgroundResource(R.drawable.shape_coast);
                        tvPay.setEnabled(true);
                        tvBuy1.setEnabled(true);
                        tvBuy2.setEnabled(true);
                    }
                }
            }

        });

       initEvent();//点击的条目
    }
    private void initEvent() {
        adapter.setOnClickListener(new DemoAdapter.OnClickListener() {
            @Override
            public void setOnClick(View view, int position) {
                if(flag.equals("0")){//如果是充值话费
                    item2 =  dataList.get(position);
                    faceprice = (String) item2.data2;//面值
                    saleprice = (String) item2.data3;//实际价格
                    thirdprice = (String) item2.data4;//第三方价格
                }else{//如果是充值流量
                    //{"code":"OK","msgtype":"0","msg":"查询成功","result":{"province":"浙江","catName":"中国移动","carrier":"浙江移动",
                            //"itemList":[{"data":"100","facePrice":"10","snPrice":"","qgPrice":"15.1","thirdPrice":"15.00"}]}}
                    item2 =  dataList.get(position);
                    datacount = (String) item2.data2;//流量数
                    System.out.println("ssssssssssssseeeeeeeeeeeeeeeeeeeeee"+datacount);
                    qgprice = (String) item2.data4;//流量全国价格
                    System.out.println("sssssssssssssqqqqqqqqqqqqqqqqqqqqqqq"+qgprice);
                    snprice = (String) item2.data3;//省内流量价格
                    System.out.println("ssssssssssssswwwwwwwwwwwwwwwwwwwwwww"+snprice);
                    llfaceprice = (String) item2.data5;//流量面额
                    System.out.println("sssssssssssssttttttttttttttttttttttttt"+faceprice);
                    llthirdprice = (String) item2.data6;//第三方价格
                    System.out.println("sssssssssssssrrrrrrrrrrrrrrrrrrrrrrrr"+thirdprice);

//                    if(snprice.equals("售价"+""+"元")){//隐藏本地流量布局
//                        rlNative.setVisibility(View.GONE);
//                    }else{
//                        rlCountry.setVisibility(View.VISIBLE);
//                    }
//                    System.out.println("lllllllllllllllllllllllllllll"+qgprice);
//                    if(qgprice.equals("售价"+""+"元")){//隐藏全国流量布局
//                        rlCountry.setVisibility(View.GONE);
//                    }else{
//                        rlNative.setVisibility(View.VISIBLE);
//                    }
                    if(!snprice.equals("售价"+""+"元")){//显示本地流量布局
                        rlNative.setVisibility(View.VISIBLE);
                    }
                    if(!qgprice.equals("售价"+""+"元")){//显示全国布局
                        rlCountry.setVisibility(View.VISIBLE);
                    }
                    if(snprice.equals("售价"+""+"元")){//隐藏
                        rlNative.setVisibility(View.GONE);
                    }
                    if(qgprice.equals("售价"+""+"元")){//隐藏全国布局
                        rlCountry.setVisibility(View.GONE);
                    }

                    tvLocal.setText(snprice);//底部设置流量本地价格
                    tvCountry.setText(qgprice);//底部显示流量全国价格
                }
            }
        });
    }
    /**
     * 拨打电话
     *
     * @param number 电话号码
     */
    private void showCallDialog(final String number) {
        LayoutInflater factory = LayoutInflater.from(CostActivity.this);
        View view = factory.inflate(R.layout.dialog_enter_call2, null);
        final TextView des = (TextView) view.findViewById(R.id.tv_des);
        final TextView enter = (TextView) view.findViewById(R.id.tv_enter);
        final TextView cancel = (TextView) view.findViewById(R.id.tv_cancel);
        final TextView content = (TextView) view.findViewById(R.id.tv_content);
        //des.setText("欢迎致电：");
        content.setText(number);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtil.isNO(number.replace("-","").trim())) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    Uri data = Uri.parse("tel:" + number);
                    intent.setData(data);
                    startActivity(intent);
                } else {
                    //showToast("号码无效！");
                    ToastUtil.toastShortShow("号码无效!");
                }
                callDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDialog.dismiss();
            }
        });
        callDialog = DialogUtil.getDialog(CostActivity.this, view, Gravity.CENTER, true);
        callDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_left:
                finish();
                break;
            case R.id.iv_service:
                //弹出客服电话
                String serviceNumber = "0571-87620191";

                    showCallDialog(serviceNumber);

                break;
            case R.id.iv_contact:
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 1);
                break;
            case R.id.tvPay:

                //传递
                 payDetailFragment1=new PayDetailFragment();
                //创建一个Bundle用来存储数据，传递到Fragment中*/
                Bundle bundle = new Bundle();
                bundle.putString("czType","1");//充值类型是话费
                bundle.putString("phonenumber",edPhoneNumber.getText().toString().replace(" ",""));//需要充值的手机号
                bundle.putString("carrier",carrier);//手机运营商
                bundle.putString("faceprice", faceprice);//话费面额
                bundle.putString("salePrice", saleprice);//话费售价
                bundle.putString("thirdPrice", thirdprice);//话费第三方价格
                //把数据设置到Fragment中
                if(faceprice!=null){
                    payDetailFragment1.setArguments(bundle);
                    payDetailFragment1.show(getSupportFragmentManager(),"payDetailFragment");
                }else{
                    ToastUtil.toastShortShow("请选择充值套餐");
                }
                break;
            case R.id.tv_buy1://购买本地流量
                dataType = "1";//流量类型赋值为1
                 payDetailFragment2=new PayDetailFragment();
                Bundle bundle2 = new Bundle();
                //往bundle中添加数据
                bundle2.putString("czType","2");//充值类型是流量
                bundle2.putString("phonenumber",edPhoneNumber.getText().toString().replace(" ",""));//需要充值的手机号
                bundle2.putString("carrier",carrier);//手机运营商
                bundle2.putString("dataCount", datacount);//流量数
                bundle2.putString("faceprice", llfaceprice);//流量面额
                bundle2.putString("thirdPrice", llthirdprice);//流量三方价格
                bundle2.putString("dataType","1");//充值类型是本地
                bundle2.putString("snsalePrice",snprice);//本地流量的价格
                System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbb"+snprice);

                //把数据设置到Fragment中
                if(datacount!=null){
                    payDetailFragment2.setArguments(bundle2);
                    payDetailFragment2.show(getSupportFragmentManager(),"payDetailFragment");
                }else{
                    ToastUtil.toastShortShow("请选择充值套餐");
                }
                break;

            case R.id.tv_buy2://购买全国流量
                 payDetailFragment3=new PayDetailFragment();
                Bundle bundle3 = new Bundle();
                bundle3.putString("czType","2");//充值类型是流量
                bundle3.putString("phonenumber",edPhoneNumber.getText().toString().replace(" ",""));//需要充值的手机号
                bundle3.putString("carrier",carrier);//手机运营商
                bundle3.putString("dataCount", datacount);//流量数
                bundle3.putString("faceprice", llfaceprice);//流量面额
                bundle3.putString("thirdPrice", llthirdprice);//流量三方价格
                bundle3.putString("dataType","0");//充值类型是全国
                bundle3.putString("qgsalePrice",qgprice);//全国流量的价格

                //把数据设置到Fragment中
                if(datacount!=null){
                    payDetailFragment3.setArguments(bundle3);
                    payDetailFragment3.show(getSupportFragmentManager(),"payDetailFragment");
                }else{
                    ToastUtil.toastShortShow("请选择充值套餐");
                }
                break;

        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

           System.out.println("sssssssssssssssssssssssssssss33333333");
        if(requestCode == 1) { //请求码为1
            Log.i("info","resultADD");
            String strNumber = "";
            if(data != null) { //判断返回的intent是不是为空

                Uri uri = data.getData();
                Log.i("info", uri.toString()); //在log打印出来获取的uri
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                while(cursor.moveToNext()) {
                    String strID = cursor.getString(cursor.getColumnIndex("name_raw_contact_id"));
                    Cursor cursor2 = getContentResolver().query(uri_DATA, null,
                            "raw_contact_id = " + strID + " and mimetype_id = 5", null, null);
                    if(cursor2.moveToFirst()) {
                        strNumber = cursor2.getString(cursor2.getColumnIndex("data1"));
                    }
                    cursor2.close();
                }
                cursor.close();
                //132  5387  1829
                String regEx = "[^0-9]";//匹配指定范围内的数字

                //Pattern是一个正则表达式经编译后的表现模式
                Pattern p = Pattern.compile(regEx);

                // 一个Matcher对象是一个状态机器，它依据Pattern对象做为匹配模式对字符串展开匹配检查。
                Matcher m = p.matcher(strNumber);

                //将输入的字符串中非数字部分全部去掉
                String nu = m.replaceAll("").trim();

                String num = nu.replace(" ","");

                if(num.length()==0){//如果获取到的手机号是空
                    edPhoneNumber.setText("");
                    tvInputNumber.setText("请输入正确的手机号码");
                    flag2 = false;
                    getData("0");
                }

                if(num.length()==11){//如果是11位就设置进去
                    if(num.substring(0,3).equals("170")){
                        edPhoneNumber.setText(num);
                        edPhoneNumber.setSelection(13);
                        //tvInputNumber.setTextColor(getResources().getColor(R.color.red));
                        tvInputNumber.setTextColor(ContextCompat.getColor(CostActivity.this, R.color.red));

                        tvInputNumber.setText("不支持虚拟运营商170号段");
                        flag2 = false;
                        if(rbTag1.isChecked()){//如果选中的是话费
                            getData("0");
                        }else if(rbTag2.isChecked()){
                            getData("1");
                        }
                        //把充值按钮置灰色
                        tvPay.setBackgroundResource(R.drawable.recharge_grey);
                        tvPay.setEnabled(false);
                        tvBuy1.setEnabled(false);
                        tvBuy2.setEnabled(false);
                    }else{
                        edPhoneNumber.setText(num);
                        edPhoneNumber.setSelection(13);
                        if(rbTag1.isChecked()){//如果选中的是话费
                            getData("0");
                        }else if(rbTag2.isChecked()){
                            getData("1");
                        }
                    }
                }else{
                    if(num.length()<11) {//如果不够11位数
                        tvInputNumber.setTextColor(ContextCompat.getColor(CostActivity.this, R.color.blacktext));
                        edPhoneNumber.setText("");
                        tvInputNumber.setText("请输入正确的手机号码");
                    }else{
                        if(num.length()==13){
                            if(num.substring(0,2).equals("86")){//如果前两个数字是86就去掉86
                                String num2 = num.substring(2);
                                edPhoneNumber.setText(num2);
                                edPhoneNumber.setSelection(13);
                                getData(flag);
                            }else{
                                // tvInputNumber.setTextColor(getResources().getColor(R.color.blacktext));
                                tvInputNumber.setTextColor(ContextCompat.getColor(CostActivity.this, R.color.blacktext));
                                tvInputNumber.setText("请输入正确的手机号码");
                                edPhoneNumber.setText("");
                                getData(flag);
                            }
                        }else{
                            if(num.length()>13){
                                tvInputNumber.setTextColor(ContextCompat.getColor(CostActivity.this, R.color.blacktext));
                                tvInputNumber.setText("请输入正确的手机号码");
                                edPhoneNumber.setText("");
                                getData(flag);
                            }
                            if(num.length()==12){

                                tvInputNumber.setTextColor(ContextCompat.getColor(CostActivity.this, R.color.blacktext));
                                edPhoneNumber.setText("");
                                tvInputNumber.setText("请输入正确的手机号码");
                            }
                        }

                    }


                }



            }
        }        //支付页面返回处理
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
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
    private void goSelect(int tag) {
        EventMessage eventMessage = new EventMessage();
        eventMessage.setTag(tag);
        EventBus.getDefault().post(eventMessage);

    }

    @Override
    protected void onPause() {
        super.onPause();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edPhoneNumber.getWindowToken(),0);

    }

}

