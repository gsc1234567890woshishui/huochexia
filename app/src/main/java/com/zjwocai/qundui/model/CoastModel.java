package com.zjwocai.qundui.model;

/**
 * Created by qundui on 2017/2/16.
 */

public class CoastModel {
    private String czType; //1话费 2流量
    private String phoneNumber;
    private String facePrice;
    private String payChannel;
    private String data;//流量
    private String dataType;//流量类型

    public void setCzType(String czType) {
        this.czType = czType;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setFacePrice(String facePrice) {
        this.facePrice = facePrice;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getCzType() {
        return czType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getFacePrice() {
        return facePrice;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public String getData() {
        return data;
    }

    public String getDataType() {
        return dataType;
    }
}
