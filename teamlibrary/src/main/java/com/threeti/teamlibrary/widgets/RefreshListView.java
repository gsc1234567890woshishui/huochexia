package com.threeti.teamlibrary.widgets;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.threeti.teamlibrary.R;
import com.threeti.teamlibrary.activity.BaseActivity;
import com.threeti.teamlibrary.widgets.PullToRefreshView.OnFooterRefreshListener;
import com.threeti.teamlibrary.widgets.PullToRefreshView.OnHeaderRefreshListener;

import java.util.List;


public class RefreshListView {
    private BaseActivity activity;
    private PullToRefreshView refreshView;
    private ListView listview;
    private TextView tv_nodata;
    private OnHeaderRefreshListener headRefresh;
    private OnFooterRefreshListener footRefreshListener;
    private boolean headHide = true;
    private boolean footHide = true;
    protected List mList;
    private BaseAdapter adapter;
    private PullEvent pullEvent;
    private OnItemClickListener onItemClickListener;
    private boolean canrefresh = true;
    private boolean canLoad = true;

    public RefreshListView(Activity activity,
                           OnItemClickListener onItemClickListener, List list,
                           BaseAdapter adapter, PullEvent pullEvent) {
        this(activity, onItemClickListener, list, adapter, pullEvent, null);
    }

    public RefreshListView(Activity activity,
                           OnItemClickListener onItemClickListener, List list,
                           BaseAdapter adapter, PullEvent pullEvent, View headView) {
        this.activity = (BaseActivity) activity;
        this.mList = list;
        this.adapter = adapter;
        this.pullEvent = pullEvent;
        this.onItemClickListener = onItemClickListener;
        refreshView = (PullToRefreshView) activity
                .findViewById(R.id.pull_refresh_view);
        listview = (ListView) activity.findViewById(R.id.lv_common);
        tv_nodata = (TextView) activity.findViewById(R.id.tv_nodata);
        initViews(headView);
        if (!canrefresh) {
            refreshView.setVisibility(View.VISIBLE);
            listview.setVisibility(View.VISIBLE);
            tv_nodata.setVisibility(View.GONE);
        }

    }

    public void setUpPullState(boolean canupPull) {
        if (canupPull) {
            refreshView.enableScroolUp();
        } else {
            refreshView.disableScroolUp();
        }

    }

    public void initViews(View headView) {
        if (headView != null) {
            listview.addHeaderView(headView);
            canrefresh = false;
        }
        if (adapter != null)
            listview.setAdapter(adapter);
        initEvents();

    }

    public boolean isCanrefresh() {
        return canrefresh;
    }

    public void setCanrefresh(boolean canrefresh) {
        this.canrefresh = canrefresh;
    }

    public void disableScroolUp() {
        refreshView.disableScroolUp();
    }

    public void disableScroolDown() {
        refreshView.disableScroolDown();
    }


    private void initEvents() {
        headRefresh = new OnHeaderRefreshListener() {

            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                if (headHide)
                    view.onHeaderRefreshComplete();
                if (pullEvent != null)
                    pullEvent.refreshEvent();

            }
        };
        footRefreshListener = new OnFooterRefreshListener() {

            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                if (footHide)
                    view.onFooterRefreshComplete();
                if (pullEvent != null) {
                    if (mList == null || mList.size() == 0)
                        pullEvent.refreshEvent();
                    else
                        pullEvent.loadMoreEvent();
                }

            }
        };
        if (headRefresh != null)
            refreshView.setOnHeaderRefreshListener(headRefresh);
        if (footRefreshListener != null)
            refreshView.setOnFooterRefreshListener(footRefreshListener);
        if (onItemClickListener != null)
            listview.setOnItemClickListener(onItemClickListener);
        tv_nodata.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (pullEvent != null)
                    pullEvent.refreshEvent();
            }
        });
    }

    public PullToRefreshView getRefreshView() {
        return refreshView;
    }

    public ListView getListview() {
        return listview;
    }

    public interface PullEvent {
        public abstract void refreshEvent();// ˢ�²���

        public abstract void loadMoreEvent();// ���ظ���
    }

    public void initListView(List list) {
        mList.clear();
        if (list != null)
            mList.addAll(list);
        adapter.notifyDataSetChanged();
        if (canrefresh)
            if (mList.size() == 0) {
//				refreshView.setVisibility(View.GONE);
//				listview.setVisibility(View.GONE);
//				tv_nodata.setVisibility(View.VISIBLE);
                refreshView.setVisibility(View.GONE);
                listview.setVisibility(View.GONE);
                tv_nodata.setVisibility(View.VISIBLE);
            } else {
                refreshView.setVisibility(View.VISIBLE);
                listview.setVisibility(View.VISIBLE);
                tv_nodata.setVisibility(View.GONE);
            }
    }

    public void loadMoreList(List list) {
        if (list == null || list.size() == 0)
            activity.showToast("没有更多数据");
        else {
            mList.addAll(list);
            adapter.notifyDataSetChanged();
        }

    }
}
