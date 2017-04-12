package com.zjwocai.qundui.activity.goods;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.threeti.teamlibrary.activity.BaseProtocolActivity;
import com.threeti.teamlibrary.finals.ProjectConfig;
import com.threeti.teamlibrary.model.UserModel;
import com.threeti.teamlibrary.net.BaseModel;
import com.threeti.teamlibrary.net.ProcotolCallBack;
import com.umeng.analytics.MobclickAgent;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.activity.login.LoginActivity;
import com.zjwocai.qundui.activity.mine.CarAddActivity;
import com.zjwocai.qundui.activity.nav.RestRouteShowActivity;
import com.zjwocai.qundui.adapter.ChooseCarsAdapter;
import com.zjwocai.qundui.bill.ProtocolBill;
import com.zjwocai.qundui.fragment.Constant;
import com.zjwocai.qundui.fragment.EventMessage;
import com.zjwocai.qundui.fragmenthome.HomeFragment;
import com.zjwocai.qundui.model.CarModel;
import com.zjwocai.qundui.model.EnableCarModel;
import com.zjwocai.qundui.model.OrderDetailModel;
import com.zjwocai.qundui.model.UploadModel;
import com.zjwocai.qundui.service.LocationService;
import com.zjwocai.qundui.service.LocationService2;
import com.zjwocai.qundui.util.BitmapUtil;
import com.zjwocai.qundui.util.DialogUtil;
import com.zjwocai.qundui.util.ImageUtil;
import com.zjwocai.qundui.util.StringUtil;
import com.zjwocai.qundui.widgets.ScrollDisabledListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单详情
 * Created by NieLiQin on 2016/7/25.
 */
public class OrderDetailActivity extends BaseProtocolActivity implements View.OnClickListener {
    private static final int REQUEST_IMAGE_GET = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private LinearLayout rlIng, rlBefore;
    private TextView tvNumber, tvStartPhone, tvEndPhone, tvAccept, tvSendEnd, tvPayHint, tvRefuse;
    private TextView tvStart, tvStart2, tvEnd, tvEnd2, tvGoods, tvCost, tvWeight, tvVolume, tvTicket, tvReceipt,
            tvStartMan, tvEndMan, tvCarTime, tvCar, tvSendInfo, tvCall, tvSendDate, messageMoney;
    private TextView tvStartAddress, tvEndAddress, tvStartAddressLong, tvEndAddressLong;
    private ImageView ivPhoto, ivFinish, rlNavi;
    private Dialog dlg;
    private int state;
    private String id;
    private Dialog netDialog;
    private List<EnableCarModel> carModels;
    private ChooseCarsAdapter dialogAdapter;
    private int dialogState;
    private OrderDetailModel model;
    private String from;
    private Dialog dialog;
    private String mCurrentPhotoPath;
    private String shAddress;

    private String isReceive = "";
    private int m = 1;

    private Runnable runnable;
    private Handler handler;
    private String filePath;
    private String path;
    private Dialog callDialog;
    private String isThird;
    private Dialog nullNumberDialog;
    private UserModel userModel;

    private TextView tvDistance;
    private List<CarModel> models;
    private int cars=0;
    private int page = 1;

    public OrderDetailActivity() {
        super(R.layout.act_order_detail);

    }

