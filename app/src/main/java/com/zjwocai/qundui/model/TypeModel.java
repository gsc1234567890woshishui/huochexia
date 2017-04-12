package com.zjwocai.qundui.model;

import com.zjwocai.qundui.R;

import java.io.Serializable;

/**
 * 类型选择实体类
 * Created by NieLiQin on 2016/8/1.
 */
public class TypeModel implements Serializable {
    public static final String CAT_XIANG = "1";  //厢式车
    public static final String CAT_PING = "2";   //平板车
    public static final String CAT_GAO = "3";    //高栏车
    public static final String CAT_ZHONG = "4";  //中栏车
    public static final String CAT_DI = "5";     //低栏车
    public static final String CAT_OTHER = "6";  //其它车型
    public static final String CAT_LEN = "7";    //冷藏车
    public static final String CAT_WEI = "8";    //危险品车
    public static final String CAT_ZI = "9";     //自卸货车
    public static final String CAT_JI = "10";    //集装箱车
    public static final String CAT_GAODI = "11"; //高低板车


    public static final String CARD_NONG = "1"; //农业银行
    public static final String CARD_ZHONGGUO = "2"; //中国银行
    public static final String CARD_JIAO = "3"; //交通银行
    public static final String CARD_ZHAO = "4"; //招商银行
    public static final String CARD_JIAN = "5"; //建设银行
    public static final String CARD_XIN = "6"; //兴业银行
    public static final String CARD_ZHONG = "7"; //中信银行
    public static final String CARD_GUANG = "8"; //光大银行
    public static final String CARD_GONG = "9"; //工商银行
    public static final String CARD_HUA = "10"; //华夏银行
    public static final String CARD_MIN = "11"; //民生银行
    public static final String CARD_PU = "12"; //浦发银行
    public static final String CARD_SHANG = "13"; //上海浦东发展银行
    public static final String CARD_BEI = "14"; //北京银行
    public static final String CARD_NAN = "15"; //南京银行
    public static final String CARD_NING = "16"; //宁波银行
    public static final String CARD_OTHER = "17"; //其它


    public static final String NO_LIMIT = "1"; //不限
    public static final String _4_2 = "2"; //4.2米
    public static final String _5_2 = "3"; //5.2米
    public static final String _6_2 = "4"; //6.2米
    public static final String _6_8 = "5"; //6.8米
    public static final String _7_2 = "6"; //7.2米
    public static final String _8_2 = "7"; //8.2米
    public static final String _8_6 = "8"; //8.6米
    public static final String _9_6 = "9"; //9.6米
    public static final String _11_7 = "10"; //11.7米
    public static final String _12_5 = "11"; //12.5米
    public static final String _13 = "12"; //13米
    public static final String _13_5 = "13"; //13.5米
    public static final String _14 = "14"; //14米
    public static final String _17 = "15"; //17米
    public static final String _17_5 = "16"; //17.5米
    public static final String _18 = "17"; //18米

    public static String getLength(String type) {
        String ret = "不限";
        if (type == null || type.equals("")) {
            return ret;
        }
        switch (type) {
            case NO_LIMIT:
                ret = "不限";
                break;
            case _4_2:
                ret = "4.2米";
                break;
            case _5_2:
                ret = "5.2米";
                break;
            case _6_2:
                ret = "6.2米";
                break;
            case _6_8:
                ret = "6.8米";
                break;
            case _7_2:
                ret = "7.2米";
                break;
            case _8_2:
                ret = "8.2米";
                break;
            case _8_6:
                ret = "8.6米";
                break;
            case _9_6:
                ret = "9.6米";
                break;
            case _11_7:
                ret = "11.7米";
                break;
            case _12_5:
                ret = "12.5米";
                break;
            case _13:
                ret = "13米";
                break;
            case _13_5:
                ret = "13.5米";
                break;
            case _14:
                ret = "14米";
                break;
            case _17:
                ret = "17米";
                break;
            case _17_5:
                ret = "17.5米";
                break;
            case _18:
                ret = "18米";
                break;

        }
        return ret;
    }


