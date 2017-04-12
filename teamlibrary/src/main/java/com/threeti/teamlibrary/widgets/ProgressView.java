/**
 * Project Name:ThreeTiProject
 * Package Name:com.threeti.threetiproject.utils.widget
 * File Name:ProgressView.java
 * Function: TODO 〈一句话功能简述〉. <br/>
 * Description: TODO 〈功能详细描述〉. <br/>
 * Date:2014年10月10日下午2:33:16
 * Copyright:Copyright (c) 2014, 翔傲科技（上海）有限公司 All Rights Reserved.
 */
package com.threeti.teamlibrary.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * ClassName:ProgressView
 * Function:加载进度条. <br/>
 * Date:2014年10月10日
 *
 * @author BaoHang
 * @since [产品/模块版本] （可选）
 */
public class ProgressView extends LinearLayout {
    private TextView titleView;
    private ProgressBar progressBar;

    /**
     * Creates a new instance of ProgressView.
     *
     * @param context
     */
    public ProgressView(Context context) {
        this(context, null);
    }

    /**
     * Creates a new instance of ProgressView.
     *
     * @param context
     * @param attrs
     */
    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * init:初始化方法. <br/>
     *
     * @param context
     * @author BaoHang
     */
    public void init(Context context) {
        setMinimumHeight(50);
        setGravity(Gravity.CENTER);
        setOrientation(LinearLayout.HORIZONTAL);
        progressBar = new ProgressBar(context, null,
                android.R.attr.progressBarStyleSmall);
        progressBar.setPadding(0, 0, 15, 0);
        addView(progressBar, new LayoutParams(-2, -2));

        titleView = new TextView(context);
        titleView.setText("加载中...");
        addView(titleView, new LayoutParams(-2, -2));
    }

    /**
     * setText:设置显示文字. <br/>
     *
     * @param str
     * @author BaoHang
     */
    public void setText(String str) {
        titleView.setText(str);
    }

    /**
     * setText:设置显示文字. <br/>
     *
     * @param strResId
     * @author BaoHang
     */
    public void setText(int strResId) {
        titleView.setText(strResId);
    }

}
