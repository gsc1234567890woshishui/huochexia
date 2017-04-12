package com.zjwocai.qundui.activity.goods;

import android.app.Dialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.threeti.teamlibrary.activity.BaseActivity;
import com.threeti.teamlibrary.finals.RequestCodeSet;
import com.threeti.teamlibrary.net.BaseModel;
import com.threeti.teamlibrary.net.ProcotolCallBack;
import com.zjwocai.qundui.QunDuiApplication;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.activity.login.LoginActivity;
import com.zjwocai.qundui.activity.nav.RestRouteShowActivity;
import com.zjwocai.qundui.adapter.GoodsListAdapter;
import com.zjwocai.qundui.adapter.OnCustomListener;
import com.zjwocai.qundui.bill.ProtocolBill;
import com.zjwocai.qundui.fragment.EventMessage;
import com.zjwocai.qundui.fragmenthome.HomeFragment;
import com.zjwocai.qundui.model.OrderModel;
import com.zjwocai.qundui.util.DialogUtil;
import com.zjwocai.qundui.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 订单列表碎片
 * Created by NieLiQin on 2016/7/25.
 */
public class ListFragment extends MyBaseFragment implements ProcotolCallBack, RequestCodeSet, OnCustomListener {
    private ImageView ivNoData;
    private PullToRefreshListView ptrlv;
    private String type;
    private GoodsListAdapter mAdapter;
    private List<OrderModel> orderModels;
    private int page = 1;
    private Dialog netdialog, confirmDialog, refuseDialog;
    private int pos;
    private String shAddress;
    private int time;
    private String sx="0";

