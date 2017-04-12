package com.zjwocai.qundui.activity.mine;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.threeti.teamlibrary.activity.BaseActivity;
import com.threeti.teamlibrary.activity.BaseProtocolActivity;
import com.threeti.teamlibrary.net.BaseModel;
import com.threeti.teamlibrary.utils.SPUtil;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.activity.login.LoginActivity;
import com.zjwocai.qundui.adapter.CarNumberGridAdapter;
import com.zjwocai.qundui.bill.ProtocolBill;
import com.zjwocai.qundui.finals.OtherFinals;
import com.zjwocai.qundui.model.CarModel;
import com.zjwocai.qundui.model.ItemModel;
import com.zjwocai.qundui.util.DateUtil;
import com.zjwocai.qundui.util.DialogUtil;
import com.zjwocai.qundui.util.InputUtil;
import com.zjwocai.qundui.widgets.ScrollDisabledGridView;
import com.zjwocai.qundui.widgets.placeview.WheelTime;
import com.zjwocai.qundui.widgets.placeview.adapter.ArrayWheelAdapter;
import com.zjwocai.qundui.widgets.placeview.lib.WheelView;
import com.zjwocai.qundui.widgets.placeview.listener.OnItemSelectedListener;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * 添加车辆
 * Created by NieLiQin on 2016/7/26.
 */
public class CarAddActivity extends BaseProtocolActivity implements View.OnClickListener {

    private TextView tvEnter, tvTime, tvType, tvWeight, tvCarNumber;
    private RelativeLayout rlTime, rlType, rlWeight, rlNumber;
    private Dialog timeDialog, typeDialog, weightDialog;
    private WheelTime wheelTime;
    private WheelView wv, wv_provice, wv_city, wv_area;
    private ArrayList<ItemModel> carModels;
    private ArrayList<ItemModel> weights;
    private List<String> wordAndNumbers;
    private String type, weight;

    private Dialog netdialog, numberDialog;

    public CarAddActivity() {
        super(R.layout.act_car_add);
    }

    @Override
    public void findIds() {
        rlType = (RelativeLayout) findViewById(R.id.rl_type);
        tvType = (TextView) findViewById(R.id.tv_type);
        rlWeight = (RelativeLayout) findViewById(R.id.rl_weight);
        rlNumber = (RelativeLayout) findViewById(R.id.rl_number);
        tvCarNumber = (TextView) findViewById(R.id.tv_number);
        tvEnter = (TextView) findViewById(R.id.tv_enter);
        tvTime = (TextView) findViewById(R.id.tv_insurance);
        rlTime = (RelativeLayout) findViewById(R.id.rl_insurance);
        tvWeight = (TextView) findViewById(R.id.tv_weights);
    }

    @Override
    public void initViews() {
        initTitle("添加车辆");
        title.setLeftIcon(R.drawable.ic_title_left, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tvEnter.setOnClickListener(this);
        rlTime.setOnClickListener(this);
        rlType.setOnClickListener(this);
        rlWeight.setOnClickListener(this);
        rlNumber.setOnClickListener(this);

        carModels = new ArrayList<>();
        carModels.add(new ItemModel("厢式车", "1"));
        carModels.add(new ItemModel("平板车", "2"));
        carModels.add(new ItemModel("高栏车", "3"));
        carModels.add(new ItemModel("中栏车", "4"));
        carModels.add(new ItemModel("低栏车", "5"));
        carModels.add(new ItemModel("其他车型", "6"));
        carModels.add(new ItemModel("冷藏车", "7"));
        carModels.add(new ItemModel("危险品车", "8"));
        carModels.add(new ItemModel("自卸货车", "9"));
        carModels.add(new ItemModel("集装箱车", "10"));
        carModels.add(new ItemModel("高低板车", "11"));

        weights = new ArrayList<>();
        weights.add(new ItemModel("10吨及以下", "10"));
        weights.add(new ItemModel("20吨", "20"));
        weights.add(new ItemModel("30吨", "30"));
        weights.add(new ItemModel("30吨以上", "100"));


        netdialog = DialogUtil.getProgressDialog(this, getString(R.string.ui_net_request));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_enter:
                if (check()) {
                    netdialog.show();
                    ProtocolBill.getInstance().addCar(this, this, tvCarNumber.getText().toString().trim(),
                            type, tvTime.getText().toString().trim(), weight);
                }
                break;
            case R.id.rl_insurance:
                showTimeDialog();
                break;
            case R.id.rl_type:
                showTypeDialog();
                break;
            case R.id.rl_weight:
                showWeightDialog();
                break;
            case R.id.rl_number:
                showCarNumberDialog();
                break;
        }
    }

