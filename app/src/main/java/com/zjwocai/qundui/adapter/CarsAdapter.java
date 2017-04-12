package com.zjwocai.qundui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.threeti.teamlibrary.activity.BaseActivity;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.model.CarModel;
import com.zjwocai.qundui.model.TypeModel;

import java.util.List;

/**
 * 车辆列表适配器
 * Created by NieLiQin on 2016/5/31.
 */
public class CarsAdapter extends BaseListAdapter {
    public CarsAdapter(BaseActivity context, List list) {
        super(context, list, -1,0);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.item_cars_lv, null);
            holder = new ViewHolder();
            holder.tvManager = (TextView) convertView.findViewById(R.id.tv_manager);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_car_name);
            holder.tvNumber = (TextView) convertView.findViewById(R.id.tv_car_number);
            holder.tvType = (TextView) convertView.findViewById(R.id.tv_car_type);
            holder.tvInsurance = (TextView) convertView.findViewById(R.id.tv_car_insurance);
            holder.tvWeight = (TextView) convertView.findViewById(R.id.tv_car_weight);
            holder.tvState = (TextView) convertView.findViewById(R.id.tv_car_state);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CarModel model = (CarModel) mList.get(position);

        holder.tvName.setText(model.getCarnumber() + " " + TypeModel.getCarType(model.getType()));
        holder.tvManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCustomerListener(v,position);
            }
        });
        holder.tvNumber.setText(model.getCarnumber());
        holder.tvType.setText(TypeModel.getCarType(model.getType()));
        if(model.getEnddate().length()>=10){
            holder.tvInsurance.setText(model.getEnddate().substring(0,10));
        }

        if (model.getCarload() != null && !model.getCarload().equals("")){
            holder.tvWeight.setText(TypeModel.getCarWeight(model.getCarload()));
        } else {
            holder.tvWeight.setText("未知");
        }

        if (model.getStatus().equals("1")){
            holder.tvState.setText("空闲中");
        } else {
            holder.tvState.setText("繁忙中");
        }

        return convertView;
    }

    private class ViewHolder {
        ImageView iv;
        TextView tvManager,tvName,tvNumber,tvType,tvInsurance,tvWeight,tvState;
    }
}
