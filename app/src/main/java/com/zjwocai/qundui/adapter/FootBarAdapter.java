package com.zjwocai.qundui.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import com.zjwocai.qundui.R;


/**
 * @author
 * @created on
 * @version $Id$
 */
public class FootBarAdapter extends SimpleAdapter {
    private View v;
    private ArrayList<View> av = new ArrayList<View>();

    public FootBarAdapter(Context context, List<? extends Map<String, ?>> data,
                          int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }

//    public void SetFocus(int index) {
//        for (int i = 0; i < 5; i++) {
//            if (i != index) {
//                av.get(i).setBackgroundResource(0);// 恢复未选中的样式
//            }
//        }
//        av.get(index).setBackgroundResource(R.drawable.headlogo);// 设置选中的样式
//        x=0;
//    }

    private int selectedPosition = 0;// 选中的位置
    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }


    int x = 0;

    public View getView(int position, View convertView, ViewGroup parent) {
        v = super.getView(position, convertView, parent);
        x++;
        if (position == selectedPosition) {
            //v.setBackgroundResource(R.drawable.headlogo);
            v.setSelected(true);
        }
        else{
            v.setBackgroundResource(0);
        }
//        System.out.println("_---" + x);
//        System.out.println("_+++" + position);
//        if (x >= 2 && x <= 6) {
//            av.add(v);
//        }
//        System.out.println("!!!" + av.size());


        return v;
    }
}