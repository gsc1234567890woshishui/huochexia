package com.zjwocai.qundui.activity.mine;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.threeti.teamlibrary.activity.BaseActivity;
import com.threeti.teamlibrary.activity.BaseProtocolActivity;
import com.threeti.teamlibrary.net.BaseModel;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.activity.login.LoginActivity;
import com.zjwocai.qundui.bill.ProtocolBill;
import com.zjwocai.qundui.model.AreaModel;
import com.zjwocai.qundui.model.CityModel;
import com.zjwocai.qundui.model.ItemModel;
import com.zjwocai.qundui.model.ProvinceModel;
import com.zjwocai.qundui.util.DateUtil;
import com.zjwocai.qundui.util.DialogUtil;
import com.zjwocai.qundui.util.InputUtil;
import com.zjwocai.qundui.widgets.placeview.WheelTime;
import com.zjwocai.qundui.widgets.placeview.adapter.ArrayWheelAdapter;
import com.zjwocai.qundui.widgets.placeview.lib.WheelView;
import com.zjwocai.qundui.widgets.placeview.listener.OnItemSelectedListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * 添加银行卡
 * Created by NieLiQin on 2016/7/26.
 */
public class CardAddActivity extends BaseProtocolActivity implements View.OnClickListener {
    private RelativeLayout rlBankName, rlAddress;
    private TextView tvBankName, tvAddress, tvEnter;
    private EditText etName, etNumber;
    private ArrayList<ItemModel> bankModels;
    private String type, province, city;
    private Dialog dialog_place, netdialog, bankdialog;
    private WheelView wv, wv_provice, wv_city, wv_area;
    private ArrayList<ProvinceModel> provinceModels;
    private ArrayList<CityModel> cityModels;
    private ArrayList<AreaModel> areaModels;

    public CardAddActivity() {
        super(R.layout.act_card_add);
    }

    @Override
    public void findIds() {
        rlBankName = (RelativeLayout) findViewById(R.id.rl_bank_name);
        rlAddress = (RelativeLayout) findViewById(R.id.rl_address);
        tvBankName = (TextView) findViewById(R.id.tv_bank_name);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        etName = (EditText) findViewById(R.id.et_name);
        etNumber = (EditText) findViewById(R.id.et_number);
        tvEnter = (TextView) findViewById(R.id.tv_enter);
    }

    @Override
    public void initViews() {
        initTitle("添加银行卡");
        title.setLeftIcon(R.drawable.ic_title_left, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rlBankName.setOnClickListener(this);
        rlAddress.setOnClickListener(this);
        tvEnter.setOnClickListener(this);

        bankModels = new ArrayList<>();
        bankModels.add(new ItemModel("农业银行", "1"));
        bankModels.add(new ItemModel("中国银行", "2"));
        bankModels.add(new ItemModel("交通银行", "3"));
        bankModels.add(new ItemModel("招商银行", "4"));
        bankModels.add(new ItemModel("建设银行", "5"));
        bankModels.add(new ItemModel("兴业银行", "6"));
        bankModels.add(new ItemModel("中信银行", "7"));
        bankModels.add(new ItemModel("光大银行", "8"));
        bankModels.add(new ItemModel("工商银行", "9"));
        bankModels.add(new ItemModel("华夏银行", "10"));
        bankModels.add(new ItemModel("民生银行", "11"));
        bankModels.add(new ItemModel("浦发银行", "12"));
        bankModels.add(new ItemModel("上海浦东发展银行", "13"));
        bankModels.add(new ItemModel("北京银行", "14"));
        bankModels.add(new ItemModel("南京银行", "15"));
        bankModels.add(new ItemModel("宁波银行", "16"));
        bankModels.add(new ItemModel("其它银行", "17"));

        provinceModels = new ArrayList<>();
        cityModels = new ArrayList<>();
        areaModels = new ArrayList<>();

        netdialog = DialogUtil.getProgressDialog(this, getString(R.string.ui_net_request));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_bank_name:
                showBankDialog();
                break;
            case R.id.rl_address:
                if (provinceModels.size() == 0) {
                    //请求位置数据
                    ProtocolBill.getInstance().getProvinces(this, this);
                } else {
                    showPlaceDialog();
                }
                break;
            case R.id.tv_enter:
                if (check()) {
                    netdialog.show();
                    ProtocolBill.getInstance().addCard(this, this, type,
                            etNumber.getText().toString().trim(),
                            etName.getText().toString().trim(),
                            province, city);
                }
                break;
        }
    }

