package com.zjwocai.qundui.activity.mine;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.threeti.teamlibrary.utils.SPUtil;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.activity.BaseProjectActivity;
import com.zjwocai.qundui.activity.home.ADActivity;
import com.zjwocai.qundui.model.CarListModel;
import com.zjwocai.qundui.util.CallDialogUtil;
import com.zjwocai.qundui.util.DialogUtil;
import com.zjwocai.qundui.util.StringUtil;
import com.zjwocai.qundui.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class EtcPresentActivity extends BaseProjectActivity implements View.OnClickListener {
    private TextView tvPay,tvUnderstand;
    private Dialog callDialog;
    private LinearLayout activity_etc_present,ll_pay;
    private  ArrayList<String> list;
    private  ArrayList<String> list2;
    private String noCar;


    public EtcPresentActivity() {
        super(R.layout.activity_etc_present);
    }

    @Override
    public void findIds() {
        tvPay = (TextView) findViewById(R.id.tv_pay);
        tvUnderstand = (TextView) findViewById(R.id.tv_understand);
        activity_etc_present = (LinearLayout) findViewById(R.id.ll_activity_etc_present);
        ll_pay = (LinearLayout) findViewById(R.id.ll_pay);


        Intent intent = getIntent();
        list= (ArrayList<String>) intent.getSerializableExtra("list");
        list2= (ArrayList<String>) intent.getSerializableExtra("list2");
        noCar = intent.getStringExtra("nocar");
    }

    @Override
    public void initViews() {

        initTitle("ETC服务");
        title.setLeftIcon(R.drawable.ic_title_left, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EtcPresentActivity.this.finish();
            }
        });
        title.setRightText("客服", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceNumber = "0571-87620191";
                new CallDialogUtil().showCallDialog(serviceNumber,EtcPresentActivity.this);
            }
        });
        tvUnderstand.setOnClickListener(this);
        tvUnderstand.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        ll_pay.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_understand:
                //了解开卡流程页面
                startActivity(new Intent(EtcPresentActivity.this,EtcOpenProcess.class));
                break;

            case R.id.ll_pay:
                //携带数据 点击开卡

                if(noCar!=null && noCar.equals("nocar")){

                    AlertDialog.Builder da = new AlertDialog.Builder(this);
                    da.setTitle("提示：");
                    da.setMessage("您还未添加车辆，是否前往添加？");
                    da.setCancelable(false);
                    //设置左边按钮监听
                    da.setPositiveButton("确定",
                            new android.content.DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                               SPUtil.saveString("addcar","1");
                               startActivity(new Intent(EtcPresentActivity.this,CarAddActivity.class));
                                    finish();
                                }
                            });
                    //设置右边按钮监听
                    da.setNegativeButton("取消",
                            new android.content.DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    arg0.dismiss();
                                }
                            });
                    da.show();
                }else{
                    Intent intent = new Intent(this,EtcAskActivity.class);
                    intent.putExtra("list", list);
                    intent.putExtra("list2", list);
                    startActivity(intent);
                }

                break;
        }

    }
}
