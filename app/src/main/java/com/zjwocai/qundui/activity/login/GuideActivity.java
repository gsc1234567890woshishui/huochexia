package com.zjwocai.qundui.activity.login;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.threeti.teamlibrary.activity.BaseProtocolActivity;
import com.threeti.teamlibrary.net.BaseModel;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.adapter.GuideVPAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导页
 * Created by NieLiQin on 2016/7/4.
 */
public class GuideActivity extends BaseProtocolActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private ViewPager vp;
    private View start;
    private List<View> views;
    private GuideVPAdapter mAdapter;

    //引导图片资源
    private static final int[] pics = { R.drawable.ic_guide_1,
            R.drawable.ic_guide_2, R.drawable.ic_guide_3};

    //记录当前选中位置
    private int currentIndex;

    public GuideActivity() {
        super(R.layout.act_guide);
    }

    @Override
    public void findIds() {
        vp = (ViewPager) findViewById(R.id.vp_guide);
        start = findViewById(R.id.view_start);
    }

    @Override
    public void initViews() {
        views = new ArrayList<View>();

        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        //初始化引导图片列表
        for(int i=0; i<pics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setImageResource(pics[i]);
            views.add(iv);
        }
        //初始化Adapter
        mAdapter = new GuideVPAdapter(views);
        vp.setAdapter(mAdapter);

        vp.addOnPageChangeListener(this);

        start.setOnClickListener(this);
    }

    @Override
    public void onTaskSuccess(BaseModel result) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        if (currentIndex == 2){
            startActivity(LoginActivity.class);
            finish();
        }
    }
}
