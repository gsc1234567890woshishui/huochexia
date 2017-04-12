package com.zjwocai.qundui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.threeti.teamlibrary.activity.BaseActivity;
import com.threeti.teamlibrary.finals.ProjectConfig;
import com.zjwocai.qundui.R;

import java.util.List;

/**
 * Created by WangXY on 2015/6/9.
 */
public abstract class BaseListAdapter extends BaseAdapter implements ProjectConfig {
    protected List mList;// 列表List
    protected LayoutInflater mInflater;// 布局管理
    protected DisplayImageOptions options;
    protected ImageLoader imageLoader;
    protected static final int NO_DEFAULT = -1;// 有图片但是没有默认图
    protected static final int NO_IMAGE = 0;// 没有图片
    public Context context;
    protected OnCustomListener listener;

    /**
     * 没有指定默认图的够造方法
     *
     * @param context
     * @param list
     */
    public BaseListAdapter(BaseActivity context, List list) {
        this(context, list, NO_DEFAULT, 0);
    }

    /**
     * @param context
     * @param list
     * @param defaultId 传0则表示适配器中没有图片需要显示,-1表示需要显示但没有默认图片
     */
    protected BaseListAdapter(Context context, List list, int defaultId, int radius) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        mList = list;
        if (defaultId == NO_IMAGE) {// 没有图片
            return;
        } else if (defaultId == NO_DEFAULT) {// 有图片但是没有默认图
            options = new DisplayImageOptions.Builder()
                    .showStubImage(R.drawable.ic_default_pic)
                    .showImageForEmptyUri(R.drawable.ic_default_pic)
                    .showImageOnFail(R.drawable.ic_default_pic)
                    .cacheInMemory(true).cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(radius))
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
        } else {// 有图片有默认图
            options = new DisplayImageOptions.Builder()
                    .showStubImage(defaultId).showImageForEmptyUri(defaultId)
                    .showImageOnFail(defaultId).cacheInMemory(true).displayer(new RoundedBitmapDisplayer(radius))
                    .cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public int getCount() {
        return (mList == null) ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return (mList == null) ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * loadWebImage:(这里用一句话描述这个方法的作用). <br/>
     *
     * @param imageview
     * @param url
     * @author BaoHang
     */
    public void loadWebImage(ImageView imageview, String url) {
        imageLoader.displayImage(IMAGE_URL + url, imageview, options);
    }

    public void loadHeadImage(ImageView imageview, String url) {
        DisplayImageOptions options1 = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_default_head).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.ic_default_head).showImageOnFail(R.drawable.ic_default_head).cacheInMemory(true).cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(context.getResources().getDimensionPixelOffset(R.dimen.dim10))).bitmapConfig(Bitmap.Config.RGB_565).build();
        imageLoader.displayImage(url, imageview, options1);
    }
    /**
     * loadWebImage:(这里用一句话描述这个方法的作用). <br/>
     *
     * @param imageview
     * @param filepath
     * @author BaoHang
     */
    public void loadLocImage(ImageView imageview, String filepath) {
        imageLoader.displayImage("file://" + filepath, imageview, options);
    }

    public void setOnCustomListener(OnCustomListener listener) {
        this.listener = listener;
    }

}
