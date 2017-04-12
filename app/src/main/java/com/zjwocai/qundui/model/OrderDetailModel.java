package com.zjwocai.qundui.model;

import java.io.Serializable;

/**
 * 订单详情
 * Created by NieLiQin on 2016/8/1.
 */
public class OrderDetailModel implements Serializable {

    /**
     * code : 1607281157
     * shipper : ggg
     * shipcompany : ggggg
     * shipdate : 2016-07-28 00:00:00
     * shippertel : 05716564255
     * fhprovice : 330000
     * fhcity : 330100
     * fharea : 330106
     * fhaddress : gggggggggggggggggg
     * receiver : vvv
     * receivecompany : vvvvvv
     * receivertel : 02168464546
     * shprovice : 310000
     * shcity : 310100
     * sharea : 310106
     * shaddress : vvvvvvvvvvvvvvvv
     * loadstartdate :
     * loadenddate :
     * goodsinfo : sssssss
     * cost : 100.0
     * freight : 300.0
     * packtype : 2
     * weight : 3.0
     * volume : 12.0
     * usecar : 1
     * cartype : 1
     * carlength : 7
     * iskp : 1
     * ishd : 1
     * arriveimg :
     * fhprovicename : 杭州市
     * fhcityname : 杭州市
     * fhareaname : 杭州市
     * shprovicename : 上海市
     * shcityname : 上海市
     * shareaname : 上海市
     */

    private String code;
    private String shipper;
    private String shipcompany;
    private String shipdate;
    private String shippertel;
    private String fhprovice;
    private String fhcity;
    private String fharea;
    private String fhaddress;
    private String receiver;
    private String receivecompany;
    private String receivertel;
    private String shprovice;
    private String shcity;
    private String sharea;
    private String shaddress;
    private String loadstartdate;
    private String loadenddate;
    private String goodsinfo;
    private String cost;
    private String freight;
    private String packtype;
    private String weight;
    private String volume;
    private String usecar;
    private String cartype;
    private String carlength;
    private String iskp;
    private String ishd;
    private String arriveimg;
    private String fhprovicename;
    private String fhcityname;
    private String fhareaname;
    private String shprovicename;
    private String shcityname;
    private String shareaname;
    private String remarks;
    private String sjtel;
    private String distance="";//总距离

    //距离
    public String getDistance(){
        return distance;
    }
    public void setDistance(){
        this.distance = distance;

    }
    public String getSjtel() {
        return sjtel;
    }

    public void setSjtel(String sjtel) {
        this.sjtel = sjtel;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShipper() {
        return shipper;
    }

    public void setShipper(String shipper) {
        this.shipper = shipper;
    }

    public String getShipcompany() {
        return shipcompany;
    }

    public void setShipcompany(String shipcompany) {
        this.shipcompany = shipcompany;
    }

    public String getShipdate() {
        return shipdate;
    }

    public void setShipdate(String shipdate) {
        this.shipdate = shipdate;
    }

    public String getShippertel() {
        return shippertel;
    }

    public void setShippertel(String shippertel) {
        this.shippertel = shippertel;
    }

    public String getFhprovice() {
        return fhprovice;
    }

    public void setFhprovice(String fhprovice) {
        this.fhprovice = fhprovice;
    }

    public String getFhcity() {
        return fhcity;
    }

    public void setFhcity(String fhcity) {
        this.fhcity = fhcity;
    }

    public String getFharea() {
        return fharea;
    }

    public void setFharea(String fharea) {
        this.fharea = fharea;
    }

    public String getFhaddress() {
        return fhaddress;
    }

    public void setFhaddress(String fhaddress) {
        this.fhaddress = fhaddress;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceivecompany() {
        return receivecompany;
    }

    public void setReceivecompany(String receivecompany) {
        this.receivecompany = receivecompany;
    }

    public String getReceivertel() {
        return receivertel;
    }

    public void setReceivertel(String receivertel) {
        this.receivertel = receivertel;
    }

    public String getShprovice() {
        return shprovice;
    }

    public void setShprovice(String shprovice) {
        this.shprovice = shprovice;
    }

    public String getShcity() {
        return shcity;
    }

    public void setShcity(String shcity) {
        this.shcity = shcity;
    }

    public String getSharea() {
        return sharea;
    }

    public void setSharea(String sharea) {
        this.sharea = sharea;
    }

    public String getShaddress() {
        return shaddress;
    }

    public void setShaddress(String shaddress) {
        this.shaddress = shaddress;
    }

    public String getLoadstartdate() {
        return loadstartdate;
    }

    public void setLoadstartdate(String loadstartdate) {
        this.loadstartdate = loadstartdate;
    }

    public String getLoadenddate() {
        return loadenddate;
    }

    public void setLoadenddate(String loadenddate) {
        this.loadenddate = loadenddate;
    }

    public String getGoodsinfo() {
        return goodsinfo;
    }

    public void setGoodsinfo(String goodsinfo) {
        this.goodsinfo = goodsinfo;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
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

    public String getUsecar() {
        return usecar;
    }

    public void setUsecar(String usecar) {
        this.usecar = usecar;
    }

    public String getCartype() {
        return cartype;
    }

    public void setCartype(String cartype) {
        this.cartype = cartype;
    }

    public String getCarlength() {
        return carlength;
    }

    public void setCarlength(String carlength) {
        this.carlength = carlength;
    }

    public String getIskp() {
        return iskp;
    }

    public void setIskp(String iskp) {
        this.iskp = iskp;
    }

    public String getIshd() {
        return ishd;
    }

    public void setIshd(String ishd) {
        this.ishd = ishd;
    }

    public String getArriveimg() {
        return arriveimg;
    }

    public void setArriveimg(String arriveimg) {
        this.arriveimg = arriveimg;
    }

    public String getFhprovicename() {
        return fhprovicename;
    }

    public void setFhprovicename(String fhprovicename) {
        this.fhprovicename = fhprovicename;
    }

    public String getFhcityname() {
        return fhcityname;
    }

    public void setFhcityname(String fhcityname) {
        this.fhcityname = fhcityname;
    }

    public String getFhareaname() {
        return fhareaname;
    }

    public void setFhareaname(String fhareaname) {
        this.fhareaname = fhareaname;
    }

    public String getShprovicename() {
        return shprovicename;
    }

    public void setShprovicename(String shprovicename) {
        this.shprovicename = shprovicename;
    }

    public String getShcityname() {
        return shcityname;
    }

    public void setShcityname(String shcityname) {
        this.shcityname = shcityname;
    }

    public String getShareaname() {
        return shareaname;
    }

    public void setShareaname(String shareaname) {
        this.shareaname = shareaname;
    }
}
