package com.threeti.teamlibrary.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.threeti.teamlibrary.ApplicationEx;

import com.threeti.teamlibrary.R;


import com.threeti.teamlibrary.finals.LocaleSet;
import com.threeti.teamlibrary.finals.ProjectConfig;
import com.threeti.teamlibrary.finals.ProjectConstant;
import com.threeti.teamlibrary.model.UserModel;
import com.threeti.teamlibrary.net.BaseModel;
import com.threeti.teamlibrary.net.ProcotolCallBack;
import com.threeti.teamlibrary.utils.SPUtil;
import com.threeti.teamlibrary.utils.ToastUtil;
import com.threeti.teamlibrary.widgets.CommTitleBar;
import com.threeti.teamlibrary.widgets.SystemBarTintManager;


import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Random;


import cn.jpush.android.api.JPushInterface;

public abstract class BaseActivity extends AppCompatActivity implements ProjectConfig, ProjectConstant, ProcotolCallBack {

    /**
     * 当前语言
     */
    protected Locale  mLocale;
    /**
     * 图片异步加载配置文件
     */
    protected DisplayImageOptions options;
    /**
     * 图片异步加载配置器
     */
    protected ImageLoader imageLoader;
    /**
     * 标题栏
     */
    public CommTitleBar title;
    /**
     * 图片异步加载时的默认图片
     */
    public int defaultId = R.drawable.im_default_load_image;
    /**
     * 图片异步加载的半径
     */
    public int radius = -1;
    /**
     * 是否需要关闭app
     */
    public boolean needFinish = false;
    /**
     * 加载界面的资源id
     */
    protected int resId = -1;
    /**
     * 最后一次返回按钮操作事件
     */
    private long lastEventTime;
    /**
     * 推出app的等待时间
     */
    private static int TIME_TO_WAIT = 3 * 1000;

    public LayoutInflater mLayoutInflater;

    public boolean isPush = true;

    public boolean NoTitle = false;

    public String actNum = "";

    private SystemBarTintManager tintManager;

    /**
     * 生成随机数
     */
    public Random random;

    public BaseActivity(int resId) {
        this.resId = resId;


    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
    }

    public BaseActivity(int resId, boolean needFinish) {

        this.resId = resId;
        this.needFinish = needFinish;
    }
    public static int getStatusBarHeight(Context context){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
            Log.v("@@@@@@", "the status bar height is : " + statusBarHeight);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setTheme(R.style.Theme_AppCompat_Light_NoActionBar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            SystemBarTintManager tintManager = new SystemBarTintManager(this);
//            tintManager.setStatusBarTintEnabled(true);
        }


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window window = getWindow();
//            // Translucent status bar
//            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            SystemBarTintManager tintManager = new SystemBarTintManager(this);
//            tintManager.setStatusBarTintEnabled(true);
//            tintManager.setStatusBarTintResource(R.color.main_bg_blue);// 通知栏所需颜色
//        }



//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |     WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
//        }


        ApplicationEx.getInstance().getActivityManager().pushActivity(this);
        readyForView();
        setContentView(resId);
        if (radius > 0) {
            options = new DisplayImageOptions.Builder().showImageOnLoading(defaultId).resetViewBeforeLoading(true)
                    .showImageForEmptyUri(defaultId).showImageOnFail(defaultId).cacheInMemory(true).cacheOnDisk(true)
                    .displayer(new RoundedBitmapDisplayer(radius)).bitmapConfig(Bitmap.Config.RGB_565).build();
        } else {
            options = new DisplayImageOptions.Builder().showImageOnLoading(defaultId).resetViewBeforeLoading(true)
                    .showImageForEmptyUri(defaultId).showImageOnFail(defaultId).cacheInMemory(true).cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
        }
        random = new Random();
        imageLoader = ImageLoader.getInstance();
        mLayoutInflater = (LayoutInflater) (this)
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        getIntentData();
        findIds();
        initViews();
    }


    /**
     * initTitle:初始化标题. <br/>
     *
     * @param id
     * @author BaoHang
     */
    public void initTitle(int id) {
        if (title == null) {
            title = new CommTitleBar(this);
        }
        title.setTitle(id);
    }

