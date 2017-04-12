package com.zjwocai.qundui.model;

import java.io.Serializable;
import java.util.List;

/**
 * 消息实体类
 * Created by NieLiQin on 2016/7/31.
 */
public class NewsListModel implements Serializable {


    /**
     * messagescount : 2
     * messagelist : []
     */

    private String messagescount;
    private List<NewsModel> messagelist;

    public String getMessagescount() {
        return messagescount;
    }

    public void setMessagescount(String messagescount) {
        this.messagescount = messagescount;
    }

    public List<NewsModel> getMessagelist() {
        return messagelist;
    }

    public void setMessagelist(List<NewsModel> messagelist) {
        this.messagelist = messagelist;
    }
}