    public static String getCarDType(String type) {
        String ret = "未知";
        if (type == null || type.equals("")) {
            return ret;
        }
        switch (type) {
            case CARD_NONG:
                ret = "农业银行";
                break;
            case CARD_ZHONGGUO:
                ret = "中国银行";
                break;
            case CARD_JIAO:
                ret = "交通银行";
                break;
            case CARD_ZHAO:
                ret = "招商银行";
                break;
            case CARD_JIAN:
                ret = "建设银行";
                break;
            case CARD_XIN:
                ret = "兴业银行";
                break;
            case CARD_ZHONG:
                ret = "中信银行";
                break;
            case CARD_GUANG:
                ret = "光大银行";
                break;
            case CARD_GONG:
                ret = "工商银行";
                break;
            case CARD_HUA:
                ret = "华夏银行";
                break;
            case CARD_MIN:
                ret = "民生银行";
                break;
            case CARD_PU:
                ret = "浦发银行";
                break;
            case CARD_SHANG:
                ret = "上海浦东发展银行";
                break;
            case CARD_BEI:
                ret = "北京银行";
                break;
            case CARD_NAN:
                ret = "南京银行";
                break;
            case CARD_NING:
                ret = "宁波银行";
                break;
            case CARD_OTHER:
                ret = "其它";
                break;

        }
        return ret;
    }

    public static int getCarDIcon(String type) {
        int ret = R.drawable.ic_bank_qita;
        if (type == null || type.equals("")) {
            return ret;
        }
        switch (type) {
            case CARD_NONG:
                ret = R.drawable.ic_bank_nongye;
                break;
            case CARD_ZHONGGUO:
                ret = R.drawable.ic_bank_zhongguo;
                break;
            case CARD_JIAO:
                ret = R.drawable.ic_bank_jiaotong;
                break;
            case CARD_ZHAO:
                ret = R.drawable.ic_bank_zhaoshang;
                break;
            case CARD_JIAN:
                ret = R.drawable.ic_bank_jianshe;
                break;
            case CARD_XIN:
                ret = R.drawable.ic_bank_xinye;
                break;
            case CARD_ZHONG:
                ret = R.drawable.ic_bank_zhongxin;
                break;
            case CARD_GUANG:
                ret = R.drawable.ic_bank_guangda;
                break;
            case CARD_GONG:
                ret = R.drawable.ic_bank_gongshang;
                break;
            case CARD_HUA:
                ret = R.drawable.ic_bank_huaxia;
                break;
            case CARD_MIN:
                ret = R.drawable.ic_bank_minsheng;
                break;
            case CARD_PU:
                ret = R.drawable.ic_bank_pufa;
                break;
            case CARD_SHANG:
                ret = R.drawable.ic_bank_pufa;
                break;
            case CARD_BEI:
                ret = R.drawable.ic_bank_beijing;
                break;
            case CARD_NAN:
                ret = R.drawable.ic_bank_nanjing;
                break;
            case CARD_NING:
                ret = R.drawable.ic_bank_ningbo;
                break;
            case CARD_OTHER:
                ret = R.drawable.ic_bank_qita;
                break;

        }
        return ret;
    }


    public static String getCarType(String type) {
        String ret = "不限";
        if (type == null || type.equals("")) {
            return ret;
        }
        switch (type) {
            case CAT_XIANG:
                ret = "厢式车";
                break;
            case CAT_PING:
                ret = "平板车";
                break;
            case CAT_GAO:
                ret = "高栏车";
                break;
            case CAT_ZHONG:
                ret = "中栏车";
                break;
            case CAT_DI:
                ret = "低栏车";
                break;
            case CAT_OTHER:
                ret = "其它车型";
                break;
            case CAT_LEN:
                ret = "冷藏车";
                break;
            case CAT_WEI:
                ret = "危险品车";
                break;
            case CAT_ZI:
                ret = "自卸货车";
                break;
            case CAT_JI:
                ret = "集装箱车";
                break;
            case CAT_GAODI:
                ret = "高低板车";
                break;

        }
        return ret;
    }

    public static String getCarWeight(String weight){
        String carWeight = "";
        try {
            double current = Double.parseDouble(weight);
            if (current < 10 || current == 10)
                carWeight = "10吨及以下";
            else if (current < 20 || current == 20)
                carWeight = "20吨";
            else if (current < 30 || current == 30)
                carWeight = "30吨";
            else
                carWeight = "30吨以上";
        }catch (NumberFormatException e){
            carWeight = "未知";
        }


        return carWeight;
    }

}
