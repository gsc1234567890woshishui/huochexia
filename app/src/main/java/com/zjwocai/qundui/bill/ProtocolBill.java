/**
 * Project Name:ThreeTiProject
 * Package Name:com.threeti.threetiproject.net.bill
 * File Name:ProtocolBill.java
 * Date:2014年10月10日下午2:46:57
 * Copyright:Copyright (c) 2014, 翔傲科技（上海）有限公司 All Rights Reserved.
 */
package com.zjwocai.qundui.bill;


import android.content.Context;
import android.util.Log;

import com.threeti.teamlibrary.ApplicationEx;
import com.threeti.teamlibrary.activity.BaseProtocolActivity;
import com.threeti.teamlibrary.finals.ProjectConfig;
import com.threeti.teamlibrary.finals.ProjectConstant;
import com.threeti.teamlibrary.finals.RequestCodeSet;
import com.threeti.teamlibrary.model.UserModel;
import com.threeti.teamlibrary.net.BaseAsyncTask;
import com.threeti.teamlibrary.net.ProcotolCallBack;
import com.threeti.teamlibrary.net.RequestConfig;
import com.threeti.teamlibrary.utils.AESUtil;
import com.threeti.teamlibrary.utils.DeviceUtil;
import com.threeti.teamlibrary.utils.MD5Util;
import com.threeti.teamlibrary.utils.SPUtil;
import com.zjwocai.qundui.model.ADModel;
import com.zjwocai.qundui.model.AreaModel;
import com.zjwocai.qundui.model.BalanceModel;
import com.zjwocai.qundui.model.CarModel;
import com.zjwocai.qundui.model.CardModel;
import com.zjwocai.qundui.model.CertModel;
import com.zjwocai.qundui.model.CityModel;
import com.zjwocai.qundui.model.CoastModel;
import com.zjwocai.qundui.model.CoastModel2;
import com.zjwocai.qundui.model.EnableCarModel;
import com.zjwocai.qundui.model.FlowModel;
import com.zjwocai.qundui.model.HomeOrderModel;
import com.zjwocai.qundui.model.HurryListModel;
import com.zjwocai.qundui.model.IAEBModel;
import com.zjwocai.qundui.model.NewsListModel;
import com.zjwocai.qundui.model.NoticeModel;
import com.zjwocai.qundui.model.OrderDetailModel;
import com.zjwocai.qundui.model.OrderModel;
import com.zjwocai.qundui.model.ProvinceModel;
import com.zjwocai.qundui.model.RecoderModel;
import com.zjwocai.qundui.model.SoftUpgradeViewModel;
import com.zjwocai.qundui.model.UploadModel;
import com.zjwocai.qundui.net.UploadAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName:ProtocolBill
 * Date:2014年10月10日
 *
 * @author BaoHang
 * @since [产品/模块版本] （可选）
 */
public class ProtocolBill implements RequestCodeSet {
    private static ProtocolBill protocolBill;
    private boolean isCode = false;//是否加密


    public static ProtocolBill getInstance() {
        if (protocolBill == null) {
            protocolBill = new ProtocolBill();
        }
        return protocolBill;
    }

    public String getResString(int id) {
        return ApplicationEx.getInstance().getResources().getString(id);
    }

    /**
     * 得到基本请求配置参数

     * @param
     * @param requestcode
     * @return
     */
    private RequestConfig getBaseConfig(String requestcode) {
        RequestConfig config = new RequestConfig();
        config.setRequestCode(requestcode);
        config.setMethod(RequestConfig.POST);
        config.setWebAddress(ProjectConfig.HEAD_URL);
        return config;
    }

    /**
     * appcode: fdd
     * appversion: 1.0
     * languagetype: zh-cn
     * devicetype: ios
     * devicemodel: phone
     * sys: iphone5s
     * sysversion: 7.1.2
     * deviceidentifier: deviceidentifier
     * service: /http/user
     * action: login.action
     * page: 1
     * pagesize: 5
     * sign: 030ae481ba39160b71be47ad74367712
     * noncestr: noncestr
     * timestamp: 201511101056
     *
     * @return
     */

