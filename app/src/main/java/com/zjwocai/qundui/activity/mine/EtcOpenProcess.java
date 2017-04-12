package com.zjwocai.qundui.activity.mine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.threeti.teamlibrary.net.BaseModel;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.activity.BaseProjectActivity;
import com.zjwocai.qundui.bill.ProtocolBill;
import com.zjwocai.qundui.model.CarListModel;
import com.zjwocai.qundui.model.CarModel;
import com.zjwocai.qundui.util.CallDialogUtil;
import com.zjwocai.qundui.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class EtcOpenProcess extends BaseProjectActivity {

    private List<CarModel> models;
    private  List<CarModel> carModels;
    public EtcOpenProcess() {
        super(R.layout.activity_etc_open_process);
    }

    @Override
    public void findIds() {

    }

    @Override
    public void initViews() {
        ProtocolBill.getInstance().getCarList(this,this,"1");

        initTitle("开卡流程");
        title.setLeftIcon(R.drawable.ic_title_left, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EtcOpenProcess.this.finish();
            }
        });
        title.setRightText("客服", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceNumber = "0571-87620191";
                new CallDialogUtil().showCallDialog(serviceNumber,EtcOpenProcess.this);
            }
        });

    }

    @Override
    public void onTaskSuccess(BaseModel result) {
        super.onTaskSuccess(result);




    }
}
