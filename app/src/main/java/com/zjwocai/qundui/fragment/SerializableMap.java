package com.zjwocai.qundui.fragment;


import java.io.Serializable;
import java.util.Map;

/**
 * Created by qundui on 2017/1/17.
 */

public class SerializableMap implements Serializable {

    private Map<String,Object> map;

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}
