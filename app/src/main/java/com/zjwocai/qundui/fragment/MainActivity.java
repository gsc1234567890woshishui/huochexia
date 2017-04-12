package com.zjwocai.qundui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.threeti.teamlibrary.activity.BaseActivity;
import com.threeti.teamlibrary.activity.BaseProtocolActivity;
import com.threeti.teamlibrary.finals.ProjectConstant;
import com.threeti.teamlibrary.utils.SPUtil;
import com.threeti.teamlibrary.utils.ToastUtil;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.fragmenthome.HomeFragment;
import com.zjwocai.qundui.fragmenthome.MessageFragment;
import com.zjwocai.qundui.fragmenthome.MineFragment;
import com.zjwocai.qundui.fragmenthome.ShopcartFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends BaseProtocolActivity {

    private static final String TAG = "vivi";
    @Bind(R.id.content)
    FrameLayout mContent;
    @Bind(R.id.rbHome)
    RadioButton mRbHome;
    @Bind(R.id.rbShop)
    RadioButton mRbShop;
    @Bind(R.id.rbMessage)
    RadioButton mRbMessage;
    @Bind(R.id.rbMine)
    RadioButton mRbMine;
    @Bind(R.id.rgTools)
    RadioGroup  mRgTools;
    private Fragment[] mFragments;
    public boolean isPush = true;

    private int mIndex;
    /**
     * 是否需要关闭app
     */
    public boolean needFinish = false;
    /**
     * 最后一次返回按钮操作事件
     */
    private long lastEventTime;
    /**
     * 推出app的等待时间
     */
    private static int TIME_TO_WAIT = 3 * 1000;

    public MainActivity() {
        super(R.layout.activity_main);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initFragment();

    }
    private void initFragment() {
        //首页
        HomeFragment homeFragment =new HomeFragment();
        //购物车
        ShopcartFragment shopcartFragment =new ShopcartFragment();

        //消息
        MessageFragment messageFragment =new MessageFragment();
        //个人中心

        MineFragment mineFragment =new MineFragment();

        //添加到数组
        mFragments = new Fragment[]{homeFragment,shopcartFragment,messageFragment,mineFragment};

        //开启事务

        FragmentTransaction ft =
                getSupportFragmentManager().beginTransaction();

        //添加首页
        ft.add(R.id.content,homeFragment).commit();

        //默认设置为第0个
        setIndexSelected(0);


    }
    private void setIndexSelected(int index) {

        if(mIndex==index){
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft  = fragmentManager.beginTransaction();


        //隐藏
        ft.hide(mFragments[mIndex]);
        //判断是否添加
        if(!mFragments[index].isAdded()){
            ft.add(R.id.content,mFragments[index]).show(mFragments[index]);
        }else {
            ft.show(mFragments[index]);
        }

        ft.commit();
        //再次赋值
        mIndex=index;

    }

    @OnClick({R.id.rbHome, R.id.rbShop, R.id.rbMessage, R.id.rbMine})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rbHome:
                setIndexSelected(0);
                break;
            case R.id.rbShop:
                setIndexSelected(1);
                //发消息给子fragment
                 //goSelect(EventMessage.EventMessageAction.TAG_REFRESH_CHILD);
                //ShopcartFragment.onHiddenChanged();
                break;
            case R.id.rbMessage:
                setIndexSelected(2);
                break;
            case R.id.rbMine:
                setIndexSelected(3);
                break;
        }

    }

    private void goSelect(int tag) {
        EventMessage eventMessage = new EventMessage();
        eventMessage.setTag(tag);
        EventBus.getDefault().post(eventMessage);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setGoIndex(EventMessage eventMessage){
        Log.d(TAG, "setGoIndex: "+eventMessage.getTag());
        if(eventMessage!=null){
            int tag = eventMessage.getTag();

            if(tag== EventMessage.EventMessageAction.TAG_GO_MAIN){
                mRbHome.performClick();
                setIndexSelected(0);
            }else if(tag== EventMessage.EventMessageAction.TAG_GO_SHOPCART){
                mRbShop.performClick();

                setIndexSelected(1);
            }else if(tag== EventMessage.EventMessageAction.TAG_GO_MESSAGE){
                mRbMessage.performClick();
                setIndexSelected(2);
            }


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
       //发消息让fragment刷新
        goSelect(EventMessage.EventMessageAction.TAG_REFRESH_RESUME);
         //mRbHome.performClick();
      //发消息让第二个fragment刷新
        goSelect(EventMessage.EventMessageAction.TAG_REFRESH_GOODS);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        System.out.println("调用点击返回键的方法");
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意
            intent.addCategory(Intent.CATEGORY_HOME);
            this.startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
//    @Override
//    public void onBackPressed() {
//        if (needFinish) {
//            long currentEventTime = System.currentTimeMillis();
//            if ((currentEventTime - lastEventTime) > TIME_TO_WAIT) {
//                ToastUtil.toastShow(this, com.threeti.teamlibrary.R.string.tip_finishapp, TIME_TO_WAIT);
//                lastEventTime = currentEventTime;
//                return;
//            } else {
//                SPUtil.saveString(ProjectConstant.KEY_LOCATION,"");
//                //finishAll();
//                android.os.Process.killProcess(android.os.Process.myPid());
//                Runtime.getRuntime().gc();
//            }
//        } else {
//            super.onBackPressed();
//        }
//    }

    @Override
    public void findIds() {

    }

    @Override
    public void initViews() {

    }


}
