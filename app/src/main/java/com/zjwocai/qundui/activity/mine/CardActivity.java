package com.zjwocai.qundui.activity.mine;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.threeti.teamlibrary.activity.BaseActivity;
import com.threeti.teamlibrary.activity.BaseProtocolActivity;
import com.threeti.teamlibrary.net.BaseModel;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.activity.login.LoginActivity;
import com.zjwocai.qundui.adapter.CardsAdapter;
import com.zjwocai.qundui.adapter.OnCustomListener;
import com.zjwocai.qundui.bill.ProtocolBill;
import com.zjwocai.qundui.model.CarModel;
import com.zjwocai.qundui.model.CardModel;
import com.zjwocai.qundui.model.TypeModel;
import com.zjwocai.qundui.util.DialogUtil;
import com.zjwocai.qundui.util.RefreshUtil;
import com.zjwocai.qundui.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的银行卡
 * Created by NieLiQin on 2016/7/26.
 */
public class CardActivity extends BaseProtocolActivity implements OnCustomListener {
    private ImageView ivNoData;
    private PullToRefreshListView ptrlv;
    private LinearLayout llCards;
    private CardsAdapter mAdapter;
    private int page = 1;
    private List<CardModel> cardModels;
    private Dialog netdialog;
    private int pos;

    @Override
    public void getIntentData() {
        super.getIntentData();
        ProtocolBill.getInstance().getCards(this,this,String.valueOf(page));
    }

    public CardActivity() {
        super(R.layout.act_card);
    }

    @Override
    public void findIds() {
        ptrlv = (PullToRefreshListView) findViewById(R.id.ptrlv_cards);
        llCards = (LinearLayout) findViewById(R.id.ll_cards);
        ivNoData = (ImageView) findViewById(R.id.iv_no_data);
        ivNoData.setVisibility(View.GONE);
    }

    @Override
    public void initViews() {
        initTitle("我的银行卡");
        title.setLeftIcon(R.drawable.ic_title_left, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title.setRightIcon(R.drawable.ic_add, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(CardAddActivity.class,null,0);
            }
        });
        DialogUtil.showWaitDialog(this,"正在加载。。。");
        ptrlv.setMode(PullToRefreshBase.Mode.BOTH);
        RefreshUtil.setPullText(this,ptrlv);

        ptrlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                page = 1;
                ProtocolBill.getInstance().getCards(CardActivity.this,CardActivity.this,String.valueOf(page));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                page ++;
                ProtocolBill.getInstance().getCards(CardActivity.this,CardActivity.this,String.valueOf(page));
            }
        });

        cardModels = new ArrayList();
        mAdapter = new CardsAdapter(this,cardModels);
        mAdapter.setOnCustomListener(this);
        ptrlv.setAdapter(mAdapter);
        netdialog = DialogUtil.getProgressDialog(this,getString(R.string.ui_net_request));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null){
            boolean flag = data.getBooleanExtra("data",true);
            if (flag){
                page = 1;
                ProtocolBill.getInstance().getCards(this,this,String.valueOf(page));
            }
        }
    }

    @Override
    public void onCustomerListener(View v, int position) {
        pos = position;
        switch (v.getId()){
            case R.id.tv_manager:
                new PopupWindows(this,llCards,position);
                break;
        }
    }

    @Override
    public void onTaskSuccess(BaseModel result) {
        if (RQ_GET_CARDS.equals(result.getRequest_code())){
            List<CardModel> models = (List<CardModel>) result.getResult();

            if (page == 1) {
                cardModels.clear();
                if (models != null && !models.isEmpty()) {
                    ivNoData.setVisibility(View.GONE);
                    cardModels.addAll(models);
                    DialogUtil.hideWaitDialog();
                } else {
                    ivNoData.setVisibility(View.VISIBLE);
                }
            } else {
                if (models != null && !models.isEmpty()) {
                    cardModels.addAll(models);
                } else {
                    ToastUtil.toastShortShow("没有更多银行卡");
                }
            }
            mAdapter.notifyDataSetChanged();
        } else if (RQ_DELETE_CARD.equals(result.getRequest_code())){
            showToast("删除银行卡成功!");
            cardModels.remove(pos);
            mAdapter.notifyDataSetChanged();
            if (cardModels.isEmpty()){
                ivNoData.setVisibility(View.VISIBLE);
            }
            netdialog.dismiss();

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
        DialogUtil.hideWaitDialog();
        if (netdialog.isShowing()){
            netdialog.dismiss();
        }
        if (ptrlv.isRefreshing()){
            ptrlv.onRefreshComplete();
        }
    }

    class PopupWindows extends PopupWindow {

        public PopupWindows(Context mContext, View parent, final int position) {

            View view = View
                    .inflate(mContext, R.layout.view_popwindow_car, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.push_bottom_in_2));

            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            Button bt1 = (Button) view
                    .findViewById(R.id.item_popupwindows_info_report);
            Button bt2 = (Button) view
                    .findViewById(R.id.item_popupwindows_info_reply);
            Button bt3 = (Button) view
                    .findViewById(R.id.item_popupwindows_info_cancel);

            bt1.setText("是否删除 “尾号" + cardModels.get(position).getCode().substring(
                    cardModels.get(position).getCode().length() - 4,
                    cardModels.get(position).getCode().length()) + "” " +
                    TypeModel.getCarDType(cardModels.get(position).getType()) + "银行卡");

            bt2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    netdialog.show();
                    ProtocolBill.getInstance().deleteCard(CardActivity.this,CardActivity.this,cardModels.get(position).getId());
                    dismiss();
                }
            });
            bt3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });

        }
    }
}
