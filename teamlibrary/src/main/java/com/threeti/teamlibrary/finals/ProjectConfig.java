package com.threeti.teamlibrary.finals;

import android.os.Environment;

import com.threeti.teamlibrary.ApplicationEx;
import com.threeti.teamlibrary.R;

import java.io.File;

/**
 * Created by BaoHang on 15/3/23.
 */
public interface ProjectConfig {
    /**
     *           是否是测试模式
     */
    public static final boolean DEBUG_MODE = false;
    // --------------服务器基本信息-----------------------
    /**
     * 测试的API头地址.
     */
    public static final String DEBUG_HEAD_URL = ApplicationEx.getInstance().getString(R.string.debug_head_url);
    /**
     * 产线的API头地址.
     */
    public static final String ONLINE_HEAD_URL = ApplicationEx.getInstance().getString(R.string.online_head_url);
    /**
     * App使用的API头地址.
     */
    public static final String HEAD_URL = DEBUG_MODE ? DEBUG_HEAD_URL : ONLINE_HEAD_URL;
    /**
     * 默认的API头地址.
     */
    public static final String DEBUG_IMAGE_URL = ApplicationEx.getInstance().getString(R.string.debug_image_url);
    /**
     * 默认的API头地址.
     */
    public static final String ONLINE_IMAGE_URL = ApplicationEx.getInstance().getString(R.string.online_image_url);
    /**
     * 默认的API头地址.
     */
    public static final String IMAGE_URL = DEBUG_MODE ? DEBUG_IMAGE_URL : ONLINE_IMAGE_URL;


    public static final String UPLOAD_URL = ApplicationEx.getInstance().getString(R.string.upload_url);

    /*
    * 轨迹的接口地址
    * */
//    public static final String TRACK_URL = ApplicationEx.getInstance().getString(R.string.track_url);


    // --------------应用缓存文件基本信息-----------------------
    /**
     * 程序在手机SDK中的主缓存目录.
     */
    public static final String APP_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator
            + ApplicationEx.getInstance().getString(R.string.app_name);
    /**
     * 程序在手机SDK中的缓存目录.
     */
    public static final String DIR_CACHE = APP_PATH + File.separator + "cache";
    /**
     * 程序在手机SDK中的更新缓存目录.
     */
    public static final String DIR_UPDATE = APP_PATH + File.separator + "update";
    /**
     * 程序在手机SDK中的图片缓存目录.
     */
    public static final String DIR_IMG = APP_PATH + File.separator + "image";
    /**
     * 程序在手机SDK中的视频缓存目录.
     */
    public static final String DIR_VIDEO = APP_PATH + File.separator + "video";
    /**
     * 程序在手机SDK中的音频缓存目录.
     */
    public static final String DIR_AUDIO = APP_PATH + File.separator + "audio";
    /**
     * 程序在手机SDK中的日志缓存目录.
     */
    public static final String LOGCAT_DIR = APP_PATH + File.separator + "Log";

    public static final String DIR_DOWNLOAD = APP_PATH + File.separator + "DownLoad/";

    public static final String DIR_EXAM = DIR_DOWNLOAD + File.separator + "Exam/";

    public static final String DIR_EXAM_VIDEO = DIR_EXAM + File.separator + "Video/";

    public static final String DIR_EXAM_IMAGE = DIR_EXAM + File.separator + "Image/";
}
