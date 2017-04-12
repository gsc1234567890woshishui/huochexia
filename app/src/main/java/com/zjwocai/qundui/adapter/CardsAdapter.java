package com.zjwocai.qundui.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.threeti.teamlibrary.activity.BaseActivity;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.model.CardModel;
import com.zjwocai.qundui.model.TypeModel;
import com.zjwocai.qundui.widgets.CircleDisplayer;

import java.util.List;

/**
 * 银行卡列表适配器
 * Created by NieLiQin on 2016/5/31.
 */
public class CardsAdapter extends BaseListAdapter {
    public CardsAdapter(BaseActivity context, List list) {
        super(context, list, -1,0);
        options =  new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(false).displayer(new CircleDisplayer(0))
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.item_cards, null);
            holder = new ViewHolder();
            holder.tvManager = (TextView) convertView.findViewById(R.id.tv_manager);
            holder.tvBankName = (TextView) convertView.findViewById(R.id.tv_bank_name);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvCardNumber = (TextView) convertView.findViewById(R.id.tv_card_number);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CardModel model = (CardModel) mList.get(position);
        holder.tvBankName.setText(TypeModel.getCarDType(model.getType()));
        holder.tvName.setText(model.getOwer());
        String s1 = model.getCode().substring(model.getCode().length() - 4,model.getCode().length()-3);
        String s2 = model.getCode().substring(model.getCode().length() - 3,model.getCode().length()-2);
        String s3 = model.getCode().substring(model.getCode().length() - 2,model.getCode().length()-1);
        String s4 = model.getCode().substring(model.getCode().length() - 1,model.getCode().length());
        //holder.tvCardNumber.setText(model.getCode().substring(model.getCode().length() - 4,model.getCode().length()));
        holder.tvCardNumber.setText(s1+" "+s2+" "+s3+" "+s4);

        holder.tvManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCustomerListener(v,position);
            }
        });
        imageLoader.displayImage("drawable://" + TypeModel.getCarDIcon(model.getType()),holder.iv,options);

        return convertView;
    }

    private class ViewHolder {
        ImageView iv;
        TextView tvManager,tvBankName,tvName,tvCardNumber;
    }
}
