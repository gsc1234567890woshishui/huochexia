package com.zjwocai.qundui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.threeti.teamlibrary.activity.BaseActivity;
import com.zjwocai.qundui.R;

import java.util.List;

/**
 * 搜索历史适配器
 * Created by NieLiQin on 2016/5/31.
 */
public class SearchHistoryAdapter extends BaseListAdapter {
    public SearchHistoryAdapter(BaseActivity context, List list) {
        super(context, list, -1,0);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.item_history, null);
            holder = new ViewHolder();
            holder.tvContent = (TextView) convertView.findViewById(R.id.tv_lv_history);
            holder.tvClear = (TextView) convertView.findViewById(R.id.tv_lv_last);
            holder.rlDelete = (RelativeLayout) convertView.findViewById(R.id.rl_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvContent.setText(mList.get(position).toString());

        holder.tvContent.setVisibility(View.VISIBLE);
        holder.rlDelete.setVisibility(View.VISIBLE);
        holder.tvClear.setVisibility(View.INVISIBLE);
        holder.rlDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCustomerListener(v,position);
            }
        });

        if (position == mList.size() - 1){
            holder.tvContent.setVisibility(View.INVISIBLE);
            holder.rlDelete.setVisibility(View.GONE);
            holder.tvClear.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    private class ViewHolder {

        TextView tvContent,tvClear;
        RelativeLayout rlDelete;
    }
}
