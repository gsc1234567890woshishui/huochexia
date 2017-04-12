package com.zjwocai.qundui.activity.nav;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviStaticInfo;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.autonavi.tbt.NaviStaticInfo;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.util.DateUtil;
import com.zjwocai.qundui.util.DialogUtil;
import com.zjwocai.qundui.util.ToastUtil;
import com.zjwocai.qundui.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class RestRouteShowActivity extends Activity implements TextWatcher,OnClickListener, AMapNaviListener, OnGeocodeSearchListener,CompoundButton.OnCheckedChangeListener {

//    private AMapLocationClient mlocationClient;
//    private OnLocationChangedListener mListener;
//    private AMapLocationClientOption mLocationOption;

    private ProgressDialog progDialog = null;
    private GeocodeSearch geocoderSearch;
    private String addressName;
    private AMap aMap;
    private MapView mapView;
    private Marker geoMarker;

    private boolean congestion, cost, hightspeed, avoidhightspeed;
    /**
     * 导航对象(单例)
     */
    private AMapNavi mAMapNavi;
    private AMap mAmap;
    /**
     * 地图对象
     */
    private MapView mRouteMapView;
    private Marker mStartMarker;
    private Marker mEndMarker;
    /**
     * 选择起点Action标志位
     */
    //private boolean mapClickStartReady;
    /**
     * 选择终点Aciton标志位
     */
    //private boolean mapClickEndReady;
    private NaviLatLng endLatlng = new NaviLatLng(39.955846, 116.352765);
    private NaviLatLng startLatlng = new NaviLatLng(120.13, 30.27);
    private List<NaviLatLng> startList = new ArrayList<NaviLatLng>();
    /**
     * 途径点坐标集合
     */
    private List<NaviLatLng> wayList = new ArrayList<NaviLatLng>();
    /**
     * 终点坐标集合［建议就一个终点］
     */
    private List<NaviLatLng> endList = new ArrayList<NaviLatLng>();

    /**
     * 保存当前算好的路线
     */
    private SparseArray<RouteOverLay> routeOverlays = new SparseArray<RouteOverLay>();

    /**
     * 路线的权值，重合路线情况下，权值高的路线会覆盖权值低的路线
     **/
    private int zindex = 1;
    /**
     * 路线计算成功标志位
     */
    private boolean calculateSuccess = false;
    //private boolean chooseRouteSuccess = false;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = new AMapLocationClientOption();
    private String keyWord;
    private String city;
    private PoiSearch poiSearch;
    private String startKeyword;
    private String startCity;

    private Button btn1;
    private Button btn2;
    private Button btn3;
    private LinearLayout select1;
    private LinearLayout select2;
    private LinearLayout select3;
    private TextView shortTime;
    private TextView shortDistance;
    private TextView two_time;
    private TextView two_distance;

    private boolean isLocation;
    /**
     * 定位监听 得到经纬度 TODO 定位完成
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            if (null != loc) {
                //mapClickStartReady = false;
                //得到起始坐标的经纬度
                double locateLat = loc.getLatitude();
                double locateLng = loc.getLongitude();
                //确定起点
                startLatlng = new NaviLatLng(locateLat, locateLng);
                startList.clear();
                startList.add(startLatlng);
                //tvResult.setText(result);
                stopLocation();
                if (isLocation) {
                    isLocation = false;
                    mAmap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(locateLat, locateLng)));
                } else {
                    //开始算路
                    startCalculate();
                }

            } else {
                // tvResult.setText("定位失败，loc is null");
                Toast.makeText(RestRouteShowActivity.this, "定位失败.......", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private LatLonPoint lp = new LatLonPoint(39.993743, 116.472995);
    //初始化搜索布局
    private LinearLayout rlDis;
    private LinearLayout llDis;
    private Button btnSearch;
    private EditText editText;
    private LinearLayout line111;
    private TextView three_time;
    private TextView three_distance;
    private ImageView iv_location;
    private RelativeLayout ivCancle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rest_calculate);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        //找到布局上的几个button按钮  初始化控件
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        shortTime = (TextView) findViewById(R.id.short_time);
        iv_location = (ImageView) findViewById(R.id.iv_location);
        shortDistance = (TextView) findViewById(R.id.short_distance);
        two_time = (TextView) findViewById(R.id.two_time);
        two_distance = (TextView) findViewById(R.id.two_distance);
        three_time = (TextView) findViewById(R.id.three_time);
        three_distance = (TextView) findViewById(R.id.three_distance);
        ivCancle = (RelativeLayout) findViewById(R.id.iv_cancle);
        Button btnNai = (Button) findViewById(R.id.btn_nai);
        select1 = (LinearLayout) findViewById(R.id.select1);
        select2 = (LinearLayout) findViewById(R.id.select2);
        select3 = (LinearLayout) findViewById(R.id.select3);
        ivCancle.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btnNai.setOnClickListener(this);
        select1.setOnClickListener(this);
        select2.setOnClickListener(this);
        select3.setOnClickListener(this);
        iv_location.setOnClickListener(this);


        //搜索部分的初始化数据
        rlDis = (LinearLayout) findViewById(R.id.rl_dis);
        llDis = (LinearLayout) findViewById(R.id.ll_dis);
        btnSearch = (Button) findViewById(R.id.btn_search);
        editText = (EditText) findViewById(R.id.edit_Text);
        line111 = (LinearLayout) findViewById(R.id.line111);
        rlDis.setOnClickListener(this);
        llDis.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        editText.setOnClickListener(this);

        //设置输入框监听
        editText.addTextChangedListener(this);



        mRouteMapView = (MapView) findViewById(R.id.navi_view);
        mRouteMapView.onCreate(savedInstanceState);
        mAmap = mRouteMapView.getMap();
        if(mAmap==null){
            mAmap = mapView.getMap();
            geoMarker = mAmap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        progDialog = new ProgressDialog(this);

        //初始化定位
        initLocation();
        //获取终点的经纬度
        Map map = (Map) getIntent().getSerializableExtra("data");
        //卸货地地址信息
        keyWord = (String) map.get("keyword");
        city = (String) map.get("city");

        //提货地地址信息
        startKeyword = (String) map.get("startKeyword");
        startCity = (String) map.get("startCity");


        if (keyWord == null || city == null || keyWord.equals("") || keyWord.equals("")) {
            btn3.setPressed(true);
            ToastUtil.toastShortShow("无法定位目标地址，导航失败！！");
            btn3.performClick();
            //重新家在页面，选中其他
            // return;
        }
        if (startKeyword == null || startCity == null || startKeyword.equals("") || startCity.equals("")) {
            ToastUtil.toastShortShow("无法定位目标地址，导航失败！！");
            btn3.performClick();
            //return;
        }
        //初始化Marker添加到地图
        mStartMarker = mAmap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.start))));
        mEndMarker = mAmap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.end))));
        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(this);


        //mRouteMapView.setAMapNaviViewListener(this);
        //开启定位
        startLocation();

        //检索目的地位置
        // doSearchQuery(keyWord, city);
        btn1.performClick();
    }
    /**
     * 响应地理编码
     */
    public void getLatlon(String s1,String s2) {
        //弹窗提示加载
        DialogUtil.showWaitDialog(this, "加载中...");
        final TimerTask task = new TimerTask(){
            public void run(){
                DialogUtil.hideWaitDialog();
                //ToastUtil.toastShortShow("网络错误，导航失败！");

            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 5000);


        //showDialog();

        GeocodeQuery query = new GeocodeQuery(s1, s2);// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
        geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
    }



    protected void doSearchQuery(String s1, String s2) {
        //弹窗提示加载
        DialogUtil.showWaitDialog(this, "加载中...");
        final TimerTask task = new TimerTask(){
            public void run(){
                DialogUtil.hideWaitDialog();
                //ToastUtil.toastShortShow("网络错误，导航失败！");

            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 5000);



        PoiSearch.Query query = new PoiSearch.Query(s1, null, s2);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(10); // 设置每页最多返回多少条poiitem
        query.setPageNum(0);// 设置查第一页

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                if (poiResult.getPois() != null && !poiResult.getPois().isEmpty()) {
                    double latitude = poiResult.getPois().get(0).getLatLonPoint().getLatitude();
                    task.cancel();
                    double longitude = poiResult.getPois().get(0).getLatLonPoint().getLongitude();
                    endLatlng = new NaviLatLng(latitude, longitude);
                    endList.clear();
                    endList.add(endLatlng);
                    //开始算路
                    startCalculate();
                } else {
                    //check();
                    ToastUtil.toastShortShow("定位地址不存在，导航失败！");
                    DialogUtil.hideWaitDialog();

                    btn3.performClick();
                    //finish();
                }
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });

        poiSearch.searchPOIAsyn();
    }

    /**
     * 开始定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void startLocation() {
        //根据控件的选择，重新设置定位参数
        //resetOption();
        // 设置定位参数
        System.out.println(locationOption + "9999999999999999999999999999999999999999999999999999999999");
        locationClient.setLocationOption(locationOption);

        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 停止定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void stopLocation() {
        // 停止定位
        locationClient.stopLocation();
    }

    /**
     * 销毁定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void destroyLocation() {
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

    /**
     * 初始化定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        //mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        //AMapLocationClientOption.setLocationProtocol(AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        //mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        return mOption;
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mRouteMapView.onResume();

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mRouteMapView.onPause();
        //startList.clear();
        //wayList.clear();
        //endList.clear();
        //routeOverlays.clear();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mRouteMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRouteMapView.onDestroy();
        /**
         * 当前页面只是展示地图，activity销毁后不需要再回调导航的状态
         */
        mAMapNavi.removeAMapNaviListener(this);
        //注意：不要调用这个destory方法，因为在当前页面进行算路，算路成功的数据全部存在此对象中。到另外一个activity中只需要开始导航即可。
        //如果用户是回退退出当前activity，可以调用下面的destory方法。
        // mAMapNavi.destroy();
        //停止定位
        stopLocation();
       // destroyLocation();


    }

    /*
   * 计算路径失败
   *
   * */
    @Override
    public void onCalculateRouteFailure(int arg0) {
        calculateSuccess = false;
        Toast.makeText(getApplicationContext(), "计算路线失败", Toast.LENGTH_SHORT).show();
        DialogUtil.hideWaitDialog();
    }

    private void drawRoutes(int routeId, AMapNaviPath path) {
        calculateSuccess = true;
        RouteOverLay routeOverLay = new RouteOverLay(mAmap, path, this);
        routeOverLay.setTrafficLine(true);
        routeOverLay.addToMap();
        routeOverlays.put(routeId, routeOverLay);
        routeOverLay.zoomToSpan();
    }


    /**
     * 清除当前地图上算好的路线
     */
    private void clearRoute() {

        for (int i = 0; i < routeOverlays.size(); i++) {
            RouteOverLay routeOverlay = routeOverlays.valueAt(i);
            routeOverlay.removeFromMap();
        }
        routeOverlays.clear();

    }

    /*
    * 开始算路的方法实现
    *
    * */
    private void startCalculate() {

//        if (PathPlanningStrategy.DRIVING_DEFAULT >= 0) {
//            mAMapNavi.calculateDriveRoute(startList, endList, wayList,PathPlanningStrategy.DRIVING_DEFAULT);
//            Toast.makeText(getApplicationContext(), "策略试试试试试试:" + PathPlanningStrategy.DRIVING_DEFAULT, Toast.LENGTH_LONG).show();
//        }

        int strategyFlag = 0;
        try {
            strategyFlag = mAMapNavi.strategyConvert(false, false, false, false, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (strategyFlag >= 0 && !startList.isEmpty() && !endList.isEmpty()) {
            mAMapNavi.calculateDriveRoute(startList, endList, wayList, strategyFlag);
            //Toast.makeText(getApplicationContext(), "策略:" + strategyFlag, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancle:
                //返回上一页
                finish();
                break;
            case R.id.btn1:
                //隐藏小搜索布局
                rlDis.setVisibility(View.GONE);
                //然后清除地图上的路线
                clearRoute();

                getLatlon(keyWord, city);
                // mapClickEndReady = true;
                line111.setVisibility(View.VISIBLE);
                endList.clear();
                break;
            case R.id.btn2:
                rlDis.setVisibility(View.GONE);
                //然后清除地图上的路线
                clearRoute();
                //startLocation();
                getLatlon(startKeyword, startCity);
                //mapClickEndReady = true;
                line111.setVisibility(View.VISIBLE);
                //清空终点集合
                endList.clear();
                break;
            case R.id.btn3:
                //选择的终点是其他 手动输入一个地址 然后检索  规划路线
                //显示gone的搜索布局
                rlDis.setVisibility(View.VISIBLE);
                //设置路线选择隐藏
                line111.setVisibility(View.GONE);

                break;
            case R.id.btn_search:
                //得到editText的数据 开始搜索
                if (!TextUtils.isEmpty(editText.getText().toString().trim())){
                    String result = editText.getText().toString().trim();
                    //进行检索地图
                    clearRoute();
                    getLatlon(result, "");
                    line111.setVisibility(View.VISIBLE);
                    endList.clear();
                } else {
                    ToastUtil.toastShortShow("输入内容不能为空");
                }
                break;
            case R.id.select1:
                setTextColor(select1, true);
                setTextColor(select2, false);
                setTextColor(select3, false);
                changeRoute(0);
                break;

            case R.id.select2:
                setTextColor(select1, false);
                setTextColor(select2, true);
                setTextColor(select3, false);
                changeRoute(1);
                break;
            case R.id.select3:
                setTextColor(select1, false);
                setTextColor(select2, false);
                setTextColor(select3, true);
                changeRoute(2);

                break;
            case R.id.btn_nai:
                //点击开始导航
                isLocation = true;
                //startLocation();
                Intent gpsintent = new Intent(getApplicationContext(), RouteNaviActivity.class);
                gpsintent.putExtra("gps", true);
                startActivity(gpsintent);
                this.finish();


                break;
            case R.id.iv_location:
                //定位 TODO
                isLocation = true;
                startLocation();

                break;
        }

    }


    private void setTextColor(ViewGroup viewGroup, boolean isSelect) {
        if (viewGroup == null) {
            return;
        }
        viewGroup.setSelected(isSelect);
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof TextView) { // 若是Button记录下
                TextView newDtv = (TextView) view;
                newDtv.setSelected(isSelect);
            }
        }
    }

    /*
    *
    * 路线偏好设置
    *
    * */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onArriveDestination(NaviStaticInfo naviStaticInfo) {

    }

    @Override
    public void onArriveDestination(AMapNaviStaticInfo aMapNaviStaticInfo) {

    }


    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void hideLaneInfo() {

    }


    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    //TODO  单条路线计算成功
    @Override
    public void onCalculateRouteSuccess() {
    }

    //TODO  路线多条路线成功
    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {
        //清空上次计算的路径列表。
        routeOverlays.clear();
        HashMap<Integer, AMapNaviPath> paths = mAMapNavi.getNaviPaths();
        ArrayList<AMapNaviPath> aMapNaviPaths = new ArrayList<>();
        for (int i = 0; i < ints.length; i++) {
            AMapNaviPath path = paths.get(ints[i]);
            aMapNaviPaths.add(path);
        }
        Collections.sort(aMapNaviPaths, new Comparator<AMapNaviPath>() {
            @Override
            public int compare(AMapNaviPath aMapNaviPath, AMapNaviPath t1) {
                return aMapNaviPath.getAllTime() - t1.getAllTime();
            }
        });

        for (int i = 0; i < aMapNaviPaths.size(); i++) {
            AMapNaviPath path = aMapNaviPaths.get(i);
            switch (i) {
                case 0:
                    shortTime.setText(DateUtil.formatTime(path.getAllTime()));
                    shortDistance.setText(Utils.formatDis(path.getAllLength()));
                    break;
                case 1:
                    two_time.setText(DateUtil.formatTime(path.getAllTime()));
                    two_distance.setText(Utils.formatDis(path.getAllLength()));
                    break;
                case 2:
                    three_time.setText(DateUtil.formatTime(path.getAllTime()));
                    three_distance.setText(Utils.formatDis(path.getAllLength()));
                    break;
            }
            select2.setVisibility(ints.length > 1 ? View.VISIBLE : View.GONE);
            select3.setVisibility(ints.length > 2 ? View.VISIBLE : View.GONE);

            if (path != null) {
                drawRoutes(ints[i], path);
            }
        }
        if (aMapNaviPaths.size() > 0)
            select1.performClick();
        //隐藏dialogog
        DialogUtil.hideWaitDialog();
    }

    public void changeRoute(int index) {
        if (!calculateSuccess) {
            Toast.makeText(this, "请先算路", Toast.LENGTH_SHORT).show();
            return;
        }
        /**
         * 计算出来的路径只有一条
         */
        if (routeOverlays.size() == 1) {
            //chooseRouteSuccess = true;
//            Toast.makeText(this, "导航距离:" + (mAMapNavi.getNaviPath()).getAllLength() + "m" + "\n" + "导航时间:" + (mAMapNavi.getNaviPath()).getAllTime() + "s", Toast.LENGTH_SHORT).show();
            return;
        }


        int routeID = routeOverlays.keyAt(index);
        //突出选择的那条路
        for (int i = 0; i < routeOverlays.size(); i++) {
            int key = routeOverlays.keyAt(i);
            routeOverlays.get(key).setTransparency(0.7f);

        }
        System.out.println("rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr"+routeID);
        routeOverlays.get(routeID).setTransparency(1);
        /**把用户选择的那条路的权值弄高，使路线高亮显示的同时，重合路段不会变的透明**/
        routeOverlays.get(routeID).setZindex(zindex++);
        //必须告诉AMapNavi 你最后选择的哪条路
        mAMapNavi.selectRouteId(routeID);
//        Toast.makeText(this, "导航距离:" + (mAMapNavi.getNaviPaths()).get(routeID).getStrategy() + "\n" + "导航时间:" + (mAMapNavi.getNaviPaths()).get(routeID).getAllTime() + "s", Toast.LENGTH_SHORT).show();


        // chooseRouteSuccess = true;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        if (TextUtils.isEmpty(charSequence)){
            llDis.setVisibility(View.VISIBLE);
        } else {
            llDis.setVisibility(View.GONE);
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }
    //
    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        //DialogUtil.hideWaitDialog();
        //ToastUtil.toastShortShow(this,"出现错错错错醋藕藕错错错错崔凑凑粗凑");

        if (i == 1000) {

            if (geocodeResult != null && geocodeResult.getGeocodeAddressList() != null
                    && geocodeResult.getGeocodeAddressList().size() > 0) {

                GeocodeAddress address = geocodeResult.getGeocodeAddressList().get(0);

                mAmap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(address.getLatLonPoint().getLatitude(), address.getLatLonPoint().getLongitude()), 15));
                //ToastUtil.toastShortShow(this,"eeeeee"+(int)address.getLatLonPoint().getLongitude());

                //geoMarker.setPosition(new LatLng(address.getLatLonPoint().getLatitude(), address.getLatLonPoint().getLongitude()));
//                addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
//                        + address.getFormatAddress();
                //  ToastUtil.toastShortShow(this, "aaaaaa"+(int)address.getLatLonPoint().getLongitude());

                //double latitude = poiResult.getPois().get(0).getLatLonPoint().getLatitude();
                //task.cancel();
                //double longitude = poiResult.getPois().get(0).getLatLonPoint().getLongitude();
                endLatlng = new NaviLatLng(address.getLatLonPoint().getLatitude(), address.getLatLonPoint().getLongitude());
                //task.cancel();

                endList.clear();
                endList.add(endLatlng);

                //开始算路
                startCalculate();

            } else {
                ToastUtil.toastShortShow(RestRouteShowActivity.this,"对不起，没有搜索到相关数据!");

            }
        } else {
            //ToastUtil.toastShortShow(this,i);
            ToastUtil.toastShortShow(RestRouteShowActivity.this,"对不起，没有搜索到相关数据!");
        }
    }

}