    private boolean check() {
        if (TextUtils.isEmpty(tvCarNumber.getText().toString().trim())) {
            showToast("请输入车牌号");
            return false;
        }
        if (tvCarNumber.getText().toString().trim().length() != 7) {
            showToast("请输入7位车牌号");
            return false;
        }
        if (TextUtils.isEmpty(tvType.getText().toString().trim())) {
            showToast("请输入车辆类型");
            return false;
        }
        if (TextUtils.isEmpty(tvWeight.getText().toString().trim())) {
            showToast("请输入车辆载重");
            return false;
        }
        if (TextUtils.isEmpty(tvTime.getText().toString().trim())) {
            showToast("请选择车险到期时间");
            return false;
        }
        return true;
    }

    private void showWeightDialog() {
        if (null == weightDialog) {
            View view = LayoutInflater.from(this).inflate(R.layout.dia_picker_time, null);
            weightDialog = DialogUtil.getDialog(CarAddActivity.this, view, Gravity.BOTTOM, true);
            wv_provice = (WheelView) view.findViewById(R.id.wv_year);
            wv_city = (WheelView) view.findViewById(R.id.wv_month);
            wv_area = (WheelView) view.findViewById(R.id.wv_day);
            wv_area.setVisibility(View.GONE);
            wv_city.setVisibility(View.GONE);
            TextView btnCancel = (TextView) view.findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    weightDialog.dismiss();
                }
            });
            final TextView btnSubmit = (TextView) view.findViewById(R.id.btnSubmit);
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (wv_provice.isEnabled()) {
                        String place = weights.get(wv_provice.getCurrentItem()).getName();
                        weight = weights.get(wv_provice.getCurrentItem()).getCode();
                        tvWeight.setText(place);
                        weightDialog.dismiss();
                    }
                }
            });
            wv_provice.setAdapter(new ArrayWheelAdapter(weights));
            wv_provice.setCyclic(false);
            wv_provice.setHide(false);

            wv_provice.setWv(wv_city, wv_area);
            wv_city.setWv(wv_provice, wv_area);
            wv_area.setWv(wv_provice, wv_city);

            wv_provice.setCurrentItem(0);
        }
        weightDialog.show();
    }

    private void showTypeDialog() {
        if (null == typeDialog) {
            View view = LayoutInflater.from(this).inflate(R.layout.dia_picker_time, null);
            typeDialog = DialogUtil.getDialog(CarAddActivity.this, view, Gravity.BOTTOM, true);
            wv_provice = (WheelView) view.findViewById(R.id.wv_year);
            wv_city = (WheelView) view.findViewById(R.id.wv_month);
            wv_area = (WheelView) view.findViewById(R.id.wv_day);
            wv_area.setVisibility(View.GONE);
            wv_city.setVisibility(View.GONE);
            TextView btnCancel = (TextView) view.findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    typeDialog.dismiss();
                }
            });
            final TextView btnSubmit = (TextView) view.findViewById(R.id.btnSubmit);
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (wv_provice.isEnabled()) {
                        String place = carModels.get(wv_provice.getCurrentItem()).getName();
                        type = carModels.get(wv_provice.getCurrentItem()).getCode();
                        tvType.setText(place);
                        typeDialog.dismiss();
                    }
                }
            });
            wv_provice.setAdapter(new ArrayWheelAdapter(carModels));
            wv_provice.setCyclic(false);
            wv_provice.setHide(false);

            wv_provice.setWv(wv_city, wv_area);
            wv_city.setWv(wv_provice, wv_area);
            wv_area.setWv(wv_provice, wv_city);

            wv_provice.setCurrentItem(0);
        }
        typeDialog.show();
    }

    private void showTimeDialog() {
        if (timeDialog == null) {
            View dia_time_picker = LayoutInflater.from(this).inflate(R.layout.dia_picker_time, null);
            timeDialog = DialogUtil.getDialog(CarAddActivity.this, dia_time_picker, Gravity.BOTTOM, true);
            View timepickerview = dia_time_picker.findViewById(R.id.ll_picker);
            wheelTime = new WheelTime(timepickerview, WheelTime.Type.YEAR_MONTH_DAY);
            TextView btnCancel = (TextView) dia_time_picker.findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    timeDialog.dismiss();
                }
            });
            TextView btnSubmit = (TextView) dia_time_picker.findViewById(R.id.btnSubmit);
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (wheelTime.isE()) {
                        Calendar cal = Calendar.getInstance();
                        String t = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH);
                        String today = DateUtil.format(t, "yyyy-M-d", "yyyy-MM-dd");
                        if (DateUtil.stringToLong(today) > DateUtil.stringToLong(wheelTime.getTime()))
                            showToast("不能选择今天之前的时间");
                        else {
                            tvTime.setText(wheelTime.getTime());
                            timeDialog.dismiss();
                        }
                    }
                }
            });
            if (TextUtils.isEmpty(tvTime.getText().toString())) {
                //默认选中当前时间
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                wheelTime.setPicker(year, month, day);
            } else {
                String time = tvTime.getText().toString();
                int year = Integer.valueOf(time.substring(0, 4));
                int month = Integer.valueOf(time.substring(5, 7)) - 1;
                int day = Integer.valueOf(time.substring(8, 10));
                wheelTime.setPicker(year, month, day);
            }
            wheelTime.setCyclic(true);
        }
        timeDialog.show();
    }

    @Override
    public void onTaskSuccess(BaseModel result) {
        showToast("添加车辆成功!");
        setResult(RESULT_OK, new Intent().putExtra("data", true));
        netdialog.dismiss();
        //添加的车辆号码
        if(SPUtil.getString("addcar").equals("1")){//如果是从充值etc添加的车辆
            Intent intent = new Intent(this, EtcAskActivity.class);
            intent.putExtra("etccar", "etccar");
            startActivity(intent);
            SPUtil.saveString("addcar","");
        }

        finish();
    }

    @Override
    public void onTaskFail(@SuppressWarnings("rawtypes") BaseModel result) {
//        super.onTaskFail(result);
        if (result.getMsgtype() != null && result.getMsgtype().equals("2")) {
            showToast("登录失效，请重新登录");
            saveUser(null);
            startActivity(LoginActivity.class);
        } else if (!TextUtils.isEmpty(result.getMsg())) {
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

    private void showCarNumberDialog() {
        if (null == numberDialog) {
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_select_car_number, null);
            final TextView tvNumber = (TextView) view.findViewById(R.id.tv_number);
            TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
            ImageView ivDelete = (ImageView) view.findViewById(R.id.iv_delete);
            ScrollDisabledGridView keyboard = (ScrollDisabledGridView) view.findViewById(R.id.sdgv_keyboard);
            final CarNumberGridAdapter adapter = new CarNumberGridAdapter(this, OtherFinals.PROVINCES);
            keyboard.setAdapter(adapter);
            wordAndNumbers = new ArrayList<>();
            wordAndNumbers.addAll(OtherFinals.LETTERS);
            wordAndNumbers.addAll(OtherFinals.NUMBERS);
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvNumber.setText("");
                    adapter.updateKeyboard(OtherFinals.PROVINCES, true);
                    numberDialog.dismiss();
                }
            });
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(tvNumber.getText())) {
                        String current = tvNumber.getText().toString();
                        if (current.length() == 1)
                            current = "";
                        else
                            current = current.substring(0, current.length() - 1);
                        tvNumber.setText(current);
                        if (current.equals("")) {
                            adapter.updateKeyboard(OtherFinals.PROVINCES, true);
                        } else {
                            adapter.updateKeyboard(wordAndNumbers, false);
                        }
                    }
                }
            });
            keyboard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (!TextUtils.isEmpty(tvNumber.getText())) {
                        String current = tvNumber.getText().toString();
                        if (current.length() == 1) {
                            if (id < 24) {
                                current = current + wordAndNumbers.get((int) id);
                                tvNumber.setText(current);
                            }
                        } else {
                            if (id < 34) {
                                current = current + wordAndNumbers.get((int) id);
                                tvNumber.setText(current);
                            }
                        }
                        if (current.length() == 7) {
                            tvCarNumber.setText(current);
                            tvNumber.setText("");
                            adapter.updateKeyboard(OtherFinals.PROVINCES, true);
                            numberDialog.dismiss();
                        }
                    } else {
                        if (id < 34) {
                            tvNumber.setText(OtherFinals.PROVINCES.get((int) id));
                            adapter.updateKeyboard(wordAndNumbers, false);
                        }
                    }
                }
            });
            numberDialog = DialogUtil.getDialog(CarAddActivity.this, view, Gravity.BOTTOM, true);
        }
        numberDialog.show();
    }



}
