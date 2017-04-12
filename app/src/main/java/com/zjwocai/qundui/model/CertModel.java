package com.zjwocai.qundui.model;

import java.io.Serializable;

/**
 * 证件实体类
 * Created by NieLiQin on 2016/8/5.
 */
public class CertModel implements Serializable {


    /**
     * sfz1 : 2.jpg
     * sfz2 :
     * jsz :
     * xsz :
     */

    private String sfz1;
    private String sfz2;
    private String jsz;
    private String xsz;
    private String carHeadimg;

    public void setCarHeadimg(String carHeadimg) {
        this.carHeadimg = carHeadimg;
    }

    public String getCarHeadimg() {

        return carHeadimg;
    }

    public String getSfz1() {
        return sfz1;
    }

    public void setSfz1(String sfz1) {
        this.sfz1 = sfz1;
    }

    public String getSfz2() {
        return sfz2;
    }

    public void setSfz2(String sfz2) {
        this.sfz2 = sfz2;
    }

    public String getJsz() {
        return jsz;
    }

    public void setJsz(String jsz) {
        this.jsz = jsz;
    }

    public String getXsz() {
        return xsz;
    }

    public void setXsz(String xsz) {
        this.xsz = xsz;
    }
}
