package com.zjwocai.qundui.fragment;

import android.os.Bundle;

/**
 * Created by 刘楠 on 2016/8/28 0028.13:34
 */
public class EventMessage {

    Bundle mBundle;
    int    tag;

    public Bundle getBundle() {
        return mBundle;
    }

    public void setBundle(Bundle bundle) {
        mBundle = bundle;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public static class EventMessageAction {

        public final static int TAG_GO_MAIN     = 1;//跳转主页
        public final static int TAG_GO_SHOPCART = 2;//购物车
        public final static int TAG_GO_MESSAGE  = 3;// 消息
        public final static int TAG_REFRESH_CHILD = 4;// 刷新
        public final static int TAG_REFRESH_HOME = 5;// 刷新主页订单
        public final static int TAG_REFRESH_GOODS = 6;//刷新运单页订单
        public final static int TAG_REFRESH_CARD = 7;//刷新三证提示
        public final static int TAG_REFRESH_RESUME = 8;//刷新提示
        public final static int TAG_MESS_CODE = 9;//让手机充值套餐显示置灰
        public final static int TAG_RECHARGE_DIALOG = 10;//刷新运单页订单
        public final static int TAG_REMOVE_DIALOG = 11;//刷新运单页订单
        public final static int TAG_SEEVICE_LOCATION = 12;//刷新运单页订单
        public final static int TAG_STOP_LOCATION = 13;//停止service确认收货
        public final static int TAG_STOP_LOCATION_ORDER = 14;//详情页停止service确认收货
        public final static int TAG_SEEVICE_LOCATION_ORDER = 15;//详情页面确认收货
        public final static int TAG_SEEVICE_LOCATION_LISTFRAGMENT = 16;//刷新运单页订单
        public final static int TAG_STOP_LOCATION_LISTFRAGMENT = 17;//刷新运单页订单

    }
}
