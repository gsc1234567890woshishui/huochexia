package com.zjwocai.qundui.activity.mine;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.threeti.teamlibrary.activity.BaseProtocolActivity;
import com.threeti.teamlibrary.finals.ProjectConfig;
import com.threeti.teamlibrary.net.BaseModel;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.activity.login.LoginActivity;
import com.zjwocai.qundui.bill.ProtocolBill;
import com.zjwocai.qundui.fragment.EventMessage;
import com.zjwocai.qundui.model.CertModel;
import com.zjwocai.qundui.model.UploadModel;
import com.zjwocai.qundui.util.BitmapUtil;
import com.zjwocai.qundui.util.DialogUtil;
import com.zjwocai.qundui.util.ImageUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 我的证件
 * Created by NieLiQin on 2016/7/26.
 */
public class CertificationActivity extends BaseProtocolActivity implements View.OnClickListener {
    private ImageView ivFront, ivBack, ivDriving, ivDriver, ivFrontEdit, ivBackEdit, ivDrivingEdit, ivDriverEdit;
    private LinearLayout llCertification;
    private Dialog dialog;
    private String mCurrentPhotoPath;
    private static final int REQUEST_IMAGE_GET = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private int type = 1;
    private ProgressDialog netDialog;
    private CertModel certModel;
    private DisplayImageOptions op1;
    private DisplayImageOptions op2;
    private String filePath;
    private String path;
    private Runnable runnable;
    private Handler handler;

    public CertificationActivity() {
        super(R.layout.act_certification);
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        ProtocolBill.getInstance().getCertification(this, this);
    }

