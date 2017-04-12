package com.zjwocai.qundui.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * 不可滚动的Gridview
 * Created by XieZuoping on 2015/12/9.
 */
public class ScrollDisabledGridView extends GridView {

    public ScrollDisabledGridView(Context context) {
        super(context);
    }

    public ScrollDisabledGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollDisabledGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            return true;// 禁止GridView进行滑动
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
