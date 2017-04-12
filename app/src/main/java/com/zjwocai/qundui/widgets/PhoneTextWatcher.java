package com.zjwocai.qundui.widgets;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by rxy on 15/7/8. E－mail:rxywxsy@163.com
 */
public class PhoneTextWatcher implements TextWatcher {

	private EditText _text;
	int beforeLen = 0;
	int afterLen = 0;

	public PhoneTextWatcher(EditText _text) {
		this._text = _text;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		beforeLen = s.length();
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		String txt = _text.getText().toString();
		afterLen = txt.length();
		if (s == null || s.length() == 0)
			return;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			if (i != 3 && i != 8 && s.charAt(i) == ' ') {
				continue;
			} else {
				sb.append(s.charAt(i));
				if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
					sb.insert(sb.length() - 1, ' ');
				}
			}
		}
		if (!sb.toString().equals(s.toString())) {
			int index = start + 1;
			if (sb.charAt(start) == ' ') {
				if (before == 0) {
					index++;
				} else {
					index--;
				}
			} else {
				if (before == 1) {
					index--;
				}
			}
			_text.setText(sb.toString());
			_text.setSelection(index);
		}

		if (afterLen < beforeLen) {
			if (txt.endsWith(" ")) {
				_text.setText(new StringBuffer(txt).deleteCharAt(txt.lastIndexOf(" ")).toString());
				_text.setSelection(_text.getText().length());
			}
		}

	}

	@Override
	public void afterTextChanged(Editable s) {

	}
}