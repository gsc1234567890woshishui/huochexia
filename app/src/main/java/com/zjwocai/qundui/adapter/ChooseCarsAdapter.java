package com.zjwocai.qundui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.threeti.teamlibrary.activity.BaseActivity;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.model.EnableCarModel;

import java.util.List;

/**
 * 车辆选择列表适配器
 * Created by NieLiQin on 2016/5/31.
 */
public class ChooseCarsAdapter extends BaseListAdapter {
    public ChooseCarsAdapter(BaseActivity context, List list) {
        super(context, list, -1, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.item_cars, null);
            holder = new ViewHolder();
            holder.tvCar = (TextView) convertView.findViewById(R.id.tv_car);
            holder.llCar = (LinearLayout) convertView.findViewById(R.id.ll_car);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.llCar.setSelected(false);
        EnableCarModel model = (EnableCarModel) mList.get(position);
        if (model.isSelect()) {
            holder.llCar.setSelected(true);
        }
        holder.tvCar.setText(model.getCarnumber());

        return convertView;
    }

    private class ViewHolder {
        ImageView iv;
        TextView tvCar;
        LinearLayout llCar;
    }
}
