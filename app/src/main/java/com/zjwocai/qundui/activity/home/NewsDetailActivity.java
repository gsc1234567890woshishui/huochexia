package com.zjwocai.qundui.activity.home;

import android.view.View;
import android.widget.TextView;

import com.threeti.teamlibrary.activity.BaseActivity;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.fragment.MainActivity;

import java.util.Map;

/**
 * 消息详情
 * Created by NieLiQin on 2016/7/28.
 */
public class NewsDetailActivity extends BaseActivity {

    private TextView tvContent;
    private Map map;
    private String content;
    private String flag = "";

    public NewsDetailActivity() {
        super(R.layout.act_news_detail);
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        map = (Map) getIntent().getSerializableExtra("data");
        if (null == map || map.isEmpty()){
            content = "";
            flag = "0";
        } else {
            content = (String) map.get("content");
            flag = (String) map.get("flag");
        }
        if (null == flag || flag.equals("")){
            flag = "0";
        }
    }

    @Override
    public void findIds() {
        tvContent = (TextView) findViewById(R.id.tv_content);
    }

    @Override
    public void initViews() {
        initTitle("消息详情");
        title.setLeftIcon(R.drawable.ic_title_left, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvContent.setText("\t\t" + content);
    }

    @Override
    public void onBackPressed() {
        if (flag.equals("0")){
            super.onBackPressed();
        } else if (flag.equals("-1")) {
            startActivity(MainActivity.class);
            finish();
        }
    }
}
