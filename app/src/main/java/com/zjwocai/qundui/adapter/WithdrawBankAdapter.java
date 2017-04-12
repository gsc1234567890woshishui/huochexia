package com.zjwocai.qundui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.threeti.teamlibrary.activity.BaseActivity;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.model.CardModel;
import com.zjwocai.qundui.model.HomeOrderModel;
import com.zjwocai.qundui.model.TypeModel;

import java.util.List;

/**
 * 提现银行列表适配器
 * Created by NieLiQin on 2016/5/31.
 */
public class WithdrawBankAdapter extends BaseListAdapter {
    public WithdrawBankAdapter(BaseActivity context, List list) {
        super(context, list, -1,0);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.item_lv_banks, null);
            holder = new ViewHolder();
            holder.ll = (LinearLayout) convertView.findViewById(R.id.ll_ccb);
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CardModel model = (CardModel) mList.get(position);
        holder.ll.setSelected(false);
        if (model.isSelected()){
            holder.ll.setSelected(true);
        }

        holder.ivIcon.setImageResource(TypeModel.getCarDIcon(model.getType()));
        holder.tvName.setText(TypeModel.getCarDType(model.getType()));
        holder.tvNumber.setText(model.getCode().substring(model.getCode().length() - 4,model.getCode().length()));

        return convertView;
    }

    private class ViewHolder {
        ImageView ivIcon;
        TextView tvName,tvNumber;
        LinearLayout ll;
    }
}
