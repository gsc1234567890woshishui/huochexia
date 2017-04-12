package com.zjwocai.qundui.activity.nav;

import android.app.Dialog;
import android.location.Location;
import android.media.AudioManager;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.LocationSource;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.model.AMapNaviGuide;
import com.amap.api.navi.model.AMapNaviLink;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviStep;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.util.DialogUtil;
import com.zjwocai.qundui.util.ToastUtil;

import java.util.List;
import java.util.Map;

/**
 * 创建时间：11/11/15 18:50
 * 项目名称：newNaviDemo
 *
 * @author lingxiang.wang
 * @email lingxiang.wang@alibaba-inc.com
 * 类说明：
 */

public class GPSNaviActivity extends BaseActivity implements LocationSource,
        AMapLocationListener, LocationSource.OnLocationChangedListener, PoiSearch.OnPoiSearchListener {

    private OnLocationChangedListener mListener;

    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private String keyWord;
    private String city;
    private PoiSearch poiSearch;
    private boolean isOk = false;
    private AMapNaviPath mAMapNaviPath;
    private List<AMapNaviStep> steps;
    private List<AMapNaviLink> links;
    private List<AMapNaviGuide> guides;
    private Dialog dialog;
//    private int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        setContentView(R.layout.activity_basic_navi);
        Map map = (Map) getIntent().getSerializableExtra("data");
        keyWord = (String) map.get("keyword");
        city = (String) map.get("city");
        if (keyWord == null || city == null || keyWord.equals("") || keyWord.equals("")) {
            ToastUtil.toastShortShow("无法定位目标地址，导航失败！！");
            return;
        }
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);
        doSearchQuery();

        dialog = DialogUtil.getProgressDialog(this, "正在初始化导航，请稍候...");
       // dialog.show();
    }

    @Override
    public void onInitNaviSuccess() {
        activate(this);
    }

    /**
     * 如果使用无起点算路，请这样写
     */
    private void noStartCalculate() {
        //无起点算路须知：
        //AMapNavi在构造的时候，会startGPS，但是GPS启动需要一定时间
        //在刚构造好AMapNavi类之后立刻进行无起点算路，会立刻返回false
        //给人造成一种等待很久，依然没有算路成功 算路失败回调的错觉
        //因此，建议，提前获得AMapNavi对象实例，并判断GPS是否准备就绪

//        if (mAMapNavi.isGpsReady()) {
//            mAMapNavi.calculateDriveRoute(eList, mWayPointList, PathPlanningStrategy.DRIVING_DEFAULT);
//        } else {
//            ToastUtil.toastShortShow("导航失败，gps未准备就绪");
////            mlocationClient.startLocation();
//        }
        /**
         * 方法:
         *   int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute);
         * 参数:
         * @congestion 躲避拥堵
         * @avoidhightspeed 不走高速
         * @cost 避免收费
         * @hightspeed 高速优先
         * @multipleroute 多路径
         *
         * 说明:
         *      以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
         * 注意:
         *      不走高速与高速优先不能同时为true
         *      高速优先与避免收费不能同时为true
         */
        int strategy = 0;
        try {
            strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAMapNavi.calculateDriveRoute(sList, eList, mWayPointList, strategy);
    }


    @Override
    public void onNaviInfoUpdate(NaviInfo naviinfo) {
        super.onNaviInfoUpdate(naviinfo);
        int currentStep = naviinfo.getCurStep();
        int currentLink = naviinfo.getCurLink();
//        Log.d("wlx", "当前Step index:" + currentStep + "当前Link index:" + currentLink);
    }

    @Override
    public void onCalculateRouteSuccess() {
        super.onCalculateRouteSuccess();
        check();

        //概览
        guides = mAMapNavi.getNaviGuideList();

        //详情
        mAMapNaviPath = mAMapNavi.getNaviPath();
        steps = mAMapNaviPath.getSteps();

        if (guides.size() == steps.size()) {

//            Toast.makeText(this, "看log", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < steps.size() - 1; i++) {
                //guide step相生相惜，指的是大导航段
                AMapNaviGuide guide = guides.get(i);
//                Log.d("wlx", "AMapNaviGuide 路线经纬度:" + guide.getCoord() + "");
//                Log.d("wlx", "AMapNaviGuide 路线名:" + guide.getName() + "");
//                Log.d("wlx", "AMapNaviGuide 路线长:" + guide.getLength() + "m");
//                Log.d("wlx", "AMapNaviGuide 路线耗时:" + guide.getTime() + "s");
//                Log.d("wlx", "AMapNaviGuide 路线IconType" + guide.getIconType() + "");
                AMapNaviStep step = steps.get(i);
//                Log.d("wlx", "AMapNaviStep 距离:" + step.getLength() + "m" + " " + "耗时:" + step.getTime() + "s");
//                Log.d("wlx", "AMapNaviStep 红绿灯个数:" + step.getTrafficLightNumber());


                //link指的是大导航段中的小导航段
                links = step.getLinks();
                for (AMapNaviLink link : links) {
//          请看com.amap.api.navi.enums.RoadClass，以及帮助文档
//                    Log.d("wlx", "AMapNaviLink 道路名:" + link.getRoadName() + " " + "道路等级:" + link.getRoadClass());
//          请看com.amap.api.navi.enums.RoadType，以及帮助文档
//                    Log.d("wlx", "AMapNaviLink 道路类型:" + link.getRoadType());

                }
            }

        } else {
//            Toast.makeText(this, "BUG！请联系我们", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCalculateRouteFailure(int errorInfo) {
        super.onCalculateRouteFailure(errorInfo);
        check();
        ToastUtil.toastShortShow("导航路线规划失败，请稍候重试！");
        finish();
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//            mLocationOption.setOnceLocation(true);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mListener != null && location != null) {
            double locateLat = location.getLatitude();
            double locateLng = location.getLongitude();

            if (0.0 == locateLat || 0.0 == locateLng) {
                ToastUtil.toastShortShow("定位失败，请检查您的GPS设置！");
                check();
                finish();
            } else {
                sList.add(new NaviLatLng(locateLat, locateLng));
                if (eList != null && !eList.isEmpty()) {
                    deactivate();
                    noStartCalculate();
                }
//                doSearchQuery();
            }
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            double locateLat = aMapLocation.getLatitude();
            double locateLng = aMapLocation.getLongitude();

            if (0.0 == locateLat || 0.0 == locateLng) {
                ToastUtil.toastShortShow("定位失败，请检查您的GPS设置！");
                check();
                finish();
            } else {
                sList.add(new NaviLatLng(locateLat, locateLng));
                if (eList != null && !eList.isEmpty()) {
                    deactivate();
                    noStartCalculate();
                } else {
//                    count++;
//                    if (count == 50) {
//                        ToastUtil.toastShortShow("无法定位目标地点，导航失败！");
//                        check();
//                        finish();
//                    }
                }
//                eList.add(new NaviLatLng(locateLat, locateLng));
//                doSearchQuery();
            }
        }
    }

    protected void doSearchQuery() {
        PoiSearch.Query query = new PoiSearch.Query(keyWord, "", city);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(10); // 设置每页最多返回多少条poiitem
        query.setPageNum(1);// 设置查第一页

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        if (poiResult.getPois() != null && !poiResult.getPois().isEmpty()) {
            double latitude = poiResult.getPois().get(0).getLatLonPoint().getLatitude();
            double longitude = poiResult.getPois().get(0).getLatLonPoint().getLongitude();
            eList.add(new NaviLatLng(latitude, longitude));

        } else {
            check();
            ToastUtil.toastShortShow("定位地址不存在，导航失败！");
            finish();
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    private void check() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
