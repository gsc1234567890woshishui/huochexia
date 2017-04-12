package com.zjwocai.qundui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Process;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.threeti.teamlibrary.activity.BaseProtocolActivity;
import com.threeti.teamlibrary.finals.ProjectConfig;
import com.threeti.teamlibrary.finals.ProjectConstant;
import com.threeti.teamlibrary.net.FileDownLoadTask;
import com.threeti.teamlibrary.utils.SPUtil;
import com.zjwocai.qundui.model.UserObj;

import java.io.File;
import java.util.UUID;

/**
 * @author Bassam
 * @version V1.0
 * @Package com.zjwocai.threeespeak.activity
 * @Title
 * @Description
 * @date 16/3/14
 */
public abstract class BaseProjectActivity extends BaseProtocolActivity implements FileDownLoadTask.DownLoadCallBack {

    private String picName = "";
    private PowerManager powerManager = null;
    private ProgressDialog dia;

    protected static final int INTENT_TAKE = 101;// 拍照
    protected static final int INTENT_SELECT = 102;// 从相册选
    protected static final int INTENT_CUT = 104;// 图片剪裁

    public BaseProjectActivity(int resId) {
        super(resId);
    }

    public BaseProjectActivity(int resId, boolean needFinish) {
        super(resId, needFinish);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void InitpowerManager() {
        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
    }

//    protected void initHeadMenu(int type) {
//        needFinish = true;
//        setFinishListener(new finishListener() {
//            @Override
//            public void onFinishListener() {
//                UserObj obj = getUserData();
//                if (obj != null && !obj.isAuto()) {
//                    setUserData(null);
//                }
//
//            }
//        });
//        InitpowerManager();
//    }

    /**
     * 简便获取本地的用户数据
     *
     * @return
     */
    public UserObj getUserData() {
        return (UserObj) SPUtil.getObjectFromShare(ProjectConstant.KEY_USERINFO);
    }

    /**
     * 重置本地的用户数据
     *
     * @param user 需要重置的用户数据
     */
    protected void setUserData(UserObj user) {
        SPUtil.saveObjectToShare(this, ProjectConstant.KEY_USERINFO, user);
    }


    /**
     * 显示拍照的选择对话框
     */
    public void showPictureDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] mItems = {"拍照", "从手机相册选择"};
        builder.setItems(mItems, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:// 拍照
                        takePhoto();
                        break;
                    case 1:// 从相册选
                        selectFromAlbum();
                        break;
                }
            }
        }).create().show();
    }

    /**
     * 拍照
     */
    protected void takePhoto() {
        try {
            File file = new File(ProjectConfig.DIR_IMG);
            if (!file.exists()) {
                file.mkdirs();
            }
            picName = UuidName(".jpg");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(ProjectConfig.DIR_IMG + picName)));
            startActivityForResult(intent, INTENT_TAKE);
        } catch (ActivityNotFoundException e) {
            showToast("拍照异常");
        }
    }

    /**
     * 从相册选照片
     */
    protected void selectFromAlbum() {
        try {
            Intent intent = null;
            if (Build.VERSION.SDK_INT < 19) {// 先让用户选择接收到该请求的APP，可以从文件系统直接选取图片
                intent = new Intent(Intent.ACTION_GET_CONTENT, null);
            } else {// 直接打开图库，只能选择图库的图片
                intent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            }
            intent.setType("image/*");
            picName = UuidName(".jpg");
            startActivityForResult(intent, INTENT_SELECT);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            showToast("没有找到所选照片");
        }
    }

    protected String UuidName(String suffix) {
        String time = String.valueOf(System.currentTimeMillis());
        return UUID.nameUUIDFromBytes(time.getBytes()).toString() + suffix;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case INTENT_CUT: // 裁剪返回的
                if (resultCode == RESULT_OK) {
                    showData(ProjectConfig.DIR_IMG + picName, true);
                } else if (resultCode == RESULT_CANCELED) {
                    // 用户取消了视频捕捉
                    showToast("你取消获取照片");
                } else {
                    // 视频捕捉失败,建议用户
                    showToast("获取照片失败");
                }
                break;
            case INTENT_TAKE:// 拍照跳裁剪
                Uri uri = Uri.fromFile(new File(ProjectConfig.DIR_IMG + picName));
                picName = UuidName(".jpg");
                Uri uri2 = Uri.fromFile(new File(ProjectConfig.DIR_IMG + picName));
                startCrop(uri, uri2);
                break;
            case INTENT_SELECT:// 相册跳裁剪
                if (data == null || data.getData() == null) {
                    return;
                }
                startCrop(data.getData(),
                        Uri.fromFile(new File(ProjectConfig.DIR_IMG + picName)));
                break;
        }
    }

    protected void startCrop(Uri inputUri, Uri outUri) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(inputUri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
            startActivityForResult(intent, INTENT_CUT);
        } catch (Exception e) {
        }
    }

    /**
     * 成功获取到路径的后续操作
     *
     * @param filePath
     * @param isPhoto
     */
    protected void showData(String filePath, boolean isPhoto) {
    }

    private boolean finish = true;

    public void showDownLoadDialog(String Updateurl,String Latestv,String Qzupdate) {
        final String url = Updateurl;
        if ("0".equals(Latestv)) {//是否有更新
            if ("1".equals(Qzupdate)) {//强制更新
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle("版本更新")
                        .setMessage("检查到最新版本，是否立即更新？")
                        .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (TextUtils.isEmpty(url)) {
                                    showToast("更新失败!");
                                    dialog.dismiss();
                                    return;
                                }
                                finish = false;
                                dialog.dismiss();
                                FileDownLoadTask downtask = new FileDownLoadTask(
                                        url, "updata.apk",
                                        BaseProjectActivity.this, BaseProjectActivity.this, "下载更新中...", false);
                                downtask.execute();
                            }
                        });
                dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (finish) {
                            finishAll();
                            Process.killProcess(Process.myPid());
                            Runtime.getRuntime().gc();
                        }
                    }
                });
                dialog.show();
            } else {//非强制更新
                SPUtil.saveObjectToShare(ProjectConstant.KEY_HAS_UPDATE, "1");
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle("版本更新")
                        .setMessage("检查到最新版本，是否立即更新？")
                        .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (TextUtils.isEmpty(url)) {
                                    showToast("更新失败!");
                                    dialog.dismiss();
                                    return;
                                }
                                dialog.dismiss();
                                FileDownLoadTask downtask = new FileDownLoadTask(
                                        url, "updata.apk",
                                        BaseProjectActivity.this, BaseProjectActivity.this, "下载更新中...",false);
                                downtask.execute();
                            }
                        })
                        .setNegativeButton("取消", null);
                dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        }
    }

    @Override
    public void callBack(boolean isScuss, String filepath) {
        if (isScuss) {
            installApk(new File(filepath));
        } else {
        }
    }

    public void installApk(File file) {
        Intent intent = new Intent();// 执行动作
        intent.setAction(Intent.ACTION_VIEW);// 执行的数据类型
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");// 编者按：此处Android应为android，否则造成安装不了
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