    @Override
    public void findIds() {
        llCertification = (LinearLayout) findViewById(R.id.ll_certification);
        ivFront = (ImageView) findViewById(R.id.iv_front);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivDriving = (ImageView) findViewById(R.id.iv_driving_license);
        ivDriver = (ImageView) findViewById(R.id.iv_driver_license);
        ivFrontEdit = (ImageView) findViewById(R.id.iv_front_edit);
        ivBackEdit = (ImageView) findViewById(R.id.iv_back_edit);
        ivDrivingEdit = (ImageView) findViewById(R.id.iv_driving_license_edit);
        ivDriverEdit = (ImageView) findViewById(R.id.iv_driver_license_edit);

        op1 = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_pic_update_small).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.ic_pic_update_small).showImageOnFail(R.drawable.ic_pic_update_small)
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        op2 = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_pic_update).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.ic_pic_update).showImageOnFail(R.drawable.ic_pic_update)
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    @Override
    public void initViews() {
        initTitle("我的证件");
        title.setLeftIcon(R.drawable.ic_title_left, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title.setRightText("示例", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PopupWindows(CertificationActivity.this, llCertification);
            }
        });

        ivFront.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivDriving.setOnClickListener(this);
        ivDriver.setOnClickListener(this);

        netDialog = DialogUtil.getProgressDialog(this, getResources().getString(R.string.ui_net_request));

        runnable = new Runnable() {
            @Override
            public void run() {
                // 自定义大小，防止OOM
                Bitmap mp = BitmapUtil.getimage(filePath);
                mp = ImageUtil.compressImage(mp);
                path = BitmapUtil.save(ProjectConfig.DIR_IMG + File.separator +
                        System.currentTimeMillis() + ".jpg", mp);
                Message m = new Message();
                m.what = 1;
                handler.handleMessage(m);
            }
        };

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    ProtocolBill.getInstance().upload(CertificationActivity.this, CertificationActivity.this, path);
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_front:
                type = 1;
                showPhotoDialog();
                break;
            case R.id.iv_back:
                type = 2;
                showPhotoDialog();
                break;
            case R.id.iv_driving_license:
                type = 3;
                showPhotoDialog();
                break;
            case R.id.iv_driver_license:
                type = 4;
                showPhotoDialog();
                break;
        }
    }

    @Override
    public void onTaskSuccess(BaseModel result) {
        if (RQ_GET_CERTIFICATION.equals(result.getRequest_code())) {
            certModel = (CertModel) result.getResult();
            if (certModel != null) {
                if (certModel.getSfz1() != null && !certModel.getSfz1().equals("")) {
                    imageLoader.displayImage(ProjectConfig.IMAGE_URL + certModel.getSfz1(), ivFront, op1);
                    ivFrontEdit.setVisibility(View.VISIBLE);
                }
                if (certModel.getSfz2() != null && !certModel.getSfz2().equals("")) {
                    imageLoader.displayImage(ProjectConfig.IMAGE_URL + certModel.getSfz2(), ivBack, op1);
                    ivBackEdit.setVisibility(View.VISIBLE);
                }
                if (certModel.getJsz() != null && !certModel.getJsz().equals("")) {
                    ivDriving.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    imageLoader.displayImage(ProjectConfig.IMAGE_URL + certModel.getJsz(), ivDriving, op2);
                    ivDrivingEdit.setVisibility(View.VISIBLE);
                }
                if (certModel.getXsz() != null && !certModel.getXsz().equals("")) {
                    ivDriver.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    imageLoader.displayImage(ProjectConfig.IMAGE_URL + certModel.getXsz(), ivDriver, op2);
                    ivDriverEdit.setVisibility(View.VISIBLE);
                }
            }

        } else if (RQ_UPLOAD.equals(result.getRequest_code())) {
            List<UploadModel> models = (List<UploadModel>) result.getResult();
            if (models != null && !models.isEmpty()) {
                switch (type) {
                    case 1:
                        imageLoader.displayImage(ProjectConfig.IMAGE_URL + models.get(0).getFileurl3(), ivFront, op1);
                        ProtocolBill.getInstance().uploadCertification(this, this, models.get(0).getFileurl3(),
                                certModel.getSfz2(), certModel.getJsz(), certModel.getXsz());
                        ivFrontEdit.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        imageLoader.displayImage(ProjectConfig.IMAGE_URL + models.get(0).getFileurl3(), ivBack, op1);
                        ProtocolBill.getInstance().uploadCertification(this, this, certModel.getSfz1(),
                                models.get(0).getFileurl3(), certModel.getJsz(), certModel.getXsz());
                        ivBackEdit.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        ivDriving.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        ;
                        imageLoader.displayImage(ProjectConfig.IMAGE_URL + models.get(0).getFileurl3(), ivDriving, op2);
                        ProtocolBill.getInstance().uploadCertification(this, this, certModel.getSfz1(),
                                certModel.getSfz2(), models.get(0).getFileurl3(), certModel.getXsz());
                        ivDrivingEdit.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        ivDriver.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        imageLoader.displayImage(ProjectConfig.IMAGE_URL + models.get(0).getFileurl3(), ivDriver, op2);
                        ProtocolBill.getInstance().uploadCertification(this, this, certModel.getSfz1(),
                                certModel.getSfz2(), certModel.getJsz(), models.get(0).getFileurl3());
                        ivDriverEdit.setVisibility(View.VISIBLE);
                        break;
                }
            }
        } else if (RQ_UPLOAD_CERTIFICATION.equals(result.getRequest_code())) {
            netDialog.dismiss();
        }
    }

    @Override
    public void onTaskFail(@SuppressWarnings("rawtypes") BaseModel result) {
//        super.onTaskFail(result);
        if (result.getMsgtype() != null && result.getMsgtype().equals("2")) {
            showToast("登录失效，请重新登录");
            saveUser(null);
            startActivity(LoginActivity.class);
        } else if (!TextUtils.isEmpty(result.getMsg())) {
            showToast(result.getMsg() + "");
        }
    }

    private void showPhotoDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_select_head, null);
        TextView take_photo = (TextView) view.findViewById(R.id.take_photo);
        TextView select_album = (TextView) view.findViewById(R.id.select_album);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        select_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        dialog = DialogUtil.getDialog(CertificationActivity.this, view, Gravity.BOTTOM, true);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            filePath = null;
            if (requestCode == REQUEST_IMAGE_GET) {
                //返回的是content://的样式
                filePath = getFilePathFromContentUri(data.getData(), this);
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                if (mCurrentPhotoPath != null) {
                    filePath = mCurrentPhotoPath;
                }
            }
            if (!TextUtils.isEmpty(filePath)) {
                new Thread(runnable).start();
                netDialog.show();
            }
        }
    }

    public static String getFilePathFromContentUri(Uri uri, Context context) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};
        Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
        if (cursor == null) return null;
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    /**
     * 从相册中获取
     */
    public void selectImage() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        intent.setType("image/*");
        //判断系统中是否有处理该Intent的Activity
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        } else {
            showToast("未找到图片查看器");
        }
    }

    private void dispatchTakePictureIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断系统中是否有处理该Intent的Activity
        if (intent.resolveActivity(getPackageManager()) != null) {
            // 创建文件来保存拍的照片
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // 异常处理
            }
            if (photoFile != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        } else {
            showToast("无法启动相机");
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* 文件名 */
                ".jpg",         /* 后缀 */
                storageDir      /* 路径 */
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    class PopupWindows extends PopupWindow {

        public PopupWindows(Context mContext, View parent) {

            View view = View
                    .inflate(mContext, R.layout.view_popwindow_certificate, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_ins));
            RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.rl_pop);
            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.CENTER, 0, 0);
            update();

            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        System.out.println("调用点击返回键的方法");
        if (keyCode == KeyEvent.KEYCODE_BACK) {
             //发消息给我的fragment
            goSelect(EventMessage.EventMessageAction.TAG_REFRESH_CARD);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    private void goSelect(int tag) {
        EventMessage eventMessage = new EventMessage();
        eventMessage.setTag(tag);
        EventBus.getDefault().post(eventMessage);

    }



}
