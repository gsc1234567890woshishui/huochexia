package com.zjwocai.qundui.service;

//Android使用Service结合高德定位SDK实现定位，有需要的朋友可以参考下。
//Android中使用Service结合高德定位SDK实现定位
//高德官方给出的定位实例都是基于Activity的，但是实际过程中会有需要不断定位的情况，特别是需要后台统计用户数据的。如果在Activity中绑定定位服务，那么定位就受Activity的生命周期限制，显然用Service实习定位是个不错的选择。然后结合BroadcastRecevier可以实现当数据需要更新的时候即使提醒提醒更新数据。
//高德定位结合Service代码实例

        import android.app.Service;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.location.Location;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.IBinder;
        import android.text.TextUtils;
        import android.util.Log;


        import com.amap.api.location.AMapLocation;
        import com.amap.api.location.AMapLocationClient;
        import com.amap.api.location.AMapLocationClientOption;
        import com.amap.api.location.AMapLocationListener;
        import com.threeti.teamlibrary.activity.BaseProtocolActivity;
        import com.threeti.teamlibrary.finals.RequestCodeSet;
        import com.threeti.teamlibrary.net.BaseModel;
        import com.threeti.teamlibrary.net.ProcotolCallBack;

        import com.zjwocai.qundui.bill.ProtocolBill;
        import com.zjwocai.qundui.fragment.Constant;
        import com.zjwocai.qundui.fragment.EventMessage;
        import com.zjwocai.qundui.util.ToastUtil;

        import org.greenrobot.eventbus.EventBus;
        import org.greenrobot.eventbus.Subscribe;
        import org.greenrobot.eventbus.ThreadMode;

//
/**
 * Created by qundui on 2016/12/29.
 */

public class LocationService2 extends Service implements ProcotolCallBack ,RequestCodeSet {

    private AMapLocationClientOption locationOption = new AMapLocationClientOption();
    private AMapLocationClient locationClient = null;
    private String id = "";
    private  double longitude,latitude;


    public LocationService2() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);

        //使用参数为Context的方法，Service也是Context实例，
        //是四大组件之一
        // 定位方式设置为混合定位，包括网络定位和GPS定位
