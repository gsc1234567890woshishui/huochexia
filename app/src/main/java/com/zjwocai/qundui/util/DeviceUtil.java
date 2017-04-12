package com.zjwocai.qundui.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

/**
 * 设备系统工具类
 * 
 * @author lanyan
 * 
 */
public class DeviceUtil {

	/**
	 * 获取设备IMEI号
	 * 
	 * @param ctx
	 * @return
	 */
	public static String getIMEI(Context ctx) {
		if (ctx == null) {
			return "";
		}
		return ((TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
	}

	/**
	 * 获取手机IMSI号
	 * 
	 * @param ctx
	 * @return
	 */
	public static String getIMSI(Context ctx) {
		if (ctx == null) {
			return "";
		}
		return ((TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
	}

	/**
	 * 返回系统版本号
	 * 
	 * @return
	 */
	public static String getSysVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 返回手机品牌
	 * 
	 * @return
	 */
	public static String getPhoneBrand() {
		return android.os.Build.BRAND;
	}

	/**
	 * 返回手机型号
	 * 
	 * @return
	 */
	public static String getPhoneModel() {
		return android.os.Build.MODEL;
	}

	/**
	 * 获取版本号
	 * 
	 * @return 当前应用的版本号
	 */
	public static String getVersion(Context ctx) {
		try {
			PackageManager manager = ctx.getPackageManager();
			PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
			return info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static void makeCall(final Context ctx, final String num) {
		AlertDialog.Builder build = new AlertDialog.Builder(ctx);
		build.setTitle("提示").setMessage("确定拨打电话：" + num + "?")
				.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							Intent intent = new Intent(Intent.ACTION_CALL, Uri
									.parse("tel:" + num));
							ctx.startActivity(intent);
						} catch (Exception e) {
							Toast.makeText(ctx, "电话功能无法使用", Toast.LENGTH_LONG)
									.show();
							e.printStackTrace();
						}
					}
				}).setNegativeButton("取消", null).create().show();
	}

	/**
	 * 隐藏键盘
	 * 
	 * @param et
	 *            需要隐藏键盘对于的EditText
	 */
	public static void hideKeyboard(Context ctx, EditText et) {
		if (et == null) {
			return;
		}
		InputMethodManager imm = (InputMethodManager) ctx
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
	}

	/**
	 * 判断当前应用程序处于前台还是后台
	 */
	public static boolean isApplicationBroughtToBackground(final Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		@SuppressWarnings("deprecation")
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}
}
