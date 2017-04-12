package com.zjwocai.qundui.model;

import java.io.Serializable;

/**
 * 车辆实体类
 * Created by NieLiQin on 2016/8/1.
 */
public class CarModel implements Serializable {


    /**
     * id : f457624b-bdd6-44e8-a2f6-541ea5dbd613
     * carnumber : 浙A88888
     * type : 1
     * carload : 11.0
     * enddate : 2016-06-11 00:00:00
     */

    private String id;
    private String carnumber;
    private String type;
    private String carload;
    private String enddate;
    private String status;

    private String etc_number;//etc卡号
    private String xsz;//行驶证
    private String audit_state;  //审核状态 0申请中 －1失败 1成功
    private String car_head_img;//车头照片

    public void setEtc_number(String etc_number) {
        this.etc_number = etc_number;
    }

    public void setXsz(String xsz) {
        this.xsz = xsz;
    }

    public void setAudit_state(String audit_state) {
        this.audit_state = audit_state;
    }

    public void setCar_head_img(String car_head_img) {
        this.car_head_img = car_head_img;
    }

    public String getEtc_number() {

        return etc_number;
    }

    public String getXsz() {
        return xsz;
    }

    public String getAudit_state() {
        return audit_state;
    }

    public String getCar_head_img() {
        return car_head_img;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarnumber() {
        return carnumber;
    }

    public void setCarnumber(String carnumber) {
        this.carnumber = carnumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCarload() {
        return carload;
    }

    public void setCarload(String carload) {
        this.carload = carload;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }
}