//        locationManagerProxy.requestLocationData(
//                LocationProviderProxy.AMapNetwork, LOCATION_UPDATE_MIN_TIME,
//                LOCATION_UPDATE_MIN_DISTANCE, this);
//        // 如果定位方式包括GPS定位需要手动设置GPS可用
//        locationManagerProxy.setGpsEnable(true);
//
//        Log.v("locationservice", "locationservicestart");
        initLocation();
        startLocation(180000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        id = intent.getStringExtra("id");

        return START_REDELIVER_INTENT;
    }

    /**
     * 初始化定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void initLocation(){
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }
    /**
     * 默认的定位参数
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(1000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        //mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        //mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(false); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }
    // 根据控件的选择，重新设置定位参数
    private void resetOption(int sec) {
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(false);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption.setGpsFirst(false);
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(true);
        // 设置是否单次定位
        locationOption.setOnceLocation(false);
        //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
        //locationOption.setOnceLocationLatest(cbOnceLastest.isChecked());
        //设置是否使用传感器
        //locationOption.setSensorEnable(true);
        //设置是否开启wifi扫描，如果设置为false时同时会停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        // locationOption.setWifiScan(true);
        //String strInterval = etInterval.getText().toString();
        //if (!TextUtils.isEmpty(strInterval)) {
           // try{
                // 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
                locationOption.setInterval(sec);//180000
                locationOption.setHttpTimeOut(10000);
            //}catch(Throwable e){
                //e.printStackTrace();
           // }


//        String strTimeout = etHttpTimeout.getText().toString();
//        if(!TextUtils.isEmpty(strTimeout)){
//            try{
//                // 设置网络请求超时时间
//            }catch(Throwable e){
//                e.printStackTrace();
//            }
        }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            if (null != loc) {
                if(loc.getLongitude()!=0.0 && loc.getLatitude() != 0.0){
                    //执行异步任务网络请求
                    executeNetListen(loc);

                }else{
                    //ToastUtil.toastShortShow(getApplicationContext(),"定位失败!");
                }
            } else {
                //ToastUtil.toastShortShow(getApplicationContext(),"定位失败!");
            }
        }
    };
    private void executeNetListen(final AMapLocation loc) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                requestServer(loc);//请求服务器，提交定位数据
                 longitude =  loc.getLongitude();
                 latitude = loc.getLatitude();

            }
        }, 7000);
    }

    //请求服务器，提交定位数据
    private void requestServer(AMapLocation loc) {
        if(id!=null && !id.isEmpty()){

            ProtocolBill.getInstance().track(this,this,id,String.valueOf(loc.getLongitude()),String.valueOf(loc.getLatitude()),"2");
             System.out.println("idididididididididididiidididi"+id);

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setGoIndex(EventMessage eventMessage){
        //Log.d(TAG, "setGoIndex: "+eventMessage.getTag());
        if(eventMessage!=null){
            int tag = eventMessage.getTag();
            if(tag== EventMessage.EventMessageAction.TAG_SEEVICE_LOCATION){

                Log.v("ggggggggggggggg","后台service接到命令");
                //上传轨迹
                ProtocolBill.getInstance().track(new ProcotolCallBack() {
                    @Override
                    public void onTaskSuccess(BaseModel result) {
                        //上传轨迹成功过后发消息结束定位
                        goSelect(EventMessage.EventMessageAction.TAG_STOP_LOCATION);
                        Log.v("ggggggggggggggg","后台service最后上传定位信息成功"+longitude+"=="+latitude);
                        Log.v("ggggggggggggggg","service发送消息给主页确认收货");
                        stopSelf();
                        EventBus.getDefault().unregister(this);
                    }

                    @Override
                    public void onTaskFail(BaseModel result) {

                    }

                    @Override
                    public void onTaskFinished(String resuestCode) {

                    }
                }, getApplicationContext(), id, String.valueOf(longitude), String.valueOf(latitude), "2");

            }
            if(tag== EventMessage.EventMessageAction.TAG_SEEVICE_LOCATION_ORDER){

                Log.v("ggggggggggggggg","后台service接到命令");
                //上传轨迹
                ProtocolBill.getInstance().track(new ProcotolCallBack() {
                    @Override
                    public void onTaskSuccess(BaseModel result) {
                        //上传轨迹成功过后发消息结束定位
                        goSelect(EventMessage.EventMessageAction.TAG_STOP_LOCATION_ORDER);
                        Log.v("ggggggggggggggg","后台service最后上传定位信息成功"+longitude+"=="+latitude);
                        Log.v("ggggggggggggggg","service发送消息给订单页面确认收货");
                        stopSelf();
                        EventBus.getDefault().unregister(this);
                    }

                    @Override
                    public void onTaskFail(BaseModel result) {

                    }

                    @Override
                    public void onTaskFinished(String resuestCode) {

                    }
                }, getApplicationContext(), id, String.valueOf(longitude), String.valueOf(latitude), "2");

            }
            if(tag== EventMessage.EventMessageAction.TAG_SEEVICE_LOCATION_LISTFRAGMENT){

                Log.v("ggggggggggggggg","后台service接到命令");
                //上传轨迹
                ProtocolBill.getInstance().track(new ProcotolCallBack() {
                    @Override
                    public void onTaskSuccess(BaseModel result) {
                        //上传轨迹成功过后发消息结束定位
                        goSelect(EventMessage.EventMessageAction.TAG_STOP_LOCATION_LISTFRAGMENT);
                        Log.v("ggggggggggggggg","后台service最后上传定位信息成功"+longitude+"=="+latitude);
                        Log.v("ggggggggggggggg","service发送消息给订单页面确认收货");
                        stopSelf();
                        EventBus.getDefault().unregister(this);
                    }

                    @Override
                    public void onTaskFail(BaseModel result) {

                    }

                    @Override
                    public void onTaskFinished(String resuestCode) {

                    }
                }, getApplicationContext(), id, String.valueOf(longitude), String.valueOf(latitude), "2");

            }

        }
    }


    /**
     * 开始定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void startLocation(int sec){
        //根据控件的选择，重新设置定位参数
        resetOption(sec);
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }
    /**
     * 停止定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void stopLocation(){
        // 停止定位
        locationClient.stopLocation();

    }

    /**
     * 销毁定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void destroyLocation(){
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    //服务销毁的时候执行
    @SuppressWarnings("deprecation")
    @Override
    public void onDestroy() {
        super.onDestroy();

        destroyLocation();
        EventBus.getDefault().unregister(this);


    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskSuccess(BaseModel result) {


    }

    @Override
    public void onTaskFail(BaseModel result) {

    }

    @Override
    public void onTaskFinished(String resuestCode) {

    }

    private void goSelect(int tag) {
        EventMessage eventMessage = new EventMessage();
        eventMessage.setTag(tag);
        EventBus.getDefault().post(eventMessage);

    }

}
