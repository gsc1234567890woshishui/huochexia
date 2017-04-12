package com.zjwocai.qundui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.threeti.teamlibrary.activity.BaseActivity;
import com.zjwocai.qundui.R;

import java.util.List;

/**
 * 车牌选择适配器
 * Created by NieLiQin on 2016/6/1.
 */
public class CarNumberGridAdapter extends BaseListAdapter {
    private boolean isPro = true;

    public CarNumberGridAdapter(BaseActivity context, List<String> list) {
        super(context, list);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.item_gv_key, null);
            holder = new ViewHolder();
            holder.tvContent = (TextView) convertView.findViewById(R.id.tv_key_value);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvContent.setText(mList.get(position).toString());

        return convertView;
    }

    public void updateKeyboard(List<String> list, boolean isPro){
        mList = list;
        this.isPro = isPro;
        notifyDataSetChanged();
    }

    public boolean isPro() {
        return isPro;
    }

    public void setPro(boolean pro) {
        isPro = pro;
    }

    class ViewHolder {
        TextView tvContent;
    }
}