    private boolean check() {
        if (TextUtils.isEmpty(tvBankName.getText().toString().trim())) {
            showToast("请选择银行");
            return false;
        }
        String code = tvAddress.getText().toString();
        if (TextUtils.isEmpty(code)) {
            showToast("请选择银行地址");
            return false;
        }
        if (TextUtils.isEmpty(etName.getText().toString().trim())) {
            showToast("请输入开户姓名");
            return false;
        }
        if (TextUtils.isEmpty(etNumber.getText().toString().trim())) {
            showToast("请输入银行卡号");
            return false;
        }
        return true;
    }


    @Override
    public void onTaskSuccess(BaseModel result) {
        if (RQ_ADD_CARD.equals(result.getRequest_code())) {
            showToast("添加银行卡成功!");
            setResult(RESULT_OK, new Intent().putExtra("data", true));
            netdialog.dismiss();
            finish();

        } else if (RQ_GET_PROVINCE_LIST.equals(result.getRequest_code())) {
            List<ProvinceModel> models = (List<ProvinceModel>) result.getResult();
            if (models != null && !models.isEmpty()) {
                provinceModels.clear();
                provinceModels.addAll(models);
                ProtocolBill.getInstance().getCities(this, this, provinceModels.get(0).getId());
            }
        } else if (RQ_GET_CITY_LIST.equals(result.getRequest_code())) {
            List<CityModel> models = (List<CityModel>) result.getResult();
            if (models != null && !models.isEmpty()) {
                cityModels.clear();
                cityModels.addAll(models);
                if (dialog_place != null) {
                    wv_city.setAdapter(new ArrayWheelAdapter(cityModels));
                    wv_city.setCurrentItem(0);
                    wv_provice.setEnabled(true);
                }
                ProtocolBill.getInstance().getAreas(this, this, cityModels.get(0).getId());
            }
        } else if (RQ_GET_AREA_LIST.equals(result.getRequest_code())) {
            List<AreaModel> models = (List<AreaModel>) result.getResult();
            if (models != null && !models.isEmpty()) {
                areaModels.clear();
                areaModels.addAll(models);
                if (dialog_place != null) {
                    wv_area.setAdapter(new ArrayWheelAdapter(areaModels));
                    wv_area.setCurrentItem(0);
                    wv_city.setEnabled(true);
                    wv_provice.setEnabled(true);
                }
                showPlaceDialog();
            }
        }
    }

    @Override
    public void onTaskFail(@SuppressWarnings("rawtypes") BaseModel result) {
//        super.onTaskFail(result);
        if (result.getMsgtype() != null && result.getMsgtype().equals("2")){
            showToast("登录失效，请重新登录");
            saveUser(null);
            startActivity(LoginActivity.class);
        }else if (!TextUtils.isEmpty(result.getMsg())) {
            showToast(result.getMsg() + "");
        }
    }

    @Override
    public void onTaskFinished(String resuestCode) {
        super.onTaskFinished(resuestCode);
        if (netdialog.isShowing()) {
            netdialog.dismiss();
        }
    }

