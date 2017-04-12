package com.zjwocai.qundui.model;

import java.io.Serializable;

/**
 * 收支明细
 * Created by NieLiQin on 2016/8/1.
 */
public class IAEBModel implements Serializable {

    /**
     * id : 3
     * type :
     * content :
     * amount : 11.0
     * create_date : 2016-07-26 21:06:43
     */

    private String id;
    private String type;
    private String content;
    private String amount;
    private String create_date;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }
}
