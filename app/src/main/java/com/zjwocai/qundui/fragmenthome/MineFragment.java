package com.zjwocai.qundui.fragmenthome;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.threeti.teamlibrary.finals.ProjectConfig;
import com.threeti.teamlibrary.finals.ProjectConstant;
import com.threeti.teamlibrary.finals.RequestCodeSet;
import com.threeti.teamlibrary.model.UserModel;
import com.threeti.teamlibrary.net.BaseModel;
import com.threeti.teamlibrary.net.ProcotolCallBack;
import com.threeti.teamlibrary.utils.SPUtil;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.activity.goods.MyBaseFragment;
import com.zjwocai.qundui.activity.login.LoginActivity;
import com.zjwocai.qundui.activity.mine.CarAddActivity;
import com.zjwocai.qundui.activity.mine.CardActivity;
import com.zjwocai.qundui.activity.mine.CarsActivity;
import com.zjwocai.qundui.activity.mine.CertificationActivity;
import com.zjwocai.qundui.activity.mine.CostActivity;
import com.zjwocai.qundui.activity.mine.EtcActivity;
import com.zjwocai.qundui.activity.mine.EtcAskActivity;
import com.zjwocai.qundui.activity.mine.EtcAskingActivity;
import com.zjwocai.qundui.activity.mine.EtcCheckFailActivity;
import com.zjwocai.qundui.activity.mine.EtcOpenProcess;
import com.zjwocai.qundui.activity.mine.EtcPresentActivity;
import com.zjwocai.qundui.activity.mine.Mine2Activity;
import com.zjwocai.qundui.bill.ProtocolBill;
import com.zjwocai.qundui.fragment.EventMessage;
import com.zjwocai.qundui.model.AreaModel;
import com.zjwocai.qundui.model.CarListModel;
import com.zjwocai.qundui.model.CarModel;
import com.zjwocai.qundui.model.CityModel;
import com.zjwocai.qundui.model.ProvinceModel;
import com.zjwocai.qundui.model.SoftUpgradeViewModel;
import com.zjwocai.qundui.model.UploadModel;
import com.zjwocai.qundui.util.AppDownload;
import com.zjwocai.qundui.util.DialogUtil;
import com.zjwocai.qundui.util.ImageUtil;
import com.zjwocai.qundui.util.SelectImageListener;
import com.zjwocai.qundui.util.StringUtil;
import com.zjwocai.qundui.util.ToastUtil;
import com.zjwocai.qundui.widgets.CircleDisplayer;
import com.zjwocai.qundui.widgets.placeview.adapter.ArrayWheelAdapter;
import com.zjwocai.qundui.widgets.placeview.lib.WheelView;
import com.zjwocai.qundui.widgets.placeview.listener.OnItemSelectedListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

import static android.R.id.list;
import static android.app.Activity.RESULT_OK;
//添加相册拍照功能

public class MineFragment extends MyBaseFragment implements ProcotolCallBack, RequestCodeSet,View.OnClickListener {

    private TextView tvLoginOut, tvSex, tvAddress, tvName, tvPhone,tvCertificate;
    private RelativeLayout rlSex, rlAddress, rlCars, rlCards, rlCertificate, rlFeedback, rlPhone;
    private ImageView ivHeader;
    private List<CarModel> models;
    private  List<CarModel> carModels;
    private Dialog dlg;
    private boolean success,asking,end = false;
    private  boolean newcar = true;

    private String proviceid = "", cityid = "", areaid = "";
    private Dialog dialog, dialog_profession, dialog_place, namedlg, netdialog;
    private WheelView wv, wv_provice, wv_city, wv_area;
    private UserModel model;
    private ArrayList<ProvinceModel> provinceModels;
    private ArrayList<CityModel> cityModels;
    private ArrayList<AreaModel> areaModels;
    private String headimg;
    private Dialog callDialog;
    private TextView tvVersion;
    private String versionName;
    private LinearLayout llNew,ivRecharge,llEtc;
    private String version;
    private String description;
    private RelativeLayout rlVersion,rlAbout;
    private String versionTitle,failCarNumber;
    private SoftUpgradeViewModel model2;
    protected DisplayImageOptions options;
    protected ImageLoader imageLoader;
    private InvokeParam invokeParam;
    private TakePhoto takePhoto;

