package com.threeti.teamlibrary.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.threeti.teamlibrary.ApplicationEx;

import org.apache.http.conn.util.InetAddressUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;


public class DriveInfoUtil {
    private static DriveInfoUtil driveInfo;

    public static DriveInfoUtil getInstance() {
        if (driveInfo == null) {
            driveInfo = new DriveInfoUtil();
        }
        return driveInfo;
    }

    public String getDeviceID() {
        StringBuilder deviceId = new StringBuilder();
        deviceId.append("a");
        if (!TextUtils.isEmpty(getIMEI())) {
            deviceId.append("iemi");
            deviceId.append(getIMEI());
        }
        if (!TextUtils.isEmpty(getSN())) {
            deviceId.append("sn");
            deviceId.append(getSN());
        }
        if (!TextUtils.isEmpty(getLocalMacAddress())) {
            deviceId.append("mac");
            deviceId.append(getLocalMacAddress());

        }
        return deviceId.toString();
    }

    public String getIMEI() {
        TelephonyManager telephonyManager = (TelephonyManager) ApplicationEx.getInstance().getSystemService(
                Context.TELEPHONY_SERVICE);
        if (telephonyManager == null)
            return "";
        return telephonyManager.getDeviceId();
    }

    public String getSN() {
        TelephonyManager telephonyManager = (TelephonyManager) ApplicationEx.getInstance().getSystemService(
                Context.TELEPHONY_SERVICE);
        if (telephonyManager == null)
            return "";
        return telephonyManager.getSimSerialNumber();
    }

    public String getLocalIPAddress() {
        try {
            for (Enumeration<NetworkInterface> mEnumeration = NetworkInterface.getNetworkInterfaces(); mEnumeration
                    .hasMoreElements(); ) {

                NetworkInterface intf = mEnumeration.nextElement();
                for (Enumeration<InetAddress> enumIPAddr = intf.getInetAddresses(); enumIPAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIPAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getLocalMacAddress() {
        WifiManager wifi = (WifiManager) ApplicationEx.getInstance().getSystemService(Context.WIFI_SERVICE);
        if (wifi == null)
            return "";
        WifiInfo info = wifi.getConnectionInfo();
        if (info == null)
            return "";
        String mac = info.getMacAddress();
        return mac == null ? "" : mac;
    }

    public String getDriveMode() {
        return Build.MANUFACTURER + Build.MODEL;
    }

    public String getOsVison() {
        return Build.VERSION.RELEASE;
    }
}
