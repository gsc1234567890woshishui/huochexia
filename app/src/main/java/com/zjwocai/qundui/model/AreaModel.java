package com.zjwocai.qundui.model;

import java.io.Serializable;

/**
 * 区
 * Created by NieLiQin on 2016/8/1.
 */
public class AreaModel implements Serializable, GetModelName {

    /**
     * id : 1301
     * code : 330101
     * name : 市辖区
     * sort : 930
     * type : 4
     */

    private String id;
    private String code;
    private String name;
    private String sort;
    private String type;

    public AreaModel() {
    }

    public AreaModel(String id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    //这个用来显示在PickerView上面的字符串,PickerView会通过反射获取getPickerViewText方法显示出来。
    public String getPickerViewText() {
        //这里还可以判断文字超长截断再提供显示
        return name;
    }

    @Override
    public String getModelName() {
        return getName();
    }
}
