package com.threeti.teamlibrary.net;

public class BaseModel<T> {
    private int httpstate;
    private String request_code;
    private String response;
    private String success;
    private String error;
    private String error_msg;
    private T result;
    private int code_msg;
    private T object;
    private boolean isRefresh;
    protected String code;//": "OK",
    protected String msgtype;//": "0",
    protected String msg;//": "加载成功",

    public int getCode_msg() {
        return code_msg;
    }

    public void setCode_msg(int code_msg) {
        this.code_msg = code_msg;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public BaseModel() {
        setTaskFail();
    }

    public void setHttpstate(int httpstate) {
        this.httpstate = httpstate;
    }

    public int getHttpstate() {
        return httpstate;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setRequest_code(String request_code) {
        this.request_code = request_code;
    }

    public String getRequest_code() {
        return request_code;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public boolean isSuccess() {
        if (("true".equals(getSuccess()) || "1".equals(getSuccess()) || "t".equals(getSuccess())) || "OK".equals(getCode())) {
            return true;
        }
        return false;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setIsRefresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    public void setTaskSuccess() {
        setSuccess("true");
        setCode_msg(1);
    }

    public void setTaskFail() {
        setSuccess("false");
        setCode_msg(0);
    }

}
