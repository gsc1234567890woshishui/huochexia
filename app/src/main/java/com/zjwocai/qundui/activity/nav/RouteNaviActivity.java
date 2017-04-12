package com.zjwocai.qundui.activity.nav;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviStaticInfo;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.util.TTSController;
import com.autonavi.tbt.NaviStaticInfo;
import com.autonavi.tbt.TrafficFacilityInfo;

import java.text.DecimalFormat;
import java.util.Calendar;


public class RouteNaviActivity extends Activity implements AMapNaviListener, AMapNaviViewListener {

	AMapNaviView mAMapNaviView;
	AMapNavi mAMapNavi;
	TTSController mTtsManager;
	private  CheckBox cbTheme;
	private  float speed;
	private TextView tvSpeed,tvServiceArea;
	private AMapNaviLocation location;
	private boolean b;
	private RelativeLayout rlService;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_basic_navi);
		mTtsManager = TTSController.getInstance(getApplicationContext());
		mTtsManager.init();

		mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
		tvServiceArea = (TextView) findViewById(R.id.tv_serviceArea);
		rlService = (RelativeLayout) findViewById(R.id.rl_service);
		tvSpeed = (TextView) findViewById(R.id.tv_speed);
		cbTheme = (CheckBox) findViewById(R.id.cb_theme);
		mAMapNaviView.onCreate(savedInstanceState);
		mAMapNaviView.setAMapNaviViewListener(this);

		mAMapNavi = AMapNavi.getInstance(getApplicationContext());
		mAMapNavi.addAMapNaviListener(this);
		mAMapNavi.addAMapNaviListener(mTtsManager);
		mAMapNavi.setEmulatorNaviSpeed(60);
		boolean gps=getIntent().getBooleanExtra("gps", false);

		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		if(hour<8 || hour >18){
			setAmapNaviViewOptions(true);
		}else{
			setAmapNaviViewOptions(false);
		}

		if(gps){
			mAMapNavi.startNavi(AMapNavi.GPSNaviMode);
		}else{
			mAMapNavi.startNavi(AMapNavi.EmulatorNaviMode);	
		}
		cbTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){


			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                  if(b){
					  setAmapNaviViewOptions(b);
				  }else{
					  setAmapNaviViewOptions(b);
				  }
			}
		});

	}
	private void setAmapNaviViewOptions(boolean b) {
		if (mAMapNaviView == null) {
			return;
		}
		AMapNaviViewOptions viewOptions = new AMapNaviViewOptions();
		viewOptions.setSettingMenuEnabled(false);//设置菜单按钮是否在导航界面显示
		viewOptions.setNaviNight(b);//设置导航界面是否显示黑夜模式
		viewOptions.setReCalculateRouteForYaw(true);//设置偏航时是否重新计算路径
		viewOptions.setReCalculateRouteForTrafficJam(true);//前方拥堵时是否重新计算路径
		viewOptions.setTrafficInfoUpdateEnabled(true);//设置交通播报是否打开
		viewOptions.setCameraInfoUpdateEnabled(true);//设置摄像头播报是否打开
		viewOptions.setScreenAlwaysBright(true);//设置导航状态下屏幕是否一直开启。
		viewOptions.setCrossDisplayEnabled(false); //设置导航时 路口放大功能是否显示
		viewOptions.setTrafficBarEnabled(false);  //设置 返回路况光柱条是否显示（只适用于驾车导航，需要联网）
		viewOptions.setMonitorCameraEnabled(true); //设置摄像头图标是否显示 是
		 //viewOptions.setLayoutVisible(true);  //设置导航界面UI是否显示
		//viewOptions.setNaviViewTopic(mThemeStle);//设置导航界面的主题
		//viewOptions.setZoom(16);
		viewOptions.setTilt(1);  //2D显示
		mAMapNaviView.setViewOptions(viewOptions);

	}

	@Override
	protected void onResume() {
		super.onResume();
		mAMapNaviView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mAMapNaviView.onPause();
		//        仅仅是停止你当前在说的这句话，一会到新的路口还是会再说的
		mTtsManager.stopSpeaking();
		//        停止导航之后，会触及底层stop，然后就不会再有回调了，但是讯飞当前还是没有说完的半句话还是会说完
		//        mAMapNavi.stopNavi();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mAMapNaviView.onDestroy();
		mAMapNavi.stopNavi();
		mAMapNavi.destroy();
		mTtsManager.destroy();
	}

	@Override
	public void onInitNaviFailure() {
		Toast.makeText(this, "init navi Failed", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onInitNaviSuccess() {

	}

	@Override
	public void onStartNavi(int type) {

	}

	@Override
	public void onTrafficStatusUpdate() {

	}

	@Override
	public void onLocationChange(AMapNaviLocation location) {
        this.location = location;
		speed = location.getSpeed();
		tvSpeed.setText(String.valueOf(speed));
		System.out.println("ssssssssssssssssssssssppppppp"+speed);
	}

	@Override
	public void onGetNavigationText(int type, String text) {

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
	public void onCalculateRouteSuccess() {
	}

	@Override
	public void onCalculateRouteFailure(int errorInfo) {
	}

	@Override
	public void onReCalculateRouteForYaw() {

	}

	@Override
	public void onReCalculateRouteForTrafficJam() {

	}

	@Override
	public void onArrivedWayPoint(int wayID) {

	}

	@Override
	public void onGpsOpenStatus(boolean enabled) {
	}

	@Override
	public void onNaviSetting() {
	}

	@Override
	public void onNaviMapMode(int isLock) {

	}

	@Override
	public void onNaviCancel() {
		finish();
	}

	@Override
	public void onNaviTurnClick() {

	}

	@Override
	public void onNextRoadClick() {

	}

	@Override
	public void onScanViewButtonClick() {
	}

	@Deprecated
	@Override
	public void onNaviInfoUpdated(AMapNaviInfo naviInfo) {

	}

	@Override
	public void onNaviInfoUpdate(NaviInfo naviinfo) {
		int i = naviinfo.getServiceAreaDistance();
		DecimalFormat    df   = new DecimalFormat("######0.00");
		double a = (double) i/1000;
		//System.out.println(String.format("%.2f", a-0.005));
		if(i<=0){

			rlService.setVisibility(View.GONE);
		}else{
			rlService.setVisibility(View.VISIBLE);
			tvServiceArea.setText(df.format(a)+"km");
		}

		System.out.println("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiii"+a+"        "+df.format(a));
	}

	@Override
	public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

	}

	@Override
	public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

	}

	@Override
	public void showCross(AMapNaviCross aMapNaviCross) {
	}

	@Override
	public void hideCross() {
	}

	@Override
	public void showLaneInfo(AMapLaneInfo[] laneInfos, byte[] laneBackgroundInfo, byte[] laneRecommendedInfo) {

	}

	@Override
	public void hideLaneInfo() {

	}

	@Override
	public void onCalculateMultipleRoutesSuccess(int[] ints) {

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

	@Override
	public void onLockMap(boolean isLock) {
	}

	@Override
	public void onNaviViewLoaded() {
	}

	@Override
	public boolean onNaviBackClick() {
		return false;
	}

}
