package com.zjwocai.qundui.activity.mine;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ta.utdid2.android.utils.StringUtils;
import com.threeti.teamlibrary.activity.BaseProtocolActivity;
import com.threeti.teamlibrary.finals.ProjectConstant;
import com.threeti.teamlibrary.net.BaseModel;
import com.threeti.teamlibrary.net.ProcotolCallBack;
import com.threeti.teamlibrary.utils.SPUtil;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.activity.login.LoginActivity;
import com.zjwocai.qundui.bill.ProtocolBill;
import com.zjwocai.qundui.model.SoftUpgradeViewModel;
import com.zjwocai.qundui.service.LocationService;
import com.zjwocai.qundui.service.LocationService2;
import com.zjwocai.qundui.util.AppDownload;
import com.zjwocai.qundui.util.DialogUtil;
import com.zjwocai.qundui.util.StringUtil;
import com.zjwocai.qundui.util.ToastUtil;

public class Mine2Activity extends BaseProtocolActivity implements View.OnClickListener {

    private  RelativeLayout rlVersion,rlFeedback,rlPhone;
    private TextView tvVersion,tvPhone,tvLoginOut;
    private Dialog callDialog;
    private  SoftUpgradeViewModel model2;
    private String versionName,versionTitle,version,description;
    private LinearLayout llNew;
    private ImageView ivCancle;
    private RelativeLayout rlLeft;




    public Mine2Activity() {
        super(R.layout.activity_mine2);
    }

    @Override
    public void findIds() {
        rlVersion = (RelativeLayout) findViewById(R.id.rl_version);
        tvVersion = (TextView)findViewById(R.id.tv_version);
        rlFeedback = (RelativeLayout) findViewById(R.id.rl_feedback);
        rlPhone = (RelativeLayout) findViewById(R.id.rl_service_phone);
        tvPhone = (TextView) findViewById(R.id.tv_service_phone);
        tvLoginOut = (TextView) findViewById(R.id.tv_login_out);
        llNew = (LinearLayout) findViewById(R.id.ll_new);

        rlLeft = (RelativeLayout) findViewById(R.id.rl_left);



    }

    @Override
    public void initViews() {
        tvLoginOut.setOnClickListener(this);
        rlVersion.setOnClickListener(this);
        rlFeedback.setOnClickListener(this);
        rlPhone.setOnClickListener(this);
        rlLeft.setOnClickListener(this);

        //获取是否更新
        String newString = SPUtil.getString("newVersion");
        //设置新版本显示
        if(newString.equals("1")){
            llNew.setVisibility(View.VISIBLE);
            tvVersion.setVisibility(View.GONE);
        }

        PackageManager packageManager = getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(this.getPackageName(), 0);

            versionName = packInfo.versionName;
            if(versionName != null){

                tvVersion.setText("v "+versionName);
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }



        tvPhone.setText("0571-87620191");

    }
    /**
     * 拨打电话
     *
     * @param number 电话号码
     */
    private void showCallDialog(final String number) {
        LayoutInflater factory = LayoutInflater.from(this);
        View view = factory.inflate(R.layout.dialog_enter_call, null);
        final TextView des = (TextView) view.findViewById(R.id.tv_des);
        final TextView enter = (TextView) view.findViewById(R.id.tv_enter);
        final TextView cancel = (TextView) view.findViewById(R.id.tv_cancel);
        final TextView content = (TextView) view.findViewById(R.id.tv_content);
        des.setText("客服电话：");
        content.setText(number);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtil.isNO(number.replace("-","").trim())) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    Uri data = Uri.parse("tel:" + number);
                    intent.setData(data);
                    startActivity(intent);
                } else {
                    showToast("号码无效！");
                }
                callDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDialog.dismiss();
            }
        });
        callDialog = DialogUtil.getDialog(this, view, Gravity.CENTER, true);
        callDialog.show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rl_left:
                finish();
                break;
            case R.id.rl_service_phone:
                String serviceNumber = tvPhone.getText().toString().trim();
                if (StringUtils.isEmpty(serviceNumber))
                    serviceNumber = "0571-87620191";
                showCallDialog(serviceNumber);
                break;

            case R.id.rl_feedback:
                startActivity(FeedbackActivity.class);
                break;
            case R.id.tv_login_out:
                DialogUtil.getAlertNoTitleDialog(this, "您确定要退出吗？", "确定", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveUser(null);
                        SPUtil.saveString(ProjectConstant.KEY_LOCATION,"");
                        SPUtil.saveString("failcar","");
                        //改变否更新sp的值
                        SPUtil.saveString("renew","ok");

                        //停止获取定位的service1
                        Intent stopLocationServiceIntent = new Intent(getApplicationContext(),
                                LocationService.class);
                        stopService(stopLocationServiceIntent);
                        //停止获取定位的service2
                        Intent stopLocationServiceIntent2 = new Intent(getApplicationContext(),
                                LocationService2.class);
                        stopService(stopLocationServiceIntent2);

                        startActivity(LoginActivity.class);
                    }
                }).show();
                break;
            case R.id.rl_version:
                if(llNew.getVisibility()==View.VISIBLE){//如果更新可见就设置点击时间

                    ProtocolBill.getInstance().renew(new ProcotolCallBack() {//调用版本更新的接口
                        @Override
                        public void onTaskSuccess(BaseModel result) {
                            model2 = (SoftUpgradeViewModel) result.getResult();
                            version= model2.getVersion();//新版本号
                            versionTitle = model2.getVersion_name();//版本标题
                            description = model2.getDescription();//更新描述
                            showUpdateDialog();//弹窗提示更新
                        }

                        @Override
                        public void onTaskFail(BaseModel result) {

                        }

                        @Override
                        public void onTaskFinished(String resuestCode) {

                        }
                    },this);

                }else{

                    ToastUtil.toastShortShow("已经是最新版!");
                }
                break;


        }
        }
    // 升级弹窗
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(versionTitle);
        builder.setMessage(description);


        //builder.setCancelable(false);//流氓手段,让用户点击返回键没有作用, 不建议采纳
        // 点击物理返回键,取消弹窗时的监听
//        builder.setOnCancelListener(new OnCancelListener() {
//
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                finishLoading();
//                enterHome();
//            }
//        });

        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateApp();
            }
        });

        builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void updateApp(){
        ToastUtil.toastShortShow(this,"已开始下载，请注意查看!");
        AppDownload.getInstance().doDownloadApp(this,model2);

    }


}
