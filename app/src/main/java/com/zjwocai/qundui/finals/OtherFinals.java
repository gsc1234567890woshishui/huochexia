package com.zjwocai.qundui.finals;

import java.util.Arrays;
import java.util.List;

/**
 * 其他常量类，不好分类的，放在这里面
 *
 * @author lanyan
 */
public class OtherFinals {
    // --------------应用缓存文件路径-----------------------


    public static final String PAGE_SIZE = "16";// 分页加载条数

    //省份简称
    public static final List<String> PROVINCES = Arrays.asList(
            "京", "津", "沪", "渝", "冀", "豫", "鲁", "晋", "陕", "皖", "苏", "浙", "鄂", "湘",
            "赣", "闽", "粤", "桂", "琼", "川", "贵", "云", "辽", "吉", "黑", "蒙", "甘", "宁",
            "青", "新", "藏", "港", "澳", "台", " ", " ");

    //字母表
    public static final List<String> LETTERS = Arrays.asList(
            "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M",
            "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");

    //数字
    public static final List<String> NUMBERS = Arrays.asList(
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", " ", " ");

}
