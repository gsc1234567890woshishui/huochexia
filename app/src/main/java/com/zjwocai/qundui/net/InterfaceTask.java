package com.zjwocai.qundui.net;

import android.util.Log;

import com.threeti.teamlibrary.activity.BaseProtocolActivity;
import com.threeti.teamlibrary.finals.ProjectConfig;
import com.threeti.teamlibrary.finals.ProjectConstant;
import com.threeti.teamlibrary.net.BaseHttpRequest;
import com.threeti.teamlibrary.net.RequestConfig;
import com.threeti.teamlibrary.utils.AESUtil;
import com.threeti.teamlibrary.utils.DeviceUtil;
import com.threeti.teamlibrary.utils.MD5Util;
import com.threeti.teamlibrary.utils.SPUtil;
import com.zjwocai.qundui.finals.OtherFinals;
import com.zjwocai.qundui.model.UserObj;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by admin on 2016/7/18.
 */
public class InterfaceTask implements ProjectConfig {
    private static InterfaceTask protocolBill;

    public static InterfaceTask getInstance() {
        if (protocolBill == null) {
            protocolBill = new InterfaceTask();
        }
        return protocolBill;
    }

    /**
     * 简便获取本地的用户数据
     *
     * @return
     */
    public UserObj getUserData() {
        return (UserObj) SPUtil.getObjectFromShare(ProjectConstant.KEY_USERINFO);
    }
    /**
     * 得到基本请求配置参数
     *
     * @param requestcode
     * @return
     */
    private RequestConfig getBaseConfig(String requestcode) {
        RequestConfig config = new RequestConfig();
        config.setRequestCode(requestcode);
        config.setMethod(BaseHttpRequest.POST);
        config.setWebAddress(HEAD_URL);
        return config;
    }
    private HashMap<String, String> getHeadersData(BaseProtocolActivity context, String service, String action, String page) {
        HashMap<String, String> map = new HashMap<>();
        String appcode = "3E_AND_PHO";
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
        map.put("pagesize", OtherFinals.PAGE_SIZE);
        String timestamp = System.currentTimeMillis() + "";
        String noncestr = MD5Util.getRandomForLen(6);
        map.put("sign", MD5Util.getSign(noncestr, timestamp, appcode, service, action));
        map.put("noncestr", noncestr);
        map.put("timestamp", timestamp);
        String visonname = "1.0";
        visonname = DeviceUtil.getVersion(context);
        map.put("appversion", visonname);
        if (getUserData() != null)
            map.put("token", getUserData().getToken());
        return map;
    }

    private RequestConfig getBase64Encode(RequestConfig config,
                                          HashMap<String, String> head, HashMap<String, String> data) {
        JSONObject jsonObject = new JSONObject();
        JSONObject headers = new JSONObject(head);
        JSONObject body = new JSONObject(data);
        String encodeString = null;
        Log.d("small","headers="+headers.toString());
        Log.d("small","body="+body.toString());
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
        config.setRequestdata(encodeString);
        HashMap<String, String> map = new HashMap<>();
        map.put("Content-type", "application/wocai");
        config.setHeader(map);
        return config;
    }

    private RequestConfig getBase64Encode(RequestConfig config,
                                          HashMap<String, String> head, String data) {
        JSONObject jsonObject = new JSONObject();
        JSONObject headers = new JSONObject(head);
//        JSONObject body = new JSONObject(data);
        String encodeString = null;
        JSONObject body = null;
        try {
            body = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        config.setRequestdata(encodeString);
        HashMap<String, String> map = new HashMap<>();
        map.put("Content-type", "application/wocai");
        config.setHeader(map);
        return config;
    }

    //    /**
//     * 1.1登录
//     *
//     * @param act
//     */
//    public void Login(ProcotolCallBack protocol, BaseProtocolActivity act, String userid, String password, String usertype) {
//
//        BaseTwoAsyncTask task = new BaseTwoAsyncTask(protocol, act, act.getResources().getString(R.string.net_request));
//        HashMap<String, String> params = getHeadersData(act, "/http/3e/auth", "login/v1", "1");
//        HashMap<String, String> data = new HashMap<>();
//        data.put("userid", userid);
//        data.put("password", password);
//        data.put("usertype", usertype);
//        RequestConfig config = getBaseConfig(InterfaceFinals.LOGIN);
//        config.setCls(UserObj.class);
//        task.execute(getBase64Encode(config, params, data));
//    }

//    /**
//     * 2.1获取推荐课程列表
//     */
//    public void getRecommendedCourseJob(ProcotolCallBack protocol, BaseProtocolActivity activty, String page, String title, boolean isShow) {
//        BaseTwoAsyncTask task;
//        if (isShow)
//            task = new BaseTwoAsyncTask(protocol, activty,
//                    activty.getResources().getString(R.string.net_request));
//        else
//            task = new BaseTwoAsyncTask(protocol);
//        HashMap<String, String> params = getHeadersData(activty, "/http/3e/achivement", "recommendedcourselist/v1", page);
//        HashMap<String, String> data = new HashMap<>();
//        data.put("title", title);
//        RequestConfig config = getBaseConfig(InterfaceFinals.GET_RECOMMENDED_LIST);
//        config.setElement(StudyObj.class);
//        if ("1".equals(page)) {
//            config.setIsRefresh(true);
//        } else {
//            config.setIsRefresh(false);
//        }
//        task.execute(getBase64Encode(config, params, data));
//    }


}
