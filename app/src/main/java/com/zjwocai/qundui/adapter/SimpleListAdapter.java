package com.zjwocai.qundui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.threeti.teamlibrary.activity.BaseActivity;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.model.AreaModel;
import com.zjwocai.qundui.model.GetModelName;
import com.zjwocai.qundui.model.ItemModel;

import java.util.List;

/**
 * 地区适配器
 * Created by NieLiQin on 2016/5/31.
 */
public class SimpleListAdapter extends BaseListAdapter {

    private int select = -1;
    private int type = -1;

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }

    public SimpleListAdapter(BaseActivity context, List list, int type) {
        super(context, list, -1,0);
        this.type = type;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.item_simple_list, null);
            holder = new ViewHolder();
            holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            holder.llContent = (LinearLayout) convertView.findViewById(R.id.ll_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mList.get(position) instanceof String){
            String content = (String)mList.get(position);
            holder.tvContent.setText(content);

        } else if (mList.get(position) instanceof GetModelName){
            GetModelName model = (GetModelName)mList.get(position);
            holder.tvContent.setText(model.getModelName());
        } else if (mList.get(position) instanceof ItemModel){
            ItemModel model = (ItemModel)mList.get(position);
            holder.tvContent.setText(model.getName());
        }

        if (select == position) {
            holder.tvContent.setTextColor(Color.parseColor("#12b7f5"));
            switch (type){
                case 1:
                    holder.llContent.setBackgroundColor(Color.parseColor("#f8f8f8"));
                    break;
                case 2:
                    holder.llContent.setBackgroundColor(Color.parseColor("#f0f0f0"));
                    break;
                case 3:
                    holder.llContent.setBackgroundColor(Color.parseColor("#e6e6e6"));
                    break;
            }

        } else {
            holder.tvContent.setTextColor(Color.parseColor("#5e5e5e"));
            switch (type){
                case 1:
                    holder.llContent.setBackgroundColor(Color.parseColor("#ffffff"));
                    break;
                case 2:
                    holder.llContent.setBackgroundColor(Color.parseColor("#f8f8f8"));
                    break;
                case 3:
                    holder.llContent.setBackgroundColor(Color.parseColor("#f0f0f0"));
                    break;
            }

        }


        return convertView;
    }

    private class ViewHolder {
        TextView tvContent;
        LinearLayout llContent;
    }
}
