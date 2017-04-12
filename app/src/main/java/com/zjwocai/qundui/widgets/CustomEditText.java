package com.zjwocai.qundui.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.zjwocai.qundui.R;


/**
 * Created by amituo on 2016/11/24.
 */

public class CustomEditText extends EditText {

    private Drawable mDeleteImage;// 删除的按钮


    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }



    public CustomEditText(Context context) {
        this(context, null);

    }


    private void init() {

        addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDeleteImage = TextUtils.isEmpty(s) ? null : getContext().getResources().getDrawable(R.drawable.delete2);
                setCompoundDrawablesWithIntrinsicBounds(null, null, mDeleteImage, null);//添加drawable ， position = right
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (mDeleteImage != null && !TextUtils.isEmpty(getText())) {//如果删除图片显示，并且输入框有内容
                    if(event.getX() > ( getWidth() - getTotalPaddingRight()) && event.getX() < (getWidth() - getPaddingRight()))
                        //只有在这区域能触发清除内容的效果
                        getText().clear();
                }
                break;

        }


        return super.onTouchEvent(event);
    }





}
