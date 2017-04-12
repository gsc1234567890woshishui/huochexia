package com.threeti.teamlibrary.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by WangXY on 2015/12/09.22:07.
 */
public class MD5Util {
    protected static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    protected static MessageDigest messagedigest = null;

    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsaex) {
            System.err.println(MD5Util.class.getName() + "初始化失败，MessageDigest不支持MD5Util。");
            nsaex.printStackTrace();
        }
    }

    public static String getMD5String(String s) {
        return getMD5String(s.getBytes());
    }

    public static boolean checkPassword(String password, String md5PwdStr) {
        String s = getMD5String(password);
        return s.equals(md5PwdStr);
    }

    public static String getFileMD5String(File file) throws IOException {
        InputStream fis;
        fis = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int numRead = 0;
        while ((numRead = fis.read(buffer)) > 0) {
            messagedigest.update(buffer, 0, numRead);
        }
        fis.close();
        return bufferToHex(messagedigest.digest());
    }

    public static String getMD5String(byte[] bytes) {
        messagedigest.update(bytes);
        return bufferToHex(messagedigest.digest());
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];// 取字节中高 4 位的数字转换, >>>
        // 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同
        char c1 = hexDigits[bt & 0xf];// 取字节中低 4 位的数字转换
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    public static void main(String[] args) throws IOException {
        long begin = System.currentTimeMillis();

        File file = new File("D:/resource/android480_1.png");
        String md5 = getFileMD5String(file);

        long end = System.currentTimeMillis();
        System.out.println("md5:" + md5 + " time:" + ((end - begin) / 1000) + "s");
    }


    public static String getSign(String noncestr, String timestamp, String appcode, String service, String action) {
        String[] str = {noncestr, timestamp, appcode, service, action};
        Arrays.sort(str);
        StringBuffer content = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            content.append(str[i]);
        }
        return getMD5String(content.toString().getBytes());
    }

    public static String getRandomForLen(int len) {
        String str = "";
        Random ran = new Random();
        for (int i = 0; i < len; i++) {
            str += ran.nextInt(9);
        }
        return str;
    }
}
