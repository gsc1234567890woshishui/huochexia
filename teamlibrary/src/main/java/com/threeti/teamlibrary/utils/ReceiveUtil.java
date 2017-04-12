package com.threeti.teamlibrary.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Administrator on 2016/7/19.
 */
public class ReceiveUtil {
    public static  boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }else if(activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE){
            return false;
        }
        return true;
    }
    public static void loadPicture(Context context){
        if(isWifi(context)) {//wifi
            ImageLoader.getInstance().resume();// 有图模式

        }else {//流量
            ImageLoader.getInstance().pause();// 无图模式
        }
    }
}
