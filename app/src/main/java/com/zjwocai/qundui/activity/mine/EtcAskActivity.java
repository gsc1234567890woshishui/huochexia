package com.zjwocai.qundui.activity.mine;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.threeti.teamlibrary.finals.ProjectConfig;
import com.threeti.teamlibrary.net.BaseModel;
import com.threeti.teamlibrary.net.ProcotolCallBack;
import com.threeti.teamlibrary.utils.SPUtil;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.activity.BaseProjectActivity;
import com.zjwocai.qundui.activity.login.LoginActivity;
import com.zjwocai.qundui.bill.ProtocolBill;
import com.zjwocai.qundui.fragment.EventMessage;
import com.zjwocai.qundui.fragment.FirstEvent;
import com.zjwocai.qundui.model.CarModel;
import com.zjwocai.qundui.model.CertModel;
import com.zjwocai.qundui.model.UploadModel;
import com.zjwocai.qundui.util.BitmapUtil;
import com.zjwocai.qundui.util.CallDialogUtil;
import com.zjwocai.qundui.util.DialogUtil;
import com.zjwocai.qundui.util.ImageUtil;
import com.zjwocai.qundui.util.ToastUtil;
import com.zjwocai.qundui.widgets.SpinerPopWindow;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.PopupWindow.OnDismissListener;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.View.OnClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Created by qundui on 2017/3/13.
 */

public class EtcAskActivity extends BaseProjectActivity implements View.OnClickListener {
    private ImageView ivFront, ivBack, ivDriving, ivDriver, ivDrivingHead,ivFrontEdit, ivBackEdit, ivDrivingEdit, ivDriverEdit,ivDriverCarEdit;
    private RelativeLayout llCertification;
    private Dialog dialog;

    private String mCurrentPhotoPath,carIntentNumber,tagFail,carId;
    private List<CarModel> models;
    private  List<CarModel> carModels;
    private List<String> listEtcState;
    private List<String> listCarNumber,listCarNumber2;

    private static final int REQUEST_IMAGE_GET = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private int type = 1;
    private ProgressDialog netDialog;
    private CertModel certModel;
    private DisplayImageOptions op1;
    private DisplayImageOptions op2;
    private String filePath,carNumberFail;
    private String path;
    private Runnable runnable;
    private Handler handler;
    private LinearLayout llPay;
    private String s1="";
    private String s2="";
    private String s3="";
    private String s4="";
    private String s5="";

    private SpinerPopWindow<String> mSpinerPopWindow;
    private List<String> list;
    private TextView tvValue;
    private  Map<String,String> map,map2;
    private int i=0;

