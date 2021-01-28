package com.example.borarachar;

import android.text.Editable;
import android.text.TextWatcher;

public class TextWatcherObserver implements TextWatcher {

    MainActivity mainActivity;
    public TextWatcherObserver(MainActivity newMainActivity) {
        this.mainActivity = newMainActivity;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mainActivity.calculate();
    }
}
