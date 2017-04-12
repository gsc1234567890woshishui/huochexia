package com.zjwocai.qundui.model;

import java.io.Serializable;

/**
 * 抢枪抢公告实体类
 * Created by NieLiQin on 2016/9/18.
 */
public class NoticeModel implements Serializable{

    /**
     * createNum : 0
     * successNum : 0
     */

    private String createNum;
    private String successNum;

    public String getCreateNum() {
        return createNum;
    }

    public void setCreateNum(String createNum) {
        this.createNum = createNum;
    }

    public String getSuccessNum() {
        return successNum;
    }

    public void setSuccessNum(String successNum) {
        this.successNum = successNum;
    }
}
