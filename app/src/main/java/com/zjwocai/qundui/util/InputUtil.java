package com.zjwocai.qundui.util;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.threeti.teamlibrary.activity.BaseActivity;


/**
 * 编辑软件盘控件
 * Created by WangXY on 2015/10/22.20:49.
 */
public class InputUtil {

    /**
     * 打开输入法键盘
     *
     * @param editText 输入框控件
     */
    public static void showInputMethodView(Context context, EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != imm) {
            imm.showSoftInput(editText, 0);
        }
    }

    public static void showInputMethodView(Context context,
                                           final EditText editText, long millseconds) {
        editText.requestFocus();
        final InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != imm) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    imm.showSoftInput(editText, 0);
                }
            }, millseconds);
        }
    }

    /**
     * 关闭输入法键盘
     *
     * @param editText 输入框控件
     */
    public static void hideInputMethdView(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != imm) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    public static String getInputString(EditText editText) {
        if (editText == null) {
            return null;
        } else {
            return editText.getText().toString();
        }

    }

    public static void HideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    /**
     * UI设计时没有搜索按键时调用输入法软键盘的搜索功能
     *
     * @param context
     * @param editText
     * @param tips     //输入为空时的提示语句
     * @param listener //点击监听事件
     */
    public static void searchClick(final Context context, final EditText editText, final String tips,
                                   final SearchActionListener listener) {
        editText.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                            InputUtil.hideInputMethdView(context, editText);
                            String key = editText.getText().toString().trim();
                            if (TextUtils.isEmpty(key)) {
                                ((BaseActivity) context).showToast(tips);
                            } else {
                                listener.searchAction();
                            }
                        }
                        return false;
                    }
                }
        );
    }


    public interface SearchActionListener {
        public void searchAction();
    }
}
