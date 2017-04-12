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
import android.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.threeti.teamlibrary.R;
import com.threeti.teamlibrary.activity.BaseActivity;


/**
 * ClassName:FramentTitleBar
 * Date:2014年10月11日
 *
 * @author BaoHang
 * @since [产品/模块版本] （可选）
 */
public class CommTitleBar {
    private BaseActivity activity;
    private ImageView im_left;
    private ImageView im_right;
    private TextView tv_title;
    private TextView tv_right;
    private TextView tv_left;
    private RelativeLayout rl_title, rl_left, rl_right;

    public CommTitleBar(Activity activity) {
        this.activity = (BaseActivity) activity;
        initViews();
        initSize();
        initEvents();
    }


    private void initViews() {
        im_left = (ImageView) activity.findViewById(R.id.iv_left);
        im_right = (ImageView) activity.findViewById(R.id.iv_right);
        tv_title = (TextView) activity.findViewById(R.id.tv_title);
        rl_title = (RelativeLayout) activity.findViewById(R.id.rl_title);
        rl_left = (RelativeLayout) activity.findViewById(R.id.rl_left);
        rl_right = (RelativeLayout) activity.findViewById(R.id.rl_right);
        tv_right = (TextView) activity.findViewById(R.id.tv_right);
        tv_left = (TextView) activity.findViewById(R.id.tv_left);
    }

    public void initSize() {

    }

    public void setLeftIcon(int resid, OnClickListener listener) {
        im_left.setVisibility(View.VISIBLE);
        rl_left.setVisibility(View.VISIBLE);
        tv_left.setVisibility(View.GONE);
        im_left.setImageResource(resid);
        if (listener != null)
            rl_left.setOnClickListener(listener);
    }

    public void setRightIcon(int resid, OnClickListener listener) {
        im_right.setVisibility(View.VISIBLE);
        im_right.setImageResource(resid);
        rl_right.setVisibility(View.VISIBLE);
        tv_right.setVisibility(View.GONE);
        rl_right.setOnClickListener(listener);
    }

    public void setBackground(int resid) {
        rl_title.setBackgroundResource(resid);
    }

    public void setBackgroundColor(int resid) {
        rl_title.setBackgroundColor(resid);
    }

    private void initEvents() {
        rl_left.setOnClickListener(new OnClickListener() {

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

    public void showLeftIcon() {
        im_left.setVisibility(View.VISIBLE);
        tv_left.setVisibility(View.GONE);
        rl_left.setVisibility(View.VISIBLE);

    }

    public void hideLeftIcon() {
        im_left.setVisibility(View.GONE);
        rl_left.setVisibility(View.GONE);
    }

    public ImageView getRightIcon() {
        im_right.setVisibility(View.VISIBLE);
        return this.im_right;
    }


    public void hideRightIcon() {
        im_right.setVisibility(View.INVISIBLE);
        rl_right.setVisibility(View.INVISIBLE);
    }

    public void hideRightText() {
        tv_right.setVisibility(View.INVISIBLE);
        rl_right.setVisibility(View.INVISIBLE);
    }

    public void showRightText() {
        tv_right.setVisibility(View.VISIBLE);
        rl_right.setVisibility(View.VISIBLE);
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


    public void setRightText(int resid, OnClickListener listener) {
        tv_right.setVisibility(View.VISIBLE);
        im_right.setVisibility(View.GONE);
        rl_right.setVisibility(View.VISIBLE);
        tv_right.setText(resid);
        rl_right.setOnClickListener(listener);
    }

    public void setRightText(String str, OnClickListener listener) {
        tv_right.setVisibility(View.VISIBLE);
        im_right.setVisibility(View.GONE);
        rl_right.setVisibility(View.VISIBLE);
        tv_right.setText(str);
        rl_right.setOnClickListener(listener);
    }

    public TextView getRightText() {
        return this.tv_right;
    }

    public void setLeftText(int resid) {
        tv_left.setVisibility(View.VISIBLE);
        im_left.setVisibility(View.GONE);
        rl_left.setVisibility(View.VISIBLE);
        tv_left.setText(resid);
    }

    public void setLeftText(String str) {
        tv_left.setVisibility(View.VISIBLE);
        im_left.setVisibility(View.GONE);
        rl_left.setVisibility(View.VISIBLE);
        tv_left.setText(str);
    }

    public void setLeftText(String str, OnClickListener listener) {
        tv_left.setVisibility(View.VISIBLE);
        im_left.setVisibility(View.GONE);
        tv_left.setText(str);
        rl_left.setVisibility(View.VISIBLE);
        rl_left.setOnClickListener(listener);
    }

    public TextView getLeftText() {
        return this.tv_left;
    }

}
