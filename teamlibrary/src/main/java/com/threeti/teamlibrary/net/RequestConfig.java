package com.threeti.teamlibrary.net;

import java.util.Map;

public class RequestConfig {
    public static final String GET = "get";
    public static final String POST = "post";
    private String requestCode = null;
    private String method = null;
    private String webAddress = null;
    private String authName = null;
    private String authPswd = null;
    private Map<String, String> data = null;
    private String requestdata;
    private Map<String, String> header = null;
    private Map<String, String> files = null;
    private Class<?> cls;
    private Class<?> element;
    private boolean needParse = true;
    private boolean isRefresh = true;

    private String target;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public RequestConfig() {

    }

    public RequestConfig(String requestCode) {
        this.requestCode = requestCode;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public Class<?> getCls() {
        return cls;
    }

    public void setCls(Class<?> cls) {
        this.cls = cls;
    }

    public Class<?> getElement() {
        return element;
    }

    public void setElement(Class<?> element) {
        this.element = element;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getWebAddress() {
        return webAddress;
    }

    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public String getAuthPswd() {
        return authPswd;
    }

    public void setAuthPswd(String authPswd) {
        this.authPswd = authPswd;
    }

    public Map<String, String> getFiles() {
        return files;
    }

    public void setFiles(Map<String, String> files) {
        this.files = files;
    }

    public boolean isNeedParse() {
        return needParse;
    }

    public void setNeedParse(boolean needParse) {
        this.needParse = needParse;
    }

    public String getRequestdata() {
        return requestdata;
    }

    public void setRequestdata(String requestdata) {
        this.requestdata = requestdata;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setIsRefresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }
}
