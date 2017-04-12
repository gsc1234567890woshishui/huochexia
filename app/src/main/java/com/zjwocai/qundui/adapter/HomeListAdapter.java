package com.zjwocai.qundui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.threeti.teamlibrary.activity.BaseActivity;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.model.HomeOrderModel;
import com.zjwocai.qundui.util.MyLogUtil;

import java.util.List;

/**
 * 主页列表适配器
 * Created by NieLiQin on 2016/5/31.
 */
public class HomeListAdapter extends BaseListAdapter {
    public HomeListAdapter(Context context, List list) {
        super(context, list, -1,0);

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.item_home_sdlv, null);
            holder = new ViewHolder();
            holder.ivState = (ImageView) convertView.findViewById(R.id.iv_state);
            //holder.ivChoice = (ImageView) convertView.findViewById(R.id.iv_choice);
            holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            //holder.tvNum = (TextView) convertView.findViewById(R.id.tv_number);
            holder.tvProvider = (TextView) convertView.findViewById(R.id.tv_provider);
            holder.tvReceive = (TextView) convertView.findViewById(R.id.tv_receiver);
            holder.tvRefuse = (TextView) convertView.findViewById(R.id.tv_refuse);
            holder.tvAccept = (TextView) convertView.findViewById(R.id.tv_accept);
            holder.tvConfirmSend = (TextView) convertView.findViewById(R.id.tv_confirm_send);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            //holder.tvCount = (TextView) convertView.findViewById(R.id.tv_count);
            holder.tvVolume = (TextView) convertView.findViewById(R.id.tv_volume);
            holder.tvWeight = (TextView) convertView.findViewById(R.id.tv_weight);
            holder.llBtns = (LinearLayout) convertView.findViewById(R.id.ll_btns);
            holder.tvDistance = (TextView) convertView.findViewById(R.id.tv_distance);//总距离
            holder.tvStart = (TextView) convertView.findViewById(R.id.tv_start);//发货城市
            holder.tvStart2 = (TextView) convertView.findViewById(R.id.tv_start2);//发货区
            holder.tvEnd = (TextView) convertView.findViewById(R.id.tv_end);//收货城市
            holder.tvEnd2 = (TextView) convertView.findViewById(R.id.tv_end2);//收货区
            holder.freight = (TextView) convertView.findViewById(R.id.tv_cost);//运费
            holder.ilNavi = (ImageView) convertView.findViewById(R.id.rl_navigation);//导航按钮

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HomeOrderModel model = (HomeOrderModel) mList.get(position);
        if (model.getStatus().equals("1")){
            holder.ivState.setImageResource(R.drawable.sign_unaccept);
        } else if (model.getStatus().equals("2")){
            holder.ivState.setImageResource(R.drawable.sign_accept);
            //正在运输中就开启service2


        } else if (model.getStatus().equals("3")){
            holder.ivState.setImageResource(R.drawable.sign_accept);
        }

        if (model.getReleasdate() != null && !model.getReleasdate().equals("")){
            holder.tvDate.setText(model.getReleasdate().substring(0,10));
        }else {
            holder.tvDate.setText("未知");
        }

//        if (model.getIssf().equals("0")){
//            holder.ivChoice.setImageResource(R.drawable.sign_free);
//        } else if (model.getIssf().equals("1") && model.getStatus().equals("1")){
//            holder.ivChoice.setImageResource(R.drawable.sign_free);
//        } else if (model.getIssf().equals("1") && (model.getStatus().equals("2") || model.getStatus().equals("3"))){
//            holder.ivChoice.setImageResource(R.drawable.sign_pay);
//        }

        holder.llBtns.setVisibility(View.INVISIBLE);
        holder.tvConfirmSend.setVisibility(View.INVISIBLE);
        if (model.getStatus().equals("1")){
            holder.llBtns.setVisibility(View.VISIBLE);
            holder.tvAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCustomerListener(v,position);
                }
            });
            holder.tvRefuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCustomerListener(v,position);
                }
            });
        } else if (model.getStatus().equals("2")){
            holder.tvConfirmSend.setVisibility(View.VISIBLE);
            holder.tvConfirmSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCustomerListener(v,position);
                }
            });
        }
        holder.ilNavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCustomerListener(v,position);
            }
        });
        //holder.tvNum.setText(model.getCode());
        holder.tvProvider.setText(model.getShipper());
        holder.tvReceive.setText(model.getReceiver());
        if(model.getFhcityname().isEmpty()){
            holder.tvStart.setText(model.getFhcityname()); //发货城市
        }else{
            holder.tvStart.setText(model.getFhcityname().substring(0,2));

        }
        if(model.getShcityname().isEmpty()){
            holder.tvEnd.setText(model.getShcityname());  //收货城市
        }else {
            holder.tvEnd.setText(model.getShcityname().substring(0,2));

        }

        //发货区
        if(model.getFhareaname().isEmpty()){
            holder.tvStart2.setText(model.getFhareaname());
        }else {
            holder.tvStart2.setText(model.getFhareaname().substring(0, 2));
        }
        //收货区
        if(model.getShareaname().isEmpty()){
            holder.tvEnd2.setText(model.getShareaname());
        }else{
            holder.tvEnd2.setText(model.getShareaname().substring(0,2));
        }
        //总里程
        if(model.getDistance().isEmpty()){
            holder.tvDistance.setText("未知");
        }else{
            holder.tvDistance.setText("约"+model.getDistance().substring(0,model.getDistance().indexOf("."))+"公里");

        }
        holder.freight.setText(model.getFreight()+"元");//运费
        holder.tvName.setText(model.getGoodsinfo());
        //holder.tvCount.setText(model.getPacknumber());
        holder.tvVolume.setText(model.getVolume() +"m³");
        holder.tvWeight.setText(model.getWeight() +"吨");
        return convertView;

//        //holder.tvStart.setText(model.getFhcityname()); //发货城市
//        //str.substring(0,str.length()-1);
//        holder.tvStart.setText(model.getFhcityname().substring(0,model.getFhcityname().length()-1)); //发货城市
//        MyLogUtil.e("opopopopopo",model.getFhcityname());
//        //holder.tvEnd.setText(model.getShcityname());  //收货城市
//        holder.tvEnd.setText(model.getShcityname().substring(0,model.getShcityname().length()-1)); //发货城市


//       // holder.tvStart2.setText(model.getFhareaname());//发货区
//        holder.tvStart2.setText(model.getFhareaname().substring(0,model.getFhareaname().length()-1)); //发货城市
//
//        //holder.tvEnd2.setText(model.getShareaname());//收货区
//        holder.tvEnd2.setText(model.getShareaname().substring(0,model.getShareaname().length()-1)); //发货城市

        //holder.tvDistance.setText("约"+model.getDistance().substring(0,model.getDistance().indexOf("."))+"公里");//总里程
//        holder.tvDistance.setText("约"+model.getDistance()+"公里");


    }

    private class ViewHolder {
        ImageView ivState,ivChoice,ilNavi;//导航按钮
        TextView tvDate,tvNum,tvProvider,tvReceive,tvRefuse,tvAccept,tvConfirmSend,tvName,tvVolume,tvWeight,tvStart
                ,tvStart2,tvEnd,tvEnd2,tvDistance,freight;
        LinearLayout llBtns;
    }
}
