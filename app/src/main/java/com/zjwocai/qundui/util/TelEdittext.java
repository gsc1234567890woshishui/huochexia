package com.zjwocai.qundui.util;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;


/**
 * Created by qundui on 2017/2/10.
 */

public class TelEdittext extends EditText {
    public boolean isTel;
    private String addString = " ";
    private boolean isRun = false;

    public TelEdittext(Context context) {
        this(context, null);
    }

    public TelEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("tag", "onTextChanged()之前");
                if (isRun) {//这几句要加，不然每输入一个值都会执行两次onTextChanged()，导致堆栈溢出，原因不明
                    isRun = false;
                    return;
                }
                isRun = true;
                Log.i("tag", "onTextChanged()");
                if (isTel) {
                    String finalString = "";
                    int index = 0;
                    String telString = s.toString().replace(" ", "");
                    if ((index + 3) < telString.length()) {
                        finalString += (telString.substring(index, index + 3) + addString);
                        index += 3;
                    }
                    while ((index + 4) < telString.length()) {
                        finalString += (telString.substring(index, index + 4) + addString);
                        index += 4;
                    }
                    finalString += telString.substring(index, telString.length());
                    TelEdittext.this.setText(finalString);
                    //此语句不可少，否则输入的光标会出现在最左边，不会随输入的值往右移动
                    TelEdittext.this.setSelection(finalString.length());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
