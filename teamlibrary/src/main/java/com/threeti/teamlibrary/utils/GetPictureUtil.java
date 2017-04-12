/**
 * @Title: GetPictureUtil.java
 * @Package com.c1618.moliandr.util
 * @author BaoHang baohang2011@gmail.com
 * @date 2014年6月17日 下午8:09:38 
 * @version V1.0
 */
package com.threeti.teamlibrary.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.Toast;


import com.threeti.teamlibrary.finals.ProjectConfig;

import java.io.File;

/**
 * @author BaoHang baohang2011@gmail.com
 * @ClassName: GetPictureUtil
 * @date 2014年6月17日 下午8:09:38
 */
@SuppressLint("ShowToast")
public class GetPictureUtil {
    /**
     * 拍照
     */
    public static final int DATA_WITH_CAMERA = 0x7;//拍照
    /**
     * 从相册中选择照片不裁剪
     */
    public static final int DATA_WITH_PHOTO_PICKED = 0x8;//从相册中选择
    /**
     * 从相册中选择照片裁剪
     */
    public static final int DATA_WITH_PHOTO_PICKED_CROP = 0x9;//从相册中选择

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
     * @param imageuri
     */
    public static void selectFromAlbum(Activity context, Uri imageuri) {
        Intent intent = getIntent(DATA_WITH_PHOTO_PICKED_CROP, imageuri);
        if (intent != null) {
            try {
                context.startActivityForResult(intent, DATA_WITH_PHOTO_PICKED_CROP);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "无法获取图片", Toast.LENGTH_SHORT);
            }
        } else {
            Toast.makeText(context, "无法获取图片", Toast.LENGTH_SHORT);
        }
    }

    public static void selectFromAlbum(Activity context, Uri imageuri, int xWeight, int yWeight, int width, int height) {
        Intent intent = getIntent(DATA_WITH_PHOTO_PICKED_CROP, imageuri);
        if (intent != null) {
            try {
                context.startActivityForResult(intent, DATA_WITH_PHOTO_PICKED_CROP);
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
        } else if (type == DATA_WITH_PHOTO_PICKED) { //从相册中获取
            return getIntent(imageuri, 0, 0, 0, 0);
        } else if (type == DATA_WITH_PHOTO_PICKED_CROP) {//从相册中获取裁剪图片
            return getIntent(imageuri, 0,0, 200, 200);
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
            intent.putExtra("aspectX", xWeight);
            intent.putExtra("aspectY", yWeight);
        if (width > 0 && height > 0) {
            intent.putExtra("outputX", width);
            intent.putExtra("outputY", height);
        }
        intent.putExtra("return-data", false);
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
     * @Description TODO
     */
    public static Intent cropImage(Context context, Uri imageuri, Uri cropImage) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(imageuri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 0);
            intent.putExtra("aspectY", 0);
            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
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
}

