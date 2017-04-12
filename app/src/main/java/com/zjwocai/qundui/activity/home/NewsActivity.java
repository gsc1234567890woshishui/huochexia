package com.zjwocai.qundui.activity.home;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.threeti.teamlibrary.ApplicationEx;
import com.threeti.teamlibrary.activity.BaseActivity;
import com.threeti.teamlibrary.activity.BaseProtocolActivity;
import com.threeti.teamlibrary.net.BaseModel;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.activity.login.LoginActivity;
import com.zjwocai.qundui.adapter.HomeNewsAdapter;
import com.zjwocai.qundui.bill.ProtocolBill;
import com.zjwocai.qundui.model.HomeOrderModel;
import com.zjwocai.qundui.model.NewsListModel;
import com.zjwocai.qundui.model.NewsModel;
import com.zjwocai.qundui.util.RefreshUtil;
import com.zjwocai.qundui.util.ToastUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息
 * Created by NieLiQin on 2016/7/25.
 */
public class NewsActivity extends BaseProtocolActivity {
    private PullToRefreshListView ptrlv;
    private ImageView ivNoData;
    private HomeNewsAdapter mAdapter;
    private List<NewsModel> newsModels;
    private String flag = "";

    private int page = 1;

    public NewsActivity() {
        super(R.layout.act_news);
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        newsModels = (List<NewsModel>) getIntent().getSerializableExtra("data");
        if (newsModels == null || newsModels.isEmpty()){
            newsModels = new ArrayList<>();
            ProtocolBill.getInstance().getHomeNews(NewsActivity.this,NewsActivity.this,"1");
        }
    }

    @Override
    public void findIds() {
        ptrlv = (PullToRefreshListView) findViewById(R.id.ptrlv_news);
        ivNoData = (ImageView) findViewById(R.id.iv_no_data);

        ivNoData.setVisibility(View.GONE);
    }

    @Override
    public void initViews() {
        initTitle("消息");
        title.setLeftIcon(R.drawable.ic_title_left, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ptrlv.setMode(PullToRefreshBase.Mode.BOTH);
        RefreshUtil.setPullText(this,ptrlv);
        ptrlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                page = 1;
                ProtocolBill.getInstance().getHomeNews(NewsActivity.this,NewsActivity.this,String.valueOf(page));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                page ++;
                ProtocolBill.getInstance().getHomeNews(NewsActivity.this,NewsActivity.this,String.valueOf(page));
            }
        });

        mAdapter = new HomeNewsAdapter(this,newsModels);
        ptrlv.setAdapter(mAdapter);
        ptrlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map map = new HashMap();
                map.put("content",newsModels.get((int) id).getContent());
                map.put("flag","0");
                startActivity(NewsDetailActivity.class,map);
            }
        });
    }

    @Override
    public void onTaskSuccess(BaseModel result) {
        if (RQ_GET_HOME_NEWS.equals(result.getRequest_code())){
            NewsListModel model = (NewsListModel) result.getResult();
            if (page == 1) {
                newsModels.clear();
                if (model.getMessagelist() != null && !model.getMessagelist().isEmpty()) {
                    ivNoData.setVisibility(View.GONE);
                    newsModels.addAll(model.getMessagelist());
                } else {
                    ivNoData.setVisibility(View.VISIBLE);
                }
            } else {
                if (model.getMessagelist() != null && !model.getMessagelist().isEmpty()) {
                    newsModels.addAll(model.getMessagelist());
                } else {
                    ToastUtil.toastShortShow("没有更多消息");
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
        if (ptrlv.isRefreshing()){
            ptrlv.onRefreshComplete();
        }
    }
}
