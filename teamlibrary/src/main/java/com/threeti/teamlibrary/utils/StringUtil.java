package com.threeti.teamlibrary.utils;

import android.text.TextUtils;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 */
public class StringUtil {
    private static HashCodeFileNameGenerator fileUtil;

    /**
     * 判断是否是全数字
     *
     * @param mobiles
     * @return
     */
    public static boolean isNO(String mobiles) {
        Pattern p = Pattern.compile("^[0-9]*$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 判断是否是手机号
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^[0-9]*$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 判断是否是邮箱地址
     *
     * @param mail
     * @return
     */
    public static boolean isMail(String mail) {
        if (mail == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches();
        }
    }

    /**
     * 根据url，得到唯一的字符串
     *
     * @param url
     * @return
     */
    public static String getUrlCode(String url) {
        if (fileUtil == null) {
            fileUtil = new HashCodeFileNameGenerator();
        }
        return fileUtil.generate(url);
    }

    /**
     * @param type 1 from web; 2 from sdcard; 3 from content; 4 from assets; 5 from drawable
     * @return
     */
    public static String getFileUrlHead(int type) {
        switch (type) {
            case 1:
                return "http://";
            case 2:
                return "file://";
            case 3:
                return "content://";
            case 4:
                return "assets://";
            case 5:
                return "drawable://";
        }
        return "";
    }

    /**
     * 解析浮点型数据
     *
     * @param string
     * @return
     */
    public static float parseFloat(String string) {
        if (TextUtils.isEmpty(string)) {
            return 0;
        }
        try {
            return Float.parseFloat(string);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 解析Int类型数据
     *
     * @param string
     * @return
     */
    public static int parseInt(String string) {
        if (TextUtils.isEmpty(string)) {
            return 0;
        }
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 解析Double类型数据
     *
     * @param string
     * @return
     */
    public static double parseDouble(String string) {
        if (TextUtils.isEmpty(string)) {
            return 0;
        }
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 解析长整型数据
     *
     * @param string
     * @return
     */
    public static long parseLong(String string) {
        if (TextUtils.isEmpty(string)) {
            return 0;
        }
        try {
            return Long.parseLong(string);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 解析Boolean类型数据
     *
     * @param string
     * @return
     */
    public static boolean parseBoolean(String string) {
        if (TextUtils.isEmpty(string)) {
            return false;
        }
        try {
            return Boolean.parseBoolean(string);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 截取字符串
     *
     * @param str
     * @param start
     * @return
     */
    public static String subString(String str, int start) {
        if (start >= str.length())
            return str;
        else
            return str.substring(start);
    }

    /**
     * 截取字符串
     *
     * @param str
     * @param start
     * @param end
     * @return
     */
    public static String subString(String str, int start, int end) {
        if (TextUtils.isEmpty(str) || start >= str.length() || end >= str.length())
            return str;
        else
            return str.substring(start, end);
    }

    /**
     * 将byte数组变成String输出
     *
     * @param b
     * @return
     */
    public static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder();
        if (b == null) return null;
        for (byte aB : b) {
            String hex = Integer.toHexString(aB & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex).append(" ");
        }
        return sb.toString();
    }

    /**
     * 解析int类型数据
     *
     * @param data
     * @param pos
     * @param len
     * @return
     */
    public static int parseInt(byte[] data, int pos, int len) {
        return parseInt(data, pos, len, false);
    }

    public static int parseInt(byte[] data, int pos, int len, boolean bigEndian) {
        int ret = 0;
        for (int i = 0; i < len; i++) {
            ret += ((data[pos + i] & 0xff)) << ((bigEndian ? i : len - 1 - i) * 8);
        }
        return ret;
    }

    public static int parseIntForWord(byte[] data, int pos, boolean bigEndian) {
        int ret = 0;
        if (data == null && data.length < (pos + 2)) {
            return 0;
        }
        if (bigEndian) {
            ret += ((data[pos] & 0xff));
            ret += ((data[pos + 1]) << 8);
        } else {
            ret += ((data[pos + 1] & 0xff));
            ret += ((data[pos]) << 8);
        }
        return ret;
    }

    public static int parseIntForDWord(byte[] data, int pos, boolean bigEndian) {
        int ret = 0;
        if (data == null && data.length < (pos + 4)) {
            return 0;
        }
        if (bigEndian) {
            ret += ((data[pos] & 0xff));
            ret += ((data[pos + 1] & 0xff) << 8);
            ret += ((data[pos + 2] & 0xff) << 16);
            ret += ((data[pos + 3]) << 24);
        } else {
            ret += ((data[pos]) << 24);
            ret += ((data[pos + 1] & 0xff) << 16);
            ret += ((data[pos + 2] & 0xff) << 8);
            ret += ((data[pos + 3] & 0xff));
        }
        return ret;
    }
}
