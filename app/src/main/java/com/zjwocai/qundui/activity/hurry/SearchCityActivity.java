package com.zjwocai.qundui.activity.hurry;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.threeti.teamlibrary.activity.BaseProtocolActivity;
import com.threeti.teamlibrary.net.BaseModel;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.adapter.OnCustomListener;
import com.zjwocai.qundui.adapter.SearchCityListAdapter;
import com.zjwocai.qundui.bill.ProtocolBill;
import com.zjwocai.qundui.model.CityModel;
import com.zjwocai.qundui.util.InputUtil;
import com.zjwocai.qundui.widgets.sortlistview.CharacterParser;
import com.zjwocai.qundui.widgets.sortlistview.PinyinComparator;
import com.zjwocai.qundui.widgets.sortlistview.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 搜索城市
 * Created by NieLiQin on 2016/8/28.
 */
public class SearchCityActivity extends BaseProtocolActivity implements SideBar.OnTouchingLetterChangedListener, OnCustomListener, View.OnClickListener {
    private RelativeLayout rlLeft;
    private EditText etSearch;
    private SideBar sb;
    private TextView tvLetter, tvCurrentCity;
    private ListView lvCities;
    private PinyinComparator pinyinComparator;
    private CharacterParser characterParser;
    private SearchCityListAdapter mAdapter;
    private List<CityModel> cityModels;
    private List<CityModel> filterDateList;
    private String currentCity;
    private LinearLayout llCurrentCity;


    public SearchCityActivity() {
        super(R.layout.act_search_city);
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        currentCity = (String) getIntent().getSerializableExtra("data");
        cityModels = ProtocolBill.getInstance().getCities();
        if (cityModels == null || cityModels.isEmpty()) {
            ProtocolBill.getInstance().getAllCities(this, this);
            cityModels = new ArrayList<>();
        } else {
            for (CityModel model : cityModels) {
                model.genSortLetters();
            }
        }
    }

    @Override
    public void findIds() {
        rlLeft = (RelativeLayout) findViewById(R.id.rl_left);
        etSearch = (EditText) findViewById(R.id.et_search);
        lvCities = (ListView) findViewById(R.id.lv_cities);
        sb = (SideBar) findViewById(R.id.sb);
        tvLetter = (TextView) findViewById(R.id.tv_letter);
        llCurrentCity = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.header_sort_list, null);
        tvCurrentCity = (TextView) llCurrentCity.findViewById(R.id.tv_current_city);
        sb.setTextView(tvLetter);
        sb.setOnTouchingLetterChangedListener(this);
    }

    @Override
    public void initViews() {
        // 根据a-z进行排序源数据
        pinyinComparator = new PinyinComparator();
        characterParser = new CharacterParser();
        rlLeft.setOnClickListener(this);
        tvCurrentCity.setText(currentCity);
        Collections.sort(cityModels, pinyinComparator);
        filterDateList = new ArrayList<>();
        filterDateList.addAll(cityModels);
        mAdapter = new SearchCityListAdapter(this, cityModels);
        lvCities.setAdapter(mAdapter);
        mAdapter.setOnCustomListener(this);
        lvCities.addHeaderView(llCurrentCity);
        lvCities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id != -1) {
                    Intent intent = new Intent();
                    intent.putExtra("data", filterDateList.get((int) id).getModelName());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onTaskSuccess(BaseModel result) {
        if (RQ_GET_ALL_CITY_LIST.equals(result.getRequest_code())) {
            List<CityModel> models = (List<CityModel>) result.getResult();
            if (models != null && !models.isEmpty()) {
                cityModels.addAll(models);
                for (CityModel model : cityModels) {
                    model.genSortLetters();
                }
                Collections.sort(cityModels, pinyinComparator);
                filterDateList.addAll(cityModels);
                mAdapter.updateListView(cityModels);
            }
        }
    }

    @Override
    public void onCustomerListener(View v, int position) {

    }

    /**
     * 字母快速排序回调接口
     */
    @Override
    public void onTouchingLetterChanged(String s) {
        //该字母首次出现的位置
        if (mAdapter != null) {
            int position = mAdapter.getPositionForSection(s.charAt(0));
            if (position != -1) {
                lvCities.setSelection(position);
            }
        }
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     */
    private void filterData(String filterStr) {
        filterDateList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = cityModels;
        } else {
            filterDateList.clear();
            filterStr = characterParser.getSelling(filterStr).toUpperCase();
            for (CityModel sortModel : cityModels) {
                String name = sortModel.getPinyinSortLetter();
                String upperName = characterParser.getSelling(name).toUpperCase();
                if (name.contains(filterStr) || upperName.startsWith(filterStr) || checkFirst(sortModel.getName(), filterStr)) {
                    filterDateList.add(sortModel);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        if (mAdapter != null) {
            mAdapter.updateListView(filterDateList);
        }
    }

    private boolean checkFirst(String name, String filterStr) {
        String upper = filterStr.toUpperCase();
        name = characterParser.getFirst(name).toUpperCase();
        if (!name.contains(upper)) {
            return false;
        }
        return true;
    }

    @Override
    protected void onPause() {
        InputUtil.hideInputMethdView(this, etSearch);
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_left:
                finish();
                break;
        }
    }
}
