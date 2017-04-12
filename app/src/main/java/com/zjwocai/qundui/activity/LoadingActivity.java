/**
 * File Name:LoadingActivity.java
 * Date:2014年10月10日下午2:17:14
 */
package com.zjwocai.qundui.activity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.threeti.teamlibrary.activity.BaseProtocolActivity;
import com.threeti.teamlibrary.net.BaseModel;
import com.threeti.teamlibrary.net.ProcotolCallBack;
import com.threeti.teamlibrary.utils.SPUtil;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.activity.login.GuideActivity;
import com.zjwocai.qundui.activity.login.LoginActivity;
import com.zjwocai.qundui.bill.ProtocolBill;
import com.zjwocai.qundui.fragment.MainActivity;
import com.zjwocai.qundui.model.SoftUpgradeViewModel;
import com.zjwocai.qundui.util.AppDownload;
import com.zjwocai.qundui.util.DialogUtil;


/**
 * ClassName:LoadingActivity
 * Date:2014年10月10日
 *
 * @author BaoHang
 * @since [产品/模块版本] （可选）
 */
public class LoadingActivity extends BaseProtocolActivity {
    private RelativeLayout rl_loding;
    private ImageView iv;
    private ImageView ivBg;
    private boolean updata = false;
    private boolean load = false;
    private int versioncode;
    private String versionName;
    private String url;
    private boolean isFinish = false;
    private AnimationDrawable mAnimation;
    private TextView tvVersion;
    private String version;
    private int number;
    private int versionNumber;

    public static final int ENTER_HOME = 1;
    public static final int SHOW_UPDATE_DIALOG = 2;
    public static final int URL_ERROR = 3;
    public static final int NETWORK_ERROR = 4;
    public static final int JSON_ERROR = 5;

    private String downloadUrl;// apk下载地址
    private String description="";// 版本描述
    private SoftUpgradeViewModel model;//下载model
    private String versionTitle;//下载标题
    /**
     * 图片异步加载配置文件
     */
    protected DisplayImageOptions option;

    //@Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//
//
//        }
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |     WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
//        }
//
//
//        ApplicationEx.getInstance().getActivityManager().pushActivity(this);
//        readyForView();
//        setContentView(resId);
//        if (radius > 0) {
//            options = new DisplayImageOptions.Builder().showImageOnLoading(defaultId).resetViewBeforeLoading(true)
//                    .showImageForEmptyUri(defaultId).showImageOnFail(defaultId).cacheInMemory(true).cacheOnDisk(true)
//                    .displayer(new RoundedBitmapDisplayer(radius)).bitmapConfig(Bitmap.Config.RGB_565).build();
//        } else {
//            options = new DisplayImageOptions.Builder().showImageOnLoading(defaultId).resetViewBeforeLoading(true)
//                    .showImageForEmptyUri(defaultId).showImageOnFail(defaultId).cacheInMemory(true).cacheOnDisk(true)
//                    .bitmapConfig(Bitmap.Config.RGB_565).build();
//        }
//        random = new Random();
//        imageLoader = ImageLoader.getInstance();
//        mLayoutInflater = (LayoutInflater) (this)
//                .getSystemService(LAYOUT_INFLATER_SERVICE);
//        getIntentData();
//        findIds();
//        initViews();
//    }

    public LoadingActivity() {

        this(R.layout.act_loading);
    }
    /**
     * Creates a new instance of LoadingActivity.
     *
     * @param resId
     */
    public LoadingActivity(int resId) {
        super(resId);
    }


    private Runnable action = new Runnable() {
        @Override
        public void run() {
            load = true;

            finishLoading();
        }
    };

    @Override
    public void getIntentData() {
        super.getIntentData();
//        ProtocolBill.getInstance().getGuide(this);
//        url = SPUtil.getObjectFromShare("cacheUrl");
    }

    /**
     */
    @Override
    public void findIds() {
        rl_loding = (RelativeLayout) findViewById(R.id.rl_loding);
        iv = (ImageView) findViewById(R.id.iv_bg);
        tvVersion = (TextView) findViewById(R.id.tv_version);
        mAnimation = (AnimationDrawable) iv.getDrawable();



//        ivBg = (ImageView) findViewById(R.id.iv_bg_load);
    }


