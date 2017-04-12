package com.zjwocai.qundui.activity.hurry;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.LocationSource;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ta.utdid2.android.utils.StringUtils;
import com.threeti.teamlibrary.activity.BaseProtocolActivity;
import com.threeti.teamlibrary.finals.ProjectConstant;
import com.threeti.teamlibrary.model.UserModel;
import com.threeti.teamlibrary.net.BaseModel;
import com.threeti.teamlibrary.utils.SPUtil;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.activity.goods.OrderDetailActivity;
import com.zjwocai.qundui.activity.nav.GPSNaviActivity;
import com.zjwocai.qundui.adapter.HurryListAdapter;
import com.zjwocai.qundui.adapter.OnCustomListener;
import com.zjwocai.qundui.adapter.SimpleListAdapter;
import com.zjwocai.qundui.bill.ProtocolBill;
import com.zjwocai.qundui.model.AreaModel;
import com.zjwocai.qundui.model.CityModel;
import com.zjwocai.qundui.model.HurryListModel;
import com.zjwocai.qundui.model.ItemModel;
import com.zjwocai.qundui.model.NoticeModel;
import com.zjwocai.qundui.model.ProvinceModel;
import com.zjwocai.qundui.util.DialogUtil;
import com.zjwocai.qundui.util.StringUtil;
import com.zjwocai.qundui.util.ToastUtil;
//import com.zjwocai.qundui.widgets.CommMenuBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抢枪抢
 * Created by NieLiQin on 2016/8/25.
 */
