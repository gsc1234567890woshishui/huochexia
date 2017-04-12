package com.threeti.teamlibrary.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.threeti.teamlibrary.R;

/**
 * Created by BaoHang on 15/4/13.
 */
public class MyImageView extends ImageView {
    private int withWeight;
    private int highWeight;

    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyImageView);
        this.highWeight = typedArray.getInt(R.styleable.MyImageView_hightweight, 0);
        this.withWeight = typedArray.getInt(R.styleable.MyImageView_widthtweight, 0);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (withWeight > 0 && highWeight > 0) {
            this.getLayoutParams().height = w * highWeight / withWeight;
            this.setLayoutParams(this.getLayoutParams());
        }
    }
}
