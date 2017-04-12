package com.zjwocai.qundui.model;

/**
 * Created by qundui on 2017/3/21.
 */

public class RecoderModel {
// `etcNumber` varchar(32) COMMENT 'etc卡号',
//    `czAmount` varchar(16) COMMENT '充值金额',
//            `czTime` varchar(16) COMMENT '缴费时间',
//            `czType` varchar(8) COMMENT '缴费类型',
//            `czState` varchar(8) COMMENT '缴费状态',
//            `xfStation` varchar(32) COMMENT '进出站信息',
//            `xfAmount` varchar(16) COMMENT '消费金额',
//            `xfTime` varchar(16) COMMENT '消费时间',
//            `jsTime` varchar(16) COMMENT '结算时间',
//            `jsBalance` varchar(16) COMMENT '卡内余额',

    private  String czTime;
    private String czType;
    private  String czState;
    private String czAmount;

    private  String xfStation;
    private String xfAmount;
    private  String xfTime;
    private String jsBalance;
    private  String type;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;

    }

    public void setCzTime(String czTime) {
        this.czTime = czTime;
    }

    public void setCzType(String czType) {
        this.czType = czType;
    }

    public void setCzState(String czState) {
        this.czState = czState;
    }

    public void setCzAmount(String czAmount) {
        this.czAmount = czAmount;
    }

    public void setXfStation(String xfStation) {
        this.xfStation = xfStation;
    }

    public void setXfAmount(String xfAmount) {
        this.xfAmount = xfAmount;
    }

    public void setXfTime(String xfTime) {
        this.xfTime = xfTime;
    }

    public void setJsBalance(String jsBalance) {
        this.jsBalance = jsBalance;
    }

    public String getXfStation() {
        return xfStation;

    }

    public String getXfAmount() {
        return xfAmount;
    }

    public String getXfTime() {
        return xfTime;
    }

    public String getJsBalance() {
        return jsBalance;
    }

    public String getCzTime() {
        return czTime;

    }

    public String getCzType() {
        return czType;
    }

    public String getCzState() {
        return czState;
    }

    public String getCzAmount() {
        return czAmount;
    }


}