    /**
     * initTitle:初始化标题. <br/>
     *
     * @param name
     * @author BaoHang
     */
    public void initTitle(String name) {
        if (title == null) {
            title = new CommTitleBar(this);
        }
        title.setTitle(name);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
        JPushInterface.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
        JPushInterface.onPause(this);
    }

    @Override
    public void finish() {
        ApplicationEx.getInstance().getActivityManager().popActivity(this);
        super.finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
    }

    public void finishNoAnim() {
        ApplicationEx.getInstance().getActivityManager().popActivity(this);
        super.finish();
        overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
    }

    public void finishAllExt(Class<?> cls) {
        ApplicationEx.getInstance().getActivityManager().popAllActivityExceptOne(cls);
    }

    public void finishAll() {
        ApplicationEx.getInstance().getActivityManager().popAllActivity();
    }

    @Override
    public void onBackPressed() {
        if (needFinish) {
            long currentEventTime = System.currentTimeMillis();
            if ((currentEventTime - lastEventTime) > TIME_TO_WAIT) {
                ToastUtil.toastShow(this, R.string.tip_finishapp, TIME_TO_WAIT);
                lastEventTime = currentEventTime;
                return;
            } else {
                SPUtil.saveString(ProjectConstant.KEY_LOCATION,"");
                finishAll();
                android.os.Process.killProcess(android.os.Process.myPid());
                Runtime.getRuntime().gc();
            }
        } else {
            super.onBackPressed();
        }
    }

    public void showToast(String msg) {
        ToastUtil.toastShortShow(getApplication(), msg);
    }

    public void showToast(int textResId) {
        ToastUtil.toastShortShow(getApplication(), textResId);
    }

    public void getIntentData() {

    }