    private Map<String, String> getBaseData(Context context, String service, String action, String page) {
        Map<String, String> map = new HashMap<String, String>();
        String appcode = "tmppro";
        map.put("appcode", appcode);
        map.put("languagetype", "zh-cn");
        map.put("devicetype", "android");
        map.put("devicemodel", "phone");
        map.put("sys", DeviceUtil.getPhoneModel());
        map.put("sysversion", DeviceUtil.getSysVersion());
        map.put("deviceidentifier", DeviceUtil.getIMEI(context));//设备唯一识别码
        map.put("service", service);
        map.put("action", action);
        map.put("page", page);
        map.put("pagesize", "10");
        String timestamp = System.currentTimeMillis() + "";
        String noncestr = MD5Util.getRandomForLen(6);
        map.put("sign", MD5Util.getSign(noncestr, timestamp, appcode, service, action));
        map.put("noncestr", noncestr);
        map.put("timestamp", timestamp);
        String visonname = "1.0";
        visonname = DeviceUtil.getVersion(context);
        map.put("appversion", visonname);
        if (getNowUser() != null) {
            String i = getNowUser().getToken();
            map.put("token", getNowUser().getToken());
        }
        return map;
    }

    public UserModel getNowUser() {
        return (UserModel) SPUtil.getObjectFromShare(ProjectConstant.KEY_USERINFO);
    }

    public List<CityModel> getCities() {
        return (List<CityModel>) SPUtil.getObjectFromShare(ProjectConstant.KEY_CITIES);
    }

    public List<ProvinceModel> getPros() {
        return (List<ProvinceModel>) SPUtil.getObjectFromShare(ProjectConstant.KEY_PROS);
    }

    public String getLocation() {
        return SPUtil.getString(ProjectConstant.KEY_LOCATION);
    }


    /**
     * 登录
     * @param protocol
     * @param activity
     * @param mobile 手机号
     * @param password 密码
     * @param type 登录类型 type 1:0 密码:验证码
     * @param verifcode 验证码
     */
    public void login(ProcotolCallBack protocol, BaseProtocolActivity activity, String mobile,
                      String password,String verifcode, String type) {
        BaseAsyncTask task = new BaseAsyncTask(protocol, activity, "正在登录...");
        Map<String, String> head = getBaseData(activity, "/http/qundui/qdauth", "login.action", "1");
        Map<String, String> data = new HashMap<>();
        data.put("mobile", mobile);
        data.put("password", password);
        data.put("verifcode", verifcode);
        data.put("type", type);
        RequestConfig config = getBaseConfig(RQ_LOGIN);
        config.setCls(UserModel.class);



        task.execute(getBase64Encode(config, head, data, isCode));
    }
    //上传手机型号
    public void handset(ProcotolCallBack protocol, Context activity, String id,
                        String mobile){
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/qdauth", "autologin.action","1");
        Map<String, String> data = new HashMap<>();
        data.put("id", id);
        data.put("mobile",mobile);
        RequestConfig config = getBaseConfig(RQ_MOBILE);
        config.setCls(String.class);
        task.execute(getBase64Encode(config, head, data, false));
    }

