package com.zjwocai.qundui.activity.goods;

import android.app.Dialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;


import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.threeti.teamlibrary.activity.BaseProtocolActivity;
import com.threeti.teamlibrary.net.BaseModel;
import com.threeti.teamlibrary.utils.SPUtil;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.activity.login.LoginActivity;
import com.zjwocai.qundui.adapter.GoodsListAdapter;
import com.zjwocai.qundui.adapter.OnCustomListener;
import com.zjwocai.qundui.adapter.SearchHistoryAdapter;
import com.zjwocai.qundui.bill.ProtocolBill;
import com.zjwocai.qundui.model.OrderModel;
import com.zjwocai.qundui.util.DialogUtil;
import com.zjwocai.qundui.util.InputUtil;
import com.zjwocai.qundui.util.RefreshUtil;
import com.zjwocai.qundui.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 搜索
 * Created by NieLiQin on 2016/7/25.
 */
public class SearchActivity extends BaseProtocolActivity implements View.OnClickListener, TextWatcher, OnCustomListener {
    private EditText et;
    private ImageView ivNoData;
    private RelativeLayout rlLeft;
    private LinearLayout llHistory, llResult;
    private ListView lv;
    private PullToRefreshListView ptrlv;
    private List<String> mItems;
    private List<String> history;
    private SearchHistoryAdapter mAdapter;
    private List<OrderModel> orderModels;
    private Dialog netdialog;
    private GoodsListAdapter mResultAdapter;
    private int page = 1;
    private String keyword = "";

    public SearchActivity() {
        super(R.layout.act_search);
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        mItems = (List<String>) SPUtil.getObjectFromShare("history");
        if (mItems == null) {
            mItems = new ArrayList<>();
        }
    }

    @Override
    public void findIds() {
        rlLeft = (RelativeLayout) findViewById(R.id.rl_left);
        lv = (ListView) findViewById(R.id.lv_search_history);
        et = (EditText) findViewById(R.id.et_search);
        llHistory = (LinearLayout) findViewById(R.id.ll_history);
        llResult = (LinearLayout) findViewById(R.id.ll_result);
        ptrlv = (PullToRefreshListView) findViewById(R.id.ptrlv_search);
        ivNoData = (ImageView) findViewById(R.id.iv_no_data);
        ivNoData.setVisibility(View.GONE);

        llResult.setVisibility(View.GONE);
    }

