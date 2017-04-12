package com.zjwocai.qundui.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.threeti.teamlibrary.activity.BaseActivity;
import com.zjwocai.qundui.R;
import com.zjwocai.qundui.widgets.sortlistview.PinyinSortModel;

import java.util.List;

/**
 * 搜索城市适配器
 * Created by NieLiQin on 2016/5/31.
 */
public class SearchCityListAdapter extends BaseListAdapter implements SectionIndexer {

    public SearchCityListAdapter(BaseActivity context, List list) {
        super(context, list, -1, 0);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.item_city_list, null);
            holder = new ViewHolder();
            holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            holder.llContent = (LinearLayout) convertView.findViewById(R.id.ll_content);
            holder.tvLetter = (TextView) convertView.findViewById(R.id.tv_letter);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PinyinSortModel model = (PinyinSortModel) mList.get(position);

        /*字母格子处理开始*/
        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            holder.tvLetter.setVisibility(View.VISIBLE);
            holder.tvLetter.setText(getAlpha(model.getPinyinSortLetter()));
        } else {
            holder.tvLetter.setVisibility(View.GONE);
        }
        /*字母格子处理结束*/
        String name = model.getModelName();
        if (!TextUtils.isEmpty(name)) {
            holder.tvContent.setText(name);
        }

        return convertView;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        if (mList != null && !mList.isEmpty()) {
            PinyinSortModel model = (PinyinSortModel) mList.get(position);
            return model.getPinyinSortLetter().charAt(0);
        }
        return -1;
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            PinyinSortModel model = (PinyinSortModel) mList.get(i);
            String sortStr = model.getPinyinSortLetter();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 提取英文的首字母，非英文字母用#代替。
     */
    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

    private class ViewHolder {
        TextView tvContent, tvLetter;
        LinearLayout llContent;
    }
}
