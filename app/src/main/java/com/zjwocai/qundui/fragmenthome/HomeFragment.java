package com.zjwocai.qundui.fragmenthome;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.threeti.teamlibrary.activity.BaseActivity;
import com.threeti.teamlibrary.finals.ProjectConstant;
import com.threeti.teamlibrary.finals.RequestCodeSet;
import com.threeti.teamlibrary.model.UserModel;
import com.threeti.teamlibrary.net.BaseModel;
import com.threeti.teamlibrary.net.ProcotolCallBack;
import com.threeti.teamlibrary.utils.SPUtil;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.activity.goods.MyBaseFragment;
import com.zjwocai.qundui.activity.goods.OrderDetailActivity;
import com.zjwocai.qundui.activity.home.ADActivity;
import com.zjwocai.qundui.activity.home.NewsActivity;
import com.zjwocai.qundui.activity.login.LoginActivity;
import com.zjwocai.qundui.activity.nav.RestRouteShowActivity;
import com.zjwocai.qundui.adapter.BannerAdapter;
import com.zjwocai.qundui.adapter.HomeListAdapter;
import com.zjwocai.qundui.adapter.OnCustomListener;
import com.zjwocai.qundui.bill.ProtocolBill;
import com.zjwocai.qundui.fragment.Constant;
import com.zjwocai.qundui.fragment.EventMessage;
import com.zjwocai.qundui.model.ADModel;
import com.zjwocai.qundui.model.HomeOrderModel;
import com.zjwocai.qundui.model.NewsListModel;
import com.zjwocai.qundui.model.NewsModel;
import com.zjwocai.qundui.model.OrderModel;
import com.zjwocai.qundui.model.ProvinceModel;
import com.zjwocai.qundui.service.LocationService;
import com.zjwocai.qundui.service.LocationService2;
import com.zjwocai.qundui.util.DialogUtil;
import com.zjwocai.qundui.util.RefreshUtil;
import com.zjwocai.qundui.util.ToastUtil;
import com.zjwocai.qundui.widgets.BannerPager;
import com.zjwocai.qundui.widgets.ScrollDisabledListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends MyBaseFragment implements ProcotolCallBack, RequestCodeSet,OnCustomListener {



    private BannerPager bp;
    private LinearLayout llDots;
    private RelativeLayout rlAD;
    private ImageView ivNoData;
    private PullToRefreshScrollView ptrsv;
    private ScrollDisabledListView sdlv;
    private HomeListAdapter mAdapter;
    private BannerAdapter bpAdapter;

    private List<ADModel> adModels;
    private List<HomeOrderModel> list;
    private int page = 1;
    private Dialog netdialog, confirmDialog, refuseDialog;
    private int count;
    private UserModel user;
    private int pos;
    private int time = 0;
    private String sx="1";
    //private List<CityModel> cityModels;
    private List<ProvinceModel> provinceModels;
    //private boolean isFirstOpen = false;
    private String shAddress;

    private BaseActivity activity;
    private ImageView im_left;
    private ImageView im_right;
    private TextView tv_title;
    private TextView tv_right;
    private TextView tv_left;
    private RelativeLayout rl_title, rl_left, rl_right;
    private  String code1="";



    public HomeFragment() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_home2;
    }

    /**
     * 当界面重新展示时（fragment.show）,调用onrequest刷新界面
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden) {

        }
        if(sx.equals("1")){//如果是0就提示
            //ToastUtil.toastShortShow("没有更多订单");
            sx = "0";//还原为0
        }

    }

    @Override
    public  void initWidget(View root){
        super.initWidget(root);
        //注册eventBus
        EventBus.getDefault().register(this);
        initData();
        im_left = (ImageView) root.findViewById(R.id.iv_left);
        im_right = (ImageView) root.findViewById(R.id.iv_right);
        tv_title = (TextView) root.findViewById(R.id.tv_title);
        rl_title = (RelativeLayout) root.findViewById(R.id.rl_title);
        rl_left = (RelativeLayout) root.findViewById(R.id.rl_left);
        rl_right = (RelativeLayout) root.findViewById(R.id.rl_right);
        tv_right = (TextView) root.findViewById(R.id.tv_right);
        tv_left = (TextView) root.findViewById(R.id.tv_left);
        ptrsv = (PullToRefreshScrollView) root.findViewById(R.id.ptrsv_home);
        sdlv = (ScrollDisabledListView)root.findViewById(R.id.sdlv_home);
        rlAD = (RelativeLayout) root.findViewById(R.id.rl_info_ads);
        bp = (BannerPager) root.findViewById(R.id.banner_home);
        llDots = (LinearLayout) root.findViewById(R.id.ll_home_dots);
        ivNoData = (ImageView) root.findViewById(R.id.iv_no_data);
        ivNoData.setVisibility(View.GONE);
        ivNoData.setVisibility(View.GONE);
        initViews();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setGoIndex(EventMessage eventMessage){
        //Log.d(TAG, "setGoIndex: "+eventMessage.getTag());
        if(eventMessage!=null){
            int tag = eventMessage.getTag();
            if(tag== EventMessage.EventMessageAction.TAG_REFRESH_HOME){

                ProtocolBill.getInstance().getHomeOrders(HomeFragment.this,getActivity(), "1");
                //刷新
                ptrsv.setRefreshing();

            }
            if(tag== EventMessage.EventMessageAction.TAG_REFRESH_RESUME){

                ProtocolBill.getInstance().getHomeOrders(HomeFragment.this,getActivity(), "1");
                //刷新
                ptrsv.setRefreshing();

            }
            if(tag== EventMessage.EventMessageAction.TAG_STOP_LOCATION){//确认收货
                //等service停止后再确认收货
                ProtocolBill.getInstance().enterSend(HomeFragment.this, getActivity(), list.get(pos).getId(), "");


            }
        }
    }

    private void initViews() {
        tv_title.setText("货车侠");
        im_left.setVisibility(View.GONE);
        rl_left.setVisibility(View.GONE);
        im_right.setVisibility(View.VISIBLE);
        im_right.setImageResource(R.drawable.ic_news);
        rl_right.setVisibility(View.VISIBLE);
        tv_right.setVisibility(View.GONE);
        rl_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

//        tv_title.setRightIcon(R.drawable.ic_news, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        ptrsv.setMode(PullToRefreshBase.Mode.BOTH);
        RefreshUtil.setPullText(getActivity(), ptrsv);


        ptrsv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> pullToRefreshBase) {
                page = 1;
                ProtocolBill.getInstance().getHomeOrders(HomeFragment.this,getActivity(), String.valueOf(page));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> pullToRefreshBase) {
                page++;
                ProtocolBill.getInstance().getHomeOrders(HomeFragment.this, getActivity(), String.valueOf(page));
            }
        });


        list = new ArrayList();
        mAdapter = new HomeListAdapter(getActivity(), list);
        mAdapter.setOnCustomListener(this);
        sdlv.setAdapter(mAdapter);
        sdlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map map = new HashMap();
                map.put("state", Integer.parseInt(list.get((int) id).getStatus()));
                map.put("id", list.get((int) id).getId());
                map.put("from", "4");
                ((BaseActivity)getActivity()).startActivity(OrderDetailActivity.class, map);
            }
        });

        adModels = new ArrayList<>();
        bpAdapter = new BannerAdapter(getActivity(), adModels);
        bp.setAdapter(bpAdapter);
//        bpAdapter.setOnCustomListener(new OnCustomListener() {
//            @Override
//            public void onCustomerListener(View v, int position) {
//                String url = adModels.get(position).getH5url();
//                if (url != null && !url.equals("")) {
//                    Map map = new HashMap();
//                    map.put("url", url);
//                    ((BaseActivity)getActivity()).startActivity(OrderDetailActivity.class, map);
//
//
//                }
//            }
//        });
        bpAdapter.setOnCustomListener(new OnCustomListener() {
            @Override
            public void onCustomerListener(View v, int position) {
                String url = adModels.get(position).getH5url();
                if (url != null && !url.equals("")) {
                    Map map = new HashMap();
                    map.put("url", url);
                    ((BaseActivity)getActivity()).startActivity(ADActivity.class, map);
                }
            }
        });

        rlAD.requestFocus();

        netdialog = DialogUtil.getProgressDialog(getActivity(), getString(R.string.ui_net_request));
        initGPS();
    }
    private void initGPS() {
        LocationManager locationManager = (LocationManager) getActivity()
                .getSystemService(Context.LOCATION_SERVICE);
        // 判断GPS模块是否开启，如果没有则跳转至设置开启界面，设置完毕后返回到当前页面
        if (!locationManager
                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {

            AlertDialog.Builder da = new AlertDialog.Builder(getActivity());
            da.setTitle("提示：");
            da.setMessage("为了更好的为您服务，请您打开您的GPS!");
            da.setCancelable(false);
            //设置左边按钮监听
            da.setPositiveButton("确定",
                    new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            // 转到手机设置界面，用户设置GPS
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            getActivity().startActivityForResult(intent, 0); // 设置完成后返回到原来的界面

                        }
                    });
            //设置右边按钮监听
            da.setNegativeButton("取消",
                    new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            arg0.dismiss();
                        }
                    });
            da.show();
        } else {
        }
    }

    private void initData() {
        ProtocolBill.getInstance().getHomePics(this, getActivity());
        ProtocolBill.getInstance().getHomeNews(this, getActivity(), "1");
        ProtocolBill.getInstance().getHomeOrders(this, getActivity(), String.valueOf(page));
        user = ProtocolBill.getInstance().getNowUser();
        ProtocolBill.getInstance().getKindOrders(new ProcotolCallBack() {
            @Override
            public void onTaskSuccess(BaseModel result) {

                //获取正在运输中的订单
                List<OrderModel> models = (List<OrderModel>) result.getResult();
                if (models != null && !models.isEmpty()) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            //开启service2
                            Intent startLocationServiceIntent2 = new Intent(getActivity(), LocationService2.class);
                            startLocationServiceIntent2.putExtra("id",user.getId());
                            getActivity().startService(startLocationServiceIntent2);
                        }
                    }, 5000);

                }
                else{
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            //开启service1
                            Intent startLocationServiceIntent = new Intent(getActivity(), LocationService.class);
                            startLocationServiceIntent.putExtra("id",user.getId());
                            getActivity().startService(startLocationServiceIntent);
                        }
                    }, 5000);
                }
            }

            @Override
            public void onTaskFail(BaseModel result) {


            }

            @Override
            public void onTaskFinished(String resuestCode) {


            }
        }, getActivity(), "2", String.valueOf(page));//获取各种订单
        //cityModels = ProtocolBill.getInstance().getCities();
        provinceModels = ProtocolBill.getInstance().getPros();
//        if (null == cityModels || cityModels.isEmpty()) {
//            ProtocolBill.getInstance().getAllCities(this, this);
//        }
        if (null == provinceModels || provinceModels.isEmpty()) {
            ProtocolBill.getInstance().getProvinces(this, getActivity());
        }
        if(user!=null){
            ProtocolBill.getInstance().handset(this,getActivity(),user.getId(),user.getMobile());

        }

    }
    /**
     * 开始轮播图
     */
    public void startScroll() {
        // 只有在列表中数量固定后才能开始
        llDots.removeAllViews();
        bp.stopScroll();
        bp.setOvalLayout(getActivity(), llDots, adModels.size(), R.drawable.sl_info_banner);
        bp.startScroll(getActivity());
    }

    /**
//     * initTitle:初始化标题. <br/>
//     *
//     * @param name
//     * @author BaoHang
//     */
//    public void initTitle(String name) {
//        if (title == null) {
//            title = new CommTitleBar(getActivity());
//        }
//        title.setTitle(name);
//    }

    @Override
    public void onTaskSuccess(BaseModel result) {
        if (RQ_GET_HOME_PICS.equals(result.getRequest_code())) {
            List<ADModel> models = (List<ADModel>) result.getResult();
            if (models != null && !models.isEmpty()) {
                adModels.clear();
                adModels.addAll(models);
                bpAdapter.notifyDataSetChanged();
                startScroll();
            }
        } else if (RQ_GET_HOME_NEWS.equals(result.getRequest_code())) {
            NewsListModel model = (NewsListModel) result.getResult();
            final int localCount = SPUtil.getInt(user.getMobile() + "news_count");
            if (model != null) {
                final List<NewsModel> newsModels = model.getMessagelist();
                try {
                    count = Integer.parseInt(model.getMessagescount());
                    if (localCount < count) {
                        im_right.setVisibility(View.VISIBLE);
                        im_right.setImageResource(R.drawable.ic_news);
                        rl_right.setVisibility(View.VISIBLE);
                        tv_right.setVisibility(View.GONE);
                        rl_right.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(),NewsActivity.class);
                                intent.putExtra("data", (Serializable) newsModels);
                                startActivity(intent);
                                SPUtil.saveInt(user.getMobile() + "news_count", count);

                                im_right.setVisibility(View.VISIBLE);
                                im_right.setImageResource(R.drawable.ic_news);
                                rl_right.setVisibility(View.VISIBLE);
                                tv_right.setVisibility(View.GONE);
                                rl_right.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        Intent intent = new Intent(getActivity(),NewsActivity.class);
                                        intent.putExtra("data", (Serializable) newsModels);
                                        startActivity(intent);
                                    }
                                });
//                                title.setRightIcon(R.drawable.ic_news, new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//
//                                        Intent intent = new Intent(getActivity(),NewsActivity.class);
//                                        intent.putExtra("data", (Serializable) newsModels);
//                                        startActivity(intent);
//                                    }
//                                });
                            }
                        });


                    } else {
                        im_right.setVisibility(View.VISIBLE);
                        im_right.setImageResource(R.drawable.ic_news);
                        rl_right.setVisibility(View.VISIBLE);
                        tv_right.setVisibility(View.GONE);
                        rl_right.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(),NewsActivity.class);
                                intent.putExtra("data", (Serializable) newsModels);
                                startActivity(intent);

                            }
                        });
                    }
                } catch (NumberFormatException e) {
                    im_right.setVisibility(View.VISIBLE);
                    im_right.setImageResource(R.drawable.ic_news);
                    rl_right.setVisibility(View.VISIBLE);
                    tv_right.setVisibility(View.GONE);
                    rl_right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(),NewsActivity.class);
                            intent.putExtra("data", (Serializable) newsModels);
                            startActivity(intent);
                            //startActivity(NewsActivity.class, newsModels);
                        }
                    });

                }
            }
        } else if (RQ_GET_HOME_ORDERS.equals(result.getRequest_code())) {//获取首页订单
            List<HomeOrderModel> models = (List<HomeOrderModel>) result.getResult();

            if (page == 1) {
                list.clear();
                if (models != null && !models.isEmpty()) {
                    ivNoData.setVisibility(View.GONE);
                    list.addAll(models);

                } else {
                    ivNoData.setVisibility(View.VISIBLE);
                }
            } else {
                if (models != null && !models.isEmpty()) {
                    list.addAll(models);
                } else {
                   //ToastUtil.toastShortShow("没有更多订单");
                    sx = "1";
                }
            }
            mAdapter.notifyDataSetChanged();
            //ptrsv.onRefreshComplete();
            rlAD.requestFocus();
        } else if (RQ_ENTER_SEND.equals(result.getRequest_code())) {
            ToastUtil.toastShortShow("确认送达成功!");

                list.remove(pos);


            mAdapter.notifyDataSetChanged();

            netdialog.dismiss();
            Log.v("ggggggggggggggg","确认收货成功");
//            new Handler().postDelayed(new Runnable() {
//                public void run() {
//                    //确认送达成功  停止服务service2
//                    Intent stopLocationServiceIntent2 = new Intent(getActivity(),
//                            LocationService2.class);
//                    getActivity().stopService(stopLocationServiceIntent2);
//                }
//            }, 3000);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    //开启service1
                    Intent startLocationServiceIntent = new Intent(getActivity(), LocationService.class);
                    startLocationServiceIntent.putExtra("id",user.getId());
                    getActivity().startService(startLocationServiceIntent);
                }
            }, 5000);

            if (list.isEmpty()) {
                ivNoData.setVisibility(View.VISIBLE);
            }
            netdialog.dismiss();
        } else if (RQ_GET_REFUSE_ORDER.equals(result.getRequest_code())) {
            ToastUtil.toastShortShow("拒单成功");
            list.remove(pos);
            mAdapter.notifyDataSetChanged();
            if (list.isEmpty()) {
                ivNoData.setVisibility(View.VISIBLE);
            }
            netdialog.dismiss();
//        } else if (RQ_GET_ALL_CITY_LIST.equals(result.getRequest_code())) {
//            cityModels = (List<CityModel>) result.getResult();
//            SPUtil.saveObjectToShare(ProjectConstant.KEY_CITIES, cityModels);
        }else if (RQ_GET_PROVINCE_LIST.equals(result.getRequest_code())){
            provinceModels = (List<ProvinceModel>) result.getResult();
            provinceModels.add(0, new ProvinceModel("", "", "不限"));
            SPUtil.saveObjectToShare(ProjectConstant.KEY_PROS, provinceModels);
        }
    }
    private void goSelect(int tag) {
        EventMessage eventMessage = new EventMessage();
        eventMessage.setTag(tag);
        EventBus.getDefault().post(eventMessage);

    }
    public void saveUser(UserModel model) {
        SPUtil.saveObjectToShare(ProjectConstant.KEY_USERINFO, model);
    }

    @Override
    public void onTaskFail(BaseModel result) {
//        super.onTaskFail(result);
        if (result.getMsgtype() != null && result.getMsgtype().equals("2")) {

            ToastUtil.toastShortShow("登录失效，请重新登录");
            saveUser(null);
            Intent intent = new Intent(getActivity(),LoginActivity.class);
            startActivity(intent);
        } else if (!TextUtils.isEmpty(result.getMsg())) {
            ToastUtil.toastShortShow(result.getMsg() + "");

        }
    }

    @Override
    public void onTaskFinished(String resuestCode) {
        //super.onTaskFinished(resuestCode);

                if (ptrsv.isRefreshing()) {
                    ptrsv.onRefreshComplete();
                }


        if (netdialog.isShowing()) {
            time++;
            if (time != 1) {
                netdialog.dismiss();

            }
            ToastUtil.toastShortShow("运行时异常");
            netdialog.dismiss();
        }
    }

    @Override
    public void onCustomerListener(View v, int position) {
        pos = position;
        switch (v.getId()) {
            case R.id.tv_refuse:
                showRefuseSendDialog();
                break;
            case R.id.tv_accept:
                Map map = new HashMap();//接单按钮
                map.put("state", Integer.parseInt(list.get(pos).getStatus()));
                map.put("id", list.get(pos).getId());
                map.put("from", "4");
                map.put("isReceive", "true");
                ((BaseActivity)getActivity()).startActivity(OrderDetailActivity.class, map);
                break;
            case R.id.tv_confirm_send:


                showConfirmSendDialog();
                break;
            case R.id.rl_navigation:
                Map map1 = new HashMap();

                //收货地址 卸货地址
                shAddress = list.get(pos).getShprovicename() + list.get(pos).getShcityname() //收货地址 ,卸货地址
                        + list.get(pos).getShareaname() + list.get(pos).getShaddress();

                map1.put("city", list.get(pos).getShcityname());
                map1.put("keyword", shAddress);
                map1.put("startCity",list.get(pos).getFhcityname());
                map1.put("startKeyword",list.get(pos).getFhaddress());

                ((BaseActivity)getActivity()).startActivity(RestRouteShowActivity.class, map1);

                break;
        }
    }
    private void showConfirmSendDialog() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_popwindow_info_comment, null);
        Button bt1 = (Button) view
                .findViewById(R.id.item_popupwindows_title);
        Button bt2 = (Button) view
                .findViewById(R.id.item_popupwindows_info_reply);
        Button bt3 = (Button) view
                .findViewById(R.id.item_popupwindows_info_cancel);

        bt1.setText("您确定要确认送达吗？");
        bt2.setText("确定");
        bt3.setText("取消");

        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                netdialog.show();
                confirmDialog.dismiss();
                //发送消息通知发送定位信息
                goSelect(EventMessage.EventMessageAction.TAG_SEEVICE_LOCATION);
                Log.v("ggggggggggggggg","发送定位信息");
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                confirmDialog.cancel();
            }
        });
        confirmDialog = DialogUtil.getDialog(getActivity(), view, Gravity.BOTTOM, true);
        confirmDialog.show();
    }
    private void showRefuseSendDialog() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_popwindow_info_comment, null);
        Button bt1 = (Button) view
                .findViewById(R.id.item_popupwindows_title);
        Button bt2 = (Button) view
                .findViewById(R.id.item_popupwindows_info_reply);
        Button bt3 = (Button) view
                .findViewById(R.id.item_popupwindows_info_cancel);

        bt1.setText("您确定要拒单吗？");
        bt2.setText("确定");
        bt3.setText("取消");

        bt2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                netdialog.show();
                ProtocolBill.getInstance().refuseOrder(HomeFragment.this, getActivity(), list.get(pos).getId());
                refuseDialog.dismiss();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                refuseDialog.cancel();
            }
        });
        refuseDialog = DialogUtil.getDialog(getActivity(), view, Gravity.BOTTOM, true);
        refuseDialog.show();
    }
    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null)
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
    }

}
