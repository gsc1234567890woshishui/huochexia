package com.zjwocai.qundui.fragmenthome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.threeti.teamlibrary.activity.BaseActivity;
import com.threeti.teamlibrary.net.BaseModel;
import com.threeti.teamlibrary.net.ProcotolCallBack;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.activity.goods.ListFragment;
import com.zjwocai.qundui.activity.goods.MyBaseFragment;
import com.zjwocai.qundui.activity.goods.SearchActivity;
import com.zjwocai.qundui.adapter.GoodsVPAdapter;

import java.util.ArrayList;
import java.util.List;


public class ShopcartFragment extends MyBaseFragment implements ProcotolCallBack,View.OnClickListener,ViewPager.OnPageChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RelativeLayout rlAll, rlIng, rlFinish, rlNot;
    private List<Fragment> mFragments;
    private ViewPager mVP;
    private GoodsVPAdapter mAdapter;
    private ImageView imSearch;
    public static final int ALL = 1;
    public static final int ING = 2;
    public static final int FINISH = 3;
    public static final int NOT = 4;
    private String checkid = "0";
    private String current = "0";



    public ShopcartFragment() {
        // Required empty public constructor

    }

    @Override
    protected int getLayoutId() {
        //View ret = LayoutInflater.from(getActivity()).inflate(R.layout.act_money2, null);
        return R.layout.act_goods2;
    }
    /**
     * 当界面重新展示时（fragment.show）,调用onrequest刷新界面
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);

        if (!hidden) {
            //autoRefresh();
            //这个方法调用去改变内部的刷新
             //ListFragment.refresh();

        }
    }

    protected void initWidget(View root) {
        super.initWidget(root);
        initData();
        rlAll = (RelativeLayout) root.findViewById(R.id.rl_all);
        rlIng = (RelativeLayout) root.findViewById(R.id.rl_ing);
        rlFinish = (RelativeLayout) root.findViewById(R.id.rl_finish);
        rlNot = (RelativeLayout) root.findViewById(R.id.rl_not);
        mVP = (ViewPager) root.findViewById(R.id.vp_goods);
        imSearch = (ImageView)root.findViewById(R.id.im_search);
        //commMenuBar = new CommMenuBar(this, CommMenuBar.MENU_GOODS);
        initViews();
    }


    public void initData(){
        checkid = (String) getActivity().getIntent().getSerializableExtra("data");
        if (null == checkid || checkid.equals("")){
            checkid = "0";
        }
        current = checkid;
    }

    public void initViews(){

        imSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseActivity)getActivity()).startActivity(SearchActivity.class);
            }
        });

        rlAll.setOnClickListener(this);
        rlIng.setOnClickListener(this);
        rlFinish.setOnClickListener(this);
        rlNot.setOnClickListener(this);

        mFragments = new ArrayList<>();
        mFragments.add(initFragment("not"));
        mFragments.add(initFragment("ing"));
        mFragments.add(initFragment("finish"));
        mFragments.add(initFragment("all"));

        mAdapter = new GoodsVPAdapter(getChildFragmentManager(), mFragments);
        mVP.setAdapter(mAdapter);
        mVP.addOnPageChangeListener(this);
        mVP.setOffscreenPageLimit(3);

        switch (checkid) {
            case "0":
                showTab(NOT);
                mVP.setCurrentItem(0);
                break;
            case "1":
                showTab(ING);
                mVP.setCurrentItem(1);
                break;
            case "2":
                showTab(FINISH);
                mVP.setCurrentItem(2);
                break;
            case "3":
                showTab(ALL);
                mVP.setCurrentItem(3);
                break;
        }

    }
    private void showTab(int type) {
        rlAll.setSelected(false);
        rlIng.setSelected(false);
        rlFinish.setSelected(false);
        rlNot.setSelected(false);
        switch (type) {
            case NOT:
                rlAll.setSelected(true);
                current = "0";
                break;
            case ING:
                rlIng.setSelected(true);
                current = "1";
                break;
            case FINISH:
                rlFinish.setSelected(true);
                current = "2";
                break;
            case ALL:
                rlNot.setSelected(true);
                current = "3";
                break;
        }
    }

    private ListFragment initFragment(String type){
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        ListFragment fragment = new ListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShopcartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShopcartFragment newInstance(String param1, String param2) {
        ShopcartFragment fragment = new ShopcartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.act_goods2, container, false);
//    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_all:
                showTab(NOT);
                mVP.setCurrentItem(0);
                break;
            case R.id.rl_ing:
                showTab(ING);
                mVP.setCurrentItem(1);
                break;
            case R.id.rl_finish:
                showTab(FINISH);
                mVP.setCurrentItem(2);
                break;
            case R.id.rl_not:
                showTab(ALL);
                mVP.setCurrentItem(3);
                break;

        }
    }

    @Override
    public void onTaskSuccess(BaseModel result) {

    }

    @Override
    public void onTaskFail(BaseModel result) {

    }

    @Override
    public void onTaskFinished(String resuestCode) {

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }


    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                showTab(NOT);
                break;
            case 1:
                showTab(ING);
                break;
            case 2:
                showTab(FINISH);
                break;
            case 3:
                showTab(ALL);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
