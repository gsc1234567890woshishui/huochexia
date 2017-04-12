package com.zjwocai.qundui.util;

public class Utils {
    public static final String DAY_NIGHT_MODE = "daynightmode";
    public static final String DEVIATION = "deviationrecalculation";
    public static final String JAM = "jamrecalculation";
    public static final String TRAFFIC = "trafficbroadcast";
    public static final String CAMERA = "camerabroadcast";
    public static final String SCREEN = "screenon";
    public static final String THEME = "theme";
    public static final String ISEMULATOR = "isemulator";


    public static final String ACTIVITYINDEX = "activityindex";

    public static final int SIMPLEHUDNAVIE = 0;
    public static final int EMULATORNAVI = 1;
    public static final int SIMPLEGPSNAVI = 2;
    public static final int SIMPLEROUTENAVI = 3;


    public static final boolean DAY_MODE = false;
    public static final boolean NIGHT_MODE = true;
    public static final boolean YES_MODE = true;
    public static final boolean NO_MODE = false;
    public static final boolean OPEN_MODE = true;
    public static final boolean CLOSE_MODE = false;


    /**
     * 格式化距离显示 km或m 2位小数
     *
     * @param distances
     * @return
     */
    public static String formatDis(double distances) {
        if (distances > 1000) {
            distances = distances / 1000;
            if (distances > 50) {
                return ((int) distances) + "公里";
            } else {
                return (((int) (distances * 100)) / 100.f) + "公里";
            }
        } else {
            return ((int) distances) + "米";
        }
    }
}