    public MineFragment() {


        // Required empty public constructor
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            String sex = data.getStringExtra("state");
            if (!model.getSex().equals(sex) && !sex.equals("0")) {
                if (sex.equals("1")) {
                    tvSex.setText("男");
                } else if (sex.equals("2")) {
                    tvSex.setText("女");
                } else {
                    tvSex.setText("未设置");
                }
                model.setSex(sex);
                saveUser(model);
                ProtocolBill.getInstance().updateUserInfo(MineFragment.this, getActivity(), sex, model.getHeadimg(),
                        model.getNickname(), model.getProvicecode(), model.getCitycode(), model.getAreacode());
            }
        }else if (resultCode == RESULT_OK){
            if (dialog != null && dialog.isShowing()){
                dialog.dismiss();
            }
            ImageUtil.onActivityResult(getActivity(), requestCode, resultCode, data, new SelectImageListener() {
                @Override
                public void selectPic() {
                    headimg = ImageUtil.getFileName();
//                        key = getImgKey("avator");
                    netdialog.show();

//                        dialog.dismiss();
                }
            });
        }
    }




    @Override
    protected int getLayoutId() {
        View ret = LayoutInflater.from(getActivity()).inflate(R.layout.act_mine2, null);
        return R.layout.act_mine2;
    }
    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        EventBus.getDefault().register(this);
        initData();
        //tvLoginOut = (TextView) root.findViewById(R.id.tv_login_out);
        tvName = (TextView) root.findViewById(R.id.tv_user_name);
        //rlSex = (RelativeLayout) root.findViewById(R.id.rl_sex);
        //tvSex = (TextView) root.findViewById(R.id.tv_sex);
        //tvAddress = (TextView) root.findViewById(R.id.tv_address);
        //rlAddress = (RelativeLayout) root.findViewById(R.id.rl_address);
        rlCars = (RelativeLayout) root.findViewById(R.id.rl_cars);
        rlCards = (RelativeLayout) root.findViewById(R.id.rl_cards);
        rlCertificate = (RelativeLayout) root.findViewById(R.id.rl_certificate);
        llEtc = (LinearLayout) root.findViewById(R.id.ll_etc);
        //rlFeedback = (RelativeLayout) root.findViewById(R.id.rl_feedback);
        ivHeader = (ImageView) root.findViewById(R.id.iv_user_header);
        //tvPhone = (TextView) root.findViewById(R.id.tv_service_phone);
        //rlPhone = (RelativeLayout) root.findViewById(R.id.rl_service_phone);
        //tvVersion = (TextView)root.findViewById(R.id.tv_version);
        //llNew = (LinearLayout) root.findViewById(R.id.ll_new);
        //rlVersion = (RelativeLayout) root.findViewById(R.id.rl_version);
        tvCertificate = (TextView) root.findViewById(R.id.tv_certificate);
        ivRecharge = (LinearLayout) root.findViewById(R.id.iv_recharge);
        rlAbout = (RelativeLayout) root.findViewById(R.id.rl_about);
        initViews();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setGoIndex(EventMessage eventMessage){
        //Log.d(TAG, "setGoIndex: "+eventMessage.getTag());
        if(eventMessage!=null){
            int tag = eventMessage.getTag();
            if(tag== EventMessage.EventMessageAction.TAG_REFRESH_CARD){
                ProtocolBill.getInstance().getUserInfo(new ProcotolCallBack() {
                    @Override
                    public void onTaskSuccess(BaseModel result) {
                        if (RQ_GET_USER_INFO.equals(result.getRequest_code())) {
                            model = (UserModel) result.getResult();
                            Boolean compelete = model.isComplete();//是否完善三证
                            String tips = model.getTips();//提示描述
                            tvCertificate.setTextColor(Color.parseColor("#FF0033"));
                            if (!compelete) {
                                tvCertificate.setText(tips);
                            }
                        }

                        }

                    @Override
                    public void onTaskFail(BaseModel result) {

                    }

                    @Override
                    public void onTaskFinished(String resuestCode) {

                    }
                },getActivity());

            }

        }
    }

    public void initData(){
        imageLoader = ImageLoader.getInstance();
       //请求网络
        ProtocolBill.getInstance().getUserInfo(this, getActivity());

    }
    /**
     * 当界面重新展示时（fragment.show）,调用onrequest刷新界面
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);

        if (!hidden) {

            ProtocolBill.getInstance().getUserInfo(new ProcotolCallBack() {
                @Override
                public void onTaskSuccess(BaseModel result) {
                    if (RQ_GET_USER_INFO.equals(result.getRequest_code())) {
                        model = (UserModel) result.getResult();

                        Boolean compelete = model.isComplete();//是否完善三证
                        String tips = model.getTips();//提示描述
                        tvCertificate.setTextColor(Color.parseColor("#FF0033"));
                        if(!compelete){
                            tvCertificate.setText(tips);
                        }

                        saveUser(model);
                        setAliasAndTags();
                        if (model.getHeadimg() != null && !model.getHeadimg().equals("")) {
                            imageLoader.displayImage(ProjectConfig.DEBUG_IMAGE_URL + model.getHeadimg(), ivHeader, options);
                        }

//                        if (model.getSex().equals("1")) {
//                            tvSex.setText("男");
//                        } else if (model.getSex().equals("2")) {
//                            tvSex.setText("女");
//                        } else {
//                            tvSex.setText("未设置");
//                        }

                        //tvAddress.setText(model.getProvicename() + " " + model.getCityname());
                        tvName.setText(model.getNickname());
//                        if (StringUtils.isEmpty(model.getKftel())){
//                            tvPhone.setText("0571-87620191");
//                        } else {
//                            tvPhone.setText(model.getKftel());
//                        }
                    } else if (RQ_GET_PROVINCE_LIST.equals(result.getRequest_code())) {
                        List<ProvinceModel> models = (List<ProvinceModel>) result.getResult();
                        if (models != null && !models.isEmpty()) {
                            provinceModels.clear();
                            provinceModels.addAll(models);
                            ProtocolBill.getInstance().getCities(this, getActivity(), provinceModels.get(0).getId());
                        }
                    } else if (RQ_GET_CITY_LIST.equals(result.getRequest_code())) {
                        List<CityModel> models = (List<CityModel>) result.getResult();
                        if (models != null && !models.isEmpty()) {
                            cityModels.clear();
                            cityModels.addAll(models);
                            if (dialog_place != null) {
                                wv_city.setAdapter(new ArrayWheelAdapter(cityModels));
                                wv_city.setCurrentItem(0);
                                wv_provice.setEnabled(true);
                            }
                            ProtocolBill.getInstance().getAreas(this, getActivity(), cityModels.get(0).getId());
                        }
                    } else if (RQ_GET_AREA_LIST.equals(result.getRequest_code())) {
                        List<AreaModel> models = (List<AreaModel>) result.getResult();
                        if (models != null && !models.isEmpty()) {
                            areaModels.clear();
                            areaModels.addAll(models);
                            if (dialog_place != null) {
                                wv_area.setAdapter(new ArrayWheelAdapter(areaModels));
                                wv_area.setCurrentItem(0);
                                wv_city.setEnabled(true);
                                wv_provice.setEnabled(true);
                            }
                            showPlaceDialog();
                        }
                    } else if (RQ_UPLOAD.equals(result.getRequest_code())) {
                        netdialog.dismiss();
                        List<UploadModel> uploadModel = (List<UploadModel>) result.getResult();
                        if (uploadModel != null && !uploadModel.isEmpty()) {
                            imageLoader.displayImage(ProjectConfig.DEBUG_IMAGE_URL + uploadModel.get(0).getFileurl3(), ivHeader, options);
                            model.setHeadimg(uploadModel.get(0).getFileurl3());
                            saveUser(model);
                            ProtocolBill.getInstance().updateUserInfo(MineFragment.this, getActivity(), model.getSex(), uploadModel.get(0).getFileurl3(),
                                    model.getNickname(), model.getProvicecode(), model.getCitycode(), model.getAreacode());
                        } else {
                            //showToast("上传失败");
                            ToastUtil.toastShortShow("上传失败");
                        }
                    }
                }

                @Override
                public void onTaskFail(BaseModel result) {
//        super.onTaskFail(result);
                    if (result.getMsgtype() != null && result.getMsgtype().equals("2")){
                        //showToast("登录失效，请重新登录");
                        ToastUtil.toastShortShow("登录失效，请重新登录");
                        saveUser(null);
                        startActivity(new Intent(getActivity(),LoginActivity.class));
                    }else if (UPDATE_USER_INFO.equals(result.getCode())){
                        ProtocolBill.getInstance().getUserInfo(this, getActivity());
                    }else if (!TextUtils.isEmpty(result.getMsg())) {
                        //showToast(result.getMsg() + "");
                        ToastUtil.toastShortShow(result.getMsg() + "");
                    }
                }

                @Override
                public void onTaskFinished(String resuestCode) {
                    //super.onTaskFinished(resuestCode);
                    if (netdialog.isShowing()) {
                        netdialog.dismiss();
                    }
                }
            }, getActivity());


        }
    }


    public void initViews() {

        //tvLoginOut.setOnClickListener(this);
        //rlSex.setOnClickListener(this);
        //rlAddress.setOnClickListener(this);
        rlCars.setOnClickListener(this);
        rlCards.setOnClickListener(this);
        rlCertificate.setOnClickListener(this);
        //rlFeedback.setOnClickListener(this);
        tvName.setOnClickListener(this);
        ivHeader.setOnClickListener(this);
        //rlPhone.setOnClickListener(this);
        //rlVersion.setOnClickListener(this);
        ivRecharge.setOnClickListener(this);
        llEtc.setOnClickListener(this);
        rlAbout.setOnClickListener(this);
        //获取是否更新
        String newString = SPUtil.getString("newVersion");
        //设置新版本显示
        if(newString.equals("1")){
            //llNew.setVisibility(View.VISIBLE);
            //tvVersion.setVisibility(View.GONE);


        }
        //获取到版本号
        PackageManager packageManager = getActivity().getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);

            versionName = packInfo.versionName;
            if(versionName != null){

                //tvVersion.setText("v "+versionName);
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_default_header).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.ic_default_header).showImageOnFail(R.drawable.ic_default_header).cacheInMemory(true).cacheOnDisk(true)
                .displayer(new CircleDisplayer(0)).bitmapConfig(Bitmap.Config.RGB_565).build();

        provinceModels = new ArrayList<>();
        cityModels = new ArrayList<>();
        areaModels = new ArrayList<>();
        model = ProtocolBill.getInstance().getNowUser();
        netdialog = DialogUtil.getProgressDialog(getActivity(), getString(R.string.ui_net_request));


    }

    public void setAliasAndTags() {
        if (null != getUser()) {
            LinkedHashSet<String> tags = new LinkedHashSet<String>();
            tags.add(getUser().getMobile() + "");
            JPushInterface.setAliasAndTags(getActivity(),
                    getUser().getMobile(), tags);// 调用JPush
        }
    }
    public UserModel getUser() {
        UserModel user = (UserModel) SPUtil.getObjectFromShare(ProjectConstant.KEY_USERINFO);
        return user;
    }

    @Override
    public void onTaskSuccess(BaseModel result) {

        if (RQ_GET_USER_INFO.equals(result.getRequest_code())) {
            model = (UserModel) result.getResult();

            Boolean compelete = model.isComplete();//是否完善三证
            String tips = model.getTips();//提示描述
            tvCertificate.setTextColor(Color.parseColor("#FF0033"));
            if (!compelete) {
                tvCertificate.setText(tips);
            }

            saveUser(model);
            setAliasAndTags();
            if (model.getHeadimg() != null && !model.getHeadimg().equals("")) {
                imageLoader.displayImage(ProjectConfig.DEBUG_IMAGE_URL + model.getHeadimg(), ivHeader, options);
            }
            tvName.setText(model.getNickname());

        } else if (RQ_GET_PROVINCE_LIST.equals(result.getRequest_code())) {
            List<ProvinceModel> models = (List<ProvinceModel>) result.getResult();
            if (models != null && !models.isEmpty()) {
                provinceModels.clear();
                provinceModels.addAll(models);
                ProtocolBill.getInstance().getCities(this, getActivity(), provinceModels.get(0).getId());
            }
        } else if (RQ_GET_CITY_LIST.equals(result.getRequest_code())) {
            List<CityModel> models = (List<CityModel>) result.getResult();
            if (models != null && !models.isEmpty()) {
                cityModels.clear();
                cityModels.addAll(models);
                if (dialog_place != null) {
                    wv_city.setAdapter(new ArrayWheelAdapter(cityModels));
                    wv_city.setCurrentItem(0);
                    wv_provice.setEnabled(true);
                }
                ProtocolBill.getInstance().getAreas(this, getActivity(), cityModels.get(0).getId());
            }
        } else if (RQ_GET_AREA_LIST.equals(result.getRequest_code())) {
            List<AreaModel> models = (List<AreaModel>) result.getResult();
            if (models != null && !models.isEmpty()) {
                areaModels.clear();
                areaModels.addAll(models);
                if (dialog_place != null) {
                    wv_area.setAdapter(new ArrayWheelAdapter(areaModels));
                    wv_area.setCurrentItem(0);
                    wv_city.setEnabled(true);
                    wv_provice.setEnabled(true);
                }
                showPlaceDialog();
            }
        } else if (RQ_UPLOAD.equals(result.getRequest_code())) {
            netdialog.dismiss();
            List<UploadModel> uploadModel = (List<UploadModel>) result.getResult();
            if (uploadModel != null && !uploadModel.isEmpty()) {
                imageLoader.displayImage(ProjectConfig.DEBUG_IMAGE_URL + uploadModel.get(0).getFileurl3(), ivHeader, options);
                model.setHeadimg(uploadModel.get(0).getFileurl3());
                saveUser(model);
                ProtocolBill.getInstance().updateUserInfo(MineFragment.this, getActivity(), model.getSex(), uploadModel.get(0).getFileurl3(),
                        model.getNickname(), model.getProvicecode(), model.getCitycode(), model.getAreacode());
            } else {
                //showToast("上传失败");
                ToastUtil.toastShortShow("上传失败");
            }
        } else if (RQ_GET_ALL_CAR_LIST.equals(result.getRequest_code())) {

           DialogUtil.hideWaitDialog();
            //判断有没有车辆
            models = (List<CarModel>) result.getResult();
            carModels = new ArrayList();
            List<String> l = new ArrayList<>();
            ArrayList<String> listCarNumber = new ArrayList<String>();
            ArrayList<String> listCarId = new ArrayList<String>();

            if (models != null && !models.isEmpty()) { //有车
                for (int i = 0; i < models.size(); i++) {
                    String carNumber = models.get(i).getCarnumber();
                    String id = models.get(i).getId();
                    listCarNumber.add(carNumber);
                    listCarId.add(id);
                    String sta = models.get(i).getAudit_state();
                    l.add(sta);
Log.v("ffffffff",models.get(i).getAudit_state());
                 if(models.get(i).getAudit_state().equals("1")){
                     if(!models.get(i).getEtc_number().equals("")){ //如果有etc卡号
                         success = true;
                         break;
                     }else{ //如果没有etc卡号
                         asking = true;
                     }
                 }
                    if(models.get(i).getAudit_state().equals("0")){ //审核中
                        asking = true;

                        Log.v("ffffffffffff",models.get(i).getCarnumber());

                    }

                    if(models.get(i).getAudit_state().equals("-1")){//审核失败
                        //申请失败的车牌号
                        failCarNumber =  models.get(i).getCarnumber();
                        end = true;
                    }
                }


                if(success){//如果有申请成功的直接充值界面
                    startActivity(new Intent(getActivity(), EtcActivity.class));
                   return;

                }
                if(asking){//申请中界面
                    startActivity(new Intent(getActivity(),EtcAskingActivity.class));
                    return;

                }
                if(end){//申请失败携带车牌号跳转
                    Intent intent = new Intent(getActivity(), EtcCheckFailActivity.class);
                    intent.putExtra("checkfail",failCarNumber);
                    startActivity(intent);
                    return;
                }

                if(!(success && asking && end)){ //没有申请过车辆直接跳进去申请界面,有车

                    Intent intent = new Intent(getActivity(), EtcPresentActivity.class);
                    intent.putExtra("list", listCarNumber);
                    intent.putExtra("list2", listCarId);
                    startActivity(intent);
                    return;
                }


                return;
            } else { //没车,去添加车辆
                Intent intent = new Intent(getActivity(), EtcPresentActivity.class);
                intent.putExtra("nocar", "nocar");
                startActivity(intent);
                return;
            }
    }

    }

    @Override
    public void onTaskFail(BaseModel result) {
//        super.onTaskFail(result);
        if (result.getMsgtype() != null && result.getMsgtype().equals("2")){
           //showToast("登录失效，请重新登录");
            ToastUtil.toastShortShow("登录失效，请重新登录");
            saveUser(null);
            startActivity(new Intent(getActivity(),LoginActivity.class));
        }else if (UPDATE_USER_INFO.equals(result.getCode())){
            ProtocolBill.getInstance().getUserInfo(this, getActivity());
        }else if (!TextUtils.isEmpty(result.getMsg())) {
            //showToast(result.getMsg() + "");
            ToastUtil.toastShortShow(result.getMsg() + "");
        }
    }

    @Override
    public void onTaskFinished(String resuestCode) {
        //super.onTaskFinished(resuestCode);
        if (netdialog.isShowing()) {
            netdialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_about:
                startActivity(new Intent(getActivity(),Mine2Activity.class));

                break;
            case R.id.iv_recharge:
                //跳转到充值页面
                startActivity(new Intent(getActivity(),CostActivity.class));
                break;
            case R.id.ll_etc:
                SPUtil.saveString("failcar","");
                SPUtil.saveString("addcar","");
                DialogUtil.showWaitDialog(getActivity(),"加载中...");
                //判断有没有车辆
                ProtocolBill.getInstance().getCarList(this,getActivity(),"1");
                break;
            case R.id.rl_cars:
                startActivity(new Intent(getActivity(),CarsActivity.class));
                break;
            case R.id.rl_cards:
                startActivity(new Intent(getActivity(),CardActivity.class));
                break;
            case R.id.rl_certificate:
                startActivity(new Intent(getActivity(),CertificationActivity.class));
                break;

            case R.id.tv_user_name:
                new MineFragment.PopupWindows(getActivity(), ivHeader);
                break;
            case R.id.iv_user_header:
                showDialog();
                break;

        }

    }
    /**
     * 拨打电话
     *
     * @param number 电话号码
     */
    private void showCallDialog(final String number) {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        View view = factory.inflate(R.layout.dialog_enter_call, null);
        final TextView des = (TextView) view.findViewById(R.id.tv_des);
        final TextView enter = (TextView) view.findViewById(R.id.tv_enter);
        final TextView cancel = (TextView) view.findViewById(R.id.tv_cancel);
        final TextView content = (TextView) view.findViewById(R.id.tv_content);
        des.setText("客服电话：");
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
        callDialog = DialogUtil.getDialog(getActivity(), view, Gravity.CENTER, true);
        callDialog.show();
    }
    private void showDialog() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_select_head, null);
        TextView take_photo = (TextView) view.findViewById(R.id.take_photo);
        File file=new File(Environment.getExternalStorageDirectory(), "/temp/"+System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists())file.getParentFile().mkdirs();
        final Uri imageUri = Uri.fromFile(file);
        TextView select_album = (TextView) view.findViewById(R.id.select_album);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ImageUtil.takePhoto(getActivity(), "temp.jpg");
                getTakePhoto().onPickFromCapture(imageUri);

            }
        });
        select_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // ImageUtil.selectFromAlbum(getActivity());
                getTakePhoto().onPickFromGallery();
            }
        });
        dialog = DialogUtil.getDialog(getActivity(), view, Gravity.BOTTOM, true);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
    public void saveUser(UserModel model) {
        SPUtil.saveObjectToShare(ProjectConstant.KEY_USERINFO, model);
    }
    // 升级弹窗
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(versionTitle);
        builder.setMessage(description);


        //builder.setCancelable(false);//流氓手段,让用户点击返回键没有作用, 不建议采纳
        // 点击物理返回键,取消弹窗时的监听