    public static String getFilePathFromContentUri(Uri uri, Context context) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};
        Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
        if (cursor == null) return null;
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    @Override
    public void getIntentData() {
        super.getIntentData();

        EventBus.getDefault().register(this);
        Log.e("oooooooooo","==ooooo");

        DialogUtil.showWaitDialog(this, "加载中");

        Map map = (Map) getIntent().getSerializableExtra("data");
        state = (int) map.get("state");
        id = (String) map.get("id");
        from = (String) map.get("from");
        isReceive = (String) map.get("isReceive");
        isThird = (String) map.get("isThird");
        if (isReceive == null || isReceive.equals("")) {
            isReceive = "false";
        }
        if (isThird == null || isThird.equals("") || isThird.equals("0")) {
            isThird = "false";
        }
        ProtocolBill.getInstance().getOrderDetail(this, this, id);
        ProtocolBill.getInstance().getCarList(new ProcotolCallBack() {
            @Override
            public void onTaskSuccess(BaseModel result) {
                models = (List<CarModel>) result.getResult();//这里得到了数据 TODO======================================================================
                if (models != null && !models.isEmpty()) {
                    cars = 1;
                }
            }
            @Override
            public void onTaskFail(BaseModel result) {

            }

            @Override
            public void onTaskFinished(String resuestCode) {

            }
        }, this, String.valueOf(page));

        userModel = ProtocolBill.getInstance().getNowUser();

    }


    @Override
    public void findIds() {
        tvDistance = (TextView) findViewById(R.id.tv_distance);
        tvNumber = (TextView) findViewById(R.id.tv_number);
        tvStart = (TextView) findViewById(R.id.tv_start);
        tvStart2 = (TextView) findViewById(R.id.tv_start2);
        tvEnd = (TextView) findViewById(R.id.tv_end);
        tvEnd2 = (TextView) findViewById(R.id.tv_end2);
        tvGoods = (TextView) findViewById(R.id.tv_goods);
        tvCost = (TextView) findViewById(R.id.tv_cost);
        //tvPack = (TextView) findViewById(R.id.tv_pack);
        tvWeight = (TextView) findViewById(R.id.tv_weight);
        tvVolume = (TextView) findViewById(R.id.tv_volume);
        tvTicket = (TextView) findViewById(R.id.tv_ticket);
        tvReceipt = (TextView) findViewById(R.id.tv_receipt);
        tvStartMan = (TextView) findViewById(R.id.tv_start_man);
        //tvStartCompany = (TextView) findViewById(R.id.tv_start_company);
        tvStartAddress = (TextView) findViewById(R.id.tv_start_address);
        tvStartAddressLong = (TextView) findViewById(R.id.tv_start_address_long);
        tvStartPhone = (TextView) findViewById(R.id.tv_start_phone);
        tvEndMan = (TextView) findViewById(R.id.tv_end_man);
        //tvEndCompany = (TextView) findViewById(R.id.tv_end_company);
        tvEndAddressLong = (TextView) findViewById(R.id.tv_end_address_long);
        tvEndAddress = (TextView) findViewById(R.id.tv_end_address);
        tvEndPhone = (TextView) findViewById(R.id.tv_end_phone);
        tvSendDate = (TextView) findViewById(R.id.tv_send_date);
        //tvCarTime = (TextView) findViewById(R.id.tv_car_time);
        //tvCarType = (TextView) findViewById(R.id.tv_car_type);
        tvCar = (TextView) findViewById(R.id.tv_car);
        //tvCarLength = (TextView) findViewById(R.id.tv_car_length);
        tvSendInfo = (TextView) findViewById(R.id.tv_goods_info);
        tvAccept = (TextView) findViewById(R.id.tv_choose_car);
        tvCall = (TextView) findViewById(R.id.tv_call);
        rlIng = (LinearLayout) findViewById(R.id.ll_ing);
        rlBefore = (LinearLayout) findViewById(R.id.ll_before);
        tvSendEnd = (TextView) findViewById(R.id.tv_enter_send);
        //tvSendHint = (TextView) findViewById(R.id.tv_send_hint);
        ivFinish = (ImageView) findViewById(R.id.iv_finish_icon);
        tvPayHint = (TextView) findViewById(R.id.tv_pay_hint);
        tvRefuse = (TextView) findViewById(R.id.tv_refuse);
        ivPhoto = (ImageView) findViewById(R.id.iv_photo);
        rlNavi = (ImageView) findViewById(R.id.rl_navigation);
        messageMoney = (TextView) findViewById(R.id.message_money);

        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_pic_update).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.ic_pic_update).showImageOnFail(R.drawable.ic_pic_update)
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    @Override
    public void initViews() {
        initTitle("订单详情");
        title.setLeftIcon(R.drawable.ic_title_left, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (state == 1) {
            tvPayHint.setVisibility(View.VISIBLE);
            rlBefore.setVisibility(View.VISIBLE);
            rlIng.setVisibility(View.GONE);
            tvSendEnd.setVisibility(View.GONE);
            if (from.equals("6")) {
                if (isThird.equals("false")) {
                    tvRefuse.setVisibility(View.GONE);
                    tvCall.setVisibility(View.VISIBLE);
                } else {
                    tvAccept.setVisibility(View.GONE);
                    tvRefuse.setVisibility(View.GONE);
                    tvCall.setVisibility(View.VISIBLE);
                }
                tvCall.setOnClickListener(this);
            }
        } else if (state == 2) {
            tvSendEnd.setVisibility(View.VISIBLE);
            tvPayHint.setVisibility(View.GONE);
            rlBefore.setVisibility(View.GONE);
            rlIng.setVisibility(View.VISIBLE);
        } else if (state == 3) {
            tvPayHint.setVisibility(View.GONE);
            rlBefore.setVisibility(View.GONE);
            rlIng.setVisibility(View.VISIBLE);
            tvSendEnd.setVisibility(View.GONE);
            //tvSendHint.setVisibility(View.GONE);
            ivFinish.setVisibility(View.VISIBLE);
        }

//        tvStartPhone.setOnClickListener(this);
//        tvEndPhone.setOnClickListener(this);
        tvAccept.setOnClickListener(this);
        tvSendEnd.setOnClickListener(this);
        tvRefuse.setOnClickListener(this);
        rlNavi.setOnClickListener(this);
        ivPhoto.setOnClickListener(this);

        carModels = new ArrayList<>();
        dialogAdapter = new ChooseCarsAdapter(this, carModels);
        netDialog = DialogUtil.getProgressDialog(this, getResources().getString(R.string.ui_net_request));

        runnable = new Runnable() {
            @Override
            public void run() {
                // 自定义大小，防止OOM
                Bitmap mp = BitmapUtil.getimage(filePath);
                mp = ImageUtil.compressImage(mp);
                path = BitmapUtil.save(ProjectConfig.DIR_IMG + File.separator +
                        System.currentTimeMillis() + ".jpg", mp);
                Message m = new Message();
                m.what = 1;
                handler.handleMessage(m);
            }
        };

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    ProtocolBill.getInstance().upload(OrderDetailActivity.this, OrderDetailActivity.this, path);

                }
            }
        };

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_start_phone:
//                startActivity(OrderPayActivity.class);  //需要询问是否是一次付费看两个，价钱是否一致？
                showCallDialog(tvStartPhone.getText().toString().trim());
                break;
            case R.id.tv_end_phone:
