package com.zjwocai.qundui.model;

import java.io.Serializable;

/**
 * 可用车辆列表项
 * Created by NieLiQin on 2016/8/1.
 */
public class EnableCarModel implements Serializable {

    /**
     * id : 1f168f5b-4df7-4e09-b227-e8bf04a54f59
     * carnumber : 浙A88888
     */

    private String id;
    private String carnumber;
    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
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
}
