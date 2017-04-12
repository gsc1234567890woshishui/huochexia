package com.threeti.teamlibrary.net;

public interface ProcotolCallBack {
    @SuppressWarnings("rawtypes")
    public abstract void onTaskSuccess(BaseModel result);

    @SuppressWarnings("rawtypes")
    public void onTaskFail(BaseModel result);

    public void onTaskFinished(String resuestCode);
}
