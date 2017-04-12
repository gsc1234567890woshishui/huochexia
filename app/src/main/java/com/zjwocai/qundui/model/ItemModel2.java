package com.zjwocai.qundui.model;

/**
 * Created by qundui on 2017/2/13.
 */
import java.io.Serializable;

/**
 * Created by WangChang on 2016/4/1.
 */
public class ItemModel2 implements Serializable {

    public static final int ONE = 1001;
    public static final int TWO = 1002;

    public int type;
    public Object data;
    public Object data2;
    public Object data3;
    public Object data4;
    public Object data5;
    public Object data6;



    public ItemModel2(int type, Object data,Object data2,Object data3,Object data4,Object data5,Object data6) {
        this.type = type;
        this.data = data;
        this.data2 = data2;
        this.data3 = data3;
        this.data4 = data4;
        this.data5 = data5;
        this.data6 = data6;

    }
}