//                startActivity(OrderPayActivity.class);
                showCallDialog(tvEndPhone.getText().toString().trim());
                break;
            case R.id.tv_choose_car:
//                netDialog = DialogUtil.getProgressDialog(this, getResources().getString(R.string.ui_net_request));
                netDialog.show();
                ProtocolBill.getInstance().getEnableCarList(this, this);
                break;
            case R.id.tv_enter_send:
                DialogUtil.getAlertNoTitleDialog(this, "您确定要确认送达吗？", "确定", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //发送消息通知发送定位信息
                        goSelect(EventMessage.EventMessageAction.TAG_SEEVICE_LOCATION_ORDER);
                        Log.v("ggggggggggggggg","发送定位信息");

                    }
                }).show();
                break;
            case R.id.tv_refuse:
                DialogUtil.getAlertNoTitleDialog(this, "您确定要拒单吗？", "确定", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ProtocolBill.getInstance().refuseOrder(OrderDetailActivity.this, OrderDetailActivity.this, id);
                    }
                }).show();
                break;
            case R.id.iv_photo:
                showPhotoDialog();
                break;
            case R.id.rl_navigation:
                if (null != model) {
                    Map map = new HashMap();
                    map.put("city", model.getShcityname());
                    map.put("keyword", shAddress);
                    map.put("startCity", model.getFhcityname());
                    map.put("startKeyword", model.getFhaddress());

                    startActivity(RestRouteShowActivity.class, map);
                }
                break;
            case R.id.tv_call:
                if (null != model) {
                    if (null == model.getSjtel() || model.getSjtel().equals("")) {
                        showNullNumberDialog();
                    } else {
                        showCallDialog(model.getSjtel());
                    }
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setGoIndex(EventMessage eventMessage){

        if(eventMessage!=null){
            int tag = eventMessage.getTag();
                if(tag== EventMessage.EventMessageAction.TAG_STOP_LOCATION_ORDER){//确认收货
                    EventBus.getDefault().unregister(this);
                    //等确认收货后再停止服务
                    ProtocolBill.getInstance().enterSend(OrderDetailActivity.this, OrderDetailActivity.this, id, model.getArriveimg());
                    Log.v("xxxxxxxxxxx","问题问题问题");
            }
            }


    }


    @Override
    public void onTaskSuccess(BaseModel result) {

        //得到车辆集合
        if (RQ_GET_ALL_CAR_LIST.equals(result.getRequest_code())) {

            models = (List<CarModel>) result.getResult();
            if (models != null && !models.isEmpty()) {
                cars = 1;
            }
        }
        if (RQ_GET_ORDER_DETAIL.equals(result.getRequest_code())) {
            model = (OrderDetailModel) result.getResult();
            tvNumber.setText(model.getCode());  //订单编号
            if (model.getFhcityname().isEmpty()) {
                tvStart.setText(model.getFhcityname()); //发货城市
            } else {
                tvStart.setText(model.getFhcityname().substring(0, 2));

            }
            if (model.getShcityname().isEmpty()) {
                tvEnd.setText(model.getShcityname());  //收货城市
            } else {
                tvEnd.setText(model.getShcityname().substring(0, 2));
            }

            //发货区
            if (model.getFhareaname().isEmpty()) {
                tvStart2.setText(model.getFhareaname());
            } else {
                tvStart2.setText(model.getFhareaname().substring(0, 2));
            }
            //收货区
            if (model.getShareaname().isEmpty()) {
                tvEnd2.setText(model.getShareaname());
            } else {
                tvEnd2.setText(model.getShareaname().substring(0, 2));
            }
//            tvStart.setText(model.getFhcityname()); //发货城市
//            tvStart2.setText(model.getFhareaname());//发货区
//            tvEnd.setText(model.getShcityname());  //收获城市
//            tvEnd2.setText(model.getShareaname());//收货区
            tvGoods.setText(model.getGoodsinfo()); //货物名
            tvCost.setText(model.getFreight());//订单运费
            //tvPack.setText(model.getPacktype());  //包装
            tvWeight.setText(model.getWeight() + "吨"); //重量
            tvVolume.setText(model.getVolume() + "立方"); //重量
            messageMoney.setText(model.getCost());//信息费
            if (model.getDistance().isEmpty()) {
                tvDistance.setText("未知");
            } else {
                tvDistance.setText("约" + model.getDistance().substring(0, model.getDistance().indexOf(".")) + "公里");//总里程
            }
            if (model.getIskp().equals("1")) {//是否开票
                tvTicket.setText("要");
            } else {
                tvTicket.setText("不要");
            }
            if (model.getIshd().equals("1")) {//回单
                tvReceipt.setText("要");
            } else {
                tvReceipt.setText("不要");
            }
            tvStartMan.setText(model.getShipper());//发货方
            //tvStartCompany.setText(model.getShipcompany());//发货公司
            String fhAddress = model.getFhprovicename() + model.getFhcityname() //发货地址
                    + model.getFhareaname() + model.getFhaddress();
            tvStartAddress.setText(fhAddress);
//            tvStartAddress.post(new StrecthRunnable(tvStartAddress,tvStartAddressLong,1,fhAddress));

            double cost = Double.parseDouble(model.getCost());//获取接单所需费用
            if (model.getShippertel() != null && !model.getShippertel().equals("") && model.getShippertel().length() >= 7) { //发货方电话
                if (state == 1 && cost != 0) {
                    tvStartPhone.setText(model.getShippertel().substring(0, 3) + "****"
                            + model.getShippertel().substring(model.getShippertel().length() - 4
                            , model.getShippertel().length()) + "（付费）");
                } else {
                    tvStartPhone.setText(model.getShippertel());
                    tvPayHint.setVisibility(View.INVISIBLE);
                    tvStartPhone.setOnClickListener(this);
                }

            } else {
                if (state == 1 && cost != 0) {
                    tvStartPhone.setText("********" + "（付费）");
                } else {
                    tvStartPhone.setText(model.getShippertel());
                    tvPayHint.setVisibility(View.INVISIBLE);
                    tvStartPhone.setOnClickListener(this);
                }
            }
            tvEndMan.setText(model.getReceiver()); //收货方,卸货地
            //tvEndCompany.setText(model.getReceivecompany());//收货公司 ,卸货公司
            //收货地址 卸货地址
            shAddress = model.getShprovicename() + model.getShcityname() //收货地址 ,卸货地址
                    + model.getShareaname() + model.getShaddress();
            tvEndAddress.setText(shAddress);
            // MyLogUtil.e("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii",shAddress);


//            tvEndAddress.post(new StrecthRunnable(tvEndAddress,tvEndAddressLong,1,shAddress));
            if (model.getReceivertel() != null && !model.getReceivertel().equals("") && model.getReceivertel().length() > 7) { //收货方电话
                if (state == 1 && cost != 0) {
                    tvEndPhone.setText(model.getReceivertel().substring(0, 3) + "****"
                            + model.getReceivertel().substring(model.getReceivertel().length() - 4
                            , model.getReceivertel().length()) + "（付费）");
                } else {
                    tvEndPhone.setText(model.getReceivertel());
                    tvPayHint.setVisibility(View.INVISIBLE);
                    tvEndPhone.setOnClickListener(this);
                }
            } else {
                if (state == 1 && cost != 0) {
                    tvEndPhone.setText("********" + "（付费）");
                } else {
                    tvEndPhone.setText(model.getReceivertel());
                    tvPayHint.setVisibility(View.INVISIBLE);
                    tvEndPhone.setOnClickListener(this);
                }
            }
            if (model.getShipdate() != null && !model.getShipdate().equals("") && model.getShipdate().length() >= 10) { //发货日期
                tvSendDate.setText(model.getShipdate().substring(0, 10));
                //tvSendDate.setText(model.getShipdate());
            } else {
                tvSendDate.setText("未知");
            }
            if (model.getLoadstartdate() != null && model.getLoadstartdate().length() > 12 &&
                    model.getLoadenddate() != null && model.getLoadenddate().length() > 12) {

                //tvCarTime.setText(model.getLoadstartdate().substring(model.getLoadstartdate().length() - 8,
//                        model.getLoadstartdate().length() - 3) + "~" +
//                        model.getLoadenddate().substring(model.getLoadenddate().length() - 8,
//                                model.getLoadenddate().length() - 3));//装车时间
            } else {
                // tvCarTime.setText("未知");
            }

            if (model.getUsecar().equals("1")) {//用车类型
                tvCar.setText("整车");
            } else if (model.getUsecar().equals("2")) {
                tvCar.setText("拼车");
            } else {
                tvCar.setText("未知");
            }
            //tvCarType.setText(TypeModel.getCarType(model.getCartype())); //车辆类型
            //tvCarLength.setText(TypeModel.getLength(model.getCarlength()));//车长
            tvSendInfo.setText(model.getRemarks());  //发货备注

            tvPayHint.setText("需支付" + model.getCost() + "元查看联系方式");
            if (model.getArriveimg() != null && !model.getArriveimg().equals("")) {
                ivPhoto.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageLoader.displayImage(ProjectConfig.IMAGE_URL + model.getArriveimg(), ivPhoto, options);
                ivPhoto.setOnClickListener(this);
            }

//            } else if (state == 3) {
//                ivPhoto.setOnClickListener(this);
//            }

            if (isReceive.equals("true")) {
                netDialog.show();
                ProtocolBill.getInstance().getEnableCarList(this, this);
            }

            DialogUtil.hideWaitDialog();


        } else if (RQ_GET_ENABLE_CAR_LIST.equals(result.getRequest_code())) {
            netDialog.dismiss();
            List<EnableCarModel> models = (List<EnableCarModel>) result.getResult();
            carModels.clear();
            if (models != null && !models.isEmpty()) {
                carModels.addAll(models);
                carModels.get(0).setSelect(true);
                dialogState = 0;
            } else {
                dialogState = 1;
            }
            showDialog();
        } else if (RQ_GET_REFUSE_ORDER.equals(result.getRequest_code())) {
            showToast("拒单成功");
            onBackPressed();
        } else if (RQ_ENTER_SEND.equals(result.getRequest_code())) {
            showToast("确认送达成功！");
            Log.v("ggggggggggggggg","确认送达成功");

//            new Handler().postDelayed(new Runnable() {
//                public void run() {
//                    //确认送达成功  停止服务service2
//                    Intent stopLocationServiceIntent2 = new Intent(getApplicationContext(), LocationService2.class);
//                    stopService(stopLocationServiceIntent2);
//                }
//            }, 1000);


            new Handler().postDelayed(new Runnable() {
                public void run() {
                    //开启service1
                    Intent startLocationServiceIntent = new Intent(getApplicationContext(), LocationService.class);
                    startLocationServiceIntent.putExtra("id",userModel.getId());
                    startService(startLocationServiceIntent);
                }
            }, 5000);


//            Map map = new HashMap();
//            map.put("state", 3);
//            map.put("id", id);
//            map.put("from", from);
            tvPayHint.setVisibility(View.GONE);
            rlBefore.setVisibility(View.GONE);
            rlIng.setVisibility(View.VISIBLE);
            tvSendEnd.setVisibility(View.GONE);
            ivFinish.setVisibility(View.VISIBLE);
            tvPayHint.setVisibility(View.INVISIBLE);

            //startActivity(OrderDetailActivity.class, map);
        } else if (RQ_PAY_BY_BALANCE.equals(result.getRequest_code())) {
            showToast("接单成功！");
            netDialog.dismiss();
            //接单成功的时候开启服务service2
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    //开启service2
                    Intent startLocationServiceIntent2 = new Intent(getApplicationContext(), LocationService2.class);
                    startLocationServiceIntent2.putExtra("id",userModel.getId());
                    startService(startLocationServiceIntent2);
                }
            }, 5000);

            //停止service1
            Intent stopLocationServiceIntent = new Intent(this, LocationService.class);
            stopService(stopLocationServiceIntent);
            Map map = new HashMap();
            map.put("state", 2);
            map.put("id", id);
            map.put("from", from);
            startActivity(OrderDetailActivity.class, map);
        } else if (RQ_UPLOAD.equals(result.getRequest_code())) {
            netDialog.dismiss();
            List<UploadModel> models = (List<UploadModel>) result.getResult();
            if (models != null && !models.isEmpty()) {
                ivPhoto.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageLoader.displayImage(ProjectConfig.IMAGE_URL + models.get(0).getFileurl3(), ivPhoto, options);
                model.setArriveimg(models.get(0).getFileurl3());
                ProtocolBill.getInstance().uploadImage(OrderDetailActivity.this, OrderDetailActivity.this, id, model.getArriveimg());
            }
        }else if(RQ__UPLOAD_IMAGE.equals(result.getRequest_code())){
             //showToast("上传图片成功！");

        }
    }

    @Override
    public void onTaskFail(@SuppressWarnings("rawtypes") BaseModel result) {
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
        if (netDialog.isShowing()) {
            netDialog.dismiss();
        }
    }

    private void showCallDialog(final String number) {
        LayoutInflater factory = LayoutInflater.from(this);
        View view = factory.inflate(R.layout.dialog_enter_call, null);
        final TextView enter = (TextView) view.findViewById(R.id.tv_enter);
        final TextView cancel = (TextView) view.findViewById(R.id.tv_cancel);
        final TextView content = (TextView) view.findViewById(R.id.tv_content);
        content.setText(number);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtil.isNO(number.replace("-", "").trim())) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    Uri data = Uri.parse("tel:" + number.replace("-", "").trim());
                    intent.setData(data);
                    startActivity(intent);
                } else {
                    showToast("号码无效！");
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
        callDialog = DialogUtil.getDialog(this, view, Gravity.CENTER, true);
        callDialog.show();
    }

    public void showDialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        View view;
        if (dialogState == 0) {
            view = factory.inflate(R.layout.dialog_unwrap, null);
            final TextView cancel = (TextView) view.findViewById(R.id.tv_cancel);
            final TextView affirm = (TextView) view.findViewById(R.id.tv_affirm);
            final ScrollDisabledListView sdlv = (ScrollDisabledListView) view.findViewById(R.id.sdlv_cars);
            sdlv.setAdapter(dialogAdapter);
            dialogAdapter.notifyDataSetChanged();
            sdlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    for (int i = 0; i < carModels.size(); i++) {
                        carModels.get(i).setSelect(false);
                    }
                    carModels.get((int) id).setSelect(true);
                    dialogAdapter.notifyDataSetChanged();
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dlg.dismiss();
                }
            });
            affirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    double cost = Double.parseDouble(model.getCost());//获取接单所需费用
                    dlg.dismiss();
                    if (cost == 0) {
                        netDialog.show();
                        ProtocolBill.getInstance().payByBalance(OrderDetailActivity.this,
                                OrderDetailActivity.this, id, getSelectedCar(), model.getCost());
                    } else {
                        Map map = new HashMap();
                        map.put("carid", getSelectedCar());
                        map.put("orderid", id);
                        map.put("fee", model.getCost());
                        map.put("from", from);
                        startActivity(OrderPayActivity.class, map);

                    }

                }
            });
        } else {

            if (cars == 1) {//您没有空闲车辆
                view = factory.inflate(R.layout.dialog_unwrap_no2, null);
                final TextView enter = (TextView) view.findViewById(R.id.tv_enter);
                enter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(CarAddActivity.class);
                        dlg.dismiss();
                    }
                });
            } else {
                view = factory.inflate(R.layout.dialog_unwrap_no, null);
                final TextView enter = (TextView) view.findViewById(R.id.tv_enter);
                enter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(CarAddActivity.class);
                        dlg.dismiss();
                    }
                });


            }
        }
        dlg = DialogUtil.getDialog(this, view, Gravity.CENTER, true);
        dlg.show();
    }

    private String getSelectedCar() {
        String ret = "";
        for (EnableCarModel model : carModels) {
            if (model.isSelect()) {
                ret = model.getId();
            }
        }
        return ret;
    }


    @Override
    public void  onBackPressed(){
        //发消息给子fragment
        goSelect(EventMessage.EventMessageAction.TAG_REFRESH_HOME);
        goSelect(EventMessage.EventMessageAction.TAG_REFRESH_GOODS);

        finish();
    }
    private void goSelect(int tag) {
        EventMessage eventMessage = new EventMessage();
        eventMessage.setTag(tag);
        EventBus.getDefault().post(eventMessage);

    }

