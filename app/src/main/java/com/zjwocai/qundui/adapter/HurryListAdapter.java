package com.zjwocai.qundui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.threeti.teamlibrary.activity.BaseActivity;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.model.HomeOrderModel;
import com.zjwocai.qundui.model.HurryListModel;
import com.zjwocai.qundui.model.TypeModel;

import java.util.List;

/**
 * 抢抢抢主页适配器
 * Created by NieLiQin on 2016/5/31.
 */
public class HurryListAdapter extends BaseListAdapter {
    public HurryListAdapter(BaseActivity context, List list) {
        super(context, list, -1,0);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.item_hurry_list, null);
            holder = new ViewHolder();
            holder.iv_phone = (ImageView) convertView.findViewById(R.id.iv_phone);
            holder.tv_start_city = (TextView) convertView.findViewById(R.id.tv_start_city);
            holder.tv_start_area = (TextView) convertView.findViewById(R.id.tv_start_area);
            holder.tv_end_city = (TextView) convertView.findViewById(R.id.tv_end_city);
            holder.tv_end_area = (TextView) convertView.findViewById(R.id.tv_end_area);
            holder.tv_length = (TextView) convertView.findViewById(R.id.tv_length);
            holder.tv_car_type = (TextView) convertView.findViewById(R.id.tv_car_type);
            holder.tv_car_length = (TextView) convertView.findViewById(R.id.tv_car_length);
            holder.tv_car_weight = (TextView) convertView.findViewById(R.id.tv_car_weight);
            holder.ll_navi_info = (LinearLayout) convertView.findViewById(R.id.ll_navi_info);
            holder.ll_navi = (LinearLayout) convertView.findViewById(R.id.ll_navi);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final HurryListModel model = (HurryListModel) mList.get(position);
        holder.tv_start_city.setText(model.getFhcityname());
        holder.tv_start_area.setText(model.getFhareaname());
        holder.tv_end_city.setText(model.getShcityname());
        holder.tv_end_area.setText(model.getShareaname());
        holder.tv_length.setText(model.getDistance() + "km");
        holder.tv_car_type.setText(TypeModel.getCarType(model.getCartype()));
        holder.tv_car_length.setText(TypeModel.getLength(model.getCarlength()));
        holder.tv_car_weight.setText(TypeModel.getCarWeight(model.getWeight()));

        holder.iv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag(model.getSjtel());
                listener.onCustomerListener(v,position);
            }
        });

        holder.ll_navi_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag(model);
                listener.onCustomerListener(v,position);
            }
        });


        return convertView;
    }

    private class ViewHolder {
        ImageView iv_phone;
        TextView tv_start_city,tv_start_area,tv_end_city,tv_end_area,tv_length,tv_car_type,tv_car_length,tv_car_weight;
        LinearLayout ll_navi_info, ll_navi;
    }
}
