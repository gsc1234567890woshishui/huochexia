package com.threeti.teamlibrary.model;

import java.io.Serializable;

/**
 * 用户信息
 * Created by NieLiQin on 2016/7/29.
 */
public class UserModel implements Serializable {

    /**
     * id : 61ef2a9981e84137b671119012d5493b
     * nickname : 老司机
     * sex : 1
     * headimg : userfiles/1/images/driver/2016/07/27105cfc889ac7158ef64a96b6800270_b.jpg
     * mobile : 13221097719
     * provicecode :
     * citycode :
     * areacode :
     * status :
     * balance :
     * token : dE3gb72MGL5P4f28Fz7Cc1XT8xS-ZK5hLSSeWcr6VO_ci6io3K5F2wDq2whKEJ1MqYTpck95okQ.
     * provicename :
     * cityname :
     * areaname :
     */

    private String id;
    private String nickname;
    private String sex;
    private String headimg;
    private String mobile;
    private String provicecode;
    private String citycode;
    private String areacode;
    private String status;
    private String balance;
    private String token;
    private String provicename;
    private String cityname;
    private String areaname;
    private String kftel;

    private boolean complete;
    private String tips;

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public boolean isComplete() {
        return complete;
    }

    public String getTips() {
        return tips;
    }

    public String getKftel() {
        return kftel;
    }

    public void setKftel(String kftel) {
        this.kftel = kftel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProvicecode() {
        return provicecode;
    }

    public void setProvicecode(String provicecode) {
        this.provicecode = provicecode;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getProvicename() {
        return provicename;
    }

    public void setProvicename(String provicename) {
        this.provicename = provicename;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }
}
