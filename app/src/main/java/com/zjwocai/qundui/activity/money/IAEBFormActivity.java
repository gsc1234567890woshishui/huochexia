package com.zjwocai.qundui.activity.money;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.threeti.teamlibrary.activity.BaseProtocolActivity;
import com.threeti.teamlibrary.net.BaseModel;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.activity.login.LoginActivity;
import com.zjwocai.qundui.adapter.FormListAdapter;
import com.zjwocai.qundui.bill.ProtocolBill;
import com.zjwocai.qundui.fragment.MainActivity;
import com.zjwocai.qundui.model.IAEBModel;
import com.zjwocai.qundui.util.RefreshUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 收支明细
 * Created by NieLiQin on 2016/7/25.
 */
public class IAEBFormActivity extends BaseProtocolActivity {

    private ImageView ivNoData;
    private PullToRefreshListView ptrlv;
    private FormListAdapter mAdapter;
    private String from = "0";

    private int page = 1;
    private List<IAEBModel> iaebModels;

    public IAEBFormActivity() {
        super(R.layout.act_form);
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        from = (String) getIntent().getSerializableExtra("data");
        if (null == from || from.equals("")){
            from = "0";
        }
    }

    @Override
    public void findIds() {
        ptrlv = (PullToRefreshListView) findViewById(R.id.ptrlv_form);
        ivNoData = (ImageView) findViewById(R.id.iv_no_data);
        ivNoData.setVisibility(View.GONE);
        ProtocolBill.getInstance().getIAEB(this, this, String.valueOf(page));
    }

    @Override
    public void initViews() {
        initTitle("收支明细");
        title.setLeftIcon(R.drawable.ic_title_left, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ptrlv.setMode(PullToRefreshBase.Mode.BOTH);
        RefreshUtil.setPullText(this, ptrlv);
        ptrlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                page = 1;
                ProtocolBill.getInstance().getIAEB(IAEBFormActivity.this, IAEBFormActivity.this, String.valueOf(page));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                page++;
                ProtocolBill.getInstance().getIAEB(IAEBFormActivity.this, IAEBFormActivity.this, String.valueOf(page));
            }
        });

        iaebModels = new ArrayList();
        mAdapter = new FormListAdapter(this, iaebModels);
        ptrlv.setAdapter(mAdapter);

    }

    @Override
    public void onTaskSuccess(BaseModel result) {
        if (RQ_GET_IAEB.equals(result.getRequest_code())) {
            List<IAEBModel> models = (List<IAEBModel>) result.getResult();
            if (page == 1) {
                iaebModels.clear();
                if (models != null && !models.isEmpty()) {
                    ivNoData.setVisibility(View.GONE);
                    iaebModels.addAll(models);
                } else {
                    ivNoData.setVisibility(View.VISIBLE);
                }
            } else {
                if (models != null && !models.isEmpty()) {
                    iaebModels.addAll(models);
                } else {
                    showToast("没有更多收支明细");
                }
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onTaskFail(@SuppressWarnings("rawtypes") BaseModel result) {
//        super.onTaskFail(result);
        if (result.getMsgtype() != null && result.getMsgtype().equals("2")){
            showToast("登录失效，请重新登录");
            saveUser(null);
            startActivity(LoginActivity.class);
        }else if (!TextUtils.isEmpty(result.getMsg())) {
            showToast(result.getMsg() + "");
        }
    }

    @Override
    public void onTaskFinished(String resuestCode) {
        super.onTaskFinished(resuestCode);
        if (ptrlv.isRefreshing()) {
            ptrlv.onRefreshComplete();
        }
    }

    @Override
    public void onBackPressed() {
        if (from.equals("0")){
            super.onBackPressed();
        } else if (from.equals("-1")){
            startActivity(MainActivity.class);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10086){
            ProtocolBill.getInstance().getIAEB(this, this, String.valueOf(page));
        }
    }
}
