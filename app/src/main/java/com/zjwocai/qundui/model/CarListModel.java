package com.zjwocai.qundui.model;

import java.io.Serializable;

/**
 * Created by qundui on 2017/3/16.
 */

public class CarListModel implements Serializable {
   private String carnumber;
    private String id;

    public String getCarnumber() {
        return carnumber;
    }

    public String getId() {
        return id;
    }

    public void setCarnumber(String carnumber) {
        this.carnumber = carnumber;
    }

    public void setId(String id) {
        this.id = id;
    }
}
