package com.zjwocai.qundui.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.Toast;

import com.threeti.teamlibrary.activity.BaseActivity;
import com.threeti.teamlibrary.finals.ProjectConfig;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by WangXY on 2015/11/10.14:36.
 * 图片工具类 包括网络以及本地图片加载  图片裁剪  图片保存等图片相关功能
 */
public class ImageUtil {
    /**
     * 拍照
     */
    public static final int DATA_WITH_CAMERA = 0x7;//拍照
    /**
     * 拍照并裁剪
     */
    public static final int DATA_WITH_CAMERA_CROP = 0x10;//拍照并裁剪
    /**
     * 从相册中选择照片不裁剪
     */
    public static final int DATA_WITH_PHOTO_PICKED = 0x8;//从相册中选择
    /**
     * 从相册中选择照片裁剪
     */
    public static final int DATA_WITH_PHOTO_PICKED_CROP = 0x9;//从相册中选择并裁剪


    /**
     * 拍照
     *
     * @param context
     * @param filename
     */
    public static void takePhoto(Activity context, String filename) {
        Uri imageuri = Uri.parse(StringUtil.getFileUrlHead(2) + ProjectConfig.DIR_IMG + File.separator + filename);
        Intent intent = getIntent(DATA_WITH_CAMERA, imageuri);
        if (intent != null) {
            try {
                context.startActivityForResult(intent, DATA_WITH_CAMERA);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "拍照功能不可用", Toast.LENGTH_SHORT);
            }
        } else {
            Toast.makeText(context, "拍照功能不可用", Toast.LENGTH_SHORT);
        }
    }

    /**
     * 从相册中选择，不裁剪
     *
     * @param context
     * @param filename
     */
    public static void selectFromAlbumFull(Activity context, String filename) {
        Uri imageuri = Uri.parse(StringUtil.getFileUrlHead(2) + ProjectConfig.DIR_IMG + File.separator + filename);
        Intent intent = getIntent(DATA_WITH_PHOTO_PICKED, imageuri);
        if (intent != null) {
            try {
                context.startActivityForResult(intent, DATA_WITH_PHOTO_PICKED);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "无法获取图片", Toast.LENGTH_SHORT);
            }
        } else {
            Toast.makeText(context, "无法获取图片", Toast.LENGTH_SHORT);
        }
    }

    /**
     * 从相册中选择图片，需要裁剪
     *
     * @param context
     */
    public static void selectFromAlbum(Activity context) {
        Intent intent = getIntent(DATA_WITH_PHOTO_PICKED);
        if (intent != null) {
            try {
                context.startActivityForResult(intent, DATA_WITH_PHOTO_PICKED);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "无法获取图片", Toast.LENGTH_SHORT);
            }
        } else {
            Toast.makeText(context, "无法获取图片", Toast.LENGTH_SHORT);
        }
    }


    /**
     * 获得跳转的Intent
     *
     * @param type
     * @param imageuri
     * @return
     */
    private static Intent getIntent(int type, Uri imageuri) {
        if (type == DATA_WITH_CAMERA) {//拍照
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
            return intent;
        }
        return null;
    }


    private static Intent getIntent(int type) {
        if (type == DATA_WITH_PHOTO_PICKED) {//从相册选取
            Intent intent = null;
            if (Build.VERSION.SDK_INT < 19) {
                intent = new Intent(Intent.ACTION_GET_CONTENT, null);
            } else {
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            }
            intent.setType("image/*");
            return intent;
        }
        return null;
    }


    /**
     * @param imageuri
     * @param xWeight
     * @param yWeight
     * @param width
     * @param height
     * @return
     */
    private static Intent getIntent(Uri imageuri, int xWeight, int yWeight, int width, int height) {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        if (xWeight > 0 && xWeight > 0) {
            intent.putExtra("aspectX", xWeight);
            intent.putExtra("aspectY", xWeight);
        }
        if (width > 0 && height > 0) {
            intent.putExtra("outputX", width);
            intent.putExtra("outputY", height);
        }
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
        return intent;
    }

    /**
     * 裁剪图片
     *
     * @param context
     * @return
     * @Title cropImage
     * i=0是拍照，i=1是相册
     */

    public static Intent cropImage(Context context, Uri uri, Uri cropImage) {
        try {
            // 裁剪图片意图
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 500);
            intent.putExtra("outputY", 500);
            intent.putExtra("scale", true);
            intent.putExtra("scaleUpIfNeeded", true);
            intent.putExtra("return-data", false);
            intent.putExtra("outputFormat",
                    Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImage);
            return intent;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean isPhotoCallBack(int requestCode) {
        if (requestCode == DATA_WITH_CAMERA || requestCode == DATA_WITH_PHOTO_PICKED ||
                requestCode == DATA_WITH_PHOTO_PICKED_CROP) {
            return true;
        }
        return false;
    }

    public static String save(String filename, Bitmap bitmap) {
        File file = null;
        try {
            File filexists = new File(filename);
            filexists.createNewFile();
            FileOutputStream fos = new FileOutputStream(filexists);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return filename;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String filename = "temp.jpg";
    private static Uri imageUri = Uri.parse("file://" + ProjectConfig.DIR_IMG
            + File.separator + filename);

    /**
     * 获取文件名称
     *
     * @return
     */
    public static String getFileName() {
        return filename;
    }

    /**
     * 因为只能在Activity中使用，故开启新方法替代
     *
     * @param context
     * @param requestCode
     * @param resultCode
     * @param data
     * @param listener
     */
    public static void onActivityResult(Context context, int requestCode, int resultCode,
                                        Intent data, SelectImageListener listener) {
        if (requestCode == DATA_WITH_CAMERA) {
            filename = System.currentTimeMillis() + ".jpg";
            Uri imageUri1 = Uri.parse("file://" + ProjectConfig.DIR_IMG
                    + File.separator + filename);
            ((BaseActivity) context).startActivityForResult(cropImage(context, imageUri, imageUri1),
                    DATA_WITH_CAMERA_CROP);
        } else if (requestCode == DATA_WITH_CAMERA_CROP) {
            // 将拍照获取的原图删除
            File file = new File(ProjectConfig.DIR_IMG + File.separator
                    + "temp.jpg");
            filename = ProjectConfig.DIR_IMG + File.separator + filename;
            if (file.exists() && file.isFile()) {
                file.delete();
            }
            listener.selectPic();
        } else if (requestCode == DATA_WITH_PHOTO_PICKED) {
            Uri imageUri = data.getData();
            filename = System.currentTimeMillis() + ".jpg";
            Uri imageUri1 = Uri.parse("file://" + ProjectConfig.DIR_IMG
                    + File.separator + filename);
            ((BaseActivity) context).startActivityForResult(cropImage(context, imageUri, imageUri1),
                    DATA_WITH_PHOTO_PICKED_CROP);
        } else if (requestCode == DATA_WITH_PHOTO_PICKED_CROP) {
            filename = ProjectConfig.DIR_IMG + File.separator + filename;
            listener.selectPic();
        }
    }

    /**
     * 因为只能在Activity中使用，故开启新方法替代
     *
     * @param context
     * @param requestCode
     * @param data
     * @param listener
     */
    public static void onActivityResult(Context context, int requestCode, Intent data, SelectImageListener listener) {
        if (requestCode == DATA_WITH_CAMERA) {
            filename = System.currentTimeMillis() + ".jpg";
            Uri imageUri1 = Uri.parse("file://" + ProjectConfig.DIR_IMG
                    + File.separator + filename);
            ((BaseActivity) context).startActivityForResult(cropImage(context, imageUri, imageUri1),
                    DATA_WITH_CAMERA_CROP);
        } else if (requestCode == DATA_WITH_CAMERA_CROP) {
            // 将拍照获取的原图删除
            File file = new File(ProjectConfig.DIR_IMG + File.separator
                    + "temp.jpg");
            filename = ProjectConfig.DIR_IMG + File.separator + filename;
            if (file.exists() && file.isFile()) {
                file.delete();
            }
            listener.selectPic();
        } else if (requestCode == DATA_WITH_PHOTO_PICKED) {
            Uri imageUri = data.getData();
            filename = System.currentTimeMillis() + ".jpg";
            Uri imageUri1 = Uri.parse("file://" + ProjectConfig.DIR_IMG
                    + File.separator + filename);
            ((BaseActivity) context).startActivityForResult(cropImage(context, imageUri, imageUri1),
                    DATA_WITH_PHOTO_PICKED_CROP);
        } else if (requestCode == DATA_WITH_PHOTO_PICKED_CROP) {
            filename = ProjectConfig.DIR_IMG + File.separator + filename;
            listener.selectPic();
        }
    }

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 200) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

}
