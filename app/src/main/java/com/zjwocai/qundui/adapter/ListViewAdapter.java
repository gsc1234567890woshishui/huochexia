package com.zjwocai.qundui.adapter;

/**
 * Created by qundui on 2017/3/21.
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjwocai.qundui.R;

public class ListViewAdapter extends BaseAdapter {

    private static Map<Integer,View> m=new HashMap<Integer,View>();

    private List<String> items;
    private LayoutInflater inflater;

    public ListViewAdapter(List<String> items, Context context) {
        super();
        this.items = items;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    @Override
    public View getView(int position, View contentView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        contentView=m.get(position);
        if(contentView==null){
            contentView=inflater.inflate(R.layout.listviewitem, null);
            TextView tvType=(TextView) contentView.findViewById(R.id.tv_type);
            TextView tvCount=(TextView) contentView.findViewById(R.id.tv_count);
            TextView tvTime=(TextView) contentView.findViewById(R.id.tv_time);
            TextView tvState=(TextView) contentView.findViewById(R.id.tv_state);

            tvType.setText(items.get(position));
            tvCount.setText(items.get(position));
            tvTime.setText(items.get(position));
            tvState.setText(items.get(position));


        }
        m.put(position, contentView);
        return contentView;
    }

    public void addItem(String item) {
        items.add(item);
    }


}