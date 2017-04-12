package com.zjwocai.qundui.widgets;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zjwocai.qundui.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 循环滚动ViewPager
 *
 * @author lanyan
 *
 */
public class BannerPager extends ViewPager {
    private long mScrollTime;// 滚动间隔时间
    private Timer mTimer;
    private boolean canScroll;// 是否能自动滚动

    public BannerPager(Context context) {
        super(context);
        initParams();
    }

    public BannerPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initParams();
    }

    /**
     * 初始化成员变量参数
     */
    private void initParams() {
        mScrollTime = 5000;
        canScroll = true;
    }

    /**
     * 添加指示标布局
     *
     * @param act
     * @param parent
     *            指示标所在布局
     * @param size
     *            指示标个数
     * @param drawId
     *            图标资源图片，必须用Selector，进行图片的切换
     */
    public void setOvalLayout(final Activity act, final ViewGroup parent,
                              final int size, int drawId) {
        if (act != null){
            if (parent == null || size == 0) {
                return;
            }

            // 设置图标布局
            int padwith = (int) getResources().getDimension(R.dimen.dim20);
            int padhight = (int) getResources().getDimension(R.dimen.dim20);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(padwith, padhight);
            // 初始化所有指示标图片
            for (int i = 0; i < size; i++) {
                ImageView iv = new ImageView(act);
                int padding = (int) getResources().getDimension(R.dimen.dim5);
                iv.setPadding(padding, padding, padding, padding);
                iv.setLayoutParams(lp);
                iv.setImageResource(drawId);
                iv.setSelected(false);
                parent.addView(iv);
            }

            // 选中第一个图标
            parent.getChildAt(0).setSelected(true);

            // 设置切换
            setOnPageChangeListener(new OnPageChangeListener() {
                @Override
                public void onPageSelected(int arg0) {
                    for (int i = 0; i < size; i++) {
                        parent.getChildAt(i).setSelected(false);
                    }
                    parent.getChildAt(arg0 % size).setSelected(true);
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                }

                @Override
                public void onPageScrollStateChanged(int arg0) {
                }
            });

            setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View arg0, MotionEvent arg1) {
                    if (arg1.getAction() == MotionEvent.ACTION_UP) {
                        startScroll(act);
                    } else {
                        stopScroll();
                    }
                    return false;
                }
            });
        }
    }

    /**
     * 开始滚动
     *
     * @param act
     *            BannerPager所在的页面
     */
    public void startScroll(final Activity act) {
        if (!isCanScroll()) {// 能否滚动
            return;
        }
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            public void run() {
                act.runOnUiThread(new Runnable() {
                    public void run() {
                        setCurrentItem(getCurrentItem() + 1);
                    }
                });
            }
        }, mScrollTime, mScrollTime);
    }

    /**
     * 停止滚动
     */
    public void stopScroll() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    /**
     * 是否能自动滚动
     *
     * @return
     */
    public boolean isCanScroll() {
        return canScroll;
    }

    /**
     * 设置是否自动滚动
     *
     * @param canScroll
     *            是否自动滚动
     */
    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }

    /**
     * 获取自动滚动间隔时间
     *
     * @return
     */
    public long getmScrollTime() {
        return mScrollTime;
    }

    /**
     * 设置自动滚动间隔时间
     *
     * @param mScrollTime
     *            自动滚动间隔时间
     */
    public void setmScrollTime(long mScrollTime) {
        this.mScrollTime = mScrollTime;
    }

}