    /**
     * 获取验证码
     * @param protocol
     * @param activity
     * @param mobile 手机号
     * @param type 类型 0:1 登录：修改密码
     */
    public void getCode(ProcotolCallBack protocol, Context activity, String mobile, String type) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/qdauth", "sendSms.action", "1");
        Map<String, String> data = new HashMap<>();
        data.put("mobile", mobile);
        data.put("type", type);
        RequestConfig config = getBaseConfig(RQ_GET_CODE);
        config.setCls(String.class);
        task.execute(getBase64Encode(config, head, data, isCode));
    }

    /**
     * 验证验证码
     * @param protocol
     * @param activity
     * @param verifcode 验证码
     * @param mobile 手机号
     */
    public void checkCode(ProcotolCallBack protocol, BaseProtocolActivity activity,String mobile, String verifcode) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/qdauth", "checkverifcode.action", "1");
        Map<String, String> data = new HashMap<>();
        data.put("verifcode", verifcode);
        data.put("mobile", mobile);
        RequestConfig config = getBaseConfig(RQ_CHECK_CODE);
        config.setCls(String.class);

        task.execute(getBase64Encode(config, head, data, isCode));
    }

    /**
     * 验证提现验证码
     * @param protocol
     * @param activity
     * @param verifcode 验证码
     * @param mobile 手机号
     */
    public void checkWithdrawCode(ProcotolCallBack protocol, Context activity,String mobile, String verifcode) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/qdauth", "txcheckverifcode.action", "1");
        Map<String, String> data = new HashMap<>();
        data.put("verifcode", verifcode);
        data.put("mobile", mobile);
        RequestConfig config = getBaseConfig(RQ_CHECK_WITHDRAW_CODE);
        config.setCls(String.class);

        task.execute(getBase64Encode(config, head, data, isCode));
    }

    /**
     * 修改密码
     * @param protocol
     * @param activity
     * @param newpassword 新密码
     * @param mobile 手机号
     */
    public void updatePassword(ProcotolCallBack protocol, BaseProtocolActivity activity, String newpassword,String mobile) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/qdauth", "updatepassword.action", "1");
        Map<String, String> data = new HashMap<>();
        data.put("newpassword", newpassword);
        data.put("mobile", mobile);
        RequestConfig config = getBaseConfig(RQ_GET_UPDATE_PASSWORD);
        config.setCls(String.class);
        task.execute(getBase64Encode(config, head, data, isCode));
    }

    /**
     * 获取用户信息
     * @param protocol
     * @param activity
     */
    public void getUserInfo(ProcotolCallBack protocol, Context activity) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/qdauth", "getDriverByToken.action", "1");
        Map<String, String> data = new HashMap<>();
        RequestConfig config = getBaseConfig(RQ_GET_USER_INFO);
        config.setCls(UserModel.class);
        task.execute(getBase64Encode(config, head, data, isCode));
    }

    /**
     * 获取首页轮播图
     * @param protocol
     * @param activity
     */
    public void getHomePics(ProcotolCallBack protocol, Context activity) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/home", "getbannerlist.action", "1");
        Map<String, String> data = new HashMap<>();
        RequestConfig config = getBaseConfig(RQ_GET_HOME_PICS);
        config.setElement(ADModel.class);
        task.execute(getBase64Encode(config, head, data, isCode));
    }

    /**
     * 获取首页订单
     * @param protocol
     * @param activity
     */
    public void getHomeOrders(ProcotolCallBack protocol, Context activity, String page) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/home", "gethomeordrelist.action", page);
        Map<String, String> data = new HashMap<>();
        RequestConfig config = getBaseConfig(RQ_GET_HOME_ORDERS);
        config.setElement(HomeOrderModel.class);
        task.execute(getBase64Encode(config, head, data, isCode));
    }


    /**
     * 获取首页消息
     * @param protocol
     * @param activity
     */
    public void getHomeNews(ProcotolCallBack protocol, Context activity, String page) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/home", "getmessageslist.action", page);
        Map<String, String> data = new HashMap<>();
        RequestConfig config = getBaseConfig(RQ_GET_HOME_NEWS);
        config.setCls(NewsListModel.class);
        task.execute(getBase64Encode(config, head, data, isCode));
    }

    /**
     * 获取公告信息
     * @param protocol
     * @param activity
     */
    public void getNotice(ProcotolCallBack protocol, BaseProtocolActivity activity) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/order", "qqqordernum.action", "1");
        Map<String, String> data = new HashMap<>();
        RequestConfig config = getBaseConfig(RQ_GET_NOTICE);
        config.setCls(NoticeModel.class);
        task.execute(getBase64Encode(config, head, data, isCode));
    }

    /**
     * 获取抢抢抢订单列表
     * @param protocol
     * @param activity
     */
    public void getHurryList(ProcotolCallBack protocol, BaseProtocolActivity activity, String fhcitycode, String shcitycode,
                             String carlength, String cartype, String page) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/order", "qqqorderlist.action", page);
        Map<String, String> data = new HashMap<>();
        data.put("fhcitycode",fhcitycode);
        data.put("shcitycode",shcitycode);
        data.put("carlength",carlength);
        data.put("cartype",cartype);
        RequestConfig config = getBaseConfig(RQ_GET_HURRY_LIST);
        config.setElement(HurryListModel.class);
        task.execute(getBase64Encode(config, head, data, isCode));
    }

    /**
     * 获取司机余额
     * @param protocol
     * @param activity
     */
    public void getBalance(ProcotolCallBack protocol, Context activity) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/money", "getdriverbalance.action", "1");
        Map<String, String> data = new HashMap<>();
        RequestConfig config = getBaseConfig(RQ_GET_BALANCE);
        config.setCls(BalanceModel.class);
        task.execute(getBase64Encode(config, head, data, isCode));
    }

    /**
     * 提现
     * @param protocol
     * @param activity
     */
    public void withdraw(ProcotolCallBack protocol, BaseProtocolActivity activity, String cardid, String amount) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/money", "withdraw.action", "1");
        Map<String, String> data = new HashMap<>();
        data.put("cardid", cardid);
        data.put("amount", amount);
        RequestConfig config = getBaseConfig(RQ_GET_WITHDRAW);
        config.setCls(Object.class);
        task.execute(getBase64Encode(config, head, data, isCode));
    }

    /**
     * 获取银行卡列表
     * @param protocol
     * @param activity
     */
    public void getCards(ProcotolCallBack protocol, BaseProtocolActivity activity, String page) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/money", "getdriverbankcardlist.action", page);
        Map<String, String> data = new HashMap<>();
        RequestConfig config = getBaseConfig(RQ_GET_CARDS);
        config.setElement(CardModel.class);
        task.execute(getBase64Encode(config, head, data, isCode));
    }

    /**
     * 获取收支明细
     * @param protocol
     * @param activity
     */
    public void getIAEB(ProcotolCallBack protocol, BaseProtocolActivity activity,String page) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/money", "getdriveraccountslist.action", page);
        Map<String, String> data = new HashMap<>();
        RequestConfig config = getBaseConfig(RQ_GET_IAEB);
        config.setElement(IAEBModel.class);
        task.execute(getBase64Encode(config, head, data, isCode));
    }

    /**
     * 获取全部订单
     * @param protocol
     * @param activity
     */
    public void getAllOrders(ProcotolCallBack protocol, Context activity,String page) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/order", "getallorderlist.action", page);
        Map<String, String> data = new HashMap<>();
        RequestConfig config = getBaseConfig(RQ_GET_ALL_ORDERS);
        config.setElement(OrderModel.class);
        task.execute(getBase64Encode(config, head, data, isCode));
    }

    /**
     * 获取各类订单
     * @param protocol
     * @param activity
     *
     * @param status 订单状态 1:2:3 未接，已接，已完成
     */
    public void getKindOrders(ProcotolCallBack protocol, Context activity,String status, String page) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/order", "getstatusorderlist.action", page);
        Map<String, String> data = new HashMap<>();
        data.put("status", status);
        RequestConfig config = getBaseConfig(RQ_GET_KIND_ORDERS);
        config.setElement(OrderModel.class);
        task.execute(getBase64Encode(config, head, data, isCode));
    }

    /**
     * 搜索订单
     * @param protocol
     * @param activity
     *
     * @param content 关键字
     */
    public void searchOrder(ProcotolCallBack protocol, BaseProtocolActivity activity,String content, String page) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/order", "searchorder.action", page);
        Map<String, String> data = new HashMap<>();
        data.put("content", content);
        RequestConfig config = getBaseConfig(RQ_SEARCH_ORDERS);
        config.setElement(OrderModel.class);
        task.execute(getBase64Encode(config, head, data, isCode));
    }

    /**
     * 上传搜索
     * @param protocol
     * @param activity
     *
     * @param content 关键字
     */
    public void uploadSearch(ProcotolCallBack protocol, BaseProtocolActivity activity,String content) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/driver", "addsearch.action", "1");
        Map<String, String> data = new HashMap<>();
        data.put("content", content);
        RequestConfig config = getBaseConfig(RQ_UPLOAD_SEARCH);
        config.setCls(Object.class);
        task.execute(getBase64Encode(config, head, data, isCode));
    }

    //上传经纬度
    public void track(ProcotolCallBack protocol, Context activity, String driverId,
                      String lng, String lat, String type){
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/track", "report.action","1");
        Map<String, String> data = new HashMap<>();
        data.put("driverId", driverId);
        data.put("lng", lng);
        data.put("lat", lat);
        data.put("type", type);
        RequestConfig config = getBaseConfig(RQ_TRACK);
        config.setCls(String.class);
        task.execute(getBase64Encode(config, head, data, false));
    }
    //调用版本更新的接口
    public void renew(ProcotolCallBack protocol, Context activity
                      ){
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/app", "getLatestVerion.action","1");
        Map<String, String> data = new HashMap<>();
        RequestConfig config = getBaseConfig(RQ_RENEW);
        config.setCls(SoftUpgradeViewModel.class);
        task.execute(getBase64Encode(config, head, data, false));
    }

    //调用话费充值
    public void rechargeCoast(ProcotolCallBack protocol, Context activity, String phoneNumber
    ){
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/business", "getHFShelf.action","1");
        Map<String, String> data = new HashMap<>();
        data.put("phoneNumber",phoneNumber);
        RequestConfig config = getBaseConfig(RQ_COAST);
        config.setCls(CoastModel2.class);
        task.execute(getBase64Encode(config, head, data, false));
    }
    //调用流量充值
    public void rechargeFlow(ProcotolCallBack protocol, Context activity,String phoneNumber
    ){
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/business", "getLLShelf.action","1");
        Map<String, String> data = new HashMap<>();
        data.put("phoneNumber",phoneNumber);
        RequestConfig config = getBaseConfig(RQ_FLOW);
        config.setCls(FlowModel.class);
        task.execute(getBase64Encode(config, head, data, false));
    }

    /**
     * 获取订单详情
     * @param protocol
     * @param activity
     *
     * @param orderid 订单ID
     */
    public void getOrderDetail(ProcotolCallBack protocol, BaseProtocolActivity activity, String orderid) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/order", "getorderinfo.action","1");
        Map<String, String> data = new HashMap<>();
        data.put("orderid", orderid);
        RequestConfig config = getBaseConfig(RQ_GET_ORDER_DETAIL);

        config.setCls(OrderDetailModel.class);
        task.execute(getBase64Encode(config, head, data, isCode));
    }

    /**
     * 拒单
     * @param protocol
     * @param activity
     *
     * @param orderid 订单ID
     */
    public void refuseOrder(ProcotolCallBack protocol, Context activity, String orderid) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/order", "refuseorder.action","1");
        Map<String, String> data = new HashMap<>();
        data.put("orderid", orderid);
        RequestConfig config = getBaseConfig(RQ_GET_REFUSE_ORDER);
        config.setCls(Object.class);
        task.execute(getBase64Encode(config, head, data, isCode));
    }

    /**
     * 获取可用车辆列表
     * @param protocol
     * @param activity
     *
     */
    public void getEnableCarList(ProcotolCallBack protocol, BaseProtocolActivity activity) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/order", "getordercarlist.action","1");
        Map<String, String> data = new HashMap<>();
        RequestConfig config = getBaseConfig(RQ_GET_ENABLE_CAR_LIST);
        config.setElement(EnableCarModel.class);
        task.execute(getBase64Encode(config, head, data, isCode));
    }

    /**
     * 余额支付
     * @param protocol
     * @param activity
     *
     */
    public void payByBalance(ProcotolCallBack protocol, BaseProtocolActivity activity, String orderid, String carid, String amount) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/order", "payorderbybanlance.action","1");
        Map<String, String> data = new HashMap<>();
        data.put("orderid", orderid);
        data.put("carid", carid);
        data.put("amount", amount);
        RequestConfig config = getBaseConfig(RQ_PAY_BY_BALANCE);
        config.setCls(String.class);
        task.execute(getBase64Encode(config, head, data, isCode));
    }

    /**
     * 确认送达
     * @param protocol
     * @param activity
     *
     */
    public void enterSend(ProcotolCallBack protocol, Context activity, String orderid, String imgurl) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/order", "orderarrive.action","1");
        Map<String, String> data = new HashMap<>();
        data.put("orderid", orderid);
        data.put("imgurl", imgurl);
        RequestConfig config = getBaseConfig(RQ_ENTER_SEND);
        config.setCls(String.class);
        task.execute(getBase64Encode(config, head, data, isCode));
    }

    /**
     * 确认送达
     * @param protocol
     * @param activity
     *
     */
    public void uploadImage(ProcotolCallBack protocol, Context activity, String orderid, String imgurl) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/order", "orderarrive.action","1");
        Map<String, String> data = new HashMap<>();
        data.put("orderid", orderid);
        data.put("imgurl", imgurl);
        RequestConfig config = getBaseConfig(RQ__UPLOAD_IMAGE);
        config.setCls(String.class);
        task.execute(getBase64Encode(config, head, data, isCode));
    }


    /**
     * 修改个人信息
     * @param protocol
     * @param activity
     *
     */
    public void updateUserInfo(ProcotolCallBack protocol, Context activity, String sex, String headimg,
                               String nickname, String provicecode, String citycode, String areacode) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/driver", "updatedriverinfo.action","1");
        Map<String, String> data = new HashMap<>();
        data.put("sex", sex);
        data.put("headimg", headimg);
        data.put("nickname", nickname);
        data.put("provicecode", provicecode);
        data.put("citycode", citycode);
        data.put("areacode", areacode);
        RequestConfig config = getBaseConfig(UPDATE_USER_INFO);
        config.setCls(String.class);
        task.execute(getBase64Encode(config, head, data, isCode));
    }

    /**
     * 获取我的车辆列表
     * @param protocol
     * @param activity
     *
     */
    public void getCarList(ProcotolCallBack protocol, Context activity, String page) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/driver", "getallcarlist.action",page);
        Map<String, String> data = new HashMap<>();
        RequestConfig config = getBaseConfig(RQ_GET_ALL_CAR_LIST);
        config.setElement(CarModel.class);
        task.execute(getBase64Encode(config, head, data, isCode));
    }


    /**
     * 获取etc查询业务
     * @param protocol
     * @param activity
     *
     */
    public void getEtcRecord(ProcotolCallBack protocol, Context activity, String page,String pagesize,String type,String carNumber) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/driver", "getEtcConsumptionRecord.action",page);
        Map<String, String> data = new HashMap<>();
        data.put("page",page);
        data.put("pagesize",pagesize);
        data.put("type",type);
        data.put("carNumber",carNumber);
        RequestConfig config = getBaseConfig(RQ_GET_ETCRECODER);
        config.setElement(RecoderModel.class);//hao
        task.execute(getBase64Encode(config, head, data, isCode));
    }

    /**
     * 添加车辆
     * @param protocol
     * @param activity
     *
     */
    public void addCar(ProcotolCallBack protocol, BaseProtocolActivity activity, String carnumber,
                       String type, String enddate, String carload) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/driver", "addcar.action","1");
        Map<String, String> data = new HashMap<>();
        data.put("carnumber", carnumber);
        data.put("type", type);
        data.put("enddate", enddate);
        data.put("carload", carload);
        RequestConfig config = getBaseConfig(RQ_ADD_CAR);
        config.setCls(String.class);
        task.execute(getBase64Encode(config, head, data, isCode));
    }

    /**
     * 删除车辆
     * @param protocol
     * @param activity
     * @param carid 车id
     *
     */
    public void deleteCar(ProcotolCallBack protocol, BaseProtocolActivity activity, String carid) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/driver", "deletecar.action","1");
        Map<String, String> data = new HashMap<>();
        data.put("carid", carid);
        RequestConfig config = getBaseConfig(RQ_DELETE_CAR);
        config.setCls(String.class);
        task.execute(getBase64Encode(config, head, data, isCode));
    }

    /**
     * 删除银行卡
     * @param protocol
     * @param activity
     * @param cardid 银行卡号
     *
     */
    public void deleteCard(ProcotolCallBack protocol, BaseProtocolActivity activity, String cardid) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/driver", "deletebankcard.action","1");
        Map<String, String> data = new HashMap<>();
        data.put("cardid", cardid);
        RequestConfig config = getBaseConfig(RQ_DELETE_CARD);
        config.setCls(String.class);
        task.execute(getBase64Encode(config, head, data, isCode));
    }

    /**
     * 添加银行卡
     * @param protocol
     * @param activity
     * @param type 银行类型
     * @param code 银行卡号
     * @param ower 注册姓名
     * @param provicecode 省份
     * @param citycode 城市
     *
     */
    public void addCard(ProcotolCallBack protocol, BaseProtocolActivity activity,
                        String type, String code, String ower, String provicecode, String citycode) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/driver", "addbankcard.action","1");
        Map<String, String> data = new HashMap<>();
        data.put("type", type);
        data.put("code", code);
        data.put("ower", ower);
        data.put("provicecode", provicecode);
        data.put("citycode", citycode);
        RequestConfig config = getBaseConfig(RQ_ADD_CARD);
        config.setCls(String.class);
        task.execute(getBase64Encode(config, head, data, isCode));
    }

    /**
     * 获取证件
     * @param protocol
     * @param activity
     *
     */
    public void getCertification(ProcotolCallBack protocol, BaseProtocolActivity activity) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/driver", "getcertificate.action","1");
        Map<String, String> data = new HashMap<>();
        RequestConfig config = getBaseConfig(RQ_GET_CERTIFICATION);
        config.setCls(CertModel.class);
        task.execute(getBase64Encode(config, head, data, isCode));
    }

    /**
     * 上传证件
     * @param protocol
     * @param activity
     *
     */
    public void uploadCertification(ProcotolCallBack protocol, BaseProtocolActivity activity, String sfz1,
                                    String sfz2, String jsz, String xsz) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/driver", "addcertificate.action","1");
        Map<String, String> data = new HashMap<>();
        data.put("sfz1", sfz1);
        data.put("sfz2", sfz2);
        data.put("jsz", jsz);
        data.put("xsz", xsz);
        RequestConfig config = getBaseConfig(RQ_UPLOAD_CERTIFICATION);
        config.setCls(String.class);
        task.execute(getBase64Encode(config, head, data, false));
    }
    /**
     * 上传证件
     * @param protocol
     * @param activity
     *
     */
    public void uploadCertification2(ProcotolCallBack protocol, BaseProtocolActivity activity, String sfz1,
                                    String sfz2, String carHeadimg,String jsz, String xsz) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/driver", "addcertificate.action","1");
        Map<String, String> data = new HashMap<>();
        data.put("sfz1", sfz1);
        data.put("sfz2", sfz2);
        data.put("carHeadimg",carHeadimg);
        data.put("jsz", jsz);
        data.put("xsz", xsz);
        RequestConfig config = getBaseConfig(RQ_UPLOAD_CERTIFICATION);
        config.setCls(String.class);
        task.execute(getBase64Encode(config, head, data, false));
    }
    //上传etc认证信息
    public void audit(ProcotolCallBack protocol, BaseProtocolActivity activity, String sfz1,
                      String sfz2, String carHeadimg,String jsz, String xsz,String carId){
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/driver", "audit.action","1");
        Map<String, String> data = new HashMap<>();
        data.put("sfz1", sfz1);
        data.put("sfz2", sfz2);
        data.put("carHeadimg",carHeadimg);
        data.put("jsz", jsz);
        data.put("xsz", xsz);
        data.put("carId",carId);
        RequestConfig config = getBaseConfig(RQ_AUDIT);
        config.setCls(String.class);
        task.execute(getBase64Encode(config, head, data, false));
    }


    /**
     * 用户反馈
     * @param protocol
     * @param activity
     *
     */
    public void feedback(ProcotolCallBack protocol, BaseProtocolActivity activity, String content) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/driver", "feedback.action","1");
        Map<String, String> data = new HashMap<>();
        data.put("content", content);
        RequestConfig config = getBaseConfig(RQ_FEEDBACK);
        config.setCls(String.class);
        task.execute(getBase64Encode(config, head, data, isCode));
    }

    /**
     * 话费充值
     * @param protocol
     * @param activity
     *
     */
    public void getCharge(ProcotolCallBack protocol, Context activity, String amount, String paytype,
                          String orderid, String carid,String cztype,String phonenumber,String faceprice,String paychannel,String thirdPrice,String data1,String datatype) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/pay", "getcharge.action","1");
        Map<String, String> data = new HashMap<>();
        data.put("amount", amount);
        data.put("paytype", paytype);
        data.put("orderid", orderid);
        data.put("carid", carid);

        data.put("czType", cztype);
        data.put("phoneNumber", phonenumber);
        data.put("facePrice", faceprice);
        data.put("payChannel", paychannel);
        data.put("thirdPrice", thirdPrice);
        data.put("data", data1);
        data.put("dataType", datatype);
        RequestConfig config = getBaseConfig(RQ_GET_CHARGE);
        config.setCls(String.class);
        task.execute(getBase64Encode(config, head, data, isCode));
    }
    /**
     * etc充值
     * @param protocol
     * @param activity
     *
     */
    public void getEtcCharge(ProcotolCallBack protocol, Context activity, String paytype, String amount,
                          String orderid, String carid,String cztype,String phonenumber,String faceprice,String paychannel,String thirdPrice,String data1,String datatype,String carNumber,String etcNumber) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/qundui/pay", "getchargeV2.action","1");
        Map<String, String> data = new HashMap<>();
        data.put("amount", amount);
        data.put("paytype", paytype);
        data.put("orderid", orderid);
        data.put("carid", carid);

        data.put("czType", cztype);
        data.put("phoneNumber", phonenumber);
        data.put("facePrice", faceprice);
        data.put("payChannel", paychannel);
        data.put("thirdPrice", thirdPrice);
        data.put("data", data1);
        data.put("dataType", datatype);

        data.put("carNumber",carNumber);
        data.put("etcNumber",etcNumber);
        RequestConfig config = getBaseConfig(RQ_GET_ETCCHARE);
        config.setCls(String.class);
        task.execute(getBase64Encode(config, head, data, isCode));
    }

    /**
     * 获取省
     * @param protocol
     * @param activity
     *
     */
    public void getProvinces(ProcotolCallBack protocol, Context activity) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/area", "provice.action","1");
        Map<String, String> data = new HashMap<>();
        RequestConfig config = getBaseConfig(RQ_GET_PROVINCE_LIST);
        config.setElement(ProvinceModel.class);
        task.execute(getBase64Encode(config, head, data, false));
    }

    /**
     * 获取市
     * @param protocol
     * @param activity
     *
     */
    public void getCities(ProcotolCallBack protocol, Context activity, String parentcode) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/area", "city.action","1");
        Map<String, String> data = new HashMap<>();
        data.put("parentcode",parentcode);
        RequestConfig config = getBaseConfig(RQ_GET_CITY_LIST);
        config.setElement(CityModel.class);
        task.execute(getBase64Encode(config, head, data, false));
    }

    /**
     * 获取区
     * @param protocol
     * @param activity
     *
     */
    public void getAreas(ProcotolCallBack protocol, Context activity, String parentcode) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/area", "area.action","1");
        Map<String, String> data = new HashMap<>();
        data.put("parentcode",parentcode);
        RequestConfig config = getBaseConfig(RQ_GET_AREA_LIST);
        config.setElement(AreaModel.class);
        task.execute(getBase64Encode(config, head, data, false));
    }

    /**
     * 获取所有市
     * @param protocol
     * @param activity
     *
     */
    public void getAllCities(ProcotolCallBack protocol, BaseProtocolActivity activity) {
        BaseAsyncTask task = new BaseAsyncTask(protocol);
        Map<String, String> head = getBaseData(activity, "/http/area", "allcity.action","1");
        Map<String, String> data = new HashMap<>();
        RequestConfig config = getBaseConfig(RQ_GET_ALL_CITY_LIST);
        config.setElement(CityModel.class);
        task.execute(getBase64Encode(config, head, data, false));
    }

    /**
     * 上传文件
     */
    public void upload(ProcotolCallBack protocol, Context activty, String filepath) {
        UploadAsyncTask task = new UploadAsyncTask(protocol);
        RequestConfig config = getBaseConfig(RQ_UPLOAD);
        config.setWebAddress(ProjectConfig.UPLOAD_URL);
        HashMap<String, String> file = new HashMap<>();
        file.put("file", filepath);
        HashMap<String, String> header = new HashMap<>();
        header.put("username","qundui");
        header.put("userkey","1qazxsw2");
        header.put("noncestr","noncestr");
        header.put("sign","AD76410A307C429985F0B666E554B46C");
        config.setFiles(file);
        config.setHeader(header);
        config.setElement(UploadModel.class);
        task.execute(config);
    }



    private RequestConfig getBase64Encode(RequestConfig config,
                                          Map<String, String> head, Map<String, String> data, boolean isCode) {
        Log.d("small", "head+" + head.toString());
        Log.d("small", "data+" + data.toString());
        if (isCode) {
            JSONObject jsonObject = new JSONObject();
            JSONObject headers = new JSONObject(head);
            JSONObject body = new JSONObject(data);
            String encodeString = null;
            try {
                jsonObject.put("header", headers);
                jsonObject.put("body", body);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String str = jsonObject.toString();
            try {
                byte[] datas = AESUtil.encrypt(str.getBytes());
                encodeString = AESUtil.base64Encode(datas);
            } catch (Exception e) {
                e.printStackTrace();
            }
//        Log.d("small", "\n" + encodeString);
            config.setRequestdata(encodeString);
            HashMap<String, String> map = new HashMap<>();
            map.put("Content-type", "application/wocai");
            config.setHeader(map);
        } else {
            config.setData(data);
            config.setHeader(head);
        }
        return config;
    }
}