    public EtcAskActivity() {
        super(R.layout.act_etc_certification);

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_front:
                type = 1;
                showPhotoDialog();
                break;
            case R.id.iv_back:
                type = 2;
                showPhotoDialog();
                break;
            case R.id.iv_driving_head:
                type = 3;
                showPhotoDialog();
                break;
            case R.id.iv_driving_license:
                type = 4;
                showPhotoDialog();
                break;
            case R.id.iv_driver_license:
                type = 5;
                showPhotoDialog();
                break;
            case R.id.ll_pay:
                //判断是否图片传完整
                Log.v("ssssssssss",s1);
              if(s1.equals("") || s2.equals("") || s3.equals("") || s4.equals("") || s5.equals("")){ //图片完整
                  ToastUtil.toastShortShow("请完整上传所有的图片！");
              }else{
                  ProtocolBill.getInstance().audit(new ProcotolCallBack() {
                                                       @Override
                                                       public void onTaskSuccess(BaseModel result) {
                                                           ToastUtil.toastShortShow("上传成功");
                                                           //跳进审核中界面
                                                           startActivity(new Intent(EtcAskActivity.this,EtcAskingActivity.class));
                                                           finish();

                                                       }

                                                       @Override
                                                       public void onTaskFail(BaseModel result) {

                                                           ToastUtil.toastShortShow("上传失败");
                                                       }

                                                       @Override
                                                       public void onTaskFinished(String resuestCode) {


                                                       }
                                                   }, this, certModel.getSfz1(), certModel.getSfz2(), certModel.getCarHeadimg(),
                          certModel.getJsz(), certModel.getXsz(), map.get(tvValue.getText()));

              }
                break;
        }
    }


    @Override
    public void findIds() {
        //EventBus.getDefault().register(this);//注册事件
        netDialog = DialogUtil.getProgressDialog(this, getResources().getString(R.string.ui_net_request));
        map = new HashMap<>();
        llCertification = (RelativeLayout) findViewById(R.id.ll_certification);
        ivFront = (ImageView) findViewById(R.id.iv_front);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivDriving = (ImageView) findViewById(R.id.iv_driving_license);
        ivDriver = (ImageView) findViewById(R.id.iv_driver_license);
        ivDrivingHead = (ImageView) findViewById(R.id.iv_driving_head);
        ivFrontEdit = (ImageView) findViewById(R.id.iv_front_edit);
        ivBackEdit = (ImageView) findViewById(R.id.iv_back_edit);
        ivDrivingEdit = (ImageView) findViewById(R.id.iv_driving_license_edit);
        ivDriverEdit = (ImageView) findViewById(R.id.iv_driver_license_edit);
        ivDriverCarEdit = (ImageView) findViewById(R.id.iv_driving_car_edit);
        llPay = (LinearLayout) findViewById(R.id.ll_pay);
        tvValue = (TextView) findViewById(R.id.tv_value);


        Bundle bundle = this.getIntent().getExtras();

        if(bundle.getString("one")!=null&&!bundle.getString("one").equals("")){//如果是从办卡界面跳转过来的
            SPUtil.saveString("failcar","");
             //设置单独的车牌号给textview
            tvValue.setText(bundle.getString("one"));
            map.put(bundle.getString("one"),bundle.getString("two"));
            tvValue.setClickable(false);

        }else if(!SPUtil.getString("failcar").equals("")){ //如果是需要重新上传图片的
            Log.v("jjjjjjjjj",SPUtil.getString("failcar"));
            tvValue.setText(SPUtil.getString("failcar"));

            tvValue.setClickable(false);

        }else {//否则联网请求
            ProtocolBill.getInstance().getCarList(this,this,"1");
            netDialog.show();
        }
        if(!(bundle.getString("one")!=null&&!bundle.getString("one").equals("")) && SPUtil.getString("failcar").equals("") ){//如果不是是从办卡界面跳转过来的就设置能点击
            tvValue.setOnClickListener(clickListener);
        }


        certModel = new CertModel();
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

        initTitle("ETC申办");
        title.setLeftIcon(R.drawable.ic_title_left, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EtcAskActivity.this.finish();
            }
        });
        title.setRightText("客服", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceNumber = "0571-87620191";
                new CallDialogUtil().showCallDialog(serviceNumber,EtcAskActivity.this);
            }
        });
        ivFront.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivDriving.setOnClickListener(this);
        ivDriver.setOnClickListener(this);
        ivDrivingHead.setOnClickListener(this);
        llPay.setOnClickListener(this);



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
                    ProtocolBill.getInstance().upload(EtcAskActivity.this, EtcAskActivity.this, path);
                }
            }
        };
    }
    /**
     * 监听popupwindow取消
     */
    private OnDismissListener  dismissListener=new OnDismissListener() {
        @Override
        public void onDismiss() {
            setTextImage(R.drawable.icon_pull_down);
        }
    };
    /**
     * popupwindow显示的ListView的item点击事件
     */
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            mSpinerPopWindow.dismiss();
            String value = tvValue.getText().toString();
            tvValue.setText(listCarNumber.get(position));
            //Toast.makeText(EtcAskActivity.this, "点击了:" + listCarNumber.get(position),Toast.LENGTH_LONG).show();

            listCarNumber.set(position,value);
            mSpinerPopWindow.refresh();
        }
    };
    /**
     * 显示PopupWindow
     */
    private OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_value:
                    mSpinerPopWindow.setWidth(tvValue.getWidth());
                    mSpinerPopWindow.showAsDropDown(tvValue);
                    setTextImage(R.drawable.icon_pull_down);
                    break;
            }
        }
    };
    /**
     * 给TextView右边设置图片
     * @param resId
     */
    private void setTextImage(int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());// 必须设置图片大小，否则不显示
        tvValue.setCompoundDrawables(null, null, drawable, null);
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
                if (certModel.getCarHeadimg() != null && !certModel.getCarHeadimg().equals("")) {
                    ivDrivingHead.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    imageLoader.displayImage(ProjectConfig.IMAGE_URL + certModel.getCarHeadimg(), ivDrivingHead, op2);
                    ivDriverCarEdit.setVisibility(View.VISIBLE);
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

        } else

        if (RQ_UPLOAD.equals(result.getRequest_code())) {
            List<UploadModel> models = (List<UploadModel>) result.getResult();

            if (models != null && !models.isEmpty()) {
                switch (type) {
                    case 1:
                        imageLoader.displayImage(ProjectConfig.IMAGE_URL + models.get(0).getFileurl3(), ivFront, op1);
                        ProtocolBill.getInstance().uploadCertification2(this, this, models.get(0).getFileurl3(),
                                certModel.getSfz2(),certModel.getCarHeadimg(), certModel.getJsz(), certModel.getXsz());
                        s1 = "1";

                        certModel.setSfz1(models.get(0).getFileurl3());
                        ivFrontEdit.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        imageLoader.displayImage(ProjectConfig.IMAGE_URL + models.get(0).getFileurl3(), ivBack, op1);
                        ProtocolBill.getInstance().uploadCertification2(this, this, certModel.getSfz1(),
                                models.get(0).getFileurl3(),certModel.getCarHeadimg(), certModel.getJsz(), certModel.getXsz());
                        s2 = "2";

                        certModel.setSfz2(models.get(0).getFileurl3());
                        ivBackEdit.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        ivDrivingHead.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

                        imageLoader.displayImage(ProjectConfig.IMAGE_URL + models.get(0).getFileurl3(), ivDrivingHead, op2);
                        ProtocolBill.getInstance().uploadCertification2(this, this, certModel.getSfz1(),
                                certModel.getSfz2(), models.get(0).getFileurl3(),certModel.getJsz(), certModel.getXsz());
                        s3 = "3";

                        certModel.setCarHeadimg(models.get(0).getFileurl3());
                        ivDriverCarEdit.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        ivDriving.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

                        imageLoader.displayImage(ProjectConfig.IMAGE_URL + models.get(0).getFileurl3(), ivDriving, op2);
                        ProtocolBill.getInstance().uploadCertification2(this, this, certModel.getSfz1(),
                                certModel.getSfz2(), certModel.getCarHeadimg(),models.get(0).getFileurl3(), certModel.getJsz());
                        s4 = "4";

                        certModel.setXsz(models.get(0).getFileurl3());
                        ivDrivingEdit.setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        ivDriver.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        imageLoader.displayImage(ProjectConfig.IMAGE_URL + models.get(0).getFileurl3(), ivDriver, op2);
                        ProtocolBill.getInstance().uploadCertification2(this, this, certModel.getSfz1(),
                                certModel.getSfz2(),  certModel.getCarHeadimg(),certModel.getXsz(),models.get(0).getFileurl3());
                        s5 = "5";

                        certModel.setJsz(models.get(0).getFileurl3());
                        ivDriverEdit.setVisibility(View.VISIBLE);
                        break;
                }
            }
        } else if (RQ_UPLOAD_CERTIFICATION.equals(result.getRequest_code())) {
            netDialog.dismiss();
        }else if(RQ_GET_ALL_CAR_LIST.equals(result.getRequest_code())) {
            //判断有没有车辆
            models = (List<CarModel>) result.getResult();
            carModels = new ArrayList();

            listEtcState = new ArrayList<>();
            listCarNumber = new ArrayList<>();

            if (models != null && !models.isEmpty()) { //有车得到车牌号
                for (int i = 0; i <models.size() ; i++) {
                    String carNumber = models.get(i).getCarnumber();
                    String id = models.get(i).getId();
                    String etcState = models.get(i).getAudit_state();
//                    if(models.get(i).getAudit_state().contains("-")){ //如果审核失败
//                        tvValue.setText(models.get(i).getCarnumber());
//                        tvValue.setClickable(false);
//                        return;
//                    }
                    map.put(carNumber,id);  //绑定车牌号和车id
                    listEtcState.add(etcState);
                    listCarNumber.add(carNumber);
                }
                tvValue.setText(models.get(0).getCarnumber());
                listCarNumber.remove(0);

                mSpinerPopWindow = new SpinerPopWindow<String>(this, listCarNumber,itemClickListener);
                mSpinerPopWindow.setOnDismissListener(dismissListener);
            }


        }
    }

    @Override
    public void onTaskFail(@SuppressWarnings("rawtypes") BaseModel result) {
//        super.onTaskFail(result);
//        if (result.getMsgtype() != null && result.getMsgtype().equals("2")) {
//            showToast("登录失效，请重新登录");
//            saveUser(null);
//            startActivity(LoginActivity.class);
//        } else if (!TextUtils.isEmpty(result.getMsg())) {
//            showToast(result.getMsg() + "");
//        }
    }

    @Override
    public void onTaskFinished(String resuestCode) {
        super.onTaskFinished(resuestCode);
        netDialog.dismiss();
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
        dialog = DialogUtil.getDialog(EtcAskActivity.this, view, Gravity.BOTTOM, true);
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

}
