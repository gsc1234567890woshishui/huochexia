package com.zjwocai.qundui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.threeti.teamlibrary.finals.ProjectConfig;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.model.ADModel;

import java.util.HashMap;
import java.util.List;

/**
 * 资讯下图片轮播器适配器
 * Created by NieLiQin on 2016/5/31.
 */
public class BannerAdapter extends PagerAdapter {
    protected List<ADModel> mList;
    protected LayoutInflater mInflater;
    protected DisplayImageOptions options;
    protected OnCustomListener mListener;
    protected Context mContext;
    protected HashMap<Integer, View> map;

    protected static final int NO_DEFAULT = -1;// 有图片但是没有默认图
    protected static final int NO_IMAGE = 0;// 没有图片

    @SuppressWarnings("deprecation")
    public BannerAdapter(Context ctx, List<ADModel> list) {
        mContext = ctx;
        mList = list;
        mInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        map = new HashMap<>();

        options = new DisplayImageOptions.Builder().cacheInMemory(true).considerExifParams(true)
                .resetViewBeforeLoading(true).cacheOnDisc(false)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageForEmptyUri(R.drawable.ic_default_pic)
                .showImageOnFail(R.drawable.ic_default_pic)
                .showImageOnLoading(R.drawable.ic_default_pic)
                .build();
    }

    public int getCount() {
        if (mList == null || mList.size() == 0) {
            return 0;
        } else if (mList.size() == 1) {// 1张图片不用滚动
            return 1;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    public Object instantiateItem(ViewGroup container, final int position) {
        if (map.get(position) != null)
            return map.get(position);

        final ImageView iv = new ImageView(mContext);
        iv.setLayoutParams(new LinearLayout.LayoutParams(
                ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT));
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onCustomerListener(iv, position % mList.size());
                }
            }
        });
        ImageLoader.getInstance().displayImage(ProjectConfig.ONLINE_IMAGE_URL + mList.get(position % mList.size()).getImage(), iv, options);

        container.addView(iv);
        map.put(position, iv);
        return iv;
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

    public void setOnCustomListener(OnCustomListener listener) {
        this.mListener = listener;
    }
}
