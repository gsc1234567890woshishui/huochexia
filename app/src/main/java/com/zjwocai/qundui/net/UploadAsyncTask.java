package com.zjwocai.qundui.net;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.threeti.teamlibrary.ApplicationEx;
import com.threeti.teamlibrary.net.BaseHttpRequest;
import com.threeti.teamlibrary.net.BaseModel;
import com.threeti.teamlibrary.net.ProcotolCallBack;
import com.threeti.teamlibrary.net.RequestConfig;
import com.threeti.teamlibrary.utils.AsyncTaskUtil;
import com.threeti.teamlibrary.utils.DialogUtil;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.finals.InterfaceFinals;
import com.zjwocai.qundui.model.BalanceModel;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

public class UploadAsyncTask extends AsyncTask<RequestConfig, Integer, BaseModel> {
    protected Dialog dialog = null;
    protected static Gson mGson;
    private ProcotolCallBack procotolcallback;
    private RequestConfig config;
    private String resuestCode;
    private boolean isRefresh;

    public UploadAsyncTask(ProcotolCallBack procotolcallback) {
        this.procotolcallback = procotolcallback;
        if (null == mGson) {
            mGson = new Gson();
        }
    }

    public UploadAsyncTask(ProcotolCallBack procotolcallback, Activity activity, String msg) {
        this(procotolcallback);
        this.dialog = DialogUtil.getProgressDialog(activity, msg);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showProgress();
    }

    @Override
    protected BaseModel doInBackground(RequestConfig... params) {
        BaseModel result = new BaseModel();
        result.setError(ApplicationEx.getInstance().getApplicationContext().getResources()
                .getString(R.string.err_net));
        result.setSuccess("false");
        try {
            Map<String, Object> response = null;
            BaseHttpRequest request = new BaseHttpRequest();
            config = params[0];
            resuestCode = config.getRequestCode();
            isRefresh = config.isRefresh();
            response = request.makeHTTPRequest(config.getMethod(), config.getWebAddress(), config.getData(),
                    config.getRequestdata(), config.getHeader(), config.getFiles(), config.getAuthName(),
                    config.getAuthPswd());
            if ("ALIOSS".equals(config.getTarget())) {
                for (String key : config.getFiles().keySet()) {
                    File file = new File(config.getFiles().get(key));
                    if (file.exists()) {
                        String s = request.put(config.getWebAddress(), config.getFiles().get(key));
                        if (TextUtils.isEmpty(s)) {
                            result.setSuccess("true");
                            result.setError("上传成功");
                        } else {
                            result.setSuccess("false");
                            result.setError("上传失败");
                        }
                    } else {
                        result.setSuccess("false");
                        result.setError("文件不存在");
                    }
                }
            } else {
                response = request.makeHTTPRequest(config.getMethod(), config.getWebAddress(), config.getData(),
                        config.getRequestdata(), config.getHeader(), config.getFiles(), config.getAuthName(),
                        config.getAuthPswd());
                if (response.get(BaseHttpRequest.HTTP_DATA).toString().length() > 0) {
                    ((BaseModel) result).setResponse(response.get(BaseHttpRequest.HTTP_DATA).toString());
                    result = parseData(config, response.get(BaseHttpRequest.HTTP_DATA).toString());
                    if (!result.getCode().equals("OK")) {
                        result.setSuccess("false");
                        result.setError("上传失败");
                    } else {
                        result.setSuccess("true");
                        result.setError("上传成功");
                    }
                }
            }
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
            result.setError(ApplicationEx.getInstance().getApplicationContext().getResources()
                    .getString(R.string.err_timeout));
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            result.setError("上传失败");
        } catch (Exception e) {
            e.printStackTrace();
            result.setError("上传失败");
        }
        return result;
    }

    public BaseModel parseData(RequestConfig config, String data) throws JsonSyntaxException {
        BaseModel base = mGson.fromJson(data, BaseModel.class);
        if (base.isSuccess()) {
            Type t = null;
            if (config.getCls() != null) {
                if (config.getCls().getName().equals(ArrayList.class.getName())) {
                    t = com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null, BaseModel.class,
                            com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null, config.getCls(),
                                    config.getElement()));

                } else
                    t = com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null, BaseModel.class,
                            config.getCls());
                return mGson.fromJson(data, t);
            } else {
                if (config.getElement() != null) {
                    t = com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null, BaseModel.class,
                            com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null, ArrayList.class,
                                    config.getElement()));
                    return mGson.fromJson(data, t);
                }
            }
        }
        return base;
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(BaseModel result) {
        super.onPostExecute(result);
        dismissProgress();
        if (!isCancelled() && null != result) {
            result.setRequest_code(resuestCode);
//            result.setHttpstate(httpstate);
            result.setIsRefresh(isRefresh);
            if (result.isSuccess()) {
                if (procotolcallback != null)
                    procotolcallback.onTaskSuccess(result);
            } else {
                if (procotolcallback != null)
                    procotolcallback.onTaskFail(result);
            }
        } else {
            result = new BaseModel();
            result.setRequest_code(resuestCode);
//            result.setHttpstate(httpstate);
            result.setIsRefresh(isRefresh);
            if (procotolcallback != null)
                procotolcallback.onTaskFail(result);
        }
        if (procotolcallback != null)
            procotolcallback.onTaskFinished(resuestCode);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        dismissProgress();
    }

    protected void showProgress() {
        try {
            if (null != dialog) {
                dialog.setOnCancelListener(AsyncTaskUtil.defaultDialogInterfaceCancelListener(this));
                dialog.show();
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void dismissProgress() {
        try {
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setConfig(RequestConfig config) {
        this.config = config;
    }
}

