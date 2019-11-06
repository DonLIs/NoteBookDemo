package me.donlis.notebook;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * 自定义文字输入监听，继承TextWatcher
 */
public abstract class DxTextWatcher implements TextWatcher {

    abstract void onTextChanged(String s);

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        onTextChanged(s.toString());
    }
}
