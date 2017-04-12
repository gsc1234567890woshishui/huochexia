package com.zjwocai.qundui.model;

import java.io.Serializable;

/**
 * 选择框实体类
 * Created by NieLiQin on 2016/8/2.
 */
public class ItemModel implements Serializable{

    private String name;
    private String code;

    public ItemModel() {
    }

    public ItemModel(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    //这个用来显示在PickerView上面的字符串,PickerView会通过反射获取getPickerViewText方法显示出来。
    public String getPickerViewText() {
        //这里还可以判断文字超长截断再提供显示
        return name;
    }
}
