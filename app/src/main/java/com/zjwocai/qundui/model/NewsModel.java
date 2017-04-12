package com.zjwocai.qundui.model;

import java.io.Serializable;

/**
 * 消息实体类
 * Created by NieLiQin on 2016/7/31.
 */
public class NewsModel implements Serializable {

    /**
     * id : 2
     * title : test
     * content : jfjfjfj
     * csdate : 2016-07-24 12:52:13
     */

    private String id;
    private String title;
    private String content;
    private String csdate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCsdate() {
        return csdate;
    }

    public void setCsdate(String csdate) {
        this.csdate = csdate;
    }
}
