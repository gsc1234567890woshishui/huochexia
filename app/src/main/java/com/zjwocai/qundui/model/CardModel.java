package com.zjwocai.qundui.model;

import java.io.Serializable;

/**
 * 银行卡实体类
 * Created by NieLiQin on 2016/7/31.
 */
public class CardModel implements Serializable {

    /**
     * id : 5cc67929-30c7-4083-955d-8b25a8a7c0cb
     * type : 6
     * code : ahfshs
     * ower : hhdshs
     */

    private String id;
    private String type;
    private String code;
    private String ower;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOwer() {
        return ower;
    }

    public void setOwer(String ower) {
        this.ower = ower;
    }
}