public class HurryActivity extends BaseProtocolActivity implements View.OnClickListener, OnCustomListener, LocationSource,
        AMapLocationListener, LocationSource.OnLocationChangedListener, AdapterView.OnItemClickListener {
    private LinearLayout llStart, llEnd, llLength, llType, llLeft;
    private TextView tvStart, tvEnd, tvLength, tvType, tvLeft, tvSend, tvFinish;
    private View viewLine;
    private PullToRefreshListView ptrlv;
    private HurryListAdapter mAdapter;
    private ImageView ivNoData;
    private int page = 1;
    private boolean type = true;
    private Dialog callDialog, netDialog;

    private PopupWindow popType, popLength, popEnd, popStart;
    private List<ItemModel> carModels, lengthModels;
    private List<ProvinceModel> spModels, epModels;
    private List<CityModel> scModels, ecModels;
    private List<AreaModel> eaModels;
    private List<HurryListModel> hurryListModels;

    //private CommMenuBar commMenuBar;
    private SimpleListAdapter mSPAdapter, mSCAdapter, mEPAdapter, mECAdapter, mEAAdapter;
    private String spId, scId, epId, ecId, eaId;
    private ImageView iv2;

    private LocationSource.OnLocationChangedListener mListener;

    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private SimpleListAdapter typeAdapter;
    private SimpleListAdapter lengthAdapter;
    private List<CityModel> cityModels;
    private boolean isLocating = true;
    private String mLocation;
    private UserModel userModel;
    private Dialog nullNumberDialog;

    public HurryActivity() {
        super(R.layout.act_hurry);
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        ProtocolBill.getInstance().getNotice(this, this);
        userModel = ProtocolBill.getInstance().getNowUser();
        spModels = epModels = ProtocolBill.getInstance().getPros();
        mLocation = ProtocolBill.getInstance().getLocation();
        if (null == spModels || spModels.isEmpty()) {
            spModels = new ArrayList<>();
        }
        if (null == epModels || epModels.isEmpty()) {
            epModels = new ArrayList<>();
        }
    }

    @Override
    public void findIds() {
        //commMenuBar = new CommMenuBar(this, CommMenuBar.MENU_HURRY);
        viewLine = findViewById(R.id.view_line);
        llStart = (LinearLayout) findViewById(R.id.ll_start);
        llEnd = (LinearLayout) findViewById(R.id.ll_end);
        llLength = (LinearLayout) findViewById(R.id.ll_length);
        llType = (LinearLayout) findViewById(R.id.ll_type);
        llLeft = (LinearLayout) findViewById(R.id.ll_left);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        tvStart = (TextView) findViewById(R.id.tv_start);
        tvEnd = (TextView) findViewById(R.id.tv_end);
        tvLength = (TextView) findViewById(R.id.tv_length);
        tvType = (TextView) findViewById(R.id.tv_type);
        tvSend = (TextView) findViewById(R.id.tv_send);
        tvFinish = (TextView) findViewById(R.id.tv_finish);
        ptrlv = (PullToRefreshListView) findViewById(R.id.ptrlv_orders);
        ivNoData = (ImageView) findViewById(R.id.iv_no_data);
        ivNoData.setVisibility(View.GONE);
    }

    @Override
    public void initViews() {
        ptrlv.setMode(PullToRefreshBase.Mode.BOTH);
        ptrlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                page = 1;
                searchList(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                page++;
                searchList(false);
            }
        });

        llLeft.setOnClickListener(this);
        llStart.setOnClickListener(this);
        llEnd.setOnClickListener(this);
        llLength.setOnClickListener(this);
        llType.setOnClickListener(this);
        scModels = new ArrayList<>();
        ecModels = new ArrayList<>();
        eaModels = new ArrayList<>();

        hurryListModels = new ArrayList();
        mAdapter = new HurryListAdapter(this, hurryListModels);
        mAdapter.setOnCustomListener(this);
        ptrlv.setAdapter(mAdapter);
        ptrlv.setOnItemClickListener(this);
        netDialog = DialogUtil.getProgressDialog(this, getString(R.string.ui_net_request));
        netDialog.show();
        if (null == mLocation || mLocation.equals("") || mLocation.equals("全部")) {
            activate(this);
            isLocating = true;
        } else {
            tvLeft.setText(mLocation);
            tvStart.setText(mLocation);
            isLocating = false;
            searchList(false);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map map = new HashMap();
        map.put("state", 1);
        map.put("id", hurryListModels.get((int) id).getId());
        map.put("from", "6");
        map.put("isThird", hurryListModels.get((int) id).getIsdsf());
        startActivity(OrderDetailActivity.class, map);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_start:
                type = true;
                if (spModels.isEmpty()) {
                    //请求位置数据
                    ProtocolBill.getInstance().getProvinces(this, this);
                } else {
                    llStart.setSelected(true);
                    showStartWindow();
                }
                break;
            case R.id.ll_end:
                type = false;
                if (epModels.isEmpty()) {
                    ProtocolBill.getInstance().getProvinces(this, this);
                } else {
                    llEnd.setSelected(true);
                    showEndWindow();
                }
                break;
            case R.id.ll_length:
                llLength.setSelected(true);
                showLengthWindow();
                break;
            case R.id.ll_type:
                llType.setSelected(true);
                showTypeWindow();
                break;
            case R.id.ll_left:
                startActivityForResult(SearchCityActivity.class, tvLeft.getText(), 0);
                break;
        }
    }

    @Override
    public void onCustomerListener(View v, int position) {
        switch (v.getId()) {
            case R.id.iv_phone:
                String number = (String) v.getTag();    //获取电话号码
                if (StringUtils.isEmpty(number)) {
                    showNullNumberDialog();
                } else {
                    showCallDialog(number);
                }
                break;
            case R.id.ll_navi_info:
                HurryListModel model = (HurryListModel) v.getTag();
                Map map = new HashMap();
                map.put("city", model.getShcityname());
                map.put("keyword", model.getShprovicename() + model.getShcityname() //收货地址
                        + model.getShareaname() + model.getShaddress());
                startActivity(GPSNaviActivity.class, map);
                break;
        }
    }

    private void searchList() {
        searchList(true);
    }

    private void searchList(boolean isShow) {
        if (isShow) {
            netDialog.show();
        }
        String startCity = tvStart.getText().toString();
        String endCity = tvEnd.getText().toString();
        String carType = tvType.getText().toString();
        String carLength = tvLength.getText().toString();
        if ("始发地".equals(startCity) || "不限".equals(startCity)) {
            startCity = "";
        } else if (mSCAdapter != null) {
            int select = mSCAdapter.getSelect();
            if (select == -1) {
                cityModels = ProtocolBill.getInstance().getCities();
                if (null == cityModels || cityModels.isEmpty()) {
                    ProtocolBill.getInstance().getAllCities(this, this);
                } else {
                    for (CityModel cityModel : cityModels) {
                        if (startCity.equals(cityModel.getName())) {
                            startCity = cityModel.getCode();
                        }
                    }
                }
            } else {
                startCity = scModels.get(select).getCode();
            }
        } else {
            cityModels = ProtocolBill.getInstance().getCities();
            if (null == cityModels || cityModels.isEmpty()) {
                ProtocolBill.getInstance().getAllCities(this, this);
            } else {
                for (CityModel cityModel : cityModels) {
                    if (startCity.equals(cityModel.getName())) {
                        startCity = cityModel.getCode();
                    }
                }
            }
        }
        if ("目的地".equals(endCity) || "不限".equals(endCity)) {
            endCity = "";
        } else if (mECAdapter != null) {
            endCity = ecModels.get(mECAdapter.getSelect()).getCode();
        } else {
            endCity = "";
        }
        if ("车型".equals(carType) || "不限".equals(carType)) {
            carType = "";
        } else if (typeAdapter != null) {
            carType = carModels.get(typeAdapter.getSelect()).getCode();
        } else {
            carType = "";
        }
        if ("车长".equals(carLength) || "不限".equals(carLength)) {
            carLength = "";
        } else if (lengthAdapter != null) {
            carLength = lengthModels.get(lengthAdapter.getSelect()).getCode();
        } else {
            carLength = "";
        }
        ProtocolBill.getInstance().getHurryList(this, this, startCity, endCity, carLength, carType, String.valueOf(page));
    }

    @Override
    public void onTaskSuccess(BaseModel result) {
        if (RQ_GET_PROVINCE_LIST.equals(result.getRequest_code())) {
            List<ProvinceModel> models = (List<ProvinceModel>) result.getResult();
            if (models != null && !models.isEmpty()) {
                if (type) {
                    spModels.clear();
                    spModels.addAll(models);
                    spModels.add(0, new ProvinceModel("", "", "不限"));
                    llStart.setSelected(true);
                    showStartWindow();
                } else {
                    epModels.clear();
                    epModels.addAll(models);
                    epModels.add(0, new ProvinceModel("", "", "不限"));
                    llEnd.setSelected(true);
                    showEndWindow();
                }

            }
        } else if (RQ_GET_CITY_LIST.equals(result.getRequest_code())) {
            List<CityModel> models = (List<CityModel>) result.getResult();
            if (models != null && !models.isEmpty()) {
                if (type) {
                    scModels.clear();
                    scModels.addAll(models);
                    if (popStart != null) {
                        mSCAdapter.setSelect(-1);
                        mSCAdapter.notifyDataSetChanged();
                    }
                } else {
                    ecModels.clear();
                    ecModels.addAll(models);
                    if (popEnd != null) {
                        mECAdapter.setSelect(-1);
                        mECAdapter.notifyDataSetChanged();
                    }
                }
            }
        } else if (RQ_GET_HURRY_LIST.equals(result.getRequest_code())) {
            List<HurryListModel> models = (List<HurryListModel>) result.getResult();
            if (page == 1) {
                hurryListModels.clear();
                if (models != null && !models.isEmpty()) {
                    ivNoData.setVisibility(View.GONE);
                    hurryListModels.addAll(models);
                } else {
                    ivNoData.setVisibility(View.VISIBLE);
                }
            } else {
                if (models != null && !models.isEmpty()) {
                    hurryListModels.addAll(models);
                } else {
                   // ToastUtil.toastShortShow("没有更多订单");
                }
            }
            mAdapter.notifyDataSetChanged();
        } else if (RQ_GET_ALL_CITY_LIST.equals(result.getRequest_code())) {
            cityModels = (List<CityModel>) result.getResult();
            SPUtil.saveObjectToShare(ProjectConstant.KEY_CITIES, cityModels);
            searchList();
        } else if (RQ_GET_NOTICE.equals(result.getRequest_code())) {
            NoticeModel model = (NoticeModel) result.getResult();
            if (model != null) {
                tvSend.setText(model.getCreateNum());
                tvFinish.setText(model.getSuccessNum());
            }
        }
    }

    @Override
    public void onTaskFinished(String resuestCode) {
        super.onTaskFinished(resuestCode);
        if (netDialog.isShowing()) {
            if (!isLocating)
                netDialog.dismiss();
        }
        if (ptrlv.isRefreshing()) {
            ptrlv.onRefreshComplete();
        }
    }

    /**
     * 拨打电话
     *
     * @param number 电话号码
     */
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
                if (StringUtil.isNO(number.replace("-","").trim())) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    Uri data = Uri.parse("tel:" + number.replace("-","").trim());
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
                if (StringUtil.isNO(userModel.getKftel().replace("-","").trim())) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    Uri data = Uri.parse("tel:" + userModel.getKftel().replace("-","").trim());
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

    /**
     * 车型筛选
     */
    private void showTypeWindow() {
        if (popType == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.pop_hurry, null);
            ListView lv1 = (ListView) view.findViewById(R.id.lv1);
            ListView lv2 = (ListView) view.findViewById(R.id.lv2);
            ListView lv3 = (ListView) view.findViewById(R.id.lv3);
            ImageView iv1 = (ImageView) view.findViewById(R.id.iv1);
            ImageView iv2 = (ImageView) view.findViewById(R.id.iv2);
            lv2.setVisibility(View.GONE);
            lv3.setVisibility(View.GONE);
            iv1.setVisibility(View.GONE);
            iv2.setVisibility(View.GONE);
            View holder = view.findViewById(R.id.view_holder);
            carModels = new ArrayList<>();
            carModels.add(new ItemModel("不限", "0"));
            carModels.add(new ItemModel("厢式车", "1"));
            carModels.add(new ItemModel("平板车", "2"));
            carModels.add(new ItemModel("高栏车", "3"));
            carModels.add(new ItemModel("中栏车", "4"));
            carModels.add(new ItemModel("低栏车", "5"));
            carModels.add(new ItemModel("其他车型", "6"));
            carModels.add(new ItemModel("冷藏车", "7"));
            carModels.add(new ItemModel("危险品车", "8"));
            carModels.add(new ItemModel("自卸货车", "9"));
            carModels.add(new ItemModel("集装箱车", "10"));
            carModels.add(new ItemModel("高低板车", "11"));
            typeAdapter = new SimpleListAdapter(this, carModels, 1);
            lv1.setAdapter(typeAdapter);
            lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (id != typeAdapter.getSelect()) {
                        typeAdapter.setSelect((int) id);
                        if (id == 0) {
                            tvType.setText("车型");
                        } else {
                            tvType.setText(carModels.get((int) id).getName());
                        }
                        typeAdapter.notifyDataSetChanged();
                        page = 1;
                        searchList();
                    }
                    popType.dismiss();
                }
            });
            holder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popType.dismiss();
                }
            });
            popType = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
        }
        popType.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                llType.setSelected(false);
            }
        });
        popType.setOutsideTouchable(true);
        popType.setBackgroundDrawable(new ColorDrawable(0));
        popType.showAsDropDown(viewLine, 0, 0);
    }

    /**
     * 车长筛选
     */
    private void showLengthWindow() {
        if (popLength == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.pop_hurry, null);
            ListView lv1 = (ListView) view.findViewById(R.id.lv1);
            ListView lv2 = (ListView) view.findViewById(R.id.lv2);
            ListView lv3 = (ListView) view.findViewById(R.id.lv3);
            ImageView iv1 = (ImageView) view.findViewById(R.id.iv1);
            ImageView iv2 = (ImageView) view.findViewById(R.id.iv2);
            lv2.setVisibility(View.GONE);
            lv3.setVisibility(View.GONE);
            iv1.setVisibility(View.GONE);
            iv2.setVisibility(View.GONE);
            View holder = view.findViewById(R.id.view_holder);
            lengthModels = new ArrayList<>();
            lengthModels.add(new ItemModel("不限", "1"));
            lengthModels.add(new ItemModel("4.2米", "2"));
            lengthModels.add(new ItemModel("5.2米", "3"));
            lengthModels.add(new ItemModel("6.2米", "4"));
            lengthModels.add(new ItemModel("6.8米", "5"));
            lengthModels.add(new ItemModel("7.2米", "6"));
            lengthModels.add(new ItemModel("8.2米", "7"));
            lengthModels.add(new ItemModel("8.6米", "8"));
            lengthModels.add(new ItemModel("9.6米", "9"));
            lengthModels.add(new ItemModel("11.7米", "10"));
            lengthModels.add(new ItemModel("12.5米", "11"));
            lengthModels.add(new ItemModel("13米", "12"));
            lengthModels.add(new ItemModel("13.5米", "13"));
            lengthModels.add(new ItemModel("14米", "14"));
            lengthModels.add(new ItemModel("17米", "15"));
            lengthModels.add(new ItemModel("17.5米", "16"));
            lengthModels.add(new ItemModel("18米", "17"));
            lengthAdapter = new SimpleListAdapter(this, lengthModels, 1);
            lv1.setAdapter(lengthAdapter);
            lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (lengthAdapter.getSelect() != id) {
                        lengthAdapter.setSelect((int) id);
                        if (id == 0) {
                            tvLength.setText("车长");
                        } else {
                            tvLength.setText(lengthModels.get((int) id).getName());
                        }
                        lengthAdapter.notifyDataSetChanged();
                        page = 1;
                        searchList();
                    }
                    popLength.dismiss();
                }
            });
            holder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popLength.dismiss();
                }
            });
            popLength = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
        }
        popLength.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                llLength.setSelected(false);
            }
        });
        popLength.setOutsideTouchable(true);
        popLength.setBackgroundDrawable(new ColorDrawable(0));
        popLength.showAsDropDown(viewLine, 0, 0);
    }

    /**
     * 始发地
     */
    private void showStartWindow() {
        if (popStart == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.pop_hurry, null);
            ListView lv1 = (ListView) view.findViewById(R.id.lv1);
            ListView lv2 = (ListView) view.findViewById(R.id.lv2);
            ListView lv3 = (ListView) view.findViewById(R.id.lv3);
            View holder = view.findViewById(R.id.view_holder);
            final ImageView iv2 = (ImageView) view.findViewById(R.id.iv2);
            lv3.setVisibility(View.GONE);
            iv2.setVisibility(View.GONE);

            mSPAdapter = new SimpleListAdapter(this, spModels, 1);
            mSCAdapter = new SimpleListAdapter(this, scModels, 2);
            lv1.setAdapter(mSPAdapter);
            lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (null != spModels && !spModels.isEmpty()) {
                        if (id == 0) {
                            mSPAdapter.setSelect((int) id);
                            mSPAdapter.notifyDataSetChanged();
                            tvStart.setText("始发地");
                            page = 1;
                            searchList();
                            popStart.dismiss();
                        } else {
                            scModels.clear();
                            mSPAdapter.setSelect((int) id);
                            mSPAdapter.notifyDataSetChanged();
                            spId = spModels.get((int) id).getId();
                            ProtocolBill.getInstance().getCities(HurryActivity.this, HurryActivity.this, spId);
                        }
                    } else {
                        popStart.dismiss();
                    }
                }
            });
            lv2.setAdapter(mSCAdapter);
            lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (null != scModels && !scModels.isEmpty()) {
                        mSCAdapter.setSelect((int) id);
                        mSCAdapter.notifyDataSetChanged();
                        tvStart.setText(scModels.get((int) id).getName());
                        page = 1;
                        searchList();
                        popStart.dismiss();
                    } else {
                        popStart.dismiss();
                    }
                }
            });
            holder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popStart.dismiss();
                }
            });
            popStart = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
        }
        mSPAdapter.setSelect(-1);
        scModels.clear();
        mSCAdapter.setSelect(-1);
        mSPAdapter.notifyDataSetChanged();
        mSCAdapter.notifyDataSetChanged();
        popStart.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                llStart.setSelected(false);
            }
        });
        popStart.setOutsideTouchable(true);
        popStart.setBackgroundDrawable(new ColorDrawable(0));
        popStart.showAsDropDown(viewLine, 0, 0);
    }

    /**
     * 目的地
     */
    private void showEndWindow() {
        if (popEnd == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.pop_hurry, null);
            ListView lv1 = (ListView) view.findViewById(R.id.lv1);
            ListView lv2 = (ListView) view.findViewById(R.id.lv2);
            ListView lv3 = (ListView) view.findViewById(R.id.lv3);
            View holder = view.findViewById(R.id.view_holder);
            lv3.setVisibility(View.GONE);
            iv2 = (ImageView) view.findViewById(R.id.iv2);
            iv2.setVisibility(View.GONE);

            mEPAdapter = new SimpleListAdapter(this, epModels, 1);
            mECAdapter = new SimpleListAdapter(this, ecModels, 2);
            lv1.setAdapter(mEPAdapter);
            lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (null != epModels && !epModels.isEmpty()) {
                        if (id == 0) {
                            tvEnd.setText("目的地");
                            mEPAdapter.setSelect((int) id);
                            mEPAdapter.notifyDataSetChanged();
                            page = 1;
                            searchList();
                            popEnd.dismiss();
                        } else {
                            ecModels.clear();
                            mEPAdapter.setSelect((int) id);
                            mEPAdapter.notifyDataSetChanged();
                            epId = epModels.get((int) id).getId();
                            ProtocolBill.getInstance().getCities(HurryActivity.this, HurryActivity.this, epId);
                        }
                    } else {
                        popEnd.dismiss();
                    }
                }
            });
            lv2.setAdapter(mECAdapter);
            lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (null != ecModels && !ecModels.isEmpty()) {
                        mECAdapter.setSelect((int) id);
                        mECAdapter.notifyDataSetChanged();
                        tvEnd.setText(ecModels.get((int) id).getName());
                        page = 1;
                        searchList();
                        popEnd.dismiss();
                    } else {
                        popEnd.dismiss();
                    }
                }
            });
            holder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popEnd.dismiss();
                }
            });
            popEnd = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
        }
        ecModels.clear();
        eaModels.clear();
        mEPAdapter.setSelect(-1);
        mECAdapter.setSelect(-1);
        mEPAdapter.notifyDataSetChanged();
        mECAdapter.notifyDataSetChanged();
        popEnd.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                llEnd.setSelected(false);
            }
        });
        popEnd.setOutsideTouchable(true);
        popEnd.setBackgroundDrawable(new ColorDrawable(0));
        popEnd.showAsDropDown(viewLine, 0, 0);
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(LocationSource.OnLocationChangedListener listener) {
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
                deactivate();
                check();
            } else {
                deactivate();
            }
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            double locateLat = aMapLocation.getLatitude();
            double locateLng = aMapLocation.getLongitude();

            if (aMapLocation.getCity() == null || aMapLocation.getCity().equals("")) {
                ToastUtil.toastShortShow("定位失败，请检查您的GPS设置！");
                tvLeft.setText("");
                tvStart.setText("始发地");
                SPUtil.saveString(ProjectConstant.KEY_LOCATION, "");
            } else {
                tvLeft.setText(aMapLocation.getCity());
                tvStart.setText(aMapLocation.getCity());
                SPUtil.saveString(ProjectConstant.KEY_LOCATION, aMapLocation.getCity());
            }
            deactivate();
            check();
            isLocating = false;
        }
    }

    private void check() {
        page = 1;
        searchList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            String city = data.getStringExtra("data");
            tvLeft.setText(city);
            tvStart.setText(city);
            SPUtil.saveString(ProjectConstant.KEY_LOCATION, city);
            if (mSCAdapter != null)
                mSCAdapter.setSelect(-1);
            check();
        }
    }
}
