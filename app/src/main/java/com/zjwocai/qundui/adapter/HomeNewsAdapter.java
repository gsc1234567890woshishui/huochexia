package com.zjwocai.qundui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.threeti.teamlibrary.activity.BaseActivity;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.model.NewsModel;

import java.util.List;

/**
 * 消息适配器
 * Created by NieLiQin on 2016/5/31.
 */
public class HomeNewsAdapter extends BaseListAdapter {
    public HomeNewsAdapter(BaseActivity context, List list) {
        super(context, list, -1,0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.item_home_news, null);
            holder = new ViewHolder();
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        NewsModel model = (NewsModel) mList.get(position);
        if (model.getCsdate() != null && !model.getCsdate().equals("") && model.getCsdate().length() >= 16){
            holder.tvTime.setText(model.getCsdate().substring(11,16));
        } else {
            holder.tvTime.setText("未知");
        }

        holder.tvTitle.setText(model.getTitle());
        holder.tvContent.setText(model.getContent());

        return convertView;
    }

    private class ViewHolder {
        TextView tvTime,tvTitle,tvContent;
    }
}