//    @Override
//    public void onBackPressed() {
//
//        finish();
//        super.onBackPressed();
////        if (from.equals("4")) {
////            startActivity(HomeActivity.class, null, 0);
////            finish();
//////        } else if (from.equals("6")){
//////            startActivity(HurryActivity.class, null, 0);
////        } else if (from.equals("0") || from.equals("") || from.equals("2") || from.equals("3")) {
////            startActivity(GoodsActivity.class, from, 0);
////        } else {
     //       super.onBackPressed();
////        }
//    }

    private void showPhotoDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_select_head, null);
        TextView take_photo = (TextView) view.findViewById(R.id.take_photo);
        TextView select_album = (TextView) view.findViewById(R.id.select_album);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        select_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        dialog = DialogUtil.getDialog(OrderDetailActivity.this, view, Gravity.BOTTOM, true);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    /**
     * 空号码提示窗口
     */
    private void showNullNumberDialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        View view = factory.inflate(R.layout.dialog_null_call, null);
        final TextView enter = (TextView) view.findViewById(R.id.tv_enter);
        final TextView cancel = (TextView) view.findViewById(R.id.tv_cancel);
        final TextView content = (TextView) view.findViewById(R.id.tv_content);
        content.setText(userModel.getKftel());
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtil.isNO(userModel.getKftel().replace("-", "").trim())) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    Uri data = Uri.parse("tel:" + userModel.getKftel().replace("-", "").trim());
                    intent.setData(data);
                    startActivity(intent);
                } else {
                    showToast("号码无效！");
                }
                nullNumberDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nullNumberDialog.dismiss();
            }
        });
        nullNumberDialog = DialogUtil.getDialog(this, view, Gravity.CENTER, true);
        nullNumberDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            filePath = null;
            if (requestCode == REQUEST_IMAGE_GET) {
                //返回的是content://的样式
                filePath = getFilePathFromContentUri(data.getData(), this);
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                if (mCurrentPhotoPath != null) {
                    filePath = mCurrentPhotoPath;
                }
            }
            if (!TextUtils.isEmpty(filePath)) {
                new Thread(runnable).start();
                netDialog.show();
            }
        }
    }

    /**
     * 从相册中获取
     */
    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        //判断系统中是否有处理该Intent的Activity
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        } else {
            showToast("未找到图片查看器");
        }
    }

    private void dispatchTakePictureIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断系统中是否有处理该Intent的Activity
        if (intent.resolveActivity(getPackageManager()) != null) {
            // 创建文件来保存拍的照片
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // 异常处理
            }
            if (photoFile != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        } else {
            showToast("无法启动相机");
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* 文件名 */
                ".jpg",         /* 后缀 */
                storageDir      /* 路径 */
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("goods_detail");
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("goods_detail"); //（仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    public void startSelf(String id, int state) {
        Map map = new HashMap();
        map.put("state", state);
        map.put("id", id);
        map.put("from", from);
        startActivity(OrderDetailActivity.class, map);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}