    /**
     * readyForView:在setContentView之前的准备工作. <br/>
     * 正常情况下不需要重写该方法.<br/>
     * 需要使用该方法的时候只需要继承BaseActivity，然后重写该方法就行.<br/>
     * 目前只是在百度地图集成中看到需要重写该方法，用于百度地图SDK的初始化操作.<br/>
     *
     * @author BaoHang
     */
    public void readyForView() {
        if (NoTitle) {
            getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
//                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
//                window.setNavigationBarColor(Color.TRANSPARENT);
            }
        }
    }

    public abstract void findIds();

    public abstract void initViews();

    /**
     * getCurrLanguage:(这里用一句话描述这个方法的作用). <br/>
     *
     * @author BaoHang
     */
    private void getCurrLanguage() {
        String lan = SPUtil.getString(ProjectConstant.KEY_LOCALE);
        if (TextUtils.isEmpty(lan)) {
            mLocale = getResources().getConfiguration().locale;
            String currLan = mLocale.toString();
            if (!currLan.startsWith(LocaleSet.SIMPLE.toString())
                    && !currLan.startsWith(LocaleSet.TRADITION.toString())
                    && !currLan.startsWith(LocaleSet.ENGLISH.toString())) {// 不是中英藏中的一种，显示中文
                mLocale = LocaleSet.DEFAULT;
            }
        } else {
            if (lan.startsWith(LocaleSet.ENGLISH.toString())) {// 英文
                mLocale = LocaleSet.ENGLISH;
            } else if (lan.toString()
                    .startsWith(LocaleSet.TRADITION.toString())) {// 繁体中文
                mLocale = LocaleSet.TRADITION;
            } else {// 中文
                mLocale = LocaleSet.SIMPLE;
            }
        }
        SPUtil.saveString(ProjectConstant.KEY_LOCALE, mLocale.toString());
        Configuration config = getResources().getConfiguration();// 获得设置对象
        if (!config.locale.toString().equals(mLocale.toString())) {
            config.locale = mLocale;
            getResources().updateConfiguration(config,
                    getResources().getDisplayMetrics());
        }
    }

    public void startActivity(Class<?> cls) {
        startActivity(cls, null, true);
    }

    public void startActivityNoAnim(Class<?> cls) {
        startActivity(cls, null, false);
    }

    public void startActivityNoAnim(Class<?> cls, Object obj) {
        startActivity(cls, obj, false);
    }

    public void startActivity(Class<?> cls, Object obj) {
        startActivity(cls, obj, true);
    }


    public void startActivity(Class<?> cls, Object obj, boolean falsh) {
        Intent intent = new Intent(this, cls);
        // 设置跳转标志为如此Activity存在则把其从任务堆栈中取出放到最上方
        if (isPush) {
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        if (obj != null)
            intent.putExtra("data", (Serializable) obj);
        startActivity(intent);
        if (falsh)
            overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
        else
            overridePendingTransition(R.anim.fade_in,
                    R.anim.fade_out);
    }

    public void startActivity(Class<?> cls, Object obj, int falsh) {
        Intent intent = new Intent(this, cls);
        // 设置跳转标志为如此Activity存在则把其从任务堆栈中取出放到最上方
        if (isPush) {
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        if (obj != null)
            intent.putExtra("data", (Serializable) obj);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);

    }


    public void startActivityForResult(Class<?> cls, Object obj, int requestCode) {
        startActivityForResult(cls, obj, requestCode, true);
    }

    public void startActivityForResultNoAnim(Class<?> cls, Object obj, int requestCode) {
        startActivityForResult(cls, obj, requestCode, false);
    }

    public void startActivityForResult(Class<?> cls, Object obj, int requestCode, boolean falsh) {
        Intent intent = new Intent(this, cls);
        // 设置跳转标志为如此Activity存在则把其从任务堆栈中取出放到最上方
        if (isPush) {
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        if (obj != null)
            intent.putExtra("data", (Serializable) obj);
        startActivityForResult(intent, requestCode);
        if (falsh)
            overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
        else
            overridePendingTransition(android.R.anim.fade_in,
                    android.R.anim.fade_out);
    }

    public void loadImage(ImageView imageview, String url) {
        imageLoader.displayImage(url, imageview, options);
    }

    public void loadHeadImage(ImageView imageview, String url, int radius) {
        DisplayImageOptions options1 = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_default_head).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.ic_default_head).showImageOnFail(R.drawable.ic_default_head).cacheInMemory(true).cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(radius)).bitmapConfig(Bitmap.Config.RGB_565).build();
        imageLoader.displayImage(url, imageview, options1);
    }

    public void loadWebImage(ImageView imageview, String url) {
        imageLoader.displayImage(ProjectConfig.IMAGE_URL + url, imageview, options);
    }

    public void loadWebImage(ImageView imageview, String url, DisplayImageOptions option) {
        imageLoader.displayImage(ProjectConfig.IMAGE_URL + url, imageview, option);
    }

    public void loadWebImage(ImageView imageview, String url, ImageLoadingListener listener) {
        imageLoader.displayImage(ProjectConfig.IMAGE_URL + url, imageview, options, listener);
    }

    public UserModel getUser() {
        UserModel user = (UserModel) SPUtil.getObjectFromShare(ProjectConstant.KEY_USERINFO);
        return user;
    }

    public void saveUser(UserModel model) {
        SPUtil.saveObjectToShare(ProjectConstant.KEY_USERINFO, model);
    }

    public void setAliasAndTags() {
        if (null != getUser()) {
            LinkedHashSet<String> tags = new LinkedHashSet<String>();
            tags.add(getUser().getMobile() + "");
            JPushInterface.setAliasAndTags(getApplicationContext(),
                    getUser().getMobile(), tags);// 调用JPush
        }
    }

    public String buildCode() {
        String ret = "1234";
        int end = random.nextInt(9000);
        end = end + 1000;
        ret = String.valueOf(end);
        return ret;
    }

    public String getActNum() {
        return actNum;
    }

    public void setActNum(String actNum) {
        this.actNum = actNum;
    }

    @Override
    public void onTaskSuccess(BaseModel result) {

    }

    @Override
    public void onTaskFail(BaseModel result) {

    }

    @Override
    public void onTaskFinished(String resuestCode) {

    }
}