    @Override
    public void initViews() {
        history = new ArrayList<>();
        mAdapter = new SearchHistoryAdapter(this, history);
        lv.setAdapter(mAdapter);
        mAdapter.setOnCustomListener(this);
        checkHistory();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id == history.size() - 1) {
                    mItems.clear();
                    SPUtil.saveObjectToShare("history", mItems);
                    checkHistory();
                } else {
                    page = 1;
                    netdialog.show();
                    llHistory.setVisibility(View.GONE);
                    llResult.setVisibility(View.VISIBLE);
                    et.setText(mItems.get((int) id));
                    keyword = mItems.get((int) id);
                    ProtocolBill.getInstance().searchOrder(SearchActivity.this, SearchActivity.this,
                            mItems.get((int) id), String.valueOf(page));
                    InputUtil.hideInputMethdView(SearchActivity.this, et);
                }
            }
        });


        ptrlv.setMode(PullToRefreshBase.Mode.BOTH);
        RefreshUtil.setPullText(this, ptrlv);
        ptrlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                if (keyword != null && !keyword.equals("")) {
                    page = 1;
                    ProtocolBill.getInstance().searchOrder(SearchActivity.this, SearchActivity.this,
                            keyword, String.valueOf(page));
                } else {
                    ptrlv.onRefreshComplete();
                }

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                if (keyword != null && !keyword.equals("")) {
                    page++;
                    ProtocolBill.getInstance().searchOrder(SearchActivity.this, SearchActivity.this,
                            keyword, String.valueOf(page));
                } else {
                    ptrlv.onRefreshComplete();
                }
            }
        });
        orderModels = new ArrayList<>();
        mResultAdapter = new GoodsListAdapter(this, orderModels, true);
        ptrlv.setAdapter(mResultAdapter);
        ptrlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map map = new HashMap();
                map.put("state", Integer.parseInt(orderModels.get((int) id).getStatus()));
                map.put("id", orderModels.get((int) id).getId());
                map.put("from", "5");
                startActivity(OrderDetailActivity.class, map);
            }
        });


        rlLeft.setOnClickListener(this);
        et.addTextChangedListener(this);

        InputUtil.searchClick(this, et, "搜索内容不能为空！", new InputUtil.SearchActionListener() {
            @Override
            public void searchAction() {
                Integer i = (Integer) et.getTag();    //用于解决重复调动接口（暂时解决方法）
                if (null == i) {
                    i = 0;
                }
                if (null != i && i < 1) {
                    i++;
                    keyword = et.getText().toString().trim();
                    checkList(keyword);
                    checkHistory();
                    SPUtil.saveObjectToShare("history", mItems);
                    llHistory.setVisibility(View.GONE);
                    llResult.setVisibility(View.VISIBLE);
                    page = 1;
                    netdialog.show();
                    ProtocolBill.getInstance().searchOrder(SearchActivity.this, SearchActivity.this,
                            keyword, String.valueOf(page));
                } else {
                    i = 0;
                }
                et.setTag(i);
            }
        });

        netdialog = DialogUtil.getProgressDialog(this, getString(R.string.ui_net_request));
    }

    @Override
    protected void onPause() {
        InputUtil.hideInputMethdView(this, et);
        super.onPause();
    }

    private boolean check() {
        if (et.getText().toString().trim().equals("")) {
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_left:
                finish();
                break;
        }
    }

    private void checkHistory() {
        history.clear();
        history.addAll(mItems);
        if (history.size() > 0) {
            history.add("");
        }
        mAdapter.notifyDataSetChanged();
    }

    private void checkList(String item) {
        for (int i = 0; i < mItems.size(); i++) {
            if (mItems.get(i).equals(item) || mItems.get(i) == item) {
                mItems.remove(i);
            }
        }
        mItems.add(0, item);

        if (mItems.size() > 10) {
            mItems.remove(mItems.size() - 1);
        }
    }

    @Override
    public void onTaskSuccess(BaseModel result) {
        if (RQ_SEARCH_ORDERS.equals(result.getRequest_code())) {
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
                    ToastUtil.toastShortShow("没有更多结果");
                }
            }
            mResultAdapter.notifyDataSetChanged();
            ProtocolBill.getInstance().uploadSearch(this, this, keyword);
        }
    }

    @Override
    public void onTaskFail(@SuppressWarnings("rawtypes") BaseModel result) {
//        super.onTaskFail(result);
        if (result.getMsgtype() != null && result.getMsgtype().equals("2")) {
            showToast("登录失效，请重新登录");
            saveUser(null);
            startActivity(LoginActivity.class);
        } else if (!TextUtils.isEmpty(result.getMsg())) {
            if (!RQ_UPLOAD_SEARCH.equals(result.getRequest_code())) {
                showToast(result.getMsg() + "");
            }
        }
    }

    @Override
    public void onTaskFinished(String resuestCode) {
        super.onTaskFinished(resuestCode);
        if (netdialog.isShowing()) {
            netdialog.dismiss();
        }
        if (ptrlv.isRefreshing()) {
            ptrlv.onRefreshComplete();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(s)) {
            llHistory.setVisibility(View.VISIBLE);
            llResult.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onCustomerListener(View v, int position) {
        switch (v.getId()) {
            case R.id.rl_delete:
                mItems.remove(position);
                SPUtil.saveObjectToShare("history", mItems);
                checkHistory();
                break;
        }
    }
}
