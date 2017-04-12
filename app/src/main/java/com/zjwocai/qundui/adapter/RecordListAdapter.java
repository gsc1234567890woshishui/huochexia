package com.zjwocai.qundui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjwocai.qundui.R;
import com.zjwocai.qundui.model.RecoderModel;

import java.util.List;

/**
 * 主页列表适配器
 * Created by NieLiQin on 2016/5/31.
 */
public class RecordListAdapter extends BaseListAdapter {

    public RecordListAdapter(Context context, List list) {
        super(context, list, -1, 0);

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.listviewitem, null);
            holder = new ViewHolder();
            //holder.ivState = (ImageView) convertView.findViewById(R.id.iv_state);
            holder.tvType = (TextView) convertView.findViewById(R.id.tv_type);
            holder.tvCount = (TextView) convertView.findViewById(R.id.tv_count);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tvState = (TextView) convertView.findViewById(R.id.tv_state);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Log.e("aaaaa", mList.size() + "====" + mList.get(position) + "===============怎么操作============");
        RecoderModel model = (RecoderModel) mList.get(position);
        if(model.getType().equals("1")){//充值记录
            holder.tvType.setText(model.getCzType());
            holder.tvCount.setText(model.getCzAmount());
            holder.tvTime.setText(model.getCzTime());
            holder.tvState.setText(model.getCzState());

        }else{
            holder.tvType.setText(model.getXfStation());
            holder.tvCount.setText(model.getXfAmount());
            holder.tvTime.setText(model.getXfTime());
            holder.tvState.setText(model.getJsBalance());
        }


        return convertView;

    }

    private class ViewHolder {

        TextView tvType, tvCount, tvTime, tvState;

    }
}
