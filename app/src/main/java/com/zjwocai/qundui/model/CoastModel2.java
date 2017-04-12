package com.zjwocai.qundui.model;

import java.util.List;

/**
 * Created by qundui on 2017/2/16.
 */

public class CoastModel2 {

    /**
     * province : 浙江
     * catName : 中国移动
     * carrier : 浙江移动
     * itemList:套餐
     */

    private String province;
    private String catName;
    private String carrier;
    private List<ItemListBean> itemList;

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

    public List<ItemListBean> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemListBean> itemList) {
        this.itemList = itemList;
    }

    public static class ItemListBean {
        /**
         * facePrice : 50.00
         * salePrice : 49.85
         * thirdPrice : 49.75
         */

        private String facePrice;
        private String salePrice;
        private String thirdPrice;

        public String getFacePrice() {
            return facePrice;
        }

        public void setFacePrice(String facePrice) {
            this.facePrice = facePrice;
        }

        public String getSalePrice() {
            return salePrice;
        }

        public void setSalePrice(String salePrice) {
            this.salePrice = salePrice;
        }

        public String getThirdPrice() {
            return thirdPrice;
        }

        public void setThirdPrice(String thirdPrice) {
            this.thirdPrice = thirdPrice;
        }
    }
}
