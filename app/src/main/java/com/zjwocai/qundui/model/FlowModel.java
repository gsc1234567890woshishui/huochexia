package com.zjwocai.qundui.model;

import java.util.List;

/**
 * Created by qundui on 2017/2/16.
 */

public class FlowModel {

    /**
     * province : 浙江
     * catName : 中国移动
     * carrier : 浙江移动
     * itemList:套餐
     */

    private String province;
    private String catName;
    private String carrier;
    private List<FlowModel.ItemListBean> itemList;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }
    public List<FlowModel.ItemListBean> getItemList() {
        return itemList;
    }


    public void setItemList(List<FlowModel.ItemListBean> itemList) {
        this.itemList = itemList;
    }
    public static class ItemListBean {
        private String  data; //流量数 M

        private String  facePrice; //面额: "10.00",

        private String  snPrice; //省内销售价格: "9.85"

        private String  qgPrice; //全国销售价格: "9.85"

        private String thirdPrice;

        public void setThirdPrice(String thirdPrice) {
            this.thirdPrice = thirdPrice;
        }

        public String getThirdPrice() {
            return thirdPrice;

        }

        public String getData() {
            return data;
        }

        public String getFacePrice() {
            return facePrice;
        }

        public String getSnPrice() {
            return snPrice;
        }

        public String getQgPrice() {
            return qgPrice;
        }

        public void setData(String data) {
            this.data = data;
        }

        public void setFacePrice(String facePrice) {
            this.facePrice = facePrice;
        }

        public void setSnPrice(String snPrice) {
            this.snPrice = snPrice;
        }

        public void setQgPrice(String qgPrice) {
            this.qgPrice = qgPrice;
        }
    }
}