    @Override
    protected int getLayoutId() {
        View ret = LayoutInflater.from(getActivity()).inflate(R.layout.frag_goods, null);

        return R.layout.frag_goods;
    }
    public ListFragment() {

    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        EventBus.getDefault().register(this);
        Log.v("ggggggggggggg","注册");
        ptrlv = (PullToRefreshListView) root.findViewById(R.id.ptrlv_goods_list);
        ivNoData = (ImageView) root.findViewById(R.id.iv_no_data);
        ivNoData.setVisibility(View.GONE);
        initData();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setGoIndex(EventMessage eventMessage){
        //Log.d(TAG, "setGoIndex: "+eventMessage.getTag());
        if(eventMessage!=null) {
            int tag = eventMessage.getTag();
            if (tag == EventMessage.EventMessageAction.TAG_REFRESH_GOODS) {
                getOrders();
                ptrlv.setRefreshing();
            }
            if (tag == EventMessage.EventMessageAction.TAG_REFRESH_CHILD) {
                getOrders();
                ptrlv.setRefreshing();
            }
            if (tag == EventMessage.EventMessageAction.TAG_STOP_LOCATION_LISTFRAGMENT) { //确认收货
//                EventBus.getDefault().unregister(this);
                //等service停止后再确认收货
                ProtocolBill.getInstance().enterSend(new ProcotolCallBack() {
                    @Override
                    public void onTaskSuccess(BaseModel result) {
                        ToastUtil.toastShortShow(QunDuiApplication.getInstance(), "确认送达成功！");
                        Log.v("ggggggggggggggg","确认送达成功");
                        orderModels.remove(pos);
                        mAdapter.notifyDataSetChanged();
                        if (orderModels.isEmpty()) {
                            ivNoData.setVisibility(View.VISIBLE);
                        }
                        netdialog.dismiss();

                    }

                    @Override
                    public void onTaskFail(BaseModel result) {
                        if (ptrlv.isRefreshing()) {
                            ptrlv.onRefreshComplete();
                        }
                        if (netdialog.isShowing()) {
                            time++;
                            if (time != 1) {
                                netdialog.dismiss();

                            }
                            ToastUtil.toastShortShow("运行时异常");
                        }

                    }

                    @Override
                    public void onTaskFinished(String resuestCode) {
                        if (ptrlv.isRefreshing()) {
                            ptrlv.onRefreshComplete();
                        }
                        if (netdialog.isShowing()) {
                            time++;
                            if (time != 1) {
                                netdialog.dismiss();
                            }
                        }

                    }
                },  getActivity(), orderModels.get(pos).getId(), "");

            }
        }
    }
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if(ptrlv!=null){
//            autoRefresh();
//        }
//
//    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);//if是可见的就提示舒心情况
        if(sx.equals("1")){//如果是0就提示
            //ToastUtil.toastShortShow("没有更多订单");
            sx = "0";
        }
    }




    public void initData() {
        netdialog = DialogUtil.getProgressDialog(getActivity(), getString(R.string.ui_net_request));
        ptrlv.setMode(PullToRefreshBase.Mode.BOTH);
        ptrlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                page = 1;
                getOrders();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                page++;
                getOrders();
            }
        });

        type = (String) getArguments().get("type");
        getOrders();
        boolean flag = false;
        if (type.equals("all")) {
            flag = true;
        }
        orderModels = new ArrayList();
        mAdapter = new GoodsListAdapter((BaseActivity) getActivity(), orderModels, flag);
        mAdapter.setOnCustomListener(this);
        ptrlv.setAdapter(mAdapter);
        ptrlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map map = new HashMap();
                if (type.equals("all")) {
                    int state = 0;
                    String type = orderModels.get((int) id).getStatus();
                    if (type != null && !type.equals("")) {
                        state = Integer.parseInt(type);
                    }
                    map.put("state", state);
                    map.put("id", orderModels.get((int) id).getId());
                    map.put("from", "3");
                    ((BaseActivity) getActivity()).startActivity(OrderDetailActivity.class, map);
                } else if (type.equals("ing")) {
                    map.put("state", 2);
                    map.put("id", orderModels.get((int) id).getId());
                    map.put("from", "1");
                    ((BaseActivity) getActivity()).startActivity(OrderDetailActivity.class, map);
                } else if (type.equals("finish")) {
                    map.put("state", 3);
                    map.put("id", orderModels.get((int) id).getId());
                    map.put("from", "2");
                    ((BaseActivity) getActivity()).startActivity(OrderDetailActivity.class, map);
                } else if (type.equals("not")) {
                    map.put("state", 1);
                    map.put("id", orderModels.get((int) id).getId());
                    map.put("from", "0");
                    ((BaseActivity) getActivity()).startActivity(OrderDetailActivity.class, map);
                }
            }
        });
        //autoRefresh();
    }



    private void getOrders() {
        if (type.equals("all")) {
            ProtocolBill.getInstance().getAllOrders(ListFragment.this, getActivity(), String.valueOf(page));
        } else if (type.equals("ing")) {
            ProtocolBill.getInstance().getKindOrders(ListFragment.this, getActivity(), "2", String.valueOf(page));
        } else if (type.equals("finish")) {
            ProtocolBill.getInstance().getKindOrders(ListFragment.this,  getActivity(), "3", String.valueOf(page));
        } else if (type.equals("not")) {
            ProtocolBill.getInstance().getKindOrders(ListFragment.this, getActivity(), "1", String.valueOf(page));
        }
    }

    @Override
    public void onTaskSuccess(BaseModel result) {
        if (RQ_GET_ALL_ORDERS.equals(result.getRequest_code())) {
            List<OrderModel> models = (List<OrderModel>) result.getResult();
            if (page == 1) {
                orderModels.clear();
                if (models != null && !models.isEmpty()) {
                    ivNoData.setVisibility(View.GONE);
                    orderModels.addAll(models);
                } else {
                    ivNoData.setVisibility(View.VISIBLE);
                }
            } else {
                if (models != null && !models.isEmpty()) {
                    orderModels.addAll(models);
                } else {
                    sx = "1";
                    //ToastUtil.toastShortShow("没有更多订单");
                }
            }
            mAdapter.notifyDataSetChanged();
        } else if (RQ_GET_KIND_ORDERS.equals(result.getRequest_code())) {
            List<OrderModel> models = (List<OrderModel>) result.getResult();
            if (page == 1) {
                orderModels.clear();
                if (models != null && !models.isEmpty()) {
                    ivNoData.setVisibility(View.GONE);
                    orderModels.addAll(models);
                } else {
                    ivNoData.setVisibility(View.VISIBLE);
                }
            } else {
                if (models != null && !models.isEmpty()) {
                    orderModels.addAll(models);
                } else {
                    //ToastUtil.toastShortShow("没有更多订单");
                }
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onTaskFail(BaseModel result) {
        if (result.getMsgtype() != null && result.getMsgtype().equals("2")) {
            ToastUtil.toastShortShow("登录失效，请重新登录");
            ((BaseActivity) getActivity()).saveUser(null);
            ((BaseActivity) getActivity()).startActivity(LoginActivity.class);
        } else if (!TextUtils.isEmpty(result.getMsg())) {
            ToastUtil.toastShortShow(result.getMsg() + "");
        }
    }

    @Override
    public void onTaskFinished(String resuestCode) {
        if (ptrlv.isRefreshing()) {
            ptrlv.onRefreshComplete();
        }
    }

    public static void refresh(){





    }

//    @Override
//    public void onTaskFinished(String resuestCode) {
//        //super.onTaskFinished(resuestCode);
//        if (ptrlv.isRefreshing()) {
//            ptrlv.onRefreshComplete();
//        }
//        if (netdialog.isShowing()) {
//            time++;
//            if (time != 1) {
//                netdialog.dismiss();
//            }
//        }
//    }

    @Override
    public void onCustomerListener(View v, int position) {
        pos = position;
        switch (v.getId()) {
            case R.id.tv_refuse:
                showRefuseSendDialog();
                break;
            case R.id.tv_accept:
                Map map = new HashMap();
                map.put("state", Integer.parseInt(orderModels.get(pos).getStatus()));
                map.put("id", orderModels.get(pos).getId());
                map.put("from", "4");
                map.put("isReceive", "true");
                ((BaseActivity) getActivity()).startActivity(OrderDetailActivity.class, map);
                break;
            case R.id.tv_confirm_send:
                showConfirmSendDialog();
                break;
            case R.id.rl_navigation:
                Map map1 = new HashMap();

                //收货地址 卸货地址
                shAddress = orderModels.get(pos).getShprovicename() + orderModels.get(pos).getShcityname() //收货地址 ,卸货地址
                        + orderModels.get(pos).getShareaname() + orderModels.get(pos).getShaddress();

                map1.put("city", orderModels.get(pos).getShcityname());
                map1.put("keyword", shAddress);
                map1.put("startCity", orderModels.get(pos).getFhcityname());
                map1.put("startKeyword", orderModels.get(pos).getFhaddress());

                ((BaseActivity) getActivity()).startActivity(RestRouteShowActivity.class, map1);
                break;
        }


    }
    private void goSelect(int tag) {
        EventMessage eventMessage = new EventMessage();
        eventMessage.setTag(tag);
        EventBus.getDefault().post(eventMessage);

    }

    private void showConfirmSendDialog() {
        View view = LayoutInflater.from((BaseActivity) getActivity()).inflate(R.layout.view_popwindow_info_comment, null);
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
                //点击确认送达
                //发送消息通知发送定位信息
                goSelect(EventMessage.EventMessageAction.TAG_SEEVICE_LOCATION_LISTFRAGMENT);
                Log.v("ggggggggggggggg","发送定位信息");


            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                confirmDialog.cancel();
            }
        });
        confirmDialog = DialogUtil.getDialog((BaseActivity) getActivity(), view, Gravity.BOTTOM, true);
        confirmDialog.show();
    }

    private void showRefuseSendDialog() {
        View view = LayoutInflater.from((BaseActivity) getActivity()).inflate(R.layout.view_popwindow_info_comment, null);
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
                ProtocolBill.getInstance().refuseOrder(new ProcotolCallBack() {
                    @Override
                    public void onTaskSuccess(BaseModel result) {
                        ToastUtil.toastShortShow(QunDuiApplication.getInstance(), "拒单成功！");
                        orderModels.remove(pos);
                        mAdapter.notifyDataSetChanged();
                        if (orderModels.isEmpty()) {
                            ivNoData.setVisibility(View.VISIBLE);
                        }
                        netdialog.dismiss();
                    }

                    @Override
                    public void onTaskFail(BaseModel result) {

                    }

                    @Override
                    public void onTaskFinished(String resuestCode) {

                    }
                },  getActivity(), orderModels.get(pos).getId());
                refuseDialog.dismiss();

            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                refuseDialog.cancel();
            }
        });
        refuseDialog = DialogUtil.getDialog((BaseActivity) getActivity(), view, Gravity.BOTTOM, true);
        refuseDialog.show();
    }

}
