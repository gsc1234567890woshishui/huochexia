package com.threeti.teamlibrary;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.L;
import com.threeti.teamlibrary.finals.ProjectConfig;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by BaoHang on 15/3/23.
 */
public class ApplicationEx extends MultiDexApplication {

    private static ApplicationEx taiLongApplication = null;
    /**
     * Activity管理器
     */
    private CustomActivityManager activityManager = null;
    /**
     * 数据缓存区，用于数据的临时缓存
     */
    private Map<String, Object> mHardCache = null;
    /**
     * 设备屏幕位深
     */
    private float mDensity;
    /**
     * 设备屏幕宽度（单位像素）
     */
    private int mWidth;
    /**
     * 设备屏幕高度（单位像素）
     */
    private int mHeight;

    /**
     * 单例方法
     *
     * @return
     */
    public static ApplicationEx getInstance() {
        return taiLongApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        taiLongApplication = this;
        mHardCache = new HashMap<String, Object>();// 初始化系统级内存缓存
        activityManager = new CustomActivityManager();
        initResolution();// 初始化屏幕显示参数（宽，高，以及像素密度）
        initFileDir();// 初始化文件缓存目录
        initOtherLib();//导入第三方SDK包
        //替换sdk 地址
//        AnalyticsConfig.setRequestPath(getResources().getString(R.string.online_record_address));
//        Analyticslib.init(this);
    }


    /**
     * initResolution:初始化屏幕信息. <br/>
     * 初始化屏幕的宽度，高度以及像素密度.<br/>
     * 宽高的单位都是像素，px不是dp.<br/>
     *
     * @author BaoHang
     */
    private void initResolution() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mWidth = dm.widthPixels;
        mHeight = dm.heightPixels;
        mDensity = dm.density;
    }

    /**
     * 获取屏幕像素密度
     *
     * @return
     */
    public float getDeviceDensity() {
        return mDensity;
    }

    /**
     * 获取当前设备的屏幕宽度
     *
     * @return
     */
    public int getDeviceWidth() {
        return mWidth;
    }

    /**
     * 获取当前屏幕的高度
     *
     * @return
     */
    public int getDeviceHeight() {
        return mHeight;
    }

    /**
     * 得到activity管理器
     *
     * @return
     */
    public CustomActivityManager getActivityManager() {
        return activityManager;
    }

    /**
     * 初始化界面缓存目录
     */
    private void initFileDir() {
        File appFile = new File(ProjectConfig.APP_PATH);
        if (!appFile.exists()) {
            appFile.mkdir();
        }
        File cacheFile = new File(ProjectConfig.DIR_CACHE);// 其他下载缓存数据目录
        if (!cacheFile.exists()) {
            cacheFile.mkdir();
        }
        File updataCacheFile = new File(ProjectConfig.DIR_UPDATE);// 程序在手机SDK中的更新缓存目录.
        if (!updataCacheFile.exists()) {
            updataCacheFile.mkdir();
        }
        File logCacheFile = new File(ProjectConfig.LOGCAT_DIR);// 程序在手机SDK中的日志缓存目录.
        if (!logCacheFile.exists()) {
            logCacheFile.mkdir();
        }

        File imageCacheFile = new File(ProjectConfig.DIR_IMG);// 程序在手机SDK中的图片缓存目录.
        if (!imageCacheFile.exists()) {
            imageCacheFile.mkdir();
        }

        File videoCacheFile = new File(ProjectConfig.DIR_VIDEO);// 程序在手机SDK中的视频缓存目录.
        if (!videoCacheFile.exists()) {
            videoCacheFile.mkdir();
        }
        File audioCacheFile = new File(ProjectConfig.DIR_AUDIO);// 程序在手机SDK中的音频缓存目录.
        if (!audioCacheFile.exists()) {
            audioCacheFile.mkdir();
        }
        File downloadFile = new File(ProjectConfig.DIR_DOWNLOAD);// 程序在手机SDK中的下载目录.
        if (!downloadFile.exists()) {
            downloadFile.mkdir();
        }
        File examFile = new File(ProjectConfig.DIR_EXAM);// 程序在手机SDK中的下载目录.
        if (!examFile.exists()) {
            examFile.mkdir();
        }

        File examvideoFile = new File(ProjectConfig.DIR_EXAM_VIDEO);// 程序在手机SDK中的下载目录.
        if (!examvideoFile.exists()) {
            examvideoFile.mkdir();
        }

        File examimageFile = new File(ProjectConfig.DIR_EXAM_IMAGE);// 程序在手机SDK中的下载目录.
        if (!examimageFile.exists()) {
            examimageFile.mkdir();
        }
    }

    /**
     * 缓存数据内容
     *
     * @param key
     * @param value
     */
    public void cache(String key, Object value) {
        mHardCache.put(key, value);
    }

    /**
     * 获取缓存内容
     *
     * @param key
     * @return
     */
    public Object getCache(String key) {
        return mHardCache.get(key);
    }

    /**
     * 移除缓存
     *
     * @param key
     */
    public void removeCache(String key) {
        mHardCache.remove(key);
    }

    /**
     * 清空系统缓存区
     */
    public void clearCache() {
        mHardCache.clear();
        Runtime.getRuntime().gc();
    }

    /**
     * 当系统内存不足时调用
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Runtime.getRuntime().gc();// 通知Java虚拟机回收垃圾
    }

    /**
     * 程序终止时候调用
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    /**
     * 初始化第三方库
     * 已经初始化友盟以及图片异步加载库
     */
    public void initOtherLib() {
        MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.setDebugMode(ProjectConfig.DEBUG_MODE);
        MobclickAgent.openActivityDurationTrack(false);

        JPushInterface.setDebugMode(ProjectConfig.DEBUG_MODE);
        JPushInterface.init(this);

        initImageLoader();
    }

    /**
     * 初始化图片异步加载库
     */
    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .diskCache(new UnlimitedDiscCache(new File(ProjectConfig.DIR_IMG)))
                .imageDownloader(
                        new BaseImageDownloader(getApplicationContext())) // default
                .imageDecoder(new BaseImageDecoder(true)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs().build();
        ImageLoader.getInstance().init(config);
//        ReceiveUtil.loadPicture(getApplicationContext());
        if (ProjectConfig.DEBUG_MODE) {
            L.enableLogging();// 图片异步加载日志开关
        }
    }
    /**
     * 分割 Dex 支持
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
