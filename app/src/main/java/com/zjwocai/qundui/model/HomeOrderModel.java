package com.zjwocai.qundui.model;

import android.widget.ImageView;

import java.io.Serializable;

/**
 * 首页订单项实体类
 * Created by NieLiQin on 2016/7/31.
 */
public class HomeOrderModel implements Serializable {

    /**
     * id : d9841f161dc64b678e8f1320440a392e
     * code : 1607281157
     * releasdate : 2016-07-28 10:28:35
     * receiver : vvv
     * goodsinfo : sssssss
     * packtype : 2
     * weight : 3.0
     * volume : 12.0
     * status : 1
     * issf : 1
     * shipper : ggg
     * packnumber
     */

    private String id;
    private String code;
    private String releasdate;
    private String receiver;
    private String goodsinfo;
    private String packtype;
    private String weight;
    private String volume;
    private String status;
    private String issf;
    private String shipper;
    private String packnumber;


    //新加的字段信息
    private String fhareaName="";//发货区的名字
    private String fhcityname="";//发货城市的名字
    private String shcityname="";//收货城市的名字
    private String shareaName="";//收货区的名字
    private String distance="";//总距离
    private String freight="";//运费
    private String shaddress="";//收货地址
    private String shprovicename="";//收货省份
    private String fhaddress="";//发货地址




    //发货的城市
    public String getFhcityname() {
        return fhcityname;
    }

    public void setFhcityname(String fhcityname) {
        this.fhcityname = fhcityname;
    }

    //发货的区
    public String getFhareaname() {
        return fhareaName;
    }
    public void setFhareaname(String fhareaname) {
        this.fhareaName = fhareaname;
    }

    //收货的城市
    public String getShcityname() {
        return shcityname;
    }

    public void setShcityname(String shcityname) {
        this.shcityname = shcityname;
    }

    //收货的区
    public String getShareaname() {
        return shareaName;
    }

    public void setShareaname(String shareaname) {
        this.shareaName = shareaname;
    }


    public String getFhaddress() {
        return fhaddress;
    }

    public void setFhaddress(String fhaddress) {
        this.fhaddress = fhaddress;
    }
    //距离
    public String getDistance(){
        return distance;
    }
    public void setDistance(){
        this.distance = distance;

    }
    //运费
    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    //收货地址
    public String getShaddress() {
        return shaddress;
    }
    //省份
    public String getShprovicename() {
        return shprovicename;
    }

    public void setShprovicename(String shprovicename) {
        this.shprovicename = shprovicename;
    }

    public void setShaddress(String shaddress) {
        this.shaddress = shaddress;
    }



    public String getPacknumber() {
        return packnumber;
    }

    public void setPacknumber(String packnumber) {
        this.packnumber = packnumber;
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

    public String getReleasdate() {
        return releasdate;
    }

    public void setReleasdate(String releasdate) {
        this.releasdate = releasdate;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getGoodsinfo() {
        return goodsinfo;
    }

    public void setGoodsinfo(String goodsinfo) {
        this.goodsinfo = goodsinfo;
    }

    public String getPacktype() {
        return packtype;
    }

    public void setPacktype(String packtype) {
        this.packtype = packtype;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIssf() {
        return issf;
    }

    public void setIssf(String issf) {
        this.issf = issf;
    }

    public String getShipper() {
        return shipper;
    }

    public void setShipper(String shipper) {
        this.shipper = shipper;
    }
}
