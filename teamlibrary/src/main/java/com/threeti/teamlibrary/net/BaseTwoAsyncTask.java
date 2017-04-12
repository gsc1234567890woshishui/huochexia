package com.threeti.teamlibrary.net;

import android.app.Activity;
import android.app.Dialog;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.threeti.teamlibrary.ApplicationEx;
import com.threeti.teamlibrary.R;
import com.threeti.teamlibrary.net.task.PriorityAsyncTask;
import com.threeti.teamlibrary.utils.AsyncTaskUtil;
import com.threeti.teamlibrary.utils.DialogUtil;

import org.apache.http.conn.ConnectTimeoutException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class BaseTwoAsyncTask extends PriorityAsyncTask<RequestConfig, Integer, BaseModel> {
    protected Dialog dialog = null;
    protected static Gson mGson;
    private ProcotolCallBack procotolcallback;
    private RequestConfig config;
    private String resuestCode;
    private boolean isRefresh = true;
    private int httpstate;

    public BaseTwoAsyncTask(ProcotolCallBack procotolcallback) {
        this.procotolcallback = procotolcallback;
        if (null == mGson) {
            mGson = new Gson();
        }
    }

    public BaseTwoAsyncTask(ProcotolCallBack procotolcallback, Activity activity, String msg) {
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
        result.setMsg(ApplicationEx.getInstance().getApplicationContext().getResources()
                .getString(R.string.err_net));
        result.setError(ApplicationEx.getInstance().getApplicationContext().getResources()
                .getString(R.string.err_net));
        result.setError_msg(ApplicationEx.getInstance().getApplicationContext().getResources()
                .getString(R.string.err_net));
        try {
            Map<String, Object> response = null;
            BaseHttpRequest request = new BaseHttpRequest();
            config = params[0];
            resuestCode = config.getRequestCode();
            isRefresh = config.isRefresh();
            response = request.makeHTTPRequest(config.getMethod(), config.getWebAddress(), config.getData(),
                    config.getRequestdata(), config.getHeader(), config.getFiles(), config.getAuthName(),
                    config.getAuthPswd());
            if (response.get(BaseHttpRequest.HTTP_DATA).toString().length() > 0) {
                ((BaseModel) result).setResponse(response.get(BaseHttpRequest.HTTP_DATA).toString());
                if (config.isNeedParse()) {
                    result = parseData(config, response.get(BaseHttpRequest.HTTP_DATA).toString());
                } else {
                    ((BaseModel) result).setTaskSuccess();
                }
            } else {
                result.setMsg(ApplicationEx.getInstance().getApplicationContext().getResources()
                        .getString(R.string.err_unknow));
                result.setError(ApplicationEx.getInstance().getApplicationContext().getResources()
                        .getString(R.string.err_unknow));
                result.setError_msg(ApplicationEx.getInstance().getApplicationContext().getResources()
                        .getString(R.string.err_unknow));
            }
            httpstate = Integer.valueOf(response.get(BaseHttpRequest.HTTP_STATE).toString());
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
            result.setMsg(ApplicationEx.getInstance().getApplicationContext().getResources()
                    .getString(R.string.err_timeout));
            result.setError(ApplicationEx.getInstance().getApplicationContext().getResources()
                    .getString(R.string.err_timeout));
            result.setError_msg(ApplicationEx.getInstance().getApplicationContext().getResources()
                    .getString(R.string.err_timeout));
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            result.setMsg(ApplicationEx.getInstance().getApplicationContext().getResources()
                    .getString(R.string.err_data));
            result.setError(ApplicationEx.getInstance().getApplicationContext().getResources()
                    .getString(R.string.err_data));
            result.setError_msg(ApplicationEx.getInstance().getApplicationContext().getResources()
                    .getString(R.string.err_data));
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg(ApplicationEx.getInstance().getApplicationContext().getResources()
                    .getString(R.string.err_net));
            result.setError(ApplicationEx.getInstance().getApplicationContext().getResources()
                    .getString(R.string.err_net));
            result.setError_msg(ApplicationEx.getInstance().getApplicationContext().getResources()
                    .getString(R.string.err_net));
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
            result.setHttpstate(httpstate);
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
            result.setHttpstate(httpstate);
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
