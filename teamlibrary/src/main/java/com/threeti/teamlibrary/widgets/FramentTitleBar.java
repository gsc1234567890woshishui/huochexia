/**
 * Project Name:TX
 * Package Name:com.threeti.tx.utils.widget
 * File Name:FramentTitleBar.java
 * Function: TODO 〈一句话功能简述〉. <br/>
 * Description: TODO 〈功能详细描述〉. <br/>
 * Date:2014年10月11日下午2:28:36
 * Copyright:Copyright (c) 2014, 翔傲科技（上海）有限公司 All Rights Reserved.
 */
package com.threeti.teamlibrary.widgets;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.threeti.teamlibrary.R;
import com.threeti.teamlibrary.activity.BaseActivity;

/**
 * ClassName:FramentTitleBar
 * Function:TODO ADD FUNCTION.〈一句话功能简述〉. <br/>
 * Description: TODO 〈功能详细描述，包括界面的上层以及下层的逻辑关系〉. <br/>
 * Date:2014年10月11日
 *
 * @author BaoHang
 * @since [产品/模块版本] （可选）
 */
public class FramentTitleBar {
    private BaseActivity activity;
    private ImageView im_left;
    private ImageView im_right;
    private ImageView im_logo;
    private TextView tv_title;
    private TextView tv_right;
    private RelativeLayout rl_title;

    public FramentTitleBar(Activity activity) {
        this.activity = (BaseActivity) activity;
        initViews();
        initSize();
        initEvents();
    }

    private void initViews() {
        im_left = (ImageView) activity.findViewById(R.id.im_left);
        im_right = (ImageView) activity.findViewById(R.id.im_right);
        tv_title = (TextView) activity.findViewById(R.id.tv_title);
        im_logo = (ImageView) activity.findViewById(R.id.im_logo);
        rl_title = (RelativeLayout) activity.findViewById(R.id.rl_title);
        tv_right = (TextView) activity.findViewById(R.id.tv_right);
    }

    public void initSize() {

    }

    public void setLeftIcon(int resid, OnClickListener listener) {
        im_left.setVisibility(View.VISIBLE);
        im_left.setImageResource(resid);
        if (listener != null)
            im_left.setOnClickListener(listener);
    }

    public void setRightIcon(int resid, OnClickListener listener) {
        im_right.setVisibility(View.VISIBLE);
        im_right.setImageResource(resid);
        im_right.setOnClickListener(listener);
    }

    public void setBackground(int resid) {
        rl_title.setBackgroundResource(resid);
    }

    public void setBackgroundColor(int resid) {
        rl_title.setBackgroundColor(resid);
    }
    private void initEvents() {
        im_left.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    public void setTitle(int resid) {
        tv_title.setText(resid);
    }

    public void setTitle(String str) {
        tv_title.setText(str);
    }

    public TextView getCenterText() {
        return this.tv_title;
    }

    public void showLeft() {
        im_left.setVisibility(View.VISIBLE);
    }

    public void hideLeft() {
        im_left.setVisibility(View.GONE);
    }

    public ImageView getRight() {
        im_right.setVisibility(View.VISIBLE);
        return this.im_right;
    }


    public void hideRight() {
        im_right.setVisibility(View.INVISIBLE);
    }

    public void setVisibility(int visibility) {
        rl_title.setVisibility(visibility);
    }

    public int getLeftId() {
        return im_left.getId();
    }

    public int getRightId() {
        return im_right.getId();
    }

    public int getRightTextId() {
        return tv_right.getId();
    }


    public void setRightText(int resid) {
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText(resid);
    }

    public void setRightText(String str) {
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText(str);
    }

    public TextView getRightText() {
        tv_right.setVisibility(View.VISIBLE);
        return this.tv_right;
    }

}
