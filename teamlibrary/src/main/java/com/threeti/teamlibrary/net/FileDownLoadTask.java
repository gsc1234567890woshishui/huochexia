package com.threeti.teamlibrary.net;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.threeti.teamlibrary.activity.BaseProtocolActivity;
import com.threeti.teamlibrary.finals.ProjectConfig;
import com.threeti.teamlibrary.utils.DialogUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class FileDownLoadTask extends AsyncTask<Void, Integer, Boolean> {
    private Dialog dialog;
    private DownLoadCallBack callback;
    private String downurl;
    private String filename;

    public FileDownLoadTask(String url, String filename, BaseProtocolActivity activity, DownLoadCallBack callback) {
        this.callback = callback;
        this.downurl = url;
        this.filename = filename;
    }


    public FileDownLoadTask(String url, String filename, BaseProtocolActivity activity, DownLoadCallBack callback, String msg) {
        this.callback = callback;
        this.downurl = url;
        this.filename = filename;
        this.dialog = DialogUtil.getProgressDialog(activity, msg, true);
    }

    public FileDownLoadTask(String url, String filename, BaseProtocolActivity activity, DownLoadCallBack callback, String msg, boolean cancancle) {
        this.callback = callback;
        this.downurl = url;
        this.filename = filename;
        this.dialog = DialogUtil.getProgressDialog(activity, msg, cancancle);
    }

    @Override
    protected void onPreExecute() {
        if (null != dialog) {
            ((ProgressDialog) dialog).setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.show();
        }
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (dialog != null)
            ((ProgressDialog) dialog).setProgress(values[0]);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            URL url;
            url = new URL(downurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            if (dialog != null)
                ((ProgressDialog) dialog).setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
//            filename = System.currentTimeMillis() + filename;
            File updataFile = new File(ProjectConfig.DIR_DOWNLOAD, filename);
            FileOutputStream fos = new FileOutputStream(updataFile);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                publishProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        dismissProgress();
        if (callback != null) {
            callback.callBack(result, ProjectConfig.DIR_DOWNLOAD + filename);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        dismissProgress();
    }

    protected void dismissProgress() {
        try {
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDialog() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public interface DownLoadCallBack {
        public void callBack(boolean isScuss, String filepath);
    }

}
