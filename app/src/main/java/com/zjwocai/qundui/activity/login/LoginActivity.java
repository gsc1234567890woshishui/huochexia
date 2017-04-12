package com.zjwocai.qundui.activity.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.threeti.teamlibrary.activity.BaseProtocolActivity;
import com.threeti.teamlibrary.model.UserModel;
import com.threeti.teamlibrary.net.BaseModel;
import com.threeti.teamlibrary.net.ProcotolCallBack;
import com.threeti.teamlibrary.utils.SPUtil;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.bill.ProtocolBill;
import com.zjwocai.qundui.fragment.MainActivity;
import com.zjwocai.qundui.model.SoftUpgradeViewModel;
import com.zjwocai.qundui.util.AppDownload;
import com.zjwocai.qundui.util.DialogUtil;
import com.zjwocai.qundui.util.InputUtil;

/**
 * 登录
 * Created by NieLiQin on 2016/7/22.
 */
public class LoginActivity extends BaseProtocolActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    private RadioGroup rgTags, rgBgs;
    private RadioButton rbTag1, rbTag2, rbBg1, rbBg2;
    private EditText etPhone, etPass;
    private TextView tvPass, tvCode, tvLogin, tvForget, tvBlank;
    private LinearLayout llGetCode;

    private boolean loginType = false;
    private TimeCount timeCount;
    private String code;
    private String password = "-1";
    private CheckBox mCheckBox; //是否显示密码

    private String downloadUrl;// apk下载地址
    private String description="";// 版本描述
    private SoftUpgradeViewModel model2;//下载model
    private String versionTitle;//下载标题
    private String versionName="";//版本名称
    private String version;//版本
    private int number;
    private int versionNumber;
    private int versionCode;

    public LoginActivity() {
        super(R.layout.act_login);
    }

    @Override
    public void findIds() {
        rgTags = (RadioGroup) findViewById(R.id.rg_login_tab);
        //rgBgs = (RadioGroup) findViewById(R.id.rg_inc);
        rbTag1 = (RadioButton) findViewById(R.id.rb_tag1);
        rbTag2 = (RadioButton) findViewById(R.id.rb_tag2);
        //rbBg1 = (RadioButton) findViewById(R.id.rb_bg1);
        //rbBg2 = (RadioButton) findViewById(R.id.rb_bg2);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etPass = (EditText) findViewById(R.id.et_code);
        tvPass = (TextView) findViewById(R.id.tv_pass);
        tvCode = (TextView) findViewById(R.id.tv_code);
        tvLogin = (TextView) findViewById(R.id.tv_login);
        tvForget = (TextView) findViewById(R.id.tv_forget);
        tvBlank = (TextView) findViewById(R.id.tv_blank);
        llGetCode = (LinearLayout) findViewById(R.id.ll_get_code);
        mCheckBox = (CheckBox) findViewById(R.id.check_isShown);

        mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    etPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());//显示密码

                else {
                    etPass.setTransformationMethod(PasswordTransformationMethod.getInstance());//隐藏密码
                }
                etPass.setSelection(TextUtils.isEmpty(etPass.getText()) ? 0 : etPass.length());//光标挪到最后
            }
        });
    }


    @Override
    public void initViews() {
        initTitle("登录");
        title.hideLeftIcon();
        needFinish = true;

        rgTags.setOnCheckedChangeListener(this);
        //rgBgs.setClickable(false);

        tvCode.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        tvForget.setOnClickListener(this);
        tvForget.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        PackageManager packageManager = getPackageManager();
        PackageInfo packInfo = null;

        try {
            packInfo = packageManager.getPackageInfo(this.getPackageName(), 0);
            //获取版本号  和  版本码
            versionName = packInfo.versionName;
            versionCode = packInfo.versionCode;
            //number = Integer.valueOf(versionName.replace(".",""));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == rbTag1.getId()) {  //账号密码登录
            mCheckBox.setVisibility(View.VISIBLE);//设置密码明文秘文显示
            tvCode.setVisibility(View.GONE);//设置验证码控件消失
            //rgBgs.check(rbBg1.getId());
            loginType = false;
            tvBlank.setText("输");
            tvPass.setText("密码");
            etPass.setText("");
            etPass.setHint("请输入密码");
            llGetCode.setVisibility(View.INVISIBLE);
        } else if (checkedId == rbTag2.getId()) { //验证码登录
            mCheckBox.setVisibility(View.GONE);//设置密码明文密文消失
            tvCode.setVisibility(View.VISIBLE);
            //rgBgs.check(rbBg2.getId());
            loginType = true;
            tvBlank.setText("");
            tvPass.setText("验证码");
            etPass.setText("");
            etPass.setHint("请输入验证码");
            llGetCode.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        InputUtil.hideInputMethdView(LoginActivity.this, etPass);
        InputUtil.hideInputMethdView(LoginActivity.this, etPhone);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_code:
                if (loginType) {

                    if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
                        showToast("请输入手机号");

                    } else if (etPhone.getText().toString().trim().length() < 11) {
                        showToast("请输入11位手机号");
                    } else {
                        ProtocolBill.getInstance().getCode(this, this, etPhone.getText().toString().trim(), "0");
                        showToast("已发送验证码至您的手机");
                        showCode();
                    }

                }
                break;
            case R.id.tv_login:
                if (check()) {
                    if (loginType) {
                        //验证码登录
                        ProtocolBill.getInstance().login(this, this,
                                etPhone.getText().toString().trim(),
                                "", etPass.getText().toString().trim(), "0");
                    } else {
                        //账号密码登录
                        password = etPass.getText().toString().trim();
//                        if (password.equals("123456")){
//                            showToast("请修改您的密码");
//                            startActivity(ResetPasswordCheckActivity.class,etPhone.getText().toString().trim());
//                        } else {
                        ProtocolBill.getInstance().login(this, this,
                                etPhone.getText().toString().trim(),
                                etPass.getText().toString().trim(),
                                "", "1");
//                        }
                    }
                }
                break;

            case R.id.tv_forget:
                startActivity(SetPasswordActivity.class);
                break;
        }
    }

    private boolean check() {
        if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
            showToast("请输入手机号");
            return false;
        }
        String code = etPass.getText().toString();
        if (TextUtils.isEmpty(code)) {
            if (loginType) {
                showToast("请输入验证码");
            } else {
                showToast("请输入密码");
            }

            return false;
        }
        if (etPhone.getText().toString().trim().length() < 11) {
            showToast("请输入11位手机号");
            return false;
        }
        return true;
    }

    public void showCode() {
        timeCount = new TimeCount(60000, 1000);
        tvCode.setSelected(false);
        tvCode.setClickable(false);
        timeCount.start();
    }

    @Override
    public void onTaskSuccess(BaseModel result) {
        if (password.equals("123456")) {
            showToast("请修改您的密码");
            startActivity(ResetPasswordCheckActivity.class, etPhone.getText().toString().trim());
            return;
        }

        if (RQ_LOGIN.equals(result.getRequest_code())) {
            UserModel model = (UserModel) result.getResult();
            //startActivity(HomeActivity.class);
            saveUser(model);
            setAliasAndTags();

            //登录成功就调用更新版本的接口
            ProtocolBill.getInstance().renew(new ProcotolCallBack() {
                @Override
                public void onTaskSuccess(BaseModel result) {
                   if(result==null){
                       //如果请求不到网络的版本数据信息就跳转到主页
                       startActivity(MainActivity.class);

                   }else{
                       model2= (SoftUpgradeViewModel) result.getResult();
                       //描述
                       description = model2.getDescription();
                       //是否强制更新
                       int i = model2.getForce_update();
                       //版本
                       version = model2.getVersion();//2.0.4
                       versionNumber = model2.getVersion_number();
                       versionTitle = model2.getVersion_name();//新年测试版Andriod
                       SPUtil.saveString("newVersion","0");
                       String url =  model2.getDownload_url();

                       if(versionCode<versionNumber){//如果小于服务器版本，就提示升级

                           //保存变量到本地供"我我我"界面使用  提示更新
                           SPUtil.saveString("newVersion","1");

                           if(i==1){//强制更新

                               showUpdateDialog2();

                           }else{//非强制更新
                               if(SPUtil.getString(version).isEmpty()){//如果sp.get(version)是空字符串
                                   //弹窗提示升级
                                   showUpdateDialog();

                               }else{
                                   //保存变量到本地供"我我我"界面使用  不提示更新
                                   SPUtil.saveString("newVersion","0");
                                   //如果这个版本对应的sp值有值就不弹窗，直接进入主页面
                                   startActivity(MainActivity.class);
                               }
                           }
                       }else{
                           //如果没有新版本就进入主页
                           enterHome();
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
        }
    }

    //更新版本

    public void updateApp(){
        AppDownload.getInstance().doDownloadApp(this,model2);



    }

    // 进入主页面
    private void enterHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();

    }
    // 升级弹窗
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(versionTitle);
        builder.setMessage(description);


        //builder.setCancelable(false);//流氓手段,让用户点击返回键没有作用, 不建议采纳
        // 点击物理返回键,取消弹窗时的监听
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
//                //点击返回键后也给他赋值
//                SPUtil.saveString(version,"sp"+version);
//                enterHome();
                finish();
            }
        });

        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateApp();
                enterHome();

            }
        });

        builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //只有点击了以后再说，才给当前的version赋值
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
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                finish();
            }
        });
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                //一个弹窗正在下载
                Dialog waitDialog = DialogUtil.getProgressDialog(LoginActivity.this,"应用正在更新，请稍候!");
                waitDialog.show();
                updateApp();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();


    }

    /**
     * 计时器
     */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            tvCode.setSelected(true);
            tvCode.setClickable(true);
            tvCode.setText("获取验证码");
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            tvCode.setText(millisUntilFinished / 1000 + "s");
        }
    }
}

