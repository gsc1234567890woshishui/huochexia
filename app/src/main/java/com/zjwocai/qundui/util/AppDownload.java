package com.zjwocai.qundui.util;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import com.threeti.teamlibrary.utils.*;

import com.threeti.teamlibrary.utils.SPUtil;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.model.SoftUpgradeViewModel;

import java.io.File;
import java.util.List;


/**
 * @author 周楠
 * @date 2015-11-3 上午9:21:43
 * @Description: App下载类
 */
public class AppDownload {
	public static final String S_EXIU = "huochexia";
	public static final String S_DOWNLOAD = "download";
	public static final String HUOCHEXIA = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
			+ S_EXIU;
	public static final String DOWNLOAD = HUOCHEXIA + File.separator + S_DOWNLOAD;
	// 下载
	private Context mContext;
	private DownloadManagerPro downloadManagerPro;
	private CompleteReceiver completeReceiver;
	private long downloadId = 0;

	public AppDownload() {

	}

	public AppDownload(Context context) {
		this.mContext = context;
	}

	private static AppDownload instance = new AppDownload();

	public static AppDownload getInstance() {
		return instance;
	}

	/**
	 * 下载最新的apk
	 */
	@SuppressWarnings("deprecation")
	public void doDownloadApp(Context context, SoftUpgradeViewModel model) {
		this.mContext = context;
		// APP Name
		String appName = getApplicationName(mContext);
		// 下载后的文件名
		String downloadName = appName + "_" + model.getVersion() + ".apk";

		// 1.已下载，直接安装
		File oldFile = new File(DOWNLOAD+ downloadName);
		if (oldFile.exists()) {
			installApk(mContext, oldFile);
			return;
		}
		// 2.未下载，初始化下载
		File file = new File(DOWNLOAD);
		if (!file.exists()) {
			file.mkdirs();
		}

		DownloadManager downloadManager = (DownloadManager) mContext.getApplicationContext().getSystemService(
				Context.DOWNLOAD_SERVICE);
		downloadManagerPro = new DownloadManagerPro(downloadManager);
		completeReceiver = new CompleteReceiver();

		mContext.getApplicationContext().registerReceiver(completeReceiver,
				new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

		String downloadUrl = model.getDownload_url();
		System.out.println("eeeeeeeeeeeeeeeeee"+downloadUrl);
		if (TextUtils.isEmpty(downloadUrl)) {
			showShort("下载异常");
			return;
		}

		DownloadManager.Request request;
		try {
			request = new DownloadManager.Request(Uri.parse(downloadUrl));

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		// request.setDestinationInExternalPublicDir(Const.DIR.S_EXIU +
		// File.separator + Const.DIR.S_DOWNLOAD,
		// downloadName);
		try {
			if (hasSDCard()) {
				// 下载在sdCard的Download文件夹下，避免可能的NullPointer
				request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, downloadName);
			} else {
				// 没有sdCard则下载到getCacheDir()
				request.setDestinationInExternalPublicDir(context.getApplicationContext().getCacheDir().getAbsolutePath(),
						downloadName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setTitle("正在下载 " + appName);
		request.setShowRunningNotification(true);
		request.setVisibleInDownloadsUi(false);
		try {
			downloadId = downloadManager.enqueue(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class CompleteReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
			if (downloadManagerPro.getStatusById(completeDownloadId) == DownloadManager.STATUS_SUCCESSFUL) {
				String filename = downloadManagerPro.getFileName(completeDownloadId);
				if (downloadId == completeDownloadId) {
					install(context, filename);
				}
			} else {
				showShort(context.getString(R.string.download_error));
			}
		}
	}
	public static boolean hasSDCard() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}




	public static String getApplicationName(Context context) {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo;
		try {
			packageManager = context.getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
		return applicationName;
	}




	public static void installApk(Context context, File file) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		context.startActivity(intent);

	}

	public static boolean install(Context context, String filePath) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		File file = new File(filePath);
		if (file.length() > 0 && file.exists() && file.isFile()) {
			i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
			return true;
		}
		return false;
	}
	public void showShort(CharSequence message) {
		if (null != mContext) {
			Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
		}
	}



}
