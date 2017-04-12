package com.threeti.teamlibrary.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.threeti.teamlibrary.activity.BaseActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * viewpager适配器
 * Created by WangXY on 2015/6/18.
 */
public abstract class BaseVPAdapter extends PagerAdapter {
    protected List mList;
    protected LayoutInflater mInflater;// 布局管理
    protected DisplayImageOptions options;
    protected ImageLoader imageLoader;
    protected BaseActivity mContext;
    protected Map<Integer, View> map;
    protected static final int NO_DEFAULT = -1;// 有图片但是没有默认图
    protected static final int NO_IMAGE = 0;// 没有图片

    protected BaseVPAdapter(List list, BaseActivity ctx) {
        this(list, ctx, NO_IMAGE);
    }

    /**
     * 构造器
     *
     * @param context
     * @param list      起始数据
     * @param defaultId NO_IMAGE为没有图片要显示，NO_DEFAULT为需要显示但没有默认图片，R.drawable.XXX为默认图id
     */
    protected BaseVPAdapter(List list, BaseActivity context, int defaultId) {
        map = new HashMap<Integer, View>();
        mContext = context;
        mList = list;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (defaultId == NO_IMAGE) {// 没有图片
        } else if (defaultId == NO_DEFAULT) {// 有图片但是没有默认图
            options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .resetViewBeforeLoading(true).cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
        } else {// 有图片有默认图
            options = new DisplayImageOptions.Builder()
                    .resetViewBeforeLoading(true).showImageOnLoading(defaultId)
                    .showImageForEmptyUri(defaultId).showImageOnFail(defaultId)
                    .cacheInMemory(true).cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
        }
        imageLoader = ImageLoader.getInstance();
    }

    public abstract Object instantiateItem(final ViewGroup container,
                                           final int position);

    @Override
    public int getItemPosition(Object object) {
        // return super.getItemPosition(object);
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(map.get(position));
        map.remove(position);
    }

    /**
     * loadWebImage:(这里用一句话描述这个方法的作用). <br/>
     *
     * @param imageview
     * @param url
     * @author BaoHang
     */
    public void loadWebImage(ImageView imageview, String url) {
        imageLoader.displayImage(url, imageview, options);
    }

    public void loadWebImage(ImageView imageview, String url, ImageLoadingListener listener) {
        imageLoader.displayImage(url, imageview, options, listener);
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
}
