package com.zjwocai.qundui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.threeti.teamlibrary.model.GridviewModel;
import com.zjwocai.qundui.R;

import java.util.List;

/**
 * Created by qundui on 2017/3/14.
 */

public class GridAdapter extends BaseAdapter {

    private List<GridviewModel> provinceBeanList;
    private LayoutInflater layoutInflater;
    private GridviewModel gridviewModel;
    private GridView gridView;
    private  Context context;
    private String s ="" ;
    private ViewHolder holder = null;

    public GridAdapter(Context context, List<GridviewModel> provinceBeanList,GridView gridView) {
        this.provinceBeanList = provinceBeanList;
        this.gridView = gridView;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }
     public void setFocus(int index){
         for (int i = 0; i <provinceBeanList.size() ; i++) {
             if(i!=index){//未选中的样式
                 holder.text.setBackgroundResource(R.drawable.tv_bg11);
                 holder.text.setTextColor(Color.rgb(92, 172, 238));
             }else{//选中的样式
                 holder.text.setBackgroundResource(R.drawable.tv_bg00);
                 holder.text.setTextColor(Color.rgb(255, 251, 240));
             }


         }

}
    @Override
    public int getCount() {
        return provinceBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return provinceBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item, null);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


            final  GridviewModel provinceBean = provinceBeanList.get(position);

           if(provinceBean.isSelect()){//选中第一个
               holder.text.setBackgroundResource(R.drawable.tv_bg00);
               holder.text.setTextColor(Color.rgb(255, 251, 240));

           }else{
               holder.text.setBackgroundResource(R.drawable.tv_bg11);
               holder.text.setTextColor(Color.rgb(92, 172, 238));

           }
            if (provinceBean != null) {
                holder.text.setText(provinceBean.getMoney());
            }



        return convertView;
    }

    class ViewHolder {
        TextView text;
    }

}