package com.zjwocai.qundui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.threeti.teamlibrary.activity.BaseActivity;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.model.IAEBModel;

import java.util.List;

/**
 * 收支明细适配器
 * Created by NieLiQin on 2016/5/31.
 */
public class FormListAdapter extends BaseListAdapter {
    public FormListAdapter(BaseActivity context, List list) {
        super(context, list, -1,0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.item_form, null);
            holder = new ViewHolder();
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tvEvent = (TextView) convertView.findViewById(R.id.tv_event);
            holder.tvCost = (TextView) convertView.findViewById(R.id.tv_cost);
            holder.tvIncome = (TextView) convertView.findViewById(R.id.tv_income);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
            IAEBModel model = (IAEBModel) mList.get(position);

            if (model.getCreate_date() != null && !model.getCreate_date().equals("") && model.getCreate_date().length() >= 10){
                holder.tvTime.setText(model.getCreate_date().substring(0,10));
            } else {
                holder.tvTime.setText("未知");
            }

            holder.tvEvent.setText(model.getContent());

            holder.tvCost.setVisibility(View.GONE);
            holder.tvIncome.setVisibility(View.GONE);
            if (model.getType() != null && !model.getType().equals("")){
                if (model.getType().equals("+")){
                    holder.tvIncome.setVisibility(View.VISIBLE);
                    holder.tvIncome.setText("+" + model.getAmount());
                } else if (model.getType().equals("-")){
                    holder.tvCost.setVisibility(View.VISIBLE);
                    holder.tvCost.setText("-" + model.getAmount());
                } else {
                    holder.tvCost.setVisibility(View.VISIBLE);
                    holder.tvCost.setText("未知");
                }
            }

        return convertView;
    }

    private class ViewHolder {
        ImageView iv;
        TextView tvEvent,tvCost,tvIncome,tvAuthor,tvTime,tvComments,tvViewers;
    }
}
