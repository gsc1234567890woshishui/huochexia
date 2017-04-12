package com.zjwocai.qundui;

import com.threeti.teamlibrary.ApplicationEx;

/**
 * @author Bassam
 * @version V1.0
 * @Package com.zjwocai.threeespeak
 * @Title
 * @Description
 * @date 16/3/14
 */
public class QunDuiApplication extends ApplicationEx {


    @Override
    public void onCreate() {
        super.onCreate();
        try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

}