    /**
     */
    @Override
    public void initViews() {

//        if (url != null && !url.equals("")){
//            if (imageLoader.getDiskCache().get(url) != null){
//                imageLoader.displayImage(url,iv,options);
//            }
//        }

//        imageLoader.displayImage();


        mAnimation.start();
        rl_loding.postDelayed(action, 3000);
        /**
         * PackageManager介绍：
         * 本类API是对所有基于加载信息的数据结构的封装，包括以下功能：
         * 安装，卸载应用 查询permission相关信息 查询Application相关
         * 信息(application，activity，receiver，service，provider及相应属性等）
         * 查询已安装应用 增加，删除permission 清除用户数据、缓存，代码段等 非查询相关的API需要特定的权限。
         * 主要包含了，安装在当前设备上的应用包的相关信息
         * 如下：获取已经安装的应用程序的信息
         */
        PackageManager packageManager = getPackageManager();
        /**
         * 通过 PackageInfo  获取具体信息方法：
         * 包名获取方法：packageInfo.packageName
         * icon获取获取方法：packageManager.getApplicationIcon(applicationInfo)
         应用名称获取方法：packageManager.getApplicationLabel(applicationInfo)
         使用权限获取方法：packageManager.getPackageInfo(packageName,PackageManager.GET_PERMISSIONS)
         .requestedPermissions
         */
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(this.getPackageName(), 0);
            //获取版本号  和  版本码
            versioncode = packInfo.versionCode;
            versionName = packInfo.versionName;
            //number = Integer.valueOf(versionName.replace(".",""));
            if (versionName != null) {
                tvVersion.setText("v " + versionName);
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
//        if (null != getNowUser()) {
//            LinkedHashSet<String> tags = new LinkedHashSet<String>();
//            tags.add(getNowUser().getUserid() + "");
//            JPushInterface.setAliasAndTags(getApplicationContext(),
//                    getNowUser().getUserid(), tags);// 调用JPush
//        }


    }

    //更新版本

    public void updateApp(){
        AppDownload.getInstance().doDownloadApp(this,model);



    }

    // 进入主页面
    private void enterHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();

    }

    /*
    *
    *
    * 检查版本更新升级
    *
    *
    * **/
    // 升级弹窗
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(versionTitle);

        builder.setMessage(description);

         //builder.setCancelable(false);//流氓手段,让用户点击返回键没有作用, 不建议采纳
        // 点击物理返回键,取消弹窗时的监听
        builder.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });

        builder.setPositiveButton("立即更新", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateApp();
                enterHome();

            }
        });

        builder.setNegativeButton("以后再说", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //只有点击以后再说后，才给当前的version赋值
                SPUtil.saveString(version,"sp"+version);
                enterHome();
            }
        });
        AlertDialog dialog = builder.create();

        dialog.show();
    }
    private void  showUpdateDialog2(){//强制更新
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(versionTitle);
        builder.setMessage(description);
         //builder.setCancelable(false);//流氓手段,让用户点击返回键没有作用, 不建议采纳

        // 点击物理返回键,取消弹窗时的监听
        builder.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                  finish();

            }
        });
        builder.setPositiveButton("立即更新", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //一个弹窗正在下载
                Dialog waitDialog = DialogUtil.getProgressDialog(LoadingActivity.this,"应用正在更新，请稍候!");
                waitDialog.show();
                updateApp();


            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }


    private void finishLoading() {
        Integer isFirst = 1;
        isFirst = SPUtil.getObjectFromShare("isFirst");
        if (isFirst == null || isFirst == 1) {
            mAnimation.stop();
            startActivity(GuideActivity.class);
            isFirst = 0;
            SPUtil.saveObjectToShare("isFirst", isFirst);
            finish();
        } else {
            mAnimation.stop();
            if (null != getUser()){
                setAliasAndTags();
                //startActivity(HomeActivity.class);
                //自动登录   判断是否更新
                ProtocolBill.getInstance().renew(new ProcotolCallBack() {
                    @Override
                    public void onTaskSuccess(BaseModel result) {

                        if(result==null){
                            //如果请求不到网络就进入主页面
                            startActivity(MainActivity.class);
                        }else{
                            model= (SoftUpgradeViewModel) result.getResult();
                            versionNumber = model.getVersion_number();
                            //描述
                            description = model.getDescription();
                            //是否强制更新
                            int i = model.getForce_update();
                            //版本
                            version = model.getVersion();//2.0.4
                            versionTitle = model.getVersion_name();//新年测试版Andriod

                            if(versioncode<versionNumber){//如果本地版本小于服务器版本号就提示更新

                                //保存变量到本地供"我我我"界面使用  提示更新
                                SPUtil.saveString("newVersion","1");

                                if(i==1){//强制更新
                                    showUpdateDialog2();

                                }else{//非强制更新
                                    if(SPUtil.getString(version).isEmpty()){//如果sp.get(version)是空字符串
                                        //弹窗提示升级
                                        showUpdateDialog();

                                    }else{
                                        //如果这个版本对应的sp值有值就不弹窗，直接进入主页面
                                        startActivity(MainActivity.class);
                                    }

                                }
                            }else{
                                //保存变量到本地供"我我我"界面使用  不提示更新
                                SPUtil.saveString("newVersion","0");
                                //如果没有新版本就进入主页
                                startActivity(MainActivity.class);
                            }

                        }

                    }
                    @Override
                    public void onTaskFail(BaseModel result) {



                    }

                    @Override
                    public void onTaskFinished(String resuestCode) {

                    }
                },this);

            } else {
                startActivity(LoginActivity.class);
            }
        }

    }

    @Override
    public void onTaskSuccess(BaseModel result) {

    }
}
