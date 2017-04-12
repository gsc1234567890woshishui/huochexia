package com.zjwocai.qundui.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjwocai.qundui.R;
import com.zjwocai.qundui.fragment.EventMessage;
import com.zjwocai.qundui.model.ItemModel2;
import com.zjwocai.qundui.model.ItemModel3;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * Created by WangChang on 2016/4/1.
 */
public class DemoAdapter extends RecyclerView.Adapter<DemoAdapter.BaseViewHolder> {
    private ArrayList<ItemModel2> dataList = new ArrayList<>();

    private int lastPressIndex = 0;
    private boolean flag;
    public DemoAdapter(ArrayList<ItemModel2> dataList){
        this.dataList = dataList;

    }

    public void replaceAll(ArrayList<ItemModel2> list,boolean flag) {
        this.flag = flag;
        lastPressIndex = 0;
        dataList.clear();
        if (list != null && list.size() > 0) {
            dataList.addAll(list);
        }
        notifyDataSetChanged();
        //让第一个选中
    }

    public interface OnClickListener {
        void setOnClick(View view, int position);
    }
    OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public DemoAdapter.BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ItemModel2.ONE:
                return new OneViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.one, parent, false));
//            case ItemModel2.TWO:
//                return new TWoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.two, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(DemoAdapter.BaseViewHolder holder, int position) {
        //如果充值的是话费
        if(dataList.get(position).data.equals("0")){//充值的是话费
            holder.setData("0",dataList.get(position).data2,dataList.get(position).data3);
        }else {//充值的是流量
            //判断全国流量是不是空
            if (dataList.get(position).data3.equals("售价" + "" + "元")) {//如果省内售价是空就显示全国流量
                if(dataList.get(position).data.equals("2")){//如果单位是G
                    holder.setData("2", dataList.get(position).data2, dataList.get(position).data4);//单位是G
                }else if(dataList.get(position).data.equals("1")){//如果单位是M
                    holder.setData("1", dataList.get(position).data2, dataList.get(position).data4);//单位是M
                }
            } else {//如果是省内流量不是空
                if(dataList.get(position).data.equals("2")){//如果单位是MG
                    holder.setData("2", dataList.get(position).data2, dataList.get(position).data3);//单位是G
                }else if(dataList.get(position).data.equals("1")){//如果单位是M
                    holder.setData("1", dataList.get(position).data2, dataList.get(position).data3);//单位是M
                }
            }

        }

    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).type;
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }


    public class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        void setData(Object data,Object data2,Object data3) {
        }
    }

    private class OneViewHolder extends BaseViewHolder {
        private TextView tv;
        private TextView tv2;
        private LinearLayout llTv;

        public OneViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv);
            tv2 = (TextView) view.findViewById(R.id.tv2);
            llTv = (LinearLayout) view.findViewById(R.id.ll_tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.setOnClick(v, getAdapterPosition());
                    int position = getAdapterPosition();//得到当前点击的条目
                    ItemModel2 item = dataList.get(position);
                    //onClickListener.setOnClick(v, getAdapterPosition());

                    Log.e("TAG", "OneViewHolder: ");

                    if (lastPressIndex == position) {
                       // lastPressIndex = -1;
                    } else {
                        lastPressIndex = position;
                    }
                    notifyDataSetChanged();
                }

            });
        }

        @Override
        void setData(Object data,Object data2,Object data3) {
            if (data != null) {
                //设置数据的时候
                String text = (String) data;
                String text2 = (String) data2;
                String text3 = (String) data3;
                if(text.equals("0")){//如果是充值话费
                    tv.setText(text2.substring(0,text2.indexOf("."))+"元");//面值
                    tv2.setText("售价"+text3+"元");
                }else if(text.equals("1")){//如果充值的流量单位是M
                    tv.setText(text2+"M");
                    tv2.setText(text3);
                }else if(text.equals("2")){//如果充值流量的单位是G 就除以1024M
                    tv.setText(Integer.parseInt(text2)/1024+"G");
                    tv2.setText(text3);
                }

                if(flag == false){//如果手机号不完整就变成灰色
                    llTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            llTv.setClickable(false);
                        }
                    });

                    tv.setTextColor(itemView.getContext().getResources().getColor(R.color.greyc));
                    //隐藏售价
                    tv2.setVisibility(View.GONE);
                    //tv2.setTextColor(itemView.getContext().getResources().getColor(R.color.line));
                    llTv.setBackgroundResource(R.drawable.tv_bg2);
                }else{
                    llTv.setClickable(false);
                    llTv.setBackgroundResource(R.drawable.tv_bg);

                    if (getAdapterPosition() == lastPressIndex) {
                        llTv.setSelected(true);
                        tv.setTextColor(itemView.getContext().getResources().getColor(R.color.white));
                        tv2.setVisibility(View.VISIBLE);//显示售价
                        tv2.setTextColor(itemView.getContext().getResources().getColor(R.color.white));
                    } else {
                        llTv.setSelected(false);
                        tv.setTextColor(itemView.getContext().getResources().getColor(R.color.t12b7f5));
                        //显示售价
                        tv2.setVisibility(View.VISIBLE);
                        tv2.setTextColor(itemView.getContext().getResources().getColor(R.color.t12b7f5));
                    }
                }

            }
        }

    }

//    private class TWoViewHolder extends BaseViewHolder {
//        private EditText et,et2;
//
//        public TWoViewHolder(View view) {
//            super(view);
//            et = (EditText) view.findViewById(R.id.et);
//        }
//
//        @Override
//        void setData(Object data) {
//            super.setData(data);
//        }
//    }
}