//        builder.setOnCancelListener(new OnCancelListener() {
//
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                finishLoading();
//                enterHome();
//            }
//        });

        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateApp();
            }
        });

        builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void updateApp(){
        ToastUtil.toastShortShow(getActivity(),"已开始下载，请注意查看!");
        AppDownload.getInstance().doDownloadApp(getActivity(),model2);

    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        Log.i("gsc","获取照片成功=="+result.getImage().getOriginalPath());
        ivHeader.setImageBitmap(BitmapFactory.decodeFile(result.getImage().getOriginalPath()));
//        上传修改的图片到服务器
        ProtocolBill.getInstance().upload(MineFragment.this,getActivity(), result.getImage().getOriginalPath());
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
//TODO
        Log.i("gsc","获取照片取消==");
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        Log.i("gsc","获取照片失败=="+result.getImage().getOriginalPath());
    }

    private void showPlaceDialog() {
        if (null == dialog_place) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dia_picker_time, null);
            dialog_place = DialogUtil.getDialog(getActivity(), view, Gravity.BOTTOM, true);
            wv_provice = (WheelView) view.findViewById(R.id.wv_year);
            wv_city = (WheelView) view.findViewById(R.id.wv_month);
            wv_area = (WheelView) view.findViewById(R.id.wv_day);
            TextView btnCancel = (TextView) view.findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_place.dismiss();
                }
            });
            final TextView btnSubmit = (TextView) view.findViewById(R.id.btnSubmit);
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (wv_provice.isEnabled() && wv_city.isEnabled() && wv_area.isEnabled()) {
                        String place = provinceModels.get(wv_provice.getCurrentItem()).getName() +
                                " " + cityModels.get(wv_city.getCurrentItem()).getName();
                        proviceid = provinceModels.get(wv_provice.getCurrentItem()).getCode();
                        cityid = cityModels.get(wv_city.getCurrentItem()).getCode();
                        areaid = areaModels.get(wv_area.getCurrentItem()).getCode();
                        tvAddress.setText(place);
                        model.setProvicecode(proviceid);
                        model.setCitycode(cityid);
                        model.setAreacode(areaid);
                        //saveUser(model);
                        //储存用户信息
                        SPUtil.saveObjectToShare(ProjectConstant.KEY_USERINFO, model);
                        ProtocolBill.getInstance().updateUserInfo(MineFragment.this, getActivity(), model.getSex(),
                                model.getHeadimg(), model.getNickname(), proviceid, cityid, areaid);
                        dialog_place.dismiss();
                    }
                }
            });
            wv_provice.setAdapter(new ArrayWheelAdapter(provinceModels));
            wv_provice.setCyclic(false);
            wv_provice.setHide(true);
            wv_provice.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int i) {
                    if (null != provinceModels && !provinceModels.isEmpty()){
                        String provinceId = provinceModels.get(i).getId();
                        ProtocolBill.getInstance().getCities(MineFragment.this, getActivity(), provinceId);
                    } else {
                        dialog_place.dismiss();
                    }

                }
            });
            wv_city.setAdapter(new ArrayWheelAdapter(cityModels));
            wv_city.setCyclic(false);
            wv_city.setHide(true);
            wv_city.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int i) {
                    if (null != cityModels && !cityModels.isEmpty()){
                        String CityId = cityModels.get(i).getId();
                        ProtocolBill.getInstance().getAreas(MineFragment.this,getActivity(), CityId);
                    } else {
                        dialog_place.dismiss();
                    }
                }
            });
            wv_area.setAdapter(new ArrayWheelAdapter(areaModels));
            wv_area.setCyclic(false);
            wv_area.setHide(true);
            wv_area.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int i) {
                    wv_area.setEnabled(true);
                    wv_city.setEnabled(true);
                    wv_provice.setEnabled(true);
                }
            });
            wv_provice.setWv(wv_city, wv_area);
            wv_city.setWv(wv_provice, wv_area);
            wv_area.setWv(wv_provice, wv_city);

            wv_provice.setCurrentItem(0);
            wv_city.setCurrentItem(0);
            wv_area.setCurrentItem(0);
        }
        dialog_place.show();
    }




    class PopupWindows extends PopupWindow {

        public PopupWindows(Context mContext, View parent) {

            View view = View
                    .inflate(mContext, R.layout.dialog_item_nickname, null);
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
                    .findViewById(R.id.et_popupwindows_reward_count);


            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

            bt1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
                    dismiss();
                }
            });
            bt2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String content = et.getText().toString().trim();
                    if (content != null && !content.equals("")) {
                        tvName.setText(content);
                        model.setNickname(content);
                        saveUser(model);
                        ProtocolBill.getInstance().updateUserInfo(MineFragment.this, getActivity(), model.getSex(), model.getHeadimg(),
                                content, model.getProvicecode(), model.getCitycode(), model.getAreacode());
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
                        dismiss();
                    } else {
                        //MineFragment.showToast("昵称不能为空");
                        ToastUtil.toastShortShow("昵称不能为空");
                    }
                }
            });
        }
    }
}