    private void showPlaceDialog() {
        if (null == dialog_place) {
            View view = LayoutInflater.from(this).inflate(R.layout.dia_picker_time, null);
            dialog_place = DialogUtil.getDialog(CardAddActivity.this, view, Gravity.BOTTOM, true);
            wv_provice = (WheelView) view.findViewById(R.id.wv_year);
            wv_city = (WheelView) view.findViewById(R.id.wv_month);
            wv_area = (WheelView) view.findViewById(R.id.wv_day);
            TextView btnCancel = (TextView) view.findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_place.dismiss();
                }
            });
            final TextView btnSubmit = (TextView) view.findViewById(R.id.btnSubmit);
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (wv_provice.isEnabled() && wv_city.isEnabled() && wv_area.isEnabled()) {
                        String place = provinceModels.get(wv_provice.getCurrentItem()).getName() +
                                " " + cityModels.get(wv_city.getCurrentItem()).getName();
                        province = provinceModels.get(wv_provice.getCurrentItem()).getCode();
                        city = cityModels.get(wv_city.getCurrentItem()).getCode();
                        tvAddress.setText(place);
                        dialog_place.dismiss();
                    }
                }
            });
            wv_provice.setAdapter(new ArrayWheelAdapter(provinceModels));
            wv_provice.setCyclic(false);
            wv_provice.setHide(true);
            wv_provice.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int i) {
                    if (null != provinceModels && !provinceModels.isEmpty()){
                        String provinceId = provinceModels.get(i).getId();
                        ProtocolBill.getInstance().getCities(CardAddActivity.this, CardAddActivity.this, provinceId);
                    } else {
                        dialog_place.dismiss();
                    }
                }
            });
            wv_city.setAdapter(new ArrayWheelAdapter(cityModels));
            wv_city.setCyclic(false);
            wv_city.setHide(true);
            wv_city.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int i) {
                    if (null != cityModels && !cityModels.isEmpty()){
                        String CityId = cityModels.get(i).getId();
                        ProtocolBill.getInstance().getAreas(CardAddActivity.this, CardAddActivity.this, CityId);
                    } else {
                        dialog_place.dismiss();
                    }
                }
            });
            wv_area.setAdapter(new ArrayWheelAdapter(areaModels));
            wv_area.setCyclic(false);
            wv_area.setHide(true);
            wv_area.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int i) {
                    wv_area.setEnabled(true);
                    wv_city.setEnabled(true);
                    wv_provice.setEnabled(true);
                }
            });
            wv_provice.setWv(wv_city, wv_area);
            wv_city.setWv(wv_provice, wv_area);
            wv_area.setWv(wv_provice, wv_city);

            wv_provice.setCurrentItem(0);
            wv_city.setCurrentItem(0);
            wv_area.setCurrentItem(0);
        }
        dialog_place.show();
    }

    private void showBankDialog() {
        if (null == bankdialog) {
            View view = LayoutInflater.from(this).inflate(R.layout.dia_picker_time, null);
            bankdialog = DialogUtil.getDialog(CardAddActivity.this, view, Gravity.BOTTOM, true);
            wv_provice = (WheelView) view.findViewById(R.id.wv_year);
            wv_city = (WheelView) view.findViewById(R.id.wv_month);
            wv_area = (WheelView) view.findViewById(R.id.wv_day);
            wv_area.setVisibility(View.GONE);
            wv_city.setVisibility(View.GONE);
            TextView btnCancel = (TextView) view.findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bankdialog.dismiss();
                }
            });
            final TextView btnSubmit = (TextView) view.findViewById(R.id.btnSubmit);
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (wv_provice.isEnabled()) {
                        String place = bankModels.get(wv_provice.getCurrentItem()).getName();
                        type = bankModels.get(wv_provice.getCurrentItem()).getCode();
                        tvBankName.setText(place);
                        bankdialog.dismiss();
                    }
                }
            });
            wv_provice.setAdapter(new ArrayWheelAdapter(bankModels));
            wv_provice.setCyclic(false);
            wv_provice.setHide(false);

            wv_provice.setWv(wv_city, wv_area);
            wv_city.setWv(wv_provice, wv_area);
            wv_area.setWv(wv_provice, wv_city);

            wv_provice.setCurrentItem(0);
        }
        bankdialog.show();
    }

    @Override
    protected void onPause() {
        InputUtil.hideInputMethdView(CardAddActivity.this, etName);
        InputUtil.hideInputMethdView(CardAddActivity.this, etNumber);
        super.onPause();
    }
}
