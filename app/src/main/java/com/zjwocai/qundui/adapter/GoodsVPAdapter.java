package com.zjwocai.qundui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 货货货ViewPager指示器
 * Created by NieLiQin on 2016/6/3.
 */
public class GoodsVPAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public GoodsVPAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (fragments != null)
            ret = fragments.size();
        return ret;
    }
}