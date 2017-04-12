package com.threeti.teamlibrary.activity;

import android.text.TextUtils;

import com.threeti.teamlibrary.finals.RequestCodeSet;
import com.threeti.teamlibrary.net.BaseModel;
import com.threeti.teamlibrary.net.ProcotolCallBack;

public abstract class BaseProtocolActivity extends BaseActivity implements ProcotolCallBack, RequestCodeSet {

    public BaseProtocolActivity(int resId) {
        super(resId);
    }

    public BaseProtocolActivity(int resId, boolean needFinish) {
        super(resId, needFinish);
    }

    @Override
    public void onTaskFail(@SuppressWarnings("rawtypes") BaseModel result) {
//        if (!TextUtils.isEmpty(result.getError_msg())) {
//            showToast(result.getError_msg() + "");
//        } else {
//            showToast(result.getError() + "");
//        }

        if (!TextUtils.isEmpty(result.getMsg())) {
            showToast(result.getMsg() + "");
        }
    }

    @Override
    public void onTaskFinished(String resuestCode) {

    }


}
